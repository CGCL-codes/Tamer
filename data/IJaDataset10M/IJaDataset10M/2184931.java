package cross.io.misc;

import java.io.File;
import org.jdom.Element;
import org.slf4j.Logger;
import cross.Factory;
import cross.Logging;
import cross.datastructures.StatsMap;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.workflow.IWorkflow;
import cross.datastructures.workflow.IWorkflowElement;
import cross.datastructures.workflow.WorkflowSlot;
import cross.io.csv.CSVWriter;
import cross.tools.StringTools;

/**
 * Writes StatsMap objects to CSV Files.
 * 
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 * 
 */
public class StatsWriter implements IWorkflowElement {

    private IWorkflow iwf;

    @Override
    public void appendXML(final Element e) {
    }

    @Override
    public IWorkflow getIWorkflow() {
        return this.iwf;
    }

    @Override
    public WorkflowSlot getWorkflowSlot() {
        return WorkflowSlot.STATISTICS;
    }

    @Override
    public void setIWorkflow(final IWorkflow iw) {
        this.iwf = iw;
    }

    public void write(final IFileFragment ff) {
        final StatsMap sm = ff.getStats();
        write(sm);
        for (final IVariableFragment vf : ff) {
            write(vf.getStats());
        }
    }

    public void write(final StatsMap... sm) {
        final Logger log = Logging.getLogger(StatsWriter.class);
        if (sm != null) {
            log.info("Writing " + sm.length + " statsMaps to file!");
            for (final StatsMap map : sm) {
                if (map != null) {
                    final IFragment f = map.getAssociation();
                    final CSVWriter csvw = Factory.getInstance().instantiate(CSVWriter.class);
                    csvw.setIWorkflow(getIWorkflow());
                    if (f instanceof IVariableFragment) {
                        final IFileFragment parent = ((IVariableFragment) f).getParent();
                        final String basename = StringTools.removeFileExt(parent.getAbsolutePath());
                        final String path = new File(parent.getAbsolutePath()).getParent();
                        csvw.writeStatsMap(path, basename + "-" + ((IVariableFragment) f).getVarname() + ".csv", map);
                    } else if (f instanceof IFileFragment) {
                        csvw.writeStatsMap((IFileFragment) f, map);
                    }
                }
            }
        } else {
            log.warn("StatsMap[] was null!");
        }
    }
}
