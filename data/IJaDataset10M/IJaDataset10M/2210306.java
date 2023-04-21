package gnu.hylafax.job;

/**
 * This class contains the information available on a basic job notification.
 * 
 * @author $Author: sjardine $
 * @version $Id: Event.java 162 2009-03-26 21:42:09Z sjardine $
 */
public class Event {

    private String queueFile;

    public String getFilename() {
        return queueFile;
    }

    public void setFilename(String fn) {
        queueFile = fn;
    }
}
