package net.sourceforge.mededis.tests;

import net.sourceforge.mededis.central.MDException;
import net.sourceforge.mededis.central.Mededis;
import net.sourceforge.mededis.central.log.Log;
import net.sourceforge.mededis.dataaccess.UserAccess;
import net.sourceforge.mededis.dataaccess.FieldGroupAccess;
import net.sourceforge.mededis.dataaccess.FieldAccess;
import net.sourceforge.mededis.model.User;
import net.sourceforge.mededis.model.FieldGroup;
import net.sourceforge.mededis.model.Field;
import net.sourceforge.mededis.model.CodeSet;
import net.sourceforge.mededis.people.dataaccess.ResidentAccess;
import net.sourceforge.mededis.people.dataaccess.RoleAccess;
import net.sourceforge.mededis.people.model.Resident;
import java.util.List;
import java.util.Collection;

/**
 *   Load some sample data, retrieve, save
 */
public class FieldGroupTest extends junit.framework.TestCase {

    User user;

    public FieldGroupTest(String name) {
        super(name);
    }

    public void setUp() throws MDException {
        user = User.login("wendy", "wendy");
        if (user == null) {
            user = new User();
            user.setUsername("wendy");
            user.setPassword("wendy");
            UserAccess uAccess = (UserAccess) Mededis.getInstance().getDataAccess(user, User.class);
            uAccess.save(user);
        }
    }

    public void tearDown() {
    }

    public List loadSampleData(FieldGroupAccess access) throws MDException {
        FieldGroup group = new FieldGroup();
        group.setName("Resident Field Group");
        group.setRootClass(Resident.class.getName());
        FieldAccess fieldAccess = (FieldAccess) Mededis.getInstance().getDataAccess(user, Field.class);
        List fields = fieldAccess.findByRootClass(Resident.class.getName());
        Log.info(user, "Found fields: " + fields.size() + " for " + Resident.class.getName());
        group.setFields(fields);
        Log.info(user, "saving field group: " + group.getName() + " with fields: " + group.getFields().size());
        access.save(group);
        return access.findAll();
    }

    public void testBasicAccess() {
        try {
            System.out.println("Username: " + user.getUsername());
            Log.info(user, "Starting testBasicAccess in RoleTest");
            ResidentAccess resAccess = (ResidentAccess) Mededis.getInstance().getDataAccess(user, Resident.class);
            FieldGroupAccess fgAccess = (FieldGroupAccess) Mededis.getInstance().getDataAccess(user, FieldGroup.class);
            List fieldGroups = fgAccess.findAll();
            if (fieldGroups.size() == 0) fieldGroups = loadSampleData(fgAccess);
            FieldGroup resGroup = fgAccess.findByName("Resident Field Group");
            List residents = resAccess.findAll(resGroup);
            if (residents.size() == 0) {
                Log.info(user, "adding new resident");
                Resident res = new Resident();
                res.setUser(user);
                resAccess.save(res);
                Log.debug(user, "Resident; " + res.getId());
                residents = resAccess.findAll(resGroup);
            }
            Resident res = (Resident) residents.get(0);
            Log.info(user, "Found resident role for user: " + res.getUser().getUsername() + ". Resident: " + res);
        } catch (Exception e) {
            Log.error(user, "error in test", e);
            fail("Error accessing data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(FieldGroupTest.class);
    }
}
