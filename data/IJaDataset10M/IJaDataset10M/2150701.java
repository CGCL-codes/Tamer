package test;

import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class HtmlBrowser extends JFrame {

    JPanel contentPane;

    BorderLayout borderLayoutAll = new BorderLayout();

    JLabel jLabelPrompt = new JLabel();

    JPanel jPanelMain = new JPanel();

    BorderLayout borderLayoutMain = new BorderLayout();

    JTextField textFieldURL = new JTextField();

    JEditorPane jEditorPane = new JEditorPane();

    public HtmlBrowser() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(borderLayoutAll);
        jPanelMain.setLayout(borderLayoutMain);
        jLabelPrompt.setText("请输入URL");
        textFieldURL.setText("");
        textFieldURL.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textFieldURL_actionPerformed(e);
            }
        });
        jEditorPane.setEditable(false);
        jEditorPane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                jEditorPane_hyperlinkUpdate(e);
            }
        });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(jEditorPane);
        jPanelMain.add(textFieldURL, "North");
        jPanelMain.add(scrollPane, "Center");
        contentPane.add(jLabelPrompt, "North");
        contentPane.add(jPanelMain, "Center");
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        this.setSize(new Dimension(600, 500));
        this.setTitle("迷你IE ");
        this.setVisible(true);
    }

    void textFieldURL_actionPerformed(ActionEvent e) {
        try {
            jEditorPane.setPage(textFieldURL.getText());
        } catch (IOException ex) {
            JOptionPane msg = new JOptionPane();
            JOptionPane.showMessageDialog(this, "URL地址不正确：" + textFieldURL.getText(), "输入不正确！", 0);
        }
    }

    void jEditorPane_hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            try {
                URL url = e.getURL();
                jEditorPane.setPage(url);
                textFieldURL.setText(url.toString());
            } catch (IOException io) {
                JOptionPane msg = new JOptionPane();
                JOptionPane.showMessageDialog(this, "打开该链接失败！", "输入不正确！", 0);
            }
        }
    }

    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new HtmlBrowser();
    }
}
