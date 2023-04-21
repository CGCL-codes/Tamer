package fr.soleil.snapArchivingApi.SnapshotingTools.Tools;

import java.sql.Timestamp;

public class SnapScalarEvent_RO extends SnapAttribute {

    public SnapScalarEvent_RO() {
    }

    public SnapScalarEvent_RO(String[] snapScalarEvent_RO) {
        setAttribute_complete_name(snapScalarEvent_RO[0]);
        setId_att(Integer.parseInt(snapScalarEvent_RO[1]));
        setId_snap(Integer.parseInt(snapScalarEvent_RO[2]));
        setSnap_date(Timestamp.valueOf(snapScalarEvent_RO[3]));
        setScalarValue(Double.valueOf(snapScalarEvent_RO[4]));
    }

    public void setScalarValue(Double d) {
        setValue(d);
    }

    public Double getScalarValueRO() {
        return (Double) getValue();
    }

    public String[] toArray() {
        Double d = (Double) getValue();
        String snapScalarEvent_RO[] = new String[5];
        snapScalarEvent_RO[0] = getAttribute_complete_name().trim();
        snapScalarEvent_RO[1] = Integer.toString(getId_att());
        snapScalarEvent_RO[2] = Integer.toString(getId_snap());
        snapScalarEvent_RO[3] = getSnap_date().toString().trim();
        snapScalarEvent_RO[4] = d + "";
        return snapScalarEvent_RO;
    }

    public String toString() {
        String snapSpectrumEvent_RO = "";
        snapSpectrumEvent_RO = "Source : \t" + getAttribute_complete_name() + "\r\n" + "Attribute ID : \t" + getId_att() + "\r\n" + "Snap ID : \t" + getId_snap() + "\r\n" + "Snap Time : \t" + getSnap_date() + "\r\n" + "Value : \t" + getScalarValueRO() + "\r\n";
        return snapSpectrumEvent_RO;
    }
}
