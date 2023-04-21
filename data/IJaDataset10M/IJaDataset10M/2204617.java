package com.tensegrity.webetlclient.modules.core.client.model;

public interface IXMLVisitor {

    public boolean isVisitChildren();

    public void enterNode(IXMLParser parser);

    public void leaveNode(IXMLParser parser);
}
