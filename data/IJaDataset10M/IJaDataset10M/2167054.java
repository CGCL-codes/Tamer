package com.jxml.qare.qhome;

import com.jxml.qare.qhome.*;
import com.jxml.qare.qhome.db.*;
import com.jxml.quick.*;
import com.jxml.quick.ocm.*;
import java.util.*;
import org.xml.sax.*;
import java.io.*;
import java.net.*;
import com.jxml.quick.config.*;
import com.jxml.quick.qmap.*;

public class DisplayInboxMsgs implements Item {

    public DBUser.Row user;

    public DBInbox.Row inboxMsg;

    public void eval(Map properties, List args, ClassLoader cl) throws Exception {
        String inboxId = (String) properties.get("sessionID");
        user = (DBUser.Row) properties.get("user");
        String userName = user.getName();
        inboxMsg = Setup.dbInbox.get(user, new Integer(inboxId).intValue());
        inboxMsg.eval(properties, args, cl);
    }
}
