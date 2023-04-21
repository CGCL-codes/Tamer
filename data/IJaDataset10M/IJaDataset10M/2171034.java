package org.t2framework.t2.exception;

/**
 * 
 * <#if locale="en">
 * <p>
 * Throws exception {@link org.t2framework.t2.contexts.UploadFile} is not found
 * in {@link org.t2framework.t2.contexts.Multipart} .
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class UploadFileNotFoundException extends T2BaseRuntimeException {

    private static final long serialVersionUID = 1L;

    public UploadFileNotFoundException(String uploadFileName) {
        super("ETDT0023", new Object[] { uploadFileName });
    }
}
