package tools.AutomaticAudio;

import core.Card;
import gui.WordsListPanel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.plaf.ProgressBarUI;

/**
 *
 * @author  reza
 */
public class main extends javax.swing.JFrame {

    int max;

    WordsListPanel WLP;

    File f;

    /** Creates new form main */
    public main(WordsListPanel WLP, File f) {
        initComponents();
        max = WLP.jTable1.getSelectedRowCount();
        if (max == 0) {
            JOptionPane.showMessageDialog(this, "You must select some cards in the List.", "Error", JOptionPane.WARNING_MESSAGE);
            jButton1.setEnabled(false);
        } else jButton1.setEnabled(true);
        this.WLP = WLP;
        this.f = f;
        jProgressBar1.setMinimum(0);
        jProgressBar1.setMaximum(max);
    }

    private void initComponents() {
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(20, 20, 20).add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 247, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap(109, Short.MAX_VALUE).add(jButton1).add(111, 111, 111)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(14, 14, 14).add(jButton1).addContainerGap(31, Short.MAX_VALUE)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jButton1.setEnabled(false);
        for (int i = 0; i < max; i++) {
            Card crd = WLP.getSelectedCard(WLP.jTable1.getSelectedRows()[i]);
            String s, s2;
            s = "";
            s2 = "";
            try {
                URL url = new URL("http://www.m-w.com/dictionary/" + crd.getWord());
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    s = s + str;
                }
                in.close();
            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }
            Pattern pattern = Pattern.compile("popWin\\('/cgi-bin/(.+?)'", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String newurl = "http://m-w.com/cgi-bin/" + matcher.group(1);
                try {
                    URL url2 = new URL(newurl);
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(url2.openStream()));
                    String str;
                    while ((str = in2.readLine()) != null) {
                        s2 = s2 + str;
                    }
                    in2.close();
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
                Pattern pattern2 = Pattern.compile("<A HREF=\"http://(.+?)\">Click here to listen with your default audio player", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher matcher2 = pattern2.matcher(s2);
                if (matcher2.find()) {
                    getWave("http://" + matcher2.group(1), crd.getWord());
                }
                int val = jProgressBar1.getValue();
                val++;
                jProgressBar1.setValue(val);
                this.paintAll(this.getGraphics());
            }
        }
        jButton1.setEnabled(true);
    }

    private boolean getWave(String url, String Word) {
        try {
            File FF = new File(f.getParent() + "/" + f.getName() + "pron");
            FF.mkdir();
            URL url2 = new URL(url);
            BufferedReader stream = new BufferedReader(new InputStreamReader(url2.openStream()));
            File Fdel = new File(f.getParent() + "/" + f.getName() + "pron/" + Word + ".wav");
            if (!Fdel.exists()) {
                FileOutputStream outstream = new FileOutputStream(f.getParent() + "/" + f.getName() + "pron/" + Word + ".wav");
                BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(outstream));
                char[] binput = new char[1024];
                int len = stream.read(binput, 0, 1024);
                while (len > 0) {
                    bwriter.write(binput, 0, len);
                    len = stream.read(binput, 0, 1024);
                }
                bwriter.close();
                outstream.close();
            }
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JProgressBar jProgressBar1;
}
