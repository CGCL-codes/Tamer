package org.fudaa.ctulu.gui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JDialog;
import com.memoire.bu.BuFileFilter;
import com.memoire.bu.BuResource;
import org.fudaa.ctulu.CtuluImageExport;
import org.fudaa.ctulu.CtuluLibString;

/**
 * @author Fred Deniger
 * @version $Id: CtuluImageFileChooser.java,v 1.3 2006-09-19 14:36:55 deniger Exp $
 */
public class CtuluImageFileChooser extends CtuluFileChooser {

    transient Map filterFormat_;

    public CtuluImageFileChooser() {
        this(false);
    }

    public CtuluImageFileChooser(final boolean _open) {
        super(true);
        super.setDialogType(SAVE_DIALOG);
        setFileHidingEnabled(false);
        setAcceptAllFileFilterUsed(false);
        final BuFileFilter[] f = new BuFileFilter[CtuluImageExport.FORMAT_LIST.size()];
        filterFormat_ = new HashMap(f.length);
        int pngIDx = 0;
        BuFileFilter all = null;
        if (_open) {
            final String[] filter = (String[]) CtuluImageExport.FORMAT_LIST.toArray(new String[CtuluImageExport.FORMAT_LIST.size()]);
            all = new BuFileFilter(filter, BuResource.BU.getString("Image"));
            addChoosableFileFilter(all);
        }
        for (int i = 0; i < f.length; i++) {
            final String fmt = (String) CtuluImageExport.FORMAT_LIST.get(i);
            if ("png".equals(fmt)) {
                pngIDx = i;
            }
            f[i] = new BuFileFilter(fmt, BuResource.BU.getString("Image") + CtuluLibString.ESPACE + fmt);
            filterFormat_.put(f[i], fmt);
            addChoosableFileFilter(f[i]);
        }
        if (_open) {
            setFileFilter(all);
        } else {
            setFileFilter(f[pngIDx]);
        }
    }

    public String getSelectedFormat() {
        return (String) filterFormat_.get(getFileFilter());
    }

    protected JDialog createDialog(final Component _parent) {
        return super.createDialog(_parent);
    }
}
