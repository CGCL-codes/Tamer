package de.spieleck.config;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Basic interface to programm against when using the Config tool.
 * @author fsn
 * @version 1.0
 */
public interface ConfigNode extends Serializable {

    /**
     * Find sub node matching name.
     */
    public ConfigNode node(String path);

    /**
     * Find node or parent sub node matching name.
     */
    public ConfigNode nodeInh(String path);

    /**
     * Find the node responsible for reading this node from file.
     */
    public ConfigFileNode getBranchNode();

    /**
     * Get the name of the node.
     */
    public String getName();

    /**
     * Get the complete path of the node.
     */
    public String getPath();

    /**
     * Get the node above.
     */
    public ConfigNode getParent();

    /** 
     * Get (expanded) value as a boolean.
     */
    public boolean getBoolean();

    /**
     * Get (expanded) value as an integer.
     */
    public int getInt();

    /**
     * Get (expanded) value as a double.
     */
    public double getDouble();

    /**
     * Get (expanded) value as String.
     */
    public String getUnexpanded();

    /**
     * Get (expanded) value as String.
     */
    public String getString();

    /**
     * Get (expanded) value of subnode as boolean, using default if necessary.
     */
    public boolean getBoolean(String path, boolean deflt);

    /**
     * Get (expanded) value of subnode as integer, using default when necessary.
     */
    public int getInt(String path, int deflt);

    /** 
     * Get (expanded) value of subnode as double, using default when necessary.
     */
    public double getDouble(String path, double deflt);

    /**
     * Get (expanded) value of subnode as String, using default when necessary.
     */
    public String getString(String path, String deflt);

    /** 
     * Get (expanded) value of subnode as boolean searching parent nodes before using
     * default.
     */
    public boolean getInhBoolean(String path, boolean deflt);

    /** 
     * Get (expanded) value of subnode as int searching parent nodes before using
     * default.
     */
    public int getInhInt(String path, int deflt);

    /** 
     * Get (expanded) value of subnode as double searching parent nodes before using
     * default.
     */
    public double getInhDouble(String path, double deflt);

    /** 
     * Get (expanded) value of subnode as String searching parent nodes before using
     * default.
     */
    public String getInhString(String path, String deflt);

    /**
     * Count the number of children we have.
     */
    public int countChildren();

    /**
     * Enumerate my children.
     */
    public Iterator children();

    /**
     * Enumerate children of me, having a certain name.
     */
    public Iterator childrenNamed(String key);

    /**
     * Count the number of children we have.
     */
    public int countChildrenNamed(String key);

    /**
     * Pretty print this node with its whole subtree.
     */
    public void printXML(PrintWriter os) throws IOException;

    /**
     * Get something describing the source location of this node.
     */
    public String getSourceDescription();
}
