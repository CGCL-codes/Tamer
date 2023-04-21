package net.sourceforge.hlm.visual.templates.text;

import net.sourceforge.hlm.generic.*;

public abstract interface HierarchicalTextTemplateItem extends TextTemplateItem {

    SelectableList<TextTemplateItem> getItems();
}
