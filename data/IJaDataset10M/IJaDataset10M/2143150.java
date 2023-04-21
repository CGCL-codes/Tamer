package edu.xjtu.jadd.impl.command;

import java.io.File;
import edu.xjtu.jadd.deployment.Deployable;
import edu.xjtu.jadd.impl.runtime.Runtime;
import edu.xjtu.jadd.util.FileUtil;

public class RecoverDeployableCommand implements Command {

    private Runtime runtime;

    @Override
    public String execute(String line) {
        String[] params = line.split(" ");
        String randomid = params[1];
        String deployableid = params[2];
        File repoFolder = new File(runtime.getRepositoryPath());
        FileUtil.makesureDir(runtime.getDeployPhasePath() + deployableid);
        File targetFolder = new File(runtime.getDeployPhasePath() + deployableid);
        File[] subfiles = repoFolder.listFiles();
        for (File file : subfiles) {
            if (file.getName().contains(randomid)) {
                FileUtil.CopyFile(file, new File(targetFolder, file.getName().substring(randomid.length() + 1)));
            }
        }
        Deployable dp = runtime.getDmanager().loadDeployableFromFolder(targetFolder.getPath());
        if (dp != null) {
            runtime.getDmanager().deploy(dp);
        }
        return "";
    }

    @Override
    public String getDescription() {
        return "recover the deployable from the out put files";
    }

    @Override
    public String getName() {
        return "recover";
    }

    public RecoverDeployableCommand(Runtime runtime) {
        super();
        this.runtime = runtime;
    }
}
