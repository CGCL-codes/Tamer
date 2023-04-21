package org.jabusuite.webclient.address.letter;

import java.util.Calendar;
import javax.naming.NamingException;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.ContentPane;
import org.jabusuite.webclient.controls.JbsExtent;
import org.jabusuite.webclient.controls.container.JbsGrid;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import org.jabusuite.address.Address;
import org.jabusuite.address.Contact;
import org.jabusuite.address.letter.Letter;
import org.jabusuite.address.letter.session.LettersRemote;
import org.jabusuite.client.utils.ClientTools;
import org.jabusuite.core.reporting.ReportTemplate;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.webclient.address.contact.JbsContactSelectField;
import org.jabusuite.webclient.controls.JbsLabel;
import org.jabusuite.webclient.controls.JbsTextField;
import org.jabusuite.webclient.controls.date.JbsDateField;
import org.jabusuite.webclient.controls.richtext.JbsRichTextArea;
import org.jabusuite.webclient.controls.toolpane.JbsObjectToolPane;
import org.jabusuite.webclient.controls.toolpane.JbsObjectToolPaneButton;
import org.jabusuite.webclient.dataediting.DlgState;
import org.jabusuite.webclient.dataediting.PnEditJbsObject;
import org.jabusuite.webclient.datalist.PnListModule;
import org.jabusuite.webclient.main.ClientGlobals;
import org.jabusuite.webclient.main.JbsL10N;
import org.jabusuite.webclient.modules.PnModule;
import org.jabusuite.webclient.windows.JbsDialogWindowOKCancel;
import org.jabusuite.webclient.windows.JbsOptionPane;

/**
 *
 * @author hilwers
 * @date 2008-09-27
 */
public class PnLetterEdit extends PnEditJbsObject {

    private static final long serialVersionUID = 2441578173201248924L;

    protected JbsTextField txLetterSubject;

    protected JbsDateField dfDate;

    protected JbsTextField txLetterSalutation;

    protected JbsRichTextArea txLetterText;

    protected SplitPane spMain;

    protected JbsObjectToolPane toolPane;

    protected JbsObjectToolPaneButton btnPrint;

    protected JbsTextField txYourReference;

    protected JbsContactSelectField selContact;

    protected JbsLabel lblContact;

    private Address address;

    private boolean saveDirectly;

    private boolean showContactField;

    public PnLetterEdit() {
        super(DlgState.dsInsert);
        this.setAddressLetter(null);
    }

    public PnLetterEdit(DlgState state, Letter addressLetter) {
        super(state);
        this.setAddressLetter(addressLetter);
    }

    public Letter getAddressLetter() {
        return (Letter) this.getJbsBaseObject();
    }

    public void setAddressLetter(Letter addressLetter) {
        this.setJbsBaseObject(addressLetter);
    }

