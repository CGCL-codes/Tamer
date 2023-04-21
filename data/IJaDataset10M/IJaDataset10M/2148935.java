package maltcms.commands.fragments2d.preprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import maltcms.datastructures.caches.IScanLine;
import maltcms.datastructures.caches.ScanLineCache;
import maltcms.datastructures.caches.ScanLineCacheFactory;
import maltcms.tools.ArrayTools2;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import ucar.ma2.Array;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.ma2.IndexIterator;
import cross.Factory;
import cross.Logging;
import cross.annotations.Configurable;
import cross.annotations.ProvidesVariables;
import cross.annotations.RequiresOptionalVariables;
import cross.annotations.RequiresVariables;
import cross.commands.fragments.AFragmentCommand;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.IVariableFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.datastructures.tuple.Tuple2D;
import cross.datastructures.tuple.TupleND;
import cross.datastructures.workflow.DefaultWorkflowResult;
import cross.datastructures.workflow.WorkflowSlot;
import cross.tools.StringTools;

/**
 * Will iterate over all mass spectra and calculates mean and variance.
 * 
 * @author Mathias Wilhelm(mwilhelm A T TechFak.Uni-Bielefeld.DE)
 */
@RequiresVariables(names = { "var.modulation_time", "var.scan_rate" })
@RequiresOptionalVariables(names = { "" })
@ProvidesVariables(names = { "var.mean_ms_intensity", "var.var_ms_intensity", "var.sd_ms_intensity", "var.v_total_intensity", "var.v_mass_values", "var.v_total_intensity_1d", "var.meanms_1d_horizontal_index", "var.meanms_1d_horizontal", "var.meanms_1d_vertical_index", "var.meanms_1d_vertical", "var.maxms_1d_horizontal_index", "var.maxms_1d_horizontal", "var.used_mass_values", "var.maxms_1d_vertical_index", "var.maxms_1d_vertical" })
public class MeanVarProducer extends AFragmentCommand {

    private final Logger log = Logging.getLogger(this);

    @Configurable(name = "var.mean_intensity_values", value = "mean_intensity_values")
    private String meanMSIntensityVar = "mean_intensity_values";

    @Configurable(name = "var.var_intensity_values", value = "var_intensity_values")
    private String varMSIntensityVar = "var_intensity_values";

    @Configurable(name = "var.sd_intensity_values", value = "sd_intensity_values")
    private String sdMSIntensityVar = "sd_intensity_values";

    @Configurable(name = "var.v_total_intensity", value = "v_total_intensity")
    private String vIntensityVar = "v_total_intensity";

    @Configurable(name = "var.v_total_intensity_1d", value = "v_total_intensity_1d")
    private String vIntensity1DVar = "v_total_intensity_1d";

    @Configurable(name = "var.meanms_1d_horizontal", value = "meanms_1d_horizontal")
    private String meanMSHorizontalVar = "meanms_1d_horizontal";

    @Configurable(name = "var.meanms_1d_horizontal_index", value = "meanms_1d_horizontal_index")
    private String meanMSHorizontalIndexVar = "meanms_1d_horizontal_index";

    @Configurable(name = "var.meanms_1d_vertical", value = "meanms_1d_vertical")
    private String meanMSVerticalVar = "meanms_1d_vertical";

    @Configurable(name = "var.meanms_1d_vertical_index", value = "meanms_1d_vertical_index")
    private String meanMSVerticalIndexVar = "meanms_1d_vertical_index";

    @Configurable(name = "var.maxms_1d_horizontal", value = "maxms_1d_horizontal")
    private String maxMSHorizontalVar = "maxms_1d_horizontal";

    @Configurable(name = "var.maxms_1d_horizontal_index", value = "maxms_1d_horizontal_index")
    private String maxMSHorizontalIndexVar = "maxms_1d_horizontal_index";

    @Configurable(name = "var.maxms_1d_vertical", value = "maxms_1d_vertical")
    private String maxMSVerticalVar = "maxms_1d_vertical";

