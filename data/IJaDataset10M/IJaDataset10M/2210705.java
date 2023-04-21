package com.simplenix.nicasio.tags;

import com.simplenix.nicasio.def.Feature;
import com.simplenix.nicasio.def.Module;
import com.simplenix.nicasio.misc.AppConfiguration;
import com.simplenix.nicasio.misc.FileUtil;
import com.simplenix.nicasio.misc.StrUtil;
import com.simplenix.nicasio.sys.SystemDef;
import com.simplenix.nicasio.textparser.TextParser;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author fronald
 */
public class WriteFile extends SimpleTagSupport {

    private java.lang.String property;

    private java.lang.String fileName;

    private java.lang.String featureName;

    private java.lang.String moduleName;

    @Override
    public void doTag() throws JspException {
        JspWriter out = getJspContext().getOut();
        try {
            java.lang.String html = "";
            if (!StrUtil.nvl(this.property).equals("")) {
                java.lang.String url = SystemDef.getInstance().getAppPath() + AppConfiguration.getInstance().getKey(this.property);
                File f = new File(url);
                if (!f.exists()) {
                    throw new Exception("File not found");
                } else {
                    List<java.lang.String> lines = FileUtil.readTextLines(url, false);
                    for (java.lang.String line : lines) {
                        html += line + "\n";
                    }
                }
            } else if (!StrUtil.nvl(this.fileName).equals("")) {
                java.lang.String url = SystemDef.getInstance().getAppPath() + StrUtil.nvl(this.fileName);
                File f = new File(url);
                if (!f.exists()) {
                    throw new Exception("File not found");
                } else {
                    List<java.lang.String> lines = FileUtil.readTextLines(url, false);
                    for (java.lang.String line : lines) {
                        html += line + "\n";
                    }
                }
            }
            html = TextParser.parseString(html);
            Module module = SystemDef.getInstance().getModules().getModuleByName(this.getModuleName());
            Feature feature = SystemDef.getInstance().getModules().getFeaturesByModule(module).getFeatureByName(this.featureName);
            html = TextParser.parseKeyTags(html, feature, (HttpServletRequest) ((PageContext) this.getJspContext()).getRequest());
            out.println(html);
        } catch (Exception ex) {
            throw new JspException("Error in WriteFile tag", ex);
        }
    }

    public void setProperty(java.lang.String property) {
        this.property = property;
    }

    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }

    /**
	 * @return the featureName
	 */
    public java.lang.String getFeatureName() {
        return featureName;
    }

    /**
	 * @param featureName the featureName to set
	 */
    public void setFeatureName(java.lang.String featureName) {
        this.featureName = featureName;
    }

    /**
	 * @return the moduleName
	 */
    public java.lang.String getModuleName() {
        return moduleName;
    }

    /**
	 * @param moduleName the moduleName to set
	 */
    public void setModuleName(java.lang.String moduleName) {
        this.moduleName = moduleName;
    }
}
