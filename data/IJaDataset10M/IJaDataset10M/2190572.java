package com.genia.toolbox.web.gwt.basics.client.service;

import java.util.List;
import com.genia.toolbox.web.gwt.basics.client.bean.GwtLocale;
import com.genia.toolbox.web.gwt.basics.client.i18n.GwtI18nMessage;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous interface for i18n handling.
 */
public interface I18nServiceAsync {

    /**
   * the async method associated to getMessage(List messages).
   * 
   * @param messages
   *          the messages to transalte
   * @param callback
   *          the callback
   */
    public void getMessage(List<GwtI18nMessage> messages, AsyncCallback<List<String>> callback);

    /**
   * the async method associated to getRegisteredLocale().
   * 
   * @param callback
   *          the callback
   */
    public void getRegisteredLocale(AsyncCallback<GwtLocale[]> callback);
}
