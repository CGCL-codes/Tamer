package pt.utl.ist.lucene.level1query;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import pt.utl.ist.lucene.*;
import pt.utl.ist.lucene.forms.GeoPoint;
import pt.utl.ist.lucene.forms.RectangleForm;
import pt.utl.ist.lucene.forms.CircleForm;
import pt.utl.ist.lucene.forms.UnknownForm;
import pt.utl.ist.lucene.utils.Dates;
import java.util.*;
import java.io.IOException;
import com.pjaol.search.geo.utils.DistanceUtils;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Jorge Machado
 * @date 25/Jul/2008
 * @see pt.utl.ist.lucene.level1query
 */
public class QueryParams {

    private static final Logger logger = Logger.getLogger(QueryParams.class);

    public static final double SPATIAL_INVALID_VALUE = 1000;

    public static final QueryParams NULL_PARAMS = new QueryParams();

    private QueryConfiguration queryConfiguration = null;

    double latitude = QueryParams.SPATIAL_INVALID_VALUE;

    double longitude = QueryParams.SPATIAL_INVALID_VALUE;

    Map<String, Double> extraLatitudes = new HashMap<String, Double>();

    Map<String, Double> extraLongitudes = new HashMap<String, Double>();

    Map<String, Long> extraTimes = new HashMap<String, Long>();

    double radium = -1;

    double northlimit = QueryParams.SPATIAL_INVALID_VALUE;

    double southlimit = QueryParams.SPATIAL_INVALID_VALUE;

    double eastlimit = QueryParams.SPATIAL_INVALID_VALUE;

    double westlimit = QueryParams.SPATIAL_INVALID_VALUE;

    long timeMiliseconds = -1;

    long radiumMiliseconds = -1;

    long startTimeMiliseconds = -1;

    long endTimeMiliseconds = -1;

    QEEnum qeEnum = QEEnum.defaultQE;

    FilterEnum filter = null;

    OrderEnum order = null;

    Model model = null;

    double diagonal = -1;

    GeoPoint centroide = null;

    RectangleForm rectangleForm = null;

    public QueryParams() {
    }

    public double getDiagonal() {
        if (diagonal < 0) {
            if (isRadium()) {
                diagonal = 2 * radium;
            } else if (isSpatialBox()) {
                diagonal = DistanceUtils.orthodromicDistance(southlimit, westlimit, northlimit, eastlimit);
            } else {
                logger.warn(">>>>>>>>>>>>>>>Diagonal impossible to compute no BOX or Radium");
            }
        }
        return diagonal;
    }

    public GeoPoint getCentroide() {
        if (centroide == null) {
            if (isSpatialPoint()) {
                centroide = new GeoPoint(latitude, longitude);
            } else if (isSpatialBox()) {
                return getRectangleForm().getCentroide();
            } else {
                logger.warn(">>>>>>>>>>>>>>>Centroide impossible to compute, no Spatial Box or Point");
            }
        }
        return centroide;
    }

    public RectangleForm getRectangleForm() {
        if (rectangleForm == null) {
            if (isSpatialBox()) {
                rectangleForm = new RectangleForm(northlimit, westlimit, southlimit, eastlimit);
            } else {
                logger.warn(">>>>>>>>>>>>>>>Centroide impossible to compute no Rectangle Form");
            }
        }
        return rectangleForm;
    }

    public QueryConfiguration getQueryConfiguration() {
        if (queryConfiguration == null) queryConfiguration = new QueryConfiguration();
        return queryConfiguration;
    }

