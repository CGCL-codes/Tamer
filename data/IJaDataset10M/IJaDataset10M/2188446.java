package net.sourceforge.cobertura.util;

/**
 * Utility methods for working with archives.
 * 
 * @author John Lewis
 */
public abstract class ArchiveUtil {

    /**
	 * Return true if the given name ends with .jar, .zip,
	 * .war, .ear, or .sar (case insensitive).
	 * 
	 * @param name The file name.
	 * @return true if the name is an archive.
	 */
    public static boolean isArchive(String name) {
        name = name.toLowerCase();
        return name.endsWith(".jar") || name.endsWith(".zip") || name.endsWith(".war") || name.endsWith(".ear") || name.endsWith(".sar");
    }

    /**
	 * Check to see if the given file name is a signature file
	 * (meta-inf/*.rsa or meta-inf/*.sf).
	 * 
	 * @param name The file name.  Commonly a ZipEntry name.
	 * @return true if the name is a signature file.
	 */
    public static boolean isSignatureFile(String name) {
        name = name.toLowerCase();
        return (name.startsWith("meta-inf/") && (name.endsWith(".rsa") || name.endsWith(".sf")));
    }
}
