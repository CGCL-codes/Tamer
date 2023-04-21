package org.web3d.x3d.palette.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.text.JTextComponent;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.web3d.x3d.PixelTextureGenerator;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;

/**
 * PIXELTEXTURECustomizer.java
 * Created on Sep 10, 2007, 3:05 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public class PIXELTEXTURECustomizer extends BaseCustomizer {

    private PIXELTEXTURE pixelTexture;

    private JTextComponent target;

    private JFileChooser fileChooser;

    private int totalPixelCount, totalComponentCount;

    public PIXELTEXTURECustomizer(PIXELTEXTURE pixelTexture, JTextComponent target) {
        super(pixelTexture);
        this.pixelTexture = pixelTexture;
        this.target = target;
        HelpCtx.setHelpIDString(this, "PIXELTEXTURE_ELEM_HELPID");
        initComponents();
        numWidthTF.setText(pixelTexture.getNumWidth());
        numHeightTF.setText(pixelTexture.getNumHeight());
        int colorCount = pixelTexture.getNumColorValue();
        if ((colorCount >= 0) && (colorCount <= 4)) numberColorsCB.setSelectedIndex(colorCount); else numberColorsCB.setSelectedIndex(0);
        imageDataTA.setText(pixelTexture.getImageData());
        repeatSCB.setSelected(pixelTexture.isRepeatS());
        repeatTCB.setSelected(pixelTexture.isRepeatT());
        computeTotalValues();
    }

    private void computeTotalValues() {
        totalPixelCount = new SFInt32(numWidthTF.getText()).getValue() * new SFInt32(numHeightTF.getText()).getValue();
        totalComponentCount = totalPixelCount * new SFInt32(numberColorsCB.getSelectedItem().toString()).getValue();
        totalPixelValues.setText(" " + String.valueOf(totalPixelCount));
        totalComponentValues.setText(" " + String.valueOf(totalComponentCount));
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;
        DEFUSEpanel dEFUSEpanel1 = getDEFUSEpanel();
        jPanel1 = new JPanel();
        jLabel3 = new JLabel();
        numWidthTF = new JTextField();
        jLabel4 = new JLabel();
        numHeightTF = new JTextField();
        colorComponentsComboBoxLabel = new JLabel();
        jScrollPane1 = new JScrollPane();
        imageDataTA = new JTextArea();
        numberColorsCB = new JComboBox();
        colorComponentsKeyLabel = new JLabel();
        repeatSLabel = new JLabel();
        repeatTLabel = new JLabel();
        repeatSCB = new JCheckBox();
        repeatTCB = new JCheckBox();
        importImageButton = new JButton();
        zeroAllValuesButton = new JButton();
        importImageFileTextField = new JTextField();
        totalPixelValuesLabel = new JLabel();
        totalPixelValues = new JLabel();
        totalComponentValuesLabel = new JLabel();
        totalComponentValues = new JLabel();
        nodeHintPanel = new JPanel();
        descriptionLabel = new JLabel();
        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(dEFUSEpanel1, gridBagConstraints);
        jPanel1.setBorder(BorderFactory.createTitledBorder("image"));
        jPanel1.setLayout(new GridBagLayout());
        jLabel3.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabel3.setText("width in pixels");
        jLabel3.setToolTipText("width of image array");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(jLabel3, gridBagConstraints);
        numWidthTF.setText("0");
        numWidthTF.setToolTipText("width of image array");
        numWidthTF.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                numWidthTFActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(numWidthTF, gridBagConstraints);
        jLabel4.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabel4.setText("height in pixels");
        jLabel4.setToolTipText("height of image array");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(jLabel4, gridBagConstraints);
        numHeightTF.setText("0");
        numHeightTF.setToolTipText("height of image array");
        numHeightTF.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                numHeightTFActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(numHeightTF, gridBagConstraints);
        colorComponentsComboBoxLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        colorComponentsComboBoxLabel.setText("color components per pixel");
        colorComponentsComboBoxLabel.setToolTipText("values defined per pixel entry");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(colorComponentsComboBoxLabel, gridBagConstraints);
        jScrollPane1.setBorder(BorderFactory.createTitledBorder("image array of pixel values"));
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        imageDataTA.setColumns(20);
        imageDataTA.setLineWrap(true);
        imageDataTA.setRows(5);
        imageDataTA.setToolTipText("array of pixel values, each holding 0..4 hex values");
        imageDataTA.setWrapStyleWord(true);
        imageDataTA.setMinimumSize(new Dimension(10, 19));
        jScrollPane1.setViewportView(imageDataTA);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(jScrollPane1, gridBagConstraints);
        numberColorsCB.setModel(new DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4" }));
        numberColorsCB.setToolTipText("values defined per pixel entry");
        numberColorsCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                numberColorsCBActionPerformed(evt);
            }
        });
        numberColorsCB.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                numberColorsCBPropertyChange(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(numberColorsCB, gridBagConstraints);
        colorComponentsKeyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        colorComponentsKeyLabel.setText("0 none, 1 BW, 2 BW alpha, 3 RGB, 4 RGBA");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.6666;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(colorComponentsKeyLabel, gridBagConstraints);
        repeatSLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        repeatSLabel.setText("repeatS");
        repeatSLabel.setToolTipText("Horizontally repeat texture along S axis using child TextureTransform");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(repeatSLabel, gridBagConstraints);
        repeatTLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        repeatTLabel.setText("repeatT");
        repeatTLabel.setToolTipText("Horizontally repeat texture along T axis using child TextureTransform");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(repeatTLabel, gridBagConstraints);
        repeatSCB.setSelected(true);
        repeatSCB.setToolTipText("Horizontally repeat texture along S axis using child TextureTransform");
        repeatSCB.setMargin(new Insets(0, 0, 0, 0));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(repeatSCB, gridBagConstraints);
        repeatTCB.setSelected(true);
        repeatTCB.setToolTipText("Horizontally repeat texture along T axis using child TextureTransform");
        repeatTCB.setMargin(new Insets(0, 0, 0, 0));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(repeatTCB, gridBagConstraints);
        importImageButton.setText("Import image file");
        importImageButton.setToolTipText("Select image file for conversion to X3D PixelTexture");
        importImageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                importImageButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(importImageButton, gridBagConstraints);
        zeroAllValuesButton.setText("zero all values");
        zeroAllValuesButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                zeroAllValuesButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(zeroAllValuesButton, gridBagConstraints);
        importImageFileTextField.setToolTipText("option: select image for conversion into PixelTexture");
        importImageFileTextField.setBorder(null);
        importImageFileTextField.setEnabled(false);
        importImageFileTextField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                importImageFileTextFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 5);
        jPanel1.add(importImageFileTextField, gridBagConstraints);
        totalPixelValuesLabel.setText("total pixel count");
        totalPixelValuesLabel.setToolTipText("number of values in image array");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(totalPixelValuesLabel, gridBagConstraints);
        totalPixelValues.setText(" 0");
        totalPixelValues.setToolTipText("number of values in image array");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(totalPixelValues, gridBagConstraints);
        totalComponentValuesLabel.setText("total components x count");
        totalComponentValuesLabel.setToolTipText("color components x pixel count");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(totalComponentValuesLabel, gridBagConstraints);
        totalComponentValues.setText(" 0");
        totalComponentValues.setToolTipText("color components x pixel count");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.3333;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        jPanel1.add(totalComponentValues, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(jPanel1, gridBagConstraints);
        nodeHintPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        nodeHintPanel.setLayout(new GridBagLayout());
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setText("<html><p align='center'><b>PixelTexture</b> is contained by <b>Appearance</b> to map an image onto peer geometry</p>");
        descriptionLabel.setToolTipText("TextureProperties, TextureTransform and TextureCoordinate can further adjust texture application");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 3, 10, 3);
        nodeHintPanel.add(descriptionLabel, gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        add(nodeHintPanel, gridBagConstraints);
    }

    private void numberColorsCBActionPerformed(java.awt.event.ActionEvent evt) {
        pixelTexture.setNumColor(numberColorsCB.getSelectedItem().toString());
    }

    private void importImageButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setDialogTitle("Select image file");
            fileChooser.setToolTipText("Select image file for conversion to X3D PixelTexture");
            fileChooser.setMultiSelectionEnabled(false);
        }
        int retVal = fileChooser.showOpenDialog(this);
        if (retVal != JFileChooser.APPROVE_OPTION) return;
        String fileName = fileChooser.getSelectedFile().getAbsolutePath();
        importImageFileTextField.setText(fileName);
        PixelTextureGenerator pixelTextureGenerator = new PixelTextureGenerator(fileName);
        if (pixelTextureGenerator.getOutputPixelString().length() == 0) {
            System.out.println("pixelTextureGenerator has no output");
            return;
        } else {
            System.out.println("pixelTextureGenerator.getOutputPixelString().length()=" + pixelTextureGenerator.getOutputPixelString().length() + ", width=" + String.valueOf(pixelTextureGenerator.getImageWidth()) + ", height=" + String.valueOf(pixelTextureGenerator.getImageHeight()));
        }
        imageDataTA.setText(pixelTextureGenerator.getOutputPixelString().toString());
        numWidthTF.setText(String.valueOf(pixelTextureGenerator.getImageWidth()));
        numHeightTF.setText(String.valueOf(pixelTextureGenerator.getImageHeight()));
        numberColorsCB.setSelectedIndex(3);
        computeTotalValues();
    }

    private void importImageFileTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void numWidthTFActionPerformed(java.awt.event.ActionEvent evt) {
        computeTotalValues();
    }

    private void numHeightTFActionPerformed(java.awt.event.ActionEvent evt) {
        computeTotalValues();
    }

    private void numberColorsCBPropertyChange(java.beans.PropertyChangeEvent evt) {
        computeTotalValues();
    }

    private void zeroAllValuesButtonActionPerformed(ActionEvent evt) {
        StringBuilder zeroArray = new StringBuilder();
        SFInt32 width = new SFInt32(numWidthTF.getText(), 0, null);
        for (int i = 0; i < totalPixelCount; i++) {
            if (numberColorsCB.getSelectedItem().toString().trim().equals("1")) zeroArray.append("0"); else if (numberColorsCB.getSelectedItem().toString().trim().equals("2")) zeroArray.append("0x0000"); else if (numberColorsCB.getSelectedItem().toString().trim().equals("3")) zeroArray.append("0x000000"); else if (numberColorsCB.getSelectedItem().toString().trim().equals("4")) zeroArray.append("0x00000000");
            if ((i > 0) && ((i + 1) % width.getValue() == 0)) zeroArray.append("\n"); else zeroArray.append(" ");
        }
        imageDataTA.setText(zeroArray.toString().trim());
    }

    private JLabel colorComponentsComboBoxLabel;

    private JLabel colorComponentsKeyLabel;

    private JLabel descriptionLabel;

    private JTextArea imageDataTA;

    private JButton importImageButton;

    private JTextField importImageFileTextField;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JPanel jPanel1;

    private JScrollPane jScrollPane1;

    private JPanel nodeHintPanel;

    private JTextField numHeightTF;

    private JTextField numWidthTF;

    private JComboBox numberColorsCB;

    private JCheckBox repeatSCB;

    private JLabel repeatSLabel;

    private JCheckBox repeatTCB;

    private JLabel repeatTLabel;

    private JLabel totalComponentValues;

    private JLabel totalComponentValuesLabel;

    private JLabel totalPixelValues;

    private JLabel totalPixelValuesLabel;

    private JButton zeroAllValuesButton;

    @Override
    public String getNameKey() {
        return "NAME_X3D_PIXELTEXTURE";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        int valueCount = (new SFInt32(numWidthTF.getText())).getValue() * (new SFInt32(numHeightTF.getText())).getValue();
        int arrayCount = imageDataTA.getText().trim().split(" ").length;
        String notificationMessage = new String();
        if ((valueCount == 0) && (imageDataTA.getText().trim().length() == 0)) {
        } else if (valueCount < arrayCount) {
            notificationMessage = "Found insufficient number of image values (need " + valueCount + ", found " + (arrayCount) + ")";
        } else if (valueCount < arrayCount) {
            notificationMessage = "Found too many image values (need " + valueCount + ", found " + (arrayCount) + ")";
        }
        if (notificationMessage.length() > 0) {
            NotifyDescriptor descriptor = new NotifyDescriptor.Message(notificationMessage, NotifyDescriptor.WARNING_MESSAGE);
            DialogDisplayer.getDefault().notify(descriptor);
        }
        unLoadDEFUSE();
        pixelTexture.setNumWidth(numWidthTF.getText().trim());
        pixelTexture.setNumHeight(numHeightTF.getText().trim());
        pixelTexture.setNumColor(numberColorsCB.getSelectedItem().toString());
        pixelTexture.setImageData(imageDataTA.getText().trim());
        pixelTexture.setRepeatS(repeatSCB.isSelected());
        pixelTexture.setRepeatT(repeatTCB.isSelected());
    }
}
