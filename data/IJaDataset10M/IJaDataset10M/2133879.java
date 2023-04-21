package cbr.service;

import java.awt.Image;
import java.io.IOException;
import cbr.FeatureVector;

/**
 * This interface provides access to content based image
 * retrieval services. Queries can be issued by means of
 * example images or feature vectors.<p>
 *
 * The return type of the principal methods is a list of
 * {@link PictureEntry picture entries}. These entries
 * provide information on the similarity of the returned
 * images to the query as well as where the corresponding
 * image can be retrieved.
 *
 * @author Volker Roth
 * @version "$Id: PicsFinder.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public interface PicsFinder {

    /**
     * Matches the given image against the stored images and
     * returnes the entries of the <code>max</code> most
     * similar pictures known. All returned picture entries
     * represent pictures that have a distance from the query
     * picture that is less than the given threshold. The
     * threshold is normalised in the range [0.0,1.0] where 0
     * denotes identity (of feature vectors) and 1 denotes
     * infinite distance.
     *
     * @param im The query image.
     * @param threshold The maximum distance retrieved pictures
     *   are allowed to have from the query image.
     * @param max The maximum number of hits to return.
     */
    public PictureEntry[] find(Image im, float threshold, int max) throws IOException;

    /**
     * Matches the given feature vector against the ones of
     * the known Pictures and returns the picture entries
     * of at most <code>max</code> pictures that have a
     * normalised distance less than the given threshold to
     * the query vector. If the feature vector is not of a
     * supported type then an exception is thrown.
     *
     * @param v The feature vector to match known picture
     *   entries against.
     * @param threshold The maximum distance a picture in
     *   the result set is allowed to have from the query
     *   vector.
     * @exception UnsupportedAlgorithmException if the given
     *   feature vector cannot be mapped to the supported
     *   ones.
     */
    public PictureEntry[] find(FeatureVector v, float threshold, int max) throws IOException;
}
