package com.blackberry.facebook.inf;

public interface Work extends com.blackberry.facebook.inf.Object {

    public Profile getEmployer();

    public Profile getLocation();

    public Profile getPosition();

    public String getStartDate();

    public String getEndDate();
}
