/*
 * FirmOrderBindingIssuesPanel.java
 *
 * Created on April 27, 2006, 7:08 PM
 */

package newgen.presentation.sm.smadv;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author  Administrator
 */
public class FirmOrderBindingIssuesPanel extends javax.swing.JPanel implements TableModelListener{
    private newgen.presentation.component.NewGenXMLGenerator newGenXMLGenerator = null;
    javax.swing.table.DefaultTableModel dftIssuesToBound=null,dftBudgets=null,dftPhysicalVolume=null;
    /** Creates new form FirmOrderBindingIssuesPanel */
    public FirmOrderBindingIssuesPanel() {
        initComponents();
        dialogBindingSpec.setModal(true);
        dialogBudget.setModal(true);
        dialogBudget.setSize(550,288);
        dialogBudget.setLocation(newgen.presentation.NewGenMain.getAppletInstance().getLocation(550, 288));
        dialogBindingSpec.setSize(700,307);
        dialogBindingSpec.getContentPane().setLocation(700, 307);
        newGenXMLGenerator = newgen.presentation.component.NewGenXMLGenerator.getInstance();
        dftIssuesToBound=new javax.swing.table.DefaultTableModel(new Object[]{"registrationId","copyId",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("EnumAndChrondisplay"),"#","xmldump",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Type")},0){
            public boolean isCellEditable(int r, int c){
                if(c == 3) 
                    return true;
                else
                    return false;
            }
            public Class getColumnClass(int columnIndex) {
                return getValueAt(0,columnIndex).getClass();
            }
            
        };
        tabIssuesToBound.setModel(dftIssuesToBound);
        tabIssuesToBound.getColumnModel().getColumn(0).setMinWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(0).setMaxWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tabIssuesToBound.getColumnModel().getColumn(1).setMinWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(1).setMaxWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        
        tabIssuesToBound.getColumnModel().getColumn(4).setMinWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(4).setMaxWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(4).setPreferredWidth(0);
        
        tabIssuesToBound.getColumnModel().getColumn(3).setMinWidth(50);
        tabIssuesToBound.getColumnModel().getColumn(3).setMaxWidth(50);
        
        tabIssuesToBound.getColumnModel().getColumn(5).setMinWidth(50);
        tabIssuesToBound.getColumnModel().getColumn(5).setMaxWidth(50);
        
        
        dftBudgets=new javax.swing.table.DefaultTableModel(new Object[]{"libraryId",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("LibraryName"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetSource"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Amount")},0){
            public boolean isCellEditable(int c,int r){
                return false;
            }
        };
        dftBudgets.addTableModelListener(this);
        tableBudgets.setModel(dftBudgets);
        tableBudgets.getColumnModel().getColumn(0).setMinWidth(0);
        tableBudgets.getColumnModel().getColumn(0).setMaxWidth(0);
        tableBudgets.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tableBudgets.getColumnModel().getColumn(1).setMinWidth(0);
        tableBudgets.getColumnModel().getColumn(1).setMaxWidth(0);
        tableBudgets.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        dftPhysicalVolume=new javax.swing.table.DefaultTableModel(new Object[]{"physicalvolumeId",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Compressedformat"),"htDump"},0){
            public boolean isCellEditable(int c,int r){
                return false;
            }
        };
        tableIssuesVolume.setModel(dftPhysicalVolume);
        
        tableIssuesVolume.getColumnModel().getColumn(0).setMinWidth(0);
        tableIssuesVolume.getColumnModel().getColumn(0).setMaxWidth(0);
        // tableIssuesVolume.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tableIssuesVolume.getColumnModel().getColumn(2).setMinWidth(0);
        tableIssuesVolume.getColumnModel().getColumn(2).setMaxWidth(0);
        tableIssuesVolume.getColumnModel().getColumn(2).setPreferredWidth(0);
        
        bnAddToPhysical.setEnabled(false);
        //        dialogBindingSpec.setLocation(680, 310);
        
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(this);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogBudget);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogOrderInfo);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogOrderNos);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(modifyDialog);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogBindingSpec);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogBudget1);
        bcurrency.setText(newgen.presentation.component.Utility.getInstance().getLocalBudgetCurrencyName());
    }
    
    public void reloadLocales(){
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogBudget);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogOrderInfo);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogOrderNos);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(modifyDialog);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogBindingSpec);
        newgen.presentation.NewGenMain.getAppletInstance().applyOrientation(dialogBudget1);
        dialogBudget.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetDetails"));
        bBudgetOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        dialogOrderInfo.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewOrder"));
        lBatchDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BatchDate"));
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ArrivalDate"));
        bOkOrderInfo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderInfo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bCloseOrderInfo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderInfo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        dialogOrderNos.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orders"));
        bOkOrderNos.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderNos.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bCloseOrderNos.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderNos.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        modifyDialog.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ModifyOrder"));
        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"));
        bOkModify.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkModify.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bCancelModify.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancelModify.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        
        
        dialogBindingSpec.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindingSpecification"));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfPhysicalVolumesWhenBound"));
        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindType"));
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Price"));
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Color"));
        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Color"));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SpineDetails")));
        jLabel29.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfSpineTitle"));
        jLabel30.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfVolumeNumber"));
        jLabel31.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfYear"));
        jLabel32.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfMonths"));
        jLabel33.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfNameOfLibrary"));
        jLabel34.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CallNumber"));
        jLabel35.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfCallNumber"));
        jLabel36.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfIssueNumbers"));
        bnSpecOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnSpecOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnSpecCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnSpecCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetOk1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetOk1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetCancel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetCancel1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfPhysicalVolumesWhenBound"));
        bnBindingspec.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindingSpecification"));
        bnBindingspec.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindingSpecification"));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("IssuesToBebound")));
        bnAddToPhysical.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Addtophysicalvolume"));
        bnAddToPhysical.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Addtophysicalvolume"));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalVolumes")));
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PricePerVolume"));
        bnBudgetComponent.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetDetails"));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Budgets")));
        
        
        dftIssuesToBound.setColumnIdentifiers(new Object[]{"registrationId","copyId",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("EnumAndChrondisplay"),"#","xmldump",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Type")});
        tabIssuesToBound.getColumnModel().getColumn(0).setMinWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(0).setMaxWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tabIssuesToBound.getColumnModel().getColumn(1).setMinWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(1).setMaxWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        
        tabIssuesToBound.getColumnModel().getColumn(4).setMinWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(4).setMaxWidth(0);
        tabIssuesToBound.getColumnModel().getColumn(4).setPreferredWidth(0);
        
        tabIssuesToBound.getColumnModel().getColumn(3).setMinWidth(50);
        tabIssuesToBound.getColumnModel().getColumn(3).setMaxWidth(50);
        
        tabIssuesToBound.getColumnModel().getColumn(5).setMinWidth(50);
        tabIssuesToBound.getColumnModel().getColumn(5).setMaxWidth(50);
        
        
        
        
        dftBudgets.setColumnIdentifiers(new Object[]{"libraryId",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetId"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("LibraryName"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetHead"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetSource"),newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Amount")});
        tableBudgets.getColumnModel().getColumn(0).setMinWidth(0);
        tableBudgets.getColumnModel().getColumn(0).setMaxWidth(0);
        tableBudgets.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tableBudgets.getColumnModel().getColumn(1).setMinWidth(0);
        tableBudgets.getColumnModel().getColumn(1).setMaxWidth(0);
        tableBudgets.getColumnModel().getColumn(1).setPreferredWidth(0);
        
        
        
        dftPhysicalVolume.setColumnIdentifiers(new Object[]{"physicalvolumeId",newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Compressedformat"),"htDump"});
        tableIssuesVolume.getColumnModel().getColumn(0).setMinWidth(0);
        tableIssuesVolume.getColumnModel().getColumn(0).setMaxWidth(0);
        // tableIssuesVolume.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        tableIssuesVolume.getColumnModel().getColumn(2).setMinWidth(0);
        tableIssuesVolume.getColumnModel().getColumn(2).setMaxWidth(0);
        tableIssuesVolume.getColumnModel().getColumn(2).setPreferredWidth(0);
        
        
        
        
    }
    
    public String getEnumChronDisplayString(String enumXml){
        String display="";
        try{
            org.jdom.input.SAXBuilder sax=new org.jdom.input.SAXBuilder();
            org.jdom.Document doc=sax.build(new java.io.StringReader(enumXml.trim()));
            org.jdom.Element root=doc.getRootElement();
            java.util.List lstEnum=root.getChildren("Enumeration");
            String enum1="",enum2="",enum3="",enum4="",enum5="",enum6="",chron1="",chron2="",chron3="",chron4="";
            for(int i=0;i<lstEnum.size();i++){
                org.jdom.Element ele=(org.jdom.Element)lstEnum.get(i);
                String code="",val="",name="";
                code=ele.getAttributeValue("code");
                name=ele.getAttributeValue("name");
                val=ele.getText();
                if(val!=null&&!val.trim().equals("")){
                    if(code!=null&&code.trim().equals("a")){
                        enum1=name+" : "+val;
                    }else if(code!=null&&code.trim().equals("b")){
                        enum2=" , "+name+" : "+val;
                    }else if(code!=null&&code.trim().equals("c")){
                        enum3=" , "+name+" : "+val;
                    }else if(code!=null&&code.trim().equals("d")){
                        enum4=" , "+name+" : "+val;
                    }else if(code!=null&&code.trim().equals("e")){
                        enum5=" , "+name+" : "+val;
                    }else if(code!=null&&code.trim().equals("f")){
                        enum6=" ,"+name+" : "+val;
                    }
                    
                    
                }
                
                
                
            }
            java.util.List lstChron=root.getChildren("Chronology");
            for(int j=0;j<lstChron.size();j++){
                //  System.out.println("lstChron   "+lstChron.size());
                org.jdom.Element ele=(org.jdom.Element)lstChron.get(j);
                String code="",val="",name="";
                code=ele.getAttributeValue("code");
                name=ele.getAttributeValue("name");
                val=ele.getText();
                // System.out.println(code+" "+name+" "+val);
                if(val!=null&&!val.trim().equals("")){
                    if(code!=null&&code.trim().equals("i")){
                        chron1=name+" : "+val;
                    }else if(code!=null&&code.trim().equals("j")){
                        chron2=" , "+name+" : "+val;
                    }else if(code!=null&&code.trim().equals("k")){
                        chron3=" , "+name+" : "+val;
                    }else if(code!=null&&code.trim().equals("l")){
                        chron4=" , "+name+" : "+val;
                    }
                    
                    
                }
            }
            
            if(!enum1.trim().equals("")){
                display=enum1;
            }
            if(!enum2.trim().equals("")){
                display+=enum2;
            }
            if(!enum3.trim().equals("")){
                display+=enum3;
            }
            if(!enum4.trim().equals("")){
                display+=enum4;
            }
            if(!enum5.trim().equals("")){
                display+=enum5;
            }
            if(!enum6.trim().equals("")){
                display+=enum6;
            }
            // System.out.println("chron1  "+chron1);
            if(!chron1.trim().equals("")){
                if(display.trim().equals(""))
                   display+=chron1;
                else
                    display+=" , "+chron1;
            }
            if(!chron2.trim().equals("")){
                display+=chron2;
            }
            if(!chron3.trim().equals("")){
                display+=chron3;
            }
            if(!chron4.trim().equals("")){
                display+=chron4;
            }
            
        }catch(Exception e){e.printStackTrace();}
        return display;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        dialogBudget = new javax.swing.JDialog();
        jPanel15 = new javax.swing.JPanel();
        budgetPanel = new newgen.presentation.component.BudgetPanel();
        jPanel16 = new javax.swing.JPanel();
        bBudgetOk = new javax.swing.JButton();
        bBudgetCancel = new javax.swing.JButton();
        dialogOrderInfo = new javax.swing.JDialog();
        panel14 = new javax.swing.JPanel();
        lBatchDate = new javax.swing.JLabel();
        tBatchDate = new newgen.presentation.component.DateField();
        jLabel4 = new javax.swing.JLabel();
        tArrivalDate = new newgen.presentation.component.DateField();
        panel15 = new javax.swing.JPanel();
        bOkOrderInfo = new javax.swing.JButton();
        bCloseOrderInfo = new javax.swing.JButton();
        dialogOrderNos = new javax.swing.JDialog();
        panel4 = new javax.swing.JPanel();
        spOrderNos = new javax.swing.JScrollPane();
        tabOrderNos = new javax.swing.JTable();
        panel5 = new javax.swing.JPanel();
        bOkOrderNos = new javax.swing.JButton();
        bCloseOrderNos = new javax.swing.JButton();
        modifyDialog = new javax.swing.JDialog();
        jPanel17 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        tfOrdernoModify = new newgen.presentation.UnicodeTextField();
        jPanel18 = new javax.swing.JPanel();
        bOkModify = new javax.swing.JButton();
        bCancelModify = new javax.swing.JButton();
        dialogBindingSpec = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tfNoofPhysicalVolumes = new newgen.presentation.UnicodeTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tfBindTypePrice = new newgen.presentation.UnicodeTextField();
        jLabel7 = new javax.swing.JLabel();
        tfColour = new newgen.presentation.UnicodeTextField();
        cmbBindType = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        tfSpineTitle = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        tfPositionOfSpineTitle = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        tfPositionOfVolumeNumber = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        tfPositionOfYears = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        tfPositionOfMonths = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        tfPositionOfNameOfLibrary = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        tfCallNumber = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        tfPositionOfCallNumber = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        tfPositionOfIssueNumbers = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        bnSpecOk = new javax.swing.JButton();
        bnSpecCancel = new javax.swing.JButton();
        dialogBudget1 = new javax.swing.JDialog();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        bBudgetOk1 = new javax.swing.JButton();
        bBudgetCancel1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tfNoOfVolumes = new newgen.presentation.UnicodeTextField();
        bnBindingspec = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabIssuesToBound = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        bnAddToPhysical = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableIssuesVolume = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tfPricePerVolume = new newgen.presentation.UnicodeTextField();
        bcurrency = new javax.swing.JLabel();
        bnBudgetComponent = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableBudgets = new javax.swing.JTable();
        lbMessage = new javax.swing.JLabel();
        bnDelete = new javax.swing.JButton();

        dialogBudget.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetDetails"));
        dialogBudget.setModal(true);
        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel15.add(budgetPanel, java.awt.BorderLayout.CENTER);

        dialogBudget.getContentPane().add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bBudgetOk.setMnemonic('o');
        bBudgetOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBudgetOkActionPerformed(evt);
            }
        });

        jPanel16.add(bBudgetOk);

        bBudgetCancel.setMnemonic('c');
        bBudgetCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBudgetCancelActionPerformed(evt);
            }
        });

        jPanel16.add(bBudgetCancel);

        dialogBudget.getContentPane().add(jPanel16, java.awt.BorderLayout.SOUTH);

        dialogOrderInfo.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NewOrder"));
        dialogOrderInfo.setModal(true);
        panel14.setLayout(new java.awt.GridBagLayout());

        panel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lBatchDate.setForeground(new java.awt.Color(170, 0, 0));
        lBatchDate.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BatchDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panel14.add(lBatchDate, gridBagConstraints);

        tBatchDate.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tBatchDateCaretUpdate(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panel14.add(tBatchDate, gridBagConstraints);

        jLabel4.setForeground(new java.awt.Color(170, 0, 0));
        jLabel4.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ArrivalDate"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panel14.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        panel14.add(tArrivalDate, gridBagConstraints);

        dialogOrderInfo.getContentPane().add(panel14, java.awt.BorderLayout.CENTER);

        panel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bOkOrderInfo.setMnemonic('o');
        bOkOrderInfo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderInfo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkOrderInfoActionPerformed(evt);
            }
        });

        panel15.add(bOkOrderInfo);

        bCloseOrderInfo.setMnemonic('c');
        bCloseOrderInfo.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderInfo.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseOrderInfoActionPerformed(evt);
            }
        });

        panel15.add(bCloseOrderInfo);

        dialogOrderInfo.getContentPane().add(panel15, java.awt.BorderLayout.SOUTH);

        dialogOrderNos.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Orders"));
        dialogOrderNos.setModal(true);
        panel4.setLayout(new java.awt.BorderLayout());

        tabOrderNos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabOrderNos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        spOrderNos.setViewportView(tabOrderNos);

        panel4.add(spOrderNos, java.awt.BorderLayout.CENTER);

        dialogOrderNos.getContentPane().add(panel4, java.awt.BorderLayout.CENTER);

        panel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bOkOrderNos.setMnemonic('o');
        bOkOrderNos.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderNos.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkOrderNos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkOrderNosActionPerformed(evt);
            }
        });

        panel5.add(bOkOrderNos);

        bCloseOrderNos.setMnemonic('c');
        bCloseOrderNos.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderNos.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCloseOrderNos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCloseOrderNosActionPerformed(evt);
            }
        });

        panel5.add(bCloseOrderNos);

        dialogOrderNos.getContentPane().add(panel5, java.awt.BorderLayout.SOUTH);

        modifyDialog.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("ModifyOrder"));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabel8.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("OrderNo"));
        jPanel17.add(jLabel8, new java.awt.GridBagConstraints());

        jPanel17.add(tfOrdernoModify, new java.awt.GridBagConstraints());

        modifyDialog.getContentPane().add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bOkModify.setMnemonic('o');
        bOkModify.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkModify.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bOkModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkModifyActionPerformed(evt);
            }
        });

        jPanel18.add(bOkModify);

        bCancelModify.setMnemonic('c');
        bCancelModify.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancelModify.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bCancelModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelModifyActionPerformed(evt);
            }
        });

        jPanel18.add(bCancelModify);

        modifyDialog.getContentPane().add(jPanel18, java.awt.BorderLayout.SOUTH);

        dialogBindingSpec.setTitle(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindingSpecification"));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel13.setLayout(new java.awt.GridBagLayout());

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel2.setForeground(new java.awt.Color(170, 0, 0));
        jLabel2.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfPhysicalVolumesWhenBound"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jLabel2, gridBagConstraints);

        tfNoofPhysicalVolumes.setEditable(false);
        tfNoofPhysicalVolumes.setFocusCycleRoot(true);
        jPanel13.add(tfNoofPhysicalVolumes, new java.awt.GridBagConstraints());

        jLabel5.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindType"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jLabel5, gridBagConstraints);

        jLabel6.setForeground(new java.awt.Color(170, 0, 0));
        jLabel6.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Price"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jLabel6, gridBagConstraints);

        tfBindTypePrice.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel13.add(tfBindTypePrice, gridBagConstraints);

        jLabel7.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Color"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel13.add(jLabel7, gridBagConstraints);

        tfColour.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel13.add(tfColour, gridBagConstraints);

        cmbBindType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBindTypeActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel13.add(cmbBindType, gridBagConstraints);

        jPanel4.add(jPanel13);

        jPanel12.setLayout(new java.awt.GridBagLayout());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SpineDetails")));
        jLabel17.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("SpineTitle"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel17, gridBagConstraints);

        tfSpineTitle.setColumns(30);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel12.add(tfSpineTitle, gridBagConstraints);

        jLabel29.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfSpineTitle"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel29, gridBagConstraints);

        tfPositionOfSpineTitle.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel12.add(tfPositionOfSpineTitle, gridBagConstraints);

        jLabel30.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfVolumeNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel30, gridBagConstraints);

        tfPositionOfVolumeNumber.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        jPanel12.add(tfPositionOfVolumeNumber, gridBagConstraints);

        jLabel31.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfYear"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel31, gridBagConstraints);

        tfPositionOfYears.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel12.add(tfPositionOfYears, gridBagConstraints);

        jLabel32.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfMonths"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel32, gridBagConstraints);

        tfPositionOfMonths.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        jPanel12.add(tfPositionOfMonths, gridBagConstraints);

        jLabel33.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfNameOfLibrary"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel33, gridBagConstraints);

        tfPositionOfNameOfLibrary.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        jPanel12.add(tfPositionOfNameOfLibrary, gridBagConstraints);

        jLabel34.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("CallNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel34, gridBagConstraints);

        tfCallNumber.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        jPanel12.add(tfCallNumber, gridBagConstraints);

        jLabel35.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfCallNumber"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel35, gridBagConstraints);

        tfPositionOfCallNumber.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 4;
        jPanel12.add(tfPositionOfCallNumber, gridBagConstraints);

        jLabel36.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PositionOfIssueNumbers"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel12.add(jLabel36, gridBagConstraints);

        tfPositionOfIssueNumbers.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        jPanel12.add(tfPositionOfIssueNumbers, gridBagConstraints);

        jPanel4.add(jPanel12);

        dialogBindingSpec.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        bnSpecOk.setMnemonic('o');
        bnSpecOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnSpecOk.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bnSpecOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnSpecOkActionPerformed(evt);
            }
        });

        jPanel8.add(bnSpecOk);

        bnSpecCancel.setMnemonic('c');
        bnSpecCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnSpecCancel.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bnSpecCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnSpecCancelActionPerformed(evt);
            }
        });

        jPanel8.add(bnSpecCancel);

        dialogBindingSpec.getContentPane().add(jPanel8, java.awt.BorderLayout.SOUTH);

        dialogBudget1.setModal(true);
        jPanel19.setLayout(new java.awt.BorderLayout());

        dialogBudget1.getContentPane().add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bBudgetOk1.setMnemonic('o');
        bBudgetOk1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetOk1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Ok"));
        bBudgetOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBudgetOk1ActionPerformed(evt);
            }
        });

        jPanel20.add(bBudgetOk1);

        bBudgetCancel1.setMnemonic('c');
        bBudgetCancel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetCancel1.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        bBudgetCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBudgetCancel1ActionPerformed(evt);
            }
        });

        jPanel20.add(bBudgetCancel1);

        dialogBudget1.getContentPane().add(jPanel20, java.awt.BorderLayout.SOUTH);

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setForeground(new java.awt.Color(170, 0, 0));
        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfPhysicalVolumesWhenBound"));
        jPanel6.add(jLabel1);

        tfNoOfVolumes.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                tfNoOfVolumesCaretUpdate(evt);
            }
        });

        jPanel6.add(tfNoOfVolumes);

        bnBindingspec.setMnemonic('s');
        bnBindingspec.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindingSpecification"));
        bnBindingspec.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BindingSpecification"));
        bnBindingspec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnBindingspecActionPerformed(evt);
            }
        });

        jPanel6.add(bnBindingspec);

        add(jPanel6);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("IssuesToBebound")));
        jPanel1.setPreferredSize(new java.awt.Dimension(465, 800));
        tabIssuesToBound.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tabIssuesToBound);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        add(jPanel1);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        bnAddToPhysical.setMnemonic('a');
        bnAddToPhysical.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Addtophysicalvolume"));
        bnAddToPhysical.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Addtophysicalvolume"));
        bnAddToPhysical.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnAddToPhysicalActionPerformed(evt);
            }
        });

        jPanel5.add(bnAddToPhysical);

        add(jPanel5);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PhysicalVolumes")));
        tableIssuesVolume.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tableIssuesVolume);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        add(jPanel2);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel3.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PricePerVolume"));
        jPanel7.add(jLabel3);

        tfPricePerVolume.setEditable(false);
        jPanel7.add(tfPricePerVolume);

        jPanel7.add(bcurrency);

        bnBudgetComponent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/Money.gif")));
        bnBudgetComponent.setMnemonic('b');
        bnBudgetComponent.setToolTipText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("BudgetDetails"));
        bnBudgetComponent.setEnabled(false);
        bnBudgetComponent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnBudgetComponentActionPerformed(evt);
            }
        });

        jPanel7.add(bnBudgetComponent);

        add(jPanel7);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Budgets")));
        tableBudgets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tableBudgets);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        lbMessage.setForeground(new java.awt.Color(170, 0, 0));
        lbMessage.setText("Warning: No budget heads are selected");
        jPanel3.add(lbMessage, java.awt.BorderLayout.SOUTH);

        bnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/general/Delete16.gif")));
        bnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnDeleteActionPerformed(evt);
            }
        });

        jPanel3.add(bnDelete, java.awt.BorderLayout.EAST);

        add(jPanel3);

    }// </editor-fold>//GEN-END:initComponents

    private void bnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnDeleteActionPerformed
