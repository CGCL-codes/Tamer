package fr.soleil.TangoArchiving.ArchivingTools.Tools;

public class ScalarEvent_RW extends ArchivingEvent {

    /**
	 * Creates a new instance of DhdbEvent
	 */
    public ScalarEvent_RW() {
        super();
    }

    /**
	 * Creates a new instance of DhdbEvent
	 */
    public ScalarEvent_RW(String[] hdbScalarEvent_RW) {
        this.setAttribute_complete_name(hdbScalarEvent_RW[0]);
        this.setTimeStamp(Long.parseLong(hdbScalarEvent_RW[1]));
        Double[] value = new Double[2];
        if (hdbScalarEvent_RW[2] == null || "".equals(hdbScalarEvent_RW[2])) {
            value[0] = null;
        } else {
            value[0] = Double.valueOf(hdbScalarEvent_RW[2]);
        }
        if (hdbScalarEvent_RW[3] == null || "".equals(hdbScalarEvent_RW[3])) {
            value[1] = null;
        } else {
            value[1] = Double.valueOf(hdbScalarEvent_RW[3]);
        }
        this.setScalarValueRW(value);
    }

    public void setScalarValueRW(Double[] value) {
        setValue(value);
    }

    public void setScalarValueRW(Boolean[] bvalue) {
        Double[] dvalue = new Double[2];
        if (bvalue[0] == null) {
            dvalue[0] = null;
        } else {
            dvalue[0] = new Double(((bvalue[0].booleanValue()) == true) ? 1 : 0);
        }
        if (bvalue[1] == null) {
            dvalue[1] = null;
        } else {
            dvalue[1] = new Double(((bvalue[1].booleanValue()) == true) ? 1 : 0);
        }
        setValue(dvalue);
    }

    public void setScalarValueRW(Float[] fvalue) {
        Double[] dvalue = new Double[2];
        if (fvalue[0] == null) {
            dvalue[0] = null;
        } else {
            dvalue[0] = (Double) new Double(fvalue[0].doubleValue());
        }
        if (fvalue[1] == null) {
            dvalue[1] = null;
        } else {
            dvalue[1] = (Double) new Double(fvalue[1].doubleValue());
        }
        setValue(dvalue);
    }

    public void setScalarValueRW(Integer[] ivalue) {
        Double[] dvalue = new Double[2];
        if (ivalue[0] == null) {
            dvalue[0] = null;
        } else {
            dvalue[0] = (Double) new Double(ivalue[0].doubleValue());
        }
        if (ivalue[1] == null) {
            dvalue[1] = null;
        } else {
            dvalue[1] = (Double) new Double(ivalue[1].doubleValue());
        }
        setValue(dvalue);
    }

    public void setScalarValueRW(Short[] svalue) {
        Double[] dvalue = new Double[2];
        if (svalue[0] == null) {
            dvalue[0] = null;
        } else {
            dvalue[0] = (Double) new Double(svalue[0].doubleValue());
        }
        if (svalue[1] == null) {
            dvalue[1] = null;
        } else {
            dvalue[1] = (Double) new Double(svalue[1].doubleValue());
        }
        setValue(dvalue);
    }

    public void setScalarValueRW(String[] svalue) {
        setValue(svalue);
    }

    public Double[] getScalarValueRW() {
        Double[] value = new Double[2];
        value = (Double[]) getValue();
        return value;
    }

    public String[] getScalarValueRWS() {
        String[] value = new String[2];
        value = (String[]) getValue();
        return value;
    }

    /**
	 * Returns an array representation of the object <I>ArchivingEvent</I>.
	 *
	 * @return an array representation of the object <I>ArchivingEvent</I>.
	 */
    public String[] toArray() {
        Double[] value;
        String[] hdbScalarEvent_RW = new String[4];
        hdbScalarEvent_RW[0] = getAttribute_complete_name().trim();
        hdbScalarEvent_RW[1] = Long.toString(getTimeStamp()).trim();
        if (getValue() instanceof Double[]) {
            value = (Double[]) getScalarValueRW();
            hdbScalarEvent_RW[2] = "" + value[0];
            hdbScalarEvent_RW[3] = "" + value[1];
        } else {
            hdbScalarEvent_RW[2] = getScalarValueRWS()[0];
            hdbScalarEvent_RW[3] = getScalarValueRWS()[1];
        }
        return hdbScalarEvent_RW;
    }

    public String toString() {
        String hdbScalarEvent_RW_String = "";
        if (getValue() instanceof Double[]) {
            hdbScalarEvent_RW_String = "Source : \t" + getAttribute_complete_name() + "\r\n" + "TimeSt : \t" + getTimeStamp() + "\r\n" + "Value READ: \t" + getScalarValueRW()[0] + "\r\n" + "Value WRITE: \t" + getScalarValueRW()[1] + "\r\n";
        } else {
            hdbScalarEvent_RW_String = "Source : \t" + getAttribute_complete_name() + "\r\n" + "TimeSt : \t" + getTimeStamp() + "\r\n" + "Value READ: \t" + getScalarValueRWS()[0] + "\r\n" + "Value WRITE: \t" + getScalarValueRWS()[1] + "\r\n";
        }
        return hdbScalarEvent_RW_String;
    }
}
