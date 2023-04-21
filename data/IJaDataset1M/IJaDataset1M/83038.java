package jp.co.lattice.vKernel.texEx.a0x;

import jp.co.lattice.vKernel.core.c0.*;
import jp.co.lattice.vKernel.tex.c0a.*;

/**
 * @author	  created by Eishin Matsui (00/04/09-)
 * 
 */
public class lvMakeUVspaceUV extends lvRoot {

    private static final int maxNumAngleFix = 256;

    private static class SparMat {

        private lvSparseMatrix.SetElm setElm[] = null;

        private double cu[] = null;

        private double cv[] = null;

        private double xu[] = null;

        private double xv[] = null;
    }

    /**
	 * static�ϐ���p�̂��߂̓����N���X
	 */
    public static class Global {

        /** �J�����g�V�F��No						*/
        private int curShellNo;

        /** ��̓f�[�^								*/
        private lvMakeUVspaceType.UVpublic srcUVpublic = null;

        /** ��̓f�[�^								*/
        private lvMakeUVspaceType.EdgeVtx srcEdgeVtx = null;

        /** �o�̓f�[�^								*/
        private lvMakeUVspaceType.UVspaceUV dstUVspaceUV = null;

        private double angleFix[] = null;

        private lvSparseMatrix sparseMatrix = null;

        private SparMat sparMat = new SparMat();

        /**
		 * �R���X�g���N�^
		 * @param  dt		(( I )) �O���[�o���f�[�^
		 */
        public Global(lvGlobal dt) {
            sparseMatrix = new lvSparseMatrix(dt);
            GlobalTmp(dt);
            GlobalStatic(dt);
        }

        /** ���[�J���ϐ��p�� new ��p�o�b�t�@�G���A		*/
        private lvVector tvSetAngleFix[] = null;

        private lvVector tvSetFixUVMain[] = null;

        private int tiArySetAngleFix[] = null;

        /**
		 * ���[�J���ϐ��p�� new ��p�o�b�t�@����i�R���X�g���N�^�Ŏg�p�j
		 * @param  dt		(( I )) �O���[�o���f�[�^
		*/
        private final void GlobalTmp(lvGlobal dt) {
            tvSetAngleFix = new lvVector[4];
            for (int i = 0; i < 4; i++) tvSetAngleFix[i] = new lvVector(dt);
            tvSetFixUVMain = new lvVector[4];
            for (int i = 0; i < 4; i++) tvSetFixUVMain[i] = new lvVector(dt);
            tiArySetAngleFix = new int[2];
        }

        /** ���K�͂� DeriveDivFace0().inner �p�̃O���[�o���f�[�^		*/
        private double staticAngleFix[] = null;

        /**
		 * ���K�͂Ȕz��p�̃O���[�o���f�[�^�̏���i�R���X�g���N�^�Ŏg�p�j
		 * @param  dt		(( I )) �O���[�o���f�[�^
		 */
        private final void GlobalStatic(lvGlobal dt) {
            staticAngleFix = new double[maxNumAngleFix];
        }
    }

    /** ���N���X�p�̃O���[�o���f�[�^						*/
    private final Global Gbl() {
        return ((lv0UVcalcGblElm) global.GUVcalc()).gMakeUVspaceUV;
    }

    /** lvMakeUVspaceType.UVpublic�f�[�^�p�N���X�I�u�W�F�N�g	*/
    private final lvMakeUVspaceType.UVpublic SrcUVpublic() {
        return Gbl().srcUVpublic;
    }

    /** lvMakeUVspaceType.Edge�f�[�^�p�N���X�I�u�W�F�N�g		*/
    private final lvMakeUVspaceType.EdgeVtx SrcEdgeVtx() {
        return Gbl().srcEdgeVtx;
    }

    /** lvMakeUVspaceType.UVspaceUV�f�[�^�p�N���X�I�u�W�F�N�g	*/
    private final lvMakeUVspaceType.UVspaceUV DstUVspaceUV() {
        return Gbl().dstUVspaceUV;
    }

    /** lvPolygon�f�[�^�p�N���X�I�u�W�F�N�g					*/
    private final lvPolygon Polygon() {
        return ((lvComGblElm) global.GCom()).gModelPoly.shell[Gbl().curShellNo].poly;
    }

    /** lvUVpublic�f�[�^�p�N���X�I�u�W�F�N�g				*/
    private final lvUVpublic ShellUVpublic() {
        return ((lvComAGblElm) global.GComA()).gModelAtt.shell[Gbl().curShellNo].uvPublic;
    }

    /** lvUVpublic�f�[�^�p�N���X�I�u�W�F�N�g				*/
    private final SparMat SparMat() {
        return Gbl().sparMat;
    }

    /**
	 * �R���X�g���N�^
	 * @param  dt		(( I )) �O���[�o���f�[�^
	 */
    public lvMakeUVspaceUV(lvGlobal dt) {
        super(dt);
    }

