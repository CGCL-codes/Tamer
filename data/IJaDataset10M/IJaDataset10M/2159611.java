package com.sitescape.team.samples.wsclient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

public class WSExport extends WSClientBase {

    public static void main(String[] args) {
        WSExport wsExport = new WSExport();
        if (args.length > 1) {
            System.err.println("Usage: WSExport [<root binder id>]");
            return;
        }
        Long rootBinderId = new Long(0);
        if (args.length == 1) {
            rootBinderId = Long.valueOf(args[0]);
        }
        try {
            File exportRoot = new File(safeName("export-" + rootBinderId + "-" + wsExport.host));
            exportRoot.mkdir();
            String xml = (String) wsExport.invokeWithCall("Facade", "getWorkspaceTreeAsXML", new Object[] { rootBinderId, new Integer(-1), "" }, null, exportRoot);
            Document document = DocumentHelper.parseText(xml);
            wsExport.createWorkspaceTree(document.getRootElement(), exportRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createWorkspaceTree(Element root, File parent) throws Exception {
        String type = root.attributeValue("type");
        if ("workspace".equals(type) || "folder".equals(type)) {
            System.err.println("Processing " + root.attributeValue("title"));
            File fsFolder = new File(parent, safeName(root));
            fsFolder.mkdir();
            File attributesFile = new File(fsFolder, "Attributes.xml");
            File completionFile = new File(fsFolder, "Completed");
            try {
                if (attributesFile.exists() && completionFile.exists() && completionFile.lastModified() > attributesFile.lastModified()) {
                    return;
                }
                attributesFile.createNewFile();
                XMLWriter writer = new XMLWriter(new FileWriter(attributesFile));
                writer.writeOpen(root);
                writer.writeClose(root);
                writer.close();
            } catch (IOException e) {
                System.err.println("Could not write Attributes file for folder " + root.attributeValue("id") + "-" + root.attributeValue("title"));
                System.err.println(e.getMessage());
            }
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                createWorkspaceTree(element, fsFolder);
            }
            if ("folder".equals(type)) {
                exportFolderEntries(root, fsFolder);
            }
            try {
                completionFile.createNewFile();
            } catch (IOException e) {
                System.err.println("Could not write Completion file for folder " + root.attributeValue("id") + "-" + root.attributeValue("title"));
                System.err.println("  Restart will export this folder again");
                System.err.println(e.getMessage());
            }
        }
    }

    void exportFolderEntries(Element root, File parent) throws Exception {
        Long id = Long.parseLong(root.attributeValue("id"));
        String xml = (String) invokeWithCall("Facade", "getFolderEntriesAsXML", new Object[] { id }, null, parent);
        Document document = DocumentHelper.parseText(xml);
        for (Iterator i = document.getRootElement().elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            exportEntry(element, id, parent);
        }
    }

    void exportEntry(Element root, Long folderId, File parent) throws Exception {
        Long id = Long.parseLong(root.attributeValue("id"));
        File attachments = new File(parent, "Attach-" + safeName(root));
        String xml = (String) invokeWithCall("Facade", "getFolderEntryAsXML", new Object[] { folderId, id, Boolean.TRUE }, null, attachments);
        try {
            File entryFile = new File(parent, safeName(root) + ".xml");
            entryFile.createNewFile();
            FileWriter writer = new FileWriter(entryFile);
            writer.write(xml, 0, xml.length());
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not write entry data for folder " + folderId + ", entry " + root.attributeValue("id") + " - " + root.attributeValue("title"));
        }
    }

    static String safeName(String name) {
        return name.replaceAll("[\\p{Punct}\\p{Space}]", "_");
    }

    static String safeName(Element binderElement) {
        String title = safeName(binderElement.attributeValue("title"));
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }
        String id = binderElement.attributeValue("id");
        return id + "-" + title;
    }
}
