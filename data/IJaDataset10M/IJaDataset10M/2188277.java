package uk.ac.aber.Blockmation.Tests;

import java.awt.image.BufferedImage;
import uk.ac.aber.Blockmation.MovieModel;

public class MovieModelTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MovieModel model = new MovieModel();
        BufferedImage bim = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        int frames = 10;
        for (int i = 0; i < frames; i++) {
            model.appendFrame(bim);
        }
        System.out.println("Worked");
    }
}