    public lvMakeUVspaceType.UVspaceUV Exec(int shellNo, lvMakeUVspaceType.UVpublic srcUVpublic, lvMakeUVspaceType.EdgeVtx srcEdgeVtx) throws lvThrowable {
        if (srcUVpublic == null) return null;
        Gbl().curShellNo = shellNo;
        Gbl().srcUVpublic = srcUVpublic;
        Gbl().srcEdgeVtx = srcEdgeVtx;
        Gbl().dstUVspaceUV = new lvMakeUVspaceType.UVspaceUV();
        NewUVspaceUV();
        SetFixUV();
        SparMatProc();
        Finish();
        return Gbl().dstUVspaceUV;
    }

    private final void NewUVspaceUV() {
        int num = SrcUVpublic().vtxUVseq.length;
        DstUVspaceUV().vtxUVseq = new lvUVdt[num];
        for (int i = 0; i < num; i++) DstUVspaceUV().vtxUVseq[i] = new lvUVdt();
    }

    private final void SetFixUV() throws lvThrowable {
        int numUVspace = ShellUVpublic().uvSpace.length;
        for (int i = 0; i < numUVspace; i++) {
            double sum = SetAngleFix(i);
            CorrectAngleFix(i, sum);
            SetFixUVMain(i);
        }
    }

    private final double SetAngleFix(int uvSpaceNo) throws lvThrowable {
        lvVector pos0 = Gbl().tvSetAngleFix[0];
        lvVector pos1 = Gbl().tvSetAngleFix[1];
        int vtxNo[] = Gbl().tiArySetAngleFix;
        int numFixVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx.length;
        int numFixEdge = SrcEdgeVtx().uvSpace[uvSpaceNo].fixEdge.length;
        NewAngleFix(numFixEdge);
        int cnt = 0;
        double sum = 0.0;
        for (int i = 0; i < numFixVtx; i++) {
            int vtxNoOrg = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx[i].vtxNo;
            lvRec.SeqPart radial = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx[i].radial;
            for (int j = 0; j < radial.num; j++) {
                lvMakeUVspaceType.RadialOne fixRadial = SrcEdgeVtx().uvSpace[uvSpaceNo].fixRadial[radial.start + j];
                if (fixRadial.isFixEdge == true) {
                    int edgeNo = SrcEdgeVtx().uvSpace[uvSpaceNo].fixEdge[fixRadial.edgeIndex].edgeNo;
                    int edgeIdx = SrcEdgeVtx().uvSpace[uvSpaceNo].fixEdge[fixRadial.edgeIndex].edgeIdx;
                    lvPolygon.InfoFaceHalf faceHalf = Polygon().edge[edgeNo].face[0];
                    lvRec.SeqPart face = Polygon().face[faceHalf.faceNo];
                    vtxNo[0] = Polygon().faceHalfSeq[face.start + faceHalf.halfNo].vtxNo;
                    vtxNo[1] = Polygon().faceHalfSeq[face.start + ((faceHalf.halfNo + 1) % face.num)].vtxNo;
                    if (vtxNo[edgeIdx] == vtxNoOrg) {
                        pos0.VecDt2Vector(Polygon().vertex[vtxNo[0]].pos);
                        pos1.VecDt2Vector(Polygon().vertex[vtxNo[1]].pos);
                        double len = (pos0.Sub(pos1)).Length();
                        Gbl().angleFix[i] = len;
                        sum += len;
                        cnt++;
                        break;
                    }
                }
            }
        }
        Err().Assert((cnt == numFixEdge), "lvMakeUVspaceUV.SetAngleFix(0)");
        return sum;
    }

    private final void NewAngleFix(int num) {
        if (num > maxNumAngleFix) Gbl().angleFix = new double[num]; else Gbl().angleFix = Gbl().staticAngleFix;
    }

    private final void CorrectAngleFix(int uvSpaceNo, double sum) {
        int numFixEdge = SrcEdgeVtx().uvSpace[uvSpaceNo].fixEdge.length;
        for (int i = 0; i < numFixEdge; i++) Gbl().angleFix[i] = 2.0 * lvConst.LV_PI / sum * Gbl().angleFix[i];
    }

    private final void SetFixUVMain(int uvSpaceNo) {
        lvVector v0 = Gbl().tvSetFixUVMain[0];
        v0.SetXYZ(0.5, 0.0, 0.0);
        lvVector v1 = Gbl().tvSetFixUVMain[1];
        v1.SetXYZ(0.0, 0.0, 1.0);
        lvVector vUV = Gbl().tvSetFixUVMain[2];
        int numFixVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx.length;
        for (int i = 0; i < numFixVtx; i++) {
            if (i > 0) v0.Rotate(v1, Gbl().angleFix[i - 1]);
            vUV.Assign(v0);
            int vtxNo = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx[i].vtxNo;
            int uvSpaceOfs = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx[i].uvSpaceOfs;
            lvRec.SeqPart vtxUV = SrcUVpublic().vtxUV[vtxNo];
            DstUVspaceUV().vtxUVseq[vtxUV.start + uvSpaceOfs].u = vUV.x + 0.5;
            DstUVspaceUV().vtxUVseq[vtxUV.start + uvSpaceOfs].v = vUV.y + 0.5;
        }
    }

