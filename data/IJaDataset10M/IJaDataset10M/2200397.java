package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.blog.BlogEntrySelectData;
import com.aimluck.eip.blog.util.BlogUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ブログエントリーの一覧を処理するクラスです。 <br />
 * 
 */
public class BlogScreen extends ALVelocityScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(BlogScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            BlogEntrySelectData listData = new BlogEntrySelectData();
            listData.initField();
            listData.loadThemaList(rundata, context);
            listData.setRowsNum(Integer.parseInt(ALEipUtils.getPortlet(rundata, context).getPortletConfig().getInitParameter("p1a-rows")));
            listData.setStrLength(100);
            listData.doViewList(this, rundata, context);
            String layout_template = "portlets/html/ja/ajax-blog.vm";
            setTemplate(rundata, context, layout_template);
        } catch (Exception ex) {
            logger.error("[BlogScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }

    /**
   * @return
   */
    @Override
    protected String getPortletName() {
        return BlogUtils.BLOG_PORTLET_NAME;
    }
}
