package sourceforge.shinigami.graphics;

/**
 * This exception must be thrown when an image in the database is corrupted or
 * nowhere to be found. It generally implies that the files in that version of
 * the client are damaged and should be replaced
 * 
 * @author kaorosorane
 * @since 4
 */
public class ImageCorruptedException extends RuntimeException {

    public ImageCorruptedException() {
    }

    public ImageCorruptedException(String message) {
        super(message);
    }

    /** SERIAL MUMBO-JUMBO */
    private static final long serialVersionUID = -7876703145413058320L;
}
