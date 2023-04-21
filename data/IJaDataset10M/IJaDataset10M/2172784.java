package android.os;

/**
 * Non-functinal dummy of an Android Parcel
 */
public class Parcel {

    public void writeLong(long id) {
    }

    public void writeString(String name) {
    }

    public void writeInt(int code) {
    }

    public void writeFloat(float partDone) {
    }

    public final void writeParcelable(Parcelable p, int parcelableFlags) {
    }

    public void writeBundle(Bundle extras) {
    }

    public long readLong() {
        return 0;
    }

    public String readString() {
        return null;
    }

    public int readInt() {
        return 0;
    }

    public float readFloat() {
        return 0;
    }

    public <T extends Parcelable> T readParcelable(ClassLoader classLoader) {
        return null;
    }

    public Bundle readBundle() {
        return null;
    }
}
