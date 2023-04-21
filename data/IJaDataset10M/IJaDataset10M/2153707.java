package net.sf.jimex.jira.plugin.projectpanel.impl;

import com.atlassian.jira.plugin.projectpanel.impl.GenericProjectTabPanel;
import com.atlassian.jira.util.map.EasyMap;
import com.atlassian.jira.web.action.browser.Browser;
import com.atlassian.jira.web.bean.FieldVisibilityBean;
import java.util.Map;
import java.util.List;
import org.ofbiz.core.entity.GenericValue;
import org.apache.log4j.Logger;
import net.sf.jimex.jira.JimexImportUtils;

/**
 * andrew 19.03.2006 1:23:34
 */
public class ImportProjectTabPanel extends GenericProjectTabPanel {

    private static Logger logger = Logger.getLogger(ImportProjectTabPanel.class);

    public String getHtml(Browser browser) {
        Map startingParams = EasyMap.build("action", browser);
        startingParams.put("i18n", this.descriptor.getI18nBean());
        startingParams.put("fieldVisibility", new FieldVisibilityBean());
        startingParams.put("portlet", this);
        try {
        } catch (Exception e) {
            logger.error(e, e);
        }
        return descriptor.getHtml("view", startingParams);
    }
}
