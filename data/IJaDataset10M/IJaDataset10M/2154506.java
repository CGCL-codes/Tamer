package dk.datenbank.test;

import javax.persistence.*;
import org.openxava.annotations.*;

@Entity
@Views({ @View(name = "AddCommentDialog", members = "comment"), @View(name = "AddComment", members = "comment;validFrom") })
public class Comment extends Historizeable {

    @Column(name = "cmmnt", length = 4000)
    @Stereotype(value = "MEMO")
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
