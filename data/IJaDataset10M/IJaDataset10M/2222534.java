package net.sf.jmoney;

import java.util.Collection;
import net.sf.jmoney.model2.Account;
import net.sf.jmoney.model2.Entry;
import net.sf.jmoney.model2.IObjectKey;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IMemento;

/**
 * Interface that must be implemented by all implementations of a transaction
 * type in the tabbed transaction edit area.
 * 
 * Some data entry views have tabbed controls with a tab for each
 * type of transaction.  Plug-ins can add further types of transactions,
 * allowing for rapid data entry.  All such implementations of a tabbed
 * control must implement this interface.
 * 
 * @author Nigel Westbury
 *
 */
public interface ITransactionTemplate {

    void addTransaction(Collection<IObjectKey> ourEntryList);

    String getDescription();

    /**
	 * Not all templates are applicable in all situations.  This method
	 * is called to determine if a template is applicable.  A tab is created
	 * for the template only if the template is applicable.
	 * 
	 * @param account the account being listed in the editor view, or null if
	 * 			the editor view is of a type that does not display information
	 * 			for an account
	 * @return true if the template is applicable, false if not applicable
	 */
    boolean isApplicable(Account account);

    /**
	 * Templates may be used to edit existing transactions.  This method loads
	 * an existing entry into the controls.  
	 * 
	 * This method will succeed only if the entry is in a transaction that exactly
	 * matches the template.  If the transaction does not exactly match the template
	 * then none of the controls will be set and false will be returned.
	 * 
	 * If Not all templates are applicable in all situations.  This method
	 * is called to determine if a template is applicable.  A tab is created
	 * for the template only if the template is applicable.
	 * 
	 * @param account the account being listed in the editor view, or null if
	 * 			the editor view is of a type that does not display information
	 * 			for an account
	 * @param entry the entry that was selected to be edited.  If account is not
	 * 			null then this entry will always be in the given account
	 * @return true if the entry matched the template, false otherwise
	 */
    boolean loadEntry(Account account, Entry entry);

    /**
	 * 
	 * @param parent
	 * @param expandedControls true if speed of user entry is more important,
	 * 			false if conservation of screen space is more important.
	 * 			For example, a list box may be appropriate when expandedControls
	 * 			is true while a drop-down combo box may be appropriate when
	 * 			expandedControls is false
	 * @param account the account into which the entry is to be added,
	 * 			or null if the account is not known and must be entered
	 * 			by the user 
	 * @return
	 */
    Control createControl(Composite parent, boolean expandedControls, Account account, Collection<IObjectKey> ourEntryList);

    void init(IMemento memento);

    void saveState(IMemento memento);
}
