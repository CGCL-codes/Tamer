package com.mkk.kenji1016.web.dto;

import com.mkk.kenji1016.domain.User;
import com.mkk.kenji1016.util.ApplicationUtil;
import java.util.List;

/**
 * Define a 'option' html component  DTO.
 *
 * @author Shengzhao Li
 */
public class OptionDto {

    private String value;

    private String name;

    public OptionDto() {
    }

    public OptionDto(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<OptionDto> fromUsers(List<User> userList) {
        List<OptionDto> optionDtos = ApplicationUtil.createList();
        for (User user : userList) {
            OptionDto dto = new OptionDto(user.getUuid(), user.getUserName());
            optionDtos.add(dto);
        }
        return optionDtos;
    }
}
