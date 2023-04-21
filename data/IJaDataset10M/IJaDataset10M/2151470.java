package net.sf.poormans.tool.ckeditor;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import net.fckeditor.FCKeditor;
import net.sf.poormans.model.domain.PojoPathInfo;
import net.sf.poormans.model.domain.pojo.Site;
import net.sf.poormans.tool.PathTool;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Construct the FCKeditor.<br />
 * Following files are loading automatically:
 * <ul>
 * <li>configurationpath/fckconfig.js</li>
 * <li>configurationpath/fckstyles.xml (optional)</li>
 * <li>format.css (= setting the EditorAreaCSS - css using inside the editor)</li>
 * </ul>
 * 
 * @version $Id: CKeditorWrapper.java 1572 2009-03-22 13:57:25Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class CKeditorWrapper {

    private static Logger logger = Logger.getLogger(CKeditorWrapper.class);

    private Site site;

    private HttpServletRequest httpServletRequest;

    public CKeditorWrapper(final Site site, final HttpServletRequest httpServletRequest) {
        if (site == null || httpServletRequest == null) throw new IllegalArgumentException("One or more arguments are null!");
        this.site = site;
        this.httpServletRequest = httpServletRequest;
    }

    public String get(final String fieldName, final String value) {
        return get(fieldName, value, null, null, null);
    }

    public String get(final String fieldName, final String value, final String width, final String height) {
        return get(fieldName, value, width, height, null);
    }

    public String get(final String fieldName, final String value, final String width, final String height, final String toolbarSetName) {
        logger.debug("Try to create editor for field: ".concat(fieldName));
        FCKeditor editor = new FCKeditor(httpServletRequest, fieldName);
        if (StringUtils.isNotBlank(width)) editor.setWidth(width);
        if (StringUtils.isNotBlank(height)) editor.setHeight(height);
        if (StringUtils.isNotBlank(toolbarSetName)) editor.setToolbarSet(toolbarSetName);
        File templatesXml = new File(PojoPathInfo.getSiteConfigurationPath(site), "fcktemplates.xml");
        if (templatesXml.exists()) editor.getConfig().put("TemplatesXmlPath", PathTool.getURLFromFile(templatesXml.getPath()));
        File stylesXml = new File(PojoPathInfo.getSiteConfigurationPath(site), "fckstyles.xml");
        if (stylesXml.exists()) editor.getConfig().put("StylesXmlPath", PathTool.getURLFromFile(stylesXml.getPath()));
        editor.getConfig().put("CustomConfigurationsPath", PathTool.getURLFromFile(PojoPathInfo.getSiteConfigurationPath(site) + "fckconfig.js"));
        editor.getConfig().put("EditorAreaCSS", PathTool.getURLFromFile(PojoPathInfo.getSitePath(site) + "format.css"));
        editor.setValue(value);
        return editor.createHtml();
    }
}
