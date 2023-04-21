package org.tigr.microarray.mev.cluster.algorithm.impl.terrain;

import javax.vecmath.Vector2f;
import org.tigr.microarray.mev.cluster.algorithm.impl.util.FloatArray;
import org.tigr.microarray.mev.cluster.algorithm.impl.util.IntArray;

public class FDGLAlgoT {

    public static final float c_AttractiveRadius = 0f;

    public static final float c_RepulsiveRadius = 200f;

    public static final float c_dblFDGLAlgoThreshold = 1f;

    public static final int c_iQuadTreeDepth = 5;

    public static final int c_iHistoryQueueSize = 10;

    private Vector2f[] m_arrPoints;

    private SLink[][] m_arrLinks;

    private Vector2f m_rPt;

    private QuadTreeT m_QTree;

    private InterfaceToObjects m_pInterface;

    private Vector2f[] m_arrForceField;

    private float m_dblMaxForceLength;

    private int m_iDoNotMove;

    private float m_arrEnergyHist[] = new float[] { -1, -1, -1, -1 };

    private int m_iEnergyIndex = 0;

    public FDGLAlgoT(InterfaceToObjects pInterface) {
        m_pInterface = pInterface;
        m_QTree = new QuadTreeT(c_iQuadTreeDepth, pInterface);
        m_rPt = new Vector2f(0, 0);
        m_iDoNotMove = -1;
    }

    private void PushEnergy(float fltEnergyValue) {
        m_arrEnergyHist[3] = m_arrEnergyHist[2];
        m_arrEnergyHist[2] = m_arrEnergyHist[1];
        m_arrEnergyHist[1] = m_arrEnergyHist[0];
        m_arrEnergyHist[0] = fltEnergyValue;
    }

    static final float MAX_ENERGY = 100f;

    public float getPercentage() {
        return 100f * m_iEnergyIndex / MAX_ENERGY;
    }

    public boolean shouldStop() {
        if (m_iEnergyIndex > MAX_ENERGY) return true;
        if (m_arrEnergyHist[3] < 0) return false;
        return false;
    }

    private static float CalcAttractiveForceFromR2(float R2) {
        float tmpVal = R2 - (c_AttractiveRadius * c_AttractiveRadius);
        if (R2 > (c_RepulsiveRadius * c_RepulsiveRadius)) return (c_RepulsiveRadius * c_RepulsiveRadius);
        if (tmpVal < 0) return 0;
        return tmpVal;
    }

    private static float CalcRepulsiveForceFromR2(float R2) {
        float tmpVal = c_RepulsiveRadius * c_RepulsiveRadius - R2;
        if (tmpVal < 0) return 0;
        return tmpVal;
    }

    private Vector2f CalcAttractiveForcesAt(int iNode) {
        SLink[] rArrAdjNodes = m_arrLinks[iNode];
        int nAdjNodeSize = rArrAdjNodes.length;
        Vector2f vectResult = new Vector2f();
        if (nAdjNodeSize > 0) {
            float dblCurX = m_arrPoints[iNode].x;
            float dblCurY = m_arrPoints[iNode].y;
            Vector2f curVector = new Vector2f();
            for (int i = 0; i < nAdjNodeSize; i++) {
                Vector2f rCurAdjNode = m_arrPoints[rArrAdjNodes[i].m_iToId];
                float CurWeight = rArrAdjNodes[i].m_Weight;
                curVector.set(rCurAdjNode.x - dblCurX, rCurAdjNode.y - dblCurY);
                float Norm = curVector.length();
                if (Norm <= 0) continue;
                float Norm2 = Norm * Norm;
                float attractive_factor = (float) (CalcAttractiveForceFromR2(Norm2));
                curVector.scale(CurWeight * attractive_factor / Norm);
                vectResult.add(curVector);
            }
        }
        return vectResult;
    }

