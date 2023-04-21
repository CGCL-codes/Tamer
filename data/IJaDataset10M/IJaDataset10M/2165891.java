package org.seqtagutils.util.services;

import java.util.Enumeration;
import java.util.Map;
import org.seqtagutils.util.CException;
import org.seqtagutils.util.CFileHelper;
import org.seqtagutils.util.CStringHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;

public class CFreemarkerServiceImpl implements IFreemarkerService {

    protected Configuration configuration;

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
	 * creates formatted text by merging a hashtable with a FreeMarker template 
	 * 
	 * @param path the path to the template
	 * @param model the data to be included in the template
	 * @return the template merged with the data model
	 */
    public String format(String path, Object... args) {
        try {
            Template template = this.configuration.getTemplate(path);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, CStringHelper.createMap(args));
        } catch (Exception e) {
            throw new CException(e);
        }
    }

    public String formatStringTemplate(String str, Object... args) {
        try {
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            String name = "stringtemplate";
            stringLoader.putTemplate(name, str);
            Configuration cfg = createConfiguration();
            cfg.setTemplateLoader(stringLoader);
            Template template = cfg.getTemplate(name);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, CStringHelper.createMap(args));
        } catch (Exception e) {
            throw new CException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void addEnum(Map<String, Object> model, String name, Enumeration enm) {
        try {
            BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
            TemplateHashModel enumModels = wrapper.getEnumModels();
            TemplateHashModel enumModel = (TemplateHashModel) enumModels.get(enm.getClass().getName());
            model.put(name, enumModel);
        } catch (Exception e) {
            throw new CException(e);
        }
    }

    private Configuration createConfiguration() {
        Configuration cfg = new Configuration();
        cfg.setDefaultEncoding(CFileHelper.ENCODING.toString());
        return cfg;
    }
}
