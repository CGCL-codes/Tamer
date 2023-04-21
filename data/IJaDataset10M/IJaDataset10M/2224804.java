package org.web3d.x3d.palette.items;

import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;
import org.web3d.x3d.X3DDataObject;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * IMAGETEXTURE3DCustomizer.java
 * Created on 20 November 2011
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class IMAGETEXTURE3DCustomizer extends BaseCustomizer {

    private IMAGETEXTURE3D imageTexture3D;

    private JTextComponent target;

    private X3DDataObject xObj;

    public IMAGETEXTURE3DCustomizer(IMAGETEXTURE3D imageTexture3D, JTextComponent target, X3DDataObject xObj) {
        super(imageTexture3D);
        this.imageTexture3D = imageTexture3D;
        this.target = target;
        this.xObj = xObj;
        HelpCtx.setHelpIDString(this, "IMAGETEXTURE3D_ELEM_HELPID");
        initComponents();
        urlList.setTarget(target);
        super.getDEFUSEpanel().setContainerFieldChoices(COMPOSEDTEXTURE3D_ATTR_CONTAINERFIELD_CHOICES, COMPOSEDTEXTURE3D_ATTR_CONTAINERFIELD_TOOLTIPS);
        urlList.setMasterDocumentLocation(xObj.getPrimaryFile());
        urlList.setUrlData(imageTexture3D.getUrls());
        urlList.setFileChooserVolume();
        repeatSCB.setSelected(imageTexture3D.isRepeatS());
        repeatTCB.setSelected(imageTexture3D.isRepeatT());
        repeatRCB.setSelected(imageTexture3D.isRepeatR());
        insertCommasCheckBox.setSelected(imageTexture3D.isInsertCommas());
        insertLineBreaksCheckBox.setSelected(imageTexture3D.isInsertLineBreaks());
        setDefaultDEFname();
    }

    private void setDefaultDEFname() {
        if (urlList.getUrlData().length == 0) return;
        String fileName = urlList.getUrlData()[0];
        super.getDEFUSEpanel().setDefaultDEFname(fileName);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel = getDEFUSEpanel();
        repeatSCB = new javax.swing.JCheckBox();
        repeatTCB = new javax.swing.JCheckBox();
        repeatRCB = new javax.swing.JCheckBox();
        urlLabel = new javax.swing.JLabel();
        urlList = new org.web3d.x3d.palette.items.UrlExpandableList2();
        nodeHintPanel = new javax.swing.JPanel();
        descriptionLabel = new javax.swing.JLabel();
        appendPanel = new javax.swing.JPanel();
        appendLabel = new javax.swing.JLabel();
        insertCommasCheckBox = new javax.swing.JCheckBox();
        insertLineBreaksCheckBox = new javax.swing.JCheckBox();
        setLayout(new java.awt.GridBagLayout());
        dEFUSEpanel.setMinimumSize(new java.awt.Dimension(198, 77));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(dEFUSEpanel, gridBagConstraints);
        repeatSCB.setText("repeatS");
        repeatSCB.setToolTipText("Horizontally repeat texture along S axis using child TextureTransform");
        repeatSCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        repeatSCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        repeatSCB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatSCBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(repeatSCB, gridBagConstraints);
        repeatTCB.setText("repeatT");
        repeatTCB.setToolTipText("Horizontally repeat texture along T axis using child TextureTransform");
        repeatTCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        repeatTCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(repeatTCB, gridBagConstraints);
        repeatRCB.setText("repeatR");
        repeatRCB.setToolTipText("Horizontally repeat texture along T axis using child TextureTransform");
        repeatRCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        repeatRCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(repeatRCB, gridBagConstraints);
        urlLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        urlLabel.setText("url");
        urlLabel.setToolTipText("List of image resources, retrieved in order until one is found");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        add(urlLabel, gridBagConstraints);
        urlList.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        urlList.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                urlListMouseReleased(evt);
            }
        });
        urlList.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                urlListPropertyChange(evt);
            }
        });
        urlList.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                urlListKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(urlList, gridBagConstraints);
        nodeHintPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nodeHintPanel.setLayout(new java.awt.GridBagLayout());
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setText("<html><b>ImageTexture3D</b> can also contain a <b>TextureProperties</b> node");
        descriptionLabel.setToolTipText("close this panel to add a child node");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 10, 3);
        nodeHintPanel.add(descriptionLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(nodeHintPanel, gridBagConstraints);
        appendPanel.setLayout(new java.awt.GridBagLayout());
        appendLabel.setFont(new java.awt.Font("Tahoma", 2, 11));
        appendLabel.setText("append:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        appendPanel.add(appendLabel, gridBagConstraints);
        insertCommasCheckBox.setFont(new java.awt.Font("Tahoma", 2, 11));
        insertCommasCheckBox.setText("commas,");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        appendPanel.add(insertCommasCheckBox, gridBagConstraints);
        insertLineBreaksCheckBox.setFont(new java.awt.Font("Tahoma", 2, 11));
        insertLineBreaksCheckBox.setText("line feeds");
        insertLineBreaksCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertLineBreaksCheckBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        appendPanel.add(insertLineBreaksCheckBox, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(appendPanel, gridBagConstraints);
    }

    private void insertLineBreaksCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void repeatSCBActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void urlListKeyReleased(java.awt.event.KeyEvent evt) {
        setDefaultDEFname();
    }

    private void urlListMouseReleased(java.awt.event.MouseEvent evt) {
        setDefaultDEFname();
    }

    private void urlListPropertyChange(java.beans.PropertyChangeEvent evt) {
        setDefaultDEFname();
    }

    private javax.swing.JLabel appendLabel;

    private javax.swing.JPanel appendPanel;

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JCheckBox insertCommasCheckBox;

    private javax.swing.JCheckBox insertLineBreaksCheckBox;

    private javax.swing.JPanel nodeHintPanel;

    private javax.swing.JCheckBox repeatRCB;

    private javax.swing.JCheckBox repeatSCB;

    private javax.swing.JCheckBox repeatTCB;

    private javax.swing.JLabel urlLabel;

    private org.web3d.x3d.palette.items.UrlExpandableList2 urlList;

    @Override
    public String getNameKey() {
        return "NAME_X3D_IMAGETEXTURE3D";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        imageTexture3D.setRepeatS(repeatSCB.isSelected());
        imageTexture3D.setRepeatT(repeatTCB.isSelected());
        imageTexture3D.setRepeatR(repeatRCB.isSelected());
        imageTexture3D.setUrls(urlList.getUrlData());
        imageTexture3D.setInsertCommas(insertCommasCheckBox.isSelected());
        imageTexture3D.setInsertLineBreaks(insertLineBreaksCheckBox.isSelected());
    }
}
