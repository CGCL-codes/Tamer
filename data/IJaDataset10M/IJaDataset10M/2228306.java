package net.sf.gwoc.provider;

import java.util.List;
import net.sf.gwoc.data.Cache;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;
import com.novell.groupwise.ws.Contact;

public class AddressContentProposalProvider implements IContentProposalProvider {

    private IContentProposal icps[] = new IContentProposal[0];

    @Override
    public IContentProposal[] getProposals(String contents, int position) {
        System.out.println(contents);
        List<Contact> cs = Cache.getConnection().searchContacts(contents, 10);
        System.out.println(cs.size());
        icps = new IContentProposal[cs.size()];
        for (int i = 0; i < icps.length; i++) {
            icps[i] = makeContentProposal(cs.get(i));
        }
        return icps;
    }

    private IContentProposal makeContentProposal(final Contact contact) {
        return new IContentProposal() {

            public String getContent() {
                return contact.getName();
            }

            public String getDescription() {
                return null;
            }

            public String getLabel() {
                return null;
            }

            public int getCursorPosition() {
                return contact.getName().length();
            }
        };
    }
}
