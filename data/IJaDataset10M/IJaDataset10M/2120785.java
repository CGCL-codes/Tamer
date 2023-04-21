package org.torweg.pulse.site.content.filter.admin;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.torweg.pulse.site.content.filter.Filter;
import org.torweg.pulse.site.content.filter.FilterRule;

/**
 * The result base for the {@code FilterRuleEditor}.
 * 
 * @author Daniel Dietz
 * @version $Revision$
 */
@XmlRootElement(name = "filter-rule-editor-result")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
public class FilterRuleEditorResult extends EditorResult {

    /**
	 * Default constructor.
	 */
    @Deprecated
    protected FilterRuleEditorResult() {
        super();
    }

    /**
	 * Creates a new {@code FilterRuleEditorResult} with the given
	 * {@code IFilterRuleEditContext}.
	 * 
	 * @param cntxt
	 *            the current {@code IFilterRuleEditContext}
	 */
    public FilterRuleEditorResult(final IFilterRuleEditContext cntxt) {
        super(cntxt);
    }

    /**
	 * The {@code FilterRuleEditorResult} for the load of {@code FilterRule}s.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    @XmlRootElement(name = "load-filter-rules-result")
    @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class LoadFilterRulesResult extends FilterRuleEditorResult {

        /**
		 * The current (absolute in DB) total of {@code FilterRule}s.
		 */
        private Long total;

        /**
		 * The current {@code FilterRule}s of the result.
		 */
        private List<FilterRule> filterRules;

        /**
		 * Default constructor.
		 */
        @Deprecated
        protected LoadFilterRulesResult() {
            super();
        }

        /**
		 * Creates a new {@code LoadFilterRulesResult} with the given
		 * {@code IFilterRuleEditContext.ILoadFilterRulesForFilterMatches}.
		 * 
		 * @param cntxt
		 *            the current
		 *            {@code IFilterRuleEditContext.ILoadFilterRulesForFilterMatches}
		 */
        public LoadFilterRulesResult(final IFilterRuleEditContext.ILoadFilterRulesForFilterMatches cntxt) {
            super(cntxt);
        }

        /**
		 * Creates a new {@code LoadFilterRulesResult} with the given
		 * {@code IFilterRuleEditContext.ILoadFilterRulesForFilter}.
		 * 
		 * @param cntxt
		 *            the current
		 *            {@code IFilterRuleEditContext.ILoadFilterRulesForFilter}
		 */
        public LoadFilterRulesResult(final IFilterRuleEditContext.ILoadFilterRulesForFilter cntxt) {
            super(cntxt);
        }

        /**
		 * Returns the <tt>total</tt>.
		 * 
		 * @return the <tt>total</tt>
		 */
        @XmlElement(name = "total")
        public final Long getTotal() {
            return this.total;
        }

        /**
		 * Sets the <tt>total</tt>.
		 * 
		 * @param ttl
		 *            the <tt>total</tt> to set
		 */
        public final void setTotal(final Long ttl) {
            this.total = ttl;
        }

        /**
		 * Returns the {@code FilterRule}s.
		 * 
		 * @return the {@code FilterRule}s
		 */
        @XmlElementWrapper(name = "filter-rules")
        @XmlElement(name = "filter-rule")
        public final List<FilterRule> getFilterRules() {
            return this.filterRules;
        }

