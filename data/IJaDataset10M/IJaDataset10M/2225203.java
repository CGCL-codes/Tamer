package org.itsnat.impl.comp;

import org.itsnat.impl.comp.text.ItsNatHTMLInputTextFormattedImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLInputFileImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLTextAreaImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLInputTextDefaultImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLInputHiddenImpl;
import org.itsnat.impl.comp.text.ItsNatHTMLInputPasswordImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLInputButtonImpl;
import org.itsnat.impl.comp.label.ItsNatHTMLLabelImpl;
import org.itsnat.impl.comp.table.ItsNatHTMLTableImpl;
import org.itsnat.impl.comp.button.toggle.ItsNatHTMLInputCheckBoxImpl;
import org.itsnat.impl.comp.button.toggle.ItsNatHTMLInputRadioImpl;
import org.itsnat.impl.comp.list.ItsNatHTMLSelectComboBoxImpl;
import org.itsnat.impl.comp.list.ItsNatHTMLSelectMultImpl;
import org.itsnat.impl.comp.layer.ItsNatHTMLModalLayerImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLInputImageImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLInputSubmitImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLInputResetImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLButtonLabelImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLAnchorLabelImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLButtonDefaultImpl;
import org.itsnat.impl.comp.button.normal.ItsNatHTMLAnchorDefaultImpl;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.itsnat.comp.ItsNatComponent;
import org.itsnat.comp.label.ItsNatLabelEditor;
import org.itsnat.comp.list.ItsNatListCellEditor;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.comp.table.ItsNatTableCellEditor;
import org.itsnat.comp.table.ItsNatTableStructure;
import org.itsnat.comp.tree.ItsNatTreeCellEditor;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchor;
import org.itsnat.comp.button.normal.ItsNatHTMLAnchorLabel;
import org.itsnat.comp.button.normal.ItsNatHTMLButton;
import org.itsnat.comp.button.normal.ItsNatHTMLButtonLabel;
import org.itsnat.comp.ItsNatHTMLComponentManager;
import org.itsnat.comp.ItsNatHTMLForm;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButton;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputCheckBox;
import org.itsnat.comp.text.ItsNatHTMLInputFile;
import org.itsnat.comp.text.ItsNatHTMLInputHidden;
import org.itsnat.comp.button.normal.ItsNatHTMLInputImage;
import org.itsnat.comp.text.ItsNatHTMLInputPassword;
import org.itsnat.comp.button.toggle.ItsNatHTMLInputRadio;
import org.itsnat.comp.button.normal.ItsNatHTMLInputReset;
import org.itsnat.comp.button.normal.ItsNatHTMLInputSubmit;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.comp.text.ItsNatHTMLInputTextFormatted;
import org.itsnat.comp.label.ItsNatHTMLLabel;
import org.itsnat.comp.list.ItsNatHTMLSelectComboBox;
import org.itsnat.comp.list.ItsNatHTMLSelectMult;
import org.itsnat.comp.table.ItsNatHTMLTable;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.NameValue;
import org.itsnat.impl.comp.factory.FactoryItsNatComponentImpl;
import org.itsnat.impl.comp.ItsNatAJAXComponentManagerImpl;
import org.itsnat.impl.comp.label.ItsNatHTMLLabelEditorDefaultImpl;
import org.itsnat.impl.comp.list.ItsNatHTMLListCellEditorDefaultImpl;
import org.itsnat.impl.comp.table.ItsNatHTMLTableCellEditorDefaultImpl;
import org.itsnat.impl.comp.tree.ItsNatHTMLTreeCellEditorDefaultImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLAnchorImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLButtonImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLComponentImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLFormImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputButtonImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatHTMLInputCheckBoxImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputFileImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputHiddenImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputImageImpl;
import org.itsnat.impl.comp.factory.FactoryItsNatHTMLInputImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputPasswordImpl;
import org.itsnat.impl.comp.factory.button.toggle.FactoryItsNatHTMLInputRadioImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputResetImpl;
import org.itsnat.impl.comp.factory.button.normal.FactoryItsNatHTMLInputSubmitImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLInputTextImpl;
import org.itsnat.impl.comp.factory.label.FactoryItsNatHTMLLabelImpl;
import org.itsnat.impl.comp.factory.list.FactoryItsNatHTMLSelectImpl;
import org.itsnat.impl.comp.factory.table.FactoryItsNatHTMLTableImpl;
import org.itsnat.impl.comp.factory.text.FactoryItsNatHTMLTextAreaImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentInvitedRemoteCtrlImpl;
import org.itsnat.impl.core.doc.ItsNatAJAXDocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLButtonElement;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLFormElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLabelElement;
import org.w3c.dom.html.HTMLSelectElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTextAreaElement;

