package org.surveyforge.core.metadata;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author jsegura
 */
@Entity
public class ValidationRule implements Serializable {

    private static final long serialVersionUID = 0L;

    @SuppressWarnings("unused")
    @Id
    @Column(length = 50)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    /** Version for optimistic locking. */
    @SuppressWarnings("unused")
    @javax.persistence.Version
    private int lockingVersion;

    @ManyToOne
    @JoinColumn(name = "register_id", insertable = false, updatable = false)
    private Register register;

    protected ValidationRule() {
    }
}
