package cz.cuni.mff.ksi.jinfer.attrstats.utils;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.FileCharacteristics;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.InputFile;

/**
 * A few constants used throughout the project.
 *
 * @author vektor
 */
public final class Constants {

    private Constants() {
    }

    public static final InputFile GRAPH = new InputFile("C:\\Users\\vektor\\Dropbox\\school\\jInfer\\example-data\\keys\\graph.xml", FileCharacteristics.ARTIFICIAL);

    public static final String TEST_DATA_ROOT = "C:\\Users\\vektor\\Dropbox\\school\\Master's Thesis\\test-data-official";

    public static final String TEST_OUTPUT_ROOT = "C:\\Users\\vektor\\Documents\\test-output";
}
