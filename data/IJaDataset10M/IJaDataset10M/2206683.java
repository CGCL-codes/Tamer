package org.jabusuite.transaction;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * @author hilwers
 * @date 23.07.2007
 *
 */
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Offer extends BusinessTransaction implements Serializable {

    private static final long serialVersionUID = -1555769323624479303L;
}
