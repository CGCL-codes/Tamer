package com.ideo.sweetdevria.proxy;

import java.util.List;

public interface ValueListIterator {

    public int getSize() throws Exception;

    public Object getCurrentElement() throws Exception;

    public List getPreviousElements(int count) throws Exception;

    public List getNextElements(int count) throws Exception;

    public void resetIndex() throws Exception;
}
