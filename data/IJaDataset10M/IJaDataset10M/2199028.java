package com.google.android.net;

import android.os.Parcel;
import android.os.Parcelable;

public class ParentalControlState implements Parcelable {

    public boolean isEnabled;

    public String redirectUrl;

    /**
     * Used to read a ParentalControlStatus from a Parcel.
     */
    public static final Parcelable.Creator<ParentalControlState> CREATOR = new Parcelable.Creator<ParentalControlState>() {

        public ParentalControlState createFromParcel(Parcel source) {
            ParentalControlState status = new ParentalControlState();
            status.isEnabled = (source.readInt() == 1);
            status.redirectUrl = source.readString();
            return status;
        }

        public ParentalControlState[] newArray(int size) {
            return new ParentalControlState[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(isEnabled ? 1 : 0);
        dest.writeString(redirectUrl);
    }

    @Override
    public String toString() {
        return isEnabled + ", " + redirectUrl;
    }
}

;
