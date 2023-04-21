package be.lassi.pdf.tags;

import be.lassi.pdf.State;

/**
 * Support html to pdf translation of the level 5 header tag.
 */
public class H5 extends H {

    public H5(final State state) {
        super(state);
    }

    @Override
    protected String getLevel() {
        return "h5";
    }
}
