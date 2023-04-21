package com.google.collabframework.texteditor.client;

import com.google.collabframework.texteditor.server.ILoginServer;
import com.google.collabframework.texteditor.server.ITextServer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author robert
 */
public class TextEditor {

    private JFrame frame;

    private JTextArea textArea;

    private ITextServer textServer;

    private ILoginServer loginServer;

    public TextEditor() {
        try {
            loginServer = (ILoginServer) Naming.lookup("//localhost/Login");
            textServer = loginServer.registerClient();
            String text = textServer.getText();
            frame = new JFrame();
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            textArea = new JTextArea(text);
            textArea.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        textServer.handleData(e);
                    } catch (RemoteException ex) {
                        Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            frame.getContentPane().add(textArea);
            frame.setVisible(true);
            checkUpdates();
        } catch (NotBoundException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        new TextEditor();
    }

    private void checkUpdates() {
        while (true) {
            try {
                String newText = textServer.getText();
                int ca = textArea.getCaretPosition();
                textArea.setText(newText);
                textArea.setCaretPosition(ca);
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(TextEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
