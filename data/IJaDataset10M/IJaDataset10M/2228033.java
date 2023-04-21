package entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import abstractEntities.AbstractComplaint;

/**
 * Complaint entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "complaint", catalog = "skillworld")
public class Complaint extends AbstractComplaint implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5952192749797885352L;

    /** default constructor */
    public Complaint() {
    }

    /** full constructor */
    public Complaint(Tutorial tutorial, User user, Date dateComplaint, String text, Integer type, Integer state) {
        super(tutorial, user, dateComplaint, text, type, state);
    }
}
