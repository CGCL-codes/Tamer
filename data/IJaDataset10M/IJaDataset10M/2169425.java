package de.objectcode.time4u.servernew.dao;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import de.objectcode.time4u.servernew.bean.TeamBean;
import de.objectcode.time4u.servernew.bean.UserBean;
import de.objectcode.time4u.servernew.util.PageParameters;

public class TeamDAO extends SqlMapClientDaoSupport {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(TeamDAO.class);

    public int countAll() {
        return (Integer) this.getSqlMapClientTemplate().queryForObject("Team-countAllTeams");
    }

    @SuppressWarnings("unchecked")
    public List<TeamBean> getAll() {
        return this.getSqlMapClientTemplate().queryForList("Team-getAll");
    }

    @SuppressWarnings("unchecked")
    public TeamBean getAllById(final String teamId) {
        return (TeamBean) this.getSqlMapClientTemplate().queryForObject("Team-getById", teamId);
    }

    @SuppressWarnings("unchecked")
    public List<UserBean> getAllLessMember(final String teamId) {
        return this.getSqlMapClientTemplate().queryForList("Team-getByIdLessMember", teamId);
    }

    @SuppressWarnings("unchecked")
    public List<UserBean> getAllMember(final String teamId) {
        return this.getSqlMapClientTemplate().queryForList("User-getByTeam", teamId);
    }

    @SuppressWarnings("unchecked")
    public List<TeamBean> getAll(final PageParameters param) {
        return this.getSqlMapClientTemplate().queryForList("Team-getAll", param);
    }

    @SuppressWarnings("unchecked")
    public List<TeamBean> getAllByOwner(final PageParameters param) {
        return this.getSqlMapClientTemplate().queryForList("Team-getAllByOwner", param);
    }

    @SuppressWarnings("unchecked")
    public TeamBean getAllByTeamName(final String teamname) {
        return (TeamBean) this.getSqlMapClientTemplate().queryForObject("Team-getAllByTeamname", teamname);
    }

    @SuppressWarnings("unchecked")
    public List<String> getAllUserIdByTeam(final String teamname) {
        return this.getSqlMapClientTemplate().queryForList("Team-getAllUserIdByTeam", teamname);
    }

    public void createTeam(final String teamname, final String teamowner) {
    }

    public void updateTeamList(final PageParameters param) {
        this.getSqlMapClientTemplate().update("Team-updateTeamList", param);
    }

    public void deleteTeam(final PageParameters param) {
        this.getSqlMapClientTemplate().delete("Team-deleteTeam1", param);
        this.getSqlMapClientTemplate().delete("Team-deleteTeam2", param);
    }
}
