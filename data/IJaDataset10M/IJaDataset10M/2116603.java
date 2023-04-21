package de.fau.cs.osr.dosis.android;

import java.io.File;
import android.content.Context;
import android.util.Log;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import de.fau.cs.dosis.dto.DrugDto;
import de.fau.cs.dosis.dto.DrugDto;

public class DbHelper {

    private static ObjectContainer objectContainer = null;

    private static DbHelper instance = null;

    private Context context;

    public static synchronized DbHelper createInstance(Context context) {
        if (instance == null) instance = new DbHelper(context);
        return instance;
    }

    public static synchronized DbHelper getInstance() {
        if (instance == null) throw new NullPointerException("DbHelper instance not created yet!");
        return instance;
    }

    public DbHelper(Context context) {
        this.context = context;
    }

    private Configuration dbConfig() {
        Configuration c = Db4o.newConfiguration();
        c.objectClass(DrugDto.class).objectField("id").indexed(true);
        return c;
    }

    private String db4oDBFullPath(Context ctx) {
        return ctx.getDir("data", 0) + "/" + "drugs.db4o";
    }

    public ObjectContainer db() {
        try {
            if (objectContainer == null || objectContainer.ext().isClosed()) objectContainer = Db4o.openFile(dbConfig(), db4oDBFullPath(this.context));
            return objectContainer;
        } catch (Exception e) {
            Log.e(DbHelper.class.getName(), e.toString());
            return null;
        }
    }

    public void commit() {
        db().commit();
    }

    public void rollback() {
        db().rollback();
    }

    public void close() {
        if (objectContainer != null) {
            objectContainer.close();
            objectContainer = null;
        }
    }

    public void dropDatabase() {
        close();
        new File(db4oDBFullPath(this.context)).delete();
    }
}
