package apps;

import java.io.File;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import ucar.ma2.Array;
import ucar.ma2.ArrayInt;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import cross.Factory;
import cross.Logging;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.tuple.TupleND;
import cross.tools.EvalTools;
import cross.tools.FileTools;

/**
 * Copy the named Variables given on the command line from a source file to an
 * output file in a different directory.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class FileCopy {

    public static void main(final String[] args) {
        final Logger log = Logging.getLogger(FileCopy.class);
        final Maltcms m = Maltcms.getInstance();
        log.info("Starting Maltcms");
        Factory.getInstance().configure(m.parseCommandLine(args));
        log.info("Configured ArrayFactory");
        final TupleND<IFileFragment> t = Factory.getInstance().getInputDataFactory().prepareInputData();
        final Date d = new Date();
        for (final IFileFragment f : t) {
            log.info("Reading defined Variables: {}", f.getAbsolutePath());
            final IFileFragment al = f;
            final IFileFragment fcopy = Factory.getInstance().getFileFragmentFactory().create(new File(FileTools.prependDefaultDirsWithPrefix("", FileCopy.class, d), al.getName()));
            fcopy.addSourceFile(al);
            for (final IVariableFragment vf : al) {
                log.info("Retrieving Variable {}", vf);
                final IVariableFragment toplevel = fcopy.getChild(vf.getVarname());
                if (toplevel.getIndex() != null) {
                    final List<Array> arraysI = toplevel.getIndexedArray();
                    EvalTools.notNull(arraysI, arraysI);
                } else {
                    final Array a = toplevel.getArray();
                    EvalTools.notNull(a, a);
                }
            }
            final IVariableFragment index_fragment = fcopy.getChild(Factory.getInstance().getConfiguration().getString("var.scan_index", "scan_index"));
            ArrayInt.D1 index_array;
            final Range[] index_range = index_fragment.getRange();
            try {
                index_array = (ArrayInt.D1) index_fragment.getArray().section(Range.toList(index_range));
                if ((index_range != null) && (index_range[0] != null)) {
                    final ArrayInt.D1 new_index = new ArrayInt.D1(index_array.getShape()[0]);
                    for (int i = 0; i < new_index.getShape()[0]; i++) {
                        new_index.set(i, index_array.get(i) - index_array.get(0));
                    }
                    index_fragment.setArray(new_index);
                }
            } catch (final InvalidRangeException e) {
                log.error(e.getLocalizedMessage());
            }
            log.info("{}", fcopy.toString());
            log.info("{}", FileFragment.printFragment(fcopy));
            fcopy.removeSourceFiles();
            fcopy.save();
        }
        System.exit(0);
    }
}
