package cross.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import cross.Factory;

/**
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 * 
 * 
 */
public class DataSourceServiceLoader {

    /**
	 * Returns the available implementations of @see{IDataSource}. Elements are
	 * sorted according to lexical order on their classnames.
	 * 
	 * @return
	 */
    public List<IDataSource> getAvailableCommands() {
        ServiceLoader<IDataSource> sl = ServiceLoader.load(IDataSource.class);
        HashSet<IDataSource> s = new HashSet<IDataSource>();
        for (IDataSource ifc : sl) {
            Factory.getInstance().getObjectFactory().configureType(ifc);
            s.add(ifc);
        }
        ArrayList<IDataSource> al = new ArrayList<IDataSource>();
        al.addAll(s);
        Collections.sort(al, new Comparator<IDataSource>() {

            @Override
            public int compare(IDataSource o1, IDataSource o2) {
                return o1.getClass().getName().compareTo(o2.getClass().getName());
            }
        });
        return al;
    }
}
