package mzmatch.ipeak;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import cmdline.*;
import peakml.*;
import peakml.chemistry.*;
import peakml.io.*;
import peakml.io.peakml.*;
import mzmatch.util.*;

public class ExtractBackgroundIons {

    /**
	 * 
	 * @param cms
	 * @param ppm
	 * @param threshold
	 * @return
	 */
    public static Vector<BackgroundIon<? extends Peak>> extractBackgroundIons(ChromatographyMS<Peak> cms, double ppm, double threshold) {
        Vector<BackgroundIon<? extends Peak>> backgroundions = new Vector<BackgroundIon<? extends Peak>>();
        int minnrscans = (int) (threshold * cms.getNrScans());
        IPeakSet<Peak> spectrum = new IPeakSet<Peak>(cms.getAllPeaks());
        for (int pos = 0; pos < spectrum.size(); ++pos) {
            double mass = spectrum.get(pos).getMass();
            double distance = PeriodicTable.PPM(mass, ppm);
            Vector<Peak> group = spectrum.getPeaksInMassRange(mass, mass + distance);
            int bestpos = pos;
            for (int i = 1; i < group.size(); ++i) {
                double thismass = group.get(i).getMass();
                double thisdistance = PeriodicTable.PPM(thismass, ppm);
                Vector<Peak> thisgroup = spectrum.getPeaksInMassRange(thismass, thismass + thisdistance);
                if (thisgroup.size() > group.size()) bestpos = pos + i;
            }
            if (bestpos != pos) {
                pos = bestpos - 1;
                continue;
            } else pos = pos + group.size() - 1;
            if (group.size() >= minnrscans) {
                PeakData<Centroid> peakdata = new PeakData<Centroid>(Centroid.factory, group.size());
                Collections.sort(group, IPeak.sort_retentiontime_ascending);
                for (int i = 0; i < group.size(); ++i) {
                    Peak p = group.elementAt(i);
                    peakdata.set(i, p.getScanID(), p.getRetentionTime(), p.getMass(), p.getIntensity());
                    peakdata.setMeasurementID(i, p.getMeasurementID());
                }
                backgroundions.add(new BackgroundIon<Centroid>(peakdata));
            }
        }
        return backgroundions;
    }

    static final String version = "1.0.0";

    static final String application = "ExtractBackgroundIons";

    @OptionsClass(name = application, version = version, author = "RA Scheltema (r.a.scheltema@rug.nl)", description = "Extracts background ions (x-axis: RT; y-axis: Intensity) from 2D mass spectrometry data " + "(LC/MS or GC/MS). The raw data is loaded from the open standard file formats (mzML, " + "mzXML or mzData) and all of the individidual mass traces (M/Z +/- ppm over the whole scan " + "range) are retrieved. A mass trace is retained when it is present in more than the given " + "percentage of scans (option 'threshold')." + "\n\n" + "The resulting output file is in PeakML-format, containing a list of all the extracted " + "background ions." + "\n\nRemarks\n" + "1. At this time only centroid data is supported.\n" + "2. NetCDF is not supported as it misses necessary meta-information\n" + "3. Direct injection data will not yield correct results\n", example = "Windows batch-file:\n" + "SET JAVA=java -cp mzmatch.jar -da -dsa -Xmn1g -Xms1425m -Xmx1425m -Xss128k -XX:+UseParallelGC -XX:ParallelGCThreads=10\n" + "\n" + "REM process a single file\n" + "%JAVA% mzmatch.ipeak.ExtractBackgroundIons -v -ppm 3 -threshold 0.18 -i file.mzXML -o file.peakml\n")
    public static class Options {

        @Option(name = "i", param = "filename", type = Option.Type.REQUIRED_ARGUMENT, usage = "Option for the input files, which should be in one of the open standard file formats " + "(mzML, mzXML or mzData) and contain data from a 2D mass spectrometry setup (LC/MS or " + "GC/MS)." + "\n" + "When this option has not been set, the input is read from the stdin (allowing for " + "pipeline building). When a single input file is defined, the output '-o' should " + "contain the output filename. When multiple input files are defined, the output " + "'-o' should define an output directory." + "\n" + "For now only centroid input data is supported.")
        public Vector<String> input = new Vector<String>();

