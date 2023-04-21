package in.espirit.tracer.action;

import in.espirit.tracer.database.dao.ConfigDao;
import in.espirit.tracer.model.Config;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

@UrlBinding("/config/{id}")
public class NotUsedConfigActionBean extends BaseActionBean {

    private static final String URL = "/WEB-INF/jsp/config.jsp";

    private Config config;

    private String id;

    public Config getConfig() throws Exception {
        if (id != null) {
            return ConfigDao.getConfigById(id);
        }
        return config;
    }

    @ValidateNestedProperties({ @Validate(field = "key", required = true, on = "submit"), @Validate(field = "value", required = true, on = "submit") })
    public void setConfig(Config config) {
        this.config = config;
    }

    @DefaultHandler
    public Resolution defect() {
        getContext().setCurrentSection("config");
        return new ForwardResolution(URL);
    }

    public Resolution cancel() {
        getContext().setCurrentSection("configlist");
        return new ForwardResolution(ConfigMilestoneActionBean.class);
    }

    public Resolution submit() throws Exception {
        config = getConfig();
        if (config.getId() == null) {
            ConfigDao.registerConfig(config);
            getContext().getMessages().add(new SimpleMessage("New Config Added."));
        } else {
            ConfigDao.editConfig(config);
            getContext().getMessages().add(new SimpleMessage("Config Successfully Edited."));
        }
        getContext().setCurrentSection("configlist");
        return new RedirectResolution(ConfigMilestoneActionBean.class);
    }

    public Resolution delete() throws Exception {
        config = getConfig();
        ConfigDao.deleteConfig(config);
        getContext().getMessages().add(new SimpleMessage("Config Deleted."));
        getContext().setCurrentSection("configlist");
        return new RedirectResolution(ConfigMilestoneActionBean.class);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
