package net.sf.jauvm.vm.insn;

import java.util.Map;
import net.sf.jauvm.vm.Frame;
import net.sf.jauvm.vm.VirtualMachine;
import org.objectweb.asm.Label;
import static org.objectweb.asm.Opcodes.*;

public abstract class JumpInsn extends LabeledInsn {

    public static Insn getInsn(int opcode, Label label) {
        switch(opcode) {
            case IFEQ:
                return new IfEqInsn(label);
            case IFNE:
                return new IfNeInsn(label);
            case IFLT:
                return new IfLtInsn(label);
            case IFGE:
                return new IfGeInsn(label);
            case IFGT:
                return new IfGtInsn(label);
            case IFLE:
                return new IfLeInsn(label);
            case IF_ICMPEQ:
                return new IfICmpEqInsn(label);
            case IF_ICMPNE:
                return new IfICmpNeInsn(label);
            case IF_ICMPLT:
                return new IfICmpLtInsn(label);
            case IF_ICMPGE:
                return new IfICmpGeInsn(label);
            case IF_ICMPGT:
                return new IfICmpGtInsn(label);
            case IF_ICMPLE:
                return new IfICmpLeInsn(label);
            case IF_ACMPEQ:
                return new IfACmpEqInsn(label);
            case IF_ACMPNE:
                return new IfACmpNeInsn(label);
            case GOTO:
                return new GotoInsn(label);
            case JSR:
                return new JsrInsn(label);
            case IFNULL:
                return new IfNullInsn(label);
            case IFNONNULL:
                return new IfNonNullInsn(label);
            default:
                assert false;
                return null;
        }
    }

    Label label;

    int target;

    JumpInsn(Label label) {
        this.label = label;
    }

    public void resolve(Map<Label, Integer> map) {
        target = map.get(label);
        label = null;
    }

    static class IfEqInsn extends JumpInsn {

        IfEqInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popInt() == 0) vm.setCp(target);
        }
    }

    static class IfNeInsn extends JumpInsn {

        IfNeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popInt() != 0) vm.setCp(target);
        }
    }

    static class IfLtInsn extends JumpInsn {

        IfLtInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popInt() < 0) vm.setCp(target);
        }
    }

    static class IfGeInsn extends JumpInsn {

        IfGeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popInt() >= 0) vm.setCp(target);
        }
    }

    static class IfGtInsn extends JumpInsn {

        IfGtInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popInt() > 0) vm.setCp(target);
        }
    }

    static class IfLeInsn extends JumpInsn {

        IfLeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popInt() <= 0) vm.setCp(target);
        }
    }

    static class IfICmpEqInsn extends JumpInsn {

        IfICmpEqInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popInt() == frame.popInt()) vm.setCp(target);
        }
    }

    static class IfICmpNeInsn extends JumpInsn {

        IfICmpNeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popInt() != frame.popInt()) vm.setCp(target);
        }
    }

    static class IfICmpLtInsn extends JumpInsn {

        IfICmpLtInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popInt() > frame.popInt()) vm.setCp(target);
        }
    }

    static class IfICmpGeInsn extends JumpInsn {

        IfICmpGeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popInt() <= frame.popInt()) vm.setCp(target);
        }
    }

    static class IfICmpGtInsn extends JumpInsn {

        IfICmpGtInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popInt() < frame.popInt()) vm.setCp(target);
        }
    }

    static class IfICmpLeInsn extends JumpInsn {

        IfICmpLeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popInt() >= frame.popInt()) vm.setCp(target);
        }
    }

    static class IfACmpEqInsn extends JumpInsn {

        IfACmpEqInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popObject() == frame.popObject()) vm.setCp(target);
        }
    }

    static class IfACmpNeInsn extends JumpInsn {

        IfACmpNeInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            Frame frame = vm.getFrame();
            if (frame.popObject() != frame.popObject()) vm.setCp(target);
        }
    }

    static class GotoInsn extends JumpInsn {

        GotoInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            vm.setCp(target);
        }
    }

    static class JsrInsn extends JumpInsn {

        JsrInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            vm.getFrame().pushInt(vm.getCp());
            vm.setCp(target);
        }
    }

    static class IfNullInsn extends JumpInsn {

        IfNullInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popObject() == null) vm.setCp(target);
        }
    }

    static class IfNonNullInsn extends JumpInsn {

        IfNonNullInsn(Label label) {
            super(label);
        }

        public void execute(VirtualMachine vm) {
            if (vm.getFrame().popObject() != null) vm.setCp(target);
        }
    }
}
