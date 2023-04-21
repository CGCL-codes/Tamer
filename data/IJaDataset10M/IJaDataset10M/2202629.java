package virtuallab;

import java.awt.*;
import java.awt.geom.Point2D;

public class Specimen extends SpecimenElement {

    private java.util.jar.JarFile jarFile;

    private java.io.File jarFileInfo;

    private java.util.LinkedHashMap controlDescriptors = new java.util.LinkedHashMap();

    private java.util.LinkedHashMap controlStates = new java.util.LinkedHashMap();

    private java.util.LinkedHashMap instrumentDescriptors = new java.util.LinkedHashMap();

    private java.util.LinkedHashMap imageDescriptors = new java.util.LinkedHashMap();

    private java.util.LinkedHashMap controlStateDescriptors = new java.util.LinkedHashMap();

    private Root root;

    private java.util.HashMap imageSets = new java.util.HashMap();

    public Specimen(java.io.File specimenJar) throws SpecimenException {
        try {
            this.jarFile = new java.util.jar.JarFile(specimenJar);
            java.util.jar.Manifest manifest = this.jarFile.getManifest();
            if (manifest == null) {
                throw new SpecimenException("Specimen archive contains no manifest.");
            }
            String specimenDefinitionFileName = manifest.getMainAttributes().getValue("Specimen-Definition");
            if (specimenDefinitionFileName == null || specimenDefinitionFileName.equals("")) {
                specimenDefinitionFileName = "specimen.xml";
            }
            java.util.jar.JarEntry specimenDefinitionEntry = this.jarFile.getJarEntry(specimenDefinitionFileName);
            if (specimenDefinitionEntry == null) {
                throw new SpecimenException("Specimen definition file missing from archive.");
            }
            try {
                java.io.InputStream specimenDefinitionXML = this.jarFile.getInputStream(specimenDefinitionEntry);
                if (specimenDefinitionXML == null) {
                    throw new SpecimenException("Unable to acquire input stream for specimen definition.");
                }
                this.load(specimenDefinitionXML);
                this.jarFileInfo = specimenJar;
                this.jarFile.close();
                this.jarFile = null;
            } catch (java.lang.Exception e) {
                throw new SpecimenException("Error attempting to read specimen definition.", e);
            }
        } catch (java.io.IOException e) {
            throw new SpecimenException("Error while attempting to open specimen.", e);
        }
    }

    public void close() {
        if (this.jarFile != null) {
            try {
                this.jarFile.close();
                this.jarFile = null;
            } catch (java.io.IOException e) {
                return;
            }
        }
    }

    public java.util.Iterator getControlDescriptors() {
        return this.controlDescriptors.values().iterator();
    }

    public java.io.InputStream getInputStream(String path) throws SpecimenException {
        try {
            if (this.jarFile == null) {
                this.jarFile = new java.util.jar.JarFile(this.jarFileInfo);
            }
            java.util.jar.JarEntry entry = this.jarFile.getJarEntry(path);
            if (entry == null) {
                throw new SpecimenException("Requested entry does not exist.");
            }
            java.io.InputStream stream = this.jarFile.getInputStream(entry);
            if (stream == null) {
                throw new SpecimenException("Unable to acquire input stream.");
            }
            return stream;
        } catch (java.io.IOException e) {
            throw new SpecimenException("Error while attempting to read specimen.", e);
        }
    }

    private void load(java.io.InputStream specimenDefinition) throws SpecimenException {
        try {
            javax.xml.parsers.DocumentBuilderFactory f = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            javax.xml.parsers.DocumentBuilder b = f.newDocumentBuilder();
            org.w3c.dom.Document doc = b.parse(specimenDefinition);
            loadElements(doc);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new SpecimenException("Error creating XML parser.", e);
        } catch (org.xml.sax.SAXException e) {
            throw new SpecimenException("Error parsing specimen XML file.", e);
        } catch (java.io.IOException e) {
            throw new SpecimenException("IO Error while parsing specimen XML file.", e);
        }
    }

