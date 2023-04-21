package ra.lajolla.experimental.engine;

import java.util.ArrayList;
import ra.lajolla.SequenceDB;
import ra.lajolla.transformation.IFileToStringTranslator;
import ra.lajolla.transformation.protein.PDBProteinTranslator;
import ra.lajolla.utilities.FileOperationsManager2;

public class LaJollaEngine {

    /**
	 * Reads and sets the properties.
	 */
    private LaJollaEngine() {
    }

    /** 
	 * Singleton Class.
	 */
    private static class SingletonHolder {

        /** The only instance of this Singleton. */
        private static LaJollaEngine instance = new LaJollaEngine();
    }

    /** 
	 * Method to return the singleton.
	 * @return PropertiesManager The Singleton of this class.
	 */
    public static LaJollaEngine getInstance() {
        return SingletonHolder.instance;
    }

    public ArrayList<String> search(final String name) {
        ArrayList<String> returnMe = new ArrayList<String>();
        returnMe.add("alles okay... klasse aufgerufen und ergebnis übergeben");
        returnMe.add("das ist jetzt noch langweilig, aber bald kommen hier echte ergebnisse wie");
        returnMe.add("pdb1 pdb2 RMSD ähnlickeitImLaJollaScore JMOLView");
        return returnMe;
    }
}
