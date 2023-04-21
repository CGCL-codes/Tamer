package org.igeek.atomqq.domain;

import java.util.HashMap;

/**
 * qq群
 * @author 作者 E-mail:hangxin1940@gmail.com
 * @version 创建时间：2011-8-10 上午11:58:16
 */
public class Group {

    private long flag;

    private String name;

    private String markname;

    private long gid;

    /** webqq用的群号标识,也就是伪群号*/
    private long code;

    /**群公告*/
    private String memo;

    private String fingermemo;

    private String owner;

    private int createtime;

    private int level;

    private int option;

    private String face;

    private HashMap<Long, Friend> members;

    private int newMessage;

    public Group(long gid) {
        this.gid = gid;
        newMessage = 0;
    }

    public Group() {
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getFingermemo() {
        return fingermemo;
    }

    public void setFingermemo(String fingermemo) {
        this.fingermemo = fingermemo;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public int hasNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }

    public HashMap<Long, Friend> getMembers() {
        return members;
    }

    public void setMembers(HashMap<Long, Friend> members) {
        this.members = members;
    }
}
