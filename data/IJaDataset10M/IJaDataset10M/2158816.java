package hu.ihash.test;

import hu.ihash.apps.dupecompare.view.DupeView;
import hu.ihash.apps.dupesearch.DupeSearch;
import hu.ihash.common.gui.task.TaskProgress;
import hu.ihash.common.gui.task.model.TaskAdapter;
import hu.ihash.common.model.file.FileWalker;
import hu.ihash.common.model.file.ImageFilter;
import hu.ihash.common.model.storage.ImageHashMap;
import hu.ihash.hashing.methods.ImageHashingMethods;
import hu.ihash.hashing.methods.versions.RGB_Method;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JFrame;

public class DupeCompareTest {

    private static DupeSearch ds;

    private static DupeView dv;

    public static void main(String[] args) {
        ArrayList<File> files = new ArrayList<File>();
        FileWalker fw = new FileWalker(ImageFilter.File);
        final ImageHashMap map = new ImageHashMap();
        files.add(new File("testimages/mass"));
        ImageHashingMethods.method = new RGB_Method();
        fw.clear();
        TaskProgress tp2 = new TaskProgress("Parsing directories...");
        tp2.started();
        fw.walk(files);
        tp2.finished();
        TaskAdapter tp = new TaskAdapter() {

            @Override
            public void finished() {
                dv.getGroupedList().set(ds.getResults().iterator());
            }
        };
        ds = new DupeSearch(fw, map, tp);
        ds.start();
        dv = new DupeView();
        dv.setVisible(true);
        dv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
