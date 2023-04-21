package de.mpiwg.vspace.extension;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import de.mpiwg.vspace.extension.interfaces.ICellEditorFactory;
import de.mpiwg.vspace.extension.interfaces.IPropertyExtension;
import de.mpiwg.vspace.extension.interfaces.ISavableItem;
import de.mpiwg.vspace.extension.interfaces.IVSpacePreferencePage;

/**
 * This singleton class manages the property extensions.
 * 
 * @author Julia Damerow
 *
 */
public class PropertyExtensionProvider {

    public static PropertyExtensionProvider INSTANCE = new PropertyExtensionProvider();

    private List<IPropertyExtension> propertyExtensions;

    private boolean cellEditorInited = false;

    private List<ICellEditorFactory> cellEditorFactories;

    private PropertyExtensionProvider() {
        propertyExtensions = new ArrayList<IPropertyExtension>();
        IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(ExtensionPointIDs.PROPERTIESEXTENSION_ID);
        for (IConfigurationElement e : configs) {
            Object o = null;
            try {
                o = e.createExecutableExtension("class");
            } catch (CoreException e1) {
                e1.printStackTrace();
                continue;
            }
            if (o != null) {
                propertyExtensions.add((IPropertyExtension) o);
            }
        }
    }

    /**
	 * Get all registered <code>FieldEditorPreferencePage</code>s.
	 * @return a list with all registered <code>FieldEditorPreferencePage</code>s
	 */
    public List<IVSpacePreferencePage> getVSpacePreferencePages() {
        List<IVSpacePreferencePage> pages = new ArrayList<IVSpacePreferencePage>();
        for (IPropertyExtension pe : propertyExtensions) {
            List<IVSpacePreferencePage> prefPages = pe.getVSpacePreferencePages();
            if (prefPages != null) pages.addAll(prefPages);
        }
        return pages;
    }

    public CellEditor getCellEditor(EStructuralFeature type, Object object) {
        if (!cellEditorInited) {
            cellEditorFactories = new ArrayList<ICellEditorFactory>();
            for (IPropertyExtension pe : propertyExtensions) {
                ICellEditorFactory factory = pe.getCellEditorFactory();
                if (factory != null) cellEditorFactories.add(factory);
            }
            cellEditorInited = true;
        }
        CellEditor cellEditor = null;
        for (ICellEditorFactory f : cellEditorFactories) {
            CellEditor ce = f.getCellEditor(type, object);
            if (ce != null) cellEditor = ce;
        }
        return cellEditor;
    }

    public List<ISavableItem> getSavableItems(List<EClass> types) {
        if (types == null) return null;
        List<ISavableItem> savableItems = new ArrayList<ISavableItem>();
        for (IPropertyExtension pe : propertyExtensions) {
            for (EClass eclass : types) {
                List<ISavableItem> items = pe.getSavableItems(eclass);
                if (items != null) savableItems.addAll(items);
            }
        }
        return savableItems;
    }
}
