package net.simpleframework.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import net.simpleframework.util.LangUtils;
import net.simpleframework.util.LocaleI18n;
import org.jdesktop.swingx.JXButton;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
@SuppressWarnings("serial")
public abstract class OkCancelDialog extends EnhancedDialog implements ActionListener {

    private boolean ok;

    protected JButton okButton;

    protected JButton cancelButton;

    protected JButton helpButton;

    public OkCancelDialog(final Window owner, final String title, final Object... element) {
        super(owner, title, element);
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        final Object source = evt.getSource();
        if (source == okButton) {
            try {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                ok();
            } catch (final Exception ex) {
                SwingUtils.showError(this, ex);
            } finally {
                setCursor(Cursor.getDefaultCursor());
            }
        } else if (source == cancelButton) {
            cancel();
        } else if (source == helpButton) {
            help();
        }
    }

    @Override
    protected void cancel() {
        ok = false;
        super.cancel();
    }

    protected JButton[] getBottomButtons() {
        return null;
    }

    protected abstract Component createContentUI();

    @Override
    protected void createUI() {
        setEnterOk(true);
        ok = false;
        final JPanel panel = new JPanel(new BorderLayout());
        okButton = new JXButton(LocaleI18n.getMessage("OkCancelDialog.0"));
        okButton.setMnemonic('O');
        cancelButton = new JXButton(LocaleI18n.getMessage("OkCancelDialog.1"));
        cancelButton.setMnemonic('C');
        helpButton = new JXButton(LocaleI18n.getMessage("OkCancelDialog.2"));
        helpButton.setMnemonic('H');
        final JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 8));
        final JButton[] bottomButtons = getBottomButtons();
        if (bottomButtons != null) {
            for (final JButton bottomButton : bottomButtons) {
                bp.add(bottomButton);
            }
        }
        if (showOk()) {
            bp.add(okButton);
        }
        bp.add(cancelButton);
        if (showHelp()) {
            bp.add(Box.createHorizontalStrut(2));
            bp.add(helpButton);
        }
        panel.setBorder(getBorder());
        final Component content = createContentUI();
        if (content != null) {
            panel.add(content, BorderLayout.CENTER);
        }
        panel.add(bp, BorderLayout.SOUTH);
        getContentPane().add(panel);
        Object[] btns = new JButton[] { okButton, cancelButton, helpButton };
        if ((bottomButtons != null) && (bottomButtons.length > 0)) {
            btns = LangUtils.add(btns, bottomButtons);
        }
        SwingUtils.setJButtonSizesTheSame((JButton[]) btns);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        helpButton.addActionListener(this);
    }

    protected Border getBorder() {
        return BorderFactory.createEmptyBorder();
    }

    protected void help() {
    }

    public boolean isOk() {
        return ok;
    }

    @Override
    public void ok() {
        ok = true;
        dispose();
    }

    protected boolean showOk() {
        return true;
    }

    protected boolean showHelp() {
        return false;
    }
}
