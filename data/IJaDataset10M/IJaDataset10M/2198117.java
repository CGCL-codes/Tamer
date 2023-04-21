package gate.versioning.svnkit;

import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.wc.ISVNInfoHandler;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.apache.log4j.Logger;

public class InfoHandler implements ISVNInfoHandler {

    /** Logger. */
    static Logger lgr = Logger.getLogger(InfoHandler.class);

    public void handleInfo(SVNInfo info) {
        lgr.info("-----------------INFO-----------------");
        lgr.info("Local Path: " + info.getFile().getPath());
        lgr.info("URL: " + info.getURL());
        if (info.isRemote() && info.getRepositoryRootURL() != null) {
            lgr.info("Repository Root URL: " + info.getRepositoryRootURL());
        }
        if (info.getRepositoryUUID() != null) {
            lgr.info("Repository UUID: " + info.getRepositoryUUID());
        }
        lgr.info("Revision: " + info.getRevision().getNumber());
        lgr.info("Node Kind: " + info.getKind().toString());
        if (!info.isRemote()) {
            lgr.info("Schedule: " + (info.getSchedule() != null ? info.getSchedule() : "normal"));
        }
        lgr.info("Last Changed Author: " + info.getAuthor());
        lgr.info("Last Changed Revision: " + info.getCommittedRevision().getNumber());
        lgr.info("Last Changed Date: " + info.getCommittedDate());
        if (info.getPropTime() != null) {
            lgr.info("Properties Last Updated: " + info.getPropTime());
        }
        if (info.getKind() == SVNNodeKind.FILE && info.getChecksum() != null) {
            if (info.getTextTime() != null) {
                lgr.info("Text Last Updated: " + info.getTextTime());
            }
            lgr.info("Checksum: " + info.getChecksum());
        }
        if (info.getLock() != null) {
            if (info.getLock().getID() != null) {
                lgr.info("Lock Token: " + info.getLock().getID());
            }
            lgr.info("Lock Owner: " + info.getLock().getOwner());
            lgr.info("Lock Created: " + info.getLock().getCreationDate());
            if (info.getLock().getExpirationDate() != null) {
                lgr.info("Lock Expires: " + info.getLock().getExpirationDate());
            }
            if (info.getLock().getComment() != null) {
                lgr.info("Lock Comment: " + info.getLock().getComment());
            }
        }
    }
}
