package unibg.overencrypt.client.commands;

/**
 * The Class AbstractCommand.
 */
public abstract class AbstractCommand implements Command {

    /** The command keyword. */
    protected String keyword;

    /** The command description. */
    protected String description;

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public abstract void run(String[] args);

    @Override
    public boolean equals(Object object) {
        if (object instanceof Command) {
            return ((Command) object).getKeyword().equals(getKeyword());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return keyword.hashCode();
    }

    protected void exitWithError(String message) {
        System.err.println("[ERROR] " + message);
        System.exit(1);
    }

    protected void exitWithSuccess(String message) {
        System.out.println("[OK] " + message);
        System.exit(0);
    }
}
