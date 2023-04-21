package com.tensegrity.palorules.source;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.tensegrity.palorules.CellAreaDefinition;
import com.tensegrity.palorules.IRuleEditor;
import com.tensegrity.palorules.IRuleSourcePartEditor;
import com.tensegrity.palorules.RuleEditorMessages;
import com.tensegrity.palorules.functions.IFunction;
import com.tensegrity.palorules.util.RuleConstants;
import com.tensegrity.palorules.util.UIUtil;
import com.tensegrity.palorules.util.XMLConstants;
import com.tensegrity.palorules.util.XMLUtil;

/**
 * Top level content item container.
 * @author AndreasEbbert
 * @version $Id: TopLevelContainer.java,v 1.3 2007/06/06 10:56:21 AndreasEbbert
 *          Exp $
 */
class TopLevelContainer extends AbstractTopLevelContainer implements PaintListener {

    private static final String MSG_NO_RULE = RuleEditorMessages.getString("label.no_rule");

    private String errMsg;

    private NewRuleItem newRuleItem;

    /**
   * Constructor specifying the parent <code>Composite</code> and the
   * RuleEditor.
   * @param p the parent composite.
   * @param ed the rule editor.
   */
    TopLevelContainer(Composite p, final IRuleSourcePartEditor ed) {
        super(p, ed);
        appHelper.getControl().setVisible(false);
        newRuleItem = new NewRuleItem(this);
        addPaintListener(this);
        addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                if (getContentItems() == null || getContentItems().length == 0) {
                    if (ed.getRuleEditor().getDataProvider() == null) return;
                    IFunction f = ed.getFunctionPool().getFunction(IFunction.TYPE_DATA_SOURCE);
                    IContentItem[] ci = ContentItemFactory.getInstance().create(TopLevelContainer.this, f, true);
                    for (int i = 0; i < ci.length; i++) {
                        appendItem(ci[i]);
                    }
                }
            }
        });
        DropTarget dropTarget = new DropTarget(getControl(), DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
        dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        RuleSourcePartEditorDropTargetListener dropAdapter = new RuleSourcePartEditorDropTargetListener((RuleSourcePartEditor) getRuleSourcePartEditor());
        dropTarget.addDropListener(dropAdapter);
        dropTarget.setDropTargetEffect(new RuleEditorDropTargetEffect(this));
    }

    public void paintControl(PaintEvent e) {
        final Display display = e.display;
        TextLayout layout = new TextLayout(display);
        layout.setAlignment(SWT.CENTER);
        Rectangle rect = getClientArea();
        layout.setWidth(rect.width);
        final int y = (rect.height / 2) - 10;
        if (errMsg == null) {
            if (getContentItems() != null && getContentItems().length > 0) return;
            layout.setText(MSG_NO_RULE);
            layout.draw(e.gc, 0, y);
        } else {
            e.gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
            layout.setText(errMsg);
            layout.draw(e.gc, 0, y);
            Rectangle bounds = layout.getBounds();
            Image img = UIUtil.getSWTImage(getShell(), SWT.ICON_WARNING);
            Rectangle imgBounds = img.getBounds();
            e.gc.drawImage(img, bounds.width / 2 - (imgBounds.width / 2), y - imgBounds.height - 10);
        }
        layout.dispose();
    }

    private List<IContentItem> allItems;

    public IContentItem[] getAllContentItems() {
        IContentItem[] topItems = super.getContentItems();
        allItems = new ArrayList<IContentItem>();
        for (int i = 0; i < topItems.length; i++) {
            allItems.add(topItems[i]);
            if (topItems[i] instanceof IContentItemContainer) traverseContainer((IContentItemContainer) topItems[i]);
        }
        return allItems.toArray(new IContentItem[0]);
    }

    private void traverseContainer(IContentItemContainer c) {
        IContentItem[] topItems = c.getContentItems();
        for (int i = 0; i < topItems.length; i++) {
            allItems.add(topItems[i]);
            if (topItems[i] instanceof IContentItemContainer) traverseContainer((IContentItemContainer) topItems[i]);
        }
    }

    public void parse(Node n) {
        setRedraw(false);
        if (newRuleItem != null && !newRuleItem.getControl().isDisposed()) {
            newRuleItem.dispose();
            newRuleItem = null;
        }
        errMsg = null;
        removeAllItems(true);
        Node defNode = XMLUtil.getChild(n, XMLConstants.NN_DEF);
        if (defNode == null) return;
        NodeList nl = defNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node cn = nl.item(i);
            if (cn.getNodeType() != Node.ELEMENT_NODE) continue;
            if (XMLConstants.NN_SRC.equals(cn.getNodeName()) || XMLConstants.isConstant(cn)) {
                SourceDefContentItem edc = new SourceDefContentItem(this);
                appendItem(edc);
                edc.parse(cn);
            } else if (XMLConstants.NN_FUNC.equals(cn.getNodeName())) {
                final IFunction function = getFunction(cn);
                if (RuleConstants.FUNCTION_ID_IF.equals(function.getName())) {
                    IFOperandContentItemContainer ifC = new IFOperandContentItemContainer(this, false);
                    appendItem(ifC);
                    ifC.parse(cn);
                } else {
                    FunctionContentItemContainer funcC = new FunctionContentItemContainer(this, function);
                    appendItem(funcC);
                    funcC.parse(cn);
                }
            }
        }
        setRedraw(true);
    }

    public void appendItem(IContentItem cItem) {
        super.appendItem(cItem);
        updateState();
    }

    public void insertAfter(IContentItem refItem, IContentItem insertItem) {
        super.insertAfter(refItem, insertItem);
        updateState();
    }

    public void insertBefore(IContentItem refItem, IContentItem insertItem) {
        super.insertBefore(refItem, insertItem);
        updateState();
    }

    public void clear() {
        super.removeAllItems(true);
        if (newRuleItem == null) newRuleItem = new NewRuleItem(this);
        updateState();
    }

    public boolean referencesCellArea(CellAreaDefinition cellAre) {
        IContentItem[] items = getAllContentItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof SourceDefContentItem) {
                CellAreaDefinition cDef = ((SourceDefContentItem) items[i]).toCellAreaDef();
                if (cellAre.equals(cDef)) {
                    items[i].setSelection(IContentItem.SEL_ITEM);
                    return true;
                }
            }
        }
        return false;
    }

    public void removeItem(IContentItem cItem, boolean dispose) {
        super.removeItem(cItem, dispose);
        updateState();
    }

    private void updateState() {
        appHelper.getControl().setVisible(getContentItems().length > 0);
        if (getContentItems().length == 0) {
            if (newRuleItem == null || newRuleItem.getControl().isDisposed()) newRuleItem = new NewRuleItem(this);
        } else {
            if (newRuleItem != null) {
                newRuleItem.dispose();
                newRuleItem = null;
            }
        }
    }

    public String toRule() {
        StringBuffer sb = new StringBuffer();
        final int ruleDefMode = getRuleSourcePartEditor().getRuleEditor().getRuleDefinitionMode();
        if (ruleDefMode != IRuleEditor.MODE_RULE_DEF_NORMAL) {
            switch(ruleDefMode) {
                case IRuleEditor.MODE_RULE_DEF_BASIS_ELEMENTS:
                    sb.append(RuleConstants.SRC_EL_BASIC_PREF);
                    break;
                case IRuleEditor.MODE_RULE_DEF_CONSOLIDATED:
                    sb.append(RuleConstants.SRC_EL_CONSOLIDATED_PREF);
                    break;
            }
        }
        sb.append(super.toRule());
        return sb.toString();
    }

    public void clearErrors() {
        errMsg = null;
        this.redraw();
    }

    public void setError(String msg) {
        if (msg == null) return;
        errMsg = msg;
        this.redraw();
    }

    public void setError(Throwable t, String ruleDef) {
        if (ruleDef != null && ruleDef.length() > 0) {
            errMsg = RuleEditorMessages.getString("label.rule_parse_error", new String[] { t == null ? ruleDef : "", t != null ? t.getLocalizedMessage() : "" });
        } else errMsg = t != null ? t.getLocalizedMessage() : "";
        this.redraw();
    }

    public IContentItem getContentItem(Control ctrl) {
        if (newRuleItem != null && newRuleItem.getControl() == ctrl) return newRuleItem;
        return super.getContentItem(ctrl);
    }
}
