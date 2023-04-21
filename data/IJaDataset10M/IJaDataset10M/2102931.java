package net.community.apps.tools.svn.wc;

import java.io.File;
import java.util.Map;
import javax.swing.Icon;
import net.community.apps.tools.svn.SVNBaseMain;
import net.community.chest.svnkit.core.wc.SVNStatusTypeEnum;
import net.community.chest.ui.components.table.file.FileAttrsCellRenderer;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Oct 27, 2010 2:50:44 PM
 *
 */
public class WCFileAttrsCellRenderer extends FileAttrsCellRenderer {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3205244311446920023L;

    private final Map<SVNStatusTypeEnum, ? extends Icon> _iconsMap;

    public WCFileAttrsCellRenderer(Map<SVNStatusTypeEnum, ? extends Icon> iconsMap) {
        _iconsMap = iconsMap;
    }

    protected Icon getPropertiesIcon(final SVNStatusTypeEnum stValue) {
        return ((_iconsMap == null) || (stValue == null)) ? null : _iconsMap.get(stValue);
    }

    protected Icon getContentIcon(final SVNStatusTypeEnum stValue) {
        return ((_iconsMap == null) || (stValue == null)) ? null : _iconsMap.get(stValue);
    }

    private static boolean isModifiedStatus(SVNStatusTypeEnum st) {
        return (st != null) && (!SVNStatusTypeEnum.STATUS_NORMAL.equals(st));
    }

    @Override
    protected Icon getIcon(File f) {
        try {
            final SVNClientManager mgr = SVNBaseMain.getSVNClientManager(true);
            final SVNStatusClient sc = mgr.getStatusClient();
            final SVNStatus st = sc.doStatus(f, false);
            final SVNStatusType ctxStatus = st.getContentsStatus();
            final SVNStatusTypeEnum ctxEnum = SVNStatusTypeEnum.fromStatus(ctxStatus);
            if (isModifiedStatus(ctxEnum)) return getContentIcon(ctxEnum);
            final SVNStatusType prpStatus = st.getPropertiesStatus();
            final SVNStatusTypeEnum prpEnum = SVNStatusTypeEnum.fromStatus(prpStatus);
            if (isModifiedStatus(prpEnum)) return getPropertiesIcon(prpEnum);
            return getContentIcon(SVNStatusTypeEnum.STATUS_NORMAL);
        } catch (SVNException e) {
            return null;
        }
    }
}
