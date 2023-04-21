package geovista.cartogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class CartogramCreate extends JPanel implements ActionListener {

    JLabel stepLabel;

    ActionListener wizard;

    JProgressBar progressBar;

    WaitPanel waitPanel;

    /**
     * CartogramCreate
     */
    public CartogramCreate(ActionListener wizard) {
        this.wizard = wizard;
        initGui();
    }

    private void initGui() {
        this.removeAll();
        stepLabel = new JLabel("Step Four: Create cartogram.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
        progressBar = new JProgressBar();
        JPanel content = new JPanel();
        JPanel content2 = new JPanel();
        content2.setLayout(new BorderLayout());
        content2.add(content, BorderLayout.NORTH);
        waitPanel = new WaitPanel();
        waitPanel.setPreferredSize(new Dimension(500, 400));
        content2.add(waitPanel, BorderLayout.CENTER);
        content2.add(progressBar, BorderLayout.SOUTH);
        this.add(content2, BorderLayout.CENTER);
        this.add(stepLabel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void addActionListener(ActionListener wizard) {
        this.wizard = wizard;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void startWaiting() {
        this.waitPanel.startWaiting();
    }

    public void stopWaiting() {
        this.waitPanel.stopWaiting();
    }
}
