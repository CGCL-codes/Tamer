package papertoolkit.pattern;

/**
 * <p>
 * Represents the direction of jitter for a particular pattern dot. Possible Values include UP,
 * DOWN, LEFT, RIGHT. Used by PDF Pattern generation to parse the pattern definition (stored in the
 * pattern package).
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>.</span>
 * </p>
 * 
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron B Yeh</a> (ronyeh(AT)cs.stanford.edu)
 */
public class PatternJitter {

    public static final char DOWN = 'd';

    public static final char LEFT = 'l';

    public static final char RIGHT = 'r';

    public static final char UP = 'u';
}
