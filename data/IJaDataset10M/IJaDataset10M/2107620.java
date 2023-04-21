package com.gusto.engine.evaluation.data.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.gusto.engine.evaluation.RealEvaluation;
import com.gusto.engine.evaluation.data.DataDAO;

/**
 * <p>Implements the {@link DataDAO} by loading data from a database.</p>
 * 
 * @author amokrane.belloui@gmail.com
 * 
 */
public class DataDAOJdbcImpl implements DataDAO {

    private Logger log = Logger.getLogger(getClass());

    private String CONFIG;

    private ConfigItem configItem;

    public void setCONFIG(String config) {
        this.CONFIG = config;
        this.configItem = processConfigItem(this.CONFIG);
    }

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private ConfigItem processConfigItem(String tablecolumn) {
        try {
            ConfigItem configItem = new ConfigItem();
            configItem.table = tablecolumn.substring(0, tablecolumn.indexOf("->")).trim();
            String rest = tablecolumn.substring(tablecolumn.indexOf("->") + 2).trim();
            configItem.user = rest.substring(0, rest.indexOf(",")).trim();
            rest = rest.substring(rest.indexOf(",") + 1).trim();
            configItem.item = rest.substring(0, rest.indexOf(",")).trim();
            rest = rest.substring(rest.indexOf(",") + 1).trim();
            configItem.eval = rest;
            return configItem;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public List<RealEvaluation> getData(Long MIN_USER, Long MAX_USER) {
        log.info("Getting data from Database");
        ConfigItem ci = configItem;
        String sql = "select * from " + ci.table + " " + "where " + ci.user + " > " + MIN_USER + " " + "and " + ci.user + " < " + MAX_USER + " ";
        Object[] params = new Object[] {};
        int[] types = new int[] {};
        List<RealEvaluation> result = jdbcTemplate.query(sql, params, types, new RealEvaluationgRowMapper());
        return result;
    }

    private class ConfigItem {

        public String table;

        public String user;

        public String item;

        public String eval;
    }

    class RealEvaluationgRowMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int index) throws SQLException {
            ConfigItem ci = configItem;
            RealEvaluation eval = new RealEvaluation();
            eval.setUser(rs.getInt(ci.user));
            eval.setItem(rs.getInt(ci.item));
            eval.setEvaluation(Double.parseDouble(rs.getObject(ci.eval).toString()));
            return eval;
        }
    }
}
