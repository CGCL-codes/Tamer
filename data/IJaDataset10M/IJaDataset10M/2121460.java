package com.tensegrity.palorules.target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Form;
import org.palo.api.ConnectionEvent;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Rule;
import com.tensegrity.palorules.CellAreaDefinition;
import com.tensegrity.palorules.EditorResources;
import com.tensegrity.palorules.ICoordinateListener;
import com.tensegrity.palorules.IRuleEditor;
import com.tensegrity.palorules.IRuleEditorDataProvider;
import com.tensegrity.palorules.RuleEditorMessages;
import com.tensegrity.palorules.RuleEditorStatusEvent;
import com.tensegrity.palorules.RuleEditorStatusListener;
import com.tensegrity.palorules.StackableLayout;
import com.tensegrity.palorules.source.BorderPainter;
import com.tensegrity.palorules.util.FramedCellsUtil;
import com.tensegrity.palorules.util.RuleConstants;
import com.tensegrity.palorules.util.RuleUtil;
import com.tensegrity.palorules.util.UIUtil;
import com.tensegrity.palorules.util.WLabel;

/**
 * To specify the target area of a rule.
 * @author Andreas Ebbert
 * @version $Id: TargetDefComposite.java,v 1.1 2007/11/16 18:32:00 AndreasEbbert
 *          Exp $
 */
public class TargetDefComposite extends AbstractTargetDefComposite implements ICoordinateListener, PaintListener, RuleEditorStatusListener {

    private static final String DATA_KEY_DIM = "dimension";

    private static final String DATA_KEY_EL = "element";

    private final Composite specPanel;

    private Composite sidePanel;

    private List<Composite> elPanelList = new ArrayList<Composite>();

    private List<CCombo> elComboList = new ArrayList<CCombo>();

    private Label hideLabel;

    private boolean hidingUnspecifiedDimensions;

    private boolean active;

    private final Form topForm;

    private final IRuleEditor editor;

    private final BorderPainter borderPainter;

    private boolean listeningForTargetChanges = true;

    public TargetDefComposite(Form topForm, Composite p, IRuleEditor editor) {
        super(p, SWT.NONE, editor);
        this.topForm = topForm;
        this.editor = editor;
        setLayout(new GridLayout(2, false));
        specPanel = new Composite(this, SWT.DOUBLE_BUFFERED);
        specPanel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        specPanel.setLayout(new StackableLayout());
        specPanel.setBackground(getBackground());
        specPanel.addPaintListener(borderPainter = new BorderPainter(true));
        specPanel.addPaintListener(this);
        addSideActionPanel();
        setTargetCoords(editor.getActiveTarget());
        setEditingEnabled(editor.isEditable() && editor.getEditingMode() != IRuleEditor.EDITING_MODE_TEXT);
        if (hidingUnspecifiedDimensions) {
            collapseUnspecifiedElementControls();
        } else {
            expandUnspecifiedElementControls();
        }
        editor.addStatusListener(this);
        this.addPaintListener(new BorderPaintListener());
    }

