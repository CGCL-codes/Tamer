package datalog.coord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DxfTrackReaderWriter implements TrackReaderWriter {

    private static Boolean haveReader = false;

    private static Boolean haveWriter = true;

    private static DecimalFormat coordF = new DecimalFormat("###0.000000", new DecimalFormatSymbols(Locale.US));

    private static DecimalFormat altF = new DecimalFormat("0.0", new DecimalFormatSymbols(Locale.US));

    private static String POINT_LAYER = "9510";

    private static String TRACK_LAYER = "9520";

    private static String TEXT_LAYER = "9530";

    private Convert converter = Convert.LAMBERT2E;

    public void write(String fn, Log log, TimeZone tz) throws IOException {
        List l = log.getList();
        coordF.applyPattern("0.00");
        altF.applyPattern("0.0");
        FileOutputStream fw = new FileOutputStream(new File(fn));
        PrintWriter pw = new PrintWriter(fw);
        writeHeaderSection(pw, log);
        pw.println("  0\nSECTION\n  2\nENTITIES");
        writeTrack(pw, l);
        writePoint(pw, l);
        pw.println("  0\nENDSEC\n  2\nEOF");
        pw.close();
    }

    public void write(String fn, Log log, Convert c, TimeZone tz) throws IOException {
        List l = log.getList();
        if (null != c) this.converter = c;
        FileOutputStream fw = new FileOutputStream(new File(fn));
        PrintWriter pw = new PrintWriter(fw);
        writeHeaderSection(pw, log);
        pw.println("  0\nSECTION\n  2\nENTITIES");
        writeTrack(pw, l);
        writePoint(pw, l);
        pw.println("  0\nENDSEC\n  2\nEOF");
        pw.close();
    }

    private void writeHeaderSection(PrintWriter pw, Log log) {
        double alt = 0;
        PointCartesien pc = null;
        pw.println("  0\nSECTION\n  2\nHEADER");
        pc = this.converter.convert(log.getMin());
        pw.println("  9\n$EXTMIN\n 10\n" + coordF.format(pc.getX()) + "\n 20\n" + coordF.format(pc.getY()));
        pc = this.converter.convert(log.getMax());
        pw.println("  9\n$EXTMAX\n 10\n" + coordF.format(pc.getX()) + "\n 20\n" + coordF.format(pc.getY()));
        pw.println("  0\nENDSEC");
    }

    private void writeTrack(PrintWriter pw, List l) {
        pw.println("  0\nPOLYLINE\n 66\n1\n  8\n" + DxfTrackReaderWriter.TRACK_LAYER);
        PointCartesien pc = null;
        PointGps pt = null;
        for (Iterator it = l.iterator(); it.hasNext(); ) {
            pt = (PointGps) it.next();
            if ((null != pt) && (pt.selected())) {
                pc = this.converter.convert(pt.getPointGeographique());
                if (null != pc) {
                    pw.println("  0\nVERTEX\n  8\n" + DxfTrackReaderWriter.TRACK_LAYER);
                    pw.println(" 10\n" + coordF.format(pc.getX()));
                    pw.println(" 20\n" + coordF.format(pc.getY()));
                    pw.println(" 30\n" + altF.format(pt.getAlt() - pt.getGeoidCor()));
                }
            }
        }
        pw.println("  0\nSEQEND");
    }

    private void writePoint(PrintWriter pw, List l) {
        PointCartesien pc = null;
        PointGps pt = null;
        for (Iterator it = l.iterator(); it.hasNext(); ) {
            pt = (PointGps) it.next();
            if ((null != pt) && (pt.selected())) {
                if ((pt instanceof PointMoyenComplet) || (pt instanceof PointMoyenSimple)) {
                    pc = this.converter.convert(pt.getPointGeographique());
                    if (null != pc) {
                        pw.println("  0\nPOINT\n  8\n" + DxfTrackReaderWriter.POINT_LAYER);
                        pw.println(" 10\n" + coordF.format(pc.getX()));
                        pw.println(" 20\n" + coordF.format(pc.getY()));
                        pw.println(" 30\n" + altF.format(pt.getAlt() - pt.getGeoidCor()));
                        pw.println("  0\nTEXT\n  8\n" + DxfTrackReaderWriter.TEXT_LAYER);
                        pw.println(" 10\n" + coordF.format(pc.getX()) + 10);
                        pw.println(" 20\n" + coordF.format(pc.getY()) + 10);
                        pw.println(" 30\n" + altF.format(pt.getAlt() - pt.getGeoidCor()));
                        pw.println(" 40\n  10");
                        pw.println("  1\n  " + pt.getInfos());
                    }
                }
            }
        }
    }

    public boolean isAValidTrackFile(File logFile) {
        return true;
    }

    public void read(String fn, Log l) throws IOException {
        return;
    }

    ;

    public boolean readerAvailable() {
        return haveReader;
    }

    ;

    public boolean writerAvailable() {
        return haveWriter;
    }

    ;
}
