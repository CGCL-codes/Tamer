package com.chasmcity.munro.server;

import static java.util.Collections.*;
import com.chasmcity.munro.client.ClimberService;
import com.chasmcity.munro.data.Climber;
import com.chasmcity.munro.facebook.FacebookAuthServlet;
import com.chasmcity.munro.shared.Munro;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import java.util.Collection;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;
import org.apache.log4j.Logger;

/**
 * TODO: Missing class description for ClimerService!
 * <p>
 * &copy; Copyright 2010 Kizoom Ltd.
 * <p>
 * $Id: wildebeest_standard.epf,v 1.9 2007/01/17 14:59:59 danf Exp $
 */
public class ClimberServiceImpl extends RemoteServiceServlet implements ClimberService {

    private final PersistenceManagerFactory persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public Collection<Munro> getClimbedMunros() {
        String userId = getCurrentFacebookUserId();
        if (userId != null) {
            logger.info("Fetching climber");
            Climber climber = persistenceManagerFactory.getPersistenceManager().getObjectById(Climber.class, userId);
        }
        return emptyList();
    }

    private String getCurrentFacebookUserId() {
        try {
            FacebookClient client = FacebookAuthServlet.getFacebookClient(getThreadLocalRequest().getSession());
            User facebookUser = client.fetchObject("me", User.class);
            return facebookUser.getId();
        } catch (Exception e) {
            logger.error(this, e);
            return null;
        }
    }
}
