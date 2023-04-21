package free.david.weather;

import java.io.IOException;
import java.util.*;

/**
 * Fetches and decodes NOAA METAR weather conditions from
 * http://weather.noaa.gov/pub/data/observations/metar/stations/
 *
 */
public class MetarWeather extends Weather {

    private static Map descriptorCodes;

    private Map precipitationCodes;

    private Map obscurationCodes;

    private Map otherCodes;

    private Map cloudCodes;

    private MetarStationList metarList;

    private String locationBase;

    private String description = "";

    private List precipList = new Vector();

    private String obscuration = "";

    private String other = "";

    private String stationDate = "";

    private String stationTime = "";

    /** used to update barometer direction */
    private float lastBarometerReading = 0;

    private long lastBarometerDirectionUpdate = 0;

    private static final long barometerUpdatePeriod = 30 * 1000 * 60l;

    public MetarWeather() {
        super();
    }

    public MetarWeather(Properties specifics) {
        super(specifics);
        locationBase = specifics.getProperty("metarURL");
    }

    protected void initStage2() {
        setStateList(getAllStates());
        createCountryList();
        if (country.equals("US")) setCityList(new TreeSet((Collection) getStates().get(stateName))); else if (getCountries().get(country) != null) setCityList(new TreeSet((Collection) getCountries().get(country)));
    }

    private void createCountryList() {
        TreeMap cl = new TreeMap();
        for (Iterator names = getCountryCodes().keySet().iterator(); names.hasNext(); ) {
            String name = (String) names.next();
            cl.put(getCountryCodes().get(name), name);
        }
        setCountryList(cl);
    }

    private SortedMap getAllStates() {
        SortedMap allStates = new TreeMap();
        for (Iterator all = getStates().keySet().iterator(); all.hasNext(); ) allStates.put((String) all.next(), locationBase);
        return allStates;
    }

    protected String translateSky() {
        String ans = "";
        switch(clouds) {
            case 0:
                ans = "clear";
                break;
            case 1:
                ans = "no significant clouds";
                break;
            case 2:
                ans = "partly cloudy";
                break;
            case 3:
                ans = "cloudy";
                break;
            case 4:
                ans = "mostly cloudy";
                break;
            case 5:
                ans = "overcast";
                break;
            default:
                break;
        }
        return ans;
    }

    protected String translateBarometer(String code) {
        return code;
    }

