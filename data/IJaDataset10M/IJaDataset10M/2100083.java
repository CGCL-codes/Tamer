package net.sf.adatagenerator.ex.healthreg.api;

import java.sql.Date;
import net.sf.adatagenerator.api.GeneratedBean;

/**
 * Extends the Health Registry PatientRecord interface by defining fields that
 * are required only for the generation process:
 * <ul>
 * <li/>Culture
 * <li/>Role
 * <li/>RecordId
 * <li/>GroupId
 * </ul>
 * 
 * @author rphall
 */
public interface GeneratedPatientRecord extends PatientRecord, GeneratedBean {

    public enum GenerationField {

        EventDate(Date.class), Culture(String.class), Role(String.class), RecordId(String.class), GroupId(String.class), PairId(String.class);

        private GenerationField(Class<?> type) {
            this.type = type;
        }

        public final Class<?> type;
    }

    void setCulture(String v);

    String getCulture();

    void setRole(String v);

    String getRole();

    void setRecordId(String v);

    String getRecordId();

    void setGroupId(String v);

    String getGroupId();

    void setPairId(String v);

    String getPairId();
}
