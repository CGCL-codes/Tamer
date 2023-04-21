package com.orientechnologies.jdo.engine.just;

import com.orientechnologies.jdo.engine.oEngineLoader;

public class oJustEngineLoader extends oEngineLoader {

    protected oJustEngineLoader() {
    }

    /**
     * Open and init Engine loader.
     */
    public void init() {
        threadGroup = new ThreadGroup("Orient ODBMS Just");
        System.loadLibrary("jdowrap");
        engineDatabaseImpl = new com.orientechnologies.jdo.engine.just.oJustEngineDatabase();
        engineObjImpl = new com.orientechnologies.jdo.engine.just.oJustEngineObj();
        engineQueryImpl = new com.orientechnologies.jdo.engine.just.oJustEngineQuery();
        engineSchemaImpl = new com.orientechnologies.jdo.engine.just.oJustEngineSchema();
        engineTxImpl = new com.orientechnologies.jdo.engine.just.oJustEngineTx();
    }

    /**
     * Close Engine loader.
     */
    public void deinit() {
        engineDatabaseImpl = null;
        engineObjImpl = null;
        engineQueryImpl = null;
        engineSchemaImpl = null;
        engineTxImpl = null;
        if (instance != null) instance = null;
    }

    public static oJustEngineLoader getInstance() {
        if (instance == null) {
            synchronized (oJustEngineLoader.class) {
                if (instance == null) instance = new oJustEngineLoader();
            }
        }
        return instance;
    }

    protected static oJustEngineLoader instance = null;
}
