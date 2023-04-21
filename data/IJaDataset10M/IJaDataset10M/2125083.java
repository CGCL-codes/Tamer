package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.EditView;
import com.tll.client.mvc.view.ViewClass;
import com.tll.client.ui.field.account.AccountPanel;
import com.tll.common.data.ModelDataRequest;
import com.tll.common.model.SmbizEntityType;

/**
 * AccountEditView - Base AbstractView for editing accounts.
 * @author jpk
 */
public class AccountEditView extends EditView {

    /**
	 * TODO This is temporary until account specific edit views are created!
	 */
    public static final Class klas = new Class();

    public static final class Class extends ViewClass {

        @Override
        public String getName() {
            return "accountEdit";
        }

        @Override
        public AccountEditView newView() {
            return new AccountEditView();
        }
    }

    /**
	 * Constructor
	 */
    public AccountEditView() {
        super(new AccountPanel());
    }

    @Override
    public ViewClass getViewClass() {
        return klas;
    }

    @Override
    protected ModelDataRequest getNeededModelData() {
        final ModelDataRequest modelCacheRequest = new ModelDataRequest();
        modelCacheRequest.requestEntityList(SmbizEntityType.CURRENCY);
        modelCacheRequest.requestEntityPrototype(SmbizEntityType.ACCOUNT_ADDRESS);
        return modelCacheRequest;
    }
}
