package zinger.dizzy.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.*;
import com.google.gwt.user.client.ui.*;

public class VisArt implements IsSerializable {

    public final String url;

    public final int width;

    public final int height;

    public final String caption;

    public final String callbackArg;

    /** @deprecated Only for GWT deserializer */
    public VisArt() {
        this(null, 0, 0, null, null);
    }

    public VisArt(final String url, final int width, final int height, final String caption, final String callbackArg) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.caption = caption;
        this.callbackArg = callbackArg;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof VisArt)) return false;
        if (obj == this) return true;
        final VisArt other = (VisArt) obj;
        return this.url.equals(other.url) && this.width == other.width && this.height == other.height && Util.equalOrNull(this.caption, other.caption) && Util.equalOrNull(this.callbackArg, other.callbackArg);
    }

    public String toString() {
        return "(" + this.url + ", " + this.width + ", " + this.height + ", " + this.caption + ", " + this.callbackArg + ")";
    }
}
