package net.sf.mzmine.modules.peaklistmethods.identification.nist;

import net.sf.mzmine.data.IonizationType;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.*;
import java.io.File;
import java.util.Collection;

/**
 * Holds NIST MS Search parameters.
 *
 * @author $Author: cpudney $
 * @version $Revision: 2369 $
 */
public class NistMsSearchParameters extends SimpleParameterSet {

    /**
     * Peak lists to operate on.
     */
    public static final PeakListsParameter PEAK_LISTS = new PeakListsParameter();

    /**
     * NIST MS Search path.
     */
    public static final DirectoryParameter NIST_MS_SEARCH_DIR = new DirectoryParameter("NIST MS Search directory", "Full path of the directory containing the NIST MS Search executable (nistms$.exe)");

    /**
     * Ionization method.
     */
    public static final ComboParameter<IonizationType> IONIZATION_METHOD = new ComboParameter<IonizationType>("Ionization method", "Type of ion used to calculate the neutral mass", IonizationType.values());

    /**
     * Spectrum RT width.
     */
    public static final DoubleParameter SPECTRUM_RT_WIDTH = new DoubleParameter("Spectrum RT tolerance", "The RT tolerance (>= 0) to use when forming search spectra; include all other detected peaks whose RT is within the specified tolerance of a given peak", MZmineCore.getConfiguration().getRTFormat(), 0.05, 0.0, null);

    /**
     * Match factor cut-off.
     */
    public static final IntegerParameter MAX_NUM_PEAKS = new IntegerParameter("Max. peaks per spectrum", "The maximum number of peaks to include in a spectrum (0 -> unlimited)", 10, 0, null);

    /**
     * Match factor cut-off.
     */
    public static final IntegerParameter MIN_MATCH_FACTOR = new IntegerParameter("Min. match factor", "The minimum match factor (0 .. 1000) that search hits must have", 800, 0, 1000);

    /**
     * Match factor cut-off.
     */
    public static final IntegerParameter MIN_REVERSE_MATCH_FACTOR = new IntegerParameter("Min. reverse match factor", "The minimum reverse match factor (0 .. 1000) that search hits must have", 800, 0, 1000);

    private static final String NIST_MS_SEARCH_EXE = "nistms$.exe";

    /**
     * Construct the parameter set.
     */
    public NistMsSearchParameters() {
        super(new Parameter[] { PEAK_LISTS, NIST_MS_SEARCH_DIR, IONIZATION_METHOD, SPECTRUM_RT_WIDTH, MAX_NUM_PEAKS, MIN_MATCH_FACTOR, MIN_REVERSE_MATCH_FACTOR });
    }

    @Override
    public boolean checkUserParameterValues(final Collection<String> errorMessages) {
        if (!isWindows()) {
        }
        boolean result = super.checkUserParameterValues(errorMessages);
        final File executable = getNistMsSearchExecutable();
        if (executable == null || !executable.exists()) {
            errorMessages.add("NIST MS Search executable (" + NIST_MS_SEARCH_EXE + ") not found.  Please set the to the full path of the directory containing the NIST MS Search executable.");
            result = false;
        }
        return result;
    }

    /**
     * Gets the full path to the NIST MS Search executable.
     *
     * @return the path.
     */
    public File getNistMsSearchExecutable() {
        final File dir = getParameter(NIST_MS_SEARCH_DIR).getValue();
        return dir == null ? null : new File(dir, NIST_MS_SEARCH_EXE);
    }

    /**
     * Is this a Windows OS?
     *
     * @return true/false if the os.name property does/doesn't contain "Windows".
     */
    private static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().contains("WINDOWS");
    }
}
