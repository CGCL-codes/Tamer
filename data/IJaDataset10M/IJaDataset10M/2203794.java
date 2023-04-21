package mil.usmc.mgrs.milGrid;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import mil.usmc.mgrs.AbstractProjection;
import mil.usmc.mgrs.IProjection;
import mil.usmc.mgrs.milGrid.UtmPoint.EHemisphere;
import mil.usmc.mgrs.objects.R2;

public class UtmG {

    public static int DT_GREATCIRCLE = 0;

    public static int DT_RHUMBLINE = 1;

    public static int nTL = 0;

    public static int nTR = 1;

    public static int nBR = 2;

    public static final int e_10M = 10;

    public static final int e_100M = 100;

    public static final int e_1K = 1000;

    public static final int e_10K = 10000;

    public static final int e_100K = 100000;

    protected int m_iGcRl;

    protected int m_nZones;

    protected int m_iZone;

    protected boolean m_bDrawLabels;

    protected int m_iLevelOfDetail;

    protected double m_dWidthDeg;

    protected Madtran m_cm = new Madtran();

    protected int m_gridSize;

    protected ClipZone m_clipZone;

    protected IProjection m_proj = null;

    protected int m_scrnDpi = AbstractProjection.DOTS_PER_INCH;

    protected double m_scrnMpp = 2.54 / m_scrnDpi * 100.0;

    protected R2 m_r2Pt = new R2();

    protected int[] m_iStartTopEw = new int[1];

    protected int[] m_iBotEw = new int[1];

    protected int[] m_iEndBotEw = new int[1];

    protected int[] m_iTopEw = new int[1];

    protected int[] m_iEndTopEw = new int[1];

    protected ArrayList<ClipZone> m_caZoneList = new ArrayList<ClipZone>();

    public UtmG() {
        m_iGcRl = DT_GREATCIRCLE;
        m_dWidthDeg = 0;
        m_bDrawLabels = true;
    }

    public int widthToGridSpace() {
        int gridSpace = 5;
        if (m_dWidthDeg > 90) gridSpace = 5; else if (m_dWidthDeg > 22.5) gridSpace = 3; else if (m_dWidthDeg > 5) gridSpace = 2; else gridSpace = 1;
        return gridSpace;
    }

    public void drawGrid(Graphics2D g, IProjection proj, PixBoundingBox box) {
        double boxWidth = box.getWidth();
        m_dWidthDeg = 360.0 * (boxWidth / proj.mapWidthInPix());
        if (m_dWidthDeg < 15) {
            _DrawNkGrid(g, proj, box);
        } else {
            int gridSize = widthToGridSpace();
            UtmGrid um = new UtmGrid(proj);
            um.drawGrid(g, box, gridSize);
        }
    }

    protected double degToMeters(double deg) {
        return (deg * 60 * 1853);
    }

    protected void _DrawNkGrid(Graphics2D g, IProjection proj, PixBoundingBox box) {
        double dWidthM = degToMeters(m_dWidthDeg);
        if (dWidthM < 100) {
            m_gridSize = e_10M;
        } else if (dWidthM < 1000) {
            m_gridSize = e_100M;
        } else if (dWidthM < 10000) {
            m_gridSize = e_1K;
        } else if (dWidthM < 100000) {
            m_gridSize = e_10K;
        } else {
            m_gridSize = e_100K;
        }
        UtmPoint bl = new UtmPoint();
        UtmPoint tl = new UtmPoint();
        UtmPoint tr = new UtmPoint();
        UtmPoint br = new UtmPoint();
        if (!Utmhelp.getUtmCornerPts(m_cm, proj, box, bl, tl, tr, br)) return;
        UtmRegion ur = new UtmRegion(bl, tl, tr, br);
        m_nZones = ur.GetClipZones(m_caZoneList);
        _DrawClipZones(g, proj, box);
    }

    protected void _DrawClipZones(Graphics2D g, IProjection proj, PixBoundingBox box) {
        for (int i = 0; i < m_nZones; i++) {
            m_clipZone = m_caZoneList.get(i);
            m_iZone = m_clipZone.getZone();
            _DrawClipZone(g, proj, box);
        }
        return;
    }

