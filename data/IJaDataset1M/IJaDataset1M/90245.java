package wsdir.resolvers;

import java.util.Enumeration;
import wsdir.core.AID;
import atomik.core.AConcept;
import atomik.core.AConceptManager;
import atomik.core.ADefManager;
import atomik.core.AStringImpl;
import atomik.core.AStructImpl;
import atomik.core.DefaultFactory;
import atomik.syntax.ASyntaxManager;

/**
 * This Class is use for the redirection 
 * message exception.
 * returns a AID object 
 *
 */
public class RedirectionResolver {

    /** Default factory for parsing sl0 text */
    DefaultFactory fac;

    /** Definition manager for parsing definitions */
    ADefManager om;

    /** Syntax manager for checking the syntax of the sl0 text */
    ASyntaxManager sm;

    /** This manager deals with concept objects */
    AConceptManager cmag;

    /** This attribute represents the output concept */
    AConcept aidDescription = null;

    AID aid = null;

    /**
	 * This constructor parses the SL0 representation of the aid
	 * and creates a new AID with a name and its addresses (NO RESOLVER!!!)
	 * @param sloAidRepresenation
	 * @throws Exception
	 */
    public RedirectionResolver(String sloAidRepresenation) throws Exception {
        aidDescription = new RequestResolver().makeAConceptFromString(sloAidRepresenation);
        aid = new AID();
        aid.name = aidDescription.get("name").toString();
        AConcept adresses = ((AStructImpl) aidDescription).get("addresses");
        int total_addresses = ((AStructImpl) adresses).getSlotsNo();
        String[] addresses = new String[total_addresses];
        Enumeration addresses_enum = ((AStructImpl) adresses).getSlotValues();
        int increment = 0;
        while (addresses_enum.hasMoreElements()) {
            AConcept temp_address = (AConcept) addresses_enum.nextElement();
            addresses[increment] = temp_address.toString();
            increment++;
        }
        aid.addresses = addresses;
    }

    /**
	 * returns the AID 
	 * @return
	 */
    public AID getAID() {
        return this.aid;
    }
}
