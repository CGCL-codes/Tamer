package org.dcm4chex.rid.mbean.ecg;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;

/**
 * @author franz.willer
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WaveformInfo {

    private Dataset dataset;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

    public WaveformInfo(Dataset ds) {
        dataset = ds;
    }

    public String getPatientName() {
        String s = dataset.getString(Tags.PatientName);
        return s == null ? "" : s.replace('^', ' ');
    }

    public String getPatientID() {
        String s = dataset.getString(Tags.PatientID);
        return s == null ? "" : s;
    }

    public String getAcquisTime() {
        Date d = dataset.getDate(Tags.AcquisitionDatetime);
        if (d == null) {
            d = dataset.getDate(Tags.AcquisitionTime);
        }
        if (d == null) return "??:??:??";
        return timeFormatter.format(d);
    }

    public String getAcquisDate() {
        Date d = dataset.getDate(Tags.AcquisitionDatetime);
        if (d == null) {
            d = dataset.getDate(Tags.AcquisitionDate);
        }
        return getDateString(d);
    }

    public String getBirthday() {
        Date d = dataset.getDate(Tags.PatientBirthDate);
        return getDateString(d);
    }

    public String getSex() {
        String sex = dataset.getString(Tags.PatientSex);
        if (sex != null) {
            if ("M".equalsIgnoreCase(sex)) return "Male";
            if ("F".equalsIgnoreCase(sex)) return "Female";
        }
        return "Other";
    }

    public String getPatientSize() {
        return (dataset.getFloat(Tags.PatientSize, 0f) * 100) + " cm";
    }

    public String getPatientWeight() {
        return dataset.getFloat(Tags.PatientWeight, 0f) + " kg";
    }

    private String getDateString(Date d) {
        if (d == null) return "????-??-??";
        return dateFormatter.format(d);
    }
}