    private void loadElements(org.w3c.dom.Document doc) throws SpecimenException {
        org.w3c.dom.NodeList nl = doc.getElementsByTagName("Specimen");
        if (nl.getLength() != 1) {
            throw new SpecimenException("Specimen definition file contains more than one Specimen element.");
        }
        this.loadElement(nl.item(0));
        nl = doc.getElementsByTagName("ImageDescriptor");
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Node n = nl.item(i);
            if (!isReference(n)) {
                ImageDescriptor desc = new ImageDescriptor(n);
                this.imageDescriptors.put(desc.getId(), desc);
            }
        }
        nl = doc.getElementsByTagName("InstrumentDescriptor");
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Node n = nl.item(i);
            if (!isReference(n)) {
                InstrumentDescriptor desc = new InstrumentDescriptor(n);
                this.instrumentDescriptors.put(desc.getId(), desc);
            }
        }
        nl = doc.getElementsByTagName("ControlDescriptor");
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Node n = nl.item(i);
            if (!isReference(n)) {
                ControlDescriptor desc = new ControlDescriptor(n);
                this.controlDescriptors.put(desc.getId(), desc);
            }
        }
        nl = doc.getElementsByTagName("ControlState");
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Node n = nl.item(i);
            if (!isReference(n) && hasElementId(n)) {
                ControlState desc = new ControlState(n);
                this.controlStateDescriptors.put(desc.getId(), desc);
            }
        }
        nl = doc.getElementsByTagName("ControlStates");
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Node n = nl.item(i);
            if (!isReference(n)) {
                ControlStates desc = new ControlStates(n);
                this.controlStates.put(desc.getId(), desc);
            }
        }
        nl = doc.getElementsByTagName("Root");
        this.root = new Root(nl.item(0));
        java.util.Iterator iter = this.controlStateDescriptors.values().iterator();
        while (iter.hasNext()) {
            ControlState cs = (ControlState) iter.next();
            cs.resolveReferences();
        }
    }

    private static java.util.ArrayList individuals;

    private static boolean isHandledIndividually(String elementName) {
        if (individuals == null) {
            individuals = new java.util.ArrayList();
            individuals.add("ImageDescriptor");
            individuals.add("InstrumentDescriptor");
            individuals.add("ControlDescriptor");
            individuals.add("ControlState");
            individuals.add("ControlStates");
            individuals.add("Root");
        }
        return individuals.contains(elementName);
    }

    protected boolean gutsOfLoadElement(org.w3c.dom.Node child) {
        return isHandledIndividually(child.getNodeName());
    }

    private java.util.Collection sortControlStates(java.util.Collection states) {
        if (states == null) {
            throw new java.lang.IllegalArgumentException("Null passed to sortControlStates()");
        }
        java.util.ArrayList sorted = new java.util.ArrayList(states.size());
        ControlStates css = this.root.states;
        for (int i = 0; i < states.size() && css != null; i++) {
            java.util.Iterator iter = states.iterator();
            while (iter.hasNext()) {
                ControlState cs = (ControlState) iter.next();
                if (cs != null && css.states.containsValue(cs)) {
                    sorted.add(cs);
                    css = cs.controlStates;
                    break;
                }
            }
        }
        return sorted;
    }

    public String getUniqueName() {
        return this.getNamedParam("UniqueName");
    }

    public ImageSet chooseImageSet(java.util.Collection states) {
        if (states == null || states.size() < 1) {
            states = this.getDefaultControlStates();
        } else {
            states = this.sortControlStates(states);
        }
        String path = this.root.getPathPart();
        java.util.Iterator iter = states.iterator();
        while (iter.hasNext()) {
            ControlState cs = (ControlState) iter.next();
            path += cs.getPathPart() + "/";
            if (cs.imageDescriptor != null) {
                ImageSet iset = (ImageSet) this.imageSets.get(path);
                if (iset == null) {
                    iset = new ImageSet(cs.imageDescriptor, path);
                    this.imageSets.put(path, iset);
                }
                return iset;
            }
        }
        return null;
    }

    public java.util.Collection getDefaultControlStates() {
        java.util.ArrayList retStates = new java.util.ArrayList();
        ControlStates css = this.root.states;
        while (css != null) {
            ControlState cs = css.getFirstState();
            retStates.add(cs);
            if (cs.controlStates == null) {
                break;
            }
            css = cs.controlStates;
        }
        return retStates;
    }

    public java.util.HashMap getValidControlStates(java.util.Collection currentStates) {
        java.util.HashMap validStates = new java.util.HashMap();
        ControlStates css = this.root.states;
        while (css != null) {
            validStates.put(css.getControlDescriptor(), css.states.values());
            ControlState cs = css.getFirstState();
            java.util.Iterator iter = currentStates.iterator();
            while (iter.hasNext()) {
                ControlState match = (ControlState) iter.next();
                if (css.states.containsValue(match)) {
                    cs = match;
                    break;
                }
            }
            if (cs != null) {
                css = cs.controlStates;
            } else {
                css = null;
            }
        }
        return validStates;
    }

    class ImageDescriptor extends SpecimenElement {

        private java.awt.Dimension tileSize;

        private java.awt.Dimension imageSize;

        private java.awt.geom.Point2D.Double pixelSize;

        public Dimension getTileSize() {
            return tileSize;
        }

        public Dimension getImageSize() {
            return imageSize;
        }

        public Point2D.Double getPixelSize() {
            return pixelSize;
        }

        ImageDescriptor(org.w3c.dom.Node n) {
            this.loadElement(n);
        }

        protected boolean gutsOfLoadElement(org.w3c.dom.Node child) {
            boolean retStatus = false;
            if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                if (child.getNodeName().equals("TileSize")) {
                    this.tileSize = this.loadDimensionAttribute(child);
                    retStatus = true;
                } else if (child.getNodeName().equals("PixelSize")) {
                    this.pixelSize = this.loadDoubleDimensionAttribute(child);
                    retStatus = true;
                } else if (child.getNodeName().equals("ImageSize")) {
                    this.imageSize = this.loadDimensionAttribute(child);
                    retStatus = true;
                }
            }
            return retStatus;
        }
    }

    public class InstrumentDescriptor extends SpecimenElement {

        private java.util.ArrayList infoRefs = new java.util.ArrayList();

        InstrumentDescriptor(org.w3c.dom.Node n) {
            this.loadElement(n);
        }

        protected boolean gutsOfLoadElement(org.w3c.dom.Node child) {
            if (child.getNodeName().equals("InfoRef")) {
                InfoRef ir = new InfoRef(child);
                this.infoRefs.add(ir);
            } else {
                return false;
            }
            return true;
        }
    }

    class InfoRef {

        String href;

        String text;

        InfoRef(org.w3c.dom.Node node) {
            org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
            org.w3c.dom.Node hrefNode = attrs.getNamedItem("href");
            if (hrefNode != null) {
                this.href = hrefNode.getNodeValue();
            }
            this.text = getFirstTextChild(node);
        }
    }

    public class ControlDescriptor extends SpecimenElement {

        ControlDescriptor(org.w3c.dom.Node n) {
            this.loadElement(n);
        }
    }

    public class ControlState extends SpecimenElement {

        private ControlDescriptor controlDescriptor;

        private ControlStates controlStates;

        private ImageDescriptor imageDescriptor;

        ControlState(org.w3c.dom.Node n) {
            this.loadElement(n);
        }

        public ControlDescriptor getControlDescriptor() {
            return this.controlDescriptor;
        }

        protected boolean gutsOfLoadElement(org.w3c.dom.Node node) throws SpecimenException {
            if (node.getNodeName().equals("ControlDescriptor")) {
                if (!isReference(node)) {
                    this.controlDescriptor = new ControlDescriptor(node);
                } else {
                    String cdescid = getElementRefId(node);
                    this.controlDescriptor = (ControlDescriptor) Specimen.this.controlDescriptors.get(cdescid);
                }
            } else {
                return false;
            }
            return true;
        }

        private void resolveReferences() {
            String cssid = (String) this.params.get("ControlStates.idref");
            if (cssid != null) {
                this.controlStates = (ControlStates) Specimen.this.controlStates.get(cssid);
            }
            String imgDescId = (String) this.params.get("ImageDescriptor.idref");
            if (imgDescId != null) {
                this.imageDescriptor = (ImageDescriptor) Specimen.this.imageDescriptors.get(imgDescId);
            }
        }

        private String getPathPart() {
            String pp = (String) this.getNamedParam("pathPart");
            return pp == null ? "" : pp;
        }
    }

    class ControlStates extends SpecimenElement {

        private java.util.LinkedHashMap states = new java.util.LinkedHashMap();

        ControlStates(org.w3c.dom.Node n) {
            this.loadElement(n);
        }

        protected boolean gutsOfLoadElement(org.w3c.dom.Node node) throws SpecimenException {
            if (node.getNodeName().equals("ControlState")) {
                if (!isReference(node)) {
                    ControlState cs = new ControlState(node);
                    states.put(cs.getId(), cs);
                    Specimen.this.controlStateDescriptors.put(cs.getId(), cs);
                } else {
                    String csid = getElementRefId(node);
                    ControlState cs = (ControlState) Specimen.this.controlStateDescriptors.get(csid);
                    if (cs != null) {
                        this.states.put(csid, cs);
                    }
                }
            } else {
                return false;
            }
            return true;
        }

        private ControlState getFirstState() {
            if (this.states.size() < 1) return null;
            java.util.Iterator iter = this.states.values().iterator();
            return (ControlState) iter.next();
        }

        public ControlDescriptor getControlDescriptor() {
            ControlState cs = this.getFirstState();
            return cs != null ? cs.getControlDescriptor() : null;
        }
    }

    class Root extends SpecimenElement {

        private ControlStates states;

        Root(org.w3c.dom.Node n) {
            this.loadElement(n);
        }

        protected boolean gutsOfLoadElement(org.w3c.dom.Node node) throws SpecimenException {
            if (node.getNodeName().equals("ControlStates")) {
                if (!isReference(node)) {
                    ControlStates css = new ControlStates(node);
                    this.states = css;
                    Specimen.this.controlStates.put(css.getId(), css);
                } else {
                    String cssid = getElementRefId(node);
                    ControlStates css = (ControlStates) Specimen.this.controlStates.get(cssid);
                    this.states = css;
                }
            } else {
                return false;
            }
            return true;
        }

        private String getPathPart() {
            String pp = (String) this.getNamedParam("pathPart");
            return pp == null ? "" : pp;
        }
    }
}
