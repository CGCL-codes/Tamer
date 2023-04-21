package fr.esrf.tango.pogo.outline;

import org.eclipse.xtext.ui.common.editor.outline.actions.DefaultContentOutlineNodeAdapterFactory;

public class PogoDeviceClassDslOutlineNodeAdapterFactory extends DefaultContentOutlineNodeAdapterFactory {

    private static final Class[] types = {};

    @Override
    public Class[] getAdapterList() {
        return types;
    }
}
