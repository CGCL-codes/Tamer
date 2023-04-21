package org.codehaus.jam.provider;

import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.mutable.MClass;

/**
 * <p>Composite implementation of JamClassBuilder.  When building,
 * the first one in the list to not return null wins.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class CompositeJamClassBuilder extends JamClassBuilder {

    private JamClassBuilder[] mBuilders;

    public CompositeJamClassBuilder(JamClassBuilder[] builders) {
        if (builders == null) throw new IllegalArgumentException("null builders");
        mBuilders = builders;
    }

    public void init(ElementContext ctx) {
        for (int i = 0; i < mBuilders.length; i++) mBuilders[i].init(ctx);
    }

    public MClass build(String pkg, String cname) {
        MClass out = null;
        for (int i = 0; i < mBuilders.length; i++) {
            out = mBuilders[i].build(pkg, cname);
            if (out != null) return out;
        }
        return null;
    }
}
