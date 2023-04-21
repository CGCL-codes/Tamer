package libomv.types;

import libomv.types.UUID;
import libomv.types.Vector3d;

/** Holds group information on an individual profile pick */
public final class ProfilePick {

    public UUID PickID;

    public UUID CreatorID;

    public boolean TopPick;

    public UUID ParcelID;

    public String Name;

    public String Desc;

    public UUID SnapshotID;

    public String User;

    public String OriginalName;

    public String SimName;

    public Vector3d PosGlobal;

    public int SortOrder;

    public boolean Enabled;
}
