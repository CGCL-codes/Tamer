package com.islewars.client.dto;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.islewars.client.TankType;
import com.islewars.client.Vector;

public class PlayerDto implements IsSerializable {

    public String name;

    public List<TankDto> tankDtos;

    public PlayerDto() {
    }

    public PlayerDto(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return name.equals(((PlayerDto) o).getName());
    }

    public String getName() {
        return name;
    }

    public void setup() {
        tankDtos = new ArrayList<TankDto>();
        TankDto tankDto = new TankDto(TankType.MARK_5, new Vector(100, 100));
        tankDtos.add(tankDto);
    }
}
