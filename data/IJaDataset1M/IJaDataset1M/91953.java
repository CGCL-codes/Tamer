package muvis.view.filters;

import muvis.Environment;
import net.bouthier.treemapSwing.TMComputeSize;
import net.bouthier.treemapSwing.TMExceptionBadTMNodeKind;
import net.bouthier.treemapSwing.TMNode;

/**
 * The TMFileSize class implements an example of a TMComputeSize
 * for a TMFileNode.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class MuVisBeatFilterSize implements TMComputeSize {

    /**
     * Test if this TMComputeSize could be used
     * with the kind of TMNode passed in parameter.
     *
     * @param node    the TMNode to test the compatibility with
     * @return        <CODE>true</CODE> if this kind of node is compatible;
     *                <CODE>false</CODE> otherwise
     */
    public boolean isCompatibleWith(TMNode node) {
        if (node instanceof MuVisFilterNode) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the size of the node.
     * The node should be an instance of TMFileNode.
     * Returns <CODE>0</CODE> for a folder, and the size
     * of the file, in byte, for a file.
     *
     * @param node                      we compute the size of this node;
     *                                  should be an instance of TMFileNode
     * @return                          the size of the node;
     *                                  <CODE>0</CODE> for a folder;
     *                                  the size of the file in byte for a file
     * @throws TMExceptionBadTMNodeKind If the node is not an
     *                                  instance of TMFileNode
     */
    @Override
    public float getSize(TMNode node) throws TMExceptionBadTMNodeKind {
        if (node instanceof MuVisBeatFilterNode) {
            MuVisBeatFilterNode fNode = (MuVisBeatFilterNode) node;
            String beat = fNode.getBeat();
            return Environment.getEnvironmentInstance().getDatabaseManager().getTracksWithBeat(beat);
        } else {
            throw new TMExceptionBadTMNodeKind(this, node);
        }
    }
}
