package org.plazmaforge.bsolution.carservice.common.beans;

/**
 * 
 * @author Oleh Hapon
 * $Id: CarInsurerPaintBoxPrice.java,v 1.2 2010/04/28 06:22:48 ohapon Exp $
 */
public class CarInsurerPaintBoxPrice extends CarInsurerContractItem {

    private CarPaintDetailType carPaintDetailType;

    private int ageFrom;

    private int ageTo;

    private float price;

    public CarPaintDetailType getCarPaintDetailType() {
        return carPaintDetailType;
    }

    public void setCarPaintDetailType(CarPaintDetailType carPaintDetailType) {
        this.carPaintDetailType = carPaintDetailType;
    }

    public String getCarPaintDetailTypeName() {
        return carPaintDetailType == null ? null : carPaintDetailType.getName();
    }

    public int getAgeFrom() {
        return ageFrom;
    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }

    public int getAgeTo() {
        return ageTo;
    }

    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
