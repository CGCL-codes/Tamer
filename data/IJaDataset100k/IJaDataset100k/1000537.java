package honeycrm.server.domain;

import honeycrm.client.actions.CreateContractAction;
import honeycrm.client.view.ModuleAction;
import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.HasExtraButton;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.OneToMany;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.SearchableEntity;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTableAnnotation;
import java.util.ArrayList;
import java.util.Date;
import com.google.appengine.api.datastore.Key;

@SearchableEntity
@ListViewable({ "contactID", "deadline" })
@DetailViewable({ "accountID", "contactID", "assignedTo", "deadline", "uniqueServices", "recurringServices" })
@Quicksearchable({ "contactID" })
@HasExtraButton(label = "Create Contract", action = CreateContractAction.class, show = ModuleAction.DETAIL)
public class Offering extends AbstractEntity {

    @Label("Unique Services")
    @FieldTableAnnotation(UniqueService.class)
    @OneToMany(UniqueService.class)
    public ArrayList<Key> uniqueServices;

    @Label("Recurring Services")
    @FieldTableAnnotation(RecurringService.class)
    @OneToMany(RecurringService.class)
    public ArrayList<Key> recurringServices;

    @Label("Contact")
    @FieldRelateAnnotation(Contact.class)
    public Key contactID;

    @FieldDateAnnotation
    @Label("Deadline")
    public Date deadline;

    @Label("Contract")
    @FieldRelateAnnotation(Contract.class)
    public Key contractID;

    @Label("Account")
    @FieldRelateAnnotation(Account.class)
    public Key accountID;

    /**
	 * Calculate the sum of all services.
	 */
    public Double getCosts() {
        return 23.0;
    }
}
