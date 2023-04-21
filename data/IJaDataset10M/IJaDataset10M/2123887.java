package org.torweg.pulse.site.content.filter.admin;

import java.util.Collection;
import org.torweg.pulse.site.content.filter.admin.FilterRuleEditor.FilterRuleLocalizationRequest;

/**
 * The {@code IEditContext} for the {@code FilterRuleEditor}.
 * 
 * @author Daniel Dietz
 * @version $Revision$
 */
public interface IFilterRuleEditContext extends IEditContext {

    /**
	 * The {@code IFilterRuleEditContext} for loading a {@code FilterRule}s
	 * specified by id.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public interface ILoadFilterRule extends IFilterRuleEditContext {

        /**
		 * Returns the id of the {@code FilterRule} to load.
		 * 
		 * @return the id of the {@code FilterRule}
		 */
        Long getFilterRuleId();
    }

    /**
	 * The {@code IFilterRuleEditContext} for saving an edited
	 * {@code FilterRule} specified by id.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public interface ISaveFilterRule extends ILoadFilterRule {

        /**
		 * Returns the name for the {@code FilterRule}.
		 * 
		 * @return the name for the {@code FilterRule}
		 */
        String getFilterRuleName();

        /**
		 * Returns the {@code FilterRuleLocalizationRequest}s.
		 * 
		 * @return the {@code FilterRuleLocalizationRequest}s
		 */
        Collection<FilterRuleLocalizationRequest> getFilterRuleLocalizationRequests();
    }

    /**
	 * The {@code IFilterRuleEditContext} for loading {@code FilterRule}s for a
	 * {@code FilterMatches} specified by id.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public interface ILoadFilterRulesForFilterMatches extends IFilterRuleEditContext {

        /**
		 * Returns the id of the {@code FilterMatches} to load the
		 * {@code FilterRule}s for.
		 * 
		 * @return the id of the {@code FilterMatches}
		 */
        Long getFilterMatchesId();
    }

    /**
	 * The {@code IFilterRuleEditContext} for loading {@code FilterRule}s for a
	 * {@code Filter} specified by id.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public interface ILoadFilterRulesForFilter extends IFilterRuleEditContext {

        /**
		 * Returns the id of the {@code Filter}.
		 * 
		 * @return the id of the {@code Filter}
		 */
        Long getFilterId();
    }

    /**
	 * The {@code IFilterRuleEditContext} for creating a {@code FilterRule} for
	 * a {@code Filter} specified by id.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public interface ICreateFilterRuleForFilter extends ILoadFilterRulesForFilter {

        /**
		 * Returns the name for the {@code FilterRule}.
		 * 
		 * @return the name for the {@code FilterRule}
		 */
        String getFilterRuleName();
    }

    /**
	 * The {@code IFilterRuleEditContext} for moving a {@code FilterRule}
	 * specified by id within its {@code Filter}.
	 * 
	 * @author Daniel Dietz
	 * @version $Revision$
	 */
    public interface IMoveFilterRuleInFilter extends ILoadFilterRule {

        /**
		 * Returns the {@code MoveDirection}.
		 * 
		 * @return the {@code MoveDirection}
		 */
        MoveDirection getMoveDirection();
    }
}
