package test.stub.dao;

import icescrum2.dao.IReleaseDao;
import icescrum2.dao.model.IRelease;
import icescrum2.dao.model.ISprint;
import icescrum2.dao.model.IStory;
import icescrum2.dao.model.impl.ProductBacklogItem;
import icescrum2.dao.model.impl.Release;
import icescrum2.dao.model.impl.Sprint;
import java.util.ArrayList;
import java.util.Collection;

public class SReleaseDao implements IReleaseDao {

    private static int key = 0;

    private ArrayList<IRelease> list = new ArrayList<IRelease>();

    public boolean checkUniqueName(IRelease _r, String idPb) {
        return false;
    }

    public void deleteRelease(IRelease _release) {
        this.list.remove(_release);
    }

    public IRelease getRelease(String cle) {
        for (IRelease r : this.list) {
            if (r.getIdRelease() == Integer.getInteger(cle)) return r;
        }
        return null;
    }

    public Double pointsForOneRelease(IRelease r, boolean separateNewItems) {
        Double totalEstimated = new Double(0);
        for (ISprint s : ((Collection<Sprint>) r.getSprints())) {
            for (IStory pbi : ((Collection<ProductBacklogItem>) s.getProductBacklogItems())) {
                totalEstimated += pbi.getEstimatedPoints();
            }
        }
        return totalEstimated;
    }

    public boolean saveRelease(IRelease _release, String idPb) {
        ((Release) _release).setIdRelease(key);
        key++;
        this.list.add(_release);
        return true;
    }

    public boolean updateRelease(IRelease _release, String idPb) {
        return true;
    }
}
