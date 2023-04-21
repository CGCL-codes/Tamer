package com.jaeksoft.searchlib.web.controller.query;

import com.jaeksoft.searchlib.SearchLibException;
import com.jaeksoft.searchlib.request.AbstractRequest;
import com.jaeksoft.searchlib.request.RequestTypeEnum;
import com.jaeksoft.searchlib.result.AbstractResult;
import com.jaeksoft.searchlib.web.controller.CommonController;
import com.jaeksoft.searchlib.web.controller.ScopeAttribute;

public abstract class AbstractQueryController extends CommonController {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1943618096637337561L;

    public AbstractQueryController() throws SearchLibException {
        super();
    }

    protected final AbstractRequest getRequest(RequestTypeEnum type) throws SearchLibException {
        AbstractRequest request = getAbstractRequest();
        if (request == null) return null;
        if (request.getType() != type) return null;
        return request;
    }

    public final AbstractRequest getAbstractRequest() throws SearchLibException {
        return (AbstractRequest) ScopeAttribute.QUERY_REQUEST.get(this);
    }

    protected AbstractResult<?> getAbstractResult() {
        return (AbstractResult<?>) ScopeAttribute.QUERY_SEARCH_RESULT.get(this);
    }

    protected AbstractResult<?> getResult(RequestTypeEnum type) {
        AbstractResult<?> result = getAbstractResult();
        if (result == null) return null;
        if (result.getRequest().getType() != type) return null;
        return result;
    }

    private boolean isResult(RequestTypeEnum type) {
        AbstractResult<?> result = getAbstractResult();
        if (result == null) return false;
        return result.getRequest().getType() == type;
    }

    public boolean isResultSearch() {
        return isResult(RequestTypeEnum.SearchRequest);
    }

    public boolean isResultSpellCheck() {
        return isResult(RequestTypeEnum.SpellCheckRequest);
    }

    public boolean isResultMoreLikeThis() {
        return isResult(RequestTypeEnum.MoreLikeThisRequest);
    }

    @Override
    public void eventQueryEditResult(AbstractResult<?> result) {
        reloadPage();
    }

    @Override
    public void eventQueryEditRequest(AbstractRequest request) {
        reloadPage();
    }
}
