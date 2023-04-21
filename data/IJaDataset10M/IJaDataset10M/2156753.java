package net.sf.solarnetwork.node.impl;

import java.util.Map;
import net.sf.solarnetwork.node.ConsumptionDatum;
import net.sf.solarnetwork.node.DayDatum;
import net.sf.solarnetwork.node.PowerDatum;
import net.sf.solarnetwork.node.PriceDatum;
import net.sf.solarnetwork.node.UploadService;
import net.sf.solarnetwork.node.WeatherDatum;
import net.sf.solarnetwork.node.dao.ConsumptionDatumDao;
import net.sf.solarnetwork.node.dao.PowerDatumDao;
import net.sf.solarnetwork.node.dao.PriceDatumDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UploadService} that uses JDBC.
 * 
 * <p>This class supports only persisting {@link PowerDatum} and 
 * {@link ConsumptionDatum} objects. All methods relating to other objects
 * throw {@link UnsupportedOperationException}.</p>
 *
 * @author matt
 * @version $Revision: 275 $ $Date: 2009-08-07 05:00:11 -0400 (Fri, 07 Aug 2009) $
 */
public class JdbcUploadService implements UploadService {

    private String key = null;

    private PowerDatumDao powerDatumDao = null;

    private ConsumptionDatumDao consumptionDatumDao = null;

    private PriceDatumDao priceDatumDao = null;

    /**
	 * Default constructor.
	 */
    public JdbcUploadService() {
        super();
    }

    public String getKey() {
        return "JdbcUploadService:" + key;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Long uploadPowerDatum(PowerDatum data) {
        return powerDatumDao.storePowerDatum(data);
    }

    public Long uploadConsumptionDatum(ConsumptionDatum data) {
        return consumptionDatumDao.storeConsumptionDatum(data);
    }

    public Long uploadDayDatum(DayDatum data) {
        throw new UnsupportedOperationException();
    }

    public Long uploadPriceDatum(PriceDatum data) {
        return priceDatumDao.storePriceDatum(data);
    }

    public Long uploadWeatherDatum(WeatherDatum data) {
        throw new UnsupportedOperationException();
    }

    public Long[] uploadDayAndWeatherDatum(DayDatum dayData, WeatherDatum weatherData, Map<String, ?> attributes) {
        throw new UnsupportedOperationException();
    }

    /**
	 * @param key the key to set
	 */
    public void setKey(String key) {
        this.key = key;
    }

    /**
	 * @return the powerDatumDao
	 */
    public PowerDatumDao getPowerDatumDao() {
        return powerDatumDao;
    }

    /**
	 * @param powerDatumDao the powerDatumDao to set
	 */
    public void setPowerDatumDao(PowerDatumDao powerDatumDao) {
        this.powerDatumDao = powerDatumDao;
    }

    /**
	 * @return the consumptionDatumDao
	 */
    public ConsumptionDatumDao getConsumptionDatumDao() {
        return consumptionDatumDao;
    }

    /**
	 * @param consumptionDatumDao the consumptionDatumDao to set
	 */
    public void setConsumptionDatumDao(ConsumptionDatumDao consumptionDatumDao) {
        this.consumptionDatumDao = consumptionDatumDao;
    }
}
