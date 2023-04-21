package ggc.pump.device.animas;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DatabaseProtocol;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.util.DataAccessPump;
import java.sql.ResultSet;
import javax.swing.JDialog;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.atech.utils.data.ATechDate;
import com.atech.utils.file.FileReaderContext;

/**
 * @author andy
 *
 */
public class FRC_EZManager_v2 extends DatabaseProtocol implements FileReaderContext {

    private static Log log = LogFactory.getLog(FRC_EZManager_v2.class);

    OutputWriter output_writer = null;

    /**
     * Constructor
     * 
     * @param ow
     */
    public FRC_EZManager_v2(OutputWriter ow) {
        super();
        output_writer = ow;
        m_da = DataAccessPump.getInstance();
    }

    private void callBack(int progress) {
    }

    public void readFile(String filename) {
        this.setJDBCConnection(DatabaseProtocol.DATABASE_MS_ACCESS_MDBTOOL, filename);
        callBack(0);
        ResultSet rs = this.executeQuery("select day, month, year, hours, minutes, bg, userid from bgLog");
        String type = "bgLog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    try {
                        ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), rs.getString("hours"), rs.getString("minutes"));
                        PumpValuesEntry pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                        pve.setSubType(PumpAdditionalDataType.PUMP_ADD_DATA_BG);
                        pve.setValue(rs.getString("bg"));
                        this.output_writer.writeData(pve);
                    } catch (Exception ex) {
                        log.error("Error reading row [" + type + ":" + ex, ex);
                        return;
                    }
                }
                rs.close();
            } catch (Exception ex) {
                log.error("Error reading [" + type + ":" + ex, ex);
                return;
            }
        }
        callBack(15);
        rs = this.executeQuery("select day, month, year, btotal, dtotal, userid from dailytotalslog");
        type = "dailytotalslog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    try {
                        ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), "23", "59");
                        float basal = getFloat(rs.getString("btotal"));
                        float total = getFloat(rs.getString("dtotal"));
                        PumpValuesEntry pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                        pve.setSubType(PumpReport.PUMP_REPORT_BASAL_TOTAL_DAY);
                        pve.setValue(DataAccessPump.Decimal3Format.format(basal));
                        this.output_writer.writeData(pve);
                        pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                        pve.setSubType(PumpReport.PUMP_REPORT_BOLUS_TOTAL_DAY);
                        pve.setValue(DataAccessPump.Decimal3Format.format(total - basal));
                        this.output_writer.writeData(pve);
                        pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                        pve.setSubType(PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY);
                        pve.setValue(DataAccessPump.Decimal3Format.format(total));
                        this.output_writer.writeData(pve);
                    } catch (Exception ex) {
                        log.error("Error reading row [" + type + ":" + ex, ex);
                    }
                }
                rs.close();
            } catch (Exception ex) {
                log.error("Error reading [" + type + ":" + ex, ex);
            }
        }
        callBack(15);
        rs = null;
        type = "insulinLog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    @SuppressWarnings("unused") ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), rs.getString("hours"), rs.getString("minutes"));
                    int units1 = rs.getInt(5);
                    int units2 = rs.getInt(16);
                    double units = 0;
                    units += (double) units1 / 10.0;
                    units += (double) units2 / 100.0;
                }
                rs.close();
            } catch (Exception ex) {
                log.error("Error reading row [" + type + "]:" + ex, ex);
            }
        }
        callBack(30);
        rs = this.executeQuery("select day, month, year, hours, minutes, note, pumpalarm, userid from notesLog");
        type = "notesLog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    ATechDate atd = getAtechDate(rs.getString("day"), rs.getString("month"), rs.getString("year"), rs.getString("hours"), rs.getString("minutes"));
                    String note = rs.getString("note");
                    int error_code = getInt(rs.getString("pumpalarm"));
                    PumpValuesEntry pve = new PumpValuesEntry();
                    pve.setDateTimeObject(atd);
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                    pve.setSubType(PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT);
                    pve.setValue(note);
                    if (error_code > 0) {
                    } else {
                        if (note.startsWith("Pump primed")) {
                        } else if (note.startsWith("Cannula filled")) {
                        } else if (note.startsWith("Pump suspended.  Resume time:")) {
                        } else System.out.println("Note: " + note);
                    }
                }
                rs.close();
                return;
            } catch (Exception ex) {
                log.error("Error reading row [" + type + "]:" + ex, ex);
            }
        }
        callBack(45);
        rs = this.executeQuery("select * from foodLog");
        type = "foodLog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
                    double multiplier = rs.getDouble(7);
                    double carbs = rs.getDouble(8);
                    carbs = carbs / multiplier;
                    double fiber = rs.getDouble(9);
                    fiber = fiber / multiplier;
                    @SuppressWarnings("unused") double calories = rs.getDouble(10);
                    double protien = rs.getDouble(11);
                    protien = protien / multiplier;
                    double fat = rs.getDouble(12);
                    fat = fat / multiplier;
                    @SuppressWarnings("unused") String name = rs.getString(13);
                    PumpValuesEntry pve = new PumpValuesEntry();
                    pve.setDateTimeObject(atd);
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                }
                rs.close();
            } catch (Exception ex) {
                log.error("Error reading row [" + type + "]:" + ex, ex);
            }
        }
        callBack(60);
        rs = this.executeQuery("select * from activityLog");
        type = "activityLog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
                    @SuppressWarnings("unused") int duration = rs.getInt(5);
                    @SuppressWarnings("unused") String name = rs.getString(13);
                    PumpValuesEntry pve = new PumpValuesEntry();
                    pve.setDateTimeObject(atd);
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                }
                rs.close();
            } catch (Exception ex) {
                log.error("Error reading row [" + type + "]:" + ex, ex);
            }
        }
        callBack(75);
        rs = this.executeQuery("select day, month, year, hours, minutes, rate, userid from pumpbasallog");
        type = "pumpbasallog";
        if (rs != null) {
            try {
                while (rs.next()) {
                    try {
                        ATechDate atd = getAtechDate(rs.getInt("day"), rs.getInt("month"), rs.getInt("year"), rs.getInt("hours"), rs.getInt("minutes"));
                        PumpValuesEntry pve = new PumpValuesEntry();
                        pve.setDateTimeObject(atd);
                        pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                        pve.setSubType(PumpBasalSubType.PUMP_BASAL_VALUE);
                        double rate = rs.getInt("rate") / 1000.0d;
                        pve.setValue(DataAccessPump.Decimal3Format.format(rate));
                    } catch (Exception ex) {
                        log.error("Error reading row [" + type + "]:" + ex, ex);
                    }
                }
                rs.close();
            } catch (Exception ex) {
                log.error("Error reading [" + type + "]:" + ex, ex);
            }
        }
        callBack(90);
        callBack(100);
    }

    private int getInt(String val) {
        return m_da.getIntValueFromString(val, 0);
    }

    private float getFloat(String val) {
        return m_da.getFloatValueFromString(val, 0.0f);
    }

    /**
     * Get ATech Date from int's 
     * 
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @return
     */
    public ATechDate getAtechDate(int day, int month, int year, int hour, int minute) {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S);
        atd.day_of_month = day;
        atd.month = month;
        atd.year = year;
        atd.hour_of_day = hour;
        atd.minute = minute;
        atd.second = 0;
        return atd;
    }

    /**
     * Get ATech Date from int's 
     * 
     * @param day
     * @param month
     * @param year
     * @param hour
     * @param minute
     * @return
     */
    public ATechDate getAtechDate(String day, String month, String year, String hour, String minute) {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S);
        atd.day_of_month = getInt(day);
        atd.month = getInt(month);
        atd.year = getInt(year);
        atd.hour_of_day = getInt(hour);
        atd.minute = getInt(minute);
        atd.second = 0;
        return atd;
    }

    public String getFileDescription() {
        return null;
    }

    public String getFileExtension() {
        return null;
    }

    public FileFilter getFileFilter() {
        return null;
    }

    public String getFullFileDescription() {
        return null;
    }

    public void goToNextDialog(JDialog currentDialog) {
    }

    public boolean hasSpecialSelectorDialog() {
        return false;
    }
}
