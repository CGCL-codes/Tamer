package com.htwg.routingengine.modules.fileoperations;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.tree.AbstractElement;
import com.htwg.routingengine.enginesmagic.ParameterSelector;
import com.htwg.routingengine.framework.ComponentAccess;
import com.htwg.routingengine.modules.generic.BasicModule;
import com.htwg.routingengine.modules.generic.FrameworkModule;
import com.htwg.routingengine.modules.generic.GenericModule;

public class FileWriterModule extends BasicModule implements GenericModule {

    public FileWriterModule(ComponentAccess componentAccess) {
        super(componentAccess);
        className = FrameworkModule.class.getName();
        logger = Logger.getLogger(className);
    }

    @Override
    public String action(String xmlIn) throws Exception {
        if (writemode.equalsIgnoreCase("dataobject")) {
            if (!dataobjectname.equalsIgnoreCase("")) {
                AbstractElement writeElement = searchDataObject(xmlIn, dataobjectname);
                writeContent(writeElement);
            }
        } else {
            Document xmldoc = DocumentHelper.parseText(xmlIn);
            String xmlPath = new String("/Output/FileWriter");
            List<Node> nodelist = xmldoc.selectNodes(xmlPath);
            AbstractElement currentElement = null;
            if (null != nodelist) {
                Iterator nodeIterator = nodelist.iterator();
                while (nodeIterator.hasNext()) {
                    currentElement = (AbstractElement) nodeIterator.next();
                    try {
                        if (writemode.equalsIgnoreCase("all")) {
                            AbstractElement writeElement = searchDataObject(xmlIn, currentElement.valueOf("@dataobjectname"));
                            writeContent(writeElement);
                            ((AbstractElement) currentElement).attributeValue("printed", "true");
                        } else if (writemode.equalsIgnoreCase("nonprinted")) {
                            if (currentElement.valueOf("@printed").equalsIgnoreCase("false")) {
                                AbstractElement writeElement = searchDataObject(xmlIn, currentElement.valueOf("@dataobjectname"));
                                writeContent(writeElement);
                                ((AbstractElement) currentElement).attributeValue("printed", "true");
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error in Filewriter", e);
                    }
                }
            }
        }
        return xmlIn;
    }

    @Override
    public void exit() throws Exception {
        super.exit();
    }

    private AbstractElement searchDataObject(String xmlIn, String dataobjectname) {
        return null;
    }

    protected void writeContent(Node writeElement) throws Exception {
        if (null != writeElement) {
            String filetype = writeElement.valueOf("@filetype");
            String filename = new String(modulePath);
            filename = filename + "/" + writeElement.valueOf("@name") + "." + writeElement.valueOf("@extension");
            String className = new String("FileContentWriter");
            className = className + filetype.toUpperCase();
            FileContentWriter contentWriter = null;
            try {
                Class c = Class.forName(className);
                contentWriter = (FileContentWriter) c.newInstance();
            } catch (ClassNotFoundException e) {
                logger.warn("ContentWriter was not found, will use ASCII: ", e);
                className = "FileContentWriterASCII";
                Class c = Class.forName(className);
                contentWriter = (FileContentWriter) c.newInstance();
            } catch (Exception e) {
                throw e;
            }
            contentWriter.writeFile(filename, writeElement);
        }
    }

    @Override
    public void init(String xmlIn) throws Exception {
        super.init(xmlIn);
        Document xmldoc = DocumentHelper.parseText(xmlIn);
        String xmlPath = new String("/Module/Param");
        List<Node> nodelist = xmldoc.selectNodes(xmlPath);
        ParameterSelector ps = new ParameterSelector();
        if (null == writemode) writemode = new String();
        if (null == dataobjectname) dataobjectname = new String();
        writemode = ps.selectParameterValue(nodelist, "writemode");
        dataobjectname = ps.selectParameterValue(nodelist, "dataobjectname");
    }

    private String dataobjectname;

    @Override
    public void output() throws Exception {
        super.output();
    }

    @Override
    public String preAction(String xmlIn) throws Exception {
        return super.preAction(xmlIn);
    }

    private String writemode;
}
