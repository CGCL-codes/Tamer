package com.liferay.portlet.documentlibrary.action;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.DiffResult;
import com.liferay.portal.kernel.util.DiffUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsKeys;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.permission.DLFileEntryPermission;
import com.liferay.portlet.documentlibrary.util.DocumentConversionUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="CompareVersionsAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bruno Farache
 *
 */
public class CompareVersionsAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        try {
            compareVersions(renderRequest);
        } catch (Exception e) {
            if (e instanceof NoSuchFileEntryException || e instanceof PrincipalException) {
                SessionErrors.add(renderRequest, e.getClass().getName());
                setForward(renderRequest, "portlet.document_library.error");
            } else {
                throw e;
            }
        }
        return mapping.findForward("portlet.document_library.compare_versions");
    }

    protected void compareVersions(RenderRequest renderRequest) throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        long companyId = themeDisplay.getCompanyId();
        long userId = themeDisplay.getUserId();
        long fileEntryId = ParamUtil.getLong(renderRequest, "fileEntryId");
        long folderId = ParamUtil.getLong(renderRequest, "folderId");
        String name = ParamUtil.getString(renderRequest, "name");
        DLFileEntryPermission.check(themeDisplay.getPermissionChecker(), folderId, name, ActionKeys.VIEW);
        String extension = FileUtil.getExtension(name);
        String titleWithExtension = ParamUtil.getString(renderRequest, "titleWithExtension");
        double sourceVersion = ParamUtil.getDouble(renderRequest, "sourceVersion");
        double targetVersion = ParamUtil.getDouble(renderRequest, "targetVersion");
        InputStream sourceIs = DLFileEntryLocalServiceUtil.getFileAsStream(companyId, userId, folderId, name, sourceVersion);
        InputStream targetIs = DLFileEntryLocalServiceUtil.getFileAsStream(companyId, userId, folderId, name, targetVersion);
        if (extension.equals("htm") || extension.equals("html") || extension.equals("xml")) {
            String escapedSource = HtmlUtil.escape(StringUtil.read(sourceIs));
            String escapedTarget = HtmlUtil.escape(StringUtil.read(targetIs));
            sourceIs = new ByteArrayInputStream(escapedSource.getBytes(StringPool.UTF8));
            targetIs = new ByteArrayInputStream(escapedTarget.getBytes(StringPool.UTF8));
        }
        if (PrefsPropsUtil.getBoolean(PropsKeys.OPENOFFICE_SERVER_ENABLED, PropsValues.OPENOFFICE_SERVER_ENABLED) && isConvertBeforeCompare(extension)) {
            String sourceTempFileId = DocumentConversionUtil.getTempFileId(fileEntryId, sourceVersion);
            String targetTempFileId = DocumentConversionUtil.getTempFileId(fileEntryId, targetVersion);
            sourceIs = DocumentConversionUtil.convert(sourceTempFileId, sourceIs, extension, "txt");
            targetIs = DocumentConversionUtil.convert(targetTempFileId, targetIs, extension, "txt");
        }
        List<DiffResult>[] diffResults = DiffUtil.diff(new InputStreamReader(sourceIs), new InputStreamReader(targetIs));
        renderRequest.setAttribute(WebKeys.SOURCE_NAME, titleWithExtension + StringPool.SPACE + sourceVersion);
        renderRequest.setAttribute(WebKeys.TARGET_NAME, titleWithExtension + StringPool.SPACE + targetVersion);
        renderRequest.setAttribute(WebKeys.DIFF_RESULTS, diffResults);
    }

    protected boolean isConvertBeforeCompare(String extension) {
        if (extension.equals("txt")) {
            return false;
        }
        String[] conversions = DocumentConversionUtil.getConversions(extension);
        for (int i = 0; i < conversions.length; i++) {
            if (conversions[i].equals("txt")) {
                return true;
            }
        }
        return false;
    }
}
