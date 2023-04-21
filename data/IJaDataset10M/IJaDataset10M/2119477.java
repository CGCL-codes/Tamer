package org.actioncenters.udm.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.actioncenters.core.contribution.data.IContributionProperty;
import org.actioncenters.core.contribution.data.IContributionProperty.ContributionPropertyType;
import org.actioncenters.udm.history.HistoryEnabled;
import org.actioncenters.xml.ceml.Binaries;
import org.actioncenters.xml.ceml.Binaryvalue;
import org.actioncenters.xml.ceml.PropertyChoice;

/**
 * A contribution property with image contents.
 *
 * @author amametjanov
 */
@Entity
@DiscriminatorValue("Image")
public class ImageProperty extends Contributionproperty {

    /** Compiler-generated UID. */
    private static final long serialVersionUID = 7802333711908322387L;

    /** The image. */
    private BinaryObject imageValue;

    /**
     * Default constructor.
     */
    public ImageProperty() {
    }

    /**
     * Minimal constructor.
     *
     * @param user
     *            the user
     * @param contribution
     *            the contribution
     * @param lockedBy
     *            the lockedBy
     * @param key
     *            the key
     * @param thumbprints
     *            the thumbprints
     * @param fromDate
     *            the fromDate
     * @param image
     *            the image
     */
    protected ImageProperty(User user, Contribution contribution, User lockedBy, String key, String thumbprints, Timestamp fromDate, BinaryObject image) {
        super(user, contribution, lockedBy, key, null, thumbprints, fromDate);
        this.imageValue = image;
    }

    /**
     * Constructs an image property from the old property.
     *
     * @param user
     *            the user
     * @param contribution
     *            the contribution
     * @param lockedBy
     *            the locking user
     * @param oldProperty
     *            the old property
     * @param thumbprints
     *            the thumb prints
     * @param fromDate
     *            the time stamp
     */
    protected ImageProperty(User user, Contribution contribution, User lockedBy, ImageProperty oldProperty, String thumbprints, Timestamp fromDate) {
        this(user, contribution, lockedBy, oldProperty.getPropertyKey(), thumbprints, fromDate, oldProperty.getImage());
    }

    /**
     * Constructs an image property based on the specified property VO.
     *
     * @param contribution
     *            the contribution
     * @param property
     *            the property VO
     * @param user
     *            the user
     * @param object
     *            the binary object
     */
    protected ImageProperty(Contribution contribution, IContributionProperty property, User user, BinaryObject object) {
        this(user, contribution, null, property.getPropertyKey(), null, null, object);
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imageValue")
    public BinaryObject getImage() {
        return this.imageValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(IContributionProperty property, User user, BinaryObjectDAO binaryObjectDAO) {
        if (ContributionPropertyType.IMAGE.equals(property.getContributionPropertyType())) {
            super.update(property, user, binaryObjectDAO);
            this.imageValue = binaryObjectDAO.findById(property.getValue());
        } else {
            super.update(property, user, binaryObjectDAO);
        }
    }

    /**
     * Sets the image.
     *
     * @param image
     *            the new image
     */
    public void setImage(BinaryObject image) {
        this.imageValue = image;
    }

    /**
     * Sets the image given a GUID to BinaryObject
     *
     * @param imageId
     *            the new image
     * @param binaryObjectDAO
     *            the binary object DAO
     */
    public void setImage(String imageId, BinaryObjectDAO binaryObjectDAO) {
        this.imageValue = binaryObjectDAO.findById(imageId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transient
    public IContributionProperty getVOWithoutUser() {
        String imageId = "";
        if (getImage() != null) {
            imageId = getImage().getId();
        }
        IContributionProperty returnValue = new org.actioncenters.core.contribution.data.impl.ImageProperty(getPropertyKey(), imageId);
        returnValue.setThumbprints(getThumbprints());
        returnValue.setFromDate(getFromDate());
        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transient
    public boolean hasChanges(HistoryEnabled original) {
        boolean returnValue = false;
        if (original instanceof ImageProperty) {
            returnValue = super.hasChanges(original);
            if (!returnValue) {
                returnValue = !(this.getImage() == ((ImageProperty) original).getImage());
            }
        }
        return returnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    protected BinaryExporter getExporter() {
        if (exporter == null) {
            exporter = new BinaryExporter();
        }
        return exporter;
    }

    /** The exporter. */
    private BinaryExporter exporter;

    /**
     * The Class BinaryExporter.
     */
    protected class BinaryExporter extends Exporter {

        /**
         * {@inheritDoc}
         */
        @Transient
        protected PropertyChoice exportPropertyChoice(Binaries binaries) throws SQLException {
            PropertyChoice returnValue = new PropertyChoice();
            returnValue.setBinaryvalue(exportBinaryValue(binaries));
            return returnValue;
        }

        /**
         * Export binary value.
         *
         * @param binaries the binaries
         * @return the binaryvalue
         * @throws SQLException the sQL exception
         */
        @Transient
        private Binaryvalue exportBinaryValue(Binaries binaries) throws SQLException {
            Binaryvalue returnValue = null;
            if (exportBinaryref(binaries) != null) {
                returnValue = new Binaryvalue();
                returnValue.setBinaryref(exportBinaryref(binaries));
            }
            return returnValue;
        }

        /**
         * Export binaryref.
         *
         * @param binaries the binaries
         * @return the string
         * @throws SQLException the sQL exception
         */
        @Transient
        protected String exportBinaryref(Binaries binaries) throws SQLException {
            try {
                return getImage().getExporter().exportBinaryref(binaries);
            } catch (NullPointerException e) {
                return null;
            }
        }
    }
}
