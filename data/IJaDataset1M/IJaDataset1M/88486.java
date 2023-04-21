package com.dcivision.dms.web;

import java.util.Calendar;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.Utility;
import com.dcivision.framework.web.AbstractSearchForm;

/**
  ListDmsArchiveForm.java

  The ActionForm for ListDmsArchiveForm

    @author          Zoe Shum
    @company         DCIVision Limited
    @creation 18   01/09/2003
    @version         $Revision: 1.5.4.1 $
    */
public class ListDmsArchiveForm extends AbstractSearchForm {

    public static final String REVISION = "$Revision: 1.5.4.1 $";

    /** Holds value of property updateDateFrom.  */
    private String accessDateFrom;

    /** Holds value of property updateDateTo.  */
    private String accessDateTo;

    /** Holds value of property periodType.   */
    private String periodType = "full";

    /** Holds value of property accessType. */
    private String accessType;

    private String objectType = GlobalConstant.OBJECT_TYPE_DOCUMENT;

    public ListDmsArchiveForm() {
        super();
        this.setSortAttribute("CREATE_DATE");
        this.setSortOrder("DESC");
        this.addSearchableAttribute("accessDateFrom");
        this.addSearchableAttribute("accessDateTo");
        this.addSearchableAttribute("periodType");
        this.addSearchableAttribute("accessType");
        this.addSearchableAttribute("objectType");
    }

    /** Getter for property updateDateFrom.
   * @return Value of property updateDateFrom.
   */
    public String getAccessDateFrom() {
        if (!"".equals(this.getPeriodType())) {
            Calendar cal = Calendar.getInstance();
            if (Utility.isEmpty(periodType)) {
                return accessDateFrom;
            } else if ("week".equals(periodType)) {
                cal.add(Calendar.DATE, 1 - cal.get(Calendar.DAY_OF_WEEK));
            } else if ("month".equals(periodType)) {
                cal.set(Calendar.DATE, 1);
            } else if ("full".equals(periodType)) {
                return null;
            }
            cal.set(Calendar.AM_PM, Calendar.AM);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return TextUtility.formatDate(cal.getTime(), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DB_DATETIME_FORMAT));
        } else {
            return this.accessDateFrom;
        }
    }

    /** Setter for property updateDateFrom.
   * @param updateDateFrom New value of property updateDateFrom.
   */
    public void setAccessDateFrom(String accessDateFrom) {
        this.accessDateFrom = accessDateFrom;
    }

    /** Getter for property updateDateTo.
   * @return Value of property updateDateTo.
   */
    public String getAccessDateTo() {
        if (!"".equals(this.getPeriodType())) {
            Calendar cal = Calendar.getInstance();
            if (Utility.isEmpty(periodType)) {
                return accessDateTo;
            } else if ("week".equals(periodType)) {
                cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
            } else if ("month".equals(periodType)) {
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DATE, 0);
            } else if ("full".equals(periodType)) {
                return null;
            }
            cal.set(Calendar.AM_PM, Calendar.PM);
            cal.set(Calendar.HOUR, 11);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 59);
            return TextUtility.formatDate(cal.getTime(), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DB_DATETIME_FORMAT));
        } else {
            return this.accessDateTo;
        }
    }

    /** Setter for property updateDateTo.
   * @param updateDateTo New value of property updateDateTo.
   */
    public void setAccessDateTo(String accessDateTo) {
        this.accessDateTo = accessDateTo;
    }

    /** Getter for property periodType.
   * @return Value of property periodType.
   */
    public String getPeriodType() {
        return this.periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    /** Getter for property accessType.
   * @return Value of property accessType.
   */
    public String getAccessType() {
        return this.accessType;
    }

    /** Setter for property accessType.
   * @param accessType New value of property accessType.
   */
    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getObjectType() {
        return (this.objectType);
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
