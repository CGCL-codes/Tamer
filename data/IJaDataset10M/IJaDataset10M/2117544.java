package org.fao.gast.lib;

import org.fao.geonet.lib.TypeLib;

public class Lib {

    public static XMLLib xml = new XMLLib();

    public static TextLib text = new TextLib();

    public static TypeLib type = new TypeLib();

    public static GuiLib gui = new GuiLib();

    public static IOLib io = new IOLib();

    public static ServiceLib service = new ServiceLib();

    public static SiteLib site = new SiteLib();

    public static MefLib mef = new MefLib();

    public static ConfigLib config;

    public static EmbeddedSCLib embeddedSC;

    public static EmbeddedDBLib embeddedDB;

    public static DatabaseLib database;

    public static MetadataLib metadata;

    public static ServerLib server;

    public static LogLib log;

    public static void init(String appPath) throws Exception {
        config = new ConfigLib(appPath);
        embeddedSC = new EmbeddedSCLib(appPath);
        embeddedDB = new EmbeddedDBLib(appPath);
        database = new DatabaseLib(appPath);
        metadata = new MetadataLib(appPath);
        server = new ServerLib(appPath);
        log = new LogLib(appPath);
    }
}
