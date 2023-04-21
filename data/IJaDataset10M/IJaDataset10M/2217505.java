package com.producteev4j.model.response;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrey
 * Date: 22/05/11
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public interface Note {

    long getIdNote();

    long getIdTaskExt();

    long getIdCreator();

    String getMessage();

    String getFileUrl();

    String getFileName();

    String getTimeCreate();

    String getTimeLastChange();

    int getDeleted();
}
