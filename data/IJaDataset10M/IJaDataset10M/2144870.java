package org.t2framework.t2.contexts;

import java.util.List;
import java.util.Map;

/**
 * <#if locale="en">
 * <p>
 * Multipart is an interface to hold UploadFiles. This instance should be set to
 * HttpServletRequest.setAttribute() with a specified key.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * @see org.t2framework.t2.filter.MultiPartRequestFilter
 */
public interface Multipart {

    /**
	 * <#if locale="en">
	 * <p>
	 * Get map of name and UploadFile[].
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return upload file map
	 */
    Map<String, UploadFile[]> getUploadMap();

    /**
	 * <#if locale="en">
	 * <p>
	 * Get list of UploadFile.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @return list of upload file
	 */
    List<UploadFile> getUploadList();

    /**
	 * <#if locale="en">
	 * <p>
	 * Add UploadFile.
	 * 
	 * </p>
	 * <#else>
	 * <p>
	 * 
	 * </p>
	 * </#if>
	 * 
	 * @param key
	 * @param uploadFile
	 */
    void addUploadFile(String key, UploadFile uploadFile);
}
