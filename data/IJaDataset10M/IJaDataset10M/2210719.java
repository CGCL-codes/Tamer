package com.ecmdeveloper.plugin.search.editor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import com.ecmdeveloper.plugin.search.Activator;
import com.ecmdeveloper.plugin.search.model.AndContainer;
import com.ecmdeveloper.plugin.search.model.ClassTest;
import com.ecmdeveloper.plugin.search.model.Comparison;
import com.ecmdeveloper.plugin.search.model.FreeText;
import com.ecmdeveloper.plugin.search.model.FullTextQuery;
import com.ecmdeveloper.plugin.search.model.InFolderTest;
import com.ecmdeveloper.plugin.search.model.InSubFolderTest;
import com.ecmdeveloper.plugin.search.model.InTest;
import com.ecmdeveloper.plugin.search.model.MultiValueInTest;
import com.ecmdeveloper.plugin.search.model.NotContainer;
import com.ecmdeveloper.plugin.search.model.NullTest;
import com.ecmdeveloper.plugin.search.model.OrContainer;
import com.ecmdeveloper.plugin.search.model.QueryElement;
import com.ecmdeveloper.plugin.search.model.QueryElementDescription;
import com.ecmdeveloper.plugin.search.model.ThisInFolderTest;
import com.ecmdeveloper.plugin.search.model.ThisInTreeTest;
import com.ecmdeveloper.plugin.search.model.WildcardTest;

/**
 * 
 * @author ricardo.belfor
 * 
 */
public class QueryPaletteFactory extends org.eclipse.ui.plugin.AbstractUIPlugin {

    public static final String CLASS_TEST_ENTRY = "CLASS_TEST_ENTRY";

    public static final String IN_FOLDER_TEST_ENTRY = "IN_FOLDER_TEST_ENTRY";

    public static final String IN_SUBFOLDER_TEST_ENTRY = "IN_SHBFOLDER_TEST_ENTRY";

    public static final String THIS_IN_FOLDER_TEST_ENTRY = "THIS_IN_FOLDER_TEST_ENTRY";

    public static final String THIS_IN_TREE_TEST_ENTRY = "THIS_IN_TREE_TEST_ENTRY";

    public static final String CMIS_ONLY_ENTRIES[] = { THIS_IN_FOLDER_TEST_ENTRY, THIS_IN_TREE_TEST_ENTRY };

    public static final String CE_ONLY_ENTRIES[] = { CLASS_TEST_ENTRY, IN_FOLDER_TEST_ENTRY, IN_SUBFOLDER_TEST_ENTRY };

    public static final String QUERY_COMPONENTS_DRAWER = "QUERY_COMPONENTS_DRAWER";

    private static QueryPaletteFactory singleton;

    @SuppressWarnings("unchecked")
    private static List createCategories(PaletteRoot root, QueryProxy queryProxy) {
        List categories = new ArrayList();
        categories.add(createContainersDrawer(queryProxy));
        categories.add(createQueryPartsDrawer(queryProxy));
        return categories;
    }

    @SuppressWarnings("unchecked")
    private static PaletteContainer createContainersDrawer(QueryProxy queryProxy) {
        PaletteDrawer drawer = new PaletteDrawer("Containers", Activator.getImageDescriptor(QueryIcons.CONTAINER));
        List entries = new ArrayList();
        CombinedTemplateCreationEntry combined;
        combined = createEntry("AND", "Logical AND container", QueryIcons.AND_CONTAINER, QueryIcons.AND_CONTAINER_LARGE, AndContainer.class, queryProxy);
        entries.add(combined);
        combined = createEntry("OR", "Logical OR container", QueryIcons.OR_CONTAINER, QueryIcons.OR_CONTAINER_LARGE, OrContainer.class, queryProxy);
        entries.add(combined);
        combined = createEntry("NOT", "Logical NOT container", QueryIcons.NOT_CONTAINER, QueryIcons.NOT_CONTAINER_LARGE, NotContainer.class, queryProxy);
        entries.add(combined);
        drawer.addAll(entries);
        return drawer;
    }

    @SuppressWarnings("unchecked")
    private static PaletteContainer createQueryPartsDrawer(QueryProxy queryProxy) {
        PaletteDrawer drawer = new PaletteDrawer("Query Components", Activator.getImageDescriptor(QueryIcons.QUERY_COMPONENT_ICON));
        drawer.setId(QUERY_COMPONENTS_DRAWER);
        List<CombinedTemplateCreationEntry> entries = new ArrayList();
        CombinedTemplateCreationEntry entry;
        entry = createEntry(Comparison.DESCRIPTION, queryProxy);
        entries.add(entry);
        entry = createEntry(NullTest.DESCRIPTION, queryProxy);
        entries.add(entry);
        entry = createEntry(WildcardTest.DESCRIPTION, queryProxy);
        entries.add(entry);
        entry = createEntry(InTest.DESCRIPTION, queryProxy);
        entries.add(entry);
        entry = createEntry(MultiValueInTest.DESCRIPTION, queryProxy);
        entries.add(entry);
        entry = createEntry(InFolderTest.DESCRIPTION, queryProxy);
        entry.setId(IN_FOLDER_TEST_ENTRY);
        entries.add(entry);
        entry = createEntry(ThisInFolderTest.DESCRIPTION, queryProxy);
        entry.setId(THIS_IN_FOLDER_TEST_ENTRY);
        entries.add(entry);
        entry = createEntry(ThisInTreeTest.DESCRIPTION, queryProxy);
        entry.setId(THIS_IN_TREE_TEST_ENTRY);
        entries.add(entry);
        entry = createEntry(InSubFolderTest.DESCRIPTION, queryProxy);
        entry.setId(IN_SUBFOLDER_TEST_ENTRY);
        entries.add(entry);
        entry = createEntry(ClassTest.DESCRIPTION, queryProxy);
        entry.setId(CLASS_TEST_ENTRY);
        entry.setVisible(false);
        entries.add(entry);
        entry = createEntry(FreeText.DESCRIPTION, queryProxy);
        entries.add(entry);
        drawer.addAll(entries);
        return drawer;
    }

    private static CombinedTemplateCreationEntry createEntry(String label, String description, String normalIcon, String largeIcon, Class<? extends QueryElement> type, QueryProxy queryProxy) {
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(label, description, new QueryCreationFactory(queryProxy, type), Activator.getImageDescriptor(normalIcon), Activator.getImageDescriptor(largeIcon));
        return combined;
    }

    private static CombinedTemplateCreationEntry createEntry(QueryElementDescription description, QueryProxy queryProxy) {
        CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(description.getLabel(), description.getDescription(), new QueryCreationFactory(queryProxy, description.getObjectType()), description.getIcon(), description.getLargeIcon());
        return combined;
    }

    static PaletteRoot createPalette(QueryProxy queryProxy) {
        PaletteRoot logicPalette = new PaletteRoot();
        logicPalette.addAll(createCategories(logicPalette, queryProxy));
        return logicPalette;
    }

    public static QueryPaletteFactory getDefault() {
        return singleton;
    }

    public QueryPaletteFactory() {
        if (singleton == null) {
            singleton = this;
        }
    }
}
