package com.aces2win.server;

import java.util.Date;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import com.aces2win.client.VoteService;
import com.aces2win.server.entities.Vote;
import com.aces2win.shared.beans.VoteBean;
import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VoteServiceImpl extends RemoteServiceServlet implements VoteService {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9192656735177578589L;

    @Override
    public void vote(boolean isBullish) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        User u = new User("forgen2502@gmail.com", "gmail.com");
        Vote v = new Vote();
        Date d = new Date();
        v.setCreateTime(d);
        v.setBullish(isBullish);
        v.setUser(u);
        pm.makePersistent(v);
    }

    @Override
    public VoteBean results(Date d) {
        VoteBean result = new VoteBean();
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Date today = new Date();
        Query query = pm.newQuery(Vote.class);
        query.setFilter("creationDate == dateParam");
        query.setFilter("isBullish == bullishParam");
        query.declareParameters("Date dateParam");
        query.declareParameters("boolean bullishParam");
        List<Vote> resultsBullish = (List<Vote>) query.execute(today, true);
        List<Vote> resultsBearish = (List<Vote>) query.execute(today, true);
        result.setResultsBullish(resultsBullish.size());
        result.setResultsBearish(resultsBearish.size());
        return null;
    }
}
