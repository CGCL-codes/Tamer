package net.sourceforge.freejava.cli.util;

import java.io.File;
import java.net.URL;
import net.sourceforge.freejava.snm.EclipseProject;
import net.sourceforge.freejava.util.file.ClassResource;
import net.sourceforge.freejava.util.file.FilePath;

public class MkbatTest {

    public static void main(String[] args) throws Throwable {
        File projBase = EclipseProject.findProjectBase(Mkbat.class);
        if (projBase == null) {
            System.err.println("can't find project base");
            return;
        }
        URL outurl = ClassResource.classData(MkbatTest.class, "out");
        File outdir = FilePath.canoniOf(outurl);
        outdir.mkdirs();
        File lapiBase = new File(projBase.getParentFile(), "net.bodz.lapiota");
        File moddir = FilePath.canoniOf(lapiBase, "mod/GUI Utilities");
        File indir = new File(moddir, "bin");
        String[] mkbatArgs = { "-O", outdir.getPath(), "-l", "bodz_swt", "-l", "bodz_lapiota", "-rq", "--", indir.getPath() };
        Mkbat.main(mkbatArgs);
    }
}
