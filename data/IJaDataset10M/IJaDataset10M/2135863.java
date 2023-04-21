package de.objectcode.time4u.server.web.admin;

import java.io.Serializable;
import de.objectcode.time4u.server.api.local.ForeignServer;

public class ForeignServerRowBean implements Serializable {

    private static final long serialVersionUID = -54009840861137865L;

    private boolean m_checked;

    private ForeignServer m_foreignServer;

    public ForeignServerRowBean(ForeignServer foreignServer) {
        m_foreignServer = foreignServer;
    }

    public boolean isChecked() {
        return m_checked;
    }

    public void setChecked(boolean checked) {
        m_checked = checked;
    }

    public ForeignServer getForeignServer() {
        return m_foreignServer;
    }
}
