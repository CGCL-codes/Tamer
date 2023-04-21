package gruntspud.ui;

import gruntspud.CVSFileTypeUtil;
import gruntspud.filter.CVSFileStatusFilter;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.netbeans.lib.cvsclient.file.FileStatus;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 */
public class CVSFileStatusFilterPane extends JPanel {

    private static final FileStatus[] ALL_FILE_STATUS = { FileStatus.UP_TO_DATE, FileStatus.MODIFIED, FileStatus.ADDED, FileStatus.REMOVED, FileStatus.HAS_CONFLICTS, FileStatus.UNKNOWN, FileStatus.NEEDS_CHECKOUT, FileStatus.NEEDS_MERGE, FileStatus.NEEDS_PATCH };

    private GruntspudCheckBox[] filters;

    private CVSFileStatusFilter filter;

    /**
     * Creates a new CVSFileStatusFilterPane object.
     */
    public CVSFileStatusFilterPane() {
        super(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 1, 8);
        filters = new GruntspudCheckBox[ALL_FILE_STATUS.length];
        for (int i = 0; i < ALL_FILE_STATUS.length; i++) {
            gbc.weightx = 0.0;
            Icon icon = CVSFileTypeUtil.getIconForStatus(ALL_FILE_STATUS[i]);
            UIUtil.jGridBagAdd(this, new JLabel(icon), gbc, GridBagConstraints.RELATIVE);
            gbc.weightx = 1.0;
            if ((i + 1) == ALL_FILE_STATUS.length) {
                gbc.weighty = 1.0;
            }
            UIUtil.jGridBagAdd(this, filters[i] = new GruntspudCheckBox((ALL_FILE_STATUS[i] == null) ? "Unknown" : ALL_FILE_STATUS[i].toString()), gbc, GridBagConstraints.REMAINDER);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param enabled DOCUMENT ME!
     */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0; i < ALL_FILE_STATUS.length; i++) {
            filters[i].setEnabled(enabled);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void setCVSFileStatusFilter(CVSFileStatusFilter filter) {
        this.filter = filter;
        for (int i = 0; i < ALL_FILE_STATUS.length; i++) {
            filters[i].setSelected((filter == null) || !filter.isFiltered(ALL_FILE_STATUS[i]));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CVSFileStatusFilter getCVSFileStatusFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     */
    public void apply() {
        filter.clearFilters();
        for (int i = 0; i < ALL_FILE_STATUS.length; i++) {
            if (!filters[i].isSelected()) {
                filter.addStatusToIgnore(ALL_FILE_STATUS[i]);
            }
        }
    }
}
