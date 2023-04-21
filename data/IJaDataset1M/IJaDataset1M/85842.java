package com.icesoft.faces.component.ext;

import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.renderkit.TableRenderer;
import com.icesoft.faces.component.ext.taglib.Util;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.EvaluationException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: rmayhew Date: Aug 28, 2006 Time: 12:45:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class RowSelector extends UIPanel {

    private Boolean value;

    private Boolean toggleOnClick;

    private Boolean toggleOnInput;

    private Boolean multiple;

    private Boolean enhancedMultiple;

    private String mouseOverClass;

    private String selectedClass;

    private String selectedMouseOverClass;

    private MethodBinding selectionListener;

    private MethodBinding selectionAction;

    private MethodBinding clickListener;

    private MethodBinding clickAction;

    private Integer clickedRow;

    private Boolean immediate;

    private Integer dblClickDelay;

    private Boolean preStyleOnSelection;

    private transient List selectedRowsList = new ArrayList();

    public static final String COMPONENT_TYPE = "com.icesoft.faces.RowSelector";

    public static final String COMPONENT_FAMILY = "com.icesoft.faces.RowSelectorFamily";

    public static final int DEFAULT_DBLCLICK_DELAY = 200;

    public RowSelector() {
        JavascriptContext.includeLib(JavascriptContext.ICE_EXTRAS, getFacesContext());
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public Boolean getValue() {
        ValueBinding vb = getValueBinding("value");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        if (value != null) {
            return value;
        }
        return Boolean.FALSE;
    }

    public void setValue(Boolean value) {
        ValueBinding vb = getValueBinding("value");
        if (vb != null) {
            vb.setValue(getFacesContext(), value);
        } else {
            this.value = value;
        }
    }

    public Integer getClickedRow() {
        ValueBinding vb = getValueBinding("clickedRow");
        if (vb != null) {
            return (Integer) vb.getValue(getFacesContext());
        }
        if (clickedRow != null) {
            return clickedRow;
        }
        return new Integer(-1);
    }

    public void setClickedRow(Integer clickedRow) {
        ValueBinding vb = getValueBinding("clickedRow");
        if (vb != null) {
            vb.setValue(getFacesContext(), clickedRow);
        } else {
            this.clickedRow = clickedRow;
        }
    }

    public boolean isEnhancedMultiple() {
        if (enhancedMultiple != null) {
            return enhancedMultiple.booleanValue();
        }
        ValueBinding vb = getValueBinding("enhancedMultiple");
        return vb != null ? ((Boolean) vb.getValue(getFacesContext())).booleanValue() : false;
    }

    public void setEnhancedMultiple(boolean enhancedMultiple) {
        this.enhancedMultiple = new Boolean(enhancedMultiple);
    }

    public Boolean getMultiple() {
        ValueBinding vb = getValueBinding("multiple");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        if (multiple != null) {
            return multiple;
        }
        return Boolean.FALSE;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

    public Boolean getToggleOnClick() {
        ValueBinding vb = getValueBinding("toggleOnClick");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        if (toggleOnClick != null) {
            return toggleOnClick;
        }
        return Boolean.TRUE;
    }

    public void setToggleOnClick(Boolean toggleOnClick) {
        this.toggleOnClick = toggleOnClick;
    }

    public Boolean getToggleOnInput() {
        ValueBinding vb = getValueBinding("toggleOnInput");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        if (toggleOnInput != null) {
            return toggleOnInput;
        }
        return Boolean.TRUE;
    }

    public void setToggleOnInput(Boolean toggleOnInput) {
        this.toggleOnInput = toggleOnInput;
    }

    public String getMouseOverClass() {
        return Util.getQualifiedStyleClass(this, mouseOverClass, CSS_DEFAULT.ROW_SELECTION_MOUSE_OVER, "mouseOverClass");
    }

    public void setMouseOverClass(String mouseOverClass) {
        this.mouseOverClass = mouseOverClass;
    }

    public String getSelectedClass() {
        return Util.getQualifiedStyleClass(this, selectedClass, CSS_DEFAULT.ROW_SELECTION_SELECTED, "selectedClass");
    }

    public void setSelectedClass(String selectedClass) {
        this.selectedClass = selectedClass;
    }

    public String getSelectedMouseOverClass() {
        return Util.getQualifiedStyleClass(this, selectedMouseOverClass, CSS_DEFAULT.ROW_SELECTION_SELECTED_MOUSE_OVER, "selectedMouseOverClass");
    }

    public void setSelectedMouseOverClass(String selectedMouseOverClass) {
        this.selectedMouseOverClass = selectedMouseOverClass;
    }

    public MethodBinding getSelectionListener() {
        return selectionListener;
    }

    public void setSelectionListener(MethodBinding selectionListener) {
        this.selectionListener = selectionListener;
    }

    public MethodBinding getSelectionAction() {
        return selectionAction;
    }

    public void setSelectionAction(MethodBinding selectionListener) {
        this.selectionAction = selectionListener;
    }

    public MethodBinding getClickListener() {
        return clickListener;
    }

    public void setClickListener(MethodBinding clickListener) {
        this.clickListener = clickListener;
    }

    public MethodBinding getClickAction() {
        return clickAction;
    }

    public void setClickAction(MethodBinding clickAction) {
        this.clickAction = clickAction;
    }

    public Integer getDblClickDelay() {
        ValueBinding vb = getValueBinding("dblClickDelay");
        if (vb != null) {
            return (Integer) vb.getValue(getFacesContext());
        }
        if (dblClickDelay != null) {
            return dblClickDelay;
        }
        return new Integer(DEFAULT_DBLCLICK_DELAY);
    }

    public void setDblClickDelay(Integer dblClickDelay) {
        ValueBinding vb = getValueBinding("dblClickDelay");
        if (vb != null) {
            vb.setValue(getFacesContext(), dblClickDelay);
        } else {
            this.dblClickDelay = dblClickDelay;
        }
    }

    public Boolean getImmediate() {
        if (immediate != null) {
            return immediate;
        }
        ValueBinding vb = getValueBinding("immediate");
        if (vb != null) {
            return (Boolean) vb.getValue(getFacesContext());
        }
        return Boolean.TRUE;
    }

    public void setImmediate(Boolean immediate) {
        this.immediate = immediate;
    }

    public boolean isPreStyleOnSelection() {
        if (preStyleOnSelection != null) {
            return preStyleOnSelection.booleanValue();
        }
        ValueBinding vb = getValueBinding("preStyleOnSelection");
        if (vb != null) {
            Boolean ret = (Boolean) vb.getValue(getFacesContext());
            if (ret != null) {
                return ret.booleanValue();
            }
        }
        return false;
    }

    public void setPreStyleOnSelection(boolean preStyleOnSelection) {
        this.preStyleOnSelection = new Boolean(preStyleOnSelection);
    }

    public void processDecodes(FacesContext facesContext) {
        HtmlDataTable dataTable = getParentDataTable(this);
        String dataTableId = dataTable.getClientId(facesContext);
        String selectedRowsParameter = TableRenderer.getSelectedRowParameterName(dataTableId);
        Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
        String selectedRows = (String) requestMap.get(selectedRowsParameter);
        boolean isCtrlKey = "true".equals(requestMap.get(selectedRowsParameter + "ctrKy"));
        boolean isShiftKey = "true".equals(requestMap.get(selectedRowsParameter + "sftKy"));
        String clickedRowParameter = TableRenderer.getClickedRowParameterName(dataTableId);
        String clickCountParameter = TableRenderer.getClickCountParameterName(dataTableId);
        String clickedRowIndex = (String) requestMap.get(clickedRowParameter);
        String clickCount = (String) requestMap.get(clickCountParameter);
        ClickActionEvent clickActionEvent = null;
        RowSelector rowSelector = (RowSelector) this;
        boolean skipSelection = false;
        if (clickedRowIndex != null && clickCount != null) {
            if (clickedRowIndex.trim().length() != 0 && clickCount.trim().length() != 0) {
                int rowIndex = dataTable.getRowIndex();
                boolean rowClicked = false;
                int row = Integer.parseInt(clickedRowIndex);
                if (row == rowIndex) {
                    if (this.getParent() instanceof UIColumns) {
                        Object servedRow = this.getParent().getAttributes().get("rowServed");
                        if (servedRow != null) {
                            if (String.valueOf(servedRow).equals(String.valueOf(rowIndex))) {
                                return;
                            }
                        } else {
                            this.getParent().getAttributes().put("rowServed", String.valueOf(rowIndex));
                        }
                    }
                    rowClicked = true;
                }
                try {
                    if (rowClicked) {
                        if (rowSelector.getClickListener() != null || rowSelector.getClickAction() != null) {
                            clickActionEvent = createClickActionEvent(rowSelector, row, Integer.parseInt(clickCount));
                        }
                        if (Integer.parseInt(clickCount) == 2 && getValue().booleanValue()) {
                            skipSelection = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (selectedRows == null || selectedRows.trim().length() == 0 || skipSelection) {
            if (null != clickActionEvent) {
                rowSelector.queueEvent(clickActionEvent);
            }
            return;
        }
        Integer oldRow = getClickedRow();
        int rowIndex = dataTable.getRowIndex();
        boolean rowClicked = false;
        int row = Integer.parseInt(selectedRows);
        if (row == rowIndex) {
            if (this.getParent() instanceof UIColumns) {
                Object servedRow = this.getParent().getAttributes().get("rowServed");
                if (servedRow != null) {
                    if (String.valueOf(servedRow).equals(String.valueOf(rowIndex))) {
                        return;
                    }
                } else {
                    this.getParent().getAttributes().put("rowServed", String.valueOf(rowIndex));
                }
            }
            rowClicked = true;
        }
        try {
            if (rowClicked) {
                boolean b = rowSelector.getValue().booleanValue();
                if (isEnhancedMultiple()) {
                    if ((!isCtrlKey && !isShiftKey) || isShiftKey) {
                        b = true;
                        _queueEvent(rowSelector, rowIndex, b, clickActionEvent);
                        return;
                    }
                    if (isCtrlKey && !isShiftKey) {
                        b = !b;
                        _queueEvent(rowSelector, rowIndex, b, clickActionEvent);
                        return;
                    }
                } else {
                    b = !b;
                    _queueEvent(rowSelector, rowIndex, b, clickActionEvent);
                    if (!getMultiple().booleanValue()) {
                        if (oldRow != null && oldRow.intValue() >= 0 && oldRow.intValue() != rowIndex) {
                            dataTable.setRowIndex(oldRow.intValue());
                            setValue(Boolean.FALSE);
                            dataTable.setRowIndex(rowIndex);
                        }
                    }
                }
            } else {
                if (isEnhancedMultiple()) {
                    if (!isCtrlKey && !isShiftKey) {
                        rowSelector.setValue(Boolean.FALSE);
                        return;
                    }
                    if (isShiftKey) {
                        if (oldRow.intValue() < row) {
                            if ((rowIndex >= oldRow.intValue() && rowIndex <= row)) {
                                rowSelector.setValue(Boolean.TRUE);
                                selectedRowsList.add(new Integer(rowIndex));
                            } else {
                                if (!isCtrlKey) rowSelector.setValue(Boolean.FALSE);
                            }
                        } else if (oldRow.intValue() >= row) {
                            if (rowIndex <= oldRow.intValue() && rowIndex >= row) {
                                rowSelector.setValue(Boolean.TRUE);
                                selectedRowsList.add(new Integer(rowIndex));
                            } else {
                                if (!isCtrlKey) rowSelector.setValue(Boolean.FALSE);
                            }
                        }
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HtmlDataTable getParentDataTable(UIComponent uiComponenent) {
        UIComponent parentComp = uiComponenent.getParent();
        if (parentComp == null) {
            throw new RuntimeException("RowSelectorRenderer: decode. Could not find an Ice:dataTable as a parent componenent");
        }
        if (parentComp instanceof com.icesoft.faces.component.ext.HtmlDataTable) {
            return (HtmlDataTable) parentComp;
        }
        return getParentDataTable(parentComp);
    }

    public void broadcast(FacesEvent event) {
        super.broadcast(event);
        if (event instanceof RowSelectorEvent) {
            this.setClickedRow(new Integer(((RowSelectorEvent) event).getRow()));
            ((RowSelectorEvent) event).setSelectedRows(selectedRowsList);
        }
        if (event instanceof RowSelectorEvent && selectionListener != null) {
            selectionListener.invoke(getFacesContext(), new Object[] { (RowSelectorEvent) event });
        }
        if (event instanceof RowSelectorActionEvent && selectionAction != null) {
            try {
                FacesContext facesContext = getFacesContext();
                Object result = selectionAction.invoke(facesContext, null);
                String outcome = (result != null) ? result.toString() : null;
                NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                nh.handleNavigation(facesContext, selectionAction.getExpressionString(), outcome);
                facesContext.renderResponse();
            } catch (MethodNotFoundException e) {
                throw new FacesException(selectionAction.getExpressionString() + ": " + e.getMessage(), e);
            } catch (EvaluationException e) {
                throw new FacesException(selectionAction.getExpressionString() + ": " + e.getMessage(), e);
            }
        }
        if (event instanceof ClickActionEvent && clickListener != null) {
            clickListener.invoke(getFacesContext(), new Object[] { (ClickActionEvent) event });
        }
        if (event instanceof ClickActionEvent && clickAction != null) {
            try {
                FacesContext facesContext = getFacesContext();
                Object result = clickAction.invoke(facesContext, null);
                String outcome = (result != null) ? result.toString() : null;
                NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                nh.handleNavigation(facesContext, clickAction.getExpressionString(), outcome);
                facesContext.renderResponse();
            } catch (MethodNotFoundException e) {
                throw new FacesException(clickAction.getExpressionString() + ": " + e.getMessage(), e);
            } catch (EvaluationException e) {
                throw new FacesException(clickAction.getExpressionString() + ": " + e.getMessage(), e);
            }
        }
        selectedRowsList.clear();
    }

    public Object saveState(FacesContext context) {
        Object[] state = new Object[18];
        state[0] = super.saveState(context);
        state[1] = value;
        state[2] = multiple;
        state[3] = toggleOnClick;
        state[4] = toggleOnInput;
        state[5] = clickedRow;
        state[6] = mouseOverClass;
        state[7] = selectedClass;
        state[8] = selectedMouseOverClass;
        state[9] = saveAttachedState(context, selectionListener);
        state[10] = saveAttachedState(context, selectionAction);
        state[11] = immediate;
        state[12] = styleClass;
        state[13] = enhancedMultiple;
        state[14] = saveAttachedState(context, clickListener);
        state[15] = saveAttachedState(context, clickAction);
        state[16] = dblClickDelay;
        state[17] = preStyleOnSelection;
        return state;
    }

    public void restoreState(FacesContext context, Object stateIn) {
        Object[] state = (Object[]) stateIn;
        super.restoreState(context, state[0]);
        value = (Boolean) state[1];
        multiple = (Boolean) state[2];
        toggleOnClick = (Boolean) state[3];
        toggleOnInput = (Boolean) state[4];
        clickedRow = (Integer) state[5];
        mouseOverClass = (String) state[6];
        selectedClass = (String) state[7];
        selectedMouseOverClass = (String) state[8];
        selectionListener = (MethodBinding) restoreAttachedState(context, state[9]);
        selectionAction = (MethodBinding) restoreAttachedState(context, state[10]);
        immediate = (Boolean) state[11];
        styleClass = (String) state[12];
        enhancedMultiple = (Boolean) state[13];
        clickListener = (MethodBinding) restoreAttachedState(context, state[14]);
        clickAction = (MethodBinding) restoreAttachedState(context, state[15]);
        dblClickDelay = (Integer) state[16];
        preStyleOnSelection = (Boolean) state[17];
    }

    private String styleClass;

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, styleClass, CSS_DEFAULT.ROW_SELECTION_BASE, "styleClass");
    }

    void _queueEvent(RowSelector rowSelector, int rowIndex, boolean isSelected, ClickActionEvent clickActionEvent) {
        rowSelector.setValue(new Boolean(isSelected));
        if (isSelected) {
            selectedRowsList.add(new Integer(rowIndex));
        }
        RowSelectorEvent evt = new RowSelectorEvent(rowSelector, rowIndex, isSelected);
        if (getImmediate().booleanValue()) {
            evt.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            evt.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        rowSelector.queueEvent(evt);
        if (null != clickActionEvent) {
            clickActionEvent.setRowSelectorEvent(evt);
            rowSelector.queueEvent(clickActionEvent);
        }
        if (rowSelector.getSelectionAction() != null) {
            RowSelectorActionEvent actevt = new RowSelectorActionEvent(this);
            if (getImmediate().booleanValue()) {
                actevt.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
            } else {
                actevt.setPhaseId(PhaseId.INVOKE_APPLICATION);
            }
            rowSelector.queueEvent(actevt);
        }
    }

    private ClickActionEvent createClickActionEvent(RowSelector rowSelector, int rowIndex, int clickCount) {
        ClickActionEvent evt = new ClickActionEvent(rowSelector, rowIndex, clickCount);
        if (getImmediate().booleanValue()) {
            evt.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            evt.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        return evt;
    }
}
