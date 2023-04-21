package com.mebigfatguy.pixelle;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import com.mebigfatguy.pixelle.utils.Closer;
import com.mebigfatguy.pixelle.utils.XMLEncoder;

public class AlgorithmArchiver {

    private static final String SYSTEM_ALGO_XML_PATH = "/com/mebigfatguy/pixelle/resources/algorithms.xml";

    private static final String SYSTEM_ALGO_XSD_PATH = "/com/mebigfatguy/pixelle/resources/algorithms.xsd";

    public static final String TYPE = "type";

    public static final String GROUP = "group";

    public static final String CURRENT = "current_";

    public static final String ALGORITHM = "algorithm";

    public static final String COMPONENT = "component";

    public static final String NAME = "name";

    public static final String PIXELLE = ".mebigfatguy/pixelle";

    public static final String ALGORITHMS_FILE = "algorithms.xml";

    private static AlgorithmArchiver archiver = new AlgorithmArchiver();

    private final Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> systemAlgorithms;

    private final Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> userAlgorithms;

    private AlgorithmArchiver() {
        systemAlgorithms = new EnumMap<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>>(ImageType.class);
        userAlgorithms = new EnumMap<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>>(ImageType.class);
        loadSystemAlgorithms();
        loadUserAlgorithms();
    }

    public static AlgorithmArchiver getArchiver() {
        return archiver;
    }

    public JPopupMenu getAlgorithmDisplayPopup(ImageType imageType, ActionListener l) {
        JPopupMenu m = new JPopupMenu(PixelleBundle.getString(PixelleBundle.PIXEL_ALGORITHM));
        populateMenuAlgorithms(m, systemAlgorithms.get(imageType), l);
        populateMenuAlgorithms(m, userAlgorithms.get(imageType), l);
        return m;
    }

    private void populateMenuAlgorithms(JPopupMenu menu, Map<String, Map<String, Map<PixelleComponent, String>>> algorithms, ActionListener l) {
        if (algorithms != null) {
            for (final Map.Entry<String, Map<String, Map<PixelleComponent, String>>> entry : algorithms.entrySet()) {
                String groupName = entry.getKey();
                if (!CURRENT.equals(groupName)) {
                    JMenu group = new JMenu(groupName);
                    menu.add(group);
                    for (final String algos : entry.getValue().keySet()) {
                        JMenuItem algoItem = new JMenuItem(algos);
                        algoItem.putClientProperty(NAME, groupName);
                        algoItem.addActionListener(l);
                        group.add(algoItem);
                    }
                }
            }
        }
    }

    public Map<PixelleComponent, String> getAlgorithm(ImageType imageType, String groupName, String algorithmName) {
        Map<String, Map<String, Map<PixelleComponent, String>>> type = systemAlgorithms.get(imageType);
        if (type != null) {
            Map<String, Map<PixelleComponent, String>> group = type.get(groupName);
            if (group != null) {
                Map<PixelleComponent, String> algo = group.get(algorithmName);
                if (algo != null) {
                    return algo;
                }
            }
        }
        type = userAlgorithms.get(imageType);
        if (type == null) {
            throw new IllegalArgumentException("Unknown type name " + imageType.name());
        }
        Map<String, Map<PixelleComponent, String>> group = type.get(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Unknown group name " + groupName);
        }
        Map<PixelleComponent, String> algo = group.get(algorithmName);
        if (algo == null) {
            throw new IllegalArgumentException("Unknown algorithm name " + algorithmName);
        }
        return algo;
    }

    public String[] getUserGroups(ImageType imageType) {
        Set<String> groups = new TreeSet<String>(userAlgorithms.get(imageType).keySet());
        groups.remove(CURRENT);
        return groups.toArray(new String[groups.size()]);
    }

    public void addAlgorithm(ImageType imageType, String groupName, String algorithmName, Map<PixelleComponent, String> algorithm) {
        Map<String, Map<String, Map<PixelleComponent, String>>> type = userAlgorithms.get(imageType);
        if (type == null) {
            type = new HashMap<String, Map<String, Map<PixelleComponent, String>>>();
            userAlgorithms.put(imageType, type);
        }
        Map<String, Map<PixelleComponent, String>> group = type.get(groupName);
        if (group == null) {
            group = new HashMap<String, Map<PixelleComponent, String>>();
            type.put(groupName, group);
        }
        group.put(algorithmName, new EnumMap<PixelleComponent, String>(algorithm));
    }

    public void setCurrent(ImageType imageType, Map<PixelleComponent, String> algorithm) {
        addAlgorithm(imageType, AlgorithmArchiver.CURRENT, AlgorithmArchiver.CURRENT, algorithm);
    }

    public Map<PixelleComponent, String> getCurrent(ImageType imageType) {
        return getAlgorithm(imageType, AlgorithmArchiver.CURRENT, AlgorithmArchiver.CURRENT);
    }

    public void removeAlgorithm(ImageType imageType, String group, String name) {
        Map<String, Map<String, Map<PixelleComponent, String>>> type = userAlgorithms.get(imageType);
        if (type != null) {
            Map<String, Map<PixelleComponent, String>> algoGroup = type.get(group);
            if (algoGroup != null) {
                algoGroup.remove(name);
            }
        }
    }

