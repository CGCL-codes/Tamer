package com.lyrisoft.chat.client.gui;

public interface IQuery {

    public void setDefaultSelection(String s);

    public String getSelection();

    public String getText();

    public boolean getCanceled();

    public void show();

    public void setCallbackParams(int id, IQueryCallback cb);

    public void dispose();
}