    private final void SparMatProc() throws lvThrowable {
        int numUVspace = ShellUVpublic().uvSpace.length;
        for (int i = 0; i < numUVspace; i++) {
            int numSparMat = NumSparMat(i);
            NewSparMat(numSparMat);
            NewCuCv(i);
            SetSparMat(i);
            NewXuXv(i);
            SetFreeUV(i);
        }
    }

    private final int NumSparMat(int uvSpaceNo) {
        int numFreeVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx.length;
        int cnt = 0;
        for (int i = 0; i < numFreeVtx; i++) {
            lvRec.SeqPart radial = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx[i].radial;
            for (int j = 0; j < radial.num; j++) {
                boolean isFixVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].freeRadial[radial.start + j].isFixVtx;
                if (isFixVtx == false) cnt++;
            }
            cnt++;
        }
        return cnt;
    }

    private final void NewSparMat(int numSparMat) {
        SparMat().setElm = new lvSparseMatrix.SetElm[numSparMat];
        for (int i = 0; i < numSparMat; i++) SparMat().setElm[i] = new lvSparseMatrix.SetElm();
    }

    private final void NewCuCv(int uvSpaceNo) {
        int numFreeVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx.length;
        SparMat().cu = new double[numFreeVtx];
        SparMat().cv = new double[numFreeVtx];
    }

    private final void SetSparMat(int uvSpaceNo) {
        int numFreeVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx.length;
        int cnt = 0;
        for (int i = 0; i < numFreeVtx; i++) {
            double cu = 0.0;
            double cv = 0.0;
            double springSum = 0.0;
            lvRec.SeqPart radial = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx[i].radial;
            for (int j = 0; j < radial.num; j++) {
                lvMakeUVspaceType.RadialOne freeRadial = SrcEdgeVtx().uvSpace[uvSpaceNo].freeRadial[radial.start + j];
                double spring = SrcEdgeVtx().uvSpace[uvSpaceNo].freeEdge[freeRadial.edgeIndex].spring;
                springSum += spring;
                if (freeRadial.isFixVtx == true) {
                    lvMakeUVspaceType.VtxOne fixVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].fixVtx[freeRadial.mateVtxIndex];
                    lvRec.SeqPart vtxUV = SrcUVpublic().vtxUV[fixVtx.vtxNo];
                    lvUVdt uv = DstUVspaceUV().vtxUVseq[vtxUV.start + fixVtx.uvSpaceOfs];
                    cu += spring * uv.u;
                    cv += spring * uv.v;
                } else {
                    SparMat().setElm[cnt].row = i;
                    SparMat().setElm[cnt].column = freeRadial.mateVtxIndex;
                    SparMat().setElm[cnt].data = -spring;
                    cnt++;
                }
            }
            SparMat().cu[i] = cu;
            SparMat().cv[i] = cv;
            SparMat().setElm[cnt].row = i;
            SparMat().setElm[cnt].column = i;
            SparMat().setElm[cnt].data = springSum;
            cnt++;
        }
        Gbl().sparseMatrix.Set(numFreeVtx, SparMat().setElm);
        Gbl().sparseMatrix.PreConjGrad();
    }

    private final void NewXuXv(int uvSpaceNo) {
        int numFreeVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx.length;
        SparMat().xu = new double[numFreeVtx];
        SparMat().xv = new double[numFreeVtx];
        for (int i = 0; i < numFreeVtx; i++) {
            SparMat().xu[i] = 1.0;
            SparMat().xv[i] = 1.0;
        }
    }

    private final void SetFreeUV(int uvSpaceNo) throws lvThrowable {
        Gbl().sparseMatrix.ConjGrad(SparMat().cu, SparMat().xu);
        Gbl().sparseMatrix.ConjGrad(SparMat().cv, SparMat().xv);
        int numFreeVtx = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx.length;
        for (int i = 0; i < numFreeVtx; i++) {
            int vtxNo = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx[i].vtxNo;
            int uvSpaceOfs = SrcEdgeVtx().uvSpace[uvSpaceNo].freeVtx[i].uvSpaceOfs;
            lvRec.SeqPart vtxUV = SrcUVpublic().vtxUV[vtxNo];
            DstUVspaceUV().vtxUVseq[vtxUV.start + uvSpaceOfs].u = SparMat().xu[i];
            DstUVspaceUV().vtxUVseq[vtxUV.start + uvSpaceOfs].v = SparMat().xv[i];
        }
    }

    private final void Finish() {
        Gbl().srcUVpublic = null;
        Gbl().srcEdgeVtx = null;
        Gbl().angleFix = null;
    }
}
