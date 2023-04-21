package uk.co.caprica.vlcj.mrl;

/**
 * Implementation for a file MRL.
 * <p>
 * This class provides a fluent API for initialising the MRL, e.g.
 * <pre>
 * String mrl = new FileMrl().file("the-file.mp4")
 *                           .value();
 * </pre>
 * This will generate <code>"file://the-file.mp4"</code>.
 */
public class FileMrl implements Mrl {

    /**
   * 
   */
    private String file;

    /**
   * 
   */
    private String value;

    /**
   * 
   * 
   * @param file
   * @return
   */
    public FileMrl file(String file) {
        this.file = file;
        return this;
    }

    public String value() {
        if (value == null) {
            value = constructValue();
        }
        return value;
    }

    private String constructValue() {
        StringBuilder sb = new StringBuilder(40);
        sb.append("file");
        sb.append("://");
        sb.append(file);
        return sb.toString();
    }
}
