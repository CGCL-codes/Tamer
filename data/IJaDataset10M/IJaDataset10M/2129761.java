package spcp7.imagegallery.contentprovider.face;

import java.util.List;
import java.util.Map;
import spcp7.imagegallery.abstractionlayer.exception.ContentCouldNotBeAccessedException;
import spcp7.imagegallery.abstractionlayer.exception.NoContentFoundException;
import spcp7.imagegallery.abstractionlayer.exception.PropertyValidationException;
import spcp7.imagegallery.abstractionlayer.exception.RangeNotValidException;
import spcp7.imagegallery.abstractionlayer.face.ContentFace;
import spcp7.imagegallery.abstractionlayer.face.persistence.PropertyModelFace;

/**
 * This interface specifies methods which are used to retrieve content of this
 * content provider.
 */
public interface ContentRetrievalFace extends ContentConnectionFace {

    /**
     * With this method you can get volumes from the specified absolute path.
     * The {@link DefaultProperties#SPCP7_CURRENT_PATH#toString()} property
     * defines this path. For instructions of the used properties within this
     * method consider the documentation of the specific content provider.
     * 
     * @param properties
     *                the properties of the specified path.
     * @param isFolder
     *                True if the path specifies a folder, false otherwise.
     * @return A List with {@link ContentFace}s or a List with only one element
     *         if param isFolder was false.
     * @throws PropertyValidationException
     *                 If a property is missing or has the wrong value.
     * @throws ContentCouldNotBeAccessedException
     *                 If the content could not be accessed. This may hav
     *                 several reasons. Look at the exception message to get the
     *                 details.
     * @throws NoContentFoundException
     *                 If the query was correct but no content was found.
     */
    public List<ContentFace> getVolumes(Map<String, PropertyModelFace> properties, boolean isFolder) throws PropertyValidationException, ContentCouldNotBeAccessedException, NoContentFoundException;

    /**
     * With this method you can get volumes from the specified absolute path.
     * The {@link DefaultProperties#SPCP7_CURRENT_PATH#toString()} property
     * defines this path. For instructions of the used properties within this
     * method consider the documentation of the specific content provider. This
     * method allows the user to get only contents of a specific range. To know
     * how many
     * 
     * @param lowerLimit
     *                The lower limit of the result set
     * @param upperLimit
     *                The upper limit of the result set
     * @param properties
     *                the properties of the specified path.
     * @param isFolder
     *                True if the path specifies a folder, false otherwise.
     * @return A List with {@link ContentFace}s
     * @throws PropertyValidationException
     *                 If a property is missing or has the wrong value.
     * @throws ContentCouldNotBeAccessedException
     *                 If the content could not be accessed. This may hav
     *                 several reasons. Look at the exception message to get the
     *                 details.
     * @throws NoContentFoundException
     *                 If the query was correct but no content was found.
     * @throws RangeNotValidException
     *                 if the specified range is not valid.
     */
    public List<ContentFace> getVolumesFromTo(int lowerLimit, int upperLimit, Map<String, PropertyModelFace> properties, boolean isFolder) throws RangeNotValidException, PropertyValidationException, ContentCouldNotBeAccessedException, NoContentFoundException;

    /**
     * @param proprietaryId
     * @param properties
     * @return
     * @throws ContentCouldNotBeAccessedException
     * @throws NoContentFoundException
     */
    public ContentFace getParent(String proprietaryId, Map<String, PropertyModelFace> properties) throws ContentCouldNotBeAccessedException, NoContentFoundException;
}
