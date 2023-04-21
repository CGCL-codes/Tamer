package nts.command;

public class IfEofPrim extends AnyIfPrim {

    private ReadPrim read;

    public IfEofPrim(String name, ReadPrim read) {
        super(name);
        this.read = read;
    }

    protected final boolean holds() {
        return read.eof(Prim.scanFileCode());
    }
}
