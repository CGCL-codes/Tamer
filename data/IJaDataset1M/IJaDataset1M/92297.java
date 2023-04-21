package org.xaware.ide.xadev.gui.dialog;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.http.HTTPAddress;
import javax.wsdl.extensions.http.HTTPOperation;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import org.apache.axis.message.SOAPEnvelope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import com.ibm.wsdl.BindingOperationImpl;
import com.ibm.wsdl.extensions.soap.SOAPBodyImpl;
import org.xaware.ide.shared.UserPrefs;
import org.xaware.ide.xadev.XA_Designer_Plugin;
import org.xaware.ide.xadev.common.ControlFactory;
import org.xaware.ide.xadev.gui.XAChooser;
import org.xaware.ide.xadev.gui.XAFileConstants;
import org.xaware.ide.xadev.gui.view.XAProjectExplorer;
import org.xaware.shared.i18n.Translator;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class creates a TestWSDL dialog and adds controls to the dialog.
 * 
 * @author Govind
 * @version 1.0
 */
public class WebServiceTesterDlg implements SelectionListener, ModifyListener {

    /** Logger for XAware. */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(WebServiceTesterDlg.class.getName());

    /** Reference to instance of XA_Designer_Plugin translator */
    public static final Translator translator = XA_Designer_Plugin.getTranslator();

    /** Reference to the namespace. */
    public static final Namespace ns = XAwareConstants.xaNamespace;

    /** Object of type TabFolder. */
    private TabFolder rootTabFld;

    /** Object of type TabItem. */
    private TabItem requestTabItm;

    /** Object of type TabItem. */
    private TabItem responseTabItm;

    /** Object of type TabItem. */
    private TabItem logTabItm;

    /** Object of type Label. */
    private Label wsdlLbl;

    /** Object of type Label. */
    private Label serviceLbl;

    /** Object of type Label. */
    private Label portLbl;

    /** Object of type Label. */
    private Label operationLbl;

    /** Object of type Label. */
    private Label urlLbl;

    /** Object of type Label. */
    private Label soapLbl;

    /** Object of type Label. */
    private Label requestLbl;

    /** Object of type Label. */
    private Label fillLbl;

    /** Object of type Text. */
    private Text wsdlTxt;

    /** Object of type Text. */
    private Text urlTxt;

    /** Object of type Text. */
    private Text soapTxt;

    /** Object of type Text. */
    private Text requestTxt;

    /** Object of type Combo. */
    private Combo serviceCmb;

    /** Object of type Combo. */
    private Combo portCmb;

    /** Object of type Combo. */
    private Combo operationCmb;

    /** Object of type Button. */
    private Button browseBtn;

    /** Object of type Button. */
    private Button executeBtn;

    /** Object of type Test. */
    private Text responseTxt;

    /** Object of type Text. */
    private Text logTxt;

    /** Object of type Shell. */
    private Shell rootShell;

    /** Object of type Definition. */
    private Definition theDef;

    /** Object of type Map. */
    private java.util.Map serviceMap;

    /** Object of type Map. */
    private java.util.Map portMap;

    /** Object of type List. */
    private java.util.List bindingOperations;

    /** Reference to the Element. */
    private Element root;

    /** Reference to the Element. */
    private Element operElem;

    /** Object of type Vector. */
    private Vector portCmbValues;

    /** Object of type Vector. */
    private Vector opeCmbValues;

    /** Object of type Vector. */
    private Vector serviceCmbValues;

    /** Holds old service index of Service combo. */
    private int oldServiceIndex;

    /** Holds old port index of Port combo. */
    private int oldPortIndex;

    /** Holds old Operation index of Operation combo. */
    private int oldOperationIndex;

