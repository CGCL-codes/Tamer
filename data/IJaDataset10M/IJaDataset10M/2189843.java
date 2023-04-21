package diet.parameters;

/**
 *
 * @author user
 */
public class BooleanParameter extends Parameter {

    Boolean value;

    public BooleanParameter() {
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = (Boolean) value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    public String getID() {
        return super.getID();
    }

    @Override
    public boolean isMutableInExperiment() {
        return super.isMutableInExperiment();
    }

    @Override
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    public void setID(String s) {
        super.setID(s);
    }

    @Override
    public void setMutableInExperiment(boolean mutable) {
        super.setMutableInExperiment(mutable);
    }

    public boolean checkValIsOK(Object val) {
        if (val instanceof Boolean) return true;
        return false;
    }

    public boolean saveAndCheckValidity(Object val) {
        if (!checkValIsOK(val)) return false;
        this.value = (Boolean) val;
        return true;
    }
}