    public void _DrawClipZone(Graphics2D g, IProjection proj, PixBoundingBox box) {
        int iStartNs, iEndNs;
        EHemisphere eHem;
        Northing sStartNs = new Northing();
        Northing sEndNs = new Northing();
        m_clipZone.getMinMaxClipNorthing(sStartNs, sEndNs);
        iStartNs = sStartNs.iNs;
        if (sStartNs.eHem != sEndNs.eHem) {
            eHem = sStartNs.eHem;
            iEndNs = 9999999;
            _LoopNsAndDraw(g, proj, box, eHem, iStartNs, iEndNs);
            iStartNs = 0;
        }
        eHem = sEndNs.eHem;
        iEndNs = sEndNs.iNs;
        _LoopNsAndDraw(g, proj, box, eHem, iStartNs, iEndNs);
    }

    public void _LoopNsAndDraw(Graphics2D g, IProjection proj, PixBoundingBox box, EHemisphere eHem, int iStartNs, int iEndNs) {
        int iBotNs, iTopNs;
        iBotNs = iStartNs;
        iTopNs = Utmhelp.getNextInc(iBotNs, iEndNs, m_gridSize);
        while (iBotNs < iEndNs) {
            _DrawLines(g, proj, box, eHem, iBotNs, iTopNs);
            iBotNs = iTopNs;
            iTopNs = Utmhelp.getNextInc(iBotNs, iEndNs, m_gridSize);
        }
        return;
    }

