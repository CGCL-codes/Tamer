package com.affectu.search.fetch.thread;

/**
 * Oct 16, 2008
 * 
 * @author daniel nathan
 */
public interface IFetchBase<T> {

    public void spidePage();

    public T spideSubPage(String url);

    public boolean saveObject(T t);

    public boolean checkLink(String link);
}
