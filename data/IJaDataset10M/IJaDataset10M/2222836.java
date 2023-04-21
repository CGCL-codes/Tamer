package tiniweb.module.env;

import com.dalsemi.system.TINIOS;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import tiniweb.core.AbstractModule;
import tiniweb.core.Module;
import tiniweb.core.JinaEnvironment;
import tiniweb.core.util.Util;

/**
 *
 * @author Yannick Poirier 
 */
public class Env extends AbstractModule {

    private Properties setEnv;

    private String[] unsetEnv;

    private String[] passEnv;

    private int sizePassEnv;

    public int init(String[] args) {
        try {
            Properties env = Util.getProperties("env.properties");
            this.setEnv = new Properties();
            this.init(env);
        } catch (FileNotFoundException ex) {
            return Module.ERROR;
        } catch (IOException ex) {
            return Module.ERROR;
        }
        return Module.OK;
    }

    private void init(Properties env) {
        Vector vecUnsetEnv = new Vector();
        Vector vecPassEnv = new Vector();
        Enumeration e = env.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if (key.endsWith(".set")) {
                setEnv.put(key.substring(0, key.indexOf(".set")), env.getProperty(key));
            } else if (key.endsWith(".unset")) {
                vecUnsetEnv.addElement(key.substring(0, key.indexOf(".unset")));
            } else if (key.endsWith(".pass")) {
                vecPassEnv.addElement(key.substring(0, key.indexOf(".pass")));
                ;
            }
        }
        this.initUnsetEnv(vecUnsetEnv);
        this.initPassEnv(vecPassEnv);
    }

    private void initPassEnv(Vector vecPass) {
        this.sizePassEnv = vecPass.size();
        this.passEnv = new String[sizePassEnv];
        for (int i = 0; i < sizePassEnv; i++) {
            this.passEnv[i] = (String) vecPass.elementAt(i);
        }
    }

    private void initUnsetEnv(Vector vecUnset) {
        int size = vecUnset.size();
        this.unsetEnv = new String[size];
        for (int i = 0; i < size; i++) {
            this.unsetEnv[i] = (String) vecUnset.elementAt(i);
        }
    }

    private void passEnv(JinaEnvironment tiniEnv) {
        for (int i = 0; i < this.sizePassEnv; i++) {
            String key = this.passEnv[i];
            try {
                tiniEnv.getEnvVar().setEnv(key, TINIOS.getFromCurrentEnvironment(key));
            } catch (Exception e) {
            }
        }
    }

    public int fixupHandler(JinaEnvironment tiniEnv) {
        tiniEnv.getEnvVar().setEnv(setEnv);
        this.passEnv(tiniEnv);
        tiniEnv.getEnvVar().unsetEnv(unsetEnv);
        return Module.OK;
    }
}
