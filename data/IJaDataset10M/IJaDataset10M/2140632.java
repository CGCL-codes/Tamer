package org.restlet.data;

import org.restlet.util.Series;

/**
 * Metadata preference definition.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public final class Preference<T extends Metadata> {

    /** The metadata associated with this preference. */
    private T metadata;

    /** The quality/preference level. */
    private float quality;

    /** The modifiable list of parameters. */
    private Series<Parameter> parameters;

    /**
     * Constructor.
     */
    public Preference() {
        this(null, 1F, null);
    }

    /**
     * Constructor.
     * 
     * @param metadata
     *            The associated metadata.
     */
    public Preference(T metadata) {
        this(metadata, 1F, null);
    }

    /**
     * Constructor.
     * 
     * @param metadata
     *            The associated metadata.
     * @param quality
     *            The quality/preference level.
     */
    public Preference(T metadata, float quality) {
        this(metadata, quality, null);
    }

    /**
     * Constructor.
     * 
     * @param metadata
     *            The associated metadata.
     * @param quality
     *            The quality/preference level.
     * @param parameters
     *            The list of parameters.
     */
    public Preference(T metadata, float quality, Series<Parameter> parameters) {
        this.metadata = metadata;
        this.quality = quality;
        this.parameters = parameters;
    }

    /**
     * Returns the metadata associated with this preference.
     * 
     * @return The metadata associated with this preference.
     */
    public T getMetadata() {
        return metadata;
    }

    /**
     * Returns the modifiable list of parameters.
     * 
     * @return The modifiable list of parameters.
     */
    public Series<Parameter> getParameters() {
        if (this.parameters == null) this.parameters = new Form();
        return this.parameters;
    }

    /**
     * Returns the quality/preference level.
     * 
     * @return The quality/preference level.
     */
    public float getQuality() {
        return quality;
    }

    /**
     * Sets the metadata associated with this preference.
     * 
     * @param metadata
     *            The metadata associated with this preference.
     */
    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }

    /**
     * Sets the quality/preference level.
     * 
     * @param quality
     *            The quality/preference level.
     */
    public void setQuality(float quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return (getMetadata() == null) ? "" : (getMetadata().getName() + ":" + getQuality());
    }
}
