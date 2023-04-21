package org.aeg.services.database;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.aeg.model.Rscpi;
import org.aeg.model.RscpiSubSet;
import org.aeg.services.database.BasicDao;
import org.aeg.services.database.test.DatabaseRecordSetLoaderTest;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;

public class DatabaseRecordSetLoader extends BasicDao implements RecordSetLoader {

    public Collection<RecordSet> loadRecordSets(RscpiSubSet subset) {
        Vector<RecordSet> c = new Vector<RecordSet>();
        RecordSetIterator it = iterateRecordSets(subset);
        while (it.hasNext()) {
            c.add(it.next());
        }
        return c;
    }

    public RecordSetIterator iterateRecordSets(RscpiSubSet subset) {
        RscpiSubSetCriteria subSectCriteria = new RscpiSubSetCriteria();
        subSectCriteria.setSubSet(subset);
        DetachedCriteria detachedCriteria = subSectCriteria.createCriteria();
        Collection<Rscpi> results = getTemplate().findByCriteria(detachedCriteria);
        Iterator<Rscpi> it = results.iterator();
        return new RecordSetIterator(it);
    }
}
