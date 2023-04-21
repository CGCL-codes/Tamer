package jode.obfuscator;

import jode.bytecode.Handler;
import jode.bytecode.Opcodes;
import jode.bytecode.ClassInfo;
import jode.bytecode.BytecodeInfo;
import jode.bytecode.Instruction;
import jode.bytecode.Reference;
import jode.GlobalOptions;
import jode.type.Type;

public class SimpleAnalyzer implements CodeAnalyzer, Opcodes {

    public Identifier canonizeReference(Instruction instr) {
        Reference ref = (Reference) instr.objData;
        Identifier ident = Main.getClassBundle().getIdentifier(ref);
        String clName = ref.getClazz();
        String realClazzName;
        if (ident != null) {
            ClassIdentifier clazz = (ClassIdentifier) ident.getParent();
            realClazzName = "L" + (clazz.getFullName().replace('.', '/')) + ";";
        } else {
            ClassInfo clazz;
            if (clName.charAt(0) == '[') {
                clazz = ClassInfo.javaLangObject;
            } else {
                clazz = ClassInfo.forName(clName.substring(1, clName.length() - 1).replace('/', '.'));
            }
            if (instr.opcode >= opc_invokevirtual) {
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
            instr.objData = ref;
        }
        return ident;
    }

    /**
     * Reads the opcodes out of the code info and determine its 
     * references
     * @return an enumeration of the references.
     */
    public void analyzeCode(MethodIdentifier m, BytecodeInfo bytecode) {
        for (Instruction instr = bytecode.getFirstInstr(); instr != null; instr = instr.nextByAddr) {
            switch(instr.opcode) {
                case opc_checkcast:
                case opc_instanceof:
                case opc_multianewarray:
                    {
                        String clName = (String) instr.objData;
                        int i = 0;
                        while (i < clName.length() && clName.charAt(i) == '[') i++;
                        if (i < clName.length() && clName.charAt(i) == 'L') {
                            clName = clName.substring(i + 1, clName.length() - 1);
                            Main.getClassBundle().reachableIdentifier(clName, false);
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
                        Identifier ident = canonizeReference(instr);
                        if (ident != null) {
                            if (instr.opcode == opc_putstatic || instr.opcode == opc_putfield) {
                                FieldIdentifier fi = (FieldIdentifier) ident;
                                if (fi != null && !fi.isNotConstant()) fi.setNotConstant();
                            } else if (instr.opcode == opc_invokevirtual || instr.opcode == opc_invokeinterface) {
                                ((ClassIdentifier) ident.getParent()).reachableIdentifier(ident.getName(), ident.getType(), true);
                            } else {
                                ident.setReachable();
                            }
                        }
                        break;
                    }
            }
        }
        Handler[] handlers = bytecode.getExceptionHandlers();
        for (int i = 0; i < handlers.length; i++) {
            if (handlers[i].type != null) Main.getClassBundle().reachableIdentifier(handlers[i].type, false);
        }
    }

    public void transformCode(BytecodeInfo bytecode) {
        for (Instruction instr = bytecode.getFirstInstr(); instr != null; instr = instr.nextByAddr) {
            if (instr.opcode == opc_putstatic || instr.opcode == opc_putfield) {
                Reference ref = (Reference) instr.objData;
                FieldIdentifier fi = (FieldIdentifier) Main.getClassBundle().getIdentifier(ref);
                if (fi != null && (Main.stripping & Main.STRIP_UNREACH) != 0 && !fi.isReachable()) {
                    int stacksize = (instr.opcode == Instruction.opc_putstatic) ? 0 : 1;
                    stacksize += Type.tType(ref.getType()).stackSize();
                    if (stacksize == 3) {
                        Instruction second = instr.appendInstruction();
                        second.length = 1;
                        second.opcode = Instruction.opc_pop;
                        stacksize--;
                    }
                    instr.objData = null;
                    instr.intData = 0;
                    instr.opcode = Instruction.opc_pop - 1 + stacksize;
                    instr.length = 1;
                }
            }
        }
    }
}
