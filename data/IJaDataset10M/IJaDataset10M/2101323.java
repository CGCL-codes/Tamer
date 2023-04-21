package tikzmodel;

/**
 * This class represents a TikZ-picture as described in the
 * tikzpicture-environment in a LaTeX-file.
 */
public class TikzPicture extends TikzScope {

    /**
	 * This picture's type, may be TeX, LaTeX or ConTeXt.
	 */
    protected TikzPictureType type;

    /**
	 * Constructs a new LaTeX-TikzPicture.
	 */
    public TikzPicture() {
        this(TikzPictureType.LATEX);
    }

    /**
	 * Constructs a new TikzPicture with a given type.
	 * 
	 * @param type
	 * 
	 */
    public TikzPicture(TikzPictureType type) {
        this.type = type;
    }
}
