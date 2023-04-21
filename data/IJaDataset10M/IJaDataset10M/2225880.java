package org.okkam.core.match.test;

import org.okkam.core.api.EntityManager;
import org.okkam.core.api.data.entityprofile.EntityProfile;
import org.okkam.core.match.query.dao.IOkkamDAO;
import org.okkam.core.match.query.dao.OkkamJDBCImpl;

public class EntityProfileManagerTest {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        EntityProfileManagerTest test = new EntityProfileManagerTest();
        test.doMain(args);
    }

    public void doMain(String[] args) {
        try {
            String uri = "http://www.okkam.org/entity/ok77ff9c2a-9d93-4b28-8236-dd93e92cac65";
            EntityManager manager = new EntityManager();
            IOkkamDAO okk = new OkkamJDBCImpl();
            EntityProfile ep = okk.getEntityProfileFromURI(uri);
            for (int i = 0; i < ep.getLabels().getLabel().size(); i++) {
                if (ep.getLabels().getLabel().get(i).getValue().compareTo("Gilbarto") == 0) {
                    ep.getLabels().getLabel().get(i).setValue("Gilburto");
                }
            }
            boolean result = manager.update(ep);
            System.out.println(result);
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        }
    }
}
