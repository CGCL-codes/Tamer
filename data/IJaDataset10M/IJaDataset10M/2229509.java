package com.cidero.upnp;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 *  Derived class of CDSContainer used to hold other objects.
 *  The most common examples of a 'StorageFolder' instance is a 
 *  directory on a disk drive.
 *  
 *  TODO - Figure out why this container doesn't have 'storageFree' 
 *  property - (it isn't derived from 'storageVolume' container class,
 *  which has that property)
 *
 */
public class CDSStorageFolder extends CDSContainer {

    static String upnpClass = "object.container.storageFolder";

    long storageUsed;

    public CDSStorageFolder() {
        storageUsed = -1;
    }

    public CDSStorageFolder(Node node) {
        super(node);
        NodeList children = node.getChildNodes();
        for (int n = 0; n < children.getLength(); n++) {
            String nodeName = children.item(n).getNodeName();
            if (nodeName.equals("upnp:storageUsed")) storageUsed = Long.parseLong(CDS.getSingleTextNodeValue(children.item(n)));
        }
    }

    /**
   *  Set storage used
   *
   *  @param  storageUsed    Storage used, in bytes
   */
    public void setStorageUsed(long storageUsed) {
        this.storageUsed = storageUsed;
    }

    /**
   *  Get storage used
   *
   *  @return  Storage used, in bytes
   */
    public long getStorageUsed() {
        return storageUsed;
    }

    public String getUPNPClass() {
        return upnpClass;
    }

    public String attributesToXML(CDSFilter filter) {
        return super.attributesToXML(filter);
    }

    public String elementsToXML(CDSFilter filter) {
        String elementXML = super.elementsToXML(filter);
        elementXML += "  <upnp:storageUsed>" + storageUsed + "</upnp:storageUsed>\n";
        return elementXML;
    }
}
