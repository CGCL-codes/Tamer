package org.cmsuite2.web.action.impl;

import it.ec.commons.struts2.action.web.WebActionSupport;
import it.ec.commons.web.UploadBean;
import it.ec.commons.web.WhereBuilder.Operator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.cmsuite2.business.form.NewsLetterForm;
import org.cmsuite2.business.handler.NewsLetterHandler;
import org.cmsuite2.business.search.NewsLetterSearch;
import org.cmsuite2.enumeration.ActivityType;
import org.cmsuite2.model.activity.Activity;
import org.cmsuite2.model.mailinglist.MailingList;
import org.cmsuite2.model.newsletter.NewsLetter;
import org.cmsuite2.model.pageitem.PageItem;
import org.cmsuite2.util.bean.ErrorBean;
import org.springframework.context.ApplicationContextAware;

public class NewsLetterAction extends WebActionSupport implements INewsLetterAction, ApplicationContextAware {

    private static final long serialVersionUID = 3989526300723557410L;

    protected static Logger logger = Logger.getLogger(NewsLetterAction.class);

    private NewsLetterHandler newsLetterHandler;

    private NewsLetter formNewsLetter;

    private List<NewsLetter> newsLetterList;

    private List<Integer> custList;

    private long mailingListId;

    private UploadBean header;

    private UploadBean footer;

    private NewsLetterForm newsLetterForm;

    private NewsLetterSearch newsLetterSearch;

    private String sendDate;

    private String sendHour;

    private String sendMinute;

    private String sendSecond;

    public String action() {
        if (logger.isInfoEnabled()) logger.info("action()");
        if (logger.isDebugEnabled()) {
            logger.debug("newButton: " + newButton);
            logger.debug("deleteButton: " + deleteButton);
        }
        if (newButton != null) {
            createForm();
            return "newsletter.createForm";
        } else if (deleteButton != null) {
            if (intList != null && intList.size() > 0) for (Integer i : intList) newsLetterHandler.delete(i); else addFieldError("intList", getText("org.cmsuite2.form.selection.empty"));
            index();
            return "newsletter.index";
        }
        return "newsletter.index";
    }

    public String index() {
        if (logger.isInfoEnabled()) logger.info("index()");
        String areaName = actionName.substring(0, actionName.indexOf("/"));
        PageItem dbPageItem = pageItemDao.find("from PageItem where preference = ? and area = ?", account.getPreference(), areaName);
        if (dbPageItem != null) dbPageItem.setNumber(formPageItem.getNumber()); else {
            formPageItem.setArea(areaName);
            pageItemDao.persist(formPageItem);
        }
        if (newsLetterSearch != null) {
            if (StringUtils.isNotEmpty(newsLetterSearch.getSubject())) getWhereBuilder().add(newsLetterSearch.getSubject(), "_obj.subject like '%?%'", Operator.AND);
            if (StringUtils.isNotEmpty(newsLetterSearch.getText())) getWhereBuilder().add(newsLetterSearch.getText(), "_obj.text like '%?%'", Operator.AND);
        }
        setPagList(paginateUtil.getPaginate(NewsLetter.class, getPagList(), getWhereBuilder().toString()));
        newsLetterForm = newsLetterHandler.initForm(idToLoad);
        return SUCCESS;
    }

    public String createForm() {
        if (logger.isInfoEnabled()) logger.info("createForm()");
        newsLetterForm = newsLetterHandler.initForm(idToLoad);
        return SUCCESS;
    }

    public String updateForm() {
        if (logger.isInfoEnabled()) logger.info("updateForm()");
        newsLetterForm = newsLetterHandler.initForm(idToLoad);
        formNewsLetter = newsLetterHandler.read(idToLoad);
        MailingList ml = formNewsLetter.getMailingList();
        if (ml != null) mailingListId = ml.getId();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar sendCal = Calendar.getInstance();
        sendCal.setTime(formNewsLetter.getSendDate());
        sendHour = String.valueOf(sendCal.get(Calendar.HOUR_OF_DAY));
        sendMinute = String.valueOf(sendCal.get(Calendar.MINUTE));
        sendSecond = String.valueOf(sendCal.get(Calendar.SECOND));
        sendDate = sdf.format(formNewsLetter.getSendDate());
        return SUCCESS;
    }

