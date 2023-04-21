package jsesh.mdc.model;

/**
 * @author rosmord
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LineBreak extends TopItem {

    /**
	 * post-break vertical spacing
	 * Default value ?
	 */
    private int spacing;

    public LineBreak(int spacing) {
        this.spacing = spacing;
    }

    /**
	 * 
	 */
    public LineBreak() {
        this(100);
    }

    public void accept(ModelElementVisitor v) {
        v.visitLineBreak(this);
    }

    /**
	 * Returns the spacing value.
	 * 100 is a full line, 200 is two lines worth, and so on.
	 * @return spacing.
	 */
    public int getSpacing() {
        return spacing;
    }

    /**
	 * @param i
	 */
    public void setSpacing(int i) {
        spacing = i;
    }

    public String toString() {
        return "lineBreak\n";
    }

    public int compareToAux(ModelElement e) {
        LineBreak b = (LineBreak) e;
        int result = spacing - b.spacing;
        if (result == 0) {
            result = getState().compareTo(b.getState());
        }
        return result;
    }

    public ModelElement deepCopy() {
        LineBreak b = new LineBreak(spacing);
        copyStateTo(b);
        return b;
    }

    public boolean isBreak() {
        return true;
    }
}