// TODO add your handling code here:
        if(tableBudgets.getSelectedRow()!=-1){
            dftBudgets.removeRow(tableBudgets.getSelectedRow());
        }
    }//GEN-LAST:event_bnDeleteActionPerformed
    
    private void bBudgetCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBudgetCancel1ActionPerformed
        // Add your handling code here:
        budgetDialogReturnCode=1;
        dialogBudget1.dispose();
    }//GEN-LAST:event_bBudgetCancel1ActionPerformed
    
    private void bBudgetOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBudgetOk1ActionPerformed
        // Add your handling code here:
        budgetDialogReturnCode=0;
        dialogBudget1.dispose();
    }//GEN-LAST:event_bBudgetOk1ActionPerformed
    
    private void bCancelModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelModifyActionPerformed
        // Add your handling code here:
        modifyDialog.setVisible(false);
    }//GEN-LAST:event_bCancelModifyActionPerformed
    
    private void bOkModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkModifyActionPerformed
        // Add your handling code here:
        modifyDialog.setVisible(false);
    }//GEN-LAST:event_bOkModifyActionPerformed
    
    private void bCloseOrderNosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseOrderNosActionPerformed
        // Add your handling code here:
        dialogOrderNos.setVisible(false);
    }//GEN-LAST:event_bCloseOrderNosActionPerformed
    
    private void bOkOrderNosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkOrderNosActionPerformed
        // Add your handling code here:
        dialogOrderNos.setVisible(false);
    }//GEN-LAST:event_bOkOrderNosActionPerformed
    
    private void bCloseOrderInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCloseOrderInfoActionPerformed
        // Add your handling code here:
        dialogOrderInfo.setVisible(false);
    }//GEN-LAST:event_bCloseOrderInfoActionPerformed
    
    private void bOkOrderInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkOrderInfoActionPerformed
        // Add your handling code here:
        dialogOrderInfo.setVisible(false);
    }//GEN-LAST:event_bOkOrderInfoActionPerformed
    
    private void tBatchDateCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tBatchDateCaretUpdate
        // Add your handling code here:
    }//GEN-LAST:event_tBatchDateCaretUpdate
    
    private void tfNoOfVolumesCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_tfNoOfVolumesCaretUpdate
        // Add your handling code here:
        String str=tfNoOfVolumes.getText();
        if(str!=null&&!str.trim().equals("")){
            int p=0;
            try{
                p=Integer.parseInt(str.trim());
            }catch(Exception e){e.printStackTrace();}
            if(p>0){
                bnAddToPhysical.setText("Add to physical volume 1");
                bnAddToPhysical.setEnabled(true);
            }
        }else{
            bnAddToPhysical.setEnabled(false);
        }
        
    }//GEN-LAST:event_tfNoOfVolumesCaretUpdate
    
    private void cmbBindTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBindTypeActionPerformed
        // Add your handling code here:
        //java.util.Enumeration enum=hashBindName.keys();
        //  System.out.println("enum size  "+enum.hasMoreElements()+" "+hashBindName.size());
        //        while(enum.hasMoreElements()){
        //            String str=enum.nextElement().toString();
        //            System.out.println(" enum  "+str+" "+ hashBindName.get(str));
        //        }
        
        String str[]=(String[])this.hashBindName.get(cmbBindType.getSelectedItem().toString().trim());
        //System.out.println("str is null  "+(str==null));
        // System.out.println("in CMBBINDTYPE action "+str[0]+" "+str[1]+" "+str[2]+" "+str[3]);
        
        tfColour.setText(str[2]);
        
        tfPricePerVolume.setText(str[3]);
        tfBindTypePrice.setText(str[3]);
        this.binderTypeId=str[0];
        
    }//GEN-LAST:event_cmbBindTypeActionPerformed
    
    private void bBudgetCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBudgetCancelActionPerformed
        // Add your handling code here:
        budgetDialogReturnCode=1;
        dialogBudget.dispose();
    }//GEN-LAST:event_bBudgetCancelActionPerformed
    
    private void bBudgetOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBudgetOkActionPerformed
        // Add your handling code here:
        budgetDialogReturnCode=0;
        dialogBudget.dispose();
    }//GEN-LAST:event_bBudgetOkActionPerformed
    
    private void bnSpecCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnSpecCancelActionPerformed
        // Add your handling code here:]
        refresh();
        dialogBindingSpec.setVisible(false);
    }//GEN-LAST:event_bnSpecCancelActionPerformed
    
    private void bnSpecOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnSpecOkActionPerformed
        // Add your handling code here:
        dialogBindingSpec.setVisible(false);
    }//GEN-LAST:event_bnSpecOkActionPerformed
    public String parseSupplyIndexXML(String xml){
        String disp="";
        try{
            org.jdom.input.SAXBuilder sax=new org.jdom.input.SAXBuilder();
            org.jdom.Document doc=sax.build(new java.io.StringReader(xml.trim()));
            org.jdom.Element ele=doc.getRootElement();
            if(ele.getChild("Type")!=null&&ele.getChildText("Type")!=null){
                disp=ele.getChildText("Type");
            }
            String a="",b="",c="",d="",e="",f="",i="",j="",k1="",l="";
            java.util.List enumList=ele.getChildren("Enumeration");
            for(int k=0;k<enumList.size();k++){
                String code="",val="",name="";
                org.jdom.Element enumele=(org.jdom.Element)enumList.get(k);
                code=enumele.getAttributeValue("code");
                name=enumele.getAttributeValue("name");
                val=enumele.getText();
                if(val!=null&&!val.trim().equals("")){
                    if(code.trim().equals("a")){
                        a=name+":"+val;
                    }else if(code.trim().equals("b")){
                        b=name+":"+val;
                    }else if(code.trim().equals("c")){
                        c=name+":"+val;
                    }else if(code.trim().equals("d")){
                        d=name+":"+val;
                    }else if(code.trim().equals("e")){
                        e=name+":"+val;
                    }else if(code.trim().equals("f")){
                        f=name+":"+val;
                    }
                }
            }
            java.util.List chronList=ele.getChildren("Chronology");
            for(int p=0;p<chronList.size();p++){
                String code="",val="",name="";
                org.jdom.Element enumele=(org.jdom.Element)chronList.get(p);
                code=enumele.getAttributeValue("code");
                name=enumele.getAttributeValue("name");
                val=enumele.getText();
                if(code.trim().equals("i")){
                    i=name+":"+val;
                }else if(code.trim().equals("j")){
                    j=name+":"+val;
                }else if(code.trim().equals("k")){
                    k1=name+":"+val;
                }else if(code.trim().equals("l")){
                    l=name+":"+val;
                }
            }
            
            if(disp.trim().equals("")){
                disp="Supplement ";
            }
            disp=disp+" : "+a+b+c+d+e+f+i+j+k1+l;
            
        }catch(Exception e){}
        return disp;
    }
    private void bnAddToPhysicalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnAddToPhysicalActionPerformed
        // Add your handling code here:
        try{
            java.util.Vector vec=new java.util.Vector(1,1);
            for(int i=0;i<dftIssuesToBound.getRowCount();i++){
                boolean  valid=((Boolean)dftIssuesToBound.getValueAt(i,3)).booleanValue();
                if(valid){
                    vec.add(new Integer(i));
                }
            }
            
            if(vec.size()>0){
                java.util.Vector vecEnum=new java.util.Vector(1,1);
                java.util.Hashtable ht=new java.util.Hashtable();
                String supply="",index="";
                for(int j=0;j<vec.size();j++){
                    String regId="",copyId="",type="";
                    int row=((Integer)vec.get(j)).intValue();
                    regId=dftIssuesToBound.getValueAt(row, 0).toString();
                    copyId=dftIssuesToBound.getValueAt(row, 1).toString();
                    type=dftIssuesToBound.getValueAt(row, 5).toString();
                    if(type.trim().equals("Regular Issue")){
                        vecEnum.addElement(dftIssuesToBound.getValueAt(row,4).toString());
                    }else if(type.trim().equals("Supplementary")){
                        supply+=parseSupplyIndexXML(dftIssuesToBound.getValueAt(row,4).toString())+" , ";
                    }else if(type.trim().equals("Index")){
                        index+=parseSupplyIndexXML(dftIssuesToBound.getValueAt(row,4).toString())+" , ";
                    }
                    
                    ht.put(regId, copyId);
                    
                }
                if(!supply.trim().equals("")){
                    supply="supply :"+supply;
                }
                if(!index.trim().equals("")){
                    index="index :"+index;
                }
                // new compression.IssuesImprovedCompressor(vec,"");
                System.out.println("captionAndPat  "+this.captionAndPat+" vec size  " +vecEnum.size());
                compression.IssuesImprovedCompressor compress=new  compression.IssuesImprovedCompressor(vecEnum,this.captionAndPat);
                String dispiss="";
                dispiss=compress.getCompressedIssues();
                System.out.println("in FirmOrderIssuePanel IssuesImprovedCompressor  "+dispiss);
                Object obj[]=new Object[3];
                obj[0]=String.valueOf(dftPhysicalVolume.getRowCount());
                obj[1]=dispiss+supply+index;
                obj[2]=ht;
                dftPhysicalVolume.addRow(obj);
                bnAddToPhysical.setText("Add to physical volume "+(dftPhysicalVolume.getRowCount()+1));
                bnBudgetComponent.setEnabled(true);
                for(int j=vec.size()-1;j>=0;j--){
                    int row=((Integer)vec.get(j)).intValue();
                    dftIssuesToBound.removeRow(row);
                }
                
                
                
            }
            
            
            
        }catch(Exception e){e.printStackTrace();}
        
        
    }//GEN-LAST:event_bnAddToPhysicalActionPerformed
    
    public void refresh(){
        tfCallNumber.setText("");
        tfPositionOfCallNumber.setText("");
        tfPositionOfIssueNumbers.setText("");
        tfPositionOfMonths.setText("");
        tfPositionOfNameOfLibrary.setText("");
        tfPositionOfSpineTitle.setText("");
        tfPositionOfVolumeNumber.setText("");
        tfPositionOfYears.setText("");
        tfSpineTitle.setText("");
      
    }
  
    private void bnBindingspecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnBindingspecActionPerformed
        // Add your handling code here:
        if(newgen.presentation.component.FieldValidator.getInstance().isPositiveInt(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("NoOfPhysicalVolumesWhenBound"),tfNoOfVolumes.getText().trim())){
             tfNoofPhysicalVolumes.setText(tfNoOfVolumes.getText());
             cmbBindType.grabFocus();
             dialogBindingSpec.show();
        }else
             tfNoOfVolumes.grabFocus();
    }//GEN-LAST:event_bnBindingspecActionPerformed
    public void getBindingIssuesAndSpecification(String subscriptionId,String libraryId,String physListLibId,String logicalId,String ordeId,String binderId){
        
        try{
            this.subscriptionId=subscriptionId;
            this.subscriptionLibId=libraryId;
            this.libraryId=physListLibId;
            this.logicalListId=logicalId;
            this.orderId=ordeId;
            this.binderId=binderId;
            refresh();
            org.jdom.Element root=new org.jdom.Element("OperationId");
            root.setAttribute("no","14");
            org.jdom.Element ele=new org.jdom.Element("subscriptionId");
            ele.setText(subscriptionId);
            root.addContent(ele);
            
            ele=new org.jdom.Element("libraryId");
            ele.setText(libraryId);
            root.addContent(ele);
            
            ele=new org.jdom.Element("physicalListLibId");
            ele.setText(physListLibId);
            root.addContent(ele);
            
            ele=new org.jdom.Element("logicalId");
            ele.setText(logicalId);
            root.addContent(ele);
            String xmlreq="";
            org.jdom.Document doc=new org.jdom.Document(root);
            xmlreq=(new org.jdom.output.XMLOutputter().outputString(doc));
            
            xmlreq=newgen.presentation.component.ServletConnector.getInstance().sendRequest("FirmOrderBindingServlet", xmlreq);
            System.out.println("xmlReq  "+xmlreq);
            String caption="";
            
            org.jdom.input.SAXBuilder sax=new org.jdom.input.SAXBuilder();
            org.jdom.Document doc1=sax.build(new java.io.StringReader(xmlreq.trim()));
            
            caption=doc1.getRootElement().getChildText("caption");
            this.captionAndPat=caption;
            java.util.List lstIssue=doc1.getRootElement().getChildren("issue");
            for(int j=0;j<lstIssue.size();j++){
                Object obj[]=new Object[6];
                String logicId="",regId="",copyId="",enumChron="",xmlDump="",type="";
                org.jdom.Element ele12=(org.jdom.Element)lstIssue.get(j);
                regId=ele12.getChildText("registrationId");
                copyId=ele12.getChildText("copyId");
                xmlDump=ele12.getChildText("enumChron");
                type=ele12.getChildText("issuetype");
                if(type.trim().equals("853")){
                    type="Regular Issue";
                }else if(type.trim().equals("854")){
                    type="Supplementary";
                }else if(type.trim().equals("855")){
                    type="Index";
                }
                obj[0]=regId;
                obj[1]=copyId;
                obj[2]=this.getEnumChronDisplayString(xmlDump);
                obj[3]=new Boolean(false);
                obj[4]=xmlDump;
                obj[5]=type;
                dftIssuesToBound.addRow(obj);
                
                
            }
            try{
                org.jdom.Element specificationEle=doc1.getRootElement().getChild("specification");
                tfPricePerVolume.setText(specificationEle.getChildText("Price"));
                tfSpineTitle.setText(specificationEle.getChildText("SpineTitle"));
                tfPositionOfSpineTitle.setText(specificationEle.getChildText("PositionSpineTitle"));
                tfPositionOfVolumeNumber.setText(specificationEle.getChildText("PositionOfVolumeNumber"));
                tfPositionOfYears.setText(specificationEle.getChildText("PositionOfYears"));
                tfPositionOfMonths.setText(specificationEle.getChildText("PositionOfMonths"));
                tfCallNumber.setText(specificationEle.getChildText("CallNumber"));
                tfPositionOfCallNumber.setText(specificationEle.getChildText("PositionOfCallNumber"));
                tfPositionOfIssueNumbers.setText(specificationEle.getChildText("PositionOfIssueNumbers"));
                tfPositionOfNameOfLibrary.setText(specificationEle.getChildText("PositionOfNameOfLibrary"));
                String bindType=specificationEle.getChildText("BindType");
                tfNoofPhysicalVolumes.setText(specificationEle.getChildText("PhysicalIssuesInbound"));
                tfNoOfVolumes.setText(specificationEle.getChildText("PhysicalIssuesInbound"));
                org.jdom.Element eleBindDeta=specificationEle.getChild("BindingSpecifications");
                java.util.List lstBindDet=eleBindDeta.getChildren("BindingDetail");
                cmbBindType.removeAllItems();
                for(int jk=0;jk<lstBindDet.size();jk++){
                    org.jdom.Element elebind=(org.jdom.Element)lstBindDet.get(jk);
                    String str[]=new String[4];
                    str[0]=elebind.getAttributeValue("Id").trim();
                    str[1]=elebind.getChildText("Name").trim();
                    str[2]=elebind.getChildText("Colour").trim();
                    str[3]=elebind.getChildText("Price");
                    System.out.println("bind Spec  "+str[0]+" "+str[1]+" "+str[2]+" "+str[3]);
                    
                    hashBindDetails.put(str[0],str);
                    hashBindName.put(str[1].trim(), str);
                    cmbBindType.addItem(str[1]);
                }
                System.out.println("insdsa  "+bindType+" "+hashBindName.size());
                if(bindType!=null&&!bindType.trim().equals("")){
                    cmbBindType.setSelectedItem(bindType.trim());
                }
            }catch(Exception e2){e2.printStackTrace();}
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public java.util.ArrayList getApprovedBudgetHead(String entryId){
        java.util.ArrayList alBudgets = null;
        java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("LibraryID", newgen.presentation.NewGenMain.getAppletInstance().getLibraryID());
        ht.put("EntryID", entryId);
        java.util.Vector[] vector = newGenXMLGenerator.parseXMLDocument(new String[]{"BudgetHead"}, newgen.presentation.component.ServletConnector.getInstance().sendRequest("FirmOrderBindingServlet", newGenXMLGenerator.buildXMLDocument("12", ht)));
        alBudgets = new java.util.ArrayList();
        for(int i = 0; i < vector[0].size(); i++){
            alBudgets.add(vector[0].elementAt(i));
        }
        return alBudgets;
    }
    private void bnBudgetComponentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnBudgetComponentActionPerformed
        // Add your handling code here:
        
        
        // Add your handling code here:
        if(tableIssuesVolume.getRowCount()>0){
            
            double noofvolumes = new Double(tableIssuesVolume.getRowCount()).doubleValue();
            double amt = new Double(tfPricePerVolume.getText().trim()).doubleValue();
            double allocatedamt = noofvolumes * amt ;
            java.text.DecimalFormat form = new java.text.DecimalFormat();
            form.setMaximumFractionDigits(2);
            form.setGroupingUsed(false);
            String amount = form.format(allocatedamt);
            String LibraryId =newgen.presentation.NewGenMain.getAppletInstance().getLibraryID();
            String entryId = newgen.presentation.NewGenMain.getAppletInstance().getEntryID();
            java.util.ArrayList a1 = new java.util.ArrayList();
            a1.add(LibraryId);
            String entryId12 = newgen.presentation.NewGenMain.getAppletInstance().getEntryID();
            arrBudgets = getApprovedBudgetHead(entryId12);
            System.out.println("Budgets array   "+arrBudgets.size()+"  "+arrBudgets);
            newgen.presentation.component.BudgetComponent2  budgetCom2=new newgen.presentation.component.BudgetComponent2(true, LibraryId, (new Double(amount)).doubleValue(), null, arrBudgets, (new java.util.Hashtable()));
            int budgetcode=budgetCom2.getReturnCode();
            if(budgetcode==0) {
                System.out.println("=======budgetcode==0============");
//            java.util.Hashtable  ht1=budgetPanel.getBudgetIds();
//
//            System.out.println("budgets HashTable... "+ht1);
                java.util.Hashtable budgetIds = new java.util.Hashtable();
//            budgetIds=ht1;
                budgetIds=budgetCom2.updateDetails();
                //budgetIds = (java.util.Hashtable)defTbModel.getValueAt(table.getSelectedRow(), 8);
                if(budgetIds.get("Budgets") != null){
                    try {
                        //AllocatedAmount
                        java.util.ArrayList datax = ((java.util.ArrayList)budgetIds.get("Budgets"));
                        if(datax.size() > 0){
                            dftBudgets.setRowCount(0);
                            for(int i = 0; i < datax.size(); i++) {
                                Object row1[]=new Object[6];
                                java.util.Hashtable ht = (java.util.Hashtable)(datax.get(i));
                                row1[0]=ht.get("LibraryId").toString();
                                row1[1]=ht.get("BudgetId").toString();
                                row1[2]=newgen.presentation.NewGenMain.getAppletInstance().getLibraryName(ht.get("LibraryId").toString());
                                row1[3]=ht.get("BudgetHead").toString();
                                row1[4]=ht.get("BudgetSource").toString();
                                row1[5]=ht.get("AllocatedAmount").toString();
                                dftBudgets.addRow(row1);
                            }
                        }
                    }catch(Exception exp){}
                }
            }else budgetCom2.setVisible(true);
        }else{
            newgen.presentation.NewGenMain.getAppletInstance().showInformationMessage("No Physicalvolumes has been prepared");
        }
        
    }//GEN-LAST:event_bnBudgetComponentActionPerformed
    
    /** Getter for property hashBindName.
     * @return Value of property hashBindName.
     *
     */
    public java.util.Hashtable getHashBindName() {
        return hashBindName;
    }
    
    /** Setter for property hashBindName.
     * @param hashBindName New value of property hashBindName.
     *
     */
    public void setHashBindName(java.util.Hashtable hashBindName) {
        this.hashBindName = hashBindName;
    }
    
    /** Getter for property hashBindDetails.
     * @return Value of property hashBindDetails.
     *
     */
    public java.util.Hashtable getHashBindDetails() {
        return hashBindDetails;
    }
    
    /** Setter for property hashBindDetails.
     * @param hashBindDetails New value of property hashBindDetails.
     *
     */
    public void setHashBindDetails(java.util.Hashtable hashBindDetails) {
        this.hashBindDetails = hashBindDetails;
    }
    public int addToCurrentOrder(){
        int k=0;
        try{
            int phyvol=dftPhysicalVolume.getRowCount();
            if(phyvol>0){
                org.jdom.Element root=new org.jdom.Element("OperationId");
                root.setAttribute("no","15");
                
                org.jdom.Element ele=new org.jdom.Element("subscriptionId");
                ele.setText(this.subscriptionId);
                root.addContent(ele);
                
                ele=new org.jdom.Element("subscriptionLibId");
                ele.setText(this.subscriptionLibId);
                root.addContent(ele);
                
                ele=new org.jdom.Element("physicalLibId");
                ele.setText(this.libraryId);
                root.addContent(ele);
                
                ele=new org.jdom.Element("logicalListId");
                ele.setText(this.logicalListId);
                root.addContent(ele);
                
                ele=new org.jdom.Element("orderId");
                ele.setText(this.orderId);
                root.addContent(ele);
                
                ele=new org.jdom.Element("binderId");
                ele.setText(this.binderId);
                root.addContent(ele);
                
                ele=new org.jdom.Element("binderTypeId");
                ele.setText(this.binderTypeId);
                root.addContent(ele);
                
                
                ele=new org.jdom.Element("physicalvolumes");
                ele.setText(String.valueOf(dftPhysicalVolume.getRowCount()));
                root.addContent(ele);
                
                ele=new org.jdom.Element("entryId");
                ele.setText(newgen.presentation.NewGenMain.getAppletInstance().getEntryID());
                root.addContent(ele);
                
                ele=new org.jdom.Element("allocatedAmt");
                root.addContent(ele);
                //============Specification====================
                org.jdom.Element spec=new org.jdom.Element("specification");
                org.jdom.Element spineTitle = new org.jdom.Element("SpineTitle");
                spineTitle.setText(tfSpineTitle.getText());
                spec.addContent(spineTitle);
                org.jdom.Element positionspineTitle = new org.jdom.Element("PositionSpineTitle");
                positionspineTitle.setText(tfPositionOfSpineTitle.getText());
                spec.addContent(positionspineTitle);
                org.jdom.Element positionofvolumeNumber = new org.jdom.Element("PositionOfVolumeNumber");
                positionofvolumeNumber.setText(tfPositionOfVolumeNumber.getText());
                spec.addContent(positionofvolumeNumber);
                org.jdom.Element positionofyears = new org.jdom.Element("PositionOfYears");
                positionofyears.setText(tfPositionOfYears.getText());
                spec.addContent(positionofyears);
                org.jdom.Element positionofmonths = new org.jdom.Element("PositionOfMonths");
                positionofmonths.setText(tfPositionOfMonths.getText());
                spec.addContent(positionofmonths);
                org.jdom.Element positionofnameoflibrary = new org.jdom.Element("PositionOfNameOfLibrary");
                positionofnameoflibrary.setText(tfPositionOfNameOfLibrary.getText());
                spec.addContent(positionofnameoflibrary);
                org.jdom.Element positionofissues = new org.jdom.Element("PositionOfIssueNumbers");
                positionofissues.setText(tfPositionOfIssueNumbers.getText());
                spec.addContent(positionofissues);
                org.jdom.Element callnumber = new org.jdom.Element("CallNumber");
                callnumber.setText(tfCallNumber.getText());
                spec.addContent(callnumber);
                org.jdom.Element positionofcallnumber = new org.jdom.Element("PositionOfCallNumber");
                positionofcallnumber.setText(tfPositionOfCallNumber.getText());
                spec.addContent(positionofcallnumber);
                root.addContent(spec);
                //=========End of Specification============================
                //  org.jdom.Element bindspec=new org.jdom.Element("");
                for(int j=0;j<dftPhysicalVolume.getRowCount();j++){
                    String volId="";
                    java.util.Hashtable ht=new java.util.Hashtable();
                    volId=dftPhysicalVolume.getValueAt(j, 0).toString();
                    ht=(java.util.Hashtable)dftPhysicalVolume.getValueAt(j, 2);
                    
                    
                    
                    java.util.Enumeration enum=ht.keys();
                    while(enum.hasMoreElements()){
                        
                        String regId="",copyId="";
                        regId=enum.nextElement().toString();
                        copyId=ht.get(regId).toString();
                        org.jdom.Element regdet=new org.jdom.Element("RegistrationIdDetails");
                        org.jdom.Element reg=new org.jdom.Element("registrationId");
                        reg.setText(regId);
                        regdet.addContent(reg);
                        reg=new org.jdom.Element("copyId");
                        reg.setText(copyId);
                        regdet.addContent(reg);
                        
                        reg=new org.jdom.Element("physicalVolumeId");
                        reg.setText(String.valueOf(j+1));
                        regdet.addContent(reg);
                        root.addContent(regdet);
                    }
                    
                    
                    
                    
                }
                
                for(int kl=0;kl<dftBudgets.getRowCount();kl++){
                    org.jdom.Element budget=new org.jdom.Element("BudgetDetails");
                    org.jdom.Element budgetele=new org.jdom.Element("BudgetId");
                    budgetele.setText(dftBudgets.getValueAt(kl, 1).toString());
                    budget.addContent(budgetele);
                    
                    budgetele=new org.jdom.Element("LibraryId");
                    budgetele.setText(dftBudgets.getValueAt(kl,0).toString());
                    budget.addContent(budgetele);
                    
                    budgetele=new org.jdom.Element("Amount");
                    budgetele.setText(dftBudgets.getValueAt(kl,5).toString());
                    budget.addContent(budgetele);
                    root.addContent(budget);
                }
                
                org.jdom.Document doc=new org.jdom.Document(root);
                String  xmlRes=(new org.jdom.output.XMLOutputter()).outputString(doc);
                xmlRes=newgen.presentation.component.ServletConnector.getInstance().sendRequest("FirmOrderBindingServlet",xmlRes);
                System.out.println("xmlres  "+xmlRes);
                org.jdom.input.SAXBuilder sax=new org.jdom.input.SAXBuilder();
                org.jdom.Document doc1=sax.build(new java.io.StringReader(xmlRes));
                if(doc1.getRootElement().getChildText("status").trim().equals("success")){
                    k=2;
                }else{
                    k=-1;
                }
                
                
                
            }else{
                k=1;
            }
            
        }catch(Exception e){e.printStackTrace();}
        return k;
    }
    
    /** Getter for property binderId.
     * @return Value of property binderId.
     *
     */
    public java.lang.String getBinderId() {
        return binderId;
    }
    
    /** Setter for property binderId.
     * @param binderId New value of property binderId.
     *
     */
    public void setBinderId(java.lang.String binderId) {
        this.binderId = binderId;
    }

    public void tableChanged(TableModelEvent e) {
        if(e.getSource().equals(dftBudgets)){
            if(dftBudgets.getRowCount()>0){
                lbMessage.setText("");
            }else{
                lbMessage.setText("Warning: No budget heads are selected");
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBudgetCancel;
    private javax.swing.JButton bBudgetCancel1;
    private javax.swing.JButton bBudgetOk;
    private javax.swing.JButton bBudgetOk1;
    private javax.swing.JButton bCancelModify;
    private javax.swing.JButton bCloseOrderInfo;
    private javax.swing.JButton bCloseOrderNos;
    private javax.swing.JButton bOkModify;
    private javax.swing.JButton bOkOrderInfo;
    private javax.swing.JButton bOkOrderNos;
    private javax.swing.JLabel bcurrency;
    private javax.swing.JButton bnAddToPhysical;
    private javax.swing.JButton bnBindingspec;
    private javax.swing.JButton bnBudgetComponent;
    private javax.swing.JButton bnDelete;
    private javax.swing.JButton bnSpecCancel;
    private javax.swing.JButton bnSpecOk;
    private newgen.presentation.component.BudgetPanel budgetPanel;
    private javax.swing.JComboBox cmbBindType;
    private javax.swing.JDialog dialogBindingSpec;
    private javax.swing.JDialog dialogBudget;
    private javax.swing.JDialog dialogBudget1;
    private javax.swing.JDialog dialogOrderInfo;
    private javax.swing.JDialog dialogOrderNos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lBatchDate;
    private javax.swing.JLabel lbMessage;
    private javax.swing.JDialog modifyDialog;
    private javax.swing.JPanel panel14;
    private javax.swing.JPanel panel15;
    private javax.swing.JPanel panel4;
    private javax.swing.JPanel panel5;
    private javax.swing.JScrollPane spOrderNos;
    private newgen.presentation.component.DateField tArrivalDate;
    private newgen.presentation.component.DateField tBatchDate;
    private javax.swing.JTable tabIssuesToBound;
    private javax.swing.JTable tabOrderNos;
    private javax.swing.JTable tableBudgets;
    private javax.swing.JTable tableIssuesVolume;
    private newgen.presentation.UnicodeTextField tfBindTypePrice;
    private javax.swing.JTextField tfCallNumber;
    private newgen.presentation.UnicodeTextField tfColour;
    private newgen.presentation.UnicodeTextField tfNoOfVolumes;
    private newgen.presentation.UnicodeTextField tfNoofPhysicalVolumes;
    private newgen.presentation.UnicodeTextField tfOrdernoModify;
    private javax.swing.JTextField tfPositionOfCallNumber;
    private javax.swing.JTextField tfPositionOfIssueNumbers;
    private javax.swing.JTextField tfPositionOfMonths;
    private javax.swing.JTextField tfPositionOfNameOfLibrary;
    private javax.swing.JTextField tfPositionOfSpineTitle;
    private javax.swing.JTextField tfPositionOfVolumeNumber;
    private javax.swing.JTextField tfPositionOfYears;
    private newgen.presentation.UnicodeTextField tfPricePerVolume;
    private javax.swing.JTextField tfSpineTitle;
    // End of variables declaration//GEN-END:variables
    private java.util.ArrayList arrBudgets = null;
    private int budgetDialogReturnCode=1;
    private java.util.Hashtable hashBindDetails=new java.util.Hashtable();
    private java.util.Hashtable hashBindName=new java.util.Hashtable();
    private int physicalVolumeCount=0;
    private String subscriptionLibId="",subscriptionId="",libraryId="",logicalListId="",binderTypeId="",physicalIssue="",orderId="",binderId="",captionAndPat="";
    
}