/**
 * Leer notas de HTMLElementGroupManagerImpl sobre la dependencia
 * con ItsNatHTMLDocument
 *
 * @author jmarranz
 */
public class ItsNatHTMLComponentManagerImpl extends ItsNatAJAXComponentManagerImpl implements ItsNatHTMLComponentManager {

    protected static final Map HTMLFACTORIES = new HashMap();

    protected LinkedList modalLayers;

    static {
        addHTMLFactory(new FactoryItsNatHTMLAnchorImpl());
        addHTMLFactory(new FactoryItsNatHTMLButtonImpl());
        addHTMLFactory(new FactoryItsNatHTMLFormImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputButtonImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputCheckBoxImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputFileImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputHiddenImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputImageImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputPasswordImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputRadioImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputResetImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputSubmitImpl());
        addHTMLFactory(new FactoryItsNatHTMLInputTextImpl());
        addHTMLFactory(new FactoryItsNatHTMLLabelImpl());
        addHTMLFactory(new FactoryItsNatHTMLSelectImpl());
        addHTMLFactory(new FactoryItsNatHTMLTableImpl());
        addHTMLFactory(new FactoryItsNatHTMLTextAreaImpl());
    }

    /** Creates a new instance of ItsNatHTMLComponentManagerImpl */
    public ItsNatHTMLComponentManagerImpl(ItsNatAJAXDocumentImpl itsNatDoc) {
        super(itsNatDoc);
    }

    protected static void addHTMLFactory(FactoryItsNatHTMLComponentImpl factory) {
        HTMLFACTORIES.put(factory.getKey(), factory);
    }

    protected static FactoryItsNatHTMLComponentImpl getHTMLFactory(HTMLElement elem) {
        String key;
        if (elem instanceof HTMLInputElement) key = FactoryItsNatHTMLInputImpl.getKey((HTMLInputElement) elem); else key = FactoryItsNatHTMLComponentImpl.getKey(elem);
        return (FactoryItsNatHTMLComponentImpl) HTMLFACTORIES.get(key);
    }

    public boolean hasItsNatHTMLModalLayers() {
        if (modalLayers == null) return false;
        return !modalLayers.isEmpty();
    }

    public LinkedList getItsNatHTMLModalLayers() {
        if (modalLayers == null) this.modalLayers = new LinkedList();
        return modalLayers;
    }

    public Element getElementById(String id) {
        Document doc = getItsNatDocumentImpl().getDocument();
        return doc.getElementById(id);
    }

    public ItsNatHTMLFormImpl getItsNatHTMLForm(HTMLFormElement formElem) {
        ItsNatHTMLFormImpl form = (ItsNatHTMLFormImpl) findItsNatComponent(formElem);
        return form;
    }

    public ItsNatComponent newItsNatComponent(Element element, String compType, NameValue[] artifacts, boolean ignoreIsComponentAttr) {
        ItsNatComponent comp = super.newItsNatComponent(element, compType, artifacts, ignoreIsComponentAttr);
        if (comp != null) return comp;
        if (!(element instanceof HTMLElement)) return null;
        FactoryItsNatComponentImpl factory = getHTMLFactory((HTMLElement) element);
        if (factory != null) return factory.createItsNatComponent(element, compType, artifacts, ignoreIsComponentAttr, this);
        return null;
    }

    public static boolean declaredAsHTMLComponent(Element element) {
        boolean decAsComp = declaredAsComponent(element);
        if (decAsComp) return true;
        if (!(element instanceof HTMLElement)) return false;
        FactoryItsNatHTMLComponentImpl factory = getHTMLFactory((HTMLElement) element);
        if (factory == null) return false;
        return factory.declaredAsHTMLComponent(element);
    }

    public ItsNatHTMLLabel createItsNatHTMLLabel(HTMLLabelElement element, NameValue[] artifacts) {
        return new ItsNatHTMLLabelImpl(element, artifacts, this);
    }

    public ItsNatHTMLAnchor createItsNatHTMLAnchor(HTMLAnchorElement element, NameValue[] artifacts) {
        return new ItsNatHTMLAnchorDefaultImpl(element, artifacts, this);
    }

    public ItsNatHTMLAnchorLabel createItsNatHTMLAnchorLabel(HTMLAnchorElement element, NameValue[] artifacts) {
        return new ItsNatHTMLAnchorLabelImpl(element, artifacts, this);
    }

