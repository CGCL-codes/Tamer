package com.fh.auge.core.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import com.fh.auge.core.Market;
import com.fh.auge.core.PreferenceService;

public class JdbcStockDao implements IStockDao {

    private SimpleJdbcTemplate jdbcTemplate;

    private PreferenceService preferenceService;

    private ISecurityDao securityDao;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    public void add(Stock stock) {
        jdbcTemplate.update("insert into stocks(symbol,market) values (?,?)", stock.getSecurity().getId(), stock.getMarket().getId());
    }

    public List<Stock> getStocks() {
        return jdbcTemplate.query("select * from stocks", mapper);
    }

    public void remove(Stock stock) {
        jdbcTemplate.update("delete stocks where symbol=?", stock.getSecurity().getId(), stock.getMarket().getId());
    }

    ParameterizedRowMapper<Stock> mapper = new ParameterizedRowMapper<Stock>() {

        public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
            Security s = securityDao.get(rs.getString("symbol"));
            if (s == null) {
                throw new DataIntegrityViolationException("security " + rs.getString("symbol") + " not found");
            }
            Market m = preferenceService.getMarket(rs.getString("market"));
            if (m == null) {
                throw new DataIntegrityViolationException("market " + rs.getString("market") + " not found");
            }
            return new Stock(s, m);
        }
    };

    public void setPreferenceService(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    public void setSecurityDao(ISecurityDao securityDao) {
        this.securityDao = securityDao;
    }

    @Override
    public Stock get(String symbol, String market) {
        return jdbcTemplate.queryForObject("select * from stocks where symbol=? and market=?", mapper, symbol, market);
    }
}
