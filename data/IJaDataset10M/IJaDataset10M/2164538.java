package cz.kibo.ekonom.model;

import cz.kibo.ekonom.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

/**
 *
 * @author tomas
 */
public class School implements UserDetails, Serializable {

    private Integer id;

    private String name;

    private String street;

    private String city;

    private String zipcode;

    private String phone;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String role;

    private List<Testset> allSchoolSharedTestsets = new ArrayList();

    private String url;

    /**
     * @return testsets from table TESTSET where TESTSET.OWNER = this.ID
     */
    public List<Testset> getAllSchoolOwnedTestsets() {
        List<Testset> ownTestsets = new ArrayList();
        for (Testset testset : getAllSchoolSharedTestsets()) {
            if (testset.getOwner().getId().equals(getId())) {
                ownTestsets.add(testset);
            }
        }
        return ownTestsets;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setUrl(Utils.correctTextToAscii(name));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * @return relation from table SCHOOL_X_QUESTION
     */
    public List<Testset> getAllSchoolSharedTestsets() {
        return allSchoolSharedTestsets;
    }

    public void setAllSchoolSharedTestsets(List<Testset> testsets) {
        this.allSchoolSharedTestsets = testsets;
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
        authList.add(new GrantedAuthorityImpl(role));
        return authList.toArray(new GrantedAuthority[] {});
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final School other = (School) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.url == null) ? (other.url != null) : !this.url.equals(other.url)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.email != null ? this.email.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Testset id#: " + id + " name: " + name;
    }
}
