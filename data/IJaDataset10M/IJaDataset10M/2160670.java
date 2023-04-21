package com.togethersoft.modules.togmap;

import com.togethersoft.openapi.ide.IdeContext;
import com.togethersoft.openapi.ide.inspector.util.editors.SwingComboBoxEditor;
import com.togethersoft.openapi.ide.inspector.util.property.RwiInspectorStringProperty;
import com.togethersoft.openapi.ide.inspector.util.table.PropertyTableComponentUI;
import com.togethersoft.openapi.util.RwiElementsUtil;
import com.togethersoft.openapi.rwi.RwiElement;
import com.togethersoft.openapi.ide.inspector.IdeInspectorPropertySetComponent;
import javax.swing.DefaultComboBoxModel;
import com.togethersoft.openapi.ide.inspector.IdeInspectorBuilder;
import com.togethersoft.openapi.ide.inspector.IdeInspector;
import com.togethersoft.openapi.ide.config.IdeConfigManager;
import com.togethersoft.openapi.ide.config.IdeConfigManagerAccess;
import com.togethersoft.openapi.ide.config.IdeConfig;
import com.togethersoft.openapi.ide.inspector.IdeInspectorProperty;

/**
 * Describe class <code>CastorClazzInspector</code> here.
 *
 * @author <a href="mailto:markus@blues">Markus Blaurock</a>
 * @version $Revision: 1.2 $
 */
public class CastorClazzInspector implements IdeInspectorBuilder {

    /**
	 * Describe <code>buildInspector</code> method here.
	 *
	 * @param context an <code>IdeContext</code> value
	 * @param inspector an <code>IdeInspector</code> value
	 * @return an <code>IdeInspector</code> value
	 */
    public IdeInspector buildInspector(IdeContext context, IdeInspector inspector) {
        IdeConfigManager configManager = IdeConfigManagerAccess.getConfigManager();
        IdeConfig config = configManager.getConfig("$script");
        if (!RwiElementsUtil.checkProperty(context, "$shapeType", "Class")) {
            return inspector;
        } else {
            RwiElement rwiElements[] = RwiElementsUtil.getRwiElements(context);
            IdeInspectorPropertySetComponent page = new IdeInspectorPropertySetComponent(context);
            page.setName("CastorMapper");
            page.setService(com.togethersoft.openapi.ide.util.UIComponentService.class, new PropertyTableComponentUI(page));
            inspector.addComponent(page, null, 10.0);
            IdeInspectorProperty property = new RwiInspectorStringProperty(rwiElements, Constants.ACCESSMETHOD);
            property.setName("class access");
            SwingComboBoxEditor editor = new SwingComboBoxEditor(new DefaultComboBoxModel(new String[] { "shared", "read-only", "exclusive", "db-locked" }));
            property.setPropertyEditor(editor);
            page.addProperty(property, null);
            property = new RwiInspectorStringProperty(rwiElements, Constants.CACHETYPE);
            property.setName("cache type");
            editor = new SwingComboBoxEditor(new DefaultComboBoxModel(new String[] { "none", "count-limited", "time-limited", "unlimited" }));
            property.setPropertyEditor(editor);
            page.addProperty(property, null);
            property = new RwiInspectorStringProperty(rwiElements, Constants.CACHECAPACITY);
            property.setName("cache capacity");
            page.addProperty(property, null);
            return inspector;
        }
    }
}
