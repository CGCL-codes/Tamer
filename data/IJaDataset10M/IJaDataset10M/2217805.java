package org.scilab.forge.jlatexmath;

/**
 * Responsible for creating a box containing a delimiter symbol that exists
 * in different sizes.
 */
public class DelimiterFactory {

    public static Box create(SymbolAtom symbol, TeXEnvironment env, int size) {
        if (size > 4) return symbol.createBox(env);
        TeXFont tf = env.getTeXFont();
        int style = env.getStyle();
        Char c = tf.getChar(symbol.getName(), style);
        int i;
        for (i = 1; i <= size && tf.hasNextLarger(c); i++) c = tf.getNextLarger(c, style);
        if (i <= size && !tf.hasNextLarger(c)) {
            CharBox A = new CharBox(tf.getChar('A', "mathnormal", style));
            Box b = create(symbol.getName(), env, size * (A.getHeight() + A.getDepth()));
            return b;
        }
        return new CharBox(c);
    }

    /**
     *
     * @param symbol the name of the delimiter symbol
     * @param env the TeXEnvironment in which to create the delimiter box
     * @param minHeight the minimum required total height of the box (height + depth).
     * @return the box representing the delimiter variant that fits best according to
     * 			the required minimum size.
     */
    public static Box create(String symbol, TeXEnvironment env, float minHeight) {
        TeXFont tf = env.getTeXFont();
        int style = env.getStyle();
        Char c = tf.getChar(symbol, style);
        Metrics m = c.getMetrics();
        float total = m.getHeight() + m.getDepth();
        while (total < minHeight && tf.hasNextLarger(c)) {
            c = tf.getNextLarger(c, style);
            m = c.getMetrics();
            total = m.getHeight() + m.getDepth();
        }
        if (total >= minHeight) {
            return new CharBox(c);
        } else if (tf.isExtensionChar(c)) {
            VerticalBox vBox = new VerticalBox();
            Extension ext = tf.getExtension(c, style);
            if (ext.hasTop()) {
                c = ext.getTop();
                vBox.add(new CharBox(c));
            }
            boolean middle = ext.hasMiddle();
            if (middle) {
                c = ext.getMiddle();
                vBox.add(new CharBox(c));
            }
            if (ext.hasBottom()) {
                c = ext.getBottom();
                vBox.add(new CharBox(c));
            }
            c = ext.getRepeat();
            CharBox rep = new CharBox(c);
            while (vBox.getHeight() + vBox.getDepth() <= minHeight) {
                if (ext.hasTop() && ext.hasBottom()) {
                    vBox.add(1, rep);
                    if (middle) vBox.add(vBox.getSize() - 1, rep);
                } else if (ext.hasBottom()) vBox.add(0, rep); else vBox.add(rep);
            }
            return vBox;
        } else return new CharBox(c);
    }
}
