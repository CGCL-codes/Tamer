package net.opensesam.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.*;

/**
 * The persistent class for the password_policy database table.
 * 
 */
@Entity
@Table(name = "password_policy")
public class PasswordPolicy implements Serializable {

    private static final long serialVersionUID = 4017064433379257104L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String description;

    @Column(name = "maximum_age")
    private int maximumAge;

    @Column(name = "maximum_length")
    private int maximumLength;

    @Column(name = "minimum_length")
    private int minimumLength;

    @Column(name = "minimum_lowercase")
    private int minimumLowercase;

    @Column(name = "minimum_non_alphanumeric")
    private int minimumNonAlphanumeric;

    @Column(name = "minimum_digits")
    private int minimumDigits;

    @Column(name = "minimum_uppercase")
    private int minimumUppercase;

    private String name;

    @Column(name = "not_allowed_characters")
    private String notAllowedCharacters;

    @Column(name = "repeats_check_number")
    private int repeatsCheckNumber;

    @Column(name = "starting_with_alphabet")
    private byte startingWithAlphabet;

    @Column(name = "user_profile_check")
    private byte userProfileCheck;

    @Column(name = "maximum_consecutive")
    private int maximumConsecutive;

    @OneToMany(mappedBy = "passwordPolicy")
    private Set<Resource> resources;

    public PasswordPolicy() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaximumAge() {
        return this.maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        this.maximumAge = maximumAge;
    }

    public int getMaximumLength() {
        return this.maximumLength;
    }

    public void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    public int getMinimumLength() {
        return this.minimumLength;
    }

    public void setMinimumLength(int minimumLength) {
        this.minimumLength = minimumLength;
    }

    public int getMinimumLowercase() {
        return this.minimumLowercase;
    }

    public void setMinimumLowercase(int minimumLowercase) {
        this.minimumLowercase = minimumLowercase;
    }

    public int getMinimumNonAlphanumeric() {
        return this.minimumNonAlphanumeric;
    }

    public void setMinimumNonAlphanumeric(int minimumNonAlphanumeric) {
        this.minimumNonAlphanumeric = minimumNonAlphanumeric;
    }

    public int getMinimumDigits() {
        return this.minimumDigits;
    }

    public void setMinimumDigits(int minimumDigits) {
        this.minimumDigits = minimumDigits;
    }

    public int getMinimumUppercase() {
        return this.minimumUppercase;
    }

    public void setMinimumUppercase(int minimumUppercase) {
        this.minimumUppercase = minimumUppercase;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotAllowedCharacters() {
        return this.notAllowedCharacters;
    }

    public void setNotAllowedCharacters(String notAllowedCharacters) {
        this.notAllowedCharacters = notAllowedCharacters;
    }

    public int getRepeatsCheckNumber() {
        return this.repeatsCheckNumber;
    }

    public void setRepeatsCheckNumber(int repeatsCheckNumber) {
        this.repeatsCheckNumber = repeatsCheckNumber;
    }

    public byte getStartingWithAlphabet() {
        return this.startingWithAlphabet;
    }

    public void setStartingWithAlphabet(byte startingWithAlphabet) {
        this.startingWithAlphabet = startingWithAlphabet;
    }

    public byte getUserProfileCheck() {
        return this.userProfileCheck;
    }

    public void setUserProfileCheck(byte userProfileCheck) {
        this.userProfileCheck = userProfileCheck;
    }

    public Set<Resource> getResources() {
        return this.resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public void setMaximumConsecutive(int maximumConsecutive) {
        this.maximumConsecutive = maximumConsecutive;
    }

    public int getMaximumConsecutive() {
        return maximumConsecutive;
    }

    @Override
    public String toString() {
        return "Password Policy [ id = " + id + ", description = " + description + ", maximumAge = " + maximumAge + ", maximumLength = " + maximumLength + ", minimumLength = " + minimumLength + ", minimumLowercase = " + minimumLowercase + ",minimumNonAlphanumeric = " + minimumNonAlphanumeric + ", minimumDigits = " + minimumDigits + ", minimumUppercase = " + minimumUppercase + ", name = " + name + ", notAllowedCharacters = " + notAllowedCharacters + ", repeatsCheckNumber = " + repeatsCheckNumber + ", startingWithAlphabet = " + startingWithAlphabet + ", userProfileCheck = " + userProfileCheck + ", maximumConsecutive = " + maximumConsecutive + " ]";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", getId());
        map.put("maximumAge", getMaximumAge());
        map.put("maximumConsecutive", getMaximumConsecutive());
        map.put("maximumLength", getMaximumLength());
        map.put("minimumDigits", getMinimumDigits());
        map.put("minimumLength", getMinimumLength());
        map.put("minimumLowercase", getMinimumLowercase());
        map.put("minimumNonAlphanumeric", getMinimumNonAlphanumeric());
        map.put("minimumUppercase", getMinimumUppercase());
        map.put("name", getName());
        map.put("notAllowedCharacters", getNotAllowedCharacters());
        map.put("repeatsCheckNumber", getRepeatsCheckNumber());
        map.put("resources", getResources());
        map.put("startingWithAlphabet", getStartingWithAlphabet());
        map.put("userProfileCheck", getUserProfileCheck());
        return map;
    }
}
