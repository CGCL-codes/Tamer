package net.sf.launch4j.formimpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.launch4j.binding.Binding;
import net.sf.launch4j.binding.Bindings;
import net.sf.launch4j.config.Config;
import net.sf.launch4j.config.ConfigPersister;
import net.sf.launch4j.form.HeaderForm;

/**
 * @author Copyright (C) 2006 Grzegorz Kowal
 */
public class HeaderFormImpl extends HeaderForm {

    private final Bindings _bindings;

    public HeaderFormImpl(Bindings bindings) {
        _bindings = bindings;
        _bindings.add("headerTypeIndex", new JRadioButton[] { _guiHeaderRadio, _consoleHeaderRadio }).add("headerObjects", "customHeaderObjects", _headerObjectsCheck, _headerObjectsTextArea).add("libs", "customLibs", _libsCheck, _libsTextArea);
        _guiHeaderRadio.addChangeListener(new HeaderTypeChangeListener());
        _headerObjectsCheck.addActionListener(new HeaderObjectsActionListener());
        _libsCheck.addActionListener(new LibsActionListener());
    }

    private class HeaderTypeChangeListener implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            Config c = ConfigPersister.getInstance().getConfig();
            c.setHeaderType(_guiHeaderRadio.isSelected() ? Config.GUI_HEADER : Config.CONSOLE_HEADER);
            if (!_headerObjectsCheck.isSelected()) {
                Binding b = _bindings.getBinding("headerObjects");
                b.put(c);
            }
        }
    }

    private class HeaderObjectsActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!_headerObjectsCheck.isSelected()) {
                ConfigPersister.getInstance().getConfig().setHeaderObjects(null);
                Binding b = _bindings.getBinding("headerObjects");
                b.put(ConfigPersister.getInstance().getConfig());
            }
        }
    }

    private class LibsActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!_libsCheck.isSelected()) {
                ConfigPersister.getInstance().getConfig().setLibs(null);
                Binding b = _bindings.getBinding("libs");
                b.put(ConfigPersister.getInstance().getConfig());
            }
        }
    }
}
