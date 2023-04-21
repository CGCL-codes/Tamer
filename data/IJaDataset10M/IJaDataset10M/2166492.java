package wsdir.policies;

import java.util.Vector;
import wsdir.core.AID;
import wsdir.core.DSClient;
import wsdir.core.DSProfile;
import wsdir.core.DirectoryDescription;
import wsdir.core.FailException;
import wsdir.core.Identity;
import wsdir.core.SearchConstraints;
import wsdir.security.SearchPolicySecurity;
import wsdir.server.CoreServer;
import wsdir.util.AbsoluteDateTime;
import wsdir.util.DateTime;
import wsdir.util.MalformedDateTimeException;
import wsdir.util.RelativeDateTime;
import atomik.core.AConcept;

public abstract class AbstractSecuritySearchPolicy implements SearchPolicy, SearchPolicySecurity {

    class Counter {

        int val;

        Counter(int _val) {
            val = _val;
        }

        synchronized void dec() {
            val--;
        }

        synchronized void inc() {
            val++;
        }

        synchronized int val() {
            return val;
        }
    }

    public AbstractSecuritySearchPolicy() {
    }

    CoreServer srv;

    public void initPolicy(CoreServer s, AConcept cfg) {
        srv = s;
    }

    public void destroyPolicy(CoreServer s) {
        srv = null;
    }

    public void initSearchPolicy(String name, String dirId) {
    }

    public abstract void getDSsForSupportDirectory(Identity id, String directoryId, Vector locals, Vector remotes) throws FailException;

    public Object[] search(Identity id, final String directoryId, final Object tpl, final SearchConstraints searchConstraints) throws FailException {
        if (!search((AConcept) tpl)) {
            return new Object[0];
        } else {
            System.out.println(">>> searching from the security abstract search policy in the directory:" + srv.getProfileLocally().aid.name);
            AbsoluteDateTime deadline = DateTime.normalize(searchConstraints.getMaxTime());
            Object tmp[] = srv.getDSStore(directoryId).search(directoryId, tpl, searchConstraints);
            final Vector vres = new Vector();
            for (int i = 0; i < Math.min(tmp.length, searchConstraints.getMaxResults()); i++) {
                vres.addElement(tmp[i]);
            }
            RelativeDateTime rt = null;
            try {
                rt = deadline.getInterval(new AbsoluteDateTime());
            } catch (MalformedDateTimeException exc) {
            }
            if ((vres.size() == searchConstraints.getMaxResults()) || (searchConstraints.getMaxDepth() == 0) || rt.getSignType().equals("-")) {
                Object res[] = new Object[vres.size()];
                vres.copyInto(res);
                return res;
            }
            final SearchConstraints tmpSC = new SearchConstraints(rt, searchConstraints.getMaxDepth() - 1, searchConstraints.getMaxResults() - vres.size(), searchConstraints.getQueryId());
            final Object lock = new Object();
            String tmpDir = directoryId;
            DirectoryDescription dd;
            Vector locals = new Vector(), remotes = new Vector();
            while (((dd = srv.getDirectoryDescription(tmpDir)) != null) && (dd.supportingDirectory != null)) {
                System.out.println(srv.getProfileLocally().aid.name + ": dir " + tmpDir + " supported by " + dd.supportingDirectory);
                tmpDir = dd.supportingDirectory;
                getDSsForSupportDirectory(id, tmpDir, locals, remotes);
            }
            DSProfile members[] = new DSProfile[locals.size() + remotes.size()];
            for (int i = 0; i < locals.size(); i++) {
                members[i] = (DSProfile) locals.elementAt(i);
            }
            for (int i = 0; i < remotes.size(); i++) {
                members[locals.size() + i] = (DSProfile) remotes.elementAt(i);
            }
            final Counter activeCount = new Counter(members.length);
            for (int i = 0; i < members.length; i++) {
                final AID searchAID = members[i].aid;
                System.out.println(srv.getProfileLocally().aid.name + " IS FORWARDING SEARCHING TO: " + searchAID.name);
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            DSClient clt = new DSClient(srv.getIdentityFor(searchAID, directoryId), searchAID.addresses[0]);
                            System.out.println(">>> SEARCHING FROM THE POLICY CLASS");
                            Object tmp_res[] = clt.searchDirectly(directoryId, tpl, tmpSC);
                            int i = 0;
                            while ((i < tmp_res.length) && (vres.size() < searchConstraints.getMaxResults())) {
                                vres.addElement(tmp_res[i]);
                                i++;
                            }
                        } catch (Exception exc) {
                            System.out.println(" " + srv.getProfileLocally().aid.name + " query " + searchConstraints.getQueryId() + " error " + exc);
                        }
                        activeCount.dec();
                        synchronized (lock) {
                            lock.notify();
                        }
                    }
                }).start();
            }
            long tosleep = deadline.getDateTimeMillis() - System.currentTimeMillis();
            while ((tosleep > 0) && (vres.size() < searchConstraints.getMaxResults()) && (activeCount.val() > 0)) {
                System.out.println(srv.getProfileLocally().aid.name + ": query " + searchConstraints.getQueryId() + " waiting results " + (searchConstraints.getMaxResults() - vres.size()) + " threads " + activeCount.val() + " sleeping " + tosleep);
                synchronized (lock) {
                    try {
                        lock.wait(tosleep);
                    } catch (Exception exc) {
                    }
                }
                tosleep = deadline.getDateTimeMillis() - System.currentTimeMillis();
            }
            System.out.println(srv.getProfileLocally().aid.name + ": query " + searchConstraints.getQueryId() + " finally " + vres.size() + " results");
            Object res[] = new Object[vres.size()];
            vres.copyInto(res);
            return res;
        }
    }

    public void destroySearchPolicy(String name, String dirId) {
    }
}
