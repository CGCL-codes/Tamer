package net.infordata.ifw2.web.grds;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import net.infordata.ifw2.web.bnds.IAction;
import net.infordata.ifw2.web.bnds.IBndsFlow;
import net.infordata.ifw2.web.bnds.IField;
import net.infordata.ifw2.web.bnds.IForm;
import net.infordata.ifw2.web.bnds.StrokeStripe;
import net.infordata.ifw2.web.ctrl.IFlowComponent;
import net.infordata.ifw2.web.dec.DefaultCellDecorator;
import net.infordata.ifw2.web.dec.IEditableGridCellDecorator;
import net.infordata.ifw2.web.form.ActionsForm;
import net.infordata.ifw2.web.menu.MenuItem;
import net.infordata.ifw2.web.menu.PopupActionAdapter;
import net.infordata.ifw2.web.menu.PopupMenuFlow;
import net.infordata.ifw2.web.view.IRenderer;
import net.infordata.ifw2.web.view.JSPRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link IBndsFlow} normally used as {@link IFlowComponent} to provide a 
 * grid like visualization of an {@link IDataGridModel}. 
 * @author valentino.proietti
 */
public class EditableGridFlow extends AGridFlow {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(EditableGridFlow.class);

    private final IEditableGridModel ivModel;

    private final IOrderedGridModel ivOrderedModel;

    private final ActionsForm ivActionsForm;

    private final MixedForm ivForm;

    private Set<String> ivColumnsToShow;

    private IEditableGridCellDecorator ivCellDecorator = new DefaultCellDecorator();

    public EditableGridFlow(IEditableGridModel model, String... fieldsToShow) {
        this(model, false, FastEditEnum.NONE, fieldsToShow);
    }

    public EditableGridFlow(IEditableGridModel model, boolean orderedModel, String... fieldsToShow) {
        this(model, orderedModel, FastEditEnum.NONE, fieldsToShow);
    }

    public EditableGridFlow(IEditableGridModel model, FastEditEnum fastEdit, String... fieldsToShow) {
        this(model, false, fastEdit, fieldsToShow);
    }

    /**
   * @param model
   * @param orderedModel - enables functions to swap rows, the model must implement
   *   the {@link IOrderedGridModel} interface.
   * @param fastEdit 
   * @param fieldsToShow
   */
    public EditableGridFlow(IEditableGridModel model, boolean orderedModel, FastEditEnum fastEdit, String... fieldsToShow) {
        this(new JSPRenderer("@ifw2.editableGrid"), model, orderedModel, fastEdit, fieldsToShow);
    }

