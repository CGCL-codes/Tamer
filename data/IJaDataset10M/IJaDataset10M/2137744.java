package eu.jbart.bunit;

/**
 * @author Bart Frackiewicz <mail@jbart.eu>
 *
 */
public class BasicAsserts {

    /**
     * kann nicht auf workingPath zugreifen.
     * @param file
     */
    public static void fileSizeIsGreatherThan(FileBackupFile file) {
        throw new AssertionError("Lala");
    }
}
