package org.web3d.x3d.palette.items;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;
import org.web3d.x3d.xj3d.viewer.Xj3dViewerPanel;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;

/**
 * MATERIALCustomizer.java
 * Created on March 14, 2007, 10:09 AM
 * 
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 * 
 * @author Mike Bailey and Don Brutzman
 * @version $Id$
 */
public class MATERIALCustomizer extends BaseCustomizer {

    private MATERIAL material;

    private JTextComponent target;

    private String libraryChoice;

    private int libraryIndex;

    private static boolean lightOnDefault = true;

    private static Color lightColorDefault = new Color(1.0f, 1.0f, 1.0f);

    private static float[] lightDirectionDefault = { -0.7071f, 0.0f, -0.7071f };

    private static float lightIntensityDefault = 1.0f;

    private static float lightAmbientIntensityDefault = 1.0f;

    private static Color skyColorDefault = Color.black;

    private UniversalMediaMaterialFinder materialFinder;

    private String DEFname = "";

    private String UNIVERSAL_MEDIA_COMMENT_HEADER1 = "<!-- ";

    private String UNIVERSAL_MEDIA_COMMENT_HEADER2 = "Universal Media Library: ";

    private String UNIVERSAL_MEDIA_COMMENT_HEADER_REGEX = ".*<!--\\s*" + UNIVERSAL_MEDIA_COMMENT_HEADER2 + "\\s*";

    private String UNIVERSAL_MEDIA_COMMENT_HEADER_FRONT_REGEX = ".*<!--\\s*frontMaterial " + UNIVERSAL_MEDIA_COMMENT_HEADER2 + "\\s*";

    private String UNIVERSAL_MEDIA_COMMENT_HEADER_BACK_REGEX = ".*<!--\\s*backMaterial " + UNIVERSAL_MEDIA_COMMENT_HEADER2 + "\\s*";

    private String UNIVERSAL_MEDIA_COMMENT_TAIL_REGEX = "\\s+(\\d+)\\s+-->.*";

    private String geometryTypeDefault = "Sphere";

    private String MEDIA_NONE_ID = "--none--";

    private int MEDIA_NONE_IDX = 0;

    private boolean mediaSliderDisablementLocked = false;

    private JFormattedTextField[] dLightColorTFArray;

    private JFormattedTextField[] diffuseColorTFArray;

    private JFormattedTextField[] specularTFArray;

    private JFormattedTextField[] emissiveTFArray;

    private JFormattedTextField[] skyColorTFArray;

    private MaterialCustomizerXj3dSupport x3dHelper;

    /** private constructor;  the (only) instance of this class is retrieved
 * by the static method above.
 * @param material
 * @param target
 */
    public MATERIALCustomizer(MATERIAL material, JTextComponent target) {
        super(material);
        this.material = material;
        this.target = target;
        HelpCtx.setHelpIDString(this, "MATERIAL_ELEM_HELPID");
        initComponents();
        leftSplitPane.setMinimumSize(new Dimension(10, 10));
        rightSplitPane.setMinimumSize(new Dimension(10, 10));
        leftTopPanel.setMinimumSize(new Dimension(10, 10));
        leftBottomPanel.setMinimumSize(new Dimension(10, 10));
        rightTopPan.setMinimumSize(new Dimension(10, 10));
        materialFinder = new UniversalMediaMaterialFinder();
        x3dHelper = new MaterialCustomizerXj3dSupport(xj3dViewer);
        initializePanelContent();
    }

    private void initializePanelContent() {
        String content = material.getContent();
        dLightColorTFArray = new JFormattedTextField[] { directionalLightColorRedTF, directionalLightColorGreenTF, directionalLightColorBlueTF };
        diffuseColorTFArray = new JFormattedTextField[] { diffuseColorRedTF, diffuseColorGreenTF, diffuseColorBlueTF };
        specularTFArray = new JFormattedTextField[] { specularColorRedTF, specularColorGreenTF, specularColorBlueTF };
        emissiveTFArray = new JFormattedTextField[] { emissiveColorRedTF, emissiveColorGreenTF, emissiveColorBlueTF };
        skyColorTFArray = new JFormattedTextField[] { skyColorRedTF, skyColorGreenTF, skyColorBlueTF };
        adjustWidgetSizes();
        resetLightingValues();
        resetMaterialValues();
        updateAllXj3dLightingFields();
        updateAllXj3dMaterialFields();
        if (material.isBack()) {
            diffuseColorLabel.setText("backDiffuseColor");
            emissiveColorLabel.setText("backEmissiveColor");
            specularColorLabel.setText("backSpecularColor");
            ambientIntensityLabel.setText("backAmbientIntensity");
            shininessLabel.setText("backShininess");
            transparencyLabel.setText("backTransparency");
        }
        material.setContent(content);
        initializeUniversalMediaSelection();
        viewPanel.setToolTipText("This view panel shows example Material effects");
        lightPanel.setToolTipText("This light only affects the Material view panel above, not the X3D scene");
        backgroundLabel.setToolTipText("This background color only affects the Material view panel above, not the X3D scene");
    }

    @Override
    public String getNameKey() {
        return "NAME_X3D_MATERIAL";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        material.setAmbientIntensity(nullTo0(ambientIntensityTF));
        material.setDiffuseColor0(nullTo0(diffuseColorRedTF));
        material.setDiffuseColor1(nullTo0(diffuseColorGreenTF));
        material.setDiffuseColor2(nullTo0(diffuseColorBlueTF));
        material.setEmissiveColor0(nullTo0(emissiveColorRedTF));
        material.setEmissiveColor1(nullTo0(emissiveColorGreenTF));
        material.setEmissiveColor2(nullTo0(emissiveColorBlueTF));
        material.setShininess(nullTo0(shininessTF));
        material.setSpecularColor0(nullTo0(specularColorRedTF));
        material.setSpecularColor1(nullTo0(specularColorGreenTF));
        material.setSpecularColor2(nullTo0(specularColorBlueTF));
        material.setTransparency(nullTo0(transparencyTF));
    }

    private Xj3dViewerPanel makeXj3D() {
        return new Xj3dViewerPanel();
    }

    public JComponent extractContent() {
        ((javax.swing.GroupLayout) getLayout()).removeLayoutComponent(masterSplitPane);
        return masterSplitPane;
    }