    protected EditableGridFlow(IRenderer renderer, IEditableGridModel model, boolean orderedModel, FastEditEnum fastEdit, String... fieldsToShow) {
        super(renderer);
        setShowHeader(false);
        ivModel = model;
        ivOrderedModel = orderedModel ? (IOrderedGridModel) model : null;
        ivActionsForm = new ActionsForm();
        ivForm = new MixedForm();
        if (fastEdit == FastEditEnum.NONE) {
            ivActionsForm.registerAction("first", new ModelActions.First(ivModel)).registerAction("prev", new ModelActions.Previous(ivModel)).registerAction("next", new ModelActions.Next(ivModel)).registerAction("last", new ModelActions.Last(ivModel)).registerAction("setCurrent", new ModelActions.SetCurrent(ivModel)).registerAction("setCurrentAndEdit", new ModelActions.SetCurrentAndEdit(ivModel)).registerAction("prevPg", new PreviousPgAction()).registerAction("nextPg", new NextPgAction());
        } else if (fastEdit == FastEditEnum.ON_MOUSE_SELECT) {
            ivActionsForm.registerAction("first", new ModelActions.First(ivModel)).registerAction("prev", new ModelActions.Previous(ivModel)).registerAction("next", new ModelActions.Next(ivModel)).registerAction("last", new ModelActions.Last(ivModel)).registerAction("setCurrent", new ModelActions.SetCurrentAndFastEdit(ivModel)).registerAction("setCurrentAndEdit", new ModelActions.SetCurrentAndFastEdit(ivModel)).registerAction("prevPg", new PreviousPgAction()).registerAction("nextPg", new NextPgAction());
        } else if (fastEdit == FastEditEnum.ALL) {
            ivActionsForm.registerAction("first", new ModelActions.FirstAndFastEdit(ivModel)).registerAction("prev", new ModelActions.PreviousAndFastEdit(ivModel)).registerAction("next", new ModelActions.NextAndFastEdit(ivModel)).registerAction("last", new ModelActions.LastAndFastEdit(ivModel)).registerAction("setCurrent", new ModelActions.SetCurrentAndFastEdit(ivModel)).registerAction("setCurrentAndEdit", new ModelActions.SetCurrentAndFastEdit(ivModel)).registerAction("prevPg", new PreviousPgAndFastEditAction(ivModel)).registerAction("nextPg", new NextPgAndFastEditAction(ivModel));
        } else {
            throw new NullPointerException();
        }
        ivActionsForm.registerAction("nop", new NOPAction()).registerAction("scrollTo", new ScrollToAction()).registerAction("showCompleteText", new ModelActions.ShowCompleteText(this)).registerAction("edit", new ModelActions.Edit(ivModel)).registerAction("insert", new ModelActions.Insert(ivModel, null, false)).registerAction("insertAfter", new ModelActions.Insert(ivModel, null, true)).registerAction("cancel", new ModelActions.Cancel(ivModel)).registerAction("post", new ModelActions.Post(ivModel)).registerAction("remove", new ModelActions.Remove(ivModel)).registerAction("columnPopup", new ColumnPopupAction()).registerAction("rowPopup", new RowPopupAction());
        if (ivOrderedModel != null) {
            ivActionsForm.registerAction("moveUp", new ModelActions.MoveRow(ivOrderedModel, -1)).registerAction("moveDn", new ModelActions.MoveRow(ivOrderedModel, 1));
        }
        if (ivModel instanceof IDataGridModel) {
            IDataGridModel dgmodel = (IDataGridModel) ivModel;
            ivActionsForm.registerAction("sort", new ModelActions.Sort(dgmodel));
        }
        setColumnsToShow(fieldsToShow);
        setRowsPopupEnabled(true);
    }

    /**
   * @return null if the model is not "manually" ordered.
   */
    public final IOrderedGridModel getOrderedModel() {
        return ivOrderedModel;
    }

    @Override
    public final IEditableGridModel getModel() {
        return ivModel;
    }

    @Override
    public EditableGridFlow setPageSize(int pageSize) {
        super.setPageSize(pageSize);
        return this;
    }

    @Override
    public EditableGridFlow setColumnsPopupEnabled(boolean value) {
        super.setColumnsPopupEnabled(value);
        return this;
    }

    @Override
    public EditableGridFlow setRowsPopupEnabled(boolean value) {
        super.setRowsPopupEnabled(value);
        return this;
    }

    @Override
    public EditableGridFlow setShowRowNumber(boolean flag) {
        super.setShowRowNumber(flag);
        return this;
    }

    @Override
    public EditableGridFlow setShowHeader(boolean flag) {
        super.setShowHeader(flag);
        return this;
    }

    @Override
    public EditableGridFlow setRowHeight(String height) {
        super.setRowHeight(height);
        return this;
    }

    @Override
    public EditableGridFlow setHeaderHeight(String height) {
        super.setHeaderHeight(height);
        return this;
    }

    @Override
    public EditableGridFlow setShowNavigationButtons(boolean flag) {
        super.setShowNavigationButtons(flag);
        return this;
    }

    @Override
    public EditableGridFlow setVertResizer(IGridVertResizer resizer) {
        super.setVertResizer(resizer);
        return this;
    }

    @Override
    public final String getDefaultRowHeight() {
        return "24px";
    }

    @Override
    public final String getDblClickAction() {
        return "setCurrentAndEdit";
    }

    @Override
    public final IGridHorzResizer getHorzResizer() {
        return null;
    }

    public EditableGridFlow setCellDecorator(IEditableGridCellDecorator dec) {
        if (dec == null) throw new NullPointerException("dec is null");
        ivCellDecorator = dec;
        return this;
    }

    @Override
    public IEditableGridCellDecorator getCellDecorator() {
        return ivCellDecorator;
    }

