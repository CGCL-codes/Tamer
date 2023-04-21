package fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools;

import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;

public class SpectrumEvent_RO extends ArchivingEvent {

    private int dimX;

    private final int dimY = 0;

    /**
     * Creates a new instance of Spectrum Event
     */
    public SpectrumEvent_RO() {
        super();
    }

    public int getDim_x() {
        return dimX;
    }

    public void setDim_x(final int dimX) {
        this.dimX = dimX;
    }

    public int getDim_y() {
        return dimY;
    }

    /**
     * Creates a new instance of Spectrum Event
     */
    public SpectrumEvent_RO(final String[] hdbSpectrumEventRO) {
        this.setAttribute_complete_name(hdbSpectrumEventRO[0]);
        this.setTimeStamp(Long.parseLong(hdbSpectrumEventRO[1]));
        this.setDim_x(Integer.parseInt(hdbSpectrumEventRO[2]));
        final Double[] value = new Double[hdbSpectrumEventRO.length - 4];
        for (int i = 0; i < value.length; i++) {
            if (hdbSpectrumEventRO[i + 4] == null || "".equals(hdbSpectrumEventRO[i + 4]) || "null".equals(hdbSpectrumEventRO[i + 4])) {
                value[i] = null;
            } else {
                value[i] = Double.valueOf(hdbSpectrumEventRO[i + 4]);
            }
        }
        this.setValue(value);
    }

    /**
     * This method returns the value of this spectrum event. The returned value
     * is then formated as a String.
     * 
     * @return the value of this spectrum event.
     */
    public String getValue_AsString() {
        final Object value = getValue();
        if (value == null) {
            return GlobalConst.ARCHIVER_NULL_VALUE;
        }
        final StringBuffer valueStr = new StringBuffer();
        if (((Object[]) value).length == 0) {
            return "";
        }
        if (value instanceof Double[]) {
            for (int i = 0; i < ((Double[]) value).length - 1; i++) {
                valueStr.append(((Double[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((Double[]) value)[((Double[]) value).length - 1]);
        } else if (value instanceof Byte[]) {
            for (int i = 0; i < ((Byte[]) value).length - 1; i++) {
                valueStr.append(((Byte[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((Byte[]) value)[((Byte[]) value).length - 1]);
        } else if (value instanceof Short[]) {
            for (int i = 0; i < ((Short[]) value).length - 1; i++) {
                valueStr.append(((Short[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((Short[]) value)[((Short[]) value).length - 1]);
        } else if (value instanceof Integer[]) {
            for (int i = 0; i < ((Integer[]) value).length - 1; i++) {
                valueStr.append(((Integer[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((Integer[]) value)[((Integer[]) value).length - 1]);
        } else if (value instanceof Float[]) {
            for (int i = 0; i < ((Float[]) value).length - 1; i++) {
                valueStr.append(((Float[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((Float[]) value)[((Float[]) value).length - 1]);
        } else if (value instanceof Boolean[]) {
            for (int i = 0; i < ((Boolean[]) value).length - 1; i++) {
                valueStr.append(((Boolean[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((Boolean[]) value)[((Boolean[]) value).length - 1]);
        } else if (value instanceof String[]) {
            for (int i = 0; i < ((String[]) value).length - 1; i++) {
                valueStr.append(((String[]) value)[i]).append(GlobalConst.CLOB_SEPARATOR);
            }
            valueStr.append(((String[]) value)[((String[]) value).length - 1]);
        } else {
            valueStr.append(value.toString());
        }
        return valueStr.toString();
    }

    /**
     * Returns an array representation of the object <I>SpectrumEvent_RO</I>.
     * 
     * @return an array representation of the object <I>SpectrumEvent_RO</I>.
     */
    @Override
    public String[] toArray() {
        final Double[] value = (Double[]) getValue();
        final String[] hdbSpectrumEvent_ro = new String[4 + value.length];
        hdbSpectrumEvent_ro[0] = getAttribute_complete_name();
        hdbSpectrumEvent_ro[1] = Long.toString(getTimeStamp()).trim();
        hdbSpectrumEvent_ro[2] = Integer.toString(value.length);
        hdbSpectrumEvent_ro[3] = "0";
        for (int i = 0; i < value.length; i++) {
            hdbSpectrumEvent_ro[i + 4] = "" + value[i];
        }
        return hdbSpectrumEvent_ro;
    }

    @Override
    public String toString() {
        final StringBuffer event_String = new StringBuffer();
        event_String.append("Source : \t").append(getAttribute_complete_name()).append("\r\n");
        event_String.append("TimeSt : \t").append(getTimeStamp()).append("\r\n");
        event_String.append("Value :  \t...").append("\r\n");
        return event_String.toString();
    }
}
