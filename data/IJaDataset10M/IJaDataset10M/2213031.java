package com.homeautomate.architecture;

import java.util.List;
import com.homeautomate.bean.IBean;

public interface IFloor extends IBean {

    void setHouse(IHouse house);

    IHouse getHouse();

    void setName(String name);

    String getName();

    List<IRoom> getRooms();

    void setRooms(List<IRoom> rooms);
}
