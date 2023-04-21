package demo.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import net.sf.resourcetasksch.model.Resource;
import net.sf.resourcetasksch.model.Task;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * @author Pierre
 */
public class Dept implements Resource {

    String name = "";

    List<Resource> pers;

    public Dept(String name) {
        super();
        this.name = name;
        pers = new ArrayList<Resource>();
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        List<Task> res = new ArrayList<Task>();
        if (pers.size() == 0) return res;
        Interval iv = getMaxInterval();
        Interval day;
        Iterator<Resource> it;
        Person p;
        DateTime dt = iv.getStart().toDateMidnight().toDateTime();
        int nbrPers = 0;
        while (iv.contains(dt)) {
            day = new Interval(dt, dt.plusHours(24));
            it = pers.iterator();
            while (it.hasNext()) {
                p = (Person) it.next();
                if (p.busyOn(day)) nbrPers++;
            }
            res.add(new OccupationSynthese(this, nbrPers, day));
            dt = dt.plusHours(24);
            nbrPers = 0;
        }
        return res;
    }

    public int getNbrPers() {
        return pers.size();
    }

    public boolean hasChildren() {
        return pers.size() > 0;
    }

    public List<Resource> getChildResources() {
        return pers;
    }

    public Object getValueAt(int colIndex) {
        if (colIndex == 0) return name;
        return "";
    }

    @Override
    public String toString() {
        return getName();
    }

    public Interval getMaxInterval() {
        DateTime fd = null;
        DateTime ed = null;
        Iterator<Resource> it = pers.iterator();
        Interval iv;
        while (it.hasNext()) {
            iv = ((Person) it.next()).getMaxInterval();
            if (fd == null) {
                fd = iv.getStart();
                ed = iv.getEnd();
            } else {
                if (fd.compareTo(iv.getStart()) > 0) fd = iv.getStart();
                if (ed.compareTo(iv.getEnd()) < 0) ed = iv.getEnd();
            }
        }
        return new Interval(fd.toDateMidnight().toDateTime(), ed.plusDays(1).toDateMidnight());
    }

    public Icon getIcon() {
        return null;
    }
}
