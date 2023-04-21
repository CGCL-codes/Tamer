package org.primordion.xholon.io;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import org.primordion.xholon.base.IXholon;
import org.primordion.xholon.base.Xholon;
import org.primordion.xholon.util.MiscIo;

/**
 * Take a shapshot of an entire Xholon tree.
 * It saves the snapshot in YAML "key: value" format.
 * The resulting file is intended to be used for debugging,
 * and as a quick global view of the structure.
 * It is NOT intended as a formal serialization.
 * For each node it includes the node name as the YAML key,
 * and the result of calling toString() as the YAML value.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.5 (Created on February 19, 2007)
 * @see http://www.yaml.org/
 */
public class SnapshotYAML extends Snapshot implements ISnapshot {

    Writer out;

    /**
	 * Constructor.
	 */
    public SnapshotYAML() {
    }

    /**
	 * Save a timestamped snapshot to an XML file.
	 * @param snRoot Root of the tree to be saved in the snapshot file.
	 * @param modelName Name of the model.
	 */
    public void saveSnapshot(IXholon snRoot, String modelName) {
        saveSnapshot(snRoot, snRoot.getName(), modelName);
    }

    /**
	 * Save a timestamped snapshot to an YAML file.
	 * @param snRoot Root of the tree to be saved in the snapshot file.
	 * @param snRootName Name of the root.
	 * @param modelName Name of the model.
	 */
    protected void saveSnapshot(Object snRoot, String snRootName, String modelName) {
        Date now = new Date();
        String fileName = pathName + "xhsnap" + now.getTime() + ".yaml";
        File dirOut = new File(pathName);
        dirOut.mkdirs();
        out = MiscIo.openOutputFile(fileName);
        try {
            out.write("%YAML 1.1\n");
            out.write("---\n");
            out.write("# Model: " + modelName + "\n");
            out.write("# File: " + fileName + "\n");
            out.write("# Date: " + now + "\n");
            out.write(snRootName + ": \n");
            writeNode(((IXholon) snRoot).getFirstChild(), 1);
            out.write("...\n");
        } catch (IOException e) {
            Xholon.getLogger().error("", e);
        }
        MiscIo.closeOutputFile(out);
    }

    /**
	 * Write a node as it currently exists in the Xholon model.
	 * @param node The node to write to a file.
	 * @param level The distance from the root node (used for indenting).
	 */
    protected void writeNode(IXholon node, int level) {
        String nodeName = getNodeName(node);
        StringBuffer indent = new StringBuffer(level);
        for (int i = 0; i < level; i++) {
            indent.append(' ');
        }
        try {
            if (snapshotTostring) {
                out.write(indent + nodeName + ": ");
                if (node.getFirstChild() != null) {
                    out.write("# ");
                }
                out.write(node.toString() + "\n");
            } else {
                out.write(indent + nodeName + ": \n");
            }
        } catch (IOException e) {
            Xholon.getLogger().error("", e);
        }
        if (node.getFirstChild() != null) {
            writeNode(node.getFirstChild(), level + 1);
        }
        if (node.getNextSibling() != null) {
            writeNode(node.getNextSibling(), level);
        }
    }
}