    public EditableGridFlow setColumnsToShow(String... columnsToShow) {
        if (columnsToShow.length <= 0) {
            ivColumnsToShow = new LinkedHashSet<String>(ivModel.getColumnCount());
            for (int i = 0; i < ivModel.getColumnCount(); i++) {
                IColumn col = ivModel.getColumn(i);
                if (col.getPreferredSize() != 0) ivColumnsToShow.add(ivModel.getColumnName(i));
            }
            ivColumnsToShow = Collections.unmodifiableSet(ivColumnsToShow);
        } else ivColumnsToShow = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(columnsToShow)));
        return this;
    }

    /**
   * @return an ordered set of columns to be showed.
   */
    @Override
    public Set<String> getColumnsToShow() {
        return ivColumnsToShow;
    }

    protected final IForm getEditingForm() {
        return ivModel.getForm();
    }

    public final IForm getForm() {
        return ivForm;
    }

    @Override
    protected Collection<IForm> getFormsCheckedForErrors() {
        return Arrays.asList(new IForm[] { getEditingForm() });
    }

    /**
   * The expected id is "form".
   */
    @Override
    public IForm getForm(String id) {
        return "form".equals(id) ? getForm() : null;
    }

    @Override
    protected void onFillRowPopup(PopupMenuFlow menuFlow, boolean emptyRow) {
        IEditableGridModel model = (IEditableGridModel) getModel();
        menuFlow.getForm().registerAction("edit", new PopupActionAdapter(new ModelActions.Edit(model))).registerAction("insert", new PopupActionAdapter(new ModelActions.Insert(model, null, false))).registerAction("insertAfter", new PopupActionAdapter(new ModelActions.Insert(model, null, true))).registerAction("remove", new PopupActionAdapter(new ModelActions.Remove(model)));
        if (menuFlow.getMenuStripe().getItems().size() > 0) {
            menuFlow.getMenuStripe().registerSpacer();
        }
        menuFlow.getMenuStripe().registerItem(new MenuItem("edit"), "edit").registerItem(new MenuItem("insert"), "insert").registerItem(new MenuItem("insertAfter"), "insertAfter").registerSpacer().registerItem(new MenuItem("remove"), "remove");
    }

    protected class MixedForm implements IForm, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public <T extends IField> T bindField(Class<T> type, String id) {
            return getEditingForm().bindField(type, id);
        }

        @Override
        public void binded(int id) {
            getEditingForm().binded(id);
        }

        @Override
        public IAction getAction(String id) {
            IAction res = ivActionsForm.getAction(id);
            if (res != null) return res;
            return getEditingForm().getAction(id);
        }

        @Override
        public Set<String> getActions() {
            Set<String> res = new HashSet<String>(ivActionsForm.getActions());
            res.addAll(getEditingForm().getActions());
            return Collections.unmodifiableSet(res);
        }

        @Override
        public IField getBindedField(String id) {
            return getEditingForm().getBindedField(id);
        }

        @Override
        public Set<String> getBindedFields() {
            return getEditingForm().getBindedFields();
        }

        @Override
        public IField getField(String id) {
            return getEditingForm().getField(id);
        }

        @Override
        public Set<String> getFields() {
            return getEditingForm().getFields();
        }

        @Override
        public String getLabel(String fieldId) {
            return getEditingForm().getLabel(fieldId);
        }

        @Override
        public StrokeStripe getStrokes() {
            return getEditingForm().getStrokes();
        }

        @Override
        public boolean isEnabled() {
            return getEditingForm().isEnabled();
        }

        @Override
        public boolean isReadOnly() {
            return getEditingForm().isReadOnly();
        }

        @Override
        public IForm registerAction(String id, IAction action) {
            ivActionsForm.registerAction(id, action);
            return this;
        }

        @Override
        public IForm replaceAction(String id, IAction action) {
            ivActionsForm.replaceAction(id, action);
            return this;
        }

        /**
     * Removes only actions from the action-form not the one from the editing form. 
     */
        @Override
        public IAction removeAction(String id) {
            return ivActionsForm.removeAction(id);
        }

        @Override
        public boolean validate(String fieldId, String... otherFieldsToValidate) {
            return getEditingForm().validate(fieldId, otherFieldsToValidate);
        }
    }
}
