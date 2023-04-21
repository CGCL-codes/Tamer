package net.sareweb.acab.actions.travel;

import javax.faces.event.ActionEvent;
import net.sareweb.acab.entity.Travel;
import net.sareweb.acab.entity.User;
import net.sareweb.acab.entity.manager.TravelManager;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

@Name("travelDetailsBean")
@Scope(ScopeType.CONVERSATION)
public class TravelDetailsBean {

    @Logger
    private Log log;

    @In(create = true)
    TravelManager travelManager;

    private Integer idTravel;

    private Travel travel;

    public String selectTravelAction() {
        log.debug("Action: obtaining travel {0}", idTravel);
        travel = travelManager.find(idTravel);
        return ("travelDetail");
    }

    public Integer getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(Integer idTravel) {
        this.idTravel = idTravel;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }
}
