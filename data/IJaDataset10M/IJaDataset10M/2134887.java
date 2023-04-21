package EDU.Washington.grad.gjb.cassowary;

class ClObjectiveVariable extends ClAbstractVariable {

    public ClObjectiveVariable(String name) {
        super(name);
    }

    public ClObjectiveVariable(long number, String prefix) {
        super(number, prefix);
    }

    public String toString() {
        return "[" + name() + ":obj]";
    }

    public boolean isExternal() {
        return false;
    }

    public boolean isPivotable() {
        return false;
    }

    public boolean isRestricted() {
        return false;
    }
}
