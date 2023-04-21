package ie.blackoutscout.common.commentary.implementations;

import ie.blackoutscout.common.commentary.interfaces.ICreativeCommentary;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author John
 */
@Entity
public class CreativeCommentary implements ICreativeCommentary {

    @SequenceGenerator(name = "Emp_Gen", sequenceName = "Emp_Seq")
    @Id
    @GeneratedValue(generator = "Emp_Gen")
    private Long id;

    @Column(length = 1000)
    private String commentary;

    private Long brTimeStamp;

    public Long getBrTimeStamp() {
        return brTimeStamp;
    }

    public void setBrTimeStamp(Long brTimeStamp) {
        this.brTimeStamp = brTimeStamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getCommentary() {
        return commentary;
    }

    public String getIdString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
