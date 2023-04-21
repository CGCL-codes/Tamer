package com.blackberry.facebook.inf;

import java.util.Date;

public interface Review extends com.blackberry.facebook.inf.Object {

    public Date getCreatedTime();

    public String getCreatedTimeAsString();

    public Profile getFrom();

    public String getMessage();

    public int getRating();

    public Application getTo();
}
