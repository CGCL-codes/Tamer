package com.agimatec.dbmigrate.action;

import com.agimatec.dbmigrate.AutoMigrationTool;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 01.12.2008 <br/>
 * Time: 10:26:08 <br/>
 * Copyright: Agimatec GmbH
 */
public abstract class MigrateAction {

    protected final AutoMigrationTool tool;

    public MigrateAction(AutoMigrationTool tool) {
        this.tool = tool;
    }

    public AutoMigrationTool getTool() {
        return tool;
    }

    public abstract void doIt() throws Exception;

    public String getInfo() {
        return toString();
    }
}
