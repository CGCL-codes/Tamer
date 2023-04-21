package za.ac.uct.mydas.writeback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import uk.ac.ebi.mydas.exceptions.DataSourceException;
import uk.ac.ebi.mydas.model.DasAnnotatedSegment;
import uk.ac.ebi.mydas.model.DasFeature;
import uk.ac.ebi.mydas.model.DasFeatureOrientation;
import uk.ac.ebi.mydas.model.DasGroup;
import uk.ac.ebi.mydas.model.DasPhase;
import uk.ac.ebi.mydas.model.DasTarget;

public class DasParser {

    public static final String ELEMENT_DASGFF = "DASGFF";

    public static final String ELEMENT_GFF = "GFF";

    public static final String ELEMENT_SEGMENT = "SEGMENT";

    public static final String ELEMENT_FEATURE = "FEATURE";

    public static final String ELEMENT_TYPE = "TYPE";

    public static final String ELEMENT_METHOD = "METHOD";

    public static final String ELEMENT_START = "START";

    public static final String ELEMENT_END = "END";

    public static final String ELEMENT_SCORE = "SCORE";

    public static final String ELEMENT_ORIENTATION = "ORIENTATION";

    public static final String ELEMENT_PHASE = "PHASE";

    public static final String ELEMENT_GROUP = "GROUP";

    public static final String ELEMENT_NOTE = "NOTE";

    public static final String ELEMENT_LINK = "LINK";

    public static final String ELEMENT_TARGET = "TARGET";

    public static final String ELEMENT_UNKNOWNSEGMENT = "UNKNOWNSEGMENT";

    public static final String ELEMENT_ERRORSEGMENT = "ERRORSEGMENT";

    public static final String ATT_version = "version";

    public static final String ATT_href = "href";

    public static final String ATT_id = "id";

    public static final String ATT_start = "start";

    public static final String ATT_stop = "stop";

    public static final String ATT_label = "label";

    public static final String ATT_type = "type";

    public static final String ATT_category = "category";

    public static final String ATT_reference = "reference";

    public static final String ATT_subparts = "subparts";

    private XmlPullParserFactory XPP_FACTORY = null;

    public DasParser(XmlPullParserFactory xmlFactory) {
        this.XPP_FACTORY = xmlFactory;
    }

    public WritebackDocument parse(String content) {
        WritebackDocument wbd = null;
        XmlPullParser xpp;
        try {
            xpp = XPP_FACTORY.newPullParser();
            xpp.setInput(new StringReader(content));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    wbd = new WritebackDocument();
                } else {
                    parseDASGFF(wbd, xpp);
                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wbd;
    }

    private void parseDASGFF(WritebackDocument wbd, XmlPullParser xpp) throws XmlPullParserException, IOException {
        while (!(xpp.next() == XmlPullParser.END_TAG && ELEMENT_DASGFF.equals(xpp.getName()))) {
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                final String tagName = xpp.getName();
                if (ELEMENT_GFF.equals(tagName)) {
                    parseGFF(wbd, xpp);
                }
            }
        }
    }

