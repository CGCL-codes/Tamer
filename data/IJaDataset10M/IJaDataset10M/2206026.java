package perf;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.ibex.js.Fountain;
import org.ibex.js.JS;
import org.ibex.js.JSU;
import org.ibex.js.JS.Keys;
import org.vexi.testutil.Utilities;
import test.Stuff;

public class PerfMultipleStream {

    static final String text;

    static {
        try {
            text = Utilities.fileAsString(new File("src_perf/perf/text"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) throws Exception {
        PerfMultipleStream pms = new PerfMultipleStream(5, 20, 2000);
        pms.create();
        pms.run();
    }

    public PerfMultipleStream(int n_archives, int files_per_archive, int file_length) {
        this.n_archives = n_archives;
        this.files_per_archive = files_per_archive;
        this.file_length = file_length;
    }

    static long t = System.currentTimeMillis();

    static void printElapsed(String note) {
        long old_t = t;
        t = System.currentTimeMillis();
        System.out.println(note + ": " + (t - old_t) + "ms");
    }

    final int n_archives;

    final int files_per_archive;

    final int file_length;

    public void create() throws Exception {
        File tmpDir = Utilities.createTmpDir();
        Utilities.deleteDir(tmpDir);
        tmpDir.mkdir();
        for (int i = 0; i < n_archives; i++) {
            String[] filenames = new String[files_per_archive];
            for (int j = 0; j < files_per_archive; j++) {
                filenames[j] = "a/b/package" + i + "/file" + j;
            }
            String archiveName = "archive" + i + ".zip";
            File srcDir = new File(tmpDir, "src" + i);
            Stuff.createZip(tmpDir, filenames, archiveName, text.substring(0, file_length));
            Stuff.extractZip(new File(tmpDir, archiveName), srcDir);
        }
        printElapsed("Created resources");
    }

    public void run() throws Exception {
        File tmpDir = Utilities.createTmpDir();
        Fountain.Multiple rr_archives = new Fountain.Multiple(n_archives);
        Fountain.Multiple rr_dirs = new Fountain.Multiple(n_archives);
        for (int i = 0; i < n_archives; i++) {
            String archiveName = "archive" + i + ".zip";
            File srcDir = new File(tmpDir, "src" + i);
            rr_archives.addOverrideStream(Fountain.newZip(new Fountain.File(new File(tmpDir, archiveName).getPath())));
            rr_dirs.addOverrideStream(new Fountain.File(srcDir.getPath()));
        }
        readAll(rr_archives);
        printElapsed("Read rr_archives");
        readAll(rr_dirs);
        printElapsed("Read rr_dirs");
    }

    public void readAll2(Fountain multiple) throws Exception {
        for (int i = 0; i < n_archives; i++) {
            Fountain dir = (Fountain) multiple.get(JSU.S("a")).get(JSU.S("b")).get(JSU.S("package" + i));
            Keys keys = dir.keys();
            JS.Enumeration en = keys.iterator();
            while (en.hasNext()) {
                JS k = en.next();
                Fountain f = (Fountain) dir.get(k);
                BufferedReader r = new BufferedReader(new InputStreamReader(f.getInputStream()));
                String l;
                int c = 0;
                while ((l = r.readLine()) != null) {
                    c += l.length();
                }
                if (c < file_length / 2) throw new RuntimeException();
            }
        }
    }

    public void readAll(Fountain multiple) throws Exception {
        for (int i = 0; i < n_archives; i++) {
            for (int j = 0; j < files_per_archive; j++) {
                Fountain f = (Fountain) multiple.get(JSU.S("a")).get(JSU.S("b")).get(JSU.S("package" + i)).get(JSU.S("file" + j));
                BufferedReader r = new BufferedReader(new InputStreamReader(f.getInputStream()));
                String l;
                int c = 0;
                while ((l = r.readLine()) != null) {
                    c += l.length();
                }
                if (c < file_length / 2) throw new RuntimeException();
            }
        }
    }
}