    protected void update(String rawData) {
        System.out.println(timeStamp() + "Parsing data: " + rawData.replace('\n', '^'));
        setSky(0);
        setTemperature(-99);
        setDewpoint(-99);
        intensity = "";
        description = "";
        precipList.clear();
        obscuration = "";
        other = "";
        clouds = 0;
        setWindGusts(0);
        StringTokenizer tok = new StringTokenizer(rawData);
        setStationDate(next(tok));
        setStationTime(next(tok));
        String elem = next(tok);
        elem = next(tok);
        elem = next(tok);
        if (extractNumber(elem).equals("") && !elem.startsWith("VRB")) elem = next(tok);
        if (elem != null) {
            if (elem.substring(0, 3).equalsIgnoreCase("VRB")) setWindDirection(-1); else setWindDirection(Integer.parseInt(elem.substring(0, 3)));
            StringTokenizer windtok = new StringTokenizer(elem.substring(3), "G");
            if (windtok.hasMoreTokens()) setWindSpeed(Integer.parseInt(extractNumber(windtok.nextToken())));
            if (windtok.hasMoreTokens()) setWindGusts(Integer.parseInt(extractNumber(windtok.nextToken())));
        }
        elem = next(tok);
        if (elem != null && elem.indexOf("V") > 0 && !elem.equalsIgnoreCase("CAVOK")) elem = next(tok);
        if (elem != null && elem.equalsIgnoreCase("CAVOK")) {
            setSky(0);
            setVisibility(6);
            elem = next(tok);
        } else {
            if (elem.endsWith("SM") || getCountry().equals("US")) setVisibilityUnits("mile"); else setVisibilityUnits("meter");
            if (elem.indexOf("/") >= 0) elem = "0";
            setVisibility(Integer.parseInt(extractNumber(elem)));
            elem = next(tok);
            if (elem.indexOf("/") >= 0) {
                if (Integer.parseInt(extractNumber(elem.substring(elem.indexOf("/")))) > 1) setVisibility(getVisibility() + 1);
                elem = next(tok);
            }
            if (getVisibility() != 1) setVisibilityUnits(getVisibilityUnits() + "s");
            if (elem.startsWith("R") && elem.indexOf("/") > 0) elem = next(tok);
            while (elem != null && (elem.length() < 3 || getCloudCodes().get(elem.substring(0, 3)) == null) && elem.indexOf("/") < 0) {
                if (elem.startsWith("+") || elem.startsWith("-")) {
                    intensity = (String) getOtherCodes().get(elem.substring(0, 1));
                    elem = elem.substring(1);
                }
                while (elem.length() > 1) {
                    String decode = (String) getOtherCodes().get(elem);
                    if (decode != null) {
                        other = decode;
                        break;
                    }
                    String code = elem.substring(0, 2);
                    decode = (String) getDescriptorCodes().get(code);
                    if (decode != null) description = decode; else {
                        decode = (String) getPrecipitationCodes().get(code);
                        if (decode != null) getPrecipList().add(decode); else {
                            decode = (String) getObscurationCodes().get(code);
                            if (decode != null) obscuration = decode; else {
                                decode = (String) getOtherCodes().get(code);
                                if (decode != null) other = decode;
                            }
                        }
                    }
                    elem = elem.substring(2);
                }
                elem = next(tok);
            }
            while (elem != null && elem.indexOf("/") < 0 && !elem.startsWith("Q") && !elem.startsWith("A")) {
                String code = (String) getCloudCodes().get(elem.substring(0, 3));
                int cloudlevel = 0;
                if (isADecimalNumber(code)) {
                    cloudlevel = Integer.parseInt(code);
                    clouds = Math.max(cloudlevel, clouds);
                }
                elem = next(tok);
            }
        }
        if (elem != null && elem.indexOf("/") >= 0) {
            String temp = elem.substring(0, elem.indexOf("/")).replace('M', '-');
            String dew = elem.substring(temp.length() + 1).replace('M', '-');
            if (isADecimalNumber(temp)) setTemperature((int) (Float.parseFloat(temp) * (9f / 5f) + 32)); else setTemperature(-99);
            if (isADecimalNumber(dew)) setDewpoint((int) (Float.parseFloat(dew) * (9f / 5f) + 32)); else setDewpoint(-99);
        }
        if (tok.hasMoreTokens()) {
            if (!elem.startsWith("Q") && !elem.startsWith("A")) elem = next(tok);
            if (elem.startsWith("Q") || elem.startsWith("A")) {
                elem = extractNumber(elem);
                setBarometer(Float.parseFloat(elem) / 100);
                long now = Calendar.getInstance().getTimeInMillis();
                if (lastBarometerReading == 0) {
                    lastBarometerReading = getBarometer();
                    lastBarometerDirectionUpdate = now;
                    setBarometerDirection("");
                } else if (now - lastBarometerDirectionUpdate > barometerUpdatePeriod) {
                    if (lastBarometerReading < getBarometer()) setBarometerDirection("rising"); else if (lastBarometerReading > getBarometer()) setBarometerDirection("falling"); else setBarometerDirection("steady");
                    lastBarometerDirectionUpdate = now;
                    lastBarometerReading = getBarometer();
                }
            }
            setRemarks("");
            elem = next(tok);
            if (elem != null && elem.startsWith("RMK")) while (tok.hasMoreTokens()) setRemarks(getRemarks() + " " + next(tok));
        }
    }

    private String next(StringTokenizer tok) {
        if (!tok.hasMoreTokens()) return null; else return tok.nextToken();
    }

    protected SortedSet loadCityList(String notUsed) {
        SortedSet cities = new TreeSet() {

            public synchronized boolean add(Object o) {
                if (o instanceof String && ((String) o).trim().length() > 0) return super.add(o); else return false;
            }
        };
        for (Iterator all = ((List) getStates().get(getStateName())).iterator(); all.hasNext(); ) {
            cities.add(((MetarStation) all.next()).getStateCode());
        }
        return cities;
    }

    public String getDefaultININame() {
        return "METARdefaults.ini";
    }

