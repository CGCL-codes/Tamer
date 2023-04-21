package net.sourceforge.code2uml.unitdata;

import java.io.ObjectStreamClass;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import net.sourceforge.code2uml.inspectors.FileInspector;
import net.sourceforge.code2uml.inspectors.InspectorsFactory;
import net.sourceforge.code2uml.inspectors.InspectorsFactoryImpl;
import net.sourceforge.code2uml.util.ProgressData;

/**
 * This class is responsible for getting information about classes/interfaces/enums
 * defined in given files. It allows adding Observers to it and notifies them
 * about progress of reading files.
 *
 * @author Mateusz Wenus
 */
class UnitsRetrieverImpl extends Observable implements UnitsRetriever, Observer {

    private InspectorsFactory factory = new InspectorsFactoryImpl();

    private double progress = 0.0;

    /**
     * Counts classes/interfaces/enums (during execution of retrieve()) or
     * qualified names (during execution of retrieveNames()) that has beeen
     * read from files.
     */
    private int readCount = 0;

    /**
     * Creates an instance of UnitsRetrieverImpl
     */
    public UnitsRetrieverImpl() {
    }

    /**
     * Gets objects representing classes/interfaces/enums defined in given files.
     * Notifies its observers about progress of processing files from
     * <code>filePaths</code>. When it notifies its observers its sets
     * notification argument to a ProgressData instance.
     *
     * @param filePaths paths of files to read
     * @return objects representing classes/interfaces/enums defined in given files
     */
    public Collection<UnitInfo> retrieve(Collection<String> filePaths) {
        return retrieve(filePaths, null);
    }

    /**
     * Returns from given files objects representing classes/interfaces/enums
     * which qualified names are in <code>namesFilter</code>.
     * Must not return two or more UnitInfos u1 and u2 such that
     * <code>u1.getName().equals(u2.getName())</code>. Should notify its observers
     * about progress of processing files from <code>filePaths</code>. When it
     * notifies its observers its sets notification argument to ProgressData
     * instance.
     *
     * @param filePaths paths of files to read
     * @param namesFilter qualified names of classes/interfaces/enums that are
     *        allowed to be returned; if it is null then all classes/interfaces/enums
     *        are returned
     * @return objects representing classes/interfaces/enums defined in given files
     */
    public Collection<UnitInfo> retrieve(Collection<String> filePaths, Collection<String> namesFilter) {
        Map<String, UnitInfo> resultMap = new HashMap<String, UnitInfo>();
        progress = 0.0;
        readCount = 0;
        for (String filePath : filePaths) {
            int idx = filePath.lastIndexOf('.');
            FileInspector inspector = factory.getInspector(filePath.substring(idx + 1));
            if (inspector instanceof Observable) ((Observable) inspector).addObserver(this);
            Collection<UnitInfo> units = inspector.inspect(filePath, namesFilter);
            if (units != null) {
                for (UnitInfo unit : units) {
                    boolean includeInResult = (namesFilter != null) && (namesFilter.contains(unit.getName()));
                    if (!includeInResult) continue;
                    UnitInfo existing = resultMap.get(unit.getName());
                    if (existing == null) {
                        resultMap.put(unit.getName(), unit);
                    } else {
                        if (existing.isPartial() && unit.isPartial()) existing.merge(unit);
                    }
                }
            }
            progress += 100.0 / filePaths.size();
            readCount = resultMap.size();
            setChanged();
            notifyObservers(new ProgressData(progress, "found " + readCount + " units"));
            clearChanged();
        }
        return resultMap.values();
    }

    /**
     * Returns qualified names of classes/interfaces/enums defined in given
     * files. Notifies its observers about progress of processing files using
     * ProgressData instance as notification argument.
     *
     * @param filePaths paths to files to read
     * @return qualified names of classes/interfaces/enums defined in given
     *         files
     */
    public Collection<String> retrieveNames(Collection<String> filePaths) {
        progress = 0.0;
        Set<String> result = new HashSet<String>();
        for (String filePath : filePaths) {
            FileInspector inspector = factory.getInspector(filePath.substring(filePath.lastIndexOf('.') + 1));
            if (inspector instanceof Observable) ((Observable) inspector).addObserver(this);
            Collection<String> names = inspector.glance(filePath);
            if (names != null) {
                for (String name : names) {
                    result.add(name);
                }
            }
            progress += 100.0 / filePaths.size();
            readCount = result.size();
            setChanged();
            notifyObservers(new ProgressData(progress, "found " + readCount + " units"));
            clearChanged();
        }
        return result;
    }

    /**
     * Called by underlying FileInspector when it reads a file. The <code>
     * arg</code> is number of classes or qualified names found by that
     * FileInspector so far.
     *
     * @param o observable whose state has changed
     * @param arg notification argument
     */
    public void update(Observable o, Object arg) {
        if (o instanceof FileInspector) {
            int count = (Integer) arg;
            setChanged();
            notifyObservers(new ProgressData(progress, "found " + (readCount + count) + " units"));
            clearChanged();
        }
    }
}
