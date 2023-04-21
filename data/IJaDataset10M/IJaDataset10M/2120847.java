package uk.ac.rdg.resc.ncwms.wms;

import java.util.Collection;
import org.joda.time.DateTime;

/**
 * Represents a Dataset, {@literal i.e.} a collection of {@link Layer}s.  Datasets are
 * represented in ncWMS as non-displayable container layers.
 *
 * @author Jon Blower
 */
public interface Dataset {

    /** Returns a unique ID for this dataset (unique on this server) */
    public String getId();

    /**
     * @return the human-readable Title of this dataset
     */
    public String getTitle();

    /**
     * Returns a copyright statement for this dataset.
     * @todo replace with Rights?
     */
    public String getCopyrightStatement();

    /**
     * Returns a web link to more information about this dataset.
     */
    public String getMoreInfoUrl();

    /**
     * <p>Returns the date/time at which this dataset was last updated.
     * This is used for Capabilities document version control in the
     * UPDATESEQUENCE part of the Capabilities document.</p>
     * <p>If the dataset has never been loaded, this method will return null.</p>
     * <p>If the last update time is unknown, the safest
     * thing to do is to return the current date/time.  This will mean that
     * clients should never cache the Capabilities document.</p>
     * @return the date/time at which this dataset was last updated, or null if
     * the dataset has never been loaded.
     */
    public DateTime getLastUpdateTime();

    /**
     * Gets the {@link Layer} with the given {@link Layer#getId() id}.  The id
     * is unique within the dataset, not necessarily on the whole server.
     * @return The layer with the given id, or null if there is no layer with
     * the given id.
     */
    public Layer getLayerById(String layerId);

    /**
     * Gets the {@link Layer}s that comprise this dataset
     * to Layer objects.
     */
    public Collection<Layer> getLayers();

    /**
     * Returns true if the dataset is ready for use.  If the dataset is ready,
     * {@link #isLoading()} and {@link #isError()} will return false and
     * {@link #getException()} will return null.
     * @return true if the dataset is ready for use.
     */
    public boolean isReady();

    /**
     * Returns true if the dataset is not ready because it is being loaded.
     * Note that there could be an outstanding error from a previous loading
     * attempt, in which case {@link #isError()} will also return true and
     * {@link #getException()} will return the exception.
     * @return true if the dataset is not ready because it is being loaded.
     */
    public boolean isLoading();

    /**
     * Returns true if there is an error with this dataset. More details about
     * the error can be found by calling {@link #getException()}.  Note that a
     * dataset could be loading, in which case this returns the error from the
     * previous attempt at loading.
     * @return true if there is an error with this dataset.
     */
    public boolean isError();

    /**
     * If a dataset cannot be used due to an error, this can be called to find
     * out more details.
     * @return Exception object containing more details about the error, or null
     * if there is no error.
     */
    public Exception getException();

    /**
     * Returns true if this dataset cannot be used because access has been
     * disabled by the system administrator.
     * @return true if this dataset cannot be used because access has been
     * disabled
     */
    public boolean isDisabled();
}
