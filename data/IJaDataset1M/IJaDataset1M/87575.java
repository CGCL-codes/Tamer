package de.mpiwg.vspace.languages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import de.mpiwg.vspace.extension.interfaces.ICellEditorFactory;
import de.mpiwg.vspace.languages.celleditor.MultiLanguageTextDialogCellEditor;
import de.mpiwg.vspace.languages.language.Language;
import de.mpiwg.vspace.languages.providers.EntryProvider;
import de.mpiwg.vspace.languages.providers.EntryProviderManager;
import de.mpiwg.vspace.languages.providers.LanguagePropertyProvider;

public class CellEditorFactory implements ICellEditorFactory {

    public CellEditor getCellEditor(EStructuralFeature type, Object object) {
        if (SupportedTypesManager.INSTANCE.isSupportedFeature(type)) {
            if (!(object instanceof EObject)) return null;
            List<Language> languages = LanguagePropertyProvider.INSTANCE.getLanguages();
            Map<String, String> oldValues = new HashMap<String, String>();
            for (Language l : languages) {
                EntryProvider ep = EntryProviderManager.INSTANCE.getEntryProvider((EObject) object, l);
                if (!(type instanceof EAttribute)) return null;
                String entryId = EntryProviderManager.INSTANCE.getEntryId((EObject) object, (EAttribute) type);
                oldValues.put(l.getLanguageCode(), ep.getEntry(entryId));
            }
            MultiLanguageTextDialogCellEditor ce = new MultiLanguageTextDialogCellEditor(type.getName());
            ce.setOldValues(oldValues);
            return ce;
        }
        return null;
    }
}
