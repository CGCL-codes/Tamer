package delphorm.utils;

import java.io.FileNotFoundException;

public class DatabaseCreator {

    /**
	 * @param args
	 * @throws FileNotFoundException 
	 */
    public static void main(String[] args) throws FileNotFoundException {
        DatabaseUtils.exportSchema("/home/jerome/foo");
    }
}