    public void _DrawLines(Graphics2D g, IProjection proj, PixBoundingBox box, EHemisphere eHem, int iBotNs, int iTopNs) {
        int iInc;
        int iStartTopEw;
        int iWidth = box.getWidth();
        int iHeight = box.getHeight();
        R2[] pt = creatR2_Array(3);
        UtmPoint[] utmPt = creatUtmPointArray(3);
        Northing sNs = new Northing();
        iInc = (int) m_gridSize;
        utmPt[nTL].iZone = utmPt[nTR].iZone = utmPt[nBR].iZone = m_iZone;
        utmPt[nTL].eHem = utmPt[nTR].eHem = utmPt[nBR].eHem = sNs.eHem = eHem;
        utmPt[nBR].iNorthing = iBotNs;
        utmPt[nTL].iNorthing = utmPt[nTR].iNorthing = iTopNs;
        sNs.iNs = iBotNs;
        m_clipZone.getClipLeftRightEastings(m_cm, m_gridSize, sNs, m_iBotEw, m_iEndBotEw);
        sNs.iNs = iTopNs;
        m_clipZone.getClipLeftRightEastings(m_cm, m_gridSize, sNs, m_iTopEw, m_iEndTopEw);
        char cHem = Utmhelp.getHemChar(eHem);
        if (m_iTopEw[0] < m_iBotEw[0] - iInc) {
            int[] iCellTopNs = new int[1];
            UtmLocSize cell = Utmhelp.findUtmCell(m_cm, cHem, m_iZone, iBotNs);
            if (cell != null && Utmhelp.getCellTopNorthing(m_cm, cell, iCellTopNs)) {
                sNs.iNs = iCellTopNs[0];
                m_clipZone.getClipLeftRightEastings(m_cm, m_gridSize, sNs, m_iBotEw, m_iEndBotEw);
            }
        } else if (m_iBotEw[0] < m_iTopEw[0] - iInc) {
            int[] iCellBotNs = new int[1];
            UtmLocSize cell = Utmhelp.findUtmCell(m_cm, cHem, m_iZone, iTopNs);
            if (cell != null && Utmhelp.getCellBotNorthing(m_cm, cell, iCellBotNs)) {
                sNs.iNs = iCellBotNs[0];
                m_clipZone.getClipLeftRightEastings(m_cm, m_gridSize, sNs, m_iTopEw, m_iEndTopEw);
            }
        }
        iStartTopEw = m_iTopEw[0];
        int iBotNextEw = Utmhelp.getNextInc(m_iBotEw[0], m_iEndBotEw[0], iInc);
        int iTopNextEw = Utmhelp.getNextInc(m_iTopEw[0], m_iEndTopEw[0], iInc);
        iTopNextEw = Utmhelp.adjustEw(iTopNextEw, iBotNextEw, iStartTopEw, m_iEndTopEw[0], iInc);
        while (m_iBotEw[0] < m_iEndBotEw[0]) {
            utmPt[nTL].iEasting = m_iTopEw[0];
            utmPt[nTR].iEasting = iTopNextEw;
            utmPt[nBR].iEasting = iBotNextEw;
            R2 p;
            int minX = 0;
            int maxX = 0;
            int minY = 0;
            int maxY = 0;
            if (Math.abs(iTopNextEw - iBotNextEw) < iInc / 2) {
                p = Utmhelp.utmToWorldPix(m_cm, proj, utmPt[nTL]);
                pt[nTL].copy(box.worldPixToBoxPt(p));
                minX = pt[nTL].m_x;
                maxX = pt[nTL].m_x;
                minY = pt[nTL].m_y;
                maxY = pt[nTL].m_y;
                p = Utmhelp.utmToWorldPix(m_cm, proj, utmPt[nTR]);
                pt[nTR].copy(box.worldPixToBoxPt(p));
                minX = Math.min(pt[nTR].m_x, minX);
                maxX = Math.max(pt[nTR].m_x, maxX);
                minY = Math.min(pt[nTR].m_y, minY);
                maxY = Math.max(pt[nTR].m_y, maxY);
                p = Utmhelp.utmToWorldPix(m_cm, proj, utmPt[nBR]);
                pt[nBR].copy(box.worldPixToBoxPt(p));
                minX = Math.min(pt[nBR].m_x, minX);
                maxX = Math.max(pt[nBR].m_x, maxX);
                minY = Math.min(pt[nBR].m_y, minY);
                maxY = Math.max(pt[nBR].m_y, maxY);
                boolean bDraw = true;
                if (maxX < 1 || minX > iWidth) bDraw = false;
                if (maxY < 1 || minY > iHeight) bDraw = false;
                if (pt[nTR].m_x < pt[nTL].m_x) {
                    pt[nTR].m_x = pt[nTL].m_x;
                }
                if (bDraw) {
                    Utmhelp.drawPolyline(g, pt, 3);
                    _DrawNkLabel(g, proj, box, m_iEndBotEw[0], utmPt, pt);
                }
            }
            if (pt[nTR].m_x > iWidth || pt[nBR].m_x > iWidth) break;
            m_iBotEw[0] = iBotNextEw;
            m_iTopEw[0] = iTopNextEw;
            iBotNextEw = Utmhelp.getNextInc(m_iBotEw[0], m_iEndBotEw[0], iInc);
            iTopNextEw = Utmhelp.getNextInc(m_iTopEw[0], m_iEndTopEw[0], iInc);
            iTopNextEw = Utmhelp.adjustEw(iTopNextEw, iBotNextEw, m_iStartTopEw[0], m_iEndTopEw[0], iInc);
        }
    }

    protected UtmPoint[] creatUtmPointArray(int n) {
        if (n < 1) return null;
        UtmPoint[] utmPt = new UtmPoint[n];
        for (int i = 0; i < n; i++) utmPt[i] = new UtmPoint();
        return utmPt;
    }

    protected R2[] creatR2_Array(int n) {
        if (n < 1) return null;
        R2[] r = new R2[n];
        for (int i = 0; i < n; i++) r[i] = new R2();
        return r;
    }

