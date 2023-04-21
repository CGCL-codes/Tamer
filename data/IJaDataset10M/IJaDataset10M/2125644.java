package org.xemeiah.dom;

public class NodeList implements org.w3c.dom.NodeList {

    protected org.xemeiah.dom.Document document;

    /**
	 * 
	 */
    protected long eltPtrList[];

    protected long attrPtrList[];

    protected NodeList(org.xemeiah.dom.Document document, long eltPtrList[], long attrPtrList[]) {
        this.document = document;
        this.eltPtrList = eltPtrList;
        this.attrPtrList = attrPtrList;
    }

    protected native org.xemeiah.dom.Node getItem(long eltPtr, long attrPtr);

    public int getLength() {
        return eltPtrList.length;
    }

    public org.xemeiah.dom.Node item(int index) {
        return getItem(eltPtrList[index], attrPtrList != null ? attrPtrList[index] : 0);
    }
}
