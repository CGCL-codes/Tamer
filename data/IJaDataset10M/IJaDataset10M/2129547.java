package au.edu.uq.itee.eresearch.dimer.migrate;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.text.NumberFormat;
import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Workspace;
import org.apache.jackrabbit.commons.cnd.CndImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Migration {

    private static final Logger log = LoggerFactory.getLogger(Migration.class);

    protected static final String NS_CUSTOM_PREFIX = "";

    protected static final String NS_CUSTOM_URI = "";

    private int migrationNum;

    protected Session session;

    public static NumberFormat migrationNumFormat;

    static {
        migrationNumFormat = NumberFormat.getInstance();
        migrationNumFormat.setMinimumIntegerDigits(3);
        migrationNumFormat.setMaximumIntegerDigits(3);
        migrationNumFormat.setGroupingUsed(false);
    }

    public Migration(Session session) {
        this.session = session;
        this.migrationNum = Integer.parseInt(getClass().getSimpleName().substring("Migration".length()));
    }

    public static Migration getInstance(int migrationInt, Session session) {
        Class<?> migrationClass = null;
        try {
            String packageName = App.class.getPackage().getName();
            migrationClass = Class.forName(packageName + "." + "Migration" + migrationNumFormat.format(migrationInt));
            Constructor<?> constructor = migrationClass.getConstructor(new Class[] { Session.class });
            return (Migration) constructor.newInstance(new Object[] { session });
        } catch (Exception exception) {
            return null;
        }
    }

    public void migrate() throws Exception {
        checkVersion();
        migrateNamespaces();
        migrateNodeTypeDefinitions();
        migrateContent();
        migrateVersion();
        session.save();
    }

    private void checkVersion() throws Exception {
        Node root = session.getRootNode();
        long versionNum = root.hasProperty("version") ? root.getProperty("version").getLong() : 0L;
        if (versionNum != (migrationNum - 1)) {
            throw new RuntimeException("Expected repository at version " + (migrationNum - 1));
        }
    }

    private void migrateNamespaces() throws Exception {
        NamespaceRegistry namespaceRegistry = session.getWorkspace().getNamespaceRegistry();
        if (migrationNum == 1 && !(NS_CUSTOM_PREFIX.equals("") && NS_CUSTOM_URI.equals(""))) {
            namespaceRegistry.registerNamespace(NS_CUSTOM_PREFIX, NS_CUSTOM_URI);
            session.setNamespacePrefix(NS_CUSTOM_PREFIX, NS_CUSTOM_URI);
        } else if (!namespaceRegistry.getURI(NS_CUSTOM_PREFIX).equals(NS_CUSTOM_URI)) {
            throw new RuntimeException("CUSTOM namespace not properly bound!");
        }
    }

    private void migrateNodeTypeDefinitions() throws Exception {
        String nodetypesFileName = "nodetypes-" + migrationNumFormat.format(migrationNum) + ".cnd";
        log.info("Migrating nodetypes from {}", nodetypesFileName);
        String nodetypesFilePath = "au/edu/uq/itee/eresearch/dimer/migrate/" + nodetypesFileName;
        InputStream cndInputStream = this.getClass().getClassLoader().getResourceAsStream(nodetypesFilePath);
        if (cndInputStream == null) {
            return;
        }
        InputStreamReader cndReader = new InputStreamReader(cndInputStream);
        try {
            Workspace wsp = session.getWorkspace();
            CndImporter.registerNodeTypes(cndReader, "cnd input stream", wsp.getNodeTypeManager(), wsp.getNamespaceRegistry(), session.getValueFactory(), true);
        } finally {
            cndReader.close();
        }
    }

    public abstract void migrateContent() throws Exception;

    private void migrateVersion() throws Exception {
        session.getRootNode().setProperty("version", migrationNum);
    }
}