    protected void _DrawNkLabel(Graphics2D g, IProjection proj, PixBoundingBox box, int iEndBotEw, UtmPoint[] pUtm, R2[] pPt) {
        int cx, cy;
        int tlX, tlY;
        int trX, trY;
        int brX, brY;
        int iBase;
        int iLine;
        String csMgr;
        String csStr;
        int vWidth = box.getWidth() + 1;
        int vHeight = box.getHeight() + 1;
        if (!m_bDrawLabels) return;
        R2 pt = new R2();
        FontMetrics fm = g.getFontMetrics();
        cy = fm.getHeight();
        switch(m_gridSize) {
            case e_100K:
                pt.m_x = pPt[nTR].m_x - 2;
                pt.m_y = pPt[nTR].m_y + 2;
                csMgr = Utmhelp.boxPixToMgr(m_cm, proj, box, pt);
                if (csMgr == null || csMgr.length() < 1) return;
                csStr = csMgr.substring(4, 6);
                cx = 2 * fm.charWidth(csStr.charAt(0));
                if (Math.abs(pPt[nTL].m_x - pPt[nTR].m_x) < 1.5 * cx || pt.m_y < -2) return;
                g.drawString(csStr, pt.m_x - cx, pt.m_y + 15);
                break;
            case e_10K:
            case e_1K:
                tlX = pPt[nTL].m_x;
                tlY = pPt[nTL].m_y;
                trX = pPt[nTR].m_x;
                trY = pPt[nTR].m_y;
                brX = pPt[nBR].m_x;
                brY = pPt[nBR].m_y;
                if (0 < brX && brX < vWidth) {
                    if (trY < vHeight && brY >= vHeight) {
                        iLine = pUtm[nBR].iEasting;
                        if (iLine >= iEndBotEw) {
                            pt.m_x = pPt[nBR].m_x + 2;
                            pt.m_y = pPt[nBR].m_y - 2;
                            UtmPoint sUpt = Utmhelp.boxPixToUtm(m_cm, proj, box, pt);
                            iLine = sUpt.iEasting;
                        }
                        iLine = iLine % 100000;
                        csStr = String.format("%02d", iLine / 1000);
                        g.drawString(csStr, brX + 7, vHeight - cy - 5);
                    }
                }
                if (0 < trY && trY < vHeight) {
                    if (tlX < vWidth && trX > vWidth) {
                        iLine = pUtm[nTR].iNorthing;
                        iLine = iLine % 1000000;
                        if (iLine > 999900) iLine = 0;
                        iLine = iLine % 100000;
                        csStr = String.format("%02d", iLine / 1000);
                        cx = 2 * fm.charWidth(csStr.charAt(0));
                        g.drawString(csStr, vWidth - cx - 5, vHeight - cy - 5);
                    }
                }
                break;
            case e_100M:
            case e_10M:
                tlX = pPt[nTL].m_x;
                tlY = pPt[nTL].m_y;
                trX = pPt[nTR].m_x;
                trY = pPt[nTR].m_y;
                brX = pPt[nBR].m_x;
                brY = pPt[nBR].m_y;
                iBase = (int) m_gridSize * 10;
                if (brX > 0 && brX < vWidth) {
                    if (trY < vHeight && brY >= vHeight) {
                        iLine = pUtm[nBR].iEasting;
                        if (iLine >= iEndBotEw) {
                            pt.m_x = pPt[nBR].m_x + 30;
                            pt.m_y = pPt[nBR].m_y - 2;
                            UtmPoint sUpt = Utmhelp.boxPixToUtm(m_cm, proj, box, pt);
                            iLine = sUpt.iEasting;
                        }
                        iLine = iLine % iBase;
                        csStr = String.format("%02d", iLine / (int) (m_gridSize));
                        cx = 2 * fm.charWidth(csStr.charAt(0));
                        g.drawString(csStr, vWidth - cx - 5, vHeight - cy - 5);
                    }
                }
                if (trY > 0 && trY < vHeight) {
                    if (tlX < vWidth && trX > vWidth) {
                        iLine = pUtm[nTR].iNorthing;
                        iLine = iLine % iBase;
                        if (iLine > 9 * (int) (m_gridSize)) iLine = 0;
                        csStr = String.format("%02d", iLine / (int) (m_gridSize));
                        cx = 2 * fm.charWidth(csStr.charAt(0));
                        g.drawString(csStr, vWidth - cx - 5, trY + cy + 15);
                    }
                }
                break;
            default:
                break;
        }
        return;
    }
}
