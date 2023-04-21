package ru.autofan.logic;

import ru.autofan.domain.Car;
import ru.autofan.view.CarBean;

public class CarWarpFactory implements WrapEntityFactory<Car, CarBean> {

    public CarBean wrap(Car car) {
        CarBean bean = new CarBean();
        bean.setDescription(car.getCarInfo().getDescription());
        bean.setPrice(car.getCarInfo().getPrice());
        return bean;
    }
}
