package co.edu.unal.ungrid.client.comm;

import java.io.File;
import net.jini.core.lease.Lease;
import co.edu.unal.space.util.SpaceProxy;
import co.edu.unal.ungrid.client.controller.App;
import co.edu.unal.ungrid.client.controller.CommManager;
import co.edu.unal.ungrid.client.controller.DesktopManager;
import co.edu.unal.ungrid.client.controller.GroupManager;
import co.edu.unal.ungrid.client.controller.VersionManager;
import co.edu.unal.ungrid.client.model.GroupStatus;
import co.edu.unal.ungrid.client.model.Message;
import co.edu.unal.ungrid.client.util.MessageType;

public class Reader extends Thread {

    public Reader() {
        m_mcLastRead = new MsgIndex();
        m_mcLastPrivateRead = new MsgIndex();
    }

    public synchronized void setStop() {
        App.getInstance().dbgMsg("Reader::setStop(): stopping thread ...");
        m_bStop = true;
    }

    private void resetLastRead(MsgIndex mi) {
        mi.reset();
    }

    public void initLastRead(MsgIndex mi) {
        App.getInstance().startWait();
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            GroupStatus gv = GroupManager.getInstance().getMaxIndex();
            if (gv != null) {
                Long li = gv.msgIndex;
                try {
                    Message fst = null;
                    while (fst == null) {
                        Message tmp = new Message(null, mi.index, GroupManager.getInstance().getChat(), null, null, null);
                        fst = (Message) proxy.readIfExists(tmp, null, Lease.FOREVER);
                        if (mi.index.longValue() == li.longValue()) break;
                        if (fst == null) mi.increment();
                        App.getInstance().dbgMsg("Reader::initLastRead(): fst=" + fst + " mi.count=" + mi.index.longValue());
                    }
                } catch (Exception exc) {
                    System.out.println("Reader::initLastRead(): " + exc);
                }
            }
            App.getInstance().dbgMsg("Reader::initLastRead(): mi.count=" + mi.index.longValue());
            App.getInstance().endWait();
        }
    }

    public Message takeMsg(Message tmp, long lease) {
        Message msg = null;
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            try {
                msg = (Message) proxy.take(tmp, null, lease);
            } catch (Exception exc) {
                System.out.println("Reader::takeMsg(): " + exc);
            }
        }
        return msg;
    }

    public Message takeMsg(Message tmp) {
        return takeMsg(tmp, Lease.FOREVER);
    }

    public Message read(Message tmp, long lease) {
        Message msg = null;
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            try {
                msg = (Message) proxy.read(tmp, null, lease);
            } catch (Exception exc) {
                System.out.println("Reader::read(): " + exc);
            }
        }
        return msg;
    }

    public Message readIfExists(Message tmp) {
        Message msg = null;
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            try {
                msg = (Message) proxy.readIfExists(tmp, null, Lease.FOREVER);
            } catch (Exception exc) {
                System.out.println("Reader::readIfExists(): " + exc);
            }
        }
        return msg;
    }

    private void readMsg(MsgIndex mi) {
        App.getInstance().dbgMsg("Reader::readMsg(): " + GroupManager.getInstance().getChat() + "=" + mi.index.longValue());
        SpaceProxy proxy = App.getInstance().getProxy();
        if (proxy != null) {
            Message tmp = new Message(null, mi.index, GroupManager.getInstance().getChat(), null, null, null);
            try {
                Message msg = (Message) proxy.read(tmp, null, Lease.FOREVER);
                App.getInstance().dbgMsg("Reader::readMsg(): msg=" + msg);
                if (!msg.type.equals(MessageType.DELETED)) {
                    dispatch(msg);
                }
                mi.increment();
            } catch (Exception exc) {
                System.out.println("Reader::readMsg(): " + exc);
                App.getInstance().recoverSpace();
            }
        }
    }

    private void dispatch(Message msg) {
        if (msg.type.equals(MessageType.EXIT)) {
            processShutdown(msg);
        } else if (msg.type.equals(MessageType.HIDESHOW)) {
            processHideShow(msg);
        } else if (msg.type.equals(MessageType.RESTART)) {
            processRestart(msg);
        } else if (msg.type.equals(MessageType.UPDATE)) {
            processUpdate(msg);
        } else if (msg.type.equals(MessageType.RING)) {
            processRing(msg);
        } else if (msg.type.equals(MessageType.INVITATION)) {
            processInvitationToPrivateChat(msg);
        } else if (msg.type.equals(MessageType.RESPONSE)) {
            processResponseToInvitation(msg);
        } else if (msg.type.equals(MessageType.SEND)) {
            processReceiveFile(msg);
        } else if (msg.type.equals(MessageType.RECEIVE)) {
            processResponseToSendFile(msg);
        } else if (msg.type.equals(MessageType.DESKTOP)) {
            processRemoteDesktop(msg);
        } else if (msg.type.equals(MessageType.SQL)) {
            processSqlSentence(msg);
        } else {
            App.getInstance().showMsg(msg);
        }
    }

    private void processShutdown(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            CommManager.getInstance().deleteMsg(msg);
            App.getInstance().showMessageDialog(msg.content);
            App.getInstance().exit();
        }
    }

    private void processHideShow(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            CommManager.getInstance().deleteMsg(msg);
            App.getInstance().handleHideShow(msg);
        }
    }

    private void processRestart(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            CommManager.getInstance().deleteMsg(msg);
            App.getInstance().restart(new File("").getAbsolutePath());
        }
    }

    private void processUpdate(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            CommManager.getInstance().deleteMsg(msg);
            VersionManager.getInstance().update(false);
        }
    }

    private void processRing(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            CommManager.getInstance().deleteMsg(msg);
            App.getInstance().ringing(msg);
        }
    }

    private void processInvitationToPrivateChat(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            try {
                CommManager.getInstance().deleteMsg(msg);
                CommManager.getInstance().sendResponseToPrivateChat(msg);
            } catch (Exception exc) {
                System.out.println("Reader::processInvitationToPrivateChat(): " + exc);
            }
        }
    }

    private void processResponseToInvitation(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            try {
                CommManager.getInstance().deleteMsg(msg);
                if (msg.content != null) {
                    if (App.getInstance().enterPrivateChat(msg.content)) {
                        resetLastRead(m_mcLastPrivateRead);
                        initLastRead(m_mcLastPrivateRead);
                        CommManager.getInstance().send("User " + GroupManager.getInstance().getName() + " has entered a private chat.");
                    }
                } else {
                }
            } catch (Exception exc) {
                System.out.println("Reader::processResponseToInvitation(): " + exc);
            }
        }
    }

    private void processReceiveFile(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            try {
                CommManager.getInstance().deleteMsg(msg);
                CommManager.getInstance().sendResponseToReceiveFile(msg);
            } catch (Exception exc) {
                System.out.println("Reader::processReceiveFile(): " + exc);
            }
        }
    }

    private void processResponseToSendFile(Message msg) {
        if (msg.to.equals(GroupManager.getInstance().getName())) {
            try {
                CommManager.getInstance().deleteMsg(msg);
                processResponseToReceiveFile(msg);
            } catch (Exception exc) {
                System.out.println("Reader::processResponseToSendFile(): " + exc);
            }
        }
    }

    private void processResponseToReceiveFile(Message msg) {
        if (msg.content != null) {
            App.getInstance().doSendFile();
        } else {
        }
    }

    private void processRemoteDesktop(Message msg) {
        DesktopManager.getInstance().startRemoteDesktop(msg);
    }

    private void processSqlSentence(Message msg) {
        App.getInstance().processSqlSentence(msg);
    }

    private void readOpenMsg() {
        readMsg(m_mcLastRead);
    }

    private void readPrivateMsg() {
        readMsg(m_mcLastPrivateRead);
    }

    public void waitChange() {
        m_bWaitingChange = true;
    }

    public void resumeChange() {
        m_bWaitingChange = false;
    }

    public void run() {
        initLastRead(m_mcLastRead);
        while (!m_bStop) {
            if (m_bWaitingChange) {
                try {
                    Thread.sleep(100);
                } catch (Exception exc) {
                    System.out.println("Reader::run(): " + exc);
                }
            } else {
                if (GroupManager.getInstance().isDefGroup()) {
                    readOpenMsg();
                } else {
                    readPrivateMsg();
                }
            }
        }
    }

    private class MsgIndex {

        public MsgIndex() {
            reset();
        }

        public void reset() {
            index = new Long(0);
        }

        public void increment() {
            index = new Long(index.longValue() + 1);
        }

        public Long index;
    }

    private MsgIndex m_mcLastRead;

    private MsgIndex m_mcLastPrivateRead;

    private boolean m_bStop;

    private boolean m_bWaitingChange;
}
