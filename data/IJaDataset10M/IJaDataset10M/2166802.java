package org.gudy.azureus2.plugins.utils.xml.simpleparser;

import java.io.PrintWriter;

public interface SimpleXMLParserDocumentNode {

    public String getName();

    public String getFullName();

    public String getNameSpaceURI();

    public String getValue();

    public SimpleXMLParserDocumentAttribute[] getAttributes();

    public SimpleXMLParserDocumentAttribute getAttribute(String name);

    public SimpleXMLParserDocumentNode[] getChildren();

    public SimpleXMLParserDocumentNode getChild(String name);

    public void print();

    public void print(PrintWriter pw);
}
