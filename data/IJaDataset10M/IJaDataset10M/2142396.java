package org.tcpfile.plugins;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcpfile.fileio.Hasher;
import org.tcpfile.main.Misc;
import org.tcpfile.net.ByteArray;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class HashGUI extends JPanel {

    private static Logger log = LoggerFactory.getLogger(HashGUI.class);

    private static final long serialVersionUID = 1L;

    private JTextArea jTextArea1;

    private JCheckBox MD5;

    private JCheckBox SHA1;

    private JPanel jPanel1;

    private JScrollPane jScrollPane1;

    private JPanel jPanel2;

    private JCheckBox SHA256;

    private Object sync = new Object();

    /**
	 * This is the default constructor
	 */
    public HashGUI() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 200);
        BorderLayout thisLayout = new BorderLayout();
        this.setLayout(thisLayout);
        this.setPreferredSize(new java.awt.Dimension(353, 245));
        {
            jScrollPane1 = new JScrollPane();
            this.add(jScrollPane1, BorderLayout.CENTER);
            {
                jPanel1 = new JPanel();
                jScrollPane1.setViewportView(jPanel1);
                {
                    jTextArea1 = new JTextArea();
                    jPanel1.setLayout(new BorderLayout());
                    jPanel1.add(jTextArea1, BorderLayout.CENTER);
                    jTextArea1.setText("Drop a file or a list of files here to create hashes of checked types");
                    jTextArea1.setTransferHandler(new HashHandler());
                }
                {
                    jPanel2 = new JPanel();
                    jPanel1.add(jPanel2, BorderLayout.NORTH);
                    {
                        MD5 = new JCheckBox();
                        jPanel2.add(MD5);
                        MD5.setText("MD5");
                        MD5.setSelected(true);
                    }
                    {
                        SHA1 = new JCheckBox();
                        jPanel2.add(SHA1);
                        SHA1.setText("SHA1");
                    }
                    {
                        SHA256 = new JCheckBox();
                        jPanel2.add(SHA256);
                        SHA256.setText("SHA-256");
                        SHA256.setPreferredSize(new java.awt.Dimension(131, 20));
                    }
                }
            }
        }
    }

    public void hashFile(final File file) {
        Runnable r = new Runnable() {

            public void run() {
                synchronized (sync) {
                    log.trace("Entering");
                    Hasher h;
                    if (MD5.isSelected()) {
                        String algorithm = "MD5";
                        hashFile(file, algorithm);
                    }
                    if (SHA1.isSelected()) {
                        String algorithm = "SHA1";
                        hashFile(file, algorithm);
                    }
                    if (SHA256.isSelected()) {
                        String algorithm = "SHA-256";
                        hashFile(file, algorithm);
                    }
                }
            }

            private void hashFile(final File file, String algorithm) {
                Hasher h;
                h = new Hasher(algorithm);
                byte[] hash = h.hash(file.getAbsolutePath());
                addLine("hashed " + file.getAbsolutePath() + " " + h.getAlgorithm() + " Hash " + ByteArray.dumpBytes16(hash));
            }
        };
        Misc.runRunnableInThread(r, "HashPlugin");
    }

    public void addLine(String line) {
        jTextArea1.setText(jTextArea1.getText() + Misc.NEWLINE + line);
    }
}
