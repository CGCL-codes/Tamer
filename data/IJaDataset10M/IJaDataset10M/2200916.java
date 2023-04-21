package cz.cuni.mff.ksi.jinfer.treeruledisplayer.options;

import cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic.Utils;
import cz.cuni.mff.ksi.jinfer.base.objects.VertexShape;
import java.awt.Color;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import org.openide.util.NbPreferences;

/**
 * Options panel of the Advanced Rule Displayer module.
 *
 * @author sviro
 */
@SuppressWarnings("PMD.SingularField")
public final class TreeRuleDisplayerPanel extends JPanel {

    private static final long serialVersionUID = 43522352345l;

    public TreeRuleDisplayerPanel() {
        super();
        initComponents();
    }

    @SuppressWarnings("PMD")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        basic = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        backgroundColor = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        horizontalDistance = new javax.swing.JSpinner();
        verticalDistance = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        shape = new javax.swing.JPanel();
        tokenLabel = new javax.swing.JLabel();
        concatLabel = new javax.swing.JLabel();
        alterLabel = new javax.swing.JLabel();
        permutLabel = new javax.swing.JLabel();
        tokenCombo = new javax.swing.JComboBox();
        concatCombo = new javax.swing.JComboBox();
        alterCombo = new javax.swing.JComboBox();
        permutCombo = new javax.swing.JComboBox();
        tokenSpinner = new javax.swing.JSpinner();
        concatSpinner = new javax.swing.JSpinner();
        alterSpinner = new javax.swing.JSpinner();
        permutSpinner = new javax.swing.JSpinner();
        rootLabel = new javax.swing.JLabel();
        rootCombo = new javax.swing.JComboBox();
        rootSpinner = new javax.swing.JSpinner();
        rootColor = new javax.swing.JPanel();
        tokenColor = new javax.swing.JPanel();
        concatColor = new javax.swing.JPanel();
        alterColor = new javax.swing.JPanel();
        permutColor = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        fillH = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        lambdaCombo = new javax.swing.JComboBox();
        lambdaColor = new javax.swing.JPanel();
        lambdaSpinner = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        simpleDataCombo = new javax.swing.JComboBox();
        simpleDataSpinner = new javax.swing.JSpinner();
        simpleDataColor = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        attributeCombo = new javax.swing.JComboBox();
        attributeSpinner = new javax.swing.JSpinner();
        attributeColor = new javax.swing.JPanel();
        fillV = new javax.swing.JPanel();
        fill = new javax.swing.JPanel();
        setLayout(new java.awt.GridBagLayout());
        basic.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.basic.border.title")));
        basic.setLayout(new java.awt.GridBagLayout());
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel4.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        basic.add(jLabel4, gridBagConstraints);
        backgroundColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        backgroundColor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        backgroundColor.setMaximumSize(new java.awt.Dimension(20, 20));
        backgroundColor.setMinimumSize(new java.awt.Dimension(20, 20));
        backgroundColor.setPreferredSize(new java.awt.Dimension(20, 20));
        backgroundColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout backgroundColorLayout = new javax.swing.GroupLayout(backgroundColor);
        backgroundColor.setLayout(backgroundColorLayout);
        backgroundColorLayout.setHorizontalGroup(backgroundColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        backgroundColorLayout.setVerticalGroup(backgroundColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        basic.add(backgroundColor, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel5.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        basic.add(jLabel5, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel6.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        basic.add(jLabel6, gridBagConstraints);
        horizontalDistance.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(150), Integer.valueOf(10), null, Integer.valueOf(1)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        basic.add(horizontalDistance, gridBagConstraints);
        verticalDistance.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(150), Integer.valueOf(10), null, Integer.valueOf(1)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        basic.add(verticalDistance, gridBagConstraints);
        jLabel7.setFont(jLabel7.getFont().deriveFont((jLabel7.getFont().getStyle() | java.awt.Font.ITALIC), 10));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel7.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
        basic.add(jLabel7, gridBagConstraints);
        jLabel8.setFont(jLabel8.getFont().deriveFont((jLabel8.getFont().getStyle() | java.awt.Font.ITALIC), 10));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel8.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
        basic.add(jLabel8, gridBagConstraints);
        jLabel9.setFont(jLabel9.getFont().deriveFont((jLabel9.getFont().getStyle() | java.awt.Font.ITALIC), 10));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel9.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 2);
        basic.add(jLabel9, gridBagConstraints);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 0, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        basic.add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(basic, gridBagConstraints);
        shape.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.shape.border.title")));
        shape.setLayout(new java.awt.GridBagLayout());
        org.openide.awt.Mnemonics.setLocalizedText(tokenLabel, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.tokenLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(tokenLabel, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(concatLabel, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.concatLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(concatLabel, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(alterLabel, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.alterLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(alterLabel, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(permutLabel, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.permutLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(permutLabel, gridBagConstraints);
        tokenCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(tokenCombo, gridBagConstraints);
        concatCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(concatCombo, gridBagConstraints);
        alterCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(alterCombo, gridBagConstraints);
        permutCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(permutCombo, gridBagConstraints);
        tokenSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(tokenSpinner, gridBagConstraints);
        concatSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(concatSpinner, gridBagConstraints);
        alterSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(alterSpinner, gridBagConstraints);
        permutSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(permutSpinner, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(rootLabel, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.rootLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(rootLabel, gridBagConstraints);
        rootCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(rootCombo, gridBagConstraints);
        rootSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(rootSpinner, gridBagConstraints);
        rootColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        rootColor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rootColor.setMaximumSize(new java.awt.Dimension(20, 20));
        rootColor.setMinimumSize(new java.awt.Dimension(20, 20));
        rootColor.setPreferredSize(new java.awt.Dimension(20, 20));
        rootColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout rootColorLayout = new javax.swing.GroupLayout(rootColor);
        rootColor.setLayout(rootColorLayout);
        rootColorLayout.setHorizontalGroup(rootColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        rootColorLayout.setVerticalGroup(rootColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(rootColor, gridBagConstraints);
        tokenColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tokenColor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tokenColor.setMaximumSize(new java.awt.Dimension(20, 20));
        tokenColor.setMinimumSize(new java.awt.Dimension(20, 20));
        tokenColor.setPreferredSize(new java.awt.Dimension(20, 20));
        tokenColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout tokenColorLayout = new javax.swing.GroupLayout(tokenColor);
        tokenColor.setLayout(tokenColorLayout);
        tokenColorLayout.setHorizontalGroup(tokenColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        tokenColorLayout.setVerticalGroup(tokenColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(tokenColor, gridBagConstraints);
        concatColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        concatColor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        concatColor.setMaximumSize(new java.awt.Dimension(20, 20));
        concatColor.setMinimumSize(new java.awt.Dimension(20, 20));
        concatColor.setPreferredSize(new java.awt.Dimension(20, 20));
        concatColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout concatColorLayout = new javax.swing.GroupLayout(concatColor);
        concatColor.setLayout(concatColorLayout);
        concatColorLayout.setHorizontalGroup(concatColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        concatColorLayout.setVerticalGroup(concatColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(concatColor, gridBagConstraints);
        alterColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        alterColor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        alterColor.setMaximumSize(new java.awt.Dimension(20, 20));
        alterColor.setMinimumSize(new java.awt.Dimension(20, 20));
        alterColor.setPreferredSize(new java.awt.Dimension(20, 20));
        alterColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout alterColorLayout = new javax.swing.GroupLayout(alterColor);
        alterColor.setLayout(alterColorLayout);
        alterColorLayout.setHorizontalGroup(alterColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        alterColorLayout.setVerticalGroup(alterColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(alterColor, gridBagConstraints);
        permutColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        permutColor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        permutColor.setMaximumSize(new java.awt.Dimension(20, 20));
        permutColor.setMinimumSize(new java.awt.Dimension(20, 20));
        permutColor.setPreferredSize(new java.awt.Dimension(20, 20));
        permutColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout permutColorLayout = new javax.swing.GroupLayout(permutColor);
        permutColor.setLayout(permutColorLayout);
        permutColorLayout.setHorizontalGroup(permutColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        permutColorLayout.setVerticalGroup(permutColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(permutColor, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel1.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        shape.add(jLabel1, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel2.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        shape.add(jLabel2, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel3.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        shape.add(jLabel3, gridBagConstraints);
        fillH.setPreferredSize(new java.awt.Dimension(0, 0));
        fillH.setLayout(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        shape.add(fillH, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jButton1.text"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultValues(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        shape.add(jButton1, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel10.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(jLabel10, gridBagConstraints);
        lambdaCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(lambdaCombo, gridBagConstraints);
        lambdaColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lambdaColor.setMaximumSize(new java.awt.Dimension(20, 20));
        lambdaColor.setMinimumSize(new java.awt.Dimension(20, 20));
        lambdaColor.setPreferredSize(new java.awt.Dimension(20, 20));
        lambdaColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout lambdaColorLayout = new javax.swing.GroupLayout(lambdaColor);
        lambdaColor.setLayout(lambdaColorLayout);
        lambdaColorLayout.setHorizontalGroup(lambdaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        lambdaColorLayout.setVerticalGroup(lambdaColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(lambdaColor, gridBagConstraints);
        lambdaSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(lambdaSpinner, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel11.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(jLabel11, gridBagConstraints);
        simpleDataCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(simpleDataCombo, gridBagConstraints);
        simpleDataSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(simpleDataSpinner, gridBagConstraints);
        simpleDataColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        simpleDataColor.setMaximumSize(new java.awt.Dimension(20, 20));
        simpleDataColor.setMinimumSize(new java.awt.Dimension(20, 20));
        simpleDataColor.setPreferredSize(new java.awt.Dimension(20, 20));
        simpleDataColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout simpleDataColorLayout = new javax.swing.GroupLayout(simpleDataColor);
        simpleDataColor.setLayout(simpleDataColorLayout);
        simpleDataColorLayout.setHorizontalGroup(simpleDataColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        simpleDataColorLayout.setVerticalGroup(simpleDataColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(simpleDataColor, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(TreeRuleDisplayerPanel.class, "TreeRuleDisplayerPanel.jLabel12.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        shape.add(jLabel12, gridBagConstraints);
        attributeCombo.setModel(new javax.swing.DefaultComboBoxModel(VertexShape.values()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(attributeCombo, gridBagConstraints);
        attributeSpinner.setModel(new javax.swing.SpinnerNumberModel(10, 10, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 10);
        shape.add(attributeSpinner, gridBagConstraints);
        attributeColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        attributeColor.setMaximumSize(new java.awt.Dimension(20, 20));
        attributeColor.setMinimumSize(new java.awt.Dimension(20, 20));
        attributeColor.setPreferredSize(new java.awt.Dimension(20, 20));
        attributeColor.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                colorMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout attributeColorLayout = new javax.swing.GroupLayout(attributeColor);
        attributeColor.setLayout(attributeColorLayout);
        attributeColorLayout.setHorizontalGroup(attributeColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        attributeColorLayout.setVerticalGroup(attributeColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 18, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        shape.add(attributeColor, gridBagConstraints);
        fillV.setLayout(null);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        shape.add(fillV, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(shape, gridBagConstraints);
        fill.setPreferredSize(new java.awt.Dimension(0, 0));
        javax.swing.GroupLayout fillLayout = new javax.swing.GroupLayout(fill);
        fill.setLayout(fillLayout);
        fillLayout.setHorizontalGroup(fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 533, Short.MAX_VALUE));
        fillLayout.setVerticalGroup(fillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 88, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(fill, gridBagConstraints);
    }

    @SuppressWarnings("PMD")
    private void colorMouseClicked(java.awt.event.MouseEvent evt) {
        final Object source = evt.getSource();
        if (source instanceof JPanel) {
            final JPanel panel = (JPanel) source;
            final Color c = JColorChooser.showDialog(TreeRuleDisplayerPanel.this, "Choose the color", panel.getBackground());
            if (c != null) {
                panel.setBackground(c);
            }
        }
    }

    /**
   * Set all fields in this panel to default value.
   * @param evt
   */
    @SuppressWarnings("PMD")
    private void setDefaultValues(java.awt.event.ActionEvent evt) {
        rootCombo.setSelectedItem(Utils.ROOT_SHAPE_DEFAULT);
        tokenCombo.setSelectedItem(Utils.ELEMENT_SHAPE_DEFAULT);
        concatCombo.setSelectedItem(Utils.CONCAT_SHAPE_DEFAULT);
        alterCombo.setSelectedItem(Utils.ALTER_SHAPE_DEFAULT);
        permutCombo.setSelectedItem(Utils.PERMUT_SHAPE_DEFAULT);
        lambdaCombo.setSelectedItem(Utils.LAMBDA_SHAPE_DEFAULT);
        simpleDataCombo.setSelectedItem(Utils.SIMPLE_DATA_SHAPE_DEFAULT);
        attributeCombo.setSelectedItem(Utils.ATTRIBUTE_SHAPE_DEFAULT);
        rootSpinner.setValue(Utils.ROOT_SIZE_DEFAULT);
        tokenSpinner.setValue(Utils.ELEMENT_SIZE_DEFAULT);
        concatSpinner.setValue(Utils.CONCAT_SIZE_DEFAULT);
        alterSpinner.setValue(Utils.ALTER_SIZE_DEFAULT);
        permutSpinner.setValue(Utils.PERMUT_SIZE_DEFAULT);
        lambdaSpinner.setValue(Utils.LAMBDA_SIZE_DEFAULT);
        simpleDataSpinner.setValue(Utils.SIMPLE_DATA_SIZE_DEFAULT);
        attributeSpinner.setValue(Utils.ATTRIBUTE_SIZE_DEFAULT);
        rootColor.setBackground(Utils.ROOT_COLOR_DEFAULT);
        tokenColor.setBackground(Utils.ELEMENT_COLOR_DEFAULT);
        concatColor.setBackground(Utils.CONCAT_COLOR_DEFAULT);
        alterColor.setBackground(Utils.ALTER_COLOR_DEFAULT);
        permutColor.setBackground(Utils.PERMUT_COLOR_DEFAULT);
        lambdaColor.setBackground(Utils.LAMBDA_COLOR_DEFAULT);
        simpleDataColor.setBackground(Utils.SIMPLE_DATA_COLOR_DEFAULT);
        attributeColor.setBackground(Utils.ATTRIBUTE_COLOR_DEFAULT);
    }

    /**
   * Load values for all fields from properties. If no value for particular field is saved
   * in properties, default value is used.
   */
    public void load() {
        backgroundColor.setBackground(Utils.getColorProperty(Utils.BG_COLOR_PROP, Utils.BG_COLOR_DEFAULT));
        horizontalDistance.setValue(Utils.getProperty(Utils.HORIZONTAL_DISTANCE_PROP, Utils.HORIZONTAL_DISTANCE_DEFAULT));
        verticalDistance.setValue(Utils.getProperty(Utils.VERTICAL_DISTANCE_PROP, Utils.VERTICAL_DISTANCE_DEFAULT));
        rootCombo.setSelectedItem(Utils.getShapeProperty(Utils.ROOT_SHAPE_PROP, Utils.ROOT_SHAPE_DEFAULT));
        tokenCombo.setSelectedItem(Utils.getShapeProperty(Utils.ELEMENT_SHAPE_PROP, Utils.ELEMENT_SHAPE_DEFAULT));
        concatCombo.setSelectedItem(Utils.getShapeProperty(Utils.CONCAT_SHAPE_PROP, Utils.CONCAT_SHAPE_DEFAULT));
        alterCombo.setSelectedItem(Utils.getShapeProperty(Utils.ALTER_SHAPE_PROP, Utils.ALTER_SHAPE_DEFAULT));
        permutCombo.setSelectedItem(Utils.getShapeProperty(Utils.PERMUT_SHAPE_PROP, Utils.PERMUT_SHAPE_DEFAULT));
        lambdaCombo.setSelectedItem(Utils.getShapeProperty(Utils.LAMBDA_SHAPE_PROP, Utils.LAMBDA_SHAPE_DEFAULT));
        simpleDataCombo.setSelectedItem(Utils.getShapeProperty(Utils.SIMPLE_DATA_SHAPE_PROP, Utils.SIMPLE_DATA_SHAPE_DEFAULT));
        attributeCombo.setSelectedItem(Utils.getShapeProperty(Utils.ATTRIBUTE_SHAPE_PROP, Utils.ATTRIBUTE_SHAPE_DEFAULT));
        rootSpinner.setValue(Utils.getProperty(Utils.ROOT_SIZE_PROP, Utils.ROOT_SIZE_DEFAULT));
        tokenSpinner.setValue(Utils.getProperty(Utils.ELEMENT_SIZE_PROP, Utils.ELEMENT_SIZE_DEFAULT));
        concatSpinner.setValue(Utils.getProperty(Utils.CONCAT_SIZE_PROP, Utils.CONCAT_SIZE_DEFAULT));
        alterSpinner.setValue(Utils.getProperty(Utils.ALTER_SIZE_PROP, Utils.ALTER_SIZE_DEFAULT));
        permutSpinner.setValue(Utils.getProperty(Utils.PERMUT_SIZE_PROP, Utils.PERMUT_SIZE_DEFAULT));
        lambdaSpinner.setValue(Utils.getProperty(Utils.LAMBDA_SIZE_PROP, Utils.LAMBDA_SIZE_DEFAULT));
        simpleDataSpinner.setValue(Utils.getProperty(Utils.SIMPLE_DATA_SIZE_PROP, Utils.SIMPLE_DATA_SIZE_DEFAULT));
        attributeSpinner.setValue(Utils.getProperty(Utils.ATTRIBUTE_SIZE_PROP, Utils.ATTRIBUTE_SIZE_DEFAULT));
        rootColor.setBackground(Utils.getColorProperty(Utils.ROOT_COLOR_PROP, Utils.ROOT_COLOR_DEFAULT));
        tokenColor.setBackground(Utils.getColorProperty(Utils.ELEMENT_COLOR_PROP, Utils.ELEMENT_COLOR_DEFAULT));
        concatColor.setBackground(Utils.getColorProperty(Utils.CONCAT_COLOR_PROP, Utils.CONCAT_COLOR_DEFAULT));
        alterColor.setBackground(Utils.getColorProperty(Utils.ALTER_COLOR_PROP, Utils.ALTER_COLOR_DEFAULT));
        permutColor.setBackground(Utils.getColorProperty(Utils.PERMUT_COLOR_PROP, Utils.PERMUT_COLOR_DEFAULT));
        lambdaColor.setBackground(Utils.getColorProperty(Utils.LAMBDA_COLOR_PROP, Utils.LAMBDA_COLOR_DEFAULT));
        simpleDataColor.setBackground(Utils.getColorProperty(Utils.SIMPLE_DATA_COLOR_PROP, Utils.SIMPLE_DATA_COLOR_DEFAULT));
        attributeColor.setBackground(Utils.getColorProperty(Utils.ATTRIBUTE_COLOR_PROP, Utils.ATTRIBUTE_COLOR_DEFAULT));
    }

    /**
   * Save all values in fields into properties.
   */
    public void store() {
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.BG_COLOR_PROP, String.valueOf(backgroundColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.HORIZONTAL_DISTANCE_PROP, (Integer) horizontalDistance.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.VERTICAL_DISTANCE_PROP, (Integer) verticalDistance.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ROOT_SHAPE_PROP, rootCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ELEMENT_SHAPE_PROP, tokenCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.CONCAT_SHAPE_PROP, concatCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ALTER_SHAPE_PROP, alterCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.PERMUT_SHAPE_PROP, permutCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.LAMBDA_SHAPE_PROP, lambdaCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.SIMPLE_DATA_SHAPE_PROP, simpleDataCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ATTRIBUTE_SHAPE_PROP, attributeCombo.getSelectedItem().toString());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.ROOT_SIZE_PROP, (Integer) rootSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.ELEMENT_SIZE_PROP, (Integer) tokenSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.CONCAT_SIZE_PROP, (Integer) concatSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.ALTER_SIZE_PROP, (Integer) alterSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.PERMUT_SIZE_PROP, (Integer) permutSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.LAMBDA_SIZE_PROP, (Integer) lambdaSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.SIMPLE_DATA_SIZE_PROP, (Integer) simpleDataSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).putInt(Utils.ATTRIBUTE_SIZE_PROP, (Integer) attributeSpinner.getValue());
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ROOT_COLOR_PROP, String.valueOf(rootColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ELEMENT_COLOR_PROP, String.valueOf(tokenColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.CONCAT_COLOR_PROP, String.valueOf(concatColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ALTER_COLOR_PROP, String.valueOf(alterColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.PERMUT_COLOR_PROP, String.valueOf(permutColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.LAMBDA_COLOR_PROP, String.valueOf(lambdaColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.SIMPLE_DATA_COLOR_PROP, String.valueOf(simpleDataColor.getBackground().getRGB()));
        NbPreferences.forModule(TreeRuleDisplayerPanel.class).put(Utils.ATTRIBUTE_COLOR_PROP, String.valueOf(attributeColor.getBackground().getRGB()));
    }

    /**
   * Check if values entered in fields are valid. In this panel is no check for validity.
   * @return <tt>true</tt>.
   */
    public boolean valid() {
        return true;
    }

    private javax.swing.JPanel alterColor;

    private javax.swing.JComboBox alterCombo;

    private javax.swing.JLabel alterLabel;

    private javax.swing.JSpinner alterSpinner;

    private javax.swing.JPanel attributeColor;

    private javax.swing.JComboBox attributeCombo;

    private javax.swing.JSpinner attributeSpinner;

    private javax.swing.JPanel backgroundColor;

    private javax.swing.JPanel basic;

    private javax.swing.JPanel concatColor;

    private javax.swing.JComboBox concatCombo;

    private javax.swing.JLabel concatLabel;

    private javax.swing.JSpinner concatSpinner;

    private javax.swing.JPanel fill;

    private javax.swing.JPanel fillH;

    private javax.swing.JPanel fillV;

    private javax.swing.JSpinner horizontalDistance;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel lambdaColor;

    private javax.swing.JComboBox lambdaCombo;

    private javax.swing.JSpinner lambdaSpinner;

    private javax.swing.JPanel permutColor;

    private javax.swing.JComboBox permutCombo;

    private javax.swing.JLabel permutLabel;

    private javax.swing.JSpinner permutSpinner;

    private javax.swing.JPanel rootColor;

    private javax.swing.JComboBox rootCombo;

    private javax.swing.JLabel rootLabel;

    private javax.swing.JSpinner rootSpinner;

    private javax.swing.JPanel shape;

    private javax.swing.JPanel simpleDataColor;

    private javax.swing.JComboBox simpleDataCombo;

    private javax.swing.JSpinner simpleDataSpinner;

    private javax.swing.JPanel tokenColor;

    private javax.swing.JComboBox tokenCombo;

    private javax.swing.JLabel tokenLabel;

    private javax.swing.JSpinner tokenSpinner;

    private javax.swing.JSpinner verticalDistance;
}
