package org.ladybug.cfg;

import org.treeconf.ConfigLeaf;
import org.treeconf.ConfigNode;

/**
 * @author Aurelian Pop
 */
public class ReleaseConfigNode extends ConfigNode {

    public final ConfigLeaf nameMaxLen = new ConfigLeaf("32");

    public final ConfigLeaf descMaxLen = new ConfigLeaf("128");
}
