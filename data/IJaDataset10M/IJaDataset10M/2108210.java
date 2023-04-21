package android.support.v2.os;

import android.os.Parcel;

public interface ParcelableCompatCreatorCallbacks<T> {

    public T createFromParcel(Parcel in, ClassLoader loader);

    public T[] newArray(int size);
}