    public void setQueryConfiguration(QueryConfiguration queryConfiguration) {
        this.queryConfiguration = queryConfiguration;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setRectangleForm(RectangleForm rectangleForm) {
        setEastlimit(rectangleForm.getEast());
        setWestlimit(rectangleForm.getWest());
        setSouthlimit(rectangleForm.getSouth());
        setNorthlimit(rectangleForm.getNorth());
        setLatitude(rectangleForm.getCentroide().getLat());
        setLongitude(rectangleForm.getCentroide().getLng());
    }

    public void setCircleForm(CircleForm circleForm) {
        setLatitude(circleForm.getCentroide().getLat());
        setLongitude(circleForm.getCentroide().getLng());
        setRadiumMiles(circleForm.getRadium());
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        setLatitude(geoPoint.getCentroide().getLat());
        setLongitude(geoPoint.getCentroide().getLng());
    }

    public void setGmlPolygon(String xml) throws IOException, ParserConfigurationException, SAXException {
        UnknownForm unknownForm = LgteDocumentWrapper.getGmlPolygonUnknownForm(xml);
        if (unknownForm instanceof RectangleForm) setRectangleForm((RectangleForm) unknownForm); else if (unknownForm instanceof CircleForm) setCircleForm((CircleForm) unknownForm); else if (unknownForm instanceof GeoPoint) setGeoPoint((GeoPoint) unknownForm); else {
            setGeoPoint(unknownForm.getCentroide());
            setRadiumMiles(unknownForm.getWidth() / (double) 2);
        }
    }

    public void setLatitude(String latitude) {
        if (latitude != null) try {
            this.latitude = Double.parseDouble(latitude);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        if (longitude != null) try {
            this.longitude = Double.parseDouble(longitude);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRadiumKm() {
        return radium;
    }

    public double getRadium() {
        return radium;
    }

    public double getRadiumMiles() {
        return radium / 1.609344;
    }

    public void setRadiumMiles(double miles) {
        radium = miles * 1.609344;
    }

    public void setRadiumMiles(String radium) {
        if (radium != null) try {
            this.radium = Double.parseDouble(radium) * 1.609344;
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumKm(String radium) {
        setRadium(radium);
    }

    public void setRadium(String radium) {
        if (radium != null) try {
            this.radium = Double.parseDouble(radium);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadium(int radium) {
        this.radium = radium;
    }

    public void setRadiumKm(int radium) {
        setRadium(radium);
    }

    public void setTime(String date) {
        GregorianCalendar c1 = Dates.getGregorianCalendar(date);
        setTimeMiliseconds(c1.getTimeInMillis());
    }

    public double getNorthlimit() {
        return northlimit;
    }

    public void setNorthlimit(String northlimit) {
        if (northlimit != null) try {
            this.northlimit = Double.parseDouble(northlimit);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public double getSouthlimit() {
        return southlimit;
    }

    public void setSouthlimit(String southlimit) {
        if (southlimit != null) try {
            this.southlimit = Double.parseDouble(southlimit);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public double getEastlimit() {
        return eastlimit;
    }

    public void setEastlimit(String eastlimit) {
        if (eastlimit != null) try {
            this.eastlimit = Double.parseDouble(eastlimit);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setEastlimit(double eastlimit) {
        this.eastlimit = eastlimit;
    }

    public void setWestlimit(double westlimit) {
        this.westlimit = westlimit;
    }

    public void setNorthlimit(double northlimit) {
        this.northlimit = northlimit;
    }

    public void setSouthlimit(double southlimit) {
        this.southlimit = southlimit;
    }

    public double getWestlimit() {
        return westlimit;
    }

    public void setWestlimit(String westlimit) {
        if (westlimit != null) try {
            this.westlimit = Double.parseDouble(westlimit);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public boolean isTime() {
        return isTimePoint() || isDateInterval();
    }

    public boolean isSpatial() {
        return isSpatialBox() || isSpatialPoint();
    }

    public boolean isSpatialBox() {
        return northlimit != QueryParams.SPATIAL_INVALID_VALUE && southlimit != QueryParams.SPATIAL_INVALID_VALUE && eastlimit != QueryParams.SPATIAL_INVALID_VALUE && westlimit != QueryParams.SPATIAL_INVALID_VALUE;
    }

    public boolean isSpatialPoint() {
        return latitude != QueryParams.SPATIAL_INVALID_VALUE && longitude != QueryParams.SPATIAL_INVALID_VALUE;
    }

    public boolean isTimePoint() {
        return timeMiliseconds != -1;
    }

    public boolean isRadiumTime() {
        return radiumMiliseconds >= 0;
    }

    public boolean isRadium() {
        return radium >= 0;
    }

    public void setStartTime(String startDate) {
        GregorianCalendar c = Dates.getGregorianCalendar(startDate);
        setStartTimeMiliseconds(c.getTimeInMillis());
    }

    public void setEndTime(String endDate) {
        GregorianCalendar c = Dates.getGregorianCalendar(endDate);
        setEndTimeMiliseconds(c.getTimeInMillis());
    }

    public boolean isDateInterval() {
        return isDateIntervalMiliseconds();
    }

    public boolean isDateIntervalMiliseconds() {
        return startTimeMiliseconds != -1 && endTimeMiliseconds != -1;
    }

    public int getRadiumYears() {
        return (int) (radiumMiliseconds / 1000.0 / 60.0 / 60.0 / 24.0 / 364.0);
    }

    public int getRadiumMonths() {
        return (int) (radiumMiliseconds / 1000 / 60 / 60 / 24 / 364 * 12);
    }

    public int getRadiumDays() {
        return (int) (radiumMiliseconds / 1000 / 60 / 60 / 24);
    }

    public int getRadiumHours() {
        return (int) (radiumMiliseconds / 1000 / 60 / 60);
    }

    public long getRadiumMinutes() {
        return (int) (radiumMiliseconds / 1000 / 60);
    }

    public long getRadiumSeconds() {
        return radiumMiliseconds / 1000;
    }

    public long getRadiumMiliseconds() {
        return radiumMiliseconds;
    }

    public void setRadiumYears(String radiumYears) {
        if (radiumYears != null) try {
            setRadiumYears(Integer.parseInt(radiumYears));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumMonths(String radiumMonths) {
        if (radiumMonths != null) try {
            setRadiumMonths(Integer.parseInt(radiumMonths));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumDays(String radiumDays) {
        if (radiumDays != null) try {
            setRadiumDays(Integer.parseInt(radiumDays));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumHours(String radiumHours) {
        if (radiumHours != null) try {
            setRadiumHours(Integer.parseInt(radiumHours));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumMinutes(String radiumMinutes) {
        if (radiumMinutes != null) try {
            setRadiumMinutes(Integer.parseInt(radiumMinutes));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumSeconds(String radiumSeconds) {
        if (radiumSeconds != null) try {
            setRadiumSeconds(Integer.parseInt(radiumSeconds));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumMiliseconds(String radiumMiliseconds) {
        if (radiumMiliseconds != null) try {
            setRadiumMiliseconds(Integer.parseInt(radiumMiliseconds));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setRadiumYears(int radiumYears) {
        this.radiumMiliseconds = ((long) radiumYears) * 364 * 24 * 60 * 60 * 1000;
    }

    public void setRadiumMonths(int radiumMonths) {
        this.radiumMiliseconds = ((long) radiumMonths) * (364 / 12) * 24 * 60 * 60 * 1000;
    }

    public void setRadiumDays(int radiumDays) {
        this.radiumMiliseconds = ((long) radiumDays) * 24 * 60 * 60 * 1000;
    }

    public void setRadiumHours(int radiumHours) {
        this.radiumMiliseconds = ((long) radiumHours) * 60 * 60 * 1000;
    }

    public void setRadiumMinutes(int radiumMinutes) {
        this.radiumMiliseconds = ((long) radiumMinutes) * 60 * 1000;
    }

    public void setRadiumSeconds(int radiumSeconds) {
        this.radiumMiliseconds = ((long) radiumSeconds) * 1000;
    }

    public void setRadiumMiliseconds(long radiumMiliseconds) {
        this.radiumMiliseconds = radiumMiliseconds;
    }

    public void setTimeMiliseconds(String timeMiliseconds) {
        if (timeMiliseconds != null) try {
            setTimeMiliseconds(Long.parseLong(timeMiliseconds));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setStartTimeMiliseconds(String startTimeMiliseconds) {
        if (startTimeMiliseconds != null) try {
            setStartTimeMiliseconds(Long.parseLong(startTimeMiliseconds));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setEndTimeMiliseconds(String endTimeMiliseconds) {
        if (endTimeMiliseconds != null) try {
            setEndTimeMiliseconds(Long.parseLong(endTimeMiliseconds));
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void setTimeMiliseconds(long timeMiliseconds) {
        this.timeMiliseconds = timeMiliseconds;
    }

    public long getStartTimeMiliseconds() {
        return startTimeMiliseconds;
    }

    public void setStartTimeMiliseconds(long startTimeMiliseconds) {
        this.startTimeMiliseconds = startTimeMiliseconds;
    }

    public long getEndTimeMiliseconds() {
        return endTimeMiliseconds;
    }

    public void setEndTimeMiliseconds(long endTimeMiliseconds) {
        this.endTimeMiliseconds = endTimeMiliseconds;
    }

    public long getTimeMiliseconds() {
        return timeMiliseconds;
    }

    public void setQEEnum(QEEnum qeEnum) {
        this.qeEnum = qeEnum;
    }

    public void setQEEnum(String qe) {
        this.qeEnum = QEEnum.parse(qe);
    }

    public QEEnum getQeEnum() {
        return qeEnum;
    }

    public FilterEnum getFilter() {
        if (filter == null) {
            if (queryConfiguration != null) {
                filter = FilterEnum.parse(queryConfiguration.getProperty("lgte.default.filter"));
            }
            if (filter == null) filter = FilterEnum.parse(null);
        }
        return filter;
    }

    public void setFilter(FilterEnum filter) {
        this.filter = filter;
    }

    public void setFilter(String filter) {
        this.filter = FilterEnum.parse(filter);
    }

    public OrderEnum getOrder() {
        if (order == null) {
            if (queryConfiguration != null) {
                order = OrderEnum.parse(queryConfiguration.getProperty("lgte.default.order"));
            }
            if (order == null) order = OrderEnum.parse(null);
        }
        return order;
    }

    public void setOrder(OrderEnum order) {
        this.order = order;
    }

    public void setOrder(String order) {
        this.order = OrderEnum.parse(order);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void setModel(String model) {
        this.model = Model.parse(model);
    }

    public Map<String, Double> getExtraLongitudes() {
        return extraLongitudes;
    }

    public Map<String, Double> getExtraLatitudes() {
        return extraLatitudes;
    }

    public void addExtraLatitude(String name, String value) {
        if (value != null) try {
            double extraLatitude = Double.parseDouble(value);
            extraLatitudes.put(name, extraLatitude);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public void addExtraLongitude(String name, String value) {
        if (value != null) try {
            double extraLongitude = Double.parseDouble(value);
            extraLongitudes.put(name, extraLongitude);
        } catch (NumberFormatException e) {
            logger.error(e, e);
        }
    }

    public Map<String, Long> getExtraTimes() {
        return extraTimes;
    }

    public void addExtraTime(String name, String value) {
        GregorianCalendar c1 = Dates.getGregorianCalendar(value);
        addExtraTimeMiliseconds(name, c1.getTimeInMillis());
    }

    public void addExtraTimeMiliseconds(String name, long value) {
        extraTimes.put(name, value);
    }
}
