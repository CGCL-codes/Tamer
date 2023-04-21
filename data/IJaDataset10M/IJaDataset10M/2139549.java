package com.nodeshop.action.admin;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import com.nodeshop.bean.MailConfig;
import com.nodeshop.util.TemplateConfigUtil;
import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import freemarker.template.TemplateException;

/**
 * 后台Action类 - 邮件模板
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop3AE912EF052249487D08E69CADDD3FCD
 
 */
@ParentPackage("admin")
public class TemplateMailAction extends BaseAdminAction {

    private static final long serialVersionUID = -3965561383196862741L;

    private MailConfig mailConfig;

    private String templateFileContent;

    @Resource
    private FreemarkerManager freemarkerManager;

    public String list() {
        return LIST;
    }

    public String edit() {
        mailConfig = TemplateConfigUtil.getMailConfig(mailConfig.getName());
        templateFileContent = TemplateConfigUtil.readTemplateFileContent(mailConfig);
        return INPUT;
    }

    @Validations(requiredStrings = { @RequiredStringValidator(fieldName = "templateFileContent", message = "模板内容不允许为空!") })
    @InputConfig(resultName = "error")
    public String update() {
        mailConfig = TemplateConfigUtil.getMailConfig(mailConfig.getName());
        TemplateConfigUtil.writeTemplateFileContent(mailConfig, templateFileContent);
        try {
            ServletContext servletContext = ServletActionContext.getServletContext();
            freemarkerManager.getConfiguration(servletContext).clearTemplateCache();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        redirectionUrl = "template_dynamic!list.action";
        return SUCCESS;
    }

    public List<MailConfig> getMailConfigList() {
        return TemplateConfigUtil.getMailConfigList();
    }

    public MailConfig getMailConfig() {
        return mailConfig;
    }

    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public String getTemplateFileContent() {
        return templateFileContent;
    }

    public void setTemplateFileContent(String templateFileContent) {
        this.templateFileContent = templateFileContent;
    }
}
