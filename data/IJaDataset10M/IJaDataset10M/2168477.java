package org.ode4j.ode.internal;

import java.util.Arrays;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DContactGeomBuffer;
import org.ode4j.ode.DHeightfieldData;
import org.ode4j.ode.DHeightfield.DHeightfieldGetHeight;
import org.ode4j.ode.internal.DxHeightfield.HeightFieldVertex;
import static org.ode4j.ode.OdeMath.*;

/**
 * dHeightfield Collider
 * Paul Cheyrou-Lagreze aka Tuan Kuranes 2006 Speed enhancements http://www.pop-3d.com
 * Martijn Buijs 2006 http://home.planet.nl/~buijs512/
 * Based on Terrain & Cone contrib by:
 * Benoit CHAPEROT 2003-2004 http://www.jstarlab.com
 * Some code inspired by Magic Software
 */
public class DxHeightfieldData implements DHeightfieldData {

    double m_fWidth;

    double m_fDepth;

    double m_fSampleWidth;

    double m_fSampleDepth;

    double m_fSampleZXAspect;

    double m_fInvSampleWidth;

    double m_fInvSampleDepth;

    double m_fHalfWidth;

    double m_fHalfDepth;

    double m_fMinHeight;

    double m_fMaxHeight;

    double m_fThickness;

    double m_fScale;

    double m_fOffset;

    int m_nWidthSamples;

    int m_nDepthSamples;

    boolean m_bCopyHeightData;

    boolean m_bWrapMode;

    int m_nGetHeightMode;

    Object m_pHeightData;

    Object m_pUserData;

    DContactGeomBuffer m_contacts = new DContactGeomBuffer(DxHeightfield.HEIGHTFIELDMAXCONTACTPERCELL);

    /** @deprecated TODO uses CPP-API! */
    DHeightfieldGetHeight m_pGetHeightCallback;

    DxHeightfieldData() {
        m_fWidth = 0;
        m_fDepth = 0;
        m_fSampleWidth = 0;
        m_fSampleDepth = 0;
        m_fSampleZXAspect = 0;
        m_fInvSampleWidth = 0;
        m_fInvSampleDepth = 0;
        m_fHalfWidth = 0;
        m_fHalfDepth = 0;
        m_fMinHeight = 0;
        m_fMaxHeight = 0;
        m_fThickness = 0;
        m_fScale = 0;
        m_fOffset = 0;
        m_nWidthSamples = 0;
        m_nDepthSamples = 0;
        m_bCopyHeightData = false;
        m_bWrapMode = false;
        m_nGetHeightMode = 0;
        m_pHeightData = null;
        m_pUserData = null;
        m_pGetHeightCallback = null;
    }

    void SetData(int nWidthSamples, int nDepthSamples, double fWidth, double fDepth, double fScale, double fOffset, double fThickness, boolean bWrapMode) {
        dIASSERT(fWidth > (0.0));
        dIASSERT(fDepth > (0.0));
        dIASSERT(nWidthSamples > 0);
        dIASSERT(nDepthSamples > 0);
        m_fWidth = fWidth;
        m_fDepth = fDepth;
        m_fHalfWidth = fWidth / (2.0);
        m_fHalfDepth = fDepth / (2.0);
        m_fScale = fScale;
        m_fOffset = fOffset;
        m_fThickness = fThickness;
        m_nWidthSamples = nWidthSamples;
        m_nDepthSamples = nDepthSamples;
        m_fSampleWidth = m_fWidth / (m_nWidthSamples - (1.0));
        m_fSampleDepth = m_fDepth / (m_nDepthSamples - (1.0));
        m_fSampleZXAspect = m_fSampleDepth / m_fSampleWidth;
        m_fInvSampleWidth = (1.0) / m_fSampleWidth;
        m_fInvSampleDepth = (1.0) / m_fSampleDepth;
        m_bWrapMode = bWrapMode;
    }

