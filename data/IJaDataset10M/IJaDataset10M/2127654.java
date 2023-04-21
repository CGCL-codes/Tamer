package ghm.follow.font;

/** Indicates that no font style is currently selected */
public class NoFontStyleSelectedException extends InvalidFontException {

    public NoFontStyleSelectedException(String msg) {
        super(msg);
    }
}
