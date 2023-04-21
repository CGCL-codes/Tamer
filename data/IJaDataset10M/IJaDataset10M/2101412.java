package fastforward.wicket.test;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import fastforward.meta.bean.annotation.ChoicesSourceForProperty;
import fastforward.meta.bean.annotation.Description;
import fastforward.meta.bean.annotation.Editable;
import fastforward.meta.bean.annotation.FloatingPointDecimals;
import fastforward.meta.bean.annotation.Format;
import fastforward.meta.bean.annotation.Label;
import fastforward.meta.bean.annotation.Length;
import fastforward.meta.bean.annotation.LongRange;
import fastforward.meta.bean.annotation.MaxSize;
import fastforward.meta.bean.annotation.Position;
import fastforward.meta.bean.annotation.Property;
import fastforward.meta.bean.annotation.Required;
import fastforward.meta.bean.annotation.Visible;
import fastforward.wicket.renderer.annotation.ChoiceRenderer;
import fastforward.wicket.renderer.annotation.TableProperty;

public class Pojo implements Serializable {

    private long id;

    private String name;

    private int age;

    private Date birthday;

    private InnerPojo inner;

    private String phoneNumber;

    private String favoriteSport;

    private String favoriteNumber;

    private String favoriteCountry;

    private boolean isWorking;

    private CreditCardNumber[] creditCardNumbers;

    private File[] avatar;

    private double bankAccount;

    @FloatingPointDecimals(2)
    public double getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(double bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Visible(false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @MaxSize(2)
    public File[] getAvatar() {
        return avatar;
    }

    public void setAvatar(File[] avatar) {
        this.avatar = avatar;
    }

    @Position(1)
    @Format(type = fastforward.meta.bean.property.Format.FormatType.DATE, pattern = "MM/dd/yyyy")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Property
    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }

    @Label("{fr:Age, en:Age}")
    @Required(true)
    @LongRange(min = 0, max = 150)
    public int getAge() {
        return age;
    }

    @Property
    public String getFavoriteCountry() {
        return favoriteCountry;
    }

    @Editable(false)
    public String getFavoriteNumber() {
        return favoriteNumber;
    }

    @Property
    public String getFavoriteSport() {
        return favoriteSport;
    }

    @Property
    public InnerPojo getInner() {
        return inner;
    }

    @Required
    @Length(min = 0, max = 30)
    @Label("{fr:Nom, en:Name}")
    @Description("{fr:Le nom du Pojo, en:The name of the Pojo}")
    @TableProperty(colspan = 5)
    public String getName() {
        return name;
    }

    @Required
    @Position(10)
    @Format(type = fastforward.meta.bean.property.Format.FormatType.ALPHANUM_INPUT_MASK, pattern = "(999) 999-9999")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @ChoicesSourceForProperty("favoriteCountry")
    @ChoiceRenderer(CountryChoiceRenderer.class)
    public Set<String> getCountries() {
        HashSet<String> countries = new HashSet<String>();
        countries.add("Canada");
        countries.add("United States");
        return countries;
    }

    @Property
    public CreditCardNumber[] getCreditCardNumbers() {
        return creditCardNumbers;
    }

    public void setCreditCardNumbers(CreditCardNumber[] creditCardNumbers) {
        this.creditCardNumbers = creditCardNumbers;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setFavoriteCountry(String favoriteCountry) {
        this.favoriteCountry = favoriteCountry;
    }

    public void setFavoriteNumber(String favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public void setFavoriteSport(String favoriteSport) {
        this.favoriteSport = favoriteSport;
    }

    public void setInner(InnerPojo inner) {
        this.inner = inner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
