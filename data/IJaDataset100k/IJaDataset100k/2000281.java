package org.vardb.util;

import java.util.*;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.vardb.CVardbException;
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

    public String format(String path, Object... args) {
        return format(path, CStringHelper.createMap(args));
    }

    /**
	 * creates formatted text by merging a hashtable with a FreeMarker template 
	 * 
	 * @param path the path to the template
	 * @param model the data to be included in the template
	 * @return the template merged with the data model
	 */
    public String format(String path, Map<String, Object> model) {
        try {
            Template template = this.configuration.getTemplate(path);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new CVardbException(e);
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
            throw new CVardbException(e);
        }
    }
}