        /**
		 * Sets the {@code FilterRule}s.
		 * 
		 * @param fltrrls
		 *            the {@code FilterRule}s to set
		 */
        public final void setFilterRules(final List<FilterRule> fltrrls) {
            this.filterRules = fltrrls;
        }
    }

    /**
	 * The {@code FilterRuleEditorResult} for the load of a {@code FilterRule}.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    @XmlRootElement(name = "load-filter-rule-result")
    @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class LoadFilterRuleResult extends FilterRuleEditorResult {

        /**
		 * The current {@code FilterRule} of the result.
		 */
        private FilterRule filterRule;

        /**
		 * Default constructor.
		 */
        @Deprecated
        protected LoadFilterRuleResult() {
            super();
        }

        /**
		 * Creates a new {@code LoadFilterRuleResult} with the given
		 * {@code IFilterRuleEditContext.ILoadFilterRule}.
		 * 
		 * @param cntxt
		 *            the current {@code IFilterRuleEditContext.ILoadFilterRule}
		 */
        public LoadFilterRuleResult(final IFilterRuleEditContext.ILoadFilterRule cntxt) {
            super(cntxt);
        }

        /**
		 * Returns the {@code FilterRule}.
		 * 
		 * @return the {@code FilterRule}
		 */
        @XmlElement(name = "filter-rule")
        public final FilterRule getFilterRule() {
            return this.filterRule;
        }

        /**
		 * Sets the {@code FilterRule}.
		 * 
		 * @param fltrrl
		 *            the {@code FilterRule} to set
		 */
        public final void setFilterRule(final FilterRule fltrrl) {
            this.filterRule = fltrrl;
        }
    }

    /**
	 * The {@code FilterRuleEditorResult} for the save of a {@code FilterRule}.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    @XmlRootElement(name = "save-filter-rule-result")
    @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class SaveFilterRuleResult extends LoadFilterRuleResult {

        /**
		 * The {@code SaveFilterRuleResult.Error}.
		 */
        private Error error;

        /**
		 * Default constructor.
		 */
        @Deprecated
        protected SaveFilterRuleResult() {
            super();
        }

        /**
		 * Creates a new {@code SaveFilterRuleResult} with the given
		 * {@code IFilterRuleEditContext.ISaveFilterRule}.
		 * 
		 * @param cntxt
		 *            the current {@code IFilterRuleEditContext.ISaveFilterRule}
		 */
        public SaveFilterRuleResult(final IFilterRuleEditContext.ISaveFilterRule cntxt) {
            super(cntxt);
        }

        /**
		 * Returns the {@code SaveFilterRuleResult.Error}.
		 * 
		 * @return the {@code SaveFilterRuleResult.Error} - if set, {@code null}
		 *         otherwise
		 */
        @XmlElement(name = "error")
        public final SaveFilterRuleResult.Error getError() {
            return this.error;
        }

        /**
		 * Sets the {@code SaveFilterRuleResult.Error}.
		 * 
		 * @param errr
		 *            the {@code SaveFilterRuleResult.Error} to set
		 * 
		 * @return the modified {@code SaveFilterRuleResult}
		 */
        public final SaveFilterRuleResult setError(final SaveFilterRuleResult.Error errr) {
            this.error = errr;
            return this;
        }

        /**
		 * The error codes for errors which might occur during the save of a
		 * {@code FilterRule}.
		 * 
		 * @author Daniel Dietz
		 * @version $Revision$
		 */
        @XmlType(name = "filter-rule-save-error")
        public enum Error {

            /**
			 * DUPLICTAE: indicates that a {@code FilterRule} with the given
			 * name already exists for the current {@code Filter}.
			 */
            DUPLICTAE
        }
    }

    /**
	 * The {@code FilterRuleEditorResult} for the creation of a
	 * {@code FilterRule} for a {@code Filter}.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    @XmlRootElement(name = "create-filter-rule-for-filter-result")
    @XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
    @XmlAccessorType(XmlAccessType.NONE)
    public static class CreateFilterRuleForFilterResult extends FilterRuleEditorResult {

        /**
		 * The current {@code FilterRule} of the result.
		 */
        private FilterRule filterRule;

        /**
		 * The current {@code Filter} of the result.
		 */
        private Filter filter;

        /**
		 * The {@code CreateFilterRuleForFilterResult.Error}.
		 */
        private Error error;

        /**
		 * Default constructor.
		 */
        @Deprecated
        protected CreateFilterRuleForFilterResult() {
            super();
        }

        /**
		 * Creates a new {@code CreateFilterRuleForFilterResult} with the given
		 * {@code IFilterRuleEditContext.ICreateFilterRuleForFilter}.
		 * 
		 * @param cntxt
		 *            the current
		 *            {@code IFilterRuleEditContext.ICreateFilterRuleForFilter}
		 */
        public CreateFilterRuleForFilterResult(final IFilterRuleEditContext.ICreateFilterRuleForFilter cntxt) {
            super(cntxt);
        }

        /**
		 * Returns the {@code FilterRule}.
		 * 
		 * @return the {@code FilterRule}
		 */
        @XmlElement(name = "filter-rule")
        public final FilterRule getFilterRule() {
            return this.filterRule;
        }

        /**
		 * Sets the {@code FilterRule}.
		 * 
		 * @param fltrrl
		 *            the {@code FilterRule} to set
		 */
        public final void setFilterRule(final FilterRule fltrrl) {
            this.filterRule = fltrrl;
        }

        /**
		 * Returns the {@code Filter}.
		 * 
		 * @return the {@code Filter}
		 */
        @XmlElement(name = "filter")
        public final Filter getFilter() {
            return this.filter;
        }

        /**
		 * Sets the {@code Filter}.
		 * 
		 * @param fltr
		 *            the {@code Filter} to set
		 */
        public final void setFilter(final Filter fltr) {
            this.filter = fltr;
        }

        /**
		 * Returns the {@code CreateFilterRuleForFilterResult.Error}.
		 * 
		 * @return the {@code CreateFilterRuleForFilterResult.Error} - if set,
		 *         {@code null} otherwise
		 */
        @XmlElement(name = "error")
        public final CreateFilterRuleForFilterResult.Error getError() {
            return this.error;
        }

        /**
		 * Sets the {@code CreateFilterRuleForFilterResult.Error}.
		 * 
		 * @param errr
		 *            the {@code CreateFilterRuleForFilterResult.Error} to set
		 * 
		 * @return the modified {@code CreateFilterRuleForFilterResult}
		 */
        public final CreateFilterRuleForFilterResult setError(final CreateFilterRuleForFilterResult.Error errr) {
            this.error = errr;
            return this;
        }

        /**
		 * The error codes for errors which might occur during creation of a new
		 * {@code FilterRule}.
		 * 
		 * @author Daniel Dietz
		 * @version $Revision$
		 */
        @XmlType(name = "filter-rule-for-filter-create-error")
        public enum Error {

            /**
			 * DUPLICTAE: indicates that a {@code FilterRule} with the given
			 * name already exists for the current {@code Filter}.
			 */
            DUPLICTAE
        }
    }
}
