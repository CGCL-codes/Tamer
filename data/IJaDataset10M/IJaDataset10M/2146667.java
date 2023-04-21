package com.imoresoft.magic.app.weather;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ambitor.grass.sql.dao.SuperDao;
import com.ambitor.grass.sql.dao.impl.SuperDaoImpl;
import com.ambitor.grass.util.data.DatasetList;
import com.ambitor.grass.util.data.IData;
import com.ambitor.grass.util.data.IDataset;
import com.imoresoft.magic.util.DateUtil;
import com.imoresoft.magic.util.LocalConnectionUtils;

/**
 * @author bugxiaoya
 * @since 2011年3月31日
 * @version 1.0
 * 
 * 天气同步器
 */
public class WeatherSync extends TimerTask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WeatherSync() {
    }

    @Override
    public void run() {
        Connection conn = null;
        try {
            conn = LocalConnectionUtils.getConnection();
        } catch (Exception e) {
            logger.error("获取数据库链接发生错误", e);
            return;
        }
        SuperDao superDao = new SuperDaoImpl(conn);
        for (int i = 0; i < 3; i++) {
            try {
                sync(superDao);
            } catch (Exception e) {
                logger.warn("同步天气过程中发生错误", e);
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
        }
    }

    @SuppressWarnings("unchecked")
    public void sync(SuperDao superDao) {
        String tableName = "TF_M_CITY_WEATHER";
        IDataset cities = new DatasetList();
        try {
            cities = superDao.executeBaseOnSqlSql(tableName, "SELECT_CITYS_TO_UPDATE");
        } catch (Exception e) {
        }
        for (int i = 0; i < cities.size(); i++) {
            IData weatherData = null;
            try {
                weatherData = cities.getData(i);
                String cityId = weatherData.getString("CITY_ID");
                updateWeatherData(cityId, weatherData);
                superDao.update(tableName, weatherData);
            } catch (Exception e) {
                String cityName = weatherData.getString("CITY_NAME");
                logger.warn("获取" + cityName + "最新天气信息发生错误", e);
                continue;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void updateWeatherData(String cityId, IData weatherData) throws Exception {
        Weather weather = getWeatherByCityId(cityId);
        Integer temp1Low = weather.getTempArray1()[0];
        Integer temp2Low = weather.getTempArray2()[0];
        Integer temp3Low = weather.getTempArray3()[0];
        Integer temp4Low = weather.getTempArray4()[0];
        Integer temp5Low = weather.getTempArray5()[0];
        Integer temp1High = weather.getTempArray1()[1];
        Integer temp2High = weather.getTempArray2()[1];
        Integer temp3High = weather.getTempArray3()[1];
        Integer temp4High = weather.getTempArray4()[1];
        Integer temp5High = weather.getTempArray5()[1];
        weatherData.put("TEMP1_LOW", temp1Low);
        weatherData.put("TEMP2_LOW", temp2Low);
        weatherData.put("TEMP3_LOW", temp3Low);
        weatherData.put("TEMP4_LOW", temp4Low);
        weatherData.put("TEMP5_LOW", temp5Low);
        weatherData.put("TEMP1_HIGH", temp1High);
        weatherData.put("TEMP2_HIGH", temp2High);
        weatherData.put("TEMP3_HIGH", temp3High);
        weatherData.put("TEMP4_HIGH", temp4High);
        weatherData.put("TEMP5_HIGH", temp5High);
        Integer avgTempToday = weather.getAvgTemp(temp1Low, temp1High);
        Integer avgTempTomrrow = weather.getAvgTemp(temp2Low, temp2High);
        Integer avgTempIn5Days = weather.getAvgTemp(temp1Low, temp1High, temp1Low, temp1High, temp2Low, temp2High, temp3Low, temp3High, temp5Low, temp5High);
        weatherData.put("AVG_TEMP_TODAY", avgTempToday);
        weatherData.put("AVG_TEMP_TOMORROW", avgTempTomrrow);
        weatherData.put("AVG_TEMP_IN_5DAYS", avgTempIn5Days);
        weatherData.put("WEATHER1", weather.getWeather1());
        weatherData.put("WEATHER2", weather.getWeather2());
        weatherData.put("WIND1", weather.getWind1());
        weatherData.put("WIND2", weather.getWind2());
        weatherData.put("IMG1", weather.getImg1());
        weatherData.put("IMG2", weather.getImg2());
        weatherData.put("IMG3", weather.getImg3());
        weatherData.put("IMG4", weather.getImg4());
        Map<String, Index> indexMap = WeatherUtil.getIndexes(cityId);
        for (String indexId : indexMap.keySet()) {
            weatherData.put(indexId, indexMap.get(indexId).getContent());
        }
        weatherData.put("LAST_UPDATE_TIME", DateUtil.formatDate(new Date()));
    }

    private Weather getWeatherByCityId(String cityId) throws Exception {
        IData data = WeatherUtil.getWeather(cityId);
        Weather weather = new Weather(data);
        return weather;
    }

    public static void main(String[] args) throws Exception {
    }
}