    public static Map getDescriptorCodes() {
        if (descriptorCodes == null) {
            descriptorCodes = new HashMap();
            descriptorCodes.put("BL", "blowing");
            descriptorCodes.put("FZ", "freezing");
            descriptorCodes.put("DR", "lowdrifting");
            descriptorCodes.put("PR", "partial");
            descriptorCodes.put("BC", "patches");
            descriptorCodes.put("MI", "shallow");
            descriptorCodes.put("SH", "showers");
            descriptorCodes.put("TS", "thunderstorms");
        }
        return descriptorCodes;
    }

    public Map getPrecipitationCodes() {
        if (precipitationCodes == null) {
            precipitationCodes = new HashMap();
            precipitationCodes.put("DZ", "drizzle");
            precipitationCodes.put("GR", "hail");
            precipitationCodes.put("IC", "ice crystals");
            precipitationCodes.put("PL", "ice pellets");
            precipitationCodes.put("RA", "rain");
            precipitationCodes.put("GS", "small hail");
            precipitationCodes.put("SG", "snow grains");
            precipitationCodes.put("SN", "snow");
            precipitationCodes.put("UP", "unknown precip");
        }
        return precipitationCodes;
    }

    public Map getObscurationCodes() {
        if (obscurationCodes == null) {
            obscurationCodes = new HashMap();
            obscurationCodes.put("FG", "fog");
            obscurationCodes.put("HZ", "haze");
            obscurationCodes.put("BR", "mist");
            obscurationCodes.put("SA", "sand");
            obscurationCodes.put("FU", "smoke");
            obscurationCodes.put("PY", "spray");
            obscurationCodes.put("VA", "volcanic ash");
            obscurationCodes.put("DU", "widespread dust");
        }
        return obscurationCodes;
    }

    public Map getCloudCodes() {
        if (cloudCodes == null) {
            cloudCodes = new HashMap();
            cloudCodes.put("SKC", "0");
            cloudCodes.put("NSC", "1");
            cloudCodes.put("FEW", "2");
            cloudCodes.put("SCT", "3");
            cloudCodes.put("BKN", "4");
            cloudCodes.put("OVC", "5");
        }
        return cloudCodes;
    }

    public Map getOtherCodes() {
        if (otherCodes == null) {
            otherCodes = new HashMap();
            otherCodes.put("AUTO", "automated report");
            otherCodes.put("CLR", "clear");
            otherCodes.put("COR", "corrected report");
            otherCodes.put("CB", "cumulonimbus");
            otherCodes.put("PO", "dust sand whirls");
            otherCodes.put("DS", "dust storm");
            otherCodes.put("FC", "funnel cloud");
            otherCodes.put("+", "heavy");
            otherCodes.put("-", "light");
            otherCodes.put("NOSIG", "no significant change");
            otherCodes.put("RMK", "remarks");
            otherCodes.put("SS", "sand storm");
            otherCodes.put("SQ", "squalls");
            otherCodes.put("TCU", "towering cumulus");
            otherCodes.put("VV", "vertical visibility");
        }
        return otherCodes;
    }

    public String getStationURL() {
        return locationBase + getCities().get(getCity()) + ".TXT";
    }

    public Map getCities() {
        return getMetarList().getCities();
    }

    public Map getCountries() {
        return getMetarList().getCountries();
    }

    public Map getCountryCodes() {
        return getMetarList().getCountryCodes();
    }

    public Map getStates() {
        return getMetarList().getStates();
    }

    public Map getStations() {
        return getMetarList().getStations();
    }

    public MetarStationList getMetarList() {
        if (metarList == null) metarList = new MetarStationList();
        return metarList;
    }

    public void setStateName(String stateName) throws IOException {
        if (stateName == null || stateName.equals(getStateName())) return;
        this.stateName = stateName;
        setCity((String) ((List) getStates().get(stateName)).get(0));
        setCityList(new TreeSet((Collection) getStates().get(stateName)));
    }

    public void setCountry(String country) throws IOException {
        if (country == null || country.equals(getCountry())) return;
        this.country = country;
        if (country.equals("US")) setStateName((String) getStateList().firstKey()); else {
            setCity((String) ((List) getCountries().get(country)).get(0));
            setCityList(new TreeSet((Collection) getCountries().get(country)));
        }
        setBarometerDirection("");
    }

