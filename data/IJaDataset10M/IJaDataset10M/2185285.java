package net.sf.fallfair.view;

import java.sql.SQLException;
import javax.swing.DefaultListModel;
import net.sf.fallfair.CRUD.CRUD;
import net.sf.fallfair.CRUD.Persistent;
import net.sf.fallfair.context.FairContext;
import net.sf.fallfair.validator.BindingError;
import net.sf.fallfair.validator.BindingResult;
import net.sf.fallfair.validator.BindingResultImpl;
import net.sf.fallfair.validator.Validator;

public abstract class AbstractFairSaveCommand<F extends FairMaintenanceView<P>, P extends Persistent> implements FairMaintenanceCommand<F, P> {

    private final CRUD<P> crud;

    private final Validator<P> validator;

    public AbstractFairSaveCommand(CRUD<P> crud, Validator<P> validator) {
        super();
        this.crud = crud;
        this.validator = validator;
    }

    @Override
    public boolean execute(F fairView, FairContext context) {
        if (fairView.isEditMode()) {
            if (!bindAndValidate(fairView, context)) {
                return false;
            }
            onAfterBindAndValidate(fairView);
        } else {
            onUpdateViewAdd(fairView);
        }
        return true;
    }

    protected boolean bindAndValidate(F fairView, FairContext context) {
        try {
            BindingResult<P> result = bind(fairView, context);
            validate(result, context);
            if (!result.isBindingError()) {
                update(result, fairView, context);
            } else {
                BindingError error = result.getErrors().next();
                fairView.updateStatus(new DictionaryStatusLabel(error.getMessage(), error.getArguments()));
                return false;
            }
        } catch (SQLException ex) {
            fairView.updateStatus(new ExceptionStatusLabel(ex));
            return false;
        }
        return true;
    }

    protected BindingResult<P> bind(F fairView, FairContext context) {
        return new BindingResultImpl<P>(fairView.bind());
    }

    protected void validate(BindingResult<P> obj, FairContext context) {
        this.validator.validate(obj, context);
    }

    protected abstract void update(BindingResult<P> result, F fairView, FairContext context) throws SQLException;

    protected void onAfterBindAndValidate(F fairView) {
        updateView(fairView);
        fairView.setBrowseMode();
    }

    protected void updateView(F fairView) {
        updateSearch(fairView);
        fairView.resetPersistent();
        fairView.render();
    }

    protected void updateSearch(F fairView) {
        fairView.getSearchTextField().setText("");
        fairView.getSearchResultsList().setModel(new DefaultListModel());
    }

    protected void onUpdateViewAdd(F fairView) {
        updateView(fairView);
        fairView.setEditMode();
    }

    protected CRUD<P> getCRUD() {
        return this.crud;
    }
}
