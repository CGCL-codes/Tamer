/*
 * OPACMain.java
 *
 * Created on April 26, 2003, 10:48 AM
 */

package newgen.presentation;
import newgen.presentation.component.*;
import newgen.presentation.circulation.*;
import javax.help.*;
import java.net.*;
import org.jdom.*;
import org.jdom.input.*;
/**
 *
 * @author  vasu praveen
 */
public class OPACMain extends javax.swing.JApplet {
    
    private String showPhotoStatus = "";
    /** Creates new form OPACMain */
    public OPACMain() {
        this.MyResource = java.util.ResourceBundle.getBundle("Administration",this.thisLocale);
        this.ComponentResource = java.util.ResourceBundle.getBundle("component",this.thisLocale);
//        System.out.println(System.getProperty("os.name"));
        javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            //            javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new newgen.presentation.component.CharcoalTheme());
            //            javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new newgen.presentation.component.KhakiMetalTheme());
            //            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            if(System.getProperty("os.name").equals("Windows XP"))
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            else {
                                javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new newgen.presentation.component.KhakiMetalTheme());
                //javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new newgen.presentation.component.AquaMetalTheme());
                javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            }
        }catch(Exception e){}
        
        contrast = new newgen.presentation.component.ContrastMetalTheme();
        aqua = new newgen.presentation.component.AquaMetalTheme();
        green = new newgen.presentation.component.GreenMetalTheme();
        khakhi = new newgen.presentation.component.KhakiMetalTheme();
        
        applet = this;
        initComponents();
        splash.setSize(458, 301);
        splash.show();
        splash.setLocation(this.getLocation(458,301));
        timer = new javax.swing.Timer(5000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                
                splash.dispose();
                timer.stop();
            }
        });
        timer.start();
        //        menuBar.setVisible(false);
        //        initialDataFill();
        loadLibraries();
        newgen.presentation.component.Utility.getInstance().getLibraryDetails(cLibrary);
        ((java.awt.CardLayout)this.jPanel1.getLayout()).show(this.jPanel1, "card1");
        
        helpset = null;
        ClassLoader loader = this.getClass().getClassLoader();
        URL url = HelpSet.findHelpSet(loader, "newgen.hs");
        try {
            helpset = new HelpSet(loader, url);
            helpbroker = helpset.createHelpBroker();
//            System.out.println("Loaded Successfully");
        } catch (HelpSetException e) {
            System.out.println(e);
            //return;
        }catch (Exception ex){
            ex.printStackTrace(System.out);
        }
        
        newgen.presentation.Privilege priv = new newgen.presentation.Privilege();
        priv.setApprovalOfCataloguing('Y');
        priv.setPrimaryCataloguing('Y');
        priv.setImport('Y');
        priv.setAuthorityFilesDelete('Y');
        priv.setAuthorityFilesModify('Y');
        priv.setAuthorityFilesNew('Y');
        priv.setILLApprovalAuthority('N');
        priv.setReservationAuthority('Y');
        priv.setModificationOfApprovedCatalogueRecord('Y');
        
        if(getEntryID().equals("94ED033")){
            priv.setACQRequestRefineAuthority('Y');
            priv.setBudgetApprovingAuthority('Y');
        }else{
            priv.setACQRequestRefineAuthority('N');
            priv.setBudgetApprovingAuthority('N');
        }
        this.privilege = priv;
                this.bOk.doClick();
    }
    public void loadLibraries() {
        allLibraries= new java.util.Hashtable();
        String xmlreq = NewGenMainXMLGenerator.getInstance().getAllLibraries("16");
        String xmlres = newgen.presentation.component.ServletConnector.getInstance().sendRequest("NewGenServlet", xmlreq);
        try    {
            org.jdom.Document doc = (new SAXBuilder()).build(new java.io.StringReader(xmlres));
            java.util.List li = doc.getRootElement().getChildren();
            for(int i=0; i<li.size(); i++) {
                allLibraries.put(((org.jdom.Element)li.get(i)).getChildText("Id"), ((org.jdom.Element)li.get(i)).getChildText("name"));
            }
        }catch(Exception exp){exp.printStackTrace();}
    }
    public void initialDataFill() {
        cataloguesatellitelibraries = new java.util.Hashtable();
        //        this.mCirculation.setText(this.getMyResource().getBundle("Administration").getString("Circulation"));
        
        //////////////////////////Initial Data Fill (very very important)////////////////////
        
        
        String xmlreq = NewGenMainXMLGenerator.getInstance().initialDataLoadup(this.getEntryID(),this.getLibraryID());
//        System.out.println("near data fuill "+xmlreq);
        String xmlres = newgen.presentation.component.ServletConnector.getInstance().sendRequest("NewGenServlet",xmlreq);
//        System.out.println(xmlres);
        
        SAXBuilder sb = new SAXBuilder();
        Document doc = null;
        String libraryid = "1";
        try {
            doc = sb.build(new java.io.StringReader(xmlres));
        }catch (Exception exp){System.out.println(exp);}
        Element elelibs = doc.getRootElement().getChild("AllLibraries");
        java.util.List lialllibs = elelibs.getChildren();
        for(int i=0; i<lialllibs.size();i++) {
            Element ele = (Element)lialllibs.get(i);
            allLibraries.put(ele.getChild("LibraryId").getText(),ele.getChild("LibraryName").getText());
        }
        
        elelibs = doc.getRootElement().getChild("AllMaterialTypes");
        lialllibs = elelibs.getChildren();
        for(int i=0; i<lialllibs.size();i++) {
            Element ele = (Element)lialllibs.get(i);
            materials.put(ele.getChild("MaterialTypeId").getText(),ele.getChild("MaterialType").getText());
        }
        elelibs = doc.getRootElement().getChild("AllMarcMaterialTypes");
        lialllibs = elelibs.getChildren();
        for(int i=0; i<lialllibs.size();i++) {
            Element ele = (Element)lialllibs.get(i);
            marcMaterialTypes.put(ele.getChild("MaterialTypeId").getText(),ele.getChild("MaterialType").getText());
        }
        this.marcMaterialTypes.remove("15");
        if(doc.getRootElement().getChild("CatalogueLibraryStatus").getAttributeValue("STATUS").trim().equals("MASTER")) {
            java.util.List licat = doc.getRootElement().getChild("CatalogueLibraryStatus").getChildren("CatalogueSatelliteLibrary");
            this.cataloguingSatelliteLibraries = new java.util.ArrayList();
            for(int i=0;i<licat.size();i++) {
                org.jdom.Element elex = (org.jdom.Element)licat.get(i);
                this.cataloguesatellitelibraries.put(elex.getText().trim(),this.allLibraries.get(elex.getText().trim()));
                this.cataloguingSatelliteLibraries.add(elex.getText().trim());
//                System.out.println(this.allLibraries.get(elex.getText().trim()));
                
            }
            this.cataloguesatellitelibraries.put(libraryid,this.allLibraries.get(libraryid));
        }
        else if(doc.getRootElement().getChild("CatalogueLibraryStatus").getAttributeValue("STATUS").trim().equals("SATELLITE")) {
            //                jMenuBar1.remove(menuCataloguing);
            //                menuCataloguing.remove(0);
            //                menuCataloguing.remove(1);
            //                menuCataloguing.remove(2);
            //                menuCataloguing.remove(3);
            //                menuCataloguing.remove(4);
            //                menuCataloguing.remove(5);
        }
        
        //write code here to fetch show_photo status from general_setup_pmt
        showPhotoStatus = "Y";
        
        this.acquisitionSatelliteLibraries = new java.util.ArrayList();
        this.acquisitionSatelliteLibraries.add("3");
        this.acquisitionSatelliteLibraries.add("4");
        this.acquisitionSatelliteLibraries.add("5");
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        splash = new javax.swing.JWindow();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        plDesktop = new javax.swing.JPanel();
        desktop = new javax.swing.JDesktopPane();
        jLabel6 = new javax.swing.JLabel();
        plLogin = new javax.swing.JPanel();
        pl = new javax.swing.JPanel();
        pl1 = new javax.swing.JPanel();
        lLibrary = new javax.swing.JLabel();
        cLibrary = new javax.swing.JComboBox();
        lPatronID = new javax.swing.JLabel();
        tPatronID = new newgen.presentation.UnicodeTextField();
        jLabel1 = new javax.swing.JLabel();
        tfPassword = new javax.swing.JPasswordField();
        pl2 = new javax.swing.JPanel();
        bOk = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bCSH = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        miSearchCatalogue = new javax.swing.JMenuItem();

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(275, 60));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 48));
        jLabel2.setForeground(java.awt.Color.blue);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("NewGenLib");
        jPanel2.add(jLabel2);

        jPanel4.add(jPanel2);

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));
        jLabel5.setFont(new java.awt.Font("Arial", 1, 18));
        jLabel5.setForeground(new java.awt.Color(0, 97, 0));
        jLabel5.setText("OPAC");
        jPanel6.add(jLabel5);

        jPanel4.add(jPanel6);

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 18));
        jLabel3.setText("New generation in library automation");
        jPanel3.add(jLabel3);

        jPanel4.add(jPanel3);

        jPanel5.setBackground(new java.awt.Color(204, 204, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/Logo_kiikm_small.jpg")));
        jPanel5.add(jLabel4);

        jPanel4.add(jPanel5);

        splash.getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.CardLayout());

        plDesktop.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 48));
        jLabel6.setText("<html><CENTER>Welcome to the OPAC of Osmania University</CENTER></html>");
        jLabel6.setBounds(130, 130, 470, 280);
        desktop.add(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);

        plDesktop.add(desktop, java.awt.BorderLayout.CENTER);

        jPanel1.add(plDesktop, "card2");

        plLogin.setLayout(new java.awt.GridBagLayout());

        plLogin.setBorder(new javax.swing.border.EtchedBorder());
        pl.setLayout(new javax.swing.BoxLayout(pl, javax.swing.BoxLayout.Y_AXIS));

        pl.setPreferredSize(new java.awt.Dimension(410, 150));
        pl1.setLayout(new java.awt.GridBagLayout());

        pl1.setBorder(new javax.swing.border.EtchedBorder());
        pl1.setPreferredSize(new java.awt.Dimension(10, 180));
        lLibrary.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Library"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl1.add(lLibrary, gridBagConstraints);

        cLibrary.setPreferredSize(new java.awt.Dimension(165, 25));
        cLibrary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cLibraryActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl1.add(cLibrary, gridBagConstraints);

        lPatronID.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("PatronID"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl1.add(lPatronID, gridBagConstraints);

        tPatronID.setText("1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pl1.add(tPatronID, gridBagConstraints);

        jLabel1.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Password"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pl1.add(jLabel1, gridBagConstraints);

        tfPassword.setColumns(15);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pl1.add(tfPassword, gridBagConstraints);

        pl.add(pl1);

        pl2.setBorder(new javax.swing.border.EtchedBorder());
        pl2.setPreferredSize(new java.awt.Dimension(10, 40));
        bOk.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Login"));
        bOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOkActionPerformed(evt);
            }
        });

        pl2.add(bOk);

        bHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/help.gif")));
        pl2.add(bHelp);

        bCSH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/newgen/images/helpcsh.gif")));
        pl2.add(bCSH);

        bCancel.setText(newgen.presentation.NewGenMain.getAppletInstance().getMyResource().getString("Cancel"));
        pl2.add(bCancel);

        pl.add(pl2);

        plLogin.add(pl, new java.awt.GridBagConstraints());

        jPanel1.add(plLogin, "card1");

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenu1.setText(this.getMyResource().getString("OPAC"));
        miSearchCatalogue.setText("Search catalogue");
        miSearchCatalogue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miSearchCatalogueActionPerformed(evt);
            }
        });

        jMenu1.add(miSearchCatalogue);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

    }//GEN-END:initComponents
    
    private void cLibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cLibraryActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_cLibraryActionPerformed
    
    private void miSearchCatalogueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miSearchCatalogueActionPerformed
        // Add your handling code here:
