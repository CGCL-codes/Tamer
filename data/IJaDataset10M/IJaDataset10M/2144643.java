package net.sf.balm.persistence;

/**
 * @author dz
 */
public interface NativeQueryRequestAware {

    /**
     * @param nativeQueryRequest
     */
    public void setNativeQueryRequest(NativeQueryRequest nativeQueryRequest);
}
