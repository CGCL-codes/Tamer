package DVDPro2Sage;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

/**
 * @author Chad
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DVDPro2Sage {

    static Document document;

    static NodeList DVDs;

    private static String descriptionTag = "Description";

    private Vector<String> titles = new Vector<String>();

    private Vector<String> safeTitles = new Vector<String>();

    private Vector<String> ids = new Vector<String>();

    private Map<String, Integer> folderTitles = new HashMap<String, Integer>();

    private int numTitles = 0;

    private XPath xpath = XPathFactory.newInstance().newXPath();

    private int dvdProfilerVersion = 2;

    public DVDPro2Sage(String f) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(f));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException sxe) {
            sxe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Element collection = (Element) document.getElementsByTagName("Collection").item(0);
        NodeList dvdElements = collection.getChildNodes();
        int dvdElementsLength = dvdElements.getLength();
        for (int i = 0; i < dvdElementsLength; i++) {
            if (dvdElements.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element currDvd = (Element) dvdElements.item(i);
                NodeList currDvdDiscs = currDvd.getElementsByTagName("Disc");
                String currDvdTitle = currDvd.getElementsByTagName("Title").item(0).getFirstChild().getNodeValue();
                for (int j = 0; j < currDvdDiscs.getLength(); j++) {
                    int discNum = j + 1;
                    Element newDvd = (Element) currDvd.cloneNode(true);
                    Element disc = (Element) currDvdDiscs.item(j);
                    String discTitle = currDvdTitle + " - Disc " + discNum;
                    String discFolder = discTitle;
                    if (disc.getElementsByTagName("DescriptionSideA").item(0).getFirstChild() != null) {
                        discFolder = disc.getElementsByTagName("DescriptionSideA").item(0).getFirstChild().getNodeValue();
                    }
                    newDvd.getElementsByTagName("Title").item(0).getFirstChild().setNodeValue(discTitle);
                    if (newDvd.getElementsByTagName("OriginalTitle").item(0).getFirstChild() == null) {
                        newDvd.getElementsByTagName("OriginalTitle").item(0).appendChild(document.createTextNode(""));
                    }
                    newDvd.getElementsByTagName("OriginalTitle").item(0).getFirstChild().setNodeValue(discFolder);
                    Element parentTitle = document.createElement("ParentTitle");
                    parentTitle.appendChild(document.createTextNode(currDvdTitle));
                    newDvd.appendChild(parentTitle);
                    collection.appendChild(newDvd);
                }
            }
        }
        if (document.getElementsByTagName("MediaType").getLength() > 0) {
            dvdProfilerVersion = 3;
            descriptionTag = "DistTrait";
        }
        try {
            DVDs = (NodeList) xpath.evaluate("/Collection/DVD", document, XPathConstants.NODESET);
        } catch (XPathException e1) {
            e1.printStackTrace();
        }
        numTitles = getNumCollection();
        for (int i = 0; i < DVDs.getLength(); i++) {
            titles.add(getTitle(i));
            safeTitles.add(getSafeTitle(i));
            ids.add(getID(i));
            folderTitles.put(getFolder(i), new Integer(i));
        }
    }

    private Node getNodeByTagName(int n, String tag) {
        Element e = (Element) DVDs.item(n);
        NodeList t = e.getElementsByTagName(tag);
        Node node = null;
        for (int i = 0; i < t.getLength(); i++) {
            if (t.item(i).getParentNode().getNodeName() == "DVD") node = t.item(i);
        }
        return node;
    }

    private String getNodeValueByTagName(int n, String tag) {
        String value = "";
        Node node = getNodeByTagName(n, tag);
        if (node != null) {
            if (node.getFirstChild() != null) {
                value = node.getFirstChild().getNodeValue();
            }
        }
        return value;
    }

    private Element getDVDRootElement(int n, String element) {
        NodeList l = ((Element) DVDs.item(n)).getElementsByTagName(element);
        int num = 0;
        for (int i = 0; i < l.getLength(); i++) {
            if (l.item(i).getParentNode().getNodeName() == "DVD") num = i;
        }
        return (Element) l.item(num);
    }

    private String getDVDRootValue(int n, String element) {
        System.out.println(element);
        String s = "";
        Element e = getDVDRootElement(n, element);
        if (e != null) {
            s = e.getFirstChild().getNodeValue();
        }
        return s;
    }

    public Vector<String> getTitles() {
        return titles;
    }

    public void writeDVD(int i, String name) {
        Node node = DVDs.item(i);
        File out = new File(name);
        File stylesheet = new File("D:\\eclipse\\workspace\\DVDPro2XML\\dvdpro2xml\\DVDProfiler.xsl");
        StreamSource stylesource = new StreamSource(stylesheet);
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(stylesource);
            DOMSource source = new DOMSource(node);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) x = tce.getException();
            x.printStackTrace();
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) x = te.getException();
            x.printStackTrace();
        }
    }

    public void printDVD(Node node) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource((Node) node);
            StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) x = tce.getException();
            x.printStackTrace();
        } catch (TransformerException te) {
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) x = te.getException();
            x.printStackTrace();
        }
    }

    public void writeAllDVDs(String path, String imagePath, boolean copy) {
        if (!path.endsWith("\\")) {
            path = path + "\\";
        }
        if (!imagePath.endsWith("\\")) {
            imagePath = imagePath + "\\";
        }
        for (int i = 0; i < numTitles; i++) {
            File dir = new File(path + getSafeTitle(i));
            dir.mkdirs();
            writeDVD(i, path + getSafeTitle(i) + "\\" + getSafeTitle(i) + ".xml");
            if (copy) {
                copyImage(imagePath + getID(i) + "f.jpg", path + getSafeTitle(i) + "\\front.jpg");
                copyImage(imagePath + getID(i) + "b.jpg", path + getSafeTitle(i) + "\\back.jpg");
            }
        }
    }

    public int findDVD(String name) {
        int index = titles.indexOf(name);
        if (index == -1) {
            name = name.toLowerCase();
            index = safeTitles.indexOf(name);
            if (index == -1) {
                Integer indexInt = (Integer) folderTitles.get(name);
                if (indexInt != null) {
                    index = indexInt.intValue();
                    System.out.println("found orig title for " + name);
                }
            }
        }
        return index;
    }

    public Vector<String> searchDVD(String name) {
        Vector<String> matches = new Vector<String>();
        for (int i = 0; i < titles.size(); i++) {
            String temp = titles.get(i).toString();
            if (temp.startsWith(name)) {
                matches.add(titles.get(i));
            }
        }
        return matches;
    }

    private boolean isEpisodic(String name) {
        Vector<String> found = searchDVD(name);
        if (found.size() > 1) {
            return true;
        } else {
            return false;
        }
    }

    private String extractTitle(String s1, String delim) {
        String[] split1 = s1.split(delim);
        String title = new String();
        int i = 1;
        Vector<String> alternates = searchDVD(split1[0]);
        String alternate = alternates.get(i).toString();
        while (alternate.endsWith(s1) && i < alternates.size()) {
            i = i + 1;
            alternate = alternates.get(i).toString();
        }
        String[] split2 = alternate.split(delim);
        for (int j = 0; j < java.lang.Math.min(split1.length, split2.length) && (split1[j].equals(split2[j])); j++) {
            if (title.length() > 0) {
                title = title + delim;
            }
            title = title + split1[j];
        }
        if (title.length() < 1) {
            title = s1;
        }
        return title;
    }

    private String extractEpisode(String s1, String delim) {
        String title = extractTitle(s1, delim);
        String episode = new String();
        if (!title.equals(s1)) {
            episode = s1.substring(title.length() + 1, s1.length()).trim();
        }
        return episode;
    }

    public int getNumCollection() {
        return DVDs.getLength();
    }

    public String getID(int n) {
        return getNodeValueByTagName(n, "ID");
    }

    public String getTitle(int n) {
        return getNodeValueByTagName(n, "Title");
    }

    public String getParentTitle(int n) {
        if (getNodeValueByTagName(n, "ParentTitle") != "") {
            return getNodeValueByTagName(n, "ParentTitle");
        } else {
            return getNodeValueByTagName(n, "Title");
        }
    }

    public int getBoxSetParent(int n) {
        Element e = (Element) getNodeByTagName(n, "BoxSet");
        Node node = null;
        String value = "";
        int i = -1;
        if (e != null) {
            NodeList l = e.getElementsByTagName("Parent");
            if (l.getLength() > 0) {
                node = l.item(0).getFirstChild();
                if (node != null) {
                    value = node.getNodeValue();
                }
            }
        }
        if (value != "") {
            i = ids.indexOf(value);
        }
        return i;
    }

    public String getTitle(int n, boolean withoutEpisode) {
        String value = "";
        String delim = ":";
        value = getTitle(n);
        String[] s = value.split(delim);
        if (isEpisodic(s[0])) {
            value = extractTitle(value, delim);
        }
        return value;
    }

    public String getTitle(int n, boolean withoutEpisode, String delim) {
        String value = "";
        value = getTitle(n);
        String[] s = value.split(delim);
        if (isEpisodic(s[0])) {
            value = extractTitle(value, delim);
        }
        return value;
    }

    public String getEpisode(int n) {
        String value = "";
        String delim = ":";
        value = getTitle(n);
        String[] s = value.split(delim);
        if (isEpisodic(s[0])) {
            value = extractEpisode(value, delim);
        } else {
            value = "";
        }
        return value;
    }

    public String getEpisode(int n, String delim) {
        String value = "";
        value = getTitle(n);
        String[] s = value.split(delim);
        if (isEpisodic(s[0])) {
            value = extractEpisode(value, delim);
        } else {
            value = "";
        }
        return value;
    }

    public String getDescription(int n) {
        return getNodeValueByTagName(n, descriptionTag);
    }

    public String getSafeTitle(int n) {
        String value = "";
        value = getTitle(n);
        value = value.replaceAll("[^a-zA-Z0-9$()#_'`~@{}%!\\. &-]|\\.$", "");
        value = value.toLowerCase();
        return value;
    }

    public String getProductionYear(int n) {
        return getNodeValueByTagName(n, "ProductionYear");
    }

    public long getReleased(int n) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date ret = null;
        long released = 0;
        String value = getNodeValueByTagName(n, "Released");
        if (value != "" && value != null) {
            try {
                ret = df.parse(value);
                released = ret.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return released;
    }

    public Vector<String> getGenres(int n) {
        Vector<String> value = new Vector<String>();
        Element e = getDVDRootElement(n, "Genres");
        NodeList genres = e.getElementsByTagName("Genre");
        for (int i = 0; i < genres.getLength(); i++) {
            if (genres.item(i).getFirstChild() != null) {
                value.add(genres.item(i).getFirstChild().getNodeValue());
            }
        }
        return value;
    }

    public String[] getAudio(int n) {
        Element e = getDVDRootElement(n, "Audio");
        String audioTrackTag = "";
        String[] value = new String[0];
        if (dvdProfilerVersion == 2) audioTrackTag = "AudioFormat"; else if (dvdProfilerVersion == 3) audioTrackTag = "AudioTrack";
        if (audioTrackTag != "") {
            NodeList Tracks = e.getElementsByTagName(audioTrackTag);
            String audioTrack = "";
            value = new String[Tracks.getLength()];
            for (int i = 0; i < Tracks.getLength(); i++) {
                audioTrack = "";
                Element track = (Element) Tracks.item(i);
                if (dvdProfilerVersion == 2) {
                    e = (Element) track.getElementsByTagName("AudioLanguage").item(0);
                    if (e != null) {
                        audioTrack = e.getFirstChild().getNodeValue();
                    }
                    e = (Element) track.getElementsByTagName("AudioCompression").item(0);
                    if (e != null) {
                        audioTrack = audioTrack + " " + e.getFirstChild().getNodeValue();
                    }
                    e = (Element) track.getElementsByTagName("AudioChannels").item(0);
                    if (e != null) {
                        audioTrack = audioTrack + " " + e.getFirstChild().getNodeValue();
                    }
                }
                if (dvdProfilerVersion == 3) {
                    e = (Element) track.getElementsByTagName("AudioContent").item(0);
                    if (e != null) {
                        audioTrack = e.getFirstChild().getNodeValue();
                    }
                    e = (Element) track.getElementsByTagName("AudioFormat").item(0);
                    if (e != null) {
                        audioTrack = audioTrack + " " + e.getFirstChild().getNodeValue();
                    }
                }
                value[i] = audioTrack;
            }
        }
        return value;
    }

    public String getAR(int n) {
        return getNodeValueByTagName(n, "FormatAspectRatio");
    }

    public boolean isLetterbox(int n) {
        boolean isLetterbox = false;
        String value = getNodeValueByTagName(n, "FormatLetterBox");
        if (value != null && value.compareToIgnoreCase("True") == 0) {
            isLetterbox = true;
        }
        return isLetterbox;
    }

    public boolean isAnamorphic(int n) {
        boolean isAnamorphic = false;
        String value = getDVDRootValue(n, "Format16X9");
        if (value != null && value.compareToIgnoreCase("True") == 0) {
            isAnamorphic = true;
        }
        return isAnamorphic;
    }

    public Vector<String> getStudios(int n) {
        Element e = getDVDRootElement(n, "Studios");
        Vector<String> value = new Vector<String>();
        NodeList nodes = e.getElementsByTagName("Studio");
        for (int i = 0; i < nodes.getLength(); i++) {
            value.add(nodes.item(i).getFirstChild().getNodeValue());
        }
        return value;
    }

    public Vector<String> getDirectors(int n) {
        Node node = DVDs.item(n);
        node.normalize();
        Node Director = findNode(node, "Credits");
        Node Name;
        String name = "";
        Vector<String> value = new Vector<String>();
        if (Director != null && Director.hasChildNodes()) {
            Director = Director.getFirstChild();
            while (Director != null && Director.getNextSibling() != null) {
                Director = Director.getNextSibling();
                if (Director.getFirstChild() != null && Director.getNodeName() == "Credit") {
                    Name = Director.getFirstChild();
                    while (Name.getNextSibling() != null) {
                        Name = Name.getNextSibling();
                        if (Name.getFirstChild() != null && (Name.getNodeName() == "FirstName" || Name.getNodeName() == "MiddleName" || Name.getNodeName() == "LastName")) {
                            name = name + Name.getFirstChild().getNodeValue() + " ";
                        }
                    }
                    value.add(name);
                    name = "";
                }
            }
        }
        return value;
    }

    public Vector<String> getDirectorsSubtypes(int n) {
        Node node = DVDs.item(n);
        node.normalize();
        Node Director = findNode(node, "Credits");
        Node Name;
        String name = "";
        Vector<String> value = new Vector<String>();
        if (Director != null && Director.hasChildNodes()) {
            Director = Director.getFirstChild();
            while (Director != null && Director.getNextSibling() != null) {
                Director = Director.getNextSibling();
                if (Director.getFirstChild() != null && Director.getNodeName() == "Credit") {
                    Name = Director.getFirstChild();
                    while (Name.getNextSibling() != null) {
                        Name = Name.getNextSibling();
                        if (Name.getFirstChild() != null && Name.getNodeName() == "CreditSubtype") {
                            name = Name.getFirstChild().getNodeValue();
                            break;
                        }
                    }
                    value.add(name);
                    name = "";
                }
            }
        }
        return value;
    }

    public Node findNode(Node node, String tag) {
        NodeList nodes = node.getChildNodes();
        int i = 0;
        for (i = 0; nodes.item(i) != null && nodes.item(i).getNodeName() != tag; i++) {
        }
        return nodes.item(i);
    }

    public String[] getActors(int n) {
        Element e = getDVDRootElement(n, "Actors");
        NodeList Actors = e.getElementsByTagName("Actor");
        String actorName = "";
        String first = "";
        String middle = "";
        String last = "";
        String[] value = new String[Actors.getLength()];
        for (int i = 0; i < Actors.getLength(); i++) {
            actorName = "";
            first = "";
            middle = "";
            last = "";
            Element actor = (Element) Actors.item(i);
            if (dvdProfilerVersion == 2) {
                e = (Element) actor.getElementsByTagName("FirstName").item(0);
                if (e != null) {
                    first = e.getFirstChild().getNodeValue() + " ";
                }
                e = (Element) actor.getElementsByTagName("MiddleName").item(0);
                if (e != null) {
                    middle = e.getFirstChild().getNodeValue() + " ";
                }
                e = (Element) actor.getElementsByTagName("LastName").item(0);
                if (e != null) {
                    last = e.getFirstChild().getNodeValue();
                }
            }
            if (dvdProfilerVersion == 3) {
                first = actor.getAttribute("FirstName");
                middle = actor.getAttribute("MiddleName");
                last = actor.getAttribute("LastName");
                if (first.length() > 0) first = first + " ";
                if (middle.length() > 0) middle = middle + " ";
            }
            actorName = first + middle + last;
            value[i] = actorName;
        }
        return value;
    }

    public String[] getRoles(int n) {
        Element e = getDVDRootElement(n, "Actors");
        NodeList Actors = e.getElementsByTagName("Actor");
        String role = "";
        String[] value = new String[Actors.getLength()];
        for (int i = 0; i < Actors.getLength(); i++) {
            role = "";
            Element actor = (Element) Actors.item(i);
            if (dvdProfilerVersion == 2) {
                e = (Element) actor.getElementsByTagName("Role").item(0);
                if (e != null) {
                    role = e.getFirstChild().getNodeValue() + " ";
                }
            }
            if (dvdProfilerVersion == 3) {
                role = actor.getAttribute("Role");
                if (actor.getAttribute("Voice").compareTo("True") == 0) role = role + " (Voice)";
            }
            value[i] = role;
        }
        return value;
    }

    public String[] getCredits(int n) {
        Element e = getDVDRootElement(n, "Credits");
        NodeList Credits = e.getElementsByTagName("Credit");
        String creditName = "";
        String first = "";
        String middle = "";
        String last = "";
        String[] value = new String[Credits.getLength()];
        for (int i = 0; i < Credits.getLength(); i++) {
            creditName = "";
            first = "";
            middle = "";
            last = "";
            Element actor = (Element) Credits.item(i);
            if (dvdProfilerVersion == 2) {
                e = (Element) actor.getElementsByTagName("FirstName").item(0);
                if (e != null) {
                    first = e.getFirstChild().getNodeValue() + " ";
                }
                e = (Element) actor.getElementsByTagName("MiddleName").item(0);
                if (e != null) {
                    middle = e.getFirstChild().getNodeValue() + " ";
                }
                e = (Element) actor.getElementsByTagName("LastName").item(0);
                if (e != null) {
                    last = e.getFirstChild().getNodeValue();
                }
            }
            if (dvdProfilerVersion == 3) {
                first = actor.getAttribute("FirstName");
                middle = actor.getAttribute("MiddleName");
                last = actor.getAttribute("LastName");
                if (first.length() > 0) first = first + " ";
                if (middle.length() > 0) middle = middle + " ";
            }
            creditName = first + middle + last;
            value[i] = creditName;
        }
        return value;
    }

    public String[] getCreditsSubtypes(int n) {
        Element e = getDVDRootElement(n, "Credits");
        NodeList Credits = e.getElementsByTagName("Credit");
        String subtype = "";
        String[] value = new String[Credits.getLength()];
        for (int i = 0; i < Credits.getLength(); i++) {
            subtype = "";
            Element credit = (Element) Credits.item(i);
            if (dvdProfilerVersion == 2) {
                e = (Element) credit.getElementsByTagName("CreditSubtype").item(0);
                if (e != null) {
                    subtype = e.getFirstChild().getNodeValue();
                }
            }
            if (dvdProfilerVersion == 3) {
                subtype = credit.getAttribute("CreditSubtype");
            }
            value[i] = subtype;
        }
        return value;
    }

    public String[][] getSagePeopleAndRoleLists(int n, String[] validRoles, boolean appendCharacterRole) {
        String[][] lists = new String[2][];
        String[] actors = getActors(n);
        String[] characterRoles = null;
        List<String> actorList = new ArrayList<String>();
        List<String> roleList = new ArrayList<String>();
        if (appendCharacterRole) {
            characterRoles = getRoles(n);
        }
        for (int i = 0; i < actors.length; i++) {
            if (appendCharacterRole) {
                actorList.add(actors[i] + " (" + characterRoles[i] + ")");
            } else {
                actorList.add(actors[i]);
            }
            roleList.add("Actor");
        }
        if (validRoles != null) {
            String[] credits = getCredits(n);
            String[] creditsSubtypes = getCreditsSubtypes(n);
            List<String> validRoleList = Arrays.asList(validRoles);
            for (int i = 0; i < credits.length; i++) {
                if (validRoleList.contains(creditsSubtypes[i])) {
                    actorList.add(credits[i]);
                    roleList.add(creditsSubtypes[i]);
                }
            }
        }
        lists[0] = new String[actorList.size()];
        lists[1] = new String[roleList.size()];
        actorList.toArray(lists[0]);
        roleList.toArray(lists[1]);
        return lists;
    }

    public String[] getSagePeopleList(int n, String[] validRoles, boolean appendCharacterRole) {
        return getSagePeopleAndRoleLists(n, validRoles, appendCharacterRole)[0];
    }

    public String[] getSageRoleList(int n, String[] validRoles) {
        return getSagePeopleAndRoleLists(n, validRoles, false)[1];
    }

    public String[] makeSageRoles(int n) {
        String[] s = new String[n];
        for (int x = 0; x < n; x++) {
            s[x] = "Actor";
        }
        return s;
    }

    public String getOverview(int n) {
        String value = "";
        value = getNodeValueByTagName(n, "Overview");
        value = value.replaceAll("\\<.*?\\>", "");
        return value;
    }

    public String getFolder(int n) {
        Node node = DVDs.item(n);
        node.normalize();
        Node Property = node.getFirstChild();
        String value = "";
        while (Property.getNodeName() != "OriginalTitle" && Property.getNextSibling() != null) {
            Property = Property.getNextSibling();
        }
        if (Property.getFirstChild() != null) {
            value = Property.getFirstChild().getNodeValue().toLowerCase();
        }
        return value;
    }

    public long getDuration(int n) {
        Node node = DVDs.item(n);
        node.normalize();
        String value = "";
        int Duration = 0;
        value = getDVDRootValue(n, "RunningTime");
        if (value != null) {
            Duration = Integer.parseInt(value);
        }
        Duration = Duration * 60 * 1000;
        return Duration;
    }

    public String getRated(int n) {
        return getNodeValueByTagName(n, "Rating");
    }

    public void copyImage(String from, String to) {
        File inputFile = new File(from);
        File outputFile = new File(to);
        try {
            if (inputFile.canRead()) {
                FileInputStream in = new FileInputStream(inputFile);
                FileOutputStream out = new FileOutputStream(outputFile);
                byte[] buf = new byte[65536];
                int c;
                while ((c = in.read(buf)) > 0) out.write(buf, 0, c);
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final String typeName[] = { "none", "Collection", "DVD", "ID", "UPC", "Title", "SortTitle", "Regions", "Region", "CollectionType", "CollectionNumber", "WishPriority", "Rating", "ProductionYear", "Released", "RunningTime", "CaseType", "Genres", "Genre", "Format", "FormatAspectRatio", "FormatVideoStandar", "FormatLetterbox", "FormatPanAndScan", "FormatFullFrame", "Format16X9", "FormatDualSided", "FormatDualLayered", "FormatFlipper", "Features", "FeatureSceneAccess", "FeatureCommentary", "FeatureTrailer", "FeatureDeletedScenes", "FeatureMakingOf", "FeatureProductionNotes", "FeatureGame", "FeatureDVDROM", "FeatureMultiAngle", "FeatureMusicVideo", "FeatureClosedCaption", "FeatureTHXCertified", "Studios", "Studio", "Audio", "AudioFormat", "AudioLanguage", "AudioCompression", "AudioChannels", "Subtitles", "Subtitle", "Directors", "Director", "Actors", "Actor", "FirstName", "MiddleName", "LastName", "Role", "Review", "ReviewFilm", "ReviewVideo", "ReviewAudio", "ReviewExtras", "SRPInfo", "SRP", "SRPCurrencyID", "SRPCurrencyName", "PurchaseInfo", "PurchasePriceInfo", "PurchasePrice", "PurchasePriceCurrencyID", "PurchasePriceCurrencyName", "PurchaseDate", "PurchasePlace", "PurchacePlaceType", "PurchasePlaceWebsite", "LoanInfo", "Overview", "EasterEggs", "LastEdited" };
}
