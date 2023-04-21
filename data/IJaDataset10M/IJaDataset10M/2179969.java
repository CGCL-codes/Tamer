package nts.command;

import nts.base.ClassAssoc;

public abstract class GroupCommand extends Command {

    private static ClassAssoc assoc = new ClassAssoc(Group.class);

    public static void registerGroup(Class grp) {
        assoc.record(grp);
    }

    public final GroupCommand defineClosing(Class grp, Closing clos) {
        assoc.put(grp, this, clos);
        return this;
    }

    public final Closing getClosing(Class grp) {
        return (Closing) assoc.get(grp, this);
    }

    public final void exec(Token src) {
        Group grp = getGrp();
        Closing clos = getClosing(grp.getClass());
        if (clos == Closing.NULL) exec(grp, src); else clos.exec(grp, src);
    }

    public abstract void exec(Group grp, Token src);
}
