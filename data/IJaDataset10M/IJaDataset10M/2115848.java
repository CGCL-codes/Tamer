package bias.util.cli;

/**
 * An switch option.
 */
public abstract class Switch extends Option {

    public Switch(char name) {
        super(name);
    }

    @Override
    protected int parsed(String arg, int to) throws CLIException {
        onSwitch();
        return to;
    }

    protected abstract void onSwitch();
}
