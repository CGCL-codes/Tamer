package as.util;

/**
 * @author evinyatar
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface ProgressNotifier {

    public void notifyProgress(int current, int max);

    public void notifyProgress(int current, int max, String message);
}
