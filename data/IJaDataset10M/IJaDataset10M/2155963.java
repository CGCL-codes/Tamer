package icebird.metadata.constpool;

/**
 * @author sergey
 */
public final class ConstInteger extends ConstObject {

    private int value;

    /**
	 * @param pool
	 * @param value
	 */
    ConstInteger(ConstPool pool, int value) {
        super(pool);
        this.value = value;
    }

    /**
	 * Gets value.
	 * @return int
	 */
    public int getValue() {
        return value;
    }

    public byte getTag() {
        return CONST_INTEGER;
    }
}
