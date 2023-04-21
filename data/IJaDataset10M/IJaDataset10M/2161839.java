package com.leclercb.taskunifier.gui.components.taskedit;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.error.ErrorInfo;
import com.leclercb.taskunifier.api.models.Task;
import com.leclercb.taskunifier.gui.main.MainFrame;
import com.leclercb.taskunifier.gui.swing.TUDialog;
import com.leclercb.taskunifier.gui.swing.buttons.TUButtonsPanel;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.swing.buttons.TUOkButton;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class BatchTaskEditDialog extends TUDialog {

    private static BatchTaskEditDialog INSTANCE;

    public static BatchTaskEditDialog getInstance() {
        if (INSTANCE == null) INSTANCE = new BatchTaskEditDialog();
        return INSTANCE;
    }

    private JXHeader header;

    private BatchTaskEditPanel batchTaskEditPanel;

    private boolean cancelled;

    private BatchTaskEditDialog() {
        super(MainFrame.getInstance().getFrame());
        this.initialize();
    }

    public Task[] getTasks() {
        return this.batchTaskEditPanel.getTasks();
    }

    public void setTasks(Task[] tasks) {
        if (tasks != null && tasks.length == 1) {
            this.header.setTitle(Translations.getString("header.title.edit_task"));
            this.header.setDescription(Translations.getString("header.description.edit_task"));
        } else {
            this.header.setTitle(Translations.getString("header.title.batch_edit_task"));
            this.header.setDescription(Translations.getString("header.description.batch_edit_task"));
        }
        this.batchTaskEditPanel.setTasks(tasks);
    }

    @Override
    public void setVisible(boolean b) {
        if (b) this.cancelled = false;
        super.setVisible(b);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    private void initialize() {
        this.setModal(true);
        this.setTitle(Translations.getString("batch_task_edit"));
        this.setSize(900, 700);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        if (this.getOwner() != null) this.setLocationRelativeTo(this.getOwner());
        this.loadWindowSettings("window.task_edit");
        this.header = new JXHeader();
        this.header.setTitle(Translations.getString("header.title.batch_edit_task"));
        this.header.setDescription(Translations.getString("header.description.batch_edit_task"));
        this.header.setIcon(ImageUtils.getResourceImage("edit.png", 32, 32));
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                BatchTaskEditDialog.this.cancelled = true;
                BatchTaskEditDialog.this.setTasks(null);
                BatchTaskEditDialog.this.setVisible(false);
            }
        });
        this.batchTaskEditPanel = new BatchTaskEditPanel();
        this.batchTaskEditPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        this.add(this.header, BorderLayout.NORTH);
        this.add(this.batchTaskEditPanel, BorderLayout.CENTER);
        this.initializeButtonsPanel();
    }

    private void initializeButtonsPanel() {
        ActionListener okListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                if (!BatchTaskEditDialog.this.batchTaskEditPanel.editTasks()) {
                    ErrorInfo info = new ErrorInfo(Translations.getString("general.error"), Translations.getString("general.synchronization_ongoing"), null, null, null, null, null);
                    JXErrorPane.showDialog(MainFrame.getInstance().getFrame(), info);
                    return;
                }
                BatchTaskEditDialog.this.cancelled = false;
                BatchTaskEditDialog.this.setTasks(null);
                BatchTaskEditDialog.this.setVisible(false);
            }
        };
        ActionListener cancelListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                BatchTaskEditDialog.this.cancelled = true;
                BatchTaskEditDialog.this.setTasks(null);
                BatchTaskEditDialog.this.setVisible(false);
            }
        };
        JButton okButton = new TUOkButton(okListener);
        JButton cancelButton = new TUCancelButton(cancelListener);
        JPanel panel = new TUButtonsPanel(okButton, cancelButton);
        this.add(panel, BorderLayout.SOUTH);
        this.getRootPane().setDefaultButton(okButton);
        this.getRootPane().registerKeyboardAction(okListener, KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_IN_FOCUSED_WINDOW);
        this.getRootPane().registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    }
}
