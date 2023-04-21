package com.genia.toolbox.web.gwt.basics.client.i18n;

/**
 * A callback for using with i18n service for multiple calls.
 */
public interface I18nArrayCallBack {

    /**
   * the callback called when the messages has been successfully translated.
   * 
   * @param messages
   *          the value of the messages.
   */
    public void getMessages(String[] messages);
}
