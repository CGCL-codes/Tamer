package prisms.util.preferences;

import prisms.arch.PrismsApplication;
import prisms.arch.ds.User;
import prisms.records.*;

/** Allows preference values to be kept consistent between instances in an enterprise */
public class PreferencesScaleImpl implements RecordPersister, ScaleImpl {

    private PrismsApplication[] theApps;

    private PrismsApplication theNonApp;

    private PreferencesPersister thePrefs;

    /** @param persister The preference persister for this sync implementation to use */
    public void setPrefPersister(PreferencesPersister persister) {
        thePrefs = persister;
        theApps = new PrismsApplication[0];
        theNonApp = new PrismsApplication(thePrefs.getEnv(), "None", "A placeholder for preferences", new int[0], System.currentTimeMillis(), this);
    }

    /**
	 * Adds an application to the recognized set for htis
	 * 
	 * @param app The application to add
	 * @return Whether the application was added (false if the application is already recognized)
	 */
    public boolean addApp(PrismsApplication app) {
        if (!prisms.util.ArrayUtils.contains(theApps, app)) {
            theApps = prisms.util.ArrayUtils.add(theApps, app);
            return true;
        }
        return false;
    }

    private PrismsApplication getApp(long id) {
        for (PrismsApplication app : theApps) if (getID(app) == id) return app;
        return theNonApp;
    }

    private long getID(PrismsApplication app) {
        return getID(app.getName());
    }

    private long getID(String name) {
        long ret = name.hashCode() * 1000000000;
        name = new StringBuilder(name).reverse().toString();
        ret += name.hashCode();
        return ret;
    }

    public Object getDBCurrentValue(ChangeRecord record) throws PrismsRecordException {
        switch((PreferenceSubjectType) record.type.subjectType) {
            case Preference:
                switch((PreferenceSubjectType.PreferenceChange) record.type.changeType) {
                    case Value:
                        return thePrefs.getDBValue(thePrefs.getValue((PrismsApplication) record.data1, (User) record.data2), (Preference<?>) record.majorSubject);
                }
        }
        return null;
    }

    public void doMemChange(ChangeRecord record, Object currentValue) throws PrismsRecordException {
        switch((PreferenceSubjectType) record.type.subjectType) {
            case Preference:
                thePrefs.getValue((PrismsApplication) record.data1, (User) record.data2).set((Preference<Object>) record.majorSubject, currentValue, false);
                break;
        }
    }

    public SubjectType getSubjectType(String typeName) throws PrismsRecordException {
        for (PreferenceSubjectType pst : PreferenceSubjectType.values()) if (pst.name().equals(typeName)) return pst;
        throw new PrismsRecordException("No such Preferences subject type: " + typeName);
    }

    public long getID(Object item) {
        if (item instanceof PrismsApplication) return getID((PrismsApplication) item); else if (item instanceof User) return ((User) item).getID(); else if (item instanceof Preference) return ((Preference<?>) item).getID();
        return -1;
    }

    public User getUser(long id) throws PrismsRecordException {
        User ret;
        try {
            ret = thePrefs.getEnv().getUserSource().getUser(id);
        } catch (prisms.arch.PrismsException e) {
            throw new PrismsRecordException(e.getMessage(), e);
        }
        if (ret == null) throw new PrismsRecordException("No such user with ID " + id);
        return ret;
    }

    public ChangeData getData(SubjectType subjectType, ChangeType changeType, Object majorSubject, Object minorSubject, Object data1, Object data2, Object preValue) throws PrismsRecordException {
        switch((PreferenceSubjectType) subjectType) {
            case Preference:
                PrismsApplication app;
                if (data1 instanceof PrismsApplication) app = (PrismsApplication) data1; else app = getApp(((Number) data1).longValue());
                User user;
                if (data2 instanceof User) user = (User) data2; else user = getUser(((Number) data2).longValue());
                Preference<?> pref = null;
                if (majorSubject instanceof Preference) pref = (Preference<?>) majorSubject; else {
                    Preferences prefs = thePrefs.getValue(app, user);
                    String[] domains = prefs.getAllDomains();
                    for (String domain : domains) {
                        Preference<?>[] domPrefs = prefs.getAllPreferences(domain);
                        for (Preference<?> domPref : domPrefs) if (domPref.getID() == ((Number) majorSubject).longValue()) {
                            pref = domPref;
                            break;
                        }
                        if (pref != null) break;
                    }
                    if (pref == null) pref = new Preference<Object>("Nothing", "Nothing", Preference.Type.ARBITRARY, Object.class, false);
                }
                if (preValue != null) preValue = pref.getType().deserialize((String) preValue);
                return new ChangeData(pref, null, app, user, preValue);
        }
        throw new PrismsRecordException("Unrecognized subject type: " + subjectType);
    }

    public SubjectType[] getAllSubjectTypes() throws PrismsRecordException {
        return PreferenceSubjectType.values();
    }

    public SubjectType[] getHistoryDomains(Object value) throws PrismsRecordException {
        return PreferenceSubjectType.values();
    }

    public String serializePreValue(ChangeRecord change) throws PrismsRecordException {
        return ((Preference<Object>) change.majorSubject).getType().serialize(change.previousValue);
    }

    public void checkItemForDelete(Object item, java.sql.Statement stmt) throws PrismsRecordException {
    }
}