//        newgen.presentation.opac.cataloguing.SearchCatalogueInternalFrame scif = newgen.presentation.opac.cataloguing.SearchCatalogueInternalFrame.getInstance();
//        this.addToDesktop(scif);
    }//GEN-LAST:event_miSearchCatalogueActionPerformed
    
    private void bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOkActionPerformed
        ((java.awt.CardLayout)this.jPanel1.getLayout()).show(this.jPanel1, "card2");
        menuBar.setVisible(true);
        this.libraryID = getLibraryId(""+cLibrary.getSelectedItem());
//        System.out.println("libraryID : "+this.getLibraryID());
//        System.out.println("entryID : "+this.getEntryID());
        this.initialDataFill();
        if(getEntryID().equals("94ED033") || getEntryID().equals("94ED032")){
            getPrivilege().setACQRequestRefineAuthority('Y');
            getPrivilege().setBudgetApprovingAuthority('Y');
        }else{
            getPrivilege().setACQRequestRefineAuthority('N');
            getPrivilege().setBudgetApprovingAuthority('N');
        }
    }//GEN-LAST:event_bOkActionPerformed
    
    public static OPACMain getAppletInstance() {
        if(applet == null)
            new OPACMain();
        return applet;
    }
    
    public javax.swing.JDesktopPane getDesktopPane() {
        return desktop;
    }
    
    public java.util.ResourceBundle getMyResource() {
        return MyResource;
    }
    
    public java.util.ResourceBundle getComponentResource() {
        return ComponentResource;
    }
    
    
    public void showErrorMessage(java.lang.String message) {
        javax.swing.JOptionPane.showMessageDialog(null, message, "Network or database error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }
    
    public void showInformationMessage(java.lang.String message) {
        javax.swing.JOptionPane.showMessageDialog(null, message, "Information", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    public java.awt.Point getLocation(int width, int height) {
        java.awt.Dimension dimen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double swidth = dimen.getWidth();
        double sheight = dimen.getHeight();
        double px = (swidth-width)/2;
        double py = (sheight-height)/2;
        
        java.awt.Point pret = new java.awt.Point((int)px,(int)py);
        return pret;
    }
    
    public void showInsufficientDataDialog(String msg){
        javax.swing.JOptionPane.showMessageDialog(this, msg, "Insufficient Data", javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    
    public String getLibraryID(){
        return libraryID;
    }
    
    public String getEntryID(){
        return tPatronID.getText();
    }
    
    /** Getter for property helpbroker.
     * @return Value of property helpbroker.
     */
    public HelpBroker getHelpbroker() {
        return helpbroker;
    }
    
    /** Setter for property helpbroker.
     * @param helpbroker New value of property helpbroker.
     */
    public void setHelpbroker(HelpBroker helpbroker) {
        this.helpbroker = helpbroker;
    }
    
    /** Getter for property helpset.
     * @return Value of property helpset.
     */
    public HelpSet getHelpset() {
        return helpset;
    }
    
    /** Setter for property helpset.
     * @param helpset New value of property helpset.
     */
    public void setHelpset(HelpSet helpset) {
        this.helpset = helpset;
    }
    public java.util.ArrayList getCataloguingPool() {
        //return this.cataloguelibrarypool;
        return new java.util.ArrayList();
    }
    public void showWarningMessage(java.lang.String message) {
        javax.swing.JOptionPane.showMessageDialog(null, message, "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
    }
    
    public int showQuestionMessage(java.lang.String message) {
        int x = javax.swing.JOptionPane.showConfirmDialog(null,message,"Question ?",javax.swing.JOptionPane.YES_NO_OPTION);
        //        javax.swing.JOptionPane.showMessageDialog(null, message, "Question ?", javax.swing.JOptionPane.QUESTION_MESSAGE);
        return x;
    }
    
    /** Getter for property privilege.
     * @return Value of property privilege.
     */
    public newgen.presentation.Privilege getPrivilege() {
        return privilege;
    }
    
    /** Setter for property privilege.
     * @param privilege New value of property privilege.
     */
    public void setPrivilege(newgen.presentation.Privilege privilege) {
        this.privilege = privilege;
    }
    public void addToDesktop(javax.swing.JInternalFrame intfr) {
        javax.swing.JDesktopPane desk=this.desktop;
        javax.swing.JInternalFrame[] allfr= desk.getAllFrames();
        
        for (int i=0;i<allfr.length;i++){
            if( allfr[i].equals(intfr) ){
                allfr[i].setVisible(true); allfr[i].moveToFront();
                try{ intfr.setSelected(true); }catch(Exception ex){ System.out.println("ex1: "+ex);}
            }
        }
        
        
        if(desk != null){
            try {
                desk.add(intfr);
            }catch (Exception exp){this.addToDesktop(intfr);System.out.println("exp: "+exp); intfr.moveToFront();}
            intfr.setVisible(true);
            try{ intfr.setSelected(true); }catch(Exception ex){ System.out.println("ex: "+ex); }
            
        }
        
        
    }
    
    /** Getter for property materials.
     * @return Value of property materials.
     */
    public java.util.Hashtable getMaterials() {
        return materials;
    }
    
    public java.util.Vector getMaterielTypes(){
        java.util.Vector vector = new java.util.Vector(materials.values());
        return vector;
    }
    
    /** Setter for property materials.
     * @param materials New value of property materials.
     */
    public void setMaterials(java.util.Hashtable materials) {
        this.materials = materials;
    }
    
    /** Getter for property cataloguesatellitelibraries.
     * @return Value of property cataloguesatellitelibraries.
     */
    public java.util.Hashtable getCataloguesatellitelibraries() {
        return cataloguesatellitelibraries;
    }
    
    /** Setter for property cataloguesatellitelibraries.
     * @param cataloguesatellitelibraries New value of property cataloguesatellitelibraries.
     */
    public void setCataloguesatellitelibraries(java.util.Hashtable cataloguesatellitelibraries) {
        this.cataloguesatellitelibraries = cataloguesatellitelibraries;
    }
    public String getLibraryId(String libname) {
        String catsatlibid="";
        java.util.Enumeration enum = this.allLibraries.keys();
        while(enum.hasMoreElements()) {
            String key = enum.nextElement().toString();
            if(this.allLibraries.get(key).toString().trim().equals(libname.trim())) {
                catsatlibid=key;
            }
        }
        return catsatlibid;
    }
    public String getLibraryName(String libid) {
        //System.out.println("libid : "+libid);
        //System.out.println("allLibraries : "+allLibraries);
        return this.allLibraries.get(libid).toString();
    }
    public String getMaterialId(String matname) {
        String catsatlibid="";
        java.util.Enumeration enum = this.materials.keys();
        while(enum.hasMoreElements()) {
            String key = enum.nextElement().toString();
            if(this.materials.get(key).toString().trim().equals(matname.trim())) {
                catsatlibid=key;
            }
        }
        return catsatlibid;
    }
    public String getMaterialName(String matid) {
        //System.out.println("libid : "+matid);
        //System.out.println("allLibraries : "+allLibraries);
        return this.materials.get(matid).toString();
    }
    public String getMarcMaterialId(String matname) {
        String catsatlibid="";
        java.util.Enumeration enum = this.marcMaterialTypes.keys();
        while(enum.hasMoreElements()) {
            String key = enum.nextElement().toString();
            if(this.marcMaterialTypes.get(key).toString().trim().equals(matname.trim())) {
                catsatlibid=key;
            }
        }
        return catsatlibid;
    }
    public String getMarcMaterialName(String matid) {
        //System.out.println("libid : "+matid);
        //System.out.println("allLibraries : "+allLibraries);
        return this.marcMaterialTypes.get(matid).toString();
    }
    public String[] getPatronLibraryIds() {
        
        return this.patlib;
    }
    
    public String getDate(){
        return "01/01/2002";
    }
    
    public java.util.Date getServerDate(){
        //change this later
        //System.out.println("server date : "+new java.util.Date());
        return new java.util.Date();
    }
    
    /** Getter for property allLibraries.
     * @return Value of property allLibraries.
     */
    public java.util.Hashtable getAllLibraries() {
        return allLibraries;
    }
    
    /** Setter for property allLibraries.
     * @param allLibraries New value of property allLibraries.
     */
    public void setAllLibraries(java.util.Hashtable allLibraries) {
        this.allLibraries = allLibraries;
    }
    
    /** Getter for property marcMaterialTypes.
     * @return Value of property marcMaterialTypes.
     */
    public java.util.Hashtable getMarcMaterialTypes() {
        return marcMaterialTypes;
    }
    
    /** Setter for property marcMaterialTypes.
     * @param marcMaterialTypes New value of property marcMaterialTypes.
     */
    public void setMarcMaterialTypes(java.util.Hashtable marcMaterialTypes) {
        this.marcMaterialTypes = marcMaterialTypes;
    }
    
    /** Getter for property showPhotoStatus.
     * @return Value of property showPhotoStatus.
     *
     */
    public java.lang.String getShowPhotoStatus() {
        return showPhotoStatus;
    }
    
    /** Setter for property showPhotoStatus.
     * @param showPhotoStatus New value of property showPhotoStatus.
     *
     */
    public void setShowPhotoStatus(java.lang.String showPhotoStatus) {
        this.showPhotoStatus = showPhotoStatus;
    }
    
    /** Getter for property acquisitionStatus.
     * @return Value of property acquisitionStatus.
     *
     */
    public java.lang.String getAcquisitionStatus() {
        return acquisitionStatus;
    }
    
    /** Setter for property acquisitionStatus.
     * @param acquisitionStatus New value of property acquisitionStatus.
     *
     */
    public void setAcquisitionStatus(java.lang.String acquisitionStatus) {
        this.acquisitionStatus = acquisitionStatus;
    }
    
    /** Getter for property serialManagementStatus.
     * @return Value of property serialManagementStatus.
     *
     */
    public java.lang.String getSerialManagementStatus() {
        return serialManagementStatus;
    }
    
    /** Setter for property serialManagementStatus.
     * @param serialManagementStatus New value of property serialManagementStatus.
     *
     */
    public void setSerialManagementStatus(java.lang.String serialManagementStatus) {
        this.serialManagementStatus = serialManagementStatus;
    }
    
    /** Getter for property cataloguingStatus.
     * @return Value of property cataloguingStatus.
     *
     */
    public java.lang.String getCataloguingStatus() {
        return cataloguingStatus;
    }
    
    /** Setter for property cataloguingStatus.
     * @param cataloguingStatus New value of property cataloguingStatus.
     *
     */
    public void setCataloguingStatus(java.lang.String cataloguingStatus) {
        this.cataloguingStatus = cataloguingStatus;
    }
    
    /** Getter for property acquisitionsMasterLibrary.
     * @return Value of property acquisitionsMasterLibrary.
     *
     */
    public java.lang.String getAcquisitionsMasterLibrary() {
        return acquisitionsMasterLibrary;
    }
    
    /** Setter for property acquisitionsMasterLibrary.
     * @param acquisitionsMasterLibrary New value of property acquisitionsMasterLibrary.
     *
     */
    public void setAcquisitionsMasterLibrary(java.lang.String acquisitionsMasterLibrary) {
        this.acquisitionsMasterLibrary = acquisitionsMasterLibrary;
    }
    
    /** Getter for property serialManagementMasterLibrary.
     * @return Value of property serialManagementMasterLibrary.
     *
     */
    public java.lang.String getSerialManagementMasterLibrary() {
        return serialManagementMasterLibrary;
    }
    
    /** Setter for property serialManagementMasterLibrary.
     * @param serialManagementMasterLibrary New value of property serialManagementMasterLibrary.
     *
     */
    public void setSerialManagementMasterLibrary(java.lang.String serialManagementMasterLibrary) {
        this.serialManagementMasterLibrary = serialManagementMasterLibrary;
    }
    
    /** Getter for property cataloguingMasterLibrary.
     * @return Value of property cataloguingMasterLibrary.
     *
     */
    public java.lang.String getCataloguingMasterLibrary() {
        return cataloguingMasterLibrary;
    }
    
    /** Setter for property cataloguingMasterLibrary.
     * @param cataloguingMasterLibrary New value of property cataloguingMasterLibrary.
     *
     */
    public void setCataloguingMasterLibrary(java.lang.String cataloguingMasterLibrary) {
        this.cataloguingMasterLibrary = cataloguingMasterLibrary;
    }
    
    /** Getter for property cataloguingSatelliteLibraries.
     * @return Value of property cataloguingSatelliteLibraries.
     *
     */
    public java.util.ArrayList getCataloguingSatelliteLibraries() {
        return cataloguingSatelliteLibraries;
    }
    
    /** Setter for property cataloguingSatelliteLibraries.
     * @param cataloguingSatelliteLibraries New value of property cataloguingSatelliteLibraries.
     *
     */
    public void setCataloguingSatelliteLibraries(java.util.ArrayList cataloguingSatelliteLibraries) {
        this.cataloguingSatelliteLibraries = cataloguingSatelliteLibraries;
    }
    
    /** Getter for property acquisitionSatelliteLibraries.
     * @return Value of property acquisitionSatelliteLibraries.
     *
     */
    public java.util.ArrayList getAcquisitionSatelliteLibraries() {
        return acquisitionSatelliteLibraries;
    }
    
    /** Setter for property acquisitionSatelliteLibraries.
     * @param acquisitionSatelliteLibraries New value of property acquisitionSatelliteLibraries.
     *
     */
    public void setAcquisitionSatelliteLibraries(java.util.ArrayList acquisitionSatelliteLibraries) {
        this.acquisitionSatelliteLibraries = acquisitionSatelliteLibraries;
    }
    
    /** Getter for property serialManagementSatelliteLibraries.
     * @return Value of property serialManagementSatelliteLibraries.
     *
     */
    public java.util.ArrayList getSerialManagementSatelliteLibraries() {
        return serialManagementSatelliteLibraries;
    }
    
    /** Setter for property serialManagementSatelliteLibraries.
     * @param serialManagementSatelliteLibraries New value of property serialManagementSatelliteLibraries.
     *
     */
    public void setSerialManagementSatelliteLibraries(java.util.ArrayList serialManagementSatelliteLibraries) {
        this.serialManagementSatelliteLibraries = serialManagementSatelliteLibraries;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCSH;
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bHelp;
    private javax.swing.JButton bOk;
    private javax.swing.JComboBox cLibrary;
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lLibrary;
    private javax.swing.JLabel lPatronID;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem miSearchCatalogue;
    private javax.swing.JPanel pl;
    private javax.swing.JPanel pl1;
    private javax.swing.JPanel pl2;
    private javax.swing.JPanel plDesktop;
    private javax.swing.JPanel plLogin;
    private javax.swing.JWindow splash;
    private newgen.presentation.UnicodeTextField tPatronID;
    private javax.swing.JPasswordField tfPassword;
    // End of variables declaration//GEN-END:variables
    private static OPACMain applet=null;
    private java.util.Locale thisLocale = java.util.Locale.getDefault();
    private java.util.ResourceBundle MyResource = null;
    private java.util.ResourceBundle ComponentResource = null;
    private ContrastMetalTheme contrast;
    private AquaMetalTheme aqua;
    private GreenMetalTheme green;
    private KhakiMetalTheme khakhi;
    private HelpBroker helpbroker;
    private HelpSet helpset = null;
    private newgen.presentation.Privilege privilege;
    private java.util.Hashtable materials = new java.util.Hashtable();
    private java.util.Hashtable cataloguesatellitelibraries = new java.util.Hashtable();
    private java.util.Hashtable allLibraries = new java.util.Hashtable();
    private String[] patlib;
    private java.util.Hashtable marcMaterialTypes = new java.util.Hashtable();
    private String libraryID = "";
    private String patronID = "";
    
    private String acquisitionStatus="";
    private String serialManagementStatus="";
    private String cataloguingStatus="";
    private String acquisitionsMasterLibrary="";
    private String serialManagementMasterLibrary="";
    private String cataloguingMasterLibrary="";
    private java.util.ArrayList cataloguingSatelliteLibraries;
    private java.util.ArrayList acquisitionSatelliteLibraries;
    private java.util.ArrayList serialManagementSatelliteLibraries;
    
    private javax.swing.Timer timer;
    
}
