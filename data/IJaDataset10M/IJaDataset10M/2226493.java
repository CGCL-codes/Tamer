package net.sourceforge.jepesi.jsch.event;

import net.sourceforge.jepesi.model.Host;

public class CopyStatusEvent extends CopyEvent {

    private static final long serialVersionUID = 5824562513511012401L;

    private long residualSize;

    private boolean holeFile;

    public CopyStatusEvent(Object source, Host host, String localFile, String remoteFile, boolean download, long residualSize) {
        super(source, host, localFile, remoteFile, download);
        this.setResidualSize(residualSize);
        this.setHoleFile(false);
    }

    public CopyStatusEvent(Object source, Host host, String localFile, String remoteFile, boolean download, long residualSize, boolean holeFile) {
        super(source, host, localFile, remoteFile, download);
        this.setResidualSize(residualSize);
        this.setHoleFile(holeFile);
    }

    /**
	 * @param residualSize the residualSize to set
	 */
    public void setResidualSize(long residualSize) {
        this.residualSize = residualSize;
    }

    /**
	 * @return the residualSize
	 */
    public long getResidualSize() {
        return residualSize;
    }

    /**
	 * @param holeFile the holeFile to set
	 */
    public void setHoleFile(boolean holeFile) {
        this.holeFile = holeFile;
    }

    /**
	 * @return the holeFile
	 */
    public boolean isHoleFile() {
        return holeFile;
    }
}