    public ItsNatHTMLForm createItsNatHTMLForm(HTMLFormElement element, NameValue[] artifacts) {
        return new ItsNatHTMLFormImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputText createItsNatHTMLInputText(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputTextDefaultImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputTextFormatted createItsNatHTMLInputTextFormatted(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputTextFormattedImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputPassword createItsNatHTMLInputPassword(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputPasswordImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputCheckBox createItsNatHTMLInputCheckBox(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputCheckBoxImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputRadio createItsNatHTMLInputRadio(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputRadioImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputSubmit createItsNatHTMLInputSubmit(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputSubmitImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputReset createItsNatHTMLInputReset(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputResetImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputButton createItsNatHTMLInputButton(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputButtonImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputImage createItsNatHTMLInputImage(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputImageImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputHidden createItsNatHTMLInputHidden(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputHiddenImpl(element, artifacts, this);
    }

    public ItsNatHTMLInputFile createItsNatHTMLInputFile(HTMLInputElement element, NameValue[] artifacts) {
        return new ItsNatHTMLInputFileImpl(element, artifacts, this);
    }

    public ItsNatHTMLSelectMult createItsNatHTMLSelectMult(HTMLSelectElement element, NameValue[] artifacts) {
        return new ItsNatHTMLSelectMultImpl(element, artifacts, this);
    }

    public ItsNatHTMLSelectComboBox createItsNatHTMLSelectComboBox(HTMLSelectElement element, NameValue[] artifacts) {
        return new ItsNatHTMLSelectComboBoxImpl(element, artifacts, this);
    }

    public ItsNatHTMLTextArea createItsNatHTMLTextArea(HTMLTextAreaElement element, NameValue[] artifacts) {
        return new ItsNatHTMLTextAreaImpl(element, artifacts, this);
    }

    public ItsNatHTMLButton createItsNatHTMLButton(HTMLButtonElement element, NameValue[] artifacts) {
        return new ItsNatHTMLButtonDefaultImpl(element, artifacts, this);
    }

    public ItsNatHTMLButtonLabel createItsNatHTMLButtonLabel(HTMLButtonElement element, NameValue[] artifacts) {
        return new ItsNatHTMLButtonLabelImpl(element, artifacts, this);
    }

    public ItsNatHTMLTable createItsNatHTMLTable(HTMLTableElement element, ItsNatTableStructure structure, NameValue[] artifacts) {
        return new ItsNatHTMLTableImpl(element, structure, artifacts, this);
    }

    public ItsNatHTMLForm addItsNatHTMLForm(HTMLFormElement element, NameValue[] artifacts) {
        ItsNatHTMLForm component = createItsNatHTMLForm(element, artifacts);
        addItsNatComponent(component);
        return component;
    }

    public ItsNatLabelEditor createDefaultItsNatLabelEditor(ItsNatComponent compEditor) {
        return new ItsNatHTMLLabelEditorDefaultImpl(compEditor, this);
    }

    public ItsNatListCellEditor createDefaultItsNatListCellEditor(ItsNatComponent compEditor) {
        return new ItsNatHTMLListCellEditorDefaultImpl(compEditor, this);
    }

    public ItsNatTableCellEditor createDefaultItsNatTableCellEditor(ItsNatComponent compEditor) {
        return new ItsNatHTMLTableCellEditorDefaultImpl(compEditor, this);
    }

    public ItsNatTreeCellEditor createDefaultItsNatTreeCellEditor(ItsNatComponent compEditor) {
        return new ItsNatHTMLTreeCellEditorDefaultImpl(compEditor, this);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element, int zIndex, float opacity, String background, NameValue[] artifacts) {
        return new ItsNatHTMLModalLayerImpl((HTMLElement) element, zIndex, opacity, background, artifacts, this);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element, float opacity, String background, NameValue[] artifacts) {
        return new ItsNatHTMLModalLayerImpl((HTMLElement) element, opacity, background, artifacts, this);
    }

    public ItsNatModalLayer createItsNatModalLayer(Element element, NameValue[] artifacts) {
        return new ItsNatHTMLModalLayerImpl((HTMLElement) element, artifacts, this);
    }

    public void addClientDocumentInvitedRemoteCtrl(ClientDocumentInvitedRemoteCtrlImpl clientDoc) {
        if (hasItsNatHTMLModalLayers()) {
            LinkedList modalLayers = getItsNatHTMLModalLayers();
            for (Iterator it = modalLayers.iterator(); it.hasNext(); ) {
                ItsNatHTMLModalLayerImpl comp = (ItsNatHTMLModalLayerImpl) it.next();
                comp.addClientDocumentInvitedRemoteCtrl(clientDoc);
            }
        }
    }

    public void removeClientDocumentInvitedRemoteCtrl(ClientDocumentInvitedRemoteCtrlImpl clientDoc) {
        if (hasItsNatHTMLModalLayers()) {
            LinkedList modalLayers = getItsNatHTMLModalLayers();
            for (Iterator it = modalLayers.iterator(); it.hasNext(); ) {
                ItsNatHTMLModalLayerImpl comp = (ItsNatHTMLModalLayerImpl) it.next();
                comp.removeClientDocumentInvitedRemoteCtrl(clientDoc);
            }
        }
    }
}
