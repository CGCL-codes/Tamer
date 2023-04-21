package com.onesadjam.yagrac.xml;

import android.sax.Element;
import android.sax.EndTextElementListener;

public class Actor {

    private int _Id;

    private String _Name;

    private String _ImageUrl;

    private String _Link;

    public void clear() {
        this.set_Id(0);
        this.set_Name("");
        this.set_ImageUrl("");
        this.set_Link("");
    }

    public Actor copy() {
        Actor actorCopy = new Actor();
        actorCopy.set_Id(this.get_Id());
        actorCopy.set_Name(this.get_Name());
        actorCopy.set_ImageUrl(this.get_ImageUrl());
        actorCopy.set_Link(this.get_Link());
        return actorCopy;
    }

    public static Actor appendSingletonListener(Element parentElement, int depth) {
        final Actor actor = new Actor();
        final Element actorElement = parentElement.getChild("actor");
        actorElement.getChild("id").setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                if (body != null && body != "") {
                    actor.set_Id(Integer.parseInt(body));
                }
            }
        });
        actorElement.getChild("name").setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                actor.set_Name(body);
            }
        });
        actorElement.getChild("image_url").setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                actor.set_ImageUrl(body);
            }
        });
        actorElement.getChild("link").setEndTextElementListener(new EndTextElementListener() {

            @Override
            public void end(String body) {
                actor.set_Link(body);
            }
        });
        return actor;
    }

    public int get_Id() {
        return _Id;
    }

    public void set_Id(int _Id) {
        this._Id = _Id;
    }

    public String get_Name() {
        return _Name;
    }

    public void set_Name(String _Name) {
        this._Name = _Name;
    }

    public String get_ImageUrl() {
        return _ImageUrl;
    }

    public void set_ImageUrl(String _ImageUrl) {
        this._ImageUrl = _ImageUrl;
    }

    public String get_Link() {
        return _Link;
    }

    public void set_Link(String _Link) {
        this._Link = _Link;
    }
}