    private Vector2f CalcRepulsiveForcesAtBruteForce(float dblX, float dblY) {
        int iSize = m_arrPoints.length;
        Vector2f vectResult = new Vector2f();
        Vector2f curVector = new Vector2f();
        for (int i = 0; i < iSize; i++) {
            curVector.set(dblX - m_arrPoints[i].x, dblY - m_arrPoints[i].y);
            float Norm = curVector.length();
            float Norm2 = Norm * Norm;
            if (Norm2 <= 0) continue;
            if (Norm <= 0) continue;
            float repulsive_factor = (float) (CalcRepulsiveForceFromR2(Norm2));
            curVector.scale(repulsive_factor / Norm);
            vectResult.add(curVector);
        }
        return vectResult;
    }

    protected Vector2f CalcRepulsiveForcesAt(float X, float Y) {
        m_rPt.x = X;
        m_rPt.y = Y;
        return GetRepulsiveForceFrom(0);
    }

    protected Vector2f GetRepulsiveForceFrom(int iNode) {
        Vector2f ptRes = new Vector2f(0, 0);
        if (iNode < 0) return ptRes;
        QuadTreeT.SNode rQuadTreeNode = m_QTree.m_arrNodes[iNode];
        if (rQuadTreeNode.m_iPointNumBehind == 0) return ptRes;
        if (rQuadTreeNode.m_iPointNumBehind == 1) {
            ptRes.x = m_rPt.x - m_arrPoints[rQuadTreeNode.m_arrPointsIds[0]].x;
            ptRes.y = m_rPt.y - m_arrPoints[rQuadTreeNode.m_arrPointsIds[0]].y;
            float Norm = ptRes.length();
            if (Norm <= 0) return ptRes;
            float Norm2 = Norm * Norm;
            float repulsive_factor = (float) (CalcRepulsiveForceFromR2(Norm2));
            ptRes.scale(repulsive_factor / Norm);
            return ptRes;
        }
        boolean bPtInCurRect = rQuadTreeNode.m_Rect.PtInRect(m_rPt);
        boolean bUseAveragePt = false;
        boolean bIsLeaf = rQuadTreeNode.IsLeaf();
        if (!bPtInCurRect) bUseAveragePt = rQuadTreeNode.m_Rect.Distance(m_rPt) / Math.max(rQuadTreeNode.m_Rect.Width(), rQuadTreeNode.m_Rect.Height()) >= c_dblFDGLAlgoThreshold;
        if (!bIsLeaf && !bUseAveragePt) {
            int iChild = m_QTree.GetChild(iNode, QuadTreeT.LEFT_UP);
            ptRes = GetRepulsiveForceFrom(iChild);
            iChild = m_QTree.GetChild(iNode, QuadTreeT.RIGHT_UP);
            ptRes.add(GetRepulsiveForceFrom(iChild));
            iChild = m_QTree.GetChild(iNode, QuadTreeT.LEFT_DOWN);
            ptRes.add(GetRepulsiveForceFrom(iChild));
            iChild = m_QTree.GetChild(iNode, QuadTreeT.RIGHT_DOWN);
            ptRes.add(GetRepulsiveForceFrom(iChild));
            return ptRes;
        }
        if (bUseAveragePt) {
            ptRes.x = m_rPt.x - rQuadTreeNode.m_ptAvg.x;
            ptRes.y = m_rPt.y - rQuadTreeNode.m_ptAvg.y;
            float Norm = ptRes.length();
            if (Norm <= 0) return ptRes;
            float Norm2 = Norm * Norm;
            float repulsive_factor = (float) (CalcRepulsiveForceFromR2(Norm2));
            if (repulsive_factor <= 0) {
                ptRes.x = 0;
                ptRes.y = 0;
                return ptRes;
            }
            ptRes.scale(repulsive_factor * (float) rQuadTreeNode.m_iPointNumBehind / Norm);
            return ptRes;
        }
        Vector2f curVector = new Vector2f();
        int iSize = rQuadTreeNode.m_arrPointsIds.length;
        for (int i = 0; i < iSize; i++) {
            curVector.set(m_rPt.x - m_arrPoints[rQuadTreeNode.m_arrPointsIds[i]].x, m_rPt.y - m_arrPoints[rQuadTreeNode.m_arrPointsIds[i]].y);
            float Norm = curVector.length();
            if (Norm <= 0) return ptRes;
            float Norm2 = Norm * Norm;
            float repulsive_factor = (float) (CalcRepulsiveForceFromR2(Norm2));
            curVector.scale(repulsive_factor / Norm);
            ptRes.add(curVector);
        }
        return ptRes;
    }