    /**
     * Creates a new TestWSDLDlg object.
     * 
     * @param parentShell
     *            Object of type Shell.
     */
    public WebServiceTesterDlg(final Shell parentShell) {
        rootShell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL | SWT.RESIZE | SWT.BORDER);
        rootShell.setLayout(new GridLayout());
        rootShell.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        rootShell.setText("Test WSDL");
        rootShell.setSize(650, 550);
        rootShell.setImage(UserPrefs.getImageDescriptorIconFor(UserPrefs.XA_DESIGNER_DIALOG_IMAGE).createImage());
        XA_Designer_Plugin.alignDialogToCenter(rootShell);
    }

    /**
     * Fills the parent Composite with controls.
     * 
     * @param parentComp
     *            Object of type Composite.
     */
    public void initGUI(final Composite parentComp) {
        Composite rootComp;
        Composite requestComp;
        Composite responseComp;
        Composite logComp;
        final GridLayout glytRequest = new GridLayout();
        rootComp = new Composite(parentComp, SWT.NONE);
        rootComp.setLayout(new GridLayout());
        rootComp.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        rootTabFld = new TabFolder(rootComp, SWT.NONE);
        rootTabFld.setLayout(new GridLayout());
        rootTabFld.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        requestTabItm = new TabItem(rootTabFld, SWT.NONE);
        requestTabItm.setText("Request");
        requestComp = new Composite(rootTabFld, SWT.NONE);
        glytRequest.numColumns = 3;
        requestComp.setLayout(glytRequest);
        requestComp.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        wsdlLbl = new Label(requestComp, SWT.NONE);
        wsdlLbl.setText("WSDL:");
        wsdlLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        wsdlTxt = ControlFactory.createText(requestComp, SWT.BORDER);
        wsdlTxt.setToolTipText("Enter WSDL file/url.");
        wsdlTxt.addModifyListener(this);
        wsdlTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        browseBtn = ControlFactory.createButton(requestComp, "&Browse...");
        browseBtn.addSelectionListener(this);
        browseBtn.setToolTipText("Browse WSDL file.");
        serviceLbl = new Label(requestComp, SWT.NONE);
        serviceLbl.setText("Service:");
        serviceLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        serviceCmb = new Combo(requestComp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
        serviceCmb.setToolTipText("Select WSDL service.");
        serviceCmb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        fillLbl = new Label(requestComp, SWT.NONE);
        fillLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        portLbl = new Label(requestComp, SWT.NONE);
        portLbl.setText("Port:");
        portLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        portCmb = new Combo(requestComp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
        portCmb.setToolTipText("Select WSDL port.");
        portCmb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        fillLbl = new Label(requestComp, SWT.NONE);
        fillLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        operationLbl = new Label(requestComp, SWT.NONE);
        operationLbl.setText("Operation:");
        operationLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        operationCmb = new Combo(requestComp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
        operationCmb.setToolTipText("Select WSDL operation.");
        operationCmb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        fillLbl = new Label(requestComp, SWT.NONE);
        fillLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        urlLbl = new Label(requestComp, SWT.NONE);
        urlLbl.setText("URL:");
        urlLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        urlTxt = ControlFactory.createText(requestComp, SWT.BORDER);
        urlTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        fillLbl = new Label(requestComp, SWT.NONE);
        fillLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        soapLbl = new Label(requestComp, SWT.NONE);
        soapLbl.setText("SOAP Action:");
        soapLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        soapTxt = ControlFactory.createText(requestComp, SWT.BORDER);
        soapTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        fillLbl = new Label(requestComp, SWT.NONE);
        fillLbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        requestLbl = new Label(requestComp, SWT.NONE);
        requestLbl.setText("Request:");
        requestLbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        requestTxt = ControlFactory.createText(requestComp, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        requestTxt.setToolTipText("Edit request message or any parameters.");
        requestTxt.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        executeBtn = ControlFactory.createButton(requestComp, "&Execute", false, new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
        executeBtn.setToolTipText("Execute request.");
        executeBtn.addSelectionListener(this);
        serviceCmb.addSelectionListener(this);
        portCmb.addSelectionListener(this);
        operationCmb.addSelectionListener(this);
        requestTabItm.setControl(requestComp);
        responseTabItm = new TabItem(rootTabFld, SWT.NONE);
        responseTabItm.setText("Response");
        responseComp = new Composite(rootTabFld, SWT.NONE);
        responseComp.setLayout(new GridLayout());
        responseComp.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        responseTxt = new Text(responseComp, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        responseTxt.setEditable(false);
        responseTxt.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        responseTxt.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        responseTabItm.setControl(responseComp);
        logTabItm = new TabItem(rootTabFld, SWT.NONE);
        logTabItm.setText("Log");
        logComp = new Composite(rootTabFld, SWT.NONE);
        logComp.setLayout(new GridLayout());
        logComp.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        logTxt = new Text(logComp, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        logTxt.setEditable(false);
        logTxt.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        logTxt.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING));
        logTabItm.setControl(logComp);
    }

    /**
     * This method opens the shell.
     * 
     * @param fileName
     *            WSDL file name with path.
     */
    public void open(final String fileName) {
        try {
            final Display display = rootShell.getDisplay();
            initGUI(rootShell);
            rootShell.open();
            if ((fileName != null) && XAProjectExplorer.isWSDL(fileName)) {
                wsdlTxt.setText(fileName);
            }
            while (!rootShell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (final Exception e) {
            logger.severe("Exception while opening TestWSDL dialog: " + e.toString());
            logger.printStackTrace(e);
        }
    }

    /**
     * Method from SelectionListener. Closes the shell when clicked on OK or Cancel. Opens FileDialog, when clicked on
     * Browse.
     * 
     * @param se
     *            Object of type SelectionEvent.
     */
    public void widgetSelected(final SelectionEvent se) {
        if (se.getSource() == browseBtn) {
            String startFile = wsdlTxt.getText();
            if ((startFile == null) || (startFile.trim().length() == 0)) {
                final IPath path = XA_Designer_Plugin.getActiveEditorPath();
                if (path != null) {
                    startFile = path.toOSString();
                } else {
                    startFile = "";
                }
            }
            final XAChooser fileChooser = new XAChooser(rootShell, startFile, "Open", SWT.OPEN);
            fileChooser.addDefaultFilter(XAFileConstants.WSDL);
            fileChooser.addFilter(XAFileConstants.WSDL);
            final String path = fileChooser.open();
            if (path != null) {
                wsdlTxt.setText(path);
            } else {
                return;
            }
            try {
                populateFromWSDL();
            } catch (final Throwable ex) {
                final String message = "Invalid WSDL file. Exception: " + ex.getMessage();
                appendLog(message);
                ControlFactory.showInfoDialog(Translator.getInstance().getString("Invalid WSDL file."), message);
                clearFields();
            }
            if (browseBtn != null) {
                browseBtn.forceFocus();
            }
        } else if (se.getSource() == executeBtn) {
            try {
                appendLog("Sending request.");
                final URL url = new URL(this.urlTxt.getText().trim());
                final org.apache.axis.client.Service service = new org.apache.axis.client.Service();
                final org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
                call.setTargetEndpointAddress(url);
                call.setSOAPActionURI(soapTxt.getText().trim());
                final String str = requestTxt.getText().trim();
                final SOAPEnvelope soapenv = new SOAPEnvelope(new ByteArrayInputStream(str.getBytes("UTF-8")));
                final Object ret = call.invoke(soapenv);
                if (ret == null) {
                    appendLog("Received null reponse.");
                    throw new Exception("Received null response from server.");
                }
                if (ret instanceof String) {
                    appendLog("Received error response from server:" + ret);
                    throw new Exception("Received error response from server: " + ret);
                }
                final SOAPEnvelope env = call.getResponseMessage().getSOAPEnvelope();
                final SAXBuilder sb = new SAXBuilder();
                final String outputstr = call.getResponseMessage().getSOAPPartAsString();
                final Document doc = sb.build(new StringReader(outputstr));
                final Format pretty = Format.getPrettyFormat();
                pretty.setOmitDeclaration(true);
                pretty.setIndent("    ");
                final XMLOutputter out = new XMLOutputter(pretty);
                this.responseTxt.setText(out.outputString(doc));
                appendLog("Received response.");
                this.rootTabFld.setSelection(1);
            } catch (final Throwable ex) {
                final String message = "Error executing request. Exception: " + ex.toString();
                appendLog(message);
                ControlFactory.showInfoDialog(Translator.getInstance().getString("Error executing request."), message);
            }
        } else if (se.getSource() == serviceCmb) {
            if (oldServiceIndex == serviceCmb.getSelectionIndex()) {
                se.doit = false;
            } else {
                oldServiceIndex = serviceCmb.getSelectionIndex();
                portMap = ((Service) serviceCmbValues.get(serviceCmb.getSelectionIndex())).getPorts();
                addItemsToPortCombo(portMap);
                final Binding curBinding = ((Port) portCmbValues.get(portCmb.getSelectionIndex())).getBinding();
                bindingOperations = curBinding.getBindingOperations();
                addItemsToOperationCombo(curBinding);
                poulateStaticTextElements(false);
            }
        } else if (se.getSource() == portCmb) {
            if (oldPortIndex == portCmb.getSelectionIndex()) {
                se.doit = false;
            } else {
                oldPortIndex = portCmb.getSelectionIndex();
                final Port curPort = (Port) portCmbValues.get(portCmb.getSelectionIndex());
                final Binding curBinding = curPort.getBinding();
                bindingOperations = curBinding.getBindingOperations();
                addItemsToOperationCombo(curBinding);
                poulateStaticTextElements(false);
            }
        } else if (se.getSource() == operationCmb) {
            if (oldOperationIndex == operationCmb.getSelectionIndex()) {
                se.doit = false;
            } else {
                oldOperationIndex = operationCmb.getSelectionIndex();
                poulateStaticTextElements(false);
            }
        }
    }

    /**
     * Method from SelectionListener.
     * 
     * @param e
     *            Object of type SelectionEvent.
     */
    public void widgetDefaultSelected(final SelectionEvent e) {
    }

    /**
     * Method from ModifyListener.
     * 
     * @param me
     *            Object of type ModifyEvent.
     */
    public void modifyText(final ModifyEvent me) {
        if (me.getSource() == this.wsdlTxt) {
            try {
                populateFromWSDL();
            } catch (final Throwable ex) {
                clearFields();
            }
        }
    }

    /**
     * To pouplate WSDL data into the TestWSDL Dialog Controls.
     * 
     * @throws Exception
     */
    private void populateFromWSDL() throws Exception {
        try {
            final WSDLFactory fact = WSDLFactory.newInstance();
            final WSDLReader reader = fact.newWSDLReader();
            theDef = reader.readWSDL(this.wsdlTxt.getText());
            appendLog("Reading WSDL " + wsdlTxt.getText().trim());
            serviceMap = theDef.getServices();
            addItemsToServiceCombo(serviceMap);
            final Service curService = (Service) serviceCmbValues.get(serviceCmb.getSelectionIndex());
            portMap = curService.getPorts();
            addItemsToPortCombo(portMap);
            final Port curPort = (Port) portCmbValues.get(portCmb.getSelectionIndex());
            final Binding curBinding = curPort.getBinding();
            bindingOperations = curBinding.getBindingOperations();
            addItemsToOperationCombo(curBinding);
            poulateStaticTextElements(true);
        } catch (final Exception e) {
            throw e;
        }
    }

    /**
     * To Poulate the WSDL Data into TestWSDL Dialog Textboxes.
     * 
     * @param setServiceURL
     */
    private void poulateStaticTextElements(final boolean setServiceURL) {
        try {
            if (setServiceURL) {
                urlTxt.setText(getServiceAddress((Port) portCmbValues.get(portCmb.getSelectionIndex())));
            }
            soapTxt.setText(getSoapAction((BindingOperation) opeCmbValues.get(operationCmb.getSelectionIndex())));
            final Binding curBinding = ((Port) portCmbValues.get(portCmb.getSelectionIndex())).getBinding();
            final PortType type = curBinding.getPortType();
            final Operation oper = type.getOperation(((BindingOperation) opeCmbValues.get(operationCmb.getSelectionIndex())).getName(), null, null);
            final Namespace soapNamespace = Namespace.getNamespace("SOAPENV", "http://schemas.xmlsoap.org/soap/envelope/");
            root = new Element("Envelope", soapNamespace);
            final Element body = new Element("Body", soapNamespace);
            String uri = "";
            final java.util.List bindingOps = curBinding.getBindingOperations();
            for (int i = 0; i < bindingOps.size(); i++) {
                final BindingOperationImpl bindingObj = (BindingOperationImpl) bindingOps.get(i);
                if (bindingObj.getName().equals(oper.getName())) {
                    final java.util.List extElemList = bindingObj.getBindingInput().getExtensibilityElements();
                    for (int j = 0; j < extElemList.size(); j++) {
                        if (extElemList.get(j) instanceof SOAPBodyImpl) {
                            uri = ((SOAPBodyImpl) extElemList.get(j)).getNamespaceURI();
                            break;
                        }
                    }
                    break;
                }
            }
            operElem = new Element(oper.getName(), "", uri);
            root.addContent(body);
            body.addContent(operElem);
            try {
                final Iterator itr = oper.getInput().getMessage().getOrderedParts(null).iterator();
                final Vector inputParams = new Vector();
                while (itr.hasNext()) {
                    final Part curPart = (Part) itr.next();
                    final Element tmpElem = new Element(curPart.getName(), "");
                    tmpElem.setText(curPart.getTypeName().getLocalPart());
                    final org.w3c.dom.Element docElem = curPart.getDocumentationElement();
                    if (docElem != null) {
                        final org.w3c.dom.Attr attr = docElem.getAttributeNodeNS(ns.getURI(), "default");
                        if ((attr != null) && !attr.getValue().trim().equals("")) {
                            tmpElem.setText(attr.getValue().trim());
                        }
                    }
                    tmpElem.removeNamespaceDeclaration(Namespace.getNamespace(""));
                    operElem.addContent(tmpElem);
                }
                final Format pretty = Format.getPrettyFormat();
                pretty.setOmitDeclaration(true);
                final XMLOutputter outputter = new XMLOutputter(pretty);
                this.requestTxt.setText(outputter.outputString(root));
            } catch (final Exception e) {
                final String message = "Error processing request parameters for WSDL. Exception:" + e.toString();
                appendLog(message);
                ControlFactory.showInfoDialog(Translator.getInstance().getString("Error processing request parameters for WSDL."), message);
            }
        } catch (final Exception e) {
            final String message = "Error processing WSDL. Exception:" + e.toString();
            appendLog(message);
            ControlFactory.showInfoDialog(Translator.getInstance().getString("Error processing WSDL."), message);
        }
    }

    /**
     * To get the Service Address of give port
     * 
     * @param port
     *            instance of Port.
     * 
     * @return the ServiveAddress.
     */
    public String getServiceAddress(final Port port) {
        String retVal = "";
        try {
            final Iterator itr = port.getExtensibilityElements().iterator();
            if (itr.hasNext()) {
                final ExtensibilityElement tmp = (ExtensibilityElement) itr.next();
                final String displayStr = tmp.toString();
                if (tmp instanceof SOAPAddress) {
                    retVal = ((SOAPAddress) tmp).getLocationURI();
                } else if (tmp instanceof HTTPAddress) {
                    retVal = ((HTTPAddress) tmp).getLocationURI();
                } else if (tmp instanceof HTTPOperation) {
                    retVal = ((HTTPOperation) tmp).getLocationURI();
                } else {
                    retVal = displayStr;
                }
            }
        } catch (final Exception e) {
            final String message = "Error processing service URL. Exception: " + e.toString();
            appendLog(message);
            ControlFactory.showInfoDialog(Translator.getInstance().getString("Error processing service URL."), message);
        }
        return retVal;
    }

    /**
     * To get the SOAP Action for given BindingOperation.
     * 
     * @param bindingOperation
     *            instance of BindingOperation.
     * 
     * @return the bindingOperation.
     */
    public String getSoapAction(final BindingOperation bindingOperation) {
        String retVal = "";
        try {
            final Iterator itr1 = bindingOperation.getExtensibilityElements().iterator();
            if (itr1.hasNext()) {
                final ExtensibilityElement tmp = (ExtensibilityElement) itr1.next();
                final String displayStr = tmp.toString();
                if (tmp instanceof SOAPOperation) {
                    retVal = ((SOAPOperation) tmp).getSoapActionURI();
                } else {
                    retVal = displayStr;
                }
            }
        } catch (final Exception e) {
            final String message = "Error processing SOAP Action for WSDL. Exception:" + e.toString();
            appendLog(message);
            ControlFactory.showInfoDialog(Translator.getInstance().getString("Error processing SOAP Action for WSDL."), message);
        }
        return retVal;
    }

    /**
     * To add the log messages to the Log Text.
     * 
     * @param log
     *            instance of String.
     */
    public void appendLog(final String log) {
        logTxt.setText(logTxt.getText().trim() + "\r\n" + log.trim());
        logTxt.setSelection(0);
    }

    /**
     * To Clear the TestWSDL Dialog Controls.
     */
    private void clearFields() {
        serviceCmb.removeSelectionListener(this);
        portCmb.removeSelectionListener(this);
        operationCmb.removeSelectionListener(this);
        serviceCmb.removeAll();
        portCmb.removeAll();
        operationCmb.removeAll();
        serviceCmb.addSelectionListener(this);
        portCmb.addSelectionListener(this);
        operationCmb.addSelectionListener(this);
        urlTxt.setText("");
        soapTxt.setText("");
        requestTxt.setText("");
        responseTxt.setText("");
    }

    /**
     * To get the Servicename for Given Service Object.
     * 
     * @param value
     *            instance of Service Class.
     * 
     * @return the Servicename.
     */
    private String getServiceName(final Service value) {
        try {
            if ((value == null) || ((value).getQName() == null)) {
                return "";
            } else {
                return value.getQName().getLocalPart();
            }
        } catch (final Exception e) {
            appendLog("Exception caught: " + e);
        }
        return "Exception";
    }

    /**
     * To get the PortName for Given WSDL PortObject.
     * 
     * @param value
     *            instance of Port.
     * 
     * @return the portname.
     */
    private String getPortName(final Port value) {
        try {
            if ((value == null) || (value.getName() == null)) {
                return "";
            } else {
                return value.getName();
            }
        } catch (final Exception e) {
            appendLog("Exception caught: " + e);
        }
        return "Exception";
    }

    /**
     * To get the Operationame for Given WSDL BindingOperation Object.
     * 
     * @param value
     *            instance of BindingOperation.
     * 
     * @return the Operationame.
     */
    private String getOperationName(final BindingOperation value) {
        try {
            if ((value == null) || (value.getName() == null)) {
                return "";
            } else {
                return value.getName();
            }
        } catch (final Exception e) {
            appendLog("Exception caught: " + e);
        }
        return "Exception";
    }

    /**
     * To add the items to Port Combobox.
     * 
     * @param portMap
     *            instance of Map.
     */
    private void addItemsToPortCombo(final java.util.Map portMap) {
        try {
            portCmb.removeAll();
            portCmbValues = new Vector(portMap.values());
            for (int i = 0; i < portCmbValues.size(); i++) {
                portCmb.add(getPortName((Port) portCmbValues.get(i)), i);
            }
            if (portCmbValues.size() > 0) {
                portCmb.select(0);
            }
        } catch (final Exception exception) {
            logger.severe("Error occured in Adding the items to PortCombo: " + exception.getMessage());
            logger.printStackTrace(exception);
            appendLog("Error occured in Adding the items to PortCombo: ");
        }
    }

    /**
     * To add the items to the Servicecombobox.
     * 
     * @param serviceMap
     *            instance of Map.
     */
    private void addItemsToServiceCombo(final java.util.Map serviceMap) {
        try {
            serviceCmb.removeAll();
            serviceCmbValues = new Vector(serviceMap.values());
            for (int i = 0; i < serviceCmbValues.size(); i++) {
                serviceCmb.add(getServiceName((Service) serviceCmbValues.get(i)), i);
            }
            if (serviceMap.size() > 0) {
                serviceCmb.select(0);
            }
        } catch (final Exception exception) {
            logger.severe("Error occured in Adding the items to ServiceCombo: " + exception.getMessage());
            logger.printStackTrace(exception);
            appendLog("Error occured in Adding the items to ServiceCombo: ");
        }
    }

    /**
     * To add the items to the operation combobox.
     * 
     * @param curBinding
     *            instance of Binding.
     */
    private void addItemsToOperationCombo(final Binding curBinding) {
        try {
            operationCmb.removeAll();
            opeCmbValues = new Vector(curBinding.getBindingOperations());
            for (int i = 0; i < opeCmbValues.size(); i++) {
                this.operationCmb.add(getOperationName((BindingOperation) opeCmbValues.get(i)), i);
            }
            if (opeCmbValues.size() > 0) {
                operationCmb.select(0);
            }
        } catch (final Exception exception) {
            logger.severe("Error occured in Adding the items to OperationCombo: " + exception.getMessage());
            logger.printStackTrace(exception);
            appendLog("Error occured in Adding the items to OperationCombo: ");
        }
    }
}
