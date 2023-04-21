package net.sf.force4maven.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {

    public static String stripExtension(String name) {
        int dotPos = name.lastIndexOf('.');
        if (dotPos > -1) {
            name = name.substring(0, dotPos);
        }
        return name;
    }

    public static String loadContent(File file) throws IOException {
        StringBuffer text = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            text.append(line).append("\n");
        }
        return text.toString();
    }

    public static String escapeReplacement(String in) {
        in = in.replace("\\", "\\\\");
        in = in.replace("$", "\\$");
        return in;
    }

    public static List<Element> getElementsByName(NodeList nodes, String name) {
        List<Element> result = new ArrayList<Element>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if ((node.getNodeType() == Node.ELEMENT_NODE) && node.getNodeName().equals(name)) {
                result.add((Element) node);
            }
        }
        return result;
    }

    public static Element getFirstElementByName(NodeList nodes, String name) {
        List<Element> result = getElementsByName(nodes, name);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
