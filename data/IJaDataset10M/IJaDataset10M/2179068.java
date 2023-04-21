package digitalsimulation;

/**
 * A DeviceBase a különböző áramköri elemek legősibb osztálya. Ebből származtatjuk az összes hálózati elemet.
 */
public abstract class DeviceBase {

    protected String name;

    DeviceBase(String name_tmp) {
        name = name_tmp;
    }

    public String getName() {
        return name;
    }
}
