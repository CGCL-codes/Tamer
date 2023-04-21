package net.taylor.audit.entity.editor;

import net.taylor.audit.entity.Watch;
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
@Name("watchFinder")
@Scope(ScopeType.SESSION)
public class WatchFinderBean extends AbstractFinderBean<Watch> implements WatchFinder {

    /** @generated */
    private static final long serialVersionUID = 1L;

    /** @generated */
    public WatchFinderBean() {
    }

    /** @generated */
    protected void initFilter(Filter filter) {
        filter.addStringParameter("principal");
        filter.addStringParameter("entityHandle");
    }

    /** @generated */
    protected void initColumns() {
        addColumn("principal", "String", null);
        addColumn("entityHandle", "String", null);
    }

    /** @generated */
    public boolean isSimpleEntity() {
        return true;
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
    public static class WatchComboBox<COMP, SRC> extends ComboBox<COMP, Watch, SRC> {

        /** @generated */
        public WatchComboBox(Object component, String name) {
            super(component, name);
        }

        /** @generated */
        public WatchComboBox(Object component, String instanceName, String name) {
            super(component, instanceName, name);
        }

        /** @generated */
        protected Class getEntityClass() {
            return Watch.class;
        }

        /** @generated */
        protected String getAlias() {
            return "t1";
        }

        /** @NOT generated */
        protected String getItemLabel() {
            return "#{t1.principal}";
        }

        /** @NOT generated */
        protected String getOrderBy() {
            return "upper(t1.principal)";
        }
    }

    /**
	 * ------------------------------------------------------------------------
	 * --- Base Picker
	 * ------------------------------------------------------------------------
	 *
	 * @generated
	 */
    public static class WatchPicker<COMP, SRC> extends Picker<COMP, Watch, SRC> {

        /** @generated */
        public WatchPicker(Object component, String name) {
            super(component, name);
        }

        /** @generated */
        public WatchPicker(Object component, String instanceName, String name) {
            super(component, instanceName, name);
        }

        /** @generated */
        protected Class getEntityClass() {
            return Watch.class;
        }

        /** @generated */
        protected String getAlias() {
            return "t1";
        }

        /** @NOT generated */
        protected String getItemLabel() {
            return "#{t1.principal}";
        }

        /** @NOT generated */
        protected String getOrderBy() {
            return "upper(t1.principal)";
        }

        /** @NOT generated */
        protected String getFilterRestriction() {
            return "lower(t1.principal) like :filter";
        }
    }
}
