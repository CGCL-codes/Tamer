package de.beas.explicanto.client.rcp.views;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import de.bea.services.vidya.client.datastructures.TreeNode;
import de.beas.explicanto.client.ExplicantoClientPlugin;
import de.beas.explicanto.client.Resources;
import de.beas.explicanto.client.model.Node;
import de.beas.explicanto.client.rcp.editor.parts.DiagramEditPart;
import de.beas.explicanto.client.template.Document;
import de.beas.explicanto.client.template.TemplateResource;

/**
 * The main class for the informations view, based on a browser. It uses the 
 * informations.xsl file to transform transform the template.xml file and 
 * display the template informations. 
 * 
 * @author alexandru.georgescu
 * @version 1.0
 *  
 */
public class TemplateInfoView extends GenericView {

    public static String ID = "de.beas.explicanto.client.rcp.views.TemplateInfoView";

    private static final String TEMPLATE_XSL_RES = "informations.xsl";

    private Browser browser = null;

    private String defaultMessage = translate("templInfoView.noTemplateOpened");

    private Document template;

    private TransformerFactory tFactory;

    public void createPartControl(Composite parent) {
        setPartName(translate("templInfoView.title"));
        if (parent == null) return;
        browser = new Browser(parent, SWT.NONE);
        browser.setText(defaultMessage);
        tFactory = TransformerFactory.newInstance();
        getSite().getPage().addSelectionListener(this);
        getSite().getPage().addPartListener(this);
        if (getSite().getPage().getSelection() != null) selectionChanged(null, getSite().getPage().getSelection());
    }

    protected void diagramSelected(DiagramEditPart part) {
        showTemplate(part.getDiagram().getDocument().getTemplate());
    }

    protected void populateFor(TreeNode treeNode, Node node) {
        Document auxTemplate = node.getElement().getDocument();
        showTemplate(auxTemplate);
    }

    /**
     * Show the informations about the new template in the browser and 
     * sets is as the current template
     * 
     * If the new template is allready shown then it does nothing 
     *
     * @param newTmplate the new template
     */
    protected void showTemplate(Document newTmplate) {
        if (newTmplate == null) return;
        if (template == null) template = newTmplate; else {
            if (template.getId().equals(newTmplate.getId())) return;
            template = newTmplate;
        }
        try {
            DOMSource source = new DOMSource(template.getXmlDocument());
            OutputStream result = new ByteArrayOutputStream();
            TemplateResource infoXSL = template.getResource(TEMPLATE_XSL_RES);
            StreamSource styleSource;
            if (infoXSL != null) styleSource = new StreamSource(infoXSL.getContent()); else styleSource = new StreamSource(Resources.class.getResourceAsStream("rcp/resources/informations.xsl"));
            StreamResult res = new StreamResult(result);
            Transformer transf = tFactory.newTransformer(styleSource);
            transf.transform(source, res);
            browser.setText(result.toString());
        } catch (Exception e) {
            ExplicantoClientPlugin.handleException(e, null);
        }
    }

    protected void performNoEditorAction() {
        browser.setText(defaultMessage);
    }
}
