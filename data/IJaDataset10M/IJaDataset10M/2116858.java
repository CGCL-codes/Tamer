package org.blackchat;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import org.imme.Contact;
import org.imme.util.Log;

public class TitleStatusField extends Field {

    private static final Log log = Log.getInstance("blackchat");

    private Options _options = null;

    private String _clientSkin = Skin.getDefault();

    private long _clientStatus = Contact.STATUS_OFFLINE;

    public TitleStatusField() {
    }

    public void setOptions(Options options) {
        _options = options;
        _clientSkin = _options.skin;
    }

    public void setClientStatus(long clientStatus) {
        _clientStatus = clientStatus;
    }

    public long getClientStatus() {
        return _clientStatus;
    }

    public int getPreferredWidth() {
        return Graphics.getScreenWidth();
    }

    public int getPreferredHeight() {
        return 18;
    }

    protected void layout(int width, int height) {
        width = Math.min(width, getPreferredWidth());
        height = Math.min(height, getPreferredHeight());
        setExtent(width, height);
    }

    protected void paint(Graphics g) {
        int width = g.drawText("BlackChat - " + (_options == null ? "" : _options.user), 0, 4);
        Bitmap curImage = Skin.getStatusIcon(_clientStatus, _clientSkin);
        if (curImage != null) g.drawBitmap(width + 10, 1, 16, 18, curImage, 0, 0);
    }
}
