package com.firescrum.core.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.firescrum.infrastructure.model.PersistentEntity;

@Entity
public class TestSuite extends PersistentEntity {

    /**
     * 
     */
    private static final long serialVersionUID = -3422613402019189762L;

    /**
     * Represents the unique identifier of the Test Suite.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the name of the Test Suite
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Represents the description of the Test Suite
     */
    private String description;

    /**
     * Represents the unique identifier of the Test Suite.
     */
    private Date createdDate;

    /**
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the description
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description the description to set
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return the createdDate
	 */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
	 * @param createdDate the createdDate to set
	 */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
