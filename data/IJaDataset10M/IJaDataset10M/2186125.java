package net.solarnetwork.central.datum.dao.ibatis;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.solarnetwork.central.datum.dao.WeatherDatumDao;
import net.solarnetwork.central.datum.domain.DatumQueryCommand;
import net.solarnetwork.central.datum.domain.SkyCondition;
import net.solarnetwork.central.datum.domain.WeatherDatum;
import org.joda.time.ReadableDateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ibatis implementation of {@link WeatherDatumDao}.
 * 
 * @author matt.magoffin
 * @version $Revision: 1178 $ $Date: 2011-02-14 22:19:25 -0500 (Mon, 14 Feb 2011) $
 */
public class IbatisWeatherDatumDao extends IbatisDatumDaoSupport<WeatherDatum> implements WeatherDatumDao {

    /** The query name used for {@link #getMostRecentWeatherDatum(Long, ReadableDateTime)}. */
    public static final String QUERY_WEATHER_DATUM_FOR_MOST_RECENT = "find-WeatherDatum-for-most-recent";

    private Map<Pattern, SkyCondition> conditionMapping;

    /**
	 * Default constructor.
	 */
    public IbatisWeatherDatumDao() {
        super(WeatherDatum.class);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @Override
    public WeatherDatum getDatum(Long id) {
        WeatherDatum datum = super.getDatum(id);
        populateCondition(datum);
        return datum;
    }

    @Override
    public WeatherDatum getDatumForDate(Long nodeId, ReadableDateTime date) {
        WeatherDatum result = super.getDatumForDate(nodeId, date);
        populateCondition(result);
        return result;
    }

    @Override
    protected String setupAggregatedDatumQuery(DatumQueryCommand criteria, Map<String, Object> params) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public WeatherDatum getMostRecentWeatherDatum(Long nodeId, ReadableDateTime upToDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("node", nodeId);
        params.put("upToDate", new java.sql.Timestamp(upToDate.getMillis()));
        List<WeatherDatum> results = getSqlMapClientTemplate().queryForList(QUERY_WEATHER_DATUM_FOR_MOST_RECENT, params, 0, 1);
        if (results != null && results.size() > 0) {
            populateCondition(results);
            return results.get(0);
        }
        return null;
    }

    /**
	 * Populate the {@link WeatherDatum#getCondition()} value for each datum 
	 * in the given list.
	 * 
	 * <p>This calls {@link #populateCondition(WeatherDatum)} on each datum
	 * in the given list.</p>
	 * 
	 * @param list datums to set
	 * @see #populateCondition(WeatherDatum)
	 */
    private void populateCondition(List<WeatherDatum> list) {
        if (list == null || this.conditionMapping == null) {
            return;
        }
        for (WeatherDatum datum : list) {
            if (datum.getCondition() != null) {
                continue;
            }
            String sky = datum.getSkyConditions();
            if (sky == null) {
                continue;
            }
            for (Map.Entry<Pattern, SkyCondition> me : this.conditionMapping.entrySet()) {
                if (me.getKey().matcher(sky).find()) {
                    datum.setCondition(me.getValue());
                    break;
                }
            }
        }
    }

    /**
	 * Populate the {@link WeatherDatum#getCondition()} value for a datum.
	 * 
	 * <p>This uses the configured {@link #getConditionMapping()} to compare
	 * regular expressions against the {@link WeatherDatum#getSkyConditions()}
	 * value. The {@link SkyCondition} for the first pattern that matches in 
	 * {@link #getConditionMapping()} iteration order will be used. If a datum
	 * already has a {@link WeatherDatum#getCondition()} value set, it will
	 * not be changed.</p>
	 * 
	 * @param list datums to set
	 */
    private void populateCondition(WeatherDatum datum) {
        if (datum == null || this.conditionMapping == null) {
            return;
        }
        if (datum.getCondition() != null) {
            return;
        }
        String sky = datum.getSkyConditions();
        if (sky == null) {
            return;
        }
        for (Map.Entry<Pattern, SkyCondition> me : this.conditionMapping.entrySet()) {
            if (me.getKey().matcher(sky).find()) {
                datum.setCondition(me.getValue());
                return;
            }
        }
    }

    /**
	 * Set the {@link #setConditionMapping(Map)} via String keys.
	 * 
	 * <p>This method is a convenience method for setting the {@code conditionMapping} 
	 * property via String keys instead of compiled {@link Pattern} objects. The 
	 * regular expressions are compiled with {@link Pattern#CASE_INSENSITIVE} and
	 * {@link Pattern#DOTALL} flags.</p>
	 * 
	 * @param conditionMapping the mapping of regular expressions to SkyCondition instances
	 */
    public void setConditionMap(Map<String, SkyCondition> conditionMapping) {
        Map<Pattern, SkyCondition> map = new LinkedHashMap<Pattern, SkyCondition>();
        for (Map.Entry<String, SkyCondition> me : conditionMapping.entrySet()) {
            Pattern p = Pattern.compile(me.getKey(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            map.put(p, me.getValue());
        }
        setConditionMapping(map);
    }

    /**
	 * @return the conditionMapping
	 */
    public Map<Pattern, SkyCondition> getConditionMapping() {
        return conditionMapping;
    }

    /**
	 * @param conditionMapping the conditionMapping to set
	 */
    public void setConditionMapping(Map<Pattern, SkyCondition> conditionMapping) {
        this.conditionMapping = conditionMapping;
    }
}
