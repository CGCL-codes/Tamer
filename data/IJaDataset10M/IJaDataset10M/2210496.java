package com.jchatting.server.thread;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import com.jchatting.db.DbHanddle;
import com.jchatting.db.bean.Friend;
import com.jchatting.pack.DataPackage;
import com.jchatting.server.util.ClientPool;
import com.jchatting.server.util.ServerMsgUtil;

/**
 * @author Xewee.Zhiwei.Wang
 * @version 2011-10-2 上午11:53:24
 */
public class OfflineTipThread extends Thread {

    private String account;

    private Map<String, Friend> friendMap;

    public OfflineTipThread(String account) {
        this.account = account;
        init();
    }

    private void init() {
        friendMap = new DbHanddle().getFriendMap(account);
    }

    @Override
    public void run() {
        Iterator<String> keyIterator = friendMap.keySet().iterator();
        String key;
        while (keyIterator.hasNext()) {
            key = keyIterator.next();
            try {
                if (ClientPool.getClient(key) != null) {
                    System.out.println("noticed offline to :" + key);
                    ServerMsgUtil.sendMsg(DataPackage.CLIENT_OFF, account, key, "");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
