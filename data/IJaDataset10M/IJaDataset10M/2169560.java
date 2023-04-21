package chrriis.dj.nativeswing.swtimpl.demo.examples.htmleditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorListener;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorSaveEvent;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;

/**
 * @author Christopher Deckers
 */
public class SimpleHTMLEditorExample extends JPanel {

    protected static final String LS = System.getProperty("line.separator");

    public SimpleHTMLEditorExample() {
        super(new BorderLayout());
        final JHTMLEditor htmlEditor = new JHTMLEditor();
        htmlEditor.addHTMLEditorListener(new HTMLEditorListener() {

            public void saveHTML(HTMLEditorSaveEvent e) {
                JOptionPane.showMessageDialog(SimpleHTMLEditorExample.this, "The data of the HTML editor could be saved anywhere...");
            }
        });
        add(htmlEditor, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createTitledBorder("Custom Controls"));
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton setHTMLButton = new JButton("Set HTML");
        middlePanel.add(setHTMLButton);
        JButton getHTMLButton = new JButton("Get HTML");
        middlePanel.add(getHTMLButton);
        southPanel.add(middlePanel, BorderLayout.NORTH);
        final JTextArea htmlTextArea = new JTextArea();
        htmlTextArea.setText("<p style=\"text-align: center\">This is an <b>HTML editor</b>, in a <u><i>Swing</i></u> application.<br />" + LS + "<img alt=\"DJ Project Logo\" src=\"http://djproject.sourceforge.net/common/logo.png\" /><br />" + LS + "<a href=\"http://djproject.sourceforge.net/ns/\">DJ Project - Native Swing</a></p>");
        htmlTextArea.setCaretPosition(0);
        JScrollPane scrollPane = new JScrollPane(htmlTextArea);
        scrollPane.setPreferredSize(new Dimension(0, 100));
        southPanel.add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        getHTMLButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                htmlTextArea.setText(htmlEditor.getHTMLContent());
                htmlTextArea.setCaretPosition(0);
            }
        });
        setHTMLButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                htmlEditor.setHTMLContent(htmlTextArea.getText());
            }
        });
        htmlEditor.setHTMLContent(htmlTextArea.getText());
    }

    public static void main(String[] args) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame("DJ Native Swing Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.getContentPane().add(new SimpleHTMLEditorExample(), BorderLayout.CENTER);
                frame.setSize(800, 600);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
            }
        });
        NativeInterface.runEventPump();
    }
}
