package co.edu.unal.ungrid.client.comm;

import java.rmi.RemoteException;
import net.jini.core.entry.Entry;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import co.edu.unal.space.util.SpaceProxy;
import co.edu.unal.ungrid.client.controller.App;
import co.edu.unal.ungrid.client.controller.GroupManager;
import co.edu.unal.ungrid.client.model.GroupStatus;
import co.edu.unal.ungrid.client.model.Message;

public class Writer {

    public void writeMsg(Integer msgType, String sGroup, String sFrom, String sTo, String sContent) {
        GroupStatus gs = GroupManager.getInstance().takeGroupStatus(sGroup);
        if (gs != null) {
            writeEntry(new Message(msgType, gs.msgIndex, sGroup, sFrom, sTo, sContent));
            gs.incMsgIndex();
            GroupManager.getInstance().writeGroupStatus(gs);
        } else {
            App.getInstance().severe("Writer::writeMsg(): GroupStatus " + sGroup + "  not found in space.");
        }
    }

    public void writeEntry(Entry e) {
        writeEntry(e, DEF_LEASE);
    }

    public void writeEntry(Entry e, long lease) {
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            try {
                proxy.write(e, null, lease);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }

    public void sendSignal(Transaction txn, Entry entry) {
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            try {
                proxy.write(entry, txn, 10 * 1000);
                proxy.take(entry, txn, JavaSpace.NO_WAIT);
                if (txn != null) {
                    txn.commit();
                }
            } catch (RemoteException exc) {
                System.out.println("Exception while sending signal: " + exc);
            } catch (TransactionException exc) {
                System.out.println("Exception while sending signal: " + exc);
            } catch (UnusableEntryException exc) {
                System.out.println("Exception while sending signal: " + exc);
            } catch (InterruptedException exc) {
                System.out.println("Exception while sending signal: " + exc);
                if (txn != null) {
                    try {
                        txn.abort();
                    } catch (Exception te) {
                        System.out.println("Writer::sendSignal(): Exception while aborting transaction: " + te);
                    }
                }
            }
        }
    }

    private static final long MINUTE = 60 * 1000;

    private static final long DEF_LEASE = 60L * MINUTE;
}
