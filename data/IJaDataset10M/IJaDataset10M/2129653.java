package at.suas.sepiaxweb.client.composite;

import at.suas.sepiaxweb.client.Entry;
import at.suas.sepiaxweb.client.EntryReceiver;
import at.suas.sepiaxweb.client.EntryReceiverFactory;
import at.suas.sepiaxweb.client.SbvrExpression;
import at.suas.sepiaxweb.client.ServentSettings;
import at.suas.sepiaxweb.client.ServiceFactory;
import at.suas.sepiaxweb.client.ServiceFactory.ServiceFactoryException;
import at.suas.sepiaxweb.client.dialog.ErrorDialog;
import at.suas.sepiaxweb.client.dialog.StandardDialog;
import at.suas.sepiaxweb.client.service.GrailsServiceAsync;
import at.suas.sepiaxweb.client.service.XPDLServiceAsync;
import at.suas.sepiaxweb.client.service.bonita.BonitaServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SbvrXpdlComposite extends Composite implements ClickListener {

    XPDLServiceAsync xpdlService = null;

    BonitaServiceAsync bonitaService = null;

    TextArea sbvrVocabularyArea;

    TextArea sbvrRuleArea;

    TextArea xpdlOutputArea;

    TextBox processName;

    TextBox processVersion;

    Button deployProcess;

    public class SbvrXpdlCompositeEntryReceiver implements EntryReceiver {

        TextArea target;

        public SbvrXpdlCompositeEntryReceiver(TextArea target) {
            this.target = target;
        }

        public void receiveEntry(Entry entry) {
            target.setText(entry.getPlainSbvr(false));
        }
    }

    public class SbvrXpdlCompositeEntryReceiver_SbvrTypeAware implements EntryReceiver {

        TextArea vocabulary, rules;

        public SbvrXpdlCompositeEntryReceiver_SbvrTypeAware(TextArea vocabulary, TextArea rules) {
            this.vocabulary = vocabulary;
            this.rules = rules;
        }

        public void receiveEntry(Entry entry) {
            ArrayList exp = entry.getSbvrExpression();
            StringBuilder voc = new StringBuilder();
            StringBuilder rul = new StringBuilder();
            Iterator iter = exp.iterator();
            while (iter.hasNext()) {
                SbvrExpression expression = (SbvrExpression) iter.next();
                if (expression.getSbvrType() == expression.SBVR_VOCABULARY) {
                    voc = voc.append(expression.getPlainSbvr(false)).append('\n');
                } else if (expression.getSbvrType() == expression.SBVR_RULE) {
                    rul = rul.append(expression.getPlainSbvr(false)).append('\n');
                }
            }
            vocabulary.setText(voc.toString());
            rules.setText(rul.toString());
        }
    }

    private XPDLServiceAsync getXpdlService() {
        if (xpdlService == null) {
            try {
                xpdlService = ServiceFactory.getInstance().createXpdlService();
            } catch (ServiceFactoryException e) {
                e.printStackTrace();
            }
        }
        return xpdlService;
    }

    private BonitaServiceAsync getBonitaService() {
        if (bonitaService == null) {
            try {
                bonitaService = ServiceFactory.getInstance().createBonitaAdapter();
            } catch (ServiceFactoryException e) {
                e.printStackTrace();
            }
        }
        return bonitaService;
    }

    public SbvrXpdlComposite() {
        getBonitaService();
        processName = new TextBox();
        processVersion = new TextBox();
        TabPanel mainPanel = new TabPanel();
        mainPanel.add(createVocAndRulePanel(), "SBVR Input");
        mainPanel.add(createXpdlOutputPanel(), "XPDL Output");
        mainPanel.selectTab(0);
        try {
            ServiceFactory factory = ServiceFactory.getInstance();
        } catch (ServiceFactory.ServiceFactoryException ex) {
        }
        initWidget(mainPanel);
        EntryReceiverFactory.instance().addEntryReceiver("XPDL", new SbvrXpdlCompositeEntryReceiver_SbvrTypeAware(sbvrVocabularyArea, sbvrRuleArea));
    }

    protected Panel createVocAndRulePanel() {
        VerticalPanel p = new VerticalPanel();
        HorizontalPanel processNamePanel = new HorizontalPanel();
        processNamePanel.add(new Label("Process Name"));
        processNamePanel.getWidget(0).setWidth("200px");
        processNamePanel.add(processName);
        HorizontalPanel processVersionPanel = new HorizontalPanel();
        processVersionPanel.add(new Label("Process Version"));
        processVersionPanel.getWidget(0).setWidth("200px");
        processVersionPanel.add(processVersion);
        p.add(processNamePanel);
        p.add(processVersionPanel);
        p.add(new Label("Vocabulary"));
        sbvrVocabularyArea = new TextArea();
        sbvrVocabularyArea.setCharacterWidth(80);
        sbvrVocabularyArea.setVisibleLines(10);
        p.add(sbvrVocabularyArea);
        p.add(new Label("Rules"));
        sbvrRuleArea = new TextArea();
        sbvrRuleArea.setCharacterWidth(80);
        sbvrRuleArea.setVisibleLines(10);
        p.add(sbvrRuleArea);
        HorizontalPanel buttonPane = new HorizontalPanel();
        Button b2 = new Button("Generate XPDL");
        b2.addClickListener(this);
        buttonPane.add(b2);
        p.add(buttonPane);
        return p;
    }

    protected Panel createXpdlOutputPanel() {
        VerticalPanel p = new VerticalPanel();
        xpdlOutputArea = new TextArea();
        xpdlOutputArea.setCharacterWidth(80);
        xpdlOutputArea.setVisibleLines(15);
        deployProcess = new Button("Deploy Process");
        deployProcess.addClickListener(this);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.add(deployProcess);
        p.add(buttonPanel);
        p.add(xpdlOutputArea);
        return p;
    }

    public void onClick(Widget arg0) {
        if (arg0 instanceof Button) {
            Button sender = (Button) arg0;
            if ("Generate XPDL".equals(sender.getText())) {
                getXpdlService().tranformSbvr2Xpdl(ServentSettings.getInstance(), sbvrVocabularyArea.getText(), sbvrRuleArea.getText(), processName.getText(), processVersion.getText(), new AsyncCallback() {

                    public void onFailure(Throwable caught) {
                        new ErrorDialog("An error occured while invoking transform service.", caught).show();
                    }

                    public void onSuccess(Object result) {
                        xpdlOutputArea.setText((String) result);
                    }
                });
            } else if ("Rules Validate Syntax".equals(sender.getText())) {
            } else if ("Vocabulary Validate Syntax".equals(sender.getText())) {
            } else if ("Deploy Process".equals(sender.getText())) {
                getBonitaService().deploy(processName.getText(), processVersion.getText(), xpdlOutputArea.getText(), new AsyncCallback() {

                    public void onFailure(Throwable caught) {
                        new ErrorDialog("Error while deploying process.", caught).show();
                    }

                    public void onSuccess(Object result) {
                        new StandardDialog(processName.getText() + " successfully deployed.").show();
                    }
                });
            }
        }
    }
}
