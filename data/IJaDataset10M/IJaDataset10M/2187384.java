package net.sf.brightside.qualifications.tapestry.pages.team;

import java.util.List;
import net.sf.brightside.qualifications.metamodel.Team;
import net.sf.brightside.qualifications.metamodel.User;
import net.sf.brightside.qualifications.service.dao.TeamDAO;
import net.sf.brightside.qualifications.tapestry.pages.Index;
import net.sf.brightside.qualifications.tapestry.springIntegration.SpringBean;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.springframework.context.ApplicationContext;

public class TeamAdministration {

    @SuppressWarnings("unused")
    @ApplicationState
    private User user;

    private boolean userExists;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private Messages messages;

    @Inject
    @SpringBean
    private TeamDAO teamDAO;

    @Inject
    private ComponentResources resources;

    @Inject
    private ApplicationContext applicationContext;

    @Persist
    private Team team;

    @Persist(value = "flash")
    private String errorMessage;

    @Persist(value = "flash")
    private String deleteErrorMessage;

    Object onActivate(long id) {
        if (id == 0L | !userExists) return Index.class;
        team = teamDAO.getById(new Long(id));
        return null;
    }

    Object onActivate() {
        if (!userExists) return Index.class;
        return null;
    }

    Object onSubmit() {
        try {
            if (team.getName() == null) {
                throw new Exception("Please enter team name.");
            }
            teamDAO.save(team);
            resources.discardPersistentFieldChanges();
        } catch (Exception e) {
            resources.discardPersistentFieldChanges();
            errorMessage = e.getMessage();
            return null;
        }
        return TeamAdministration.class;
    }

    public Team getTeam() {
        if (team == null) team = createTeam();
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    private Team createTeam() {
        return (Team) applicationContext.getBean(Team.class.getName());
    }

    private Team listTeam;

    public Team getListTeam() {
        return listTeam;
    }

    public void setListTeam(Team listTeam) {
        this.listTeam = listTeam;
    }

    public List<Team> getAllTeams() {
        return teamDAO.getAll();
    }

    public BeanModel<Team> getTeamModel() {
        BeanModel<Team> teamModel = beanModelSource.create(Team.class, false, messages);
        teamModel.add("Action", null);
        return teamModel;
    }

    Object onActionFromDelete(long id) {
        team = teamDAO.getById(new Long(id));
        try {
            teamDAO.delete(team);
            resources.discardPersistentFieldChanges();
        } catch (Exception e) {
            resources.discardPersistentFieldChanges();
            deleteErrorMessage = e.getMessage();
        }
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDeleteErrorMessage() {
        return deleteErrorMessage;
    }
}
