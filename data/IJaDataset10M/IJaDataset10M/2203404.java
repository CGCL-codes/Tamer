package geovista.cartogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CartogramOutputFile extends JPanel implements ActionListener {

    JLabel stepLabel;

    JTextField outputFileNameTextField;

    JButton outputFileNameButton;

    ActionListener wizard;

    String outputFileName;

    public CartogramOutputFile(ActionListener wizard, String outputFileName) {
        this.wizard = wizard;
        initGui();
        this.outputFileNameTextField.setText(outputFileName);
    }

    private void initGui() {
        this.removeAll();
        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(300, 350));
        JPanel stuffPanel = new JPanel();
        BorderLayout stuffBorder = new BorderLayout();
        stuffPanel.setLayout(stuffBorder);
        stepLabel = new JLabel("Step three: Choose output file location.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
        JLabel outputFileNameLabel = new JLabel();
        outputFileNameButton = new JButton();
        outputFileNameButton.addActionListener(this);
        outputFileNameTextField = new JTextField(this.outputFileName);
        outputFileNameTextField.setPreferredSize(new Dimension(400, 25));
        outputFileNameLabel.setText("Output File Name:");
        JPanel content = GuiUtils.createOutputPanel(outputFileNameLabel, outputFileNameButton, outputFileNameTextField, "Pick Output Shapefile Name");
        stuffPanel.add(content, BorderLayout.CENTER);
        stuffPanel.add(stepLabel, BorderLayout.SOUTH);
        this.add(spacePanel, BorderLayout.CENTER);
        this.add(stuffPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.outputFileNameButton) {
            String fileName = GuiUtils.chooseOutputFilename(this);
            if (fileName != null) {
                this.outputFileNameTextField.setText(fileName);
                ActionEvent e2 = new ActionEvent(this, 0, fileName);
                wizard.actionPerformed(e2);
            }
        }
    }

    public void setFileName(String fileName) {
        this.outputFileNameTextField.setText(fileName);
    }

    public void addActionListener(ActionListener wizard) {
        this.wizard = wizard;
    }
}
