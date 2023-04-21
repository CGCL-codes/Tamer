package fr.soleil.TangoArchiving.ArchivingManagerApi;

/**
 * @author HO
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ArchivingManagerResult {

    /**
	 * ArchivingStartHdb/Tdb command succeed
	 */
    public static final short OK_ARCHIVINGSTART = 0;

    /**
	 * ArchivingStopHdb/Tdb command succeed
	 */
    public static final short OK_ARCHIVINGSTOP = 3;

    /**
	 * ArchivingModif command succeed
	 */
    public static final short OK_ARCHIVINGMODIF = 6;

    public static final String CONNECTION_SUCCEED = "Connection succeed";

    public static final String CONNECTION_FAILED = "Connection succeed";
}
