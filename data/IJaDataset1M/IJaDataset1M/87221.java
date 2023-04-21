package net.sourceforge.omov.app.gui.movie;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import net.sourceforge.jpotpourri.jpotface.IPtEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.jpotface.PtEscapeDisposer;
import net.sourceforge.omov.guicore.GuiActionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class AbstractAddEditDialog<T> extends JDialog implements IPtEscapeDisposeReceiver {

    private static final long serialVersionUID = 2776145376462694019L;

    private static final Log LOG = LogFactory.getLog(AbstractAddEditDialog.class);

    private T editItem;

    private final boolean isAddMode;

    private boolean actionConfirmed = false;

    public AbstractAddEditDialog(JFrame owner, T editObject) {
        super(owner, true);
        this.editItem = editObject;
        this.isAddMode = editObject == null;
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        PtEscapeDisposer.enableEscape(this.getRootPane(), this);
        this.setResizable(false);
    }

    protected final JPanel newCommandPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        final JButton btnCancel = new JButton("Cancel");
        final JButton btnConfirm = new JButton(isAddMode() ? "Create" : "Update");
        this.rootPane.setDefaultButton(btnConfirm);
        btnCancel.setOpaque(false);
        btnConfirm.setOpaque(false);
        btnCancel.addActionListener(new GuiActionListener() {

            @Override
            public void action(ActionEvent e) {
                doCancel();
            }
        });
        btnConfirm.addActionListener(new GuiActionListener() {

            @Override
            public void action(ActionEvent e) {
                doConfirm();
            }
        });
        panel.add(btnCancel);
        panel.add(btnConfirm);
        return panel;
    }

    protected abstract T _getConfirmedObject();

    public final T getConfirmedObject() {
        assert (this.isActionConfirmed() == true);
        final T confirmedObject = this._getConfirmedObject();
        LOG.debug("Returning confirmed object: " + confirmedObject);
        return confirmedObject;
    }

    protected final boolean isAddMode() {
        return this.isAddMode;
    }

    protected T getEditItem() {
        assert (this.isAddMode == false);
        return this.editItem;
    }

    protected void setEditItem(T editItem) {
        LOG.debug("setting edit item to: " + editItem);
        this.editItem = editItem;
    }

    protected final void doConfirm() {
        LOG.debug("doConfirm()");
        this.actionConfirmed = true;
        this.dispose();
    }

    /**
     * used by prev/next buttons in movie dialog
     */
    protected final void doConfirmWithoutDispose() {
        LOG.debug("doConfirmWithoutDispose()");
        this.actionConfirmed = true;
    }

    public final boolean isActionConfirmed() {
        return this.actionConfirmed;
    }

    protected final void doCancel() {
        LOG.debug("doCancel()");
        this.dispose();
    }

    public void doEscape() {
        LOG.debug("doEscape()");
        this.doCancel();
    }
}
