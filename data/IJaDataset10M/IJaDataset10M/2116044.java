package co.edu.unal.ungrid.interpolation;

import java.util.Hashtable;
import co.edu.unal.ungrid.grid.worker.subtraction.SplineCoefficientsEntry;
import co.edu.unal.ungrid.image.AbstractPlane;

public abstract class CacheSplineInterpolator<Plane extends AbstractPlane> extends SplineInterpolator<Plane> {

    public abstract Interpolator<Plane> clone() throws CloneNotSupportedException;

    public abstract SplineCoefficientsEntry computeCoefficients(int[] naImgData, int width, int height, String sId);

    public CacheSplineInterpolator() {
        m_coeffsCache = new Hashtable<String, SplineCoefficientsEntry>();
    }

    public SplineCoefficientsEntry getCache(String sId) {
        return m_coeffsCache.get(sId);
    }

    public void setCache(String sId, SplineCoefficientsEntry splineCoeffs) {
        m_coeffsCache.put(sId, splineCoeffs);
    }

    public void clearCache() {
        m_coeffsCache.clear();
    }

    private Hashtable<String, SplineCoefficientsEntry> m_coeffsCache;
}
