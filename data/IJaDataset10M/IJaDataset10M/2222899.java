package net.sf.josceleton.core.impl.entity.user;

import net.sf.josceleton.core.api.entity.user.UserColorFactory;

class DefaultUserColorFactoryImpl implements UserColorFactory {

    private static final int[] DEFAULT_STARTING_VALUES = new int[] { 0x990000, 0x0033FF, 0x009900, 0x6B9B98, 0xFFCC00, 0x9933FF };

    private int count = 0;

    @Override
    public int create() {
        final int resultColor;
        if (this.count < DEFAULT_STARTING_VALUES.length) {
            resultColor = DEFAULT_STARTING_VALUES[this.count];
            this.count++;
        } else {
            resultColor = 0xFF308F;
        }
        return resultColor;
    }
}
