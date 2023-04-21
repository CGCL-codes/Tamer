package org.mitre.rt.server.database.dao;

import java.util.logging.Logger;
import org.mitre.rt.rtclient.OrderedSharedIdsType;

/**
 *
 * @author BWORRELL
 */
public class ProfileOrderNumbersDAO extends AppComponentVersionedItemTypeDAO {

    private static final Logger logger = Logger.getLogger(ProfileOrderNumbersDAO.class.getPackage().getName());

    public ProfileOrderNumbersDAO() {
        super("Profile_Order", OrderedSharedIdsType.class);
    }
}
