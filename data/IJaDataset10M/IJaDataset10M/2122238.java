package net.sf.eclint.splint.options;

public class ModeOption extends Option implements IBooleanOption {

    public final boolean weak;

    public final boolean standard;

    public final boolean checks;

    public final boolean strict;

    private boolean enabled;

    public ModeOption(String description, String id, boolean weak, boolean standard, boolean checks, boolean strict, String argument) {
        super(description, id, argument);
        this.weak = weak;
        this.standard = standard;
        this.checks = checks;
        this.strict = strict;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
