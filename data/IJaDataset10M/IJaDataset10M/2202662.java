package tufts.vue;

import java.util.Vector;
import javax.swing.tree.*;
import javax.swing.*;
import java.io.*;
import java.util.Enumeration;

public class SaveNode {

    private static final org.apache.log4j.Logger Log = org.apache.log4j.Logger.getLogger(SaveNode.class);

    private Resource resource;

    private Vector children;

    public SaveNode() {
    }

    public SaveNode(ResourceNode resourceNode) {
        setResource(resourceNode.getResource());
        if (DEBUG.Enabled) Log.debug("created for " + resource.asDebug());
        if (resource.getClientType() == Resource.FAVORITES) {
            final Enumeration e = resourceNode.children();
            final Vector v = new Vector();
            while (e.hasMoreElements()) {
                ResourceNode newResNode = (ResourceNode) e.nextElement();
                SaveNode child = new SaveNode(newResNode);
                v.add(child);
            }
            setChildren(v);
        }
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return (this.resource);
    }

    public void setChildren(Vector children) {
        this.children = children;
    }

    public Vector getChildren() {
        return (this.children);
    }
}
