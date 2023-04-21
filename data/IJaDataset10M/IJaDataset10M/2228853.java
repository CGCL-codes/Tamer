package rubbish.db;

/**
 * �߂�l�t���N���[�W��(Database)
 * 
 * @author $Author: winebarrel $
 * @version $Revision: 1.1 $
 */
public abstract class ReturnedDatabaseHandler implements DatabaseHandler {

    protected Object returnValue = null;

    protected void setReturnValue(Object value) {
        returnValue = value;
    }

    protected void setBoolReturnValue(boolean value) {
        returnValue = Boolean.valueOf(value);
    }

    protected void setByteReturnValue(byte value) {
        returnValue = new Byte(value);
    }

    protected void setShortReturnValue(short value) {
        returnValue = new Short(value);
    }

    protected void setIntReturnValue(int value) {
        returnValue = new Integer(value);
    }

    protected void setLongReturnValue(long value) {
        returnValue = new Long(value);
    }

    protected void setFloatReturnValue(float value) {
        returnValue = new Float(value);
    }

    protected void setDoubleReturnValue(double value) {
        returnValue = new Double(value);
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public boolean getBoolReturnValue() {
        return ((Boolean) returnValue).booleanValue();
    }

    public byte getByteReturnValue() {
        return ((Number) returnValue).byteValue();
    }

    public short getShortReturnValue() {
        return ((Number) returnValue).shortValue();
    }

    public int getIntReturnValue() {
        return ((Number) returnValue).intValue();
    }

    public long getLongReturnValue() {
        return ((Number) returnValue).longValue();
    }

    public float getFloatReturnValue() {
        return ((Number) returnValue).floatValue();
    }

    public double getDoubleReturnValue() {
        return ((Number) returnValue).doubleValue();
    }
}
