package net.sourceforge.etsysync.utils.etsy.api.resources;

import java.util.Date;
import java.util.Iterator;
import net.sourceforge.etsysync.utils.ResponsePattern;

public class ShopBanner extends EtsyObject {

    private Integer shopBannerId;

    private String hexCode;

    private Integer red;

    private Integer green;

    private Integer blue;

    private Integer hue;

    private Integer saturation;

    private Integer brightness;

    private Boolean isBlackAndWhite;

    private Date creationTsz;

    private Integer userId;

    public ShopBanner(Integer shopBannerId, String hexCode, Integer red, Integer green, Integer blue, Integer hue, Integer saturation, Integer brightness, Boolean isBlackAndWhite, Date creationTsz, Integer userId) {
        this.shopBannerId = shopBannerId;
        this.hexCode = hexCode;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
        this.isBlackAndWhite = isBlackAndWhite;
        this.creationTsz = creationTsz;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            ShopBanner operand = (ShopBanner) obj;
            if (shopBannerId.equals(operand.getShopBannerId()) && hexCode.equals(operand.getHexCode()) && red.equals(operand.getRed()) && green.equals(operand.getGreen()) && blue.equals(operand.getBlue()) && hue.equals(operand.getHue()) && saturation.equals(operand.getSaturation()) && brightness.equals(operand.getBrightness()) && isBlackAndWhite.equals(operand.getIsBlackAndWhite()) && creationTsz.equals(operand.getCreationTsz()) && userId.equals(operand.getUserId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return shopBannerId.hashCode() + hexCode.hashCode() + red.hashCode() + green.hashCode() + blue.hashCode() + hue.hashCode() + saturation.hashCode() + brightness.hashCode() + isBlackAndWhite.hashCode() + creationTsz.hashCode() + userId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("[shopBannerId:%s], [hexCode:%s], [red:%s], [green:%s], [blue:%s], [hue:%s], [saturation:%s], [brightness:%s], [isBlackAndWhite:%s], [creationTsz:%s], [userId:%s]", shopBannerId, hexCode, red, green, blue, hue, saturation, brightness, isBlackAndWhite, creationTsz, userId);
    }

    public static ResponsePattern getPattern() {
        ResponsePattern response = new ResponsePattern(ShopBanner.class.getSimpleName());
        response.add(ParameterKeys.shopBannerIdKey.getKey());
        response.add(ParameterKeys.hexCodeKey.getKey());
        response.add(ParameterKeys.redKey.getKey());
        response.add(ParameterKeys.greenKey.getKey());
        response.add(ParameterKeys.blueKey.getKey());
        response.add(ParameterKeys.hueKey.getKey());
        response.add(ParameterKeys.saturationKey.getKey());
        response.add(ParameterKeys.brightnessKey.getKey());
        response.add(ParameterKeys.isBlackAndWhiteKey.getKey());
        response.add(ParameterKeys.creationTszKey.getKey());
        response.add(ParameterKeys.userIdKey.getKey());
        return response;
    }

    public static ShopBanner create(ResponsePattern response) {
        Iterator<ResponsePattern> responseIterator = response.iterator();
        Integer shopBannerId = responseIterator.next().getInteger();
        String hexCode = responseIterator.next().getString();
        Integer red = responseIterator.next().getInteger();
        Integer green = responseIterator.next().getInteger();
        Integer blue = responseIterator.next().getInteger();
        Integer hue = responseIterator.next().getInteger();
        Integer saturation = responseIterator.next().getInteger();
        Integer brightness = responseIterator.next().getInteger();
        Boolean isBlackAndWhite = responseIterator.next().getBoolean();
        Date creationTsz = responseIterator.next().getDate();
        Integer userId = responseIterator.next().getInteger();
        return new ShopBanner(shopBannerId, hexCode, red, green, blue, hue, saturation, brightness, isBlackAndWhite, creationTsz, userId);
    }

    public Integer getShopBannerId() {
        return shopBannerId;
    }

    public String getHexCode() {
        return hexCode;
    }

    public Integer getRed() {
        return red;
    }

    public Integer getGreen() {
        return green;
    }

    public Integer getBlue() {
        return blue;
    }

    public Integer getHue() {
        return hue;
    }

    public Integer getSaturation() {
        return saturation;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public Boolean getIsBlackAndWhite() {
        return isBlackAndWhite;
    }

    public Date getCreationTsz() {
        return creationTsz;
    }

    public Integer getUserId() {
        return userId;
    }
}
