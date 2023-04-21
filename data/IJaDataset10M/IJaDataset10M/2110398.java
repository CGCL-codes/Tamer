package org.eclipse.ui;

import nill.NullInterface;

public interface IMemento extends NullInterface {

    public String getString(String key);

    public Integer getInteger(String key);

    public void putString(String key, String val);

    public void putInteger(String key, int val);

    public IMemento createChild(String xmlName);

    public IMemento[] getChildren(String xmlName);
}