    @Configurable(name = "var.maxms_1d_vertical_index", value = "maxms_1d_vertical_index")
    private String maxMSVerticalIndexVar = "maxms_1d_vertical_index";

    @Configurable(name = "var.used_mass_values", value = "used_mass_values")
    private String usedMassValuesVar = "used_mass_values";

    @Configurable(value = "0.01d")
    private double minStandardDeviationQuantil = 0.01d;

    @Configurable(value = "-1.0d")
    private double minStandardDeviation = -1.0d;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public TupleND<IFileFragment> apply(final TupleND<IFileFragment> t) {
        final ArrayList<IFileFragment> ret = new ArrayList<IFileFragment>();
        for (final IFileFragment ff : t) {
            this.log.info("Computing mean and std for {}", StringTools.removeFileExt(ff.getName()));
            final IFileFragment fret = Factory.getInstance().getFileFragmentFactory().create(new File(getIWorkflow().getOutputDirectory(this), ff.getName()));
            fret.addSourceFile(ff);
            final IScanLine slc = ScanLineCacheFactory.getSparseScanLineCache(ff);
            slc.setCacheModulations(false);
            this.log.info("Calculating {} and {}", this.meanMSIntensityVar, this.varMSIntensityVar);
            long start = System.currentTimeMillis();
            final Tuple2D<ArrayDouble.D1, ArrayDouble.D1> tuple = calculateMeanVarSparse(slc);
            this.log.info("			{}", System.currentTimeMillis() - start);
            final ArrayDouble.D1 mean = tuple.getFirst();
            final ArrayDouble.D1 var = tuple.getSecond();
            final ArrayDouble.D1 sd = calculateSd(var, slc);
            if (this.minStandardDeviationQuantil >= 0.0d) {
                this.minStandardDeviation = ArrayTools2.getQuantilValue(ff, sd, new double[] { this.minStandardDeviationQuantil }, true, this)[0];
                this.log.info("Computing {} (using standard deviation minimum {}[" + (this.minStandardDeviationQuantil * 100.0d) + "%])", this.vIntensityVar, this.minStandardDeviation);
            } else {
                this.log.info("Computing {} (using fixed minimal standard deviation {})", this.vIntensityVar, this.minStandardDeviation);
            }
            final Tuple2D<ArrayDouble.D1, ArrayDouble.D1> intensities = computeNewIntensity(sd, slc, fret);
            final IVariableFragment meanV = new VariableFragment(fret, this.meanMSIntensityVar);
            meanV.setArray(mean);
            final IVariableFragment varV = new VariableFragment(fret, this.varMSIntensityVar);
            varV.setArray(var);
            final IVariableFragment sdV = new VariableFragment(fret, this.sdMSIntensityVar);
            sdV.setArray(sd);
            final IVariableFragment intV = new VariableFragment(fret, this.vIntensityVar);
            intV.setArray(intensities.getFirst());
            final IVariableFragment int1DV = new VariableFragment(fret, this.vIntensity1DVar);
            int1DV.setArray(intensities.getSecond());
            slc.showStat();
            slc.setCacheModulations(true);
            final DefaultWorkflowResult dwr = new DefaultWorkflowResult(new File(fret.getAbsolutePath()), this, getWorkflowSlot(), ff);
            getIWorkflow().append(dwr);
            fret.save();
            ret.add(fret);
        }
        return new TupleND<IFileFragment>(ret);
    }

    /**
	 * Creates an {@link Array} containing the standard deviation.
	 * 
	 * @param var
	 *            variance
	 * @param slc
	 *            scan line chache
	 * @return array with standard deviation
	 */
    private ArrayDouble.D1 calculateSd(final ArrayDouble.D1 var, final IScanLine slc) {
        final ArrayDouble.D1 sd = new ArrayDouble.D1(slc.getBinsSize());
        final IndexIterator varIter = var.getIndexIterator();
        final IndexIterator sdIter = sd.getIndexIterator();
        while (varIter.hasNext() && sdIter.hasNext()) {
            sdIter.setDoubleNext(Math.sqrt(varIter.getDoubleNext()));
        }
        return sd;
    }