    public void save() {
        OutputStream xmlOut = null;
        try {
            File pixelleDir = new File(System.getProperty("user.home"), PIXELLE);
            pixelleDir.mkdirs();
            File algoFile = new File(pixelleDir, ALGORITHMS_FILE);
            xmlOut = new BufferedOutputStream(new FileOutputStream(algoFile));
            writeAlgorithms(xmlOut, userAlgorithms);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Closer.close(xmlOut);
        }
    }

    private void loadSystemAlgorithms() {
        InputStream xmlIs = null;
        try {
            xmlIs = AlgorithmArchiver.class.getResourceAsStream(SYSTEM_ALGO_XML_PATH);
            parseAlgorithms(xmlIs, systemAlgorithms);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Closer.close(xmlIs);
        }
    }

    private void loadUserAlgorithms() {
        InputStream is = null;
        try {
            File pixelleDir = new File(System.getProperty("user.home"), PIXELLE);
            pixelleDir.mkdirs();
            File algoFile = new File(pixelleDir, ALGORITHMS_FILE);
            if (algoFile.exists() && algoFile.isFile()) {
                is = new BufferedInputStream(new FileInputStream(algoFile));
                parseAlgorithms(is, userAlgorithms);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            Closer.close(is);
        }
    }

    private void writeAlgorithms(OutputStream is, Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> algorithms) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(is));
            pw.println("<algorithms xmlns='http://pixelle.mebigfatguy.com/" + Version.getVersion() + "'");
            pw.println("            xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
            pw.println("            xsi:schemaLocation='/com/mebigfatguy/pixelle/resources/algorithms.xsd'>");
            for (Map.Entry<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> type : algorithms.entrySet()) {
                pw.println("    <type name='" + type.getKey() + "'>");
                for (Map.Entry<String, Map<String, Map<PixelleComponent, String>>> group : type.getValue().entrySet()) {
                    pw.println("        <group name='" + group.getKey() + "'>");
                    for (Map.Entry<String, Map<PixelleComponent, String>> algorithm : group.getValue().entrySet()) {
                        pw.println("            <algorithm name='" + algorithm.getKey() + "'>");
                        for (Map.Entry<PixelleComponent, String> component : algorithm.getValue().entrySet()) {
                            pw.println("                <component name='" + component.getKey().name().toLowerCase() + "'>");
                            pw.println("                    " + XMLEncoder.xmlEncode(component.getValue()));
                            pw.println("                </component>");
                        }
                        pw.println("            </algorithm>");
                    }
                    pw.println("        </group>");
                }
                pw.println("    </type>");
            }
            pw.println("</algorithms>");
            pw.flush();
        } finally {
            Closer.close(pw);
        }
    }

    private void parseAlgorithms(InputStream is, final Map<ImageType, Map<String, Map<String, Map<PixelleComponent, String>>>> algorithms) throws IOException, SAXException {
        XMLReader r = XMLReaderFactory.createXMLReader();
        r.setContentHandler(new DefaultHandler() {

            Map<String, Map<String, Map<PixelleComponent, String>>> currentType = null;

            Map<String, Map<PixelleComponent, String>> currentGroup = null;

            Map<PixelleComponent, String> currentAlgorithm = null;

            String currentComponentName = null;

            StringBuilder algorithmText = null;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes atts) {
                if (TYPE.equals(localName)) {
                    currentType = new HashMap<String, Map<String, Map<PixelleComponent, String>>>();
                    algorithms.put(ImageType.valueOf(atts.getValue(NAME)), currentType);
                } else if (GROUP.equals(localName)) {
                    currentGroup = new HashMap<String, Map<PixelleComponent, String>>();
                    currentType.put(atts.getValue(NAME), currentGroup);
                } else if (ALGORITHM.equals(localName)) {
                    currentAlgorithm = new EnumMap<PixelleComponent, String>(PixelleComponent.class);
                    currentGroup.put(atts.getValue(NAME), currentAlgorithm);
                } else if (COMPONENT.equals(localName)) {
                    currentComponentName = atts.getValue(NAME);
                    algorithmText = new StringBuilder();
                }
            }

            @Override
            public void characters(char[] c, int start, int offset) {
                if (currentComponentName != null) {
                    algorithmText.append(c, start, offset);
                }
            }

            @Override
            public void endElement(String uri, String localName, String qName) {
                if (COMPONENT.equals(localName)) {
                    PixelleComponent pc = PixelleComponent.valueOf(currentComponentName.toUpperCase());
                    currentAlgorithm.put(pc, algorithmText.toString().trim());
                    algorithmText = null;
                    currentComponentName = null;
                } else if (ALGORITHM.equals(localName)) {
                    currentAlgorithm = null;
                } else if (GROUP.equals(localName)) {
                    currentGroup = null;
                } else if (TYPE.equals(localName)) {
                    currentType = null;
                }
            }
        });
        r.parse(new InputSource(is));
    }
}