    private void addSideActionPanel() {
        sidePanel = new Composite(this, SWT.NONE);
        sidePanel.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, !true, false));
        GridLayout gl = UIUtil.createNullMarginGridLayout();
        gl.marginTop = 10;
        gl.verticalSpacing = 5;
        sidePanel.setLayout(gl);
        sidePanel.setBackground(getBackground());
        hideLabel = new Label(sidePanel, SWT.NONE);
        hideLabel.setBackground(sidePanel.getBackground());
        hideLabel.setToolTipText(hidingUnspecifiedDimensions ? RuleEditorMessages.getString("ElementDefContentItem.Button.Hide_Unspec") : RuleEditorMessages.getString("ElementDefContentItem.Button.Show_Unspec"));
        hideLabel.setImage(!hidingUnspecifiedDimensions ? EditorResources.ICON_SMALL_ARROW_DOWN : EditorResources.ICON_SMALL_ARROW_LEFT);
        hideLabel.addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                hidingUnspecifiedDimensions = !hidingUnspecifiedDimensions;
                if (hidingUnspecifiedDimensions) {
                    collapseUnspecifiedElementControls();
                } else {
                    expandUnspecifiedElementControls();
                }
                hideLabel.setImage(!hidingUnspecifiedDimensions ? EditorResources.ICON_SMALL_ARROW_DOWN : EditorResources.ICON_SMALL_ARROW_LEFT);
                hideLabel.setToolTipText(hidingUnspecifiedDimensions ? RuleEditorMessages.getString("ElementDefContentItem.Button.Hide_Unspec") : RuleEditorMessages.getString("ElementDefContentItem.Button.Show_Unspec"));
            }
        });
        sidePanel.setVisible(false);
    }

    private void collapseUnspecifiedElementControls() {
        if (elComboList == null || elComboList.size() == 0) return;
        for (int i = 0; i < elComboList.size(); i++) {
            CCombo combo = elComboList.get(i);
            if (combo.getSelectionIndex() == combo.getItemCount() - 1) {
                Composite c = combo.getParent();
                c.setData(StackableLayout.DK_HIDDEN, "true");
            }
        }
        topForm.layout(true, true);
    }

    private void expandUnspecifiedElementControls() {
        for (int i = 0; i < elPanelList.size(); i++) {
            Composite c = elPanelList.get(i);
            c.setData(StackableLayout.DK_HIDDEN, "false");
        }
        topForm.layout(true, true);
    }

    private void setEditingEnabled(boolean en) {
        for (int i = 0; i < elComboList.size(); i++) {
            CCombo c = elComboList.get(i);
            c.setEditable(en);
            c.setEnabled(en);
        }
    }

    public void dispose() {
        specPanel.removePaintListener(borderPainter);
        specPanel.removePaintListener(this);
        getEditor().removeStatusListener(this);
        super.dispose();
    }

    private boolean targetSet;

    public void setTargetCoords(CellAreaDefinition targetDef) {
        clear();
        if (targetDef == null || targetDef.length() == 0) {
            targetSet = false;
            if (sidePanel.isVisible()) sidePanel.setVisible(false);
            return;
        }
        final Element[] coords = targetDef.getElements();
        targetSet = true;
        if (!sidePanel.isVisible()) sidePanel.setVisible(true);
        for (int i = 0; i < coords.length; i++) {
            Dimension dim = coords[i] != null ? coords[i].getDimension() : targetDef.getDimensions()[i];
            if (dim != null) {
                createElementRow(RuleUtil.getRegularDimension(dim), coords[i]);
            }
        }
        setEditingEnabled(editor.isEditable());
        updateAll();
    }

    public void setListeningForTargetChanges(boolean b) {
        if (listeningForTargetChanges == b) return;
        this.listeningForTargetChanges = b;
    }

    public String toRule() {
        StringBuffer sb = new StringBuffer();
        sb.append(RuleConstants.ELEMENT_SPECIFICATION_HOOK_OPEN);
        for (int i = 0; i < elComboList.size(); i++) {
            CCombo c = elComboList.get(i);
            Dimension dim = (Dimension) c.getData(DATA_KEY_DIM);
            if (c.getSelectionIndex() == -1) continue;
            if (c.getSelectionIndex() < c.getItemCount() - 1) {
                if (dim != null) {
                    sb.append("'");
                    sb.append(mangleName(dim.getName()));
                    sb.append("'");
                    sb.append(RuleConstants.CUBE_DIM_EL_DEL);
                }
                String el = mangleName(c.getItem(c.getSelectionIndex()));
                sb.append("'");
                sb.append(el);
                sb.append("'");
                if (i < elComboList.size() - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append(RuleConstants.ELEMENT_SPECIFICATION_HOOK_CLOSE);
        return sb.toString();
    }

    private String mangleName(String elName) {
        return RuleUtil.mangleName(elName);
    }

    public void coordinatesSelected(Element[] coords) {
    }

    private void setActive(boolean b) {
        this.active = b;
    }

    public void deactivate() {
        setActive(false);
    }

    public boolean isListeningForCoordinateSelections() {
        return active;
    }

    public void handleConnectionEvent(ConnectionEvent e) {
        switch(e.getType()) {
            case ConnectionEvent.CONNECTION_EVENT_DIMENSIONS_RENAMED:
                {
                    Dimension[] dims = e.getDimensions();
                    for (int i = 0; i < dims.length; i++) {
                        CCombo combo = getCCombo(dims[i]);
                        if (combo == null) continue;
                        Control[] ctrl = combo.getParent().getChildren();
                        WLabel wl = (WLabel) ctrl[0];
                        wl.setText(dims[i].getName());
                        wl.redraw();
                        updateComposite(combo.getParent());
                    }
                    break;
                }
            case ConnectionEvent.CONNECTION_EVENT_ELEMENTS_REMOVED:
                {
                    Element[] els = e.getElements();
                    for (int i = 0; i < els.length; i++) {
                        Dimension dim = els[i].getDimension();
                        CCombo combo = getCCombo(dim);
                        if (combo == null) continue;
                        combo.remove(els[i].getName());
                        combo.redraw();
                        updateComposite(combo.getParent());
                    }
                    break;
                }
            case ConnectionEvent.CONNECTION_EVENT_ELEMENTS_ADDED:
            case ConnectionEvent.CONNECTION_EVENT_ELEMENTS_RENAMED:
                {
                    Element[] els = e.getElements();
                    for (int i = 0; i < els.length; i++) {
                        Dimension dim = els[i].getDimension();
                        CCombo combo = getCCombo(dim);
                        if (combo == null) continue;
                        String[] items = dim.getElementNames();
                        List<String> il = new LinkedList<String>(Arrays.asList(items));
                        il.add(RuleEditorMessages.getString("ElementDefContentItem.Item.All"));
                        final int sel = combo.getSelectionIndex();
                        combo.removeAll();
                        combo.setItems(il.toArray(new String[0]));
                        if (sel != -1) combo.select(sel);
                        updateComposite(combo.getParent());
                    }
                    break;
                }
        }
    }

    private void updateComposite(final Composite ctrl) {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                if (ctrl == null || ctrl.isDisposed()) return;
                ctrl.redraw();
                ctrl.update();
                ctrl.layout(true, true);
            }
        });
    }

    private void updateAll() {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                if (specPanel == null || topForm == null) return;
                if (specPanel.isDisposed() || topForm.isDisposed()) return;
                redraw();
                layout(true, true);
            }
        });
    }

    private CCombo getCCombo(Dimension dim) {
        for (int i = 0; i < elComboList.size(); i++) {
            CCombo combo = elComboList.get(i);
            Object o = combo.getData(DATA_KEY_DIM);
            if (dim.equals(o)) return combo;
        }
        return null;
    }

    private void clear() {
        for (int i = elPanelList.size() - 1; i >= 0; i--) {
            Control ctrl = elPanelList.get(i);
            ctrl.dispose();
            ctrl = null;
        }
        elPanelList = new ArrayList<Composite>();
        elComboList = new ArrayList<CCombo>();
    }

    private CCombo createCombo(Composite panel) {
        final CCombo combo = new CCombo(panel, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setBackground(panel.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        return combo;
    }

    private int getIndexOf(CCombo c, String item) {
        if (item == null) return -1;
        String[] items = c.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) return i;
        }
        return -1;
    }

    private Composite createNewRowPanel() {
        Composite panel = new Composite(specPanel, SWT.NONE);
        panel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
        panel.setLayout(new FillLayout());
        panel.setBackground(specPanel.getBackground());
        return panel;
    }

    private void createElementRow(Dimension dim, Element selectedEl) {
        final Composite elPanel = createNewRowPanel();
        String lt = dim.getName();
        WLabel elLabel = new WLabel(elPanel, lt);
        elLabel.setBackground(specPanel.getBackground());
        final CCombo elCombo = createCombo(elPanel);
        final IRuleEditorDataProvider dProv = editor.getDataProvider();
        if (dProv != null) {
            String[] items = dProv.getElementNames(dim.getId());
            List<String> il = new LinkedList<String>(Arrays.asList(items));
            il.add(RuleEditorMessages.getString("ElementDefContentItem.Item.All"));
            elCombo.setItems(il.toArray(new String[0]));
        }
        if (selectedEl == null) {
            elCombo.select(elCombo.getItemCount() - 1);
        } else {
            int index = getIndexOf(elCombo, selectedEl.getName());
            if (index != -1) {
                elCombo.select(index);
            }
            elCombo.setData(DATA_KEY_EL, selectedEl);
        }
        elCombo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                Element orgEl = (Element) elCombo.getData(DATA_KEY_EL);
                if (orgEl != null && elCombo.getSelectionIndex() < elCombo.getItemCount() - 1) {
                    String el = elCombo.getItem(elCombo.getSelectionIndex());
                    if (el.equals(orgEl.getName())) return;
                }
                setDirty();
            }
        });
        elCombo.setData(DATA_KEY_DIM, dim);
        elComboList.add(elCombo);
        elPanelList.add(elPanel);
    }

    public void dirtyStateChanged(boolean newDirtyState) {
        updateAll();
    }

    public void editingModeChanged() {
    }

    public void editorClear(boolean b) {
    }

    public void preSave(RuleEditorStatusEvent e) {
    }

    public void ruleChanged(Rule rule) {
    }

    public void targetChanged(CellAreaDefinition targetDef) {
    }

    public void paintControl(PaintEvent e) {
        if (targetSet) return;
        TextLayout layout = new TextLayout(e.display);
        layout.setAlignment(SWT.CENTER);
        Rectangle rect = getClientArea();
        layout.setWidth(rect.width);
        final int y = (rect.height / 2) - 10;
        layout.setText(RuleEditorMessages.getString("label.no_cube"));
        layout.draw(e.gc, 0, y);
    }

    private class BorderPaintListener implements PaintListener {

        public void paintControl(PaintEvent e) {
            if ((getEditor().getRule() != null && FramedCellsUtil.fittingCubeEditor(getEditor(), getEditor().getRule(), getEditor().getDataProvider().getActiveCubeEditor())) || getEditor().isDirty()) {
                Composite composite = (Composite) e.widget;
                Rectangle b = composite.getClientArea();
                e.gc.setForeground(EditorResources.COLOR_BORDER_TARGET);
                e.gc.setLineStyle(FramedCellsUtil.LINE_STYLE);
                e.gc.setLineWidth(FramedCellsUtil.LINE_WIDTH);
                e.gc.drawRectangle(b.x, b.y, b.width - 1, b.height - 1);
            }
        }
    }
}