    void ComputeHeightBounds() {
        int i;
        double h;
        byte[] data_byte;
        short[] data_short;
        float[] data_float;
        double[] data_double;
        switch(m_nGetHeightMode) {
            case 0:
                return;
            case 1:
                data_byte = (byte[]) m_pHeightData;
                m_fMinHeight = dInfinity;
                m_fMaxHeight = -dInfinity;
                for (i = 0; i < m_nWidthSamples * m_nDepthSamples; i++) {
                    h = data_byte[i];
                    if (h < m_fMinHeight) m_fMinHeight = h;
                    if (h > m_fMaxHeight) m_fMaxHeight = h;
                }
                break;
            case 2:
                data_short = (short[]) m_pHeightData;
                m_fMinHeight = dInfinity;
                m_fMaxHeight = -dInfinity;
                for (i = 0; i < m_nWidthSamples * m_nDepthSamples; i++) {
                    h = data_short[i];
                    if (h < m_fMinHeight) m_fMinHeight = h;
                    if (h > m_fMaxHeight) m_fMaxHeight = h;
                }
                break;
            case 3:
                data_float = (float[]) m_pHeightData;
                m_fMinHeight = dInfinity;
                m_fMaxHeight = -dInfinity;
                for (i = 0; i < m_nWidthSamples * m_nDepthSamples; i++) {
                    h = data_float[i];
                    if (h < m_fMinHeight) m_fMinHeight = h;
                    if (h > m_fMaxHeight) m_fMaxHeight = h;
                }
                break;
            case 4:
                data_double = (double[]) m_pHeightData;
                m_fMinHeight = dInfinity;
                m_fMaxHeight = -dInfinity;
                for (i = 0; i < m_nWidthSamples * m_nDepthSamples; i++) {
                    h = data_double[i];
                    if (h < m_fMinHeight) m_fMinHeight = h;
                    if (h > m_fMaxHeight) m_fMaxHeight = h;
                }
                break;
        }
        m_fMinHeight *= m_fScale;
        m_fMaxHeight *= m_fScale;
        m_fMinHeight += m_fOffset;
        m_fMaxHeight += m_fOffset;
        m_fMinHeight -= m_fThickness;
    }

    boolean IsOnHeightfield2(final HeightFieldVertex CellCorner, final DVector3C pos, final boolean isABC) {
        double MaxX, MinX;
        double MaxZ, MinZ;
        if (isABC) {
            MinX = CellCorner.vertex.get0();
            if (pos.get0() < MinX) return false;
            MaxX = (CellCorner.coords0 + 1) * m_fSampleWidth;
            if (pos.get0() >= MaxX) return false;
            MinZ = CellCorner.vertex.get2();
            if (pos.get2() < MinZ) return false;
            MaxZ = (CellCorner.coords1 + 1) * m_fSampleDepth;
            if (pos.get2() >= MaxZ) return false;
            return (MaxZ - pos.get2()) > (pos.get0() - MinX) * m_fSampleZXAspect;
        } else {
            MaxX = CellCorner.vertex.get0();
            if (pos.get0() >= MaxX) return false;
            MinX = (CellCorner.coords0 - 1) * m_fSampleWidth;
            if (pos.get0() < MinX) return false;
            MaxZ = CellCorner.vertex.get2();
            if (pos.get2() >= MaxZ) return false;
            MinZ = (CellCorner.coords1 - 1) * m_fSampleDepth;
            if (pos.get2() < MinZ) return false;
            return (MaxZ - pos.get2()) <= (pos.get0() - MinX) * m_fSampleZXAspect;
        }
    }

    double GetHeight(int x, int z) {
        double h = 0;
        byte[] data_byte;
        short[] data_short;
        float[] data_float;
        double[] data_double;
        if (m_bWrapMode == false) {
            if (x < 0) x = 0;
            if (z < 0) z = 0;
            if (x > m_nWidthSamples - 1) x = m_nWidthSamples - 1;
            if (z > m_nDepthSamples - 1) z = m_nDepthSamples - 1;
        } else {
            x %= m_nWidthSamples - 1;
            z %= m_nDepthSamples - 1;
            if (x < 0) x += m_nWidthSamples - 1;
            if (z < 0) z += m_nDepthSamples - 1;
        }
        switch(m_nGetHeightMode) {
            case 0:
                h = m_pGetHeightCallback.call(m_pUserData, x, z);
                break;
            case 1:
                data_byte = (byte[]) m_pHeightData;
                h = data_byte[x + (z * m_nWidthSamples)];
                break;
            case 2:
                data_short = (short[]) m_pHeightData;
                h = data_short[x + (z * m_nWidthSamples)];
                break;
            case 3:
                data_float = (float[]) m_pHeightData;
                h = data_float[x + (z * m_nWidthSamples)];
                break;
            case 4:
                data_double = (double[]) m_pHeightData;
                h = (double) (data_double[x + (z * m_nWidthSamples)]);
                break;
        }
        return (h * m_fScale) + m_fOffset;
    }

    void DESTRUCTOR() {
    }

    public static DHeightfieldData dGeomHeightfieldDataCreate() {
        return new DxHeightfieldData();
    }

    public void dGeomHeightfieldDataBuildCallback(Object pUserData, DHeightfieldGetHeight pCallback, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dIASSERT(pCallback != null);
        dIASSERT(widthSamples >= 2);
        dIASSERT(depthSamples >= 2);
        m_nGetHeightMode = 0;
        m_pUserData = pUserData;
        m_pGetHeightCallback = pCallback;
        SetData(widthSamples, depthSamples, width, depth, scale, offset, thickness, bWrap);
        m_fMinHeight = -dInfinity;
        m_fMaxHeight = dInfinity;
    }

