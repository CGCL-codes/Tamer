package at.ac.lbg.media.vis.framework.service;

import java.util.ArrayList;
import java.util.List;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.EVD;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.NotConvergedException;
import no.uib.cipr.matrix.sparse.CompDiagMatrix;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.ac.lbg.media.vis.framework.domain.ArtworkDO;
import at.ac.lbg.media.vis.framework.domain.CategoryDO;
import at.ac.lbg.media.vis.framework.persistence.ICategoryDAO;

/**
 * Multidimensional Scaling Service for Artworks.
 * @author Evelyn Münster
 *
 */
public class MDSService implements IMDSService {

    protected final Log logger = LogFactory.getLog(getClass());

    private List<CategoryDO> allCats;

    private ICategoryDAO categoryDAO;

    public MDSService() {
    }

    @Override
    public Matrix provideCoordinates(List<ArtworkDO> artworks) throws NotConvergedException, Exception {
        provideCategoryVector(artworks, null);
        Matrix similarityM = generateObjectSimilarityMatrix(artworks);
        logger.debug("similarityM is set with:" + similarityM.numColumns() + "/" + similarityM.numRows());
        Matrix mds = generateClassicalMDS(similarityM);
        return mds;
    }

    @Override
    public Matrix provideCoordinates(List<ArtworkDO> artworks, List<CategoryDO> categories) throws NotConvergedException, Exception {
        provideCategoryVector(artworks, categories);
        Matrix similarityM = generateObjectSimilarityMatrix(artworks);
        logger.debug("similarityM is set with:" + similarityM.numColumns() + "/" + similarityM.numRows());
        Matrix mds = generateClassicalMDS(similarityM);
        return mds;
    }

    private void provideCategoryVector(List<ArtworkDO> artworks, List<CategoryDO> cats) {
        for (ArtworkDO artwork : artworks) {
            provideCategoryVector(artwork, cats);
        }
    }

    /**
	 * Fills the vector attribute of an ArtworkDAO with the
	 * Category-Vector like {1,0,0,1,0,0,0,1,1,0,0,0,0,0,0,...}
	 * Size of array is number of keywords. Order of array
	 * is ascending order of Category#id.
	 * 1 = keyword is set
	 * 0 = keyword is not set
	 * @param artwork the artwork to be filled
	 * @param cats these keywords will be used for the vector. if null, all keywords will be
	 * fetched from db and used.
	 */
    private void provideCategoryVector(ArtworkDO artwork, List<CategoryDO> cats) {
        if (cats == null) {
            if (allCats == null) {
                allCats = categoryDAO.getKeywords();
            }
            cats = allCats;
        }
        List<Integer> vector = new ArrayList<Integer>(cats.size());
        List<CategoryDO> artworkCats = artwork.getCategories();
        boolean found = false;
        for (CategoryDO cat : cats) {
            for (CategoryDO awCat : artworkCats) {
                if (awCat.getId().intValue() == cat.getId().intValue()) {
                    vector.add(1);
                    found = true;
                    break;
                }
            }
            if (!found) {
                vector.add(0);
            }
            found = false;
        }
        artwork.setVector(vector);
    }

    private Matrix generateClassicalMDS(Matrix similarityMatrix) throws NotConvergedException, Exception {
        int dimension = similarityMatrix.numColumns();
        Matrix diagonal1 = new CompDiagMatrix(dimension, dimension);
        for (int i = 0; i < dimension; i++) {
            diagonal1.add(i, i, 1.0);
        }
        Matrix just1s = new DenseMatrix(dimension, dimension);
        for (int r = 0; r < dimension; r++) {
            for (int c = 0; c < dimension; c++) {
                just1s.add(r, c, 1.0);
            }
        }
        double scaler = -(1.0 / dimension);
        Matrix j = just1s.scale(scaler);
        j = diagonal1.add(j);
        Matrix jhalf = j.copy();
        jhalf = jhalf.scale(-0.5);
        Matrix interim = similarityMatrix.mult(j, new DenseMatrix(dimension, dimension));
        Matrix b = jhalf.mult(interim, new DenseMatrix(dimension, dimension));
        EVD eigen = EVD.factorize(b);
        if (eigen == null || !eigen.hasRightEigenvectors()) {
            throw new Exception("Eigen decomposition failed.");
        }
        DenseMatrix eigenvectors = eigen.getRightEigenvectors();
        double[] eigenvalues = eigen.getRealEigenvalues();
        double eval1 = eigenvalues[0];
        double eval2 = eigenvalues[1];
        Matrix diagonalval = new FlexCompColMatrix(2, 2);
        diagonalval.add(0, 0, Math.pow(eval1, 0.5));
        diagonalval.add(1, 1, Math.pow(eval2, 0.5));
        Matrix evecs = new FlexCompColMatrix(eigenvectors.numRows(), 2);
        for (int i = 0; i < eigenvectors.numRows(); i++) {
            evecs.add(i, 0, eigenvectors.get(i, 0));
            evecs.add(i, 1, eigenvectors.get(i, 1));
        }
        Matrix mds = new FlexCompColMatrix(evecs.numRows(), diagonalval.numColumns());
        mds = evecs.mult(diagonalval, mds);
        logger.debug("mds is set with:" + mds.numColumns() + "/" + mds.numRows());
        return mds;
    }

    /**
	 * Use Euclidean Distance as measure for the similarity matrix.
	 * Computing the square root of the sum of the squares of the differences
	 * between corresponding values.
	 * @return
	 */
    private Matrix generateObjectSimilarityMatrix(List<ArtworkDO> artworks) {
        int size = artworks.size();
        Matrix distanceMatrix = new FlexCompColMatrix(size, size);
        for (int i1 = 0; i1 < size; i1++) {
            ArtworkDO artwork1 = artworks.get(i1);
            List<Integer> vector1 = artwork1.getVector();
            for (int i2 = 0; i2 < size; i2++) {
                ArtworkDO artwork2 = artworks.get(i2);
                List<Integer> vector2 = artwork2.getVector();
                double distance = 0;
                for (int i = 0; i < vector1.size(); i++) {
                    int x = vector1.get(i);
                    int y = vector2.get(i);
                    double square = Math.pow(x - y, 2);
                    distance += square;
                }
                distanceMatrix.add(i1, i2, distance);
            }
        }
        return distanceMatrix;
    }

    /**
	 * @param categoryDAO Dependency Injection
	 */
    public void setCategoryDAO(ICategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }
}
