package net.sourceforge.bricksviewer.brick.io;

import java.util.*;
import org.jdom.Document;
import net.sourceforge.bricksviewer.brick.*;
import net.sourceforge.bricksviewer.xml.JarXMLLoadingException;

public class JarXMLBrickStyleLoader extends XMLBrickStyleLoader implements BrickStyleLoader {

    public BrickStyle load(String brickStyleID) throws BrickStyleLoadingException {
        BrickStyle brickStyle = null;
        Document document = null;
        String pathToXML;
        pathToXML = "/bricks/" + brickStyleID + ".xml";
        try {
            document = loadDocumentFromJar(pathToXML);
            if (document != null) {
                brickStyle = load(document);
            }
        } catch (JarXMLLoadingException e) {
            throw new BrickStyleLoadingException("Problem loading brick style from jar path '" + pathToXML + "'", e);
        }
        return brickStyle;
    }
}
