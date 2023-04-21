package de.offis.faint.recognition.plugins.eigenfaces;

import java.util.ArrayList;
import javax.swing.SwingWorker;
import de.offis.faint.controller.MainController;
import de.offis.faint.global.Constants;
import de.offis.faint.global.Utilities;
import de.offis.faint.model.Region;

/**
 * @author maltech
 *
 */
public class EigenfaceBuilder {

    private EigenfaceRecognition eigenfaceRecognition;

    /**
	 * Constructor.
	 * 
	 * @param eigenfaceRecognition
	 */
    public EigenfaceBuilder(EigenfaceRecognition eigenfaceRecognition) {
        this.eigenfaceRecognition = eigenfaceRecognition;
    }

    /**
	 * Calculates a new set of Eigenfaces and then replaces the old set.
	 *
	 */
    public void updateEigenfaces() {
        Region[] knownFaces = MainController.getInstance().getFaceDB().getTrainingImages();
        int numTrainingImages = knownFaces.length;
        int lastNumTrainingImages = eigenfaceRecognition.lastNumberOfTrainingImages;
        Integer step = eigenfaceRecognition.rebuildFaceSpace;
        if ((step == null && Math.abs(numTrainingImages - lastNumTrainingImages) > 0) || (step != null && Math.abs(numTrainingImages - lastNumTrainingImages) > step)) {
            if (knownFaces.length > 0) {
                eigenfaceRecognition.updateIsRunning = true;
                eigenfaceRecognition.updateView();
                double factor = 1.0 / knownFaces.length;
                byte[] averageFace = new byte[EigenfaceRecognition.VECTORLENGTH];
                double[] tempAverageFace = new double[EigenfaceRecognition.VECTORLENGTH];
                byte[][] faceVectors = new byte[knownFaces.length][];
                for (int r = 0; r < knownFaces.length; r++) {
                    byte[] intensityImage = Utilities.bufferedImageToIntensityArray(knownFaces[r].toThumbnail(Constants.FACE_THUMBNAIL_SIZE));
                    for (int i = 0; i < tempAverageFace.length; i++) {
                        tempAverageFace[i] += ((double) (intensityImage[i] & 0xFF)) * factor;
                    }
                    faceVectors[r] = intensityImage;
                }
                for (int i = 0; i < tempAverageFace.length; i++) {
                    averageFace[i] = (byte) (Math.round(tempAverageFace[i]));
                }
                short[][] distances = new short[faceVectors.length][EigenfaceRecognition.VECTORLENGTH];
                for (int i = 0; i < faceVectors.length; i++) {
                    for (int j = 0; j < EigenfaceRecognition.VECTORLENGTH; j++) {
                        distances[i][j] = (short) ((short) (faceVectors[i][j] & 0xFF) - (short) (averageFace[j] & 0xFF));
                    }
                }
                CovarianceMatrix matrix = new CovarianceMatrix(distances);
                ArrayList<double[]> eigenFaces = new ArrayList<double[]>();
                int numEigenfaces = distances.length;
                if (eigenfaceRecognition.maxEigenfaces != null && eigenfaceRecognition.maxEigenfaces < numEigenfaces) numEigenfaces = eigenfaceRecognition.maxEigenfaces;
                for (int i = 0; i < numEigenfaces; i++) {
                    eigenFaces.add(matrix.getEigenVector(i));
                }
                eigenfaceRecognition.updateIsRunning = false;
                eigenfaceRecognition.updateData(averageFace, eigenFaces, numTrainingImages);
                eigenfaceRecognition.updateView();
            }
        }
    }

    /**
	 * Initiates an update of the Eigenfaces in a second thread.
	 */
    public void updateEigenfacesInBackground() {
        (new BackgroundWorker()).execute();
    }

    class BackgroundWorker extends SwingWorker {

        @Override
        protected Object doInBackground() throws Exception {
            updateEigenfaces();
            return null;
        }
    }
}
