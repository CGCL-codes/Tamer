package com.sin.client.ui.nord2;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Composite;

public interface ContactWidget extends IsWidget {

    public void setDinamicContents(String imagestr, String phone, String name, String age, String city, String desc);

    public void setId(int id);

    public int getId();
}
