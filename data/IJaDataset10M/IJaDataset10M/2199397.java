package be.stijn.moviez;

import java.io.File;

public class Runner {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        IndexingService service = new IndexingService();
        service.setDirectory(new File("L:/Movies/Nog Niet Gezien"));
        service.index();
    }
}