    private void parseGFF(WritebackDocument wbd, XmlPullParser xpp) throws XmlPullParserException, IOException {
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_version)) wbd.setVersion(xpp.getAttributeValue(i)); else if (attName.equals(ATT_href)) wbd.setHref(xpp.getAttributeValue(i));
        }
        while (!(xpp.next() == XmlPullParser.END_TAG && ELEMENT_GFF.equals(xpp.getName()))) {
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                final String tagName = xpp.getName();
                if (ELEMENT_SEGMENT.equals(tagName)) {
                    try {
                        parseSegment(wbd, xpp);
                    } catch (DataSourceException e) {
                        throw new XmlPullParserException("Error. Parsing problems.");
                    }
                } else if (ELEMENT_ERRORSEGMENT.equals(tagName)) {
                    throw new XmlPullParserException("Error Segment tag found.");
                } else if (ELEMENT_UNKNOWNSEGMENT.equals(tagName)) {
                    throw new XmlPullParserException("Uknown Segment tag found.");
                }
            }
        }
    }

    private void parseSegment(WritebackDocument wbd, XmlPullParser xpp) throws XmlPullParserException, IOException, DataSourceException {
        String id = null, start = null, stop = null, version = null, label = null;
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_id)) id = xpp.getAttributeValue(i); else if (attName.equals(ATT_start)) start = xpp.getAttributeValue(i); else if (attName.equals(ATT_stop)) stop = xpp.getAttributeValue(i); else if (attName.equals(ATT_version)) version = xpp.getAttributeValue(i); else if (attName.equals(ATT_label)) label = xpp.getAttributeValue(i);
        }
        List<DasFeature> features = new ArrayList<DasFeature>();
        while (!(xpp.next() == XmlPullParser.END_TAG && ELEMENT_SEGMENT.equals(xpp.getName()))) {
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                final String tagName = xpp.getName();
                if (ELEMENT_FEATURE.equals(tagName)) {
                    features.add(parsefeature(xpp));
                }
            }
        }
        try {
            wbd.setSegment(new DasAnnotatedSegment(id, Integer.parseInt(start), Integer.parseInt(stop), version, label, features));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (DataSourceException e) {
            e.printStackTrace();
        }
    }

    private DasFeature parsefeature(XmlPullParser xpp) throws XmlPullParserException, IOException, DataSourceException {
        DasFeature feature = null;
        String id = null, label = null;
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_id)) id = xpp.getAttributeValue(i); else if (attName.equals(ATT_label)) label = xpp.getAttributeValue(i);
        }
        String[] type = { "", "", "" };
        String[] method = { "", "" };
        int start = 0, end = 0;
        Double score = null;
        DasFeatureOrientation orientation = null;
        DasPhase phase = null;
        List<DasGroup> groups = new ArrayList<DasGroup>();
        Map<URL, String> links = new HashMap<URL, String>();
        List<String> notes = new ArrayList<String>();
        List<DasTarget> targets = new ArrayList<DasTarget>();
        while (!(xpp.next() == XmlPullParser.END_TAG && ELEMENT_FEATURE.equals(xpp.getName()))) {
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                final String tagName = xpp.getName();
                if (ELEMENT_TYPE.equals(tagName)) {
                    type = parseType(xpp);
                } else if (ELEMENT_METHOD.equals(tagName)) {
                    method = parseMethod(xpp);
                } else if (ELEMENT_START.equals(tagName)) {
                    xpp.next();
                    start = Integer.parseInt(xpp.getText());
                } else if (ELEMENT_END.equals(tagName)) {
                    xpp.next();
                    end = Integer.parseInt(xpp.getText());
                } else if (ELEMENT_SCORE.equals(tagName)) {
                    xpp.next();
                    score = new Double(xpp.getText());
                } else if (ELEMENT_ORIENTATION.equals(tagName)) {
                    xpp.next();
                    String ori = xpp.getText();
                    if (ori.equals("0")) orientation = DasFeatureOrientation.ORIENTATION_NOT_APPLICABLE; else if (ori.equals("+")) orientation = DasFeatureOrientation.ORIENTATION_SENSE_STRAND; else if (ori.equals("-")) orientation = DasFeatureOrientation.ORIENTATION_ANTISENSE_STRAND;
                } else if (ELEMENT_PHASE.equals(tagName)) {
                    xpp.next();
                    String pha = xpp.getText();
                    if (pha.equals("-")) phase = DasPhase.PHASE_NOT_APPLICABLE; else if (pha.equals("0")) phase = DasPhase.PHASE_READING_FRAME_0; else if (pha.equals("1")) phase = DasPhase.PHASE_READING_FRAME_1; else if (pha.equals("2")) phase = DasPhase.PHASE_READING_FRAME_2;
                } else if (ELEMENT_GROUP.equals(tagName)) {
                    groups.add(parseGroup(xpp));
                } else if (ELEMENT_LINK.equals(tagName)) {
                    parseLink(xpp, links);
                } else if (ELEMENT_NOTE.equals(tagName)) {
                    xpp.next();
                    notes.add(xpp.getText());
                } else if (ELEMENT_TARGET.equals(tagName)) {
                    targets.add(parseTarget(xpp));
                }
            }
        }
        try {
            feature = new DasFeature(id, label, type[0], type[1], type[2], method[0], method[1], start, end, score, orientation, phase, notes, links, targets, groups);
        } catch (DataSourceException e) {
            e.printStackTrace();
        }
        return feature;
    }

    private String[] parseType(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String[] type = { "", "", "" };
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_id)) type[0] = xpp.getAttributeValue(i); else if (attName.equals(ATT_category)) type[1] = xpp.getAttributeValue(i);
        }
        xpp.next();
        type[2] = xpp.getText();
        return type;
    }

    private String[] parseMethod(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String[] method = { "", "" };
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_id)) method[0] = xpp.getAttributeValue(i);
        }
        xpp.next();
        method[1] = xpp.getText();
        return method;
    }

    private void parseLink(XmlPullParser xpp, Map<URL, String> links) throws XmlPullParserException, IOException {
        URL url = null;
        String text;
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_href)) url = new URL(xpp.getAttributeValue(i));
        }
        xpp.next();
        text = xpp.getText();
        links.put(url, text);
    }

    private DasTarget parseTarget(XmlPullParser xpp) throws DataSourceException, XmlPullParserException, IOException {
        String id = null, label = null;
        int start = 0, stop = 0;
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_id)) id = xpp.getAttributeValue(i); else if (attName.equals(ATT_start)) start = Integer.parseInt(xpp.getAttributeValue(i)); else if (attName.equals(ATT_stop)) stop = Integer.parseInt(xpp.getAttributeValue(i));
        }
        xpp.next();
        label = xpp.getText();
        DasTarget target = new DasTarget(id, start, stop, label);
        return target;
    }

    private DasGroup parseGroup(XmlPullParser xpp) throws DataSourceException, XmlPullParserException, IOException {
        String id = null, label = null, type = null;
        Map<URL, String> links = new HashMap<URL, String>();
        List<String> notes = new ArrayList<String>();
        List<DasTarget> targets = new ArrayList<DasTarget>();
        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            final String attName = xpp.getAttributeName(i);
            if (attName.equals(ATT_id)) id = xpp.getAttributeValue(i); else if (attName.equals(ATT_type)) type = xpp.getAttributeValue(i); else if (attName.equals(ATT_label)) label = xpp.getAttributeValue(i);
        }
        while (!(xpp.next() == XmlPullParser.END_TAG && ELEMENT_GROUP.equals(xpp.getName()))) {
            if (xpp.getEventType() == XmlPullParser.START_TAG) {
                final String tagName = xpp.getName();
                if (ELEMENT_LINK.equals(tagName)) {
                    parseLink(xpp, links);
                } else if (ELEMENT_NOTE.equals(tagName)) {
                    xpp.next();
                    notes.add(xpp.getText());
                } else if (ELEMENT_TARGET.equals(tagName)) {
                    targets.add(parseTarget(xpp));
                }
            }
        }
        DasGroup group = new DasGroup(id, label, type, notes, links, targets);
        return group;
    }

    public static void main(String[] arg) throws IOException {
        XmlPullParserFactory PULL_PARSER_FACTORY;
        try {
            PULL_PARSER_FACTORY = XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null);
            PULL_PARSER_FACTORY.setNamespaceAware(true);
            DasParser dp = new DasParser(PULL_PARSER_FACTORY);
            URL url = new URL("http://www.ebi.ac.uk/das-srv/uniprot/das/uniprot/features?segment=P05067");
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String aLine, xml = "";
            while ((aLine = br.readLine()) != null) {
                xml += aLine;
            }
            WritebackDocument wbd = dp.parse(xml);
            System.out.println("FIN" + wbd);
        } catch (XmlPullParserException xppe) {
            throw new IllegalStateException("Fatal Exception thrown at initialisation.  Cannot initialise the PullParserFactory required to allow generation of the DAS XML.", xppe);
        }
    }
}
