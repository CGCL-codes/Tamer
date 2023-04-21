package scheduler.fixtures;

import fitlibrary.SetUpFixture;
import se392.ateam2006.resourcemgmt.ResourceMgmtRemote;

/**
 * Sets up a room's owner
 * @author Ateam (Matthew Bennett, Claire Melton, Shingai Manyiwa, John Adderley)
 * @version 25/03/07
 */
public class SetUpVenueOwnership extends SetUpFixture {

    private ResourceMgmtRemote rmr;

    /** Creates a new instance of SetUpVenueOwnership */
    public SetUpVenueOwnership(ResourceMgmtRemote rmr) {
        this.rmr = rmr;
    }

    public void venueOwner(String venue, String owner) {
        rmr.setRoomOwner(venue, owner);
    }
}
