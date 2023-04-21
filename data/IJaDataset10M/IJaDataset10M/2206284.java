package org.expasy.jpl.core.ms.export;

import java.io.File;
import java.io.IOException;
import org.expasy.jpl.commons.collection.stat.HistogramDataSetExporter;
import org.expasy.jpl.commons.collection.stat.HistogramDataSetExporter.BinType;
import org.expasy.jpl.core.ms.spectrum.stat.PeakListDistribution;

/**
 * This object exports {@code MSPeakListDistribution}s into files and generate R
 * scripts for rendering.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public class MSPeakListDistributionExporter {

    private static HistogramDataSetExporter HISTO_EXPORTER = HistogramDataSetExporter.newInstance();

    private MSPeakListDistributionExporter() {
    }

    public static MSPeakListDistributionExporter newInstance() {
        return new MSPeakListDistributionExporter();
    }

    /**
	 * Export data from normal and baseline histograms.
	 * 
	 * /usr/bin/R -f /tmp/script.R
	 * 
	 * @param pld the peaklist dists to export.
	 * @param dir the directory to export files to.
	 * @param title the dist title.
	 * @param isRScript generate R script if true.
	 * 
	 * @throws IOException if IO error.
	 */
    public String export(PeakListDistribution pld, String dir, String title, boolean isRScript) throws IOException {
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        directory = new File(dir + File.separatorChar + "data");
        if (!directory.exists()) {
            directory.mkdir();
        }
        HISTO_EXPORTER.setBinType(BinType.LOWER);
        String dirname = dir + File.separatorChar + "data" + File.separatorChar;
        String filename1 = dirname + title + ".tsv";
        String filename2 = dirname + title + "_med.tsv";
        HISTO_EXPORTER.export(pld.getRawHistogram(), filename1);
        HISTO_EXPORTER.export(pld.getSmoothedHistogram(), filename2);
        if (isRScript) {
            return generateRScript(dir, title, filename1, filename2);
        }
        return "";
    }

    private String generateRScript(String dir, String title, String filename1, String filename2) {
        StringBuilder sb = new StringBuilder();
        String pdfname = dir + File.separatorChar + title + ".pdf";
        sb.append("pdf(\"" + pdfname + "\")\n");
        sb.append("data1 <- read.table('" + filename1 + "', sep=\"\\t\", " + "header=T, blank.lines.skip = TRUE)\n");
        sb.append("data1BS <- read.table('" + filename2 + "', sep=\"\\t\", " + "header=T,blank.lines.skip = TRUE)\n");
        sb.append("plot(data1$bin,data1$frequency, col=1)\n");
        sb.append("points(data1BS$bin,data1BS$frequency, col=2)\n");
        sb.append("legend(\"topright\", c(\"RAW\",\"BASELINE\")," + " cex=1.5, fill=c(1,2))\n");
        sb.append("graphics.off()\n");
        return sb.toString();
    }
}
