package org.notima.adempiere;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 *
 * @author daniel
 */
public class CDbCommandsPostgreSQL implements IDbCommands {

    private Properties setLoginProperties(CAdempiereInstance ai, Properties env) {
        if (DbCommandFactory.DB_TYPE_PGSQL.equals(ai.getConnectionMap().get("type"))) {
            env.put("PGUSER", ai.getConnectionMap().get("UID").toString());
            env.put("PGPASSWORD", ai.getConnectionMap().get("PWD").toString());
            env.put("PGHOST", ai.getConnectionMap().get("DBhost"));
        }
        return env;
    }

    public List<String> runDbScript(CAdempiereInstance ai, Properties env, File file) {
        setLoginProperties(ai, env);
        List<String> cmd = new Vector<String>();
        cmd.add("psql");
        cmd.add("-d" + ai.getConnectionMap().get("DBname"));
        cmd.add("-f" + file.getAbsolutePath());
        return cmd;
    }

    public List<String> getVersionCmd(CAdempiereInstance ai, Properties env) {
        setLoginProperties(ai, env);
        List<String> cmd = new Vector<String>();
        cmd.add("psql");
        cmd.add("--version");
        return (cmd);
    }

    public List<String> dropDatabase(CAdempiereInstance ai, Properties env) {
        setLoginProperties(ai, env);
        List<String> cmd = new Vector<String>();
        cmd.add("dropdb");
        cmd.add(ai.getConnectionMap().get("DBname").toString());
        return (cmd);
    }

    public List<String> createDatabase(CAdempiereInstance ai, Properties env) {
        setLoginProperties(ai, env);
        List<String> cmd = new Vector<String>();
        cmd.add("createdb");
        cmd.add("-O " + ai.getConnectionMap().get("UID"));
        cmd.add("-E UTF8");
        cmd.add(ai.getConnectionMap().get("DBname").toString());
        return (cmd);
    }

    public List<String> restoreDatabase(CAdempiereInstance ai, Properties env, File file) {
        return (runDbScript(ai, env, file));
    }

    public List<String> backupDatabase(CAdempiereInstance ai, Properties env, File file) {
        setLoginProperties(ai, env);
        List<String> cmd = new Vector<String>();
        cmd.add("pg_dump");
        cmd.add("-f " + file.getAbsolutePath());
        cmd.add(ai.getConnectionMap().get("DBname").toString());
        return (cmd);
    }
}
