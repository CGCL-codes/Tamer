package org.aha.mf4j;

/**
 * <p>
 *   Utility methods for use when working with Flickr photo data.
 * </p>
 * @author Arne Halvorsen (aha42)
 */
public final class Photos {

    private Photos() {
    }

    /**
   * <p>
   *   Check if single character
   *   {@code String} specify valid picture size.
   * </p>
   * @param s {@code String} to check.
   * @throws IllegalArgumentException If {@code s} does not specify legal size.
   */
    public static void checkSize(String s) {
        if (s == null) {
            throw new NullPointerException("s");
        }
        int n = s.length();
        if (n == 0) {
            throw new IllegalArgumentException("s is the empty String");
        }
        if (n > 1) {
            throw new IllegalArgumentException("s.length>1 : " + n);
        }
        checkSize(s.charAt(0));
    }

    /**
   * <p>
   *   Check if character specify valid picture size.
   * </p>
   * @param c Character to check.
   * @throws IllegalArgumentException If {@code c} does not specify legal size.
   */
    public static void checkSize(char c) {
        switch(c) {
            case 'm':
            case 's':
            case 't':
            case 'b':
                return;
            default:
                throw new IllegalArgumentException("does not specify valid size: " + c);
        }
    }

    /**
   * <p>
   *   Checks if given
   *   {@code String} specify valid format for a Flickr image.
   * </p>
   * @param f {@link String} to check.
   * @throws IllegalArgumentException If {@code f} does not specify valid 
   *         format.
   */
    public static void checkPhotoFormat(String f) {
        if (f == null) {
            throw new NullPointerException("f");
        }
        if (f.equals("jpg")) return;
        if (f.equals("png")) return;
        if (f.equals("gif")) return;
        throw new IllegalArgumentException("uknown image format : " + f);
    }
}
