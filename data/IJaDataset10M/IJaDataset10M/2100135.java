package jpen;

public class PenStateCopy extends PenState implements java.io.Serializable {

    public static final long serialVersionUID = 1l;

    public PenStateCopy() {
    }

    public PenStateCopy(PenState penState) {
        setValues(penState);
    }

    public PenState.Levels getLevels() {
        return levels;
    }

    public void setLevelValue(PLevel level) {
        levels.setValue(level.typeNumber, level.value);
    }

    public void setLevelValue(PLevel.Type type, float value) {
        levels.setValue(type, value);
    }

    public void setLevelValue(int typeNumber, float value) {
        levels.setValue(typeNumber, value);
    }

    public boolean setButtonValue(PButton button) {
        return super.setButtonValue(button.typeNumber, button.value);
    }

    public boolean setButtonValue(int typeNumber, boolean value) {
        return super.setButtonValue(typeNumber, value);
    }

    public boolean setButtonValue(PButton.Type type, boolean value) {
        return super.setButtonValue(type.ordinal(), value);
    }

    public void setKind(PKind kind) {
        super.setKind(kind);
    }

    public void setKind(PKind.Type type) {
        super.setKind(PKind.valueOf(type.ordinal()));
    }

    public void setKind(int kindTypeNumber) {
        super.setKind(PKind.valueOf(kindTypeNumber));
    }

    public void setValues(PenState penState) {
        super.setValues(penState);
    }

    public void setValues(PenEvent ev) {
        ev.copyTo(this);
    }
}
