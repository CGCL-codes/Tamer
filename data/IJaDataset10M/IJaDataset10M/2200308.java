package mou.net2.dht;

import java.io.File;
import org.apache.commons.lang.RandomStringUtils;
import mou.Main;
import mou.Modul;
import mou.Subsystem;
import mou.net2.dht.simple.LocalDHT;

public class DhtModul extends Modul {

    private DHT dht;

    public DhtModul(Subsystem parent) {
        super(parent);
    }

    @Override
    public String getModulName() {
        return "DHT Subsystem";
    }

    @Override
    protected void shutdownIntern() {
        if (dht != null) dht.close();
    }

    @Override
    protected File getPreferencesFile() {
        return null;
    }

    @Override
    protected void startModulIntern() throws Exception {
        if (Main.isOnlineMode()) {
            dht = DHT.getInstance(RandomStringUtils.randomAscii(15).getBytes(), getLogger());
        } else {
            dht = new LocalDHT();
        }
    }

    public DHT getDht() {
        return dht;
    }
}
