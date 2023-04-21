package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.ContextManager;
import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.PathManager;
import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.SourceSource;
import com.bluemarsh.jswat.util.Strings;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.gjt.jclasslib.bytecode.AbstractInstruction;
import org.gjt.jclasslib.bytecode.BranchInstruction;
import org.gjt.jclasslib.bytecode.ImmediateByteInstruction;
import org.gjt.jclasslib.bytecode.ImmediateIntInstruction;
import org.gjt.jclasslib.bytecode.ImmediateShortInstruction;
import org.gjt.jclasslib.bytecode.IncrementInstruction;
import org.gjt.jclasslib.bytecode.InvokeInterfaceInstruction;
import org.gjt.jclasslib.bytecode.LookupSwitchInstruction;
import org.gjt.jclasslib.bytecode.MatchOffsetPair;
import org.gjt.jclasslib.bytecode.MultianewarrayInstruction;
import org.gjt.jclasslib.bytecode.Opcodes;
import org.gjt.jclasslib.bytecode.OpcodesUtil;
import org.gjt.jclasslib.bytecode.TableSwitchInstruction;
import org.gjt.jclasslib.io.ByteCodeReader;
import org.gjt.jclasslib.io.ClassFileReader;
import org.gjt.jclasslib.structures.AttributeInfo;
import org.gjt.jclasslib.structures.ClassFile;
import org.gjt.jclasslib.structures.InvalidByteCodeException;
import org.gjt.jclasslib.structures.MethodInfo;
import org.gjt.jclasslib.structures.attributes.CodeAttribute;

/**
 * Defines the class that handles the 'bytecodes' command.
 *
 * @author  Nathan Fiedler
 * @author  Ingo Kegel (portions)
 */
public class bytecodesCommand extends JSwatCommand {

    /** Array of spaces for padding numbers. */
    private static String[] spaces = new String[] { " ", "  ", "   ", "    " };

    /**
     * Look for the .class file that represents the class.
     *
     * @param  session  owning session.
     * @param  clazz    class to find.
     * @return  loaded ClassFile, if found.
     */
    private ClassFile findClass(Session session, ReferenceType clazz) {
        PathManager pathman = (PathManager) session.getManager(PathManager.class);
        SourceSource src = pathman.mapClass(clazz);
        ClassFile classFile = null;
        if (src == null) {
            return null;
        } else {
            InputStream is = src.getInputStream();
            try {
                classFile = ClassFileReader.readFromInputStream(is);
            } catch (InvalidByteCodeException ibce) {
                throw new CommandException(Bundle.getString("bytecodes.badcode"));
            } catch (IOException ioe) {
                classFile = null;
            }
        }
        return classFile;
    }

