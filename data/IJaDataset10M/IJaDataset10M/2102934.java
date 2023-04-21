package mindbright.terminal;

public abstract class TerminalInterpreter {

    protected Terminal term;

    public static final int IGNORE = -1;

    public abstract String terminalType();

    public abstract int interpretChar(char c);

    public void vtReset() {
    }

    public void keyHandler(int virtualKey, int gMode) {
    }

    public void mouseHandler(int x, int y, boolean press, int modifiers) {
    }

    public final void setTerminal(Terminal term) {
        this.term = term;
    }
}
