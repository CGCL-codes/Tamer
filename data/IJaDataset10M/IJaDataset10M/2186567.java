package com.redhat.gs.mrlogistics.data;

import java.util.*;
import java.util.SortedSet;
import java.util.TreeSet;
import com.redhat.gs.mrlogistics.data.cleanupCommands.*;
import javax.persistence.*;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.jboss.seam.annotations.*;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.hibernate.validator.Email;
import org.hibernate.validator.Valid;
import org.hibernate.validator.Range;
import com.redhat.gs.mrlogistics.auth.User;
import org.hibernate.annotations.Cascade;
import static org.hibernate.annotations.CascadeType.*;

/**
 * A class that represents any person in the system.
 * @author Benjamin Kreuter
 * @see ContactCard
 * @see Consultant
 * @see CompanyDivision
 */
@Entity
@Name("person")
public class Person implements java.io.Serializable, IDestroyable {

    @Id
    @GeneratedValue
    private long id;

    private String firstName, lastName, emailAddress;

    private int position;

    private ContactCard contactInfo;

    private Region region;

    private List<Assignment> assignments;

    private List<PersonSkills> skills;

    private User user;

    public class PersonCleanupCommand extends Command {

        class PersonCleanupNullCommand extends PersonCleanupCommand {

            PersonCleanupNullCommand() {
                super.setEnabled(true);
            }

            protected void DoExecute() {
            }

            public void Execute(Assignment ass) {
            }

            public void Execute(PersonSkills psk) {
            }

            protected Command getNullCommand() {
                return this;
            }
        }

        public PersonCleanupCommand() {
        }

        protected void DoExecute() {
            assignments.clear();
            skills.clear();
            region = null;
        }

        public void Execute(Assignment ass) {
            if (isEnabled()) {
                assignments.remove(ass);
            }
        }

        public void Execute(PersonSkills psk) {
            if (isEnabled()) {
                skills.remove(psk);
            }
        }

        public PersonCleanupCommand getThis() {
            return (PersonCleanupCommand) (super.getThis());
        }

        protected Command getNullCommand() {
            return new PersonCleanupNullCommand();
        }
    }

    @Transient
    PersonCleanupCommand cleanupCommand;

    protected boolean deleted = false;

    public void setDeleted(boolean d) {
        deleted = d;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Person() {
        assignments = new ArrayList<Assignment>();
        contactInfo = new ContactCard();
        cleanupCommand = new PersonCleanupCommand();
        firstName = "John";
        lastName = "Doe";
        deleted = false;
    }

    @Transient
    public PersonCleanupCommand getCleanupCommand() {
        return cleanupCommand.getThis();
    }

    /**
     * Return a vCard style map of categories and contentseam/admin/
     * @return the contactInfo
     */
    @OneToOne
    @Cascade({ SAVE_UPDATE, DELETE })
    public ContactCard getContactInfo() {
        return contactInfo;
    }

    /**
     * Used by Hibernate
     * @param contactInfo the contactInfo to set
     */
    public void setContactInfo(ContactCard contactInfo) {
        this.contactInfo = contactInfo;
    }

    /**
     * @return the iD
     */
    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    /**
     * @param id the iD to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the assignments
     */
    @OneToMany(mappedBy = "person")
    @Cascade({ SAVE_UPDATE, DELETE })
    public List<Assignment> getAssignments() {
        return (assignments);
    }

    /**
     * @param assignments the assignments to set
     */
    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    /**
     * Helper function
     */
    public void addAssignment(Assignment assignment) {
        assignment.setPerson(this);
        assignments.add(assignment);
    }

    /**
     * @return the position
     */
    @Column(name = "JOB_POSITION")
    @NotNull
    @Range(min = 0, max = 100, message = "Position is a value from 0 to 100")
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int roles) {
        this.position = roles;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     * @return the Region
     */
    @ManyToOne
    @Valid
    public Region getRegion() {
        return this.region;
    }

    @NotNull
    @Pattern(regex = "^[a-zA-Z.-]+")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    @NotNull
    @Pattern(regex = "^[a-zA-Z.-]+")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    /**
     * @return the emailAddress
     */
    @NotNull
    @Email
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
	 * @return the skills
	 */
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "person")
    @Cascade({ SAVE_UPDATE, DELETE })
    public List<PersonSkills> getSkills() {
        return skills;
    }

    /**
	 * @param skills the skills to set
	 */
    public void setSkills(List<PersonSkills> skills) {
        this.skills = skills;
    }

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void Destroy() {
        cleanupCommand.setEnabled(false);
        for (Assignment a : getAssignments()) {
            a.getCleanupCommand().Execute();
            a.Destroy();
        }
        for (PersonSkills psk : getSkills()) {
            psk.getCleanupCommand().Execute();
            psk.Destroy();
        }
    }

    @Override
    public String toString() {
        return getLastName() + ", " + getFirstName();
    }
}
