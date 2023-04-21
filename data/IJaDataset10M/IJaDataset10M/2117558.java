package com.shyfish.model;

import com.shyfish.model.enums.CurrencyType;
import com.shyfish.model.enums.Gender;
import com.shyfish.model.enums.Language;
import com.shyfish.model.enums.Location;

public class User extends AbstractModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String email;

    private String password;

    private String name;

    private Gender gender;

    private Integer age;

    private String timeZone;

    private Location location;

    private Language language;

    private CurrencyType defaultCurrencyType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        if (gender != null) {
            return gender.name();
        }
        return null;
    }

    public Gender getGenderStr() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLanguage() {
        if (language != null) {
            return language.name();
        }
        return null;
    }

    public Language getLanguageStr() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getLocation() {
        if (location != null) {
            return location.name();
        }
        return null;
    }

    public Location getLocationStr() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDefaultCurrencyType() {
        if (defaultCurrencyType != null) {
            return defaultCurrencyType.name();
        }
        return null;
    }

    public CurrencyType getDefaultCurrencyTypeStr() {
        return defaultCurrencyType;
    }

    public void setDefaultCurrencyType(CurrencyType defaultCurrencyType) {
        this.defaultCurrencyType = defaultCurrencyType;
    }
}
