package org.azrul.epice.patch;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import org.azrul.epice.domain.Attachment;
import org.azrul.epice.util.DBUtil;

public class DeleteAllAttachments {

    public static void main(String[] args) {
        ObjectContainer db = null;
        try {
            db = DBUtil.getDB();
            ObjectSet<Attachment> attachments = db.get(new Attachment());
            while (attachments.hasNext()) {
                Attachment attachment = attachments.next();
                db.ext().delete(attachment);
            }
            db.commit();
        } catch (Exception e) {
            if (db != null) {
                db.rollback();
            }
        } finally {
            if (db != null) {
                DBUtil.closeDB();
            }
        }
    }
}
