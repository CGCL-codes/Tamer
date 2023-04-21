package maltcms.commands.fragments2d.peakfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import maltcms.commands.distances.Array2DTimePenalized;
import maltcms.commands.distances.ArrayCos;
import maltcms.commands.distances.IArrayDoubleComp;
import maltcms.datastructures.peak.Peak2D;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import cross.Factory;
import cross.Logging;
import cross.annotations.Configurable;

/**
 * Will compute a list of bidirectional best hits.
 * 
 * @author Mathias Wilhelm(mwilhelm A T TechFak.Uni-Bielefeld.DE)
 */
public class FastBidirectionalBestHit implements IBidirectionalBestHit {

    private final Logger log = Logging.getLogger(this);

    @Configurable(value = "maltcms.commands.distances.ArrayCos")
    private String distClass = "maltcms.commands.distances.ArrayCos";

    @Configurable(value = "true")
    private boolean useMeanMS = true;

    @Configurable(value = "0.9d")
    private Double threshold = 0.9d;

    @Configurable(value = "25.0d")
    private double maxRetDiff = 25.0d;

    private final List<List<Peak2D>> peaklists;

    private IArrayDoubleComp dist = new ArrayCos();

    private final List<Map<Integer, Boolean>> doneList;

    private int counter = 0;

    private int fcounter = 0;

    /**
	 * Default constructor. Sets up all needed variables.
	 */
    public FastBidirectionalBestHit() {
        this.peaklists = new ArrayList<List<Peak2D>>();
        this.doneList = new ArrayList<Map<Integer, Boolean>>();
    }

    /**
	 * Adds a peak list to a internal peak list.
	 * 
	 * @param peakList
	 *            peak list
	 */
    public void addPeakLists(final List<Peak2D> peakList) {
        this.peaklists.add(peakList);
        this.doneList.add(new HashMap<Integer, Boolean>());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void configure(final Configuration cfg) {
        this.threshold = cfg.getDouble(this.getClass().getName() + ".threshold", 0.9d);
        this.useMeanMS = cfg.getBoolean(this.getClass().getName() + ".useMeanMS", true);
        this.distClass = cfg.getString(this.getClass().getName() + ".dist", "maltcms.commands.distances.ArrayCos");
        this.dist = Factory.getInstance().getObjectFactory().instantiate(Array2DTimePenalized.class);
        this.maxRetDiff = cfg.getDouble(this.getClass().getName() + ".maxRetDiff", 500.0d);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public double sim(Peak2D p, Peak2D np) {
        double sim, diffrt1, diffrt2;
        diffrt1 = Math.abs(p.getFirstRetTime() - np.getFirstRetTime());
        diffrt2 = Math.abs(p.getSecondRetTime() - np.getSecondRetTime());
        if (this.useMeanMS) {
            sim = this.dist.apply(0, 0, diffrt1, diffrt2, p.getPeakArea().getMeanMS(), np.getPeakArea().getMeanMS());
        } else {
            sim = this.dist.apply(0, 0, diffrt1, diffrt2, p.getPeakArea().getSeedMS(), np.getPeakArea().getSeedMS());
        }
        return sim;
    }

    /**
	 * Will find the best hit in list of peak p.
	 * 
	 * @param p
	 *            peak
	 * @param list
	 *            list
	 * @return <code>-1</code> if no one was found
	 */
    private int findBidiBestHist(final Peak2D p, final List<Peak2D> list) {
        int maxI = -1;
        double max;
        if (this.dist.minimize()) {
            max = Double.MAX_VALUE;
        } else {
            max = Double.MIN_VALUE;
        }
        double sim;
        Peak2D np;
        for (int i = 0; i < list.size(); i++) {
            np = list.get(i);
            if (Math.abs(p.getFirstRetTime() - np.getFirstRetTime()) < this.maxRetDiff) {
                this.counter++;
                sim = sim(p, np);
                if (this.dist.minimize()) {
                    if (sim < max) {
                        maxI = i;
                        max = sim;
                    }
                } else {
                    if (sim > max) {
                        maxI = i;
                        max = sim;
                    }
                }
            } else {
                this.fcounter++;
            }
        }
        if (this.threshold != 0) {
            if (this.dist.minimize()) {
                if (max > this.threshold) {
                    return -1;
                }
            } else {
                if (max < this.threshold) {
                    return -1;
                }
            }
        }
        return maxI;
    }

    /**
	 * Getter.
	 * 
	 * @return a list of all bidirectional best hits. List contains the indices
	 *         of peak in the peaklist.
	 */
    public List<List<Point>> getBidiBestHitList() {
        this.log.info("Dist: {}", this.dist.getClass().getName());
        this.log.info("Threshold: {}", this.threshold);
        this.log.info("Use mean MS: {}", this.useMeanMS);
        this.log.info("peaklistsize {}:", this.peaklists.size());
        for (List<Peak2D> l : this.peaklists) {
            this.log.info("	{}", l.size());
        }
        final List<List<Point>> indexList = new ArrayList<List<Point>>();
        List<Point> bidibestlist = new ArrayList<Point>();
        int ii;
        for (int h = 0; h < this.peaklists.size() - 1; h++) {
            for (int i = 0; i < this.peaklists.get(h).size(); i++) {
                if (!this.doneList.get(h).containsKey(i)) {
                    int r = h + 1;
                    int l = h;
                    for (int j = 0; j < h; j++) {
                        bidibestlist.add(new Point(-1, j));
                    }
                    bidibestlist.add(new Point(i, l));
                    ii = i;
                    while (true) {
                        final int bidibestr = findBidiBestHist(this.peaklists.get(l).get(ii), this.peaklists.get(r));
                        if (bidibestr != -1) {
                            final int bidibestl = findBidiBestHist(this.peaklists.get(r).get(bidibestr), this.peaklists.get(l));
                            if (bidibestl == ii) {
                                bidibestlist.add(new Point(bidibestr, r));
                                this.doneList.get(l).put(ii, true);
                                this.doneList.get(r).put(bidibestr, true);
                                l = r;
                                r++;
                                ii = bidibestr;
                            } else {
                                bidibestlist.add(new Point(-1, r));
                                r++;
                            }
                        } else {
                            bidibestlist.add(new Point(-1, r));
                            r++;
                        }
                        if (r == this.peaklists.size()) {
                            break;
                        }
                    }
                    indexList.add(bidibestlist);
                    bidibestlist = new ArrayList<Point>();
                }
            }
        }
        final int lastIndex = this.peaklists.size() - 1;
        for (int i = 0; i < this.peaklists.get(lastIndex).size(); i++) {
            if (!this.doneList.get(lastIndex).containsKey(i)) {
                bidibestlist = new ArrayList<Point>();
                for (int j = 0; j < lastIndex; j++) {
                    bidibestlist.add(new Point(-1, j));
                }
                bidibestlist.add(new Point(i, lastIndex));
                indexList.add(bidibestlist);
            }
        }
        this.log.info("Did: {}", this.counter);
        this.log.info("Skipped: {}", this.fcounter);
        return indexList;
    }

    /**
	 * Getter.
	 * 
	 * @return peak list
	 */
    public List<List<Peak2D>> getPeakLists() {
        return this.peaklists;
    }
}
