package api.client.bpmModel;

import java.io.File;

public class DeployUtil {

    static File out = new File("/ordner/bpr");

    public static void deploy(final File f) {
        f.renameTo(new File(out + "/echo.bpr"));
    }

    public static void undeploy(final File f) {
        f.delete();
    }

    File[] listDeployed() {
        return out.listFiles();
    }

    public void update() {
    }

    public void buildService() {
    }
}
