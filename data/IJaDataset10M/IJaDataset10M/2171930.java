package test;

import java.util.Observable;
import java.util.Observer;
import net.sourceforge.fsync.connection.Session;
import net.sourceforge.fsync.filesystem.compare.ComparisonStrategy;
import net.sourceforge.fsync.filesystem.compare.FolderComparator;
import net.sourceforge.fsync.gui.LocalFolderChooser;
import net.sourceforge.fsync.properties.GlobalProperties;

public class Rolex {

    public Rolex() {
        super();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        GlobalProperties props = GlobalProperties.getFromFileSystem();
        boolean active = props.getConnections().iterator().next().isActive();
        String adr = props.getConnections().iterator().next().getOtherInet();
        Session session = new Session(active, adr);
        session.setLocalFolder(LocalFolderChooser.getFolder());
        FolderComparator fcomp = new FolderComparator(session, ComparisonStrategy.NAMES_ONLY);
        new Thread(fcomp.getFolder()).start();
    }
}
