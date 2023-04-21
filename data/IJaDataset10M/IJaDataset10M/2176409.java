package org.modelibra.swing.app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.modelibra.util.NatLang;
import org.modelibra.util.PathLocator;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog implements ActionListener, IConstants {

    private JButton button;

    public AboutDialog(MainFrame mainFrame) {
        super(mainFrame);
        NatLang natLang = mainFrame.getApp().getNatLang();
        String about = natLang.getText("about");
        setTitle(about);
        setResizable(false);
        JPanel northPanel = new JPanel();
        JPanel centerPanel = new JPanel();
        JPanel southPanel = new JPanel();
        JPanel westPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(northPanel, BorderLayout.NORTH);
        cp.add(southPanel, BorderLayout.SOUTH);
        cp.add(centerPanel, BorderLayout.CENTER);
        cp.add(westPanel, BorderLayout.WEST);
        cp.add(eastPanel, BorderLayout.EAST);
        JPanel imagePanel = new JPanel();
        JPanel textPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(imagePanel, BorderLayout.NORTH);
        centerPanel.add(textPanel, BorderLayout.SOUTH);
        PathLocator pathLocator = new PathLocator();
        ImageIcon imageIcon = pathLocator.getImageIcon(AboutDialog.class, MODELIBRA_IMAGE_RELATIVE_PATH);
        JLabel imageLabel;
        imageLabel = new JLabel(imageIcon);
        imagePanel.setBackground(BACKGROUND_COLOR);
        imagePanel.add(imageLabel);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(BACKGROUND_COLOR);
        JLabel label0 = new JLabel();
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        JLabel label3 = new JLabel();
        JLabel label4 = new JLabel();
        String category = natLang.getText("category");
        String product = natLang.getText("product");
        String version = natLang.getText("version");
        String date = natLang.getText("date");
        String creator = natLang.getText("creator");
        label0.setText(category);
        label1.setText(product);
        label2.setText(version);
        label3.setText(date);
        label4.setText(creator);
        textPanel.add(label0);
        textPanel.add(label1);
        textPanel.add(label2);
        textPanel.add(label3);
        textPanel.add(label4);
        southPanel.setLayout(new FlowLayout());
        String ok = natLang.getText("ok");
        button = new JButton(ok);
        southPanel.add(button);
        button.addActionListener(this);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            setVisible(false);
            dispose();
        }
    }
}
