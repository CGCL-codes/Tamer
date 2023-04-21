package klava.examples.newsgatherer;

import klava.KString;
import klava.KlavaException;
import klava.Locality;
import klava.LogicalLocality;
import klava.PhysicalLocality;
import klava.Tuple;
import klava.topology.KlavaProcess;

/**
 * A migrating process encharged of finding a specific information on a
 * distributed database.
 * 
 * @author Lorenzo Bettini
 * @version $Revision: 1.1 $
 */
public class NewsGatherer extends KlavaProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * The item to find in the database.
     */
    KString itemToFind;

    /**
     * Where to return results.
     */
    Locality homeLoc;

    /**
     * The locality of the screen of the home locality.
     */
    Locality homeScreen;

    /**
     * @param itemToFind
     * @param homeLoc
     * @param homeScreen
     */
    public NewsGatherer(KString itemToFind, Locality homeLoc, Locality homeScreen) {
        this.itemToFind = itemToFind;
        this.homeLoc = homeLoc;
        this.homeScreen = homeScreen;
    }

    /**
     * @see klava.topology.KlavaProcess#executeProcess()
     */
    @Override
    public void executeProcess() throws KlavaException {
        KString itemVal = new KString();
        Locality nextLoc = new PhysicalLocality();
        LogicalLocality screen = new LogicalLocality("screen");
        out(new Tuple("searching for " + itemToFind + " at " + getPhysical(self) + "\n"), homeScreen);
        if (read_nb(new Tuple(itemToFind, itemVal), self)) {
            out(new Tuple("found item " + itemVal + "\n"), screen);
            out(new Tuple(itemToFind, itemVal), homeLoc);
            return;
        } else if (read_nb(new Tuple(itemToFind, nextLoc), self)) {
            out(new Tuple("found next locality " + nextLoc + "\n"), screen);
            migrate(nextLoc);
        } else {
            out(new Tuple(itemToFind, "failed"), homeLoc);
            return;
        }
    }
}