    @Override
    protected void createComponents() {
        this.setSaveDirectly(true);
        this.dfDate = new JbsDateField();
        this.txYourReference = new JbsTextField();
        this.txLetterSubject = new JbsTextField();
        this.txLetterSubject.setWidth(new JbsExtent(95, JbsExtent.PERCENT));
        this.txLetterSalutation = new JbsTextField();
        this.txLetterSalutation.setWidth(new JbsExtent(95, JbsExtent.PERCENT));
        this.txLetterText = new JbsRichTextArea();
        this.txLetterText.setWidth(new JbsExtent(95, JbsExtent.PERCENT));
        this.txLetterText.setHeight(new JbsExtent(400, JbsExtent.PX));
        this.lblContact = new JbsLabel();
        this.selContact = new JbsContactSelectField();
        this.setBtnPrint(new JbsObjectToolPaneButton("print.png", JbsL10N.getString("Generic.print"), false));
        this.getBtnPrint().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                printLetter();
            }
        });
        this.setSpMain(new SplitPane());
        this.getSpMain().setOrientation(SplitPane.ORIENTATION_HORIZONTAL_RIGHT_LEFT);
        this.getSpMain().setSeparatorPosition(new JbsExtent(PnModule.STD_TOOLPANEWIDTH, JbsExtent.PX));
        this.getSpMain().setSeparatorWidth(new JbsExtent(1, JbsExtent.PX));
        this.setToolPane(new JbsObjectToolPane());
        this.setShowContactField(true);
    }

    protected void fillToolpane() {
        if (this.getToolPane() != null) {
            this.getToolPane().addControl(JbsL10N.getString(PnListModule.TOOLPANEGROUP_ACTIONS), this.getBtnPrint());
        }
    }

    @Override
    protected void initPanel() {
        this.fillToolpane();
        this.getSpMain().add(this.getToolPane());
        ContentPane cpMain = new ContentPane();
        cpMain.setInsets(ClientGlobals.getStandardInsets());
        Column colMain = new Column();
        JbsGrid grdMain = new JbsGrid(2);
        grdMain.setColumnWidth(0, new JbsExtent(150));
        lblContact.setText(JbsL10N.getString("AddressLetter.contact"));
        grdMain.add(lblContact);
        grdMain.add(this.selContact);
        grdMain.add(new JbsLabel(JbsL10N.getString("AddressLetter.letterDate")));
        grdMain.add(this.dfDate);
        grdMain.add(new JbsLabel(JbsL10N.getString("AddressLetter.yourReference")));
        grdMain.add(this.txYourReference);
        colMain.add(grdMain);
        colMain.add(new JbsLabel(JbsL10N.getString("AddressLetter.letterSubject")));
        colMain.add(this.txLetterSubject);
        colMain.add(new JbsLabel(JbsL10N.getString("AddressLetter.letterSalutation")));
        colMain.add(this.txLetterSalutation);
        colMain.add(new JbsLabel(JbsL10N.getString("AddressLetter.letterText")));
        colMain.add(this.txLetterText);
        cpMain.add(colMain);
        this.getSpMain().add(cpMain);
        this.add(this.getSpMain());
    }

    @Override
    protected void setControlData() {
        if (this.getAddressLetter() != null) {
            if (this.isShowContactField()) this.selContact.setSelectedContact(this.getAddressLetter().getContact());
            this.txLetterSubject.setText(this.getAddressLetter().getLetterSubject());
            if (this.getDlgState() == DlgState.dsInsert) {
                this.dfDate.setSelectedDate(Calendar.getInstance());
                if (this.getAddress() != null) {
                    this.txLetterSalutation.setText(this.getAddress().getLetterSalutation());
                    this.txYourReference.setText(this.getAddress().getAbbreviation());
                }
            } else {
                this.dfDate.setSelectedDate(this.getAddressLetter().getLetterDate());
                this.txLetterSalutation.setText(this.getAddressLetter().getLetterSalutation());
                this.txYourReference.setText(this.getAddressLetter().getYourReference());
            }
            this.txLetterText.setText(this.getAddressLetter().getLetterText());
        }
    }

    @Override
    protected void getControlData() {
        if (this.getAddressLetter() != null) {
            if (this.isShowContactField()) this.getAddressLetter().setContact(this.selContact.getSelectedContact());
            this.getAddressLetter().setLetterSubject(this.txLetterSubject.getText());
            this.getAddressLetter().setLetterDate(this.dfDate.getSelectedDate());
            this.getAddressLetter().setLetterSalutation(this.txLetterSalutation.getText());
            this.getAddressLetter().setLetterText(this.txLetterText.getText());
            this.getAddressLetter().setYourReference(this.txYourReference.getText());
        }
    }

    protected void printLetter() {
        final FmLetterReportOptions fmReportOptions = new FmLetterReportOptions();
        fmReportOptions.setContact(this.getLetter().getContact());
        fmReportOptions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (arg0.getActionCommand().equals(JbsDialogWindowOKCancel.ACTION_OK)) {
                    printLetter(fmReportOptions.getSelectedPageTemplate(), fmReportOptions.getSelectedReportTemplate(), fmReportOptions.getContact());
                }
            }
        });
        fmReportOptions.showForm();
    }

    protected void printLetter(ReportTemplate pageTemplate, ReportTemplate reportTemplate, Contact contact) {
        try {
            RptLetter rptLetter = new RptLetter(pageTemplate, reportTemplate, this.getAddressLetter(), ClientGlobals.getUser(), ClientGlobals.getCompany());
            rptLetter.setContact(contact);
            rptLetter.createReport();
            getApplicationInstance().enqueueCommand(rptLetter.getReportPdfDownload(true));
        } catch (Exception e) {
            JbsOptionPane.showErrorDialog(this, JbsL10N.getString(JbsL10N.getString("Dunning.printError")), e);
        }
    }

    @Override
    public void doSave() throws EJbsObject {
        if (!this.isSaveDirectly()) super.doSave(); else {
            try {
                LettersRemote letters = (LettersRemote) ClientTools.getRemoteBean(LettersRemote.class);
                super.doSave();
                if (this.getDlgState() == DlgState.dsInsert) {
                    System.out.println("Adding new entity");
                    letters.createDataset(this.getLetter(), ClientGlobals.getUser(), ClientGlobals.getCompany());
                } else if (this.getDlgState() == DlgState.dsEdit) {
                    System.out.println("Saving exisiting entity.");
                    letters.updateDataset(this.getLetter(), ClientGlobals.getUser());
                }
                System.out.println("Entity saved.");
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
    }

    public Letter getLetter() {
        if ((this.getJbsBaseObject() != null) && (this.getJbsBaseObject() instanceof Letter)) return (Letter) this.getJbsBaseObject(); else return null;
    }

    public void setLetter(Letter letter) {
        this.setJbsBaseObject(letter);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
        this.selContact.setSelectedContact(address);
    }

    public SplitPane getSpMain() {
        return spMain;
    }

    public void setSpMain(SplitPane spMain) {
        this.spMain = spMain;
    }

    public JbsObjectToolPane getToolPane() {
        return toolPane;
    }

    public void setToolPane(JbsObjectToolPane toolPane) {
        this.toolPane = toolPane;
    }

    public JbsObjectToolPaneButton getBtnPrint() {
        return btnPrint;
    }

    public void setBtnPrint(JbsObjectToolPaneButton btnPrint) {
        this.btnPrint = btnPrint;
    }

    public boolean isSaveDirectly() {
        return saveDirectly;
    }

    public void setSaveDirectly(boolean saveDirectly) {
        this.saveDirectly = saveDirectly;
    }

    public boolean isShowContactField() {
        return showContactField;
    }

    public void setShowContactField(boolean showContactField) {
        this.showContactField = showContactField;
        this.lblContact.setVisible(this.showContactField);
        this.selContact.setVisible(this.showContactField);
    }
}
