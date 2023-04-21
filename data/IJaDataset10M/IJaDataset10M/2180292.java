package com.manning.gwtip.scriptaculous.client;

import com.google.gwt.user.client.ui.Image;

public class File extends Image {

    public String name;

    public File(String name, String url) {
        super(url);
        this.name = name;
    }
}