    /**
     * Perform the 'bytecodes' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        if (!session.isActive()) {
            throw new CommandException(Bundle.getString("noActiveSession"));
        }
        int tokenCount = args.countTokens();
        if (tokenCount == 0) {
            ContextManager conman = (ContextManager) session.getManager(ContextManager.class);
            Location loc = conman.getCurrentLocation();
            if (loc == null) {
                throw new CommandException(Bundle.getString("noCurrentLocation"));
            }
            ReferenceType clazz = loc.declaringType();
            Method method = loc.method();
            ClassFile classFile = findClass(session, clazz);
            if (classFile == null) {
                out.writeln(Bundle.getString("bytecodes.nofile"));
            }
            printBytecodes(session, clazz, method, out, classFile);
            return;
        } else if (tokenCount == 1) {
            throw new MissingArgumentsException();
        }
        String className = args.nextToken();
        String methodName = args.nextToken();
        VirtualMachine vm = session.getVM();
        vm.suspend();
        List classes = vm.allClasses();
        vm.resume();
        classes = new ArrayList(classes);
        Iterator iter = classes.iterator();
        ReferenceType matchingClass = null;
        while (iter.hasNext()) {
            ReferenceType clazz = (ReferenceType) iter.next();
            if (clazz.name().equals(className)) {
                matchingClass = clazz;
            }
        }
        if (matchingClass == null) {
            throw new CommandException(Bundle.getString("bytecodes.nomatch"));
        }
        List matchingMethods = matchingClass.methodsByName(methodName);
        if (matchingMethods.isEmpty()) {
            throw new CommandException(Bundle.getString("bytecodes.nomatch"));
        }
        ClassFile classFile = findClass(session, matchingClass);
        if (classFile == null) {
            out.writeln(Bundle.getString("bytecodes.nofile"));
        }
        iter = matchingMethods.iterator();
        while (iter.hasNext()) {
            Method method = (Method) iter.next();
            printBytecodes(session, matchingClass, method, out, classFile);
        }
    }

    /**
     * Prints the bytecodes of the given method in the class.
     *
     * @param  session    owning Session.
     * @param  clazz      class containing method.
     * @param  method     method from which to get bytecodes.
     * @param  out        log to write it all to.
     * @param  classFile  ClassFile, if found.
     */
    protected void printBytecodes(Session session, ReferenceType clazz, Method method, Log out, ClassFile classFile) {
        List opcodes = null;
        if (classFile != null) {
            String sign = method.signature();
            MethodInfo methodInfo = null;
            try {
                methodInfo = classFile.getMethod(method.name(), sign);
            } catch (InvalidByteCodeException ibce) {
                throw new CommandException(ibce);
            }
            if (methodInfo == null) {
                throw new CommandException(Bundle.getString("bytecodes.nomatch"));
            }
            AttributeInfo[] ainfo = methodInfo.getAttributes();
            int index = 0;
            while (index < ainfo.length) {
                AttributeInfo info = ainfo[index];
                if (info instanceof CodeAttribute) {
                    CodeAttribute code = (CodeAttribute) ainfo[0];
                    byte[] bytes = code.getCode();
                    try {
                        opcodes = ByteCodeReader.readByteCode(bytes);
                    } catch (IOException ioe) {
                        throw new CommandException(ioe);
                    }
                    break;
                }
            }
        }
        if (opcodes == null) {
            VirtualMachine vm = session.getVM();
            if (!vm.canGetBytecodes()) {
                throw new CommandException(Bundle.getString("bytecodes.nosupport"));
            }
            byte[] bytes = method.bytecodes();
            try {
                opcodes = ByteCodeReader.readByteCode(bytes);
            } catch (IOException ioe) {
                throw new CommandException(ioe);
            }
        }
        out.writeln(Bundle.getString("bytecodes.opsfor") + ' ' + clazz.name() + "." + method.name() + ':');
        Iterator biter = opcodes.iterator();
        while (biter.hasNext()) {
            AbstractInstruction opcode = (AbstractInstruction) biter.next();
            StringBuffer buf = new StringBuffer();
            int offset = opcode.getOffset();
            String dec = String.valueOf(offset);
            if (dec.length() < 5) {
                dec = spaces[4 - dec.length()].concat(dec);
            }
            buf.append(dec);
            buf.append(' ');
            buf.append(opcode.getOpcodeVerbose());
            addOpcodeSpecificInfo(buf, classFile, opcode);
            out.writeln(buf.toString());
        }
        out.writeln("");
    }

