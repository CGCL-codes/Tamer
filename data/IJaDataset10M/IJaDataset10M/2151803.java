package com.jhyle.sce;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import com.jhyle.sce.dao.Hibernate;

public class Navigation extends Controller {

    private static final String NAVIGATION_TEMPLATE = TEMPLATE_PATH + "nav.tpl";

    public Navigation(Config config, Hibernate orm) {
        super(config, orm);
    }

    @Override
    public void view(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = (String) request.getSession().getAttribute("user");
        if (StringUtils.isEmpty(username)) return;
        VelocityContext ctx = getContext(request);
        ctx.put("user", userDao.load(username));
        ctx.put("title", config.getProperty(Config.TITLE));
        Template tpl = Velocity.getTemplate(NAVIGATION_TEMPLATE);
        tpl.merge(ctx, response.getWriter());
    }
}
