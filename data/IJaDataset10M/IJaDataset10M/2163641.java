package com.techstar.dmis.helper.dto;

import com.techstar.dmis.dto.DdDoutageplanDto;

public class DayPlanWorkDto {

    private DdDoutageplanDto dto;

    private String earlyTime;

    public DdDoutageplanDto getDto() {
        return dto;
    }

    public void setDto(DdDoutageplanDto dto) {
        this.dto = dto;
    }

    public String getEarlyTime() {
        return earlyTime;
    }

    public void setEarlyTime(String earlyTime) {
        this.earlyTime = earlyTime;
    }
}
