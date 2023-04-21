package net.sf.myway.trainer.da.entities;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import net.sf.myway.hibernate.UuidEntity;

/**
 * @author Andreas Beckers
 * @version $Revision: $
 */
@Entity
@Table(name = "TRN_USER_PROFILE")
public class UserProfile extends UuidEntity {

    private String _name;

    private List<ProfileActivity> _activities;

    private Float _weight;

    private Short _birthYear;

    private Byte _birthMonth;

    private Byte _birthDay;

    private Gender _gender;

    /**
	 * @return the activities
	 */
    @OneToMany(mappedBy = "profile")
    public List<ProfileActivity> getActivities() {
        return _activities;
    }

    /**
	 * @param activities the activities to set
	 */
    public void setActivities(List<ProfileActivity> activities) {
        _activities = activities;
    }

    /**
	 * @return the weight
	 */
    public Float getWeight() {
        return _weight;
    }

    /**
	 * @param weight the weight to set
	 */
    public void setWeight(Float weight) {
        _weight = weight;
    }

    /**
	 * @return the birthYear
	 */
    public Short getBirthYear() {
        return _birthYear;
    }

    /**
	 * @param birthYear the birthYear to set
	 */
    public void setBirthYear(Short birthYear) {
        _birthYear = birthYear;
    }

    /**
	 * @return the birthMonth
	 */
    public Byte getBirthMonth() {
        return _birthMonth;
    }

    /**
	 * @param birthMonth the birthMonth to set
	 */
    public void setBirthMonth(Byte birthMonth) {
        _birthMonth = birthMonth;
    }

    /**
	 * @return the birthDay
	 */
    public Byte getBirthDay() {
        return _birthDay;
    }

    /**
	 * @param birthDay the birthDay to set
	 */
    public void setBirthDay(Byte birthDay) {
        _birthDay = birthDay;
    }

    /**
	 * @return the gender
	 */
    public Gender getGender() {
        return _gender;
    }

    /**
	 * @param gender the gender to set
	 */
    public void setGender(Gender gender) {
        _gender = gender;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return _name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        _name = name;
    }
}
