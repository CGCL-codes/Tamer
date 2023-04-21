package edu.vub.at.android.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcel;
import android.os.Parcelable;
import edu.vub.at.util.logging.Logging;

public class IATSettings {

    public static final File _ENV_AT_HOME_ = new File(Constants._ENV_AT_BASE_, Constants._AT_HOME_RELATIVE_PATH_);

    public static final File _ENV_AT_INIT_ = new File(_ENV_AT_HOME_, "/at/init/init.at");

    public static String getMyIp() {
        Set<String> eligible = eligibleIpAddresses();
        if (eligible.size() > 1) {
            eligible.remove("10.0.2.15");
            return eligible.iterator().next();
        } else if (eligible.size() == 1) {
            return eligible.iterator().next();
        } else {
            Logging.Network_LOG.warn("Using local IP address, no external objects will be discovered");
            return "127.0.0.1";
        }
    }

    public static Set<String> eligibleIpAddresses() {
        Set<String> eligible = new HashSet<String>();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress addr = address.nextElement();
                    if (!addr.isLoopbackAddress() && !(addr.getHostAddress().indexOf(":") > -1)) {
                        eligible.add(addr.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {
        }
        return eligible;
    }

    public static IATOptions getDefaultIATOptions() {
        return new IATOptions();
    }

    public static IATOptions getIATOptions(Activity a) {
        SharedPreferences preferences = a.getSharedPreferences(Constants.IAT_SETTINGS_FILE, Context.MODE_PRIVATE);
        IATOptions opt = IATSettings.getDefaultIATOptions();
        opt.merge(preferences);
        return opt;
    }

    public static class IATOptions implements Parcelable {

        public String ipAddress_;

        public String networkName_;

        public String AT_HOME_;

        public String AT_INIT_;

        public String logFilePath_;

        IATOptions() {
            ipAddress_ = getMyIp();
            networkName_ = "AmbientTalk";
            AT_HOME_ = _ENV_AT_HOME_.toString();
            AT_INIT_ = _ENV_AT_INIT_.toString();
            logFilePath_ = _ENV_AT_HOME_ + "/at.log";
        }

        IATOptions(String ipAddress, String networkName, String atHome, String atInit) {
            ipAddress_ = ipAddress;
            networkName_ = networkName;
            AT_HOME_ = atHome;
            AT_INIT_ = atInit;
        }

        public void merge(SharedPreferences p) {
            ipAddress_ = p.getString("ipAddress", ipAddress_);
            networkName_ = p.getString("networkName", networkName_);
            AT_HOME_ = p.getString("AT_HOME", AT_HOME_);
            AT_INIT_ = p.getString("AT_INIT", AT_INIT_);
            logFilePath_ = p.getString("logFilePath", logFilePath_);
        }

        public void writeToPreferences(Editor ep) {
            ep.putString("ipAddress", ipAddress_);
            ep.putString("networkName", networkName_);
            ep.putString("AT_HOME", AT_HOME_);
            ep.putString("AT_INIT", AT_INIT_);
            ep.putString("logFilePath", logFilePath_);
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(ipAddress_);
            dest.writeString(networkName_);
            dest.writeString(AT_HOME_);
            dest.writeString(AT_INIT_);
            dest.writeString(logFilePath_);
        }

        private IATOptions(Parcel in) {
            ipAddress_ = in.readString();
            networkName_ = in.readString();
            AT_HOME_ = in.readString();
            AT_INIT_ = in.readString();
            logFilePath_ = in.readString();
        }

        public static final Parcelable.Creator<IATOptions> CREATOR = new Parcelable.Creator<IATOptions>() {

            public IATOptions createFromParcel(Parcel in) {
                return new IATOptions(in);
            }

            public IATOptions[] newArray(int size) {
                return new IATOptions[size];
            }
        };
    }
}
