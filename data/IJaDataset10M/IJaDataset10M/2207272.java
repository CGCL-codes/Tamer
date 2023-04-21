package net.sf.gateway.mef.configuration.KeyStore;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import net.sf.gateway.mef.configuration.ConfigGUIJFrame;

public class KeyStoreAliasFocusListener implements FocusListener {

    private ConfigGUIJFrame myParent;

    public KeyStoreAliasFocusListener(ConfigGUIJFrame myParent) {
        super();
        this.myParent = myParent;
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        myParent.getConfig().getKeyStore().setKeyStoreAlias(((KeyStoreAliasJTextField) e.getComponent()).getText());
    }
}
