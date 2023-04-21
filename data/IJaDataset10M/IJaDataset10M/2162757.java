package org.pfyshnet.clienttools;

import java.io.File;
import java.io.IOException;
import org.pfyshnet.fec.FileGenInterface;
import org.pfyshnet.utils.FileKeeper;

public class ClientFileGenerater implements FileGenInterface {

    private FileKeeper Keeper;

    public ClientFileGenerater(String basedir) {
        Keeper = new FileKeeper(basedir, true, 0);
    }

    public File getNextFile(long block, int packet) {
        try {
            return Keeper.getKeeperFile("b" + block + ".p" + packet + ".");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
