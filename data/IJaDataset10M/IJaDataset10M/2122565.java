package com.liferay.portal.kernel.lar;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.zip.ZipReader;
import com.liferay.portal.kernel.zip.ZipWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <a href="PortletDataContext.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * Holds context information that is used during exporting adn importing portlet
 * data.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 *
 */
public interface PortletDataContext extends Serializable {

    public long getCompanyId();

    public long getGroupId();

    public long getPlid();

    public void setPlid(long plid);

    public Map getParameterMap();

    public Set getPrimaryKeys();

    public boolean addPrimaryKey(Class classObj, Object primaryKey);

    public boolean hasPrimaryKey(Class classObj, Object primaryKey);

    public String[] getTagsEntries(Class classObj, Object primaryKey);

    public String[] getTagsEntries(String className, Object primaryKey);

    public Map getTagsEntries();

    public void addTagsEntries(Class classObj, Object classPK) throws PortalException, SystemException;

    public void addTagsEntries(String className, Object classPK, String[] values) throws PortalException, SystemException;

    public ZipReader getZipReader();

    public ZipWriter getZipWriter();
}
