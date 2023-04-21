package icebird.compiler.ncomp;

import java.io.IOException;
import java.io.OutputStream;
import icebird.compiler.ncomp.X86Register.FPU;
import icebird.utils.ObjectBuilder;

/**
 * @author Sergey Shulepoff[Knott]
 */
public final class X86TextAssembler extends X86Assembler implements X86Constants {

    private ObjectBuilder obj = new ObjectBuilder();

    private void nl() {
        obj.appendln();
    }

    private void write(String str) {
        if (isMethodArea()) {
            obj.append("\t");
        }
        obj.append(str);
        nl();
    }

    private static final String ccName(int jumpOpcode) {
        final String opc;
        switch(jumpOpcode) {
            case JA:
                opc = "a";
                break;
            case JAE:
                opc = "ae";
                break;
            case JB:
                opc = "b";
                break;
            case JBE:
                opc = "be";
                break;
            case JE:
                opc = "e";
                break;
            case JNE:
                opc = "ne";
                break;
            case JLE:
                opc = "le";
                break;
            case JL:
                opc = "l";
                break;
            case JGE:
                opc = "ge";
                break;
            case JG:
                opc = "g";
                break;
            default:
                throw new RuntimeException("Unknown jump opcode " + jumpOpcode);
        }
        return opc;
    }

    @Override
    public void fild(X86Register r, int srcDisp) {
        write("fild dword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fild64(X86Register r, int srcDisp) {
        write("fild qword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fistp(X86Register r, int srcDisp) {
        write("fistp dword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fistp64(X86Register r, int srcDisp) {
        write("fistp qword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fld(X86Register r, int srcDisp) {
        write("fld dword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fld64(X86Register r, int srcDisp) {
        write("fld qword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fstp(X86Register r, int srcDisp) {
        write("fstp dword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fstp64(X86Register r, int srcDisp) {
        write("fstp qword [" + r + "+" + srcDisp + "]");
    }

    @Override
    public void fxch(FPU fp) {
        write("fxch " + fp);
    }

    @Override
    public void lea(X86Register dst, X86Register src, int srcDisp) {
        write("lea " + dst + ",[" + src + "+" + srcDisp + "]");
    }

    @Override
    public void move_const(X86Register dst, int v) {
        write("mov " + dst + "," + v);
    }

    @Override
    public void move(X86Register dst, int v) {
        move_const(dst, v);
    }

    @Override
    public void move(X86Register dst, X86Register src, int srcDisp) {
        write("mov " + dst + ",[" + src + "+" + srcDisp + "]");
    }

    @Override
    public void move(X86Register dst, X86Register src) {
        write("mov " + dst + "," + src);
    }

    @Override
    public void pop(int v) {
        write("pop " + v);
    }

    @Override
    public void pop(X86Register reg, int offset) {
        write("pop [" + reg + "+" + offset + "]");
    }

    @Override
    public void pop(X86Register reg) {
        write("pop " + reg);
    }

    @Override
    public void push(int v) {
        write("push " + v);
    }

    @Override
    public void push(X86Register reg, int offset) {
        write("push [" + reg + "+" + offset + "]");
    }

    @Override
    public void push(X86Register reg) {
        write("push " + reg);
    }

    @Override
    public void xchg(X86Register dst, X86Register src) {
        write("xchg " + dst + "," + src);
    }

    @Override
    public void xor(X86Register dst, X86Register src) {
        write("xor " + dst + "," + src);
    }

    @Override
    public void emit(OutputStream s) {
        try {
            s.write(obj.toString().getBytes());
        } catch (IOException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void add(X86Register dst, X86Register src) {
        write("add " + dst + "," + src);
    }

    @Override
    public void add(X86Register dst, X86Register src, int disp) {
        write("add " + dst + ",[" + src + "+" + disp + "]");
    }

    @Override
    public void and(X86Register dst, X86Register src) {
        write("and " + dst + "," + src);
    }

    @Override
    public void and(X86Register dst, X86Register src, int disp) {
        write("and " + dst + ",[" + src + "+" + disp + "]");
    }

    @Override
    public void or(X86Register dst, X86Register src) {
        write("or " + dst + "," + src);
    }

    @Override
    public void or(X86Register dst, X86Register src, int disp) {
        write("or " + dst + ",[" + src + "+" + disp + "]");
    }

    @Override
    public void sub(X86Register dst, X86Register src) {
        write("sub " + dst + "," + src);
    }

    @Override
    public void sub(X86Register dst, X86Register src, int disp) {
        write("sub " + dst + ",[" + src + "+" + disp + "]");
    }

    @Override
    public void xor(X86Register dst, X86Register src, int disp) {
        write("xor " + dst + ",[" + src + "+" + disp + "]");
    }

    @Override
    public void add(X86Register dst, int v) {
        write("add " + dst + "," + v);
    }

    @Override
    public void and(X86Register dst, int v) {
        write("and " + dst + "," + v);
    }

    @Override
    public void or(X86Register dst, int v) {
        write("or " + dst + "," + v);
    }

    @Override
    public void sub(X86Register dst, int v) {
        write("sub " + dst + "," + v);
    }

    @Override
    public void xor(X86Register dst, int v) {
        write("xor " + dst + "," + v);
    }

    @Override
    public void neg(X86Register r) {
        write("neg " + r);
    }

    @Override
    public void move(X86Register dst, int disp, X86Register src) {
        write("mov [" + dst + "+" + disp + "]," + src);
    }

    @Override
    public void move_const(X86Register dst, int disp, int v) {
        write("mov [" + dst + "+" + disp + "]," + v);
    }

    private int bc;

    @Override
    public void bind(Label l) {
        String name;
        if (!l.isBinded()) {
            name = ".dl" + Integer.toHexString(++bc);
            l.bind(name);
        } else {
            name = l.toString();
        }
        write(name + ":");
    }

    @Override
    public void sal(X86Register dst, int shift) {
        write("sal " + dst + "," + shift);
    }

    @Override
    public void sar(X86Register dst, int shift) {
        write("sar " + dst + "," + shift);
    }

    @Override
    public void idiv(X86Register dst) {
        write("idiv " + dst);
    }

    @Override
    public void imul(X86Register dst, X86Register src) {
        write("imul " + dst + "," + src);
    }

    @Override
    public void imul(X86Register dst, X86Register src, int v) {
        write("imul " + dst + "," + src + "," + v);
    }

    @Override
    public void ret() {
        write("ret");
    }

    @Override
    public void jcc(Label l, int cond) {
        if (isMethodArea()) {
            obj.append("\t");
        }
        obj.append("j" + ccName(cond) + " ");
        obj.append(l);
        nl();
    }

    @Override
    public void jmp(Label l) {
        if (isMethodArea()) {
            obj.append("\t");
        }
        obj.append("jmp ");
        obj.append(l);
        nl();
    }

    @Override
    public void call(Label l) {
        if (isMethodArea()) {
            obj.append("\t");
        }
        obj.append("call ");
        obj.append(l);
        nl();
    }
}
