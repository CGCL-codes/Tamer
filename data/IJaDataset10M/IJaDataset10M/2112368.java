package satmule.domain.infoSearcher;

/**
 * @author mierdoso
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IChunkProvider {

    public void downloadAcepted(String hash);

    public void downloadRejected(String hash);

    public void chunkConfirm(String hash, long length, long offset);
}
