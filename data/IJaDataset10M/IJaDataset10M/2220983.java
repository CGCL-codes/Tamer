package ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import ui.Messages;
import ui.command.CommandExecutor;
import ui.command.IO.SaveClientWorkstationCommand;
import ui.panel.WorkstationPanel;
import base.network.Workstation;

@SuppressWarnings("serial")
public class WorkstationDialog extends JDialog {

    private static final transient Logger logger = Logger.getLogger(WorkstationDialog.class.getName());

    private JPanel contentPanel;

    private JPanel buttonPanel;

    private JButton saveWorkstationButton;

    private JButton clearWorkstationDataButton;

    private WorkstationPanel workstationPanel;

    private Workstation workstation;

    private boolean createWorkstation = false;

    public WorkstationDialog() {
        createWorkstation = true;
        initialize();
    }

    public WorkstationDialog(Workstation workstation) {
        this.workstation = workstation;
        if (workstation == null) createWorkstation = true;
        initialize();
    }

    protected void initialize() {
        this.setResizable(false);
        this.setSize(550, 600);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setContentPane(getContentPanel());
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
    }

    /**
	 * @return Returns the contentPanel.
	 */
    protected JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            contentPanel.setLayout(new BorderLayout());
            contentPanel.add(getWorkstationPanel(), BorderLayout.CENTER);
            contentPanel.add(getButtonPanel(), BorderLayout.SOUTH);
        }
        return contentPanel;
    }

    /**
	 * @return Returns the buttonPanel.
	 */
    protected JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            TitledBorder titledBorder = new TitledBorder(Messages.getString("dialog.availableactions"));
            buttonPanel.setBorder(titledBorder);
            buttonPanel.setLayout(new GridLayout(1, 4));
            buttonPanel.add(new JLabel(""));
            buttonPanel.add(getSaveWorkstationButton());
            buttonPanel.add(getClearWorkstationDataButton());
            buttonPanel.add(new JLabel(""));
        }
        return buttonPanel;
    }

    /**
	 * @return Returns the workstationPanel.
	 */
    protected WorkstationPanel getWorkstationPanel() {
        if (workstationPanel == null) {
            workstationPanel = new WorkstationPanel(workstation);
        }
        return workstationPanel;
    }

    /**
	 * @return Returns the clearWorkstationDataButton.
	 */
    protected JButton getClearWorkstationDataButton() {
        if (clearWorkstationDataButton == null) {
            clearWorkstationDataButton = new JButton(Messages.getString("button.clear"));
            clearWorkstationDataButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/actions/view-refresh.png")));
            clearWorkstationDataButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed clearWorkstationDataButton");
                    getWorkstationPanel().clearWorkstationData();
                }
            });
        }
        return clearWorkstationDataButton;
    }

    /**
	 * @return Returns the saveWorkstationButton.
	 */
    protected JButton getSaveWorkstationButton() {
        if (saveWorkstationButton == null) {
            saveWorkstationButton = new JButton(Messages.getString("button.save"));
            saveWorkstationButton.setIcon(new ImageIcon(this.getClass().getResource("/icon/16x16/actions/document-save.png")));
            saveWorkstationButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    logger.debug("actionPerformed saveWorkstationButton");
                    SaveClientWorkstationCommand saveWorkstationCommand = new SaveClientWorkstationCommand(getWorkstationPanel(), workstation, createWorkstation);
                    CommandExecutor.getInstance().executeCommand(saveWorkstationCommand, true);
                    if (saveWorkstationCommand.getStatus() == SaveClientWorkstationCommand.SUCCESS_STATUS) setVisible(false);
                }
            });
        }
        return saveWorkstationButton;
    }
}
