package net.solarnetwork.central.query.web;

import javax.servlet.http.HttpServletRequest;
import net.solarnetwork.central.dao.SolarNodeDao;
import net.solarnetwork.central.datum.dao.DayDatumDao;
import net.solarnetwork.central.datum.dao.WeatherDatumDao;
import net.solarnetwork.central.datum.domain.DayDatum;
import net.solarnetwork.central.datum.domain.ReportingDatum;
import net.solarnetwork.central.datum.domain.WeatherDatum;
import net.solarnetwork.central.domain.SolarNode;
import net.solarnetwork.central.web.AbstractNodeController;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for getting the most recently available WeatherDatum for a node.
 *
 * @author matt
 * @version $Revision: 1405 $ $Date: 2011-06-03 22:19:43 -0400 (Fri, 03 Jun 2011) $
 */
@Controller
@RequestMapping("/currentWeather.*")
public class MostRecentWeatherController extends AbstractNodeController {

    private WeatherDatumDao weatherDatumDao;

    private DayDatumDao dayDatumDao;

    /**
	 * Constructor.
	 * 
	 * @param solarNodeDao the SolarNodeDao to use
	 * @param weatherDatumDao the WeatherDatumDao to use
	 * @param dayDatumDao the DayDatumDao to use
	 */
    @Autowired
    public MostRecentWeatherController(SolarNodeDao solarNodeDao, WeatherDatumDao weatherDatumDao, DayDatumDao dayDatumDao) {
        super();
        setSolarNodeDao(solarNodeDao);
        this.weatherDatumDao = weatherDatumDao;
        this.dayDatumDao = dayDatumDao;
    }

    /**
	 * Get the most recent WeatherDatum for a given node.
	 * 
	 * @param nodeId the ID of the node to get the weather info for
	 * @param request the servlet request
	 * @return model and view
	 */
    @RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
    public ModelAndView getMostRecentWeather(@RequestParam(value = "nodeId") Long nodeId, HttpServletRequest request) {
        ModelAndView mv = resolveViewFromUrlExtension(request);
        SolarNode node = getSolarNodeDao().get(nodeId);
        WeatherDatum weather = weatherDatumDao.getMostRecentWeatherDatum(nodeId, new DateTime());
        DayDatum day = null;
        LocalTime infoTime = null;
        if (weather instanceof ReportingDatum) {
            ReportingDatum repWeather = (ReportingDatum) weather;
            day = dayDatumDao.getDatumForDate(nodeId, repWeather.getLocalDate());
            infoTime = repWeather.getLocalTime();
        } else if (weather != null && weather.getInfoDate() != null) {
            day = dayDatumDao.getDatumForDate(nodeId, weather.getInfoDate());
            infoTime = weather.getInfoDate().toDateTime(DateTimeZone.forTimeZone(node.getTimeZone())).toLocalTime();
        }
        if (weather != null && day != null && infoTime != null && (weather.getCondition() != null || day.getCondition() != null)) {
            if (infoTime.isBefore(day.getSunrise()) || infoTime.isAfter(day.getSunset())) {
                if (weather.getCondition() != null) {
                    weather.setCondition(weather.getCondition().getNightEquivalent());
                }
                if (day.getCondition() != null) {
                    day.setCondition(weather.getCondition().getNightEquivalent());
                }
            }
        }
        mv.addObject("weather", weather);
        mv.addObject("day", day);
        mv.addObject("tz", node.getTimeZone());
        return mv;
    }
}
