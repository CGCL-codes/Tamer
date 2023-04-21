package org.drftpd.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import net.sf.drftpd.event.Event;
import net.sf.drftpd.event.FtpListener;
import net.sf.drftpd.master.config.FtpConfig;
import org.drftpd.GlobalContext;
import org.drftpd.permissions.PathPermission;
import org.drftpd.remotefile.LinkedRemoteFileInterface;

/**
 * @author mog
 * @version $Id: AffilManagement.java 879 2004-12-29 03:39:22Z mog $
 */
public class AffilManagement extends FtpListener {

    public static class AffilPermission extends PathPermission {

        private String _group;

        public AffilPermission(String group) {
            super(Collections.singletonList("=" + group));
            _group = group;
        }

        public boolean checkPath(LinkedRemoteFileInterface path) {
            List<LinkedRemoteFileInterface> files = path.getAllParentFiles();
            return (files.get(files.size() - 2).getName().equals("groups") && files.get(files.size() - 3).getName().equals(_group));
        }
    }

    public void actionPerformed(Event event) {
    }

    public void unload() {
    }

    public ArrayList<String> groups;

    public void init(GlobalContext gctx) {
        super.init(gctx);
        getGlobalContext().getConfig().addObserver(new Observer() {

            public void update(Observable o, Object arg) {
                FtpConfig cfg = (FtpConfig) o;
                for (String group : groups) {
                    cfg.addPathPermission("privpath", new AffilPermission(group));
                }
            }
        });
    }
}