    public MATERIAL getMATERIAL() {
        return material;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        dEFUSEpan = getDEFUSEpanel();
        masterSplitPane = new javax.swing.JSplitPane();
        leftSplitPane = new javax.swing.JSplitPane();
        leftTopPanel = new javax.swing.JPanel();
        xj3dViewer = makeXj3D();
        xj3dViewer.initialize("/X3dExamples/MaterialExample.x3d");
        leftBottomPanel = new javax.swing.JPanel();
        panelsContainer = new javax.swing.JPanel();
        viewPanel = new javax.swing.JPanel();
        geometryTypeCombo = new javax.swing.JComboBox();
        axesCB = new javax.swing.JCheckBox();
        lightVectorCB = new javax.swing.JCheckBox();
        lightPanel = new javax.swing.JPanel();
        directionalLightOnLabel = new javax.swing.JLabel();
        directionalLightOnCB = new javax.swing.JCheckBox();
        directionalLightColorLabel = new javax.swing.JLabel();
        directionalLightColorRedTF = new javax.swing.JFormattedTextField();
        directionalLightColorGreenTF = new javax.swing.JFormattedTextField();
        directionalLightColorBlueTF = new javax.swing.JFormattedTextField();
        directionalLightColorChooser = new net.java.dev.colorchooser.ColorChooser();
        directionalLightDirectionLabel = new javax.swing.JLabel();
        directionalLightDirectionXTF = new javax.swing.JFormattedTextField();
        directionalLightDirectionYTF = new javax.swing.JFormattedTextField();
        directionalLightDirectionZTF = new javax.swing.JFormattedTextField();
        directionalLighttDirectionNormalizeButton = new javax.swing.JButton();
        directionalLightIntensityLabel = new javax.swing.JLabel();
        directionalLightIntensityTF = new javax.swing.JFormattedTextField();
        directionalLightIntensitySlider = new javax.swing.JSlider();
        directionalLightAmbientIntensityLabel = new javax.swing.JLabel();
        directionalLightAmbientIntensityTF = new javax.swing.JFormattedTextField();
        directionalLightAmbientIntensitySlider = new javax.swing.JSlider();
        BackgroundColorSeparator = new javax.swing.JSeparator();
        backgroundLabel = new javax.swing.JLabel();
        skyColorLabel = new javax.swing.JLabel();
        skyColorRedTF = new javax.swing.JFormattedTextField();
        skyColorGreenTF = new javax.swing.JFormattedTextField();
        skyColorBlueTF = new javax.swing.JFormattedTextField();
        skyColorChooser = new net.java.dev.colorchooser.ColorChooser();
        rightSplitPane = new javax.swing.JSplitPane();
        rightTopPan = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        materialFieldsPan = new javax.swing.JPanel();
        diffuseColorLabel = new javax.swing.JLabel();
        diffuseColorRedTF = new javax.swing.JFormattedTextField();
        diffuseColorGreenTF = new javax.swing.JFormattedTextField();
        diffuseColorBlueTF = new javax.swing.JFormattedTextField();
        diffuseColorChooser = new net.java.dev.colorchooser.ColorChooser();
        emissiveColorLabel = new javax.swing.JLabel();
        emissiveColorRedTF = new javax.swing.JFormattedTextField();
        emissiveColorGreenTF = new javax.swing.JFormattedTextField();
        emissiveColorBlueTF = new javax.swing.JFormattedTextField();
        emissiveColorChooser = new net.java.dev.colorchooser.ColorChooser();
        specularColorLabel = new javax.swing.JLabel();
        specularColorRedTF = new javax.swing.JFormattedTextField();
        specularColorGreenTF = new javax.swing.JFormattedTextField();
        specularColorBlueTF = new javax.swing.JFormattedTextField();
        specularColorChooser = new net.java.dev.colorchooser.ColorChooser();
        transparencyLabel = new javax.swing.JLabel();
        transparencyTF = new javax.swing.JFormattedTextField();
        transparencySlider = new javax.swing.JSlider();
        shininessLabel = new javax.swing.JLabel();
        shininessTF = new javax.swing.JFormattedTextField();
        shininessSlider = new javax.swing.JSlider();
        ambientIntensityLabel = new javax.swing.JLabel();
        ambientIntensityTF = new javax.swing.JFormattedTextField();
        ambientIntensitySlider = new javax.swing.JSlider();
        universalMediaSelectorPanel = new javax.swing.JPanel();
        universalMediaThemeLabel = new javax.swing.JLabel();
        universalMediaGroupCombo = new javax.swing.JComboBox();
        universalMediaMaterialSlider = new javax.swing.JSlider();
        universalMediaMaterialTF = new javax.swing.JFormattedTextField();
        srcTabbedPane = new javax.swing.JTabbedPane();
        x3dSrcPan = new javax.swing.JPanel();
        x3dSrcSP = new javax.swing.JScrollPane();
        x3dTextArea = new javax.swing.JTextArea();
        x3dvSrcPan = new javax.swing.JPanel();
        x3dvSrcSP = new javax.swing.JScrollPane();
        x3dvTextArea = new javax.swing.JTextArea();
        ecmaSrcPan = new javax.swing.JPanel();
        ecmaSrcSP = new javax.swing.JScrollPane();
        ecmascriptTextArea = new javax.swing.JTextArea();
        javaSrcPan = new javax.swing.JPanel();
        javaSrcSP = new javax.swing.JScrollPane();
        javaTextArea = new javax.swing.JTextArea();
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(dEFUSEpan, gridBagConstraints);
        masterSplitPane.setBorder(null);
        masterSplitPane.setDividerLocation(400);
        masterSplitPane.setResizeWeight(1.0);
        leftSplitPane.setBorder(null);
        leftSplitPane.setDividerLocation(280);
        leftSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        leftSplitPane.setResizeWeight(1.0);
        leftTopPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.leftTopPanel.border.title")));
        leftTopPanel.setLayout(new java.awt.BorderLayout());
        xj3dViewer.setLayout(new java.awt.BorderLayout());
        leftTopPanel.add(xj3dViewer, java.awt.BorderLayout.CENTER);
        leftSplitPane.setTopComponent(leftTopPanel);
        panelsContainer.setLayout(new java.awt.GridBagLayout());
        viewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.viewPan.border.title")));
        geometryTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Box", "Cone", "Cylinder", "Sphere" }));
        geometryTypeCombo.setSelectedIndex(3);
        geometryTypeCombo.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.geometryTypeCombo.toolTipText"));
        geometryTypeCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geometryTypeComboActionPerformed(evt);
            }
        });
        axesCB.setSelected(true);
        axesCB.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.jCheckBox2.text"));
        axesCB.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.axesCB.toolTipText"));
        axesCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        axesCB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                axesCBActionPerformed(evt);
            }
        });
        lightVectorCB.setSelected(true);
        lightVectorCB.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.jCheckBox3.text"));
        lightVectorCB.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.lightVectorCB.toolTipText"));
        lightVectorCB.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        lightVectorCB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightVectorCBActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewPanelLayout.createSequentialGroup().addContainerGap().addComponent(geometryTypeCombo, 0, 210, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(axesCB).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lightVectorCB).addContainerGap()));
        viewPanelLayout.setVerticalGroup(viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(viewPanelLayout.createSequentialGroup().addGroup(viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(geometryTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(axesCB).addComponent(lightVectorCB)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panelsContainer.add(viewPanel, gridBagConstraints);
        lightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.lightPan.border.border.title"))));
        lightPanel.setLayout(new java.awt.GridBagLayout());
        directionalLightOnLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        directionalLightOnLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.onLab.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightOnLabel, gridBagConstraints);
        directionalLightOnCB.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.jCheckBox1.text"));
        directionalLightOnCB.setMargin(new java.awt.Insets(0, 0, 0, 0));
        directionalLightOnCB.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directionalLightOnCBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightOnCB, gridBagConstraints);
        directionalLightColorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        directionalLightColorLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.colorLab.text"));
        directionalLightColorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightColorLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightColorLabel, gridBagConstraints);
        directionalLightColorRedTF.setColumns(3);
        directionalLightColorRedTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.colorRedTF.text"));
        directionalLightColorRedTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightColorRedTF.toolTipText"));
        directionalLightColorRedTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightColorRedTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightColorRedTF, gridBagConstraints);
        directionalLightColorGreenTF.setColumns(1);
        directionalLightColorGreenTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.colorGrnTF.text"));
        directionalLightColorGreenTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightColorGreenTF.toolTipText"));
        directionalLightColorGreenTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightColorGreenTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightColorGreenTF, gridBagConstraints);
        directionalLightColorBlueTF.setColumns(1);
        directionalLightColorBlueTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.colorBluTF.text"));
        directionalLightColorBlueTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightColorBlueTF.toolTipText"));
        directionalLightColorBlueTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightColorBlueTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightColorBlueTF, gridBagConstraints);
        directionalLightColorChooser.setMinimumSize(new java.awt.Dimension(15, 15));
        directionalLightColorChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directionalLightColorChooserActionPerformed(evt);
            }
        });
        directionalLightColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                directionalLightColorChooserPropertyChange(evt);
            }
        });
        javax.swing.GroupLayout directionalLightColorChooserLayout = new javax.swing.GroupLayout(directionalLightColorChooser);
        directionalLightColorChooser.setLayout(directionalLightColorChooserLayout);
        directionalLightColorChooserLayout.setHorizontalGroup(directionalLightColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        directionalLightColorChooserLayout.setVerticalGroup(directionalLightColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightColorChooser, gridBagConstraints);
        directionalLightDirectionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        directionalLightDirectionLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.dirLab.text"));
        directionalLightDirectionLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightDirectionXTF.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightDirectionLabel, gridBagConstraints);
        directionalLightDirectionXTF.setColumns(1);
        directionalLightDirectionXTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.dir0TF.text"));
        directionalLightDirectionXTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightDirectionXTF.toolTipText"));
        directionalLightDirectionXTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightDirectionXTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightDirectionXTF, gridBagConstraints);
        directionalLightDirectionYTF.setColumns(1);
        directionalLightDirectionYTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.dir1TF.text"));
        directionalLightDirectionYTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightDirectionXTF.toolTipText"));
        directionalLightDirectionYTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightDirectionYTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightDirectionYTF, gridBagConstraints);
        directionalLightDirectionZTF.setColumns(1);
        directionalLightDirectionZTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.dir2TF.text"));
        directionalLightDirectionZTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightDirectionXTF.toolTipText"));
        directionalLightDirectionZTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightDirectionZTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightDirectionZTF, gridBagConstraints);
        directionalLighttDirectionNormalizeButton.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLighttDirectionNormalizeButton.text"));
        directionalLighttDirectionNormalizeButton.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLighttDirectionNormalizeButton.toolTipText"));
        directionalLighttDirectionNormalizeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directionalLighttDirectionNormalizeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        lightPanel.add(directionalLighttDirectionNormalizeButton, gridBagConstraints);
        directionalLightIntensityLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        directionalLightIntensityLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.intensLab.text"));
        directionalLightIntensityLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightIntensityLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightIntensityLabel, gridBagConstraints);
        directionalLightIntensityTF.setColumns(1);
        directionalLightIntensityTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.intensTF.text"));
        directionalLightIntensityTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightIntensityTF.toolTipText"));
        directionalLightIntensityTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightIntensityTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directionalLightIntensityTFActionPerformed(evt);
            }
        });
        directionalLightIntensityTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightIntensityTF, gridBagConstraints);
        directionalLightIntensitySlider.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightIntensitySlider.toolTipText"));
        directionalLightIntensitySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                directionalLightIntensitySliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.66;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightIntensitySlider, gridBagConstraints);
        directionalLightAmbientIntensityLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        directionalLightAmbientIntensityLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.ambLab.text"));
        directionalLightAmbientIntensityLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightAmbientIntensityLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(directionalLightAmbientIntensityLabel, gridBagConstraints);
        directionalLightAmbientIntensityTF.setColumns(1);
        directionalLightAmbientIntensityTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.ambTF.text"));
        directionalLightAmbientIntensityTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightAmbientIntensityTF.toolTipText"));
        directionalLightAmbientIntensityTF.setMinimumSize(new java.awt.Dimension(50, 28));
        directionalLightAmbientIntensityTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directionalLightAmbientIntensityTFActionPerformed(evt);
            }
        });
        directionalLightAmbientIntensityTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 8, 3);
        lightPanel.add(directionalLightAmbientIntensityTF, gridBagConstraints);
        directionalLightAmbientIntensitySlider.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.directionalLightAmbientIntensitySlider.toolTipText"));
        directionalLightAmbientIntensitySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                directionalLightAmbientIntensitySliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.66;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 8, 3);
        lightPanel.add(directionalLightAmbientIntensitySlider, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        lightPanel.add(BackgroundColorSeparator, gridBagConstraints);
        backgroundLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.backgroundLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        lightPanel.add(backgroundLabel, gridBagConstraints);
        skyColorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        skyColorLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.skyLab.text"));
        skyColorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.skyColorLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 8, 3);
        lightPanel.add(skyColorLabel, gridBagConstraints);
        skyColorRedTF.setColumns(4);
        skyColorRedTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.skyTF0.text"));
        skyColorRedTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.skyColorRedTF.toolTipText"));
        skyColorRedTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 8, 3);
        lightPanel.add(skyColorRedTF, gridBagConstraints);
        skyColorGreenTF.setColumns(4);
        skyColorGreenTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.skyTF1.text"));
        skyColorGreenTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.skyColorGreenTF.toolTipText"));
        skyColorGreenTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 8, 3);
        lightPanel.add(skyColorGreenTF, gridBagConstraints);
        skyColorBlueTF.setColumns(4);
        skyColorBlueTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.skyTF2.text"));
        skyColorBlueTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.skyColorBlueTF.toolTipText"));
        skyColorBlueTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 8, 3);
        lightPanel.add(skyColorBlueTF, gridBagConstraints);
        skyColorChooser.setMaximumSize(new java.awt.Dimension(48, 24));
        skyColorChooser.setMinimumSize(new java.awt.Dimension(48, 24));
        skyColorChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skyColorChooserActionPerformed(evt);
            }
        });
        skyColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                skyColorChooserPropertyChange(evt);
            }
        });
        javax.swing.GroupLayout skyColorChooserLayout = new javax.swing.GroupLayout(skyColorChooser);
        skyColorChooser.setLayout(skyColorChooserLayout);
        skyColorChooserLayout.setHorizontalGroup(skyColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        skyColorChooserLayout.setVerticalGroup(skyColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        lightPanel.add(skyColorChooser, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panelsContainer.add(lightPanel, gridBagConstraints);
        javax.swing.GroupLayout leftBottomPanelLayout = new javax.swing.GroupLayout(leftBottomPanel);
        leftBottomPanel.setLayout(leftBottomPanelLayout);
        leftBottomPanelLayout.setHorizontalGroup(leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE).addGroup(leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(leftBottomPanelLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(panelsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addGap(0, 0, 0))));
        leftBottomPanelLayout.setVerticalGroup(leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 370, Short.MAX_VALUE).addGroup(leftBottomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(leftBottomPanelLayout.createSequentialGroup().addGap(0, 0, 0).addComponent(panelsContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE).addGap(0, 0, 0))));
        leftSplitPane.setRightComponent(leftBottomPanel);
        masterSplitPane.setLeftComponent(leftSplitPane);
        rightSplitPane.setBorder(null);
        rightSplitPane.setDividerLocation(300);
        rightSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        rightTopPan.setLayout(new java.awt.BorderLayout());
        jPanel2.setLayout(new java.awt.GridBagLayout());
        materialFieldsPan.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.materialFieldsPan.border.title")));
        materialFieldsPan.setLayout(new java.awt.GridBagLayout());
        diffuseColorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        diffuseColorLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.diffLab.text"));
        diffuseColorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.diffuseColorLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(diffuseColorLabel, gridBagConstraints);
        diffuseColorRedTF.setColumns(3);
        diffuseColorRedTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.diffuseColorRedTF.text"));
        diffuseColorRedTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.diffuseColorRedTF.toolTipText"));
        diffuseColorRedTF.setMinimumSize(new java.awt.Dimension(6, 15));
        diffuseColorRedTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(diffuseColorRedTF, gridBagConstraints);
        diffuseColorGreenTF.setColumns(3);
        diffuseColorGreenTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.diffuseColorGreenTF.text"));
        diffuseColorGreenTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.diffuseColorGreenTF.toolTipText"));
        diffuseColorGreenTF.setMinimumSize(new java.awt.Dimension(6, 15));
        diffuseColorGreenTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(diffuseColorGreenTF, gridBagConstraints);
        diffuseColorBlueTF.setColumns(3);
        diffuseColorBlueTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.diffuseColorBlueTF.text"));
        diffuseColorBlueTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.diffuseColorBlueTF.toolTipText"));
        diffuseColorBlueTF.setMinimumSize(new java.awt.Dimension(6, 15));
        diffuseColorBlueTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(diffuseColorBlueTF, gridBagConstraints);
        diffuseColorChooser.setMinimumSize(new java.awt.Dimension(15, 15));
        diffuseColorChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diffuseColorChooserActionPerformed(evt);
            }
        });
        diffuseColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                diffuseColorChooserPropertyChange(evt);
            }
        });
        javax.swing.GroupLayout diffuseColorChooserLayout = new javax.swing.GroupLayout(diffuseColorChooser);
        diffuseColorChooser.setLayout(diffuseColorChooserLayout);
        diffuseColorChooserLayout.setHorizontalGroup(diffuseColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        diffuseColorChooserLayout.setVerticalGroup(diffuseColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(diffuseColorChooser, gridBagConstraints);
        emissiveColorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        emissiveColorLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.emisLab.text"));
        emissiveColorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.emissiveColorLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(emissiveColorLabel, gridBagConstraints);
        emissiveColorRedTF.setColumns(3);
        emissiveColorRedTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.emissiveColorRedTF.text"));
        emissiveColorRedTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.emissiveColorRedTF.toolTipText"));
        emissiveColorRedTF.setMinimumSize(new java.awt.Dimension(6, 15));
        emissiveColorRedTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(emissiveColorRedTF, gridBagConstraints);
        emissiveColorGreenTF.setColumns(3);
        emissiveColorGreenTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.emissiveColorGreenTF.text"));
        emissiveColorGreenTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.emissiveColorGreenTF.toolTipText"));
        emissiveColorGreenTF.setMinimumSize(new java.awt.Dimension(6, 15));
        emissiveColorGreenTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(emissiveColorGreenTF, gridBagConstraints);
        emissiveColorBlueTF.setColumns(3);
        emissiveColorBlueTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.emissiveColorBlueTF.text"));
        emissiveColorBlueTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.emissiveColorBlueTF.toolTipText"));
        emissiveColorBlueTF.setMinimumSize(new java.awt.Dimension(6, 15));
        emissiveColorBlueTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(emissiveColorBlueTF, gridBagConstraints);
        emissiveColorChooser.setMinimumSize(new java.awt.Dimension(15, 15));
        emissiveColorChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emissiveColorChooserActionPerformed(evt);
            }
        });
        emissiveColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                emissiveColorChooserPropertyChange(evt);
            }
        });
        javax.swing.GroupLayout emissiveColorChooserLayout = new javax.swing.GroupLayout(emissiveColorChooser);
        emissiveColorChooser.setLayout(emissiveColorChooserLayout);
        emissiveColorChooserLayout.setHorizontalGroup(emissiveColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        emissiveColorChooserLayout.setVerticalGroup(emissiveColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(emissiveColorChooser, gridBagConstraints);
        specularColorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        specularColorLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.specLab.text"));
        specularColorLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.specularColorLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(specularColorLabel, gridBagConstraints);
        specularColorRedTF.setColumns(3);
        specularColorRedTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.specularColorRedTF.text"));
        specularColorRedTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.specularColorRedTF.toolTipText"));
        specularColorRedTF.setMinimumSize(new java.awt.Dimension(6, 15));
        specularColorRedTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(specularColorRedTF, gridBagConstraints);
        specularColorGreenTF.setColumns(3);
        specularColorGreenTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.specularColorGreenTF.text"));
        specularColorGreenTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.specularColorGreenTF.toolTipText"));
        specularColorGreenTF.setMinimumSize(new java.awt.Dimension(6, 15));
        specularColorGreenTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(specularColorGreenTF, gridBagConstraints);
        specularColorBlueTF.setColumns(3);
        specularColorBlueTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.specularColorBlueTF.text"));
        specularColorBlueTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.specularColorBlueTF.toolTipText"));
        specularColorBlueTF.setMinimumSize(new java.awt.Dimension(6, 15));
        specularColorBlueTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(specularColorBlueTF, gridBagConstraints);
        specularColorChooser.setMinimumSize(new java.awt.Dimension(15, 15));
        specularColorChooser.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specularColorChooserActionPerformed(evt);
            }
        });
        specularColorChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                specularColorChooserPropertyChange(evt);
            }
        });
        javax.swing.GroupLayout specularColorChooserLayout = new javax.swing.GroupLayout(specularColorChooser);
        specularColorChooser.setLayout(specularColorChooserLayout);
        specularColorChooserLayout.setHorizontalGroup(specularColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        specularColorChooserLayout.setVerticalGroup(specularColorChooserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 22, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(specularColorChooser, gridBagConstraints);
        transparencyLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        transparencyLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.transLab.text"));
        transparencyLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.transparencyLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(transparencyLabel, gridBagConstraints);
        transparencyTF.setColumns(3);
        transparencyTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.transparencyTF.text"));
        transparencyTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.transparencyTF.toolTipText"));
        transparencyTF.setMinimumSize(new java.awt.Dimension(6, 15));
        transparencyTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transparencyTFActionPerformed(evt);
            }
        });
        transparencyTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(transparencyTF, gridBagConstraints);
        transparencySlider.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.transparencySlider.toolTipText"));
        transparencySlider.setMaximumSize(new java.awt.Dimension(100, 25));
        transparencySlider.setPreferredSize(new java.awt.Dimension(100, 25));
        transparencySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transparencySliderHandler(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.67;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(transparencySlider, gridBagConstraints);
        shininessLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        shininessLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.shinLab.text"));
        shininessLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.shininessLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(shininessLabel, gridBagConstraints);
        shininessTF.setColumns(3);
        shininessTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.shininessTF.text"));
        shininessTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.shininessTF.toolTipText"));
        shininessTF.setMinimumSize(new java.awt.Dimension(6, 15));
        shininessTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shininessTFActionPerformed(evt);
            }
        });
        shininessTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(shininessTF, gridBagConstraints);
        shininessSlider.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.shininessSlider.toolTipText"));
        shininessSlider.setMaximumSize(new java.awt.Dimension(100, 25));
        shininessSlider.setPreferredSize(new java.awt.Dimension(100, 25));
        shininessSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                shininessSliderHandler(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.67;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(shininessSlider, gridBagConstraints);
        ambientIntensityLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ambientIntensityLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.ambLabMat.text"));
        ambientIntensityLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.ambientIntensityLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(ambientIntensityLabel, gridBagConstraints);
        ambientIntensityTF.setColumns(3);
        ambientIntensityTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.ambientIntensityTF.text"));
        ambientIntensityTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.ambientIntensityTF.toolTipText"));
        ambientIntensityTF.setMinimumSize(new java.awt.Dimension(6, 15));
        ambientIntensityTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ambientIntensityTFActionPerformed(evt);
            }
        });
        ambientIntensityTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(ambientIntensityTF, gridBagConstraints);
        ambientIntensitySlider.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.ambientIntensitySlider.toolTipText"));
        ambientIntensitySlider.setMaximumSize(new java.awt.Dimension(100, 25));
        ambientIntensitySlider.setPreferredSize(new java.awt.Dimension(100, 25));
        ambientIntensitySlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ambientIntensitySliderHandler(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.67;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        materialFieldsPan.add(ambientIntensitySlider, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(materialFieldsPan, gridBagConstraints);
        universalMediaSelectorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.universalMediaSelectorPanel.border.title")));
        universalMediaSelectorPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        universalMediaSelectorPanel.setLayout(new java.awt.GridBagLayout());
        universalMediaThemeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        universalMediaThemeLabel.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.selectLab.text"));
        universalMediaThemeLabel.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.universalMediaThemeLabel.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        universalMediaSelectorPanel.add(universalMediaThemeLabel, gridBagConstraints);
        universalMediaGroupCombo.setModel(new DefaultComboBoxModel(getMediaGroupList()));
        universalMediaGroupCombo.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.universalMediaGroupCombo.toolTipText"));
        universalMediaGroupCombo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                universalMediaGroupComboActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.33;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        universalMediaSelectorPanel.add(universalMediaGroupCombo, gridBagConstraints);
        universalMediaMaterialSlider.setMaximum(34);
        universalMediaMaterialSlider.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.universalMediaMaterialSlider.toolTipText"));
        universalMediaMaterialSlider.setValue(0);
        universalMediaMaterialSlider.setEnabled(false);
        universalMediaMaterialSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                universalMediaMaterialSelectSliderStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.67;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        universalMediaSelectorPanel.add(universalMediaMaterialSlider, gridBagConstraints);
        universalMediaMaterialTF.setColumns(3);
        universalMediaMaterialTF.setEditable(false);
        universalMediaMaterialTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        universalMediaMaterialTF.setText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.universalMediaMaterialTF.text"));
        universalMediaMaterialTF.setToolTipText(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "MATERIALCustomizer.universalMediaMaterialTF.toolTipText"));
        universalMediaMaterialTF.setEnabled(false);
        universalMediaMaterialTF.setMinimumSize(new java.awt.Dimension(39, 22));
        universalMediaMaterialTF.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                globalPropertyChangeListener(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        universalMediaSelectorPanel.add(universalMediaMaterialTF, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(universalMediaSelectorPanel, gridBagConstraints);
        rightTopPan.add(jPanel2, java.awt.BorderLayout.CENTER);
        rightSplitPane.setTopComponent(rightTopPan);
        srcTabbedPane.setMaximumSize(new java.awt.Dimension(90, 60));
        x3dSrcSP.setBorder(null);
        x3dTextArea.setColumns(20);
        x3dTextArea.setEditable(false);
        x3dTextArea.setFont(new Font("MonoSpaced", getFont().getStyle(), getFont().getSize()));
        x3dTextArea.setRows(5);
        x3dSrcSP.setViewportView(x3dTextArea);
        javax.swing.GroupLayout x3dSrcPanLayout = new javax.swing.GroupLayout(x3dSrcPan);
        x3dSrcPan.setLayout(x3dSrcPanLayout);
        x3dSrcPanLayout.setHorizontalGroup(x3dSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(x3dSrcSP, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE));
        x3dSrcPanLayout.setVerticalGroup(x3dSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(x3dSrcSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE));
        srcTabbedPane.addTab(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.x3dSrcPan.TabConstraints.tabTitle"), x3dSrcPan);
        x3dvSrcSP.setBorder(null);
        x3dvTextArea.setColumns(20);
        x3dvTextArea.setEditable(false);
        x3dvTextArea.setFont(new Font("MonoSpaced", getFont().getStyle(), getFont().getSize()));
        x3dvTextArea.setRows(5);
        x3dvSrcSP.setViewportView(x3dvTextArea);
        javax.swing.GroupLayout x3dvSrcPanLayout = new javax.swing.GroupLayout(x3dvSrcPan);
        x3dvSrcPan.setLayout(x3dvSrcPanLayout);
        x3dvSrcPanLayout.setHorizontalGroup(x3dvSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(x3dvSrcSP, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE));
        x3dvSrcPanLayout.setVerticalGroup(x3dvSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(x3dvSrcSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE));
        srcTabbedPane.addTab(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.x3dvSrcPan.TabConstraints.tabTitle"), x3dvSrcPan);
        ecmaSrcSP.setBorder(null);
        ecmascriptTextArea.setColumns(20);
        ecmascriptTextArea.setEditable(false);
        ecmascriptTextArea.setFont(new Font("MonoSpaced", getFont().getStyle(), getFont().getSize()));
        ecmascriptTextArea.setRows(5);
        ecmaSrcSP.setViewportView(ecmascriptTextArea);
        javax.swing.GroupLayout ecmaSrcPanLayout = new javax.swing.GroupLayout(ecmaSrcPan);
        ecmaSrcPan.setLayout(ecmaSrcPanLayout);
        ecmaSrcPanLayout.setHorizontalGroup(ecmaSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(ecmaSrcSP, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE));
        ecmaSrcPanLayout.setVerticalGroup(ecmaSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(ecmaSrcSP, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE));
        srcTabbedPane.addTab(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.ecmaSrcPan.TabConstraints.tabTitle"), ecmaSrcPan);
        javaSrcSP.setBorder(null);
        javaTextArea.setColumns(20);
        javaTextArea.setEditable(false);
        javaTextArea.setFont(new Font("MonoSpaced", getFont().getStyle(), getFont().getSize()));
        javaTextArea.setRows(5);
        javaSrcSP.setViewportView(javaTextArea);
        javax.swing.GroupLayout javaSrcPanLayout = new javax.swing.GroupLayout(javaSrcPan);
        javaSrcPan.setLayout(javaSrcPanLayout);
        javaSrcPanLayout.setHorizontalGroup(javaSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(javaSrcSP, javax.swing.GroupLayout.DEFAULT_SIZE, 517, Short.MAX_VALUE));
        javaSrcPanLayout.setVerticalGroup(javaSrcPanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(javaSrcSP, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE));
        srcTabbedPane.addTab(org.openide.util.NbBundle.getMessage(MATERIALCustomizer.class, "NewJPanel.javaSrcPan.TabConstraints.tabTitle"), javaSrcPan);
        rightSplitPane.setRightComponent(srcTabbedPane);
        masterSplitPane.setRightComponent(rightSplitPane);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(masterSplitPane, gridBagConstraints);
    }

    private void geometryTypeComboActionPerformed(java.awt.event.ActionEvent evt) {
        x3dHelper.setGeometryField((String) geometryTypeCombo.getSelectedItem());
    }

    private void universalMediaGroupComboActionPerformed(java.awt.event.ActionEvent evt) {
        mediaSliderDisablementLocked = true;
        globalPropertyChangeListener(new PropertyChangeEvent(universalMediaGroupCombo, "value", null, null));
        mediaSliderDisablementLocked = false;
    }

    private void axesCBActionPerformed(java.awt.event.ActionEvent evt) {
        x3dHelper.setShowAxes(axesCB.isSelected());
    }

    private void lightVectorCBActionPerformed(java.awt.event.ActionEvent evt) {
        x3dHelper.setShowLightVector(lightVectorCB.isSelected());
    }

    private void directionalLightOnCBActionPerformed(java.awt.event.ActionEvent evt) {
        x3dHelper.setLightOn(directionalLightOnCB.isSelected());
    }

    private void transparencySliderHandler(javax.swing.event.ChangeEvent evt) {
        int value = transparencySlider.getValue();
        transparencyTF.setValue((float) value / 100.0f);
        disableUniversalMediaSliders();
    }

    private void shininessSliderHandler(javax.swing.event.ChangeEvent evt) {
        int val = shininessSlider.getValue();
        shininessTF.setValue((float) val / 100.0f);
        disableUniversalMediaSliders();
    }

    private void ambientIntensitySliderHandler(javax.swing.event.ChangeEvent evt) {
        int value = ambientIntensitySlider.getValue();
        ambientIntensityTF.setValue((float) value / 100.0f);
        disableUniversalMediaSliders();
    }

    private void directionalLightIntensitySliderStateChanged(javax.swing.event.ChangeEvent evt) {
        int value = directionalLightIntensitySlider.getValue();
        directionalLightIntensityTF.setValue((float) value / 100.0f);
    }

    private void directionalLightAmbientIntensitySliderStateChanged(javax.swing.event.ChangeEvent evt) {
        int val = directionalLightAmbientIntensitySlider.getValue();
        directionalLightAmbientIntensityTF.setValue((float) val / 100.0f);
    }

    private void universalMediaMaterialSelectSliderStateChanged(javax.swing.event.ChangeEvent evt) {
        mediaSliderDisablementLocked = true;
        int val = universalMediaMaterialSlider.getValue();
        universalMediaMaterialTF.setValue(val);
        mediaSliderDisablementLocked = false;
    }

    private void diffuseColorChooserActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = diffuseColorChooser.getColor();
        setAColor(c, diffuseColorTFArray);
        disableUniversalMediaSliders();
    }

    private void emissiveColorChooserActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = emissiveColorChooser.getColor();
        setAColor(c, emissiveTFArray);
        disableUniversalMediaSliders();
    }

    private void specularColorChooserActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = specularColorChooser.getColor();
        setAColor(c, specularTFArray);
        disableUniversalMediaSliders();
    }

    private void directionalLightColorChooserActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = directionalLightColorChooser.getColor();
        setAColor(c, dLightColorTFArray);
    }

    private void skyColorChooserActionPerformed(java.awt.event.ActionEvent evt) {
        Color c = skyColorChooser.getColor();
        setAColor(c, skyColorTFArray);
    }

    private void diffuseColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {
        cChooserPropertyChange(evt, diffuseColorTFArray);
    }

    private void skyColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {
        cChooserPropertyChange(evt, skyColorTFArray);
    }

    private void directionalLightColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {
        cChooserPropertyChange(evt, dLightColorTFArray);
    }

    private void emissiveColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {
        cChooserPropertyChange(evt, emissiveTFArray);
    }

    private void specularColorChooserPropertyChange(java.beans.PropertyChangeEvent evt) {
        cChooserPropertyChange(evt, specularTFArray);
    }

    private void cChooserPropertyChange(java.beans.PropertyChangeEvent evt, JFormattedTextField[] rgbTF) {
        if (evt.getPropertyName().equals(net.java.dev.colorchooser.ColorChooser.PROP_TRANSIENT_COLOR) || evt.getPropertyName().equals(net.java.dev.colorchooser.ColorChooser.PROP_COLOR)) {
            setAColor((Color) evt.getNewValue(), rgbTF);
        }
    }

    private void disableUniversalMediaSliders() {
        if (!mediaSliderDisablementLocked) {
            if (universalMediaGroupCombo.getSelectedIndex() != MEDIA_NONE_IDX) universalMediaGroupCombo.setSelectedIndex(MEDIA_NONE_IDX);
            if (universalMediaMaterialSlider.isEnabled()) {
                universalMediaMaterialSlider.setValue(0);
                universalMediaMaterialSlider.setEnabled(false);
            }
            if (universalMediaMaterialTF.isEnabled()) universalMediaMaterialTF.setEnabled(false);
        }
    }

    private void enableUniversalMediaSliders() {
        if (!universalMediaMaterialSlider.isEnabled()) universalMediaMaterialSlider.setEnabled(true);
        if (!universalMediaMaterialTF.isEnabled()) universalMediaMaterialTF.setEnabled(true);
    }

    private void updateAllXj3dMaterialFields() {
        x3dHelper.setDiffuseColor(nullTo0(diffuseColorRedTF), nullTo0(diffuseColorGreenTF), nullTo0(diffuseColorBlueTF));
        x3dHelper.setEmissiveColor(nullTo0(emissiveColorRedTF), nullTo0(emissiveColorGreenTF), nullTo0(emissiveColorBlueTF));
        x3dHelper.setSpecularColor(nullTo0(specularColorRedTF), nullTo0(specularColorGreenTF), nullTo0(specularColorBlueTF));
        x3dHelper.setTransparency(nullTo0(transparencyTF));
        x3dHelper.setAmbientIntensity(nullTo0(ambientIntensityTF));
        x3dHelper.setShininess(nullTo0(shininessTF));
    }

    private void updateAllXj3dLightingFields() {
        x3dHelper.setDirectionalLightDirection(nullTo0(directionalLightDirectionXTF), nullTo0(directionalLightDirectionYTF), nullTo0(directionalLightDirectionZTF));
        x3dHelper.setDirectionalLightColor(nullTo0(directionalLightColorRedTF), nullTo0(directionalLightColorGreenTF), nullTo0(directionalLightColorBlueTF));
        x3dHelper.setSkyColor(nullTo0(skyColorRedTF), nullTo0(skyColorGreenTF), nullTo0(skyColorBlueTF));
        x3dHelper.setDirectionalLightIntensity(nullTo0(directionalLightIntensityTF));
        x3dHelper.setDirectionalLightAmbientIntensity(nullTo0(directionalLightAmbientIntensityTF));
    }

    /**
   * double-check event handling to ensure that all sliders are consistent with all text fields, updating Xj3D and source-text panes also
   * @param evt 
   */
    private void globalPropertyChangeListener(java.beans.PropertyChangeEvent evt) {
        if (!"value".equals(evt.getPropertyName())) {
            return;
        }
        Object src = evt.getSource();
        if (src == directionalLightDirectionXTF || src == directionalLightDirectionYTF || src == directionalLightDirectionZTF) {
            x3dHelper.setDirectionalLightDirection(nullTo0(directionalLightDirectionXTF), nullTo0(directionalLightDirectionYTF), nullTo0(directionalLightDirectionZTF));
        } else if (src == directionalLightColorRedTF || src == directionalLightColorGreenTF || src == directionalLightColorBlueTF) {
            x3dHelper.setDirectionalLightColor(nullTo0(directionalLightColorRedTF), nullTo0(directionalLightColorGreenTF), nullTo0(directionalLightColorBlueTF));
            updateDirectionalLightColorChooser();
        } else if (src == directionalLightIntensityTF) {
            x3dHelper.setDirectionalLightIntensity(nullTo0(directionalLightIntensityTF));
            double value = Double.parseDouble(nullTo0(directionalLightIntensityTF));
            directionalLightIntensitySlider.setValue((int) (value * 100.0));
        } else if (src == directionalLightAmbientIntensityTF) {
            x3dHelper.setDirectionalLightAmbientIntensity(nullTo0(directionalLightAmbientIntensityTF));
            double value = Double.parseDouble(nullTo0(directionalLightAmbientIntensityTF));
            directionalLightAmbientIntensitySlider.setValue((int) (value * 100.0));
        } else if (src == skyColorRedTF || src == skyColorGreenTF || src == skyColorBlueTF) {
            x3dHelper.setSkyColor(nullTo0(skyColorRedTF), nullTo0(skyColorGreenTF), nullTo0(skyColorBlueTF));
            updateSkyColorChooser();
        } else if (src == universalMediaGroupCombo) {
            if (MEDIA_NONE_ID.equals((String) universalMediaGroupCombo.getSelectedItem())) {
                disableUniversalMediaSliders();
            } else {
                enableUniversalMediaSliders();
            }
            handleUniversalMediaChange();
        } else if (src == universalMediaMaterialTF) {
            handleUniversalMediaChange();
        } else {
            material.setContent("");
            disableUniversalMediaSliders();
            if (src == diffuseColorRedTF || src == diffuseColorGreenTF || src == diffuseColorBlueTF) {
                x3dHelper.setDiffuseColor(nullTo0(diffuseColorRedTF), nullTo0(diffuseColorGreenTF), nullTo0(diffuseColorBlueTF));
                updateDiffuseColorChooser();
            } else if (src == emissiveColorRedTF || src == emissiveColorGreenTF || src == emissiveColorBlueTF) {
                x3dHelper.setEmissiveColor(nullTo0(emissiveColorRedTF), nullTo0(emissiveColorGreenTF), nullTo0(emissiveColorBlueTF));
                updateEmissiveColorChooser();
            } else if (src == specularColorRedTF || src == specularColorGreenTF || src == specularColorBlueTF) {
                x3dHelper.setSpecularColor(nullTo0(specularColorRedTF), nullTo0(specularColorGreenTF), nullTo0(specularColorBlueTF));
                updateSpecularColorChooser();
            } else if (src == transparencyTF) {
                x3dHelper.setTransparency(nullTo0(transparencyTF));
                double value = Double.parseDouble(nullTo0(transparencyTF));
                transparencySlider.setValue((int) (value * 100.0));
            } else if (src == ambientIntensityTF) {
                x3dHelper.setAmbientIntensity(nullTo0(ambientIntensityTF));
                double value = Double.parseDouble(nullTo0(ambientIntensityTF));
                ambientIntensitySlider.setValue((int) (value * 100.0));
            } else if (src == shininessTF) {
                x3dHelper.setShininess(nullTo0(shininessTF));
                double value = Double.parseDouble(nullTo0(shininessTF));
                shininessSlider.setValue((int) (value * 100.0));
            }
        }
        updateAllXj3dLightingFields();
        updateAllXj3dMaterialFields();
        x3dTextArea.setText(x3dEncoding());
        x3dvTextArea.setText(x3dvEncoding());
    }

    private void directionalLighttDirectionNormalizeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        double x = Double.parseDouble(nullTo0(directionalLightDirectionXTF));
        double y = Double.parseDouble(nullTo0(directionalLightDirectionYTF));
        double z = Double.parseDouble(nullTo0(directionalLightDirectionZTF));
        double r = Math.sqrt(x * x + y * y + z * z);
        if (r == 0) {
            directionalLightDirectionXTF.setText("" + lightDirectionDefault[0]);
            directionalLightDirectionYTF.setText("" + lightDirectionDefault[1]);
            directionalLightDirectionZTF.setText("" + lightDirectionDefault[2]);
        } else {
            directionalLightDirectionXTF.setValue("" + Math.floor((x / r) * 10000.) / 10000.);
            directionalLightDirectionYTF.setValue("" + Math.floor((y / r) * 10000.) / 10000.);
            directionalLightDirectionZTF.setValue("" + Math.floor((z / r) * 10000.) / 10000.);
        }
    }

    private void directionalLightIntensityTFActionPerformed(java.awt.event.ActionEvent evt) {
        double value = Double.parseDouble(nullTo0(directionalLightIntensityTF));
        directionalLightIntensitySlider.setValue((int) (value * 100.0));
        disableUniversalMediaSliders();
    }

    private void shininessTFActionPerformed(java.awt.event.ActionEvent evt) {
        double value = Double.parseDouble(nullTo0(shininessTF));
        shininessSlider.setValue((int) (value * 100.0));
        disableUniversalMediaSliders();
    }

    private void ambientIntensityTFActionPerformed(java.awt.event.ActionEvent evt) {
        double value = Double.parseDouble(nullTo0(ambientIntensityTF));
        ambientIntensitySlider.setValue((int) (value * 100.0));
        disableUniversalMediaSliders();
    }

    private void transparencyTFActionPerformed(java.awt.event.ActionEvent evt) {
        double value = Double.parseDouble(nullTo0(transparencyTF));
        transparencySlider.setValue((int) (value * 100.0));
        disableUniversalMediaSliders();
    }

    private void directionalLightAmbientIntensityTFActionPerformed(java.awt.event.ActionEvent evt) {
        double value = Double.parseDouble(nullTo0(directionalLightAmbientIntensityTF));
        directionalLightAmbientIntensitySlider.setValue((int) (value * 100.0));
        disableUniversalMediaSliders();
    }

    private void handleUniversalMediaChange() {
        libraryChoice = (String) universalMediaGroupCombo.getSelectedItem();
        libraryIndex = universalMediaGroupCombo.getSelectedIndex();
        if (!libraryChoice.equals(MEDIA_NONE_ID)) {
            int materialVal = universalMediaMaterialSlider.getValue();
            UniversalMediaMaterials newMaterial = materialFinder.getMaterial(libraryChoice, materialVal);
            setMaterialFields(newMaterial);
            updateMediaChoosers();
            updateAllXj3dMaterialFields();
            String frontBackNoneLabel = new String();
            if (material.isFront()) frontBackNoneLabel = "frontMaterial "; else if (material.isBack()) frontBackNoneLabel = " backMaterial ";
            material.setContent("\n\t\t\t" + UNIVERSAL_MEDIA_COMMENT_HEADER1 + frontBackNoneLabel + UNIVERSAL_MEDIA_COMMENT_HEADER2 + UniversalMediaMaterialFinder.MATERIAL_UNIVERSAL_MEDIA_CHOICES[libraryIndex - 1] + " " + materialVal + " -->\n\t\t");
        } else material.setContent("");
    }

    private void updateMediaChoosers() {
        updateDiffuseColorChooser();
        updateEmissiveColorChooser();
        updateSpecularColorChooser();
    }

    private void updateDiffuseColorChooser() {
        diffuseColorChooser.setColor(new SFColor(diffuseColorRedTF.getText(), diffuseColorGreenTF.getText(), diffuseColorBlueTF.getText()).getColor());
    }

    private void updateEmissiveColorChooser() {
        emissiveColorChooser.setColor(new SFColor(emissiveColorRedTF.getText(), emissiveColorGreenTF.getText(), emissiveColorBlueTF.getText()).getColor());
    }

    private void updateSpecularColorChooser() {
        specularColorChooser.setColor(new SFColor(specularColorRedTF.getText(), specularColorGreenTF.getText(), specularColorBlueTF.getText()).getColor());
    }

    private void updateSkyColorChooser() {
        skyColorChooser.setColor(new SFColor(skyColorRedTF.getText(), skyColorGreenTF.getText(), skyColorBlueTF.getText()).getColor());
    }

    private void updateDirectionalLightColorChooser() {
        directionalLightColorChooser.setColor(new SFColor(directionalLightColorRedTF.getText(), directionalLightColorGreenTF.getText(), directionalLightColorBlueTF.getText()).getColor());
    }

    String nullTo0(JTextField tf) {
        String s = tf.getText().trim();
        return s.length() <= 0 ? "0" : s;
    }

    private void setAColor(Color c, JFormattedTextField[] rgbTF) {
        float[] fa = c.getRGBColorComponents(null);
        rgbTF[0].setValue("" + fa[0]);
        rgbTF[1].setValue("" + fa[1]);
        rgbTF[2].setValue("" + fa[2]);
    }

    private String[] getMediaGroupList() {
        UniversalMediaGroup[] grps = UniversalMediaGroup.values();
        String[] sa = new String[grps.length + 1];
        sa[0] = MEDIA_NONE_ID;
        int i = 1;
        for (UniversalMediaGroup umg : grps) {
            sa[i] = umg.name();
            i++;
        }
        return sa;
    }

    private String getUniversalMediaGroupRegEx() {
        StringBuilder sb = new StringBuilder();
        if (material.isFront()) sb.append(UNIVERSAL_MEDIA_COMMENT_HEADER_FRONT_REGEX); else if (material.isBack()) sb.append(UNIVERSAL_MEDIA_COMMENT_HEADER_BACK_REGEX); else sb.append(UNIVERSAL_MEDIA_COMMENT_HEADER_REGEX);
        sb.append('(');
        UniversalMediaGroup[] grps = UniversalMediaGroup.values();
        for (UniversalMediaGroup umg : grps) {
            sb.append(umg.name());
            sb.append('|');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(')');
        sb.append(UNIVERSAL_MEDIA_COMMENT_TAIL_REGEX);
        return sb.toString();
    }

    /** Create .x3d output string */
    private String x3dEncoding() {
        String DEFconstruct;
        if (DEFname.length() > 0) DEFconstruct = " DEF='" + DEFname + "'"; else DEFconstruct = "";
        String value = "<Material" + DEFconstruct + "\n     diffuseColor='" + nullTo0(diffuseColorRedTF) + " " + nullTo0(diffuseColorGreenTF) + " " + nullTo0(diffuseColorBlueTF) + "'\n    emissiveColor='" + nullTo0(emissiveColorRedTF) + " " + nullTo0(emissiveColorGreenTF) + " " + nullTo0(emissiveColorBlueTF) + "'\n    specularColor='" + nullTo0(specularColorRedTF) + " " + nullTo0(specularColorGreenTF) + " " + nullTo0(specularColorBlueTF) + "'\n     transparency='" + nullTo0(transparencyTF) + "'\n ambientIntensity='" + nullTo0(ambientIntensityTF) + "'\n        shininess='" + nullTo0(shininessTF) + "'\n   containerField='material'\n/>";
        return value;
    }

    /** Create .x3dv output string */
    private String x3dvEncoding() {
        String DEFconstruct;
        if (DEFname.length() > 0) DEFconstruct = " DEF " + DEFname; else DEFconstruct = "";
        String value = "material" + DEFconstruct + " Material {" + "\n     diffuseColor " + nullTo0(diffuseColorRedTF) + " " + nullTo0(diffuseColorGreenTF) + " " + nullTo0(diffuseColorBlueTF) + "\n    emissiveColor " + nullTo0(emissiveColorRedTF) + " " + nullTo0(emissiveColorGreenTF) + " " + nullTo0(emissiveColorBlueTF) + "\n    specularColor " + nullTo0(specularColorRedTF) + " " + nullTo0(specularColorGreenTF) + " " + nullTo0(specularColorBlueTF) + "\n     transparency " + nullTo0(transparencyTF) + "\n ambientIntensity " + nullTo0(ambientIntensityTF) + "\n        shininess " + nullTo0(shininessTF) + "\n}";
        return value;
    }

    private void setMaterialFields(UniversalMediaMaterials mat) {
        diffuseColorRedTF.setText("" + mat.diffuseColorRed());
        diffuseColorGreenTF.setText("" + mat.diffuseColorGreen());
        diffuseColorBlueTF.setText("" + mat.diffuseColorBlue());
        emissiveColorRedTF.setText("" + mat.emissiveColorRed());
        emissiveColorGreenTF.setText("" + mat.emissiveColorGreen());
        emissiveColorBlueTF.setText("" + mat.emissiveColorBlue());
        specularColorRedTF.setText("" + mat.specularColorRed());
        specularColorGreenTF.setText("" + mat.specularColorGreen());
        specularColorBlueTF.setText("" + mat.specularColorBlue());
        transparencyTF.setText("" + mat.transparency());
        ambientIntensityTF.setText("" + mat.ambientIntensity());
        shininessTF.setText("" + mat.shininess());
        float f = new SFFloat(mat.transparency()).getValue();
        transparencySlider.setValue((int) (f * 100.));
        f = new SFFloat(mat.ambientIntensity()).getValue();
        ambientIntensitySlider.setValue((int) (f * 100.));
        f = new SFFloat(mat.shininess()).getValue();
        shininessSlider.setValue((int) (f * 100.));
    }

    private void resetMaterialValues() {
        emissiveColorRedTF.setText(material.getEmissiveColor0());
        emissiveColorGreenTF.setText(material.getEmissiveColor1());
        emissiveColorBlueTF.setText(material.getEmissiveColor2());
        emissiveColorChooser.setColor(new SFColor(material.getEmissiveColor0(), material.getEmissiveColor1(), material.getEmissiveColor2()).getColor());
        diffuseColorRedTF.setText(material.getDiffuseColor0());
        diffuseColorGreenTF.setText(material.getDiffuseColor1());
        diffuseColorBlueTF.setText(material.getDiffuseColor2());
        diffuseColorChooser.setColor(new SFColor(material.getDiffuseColor0(), material.getDiffuseColor1(), material.getDiffuseColor2()).getColor());
        specularColorRedTF.setText(material.getSpecularColor0());
        specularColorGreenTF.setText(material.getSpecularColor1());
        specularColorBlueTF.setText(material.getSpecularColor2());
        specularColorChooser.setColor(new SFColor(material.getSpecularColor0(), material.getSpecularColor1(), material.getSpecularColor2()).getColor());
        transparencySlider.setValue((int) ((new SFFloat(material.getTransparency(), 0.0f, 1.0f, true)).getValue() * 100.0f));
        ambientIntensitySlider.setValue((int) ((new SFFloat(material.getAmbientIntensity(), 0.0f, 1.0f, true)).getValue() * 100.0f));
        shininessSlider.setValue((int) ((new SFFloat(material.getShininess(), 0.0f, 1.0f, true)).getValue() * 100.0f));
        transparencyTF.setText(material.getTransparency());
        ambientIntensityTF.setText(material.getAmbientIntensity());
        shininessTF.setText(material.getShininess());
    }

    private void initializeUniversalMediaSelection() {
        String comboVal;
        int sliderVal;
        try {
            String content = material.getContent();
            if (content.length() > 0) {
                Pattern pattern = Pattern.compile(getUniversalMediaGroupRegEx(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher matcher = pattern.matcher(content);
                if (matcher.matches()) {
                    int gc = matcher.groupCount();
                    if (gc >= 2) {
                        comboVal = matcher.group(1);
                        sliderVal = Integer.parseInt(matcher.group(2));
                        int count = universalMediaGroupCombo.getItemCount();
                        for (int i = 0; i < count; i++) {
                            if (comboVal.equalsIgnoreCase((String) universalMediaGroupCombo.getItemAt(i))) {
                                universalMediaGroupCombo.setSelectedIndex(i);
                                universalMediaMaterialSlider.setValue(sliderVal);
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
        }
        universalMediaGroupCombo.setSelectedItem(MEDIA_NONE_ID);
        universalMediaMaterialSlider.setValue(0);
    }

    private void initializeUniversalMediaSelectionUnused() {
        System.out.println("material.getContent().length()=" + material.getContent().length());
        if (material.getContent().length() == 0) return;
        System.out.println(material.getContent());
        if (!material.getContent().contains(UNIVERSAL_MEDIA_COMMENT_HEADER1)) {
            System.out.println("warning, unrecognized content, ignoring");
            return;
        }
        System.out.println("found header: " + UNIVERSAL_MEDIA_COMMENT_HEADER1);
        String remainder1 = material.getContent().trim().substring(UNIVERSAL_MEDIA_COMMENT_HEADER1.length());
        System.out.println("remainder1: " + remainder1);
        int nextSpaceIndex = remainder1.indexOf(" ");
        System.out.println("nextSpaceIndex for library name: " + nextSpaceIndex);
        if (nextSpaceIndex <= 0) return;
        String libraryName = remainder1.substring(0, nextSpaceIndex);
        System.out.println("libraryName: " + libraryName);
        String remainder2 = remainder1.substring(nextSpaceIndex);
        System.out.println("remainder2: " + remainder2);
        nextSpaceIndex = remainder2.indexOf(" ");
        System.out.println("nextSpaceIndex for library number: " + nextSpaceIndex);
        String libraryNumber = remainder2.substring(0, nextSpaceIndex);
        System.out.println("libraryNumber: " + libraryNumber);
        universalMediaGroupCombo.setSelectedItem(libraryName);
        universalMediaGroupCombo.setSelectedItem(libraryNumber);
    }

    private void resetLightingValues() {
        directionalLightOnCB.setSelected(lightOnDefault);
        float[] lightDefaultFloats = lightColorDefault.getRGBColorComponents(null);
        directionalLightColorRedTF.setText("" + lightDefaultFloats[0]);
        directionalLightColorGreenTF.setText("" + lightDefaultFloats[1]);
        directionalLightColorBlueTF.setText("" + lightDefaultFloats[2]);
        directionalLightColorChooser.setColor(lightColorDefault);
        directionalLightDirectionXTF.setText("" + lightDirectionDefault[0]);
        directionalLightDirectionYTF.setText("" + lightDirectionDefault[1]);
        directionalLightDirectionZTF.setText("" + lightDirectionDefault[2]);
        directionalLightIntensityTF.setText("" + lightIntensityDefault);
        directionalLightIntensitySlider.setValue((int) (lightIntensityDefault * 100.0));
        directionalLightAmbientIntensityTF.setText("" + lightAmbientIntensityDefault);
        directionalLightAmbientIntensitySlider.setValue((int) (lightAmbientIntensityDefault * 100.0));
        float[] skyColorDefaultFloats = skyColorDefault.getRGBColorComponents(null);
        skyColorRedTF.setText("" + skyColorDefaultFloats[0]);
        skyColorGreenTF.setText("" + skyColorDefaultFloats[1]);
        skyColorBlueTF.setText("" + skyColorDefaultFloats[2]);
        skyColorChooser.setColor(skyColorDefault);
        universalMediaGroupCombo.setSelectedIndex(MEDIA_NONE_IDX);
        universalMediaMaterialSlider.setValue(0);
        geometryTypeCombo.setSelectedItem(geometryTypeDefault);
        lightVectorCB.setSelected(true);
        axesCB.setSelected(true);
    }

    private javax.swing.JSeparator BackgroundColorSeparator;

    private javax.swing.JLabel ambientIntensityLabel;

    private javax.swing.JSlider ambientIntensitySlider;

    private javax.swing.JFormattedTextField ambientIntensityTF;

    private javax.swing.JCheckBox axesCB;

    private javax.swing.JLabel backgroundLabel;

    private org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpan;

    private javax.swing.JFormattedTextField diffuseColorBlueTF;

    private net.java.dev.colorchooser.ColorChooser diffuseColorChooser;

    private javax.swing.JFormattedTextField diffuseColorGreenTF;

    private javax.swing.JLabel diffuseColorLabel;

    private javax.swing.JFormattedTextField diffuseColorRedTF;

    private javax.swing.JLabel directionalLightAmbientIntensityLabel;

    private javax.swing.JSlider directionalLightAmbientIntensitySlider;

    private javax.swing.JFormattedTextField directionalLightAmbientIntensityTF;

    private javax.swing.JFormattedTextField directionalLightColorBlueTF;

    private net.java.dev.colorchooser.ColorChooser directionalLightColorChooser;

    private javax.swing.JFormattedTextField directionalLightColorGreenTF;

    private javax.swing.JLabel directionalLightColorLabel;

    private javax.swing.JFormattedTextField directionalLightColorRedTF;

    private javax.swing.JLabel directionalLightDirectionLabel;

    private javax.swing.JFormattedTextField directionalLightDirectionXTF;

    private javax.swing.JFormattedTextField directionalLightDirectionYTF;

    private javax.swing.JFormattedTextField directionalLightDirectionZTF;

    private javax.swing.JLabel directionalLightIntensityLabel;

    private javax.swing.JSlider directionalLightIntensitySlider;

    private javax.swing.JFormattedTextField directionalLightIntensityTF;

    private javax.swing.JCheckBox directionalLightOnCB;

    private javax.swing.JLabel directionalLightOnLabel;

    private javax.swing.JButton directionalLighttDirectionNormalizeButton;

    private javax.swing.JPanel ecmaSrcPan;

    private javax.swing.JScrollPane ecmaSrcSP;

    private javax.swing.JTextArea ecmascriptTextArea;

    private javax.swing.JFormattedTextField emissiveColorBlueTF;

    private net.java.dev.colorchooser.ColorChooser emissiveColorChooser;

    private javax.swing.JFormattedTextField emissiveColorGreenTF;

    private javax.swing.JLabel emissiveColorLabel;

    private javax.swing.JFormattedTextField emissiveColorRedTF;

    private javax.swing.JComboBox geometryTypeCombo;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel javaSrcPan;

    private javax.swing.JScrollPane javaSrcSP;

    private javax.swing.JTextArea javaTextArea;

    private javax.swing.JPanel leftBottomPanel;

    private javax.swing.JSplitPane leftSplitPane;

    private javax.swing.JPanel leftTopPanel;

    private javax.swing.JPanel lightPanel;

    private javax.swing.JCheckBox lightVectorCB;

    private javax.swing.JSplitPane masterSplitPane;

    private javax.swing.JPanel materialFieldsPan;

    private javax.swing.JPanel panelsContainer;

    private javax.swing.JSplitPane rightSplitPane;

    private javax.swing.JPanel rightTopPan;

    private javax.swing.JLabel shininessLabel;

    private javax.swing.JSlider shininessSlider;

    private javax.swing.JFormattedTextField shininessTF;

    private javax.swing.JFormattedTextField skyColorBlueTF;

    private net.java.dev.colorchooser.ColorChooser skyColorChooser;

    private javax.swing.JFormattedTextField skyColorGreenTF;

    private javax.swing.JLabel skyColorLabel;

    private javax.swing.JFormattedTextField skyColorRedTF;

    private javax.swing.JFormattedTextField specularColorBlueTF;

    private net.java.dev.colorchooser.ColorChooser specularColorChooser;

    private javax.swing.JFormattedTextField specularColorGreenTF;

    private javax.swing.JLabel specularColorLabel;

    private javax.swing.JFormattedTextField specularColorRedTF;

    private javax.swing.JTabbedPane srcTabbedPane;

    private javax.swing.JLabel transparencyLabel;

    private javax.swing.JSlider transparencySlider;

    private javax.swing.JFormattedTextField transparencyTF;

    private javax.swing.JComboBox universalMediaGroupCombo;

    private javax.swing.JSlider universalMediaMaterialSlider;

    private javax.swing.JFormattedTextField universalMediaMaterialTF;

    private javax.swing.JPanel universalMediaSelectorPanel;

    private javax.swing.JLabel universalMediaThemeLabel;

    private javax.swing.JPanel viewPanel;

    private javax.swing.JPanel x3dSrcPan;

    private javax.swing.JScrollPane x3dSrcSP;

    private javax.swing.JTextArea x3dTextArea;

    private javax.swing.JPanel x3dvSrcPan;

    private javax.swing.JScrollPane x3dvSrcSP;

    private javax.swing.JTextArea x3dvTextArea;

    private static org.web3d.x3d.xj3d.viewer.Xj3dViewerPanel xj3dViewer;

    private void adjustWidgetSizes() {
        Dimension d = new Dimension(directionalLightColorRedTF.getPreferredSize());
        ambientIntensityTF.setMinimumSize(d);
        diffuseColorBlueTF.setMinimumSize(d);
        diffuseColorGreenTF.setMinimumSize(d);
        diffuseColorRedTF.setMinimumSize(d);
        directionalLightAmbientIntensityTF.setMinimumSize(d);
        directionalLightColorBlueTF.setMinimumSize(d);
        directionalLightColorGreenTF.setMinimumSize(d);
        directionalLightColorRedTF.setMinimumSize(d);
        directionalLightDirectionXTF.setMinimumSize(d);
        directionalLightDirectionYTF.setMinimumSize(d);
        directionalLightDirectionZTF.setMinimumSize(d);
        directionalLightIntensityTF.setMinimumSize(d);
        emissiveColorBlueTF.setMinimumSize(d);
        emissiveColorGreenTF.setMinimumSize(d);
        emissiveColorRedTF.setMinimumSize(d);
        universalMediaMaterialTF.setMinimumSize(d);
        shininessTF.setMinimumSize(d);
        skyColorBlueTF.setMinimumSize(d);
        skyColorGreenTF.setMinimumSize(d);
        skyColorRedTF.setMinimumSize(d);
        specularColorBlueTF.setMinimumSize(d);
        specularColorGreenTF.setMinimumSize(d);
        specularColorRedTF.setMinimumSize(d);
        transparencyTF.setMinimumSize(d);
    }
}
