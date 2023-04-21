package org.campware.cream.modules.actions;

import org.apache.velocity.context.Context;
import org.apache.turbine.util.RunData;
import org.apache.torque.util.Criteria;
import org.campware.cream.om.Language;
import org.campware.cream.om.LanguagePeer;

/**
 * This class provides a simple set of methods to
 * insert/update/delete records in a database.
 */
public class LanguageSQL extends CreamLookupAction {

    protected void initScreen() {
        setModuleType(LOOKUP);
        setModuleName("LANGUAGE");
    }

    /**
     * This simply takes an entry from the web form and
     * inserts it directly into the database.
     *
     * This would not be good in practice as the
     * data should be verified before being allowed
     * into the database. This is merely an
     * example of how to use peers, this certainly
     * wouldn't be secure.
     */
    public void doInsert(RunData data, Context context) throws Exception {
        Language entry = new Language();
        data.getParameters().setProperties(entry);
        entry.save();
        setSavedId(entry.getPrimaryKey().toString());
    }

    /**
     * Update a record in the database with the
     * information present in the web form.
     *
     * Again, this is merely an example. The data
     * should be checked before being allowed
     * into the database.
     */
    public void doUpdate(RunData data, Context context) throws Exception {
        Language entry = new Language();
        data.getParameters().setProperties(entry);
        entry.setModified(true);
        entry.setNew(false);
        entry.save();
    }

    /**
     * Delete a record from the database using
     * the unique id gleaned from the web form.
     */
    public void doDelete(RunData data, Context context) throws Exception {
        Criteria criteria = new Criteria();
        criteria.add(LanguagePeer.LANGUAGE_ID, data.getParameters().getInt("languageid"));
        LanguagePeer.doDelete(criteria);
    }

    /**
     * Delete selected records from the database using
     * the unique ids gleaned from the web form.
     */
    public void doDeleteselected(RunData data, Context context) throws Exception {
        int[] delIds = data.getParameters().getInts("rowid");
        Criteria criteria = new Criteria();
        criteria.addIn(LanguagePeer.LANGUAGE_ID, delIds);
        LanguagePeer.doDelete(criteria);
    }
}
