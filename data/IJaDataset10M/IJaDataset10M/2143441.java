package org.furthurnet.furi;

import java.util.*;

public class IrcChannel implements IComparable {

    private String mName;

    private String mDescription;

    private String mTopic = "";

    private boolean mJoined = false;

    private Vector mUsers = new Vector();

    private Vector mMsgs = new Vector();

    private int mLastSeenIndex = -1;

    private boolean mIsCurrent = false;

    private DataChanger mUserChanger = new DataChanger();

    private DataChanger mMsgChanger = new DataChanger();

    private IrcManager mIrcMgr = ServiceManager.getIrcManager();

    private IrcChannel() {
    }

    public IrcChannel(String channelName) {
        mName = channelName;
    }

    public IrcChannel(String channelName, String channelDescription) {
        mName = channelName;
        mDescription = channelDescription;
    }

    public void addUserChangedListener(IDataChangedListener listener) {
        mUserChanger.addListener(listener);
    }

    public void addMsgChangedListener(IDataChangedListener listener) {
        mMsgChanger.addListener(listener);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        mTopic = topic;
    }

    public void addUser(IrcUser user) {
        SortUtil.orderedInsert(mUsers, user);
        if (mIsCurrent) mUserChanger.dataChanged(this);
    }

    public void deleteUser(IrcUser user) {
        int i = SortUtil.orderedFind(mUsers, user);
        if (i != -1) {
            mUsers.removeElementAt(i);
            if (mIsCurrent) mUserChanger.dataChanged(this);
        }
    }

    public void deleteUser(String nick) {
        deleteUser(new IrcUser(nick));
    }

    public IrcUser findUser(String nick) {
        int i = SortUtil.orderedFind(mUsers, new IrcUser(nick));
        if (i != -1) {
            return (IrcUser) mUsers.elementAt(i);
        }
        return null;
    }

    public void renameUser(String oldNick, String newNick) {
        IrcUser oldUser = new IrcUser(oldNick);
        int i = SortUtil.orderedFind(mUsers, oldUser);
        if (i != -1) {
            deleteUser(oldUser);
            addUser(new IrcUser(newNick));
            addMsg(new IrcMsg(null, oldNick + " is now known as " + newNick));
        }
    }

    public void addMsg(IrcMsg msg) {
        if (mMsgs.size() >= ServiceManager.getCfg().mChatMax) {
            mMsgs.removeElementAt(0);
            mLastSeenIndex--;
        }
        mMsgs.addElement(msg);
        if (mIsCurrent) mMsgChanger.dataChanged(this);
    }

    public int getMsgCount() {
        return mMsgs.size();
    }

    public int getUserCount() {
        return mUsers.size();
    }

    public Vector getUsers() {
        return mUsers;
    }

    public String getMsgCountStr() {
        if (mLastSeenIndex < mMsgs.size() && mMsgs.size() > 0) {
            return (mMsgs.size() + " (new messages)");
        } else {
            return "" + mMsgs.size();
        }
    }

    public String getLastMsgText() {
        mLastSeenIndex = mMsgs.size();
        if (mLastSeenIndex == 0) return "";
        IrcMsg msg = (IrcMsg) mMsgs.elementAt(mLastSeenIndex - 1);
        StringBuffer buf = new StringBuffer();
        msg.toDisplayString(buf);
        return buf.toString();
    }

    public String getChatLogText() {
        StringBuffer buf = new StringBuffer();
        mLastSeenIndex = mMsgs.size();
        for (int i = 0; i < mLastSeenIndex; i++) {
            IrcMsg msg = (IrcMsg) mMsgs.elementAt(i);
            msg.toDisplayString(buf);
        }
        return buf.toString();
    }

    public String getChatLogText(boolean displaySystemMessages) {
        StringBuffer buf = new StringBuffer();
        mLastSeenIndex = mMsgs.size();
        for (int i = 0; i < mLastSeenIndex; i++) {
            IrcMsg msg = (IrcMsg) mMsgs.elementAt(i);
            if (displaySystemMessages || msg.getSender() != null) msg.toDisplayString(buf);
        }
        return buf.toString();
    }

    public boolean getIsCurrent() {
        return mIsCurrent;
    }

    public void setIsCurrent(boolean current) {
        mIsCurrent = current;
    }

    public boolean getJoined() {
        return mJoined;
    }

    public void setJoined(boolean joined) {
        mJoined = joined;
    }

    public void join() {
        if (!mIrcMgr.getIsConnected()) return;
        if (mJoined) return;
        MsgIRC msg = new MsgIRC(null, "JOIN", mName);
        mIrcMgr.sendMsg(msg);
        mJoined = true;
        try {
            ServiceManager.getManager().getMainFrame().chatPane().setIrcFocus();
        } catch (Exception e) {
        }
    }

    public void part() {
        if (!mIrcMgr.getIsConnected()) return;
        if (!mJoined) return;
        MsgIRC msg = new MsgIRC(null, "PART", mName);
        mIrcMgr.sendMsg(msg);
        mJoined = false;
        clearAll();
    }

    public void send(String text) {
        if (!mIrcMgr.getIsConnected()) return;
        if (!mJoined) return;
        MsgIRC msg;
        IrcMsg log;
        StringBuffer msgBuf = new StringBuffer(mName + " :");
        if (text.matches("^\\* .*")) {
            text = text.replaceFirst("\\* ", "");
            StringBuffer textBuf = new StringBuffer();
            textBuf.append((char) 0x01);
            textBuf.append(" ");
            textBuf.append(text);
            textBuf.append(" ");
            textBuf.append((char) 0x01);
            StringBuffer sender = new StringBuffer(ServiceManager.getCfg().mIrcNickname + ServiceManager.getCfg().mIrcNickIdentifier);
            sender.deleteCharAt(sender.length() - 1);
            log = new IrcMsg(sender.toString(), textBuf.toString());
            textBuf.replace(1, 1, "ACTION ");
            msgBuf.append(textBuf);
            msg = new MsgIRC(null, "PRIVMSG", msgBuf.toString());
        } else if ((text.trim().startsWith("/")) && (!text.startsWith("/list"))) {
            return;
        } else {
            msgBuf.append(text);
            msg = new MsgIRC(null, "PRIVMSG", msgBuf.toString());
            log = new IrcMsg(ServiceManager.getCfg().mIrcNickname + ServiceManager.getCfg().mIrcNickIdentifier, text);
        }
        addMsg(log);
        mIrcMgr.sendMsg(msg);
    }

    public int compares(IComparable obj2) {
        return mName.compareTo(((IrcChannel) obj2).mName);
    }

    public void clearAll() {
        mMsgs.removeAllElements();
        mUsers.removeAllElements();
        mUserChanger.dataChanged(this);
        mMsgChanger.dataChanged(this);
        try {
            ServiceManager.getManager().getMainFrame().chatPane().clearIrcWindows(false);
        } catch (Exception e) {
        }
    }
}
