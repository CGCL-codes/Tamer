package loci.formats;

/**
 * A listener for status updates.
 *
 * <dl><dt><b>Source code:</b></dt>
 * <dd><a href="https://skyking.microscopy.wisc.edu/trac/java/browser/trunk/loci/formats/StatusListener.java">Trac</a>,
 * <a href="https://skyking.microscopy.wisc.edu/svn/java/trunk/loci/formats/StatusListener.java">SVN</a></dd></dl>
 */
public interface StatusListener {

    /** Called when status is updated. */
    void statusUpdated(StatusEvent e);
}
