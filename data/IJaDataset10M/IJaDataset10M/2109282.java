package de.mpiwg.vspace.navigation.fieldeditor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import de.mpiwg.vspace.generation.navigation.service.NavigationEntry;
import de.mpiwg.vspace.navigation.fieldeditor.impl.FieldEditorDescription;
import de.mpiwg.vspace.navigation.fieldeditor.impl.Property;
import de.mpiwg.vspace.navigation.fieldeditor.impl.SeparatorDescription;
import de.mpiwg.vspace.navigation.util.PropertyHandler;

public class PreferencePageInfoProvider {

    public static final PreferencePageInfoProvider INSTANCE = new PreferencePageInfoProvider();

    private List<IPageContentDescription> scenePageContent;

    private List<IPageContentDescription> modulePageContent;

    private PreferencePageInfoProvider() {
        init();
    }

    protected void init() {
        scenePageContent = new ArrayList<IPageContentDescription>();
        modulePageContent = new ArrayList<IPageContentDescription>();
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_start"), PropertyHandler.getInstance().getProperty("_entry_name_start"), PropertyHandler.getInstance().getProperty("_entry_label_start_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_start_use_default_text")), NavigationEntry.SCENE_START, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_start_button"), PropertyHandler.getInstance().getProperty("_entry_name_start_button"), PropertyHandler.getInstance().getProperty("_entry_label_start_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_start_use_default_image")), NavigationEntry.SCENE_START, NavigationEntryType.IMAGE));
        scenePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_back"), PropertyHandler.getInstance().getProperty("_entry_name_back"), PropertyHandler.getInstance().getProperty("_entry_label_back_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_back_use_default_text")), NavigationEntry.SCENE_BACK, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_back_button"), PropertyHandler.getInstance().getProperty("_entry_name_back_button"), PropertyHandler.getInstance().getProperty("_entry_label_back_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_back_use_default_image")), NavigationEntry.SCENE_BACK, NavigationEntryType.IMAGE));
        scenePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_copyright"), PropertyHandler.getInstance().getProperty("_entry_name_copyright"), PropertyHandler.getInstance().getProperty("_entry_label_copyright_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_copyright_use_default_text")), NavigationEntry.SCENE_COPYRIGHT, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_copyright_button"), PropertyHandler.getInstance().getProperty("_entry_name_copyright_button"), PropertyHandler.getInstance().getProperty("_entry_label_copyright_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_copyright_use_default_image")), NavigationEntry.SCENE_COPYRIGHT, NavigationEntryType.IMAGE));
        scenePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_overview"), PropertyHandler.getInstance().getProperty("_entry_name_overview"), PropertyHandler.getInstance().getProperty("_entry_label_overview_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_overview_use_default_text")), NavigationEntry.SCENE_OVERVIEW_MAP, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_overview_button"), PropertyHandler.getInstance().getProperty("_entry_name_overview_button"), PropertyHandler.getInstance().getProperty("_entry_label_overview_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_overview_use_default_image")), NavigationEntry.SCENE_OVERVIEW_MAP, NavigationEntryType.IMAGE));
        scenePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_scene_desc"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_use_default_text")), NavigationEntry.SCENE_DESCRIPTION, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_scene_desc_button"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_button"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_use_default_image")), NavigationEntry.SCENE_DESCRIPTION, NavigationEntryType.IMAGE));
        scenePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_scene_desc_bg"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_bg"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_bg_use_default_text")), NavigationEntry.SCENE_DESCRIPTION_BACKGROUND, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_scene_desc_bg_button"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_bg_button"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_scene_desc_bg_use_default_image")), NavigationEntry.SCENE_DESCRIPTION_BACKGROUND, NavigationEntryType.IMAGE));
        scenePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_link_bg"), PropertyHandler.getInstance().getProperty("_entry_name_link_bg"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_link_bg_use_default_text")), NavigationEntry.SCENE_LINK_BACKGROUND, NavigationEntryType.TEXT));
        scenePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_link_bg_button"), PropertyHandler.getInstance().getProperty("_entry_name_link_bg_button"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_link_bg_use_default_image")), NavigationEntry.SCENE_LINK_BACKGROUND, NavigationEntryType.IMAGE));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_start"), PropertyHandler.getInstance().getProperty("_entry_name_module_start"), PropertyHandler.getInstance().getProperty("_entry_label_module_start_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_module_start_use_default_text")), NavigationEntry.MODULE_START, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_start_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_start_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_start_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_start_use_default_image")), NavigationEntry.MODULE_START, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_back_scene"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_scene"), PropertyHandler.getInstance().getProperty("_entry_label_module_back_scene_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_scene_use_default_text")), NavigationEntry.MODULE_BACK_TO_SCENE, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_back_scene_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_scene_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_back_scene_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_scene_use_default_image")), NavigationEntry.MODULE_BACK_TO_SCENE, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_forward"), PropertyHandler.getInstance().getProperty("_entry_name_module_forward"), PropertyHandler.getInstance().getProperty("_entry_label_module_forward_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_module_forward_use_default_text")), NavigationEntry.MODULE_FORWARD, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_forward_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_forward_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_forward_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_forward_use_default_image")), NavigationEntry.MODULE_FORWARD, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_bp"), PropertyHandler.getInstance().getProperty("_entry_name_module_bp"), PropertyHandler.getInstance().getProperty("_entry_label_module_bp_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_module_bp_use_default_text")), NavigationEntry.MODULE_BACK_TO_BP, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_bp_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_bp_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_bp_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_bp_use_default_image")), NavigationEntry.MODULE_BACK_TO_BP, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_back"), PropertyHandler.getInstance().getProperty("_entry_name_module_back"), PropertyHandler.getInstance().getProperty("_entry_label_module_back_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_use_default_text")), NavigationEntry.MODULE_BACK, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_back_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_back_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_back_use_default_image")), NavigationEntry.MODULE_BACK, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_copyright"), PropertyHandler.getInstance().getProperty("_entry_name_module_copyright"), PropertyHandler.getInstance().getProperty("_entry_label_module_copyright_use_default_text"), PropertyHandler.getInstance().getProperty("_entry_name_module_copyright_use_default_text")), NavigationEntry.MODULE_COPYRIGHT, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_copyright_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_copyright_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_copyright_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_copyright_use_default_image")), NavigationEntry.MODULE_COPYRIGHT, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_category_text"), NavigationEntry.MODULE_CATEGORY.getPropertyName() + PropertyHandler.getInstance().getProperty("_prop_appendix_text"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_text"), NavigationEntry.MODULE_CATEGORY.getPropertyName() + PropertyHandler.getInstance().getProperty("_prop_ext_use_default") + PropertyHandler.getInstance().getProperty("_prop_appendix_text")), NavigationEntry.MODULE_CATEGORY, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_category_icon"), NavigationEntry.MODULE_CATEGORY.getPropertyName() + PropertyHandler.getInstance().getProperty("_prop_appendix_image"), PropertyHandler.getInstance().getProperty("_entry_label_use_default_image"), NavigationEntry.MODULE_CATEGORY.getPropertyName() + PropertyHandler.getInstance().getProperty("_prop_ext_use_default") + PropertyHandler.getInstance().getProperty("_prop_appendix_image")), NavigationEntry.MODULE_CATEGORY, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_bp_choice_button"), PropertyHandler.getInstance().getProperty("_entry_name_module_bp_choice_button"), PropertyHandler.getInstance().getProperty("_entry_label_module_bp_choice_use_default_image"), PropertyHandler.getInstance().getProperty("_entry_name_module_bp_choice_use_default_image")), NavigationEntry.MODULE_BRANCHING_POINT_CHOICE, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_alien_link"), NavigationEntry.MODULE_ALIEN_LINK.getPropertyName() + "_text", PropertyHandler.getInstance().getProperty("_entry_label_module_alien_link_use_default_text"), NavigationEntry.MODULE_ALIEN_LINK.getPropertyName() + "_use_default_text"), NavigationEntry.MODULE_ALIEN_LINK, NavigationEntryType.TEXT));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_alien_link_button"), NavigationEntry.MODULE_ALIEN_LINK.getPropertyName() + "_image", PropertyHandler.getInstance().getProperty("_entry_label_module_alien_link_use_default_image"), NavigationEntry.MODULE_ALIEN_LINK.getPropertyName() + "_use_default_image"), NavigationEntry.MODULE_ALIEN_LINK, NavigationEntryType.IMAGE));
        modulePageContent.add(new SeparatorDescription(SWT.HORIZONTAL));
        modulePageContent.add(new FieldEditorDescription(new Property(PropertyHandler.getInstance().getProperty("_entry_label_module_alien_link_box"), NavigationEntry.MODULE_ALIEN_LINK_SLIDE_BOX.getPropertyName() + "_text", PropertyHandler.getInstance().getProperty("_entry_label_module_alien_link_box_use_default_text"), NavigationEntry.MODULE_ALIEN_LINK_SLIDE_BOX.getPropertyName() + "_use_default_text"), NavigationEntry.MODULE_ALIEN_LINK_SLIDE_BOX, NavigationEntryType.TEXT));
    }

    public List<IPageContentDescription> getScenePageContent() {
        return scenePageContent;
    }

    public List<IPageContentDescription> getModulePageContent() {
        return modulePageContent;
    }

    public String getScenePageTitle() {
        return PropertyHandler.getInstance().getProperty("_preference_page_title_scene");
    }

    public String getModulePageTitle() {
        return PropertyHandler.getInstance().getProperty("_preference_page_title_module");
    }
}
