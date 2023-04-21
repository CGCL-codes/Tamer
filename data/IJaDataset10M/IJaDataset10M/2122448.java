package bee.core;

import java.awt.Image;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A container for managing texture atlases.
 *
 * @author boto
 */
public class TextureAtlasContainer {

    protected HashMap<String, TextureAtlas> atlases = new HashMap<String, TextureAtlas>();

    protected Date firstCreated = null;

    protected Date lastModified = null;

    protected String fileName = new String();

    protected String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Create a texture atlas container.
     *
     */
    public TextureAtlasContainer() {
    }

    /**
     * Get a sorted list of atlas names.
     * 
     */
    public List<String> getAtlasNames() {
        List<String> names = new ArrayList<String>();
        Set<Entry<String, TextureAtlas>> keys = atlases.entrySet();
        for (Entry<String, TextureAtlas> s : keys) {
            names.add(s.getKey());
        }
        Collections.sort(names);
        return names;
    }

    /**
     * Get an atlas given its name.
     * Return null if the name does not exsist.
     */
    public TextureAtlas getAtlas(String atlasname) {
        TextureAtlas atlas = atlases.get(atlasname);
        return atlas;
    }

    /**
     * Add an new atlas given.
     * Return false if the name already exsists.
     */
    public boolean addAtlas(TextureAtlas atlas) {
        if (atlases.containsKey(atlas.getName())) {
            return false;
        }
        atlases.put(atlas.getName(), atlas);
        return true;
    }

    /**
     * Remove an atlas.
     * Returns false if no atlas with given name exists.
     */
    public boolean removeAtlas(String atlasname) {
        if (!atlases.containsKey(atlasname)) {
            return false;
        }
        atlases.remove(atlasname);
        return true;
    }

    /**
     * Get the atlas filename.
     *
     */
    public String getAtlasFilename() {
        return fileName;
    }

    /**
     * Set the atlas filename.
     * 
     */
    public void setAtlasFilename(String filename) {
        fileName = filename;
    }

    /**
     * Clear the texture atlas container.
     * 
     */
    public void clear() {
        atlases.clear();
        firstCreated = null;
        lastModified = null;
    }

    /**
     * Load a collection of atlases from given resource.
     *
     */
    public void loadAtlases(String resname) throws Exception {
        Log.verbose("loading atlas container from " + resname);
        fileName = resname;
        atlases.clear();
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputStream in = Resource.get().getFileStream(resname);
        Document doc = db.parse(in);
        doc.getDocumentElement().normalize();
        Node top = doc.getFirstChild();
        NodeList nodes = top.getChildNodes();
        for (int cnt = 0; cnt < nodes.getLength(); cnt++) {
            Node node = nodes.item(cnt);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element elem = (Element) node;
            String elemname = elem.getNodeName();
            if (elemname.equalsIgnoreCase("firstcreated")) {
                String date = elem.getAttribute("date");
                if (!date.isEmpty()) {
                    try {
                        firstCreated = new SimpleDateFormat(DATE_FORMAT).parse(date);
                    } catch (Exception e) {
                        Log.warning(getClass().getSimpleName() + ": could not parse the 'firstcreated' date\n reason: " + e);
                    }
                }
            } else if (elemname.equalsIgnoreCase("lastmodified")) {
                String date = elem.getAttribute("date");
                if (!date.isEmpty()) {
                    try {
                        lastModified = new SimpleDateFormat(DATE_FORMAT).parse(date);
                    } catch (Exception e) {
                        Log.warning(getClass().getSimpleName() + ": could not parse the 'lastmodified' date\n reason: " + e);
                    }
                }
            } else if (elemname.equalsIgnoreCase("atlas")) {
                try {
                    readAtlas(elem);
                } catch (Exception e) {
                    Log.error(getClass().getSimpleName() + ": problem occured during atlas parsing\n reason: " + e);
                }
            } else {
                Log.error(getClass().getSimpleName() + ": invalid atlas file format detected, ignoring ...");
            }
        }
    }

    /**
     * Read a single atlas definition from given element.
     *
     */
    private void readAtlas(Element atlas) throws Exception {
        TextureAtlas ta = new TextureAtlas();
        Node child = atlas.getFirstChild();
        String atlasname = atlas.getAttribute("name");
        String filepath = atlas.getAttribute("file");
        if (!atlasname.isEmpty()) {
            ta.name = atlasname;
        }
        if (!filepath.isEmpty()) {
            ta.filePath = filepath;
        }
        ta.loadAtlasImage(filepath, ta.name);
        for (; child != null; child = child.getNextSibling()) {
            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element elem = (Element) child;
            String elemname = child.getNodeName();
            if (elemname.equalsIgnoreCase("tile")) {
                if (ta.getAtlasImage() == null) {
                    Log.error(getClass().getSimpleName() + ": cannot create tile, no atlas image was loaded (" + ta.name + ")");
                    continue;
                }
                String name = elem.getAttribute("name");
                String area = elem.getAttribute("area");
                if ((name == null) || (area == null)) {
                    Log.error(getClass().getSimpleName() + ": cannot create tile, missing name or area attribute (" + ta.name + ")");
                    continue;
                }
                try {
                    ta.createTile(name, new Vec4dInt().fromString(area));
                } catch (Exception e) {
                    Log.error(getClass().getSimpleName() + ": cannot create tile for atlas '" + ta.name + "'\n reason: " + e);
                    continue;
                }
                Image im = ta.getTileImage(name);
                Log.verbose(" created tile " + name + ", area " + area + ", size " + im.getWidth(null) + " " + im.getHeight(null));
            }
        }
        atlases.put(ta.name, ta);
    }

    /**
     * Save atlases to given file.
     *
     */
    public void saveAtlases(String filename) throws Exception {
        Log.verbose("saving atlas container to " + filename);
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.newDocument();
        Node top = doc.createElement("AtlasCollection");
        doc.appendChild(top);
        doc.setXmlStandalone(true);
        Element elem = doc.createElement("FirstCreated");
        if (firstCreated == null) {
            firstCreated = new Date();
        }
        elem.setAttribute("date", new SimpleDateFormat(DATE_FORMAT).format(firstCreated));
        top.appendChild(elem);
        elem = doc.createElement("LastModified");
        lastModified = new Date();
        elem.setAttribute("date", new SimpleDateFormat(DATE_FORMAT).format(lastModified));
        top.appendChild(elem);
        Set<Entry<String, TextureAtlas>> keys = atlases.entrySet();
        for (Entry<String, TextureAtlas> key : keys) {
            TextureAtlas ta = key.getValue();
            elem = doc.createElement("Atlas");
            elem.setAttribute("name", ta.name);
            elem.setAttribute("file", ta.filePath);
            top.appendChild(elem);
            List<String> tilenames = ta.getTileNames();
            for (int cnt = 0; cnt < tilenames.size(); cnt++) {
                Element tileelem = doc.createElement("Tile");
                elem.appendChild(tileelem);
                String tname = tilenames.get(cnt);
                tileelem.setAttribute("name", tname);
                Vec2dInt pos = ta.getTilePosition(tname);
                Vec2dInt size = ta.getTileSize(tname);
                Vec4dInt area = new Vec4dInt(pos.x, pos.y, pos.x + size.x, pos.y + size.y);
                tileelem.setAttribute("area", area.toString());
            }
        }
        doc.normalize();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlstring = result.getWriter().toString();
        FileWriter f = new FileWriter(filename);
        f.write(xmlstring);
        f.close();
    }
}
