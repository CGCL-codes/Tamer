package org.nicocube.airain.ds;

import org.nicocube.airain.utils.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class DSClient {

    private static final Logger log = LoggerFactory.getLogger(DSClient.class);

    public static ObjectContainer open(Config c) {
        if (log.isDebugEnabled()) log.debug("open(\n" + "\t" + DataServerKeys.HOSTNAME + "=" + c.get(DataServerKeys.HOSTNAME) + "\n" + "\t" + DataServerKeys.PORT + "=" + c.get(DataServerKeys.PORT) + "\n" + "\t" + DataServerKeys.USER + "=" + c.get(DataServerKeys.USER) + "\n" + "\t" + DataServerKeys.PASSWORD + "=" + c.get(DataServerKeys.PASSWORD) + "\n" + ")");
        return Db4o.openClient(c.get(DataServerKeys.HOSTNAME), c.getInt(DataServerKeys.PORT), c.get(DataServerKeys.USER), c.get(DataServerKeys.PASSWORD));
    }

    public static ObjectContainer open(String prefix, Config c) {
        ObjectContainer db = Db4o.openClient(c.get(prefix + "." + DataServerKeys.HOSTNAME), c.getInt(prefix + "." + DataServerKeys.PORT), c.get(prefix + "." + DataServerKeys.USER), c.get(prefix + "." + DataServerKeys.PASSWORD));
        return db;
    }
}
