package net.sf.bt747.waba.system;

import bt747.sys.interfaces.BT747Path;

/**
 * @author Mario
 *
 */
public class WabaPath extends BT747Path {

    private int card;

    /**
     * 
     */
    public WabaPath(final String path, final int card) {
        super(path);
        this.card = card;
    }

    public BT747Path proto(final String path) {
        return new WabaPath(path, card);
    }

    public final int getCard() {
        return this.card;
    }
}
