package yaw.cjef.templates.project.templates.descriptoren;

import java.io.*;
import yaw.core.codegenerator.ICodeGenerator;
import yaw.cjef.templates.project.Model;

public class ejb_inprise_xml implements ICodeGenerator<Model> {

    public void generate(Writer writer, Model model) throws IOException {
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n\r\n<!DOCTYPE inprise-specific PUBLIC '-//Inprise Corporation//DTD Enterprise JavaBeans 1.1//EN' 'http://www.borland.com/devsupport/appserver/dtds/ejb-inprise.dtd'>\r\n\r\n<inprise-specific>\r\n  <enterprise-beans>\r\n    <session>\r\n      <ejb-name>TMUApplicationMgrEJB</ejb-name>\r\n      <bean-home-name>TMUApplicationMgrEJB</bean-home-name>\r\n      <timeout>9999</timeout>\r\n    </session>\r\n    <session>\r\n      <ejb-name>TMUEditorMgrEJB</ejb-name>\r\n      <bean-home-name>TMUEditorMgrEJB</bean-home-name>\r\n      <timeout>3600</timeout>\r\n    </session>\r\n    <session>\r\n      <ejb-name>TMUSelectorMgrEJB</ejb-name>\r\n      <bean-home-name>TMUSelectorMgrEJB</bean-home-name>\r\n      <timeout>3600</timeout>\r\n    </session>\r\n  </enterprise-beans>\r\n</inprise-specific>");
    }
}
