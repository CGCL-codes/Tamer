package hidb2.kern;

import java.sql.PreparedStatement;

/**
 * Set of Prepared Statement that shall be kept at DB connection level.
 * 
 * To respect some aspect of this generic design, they've to be created at
 * AttrExtend level. (i.e. AttrImage for example)
 *
 */
public class StatKit {

    public PreparedStatement fullSelect;

    public PreparedStatement insert;

    public PreparedStatement update;

    public PreparedStatement delete;

    /** Count datapaths' ID that reference a DATAPATH - May be null if not applicable */
    public PreparedStatement datapathReference;

    public PreparedStatement datapathReplace;
}
