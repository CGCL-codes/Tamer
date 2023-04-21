package su.nsk.inp.roentgen.model.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import su.nsk.inp.roentgen.model.SecurePerson;
import su.nsk.inp.roentgen.model.User;

/**
 * Represents assistant model based on secure person.
 * 
 * @author Andrey Basalaev
 */
@Entity
@Table(name = "laborant")
public class Laborant extends SecurePerson {

    private static final long serialVersionUID = 596563046294877700L;

    private Long _id;

    private boolean _modifyPatient = false;

    private boolean _removePatient = false;

    private boolean _removeExamination = false;

    private User _user;

    public Laborant() {
        super();
    }

    public Laborant(SecurePerson person) {
        super(person);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return _id;
    }

    @Override
    @Column(name = "name", length = 50, nullable = false)
    public String getFirstName() {
        return _firstName;
    }

    @Override
    @Column(name = "surname", length = 50, nullable = false)
    public String getLastName() {
        return _lastName;
    }

    @Override
    @Column(name = "patronymic", length = 50, nullable = false)
    public String getPatronymic() {
        return _patronymic;
    }

    @Override
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return _password;
    }

    @Column(name = "patient_data_change")
    public boolean isModifyPatient() {
        return _modifyPatient;
    }

    @Column(name = "patient_remove")
    public boolean isRemovePatient() {
        return _removePatient;
    }

    @Column(name = "exam_remove")
    public boolean isRemoveExamination() {
        return _removeExamination;
    }

    @OneToOne
    @JoinTable(name = "user_assistant", joinColumns = { @JoinColumn(name = "assistant_id") }, inverseJoinColumns = @JoinColumn(name = "user_id"))
    public User getUser() {
        return _user;
    }

    public void setModifyPatient(boolean modifyPatient) {
        _modifyPatient = modifyPatient;
    }

    public void setRemovePatient(boolean removePatient) {
        _removePatient = removePatient;
    }

    public void setRemoveExamination(boolean removeExamination) {
        _removeExamination = removeExamination;
    }

    public void setId(Long id) {
        _id = id;
    }

    public void setUser(User user) {
        _user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DirectiveDoctor)) {
            return false;
        }
        final DirectiveDoctor doctor = (DirectiveDoctor) o;
        return getFullName().equals(doctor.getFullName());
    }
}
