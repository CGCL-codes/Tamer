package nakayo.gameserver.dataholders.loadingutils.adapters;

import nakayo.gameserver.model.items.NpcEquippedGear;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Luno
 */
public class NpcEquippedGearAdapter extends XmlAdapter<NpcEquipmentList, NpcEquippedGear> {

    @Override
    public NpcEquipmentList marshal(NpcEquippedGear v) throws Exception {
        return null;
    }

    @Override
    public NpcEquippedGear unmarshal(NpcEquipmentList v) throws Exception {
        return new NpcEquippedGear(v);
    }
}
