package gwt.cassowary.client.EDU.Washington.grad.gjb;

class ClSlackVariable extends ClAbstractVariable {

    public ClSlackVariable(String name) {
        super(name);
    }

    public ClSlackVariable() {
    }

    public ClSlackVariable(long number, String prefix) {
        super(number, prefix);
    }

    public String toString() {
        return "[" + name() + ":slack]";
    }

    public boolean isExternal() {
        return false;
    }

    public boolean isPivotable() {
        return true;
    }

    public boolean isRestricted() {
        return true;
    }
}
