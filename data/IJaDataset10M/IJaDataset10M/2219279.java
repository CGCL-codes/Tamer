package org.xmlcml.schemagen;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import org.xmlcml.schemagen.cml.base.CMLException;
import org.xmlcml.schemagen.cml.base.CMLType;

/**
 * 
 * <p>
 * make code for simple Content (text)
 * </p>
 * <xsd:complexType> <xsd:simpleContent> <xsd:extension base="stereoType">
 * <xsd:attributeGroup ref="atomRefs4"/> ... </xsd:extension>
 * </xsd:simpleContent> </xsd:complexType>
 * 
 * <pre>
 * </pre>
 * 
 * @author Peter Murray-Rust
 * @version 5.0
 * 
 */
public class TextProcessor extends AbstractProcessor {

    Map refMap = new HashMap();

    protected String name;

    protected String base = null;

    protected String id;

    protected CMLType schemaType;

    Map<String, TextProcessor> textMap = new HashMap<String, TextProcessor>();

    /**
     * constructor.
     * 
     * 
     */
    public TextProcessor() {
        super(CMLXSD_TEXT);
    }

    /**
     * constructor.
     * 
     * @param el
     * @throws CMLException
     */
    public TextProcessor(Element el) throws CMLException {
        super(CMLXSD_TEXT);
        processRecursively(el);
    }

    String getName() {
        return name;
    }

    void setName(String n) {
        name = n;
    }

    String getBase() {
        return base;
    }

    void setBase(String b) {
        base = b;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    protected boolean processRecursively(Element el) throws CMLException {
        boolean ok = true;
        String localName = el.getLocalName();
        if (super.processRecursively(el)) {
        } else if (localName.equals(CMLXSD_ATTRIBUTEGROUP)) {
        } else if (localName.equals(CMLXSD_CHOICE)) {
            processChoice(el);
        } else if (localName.equals(CMLXSD_COMPLEXTYPE)) {
            processComplexType(el);
        } else if (localName.equals(CMLXSD_ELEMENT)) {
            processElement(el);
        } else if (localName.equals(CMLXSD_EXTENSION)) {
            processExtension(el);
        } else if (localName.equals(CMLXSD_SIMPLECONTENT)) {
            processSimpleContent(el);
        } else if (localName.equals(CMLXSD_SEQUENCE)) {
        } else {
            logger.info("???unknown element??" + localName);
            ok = false;
        }
        return ok;
    }

    private void processSimpleContent(Element el) throws CMLException {
        processChildren(el);
    }

    private void processChoice(Element el) throws CMLException {
        processChildren(el);
    }

    private void processElement(Element el) throws CMLException {
        processChildren(el);
    }

    private void processComplexType(Element el) throws CMLException {
        processChildren(el);
    }

    private void processExtension(Element el) throws CMLException {
        base = el.getAttributeValue("base");
        if (base == null) {
            throw new CMLException("extension must have base");
        }
    }

    void processDirectory(String dirName) throws CMLException, IOException {
        Element root = new Element(CMLXSD_ROOT);
        File dir = new File(dirName);
        if (!dir.isDirectory()) {
            throw new CMLException("not directory: " + dirName);
        }
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].endsWith(".xsd")) {
                continue;
            }
            logger.info("======" + files[i] + "=======");
            Builder builder = new Builder();
            Document doc = null;
            try {
                doc = builder.build(new File(dir, files[i]));
            } catch (Exception e) {
                throw new CMLException("" + e);
            }
            TextProcessor ap = new TextProcessor(doc.getRootElement());
            if (ap.getBase() != null) {
                root.appendChild(ap);
                textMap.put(ap.getBase(), ap);
            }
        }
        TextWriter nodeG = new TextWriter(this, TEXT_LIST);
        nodeG.writeToFile(TEXT_DIR + F_S + TEXT_LIST + ".java");
    }
}
