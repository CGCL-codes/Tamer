package net.sourceforge.blogentis.utils.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sourceforge.blogentis.om.AbstractConfigurablePeer;
import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.om.StoredBlog;
import net.sourceforge.blogentis.utils.BlogConstants;
import net.sourceforge.blogentis.utils.MappedConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.turbine.Turbine;
import org.apache.turbine.om.security.Group;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.TurbineException;

/**
 * @author abas
 */
public class BlogImpl implements Blog {

    private static final Log log = LogFactory.getLog(BlogImpl.class);

    protected StoredBlog blog = null;

    protected MappedConfiguration configuration = null;

    protected Group turbineGroup = null;

    protected Map tempMap = new HashMap();

    private Locale defLocale = null;

    private String defLocaleName = null;

    public BlogImpl(StoredBlog sb) {
        blog = sb;
    }

    public synchronized void setBlog(StoredBlog blog) {
        this.blog = blog;
        turbineGroup = null;
        configuration = null;
        tempMap = new HashMap();
        defLocale = null;
        defLocaleName = null;
    }

    public int getBlogId() {
        return blog.getBlogId();
    }

    public AbstractConfigurablePeer getConfigurablePeer() {
        return blog.getConfigurablePeer();
    }

    public String getDescription() {
        return blog.getDescription();
    }

    public String getName() {
        return blog.getName();
    }

    public List getSections() throws TorqueException {
        return blog.getSections();
    }

    public List getSections(Criteria criteria) throws TorqueException {
        return blog.getSections(criteria);
    }

    public String getTitle() {
        return blog.getTitle();
    }

    public synchronized MappedConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new MappedConfiguration(blog);
        }
        return configuration;
    }

    public boolean isRootBlog() {
        return getBlogId() == Turbine.getConfiguration().getInt(BlogConstants.ROOT_BLOG_PROPERTY, -1);
    }

    public Group getGroup() {
        if (turbineGroup == null) try {
            turbineGroup = TurbineSecurity.getGroupByName(getName());
        } catch (TurbineException e) {
            log.error("Could not determing Security Group for blog " + blog.getName(), e);
        }
        return turbineGroup;
    }

    public synchronized void setTemp(String name, Object object) {
        if (object != null) tempMap.put(name, object); else tempMap.remove(name);
    }

    public synchronized Object getTemp(String name) {
        return tempMap.get(name);
    }

    public String getBaseUrl() {
        return blog.getBaseUrl();
    }

    public boolean isHidden() {
        return blog.isHidden();
    }

    public synchronized Locale getDefaultLocale() {
        String locale = getConfiguration().getString("blog.locale", "en_US");
        if (defLocale == null || defLocaleName != locale) {
            String[] parts = StringUtils.split(locale, '_');
            defLocale = new Locale(parts.length > 0 ? parts[0] : "", parts.length > 1 ? parts[1] : "", parts.length > 2 ? parts[2] : "");
            defLocaleName = locale;
        }
        return defLocale;
    }
}
