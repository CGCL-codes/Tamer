package javaaxp.core.service;

import javaaxp.core.service.model.document.IDocumentStructure;

public interface IXPSDocumentAccess {

    public IDocumentStructure getDocumentStructure(int docNum) throws XPSSpecError, XPSError;

    public int getFirstDocNum() throws XPSError;

    public int getLastDocNum() throws XPSError;
}