    public String create() throws Exception {
        if (logger.isInfoEnabled()) logger.info("create()");
        if (header == null) header = new UploadBean();
        header.setFieldName("header.myFile");
        if (footer == null) footer = new UploadBean();
        footer.setFieldName("footer.myFile");
        if (!StringUtils.isBlank(sendDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar sendCal = Calendar.getInstance();
            sendCal.setTime(sdf.parse(sendDate));
            sendCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sendHour));
            sendCal.set(Calendar.MINUTE, Integer.parseInt(sendMinute));
            sendCal.set(Calendar.SECOND, Integer.parseInt(sendSecond));
            formNewsLetter.setSendDate(new Date(sendCal.getTimeInMillis()));
        }
        NewsLetter resultNewsLetter = newsLetterHandler.create(formNewsLetter, config, mailingListId, custList, header, footer);
        if (resultNewsLetter.hasErrors()) {
            logger.warn("SecCap " + resultNewsLetter + " has errors!");
            prepareApp("newsletter/createForm");
            initPaginator("newsletter/createForm");
            createForm();
            for (ErrorBean eb : resultNewsLetter.getErrors()) addFieldError(eb.getKey(), getText(eb.getValue(), eb.getParams()));
            return "newsletter.createForm";
        }
        activityDao.persist(new Activity(account.getUsername(), new Date(), ActivityType.CREATE_NewsLetter, "[" + formNewsLetter + "]"));
        return "newsletter.index";
    }

    public String update() throws Exception {
        if (logger.isInfoEnabled()) logger.info("update()");
        if (header == null) header = new UploadBean();
        header.setFieldName("header.myFile");
        if (footer == null) footer = new UploadBean();
        footer.setFieldName("footer.myFile");
        if (!StringUtils.isBlank(sendDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar sendCal = Calendar.getInstance();
            sendCal.setTime(sdf.parse(sendDate));
            sendCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sendHour));
            sendCal.set(Calendar.MINUTE, Integer.parseInt(sendMinute));
            sendCal.set(Calendar.SECOND, Integer.parseInt(sendSecond));
            formNewsLetter.setSendDate(new Date(sendCal.getTimeInMillis()));
        }
        NewsLetter resultNewsLetter = newsLetterHandler.update(idToLoad, formNewsLetter, config, mailingListId, custList, header, footer);
        if (resultNewsLetter.hasErrors()) {
            logger.warn("SecCap " + resultNewsLetter + " has errors!");
            prepareApp("newsletter/updateForm");
            initPaginator("newsletter/updateForm");
            updateForm();
            for (ErrorBean eb : resultNewsLetter.getErrors()) addFieldError(eb.getKey(), getText(eb.getValue(), eb.getParams()));
            return "newsletter.updateForm";
        }
        activityDao.persist(new Activity(account.getUsername(), new Date(), ActivityType.UPDATE_NewsLetter, "[" + formNewsLetter + "]"));
        return "newsletter.index";
    }

    public String view() {
        if (logger.isInfoEnabled()) logger.info("view()");
        newsLetterForm = newsLetterHandler.initForm(idToLoad);
        formNewsLetter = newsLetterHandler.read(idToLoad);
        activityDao.persist(new Activity(account.getUsername(), new Date(), ActivityType.READ_NewsLetter, "[" + idToLoad + "]"));
        return SUCCESS;
    }

    public String delete() {
        if (logger.isInfoEnabled()) logger.info("delete()");
        newsLetterHandler.delete(idToLoad);
        activityDao.persist(new Activity(account.getUsername(), new Date(), ActivityType.DELETE_NewsLetter, "[" + idToLoad + "]"));
        index();
        return "newsletter.index";
    }

    public NewsLetter getFormNewsLetter() {
        return formNewsLetter;
    }

    public void setFormNewsLetter(NewsLetter formNewsLetter) {
        this.formNewsLetter = formNewsLetter;
    }

    public List<NewsLetter> getNewsLetterList() {
        return newsLetterList;
    }

    public void setNewsLetterList(List<NewsLetter> newsLetterList) {
        this.newsLetterList = newsLetterList;
    }

    public NewsLetterHandler getNewsLetterHandler() {
        return newsLetterHandler;
    }

    public void setNewsLetterHandler(NewsLetterHandler newsLetterHandler) {
        this.newsLetterHandler = newsLetterHandler;
    }

    public void setNewsLetterSearch(NewsLetterSearch newsLetterSearch) {
        this.newsLetterSearch = newsLetterSearch;
    }

    public NewsLetterSearch getNewsLetterSearch() {
        return newsLetterSearch;
    }

    public UploadBean getHeader() {
        return header;
    }

    public void setHeader(UploadBean header) {
        this.header = header;
    }

    public UploadBean getFooter() {
        return footer;
    }

    public void setFooter(UploadBean footer) {
        this.footer = footer;
    }

    public void setNewsLetterForm(NewsLetterForm newsLetterForm) {
        this.newsLetterForm = newsLetterForm;
    }

    public NewsLetterForm getNewsLetterForm() {
        return newsLetterForm;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendDate() {
        return sendDate;
    }

    public String getSendHour() {
        return sendHour;
    }

    public void setSendHour(String sendHour) {
        this.sendHour = sendHour;
    }

    public String getSendMinute() {
        return sendMinute;
    }

    public List<Integer> getCustList() {
        return custList;
    }

    public void setCustList(List<Integer> custList) {
        this.custList = custList;
    }

    public void setSendMinute(String sendMinute) {
        this.sendMinute = sendMinute;
    }

    public String getSendSecond() {
        return sendSecond;
    }

    public void setSendSecond(String sendSecond) {
        this.sendSecond = sendSecond;
    }

    public long getMailingListId() {
        return mailingListId;
    }

    public void setMailingListId(long mailingListId) {
        this.mailingListId = mailingListId;
    }
}
