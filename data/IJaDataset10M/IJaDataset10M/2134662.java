package migrator.actions;

import migrator.core.Database;
import migrator.core.Direction;
import migrator.core.Script;
import migrator.utils.Commons;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class SQLDumpAction extends Action {

    private Direction dir;

    private String scriptsLocation;

    private String dumpLocation;

    public SQLDumpAction(Database db, Direction dir, String scriptsLocation, String dumpLocation) {
        super(db);
        this.dir = dir;
        this.scriptsLocation = scriptsLocation;
        this.dumpLocation = dumpLocation;
    }

    public void execute() {
        List c = changes(dir);
        File f = new File(dumpLocation + "/" + dir.value() + "_DUMP.sql");
        if (f.exists()) f.delete();
        Commons.writeLines(f, c, ";");
    }

    private List changes(Direction dir) {
        File[] all = changeScripts();
        List statements = new ArrayList();
        for (int i = 0; i < all.length; i++) {
            Script s = new Script(all[i]);
            List lines = s.executables(dir);
            statements.addAll(lines);
        }
        return statements;
    }

    private File[] changeScripts() {
        return reOrder(new File(scriptsLocation).listFiles(new FilenameFilter() {

            public boolean accept(File file, String fileName) {
                return fileName.endsWith(".sql") && !fileName.equals("FORWARD_DUMP.sql") && !fileName.equals("REVERSE_DUMP.sql");
            }
        }));
    }

    private File[] reOrder(File[] files) {
        for (int i = 0; i < files.length; i++) {
            for (int j = i; j < files.length; j++) {
                if (compare(files[i], files[j])) {
                    File temp = files[i];
                    files[i] = files[j];
                    files[j] = temp;
                }
            }
        }
        return files;
    }

    private boolean compare(File file, File file1) {
        return Integer.parseInt(file.getName().split("_")[0]) > Integer.parseInt(file1.getName().split("_")[0]);
    }
}
