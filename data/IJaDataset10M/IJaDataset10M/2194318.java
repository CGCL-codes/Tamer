package weather_service.weatherprovider.weatherdatamodel;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Value Object bean for the Wind conditions.
 * <p/>
 */
public class Wind {

    private String direction;

    private String degrees;

    private String gust;

    private String speed;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String description) {
        this.direction = description;
    }

    public String getDegrees() {
        return degrees;
    }

    public void setDegrees(String direction) {
        this.degrees = direction;
    }

    public String getGust() {
        return gust;
    }

    public void setGust(String gust) {
        this.gust = gust;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
