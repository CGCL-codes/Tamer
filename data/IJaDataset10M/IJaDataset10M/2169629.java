package com.sin.shared.autobean;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sin.shared.domains.AllOnLineAutoBean;
import com.sin.shared.domains.AllSmsimagesAutoBean;
import com.sin.shared.domains.CliCookie;
import com.sin.shared.domains.MessageAutoBean;
import com.sin.shared.domains.OnLineAutoBean;
import com.sin.shared.domains.SmsimagesAutoBean;

public class AutoBeanHandlerImpl implements AutoBeanHandler {

    public AllOnLineAutoBean makeAllOnLineAutoBean(ABeanFactory factory) {
        AutoBean<AllOnLineAutoBean> allOnLineAutoBean = factory.allOnLineAutoBean();
        return allOnLineAutoBean.as();
    }

    public String serializeToJsonAllOnLineAutoBean(AllOnLineAutoBean allOnLineAutoBean) {
        AutoBean<AllOnLineAutoBean> bean = AutoBeanUtils.getAutoBean(allOnLineAutoBean);
        return AutoBeanCodex.encode(bean).getPayload();
    }

    public AllOnLineAutoBean deserializeAllOnLineAutoBeanFromJson(ABeanFactory factory, String json) {
        AutoBean<AllOnLineAutoBean> bean = AutoBeanCodex.decode(factory, AllOnLineAutoBean.class, json);
        return bean.as();
    }

    public OnLineAutoBean makeOnLineAutoBean(ABeanFactory factory) {
        AutoBean<OnLineAutoBean> onLineAutoBean = factory.onLineAutoBean();
        return onLineAutoBean.as();
    }

    public String serializeToJsonOnLineAutoBean(OnLineAutoBean onLineAutoBean) {
        AutoBean<OnLineAutoBean> bean = AutoBeanUtils.getAutoBean(onLineAutoBean);
        return AutoBeanCodex.encode(bean).getPayload();
    }

    public OnLineAutoBean deserializeOnLineAutoBeanFromJson(ABeanFactory factory, String json) {
        AutoBean<OnLineAutoBean> bean = AutoBeanCodex.decode(factory, OnLineAutoBean.class, json);
        return bean.as();
    }

    public CliCookie makeCliCookie(ABeanFactory factory) {
        AutoBean<CliCookie> cliCookie = factory.client();
        return cliCookie.as();
    }

    public String serializeToJsonCliCookie(CliCookie cliCookie) {
        AutoBean<CliCookie> bean = AutoBeanUtils.getAutoBean(cliCookie);
        return AutoBeanCodex.encode(bean).getPayload();
    }

    public CliCookie deserializeCliCookieFromJson(ABeanFactory factory, String json) {
        AutoBean<CliCookie> bean = AutoBeanCodex.decode(factory, CliCookie.class, json);
        return bean.as();
    }

    public SmsimagesAutoBean makeSmsimagesAutoBean(ABeanFactory factory) {
        AutoBean<SmsimagesAutoBean> smsimagesAutoBean = factory.smsimagesAutoBean();
        return smsimagesAutoBean.as();
    }

    public String serializeToJsonSmsimagesAutoBean(SmsimagesAutoBean smsimagesAutoBean) {
        AutoBean<SmsimagesAutoBean> bean = AutoBeanUtils.getAutoBean(smsimagesAutoBean);
        return AutoBeanCodex.encode(bean).getPayload();
    }

    public SmsimagesAutoBean deserializeSmsimagesAutoBean(ABeanFactory factory, String json) {
        AutoBean<SmsimagesAutoBean> bean = AutoBeanCodex.decode(factory, SmsimagesAutoBean.class, json);
        return bean.as();
    }

    public AllSmsimagesAutoBean makeAllSmsimagesAutoBean(ABeanFactory factory) {
        AutoBean<AllSmsimagesAutoBean> allSmsimagesAutoBean = factory.allSmsimagesAutoBean();
        return allSmsimagesAutoBean.as();
    }

    public String serializeToJsonAllSmsimagesAutoBean(AllSmsimagesAutoBean allSmsimagesAutoBean) {
        AutoBean<AllSmsimagesAutoBean> bean = AutoBeanUtils.getAutoBean(allSmsimagesAutoBean);
        return AutoBeanCodex.encode(bean).getPayload();
    }

    public AllSmsimagesAutoBean deserializeAllSmsimagesAutoBean(ABeanFactory factory, String json) {
        AutoBean<AllSmsimagesAutoBean> bean = AutoBeanCodex.decode(factory, AllSmsimagesAutoBean.class, json);
        return bean.as();
    }

    @Override
    public MessageAutoBean makeMessageAutoBean(ABeanFactory factory) {
        AutoBean<MessageAutoBean> messageAutoBean = factory.messageAutoBean();
        return messageAutoBean.as();
    }

    @Override
    public String serializeToJsonMessageAutoBean(MessageAutoBean messageAutoBean) {
        AutoBean<MessageAutoBean> bean = AutoBeanUtils.getAutoBean(messageAutoBean);
        return AutoBeanCodex.encode(bean).getPayload();
    }

    @Override
    public MessageAutoBean deserializeMessageAutoBean(ABeanFactory factory, String json) {
        AutoBean<MessageAutoBean> bean = AutoBeanCodex.decode(factory, MessageAutoBean.class, json);
        return bean.as();
    }
}
