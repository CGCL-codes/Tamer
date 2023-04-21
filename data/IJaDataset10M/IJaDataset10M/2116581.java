package org.tockit.docco.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import net.sourceforge.toscanaj.dbviewer.BrowserLauncher;

public class HtmlDisplayDialog {

    private static class ViewerDialog extends JDialog {

        private static final long serialVersionUID = 1L;

        class Hyperactive implements HyperlinkListener {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    JEditorPane pane = (JEditorPane) e.getSource();
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else {
                        try {
                            if (e.getURL().getProtocol().startsWith("http")) {
                                BrowserLauncher.openURL(e.getURL().toString());
                            } else {
                                pane.setPage(e.getURL());
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            }
        }

        private JEditorPane textArea;

        public ViewerDialog(Frame frame, String title) {
            super(frame, title, true);
            final JButton closeButton = new JButton(GuiMessages.getString("HtmlDisplayDialog.closeButton.label"));
            closeButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    closeDialog();
                }
            });
            getRootPane().setDefaultButton(closeButton);
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
            buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            buttonPane.add(Box.createHorizontalGlue());
            buttonPane.add(closeButton);
            this.textArea = new JEditorPane();
            this.textArea.setContentType("text/html");
            this.textArea.setEditable(false);
            this.textArea.addHyperlinkListener(new Hyperactive());
            JScrollPane scrollview = new JScrollPane();
            scrollview.getViewport().add(this.textArea);
            Container contentPane = getContentPane();
            contentPane.add(scrollview, BorderLayout.CENTER);
            contentPane.add(buttonPane, BorderLayout.SOUTH);
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    closeDialog();
                }
            });
        }

        private void closeDialog() {
            this.setVisible(false);
        }

        private void showDescription(URL fileURL) {
            if (fileURL != null) {
                HTMLDocument doc = (HTMLDocument) this.textArea.getDocument();
                String fileUrlString = fileURL.toString();
                int lastSlashUndex = fileUrlString.lastIndexOf("/");
                String baseUrlString = fileUrlString.substring(0, lastSlashUndex);
                try {
                    URL baseURL = new URL(baseUrlString);
                    doc.setBase(baseURL);
                    this.textArea.setPage(fileURL);
                } catch (MalformedURLException e) {
                    this.textArea.setText(MessageFormat.format(GuiMessages.getString("HtmlDisplayDialog.baseUrlNotFoundError.text"), new Object[] { fileURL, e.getMessage() }));
                } catch (IOException e) {
                    this.textArea.setText(MessageFormat.format(GuiMessages.getString("HtmlDisplayDialog.openUrlFailedError.text"), new Object[] { fileURL, e.getMessage() }));
                }
                return;
            }
            this.textArea.setText(GuiMessages.getString("HtmlDisplayDialog.urlAttributeNotFoundError.text"));
        }
    }

    private HtmlDisplayDialog() {
    }

    public static void show(Frame parent, String title, URL relativePathToHtmlFile, Dimension dialogSize) {
        ViewerDialog dialog = new ViewerDialog(parent, title);
        dialog.setBounds(new Rectangle(150, 150, dialogSize.width, dialogSize.height));
        dialog.showDescription(relativePathToHtmlFile);
        dialog.setVisible(true);
    }
}
