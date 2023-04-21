package com.gcalsync.cal.gcal;

import com.gcalsync.store.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Thomas Oldervoll, thomas@zenior.no
 * @author $Author: thomasold $
 * @version $Rev: 19 $
 * @date $Date: 2006-12-21 16:42:52 -0500 (Thu, 21 Dec 2006) $
 */
public class GCalFeed extends Storable {

    public String id;

    public String title;

    public String url;

    public String prefix;

    public boolean mainCalendar;

    public boolean reminders;

    public boolean sync;

    public GCalFeed() {
        super(RecordTypes.FEED);
    }

    public GCalFeed(String id, String title, String url) {
        this();
        this.id = id;
        this.title = title;
        this.url = url;
        mainCalendar = url.endsWith(Store.getOptions().username + "%40gmail.com/private/full");
        this.prefix = "";
        this.sync = false;
        this.reminders = false;
    }

    public void readRecord(DataInputStream in) throws IOException {
        id = in.readUTF();
        title = in.readUTF();
        url = in.readUTF();
        prefix = in.readUTF();
        sync = in.readBoolean();
        reminders = in.readBoolean();
    }

    public void writeRecord(DataOutputStream out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(title);
        out.writeUTF(url);
        out.writeUTF(prefix);
        out.writeBoolean(sync);
        out.writeBoolean(reminders);
    }
}
