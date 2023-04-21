package net.sourceforge.olduvai.lrac.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;
import net.sourceforge.olduvai.lrac.DataGrid;
import net.sourceforge.olduvai.lrac.LiveRAC;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class AnnotatedScreenShot extends JDialog {

    static final String TITLE = "Annotate screen shot";

    public static final String USERHOME = System.getProperty("user.home") + "/LiveRAC/";

    public static final String SCREENSHOTPREFIX = "screenshot";

    public static final String IMAGEFILESUFFIX = ".png";

    public static final String XMLFILESUFFIX = ".xml";

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yy-MMM-dd_HH.mm.ss");

    private static final String ROOTTAG = "root";

    private static final String FILENAMETAG = "FILENAME";

    private static final String TEXTTAG = "ANNOTATION";

    /**
	 * 
	 */
    private static final long serialVersionUID = 4484163258694589436L;

    JTextArea text = new JTextArea();

    LiveRAC lr;

    public AnnotatedScreenShot(Frame owner, boolean modal, LiveRAC lr) throws HeadlessException {
        super(owner, TITLE, modal);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.lr = lr;
        this.setLayout(new MigLayout("fill", "[grow][grow]", "[][grow][]"));
        this.setPreferredSize(new Dimension(300, 400));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveAnnotation();
            }
        });
        add(new JLabel("Please enter comments below: "), "span, wrap, growx");
        add(text, "span, wrap, grow");
        add(cancel, "growx");
        add(save, "growx");
        this.pack();
    }

    void saveAnnotation() {
        setVisible(false);
        Timer snapTimer = new Timer("Snap shot timer", true);
        TimerTask tt = new TimerTask() {

            @Override
            public void run() {
                final String saveDateString = sdf.format(new Date());
                final String participant = DataGrid.getInstance().loadUserId();
                final String imageFileName = USERHOME + SCREENSHOTPREFIX + "_" + saveDateString + IMAGEFILESUFFIX;
                final File file = new File(imageFileName);
                lr.screenShot(file);
                Element root = new Element(ROOTTAG);
                Document d = new Document();
                d.setRootElement(root);
                Element textEl = new Element(TEXTTAG);
                textEl.setText(text.getText());
                Element fileNameEl = new Element(FILENAMETAG);
                fileNameEl.setText(imageFileName);
                root.addContent(textEl);
                root.addContent(fileNameEl);
                final String xmlFileName = USERHOME + SCREENSHOTPREFIX + participant + "_" + saveDateString + XMLFILESUFFIX;
                XMLOutputter outputter = new XMLOutputter();
                try {
                    outputter.output(d, new FileWriter(xmlFileName));
                    System.err.println("Annotated screen shot: Upload of file disabled");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("AnnotatedScreenShot: Error writing screenshot to server");
                }
                JOptionPane.showMessageDialog(getThis(), "Screenshot saved to: " + imageFileName, "Screenshot saved", JOptionPane.INFORMATION_MESSAGE);
                close();
            }
        };
        snapTimer.schedule(tt, DELAY);
    }

    static final long DELAY = 300;

    AnnotatedScreenShot getThis() {
        return this;
    }

    void close() {
        setVisible(false);
        dispose();
    }
}
