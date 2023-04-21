package org.subethamail.web.action;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.core.lists.i.ListDataPlus;
import org.subethamail.web.Backend;
import org.subethamail.web.action.auth.AuthRequired;
import org.subethamail.web.model.PaginateModel;

/**
 * Searches all lists for a query term (if provided), or
 * just gets basic list if no term provided.  Results
 * are paginated.
 * 
 * @author Jon Stevens
 * @author Jeff Schnitzer
 */
public class GetLists extends AuthRequired {

    /** */
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(GetLists.class);

    public static class Model extends PaginateModel {

        /** */
        @Getter
        @Setter
        String query = "";

        @Getter
        @Setter
        List<ListDataPlus> lists;
    }

    public void initialize() {
        this.getCtx().setModel(new Model());
    }

    /** */
    public void authExecute() throws Exception {
        Model model = (Model) this.getCtx().getModel();
        if (model.query.trim().length() == 0) {
            model.lists = Backend.instance().getAdmin().getListsPlus(model.getSkip(), model.getCount());
            model.setTotalCount(Backend.instance().getAdmin().countLists());
        } else {
            model.lists = Backend.instance().getAdmin().searchListsPlus(model.query, model.getSkip(), model.getCount());
            model.setTotalCount(Backend.instance().getAdmin().countListsQuery(model.query));
        }
    }
}
