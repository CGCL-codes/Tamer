package com.nhncorp.cubridqa.configuration;

import com.nhncorp.cubridqa.utils.XstreamHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * A Java bean which is used to save information of build server.
 * @ClassName: BuildServer
 * @date 2009-9-1
 * @version V1.0
 * Copyright (C) www.nhn.com
 */
@XStreamAlias(value = "BuildServer")
public class BuildServer {

    private String dbBuildServer;

    private String dbBuildServerpath;

    private String dbBuildServeruser;

    private String dbBuildServerpwd;

    public String getDbBuildServer() {
        return dbBuildServer;
    }

    public void setDbBuildServer(String dbBuildServer) {
        this.dbBuildServer = dbBuildServer;
    }

    public String getDbBuildServerpath() {
        return dbBuildServerpath;
    }

    public void setDbBuildServerpath(String dbBuildServerpath) {
        this.dbBuildServerpath = dbBuildServerpath;
    }

    public String getDbBuildServeruser() {
        return dbBuildServeruser;
    }

    public void setDbBuildServeruser(String dbBuildServeruser) {
        this.dbBuildServeruser = dbBuildServeruser;
    }

    public String getDbBuildServerpwd() {
        return dbBuildServerpwd;
    }

    public void setDbBuildServerpwd(String dbBuildServerpwd) {
        this.dbBuildServerpwd = dbBuildServerpwd;
    }

    public static BuildServer fromFile(ConfigurationTreeModel model) {
        String filePath = model.getFilePath();
        Object obj = XstreamHelper.fromXml(filePath);
        BuildServer server = null;
        if (obj != null) {
            server = (BuildServer) obj;
        }
        return server;
    }
}
