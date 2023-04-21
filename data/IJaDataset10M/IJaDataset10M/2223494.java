package l1j.server.server.clientpackets;

import java.util.logging.Logger;
import l1j.server.server.ClientThread;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;

public class C_SelectTarget extends ClientBasePacket {

    private static final String C_SELECT_TARGET = "[C] C_SelectTarget";

    private static Logger _log = Logger.getLogger(C_SelectTarget.class.getName());

    public C_SelectTarget(byte abyte0[], ClientThread clientthread) throws Exception {
        super(abyte0);
        int petId = readD();
        int type = readC();
        int targetId = readD();
        L1PetInstance pet = (L1PetInstance) L1World.getInstance().findObject(petId);
        L1Character target = (L1Character) L1World.getInstance().findObject(targetId);
        if (pet != null && target != null) {
            if (target instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) target;
                if (pc.checkNonPvP(pc, pet)) {
                    return;
                }
            }
            pet.setMasterTarget(target);
        }
    }

    @Override
    public String getType() {
        return C_SELECT_TARGET;
    }
}
