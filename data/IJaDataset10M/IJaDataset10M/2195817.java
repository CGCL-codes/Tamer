package net.sourceforge.blogentis.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sourceforge.blogentis.om.Blog;
import net.sourceforge.blogentis.om.Comment;
import net.sourceforge.blogentis.om.Post;
import net.sourceforge.blogentis.om.Section;
import net.sourceforge.blogentis.turbine.BlogParameterParser;
import net.sourceforge.blogentis.turbine.BlogRunDataService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.TorqueException;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.pull.ApplicationTool;
import org.apache.turbine.util.RunData;

public class AbsoluteLinkURL implements ApplicationTool {

    private static Log log = LogFactory.getLog(AbsoluteLinkURL.class);

    public static final NumberFormat YearFormat = new DecimalFormat("0000");

    public static final NumberFormat MonthFormat = new DecimalFormat("00");

    public static final NumberFormat DayFormat = new DecimalFormat("00");

    protected boolean absolute_url = false;

    protected boolean secure_link = false;

    protected String blogName = null;

    protected DateSpecification date = null;

    protected Map map = new HashMap();

    protected int postId = -1;

    protected String sectionName = null;

    protected String fragmentName = null;

    protected String template = null;

    protected String postFragment = null;

    protected String uriData = null;

    protected User user = null;

