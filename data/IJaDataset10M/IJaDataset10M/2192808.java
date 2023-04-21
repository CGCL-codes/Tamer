package ugent.tultimouch.touch;

public class TouchList {

    private Touch[] mTouches;

    public long length;

    TouchList(Touch[] Touches) {
        mTouches = Touches;
        length = Touches.length;
    }

    public Touch item(long index) {
        return mTouches[(int) index];
    }
}
