package jtiger.core.web.ui.freemarker;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import jtiger.core.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 执行freemarker模板操作的辅助类，子类需要注入这个类才能使用。简化了freemarker
 * 模板操作的异常处理并且对于DataModel为Collection,Array,Number,String,Date的情况作了处理。
 * @author catstiger@gmail.com
 */
@Component
public class FreeMarkerService {

    @Autowired
    protected Configuration configuration;

    /**
   * 加载指定模板，并结合给出的数据将模板解析为String.
   * @param templateName 模板名称，例如 myTemplate.ftl。建议是唯一的，否则没准儿加载哪个。
   * @param model 用于填充模板的数据。
   * <ul>
   * <li>如果数据类型是Map或者JavaBean,则直接写入模板，模板中可以直接使用他们的属性或者Entry。</li>
   * <li>如果数据类型是List或者Array，则以"list"作为Key写入模板.</li>
   * <li>如果数据类型是Number或者String，则以"data"作为Key写入模板.</li>
   * <li>如果数据类型是Date，则以"dt"作为Key写入模板.</li>
   * </ul>
   * @throws ApplicationException if any exception occurs.
   * @return the result as String
   */
    public String processTemplate(String templateName, Object model) {
        try {
            Template template = configuration.getTemplate(templateName);
            if (model != null) {
                Map<String, Object> dataModel = new HashMap<String, Object>();
                if (model instanceof Collection<?> || model.getClass().isArray()) {
                    dataModel.put("list", model);
                    model = dataModel;
                } else if (model instanceof Number || model instanceof String) {
                    dataModel.put("data", model);
                    model = dataModel;
                } else if (model instanceof Date) {
                    dataModel.put("dt", model);
                    model = dataModel;
                }
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        } catch (TemplateException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
    }
}
