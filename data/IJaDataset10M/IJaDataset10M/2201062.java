package ArianneEditor;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import ArianneUtil.*;
import com.borland.jbcl.layout.*;

/**
 * <p>Title: Picture Editor</p>
 *
 * <p>Description: Editor grafico per sistemi SCADA</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Itaco S.r.l.</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ImageProperties extends JDialog {

    XYLayout xYLayout1 = new XYLayout();

    XYLayout xYLayout2 = new XYLayout();

    XYLayout xYLayout3 = new XYLayout();

    XYLayout xYLayout4 = new XYLayout();

    JPanel jPanel1 = new JPanel();

    Border border1;

    Border border2;

    Border border3;

    JButton applicaButton = new JButton();

    JButton annullaButton = new JButton();

    EditorDrawingPanel drawPanel;

    JLabel widthLabel = new JLabel();

    JLabel heightLabel = new JLabel();

    JPanel jPanel2 = new JPanel();

    JTextField widthText = new JTextField();

    JTextField heightText = new JTextField();

    JPanel jPanel3 = new JPanel();

    JCheckBox attiveCheckBox = new JCheckBox();

    JSpinner timeSpinner = new JSpinner();

    JLabel timeLabel = new JLabel();

    Vector listTime = new Vector();

    int width = 0;

    int height = 0;

    int reload = 0;

    JButton bckColorButton = new JButton();

    Color bckColor = Color.white;

    JButton bckImgButton = new JButton();

    String path = "";

    ImageIcon bckImg = null;

    JButton nullBckButton = new JButton();

    JButton nullBckColorButton = new JButton();

    public ImageProperties(int w, int h, Color c, int r, EditorDrawingPanel dp) {
        this.width = w;
        this.height = h;
        this.reload = r;
        this.drawPanel = dp;
        this.setBckColor(c);
        this.setPath(dp.getBckImgPath());
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isLoggingEnabled() {
        return drawPanel.isLoggingEnabled();
    }

    private void jbInit() throws Exception {
        border1 = new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151));
        border2 = new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "Parameters");
        border3 = new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.white, new Color(165, 163, 151)), "Reload Time");
        this.setSize(500, 340);
        this.getContentPane().setLayout(xYLayout1);
        this.setTitle("Set image properties");
        this.addWindowListener(new ImageProperties_this_windowAdapter(this));
        jPanel1.setLayout(xYLayout2);
        jPanel1.setBorder(border1);
        jPanel1.setMinimumSize(new Dimension(451, 320));
        jPanel1.setPreferredSize(new Dimension(451, 320));
        jPanel1.setToolTipText("");
        jPanel2.setLayout(xYLayout3);
        jPanel2.setBorder(border2);
        jPanel3.setLayout(xYLayout4);
        jPanel3.setBorder(border3);
        applicaButton.setFont(new java.awt.Font("Dialog", 1, 11));
        applicaButton.setText("Applica");
        applicaButton.addActionListener(new ImageProperties_chiudiButton_actionAdapter(this));
        annullaButton.setFont(new java.awt.Font("Dialog", 1, 11));
        annullaButton.setText("Annulla");
        annullaButton.addActionListener(new ImageProperties_annullaButton_actionAdapter(this));
        widthLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        widthLabel.setText("Width");
        heightLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        heightLabel.setText("Height");
        widthText.setText("" + width);
        widthText.setHorizontalAlignment(SwingConstants.RIGHT);
        heightText.setText("" + height);
        heightText.setHorizontalAlignment(SwingConstants.RIGHT);
        attiveCheckBox.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        attiveCheckBox.setText("Attiva reload immagine");
        attiveCheckBox.addActionListener(new ImageProperties_attiveCheckBox_actionAdapter(this));
        timeLabel.setFont(new java.awt.Font("Dialog", Font.BOLD, 13));
        timeLabel.setText("Reload Time (sec)");
        for (int i = 1; i <= 90000; i++) listTime.add(new Integer(i));
        SpinnerListModel listModel = new SpinnerListModel(listTime);
        timeSpinner = new JSpinner(listModel);
        JFormattedTextField tf = ((JSpinner.DefaultEditor) timeSpinner.getEditor()).getTextField();
        tf.setEditable(false);
        if (reload == 0) {
            timeLabel.setEnabled(false);
            timeSpinner.setEnabled(false);
        } else {
            attiveCheckBox.setSelected(true);
            Integer a = new Integer(reload);
            timeSpinner.getModel().setValue(a);
        }
        xYLayout1.setWidth(488);
        xYLayout1.setHeight(380);
        bckColorButton.setMaximumSize(new Dimension(110, 25));
        bckColorButton.setMinimumSize(new Dimension(110, 25));
        bckColorButton.setPreferredSize(new Dimension(110, 25));
        bckColorButton.setMargin(new Insets(1, 10, 1, 10));
        bckColorButton.setText("Background Color");
        bckColorButton.addActionListener(new ImageProperties_bckColorButton_actionAdapter(this));
        bckImgButton.setMaximumSize(new Dimension(110, 25));
        bckImgButton.setMinimumSize(new Dimension(110, 25));
        bckImgButton.setPreferredSize(new Dimension(110, 25));
        bckImgButton.setMargin(new Insets(1, 8, 1, 8));
        bckImgButton.setText("Background image");
        bckImgButton.addActionListener(new ImageProperties_bckImgButton_actionAdapter(this));
        nullBckButton.setText("No Bck Image");
        nullBckButton.addActionListener(new ImageProperties_nullBckButton_actionAdapter(this));
        nullBckColorButton.setText("No Bck Color");
        nullBckColorButton.addActionListener(new ImageProperties_nullBckColorButton_actionAdapter(this));
        jPanel3.add(attiveCheckBox, new XYConstraints(122, 12, 187, 16));
        jPanel3.add(timeLabel, new XYConstraints(107, 48, 128, 17));
        jPanel3.add(timeSpinner, new XYConstraints(246, 46, 80, 19));
        jPanel1.add(annullaButton, new XYConstraints(241, 240, 95, 26));
        jPanel1.add(applicaButton, new XYConstraints(350, 240, 95, 26));
        jPanel1.add(jPanel2, new XYConstraints(11, 10, 436, 120));
        this.getContentPane().add(jPanel1, new XYConstraints(13, 14, 462, 278));
        jPanel1.add(jPanel3, new XYConstraints(11, 130, 436, 104));
        jPanel2.add(widthLabel, new XYConstraints(55, 0, 40, 19));
        jPanel2.add(widthText, new XYConstraints(95, 0, 84, 19));
        jPanel2.add(heightLabel, new XYConstraints(225, 0, 45, 19));
        jPanel2.add(heightText, new XYConstraints(270, 0, 84, 19));
        jPanel2.add(bckColorButton, new XYConstraints(69, 35, -1, -1));
        jPanel2.add(bckImgButton, new XYConstraints(244, 35, -1, -1));
        jPanel2.add(nullBckButton, new XYConstraints(244, 68, 110, -1));
        jPanel2.add(nullBckColorButton, new XYConstraints(69, 68, 110, -1));
    }

    public Color getBckColor() {
        return bckColor;
    }

    public void setBckColor(Color c) {
        bckColor = c;
    }

    public void attiveCheckBox_actionPerformed(ActionEvent e) {
        if (attiveCheckBox.isSelected()) {
            timeSpinner.setEnabled(true);
            timeLabel.setEnabled(true);
        } else {
            timeSpinner.setEnabled(false);
            timeLabel.setEnabled(false);
        }
    }

    public void annullaButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    public void chiudiButton_actionPerformed(ActionEvent e) {
        try {
            Integer.parseInt(widthText.getText());
            Integer.parseInt(heightText.getText());
            if ((widthText.getText() != null) && (!widthText.getText().equals("")) && (heightText.getText() != null) && (!heightText.getText().equals(""))) {
                Integer w = new Integer(widthText.getText());
                Integer h = new Integer(heightText.getText());
                boolean newDimOk = true;
                for (int i = 0; newDimOk && i < drawPanel.getShapeList().size(); i++) {
                    EditorShapes sh = (EditorShapes) drawPanel.getShapeList().get(i);
                    if (sh.getMaxX() > w.intValue()) newDimOk = false;
                    if (sh.getMaxY() > h.intValue()) newDimOk = false;
                }
                if (newDimOk) {
                    drawPanel.setImgSize(w.intValue(), h.intValue());
                    drawPanel.getFather().getCustomGlassPane().setMaximumSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().setMinimumSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().setPreferredSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().setSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().getGlassPane().setMinimumSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().getGlassPane().setMaximumSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().getGlassPane().setPreferredSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().getGlassPane().setSize(new Dimension(w.intValue(), h.intValue()));
                    drawPanel.getFather().getCustomGlassPane().getGlassPane().revalidate();
                    drawPanel.setCanvasBckColor(getBckColor());
                    drawPanel.setBckImgPath(getPath());
                    setBckImg(getPath());
                    drawPanel.oldoffscreen = null;
                    drawPanel.setBckImg(bckImg);
                    drawPanel.forceRepaint();
                    if (this.attiveCheckBox.isSelected()) {
                        Integer t = (Integer) timeSpinner.getValue();
                        drawPanel.setReloadTime(t.intValue());
                    } else {
                        drawPanel.setReloadTime(0);
                    }
                    drawPanel.getFather().paintComponents(drawPanel.getFather().getGraphics());
                    this.dispose();
                } else {
                    JOptionPane pane = new JOptionPane();
                    pane.showMessageDialog(null, "Con queste nuove dimensioni alcuni oggetti rimarrebbero al di fuori dei bordi dell'immagine", "Warning", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane pane = new JOptionPane();
                pane.showMessageDialog(null, "Inserimento Dimensioni errate", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane pane = new JOptionPane();
            pane.showMessageDialog(null, "Inserimento Dimensioni errate", "Warning", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void bckColorButton_actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Choose image background color", getBckColor());
        setBckColor(newColor);
    }

    public void setPath(String pth) {
        path = pth;
    }

    public String getPath() {
        return path;
    }

    public void bckImgButton_actionPerformed(ActionEvent e) {
        JFileChooser jFileChooser1 = new JFileChooser();
        String separator = "";
        if (JFileChooser.APPROVE_OPTION == jFileChooser1.showOpenDialog(null)) {
            setPath(jFileChooser1.getSelectedFile().getPath());
            separator = jFileChooser1.getSelectedFile().separator;
            File dirImg = new File("." + separator + "images");
            if (!dirImg.exists()) {
                dirImg.mkdir();
            }
            int index = getPath().lastIndexOf(separator);
            String imgName = getPath().substring(index);
            String newPath = dirImg + imgName;
            setBckImg(newPath);
        }
    }

    public void setBckImg(String newPath) {
        try {
            File inputFile = new File(getPath());
            File outputFile = new File(newPath);
            if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                FileInputStream in = new FileInputStream(inputFile);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(outputFile);
                } catch (FileNotFoundException ex1) {
                    ex1.printStackTrace();
                    JOptionPane.showMessageDialog(null, ex1.getMessage().substring(0, Math.min(ex1.getMessage().length(), drawPanel.MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "Set Bck Img", JOptionPane.ERROR_MESSAGE);
                }
                int c;
                if (out != null) {
                    while ((c = in.read()) != -1) out.write(c);
                    out.close();
                }
                in.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
            JOptionPane.showMessageDialog(null, ex.getMessage().substring(0, Math.min(ex.getMessage().length(), drawPanel.MAX_DIALOG_MSG_SZ)) + "-" + getClass(), "Set Bck Img", JOptionPane.ERROR_MESSAGE);
        }
        setPath(newPath);
        bckImg = new ImageIcon(getPath());
    }

    public void this_windowClosed(WindowEvent e) {
        drawPanel.enableKeyListening();
    }

    public void nullBckButton_actionPerformed(ActionEvent e) {
        bckImg = null;
        setBckImg("");
    }

    public void nullBckColorButton_actionPerformed(ActionEvent e) {
        setBckColor(Color.white);
    }
}

class ImageProperties_nullBckColorButton_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_nullBckColorButton_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.nullBckColorButton_actionPerformed(e);
    }
}

class ImageProperties_nullBckButton_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_nullBckButton_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.nullBckButton_actionPerformed(e);
    }
}

class ImageProperties_bckImgButton_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_bckImgButton_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.bckImgButton_actionPerformed(e);
    }
}

class ImageProperties_bckColorButton_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_bckColorButton_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.bckColorButton_actionPerformed(e);
    }
}

class ImageProperties_chiudiButton_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_chiudiButton_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.chiudiButton_actionPerformed(e);
    }
}

class ImageProperties_this_windowAdapter extends WindowAdapter {

    private ImageProperties adaptee;

    ImageProperties_this_windowAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void windowClosed(WindowEvent e) {
        adaptee.this_windowClosed(e);
    }
}

class ImageProperties_annullaButton_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_annullaButton_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.annullaButton_actionPerformed(e);
    }
}

class ImageProperties_attiveCheckBox_actionAdapter implements ActionListener {

    private ImageProperties adaptee;

    ImageProperties_attiveCheckBox_actionAdapter(ImageProperties adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.attiveCheckBox_actionPerformed(e);
    }
}
