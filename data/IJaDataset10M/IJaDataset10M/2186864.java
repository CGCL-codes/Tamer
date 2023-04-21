package me.yonatan.hy.entity;

import java.security.SecureRandom;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import com.googlecode.objectify.annotation.Indexed;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, exclude = { "password", "salt" })
public class Customer extends Entity {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private Date DOB;

    public void setDOB(Date dob) {
        this.DOB = fixDateOnly(dob);
        if (DOB == null) return;
        yearOfBirth = new DateTime(dob).getYear();
    }

    private Integer yearOfBirth;

    public void setYearOfBirth(Integer year) {
        this.yearOfBirth = year;
        if (yearOfBirth == null || this.DOB == null) return;
        this.DOB = new DateTime(DOB).year().setCopy(year).toDate();
    }

    public Integer getYearOfBirth() {
        if (this.yearOfBirth != null) return yearOfBirth;
        if (this.DOB != null) return new DateTime(this.DOB).getYear();
        return null;
    }

    @Indexed
    private String origin;

    @Indexed
    private String category;

    private String comment;

    private String phone;

    private Gender gender;

    private FamilyStatusEnum familyStatus;

    private DayOfWeek preferredDow;

    private WeekOfMonth preferredWom;

    @Getter(AccessLevel.NONE)
    private String password;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private byte[] salt;

    private String getHashedPassword(byte[] salt, String password) {
        try {
            SecretKey secretKey = new SecretKeySpec(salt, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            mac.update(password.getBytes());
            byte[] passwordBytes = mac.doFinal();
            return new String(Base64.encodeBase64(passwordBytes));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void setPassword(String password) {
        this.salt = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);
        this.password = getHashedPassword(salt, password);
    }

    public boolean verifyPassword(String passwordToCheck) {
        String hashed = getHashedPassword(salt, passwordToCheck);
        return hashed.equals(this.password);
    }

    public Date suggestNextInvitation(Date invitationDate) {
        DateTime date = new DateTime(invitationDate);
        date = date.plusMonths(1);
        int thisMonth = date.getMonthOfYear();
        date = date.dayOfMonth().setCopy(1);
        DayOfWeek firstDayofMonth = DayOfWeek.getFromIso(date.getDayOfWeek());
        int diff = preferredDow.ordinal() - firstDayofMonth.ordinal();
        if (diff < 0) {
            diff = 7 + diff;
        }
        date = date.plusDays(diff);
        date = date.plusWeeks(preferredWom.ordinal());
        while (date.getMonthOfYear() > thisMonth) {
            date = date.minusWeeks(1);
        }
        return date.toDate();
    }

    @Transient
    public String getFullname() {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) sb.append(firstName);
        if (lastName != null) sb.append(sb.length() > 0 ? " " : "").append(lastName);
        return sb.toString();
    }
}
