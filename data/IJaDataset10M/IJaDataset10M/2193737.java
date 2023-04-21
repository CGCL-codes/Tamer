package datalog.coord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GpxTrackReaderWriter implements TrackReaderWriter {

    private static Boolean haveReader = true;

    private static Boolean haveWriter = true;

    private static DecimalFormat coordF = new DecimalFormat("0.00000000", new DecimalFormatSymbols(Locale.US));

    private static DecimalFormat altF = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));

    private static String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<gpx  version=\"1.1\"\n   creator=\"Jdatalog converter\"\n" + "   xmlns=\"http://www.topografix.com/GPX/1/1\"\n" + "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" + "   xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";

    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);

    public void read(String fn, Log l) throws IOException {
        InputStream in = new FileInputStream(fn);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();
            GpxReader reader = new GpxReader(l.getLogListeners());
            parser.parse(in, reader);
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        } finally {
            in.close();
        }
    }

    public void write(String fn, Log log, TimeZone tz) throws IOException {
        List l = log.getList();
        boolean saveWpts = false;
        FileOutputStream fw = new FileOutputStream(new File(fn));
        PrintWriter pw = new PrintWriter(fw);
        altF.applyPattern("0.00");
        writeHeaderSection(pw, log, tz);
        writeTrack(pw, log.buildFileName(), l, tz);
        if (saveWpts) writePoint(pw, l, tz);
        pw.println("</gpx>");
        pw.close();
    }

    public void write(String fn, Log log, Convert c, TimeZone tz) throws IOException {
        this.write(fn, log, tz);
    }

    public void write(String fn, Log log, TimeZone tz, boolean saveWpts) throws IOException {
        List l = log.getList();
        FileOutputStream fw = new FileOutputStream(new File(fn));
        PrintWriter pw = new PrintWriter(fw);
        altF.applyPattern("0.00");
        writeHeaderSection(pw, log, tz);
        writeTrack(pw, log.buildFileName(), l, tz);
        if (saveWpts) writePoint(pw, l, tz);
        pw.println("</gpx>");
        pw.close();
    }

    private static void writeHeaderSection(PrintWriter pw, Log log, TimeZone tz) {
        double lon = 0.0d, alt = 0.0d;
        pw.println(GpxTrackReaderWriter.header);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        GregorianCalendar date = log.getMinDate();
        date.setTimeZone(tz);
        pw.println("<metadata>\n");
        pw.println("<time>" + sdf.format(date.getTime()) + "T" + shf.format(date.getTime()) + "Z</time>");
        PointGeographique p = null;
        p = log.getMin();
        lon = ((lon = Math.toDegrees(p.getLon())) < 180) ? lon : lon - 360;
        pw.print("<bounds minlat=\"" + coordF.format(Math.toDegrees(p.getLat())) + "\" minlon=\"" + coordF.format(lon));
        p = log.getMax();
        lon = ((lon = p.getLat()) < 180) ? lon : lon - 360;
        pw.println("\" maxlat=\"" + coordF.format(Math.toDegrees(p.getLat())) + "\" maxlon=\"" + coordF.format(lon) + "\"/>");
        pw.println("</metadata>\n");
    }

    private static void writeTrack(PrintWriter pw, String trackName, List l, TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        GregorianCalendar date = null;
        double lon = 0.0d;
        pw.println("<trk>\n<name>" + trackName + "</name>\n<trkseg>");
        PointGeographique pg = null;
        PointGps pt = null;
        for (Iterator it = l.iterator(); it.hasNext(); ) {
            pt = (PointGps) it.next();
            if ((null != pt) && (pt.selected())) {
                pg = pt.getPointGeographique();
                if (null != pg) {
                    date = pt.getGCDate();
                    lon = ((lon = Math.toDegrees(pt.getLon())) < 180) ? lon : lon - 360;
                    pw.print("<trkpt lat=\"" + coordF.format(Math.toDegrees(pt.getLat())));
                    pw.println("\" lon=\"" + coordF.format(lon) + "\">");
                    pw.println("<ele>" + altF.format(pt.getAlt()) + "</ele>");
                    pw.println("<time>" + sdf.format(date.getTime()) + "T" + shf.format(date.getTime()) + "Z</time>");
                    pw.println("<geoidheight>" + altF.format(pt.getGeoidCor()) + "</geoidheight>");
                    if ((pt instanceof PointComplet) || (pt instanceof PointMoyenComplet)) {
                        pw.println("<sat>" + (int) pt.getNbSatellite() + "</sat>");
                        pw.println("<hdop>" + pt.getHdop() + "</hdop>");
                        pw.println("<pdop>" + pt.getPdop() + "</pdop>");
                    }
                    pw.println("</trkpt>");
                }
            }
        }
        pw.println("</trkseg>\n</trk>");
    }

    private static void writePoint(PrintWriter pw, List l, TimeZone tz) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat shf = new SimpleDateFormat("HH:mm:ss");
        GregorianCalendar date = null;
        double lon = 0.0d;
        PointGeographique pg = null;
        PointGps pt = null;
        for (Iterator it = l.iterator(); it.hasNext(); ) {
            pt = (PointGps) it.next();
            if ((null != pt) && (pt.selected())) {
                if ((pt instanceof PointMoyenComplet) || (pt instanceof PointMoyenSimple)) {
                    pg = pt.getPointGeographique();
                    if (null != pg) {
                        date = pt.getGCDate();
                        lon = ((lon = Math.toDegrees(pt.getLon())) < 180) ? lon : lon - 360;
                        pw.print("<wpt lat=\"" + coordF.format(Math.toDegrees(pt.getLat())));
                        pw.println("\" lon=\"" + coordF.format(lon) + "\">");
                        pw.println("<ele>" + altF.format(pt.getAlt()) + "</ele>");
                        pw.println("<geoidheight>" + altF.format(pt.getGeoidCor()) + "</geoidheight>");
                        pw.println("<time>" + sdf.format(date.getTime()) + "T" + shf.format(date.getTime()) + "Z</time>");
                        pw.println("<desc>\"" + pt.getInfos() + "\"</desc>");
                        if (pt instanceof PointMoyenComplet) {
                            pw.println("<sat>" + (int) pt.getNbSatellite() + "</sat>");
                            pw.println("<hdop>" + pt.getHdop() + "</hdop>");
                            pw.println("<pdop>" + pt.getPdop() + "</pdop>");
                        }
                        pw.println("</wpt>");
                    }
                }
            }
        }
    }

    public boolean isAValidTrackFile(File logFile) {
        return true;
    }

    public boolean readerAvailable() {
        return haveReader;
    }

    ;

    public boolean writerAvailable() {
        return haveWriter;
    }

    public class GpxReader extends DefaultHandler {

        private DateFormat dFormat = null;

        private StringBuffer buf = new StringBuffer();

        GregorianCalendar date = null;

        private double lat = 0d;

        private double lon = 0d;

        private float alt = Float.MIN_VALUE;

        private float geoide = 0f;

        private float pdop = 0f;

        private float hdop = 0f;

        private float vdop = 0f;

        private int nbSat = 0;

        private Date time;

        private int counter = 0;

        private Collection<LogListener> logListeners;

        private boolean debug = false;

        public GpxReader(Collection<LogListener> l) {
            this.logListeners = l;
            dFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            buf.setLength(0);
            if (debug) {
                System.out.println("Start -> " + qName);
            }
            if (qName.equals("trkpt")) {
                lat = Double.parseDouble(attributes.getValue("lat"));
                lon = Double.parseDouble(attributes.getValue("lon"));
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (debug) {
                System.out.println("#CDATA : " + buf.toString());
                System.out.println("End -> " + qName + "\n");
            }
            try {
                if (qName.equals("trkpt")) {
                    for (LogListener listener : this.logListeners) {
                        counter++;
                        if ((0f != geoide) || (0f != pdop) || (0f != hdop) || (0 != nbSat)) {
                            listener.addPoint(new PointComplet(date, lat, lon, alt, geoide, pdop, hdop, nbSat));
                        } else {
                            listener.addPoint(new PointSimple(date, lat, lon, alt));
                        }
                    }
                    if ((counter % Log.SERIES) == 0) {
                        for (LogListener listener : logListeners) {
                            listener.seriesReceived();
                        }
                    }
                } else if (qName.equals("ele")) {
                    alt = Float.parseFloat(buf.toString());
                } else if (qName.equals("geoidheight")) {
                    geoide = Float.parseFloat(buf.toString());
                } else if (qName.equals("hdop")) {
                    hdop = Float.parseFloat(buf.toString());
                } else if (qName.equals("pdop")) {
                    pdop = Float.parseFloat(buf.toString());
                } else if (qName.equals("sat")) {
                    nbSat = (int) Float.parseFloat(buf.toString());
                } else if (qName.equals("time")) {
                    date = new GregorianCalendar();
                    date.setTime(dFormat.parse(buf.toString()));
                }
            } catch (Exception e) {
                throw new SAXException("Parse Exception <" + qName + ">" + buf.toString() + "<" + qName + ">");
            }
        }

        @Override
        public void characters(char[] chars, int start, int length) throws SAXException {
            buf.append(chars, start, length);
        }
    }

    void main(String[] args) {
        GpxTrackReaderWriter rw = new GpxTrackReaderWriter();
        try {
            rw.read("test/20091129_095033_104431.gpx", null);
        } catch (IOException e) {
        }
    }
}
