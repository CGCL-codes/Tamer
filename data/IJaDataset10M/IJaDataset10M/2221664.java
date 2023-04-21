package com.aimluck.eip.modules.screens;

import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import com.aimluck.eip.blog.BlogEntryFormData;
import com.aimluck.eip.blog.util.BlogUtils;
import com.aimluck.eip.util.ALEipUtils;

/**
 * ブログエントリーを処理するクラスです。 <br />
 * 
 */
public class BlogEntryFormScreen extends ALVelocityScreen {

    /** logger */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(BlogEntryFormScreen.class.getName());

    /**
   * 
   * @param rundata
   * @param context
   * @throws Exception
   */
    @Override
    protected void doOutput(RunData rundata, Context context) throws Exception {
        try {
            doBlogEntry_form(rundata, context);
        } catch (Exception ex) {
            logger.error("[BlogEntryFormScreen] Exception.", ex);
            ALEipUtils.redirectDBError(rundata);
        }
    }

    protected void doBlogEntry_form(RunData rundata, Context context) {
        BlogEntryFormData formData = new BlogEntryFormData();
        formData.initField();
        formData.loadThemaList(rundata, context);
        formData.doViewForm(this, rundata, context);
        if (formData.getThemaId().getValue() == 0) {
            formData.setThemaId(1);
        }
        String layout_template = "portlets/html/ja/ajax-blog-entry-form.vm";
        setTemplate(rundata, context, layout_template);
    }

    /**
   * @return
   */
    @Override
    protected String getPortletName() {
        return BlogUtils.BLOG_PORTLET_NAME;
    }
}
