package boccaccio.andrea.myfilesutils.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Boccaccio
 *
 */
public class ConcreteIterativeExplorer extends AbsExplorer {

    private static ConcreteIterativeExplorer instance = null;

    private ConcreteIterativeExplorer() {
        this.setAlgorithm("iterativeExporer");
    }

    public static ConcreteIterativeExplorer getInstance() {
        if (instance == null) instance = new ConcreteIterativeExplorer();
        return instance;
    }

    @Override
    public List<File> getPreorderedFilesAndDirectoriesList(File f) {
        List<File> lfRet = new ArrayList<File>();
        List<File> lfTemp = new ArrayList<File>();
        if (f.exists()) {
            lfTemp.add(f);
            while (!lfTemp.isEmpty()) {
                File fso = lfTemp.remove(0);
                lfRet.add(fso);
                if (fso.isDirectory()) {
                    File[] childs = fso.listFiles();
                    for (int i = 0; i < childs.length; ++i) lfTemp.add(childs[i]);
                }
            }
        }
        return lfRet;
    }
}
