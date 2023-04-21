package net.mlw.vlh.swing.support;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import net.mlw.vlh.PagingInfo;
import net.mlw.vlh.swing.PagingComponent;
import net.mlw.vlh.swing.ValueListHelper;

/**
 * @author Matthew L. Wilson, Andrej Zachar
 * @version $Revision: 1.6 $ $Date: 2006/04/18 17:15:24 $
 */
public class DefaultPagingPanel extends JPanel implements PagingComponent {

    private ActionListener actionListener;

    private PagingInfo pagingInfo = null;

    private JLabel description = new JLabel();

    private ResourceBundle resourceBundle;

    private JButton first;

    private JButton previous;

    private JButton next;

    private JButton last;

    private String pagingLabel = null;

    public DefaultPagingPanel() {
        first = new JButton(new ArrowIcon(SwingConstants.LEFT, 2));
        first.setActionCommand(ValueListHelper.ACTION_COMMAND_FIRST);
        first.setEnabled(false);
        previous = new JButton(new ArrowIcon(SwingConstants.LEFT));
        previous.setActionCommand(ValueListHelper.ACTION_COMMAND_PREVIOUS);
        previous.setEnabled(false);
        next = new JButton(new ArrowIcon(SwingConstants.RIGHT));
        next.setActionCommand(ValueListHelper.ACTION_COMMAND_NEXT);
        next.setEnabled(false);
        last = new JButton(new ArrowIcon(SwingConstants.RIGHT, 2));
        last.setActionCommand(ValueListHelper.ACTION_COMMAND_LAST);
        last.setEnabled(false);
        init();
    }

    protected void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        add(description, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.1;
        gbc.anchor = GridBagConstraints.CENTER;
        add(first);
        gbc.gridx = 2;
        add(previous);
        gbc.gridx = 3;
        add(next);
        gbc.gridx = 4;
        add(last);
    }

    public void setPagingInfo(PagingInfo pagingInfo) {
        this.pagingInfo = pagingInfo;
        int numberOfPages = pagingInfo.getTotalNumberOfEntries() / pagingInfo.getPagingNumberPer() + 1;
        Object[] messageParameters = new Object[] { new Integer(pagingInfo.getPagingPage()), new Integer(pagingInfo.getTotalNumberOfPages()), new Integer(pagingInfo.getTotalNumberOfEntries()) };
        if (pagingLabel == null) {
            description.setText(MessageFormat.format(resourceBundle.getString("paging.text.pageFromTotal"), messageParameters));
        } else {
            description.setText(MessageFormat.format(pagingLabel, messageParameters));
        }
        first.setEnabled(pagingInfo.getPagingPage() > 1);
        previous.setEnabled(pagingInfo.getPagingPage() > 1);
        next.setEnabled(pagingInfo.getPagingPage() < pagingInfo.getTotalNumberOfPages());
        last.setEnabled(pagingInfo.getPagingPage() < pagingInfo.getTotalNumberOfPages());
    }

    /**
    * @param actionListener
    *            The actionListener to set.
    */
    public void addActionListener(ActionListener actionListener) {
        first.addActionListener(actionListener);
        previous.addActionListener(actionListener);
        next.addActionListener(actionListener);
        last.addActionListener(actionListener);
    }

    /**
    * @return Returns the resourceBundle.
    * If no resourceBundle is defined, it uses the classicLook.properties file. Thus it should be located in the classpath.
    */
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle("swingLook");
        }
        return resourceBundle;
    }

    /**
    * @param resourceBundle
    *            The resourceBundle to set.
    */
    public void setResourceBundle(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }

    public void setPagingLabel(String pagingLabel) {
        this.pagingLabel = pagingLabel;
    }
}