    public String getCloudCover() {
        return null;
    }

    public float assessSky() {
        return 1f / (clouds + 1f);
    }

    public int assessRain() {
        int s = 0;
        if (getPrecipList().contains((String) getPrecipitationCodes().get("RA"))) s = 10; else if (getDescription().indexOf((String) getDescriptorCodes().get("TS")) >= 0) s = 10; else if (getPrecipList().contains((String) getPrecipitationCodes().get("DZ"))) s = 5;
        if (getOtherCodes().get("+").equals(intensity)) s *= 2; else if (getOtherCodes().get("-").equals(intensity)) s /= 2;
        return s;
    }

    public int assessLightning() {
        int s = 0;
        if (getDescription().indexOf((String) getDescriptorCodes().get("TS")) >= 0) s = 2;
        if (getOtherCodes().get("+").equals(intensity)) s *= 2; else if (getOtherCodes().get("-").equals(intensity)) s /= 2;
        return s;
    }

    public int assessFog() {
        int s = 0;
        if (getObscuration().indexOf((String) getObscurationCodes().get("FG")) >= 0) s = 4; else if (getObscuration().indexOf((String) getObscurationCodes().get("BR")) >= 0) s = 3; else if (getObscuration().indexOf((String) getObscurationCodes().get("HZ")) >= 0) s = 2;
        if (getOtherCodes().get("+").equals(intensity)) s *= 2; else if (getOtherCodes().get("-").equals(intensity)) s /= 2;
        return s;
    }

    public int assessSnow() {
        int s = 0;
        if (getPrecipList().contains((String) getPrecipitationCodes().get("SN"))) s = 10; else if (getPrecipList().contains((String) getPrecipitationCodes().get("SG"))) s = 6; else if (getPrecipList().contains((String) getPrecipitationCodes().get("IC"))) s = 4;
        if (getOtherCodes().get("+").equals(intensity)) s *= 2; else if (getOtherCodes().get("-").equals(intensity)) s /= 2;
        return s;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getObscuration() {
        return obscuration;
    }

    public void setObscuration(String obscuration) {
        this.obscuration = obscuration;
    }

    public String getPrecipitation() {
        StringBuffer wet = new StringBuffer();
        wet.append(getIntensity()).append(" ");
        wet.append(getDescription()).append(" ");
        for (Iterator p = getPrecipList().iterator(); p.hasNext(); ) {
            wet.append(p.next()).append(" ");
            if (p.hasNext()) wet.append(" and ");
        }
        if (getObscuration().length() > 0) {
            if (wet.toString().trim().length() > 0) wet.append(" and ");
            wet.append(getObscuration());
        }
        return wet.toString().trim();
    }

    public List getPrecipList() {
        return precipList;
    }

    public void setPrecipList(List precipList) {
        this.precipList = precipList;
    }

    protected String getStationDate() {
        return stationDate;
    }

    protected void setStationDate(String stationDate) {
        this.stationDate = stationDate;
    }

    protected String getStationTime() {
        return stationTime;
    }

    protected void setStationTime(String stationTime) {
        this.stationTime = stationTime;
    }

    public String getStationTimestamp() {
        Calendar c = Calendar.getInstance();
        int localOffset = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET);
        int y = 0, mon = 0, d = 0, h = 0, min = 0;
        StringTokenizer tok = new StringTokenizer(getStationDate(), "/");
        if (tok.hasMoreTokens()) y = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens()) mon = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens()) d = Integer.parseInt(tok.nextToken());
        tok = new StringTokenizer(getStationTime(), ":");
        if (tok.hasMoreTokens()) h = Integer.parseInt(tok.nextToken());
        if (tok.hasMoreTokens()) min = Integer.parseInt(tok.nextToken());
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.clear();
        c.set(y, mon, d, h, min);
        c.add(Calendar.MILLISECOND, localOffset);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        if (hour > 12) hour -= 12;
        return (c.get(Calendar.MONTH)) + "/" + c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR) + " at " + hour + ":" + c.get(Calendar.MINUTE) + (c.get(Calendar.HOUR_OF_DAY) > 11 ? " pm" : " am");
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
