package nts.command;

public class EndCsNamePrim extends Prim {

    public EndCsNamePrim(String name) {
        super(name);
    }

    public final boolean isEndCsName() {
        return true;
    }

    public void exec(Token src) {
        error("ExtraEndcsname", esc("endcsname"));
    }
}
