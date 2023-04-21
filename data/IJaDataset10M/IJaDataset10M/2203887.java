package org.xmlcml.cml;

import org.w3c.dom.Document;

public interface BuiltinArrayContainer extends BuiltinContainer {

    public CMLStringArray getBuiltinArray(int builtinId);

    public void setBuiltinArray(int builtinId, CMLStringArray array);

    public CMLStringArray getNonBuiltinArray(String name);

    public void setNonBuiltinArray(String name, CMLStringArray array);

    public void deleteNonBuiltinArray(String name);

    public AbstractBuiltinContainer createArrayElement(Document document);
}