        @Option(name = "o", param = "filename", type = Option.Type.REQUIRED_ARGUMENT, usage = "Option for the ouput file(s); refer to the option input '-i' for a description of " + "behaviours with regards to multiple input files. The extracted backgroundions are " + "written here in the PeakML format." + "\n" + "When this option has not been set the output is written to the standard output (works " + "only when there is a single input file).Be sure to unset the verbose option when setting " + "up a pipeline reading and writing from the standard in- and outputs.")
        public String output = null;

        @Option(name = "ppm", param = "double", type = Option.Type.REQUIRED_ARGUMENT, usage = "The accuracy of the measurement in parts-per-milion. This value is used for the " + "collection of the data-points belonging to a background ion and needs to be " + "reasonable for the equipment used to make the measurement (the LTQ-Orbitrap manages " + "approximatetly 3 ppm).")
        public double ppm = -1;

        @Option(name = "threshold", param = "double", type = Option.Type.REQUIRED_ARGUMENT, usage = "The percentage (a value between 0 and 1) of scans that minimally need to contain " + "a measurement.")
        public double threshold = -1;

        @Option(name = "h", param = "", type = Option.Type.NO_ARGUMENT, usage = "When this is set, the help is shown.")
        public boolean help = false;

        @Option(name = "v", param = "", type = Option.Type.NO_ARGUMENT, usage = "When this is set, the progress is shown on the standard output.")
        public boolean verbose = false;
    }

    @SuppressWarnings("unchecked")
    public static void main(String args[]) {
        try {
            Tool.init();
            Options options = new Options();
            CmdLineParser cmdline = new CmdLineParser(options);
            cmdline.parse(args);
            if (options.help) {
                Tool.printHeader(System.out, application, version);
                cmdline.printUsage(System.out, "");
                return;
            }
            if (options.verbose) {
                Tool.printHeader(System.out, application, version);
                cmdline.printOptions();
            }
            {
                if (options.ppm == -1) {
                    System.err.println("[ERROR]: the ppm-value needs to be set.");
                    System.exit(0);
                }
                if (options.threshold == -1) {
                    System.err.println("[ERROR]: the threshold-value needs to be set.");
                    System.exit(0);
                }
                if (options.threshold < 0 || options.threshold > 1) {
                    System.err.println("[ERROR]: the threshold-value needs to be between 0 and 1.");
                    System.exit(0);
                }
                if (options.input.size() == 0) {
                    options.input.add(null);
                } else if (options.input.size() == 1) {
                    File inputfile = new File(options.input.firstElement());
                    if (!inputfile.exists()) {
                        System.err.println("[ERROR]: the input-file '" + options.input.firstElement() + "' does not exist.");
                        System.exit(0);
                    }
                } else {
                    for (String filename : options.input) {
                        File inputfile = new File(filename);
                        if (!inputfile.exists()) {
                            System.err.println("[ERROR]: the input-file '" + filename + "' does not exist.");
                            System.exit(0);
                        }
                    }
                    if (options.output == null) {
                        System.err.println("[ERROR]: multiple input files defined without an output destination.");
                        System.exit(0);
                    }
                }
                if (options.output != null) Tool.createFilePath(options.output, options.input.size() == 1);
            }
            for (String filename : options.input) {
                File file = new File(filename);
                OutputStream out_output = System.out;
                if (options.input.size() == 1) {
                    if (options.output != null) out_output = new FileOutputStream(options.output);
                } else {
                    String name = new File(filename).getName();
                    out_output = new FileOutputStream(options.output + "/" + name);
                }
                if (options.verbose) System.out.println("Loading '" + filename + "'.");
                ParseResult result = ParseResult.parse(filename);
                ChromatographyMS<Peak> cms = (ChromatographyMS<Peak>) result.measurement;
                if (options.verbose) System.out.println("Extracting background ions.");
                Vector<BackgroundIon<? extends Peak>> backgroundions = extractBackgroundIons(cms, options.ppm, options.threshold);
                for (BackgroundIon backgroundion : backgroundions) backgroundion.setMeasurementID(0);
                if (options.verbose) System.out.println("Writing results.");
                Header header = result.header;
                header.setNrPeaks(backgroundions.size());
                MeasurementInfo measurement = header.getMeasurementInfo(0);
                measurement.addFileInfo(new FileInfo(null, file.getName(), file.getParentFile().getAbsolutePath()));
                PeakMLWriter.writeBackgroundIons(header, backgroundions, null, new GZIPOutputStream(out_output), "");
            }
        } catch (Exception e) {
            Tool.unexpectedError(e, application);
        }
    }
}
