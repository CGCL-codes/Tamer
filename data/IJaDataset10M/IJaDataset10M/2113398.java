package net.taylor.portal.entity.theme.editor;

import net.taylor.portal.entity.theme.ThemeResource;
import net.taylor.portal.menu.MenuInit;
import net.taylor.richfaces.Picker;
import net.taylor.seam.AbstractFinderBean;
import net.taylor.seam.ComboBox;
import net.taylor.seam.Filter;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * The implementation of the Finder interface.
 *
 * @author jgilbert
 * @generated
 */
@Name("themeResourceFinder")
@Scope(ScopeType.SESSION)
public class ThemeResourceFinderBean extends AbstractFinderBean<ThemeResource> implements ThemeResourceFinder {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public ThemeResourceFinderBean() {
    }

    /** @generated */
    protected void initFilter(Filter filter) {
        filter.addStringParameter("partition");
        filter.addStringParameter("path");
        filter.addStringParameter("text");
    }

    /** @NOT generated */
    protected void initColumns() {
        addColumn("partition", "String", null);
        addColumn("path", "String", null);
    }

    /** @NOT generated */
    public boolean isSimpleEntity() {
        return false;
    }

    /** @generated */
    public boolean isRenderCharts() {
        return false;
    }

    /**
	 * ------------------------------------------------------------------------
	 * --- Base ComboBox
	 * ------------------------------------------------------------------------
	 *
	 * @generated
	 */
    public static class ThemeResourceComboBox<COMP, SRC> extends ComboBox<COMP, ThemeResource, SRC> {

        /** @generated */
        public ThemeResourceComboBox(Object component, String name) {
            super(component, name);
        }

        /** @generated */
        public ThemeResourceComboBox(Object component, String instanceName, String name) {
            super(component, instanceName, name);
        }

        /** @generated */
        protected Class getEntityClass() {
            return ThemeResource.class;
        }

        /** @generated */
        protected String getAlias() {
            return "t1";
        }

        /** @generated */
        protected String getItemLabel() {
            return "#{t1.name}";
        }

        /** @generated */
        protected String getOrderBy() {
            return "upper(t1.name)";
        }
    }

    /**
	 * ------------------------------------------------------------------------
	 * --- Base Picker
	 * ------------------------------------------------------------------------
	 *
	 * @generated
	 */
    public static class ThemeResourcePicker<COMP, SRC> extends Picker<COMP, ThemeResource, SRC> {

        /** @generated */
        public ThemeResourcePicker(Object component, String name) {
            super(component, name);
        }

        /** @generated */
        public ThemeResourcePicker(Object component, String instanceName, String name) {
            super(component, instanceName, name);
        }

        /** @generated */
        protected Class getEntityClass() {
            return ThemeResource.class;
        }

        /** @generated */
        protected String getAlias() {
            return "t1";
        }

        /** @generated */
        protected String getItemLabel() {
            return "#{t1.name}";
        }

        /** @generated */
        protected String getOrderBy() {
            return "upper(t1.name)";
        }

        /** @generated */
        protected String getFilterRestriction() {
            return "lower(t1.name) like :filter";
        }
    }

    public static class ThemeResourceMenuInit extends MenuInit {

        /** @generated */
        public ThemeResourceMenuInit() {
            super("ATaylorPortalMenu", 0l, "#{messages['ThemeResource_Menu']}", "#{s:hasPermission('net.taylor.portal.entity.theme.ThemeResource','read')}", "", "/jsf/ThemeResource/Search.xhtml");
        }
    }
}
