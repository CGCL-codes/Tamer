package net.sf.jode.obfuscator.modules;

import net.sf.jode.bytecode.Opcodes;
import net.sf.jode.bytecode.ClassPath;
import net.sf.jode.bytecode.ClassInfo;
import net.sf.jode.bytecode.BasicBlocks;
import net.sf.jode.bytecode.Block;
import net.sf.jode.bytecode.Handler;
import net.sf.jode.bytecode.Instruction;
import net.sf.jode.bytecode.Reference;
import net.sf.jode.bytecode.TypeSignature;
import net.sf.jode.obfuscator.Identifier;
import net.sf.jode.obfuscator.*;
import net.sf.jode.GlobalOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class SimpleAnalyzer implements CodeAnalyzer, Opcodes {

    private ClassInfo canonizeIfaceRef(ClassInfo clazz, Reference ref) {
        while (clazz != null) {
            if (clazz.findMethod(ref.getName(), ref.getType()) != null) return clazz;
            ClassInfo[] ifaces = clazz.getInterfaces();
            for (int i = 0; i < ifaces.length; i++) {
                ClassInfo realClass = canonizeIfaceRef(ifaces[i], ref);
                if (realClass != null) return realClass;
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    protected Identifier canonizeReference(Instruction instr) {
        Reference ref = instr.getReference();
        Identifier ident = Main.getClassBundle().getIdentifier(ref);
        ClassPath classPath = Main.getClassBundle().getClassPath();
        String clName = ref.getClazz();
        String realClazzName;
        if (ident != null) {
            ClassIdentifier clazz = (ClassIdentifier) ident.getParent();
            realClazzName = "L" + (clazz.getFullName().replace('.', '/')) + ";";
        } else {
            ClassInfo clazz;
            if (clName.charAt(0) == '[') {
                clazz = classPath.getClassInfo("java.lang.Object");
            } else {
                clazz = classPath.getClassInfo(clName.substring(1, clName.length() - 1).replace('/', '.'));
            }
            try {
                clazz.load(clazz.DECLARATIONS);
            } catch (IOException ex) {
                throw new RuntimeException("Can't get declarations of " + clazz);
            }
            if (instr.getOpcode() == opc_invokeinterface) {
                clazz = canonizeIfaceRef(clazz, ref);
            } else if (instr.getOpcode() >= opc_invokevirtual) {
                while (clazz != null && clazz.findMethod(ref.getName(), ref.getType()) == null) clazz = clazz.getSuperclass();
            } else {
                while (clazz != null && clazz.findField(ref.getName(), ref.getType()) == null) clazz = clazz.getSuperclass();
            }
            if (clazz == null) {
                GlobalOptions.err.println("WARNING: Can't find reference: " + ref);
                realClazzName = clName;
            } else realClazzName = "L" + clazz.getName().replace('.', '/') + ";";
        }
        if (!realClazzName.equals(ref.getClazz())) {
            ref = Reference.getReference(realClazzName, ref.getName(), ref.getType());
            instr.setReference(ref);
        }
        return ident;
    }

    /**
     * Reads the opcodes out of the code info and determine its 
     * references
     * @return an enumeration of the references.
     */
    public void analyzeCode(MethodIdentifier m, BasicBlocks bb) {
        Block[] blocks = bb.getBlocks();
        for (int i = 0; i < blocks.length; i++) {
            Instruction[] instrs = blocks[i].getInstructions();
            for (int idx = 0; idx < instrs.length; idx++) {
                int opcode = instrs[idx].getOpcode();
                switch(opcode) {
                    case opc_checkcast:
                    case opc_instanceof:
                    case opc_multianewarray:
                        {
                            String clName = instrs[idx].getClazzType();
                            int k = 0;
                            while (k < clName.length() && clName.charAt(k) == '[') k++;
                            if (k < clName.length() && clName.charAt(k) == 'L') {
                                clName = clName.substring(k + 1, clName.length() - 1).replace('/', '.');
                                Main.getClassBundle().reachableClass(clName);
                            }
                            break;
                        }
                    case opc_invokespecial:
                    case opc_invokestatic:
                    case opc_invokeinterface:
                    case opc_invokevirtual:
                    case opc_putstatic:
                    case opc_putfield:
                        m.setGlobalSideEffects();
                    case opc_getstatic:
                    case opc_getfield:
                        {
                            Identifier ident = canonizeReference(instrs[idx]);
                            if (ident != null) {
                                if (opcode == opc_putstatic || opcode == opc_putfield) {
                                    FieldIdentifier fi = (FieldIdentifier) ident;
                                    if (!fi.isNotConstant()) fi.setNotConstant();
                                } else if (opcode == opc_invokevirtual || opcode == opc_invokeinterface) {
                                    ((ClassIdentifier) ident.getParent()).reachableReference(instrs[idx].getReference(), true);
                                } else {
                                    ident.setReachable();
                                }
                            }
                            break;
                        }
                }
            }
        }
        Handler[] handlers = bb.getExceptionHandlers();
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i].getType() != null) Main.getClassBundle().reachableClass(handlers[i].getType());
        }
    }

    public void transformCode(BasicBlocks bb) {
        Block[] blocks = bb.getBlocks();
        for (int i = 0; i < blocks.length; i++) {
            Instruction[] instrs = blocks[i].getInstructions();
            ArrayList newCode = new ArrayList();
            Block[] newSuccs = blocks[i].getSuccs();
            for (int idx = 0; idx < instrs.length; idx++) {
                int opcode = instrs[idx].getOpcode();
                if (opcode == opc_putstatic || opcode == opc_putfield) {
                    Reference ref = instrs[idx].getReference();
                    FieldIdentifier fi = (FieldIdentifier) Main.getClassBundle().getIdentifier(ref);
                    if (fi != null && (Main.stripping & Main.STRIP_UNREACH) != 0 && !fi.isReachable()) {
                        int stacksize = (opcode == Instruction.opc_putstatic) ? 0 : 1;
                        stacksize += TypeSignature.getTypeSize(ref.getType());
                        switch(stacksize) {
                            case 1:
                                newCode.add(Instruction.forOpcode(Instruction.opc_pop));
                                continue;
                            case 2:
                                newCode.add(Instruction.forOpcode(Instruction.opc_pop2));
                                continue;
                            case 3:
                                newCode.add(Instruction.forOpcode(Instruction.opc_pop2));
                                newCode.add(Instruction.forOpcode(Instruction.opc_pop));
                                continue;
                        }
                    }
                }
                newCode.add(instrs[idx]);
            }
            blocks[i].setCode((Instruction[]) newCode.toArray(instrs), newSuccs);
        }
    }
}
