package com.tysanclan.site.projectewok.entities.dao.filters;

import org.apache.wicket.model.IModel;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.JoinApplication;
import com.tysanclan.site.projectewok.entities.JoinVerdict;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class JoinVerdictFilter extends SearchFilter<JoinVerdict> {

    private static final long serialVersionUID = 1L;

    private IModel<User> senator;

    private IModel<JoinApplication> joinApplication;

    /**
	 * @return the senator
	 */
    public User getSenator() {
        return senator.getObject();
    }

    /**
	 * @param senator
	 *            the senator to set
	 */
    public void setSenator(User senator) {
        this.senator = ModelMaker.wrap(senator);
    }

    /**
	 * @return the joinApplication
	 */
    public JoinApplication getJoinApplication() {
        return joinApplication.getObject();
    }

    /**
	 * @param joinApplication
	 *            the joinApplication to set
	 */
    public void setJoinApplication(JoinApplication joinApplication) {
        this.joinApplication = ModelMaker.wrap(joinApplication);
    }

    @Override
    public void detach() {
        super.detach();
        senator.detach();
        joinApplication.detach();
    }
}
