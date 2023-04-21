package org.or5e.core;

public interface ApplicationManager {

    public void initilizeApplication();

    public void addPlugin();

    public void removePlugin();

    public void startPlugin();

    public void stopPlugin();

    public void listPlugin();

    public void destroyApplication();
}
