package br.ufmg.saotome.arangi.dto;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import br.ufmg.saotome.arangi.commons.MessageFactory;
import br.ufmg.saotome.arangi.commons.NVLHelper;
import br.ufmg.saotome.arangi.commons.StringHelper;

/**
 * Basic bean to represent a entity model and to transfer
 * data between the model tier and the controller tier.
 * Contains properties to make possible some pre built-in
 * processing codes of framework like "id", "masterId", "checkdelete" etc. 
 * @author Cesar Correia
 */
public class BasicDTO<I extends Object> implements Serializable {

    public static String NO = "N";

    public static String YES = "Y";

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Object identifier. It should match with database primary key. PK with multiple fields are supported
	 */
    private I id;

    private Long longId;

    private String stringId;

    /**
	 * Assign to a name or description database field.
	 */
    private String name;

    /**
	 * Use it to map the master id in master-detail logics in cases where 
	 * doesn't want map the master object directly;
	 */
    private Long masterId;

    /**
	 * Use it to map the master object in master-detail logics.
	 */
    private BasicDTO master;

    /**
	 * For processing. The framework uses it to delete this object in tabular and
	 * master-detail logics.
	 */
    private boolean checkDelete = false;

    /**
	 * For processing. The framework uses it to discard this object in tabular and 
	 * master-detail logics.
	 */
    private boolean discard = false;

    /**
	 * For audit logic. Last user that make changes in this object.  
	 */
    private String lastUserAudit;

    /**
	 * For audit logic. Last date that this object suffer changes.
	 */
    private Date lastDateAudit;

    /**
	 * For persistence processing logic. For concurrent store processing. 
	 */
    private int version;

    /**
	 * The security authentication object. Contais user information.
	 */
    private SecurityAuth securityAuth;

    /**
	 * Bundle to format messages
	 */
    private String bundle = "ApplicationResources";

    /**
	 * Date Mask to format dates. If the date mask is null Date Format of this class will be created from default time zone
	 */
    private String dateMask = "dd/MM/yyyy";

    /**
	 * Time zone of the user
	 */
    private TimeZone userTimeZone;

    /**
	 * Date format to format dates and times
	 */
    private DateFormat dateFormat;

    /**
	 * The user locale object.
	 */
    private Locale locale;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
        if (id instanceof Long) {
            this.longId = (Long) id;
        }
        if (id instanceof String) {
            this.stringId = (String) id;
        }
    }

    public Date getLastDateAudit() {
        return lastDateAudit;
    }

    public void setLastDateAudit(Date lastDateAudit) {
        this.lastDateAudit = lastDateAudit;
    }

    public String getLastUserAudit() {
        return lastUserAudit;
    }

    public void setLastUserAudit(String lastUserAudit) {
        this.lastUserAudit = lastUserAudit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Long getMasterId() {
        return masterId;
    }

    public void setMasterId(Long idMaster) {
        this.masterId = idMaster;
    }

    public BasicDTO getMaster() {
        return master;
    }

    public void setMaster(BasicDTO master) {
        this.master = master;
    }

    public boolean isCheckDelete() {
        return checkDelete;
    }

    public void setCheckDelete(boolean markForDelete) {
        this.checkDelete = markForDelete;
    }

    public boolean isDiscard() {
        return discard;
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    public SecurityAuth getSecurityAuth() {
        return securityAuth;
    }

    public void setSecurityAuth(SecurityAuth securityAuth) {
        this.securityAuth = securityAuth;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Character convertBooleanToChar(Boolean bool) {
        if (bool != null) {
            if (bool) {
                return 'Y';
            } else {
                return 'N';
            }
        } else {
            return null;
        }
    }

    public Boolean convertCharToBoolean(Character char1) {
        if (char1 != null) {
            if (char1.equals('1')) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return null;
        }
    }

    public String convertBooleanToString(Boolean bool) {
        if (bool != null) {
            if (bool) {
                return StringHelper.YES;
            } else {
                return StringHelper.NO;
            }
        } else {
            return null;
        }
    }

    public Boolean convertStringToBoolean(String str) {
        if (str != null) {
            if (str.trim().equals(StringHelper.YES)) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BasicDTO)) {
            return false;
        }
        BasicDTO dto = (BasicDTO) obj;
        if (getId() != null && dto.getId() != null) {
            if (getId().equals(dto.getId())) {
                return true;
            }
        }
        return (this == obj);
    }

    @Override
    public String toString() {
        return getName();
    }

    protected String getI18NDescription(String key) {
        String desc = MessageFactory.getLocalizedMessage(bundle, key, StringHelper.EMPTY_STRING_VECTOR, locale);
        if (NVLHelper.isEmpty(desc)) {
            return key;
        }
        return desc;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDateMask() {
        return dateMask;
    }

    public void setDateMask(String dateMask) {
        this.dateMask = dateMask;
    }

    public TimeZone getUserTimeZone() {
        if (userTimeZone == null) {
            userTimeZone = TimeZone.getDefault();
        }
        return userTimeZone;
    }

    public void setUserTimeZone(TimeZone userTimeZone) {
        this.userTimeZone = userTimeZone;
    }

    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            if (dateMask != null) {
                dateFormat = new SimpleDateFormat(dateMask);
            } else {
                dateFormat = DateFormat.getDateInstance();
                if (userTimeZone != null) {
                    GregorianCalendar cal = new GregorianCalendar(userTimeZone);
                    dateFormat.setCalendar(cal);
                }
            }
        }
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void clearId() {
        this.id = null;
    }

    public Long getLongId() {
        return longId;
    }

    public void setLongId(Long longId) {
        this.longId = longId;
        setId((I) longId);
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
        setId((I) stringId);
    }
}
