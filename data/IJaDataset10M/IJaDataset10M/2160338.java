package org.cubictest.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.cubictest.common.URLList;
import org.cubictest.common.URLMapper;
import org.cubictest.common.utils.XmlUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * File for writing URLmappings to file and reading them back.
 * 
 * @author ovstetun
 *
 */
public class URLListPersistance {

    /** The default filename containing URLMappings in an CubicTest projecs. */
    private static String fileName = "StartPoints.urls";

    /** Creates a empty list of URLMappings for writing to a xml file */
    public static String createEmptyUrlList() {
        return "<urlMappings />";
    }

    /**
	 * Reads a file for URLMappings. Eclipse-internal method.
	 * 
	 * @param file The file to read from.
	 * @return The mappings contained in the file.
	 */
    public static URLList fromFile(IFile file) {
        return fromFile(file.getLocation().toFile());
    }

    /**
	 * Reads a list of URLMappings from the given file. 
	 * 
	 * @param file The file to read from.
	 * @return The mappings contained in the file.
	 */
    public static URLList fromFile(File file) {
        URLList result = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            Document document = builder.build(file);
            result = fromElement(document.getRootElement());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * Finds the URLMappings in a project. Assumes the default filename is used.
	 * 
	 * @param project The active project.
	 * @return The mappings found.
	 */
    public static URLList findInProject(IProject project) {
        File f = project.getFile(fileName).getLocation().toFile();
        return fromFile(f);
    }

    public static void saveInProject(IProject project, URLList urlList) {
        IFile iFile = project.getFile(fileName);
        toFile(urlList, iFile);
    }

    /**
	 * Converts from xml elements to URLMappings.
	 * 
	 * @param element The base element.
	 * @return The resulting mappings.
	 */
    private static URLList fromElement(Element element) {
        URLList result = new URLList();
        List list = element.getChildren("mapping");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element mapper = (Element) it.next();
            String id = mapper.getAttributeValue("id");
            String name = mapper.getAttributeValue("name");
            String url = mapper.getAttributeValue("url");
            URLMapper urlMapper = new URLMapper();
            urlMapper.setId(id);
            urlMapper.setName(name);
            urlMapper.setUrl(url);
            result.addURL(urlMapper);
        }
        return result;
    }

    /**
	 * Writes URLMappings to file. Eclipse-internal method.
	 * 
	 * @param list The mappings to write.
	 * @param iFile The file to write to.
	 */
    public static void toFile(URLList list, IFile iFile) {
        Element rootElement = toElement(list);
        try {
            Document document = new Document(rootElement);
            FileWriter fw = new FileWriter(iFile.getLocation().toFile());
            XmlUtils.getNewXmlOutputter().output(document, fw);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
	 * Converts all URLMappings to xml elements.
	 * 
	 * @param list The mappings to convert.
	 * @return The resulting xml elements.
	 */
    private static Element toElement(URLList list) {
        Element result = new Element("urlMappings");
        Object[] mappers = (Object[]) list.getAllUrls();
        for (int i = 0; i < mappers.length; i++) {
            URLMapper m = (URLMapper) mappers[i];
            Element e = new Element("mapping");
            e.setAttribute("id", m.getId());
            e.setAttribute("name", m.getName());
            e.setAttribute("url", m.getUrl());
            result.addContent(e);
        }
        return result;
    }
}