    void dGeomHeightfieldDataBuildByte(final byte[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dIASSERT(pHeightData != null);
        dIASSERT(widthSamples >= 2);
        dIASSERT(depthSamples >= 2);
        SetData(widthSamples, depthSamples, width, depth, scale, offset, thickness, bWrap);
        m_nGetHeightMode = 1;
        m_bCopyHeightData = bCopyHeightData;
        if (m_bCopyHeightData == false) {
            m_pHeightData = pHeightData;
        } else {
            m_pHeightData = Arrays.copyOf(pHeightData, m_nWidthSamples * m_nDepthSamples);
        }
        ComputeHeightBounds();
    }

    void dGeomHeightfieldDataBuildShort(final short[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dIASSERT(pHeightData != null);
        dIASSERT(widthSamples >= 2);
        dIASSERT(depthSamples >= 2);
        SetData(widthSamples, depthSamples, width, depth, scale, offset, thickness, bWrap);
        m_nGetHeightMode = 2;
        m_bCopyHeightData = bCopyHeightData;
        if (m_bCopyHeightData == false) {
            m_pHeightData = pHeightData;
        } else {
            m_pHeightData = Arrays.copyOf(pHeightData, m_nWidthSamples * m_nDepthSamples);
        }
        ComputeHeightBounds();
    }

    void dGeomHeightfieldDataBuildSingle(final float[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dIASSERT(pHeightData != null);
        dIASSERT(widthSamples >= 2);
        dIASSERT(depthSamples >= 2);
        SetData(widthSamples, depthSamples, width, depth, scale, offset, thickness, bWrap);
        m_nGetHeightMode = 3;
        m_bCopyHeightData = bCopyHeightData;
        if (m_bCopyHeightData == false) {
            m_pHeightData = pHeightData;
        } else {
            m_pHeightData = Arrays.copyOf(pHeightData, m_nWidthSamples * m_nDepthSamples);
        }
        ComputeHeightBounds();
    }

    void dGeomHeightfieldDataBuildDouble(final double[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dIASSERT(pHeightData != null);
        dIASSERT(widthSamples >= 2);
        dIASSERT(depthSamples >= 2);
        SetData(widthSamples, depthSamples, width, depth, scale, offset, thickness, bWrap);
        m_nGetHeightMode = 4;
        m_bCopyHeightData = bCopyHeightData;
        if (m_bCopyHeightData == false) {
            m_pHeightData = pHeightData;
        } else {
            m_pHeightData = Arrays.copyOf(pHeightData, m_nWidthSamples * m_nDepthSamples);
        }
        ComputeHeightBounds();
    }

    public void dGeomHeightfieldDataSetBounds(double minHeight, double maxHeight) {
        m_fMinHeight = (minHeight * m_fScale) + m_fOffset - m_fThickness;
        m_fMaxHeight = (maxHeight * m_fScale) + m_fOffset;
    }

    public void dGeomHeightfieldDataDestroy() {
        DESTRUCTOR();
    }

    @Override
    public void destroy() {
        dGeomHeightfieldDataDestroy();
    }

    @Override
    public void setBounds(double minHeight, double maxHeight) {
        dGeomHeightfieldDataSetBounds(minHeight, maxHeight);
    }

    @Override
    public void buildCallback(Object userData, DHeightfieldGetHeight callback, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean wrap) {
        dGeomHeightfieldDataBuildCallback(userData, callback, width, depth, widthSamples, depthSamples, scale, offset, thickness, wrap);
    }

    @Override
    public void buildByte(final byte[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        build(pHeightData, bCopyHeightData, width, depth, widthSamples, depthSamples, scale, offset, thickness, bWrap);
    }

    @Override
    public void build(final byte[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dGeomHeightfieldDataBuildByte(pHeightData, bCopyHeightData, width, depth, widthSamples, depthSamples, scale, offset, thickness, bWrap);
    }

    @Override
    public void build(short[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dGeomHeightfieldDataBuildShort(pHeightData, bCopyHeightData, width, depth, widthSamples, depthSamples, scale, offset, thickness, bWrap);
    }

    @Override
    public void build(float[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dGeomHeightfieldDataBuildSingle(pHeightData, bCopyHeightData, width, depth, widthSamples, depthSamples, scale, offset, thickness, bWrap);
    }

    @Override
    public void build(double[] pHeightData, boolean bCopyHeightData, double width, double depth, int widthSamples, int depthSamples, double scale, double offset, double thickness, boolean bWrap) {
        dGeomHeightfieldDataBuildDouble(pHeightData, bCopyHeightData, width, depth, widthSamples, depthSamples, scale, offset, thickness, bWrap);
    }
}
