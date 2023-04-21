package gov.nasa.gsfc.visbard.repository.resource;

import gov.nasa.gsfc.visbard.model.threadtask.ProgressReporter;
import gov.nasa.gsfc.visbard.repository.category.CategoryType;
import gov.nasa.gsfc.visbard.util.Range;
import gov.nasa.gsfc.visbard.util.VisbardDate;
import gov.nasa.gsfc.visbard.util.VisbardException;
import gsfc.nssdc.cdf.util.Epoch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ResourceWriterASCII implements ResourceWriter {

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(ResourceWriterASCII.class.getName());

    private static final int TABSPACE = 20;

    private File fFile;

    private ResourceInfo fOutputInfo;

    private ReadConfiguration fConfig;

    private Resource fResource;

    private ArrayList fCols = new ArrayList();

    private int fTabSpace = TABSPACE;

    private String fHeaderLine;

    private BufferedWriter fWriter;

    public ResourceWriterASCII(ResourceInfo outputInfo) throws VisbardException {
        fOutputInfo = outputInfo;
        if (fOutputInfo == null) throw new VisbardException("Missing output format");
    }

    private void initVariables(ReadConfiguration config) throws VisbardException {
        ArrayList incols = config.getColumns();
        fHeaderLine = "";
        ArrayList readerCols = new ArrayList(Arrays.asList(fResource.getReader().getAllAvaliableColumns()));
        for (int i = 0; i < incols.size(); i++) {
            Column incol = (Column) incols.get(i);
            if (readerCols.contains(incol)) {
                fCols.add(incol);
            }
        }
        for (int i = 0; i < fCols.size(); i++) {
            Column col = (Column) fCols.get(i);
            int ul = col.getName().length() + col.getUnit().toString().length() + 9;
            if (!col.isScalar()) ul += 3;
            if (fTabSpace < ul) fTabSpace = ul;
        }
        for (int i = 0; i < fCols.size(); i++) {
            Column col = (Column) fCols.get(i);
            String sz = "f";
            if (col.isDouble()) {
                sz = "d";
            }
            if (col.isScalar()) {
                if (col.getName().equals(CategoryType.TIME.getName())) {
                    fHeaderLine += "{Time}[Year]{}{d}";
                    fHeaderLine += tab(fHeaderLine);
                    fHeaderLine += "{Time}[Day]{}{d}";
                    fHeaderLine += tab(fHeaderLine);
                    fHeaderLine += "{Time}[Hour]{}{d}";
                    fHeaderLine += tab(fHeaderLine);
                } else {
                    fHeaderLine += "{" + col.getName() + "}{" + col.getUnit() + "}{" + sz + "}";
                    fHeaderLine += tab(fHeaderLine);
                }
            } else {
                fHeaderLine += "{" + col.getName() + "}[X]{" + col.getUnit() + "}{" + sz + "}";
                fHeaderLine += tab(fHeaderLine);
                fHeaderLine += "{" + col.getName() + "}[Y]{" + col.getUnit() + "}{" + sz + "}";
                fHeaderLine += tab(fHeaderLine);
                fHeaderLine += "{" + col.getName() + "}[Z]{" + col.getUnit() + "}{" + sz + "}";
                fHeaderLine += tab(fHeaderLine);
            }
        }
        try {
            fWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fFile)));
        } catch (IOException e) {
            throw new VisbardException(e.getMessage());
        }
    }

    /**
     * Writes the repository
     **/
    public void writeResource(File file, Resource rsrc, ProgressReporter prg, String fillVal) throws VisbardException {
        fFile = file;
        if (fFile == null) throw new VisbardException("Missing output file");
        fResource = rsrc;
        if (fResource == null) throw new VisbardException("Missing resource");
        fConfig = rsrc.getCurrentConfig();
        ResourceReader reader = fResource.getReader();
        if (prg != null) {
            prg.setTaskTitle("Writing Data");
            prg.setProgress(0);
        }
        initVariables(fConfig);
        reader.open();
        try {
            fWriter.write(reader.getCentricity());
            fWriter.newLine();
            fWriter.write(fHeaderLine);
            fWriter.newLine();
        } catch (IOException e) {
            throw new VisbardException(e.getMessage());
        }
        Range timeRange = fConfig.getTimeRange();
        long numreadings = reader.getNumReadings(timeRange);
        sLogger.info("Found " + numreadings + " readings in range " + VisbardDate.getString(timeRange) + ".");
        int d = fConfig.getDecimation();
        int toread = (int) (Math.floor(numreadings / d));
        sLogger.info("Decimation factor " + d + ". Saving " + toread + " readings.");
        if (toread == 0) throw new VisbardException("Nothing to output");
        reader.reset();
        reader.fastForward(timeRange.fStart);
        String line = "";
        int rcount = 0;
        double[] data;
        int linesToSkip = (int) d - 1;
        if (prg != null) prg.setProgress(0.2f);
        for (int j = 0; j < toread; j++) {
            if (prg != null) prg.setProgress((((float) j / toread) * 0.8f) + 0.2f);
            reader.skip(linesToSkip);
            Reading reading = reader.next();
            line = "";
            if (reading != null) {
                rcount++;
                for (int i = 0; i < fCols.size(); i++) {
                    Column col = (Column) fCols.get(i);
                    data = reading.get(col);
                    if (col.getName().equals(CategoryType.TIME.getName())) {
                        if (Double.isNaN(data[0])) {
                            String misdata = "---";
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                        } else {
                            double timeData = data[0];
                            if (col.getUnit().toString().equals("sec1970")) {
                                timeData *= 1000d;
                                timeData += 62168472000000d - 1252800000l;
                            }
                            long[] date = Epoch.breakdown(timeData);
                            Calendar cal = new GregorianCalendar((int) date[0], (int) date[1] - 1, (int) date[2], (int) date[3], (int) date[4], (int) date[5]);
                            cal.add(Calendar.MILLISECOND, (int) date[6]);
                            double year = (double) date[0];
                            double day = cal.get(Calendar.DAY_OF_YEAR);
                            double hour = (double) date[3] + ((double) date[4] / 60d) + ((double) date[5] / (60 * 60d) + ((double) date[6] / (1000 * 60 * 60d)));
                            line += Double.toString(year);
                            line += tab(line);
                            line += Double.toString(day);
                            line += tab(line);
                            line += Double.toString(hour);
                            line += tab(line);
                        }
                    } else if (col.isScalar()) {
                        if (Double.isNaN(data[0])) {
                            line += fillVal;
                        } else {
                            line += Double.toString(data[0]);
                        }
                        line += tab(line);
                    } else {
                        if (Double.isNaN(data[0]) || Double.isNaN(data[1]) || (Double.isNaN(data[2]))) {
                            String misdata = fillVal;
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                            line += misdata;
                            line += tab(line);
                        } else {
                            line += Double.toString(data[0]);
                            line += tab(line);
                            line += Double.toString(data[1]);
                            line += tab(line);
                            line += Double.toString(data[2]);
                            line += tab(line);
                        }
                    }
                }
                try {
                    fWriter.write(line);
                    fWriter.newLine();
                } catch (IOException e) {
                    throw new VisbardException(e.getMessage());
                }
            }
        }
        try {
            fWriter.close();
        } catch (IOException e) {
            throw new VisbardException(e.getMessage());
        }
        reader.close();
    }

    public boolean canSaveMetadata() {
        return false;
    }

    private String tab(String str) {
        int numspac = (fTabSpace - ((str.length() + 1) % fTabSpace)) + 1;
        StringBuffer strb = new StringBuffer(numspac);
        for (int i = 0; i < numspac; i++) strb.append(' ');
        return strb.toString();
    }
}
