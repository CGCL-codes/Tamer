package playground.anhorni.locationchoice.preprocess.plans.modifications;

import java.util.List;
import java.util.Vector;

public class NOGATypes {

    public String[] shopGrocery = { "B015211A", "B015211B", "B015211C", "B015211D", "B015211E", "B015212A", "B015221A", "B015222A", "B015223A", "B015224A", "B015225A", "B015227A", "B015227B" };

    public String[] shopNonGrocery = { "B015211A", "B015211B", "B015211C", "B015211D", "B015211E", "B015212A", "B015212B", "B015226A", "B015231A", "B015232A", "B015233A", "B015233B", "B015241A", "B015242A", "B015242B", "B015242C", "B015242D", "B015242E", "B015243A", "B015243B", "B015244A", "B015244B", "B015244C", "B015245A", "B015245B", "B015245C", "B015245D", "B015245E", "B015246A", "B015246B", "B015247A", "B015247B", "B015247C", "B015248A", "B015248B", "B015248C", "B015248D", "B015248E", "B015248F", "B015248G", "B015248H", "B015248I", "B015248J", "B015248K", "B015248L", "B015248M", "B015248N", "B015248O", "B015248P", "B015250A", "B015250B", "B015262A", "B015263A", "B015271A", "B015272A", "B015273A", "B015274A" };

    public List<String> getGroceryTypes() {
        List<String> groceryTypes = new Vector<String>();
        for (int i = 0; i < shopGrocery.length; i++) {
            groceryTypes.add(shopGrocery[i]);
        }
        return groceryTypes;
    }

    public List<String> getNonGroceryTypes() {
        List<String> nongroceryTypes = new Vector<String>();
        for (int i = 0; i < shopNonGrocery.length; i++) {
            nongroceryTypes.add(shopNonGrocery[i]);
        }
        return nongroceryTypes;
    }
}
