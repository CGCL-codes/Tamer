package com.mr.qa.action;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.mr.qa.GlobalConfigs;

public class SetSystemPropertyAction extends BaseAction {

    @Override
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filePath = this.getClass().getResource("/").getPath() + GlobalConfigs.SYSTEM_PROPERTY_FILE;
        filePath = URLDecoder.decode(filePath, "UTF-8");
        Properties props = new Properties();
        InputStream is = null;
        is = new FileInputStream(filePath);
        props.load(is);
        is.close();
        String action = request.getParameter("action");
        if ("setsecurity".equals(action)) {
            String adminPassword = request.getParameter("adminPassword");
            props.setProperty("adminPassword", adminPassword);
            GlobalConfigs.ADMIN_PASSWORD = adminPassword;
        } else if ("moneyspenddef".equals(action)) {
            String sharedFileMoney = request.getParameter("sharedFileMoney");
            props.setProperty("sharedFileMoney", sharedFileMoney);
            GlobalConfigs.SHARED_FILE_MONEY = Integer.parseInt(sharedFileMoney);
            String intervalRegisterDownload = request.getParameter("intervalRegisterDownload");
            props.setProperty("intervalRegisterDownload", intervalRegisterDownload);
            GlobalConfigs.INTERVAL_REGISTER_DOWNLOAD = Integer.parseInt(intervalRegisterDownload);
            String moneyAwardLoginId = request.getParameter("moneyAwardLoginId");
            props.setProperty("moneyAwardLoginId", moneyAwardLoginId);
            GlobalConfigs.MONEY_AWARD_LOGIN_ID = moneyAwardLoginId;
            String weeklyAutoAwardCriteria = request.getParameter("weeklyAutoAwardCriteria");
            props.setProperty("weeklyAutoAwardCriteria", weeklyAutoAwardCriteria);
            GlobalConfigs.WEEKLY_AUTO_AWARD_CRITERIA = Integer.parseInt(weeklyAutoAwardCriteria);
            String shopArticleListSize = request.getParameter("shopArticleListSize");
            props.setProperty("shopArticleListSize", shopArticleListSize);
            GlobalConfigs.SHOP_ARTICLE_LIST_SIZE = Integer.parseInt(shopArticleListSize);
            String shopArticleListSizeNav = request.getParameter("shopArticleListSizeNav");
            props.setProperty("shopArticleListSizeNav", shopArticleListSizeNav);
            GlobalConfigs.SHOP_ARTICLE_LIST_SIZE_NAV = Integer.parseInt(shopArticleListSizeNav);
            String mailAddress = request.getParameter("mail_address");
            props.setProperty("mailAddress", mailAddress);
            GlobalConfigs.mail_address = mailAddress;
        }
        OutputStream ops = new FileOutputStream(filePath);
        props.store(ops, "System Parameter");
        ops.close();
        return mapping.findForward(action);
    }
}
