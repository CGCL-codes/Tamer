package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;

public interface SentenceEnum {

    public void load() throws BasicException;

    public void load(Object params) throws BasicException;

    public Object getCurrent() throws BasicException;

    public boolean next() throws BasicException;
}
