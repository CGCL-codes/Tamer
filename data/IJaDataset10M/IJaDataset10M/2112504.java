package gameserver.model.academy;

import java.util.ArrayList;
import java.util.List;

public class AcademyBox {

    public AcademyBox() {
    }

    public List getBoxTemplate() {
        if (boxSpawnList == null) boxSpawnList = new ArrayList();
        return boxSpawnList;
    }

    public int getNpcId() {
        return npcid;
    }

    public int getMapId() {
        return map;
    }

    public int getPool() {
        return pool;
    }

    protected List boxSpawnList;

    protected int npcid;

    protected int map;

    protected int pool;
}
