package net.sf.fallfair.fallfair;

import net.sf.fallfair.view.FairCloseCommand;
import net.sf.fallfair.view.FairController;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.fallfair.CRUD.Persistent;
import net.sf.fallfair.sectiontype.SectionType;
import net.sf.fallfair.utils.DecimalFormatWrapper;
import net.sf.fallfair.panel.AbstractFairPanel;
import net.sf.fallfair.view.FairControllerImpl;
import net.sf.fallfair.view.FairView;
import net.sf.fallfair.menu.FairMenu;
import net.sf.fallfair.view.utils.SectionTypeComboBoxModel;

public class FallFairMaintenance extends AbstractFairPanel implements FairView {

    private final JMenuItem okMenuItem;

    private final JMenuItem cancelMenuItem;

    private final FairController<FallFairMaintenance> fallFairController;

    private FallFair fallFair;

    public FallFairMaintenance(FairMenu menu) {
        super(menu);
        this.fallFairController = initController();
        initComponents();
        this.okMenuItem = initOkMenuItem();
        this.cancelMenuItem = initCancelMenuItem();
        this.fallFairController.execute("init", this, getContext());
    }

    private FairController<FallFairMaintenance> initController() {
        FairController<FallFairMaintenance> controller = new FairControllerImpl<FallFairMaintenance>();
        controller.addCommand("init", new FallFairInitCommand());
        controller.addCommand("save", new FallFairSaveCommand());
        controller.addCommand("cancel", new FairCloseCommand<FallFairMaintenance>());
        return controller;
    }

    private JMenuItem initOkMenuItem() {
        JMenuItem menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok();
            }
        });
        return menuItem;
    }

    private JMenuItem initCancelMenuItem() {
        JMenuItem menuItem = new JMenuItem("Cancel");
        menuItem.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel();
            }
        });
        return menuItem;
    }

    private void initComponents() {
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        fairNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        fourHPrizeAmountTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        sectionTypeComboBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        membershipAmountTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        awardedBeforeApplyingEntryFeeTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        entryFeeTextField = new javax.swing.JTextField();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel6.setText("jLabel6");
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("net/sf/fallfair/view/configs/labels");
        jLabel1.setText(bundle.getString("fairName"));
        jLabel2.setText(bundle.getString("fourHPrizeAmount"));
        jLabel3.setText(bundle.getString("calculatedEntryFeeSectionType"));
        jLabel4.setText(bundle.getString("membershipAmount"));
        jLabel5.setText(bundle.getString("minimumAwardedBeforeDeductingEntryFee"));
        jLabel7.setText(bundle.getString("entryFeePercentage"));
        okButton.setText(bundle.getString("save"));
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        cancelButton.setText(bundle.getString("cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jLabel7).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel3).addComponent(jLabel2).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(fourHPrizeAmountTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(sectionTypeComboBox, 0, 213, Short.MAX_VALUE).addComponent(membershipAmountTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE).addComponent(awardedBeforeApplyingEntryFeeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE).addComponent(entryFeeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(fairNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))).addGroup(layout.createSequentialGroup().addComponent(okButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton))).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel7 });
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cancelButton, okButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(fairNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(fourHPrizeAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(sectionTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(12, 12, 12).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(membershipAmountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(awardedBeforeApplyingEntryFeeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(entryFeeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(okButton).addComponent(cancelButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { cancelButton, okButton });
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ok();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        cancel();
    }

    public void ok() {
        this.fallFairController.execute("save", this, getContext());
    }

    public void cancel() {
        this.fallFairController.execute("cancel", this, getContext());
    }

    @Override
    public void buildFileMenu(JMenuItem rootMenu) {
        JMenu tmpRootMenu = (JMenu) rootMenu;
        tmpRootMenu.removeAll();
        tmpRootMenu.add(this.okMenuItem);
        tmpRootMenu.add(this.cancelMenuItem);
    }

    private javax.swing.JTextField awardedBeforeApplyingEntryFeeTextField;

    private javax.swing.JButton cancelButton;

    private javax.swing.JTextField entryFeeTextField;

    private javax.swing.JTextField fairNameTextField;

    private javax.swing.JTextField fourHPrizeAmountTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JTextField membershipAmountTextField;

    private javax.swing.JButton okButton;

    private javax.swing.JComboBox sectionTypeComboBox;

    public FallFair bind() {
        FallFair bindFallFair = getCurrent();
        bindFallFair.setYear(getContext().getYear());
        bindFallFair.setName(this.fairNameTextField.getText());
        bindFallFair.setFourHPrize(new BigDecimal(this.fourHPrizeAmountTextField.getText()));
        List<SectionType> sectionTypes = ((SectionTypeComboBoxModel) this.sectionTypeComboBox.getModel()).getData();
        bindFallFair.setMembershipCalculatedAgainst(sectionTypes.get(this.sectionTypeComboBox.getSelectedIndex()));
        bindFallFair.setAnnualMembership(new BigDecimal(this.membershipAmountTextField.getText()));
        bindFallFair.setMinimumAwardedBeforeEntryFee(new BigDecimal(this.awardedBeforeApplyingEntryFeeTextField.getText()));
        bindFallFair.setEntryFeePercent((new BigDecimal(this.entryFeeTextField.getText())).divide(new BigDecimal(100)));
        return bindFallFair;
    }

    public void render() {
        this.fairNameTextField.setText(getCurrent().getName());
        this.fourHPrizeAmountTextField.setText(DecimalFormatWrapper.formatCurrency(getCurrent().getFourHPrize()));
        List<SectionType> sectionTypes = ((SectionTypeComboBoxModel) this.sectionTypeComboBox.getModel()).getData();
        for (int j = 0; j < sectionTypes.size(); j++) {
            if (sectionTypes.get(j).equals(getCurrent().getMembershipCalculatedAgainst())) {
                this.sectionTypeComboBox.setSelectedIndex(j);
            }
        }
        this.membershipAmountTextField.setText(DecimalFormatWrapper.formatCurrency(getCurrent().getAnnualMembership()));
        this.awardedBeforeApplyingEntryFeeTextField.setText(DecimalFormatWrapper.formatCurrency(getCurrent().getMinimumAwardedBeforeEntryFee()));
        this.entryFeeTextField.setText(DecimalFormatWrapper.formatPercent(getCurrent().getEntryFeePercent()));
    }

    public FallFair getCurrent() {
        return this.fallFair;
    }

    public void setCurrent(Persistent obj) {
        this.fallFair = (FallFair) obj;
    }

    public JComboBox getSectionTypeComboBox() {
        return this.sectionTypeComboBox;
    }
}
