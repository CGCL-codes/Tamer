package abstractEntities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;
import entities.Languagecourse;
import entities.Languagedepartment;
import entities.Languageitem;
import entities.Languagelesson;
import entities.Tutorial;

/**
 * AbstractLanguage entity provides the base persistence definition of the
 * Language entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractLanguage implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5099913732612949071L;

    private String abb;

    private String language;

    private Set<Languagedepartment> languagedepartments = new HashSet<Languagedepartment>(0);

    private Set<Tutorial> tutorials = new HashSet<Tutorial>(0);

    private Set<Languageitem> languageitems = new HashSet<Languageitem>(0);

    private Set<Languagecourse> languagecourses = new HashSet<Languagecourse>(0);

    private Set<Languagelesson> languagelessons = new HashSet<Languagelesson>(0);

    /** default constructor */
    public AbstractLanguage() {
    }

    /** minimal constructor */
    public AbstractLanguage(String language) {
        this.language = language;
    }

    /** full constructor */
    public AbstractLanguage(String language, Set<Languagedepartment> languagedepartments, Set<Tutorial> tutorials, Set<Languageitem> languageitems, Set<Languagecourse> languagecourses, Set<Languagelesson> languagelessons) {
        this.language = language;
        this.languagedepartments = languagedepartments;
        this.tutorials = tutorials;
        this.languageitems = languageitems;
        this.languagecourses = languagecourses;
        this.languagelessons = languagelessons;
    }

    @GenericGenerator(name = "generator", strategy = "increment")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "abb", unique = true, nullable = false, length = 5)
    public String getAbb() {
        return this.abb;
    }

    public void setAbb(String abb) {
        this.abb = abb;
    }

    @Column(name = "language", nullable = false, length = 20)
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "language")
    public Set<Languagedepartment> getLanguagedepartments() {
        return this.languagedepartments;
    }

    public void setLanguagedepartments(Set<Languagedepartment> languagedepartments) {
        this.languagedepartments = languagedepartments;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "language")
    public Set<Tutorial> getTutorials() {
        return this.tutorials;
    }

    public void setTutorials(Set<Tutorial> tutorials) {
        this.tutorials = tutorials;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "language")
    public Set<Languageitem> getLanguageitems() {
        return this.languageitems;
    }

    public void setLanguageitems(Set<Languageitem> languageitems) {
        this.languageitems = languageitems;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "language")
    public Set<Languagecourse> getLanguagecourses() {
        return this.languagecourses;
    }

    public void setLanguagecourses(Set<Languagecourse> languagecourses) {
        this.languagecourses = languagecourses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "language")
    public Set<Languagelesson> getLanguagelessons() {
        return this.languagelessons;
    }

    public void setLanguagelessons(Set<Languagelesson> languagelessons) {
        this.languagelessons = languagelessons;
    }
}
