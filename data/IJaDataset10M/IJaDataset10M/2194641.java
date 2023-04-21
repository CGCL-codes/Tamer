package net.sourceforge.javo2.example.valueobjects;

import net.sourceforge.javo2.api.IValueObject;

public interface IVeryDetailedAddressVO extends IValueObject {

    public String getName();

    public void setName(String name);

    public String getStreet();

    public void setStreet(String street);

    public Integer getNumber();

    public void setNumber(Integer number);

    public Integer getFloor();

    public void setFloor(Integer floor);

    public Boolean getIsAppartment();

    public void setIsAppartment(Boolean isAppartment);

    public String getApartment();

    public void setApartment(String apartment);

    public ICityVO getCity();

    public void setCity(ICityVO city);
}
