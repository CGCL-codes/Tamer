package com.liferay.portlet.journal.webdav;

import com.liferay.portal.PortalException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.webdav.BaseResourceImpl;
import com.liferay.portal.webdav.BaseWebDAVStorageImpl;
import com.liferay.portal.webdav.Resource;
import com.liferay.portal.webdav.Status;
import com.liferay.portal.webdav.WebDAVException;
import com.liferay.portal.webdav.WebDAVRequest;
import com.liferay.portlet.journal.NoSuchStructureException;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalStructureServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateServiceUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href="JournalWebDAVStorageImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalWebDAVStorageImpl extends BaseWebDAVStorageImpl {

    public Status addFolder(WebDAVRequest webDavReq) throws WebDAVException {
        return new Status(HttpServletResponse.SC_FORBIDDEN);
    }

    public int copyResource(WebDAVRequest webDavReq, String destination) throws WebDAVException {
        return HttpServletResponse.SC_FORBIDDEN;
    }

    public int deleteResource(WebDAVRequest webDavReq) throws WebDAVException {
        try {
            Resource resource = getResource(webDavReq);
            if (resource == null) {
                return HttpServletResponse.SC_NOT_FOUND;
            }
            Object model = resource.getModel();
            if (model instanceof JournalStructure) {
                JournalStructure structure = (JournalStructure) model;
                JournalStructureServiceUtil.deleteStructure(structure.getGroupId(), structure.getStructureId());
                return HttpServletResponse.SC_NO_CONTENT;
            } else if (model instanceof JournalTemplate) {
                JournalTemplate template = (JournalTemplate) model;
                JournalTemplateServiceUtil.deleteTemplate(template.getGroupId(), template.getTemplateId());
                return HttpServletResponse.SC_NO_CONTENT;
            } else {
                return HttpServletResponse.SC_FORBIDDEN;
            }
        } catch (PortalException pe) {
            return HttpServletResponse.SC_FORBIDDEN;
        } catch (Exception e) {
            throw new WebDAVException(e);
        }
    }

    public Resource getResource(WebDAVRequest webDavReq) throws WebDAVException {
        try {
            String[] pathArray = webDavReq.getPathArray();
            if (pathArray.length == 3) {
                String type = pathArray[2];
                return toResource(webDavReq, type, false);
            } else if (pathArray.length == 4) {
                String type = pathArray[2];
                String journalTypeId = pathArray[3];
                if (type.equals(_TYPE_STRUCTURES)) {
                    try {
                        JournalStructure journalStructure = JournalStructureLocalServiceUtil.getStructure(webDavReq.getGroupId(), journalTypeId);
                        return toResource(webDavReq, journalStructure, false);
                    } catch (NoSuchStructureException nsse) {
                        return null;
                    }
                } else if (type.equals(_TYPE_TEMPLATES)) {
                    try {
                        JournalTemplate journalTemplate = JournalTemplateLocalServiceUtil.getTemplate(webDavReq.getGroupId(), journalTypeId);
                        return toResource(webDavReq, journalTemplate, false);
                    } catch (NoSuchTemplateException nste) {
                        return null;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new WebDAVException(e);
        }
    }

    public List getResources(WebDAVRequest webDavReq) throws WebDAVException {
        try {
            String[] pathArray = webDavReq.getPathArray();
            if (pathArray.length == 2) {
                return getFolders(webDavReq);
            } else if (pathArray.length == 3) {
                String type = pathArray[2];
                if (type.equals(_TYPE_STRUCTURES)) {
                    return getStructures(webDavReq);
                } else if (type.equals(_TYPE_TEMPLATES)) {
                    return getTemplates(webDavReq);
                }
            }
            return new ArrayList();
        } catch (Exception e) {
            throw new WebDAVException(e);
        }
    }

    public int moveResource(WebDAVRequest webDavReq, String destination) throws WebDAVException {
        return HttpServletResponse.SC_FORBIDDEN;
    }

    public int putResource(WebDAVRequest webDavReq, String destination) throws WebDAVException {
        try {
            Resource resource = getResource(webDavReq);
            if (resource == null) {
                return HttpServletResponse.SC_NOT_FOUND;
            }
            Object model = resource.getModel();
            if (model instanceof JournalStructure) {
                JournalStructure structure = (JournalStructure) model;
                HttpServletRequest req = webDavReq.getHttpServletRequest();
                String xsd = StringUtil.read(req.getInputStream());
                JournalStructureServiceUtil.updateStructure(structure.getGroupId(), structure.getStructureId(), structure.getName(), structure.getDescription(), xsd);
                return HttpServletResponse.SC_CREATED;
            } else if (model instanceof JournalTemplate) {
                JournalTemplate template = (JournalTemplate) model;
                HttpServletRequest req = webDavReq.getHttpServletRequest();
                String xsl = StringUtil.read(req.getInputStream());
                boolean formatXsl = true;
                File smallFile = null;
                JournalTemplateServiceUtil.updateTemplate(template.getGroupId(), template.getTemplateId(), template.getStructureId(), template.getName(), template.getDescription(), xsl, formatXsl, template.getLangType(), template.isSmallImage(), template.getSmallImageURL(), smallFile);
                return HttpServletResponse.SC_CREATED;
            } else {
                return HttpServletResponse.SC_FORBIDDEN;
            }
        } catch (PortalException pe) {
            return HttpServletResponse.SC_FORBIDDEN;
        } catch (Exception e) {
            throw new WebDAVException(e);
        }
    }

    protected List getFolders(WebDAVRequest webDavReq) throws Exception {
        List folders = new ArrayList();
        folders.add(toResource(webDavReq, _TYPE_STRUCTURES, true));
        folders.add(toResource(webDavReq, _TYPE_TEMPLATES, true));
        return folders;
    }

    protected List getStructures(WebDAVRequest webDavReq) throws Exception {
        List templates = new ArrayList();
        Iterator itr = JournalStructureLocalServiceUtil.getStructures(webDavReq.getGroupId()).iterator();
        while (itr.hasNext()) {
            JournalStructure structure = (JournalStructure) itr.next();
            Resource resource = toResource(webDavReq, structure, true);
            templates.add(resource);
        }
        return templates;
    }

    protected List getTemplates(WebDAVRequest webDavReq) throws Exception {
        List templates = new ArrayList();
        Iterator itr = JournalTemplateLocalServiceUtil.getTemplates(webDavReq.getGroupId()).iterator();
        while (itr.hasNext()) {
            JournalTemplate template = (JournalTemplate) itr.next();
            Resource resource = toResource(webDavReq, template, true);
            templates.add(resource);
        }
        return templates;
    }

    protected Resource toResource(WebDAVRequest webDavReq, String type, boolean appendPath) {
        String href = getRootPath() + webDavReq.getPath();
        if (appendPath) {
            href += StringPool.SLASH + type;
        }
        Resource resource = new BaseResourceImpl(href, type, true);
        resource.setModel(type);
        return resource;
    }

    protected Resource toResource(WebDAVRequest webDavReq, JournalStructure structure, boolean appendPath) {
        String href = getRootPath() + webDavReq.getPath();
        if (appendPath) {
            href += StringPool.SLASH + structure.getStructureId();
        }
        return new JournalStructureResourceImpl(structure, href);
    }

    protected Resource toResource(WebDAVRequest webDavReq, JournalTemplate template, boolean appendPath) {
        String href = getRootPath() + webDavReq.getPath();
        if (appendPath) {
            href += StringPool.SLASH + template.getTemplateId();
        }
        return new JournalTemplateResourceImpl(template, href);
    }

    private static final String _TYPE_STRUCTURES = "Structures";

    private static final String _TYPE_TEMPLATES = "Templates";
}
