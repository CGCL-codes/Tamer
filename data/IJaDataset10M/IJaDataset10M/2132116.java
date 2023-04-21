package br.com.qotsa.j2me.btsoftcontrol.bluetooth.screen;

import br.com.qotsa.j2me.btsoftcontrol.language.ResourceFactory;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Screen;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Francisco Guimar�es
 */
class SearchBluetoothDeviceForm extends SearchBluetoothDeviceScreen {

    private StringItem counter;

    private Form frmSearchDevice;

    SearchBluetoothDeviceForm(DiscoveryAgent discoveryAgent, DiscoveryListener discoveryListener) {
        super(discoveryAgent, discoveryListener);
    }

    public void paintScreen() {
        frmSearchDevice = new Form(ResourceFactory.getResource().getSearchingMessage());
        Gauge gauge = new Gauge("", false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING);
        counter = new StringItem(ResourceFactory.getResource().getFoundMessage() + ": ", "0");
        frmSearchDevice.append(counter);
        frmSearchDevice.append(gauge);
    }

    public Screen getScreen() {
        return frmSearchDevice;
    }

    void changeCounterNumber(int number) {
        counter.setText(String.valueOf(number));
    }
}
