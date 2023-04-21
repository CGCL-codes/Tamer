package models;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import manager.PersonManager;
import play.Logger;
import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Index;
import siena.Model;
import siena.NotNull;
import siena.Table;
import util.Strings;
import constant.SexEnum;
import exception.MoreThanOneSpouseException;

/**
 * Class representing a person entity.
 * 
 * @author slever
 * 
 */
@Table("persons")
public class Person extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    @Column("address")
    @Index("address_index")
    public Address address;

    @NotNull
    public String firstName;

    @NotNull
    public String lastName = "";

    @NotNull
    @Column("history")
    @Index("history_index")
    public History history;

    @NotNull
    public String sex;

    @NotNull
    public String linkedUser;

    @NotNull
    public String owner;

    @NotNull
    public String prefix;

    @Column("father")
    @Index("father_index")
    public Person father;

    @Column("mother")
    @Index("mother_index")
    public Person mother;

    /**
	 * represent the functional user id (ex:"sebastien.lever" or "sebastien.lever2")
	 * <br>
	 * this is the person UID and should be the same as client application user
	 * uid.
	 * 
	 * @uml.property name="uid"
	 */
    @NotNull
    public String uid;

    /**
	 * Constructor
	 * 
	 * @param lastName
	 * @param firstName
	 * @param theSex
	 * @param birthDate
	 * @param birthPlace
	 * @throws InvalidParameterException
	 */
    public Person(String lastName, String firstName, SexEnum theSex, Date birthDate, String birthPlace) throws InvalidParameterException {
        if (Strings.isEmpty(lastName) || Strings.isEmpty(firstName)) {
            throw new InvalidParameterException("fistname and last name cannot be empty");
        }
        if (birthDate.getTime() > Calendar.getInstance().getTimeInMillis()) {
            throw new InvalidParameterException("birthDate in the future is invalid ! " + birthDate);
        }
        if (theSex == null) {
            throw new InvalidParameterException("Sex cannot be null ! ");
        }
        this.lastName = lastName.trim().toUpperCase();
        this.prefix = String.valueOf(lastName.charAt(0));
        firstName = firstName.trim();
        this.firstName = String.valueOf(firstName.charAt(0)).toUpperCase() + firstName.substring(1).toLowerCase();
        this.history = new History(birthDate, birthPlace);
        this.sex = theSex.name();
    }

    /**
	 * add a parent (auto-save)
	 * 
	 * @param parent
	 */
    public void addParent(Person parent) {
        switch(SexEnum.valueOf(parent.sex)) {
            case MALE:
                father = parent;
                break;
            case FEMALE:
                mother = parent;
                break;
            default:
                Logger.warn("unknown sex for parent " + parent);
                break;
        }
        save();
    }

    /**
	 * add a child (auto-save)
	 * 
	 * @param child
	 */
    public void addChild(Person child) {
        child.addParent(this);
    }

    /**
	 * Compute person Id based on the firstName and the lastName.<br>
	 * This should not be call before mandatory person attributes
	 * initialisation.
	 * 
	 * @return the person unique uid
	 */
    public String computePersonUid() {
        StringBuffer generatedId = new StringBuffer();
        generatedId.append(firstName.toLowerCase());
        generatedId.append(".");
        generatedId.append(lastName.toLowerCase());
        String normalizedId = Strings.sansAccents(generatedId.toString());
        normalizedId = normalizedId.replaceAll("[^\\p{Lower}&&[^\\.]]", "");
        boolean exist = true;
        int i = 0;
        String tmpUid = normalizedId;
        do {
            if (i > 0) {
                tmpUid = normalizedId + i;
            }
            exist = !PersonManager.isUnique(tmpUid);
            if (exist) {
                ++i;
            }
        } while (exist);
        return tmpUid;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;
        if (obj instanceof Person) {
            equal = ((Person) obj).id.intValue() == this.id.intValue();
        }
        return equal;
    }

    /**
	 * @return brothers and sisters
	 */
    public List<Person> findBrothers() {
        List<Person> brothers = new ArrayList<Person>();
        if (father != null) {
            for (Person child : Model.all(Person.class).filter("father", father).fetch()) {
                if (!brothers.contains(child) && !child.equals(this)) {
                    brothers.add(child);
                }
            }
        }
        if (mother != null) {
            for (Person child : Model.all(Person.class).filter("mother", mother).fetch()) {
                if (!brothers.contains(child) && !child.equals(this)) {
                    brothers.add(child);
                }
            }
        }
        return brothers;
    }

    public List<Person> findCousins() {
        List<Person> cousins = new ArrayList<Person>();
        if (father != null) {
            for (Person uncle : father.findBrothers()) {
                for (Person child : Model.all(Person.class).filter("mother", uncle).fetch()) {
                    if (!cousins.contains(child)) {
                        cousins.add(child);
                    }
                }
                for (Person child : Model.all(Person.class).filter("father", uncle).fetch()) {
                    if (!cousins.contains(child)) {
                        cousins.add(child);
                    }
                }
            }
        }
        if (mother != null) {
            for (Person uncle : mother.findBrothers()) {
                for (Person child : Model.all(Person.class).filter("mother", uncle).fetch()) {
                    if (!cousins.contains(child)) {
                        cousins.add(child);
                    }
                }
                for (Person child : Model.all(Person.class).filter("father", uncle).fetch()) {
                    if (!cousins.contains(child)) {
                        cousins.add(child);
                    }
                }
            }
        }
        return cousins;
    }

    /**
	 * Find a spouse for the dateTime (should be used for monogamous unions),
	 * null if not found
	 * 
	 * @param dateTime
	 * @return spouse for the dateTime, null if not found
	 * @throws MoreThanOneSpouseException
	 *             thrown exception if Person has more than one union for a
	 *             dateTime
	 */
    public Person findSpouse(long dateTime) throws MoreThanOneSpouseException {
        Person spouse = null;
        List<Union> unions = history.findUnions();
        if (unions.size() > 1) {
            throw new MoreThanOneSpouseException("Monogamous unions cannot have multiple spouses, please use findSpouses() method instead !");
        }
        for (Union union : unions) {
            if (dateTime >= union.event.date.getTime() && (!union.isEnded() || dateTime < union.getEndTime())) {
                spouse = union.getSpouse();
                break;
            }
        }
        return spouse;
    }

    /**
	 * Find one or more spouse for the dateTime, empty list if not found
	 * 
	 * @param dateTime
	 *            the date time
	 * @return spouses for the dateTime, empty list if not found
	 */
    public List<Person> findSpouses(long dateTime) {
        List<Person> spouses = new ArrayList<Person>();
        for (Union union : history.findUnions()) {
            if (dateTime >= union.event.date.getTime() && (!union.isEnded() || dateTime < union.getEndTime())) {
                spouses.add(union.getSpouse());
            }
        }
        return spouses;
    }

    /**
	 * @return true if has no spouse for now.
	 */
    public boolean isSingle() {
        return findSpouses(Calendar.getInstance().getTimeInMillis()).isEmpty();
    }

    @Override
    public String toString() {
        return "Person[" + id + "," + lastName + " " + firstName + "(" + uid + "),mother:" + mother + ",father:" + father + "]";
    }

    /**
	 * Unite a person to another.
	 * 
	 * @param spouse
	 * @param dateTime
	 * @param isMonogamous
	 * @throws MoreThanOneSpouseException
	 */
    public void unite(Person spouse, Date date, boolean isMonogamous) throws MoreThanOneSpouseException {
        if (isMonogamous) {
            for (Union union : history.findUnions()) {
                if (!union.isEnded()) {
                    throw new MoreThanOneSpouseException("Cannot add more than one spouse for monogamous union.");
                }
            }
        }
        Union union = new Union(spouse, date);
        history.insertUnion(union);
        history.update();
        spouse.history.insertUnion(union);
        spouse.history.update();
    }

    /**
	 * 
	 */
    public void save() {
        this.update();
    }

    @Override
    public void delete() {
        Logger.debug("delete " + this.history);
        this.history.delete();
        Logger.debug("delete " + this);
        super.delete();
    }

    @Override
    public void insert() {
        this.uid = computePersonUid();
        this.history.insert();
        super.insert();
        Logger.debug("insert history " + history + " for person " + this);
    }

    @Override
    public void update() {
        this.history.update();
        super.update();
        Logger.debug("update history " + history + " for person " + this);
    }

    public boolean hasLinkedUser() {
        return linkedUser != null && !"".equals(linkedUser);
    }
}
