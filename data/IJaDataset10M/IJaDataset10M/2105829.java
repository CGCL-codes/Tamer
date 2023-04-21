package velosurf.standalone;

import velosurf.context.DBReference;
import velosurf.util.Logger;
import velosurf.sql.Database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import org.apache.velocity.app.Velocity;

public class Velosurf extends DBReference {

    public Velosurf() throws IOException, SQLException {
        this(findConfig());
    }

    public Velosurf(File file) throws IOException, SQLException {
        this(new FileInputStream(file));
    }

    public Velosurf(InputStream config) throws IOException, SQLException {
        initLogging();
        init(config);
    }

    protected void initLogging() {
        Logger.log2Stderr();
    }

    protected static InputStream findConfig() throws IOException {
        String pathname = null;
        File file = null;
        pathname = System.getProperty("velosurf.config");
        if (pathname != null) {
            file = new File(pathname);
            if (file.exists()) return new FileInputStream(file);
        }
        pathname = (String) Velocity.getProperty("velosurf.config");
        if (pathname != null) {
            file = new File(pathname);
            if (file.exists()) return new FileInputStream(file);
        }
        String[] guesses = { "./velosurf.xml", "./conf/velosurf.xml", "./WEB-INF/velosurf.xml", "./cfg/velosurf.xml" };
        for (int i = 0; i < guesses.length; i++) {
            file = new File(guesses[i]);
            if (file.exists()) return new FileInputStream(file);
        }
        return null;
    }

    protected void init(InputStream configStream) throws SQLException, IOException {
        if (configStream == null) throw new IOException("Configuration InputStream is null!");
        Database db = Database.getInstance(configStream);
        super.init(db);
    }
}
