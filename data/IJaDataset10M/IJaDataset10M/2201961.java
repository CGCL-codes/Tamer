package org.itsnat.feashow.features.comp.autobuild.mdforms;

import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.core.ItsNatDocument;
import org.itsnat.feashow.FeatureTreeNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLOptionElement;
import org.w3c.dom.html.HTMLSelectElement;

public class MarkupDrivenComboBoxTreeNode extends FeatureTreeNode implements EventListener {

    protected Element parentElem;

    protected HTMLSelectElement selectElem;

    protected Element removeElem;

    protected HTMLInputElement itemElem;

    protected HTMLInputElement posElem;

    protected Element insertElem;

    public MarkupDrivenComboBoxTreeNode() {
    }

    public void startExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        Document doc = itsNatDoc.getDocument();
        this.parentElem = doc.getElementById("compGroupId");
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.setMarkupDrivenComponents(true);
        compMgr.buildItsNatComponents(parentElem);
        this.selectElem = (HTMLSelectElement) doc.getElementById("selectId");
        itsNatDoc.addEventListener((EventTarget) selectElem, "change", this, false);
        this.removeElem = doc.getElementById("removeId");
        itsNatDoc.addEventListener((EventTarget) removeElem, "click", this, false);
        this.itemElem = (HTMLInputElement) doc.getElementById("itemId");
        this.posElem = (HTMLInputElement) doc.getElementById("posId");
        updateItemForm();
        this.insertElem = doc.getElementById("insertId");
        itsNatDoc.addEventListener((EventTarget) insertElem, "click", this, false);
    }

    public void endExamplePanel() {
        ItsNatDocument itsNatDoc = getItsNatDocument();
        itsNatDoc.removeEventListener((EventTarget) selectElem, "change", this, false);
        this.selectElem = null;
        itsNatDoc.removeEventListener((EventTarget) removeElem, "click", this, false);
        this.removeElem = null;
        this.itemElem = null;
        this.posElem = null;
        itsNatDoc.removeEventListener((EventTarget) insertElem, "click", this, false);
        this.insertElem = null;
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        compMgr.removeItsNatComponents(parentElem, true);
        compMgr.setMarkupDrivenComponents(false);
        this.parentElem = null;
    }

    public HTMLOptionElement getOptionElement(int index) {
        if (index < 0) return null;
        return (HTMLOptionElement) selectElem.getOptions().item(index);
    }

    public String getOptionText(int index) {
        HTMLOptionElement option = getOptionElement(index);
        if (option == null) return null;
        return option.getText();
    }

    public int indexOf(String item) {
        HTMLCollection col = selectElem.getOptions();
        int len = col.getLength();
        for (int i = 0; i < len; i++) {
            HTMLOptionElement option = (HTMLOptionElement) col.item(i);
            if (option.getText().equals(item)) return i;
        }
        return -1;
    }

    public void updateItemForm() {
        int selectedIndex = selectElem.getSelectedIndex();
        itemElem.setValue(getOptionText(selectedIndex));
        posElem.setValue(Integer.toString(selectedIndex));
    }

    public void handleEvent(Event evt) {
        log(evt.getCurrentTarget() + " " + evt.getType());
        EventTarget currentTarget = evt.getCurrentTarget();
        if (currentTarget == selectElem) {
            updateItemForm();
        } else if (currentTarget == removeElem) {
            int index = selectElem.getSelectedIndex();
            if (index >= 0) {
                selectElem.remove(index);
                updateItemForm();
            }
        } else if (currentTarget == insertElem) {
            String newItem = itemElem.getValue();
            if (indexOf(newItem) != -1) getItsNatDocument().addCodeToSend("alert('Duplicated Element');"); else {
                try {
                    Document doc = getItsNatDocument().getDocument();
                    int pos = Integer.parseInt(posElem.getValue());
                    HTMLOptionElement before = getOptionElement(pos);
                    HTMLOptionElement added = (HTMLOptionElement) doc.createElement("option");
                    added.appendChild(doc.createTextNode(newItem));
                    selectElem.add(added, before);
                    selectElem.setSelectedIndex(pos);
                } catch (NumberFormatException ex) {
                    getItsNatDocument().addCodeToSend("alert('Bad Position');");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    getItsNatDocument().addCodeToSend("alert('Bad Position');");
                }
            }
        }
    }
}
