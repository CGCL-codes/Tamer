package maltcms.experimental.bipace.peakCliqueAlignment.io;

import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tuple.TupleND;
import cross.datastructures.workflow.DefaultWorkflowResult;
import cross.datastructures.workflow.IWorkflow;
import cross.datastructures.workflow.IWorkflowElement;
import cross.datastructures.workflow.WorkflowSlot;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import maltcms.datastructures.peak.Peak;
import maltcms.experimental.bipace.datastructures.api.Clique;
import maltcms.experimental.bipace.peakCliqueAlignment.CenterFinder;
import maltcms.tools.ArrayTools;
import org.jdom.Element;
import ucar.ma2.ArrayChar;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;

/**
 *
 * @author Nils.Hoffmann@cebitec.uni-bielefeld.de
 */
@Slf4j
@Data
public class AnchorExporter<T extends Peak> implements IWorkflowElement {

    private String anchorNames;

    private String anchorTimes;

    private String anchorRetentionIndex;

    private String anchorScanIndex;

    private String ticPeaks;

    private String binnedIntensities;

    private String binnedScanIndex;

    private String scanIndex;

    private String massValues;

    private String intensityValues;

    private String scanAcquisitionTime;

    private IWorkflow workflow;

    /**
     * FIXME this method seems to create correct scan indices only for the first
     * two files.
     *
     * @param al
     * @param newFragments
     * @param cliques
     * @param peakToClique
     * @return
     */
    public TupleND<IFileFragment> addAnchors(final List<List<Peak>> al, final TupleND<IFileFragment> newFragments, final List<Clique<T>> cliques, final HashMap<Peak, Clique<T>> peakToClique) {
        final String ri_names = this.anchorNames;
        final String ri_times = this.anchorTimes;
        final String ri_indices = this.anchorRetentionIndex;
        final String ri_scans = this.anchorScanIndex;
        CenterFinder cf = new CenterFinder();
        cf.setWorkflow(getWorkflow());
        cf.findCenter(newFragments, cliques);
        final HashMap<String, IFileFragment> hm = new HashMap<String, IFileFragment>();
        log.info("Preparing files!");
        for (final IFileFragment ff : newFragments) {
            log.debug("Source files of fragment {}: {}", ff.getAbsolutePath(), ff.getSourceFiles());
            hm.put(ff.getName(), ff);
            final int size = cf.getNumberOfPeaksWithinCliques(ff, cliques);
            if (size > 0) {
                final ArrayInt.D1 anchors = new ArrayInt.D1(size);
                ArrayTools.fillArray(anchors, Integer.valueOf(-1));
                final IVariableFragment ri = ff.hasChild(ri_scans) ? ff.getChild(ri_scans) : new VariableFragment(ff, ri_scans);
                final ArrayDouble.D1 anchorTimes = new ArrayDouble.D1(size);
                ArrayTools.fillArray(anchorTimes, Double.valueOf(-1));
                final IVariableFragment riTimes = ff.hasChild(ri_times) ? ff.getChild(ri_times) : new VariableFragment(ff, ri_times);
                final ArrayChar.D2 names = cross.datastructures.tools.ArrayTools.createStringArray(size, 256);
                final IVariableFragment riNames = ff.hasChild(ri_names) ? ff.getChild(ri_names) : new VariableFragment(ff, ri_names);
                final ArrayDouble.D1 rindexA = new ArrayDouble.D1(size);
                final IVariableFragment rindex = ff.hasChild(ri_indices) ? ff.getChild(ri_indices) : new VariableFragment(ff, ri_indices);
                ri.setArray(anchors);
                riNames.setArray(names);
                riTimes.setArray(anchorTimes);
                rindex.setArray(rindexA);
            }
        }
        int id = 0;
        int[] nextIndex = new int[newFragments.size()];
        HashMap<String, Integer> placeMap = new HashMap<String, Integer>();
        int cnt = 0;
        for (IFileFragment f : newFragments) {
            placeMap.put(f.getName(), cnt++);
        }
        log.info("Setting anchors!");
        for (final Clique<T> c : cliques) {
            for (final Peak p : c.getPeakList()) {
                final IFileFragment association = hm.get(p.getAssociation());
                int slot = placeMap.get(association.getName());
                if (peakToClique.containsKey(p)) {
                    id = nextIndex[slot];
                    long cid = peakToClique.get(p).getID();
                    log.debug("Adding anchor at scan index {} and rt {} with id {} to file {}", new Object[] { p.getScanIndex(), p.getScanAcquisitionTime(), cid, association.getName() });
                    String name = "A" + cid;
                    if (!(p.getName().equals(""))) {
                        name = name + "_" + p.getName();
                    }
                    ((ArrayChar.D2) association.getChild(ri_names).getArray()).setString(id, name);
                    ((ArrayInt.D1) association.getChild(ri_scans).getArray()).set(id, p.getScanIndex());
                    ((ArrayDouble.D1) association.getChild(ri_times).getArray()).set(id, p.getScanAcquisitionTime());
                    nextIndex[slot]++;
                }
            }
            id++;
        }
        for (final IFileFragment ff : newFragments) {
            if (ff.hasChild(this.ticPeaks)) {
                ff.removeChild(ff.getChild(this.ticPeaks));
            }
            if (ff.hasChild(this.binnedIntensities)) {
                ff.removeChild(ff.getChild(this.binnedIntensities));
            }
            if (ff.hasChild(this.binnedScanIndex)) {
                ff.removeChild(ff.getChild(this.binnedScanIndex));
            }
            if (ff.hasChild(this.scanIndex)) {
                ff.removeChild(ff.getChild(this.scanIndex));
            }
            if (ff.hasChild(this.massValues)) {
                ff.removeChild(ff.getChild(this.massValues));
            }
            if (ff.hasChild(this.intensityValues)) {
                ff.removeChild(ff.getChild(this.intensityValues));
            }
            ff.getChild(this.scanAcquisitionTime).getArray();
            final DefaultWorkflowResult dwr = new DefaultWorkflowResult(new File(ff.getAbsolutePath()), this, getWorkflowSlot(), ff);
            getWorkflow().append(dwr);
            ff.save();
        }
        return newFragments;
    }

    @Override
    public WorkflowSlot getWorkflowSlot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void appendXML(Element e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
