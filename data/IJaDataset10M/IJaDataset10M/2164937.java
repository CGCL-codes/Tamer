package net.sf.l2j.gameserver.serverpackets;

import java.util.List;
import javolution.util.FastList;

/**
 * <code>
 * sample
 *
 * a4
 * 4d000000 01000000 98030000 			Attack Aura, level 1, sp cost
 * 01000000 							number of requirements
 * 05000000 47040000 0100000 000000000	   1 x spellbook advanced ATTACK                                                 .
 * </code>
 *
 * format   ddd d (dddd)
 *
 * @version $Revision: 1.3.2.1.2.4 $ $Date: 2005/03/27 15:29:39 $
 */
public class AcquireSkillInfo extends L2GameServerPacket {

    private static final String _S__A4_AQUIRESKILLINFO = "[S] 91 AcquireSkillInfo";

    private List<Req> _reqs;

    private int _id, _level, _spCost, _mode;

    private class Req {

        public int itemId;

        public int count;

        public int type;

        public int unk;

        public Req(int pType, int pItemId, int pCount, int pUnk) {
            itemId = pItemId;
            type = pType;
            count = pCount;
            unk = pUnk;
        }
    }

    public AcquireSkillInfo(int id, int level, int spCost, int mode) {
        _reqs = new FastList<Req>();
        _id = id;
        _level = level;
        _spCost = spCost;
        _mode = mode;
    }

    public void addRequirement(int type, int id, int count, int unk) {
        _reqs.add(new Req(type, id, count, unk));
    }

    @Override
    protected final void writeImpl() {
        writeC(0x91);
        writeD(_id);
        writeD(_level);
        writeD(_spCost);
        writeD(_mode);
        writeD(_reqs.size());
        for (Req temp : _reqs) {
            writeD(temp.type);
            writeD(temp.itemId);
            writeD(temp.count);
            writeD(temp.unk);
        }
    }

    @Override
    public String getType() {
        return _S__A4_AQUIRESKILLINFO;
    }
}
