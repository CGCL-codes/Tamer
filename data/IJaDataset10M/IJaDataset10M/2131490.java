package uturismu.bean;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class TourOperatorUpdate implements Serializable {

    private static final long serialVersionUID = -148746248426846536L;

    private String email;

    private String password;

    @NotEmpty
    private String vatNumber;

    @NotEmpty
    private String name;

    @NotEmpty
    private String holderName;

    @NotEmpty
    private String street;

    @NotEmpty
    private String zipCode;

    private Long city;

    public TourOperatorUpdate() {
    }

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

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getCity() {
        return city;
    }

    public void setCity(Long city) {
        this.city = city;
    }
}
