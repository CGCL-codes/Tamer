package com.mkk.kenji1016.domain;

/**
 * User: mkk
 * Date: 11-7-15
 * Time: 下午9:20
 */
public class UserDetail extends Domain {

    private String email;

    private String city;

    private String phone;

    private String address;

    public UserDetail() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
