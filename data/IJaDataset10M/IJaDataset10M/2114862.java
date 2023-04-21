package base.value.userobject;

public class CNull extends CUserObject {

    public static CNull Create() {
        CNull Null = new CNull();
        LastID++;
        Null.ID = LastID;
        Null.BaseData = new CBaseData();
        return Null;
    }

    @Override
    public boolean ToBoolean() {
        return false;
    }
}
