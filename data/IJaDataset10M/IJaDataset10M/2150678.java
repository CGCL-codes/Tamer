package mips;

import java.util.ArrayList;
import assem.InstrList;
import temp.Label;
import temp.Temp;
import temp.TempList;
import tree.BasicExp;
import tree.BasicStm;
import tree.ExpList;
import util.BoolList;
import frame.Access;

public class Frame extends frame.Frame {

    int wordSize = 4, frameSize = 0, outSize = 0, spillSize = 0;

    temp.Temp fpReg = new temp.Temp(1);

    temp.Temp spReg = new temp.Temp(2);

    ArrayList<temp.Temp> argReg = new ArrayList<temp.Temp>();

    temp.TempList callerSaveReg = null;

    temp.TempList calleeSaveReg = null;

    temp.Temp rvReg = new temp.Temp(7);

    temp.Temp raReg = new temp.Temp(8);

    temp.Temp v1Reg = new temp.Temp(37);

    private String getSpillSizeStrNeg() {
        return name.toString() + "_spillsize";
    }

    public int allocSpillVar() {
        return (frameSize + (spillSize += wordSize));
    }

    public Frame(Label n, BoolList fmls) {
        super();
        AccessList head = null, tail = null;
        Access ac = null;
        name = n;
        for (int i = 0; i < 4; i++) argReg.add(new temp.Temp(3 + i));
        for (int i = 0; i < 10; i++) callerSaveReg = new temp.TempList(new temp.Temp(17 + i), callerSaveReg);
        for (int i = 0; i < 8; i++) calleeSaveReg = new temp.TempList(new temp.Temp(9 + i), calleeSaveReg);
        for (BoolList b = fmls; b != null; b = b.tail) {
            ac = allocLocal(b.head);
            if (head == null) head = tail = new AccessList(ac, null); else tail = tail.tail = new AccessList(ac, null);
        }
        formals = head;
    }

    public Temp FP() {
        return fpReg;
    }

    public Temp SP() {
        return spReg;
    }

    public Access allocLocal(boolean escape) {
        if (escape) return new InFrame(-(frameSize += wordSize)); else return new InReg();
    }

    public BasicExp externalCall(String func, ExpList args) {
        return new tree.CALL(new tree.NAME(new Label(func)), new ExpList(new tree.CONST(0), args));
    }

    @Override
    public BasicStm procEntryExit1(BasicStm body) {
        int c = 0;
        tree.BasicStm head = null, tail = null;
        tree.BasicStm stm;
        for (AccessList al = (AccessList) formals; al != null; al = al.tail) {
            if (c < 4) stm = new tree.MOVE(al.head.exp(new tree.TEMP(spReg)), new tree.TEMP(argReg.get(c))); else stm = new tree.MOVE(al.head.exp(new tree.TEMP(spReg)), new tree.MEM(new tree.BINOP(tree.BINOP.PLUS, new tree.TEMP(fpReg), new tree.CONST((c - 3) * 4))));
            if (head == null) {
                head = tail = new tree.SEQ(stm, null);
                if (al.tail == null) head = tail = stm;
            } else {
                if (al.tail == null) (((tree.SEQ) tail).right) = stm; else tail = ((tree.SEQ) tail).right = new tree.SEQ(stm, null);
            }
            c++;
        }
        if (head == null) return body; else return new tree.SEQ(head, body);
    }

    public int wordSize() {
        return wordSize;
    }

    public Temp RV() {
        return rvReg;
    }

    public Temp V1() {
        return v1Reg;
    }

    public Temp RA() {
        return raReg;
    }

    public ArrayList<temp.Temp> getArgRegs() {
        return argReg;
    }

    public assem.InstrList generateCode(tree.StmList sl) {
        Codegen cg = new Codegen(this);
        InstrList tmp = cg.generateCode(sl);
        return procEntryExit2(tmp);
    }

    public InstrList procEntryExit2(InstrList body) {
        InstrList temp = null;
        int count;
        temp = new InstrList(new assem.OPER("\tjr\t's0\n", null, new temp.TempList(raReg, null)), temp);
        temp = new InstrList(new assem.OPER("\tlw\t'd0, " + (-frameSize - 3 * wordSize) + "+" + getSpillSizeStrNeg() + "('s0)\n", new temp.TempList(fpReg, null), new temp.TempList(spReg, null)), temp);
        temp = new InstrList(new assem.OPER("\taddi\t'd0, 's0, 4\n", new temp.TempList(spReg, null), new temp.TempList(fpReg, null)), temp);
        temp = new InstrList(new assem.OPER("\tlw\t'd0, " + (-frameSize - wordSize) + "+" + getSpillSizeStrNeg() + "('s0)\n", new temp.TempList(raReg, null), new temp.TempList(fpReg, null)), temp);
        count = 0;
        for (TempList i = calleeSaveReg; i != null; i = i.tail) temp = new InstrList(new assem.OPER("\tlw\t'd0, " + (count++ * wordSize) + "('s0)\n", new temp.TempList(i.head, null), new temp.TempList(spReg, null)), temp);
        InstrList mid = append(body, temp);
        temp = null;
        count = 0;
        for (TempList i = calleeSaveReg; i != null; i = i.tail) temp = new InstrList(new assem.OPER("\tsw\t's0, " + (count++ * wordSize) + "('s1)\n", null, new temp.TempList(i.head, new temp.TempList(spReg, null))), temp);
        temp = new InstrList(new assem.OPER("\tsw\t's0, " + (-frameSize - wordSize) + "+" + getSpillSizeStrNeg() + "('s1)\n", null, new temp.TempList(raReg, new temp.TempList(fpReg, null))), temp);
        temp = new InstrList(new assem.OPER("\tsw\t's0, 0('s1)\n", null, new temp.TempList(RV(), new temp.TempList(fpReg, null))), temp);
        temp = new InstrList(new assem.OPER("\taddi\t'd0, 's0, " + (-frameSize - 11 * wordSize) + "+" + getSpillSizeStrNeg() + "\n", new temp.TempList(spReg, null), new temp.TempList(spReg, null)), temp);
        temp = new InstrList(new assem.OPER("\taddi\t'd0, 's0, -4\n", new temp.TempList(fpReg, null), new temp.TempList(spReg, null)), temp);
        temp = new InstrList(new assem.OPER("\tsw\t's0, " + (-frameSize - 3 * wordSize) + "+" + getSpillSizeStrNeg() + "('s1)\n", null, new temp.TempList(fpReg, new temp.TempList(spReg, null))), temp);
        temp = append(temp, mid);
        return temp;
    }

    public Proc procEntryExit3(InstrList body) {
        return new Proc(getSpillSizeStrNeg() + " = " + (-spillSize) + "\n" + this.name.toString() + ":\n", body, "");
    }

    public static InstrList append(InstrList a, InstrList b) {
        InstrList i;
        for (i = a; (i != null) && i.tail != null; i = i.tail) ;
        i.tail = b;
        return a;
    }

    public TempList getCallModifyRegs() {
        TempList tmp = null;
        for (TempList i = callerSaveReg; i != null; i = i.tail) tmp = new TempList(i.head, tmp);
        for (int i = 0; i < argReg.size(); i++) tmp = new TempList(argReg.get(i), tmp);
        return tmp;
    }

    public TempList getCallerSaveRegs() {
        return callerSaveReg;
    }

    public TempList getCalleeSaveRegs() {
        return calleeSaveReg;
    }
}
