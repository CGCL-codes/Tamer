package net.sourceforge.blogentis.prefs.settings;

import net.sourceforge.blogentis.turbine.BlogRunData;
import net.sourceforge.blogentis.utils.HTMLUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

/**
 * @author abas
 */
public class StringInput extends BaseSetting {

    public StringInput(String name, String title) {
        super(name, title);
    }

    public StringInput(String name, String title, String description) {
        super(name, title, description);
    }

    public StringInput(String name, String title, String description, String formName) {
        super(name, title, description);
        setFormName(formName);
    }

    public String render(BlogRunData data, Configuration conf) {
        Object[] o = conf.getStringArray(getName());
        if (o == null || o.length == 0 || (o.length == 1 && StringUtils.isEmpty((String) o[0]))) {
            o = new String[] { defaultValue == null ? "" : defaultValue };
        }
        return "<input type='text' name='" + getFormName() + "' value='" + HTMLUtils.escapeHTML(StringUtils.join(o, ", ")) + "' />";
    }
}
