package nts.command;

public class NoExpandPrim extends ExpandablePrim {

    public NoExpandPrim(String name) {
        super(name);
    }

    public void expand(Token src) {
        Token tok = nextUncheckedRawToken();
        boolean exp = (tok.definable() || meaningOf(tok).expandable());
        getTokStack().cleanFinishedLists();
        getTokStack().backUp(tok, !exp);
    }
}
