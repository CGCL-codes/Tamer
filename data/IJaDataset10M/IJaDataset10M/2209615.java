package org.isurf.spmiddleware.reader.identity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.isurf.spmiddleware.reader.ReaderProfile;

/**
 * Persistant entity representing a business transaction type associated with a Reader.
 */
@Entity
public class BusinessTransactionType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ReaderProfile readerProfile;

    private String businessTransactionType;

    /**
	 * Gets the business transaction type.
	 *
	 * @return the businessTransactionType
	 */
    public String getBusinessTransactionType() {
        return businessTransactionType;
    }

    /**
	 * Sets the business transaction type.
	 *
	 * @param businessTransactionType the businessTransactionType to set
	 */
    public void setBusinessTransactionType(String businessTransactionType) {
        this.businessTransactionType = businessTransactionType;
    }

    /**
	 * Gets the ID.
	 *
	 * @return the id
	 */
    public Long getId() {
        return id;
    }

    /**
	 * Sets the ID.
	 *
	 * @param id the id to set
	 */
    public void setId(Long id) {
        this.id = id;
    }

    /**
	 * Gets the {@link ReaderProfile}.
	 *
	 * @return the readerProfile
	 */
    public ReaderProfile getReaderProfile() {
        return readerProfile;
    }

    /**
	 * Sets the {@link ReaderProfile}.
	 *
	 * @param readerProfile the readerProfile to set
	 */
    public void setReaderProfile(ReaderProfile readerProfile) {
        this.readerProfile = readerProfile;
    }
}