    /**
     * Adds the instruction-specific information to the buffer.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addOpcodeSpecificInfo(StringBuffer buf, ClassFile classFile, AbstractInstruction instruction) {
        if (instruction instanceof ImmediateByteInstruction) {
            addImmediateByteSpecificInfo(buf, classFile, (ImmediateByteInstruction) instruction);
        } else if (instruction instanceof ImmediateShortInstruction) {
            addImmediateShortSpecificInfo(buf, classFile, (ImmediateShortInstruction) instruction);
        } else if (instruction instanceof ImmediateIntInstruction) {
            addImmediateIntSpecificInfo(buf, classFile, (ImmediateIntInstruction) instruction);
        } else if (instruction instanceof BranchInstruction) {
            addBranchSpecificInfo(buf, classFile, (BranchInstruction) instruction);
        } else if (instruction instanceof TableSwitchInstruction) {
            addTableSwitchSpecificInfo(buf, classFile, (TableSwitchInstruction) instruction);
        } else if (instruction instanceof LookupSwitchInstruction) {
            addLookupSwitchSpecificInfo(buf, classFile, (LookupSwitchInstruction) instruction);
        }
    }

    /**
     * Adds the immediate byte specific information.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addImmediateByteSpecificInfo(StringBuffer buf, ClassFile classFile, ImmediateByteInstruction instruction) {
        int opcode = instruction.getOpcode();
        int sourceOffset = instruction.getOffset();
        int immediateByte = instruction.getImmediateByte();
        if (opcode == Opcodes.OPCODE_LDC) {
            addConstantPool(buf, classFile, immediateByte);
        } else if (opcode == Opcodes.OPCODE_NEWARRAY) {
            String verbose = OpcodesUtil.getArrayTypeVerbose(immediateByte);
            buf.append(' ');
            buf.append(immediateByte);
            buf.append(" (");
            buf.append(verbose);
            buf.append(')');
        } else {
            buf.append(' ');
            buf.append(immediateByte);
            if (instruction instanceof IncrementInstruction) {
                buf.append(" by");
                IncrementInstruction ii = (IncrementInstruction) instruction;
                buf.append(' ');
                buf.append(ii.getIncrementConst());
            }
        }
    }

    /**
     * Adds the immediate short specific information.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addImmediateShortSpecificInfo(StringBuffer buf, ClassFile classFile, ImmediateShortInstruction instruction) {
        int opcode = instruction.getOpcode();
        int sourceOffset = instruction.getOffset();
        int immediateShort = instruction.getImmediateShort();
        if (opcode == Opcodes.OPCODE_SIPUSH) {
            buf.append(' ');
            buf.append(immediateShort);
        } else {
            addConstantPool(buf, classFile, immediateShort);
            if (instruction instanceof InvokeInterfaceInstruction) {
                InvokeInterfaceInstruction iii = (InvokeInterfaceInstruction) instruction;
                buf.append(" count ");
                buf.append(iii.getCount());
            } else if (instruction instanceof MultianewarrayInstruction) {
                MultianewarrayInstruction mi = (MultianewarrayInstruction) instruction;
                buf.append(" dim ");
                buf.append(mi.getDimensions());
            }
        }
    }

    /**
     * Adds the immediate int specific information.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addImmediateIntSpecificInfo(StringBuffer buf, ClassFile classFile, ImmediateIntInstruction instruction) {
        int immediateInt = instruction.getImmediateInt();
        int sourceOffset = instruction.getOffset();
        addConstantPool(buf, classFile, immediateInt);
    }

    /**
     * Adds the branch specific information.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addBranchSpecificInfo(StringBuffer buf, ClassFile classFile, BranchInstruction instruction) {
        int branchOffset = instruction.getBranchOffset();
        int instructionOffset = instruction.getOffset();
        addOffset(buf, branchOffset, instructionOffset);
    }

    /**
     * Adds the table switch specific information.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addTableSwitchSpecificInfo(StringBuffer buf, ClassFile classFile, TableSwitchInstruction instruction) {
        int instructionOffset = instruction.getOffset();
        int lowByte = instruction.getLowByte();
        int highByte = instruction.getHighByte();
        int[] jumpOffsets = instruction.getJumpOffsets();
        buf.append(' ');
        buf.append(lowByte);
        buf.append(" to ");
        buf.append(highByte);
        buf.append('\n');
        for (int i = 0; i <= highByte - lowByte; i++) {
            buf.append("	");
            buf.append(i + lowByte);
            buf.append(": ");
            addOffset(buf, jumpOffsets[i], instructionOffset);
            buf.append('\n');
        }
        buf.append("	default: ");
        addOffset(buf, instruction.getDefaultOffset(), instructionOffset);
    }

    /**
     * Adds the lookup switch specific information.
     *
     * @param  buf          buffer to append to.
     * @param  classFile    ClassFile structure.
     * @param  instruction  instruction to evaluate.
     */
    protected void addLookupSwitchSpecificInfo(StringBuffer buf, ClassFile classFile, LookupSwitchInstruction instruction) {
        int instructionOffset = instruction.getOffset();
        List matchOffsetPairs = instruction.getMatchOffsetPairs();
        int matchOffsetPairsCount = matchOffsetPairs.size();
        buf.append(' ');
        buf.append(matchOffsetPairsCount);
        buf.append('\n');
        MatchOffsetPair matchOffsetPairEntry;
        for (int i = 0; i < matchOffsetPairsCount; i++) {
            matchOffsetPairEntry = (MatchOffsetPair) matchOffsetPairs.get(i);
            buf.append("	");
            ;
            buf.append(matchOffsetPairEntry.getMatch());
            buf.append(": ");
            addOffset(buf, matchOffsetPairEntry.getOffset(), instructionOffset);
            buf.append('\n');
        }
        buf.append("	default: ");
        addOffset(buf, instruction.getDefaultOffset(), instructionOffset);
    }

    /**
     * Adds the immediate byte specific information.
     *
     * @param  buf                buffer to append to.
     * @param  classFile          ClassFile structure.
     * @param  constantPoolIndex  index into constants pool.
     */
    protected void addConstantPool(StringBuffer buf, ClassFile classFile, int constantPoolIndex) {
        buf.append(' ');
        buf.append('#');
        buf.append(constantPoolIndex);
        if (classFile != null) {
            try {
                String name = classFile.getConstantPoolEntryName(constantPoolIndex);
                if (name != null && name.length() > 0) {
                    buf.append(" <");
                    name = Strings.cleanForPrinting(name, 0);
                    buf.append(name);
                    buf.append('>');
                }
            } catch (InvalidByteCodeException ibce) {
            }
        }
    }

    /**
     * Adds the offset information.
     *
     * @param  buf                buffer to append to.
     * @param  branchOffset       branch offset.
     * @param  instructionOffset  offset to instruction.
     *
     */
    protected void addOffset(StringBuffer buf, int branchOffset, int instructionOffset) {
        buf.append(' ');
        buf.append(String.valueOf(branchOffset + instructionOffset));
        buf.append(" (");
        if (branchOffset > 0) {
            buf.append('+');
        }
        buf.append(String.valueOf(branchOffset));
        buf.append(')');
    }
}
