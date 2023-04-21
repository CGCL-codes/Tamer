package com.loribel.commons.business.convertor;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.loribel.commons.ant.GB_AntTask;
import com.loribel.commons.exception.GB_TaskException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_TaskCheckTools;
import com.loribel.commons.util.GB_XmlTools;
import com.loribel.commons.util.GB_XmlWriterTools;

/**
 * Task to use GB_BO2BOShort.
 *
 * @author Gr�gory Borelli
 */
public class GB_BO2BOShortTask extends GB_AntTask {

    public static class MyParamBOName {

        private String name;

        /**
         * List of MyParamProperty.
         */
        private List properties;

        public void addConfiguredProperty(MyParamProperty a_property) {
            if (properties == null) {
                properties = new ArrayList();
            }
            properties.add(a_property);
        }

        public String getName() {
            return name;
        }

        public void setName(String a_name) {
            name = a_name;
        }

        public Node toNode(Document a_doc) {
            GB_BO2BOShort l_bo2BoShort = new GB_BO2BOShort();
            return l_bo2BoShort.toNode(name, a_doc);
        }
    }

    public static class MyParamProperty {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String a_name) {
            name = a_name;
        }
    }

    private List boNames;

    protected File destFile;

    public GB_BO2BOShortTask() {
    }

    public void addConfiguredBOName(MyParamBOName a_boName) {
        if (boNames == null) {
            boNames = new ArrayList();
        }
        boNames.add(a_boName);
    }

    protected void checkProperties() throws BuildException {
        try {
            GB_TaskCheckTools.checkParameter("destFile", destFile);
        } catch (GB_TaskException ex) {
            throw new BuildException(ex.getMessage());
        }
    }

    /**
     * Execute the task.
     */
    public void execute() throws BuildException {
        checkProperties();
        int len = CTools.getSize(boNames);
        Element l_root = GB_XmlTools.newRootXML();
        Document l_doc = l_root.getOwnerDocument();
        Element l_nodeBody = GB_XmlTools.addElement(l_root, "body", null);
        for (int i = 0; i < len; i++) {
            MyParamBOName l_boName = (MyParamBOName) boNames.get(i);
            l_nodeBody.appendChild(l_boName.toNode(l_doc));
        }
        try {
            FileWriter l_writer = new FileWriter(destFile, false);
            GB_XmlWriterTools.writeXml(l_root, l_writer, null);
            l_writer.close();
            println("Write " + destFile.getAbsolutePath());
        } catch (Exception ex) {
            throwException(ex);
        }
    }

    public void setDestFile(File a_destFile) {
        destFile = a_destFile;
    }
}