    public void DoNotMove(int i) {
        m_iDoNotMove = i;
    }

    public void CalculateForceField() {
        int iSize = m_arrPoints.length;
        m_arrForceField = new Vector2f[iSize];
        m_dblMaxForceLength = 0;
        m_QTree.Initialize();
        for (int i = 0; i < iSize; i++) {
            m_arrForceField[i] = CalcAttractiveForcesAt(i);
            m_arrForceField[i].add(CalcRepulsiveForcesAt(m_arrPoints[i].x, m_arrPoints[i].y));
            float dblCurLength = m_arrForceField[i].length();
            if (m_dblMaxForceLength < dblCurLength) m_dblMaxForceLength = dblCurLength;
        }
    }

    public void MoveSystem() {
        m_iEnergyIndex++;
        if (m_dblMaxForceLength <= 0) return;
        float dblFactor = (float) Math.sqrt((Math.sqrt(m_dblMaxForceLength)));
        if (dblFactor <= 0) return;
        int iSize = m_arrPoints.length;
        for (int i = 0; i < iSize; i++) {
            if (i == m_iDoNotMove) continue;
            float abs_force = m_arrForceField[i].length();
            if (abs_force < dblFactor) {
                m_arrPoints[i].x += m_arrForceField[i].x;
                m_arrPoints[i].y += m_arrForceField[i].y;
            } else {
                m_arrPoints[i].x += m_arrForceField[i].x / abs_force * dblFactor;
                m_arrPoints[i].y += m_arrForceField[i].y / abs_force * dblFactor;
            }
        }
        PushEnergy(m_dblMaxForceLength);
    }

    public void UpdateSource() {
        m_pInterface.SetObjectGeom(m_arrPoints);
    }

    public void InitFromInterface() {
        int[] arrObjIds = m_pInterface.GetAllObjectsIds();
        int n = arrObjIds.length;
        m_arrLinks = new SLink[n][];
        m_arrPoints = new Vector2f[n];
        for (int i = 0; i < m_arrPoints.length; i++) m_arrPoints[i] = new Vector2f();
        Vector2f pdDbl = new Vector2f();
        for (int i = 0; i < n; i++) {
            m_pInterface.GetObjectGeom(arrObjIds[i], pdDbl);
            m_arrPoints[i].x = pdDbl.x;
            m_arrPoints[i].y = pdDbl.y;
            FloatArray arrWeight = new FloatArray();
            IntArray arrAdjNodes = new IntArray();
            m_pInterface.GetAdjInfoFor(arrObjIds[i], arrAdjNodes, arrWeight);
            int nAdjNodesCouns = arrAdjNodes.getSize();
            m_arrLinks[i] = new SLink[nAdjNodesCouns];
            for (int j = 0; j < nAdjNodesCouns; j++) {
                m_arrLinks[i][j] = new SLink();
                m_arrLinks[i][j].m_iToId = arrAdjNodes.get(j);
                m_arrLinks[i][j].m_Weight = arrWeight.get(j);
            }
        }
    }

    public QuadTreeT GetQuadTree() {
        return m_QTree;
    }

    public class SLink {

        int m_iToId;

        float m_Weight;
    }
}
