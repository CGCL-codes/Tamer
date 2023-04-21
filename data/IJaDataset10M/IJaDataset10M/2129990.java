package org.aladdinframework.application.api;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * VersionInfo provides generic version information using a 'major', 'minor', 'micro' scheme. Importantly, VersionInfo
 * is Parcelable and can be sent using Android's AIDL event mechanism.
 * 
 * @author Darren Carlson
 */
public final class VersionInfo implements Parcelable, Comparable<VersionInfo>, Serializable {

    private static final long serialVersionUID = -4833433156542436517L;

    private static final String TAG = VersionInfo.class.getSimpleName();

    /**
     * Static Parcelable Creator required to reconstruct a the object from an incoming Parcel
     */
    public static final Parcelable.Creator<VersionInfo> CREATOR = new Parcelable.Creator<VersionInfo>() {

        public VersionInfo createFromParcel(Parcel in) {
            return new VersionInfo(in);
        }

        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };

    /**
     * Parses the incoming string to produce a VersionInfo. The string must be in the form "<major>.<minor>.<micro>".
     * trailing values are assumed to be zero if they are not provided, meaning that 2 would be interpreted as 2.0.0 and
     * 3.5 would be interpretated 3.5.0.
     * 
     * @param versionString The string to interpret
     * @return A valid VersionInfo; or null if the versionString is malformed
     */
    public static VersionInfo createVersionInfo(String versionString) {
        try {
            int minor = 0;
            int micro = 0;
            String[] values = versionString.split("\\.");
            int major = Integer.parseInt(values[0]);
            if (values.length > 1) minor = Integer.parseInt(values[1]);
            if (values.length > 2) micro = Integer.parseInt(values[2]);
            return new VersionInfo(major, minor, micro);
        } catch (NumberFormatException e) {
            Log.w(TAG, e.getMessage());
        }
        return null;
    }

    private int major = 0, minor = 0, micro = 0;

    /**
     * Creates a VersionInfo from integer values according to standard major, minor, and micro version designations.
     */
    public VersionInfo(int major, int minor, int micro) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
    }

    /**
     * Returns the major version number.
     */
    public int getMajor() {
        return major;
    }

    /**
     * Returns the minor version number.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Returns the micro version number.
     */
    public int getMicro() {
        return micro;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(major);
        dest.writeInt(minor);
        dest.writeInt(micro);
    }

    private VersionInfo(Parcel in) {
        this.major = in.readInt();
        this.minor = in.readInt();
        this.micro = in.readInt();
    }

    @Override
    public int compareTo(VersionInfo other) {
        if (this.major > other.major) return 1; else if (this.major < other.major) return -1; else {
            if (this.minor > other.minor) return 1; else if (this.minor < other.minor) return -1; else {
                if (this.micro > other.micro) return 1; else if (this.micro < other.micro) return -1; else {
                    return 0;
                }
            }
        }
    }

    @Override
    public boolean equals(Object candidate) {
        if (this == candidate) return true;
        if (candidate == null || candidate.getClass() != getClass()) return false;
        VersionInfo other = (VersionInfo) candidate;
        if (other.major == this.major && other.minor == this.minor && other.micro == this.micro) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + new Integer(major).hashCode() + new Integer(minor).hashCode() + new Integer(micro).hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Version: " + String.valueOf(major) + "." + String.valueOf(minor) + "." + String.valueOf(micro);
    }
}
