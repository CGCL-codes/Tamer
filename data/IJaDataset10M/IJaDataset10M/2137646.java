package com.sitescape.team.module.folder.impl;

public interface AbstractFolderModuleMBean {

    public void clearStatistics();

    public int getAddEntryCount();

    public int getModifyEntryCount();

    public int getDeleteEntryCount();

    public int getAddReplyCount();
}
