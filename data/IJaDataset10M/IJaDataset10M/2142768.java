package jsomap.map;

import jsomap.data.Pattern;
import jsomap.data.IntegerPattern;

/** A node with an underlying integer pattern.
 *
 * @author Zach Cox <zcox@iastate.edu>
 * @version $Revision: 1.3 $
 * @since JDK1.3
 */
public final class IntegerNode extends AbstractNode {

    private IntegerPattern _pattern;

    /** Creates a new <CODE>IntegerNode</CODE> using the specified pattern.
	 *
	 * @param pattern the pattern for this node.
	 * @throws NullPointerException if <CODE>pattern</CODE> is null.
	 */
    public IntegerNode(IntegerPattern pattern) {
        super(pattern);
        setPattern(pattern);
    }

    /** Creates a new <CODE>IntegerNode</CODE> using the specified pattern and model.
	 *
	 * @param pattern the pattern for this node.
	 * @param model the model for this node.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 */
    public IntegerNode(IntegerPattern pattern, Model model) {
        super(pattern, model);
        setPattern(pattern);
    }

    /** Returns the integer pattern for this node.
	 *
	 * @return the integer pattern for this node.
	 */
    public IntegerPattern getInteger() {
        return _pattern;
    }

    /** Returns the pattern for this node.
	 *
	 * @return the pattern for this node.
	 */
    public Pattern getPattern() {
        return _pattern;
    }

    /** Sets this node's pattern to the specified integer pattern.
	 *
	 * @param pattern the integer pattern.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 */
    public void setInteger(IntegerPattern pattern) {
        setPattern(pattern);
    }

    /** Sets this node's pattern to the specified pattern.
	 *
	 * @param pattern the pattern.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 * @throws ClassCastException if <CODE>pattern</CODE> is not an <CODE>IntegerPattern</CODE>.
	 */
    public void setPattern(Pattern pattern) {
        if (pattern == null) {
            throw new NullPointerException("pattern was null");
        }
        _pattern = (IntegerPattern) pattern;
    }
}
