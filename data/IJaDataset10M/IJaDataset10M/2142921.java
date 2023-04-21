package com.darrylsite.ihm.reader;

/**
 *
 * @author nabster
 */
public interface SentMessagerListener {

    public void reSend(long id);

    public void delete(long id);

    public void forward(long id);
}
