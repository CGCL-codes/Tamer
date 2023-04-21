package tippmeister.template;

import java.io.File;
import java.util.ArrayList;
import tippmeister.utilities.TXTFilenameFilter;

public class Template {

    /**
	 * Reads the available cathegories in the "templates" directory.
	 * A cathegorie equivalents to a directory.
	 * 
	 * @return ArrayList with String elements with the names of the templates
	 */
    public static ArrayList<String> getTemplateCathegories() {
        ArrayList<String> cathegories = new ArrayList<String>();
        File directory = new File("./templates");
        for (File f : directory.listFiles()) {
            if (f.isDirectory()) {
                cathegories.add(f.getName());
            }
        }
        return cathegories;
    }

    /**
	 * Returns all template files for one cathegory.
	 * 
	 * @param cathegory Cathegory name (the same as the directory name)
	 * @return ArrayList with Sting elements with the template filenames
	 */
    public static ArrayList<String> getCathegoryNames(String cathegory) {
        ArrayList<String> cathegories = new ArrayList<String>();
        File direcotry = new File("./templates/" + cathegory);
        for (File f : direcotry.listFiles(new TXTFilenameFilter())) {
            cathegories.add(f.getName());
        }
        return cathegories;
    }
}
