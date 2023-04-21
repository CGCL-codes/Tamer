package examples;

import com.entelience.sql.Db;

public class DbErrors {

    private Db getDb() {
        return null;
    }

    public void badMethodNoEnter() {
        Db db = getDb();
        try {
        } finally {
            db.exit();
        }
    }

    public void badMethodNoExit() {
        Db db = getDb();
        try {
            db.enter();
        } finally {
        }
    }
}
