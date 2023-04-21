package com.fh.auge.internal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import com.domainlanguage.money.Money;
import com.fh.auge.currency.CurrencyExchangeRate;
import com.fh.auge.currency.CurrencyExchangeRateDao;

public class CurrencyExchangeRateDaoImpl implements CurrencyExchangeRateDao {

    private SimpleJdbcTemplate jdbcTemplate;

    public Money convert(Currency currency, Money m) {
        CurrencyExchangeRate rate = getCurrencyExchangeRate(m.breachEncapsulationOfCurrency(), currency);
        return rate.convert(m);
    }

    @Required
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
    }

    ParameterizedRowMapper<CurrencyExchangeRate> mapper = new ParameterizedRowMapper<CurrencyExchangeRate>() {

        public CurrencyExchangeRate mapRow(ResultSet rs, int rowNum) throws SQLException {
            CurrencyExchangeRate s = new CurrencyExchangeRate(Currency.getInstance(rs.getString("cur_from")), Currency.getInstance(rs.getString("cur_to")), rs.getBigDecimal("ratio"), rs.getDate("curs_time"));
            return s;
        }
    };

    public void add(CurrencyExchangeRate rate) {
        jdbcTemplate.update("insert into auge_currency_rate(cur_from,cur_to, curs_time, ratio) values(?,?,?,?)", rate.getFromCurrency().getCurrencyCode(), rate.getToCurrency().getCurrencyCode(), rate.getTime(), rate.getRate());
    }

    public void delete(CurrencyExchangeRate rate) {
        jdbcTemplate.update("delete from auge_currency_rate where cur_from=? and cur_to=?", rate.getFromCurrency().getCurrencyCode(), rate.getToCurrency().getCurrencyCode());
    }

    public CurrencyExchangeRate getCurrencyExchangeRate(Currency fromCurrency, Currency toCurrency) {
        if (fromCurrency.equals(toCurrency)) return CurrencyExchangeRate.createDummyRate(fromCurrency);
        return jdbcTemplate.queryForObject("select * from auge_currency_rate where cur_from=? and cur_to=?", mapper, fromCurrency.getCurrencyCode(), toCurrency.getCurrencyCode());
    }

    public List<CurrencyExchangeRate> getCurrencyExchangeRates() {
        return jdbcTemplate.query("select * from auge_currency_rate", mapper);
    }

    public void update(CurrencyExchangeRate rate) {
        delete(rate);
        add(rate);
    }
}
