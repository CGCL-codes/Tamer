package com.tysanclan.site.projectewok.entities.dao.filters;

import java.util.Date;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.jeroensteenbeeke.hyperion.data.SearchFilter;
import com.tysanclan.site.projectewok.entities.ChancellorElection;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public class ChancellorElectionFilter extends SearchFilter<ChancellorElection> {

    private static final long serialVersionUID = 1L;

    private Date startBefore;

    private Date startAfter;

    private IModel<User> winner = new Model<User>();

    private boolean noWinner = false;

    /**
	 * @return the winner
	 */
    public User getWinner() {
        return winner.getObject();
    }

    /**
	 * @param winner
	 *            the winner to set
	 */
    public void setWinner(User winner) {
        this.winner = ModelMaker.wrap(winner);
    }

    /**
	 * @return the startBefore
	 */
    public Date getStartBefore() {
        return startBefore;
    }

    /**
	 * @param startBefore
	 *            the startBefore to set
	 */
    public void setStartBefore(Date startBefore) {
        this.startBefore = startBefore;
    }

    /**
	 * @return the startAfter
	 */
    public Date getStartAfter() {
        return startAfter;
    }

    /**
	 * @param startAfter
	 *            the startAfter to set
	 */
    public void setStartAfter(Date startAfter) {
        this.startAfter = startAfter;
    }

    @Override
    public void detach() {
        super.detach();
        if (winner != null) winner.detach();
    }

    public boolean isNoWinner() {
        return noWinner;
    }

    public void setNoWinner(boolean noWinner) {
        this.noWinner = noWinner;
    }
}
