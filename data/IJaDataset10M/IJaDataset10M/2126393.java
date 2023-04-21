package translate;

import tree.ExpList;

public class ArrayExp extends Ex {

    public ArrayExp(Exp size, Exp init, Level l) {
        super(null);
        temp.Temp pointer = new temp.Temp();
        temp.Temp itr = new temp.Temp();
        temp.Temp target = new temp.Temp();
        tree.BasicStm initStm = null;
        temp.Label over = new temp.Label();
        temp.Label next = new temp.Label();
        temp.Label test = new temp.Label();
        tree.BasicExp subscriptAddr = new tree.MEM(new tree.BINOP(tree.BINOP.PLUS, new tree.TEMP(pointer), new tree.BINOP(tree.BINOP.MUL, new tree.TEMP(itr), new tree.CONST(4))));
        initStm = new tree.SEQ(new tree.MOVE(new tree.TEMP(itr), new tree.CONST(0)), new tree.SEQ(new tree.MOVE(new tree.TEMP(target), size.unEx()), new tree.SEQ(new tree.LABEL(test), new tree.SEQ(new tree.CJUMP(tree.CJUMP.LT, new tree.TEMP(itr), new tree.TEMP(target), next, over), new tree.SEQ(new tree.LABEL(next), new tree.SEQ(new tree.MOVE(subscriptAddr, init.unEx()), new tree.SEQ(new tree.MOVE(new tree.TEMP(itr), new tree.BINOP(tree.BINOP.PLUS, new tree.TEMP(itr), new tree.CONST(1))), new tree.SEQ(new tree.JUMP(test), new tree.LABEL(over)))))))));
        exp = new tree.ESEQ(new tree.SEQ(new tree.MOVE(new tree.TEMP(pointer), l.frame.externalCall("initArray", new ExpList(size.unEx(), new ExpList(new tree.CONST(0), null)))), initStm), new tree.TEMP(pointer));
    }
}
