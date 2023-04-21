package net.sareweb.acab.actions.travel;

import java.util.Arrays;
import javax.faces.event.ActionEvent;
import net.sareweb.acab.entity.Travel;
import net.sareweb.acab.entity.manager.TravelManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;

@Name("myTravelsBean")
@Scope(ScopeType.CONVERSATION)
public class MyTravelsBean extends EntityQuery<Travel> {

    @Logger
    private Log log;

    @In
    Credentials credentials;

    @In(create = true)
    TravelManager travelManager;

    private static final String EJBQL = "select travel from Travel travel";

    private static final String[] RESTRICTIONS = { "lower(travel.user.login) = #{credentials.username}" };

    private Travel travel = new Travel();

    public MyTravelsBean() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
    }

    public Travel getTravel() {
        return travel;
    }

    public String showMyTravelsAction() {
        log.debug("to show {0} user�s travels", credentials.getUsername());
        return "myTravels";
    }

    public void showMyTravelsActionListener(ActionEvent event) {
        log.debug("Obtaining {0} user�s tralves", credentials.getUsername());
    }

    public String deleteTravelAction() {
        log.debug("to delete  user�s travel {0}", travel.getIdTravel());
        travelManager.fullTravelDeletion(travel.getIdTravel());
        this.refresh();
        return "myTravels";
    }

    @Begin
    public String start() {
        return "/acab/travel/MyTravels.xhtml";
    }

    @End
    public String end() {
        return "/home.xhtml";
    }
}
