package au.org.tpac.portal.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DlpUser;
import au.org.tpac.portal.domain.Notification;
import au.org.tpac.portal.domain.PartyIndividual;
import au.org.tpac.portal.domain.PartyOrganisation;
import au.org.tpac.portal.domain.PathWatch;
import au.org.tpac.portal.repository.RowMappers.DatasetMapper;
import au.org.tpac.portal.repository.RowMappers.CategoryMapper;
import au.org.tpac.portal.repository.RowMappers.PartyIndividualMapper;
import au.org.tpac.portal.repository.RowMappers.PathWatchMapper;
import au.org.tpac.portal.repository.RowMappers.NotificationMapper;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

;

/**
 * The Class JdbcNotificationDao.
 */
public class JdbcNotificationDao extends SimpleJdbcDaoSupport implements NotificationDao, InitializingBean {

    /**
	 * Constructor
	 */
    public JdbcNotificationDao() {
        super();
    }

    @Override
    public void add(final Notification entry) {
        getJdbcTemplate().update("INSERT INTO notification (enabled, email, period_type, notification_type, offset_in_period)" + " VALUES (?,?,?,?,?)", new PreparedStatementSetter() {

            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, entry.getEnabled());
                ps.setString(2, entry.getEmail());
                ps.setString(3, entry.getPeriodType());
                ps.setInt(4, entry.getNotificationType());
                ps.setInt(5, entry.getOffsetInPeriod());
            }
        });
    }

    @Override
    public void update(final Notification entry) {
        getJdbcTemplate().update("UPDATE notification SET " + " enabled=?, email=?, period_type=?, notification_type=?, offset_in_period=? " + " WHERE id = ?", new PreparedStatementSetter() {

            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBoolean(1, entry.getEnabled());
                ps.setString(2, entry.getEmail());
                ps.setString(3, entry.getPeriodType());
                ps.setInt(4, entry.getNotificationType());
                ps.setInt(5, entry.getOffsetInPeriod());
                ps.setInt(6, entry.getId());
            }
        });
    }

    @Override
    public void delete(final Notification entry) {
        getJdbcTemplate().update("DELETE from notification WHERE id = ?", new PreparedStatementSetter() {

            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, entry.getId());
            }
        });
    }

    @Override
    public List<Notification> list() {
        return getJdbcTemplate().query("SELECT * FROM notification", new NotificationMapper());
    }

    @Override
    public Notification get(int id) {
        return getJdbcTemplate().queryForObject("SELECT * FROM notification WHERE id = ?", new Object[] { id }, new NotificationMapper());
    }
}
