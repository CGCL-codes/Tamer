package org.eclipse.pde.nls.internal.ui.editor;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

public class LocalizationEditorInputFactory implements IElementFactory {

    public static final String FACTORY_ID = "org.eclipse.pde.nls.ui.LocalizationEditorInputFactory";

    public LocalizationEditorInputFactory() {
    }

    public IAdaptable createElement(IMemento memento) {
        return new LocalizationEditorInput();
    }
}
