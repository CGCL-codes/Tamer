package com.ynhenc.kml;

import java.awt.Color;
import java.io.Writer;
import com.ynhenc.comm.util.*;

public class KmlStyleUrl extends KmlContainer<KmlText> {

    public String getUrl() {
        return this.url;
    }

    @Override
    public String getTag() {
        return "styleUrl";
    }

    public KmlStyleUrl(int id) {
        super(null, null);
        this.url = "#" + id;
        this.addComponent(new KmlText(this.getUrl()));
    }

    private String url;
}
