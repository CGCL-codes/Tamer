package br.com.nix.beans.obj;

import br.com.nix.beans.ExtendedBeanInfo;
import br.com.nix.configuration.Configuration;
import br.com.nix.editors.DataListPropertyEditor;
import br.com.nix.editors.FlagsPropertyEditor;
import br.com.nix.renderers.DataListRenderer;
import br.com.nix.renderers.FlagsRenderer;

public class ProtoObjBeanInfo extends ExtendedBeanInfo {

    public ProtoObjBeanInfo() {
        super(Obj.class);
        addProperty("vnum", "General");
        addProperty("name", "General");
        addProperty("shortDesc", "General");
        addProperty("description", "General");
        addProperty("actionDesc", "General");
        addProperty("layers", "General", LayersEditor.class, LayersRenderer.class);
        addProperty("weight", "General");
        addProperty("cost", "General");
        addProperty("rent", "General");
        addProperty("itemType", "General", ItemTypeEditor.class, ItemTypeRenderer.class);
        addProperty("value0", "Values");
        addProperty("value1", "Values");
        addProperty("value2", "Values");
        addProperty("value3", "Values");
        addProperty("value4", "Values");
        addProperty("value5", "Values");
        addProperty("extraFlags", "General", ExtraFlagsEditor.class, ExtraFlagsRenderer.class);
        addProperty("wearFlags", "General", WearFlagsEditor.class, WearFlagsRenderer.class);
        addProperty("limit", "Reset");
        addProperty("hide", "Reset");
        addProperty("position", "Reset");
        addProperty("type", "Reset");
    }

    public static class ItemTypeEditor extends DataListPropertyEditor {

        public ItemTypeEditor() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.itemType").toArray());
        }
    }

    @SuppressWarnings("serial")
    public static class ItemTypeRenderer extends DataListRenderer {

        public ItemTypeRenderer() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.itemType").toArray());
        }
    }

    public static class ExtraFlagsEditor extends FlagsPropertyEditor {

        public ExtraFlagsEditor() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.extraFlags"));
        }
    }

    @SuppressWarnings("serial")
    public static class ExtraFlagsRenderer extends FlagsRenderer {

        public ExtraFlagsRenderer() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.extraFlags"));
        }
    }

    public static class WearFlagsEditor extends FlagsPropertyEditor {

        public WearFlagsEditor() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.wearFlags"));
        }
    }

    @SuppressWarnings("serial")
    public static class WearFlagsRenderer extends FlagsRenderer {

        public WearFlagsRenderer() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.wearFlags"));
        }
    }

    @SuppressWarnings("serial")
    public static class LayersRenderer extends FlagsRenderer {

        public LayersRenderer() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.layers"));
        }
    }

    public static class LayersEditor extends FlagsPropertyEditor {

        public LayersEditor() {
            super();
            setAvailableValues(Configuration.getConfiguration().getOptions("object.layers"));
        }
    }
}
