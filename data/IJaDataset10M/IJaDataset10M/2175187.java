package chipchat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Mr. Lee
 */
public final class Room extends Thread {

    /** Chennel */
    private Channel channel;

    /** Room id number */
    private Long roomid;

    /** The name of room */
    private String name;

    /** Maximum number of person in this room */
    private int maxMan;

    /** Password */
    private String passwd;

    /** Host id number */
    private int host;

    /** Users */
    private HashMap users = new HashMap();

    /** Administrators */
    private HashMap admins = new HashMap();

    /** Message to send. */
    private Msg msg;

    /** Is this room finished? */
    private boolean isFinish = false;

    /** Lock object */
    private Object msgInputLock = new Object();

    /** Lock object */
    private Object msgOutputLock = new Object();

    /**
	 * @param channel Chennel of this room
	 * @param roomid Id number of room
	 * @param name The name of room
	 * @param maxMan  The number of maximum person
	 * @param passwd Password
	 * @param host Id number of host
	 */
    public Room(final Channel channel, final Long roomid, final String name, final int maxMan, final String passwd, final int host) {
        this.channel = channel;
        this.roomid = roomid;
        this.name = name;
        this.maxMan = maxMan;
        if (passwd != null && passwd.equals("")) {
            this.passwd = null;
        } else {
            this.passwd = passwd;
        }
        this.host = host;
        this.setDaemon(true);
        this.start();
    }

    /**
	 * Set master.
	 * @param master The id of master
	 * @return Id number of new master
	 */
    int setMaster(final int master) {
        User user;
        synchronized (users) {
            user = (User) users.get(new Integer(master));
        }
        if (user != null) {
            this.host = master;
            inputAdminInfo();
            inputAdminChange(user.getUsername());
            return this.host;
        } else {
            return setMaster();
        }
    }

    /**
	 * Set master anyone.
	 * @return New id number of master.
	 */
    int setMaster() {
        User user;
        synchronized (users) {
            if (!users.isEmpty()) {
                user = (User) users.get(users.keySet().iterator().next());
            } else {
                return host;
            }
        }
        host = user.getUserid().intValue();
        inputAdminInfo();
        inputAdminChange(user.getUsername());
        return host;
    }

    /**
	 * Is Full.
	 * @return Is Full?
	 */
    boolean isFull() {
        return (maxMan == users.size());
    }

    /**
	 * Is Empty.
	 * @return Is Empty?
	 */
    boolean isEmpty() {
        return (0 == users.size());
    }

    /**
	 * Insert user into this room.
	 * @param user User
	 * @param passwd Password
	 * @return Error Code, 0 : Ok, -1 : Room is full, -2 : Wrong password.
	 */
    int enterUser(final User user, final String passwd) {
        synchronized (users) {
            if (users.size() >= maxMan) {
                return -1;
            }
            if (this.passwd != null && (!this.passwd.equalsIgnoreCase(passwd))) {
                return -2;
            }
            users.put(user.getUserid(), user);
        }
        channel.setListChanged();
        inputInfo("GetIn", user.getUserid() + ">" + user.getUsername());
        inputUserInfo();
        return 0;
    }

    /**
	 * Insert administrator into this room.
	 * @param user User
	 * @param passwd Password
	 * @return Error Code, 0 : Ok
	 */
    int enterAdmin(final User user, final String passwd) {
        synchronized (admins) {
            admins.put(user.getUserid(), user);
        }
        return 0;
    }

    /**
	 * Get user out in this room.
	 * @param user User
	 */
    void exitUser(final User user) {
        boolean needSetMaster = false;
        synchronized (users) {
            if (users.remove(user.getUserid()) == null) {
                if (admins.remove(user.getUserid()) == null) {
                    System.out.println("Error : Userid not Exist in room.");
                }
                return;
            }
            channel.setListChanged();
            if (users.size() == 0) {
                this.maxMan = 0;
                this.isFinish = true;
                channel.removeRoom(roomid);
            }
            if (user.getUserid().intValue() == host) {
                needSetMaster = true;
            }
        }
        if (needSetMaster) {
            setMaster();
        }
        inputInfo("GetOut", user.getUsername());
        inputUserInfo();
    }

