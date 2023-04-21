package org.silicolife.bonzai.model.environment;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;

public class Weather {

    private int cloud;

    private int humidity;

    private int temperature;

    private int wind;

    private int precipitation;

    private int pollution;

    public static int CLEAR = 0;

    public static int MIXED_CLOUD = 1;

    public static int OVERCAST = 2;

    public static String CLOUD_KEY = "Cloud";

    public static String[] CLOUD_VALUES = new String[] { "Clear", "MixedCloud", "Overcast" };

    public static int DRY = 0;

    public static int MOIST = 1;

    public static int DAMP = 2;

    public static int WET = 3;

    public static String HUMIDITY_KEY = "Humidity";

    public static String[] HUMIDITY_VALUES = new String[] { "Dry", "Moist", "Damp", "Wet" };

    public static int FREEZING = 0;

    public static int COLD = 1;

    public static int MILD = 2;

    public static int WARM = 3;

    public static int HOT = 4;

    public static String TEMPERATURE_KEY = "Temperature";

    public static String[] TEMPERATURE_VALUES = new String[] { "Freezing", "Cold", "Mild", "Warm", "Hot" };

    public static int STILL = 0;

    public static int BREEZY = 1;

    public static int WINDY = 2;

    public static int GALE = 3;

    public static String WIND_KEY = "Wind";

    public static String[] WIND_VALUES = new String[] { "Still", "Breezy", "Windy", "Gale" };

    public static int PRECIPITATION_NONE = 0;

    public static int PRECIPITATION_LIGHT = 1;

    public static int PRECIPITATION_MEDIUM = 2;

    public static int PRECIPITATION_HEAVY = 3;

    public static String PRECIPITATION_KEY = "Precipitation";

    public static String[] PRECIPITATION_VALUES = new String[] { "None", "Light", "Medium", "Heavy" };

    public static int CLEAN = 0;

    public static int DIRTY = 1;

    public static int POISONOUS = 2;

    public static String pollution_KEY = "pollution";

    public static String[] pollution_VALUES = new String[] { "Clean", "Dirty", "Poisonous" };

    public static String WEATHER_KEY = "Weather";

    public void increaseCloud() {
        if (cloud < OVERCAST) {
            cloud++;
        }
    }

    public void humidify() {
        if (humidity < WET) {
            humidity++;
        }
    }

    public void heat() {
        if (temperature < HOT) {
            temperature++;
        }
    }

    public void increaseWind() {
        if (wind < GALE) {
            wind++;
        }
    }

    public void increasePrecipitation() {
        if (precipitation < PRECIPITATION_HEAVY) {
            precipitation++;
        }
    }

    public void polute() {
        if (pollution < POISONOUS) {
            pollution++;
        }
    }

    public void decreaseCloud() {
        if (cloud > CLEAR) {
            cloud--;
        }
    }

    public void dehumidify() {
        if (humidity > DRY) {
            humidity--;
        }
    }

    public void cool() {
        if (temperature > FREEZING) {
            temperature--;
        }
    }

    public void decreaseWind() {
        if (wind > STILL) {
            wind--;
        }
    }

    public void decreasePrecipitation() {
        if (precipitation > PRECIPITATION_NONE) {
            precipitation--;
        }
    }

    public void cleanUp() {
        if (pollution > CLEAN) {
            pollution--;
        }
    }

    public void setClear() {
        cloud = CLEAR;
    }

    public void setMixedCloud() {
        cloud = MIXED_CLOUD;
    }

    public void setOvercast() {
        cloud = OVERCAST;
    }

    public boolean isClear() {
        return cloud == CLEAR;
    }

    public boolean isMixedCloud() {
        return cloud == MIXED_CLOUD;
    }

    public boolean isOvercast() {
        return cloud == OVERCAST;
    }

    public int getCloud() {
        return cloud;
    }

    public void setDry() {
        humidity = DRY;
    }

    public void setMoist() {
        humidity = MOIST;
    }

    public void setDamp() {
        humidity = DAMP;
    }

    public void setWet() {
        humidity = WET;
    }

    public boolean isDry() {
        return humidity == DRY;
    }

    public boolean isMoist() {
        return humidity == MOIST;
    }

    public boolean isDamp() {
        return humidity == DAMP;
    }

    public boolean isWet() {
        return humidity == WET;
    }

    public int getHumidity() {
        return humidity;
    }

    public boolean isFreezing() {
        return temperature == FREEZING;
    }

    public boolean isCold() {
        return temperature == COLD;
    }

    public boolean isMild() {
        return temperature == MILD;
    }

    public boolean isWarm() {
        return temperature == WARM;
    }

    public boolean isHot() {
        return temperature == HOT;
    }

    public void setFreezing() {
        temperature = FREEZING;
    }

    public void setCold() {
        temperature = COLD;
    }

    public void setMild() {
        temperature = MILD;
    }

    public void setWarm() {
        temperature = WARM;
    }

    public void setHot() {
        temperature = HOT;
    }

    public int getTemperature() {
        return temperature;
    }

    public boolean isStill() {
        return wind == STILL;
    }

    public boolean isBreezy() {
        return wind == BREEZY;
    }

    public boolean isWindy() {
        return wind == WINDY;
    }

    public boolean isGale() {
        return wind == GALE;
    }

    public void setStill() {
        wind = STILL;
    }

    public void setBreezy() {
        wind = BREEZY;
    }

    public void setWindy() {
        wind = WINDY;
    }

    public void setGale() {
        wind = GALE;
    }

    public int getWind() {
        return wind;
    }

    public boolean isPrecipitationNone() {
        return precipitation == PRECIPITATION_NONE;
    }

    public boolean isPrecipitationLight() {
        return precipitation == PRECIPITATION_LIGHT;
    }

    public boolean isPrecipitationMedium() {
        return precipitation == PRECIPITATION_MEDIUM;
    }

    public boolean isPrecipitationHeavy() {
        return precipitation == PRECIPITATION_HEAVY;
    }

    public void setPrecipitationNone() {
        precipitation = PRECIPITATION_NONE;
    }

    public void setPrecipitationLight() {
        precipitation = PRECIPITATION_LIGHT;
    }

    public void setPrecipitationMedium() {
        precipitation = PRECIPITATION_MEDIUM;
    }

    public void setPrecipitationHeavy() {
        precipitation = PRECIPITATION_HEAVY;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public boolean isClean() {
        return pollution == CLEAN;
    }

    public boolean isDirty() {
        return pollution == DIRTY;
    }

    public boolean isPoisonous() {
        return pollution == POISONOUS;
    }

    public void setClean() {
        pollution = CLEAN;
    }

    public void setDirty() {
        pollution = DIRTY;
    }

    public void setPoisonous() {
        pollution = POISONOUS;
    }

    public int getPollution() {
        return pollution;
    }

    public String toString() {
        try {
            return toXML();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String toXML() throws JSONException {
        JSONObject weatherJson = toJSON();
        weatherJson.put("tagName", WEATHER_KEY);
        return JSONML.toString(weatherJson);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject tmpJson = new JSONObject();
        tmpJson.put(CLOUD_KEY, CLOUD_VALUES[cloud]);
        tmpJson.put(HUMIDITY_KEY, HUMIDITY_VALUES[humidity]);
        tmpJson.put(TEMPERATURE_KEY, TEMPERATURE_VALUES[temperature]);
        tmpJson.put(WIND_KEY, WIND_VALUES[wind]);
        tmpJson.put(PRECIPITATION_KEY, PRECIPITATION_VALUES[precipitation]);
        tmpJson.put(pollution_KEY, pollution_VALUES[pollution]);
        return tmpJson;
    }
}
