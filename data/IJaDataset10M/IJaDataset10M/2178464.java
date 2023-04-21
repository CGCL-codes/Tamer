package net.jankenpoi.sudokuki.ui.swing;

import static net.jankenpoi.i18n.I18n._;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import net.jankenpoi.sudokuki.view.GridView;

/**
 * CheckUpdateDialog.java
 * 
 * @author svedrenne
 */
@SuppressWarnings("serial")
public class GridGenerationDialog extends JDialog {

    private JFrame parent;

    private final GridView view;

    private final SwingWorker<Integer, Void> worker;

    public GridGenerationDialog(JFrame parent, final GridView view) {
        super(parent, true);
        this.parent = parent;
        this.view = view;
        setResizable(false);
        short[] flagsTable = new short[81];
        for (int li = 0; li < 9; li++) {
            for (int co = 0; co < 9; co++) {
                if (view.isCellReadOnly(li, co)) {
                    flagsTable[9 * li + co] = view.getValueAt(li, co);
                }
            }
        }
        worker = new SwingWorker<Integer, Void>() {

            @Override
            protected Integer doInBackground() {
                return generateGrid();
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return;
                } finally {
                    dispose();
                }
            }
        };
        initComponents();
        worker.execute();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        Container pane = getContentPane();
        GridLayout layout = new GridLayout(3, 1);
        pane.setLayout(layout);
        JLabel messageLbl1 = new JLabel("<html>" + "<table border=\"0\">" + "<tr>" + _("Generating grid...") + "</tr>" + "</html>");
        JLabel messageLbl3 = new JLabel("");
        JButton cancelBtn = new JButton(_("Please wait..."));
        cancelBtn.setEnabled(false);
        cancelBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clickedCancel();
            }
        });
        pane.add(messageLbl1);
        pane.add(messageLbl3);
        FlowLayout btnLayout = new FlowLayout(1);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(btnLayout);
        btnPanel.add(cancelBtn);
        pane.add(btnPanel);
        pack();
        setLocationRelativeTo(parent);
    }

    private void clickedCancel() {
        dispose();
    }

    private int generateGrid() {
        view.getController().notifyNewGridRequested();
        return 0;
    }
}
