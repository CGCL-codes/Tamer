package com.mkk.kenji1016.service.impl;

import com.mkk.kenji1016.mobile.dto.Paging;
import com.mkk.kenji1016.mobile.dto.PagingTemplateDetails;
import com.mkk.kenji1016.service.FreemarkerService;
import com.mkk.kenji1016.util.LogHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mkk
 * Date: 11-8-27
 * Time: 下午2:52
 * <p/>
 * Free marker impl
 */
public class FreemarkerServiceImpl implements FreemarkerService, InitializingBean {

    private static final LogHelper logger = LogHelper.create(FreemarkerService.class);

    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * Get freemarker template content as text.
     *
     * @param model        Data model
     * @param templateName Template file name,include path
     * @return content ,return null is throws exception
     */
    public String getTemplateContent(Map<String, Object> model, String templateName) {
        try {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            logger.debug("Freemarker IO error", e);
        } catch (TemplateException e) {
            logger.debug("Freemarker template error", e);
        }
        return null;
    }

    /**
     * Return mobile paging form content by template {@link #MOBILE_PAGING_TEMPLATE}.
     *
     * @param paging A Paging instance
     */
    public void mobilePagingContent(Paging paging) {
        PagingTemplateDetails pagingTemplateDetails = new PagingTemplateDetails(paging);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("pagingTemplate", pagingTemplateDetails);
        String templateContent = getTemplateContent(model, MOBILE_PAGING_TEMPLATE);
        paging.setPagingFormHtmlContent(templateContent);
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(freeMarkerConfigurer);
    }

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }
}
