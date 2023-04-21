package org.processmining.importing.fuzzyModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.processmining.framework.log.LogEvent;
import org.processmining.framework.log.LogEvents;
import org.processmining.importing.ImportPlugin;
import org.processmining.mining.MiningResult;
import org.processmining.mining.fuzzymining.graph.ClusterNode;
import org.processmining.mining.fuzzymining.graph.MutableFuzzyGraph;
import org.processmining.mining.fuzzymining.metrics.binary.BinaryMetric;
import org.processmining.mining.fuzzymining.metrics.unary.UnaryMetric;
import org.processmining.mining.fuzzymining.ui.FuzzyModelViewResult;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Christian W. Guenther (christian@deckfour.org)
 * 
 */
public class FuzzyModelImport implements ImportPlugin {

    public FileFilter getFileFilter() {
        return new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".fmz");
            }

            @Override
            public String getDescription() {
                return "Compact Fuzzy Model";
            }
        };
    }

    public MiningResult importFile(InputStream input) throws IOException {
        FuzzyModelHandler fmHandler = new FuzzyModelHandler();
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = parserFactory.newSAXParser();
            input = new BufferedInputStream(new GZIPInputStream(input));
            parser.parse(input, fmHandler);
            return new FuzzyModelViewResult(fmHandler.getFuzzyModel());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getHtmlDescription() {
        return "Imports a Fuzzy Model";
    }

    public String getName() {
        return "Fuzzy Model import";
    }

    protected class FuzzyModelHandler extends DefaultHandler {

        protected StringBuffer buffer;

        protected int clusterIndex = 0;

        protected String clusterName = null;

        protected MutableFuzzyGraph model;

        protected int size;

        protected LogEvents events;

        protected UnaryMetric nodeSignificance;

        protected BinaryMetric edgeSignificance;

        protected BinaryMetric edgeCorrelation;

        protected HashMap<String, String> attributeMap;

        protected FuzzyModelHandler() {
            buffer = new StringBuffer();
        }

        protected MutableFuzzyGraph getFuzzyModel() {
            if (attributeMap != null) {
                for (String key : attributeMap.keySet()) {
                    model.setAttribute(key, attributeMap.get(key));
                }
            }
            return model;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            buffer.append(ch, start, length);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            String tagName = localName;
            if (tagName.equalsIgnoreCase("")) {
                tagName = qName;
            }
            if (tagName.equalsIgnoreCase("fuzzyModel")) {
                size = Integer.parseInt(attributes.getValue("size"));
            } else if (tagName.equalsIgnoreCase("logEvents")) {
                events = new LogEvents();
            } else if (tagName.equalsIgnoreCase("logEvent")) {
                String element = attributes.getValue("element");
                String type = attributes.getValue("type");
                int occurrenceCount = Integer.parseInt(attributes.getValue("occurrence"));
                events.add(new LogEvent(element, type, occurrenceCount));
            } else if (tagName.equalsIgnoreCase("attributes")) {
                attributeMap = new HashMap<String, String>();
            } else if (tagName.equalsIgnoreCase("attribute")) {
                String key = attributes.getValue("key");
                String value = attributes.getValue("value");
                attributeMap.put(key, value);
            } else if (tagName.equalsIgnoreCase("unarySignificance")) {
                int mSize = Integer.parseInt(attributes.getValue("size"));
                nodeSignificance = new UnaryMetric("Unary significance", "Node significance", mSize);
            } else if (tagName.equalsIgnoreCase("binarySignificance")) {
                int mSize = Integer.parseInt(attributes.getValue("size"));
                edgeSignificance = new BinaryMetric("Binary significance", "edge significance", mSize);
            } else if (tagName.equalsIgnoreCase("binaryCorrelation")) {
                int mSize = Integer.parseInt(attributes.getValue("size"));
                edgeCorrelation = new BinaryMetric("Binary correlation", "edge correlation", mSize);
            } else if (tagName.equalsIgnoreCase("abstractedNode")) {
                int aIndex = Integer.parseInt(attributes.getValue("index"));
                model.setNodeAliasMapping(aIndex, null);
            } else if (tagName.equalsIgnoreCase("cluster")) {
                clusterIndex = Integer.parseInt(attributes.getValue("index"));
                String tmpName = attributes.getValue("name");
                if (clusterName != null && clusterName.trim().length() == 0) {
                    clusterName = tmpName;
                } else {
                    clusterName = null;
                }
            }
            buffer.delete(0, buffer.length());
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            String tagName = localName;
            if (tagName.equalsIgnoreCase("")) {
                tagName = qName;
            }
            if (tagName.equalsIgnoreCase("metrics")) {
                model = new MutableFuzzyGraph(nodeSignificance, edgeSignificance, edgeCorrelation, events);
            } else if (tagName.equalsIgnoreCase("unarySignificance")) {
                String val[] = buffer.toString().split(";");
                if (val.length != size) {
                    throw new SAXException("Incorrect number of values in unary metric!");
                }
                for (int i = 0; i < size; i++) {
                    nodeSignificance.setMeasure(i, Double.parseDouble(val[i]));
                }
            } else if (tagName.equalsIgnoreCase("binarySignificance")) {
                String val[] = buffer.toString().split(";");
                if (val.length != (size * size)) {
                    throw new SAXException("Incorrect number of values in binary significance metric!");
                }
                int index = 0;
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        edgeSignificance.setMeasure(x, y, Double.parseDouble(val[index]));
                        index++;
                    }
                }
            } else if (tagName.equalsIgnoreCase("binaryCorrelation")) {
                String val[] = buffer.toString().split(";");
                if (val.length != (size * size)) {
                    throw new SAXException("Incorrect number of values in binary correlation metric!");
                }
                int index = 0;
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        edgeCorrelation.setMeasure(x, y, Double.parseDouble(val[index]));
                        index++;
                    }
                }
            } else if (tagName.equalsIgnoreCase("transformedBinarySignificance")) {
                String val[] = buffer.toString().split(";");
                if (val.length != (size * size)) {
                    throw new SAXException("Incorrect number of values in transformed binary significance metric!");
                }
                int index = 0;
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        model.setBinarySignificance(x, y, Double.parseDouble(val[index]));
                        index++;
                    }
                }
            } else if (tagName.equalsIgnoreCase("transformedBinaryCorrelation")) {
                String val[] = buffer.toString().split(";");
                if (val.length != (size * size)) {
                    throw new SAXException("Incorrect number of values in transformed binary correlation metric!");
                }
                int index = 0;
                for (int x = 0; x < size; x++) {
                    for (int y = 0; y < size; y++) {
                        model.setBinaryCorrelation(x, y, Double.parseDouble(val[index]));
                        index++;
                    }
                }
            } else if (tagName.equalsIgnoreCase("cluster")) {
                String val[] = buffer.toString().split(";");
                ClusterNode cluster = new ClusterNode(model, clusterIndex);
                if (clusterName != null) {
                    cluster.setElementName(clusterName);
                }
                model.addClusterNode(cluster);
                for (int i = 0; i < val.length; i++) {
                    int nodeIndex = Integer.parseInt(val[i]);
                    cluster.add(model.getPrimitiveNode(nodeIndex));
                    model.setNodeAliasMapping(nodeIndex, cluster);
                }
            }
        }
    }
}
