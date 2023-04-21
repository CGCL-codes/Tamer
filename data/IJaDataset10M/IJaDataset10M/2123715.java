package org.test.files;

import org.pos.jdbc.PosDB;

/**
 *
 * @author pablo
 */
public class DBTest {

    public static void main(String[] args) {
        PosDB db = PosDB.getInstance();
        db.setSchema();
    }
}
