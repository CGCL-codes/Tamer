package org.openxml4j.samples.opc;

import java.io.File;
import java.net.URI;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.openxml4j.opc.ContentTypes;
import org.openxml4j.opc.Package;
import org.openxml4j.opc.PackagePart;
import org.openxml4j.opc.PackagePartName;
import org.openxml4j.opc.PackageRelationshipTypes;
import org.openxml4j.opc.PackagingURIHelper;
import org.openxml4j.opc.StreamHelper;
import org.openxml4j.opc.TargetMode;
import org.openxml4j.samples.DemoCore;

/**
 * Creates a WordprocessingML document from scratch using only the OPC part of
 * OpenXML4J.
 * 
 * @author Julien Chable
 * @version 0.1
 */
public class CreateWordprocessingMLDocumentwithCustomXml {

    public static void main(String[] args) throws Exception {
        DemoCore demoCore = new DemoCore();
        File outputDocument = new File(demoCore.getTestRootPath() + "sample_output.docx");
        Package pkg = Package.create(outputDocument, true);
        PackagePartName corePartName = PackagingURIHelper.createPartName("/word/document.xml");
        pkg.addRelationship(corePartName, TargetMode.INTERNAL, PackageRelationshipTypes.CORE_DOCUMENT, "rId1");
        PackagePart corePart = pkg.createPart(corePartName, "application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml");
        Document doc = DocumentHelper.createDocument();
        Namespace nsWordprocessinML = new Namespace("w", "http://schemas.openxmlformats.org/wordprocessingml/2006/main");
        Element elDocument = doc.addElement(new QName("document", nsWordprocessinML));
        Element elBody = elDocument.addElement(new QName("body", nsWordprocessinML));
        Element elParagraph = elBody.addElement(new QName("p", nsWordprocessinML));
        Element elRun = elParagraph.addElement(new QName("r", nsWordprocessinML));
        Element elText = elRun.addElement(new QName("t", nsWordprocessinML));
        elText.setText("Hello Open XML !");
        StreamHelper.saveXmlInStream(doc, corePart.getOutputStream());
        PackagePartName customXmlPartName = PackagingURIHelper.createPartName("/customXml/item1.xml");
        pkg.createPart(customXmlPartName, ContentTypes.CUSTOM_XML_PART);
        URI relativeCustomXmlPartURI = PackagingURIHelper.relativizeURI(corePartName.getURI(), customXmlPartName.getURI());
        corePart.addRelationship(relativeCustomXmlPartURI, TargetMode.INTERNAL, PackageRelationshipTypes.CUSTOM_XML);
        Document customXmlDoc = DocumentHelper.createDocument();
        Element elCustomer = customXmlDoc.addElement(new QName("customer", nsWordprocessinML));
        Element elName = elCustomer.addElement(new QName("name", nsWordprocessinML));
        elName.setText("Leonarde Da Vinci");
        StreamHelper.saveXmlInStream(customXmlDoc, corePart.getOutputStream());
        pkg.close();
    }
}