    /**
	 * Input message.
	 * @param msg Message
	 */
    void inputMsg(final Msg msg) {
        synchronized (msgInputLock) {
            while (this.msg != null && !isFinish) {
                try {
                    msgInputLock.wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.msg = msg;
        }
        synchronized (msgOutputLock) {
            msgOutputLock.notify();
        }
    }

    /**
	 * Input message.
	 * @param to To
	 * @param msg Message
	 * @param writer Writer name
	 */
    void inputMsg(final int to, final String msg, final String writer) {
        inputMsg(new Msg(Msg.TYPE_MSG, to, msg, writer));
    }

    /**
	 * Input custom message.
	 * @param to To
	 * @param msg Message
	 * @param writer Writer name
	 */
    void inputCustomMsg(final int to, final String msg, final String writer) {
        inputMsg(new Msg(Msg.TYPE_CUSTOMMSG, to, msg, writer));
    }

    /**
	 * Input message.
	 * @param msgType Tye of message
	 * @param to To
	 * @param msg Message
	 * @param writer Writer name
	 */
    void inputMsg(final int msgType, final int to, final String msg, final String writer) {
        inputMsg(new Msg(msgType, to, msg, writer));
    }

    /**
	 * Whisper message.
	 * @param from From
	 * @param to To
	 * @param msg Message
	 * @param writer Writer name
	 */
    void inputWhisper(final int from, final int to, final String msg, final String writer) {
        if (from == to) {
            inputError(from, "WSP:SendToMyself");
            return;
        }
        User user = (User) users.get(new Integer(to));
        if (user == null) {
            inputError(from, "WSP:NotExistAnother");
        } else {
            inputMsg(new Msg(Msg.TYPE_WSPSND, from, msg, user.getUsername()));
            inputMsg(new Msg(Msg.TYPE_WSPRCV, to, msg, writer));
        }
    }

    /**
	 * Input error message.
	 * @param to To
	 * @param msg Message
	 */
    void inputError(final int to, final String msg) {
        inputMsg(new Msg(Msg.TYPE_ERROR, to, msg, null));
    }

    /**
	 * Input room info.
	 */
    void inputRoomInfo() {
        int isPasswd = (passwd != null && (!"".equals(passwd))) ? 1 : 0;
        inputInfo("RoomInfo", maxMan + ">" + isPasswd + ">" + name);
    }

    /**
	 * Input User List information.
	 */
    void inputUserInfo() {
        StringBuffer sb = new StringBuffer();
        synchronized (users) {
            for (Iterator i = users.keySet().iterator(); i.hasNext(); ) {
                Object key = i.next();
                User user = (User) users.get(key);
                sb.append(user.getUserid()).append("<").append(user.getUsername()).append(">");
            }
        }
        inputMsg(Msg.TYPE_USERS, -1, sb.toString(), null);
    }

    /**
	 * Input Administrator information.
	 */
    void inputAdminInfo() {
        inputMsg(new Msg(Msg.TYPE_ADMIN, -1, "" + host, null));
    }

    /**
	 * Input message that administrator is chaged.
	 * @param to To
	 */
    void inputAdminChange(final String to) {
        inputMsg(new Msg(Msg.TYPE_ADMINCHANGE, -1, to, null));
    }

    /**
	 * Input command for someone to keep quiet.
	 * @param from From. Must be administrator.
	 * @param to To
	 */
    void inputKeepQuiet(final int from, final int to) {
        if (from == host) {
            User user = (User) users.get(new Integer(to));
            if (user == null) {
                inputError(host, "KEEPQUIET:NotExistUser");
            } else {
                inputMsg(new Msg(Msg.TYPE_KEEPQUIET, -1, "" + to, user.getUsername()));
            }
        } else {
            inputError(from, "YouNotMaster");
        }
    }

    /**
	 * Input command for someone to get out.
	 * @param from From
	 * @param to To
	 */
    void inputKickOut(final int from, final int to) {
        if (from == host) {
            User user = (User) users.get(new Integer(to));
            if (user == null) {
                inputError(host, "KICKOUT:NotExistUser");
            } else {
                inputMsg(new Msg(Msg.TYPE_KICKOUT, -1, "" + to, user.getUsername()));
            }
        } else {
            inputError(from, "YouNotMaster");
        }
    }

    /**
	 * Input command that administrator entrust to other.
	 * @param from From
	 * @param to To
	 */
    void inputEntrust(final int from, final int to) {
        if (from == host) {
            User user = (User) users.get(new Integer(to));
            if (user == null) {
                inputError(host, "ENTRUST:NotExistUser");
            } else {
                host = to;
                inputAdminInfo();
                inputAdminChange(user.getUsername());
            }
        } else {
            inputError(from, "YouNotMaster");
        }
    }

    /**
	 * Input some information message.
	 * @param infoName Name of infomation
	 * @param appendMsg Appending message
	 */
    void inputInfo(final String infoName, final String appendMsg) {
        inputMsg(new Msg(Msg.TYPE_INFO, -1, appendMsg, infoName));
    }

    /**
	 * Chage password of this room.
	 * @param from From
	 * @param newPasswd New password
	 */
    void changePasswd(final int from, final String newPasswd) {
        if (from == host) {
            this.passwd = newPasswd;
            inputRoomInfo();
            inputInfo("ChangePasswd", "");
        } else {
            inputError(from, "YouNotMaster");
        }
    }

    /**
	 * Change Room name.
	 * @param from From
	 * @param name Room name
	 */
    void changeRoomName(final int from, final String name) {
        if (from == host) {
            this.name = name;
            inputRoomInfo();
            inputInfo("ChangeRoomName", name);
        } else {
            inputError(from, "YouNotMaster");
        }
    }

    /**
	 * Change maximum number of mem.
	 * @param from From
	 * @param num Number
	 */
    void changeMaxMan(final int from, final int num) {
        if (from == host) {
            this.maxMan = num;
            inputRoomInfo();
            inputInfo("ChangeMaxMan", "" + num);
        } else {
            inputError(from, "YouNotMaster");
        }
    }

    /**
	 * Run function of Thread.
	 */
    public void run() {
        synchronized (msgOutputLock) {
            try {
                msgOutputLock.wait(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (msg == null) {
            System.out.println("This room is timeout. Member does not enter. [" + this.roomid + "]:" + this.name);
            maxMan = 0;
            isFinish = true;
            channel.removeRoom(roomid);
            return;
        }
        while (!isFinish) {
            synchronized (msgOutputLock) {
                if (msg != null) {
                    Msg lmsg = msg;
                    msg = null;
                    synchronized (msgInputLock) {
                        msgInputLock.notify();
                    }
                    synchronized (users) {
                        for (Iterator i = users.keySet().iterator(); i.hasNext(); ) {
                            Object key = i.next();
                            User user = (User) users.get(key);
                            String smsg = lmsg.getString(user.getUserid().intValue());
                            if (smsg != null) {
                                try {
                                    user.getOutputStream().write(smsg.getBytes());
                                    user.getOutputStream().flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    synchronized (admins) {
                        if (admins.size() > 0) {
                            Iterator i = admins.keySet().iterator();
                            while (i.hasNext()) {
                                Object key = i.next();
                                User user = (User) admins.get(key);
                                String smsg = lmsg.getString(-2);
                                if (smsg != null) {
                                    try {
                                        user.getOutputStream().write(smsg.getBytes());
                                        user.getOutputStream().flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    try {
                        msgOutputLock.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
	 * Getter of maxMan
	 * @return maxMan
	 */
    public int getMaxMan() {
        return maxMan;
    }

    /**
	 * Getter of name
	 * @return name
	 */
    public String getRoomName() {
        return name;
    }

    /**
	 * Getter of passwd
	 * @return passwd
	 */
    public String getPasswd() {
        return passwd;
    }

    /**
	 * Getter of users
	 * @return users
	 */
    public HashMap getUsers() {
        return users;
    }

    /**
	 * Getter of roomid
	 * @return roomid
	 */
    public Long getRoomid() {
        return roomid;
    }

    /**
	 * Getter of roomid
	 * @return master
	 */
    public int getMaster() {
        return host;
    }
}
