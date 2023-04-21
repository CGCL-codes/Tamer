package ggc.pump.output;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.util.DataAccessPump;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.GregorianCalendar;
import com.atech.utils.ATechDate;

public class GGCFileOutputWriter extends AbstractOutputWriter {

    BufferedWriter bw;

    DataAccessPump m_da;

    long time_created;

    public GGCFileOutputWriter() {
        super();
        out_util = new OutputUtil(this);
        m_da = DataAccessPump.getInstance();
        try {
            System.out.println("OPEN FILE");
            bw = new BufferedWriter(new FileWriter(new File("DayValueH" + getCurrentDateForFile() + ".txt")));
            this.time_created = System.currentTimeMillis();
        } catch (Exception ex) {
            System.out.println("Error opening file:" + ex);
        }
    }

    public String getCurrentDateForFile() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());
        return gc.get(GregorianCalendar.YEAR) + "_" + m_da.getLeadingZero((gc.get(GregorianCalendar.MONTH) + 1), 2) + "_" + m_da.getLeadingZero(gc.get(GregorianCalendar.DAY_OF_MONTH), 2);
    }

    private void setReadData() {
    }

    public void writeRawData(String input, boolean bg_data) {
        writeToFile(input);
        if (bg_data) setReadData();
    }

    public void writeDeviceIdentification() {
        writeToFile(this.getDeviceIdentification().getInformation("; "));
    }

    public void writeBGData(PumpValuesEntry mve) {
        System.out.println("writeBGData in GGCFileOutputWriter from PumpTools not implemented !");
    }

    public void writeHeader() {
        StringBuffer sb = new StringBuffer();
        sb.append("; Class: ggc.db.hibernate.DayValueH\n");
        sb.append("; Date of export: " + ATechDate.getDateTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, new GregorianCalendar()));
        sb.append("; Exported by GGC Meter Tools - GGCHibernateOutputWriter\n");
        sb.append(";\n");
        sb.append("; Columns: id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, comment, changed\n");
        sb.append(";\n");
        writeToFile(sb.toString());
    }

    private void writeToFile(String values) {
        try {
            bw.write(values);
            bw.newLine();
            bw.flush();
        } catch (Exception ex) {
            System.out.println("Write to file failed: " + ex);
        }
    }

    public void endOutput() {
        System.out.println("END OUTPUT");
        try {
            bw.flush();
            bw.close();
        } catch (Exception ex) {
            System.out.println("Closing file failed: " + ex);
        }
        this.interruptCommunication();
    }
}