    /**
	 * Computes a new total_intensity containing only mzs with max variance.
	 * 
	 * @param sd
	 *            array containing the standard deviation
	 * @param slc
	 *            scan line cache
	 * @param fret
	 *            file fragment
	 * @return v_total_intensity
	 */
    private Tuple2D<ArrayDouble.D1, ArrayDouble.D1> computeNewIntensity(final ArrayDouble.D1 sd, final IScanLine slc, final IFileFragment fret) {
        final ArrayDouble.D1 intensities = new ArrayDouble.D1(slc.getLastIndex());
        final ArrayDouble.D1 intensities1D = new ArrayDouble.D1(slc.getScanLineCount());
        final Vector<Integer> holdVector = new Vector<Integer>();
        final IndexIterator iiter = intensities.getIndexIterator();
        final List<Array> hMeanMs = new ArrayList<Array>();
        final List<Array> vMeanMs = new ArrayList<Array>();
        final List<Array> hMaxMs = new ArrayList<Array>();
        final List<Array> vMaxMs = new ArrayList<Array>();
        final ArrayDouble.D1 nullScan = new ArrayDouble.D1(slc.getBinsSize());
        for (int i = 0; i < slc.getScansPerModulation(); i++) {
            vMeanMs.add(nullScan.copy());
            vMaxMs.add(nullScan.copy());
        }
        for (int i = 0; i < slc.getScanLineCount(); i++) {
            hMeanMs.add(nullScan.copy());
            hMaxMs.add(nullScan.copy());
        }
        final StringBuffer sb = new StringBuffer("");
        double max = 0;
        for (int i = 0; i < slc.getBinsSize(); i++) {
            if (sd.get(i) > this.minStandardDeviation) {
                sb.append(i + ", ");
                holdVector.add(i);
            }
            if (sd.get(i) > max) {
                max = sd.get(i);
            }
        }
        this.log.info("Using mz: {}", sb);
        final IndexIterator iter1D = intensities1D.getIndexIterator();
        for (int i = 0; i < slc.getScanLineCount(); i++) {
            if (i % 100 == 0) {
                this.log.info("scanline {}", i);
            }
            final List<Array> scanline = slc.getScanlineMS(i);
            double sum1D = 0;
            Array ms = null;
            for (int j = 0; j < scanline.size(); j++) {
                ms = scanline.get(j).copy();
                hMeanMs.set(i, maltcms.tools.ArrayTools.sum(hMeanMs.get(i), ms));
                vMeanMs.set(j, maltcms.tools.ArrayTools.sum(vMeanMs.get(j), ms));
                hMaxMs.set(i, ArrayTools2.max(hMaxMs.get(i), ms));
                vMaxMs.set(j, ArrayTools2.max(vMaxMs.get(j), ms));
                final IndexIterator msIter = ms.getIndexIterator();
                double sum = 0;
                Integer counter = 0;
                while (msIter.hasNext()) {
                    final double msi = msIter.getDoubleNext();
                    if (this.minStandardDeviation == 0) {
                        sum += (sd.get(counter) / max) * msi;
                    } else {
                        if (sd.get(counter) > this.minStandardDeviation) {
                            sum += msi;
                        }
                    }
                    counter++;
                }
                iiter.setDoubleNext(sum);
                sum1D += sum;
            }
            iter1D.setDoubleNext(sum1D);
        }
        for (final Array a : vMeanMs) {
            maltcms.tools.ArrayTools.mult(a, 1.0d / slc.getScanLineCount());
        }
        for (final Array a : hMeanMs) {
            maltcms.tools.ArrayTools.mult(a, 1.0d / slc.getScansPerModulation());
        }
        final Vector<Integer> hMeanMsIndex = new Vector<Integer>();
        hMeanMsIndex.add(0);
        int index = 0;
        for (final Array a : hMeanMs) {
            index += a.getShape()[0];
            hMeanMsIndex.add(index);
        }
        hMeanMsIndex.remove(hMeanMsIndex.size() - 1);
        final Vector<Integer> hMaxMsIndex = new Vector<Integer>();
        hMaxMsIndex.add(0);
        index = 0;
        for (final Array a : hMaxMs) {
            index += a.getShape()[0];
            hMaxMsIndex.add(index);
        }
        hMaxMsIndex.remove(hMaxMsIndex.size() - 1);
        final Vector<Integer> vMeanMsIndex = new Vector<Integer>();
        vMeanMsIndex.add(0);
        index = 0;
        for (final Array a : vMeanMs) {
            index += a.getShape()[0];
            vMeanMsIndex.add(index);
        }
        vMeanMsIndex.remove(vMeanMsIndex.size() - 1);
        final Vector<Integer> vMaxMsIndex = new Vector<Integer>();
        vMaxMsIndex.add(0);
        index = 0;
        for (final Array a : vMaxMs) {
            index += a.getShape()[0];
            vMaxMsIndex.add(index);
        }
        vMaxMsIndex.remove(vMaxMsIndex.size() - 1);
        final IVariableFragment meanMSH = new VariableFragment(fret, this.meanMSHorizontalVar);
        meanMSH.setIndexedArray(hMeanMs);
        final IVariableFragment meanMSHindex = new VariableFragment(fret, this.meanMSHorizontalIndexVar);
        meanMSHindex.setArray(ArrayTools2.createIntegerArray(hMeanMsIndex));
        final IVariableFragment meanMSV = new VariableFragment(fret, this.meanMSVerticalVar);
        meanMSV.setIndexedArray(vMeanMs);
        final IVariableFragment meanMSVindex = new VariableFragment(fret, this.meanMSVerticalIndexVar);
        meanMSVindex.setArray(ArrayTools2.createIntegerArray(vMeanMsIndex));
        final IVariableFragment maxMSV = new VariableFragment(fret, this.maxMSVerticalVar);
        maxMSV.setIndexedArray(vMaxMs);
        final IVariableFragment maxMSVindex = new VariableFragment(fret, this.maxMSVerticalIndexVar);
        maxMSVindex.setArray(ArrayTools2.createIntegerArray(vMaxMsIndex));
        final IVariableFragment maxMSH = new VariableFragment(fret, this.maxMSHorizontalVar);
        maxMSH.setIndexedArray(hMaxMs);
        final IVariableFragment maxMSHindex = new VariableFragment(fret, this.maxMSHorizontalIndexVar);
        maxMSHindex.setArray(ArrayTools2.createIntegerArray(hMaxMsIndex));
        final IVariableFragment hold = new VariableFragment(fret, this.usedMassValuesVar);
        hold.setArray(ArrayTools2.createIntegerArray(holdVector));
        return new Tuple2D<ArrayDouble.D1, ArrayDouble.D1>(intensities, intensities1D);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void configure(final Configuration cfg) {
        this.minStandardDeviation = cfg.getDouble(this.getClass().getName() + ".minStandardDeviation", -1.0d);
        if (this.minStandardDeviation >= 0) {
            this.minStandardDeviationQuantil = -1.0d;
        } else {
            this.minStandardDeviationQuantil = cfg.getDouble(this.getClass().getName() + ".minStandardDeviationQuantil", 0.01d);
            while (this.minStandardDeviationQuantil > 1) {
                this.minStandardDeviationQuantil /= 100.0d;
            }
        }
        this.meanMSIntensityVar = cfg.getString("var.mean_ms_intensity", "mean_intensity_values");
        this.varMSIntensityVar = cfg.getString("var.var_ms_intensity", "var_intensity_values");
        this.sdMSIntensityVar = cfg.getString("var.sd_ms_intensity", "sd_intensity_values");
        this.vIntensityVar = cfg.getString("var.v_total_intensity", "v_total_intensity");
        this.vIntensity1DVar = cfg.getString("var.v_total_intensity_1d", "v_total_intensity_1d");
        this.meanMSHorizontalVar = cfg.getString("var.meanms_1d_horizontal", "meanms_1d_horizontal");
        this.meanMSHorizontalIndexVar = cfg.getString("var.meanms_1d_horizontal_index", "meanms_1d_horizontal_index");
        this.meanMSVerticalVar = cfg.getString("var.meanms_1d_vertical", "meanms_1d_vertical");
        this.meanMSVerticalIndexVar = cfg.getString("var.meanms_1d_vertical_index", "meanms_1d_vertical_index");
        this.maxMSHorizontalVar = cfg.getString("var.maxms_1d_horizontal", "maxms_1d_horizontal");
        this.maxMSHorizontalIndexVar = cfg.getString("var.maxms_1d_horizontal_index", "maxms_1d_horizontal_index");
        this.maxMSVerticalVar = cfg.getString("var.maxms_1d_vertical", "maxms_1d_vertical");
        this.maxMSVerticalIndexVar = cfg.getString("var.maxms_1d_vertical_index", "maxms_1d_vertical_index");
        this.usedMassValuesVar = cfg.getString("var.used_mass_values", "used_mass_values");
    }

    /**
	 * Estimates the mean and variance by an online algorithm.
	 * 
	 * @param slc
	 *            {@link ScanLineCache}
	 * @return tuple containing mean as first and variance as second element
	 */
    private Tuple2D<ArrayDouble.D1, ArrayDouble.D1> calculateMeanVarSparse(final IScanLine slc) {
        final ArrayDouble.D1 mean = new ArrayDouble.D1(slc.getBinsSize());
        final ArrayDouble.D1 var = new ArrayDouble.D1(slc.getBinsSize());
        final ArrayInt.D1 nn = new ArrayInt.D1(slc.getBinsSize());
        int n = 0;
        double delta = 0, meanCurrent = 0, x = 0, v = 0;
        double inten;
        int mass;
        for (int i = 0; i < slc.getScanLineCount(); i++) {
            if (i % 100 == 0) {
                this.log.info("scanline {}", i);
            }
            final List<Tuple2D<Array, Array>> scanline = slc.getScanlineSparseMS(i);
            for (final Tuple2D<Array, Array> ms : scanline) {
                final IndexIterator massIter = ms.getFirst().getIndexIterator();
                final IndexIterator intenIter = ms.getSecond().getIndexIterator();
                while (massIter.hasNext() && intenIter.hasNext()) {
                    inten = intenIter.getDoubleNext();
                    mass = massIter.getIntNext();
                    n = nn.get(mass) + 1;
                    nn.set(mass, n);
                    meanCurrent = mean.get(mass);
                    x = inten;
                    delta = x - meanCurrent;
                    meanCurrent = meanCurrent + delta / n;
                    mean.set(mass, meanCurrent);
                    v = var.get(mass);
                    var.set(mass, v + delta * (x - meanCurrent));
                }
            }
        }
        final IndexIterator iter = var.getIndexIterator();
        final IndexIterator niter = nn.getIndexIterator();
        double m2;
        while (iter.hasNext()) {
            m2 = iter.getDoubleNext();
            n = niter.getIntNext();
            if (n > 0) {
                iter.setDoubleCurrent(m2 / (n - 1));
            }
        }
        return new Tuple2D<ArrayDouble.D1, ArrayDouble.D1>(mean, var);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getDescription() {
        return "Produces mean and variance of a chromatogram";
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public WorkflowSlot getWorkflowSlot() {
        return WorkflowSlot.GENERAL_PREPROCESSING;
    }
}
