package sfa.projetIHM.VolleyBall.command;

class NullCommand implements Command {

    private static final NullCommand DEFAULT = new NullCommand();

    private NullCommand() {
    }

    public static NullCommand getDefault() {
        return DEFAULT;
    }

    public void execute() {
    }

    public void undo() {
    }

    public void redo() {
    }
}
