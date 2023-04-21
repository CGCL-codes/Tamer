package gov.nasa.worldwind.formats.vpf;

/**
 * Encapsulation of the Wiged-Edge Algorithm for VPF ring primitives, described in DIGEST Part 2, Annex C2.4.3. Given a
 * row from the ring primitive table, navigate the ring and edge primitive tables to construct the edge information
 * associated with the specified ring.
 *
 * @author dcollins
 * @version $Id: VPFWingedEdgeTraverser.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class VPFWingedEdgeTraverser {

    public interface EdgeTraversalListener {

        void nextEdge(int index, int primitiveId, boolean reverseCoordinates);
    }

    protected enum Orientation {

        LEFT, RIGHT, LEFT_AND_RIGHT
    }

    public VPFWingedEdgeTraverser() {
    }

    /**
     * Implementation of the Wiged-Edge Algorithm for ring primitives, described in DIGEST Part 2, Annex C2.4.3. Given a
     * row from the ring primitive table, navigate the ring and edge primitive tables to construct the edge information
     * associated with the specified ring.
     *
     * @param edgeInfoArray the edge primitive data.
     * @param listener      the ring edge listener, may be null.
     *
     * @return the number of edges composing the specified ring.
     */
    public int traverseRing(int faceId, int startEdgeId, VPFPrimitiveData.PrimitiveInfo[] edgeInfoArray, EdgeTraversalListener listener) {
        int count = 0;
        int prevEdgeId;
        int curEdgeId = -1;
        int nextEdgeId = startEdgeId;
        do {
            prevEdgeId = curEdgeId;
            curEdgeId = nextEdgeId;
            if (listener != null) {
                listener.nextEdge(count, curEdgeId, this.getMustReverseCoordinates(faceId, prevEdgeId, curEdgeId, edgeInfoArray));
            }
            count++;
        } while ((nextEdgeId = this.nextEdgeId(faceId, prevEdgeId, curEdgeId, edgeInfoArray)) > 0 && (nextEdgeId != startEdgeId));
        return count;
    }

    protected int nextEdgeId(int faceId, int prevEdgeId, int curEdgeId, VPFPrimitiveData.PrimitiveInfo[] edgeInfoArray) {
        Orientation o = this.getOrientation(faceId, curEdgeId, edgeInfoArray);
        if (o == null) {
            return -1;
        }
        switch(o) {
            case LEFT:
                return getEdgeInfo(edgeInfoArray, curEdgeId).getLeftEdge();
            case RIGHT:
                return getEdgeInfo(edgeInfoArray, curEdgeId).getRightEdge();
            case LEFT_AND_RIGHT:
                return (prevEdgeId > 0) ? this.auxiliaryNextEdgeId(prevEdgeId, curEdgeId, edgeInfoArray) : -1;
            default:
                return -1;
        }
    }

    protected Orientation getOrientation(int faceId, int edgeId, VPFPrimitiveData.PrimitiveInfo[] edgeInfo) {
        VPFPrimitiveData.EdgeInfo thisInfo = getEdgeInfo(edgeInfo, edgeId);
        boolean matchLeft = thisInfo.getLeftFace() == faceId;
        boolean matchRight = thisInfo.getRightFace() == faceId;
        if (matchLeft && matchRight) {
            return Orientation.LEFT_AND_RIGHT;
        } else if (matchLeft) {
            return Orientation.LEFT;
        } else if (matchRight) {
            return Orientation.RIGHT;
        }
        return null;
    }

    protected boolean getMustReverseCoordinates(int faceId, int prevEdgeId, int curEdgeId, VPFPrimitiveData.PrimitiveInfo[] edgeInfo) {
        Orientation o = this.getOrientation(faceId, curEdgeId, edgeInfo);
        if (o == null) {
            return false;
        }
        switch(o) {
            case LEFT:
                return true;
            case RIGHT:
                return false;
            case LEFT_AND_RIGHT:
                return (prevEdgeId > 0) && this.auxiliaryMustReverseCoordinates(prevEdgeId, curEdgeId, edgeInfo);
            default:
                return false;
        }
    }

    protected int auxiliaryNextEdgeId(int prevEdgeId, int curEdgeId, VPFPrimitiveData.PrimitiveInfo[] edgeInfoArray) {
        VPFPrimitiveData.EdgeInfo prevInfo = getEdgeInfo(edgeInfoArray, prevEdgeId);
        VPFPrimitiveData.EdgeInfo curInfo = getEdgeInfo(edgeInfoArray, curEdgeId);
        if (curInfo.getStartNode() == prevInfo.getStartNode() || curInfo.getStartNode() == prevInfo.getEndNode()) {
            return (curInfo.getRightEdge() != curEdgeId) ? curInfo.getRightEdge() : curInfo.getLeftEdge();
        } else if (curInfo.getEndNode() == prevInfo.getStartNode() || curInfo.getEndNode() == prevInfo.getEndNode()) {
            return (curInfo.getLeftEdge() != curEdgeId) ? curInfo.getLeftEdge() : curInfo.getRightEdge();
        } else {
            return -1;
        }
    }

    protected boolean auxiliaryMustReverseCoordinates(int prevEdgeId, int curEdgeId, VPFPrimitiveData.PrimitiveInfo[] edgeInfoArray) {
        VPFPrimitiveData.EdgeInfo prevInfo = getEdgeInfo(edgeInfoArray, prevEdgeId);
        VPFPrimitiveData.EdgeInfo curInfo = getEdgeInfo(edgeInfoArray, curEdgeId);
        return curInfo.getEndNode() == prevInfo.getStartNode() || curInfo.getEndNode() == prevInfo.getEndNode();
    }

    protected static VPFPrimitiveData.EdgeInfo getEdgeInfo(VPFPrimitiveData.PrimitiveInfo[] edgeInfo, int id) {
        return (VPFPrimitiveData.EdgeInfo) edgeInfo[VPFBufferedRecordData.indexFromId(id)];
    }
}
