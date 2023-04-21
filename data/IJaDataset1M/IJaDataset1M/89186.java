package net.sf.msnalert.command.admin;

import java.util.Set;
import net.sf.msnalert.command.Command;
import net.sf.jml.Email;
import net.sf.jml.MsnContactList;
import net.sf.msnalert.i18n.ResourceBundleLocator;

/**
 *
 * @author Mauro Codella
 */
public class RemoveContactCommand extends Command {

    @Override
    public String handle(String[] args, Email email) {
        String response = "";
        if (args.length == 1) {
            MsnContactList contactList = getMessenger().getContactList();
            Email contactEmail = Email.parseStr(args[0]);
            if (contactList.getContactByEmail(contactEmail) != null) {
                getMessenger().removeFriend(contactEmail, false);
                response = args[0] + ResourceBundleLocator.getResourceBundle().getString("_removed.");
            } else response = ResourceBundleLocator.getResourceBundle().getString("can't_remove_") + args[0] + ResourceBundleLocator.getResourceBundle().getString(":_it's_not_in_bot's_contact_list.");
        }
        return response;
    }

    @Override
    public Set<String> getCommandNames() {
        return getAliases("removecontact", "rc");
    }

    @Override
    public String getHelp() {
        return ResourceBundleLocator.getResourceBundle().getString("removes_a_contact_from_the_bot's_contact_list");
    }
}
