package storage.statistics;

public class StatisticType {

    public static final StatisticType MIN_TEMP = new StatisticType("minTemp");

    public static final StatisticType MAX_TEMP = new StatisticType("maxTemp");

    public static final StatisticType MEAN_TEMP_REGION = new StatisticType("meanTempRegion");

    public static final StatisticType MEAN_TEMP_TIME = new StatisticType("meanTempTime");

    private String type;

    private StatisticType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return type;
    }
}