    public AbsoluteLinkURL addMap(Map m) throws ClassCastException {
        Set keys = m.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String name = (String) i.next();
            addQueryData(name, (String) m.get(name));
        }
        return this;
    }

    public AbsoluteLinkURL addQueryData(String name, String value) {
        if (value != null) {
            map.put(name, value);
        } else {
            map.remove(name);
        }
        return this;
    }

    public AbsoluteLinkURL clear() {
        map.clear();
        template = null;
        blogName = null;
        date = null;
        postId = -1;
        postFragment = null;
        sectionName = null;
        fragmentName = null;
        absolute_url = false;
        secure_link = false;
        uriData = null;
        user = null;
        return this;
    }

    private void fillQueryData(StringBuffer buffer) throws UnsupportedEncodingException {
        if (map.size() == 0) return;
        buffer.append("?");
        for (Iterator i = map.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = (Map.Entry) i.next();
            buffer.append(URLEncoder.encode((String) e.getKey(), "utf-8"));
            buffer.append("=");
            buffer.append(URLEncoder.encode((String) e.getValue(), "utf-8"));
            if (i.hasNext()) buffer.append("&amp;");
        }
    }

    public String getBlogName() {
        return blogName;
    }

    public AbsoluteLinkURL permaLink(Blog blog) {
        clear();
        setBlog(blog);
        setAbsolute(true);
        return this;
    }

    public AbsoluteLinkURL permaLink(Post post) {
        try {
            permaLink(post.getBlog());
            setPost(post);
        } catch (TorqueException e) {
        }
        return this;
    }

    public AbsoluteLinkURL permaLink(Comment c) {
        try {
            permaLink(c.getPost());
            fragmentName = "c" + c.getCommentId();
        } catch (TorqueException e) {
        }
        return this;
    }

    public AbsoluteLinkURL permaLink(Section section) {
        try {
            permaLink(section.getBlog());
            setSection(section);
        } catch (TorqueException e) {
        }
        return this;
    }

    public String getTemplate() {
        return template;
    }

    public AbsoluteLinkURL setAbsolute(boolean isAbsolute) {
        this.absolute_url = isAbsolute;
        return this;
    }

    public AbsoluteLinkURL setSecure(boolean isSecure) {
        this.secure_link = isSecure;
        return this;
    }

    public AbsoluteLinkURL setBlog(Blog blog) {
        if (blog != null) setBlogName(blog.getName()); else setBlogName(null);
        return this;
    }

    public void setBlogName(String string) {
        blogName = string;
    }

    public AbsoluteLinkURL setFile(String file) {
        clear();
        if (file.charAt(0) == '/') file = file.substring(1);
        setPage(file);
        return this;
    }

    public AbsoluteLinkURL setPost(int post) {
        postId = post;
        return this;
    }

    public AbsoluteLinkURL setPost(Post post) {
        if (post == null) {
            postId = -1;
            postFragment = null;
        } else {
            postId = post.getPostId();
            postFragment = post.getUriFragment();
            if (postFragment != null) {
                setDate(post.getPostedTime());
            }
            if (blogName == null) {
                try {
                    setBlog(post.getBlog());
                } catch (TorqueException e) {
                }
            }
        }
        return this;
    }

    public void setTemplate(String string) {
        template = string;
    }

    public AbsoluteLinkURL thisPage(RunData data) {
        BlogParameterParser pp = (BlogParameterParser) data.getParameters();
        if (pp.getBlog() != null) {
            setBlog(pp.getBlog());
            setPost(pp.getPost());
            setUser(pp.getURIUser());
            if (postId == -1) {
                if (pp.getDate() != null) {
                    setDate(pp.getDate());
                } else {
                    if (pp.getSection() != null) setSection(pp.getSection());
                }
            }
        }
        if (pp.getRequestedTemplate() != null) {
            template = pp.getRequestedTemplate();
            if ("Index".equals(template) || "Default".equals(template)) template = null;
        }
        if (!StringUtils.isEmpty(pp.getURIData())) {
            setUriData(pp.getURIData());
        }
        return this;
    }

    public AbsoluteLinkURL thisBlog(RunData data) {
        BlogParameterParser pp = (BlogParameterParser) data.getParameters();
        if (pp.getBlog() != null) setBlog(pp.getBlog());
        return this;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(100);
        String base = null;
        try {
            base = BlogManagerService.getInstance().getBlog(blogName).getBaseUrl();
        } catch (Exception ignored) {
        }
        if (base != null && base.length() > 0) {
            if (secure_link) buffer.append("https://"); else buffer.append("http://");
            buffer.append(base);
        } else {
            LinkFactoryService lfi = LinkFactoryService.getInstance();
            if (secure_link) buffer.append(lfi.getSecureBase()); else if (absolute_url) buffer.append(lfi.getAbsoluteBase()); else buffer.append(lfi.getRelativeBase());
            if (buffer.charAt(buffer.length() - 1) != '/') buffer.append('/');
            buffer.append(blogName);
        }
        if (postId != -1 && StringUtils.isEmpty(postFragment)) {
            buffer.append("/post/");
            buffer.append(postId);
        } else if (user != null) {
            buffer.append("/~");
            buffer.append(user.getName());
        } else {
            if (sectionName != null) {
                buffer.append("/");
                buffer.append(sectionName);
            }
            if (date != null) {
                buffer.append("/");
                buffer.append(YearFormat.format(date.getYear()));
                if (date.hasMonth()) {
                    buffer.append("/");
                    buffer.append(MonthFormat.format(date.getMonth()));
                    if (date.hasDay()) {
                        buffer.append("/");
                        buffer.append(MonthFormat.format(date.getDay()));
                    }
                }
                if (postFragment != null) {
                    buffer.append("/");
                    buffer.append(postFragment);
                }
            }
        }
        if (StringUtils.isNotEmpty(template)) {
            if (buffer.charAt(buffer.length() - 1) != '/') buffer.append("/");
            buffer.append(template);
        }
        if (!StringUtils.isEmpty(uriData)) {
            if (!(uriData.charAt(0) == '/')) buffer.append("/");
            buffer.append(uriData);
        }
        if (fragmentName != null) {
            buffer.append("#");
            buffer.append(fragmentName);
        }
        try {
            fillQueryData(buffer);
        } catch (UnsupportedEncodingException e) {
            log.warn(e);
        }
        String rv = buffer.toString();
        if (!absolute_url) rv = BlogRunDataService.getCurrentRunData().getResponse().encodeURL(rv);
        if (log.isDebugEnabled()) log.debug("link: " + rv);
        clear();
        return rv;
    }

    public AbsoluteLinkURL setAction(String action) {
        addQueryData("action", action);
        return this;
    }

    public void init(Object data) {
        refresh();
    }

    public void refresh() {
        clear();
    }

    public AbsoluteLinkURL setUser(User u) {
        this.user = u;
        return this;
    }

    public AbsoluteLinkURL setPage(String page) {
        if (page != null && page.length() > 0 && page.charAt(0) == '/') page = page.substring(1);
        setTemplate(page);
        return this;
    }

    public AbsoluteLinkURL setSection(String name) {
        sectionName = name;
        return this;
    }

    public AbsoluteLinkURL setSection(Section sec) {
        if (sec != null) {
            sectionName = sec.getName();
            try {
                setBlog(sec.getBlog());
            } catch (TorqueException e) {
            }
        } else sectionName = null;
        return this;
    }

    public AbsoluteLinkURL setDate(DateSpecification date) {
        this.date = date;
        return this;
    }

    public AbsoluteLinkURL setDate(Date date) {
        this.date = new DateSpecification(date);
        return this;
    }

    public AbsoluteLinkURL defaultTemplate() {
        this.template = null;
        return this;
    }

    public AbsoluteLinkURL setImage(String image) {
        setPage("images/" + image);
        return this;
    }

    public AbsoluteLinkURL setFragment(String string) {
        fragmentName = string;
        return this;
    }

    public String getUriData() {
        return uriData;
    }

    public AbsoluteLinkURL setUriData(String uriData) {
        this.uriData = uriData;
        return this;
    }
}
