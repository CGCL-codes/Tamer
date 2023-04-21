package dde.input;

public class KeyAction {

    private String actionName;

    private int keyCode;

    private boolean active;

    private boolean restrict;

    private int[] modifiers;

    public KeyAction(String actionName, int keyCode, int... modifiers) {
        setActionName(actionName);
        setKeyCode(keyCode);
        setActive(false);
        setModifiers(modifiers);
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean equals(KeyAction action) {
        System.out.println("equals");
        return getActionName().equals(action.getActionName()) && getKeyCode() == action.getKeyCode();
    }

    public boolean isRestrict() {
        return restrict;
    }

    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }

    public int getModifiers() {
        int mask = 0;
        for (int mod : modifiers) {
            mask |= mod;
        }
        return mask;
    }

    public void setModifiers(int[] modifiers) {
        this.modifiers = modifiers;
    }

    public boolean hasModifiers() {
        return modifiers.length > 0;
    }
}
