package gov.lanl.PidTrader;

import org.apache.log4j.Logger;
import org.omg.CORBA.ORB;
import org.omg.CosTrading.*;
import org.omg.CosTrading.LookupPackage.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This Class implements the connections between client request for PID
 * information and the OMG Trader Service(s) that supply that information.
 * (humm... this sould probably be in a idl). Two CORBA objects are used:
 * an ORB (omg.org.CORBA.ORB) and a Trader Lookup interface
 * (omg.org.CosTrading.Lookup, which I just call "the Trader" in most
 * descriptions. This class is used to get a list of Person Identifacation
 * Service (PIDS) offers from the associated Trader. Verious methods are
 * available supporting unconstrained and constrained searches. Searches
 * can be constrained on properties of the offer: the domain name, the
 * component (local) name, and traits in the list of supported traits.
 *
 * @version    1.0 27 Aug 1998
 * @author     Terry E Weymouth
 *
 **/
public class Trader {

    private Lookup lookup_;

    private ORB orb_;

    private Vector traitsToMatch = null;

    private String TRADER_OFFER_TYPE = "IdentificationComponent";

    private static Logger cat = Logger.getLogger(Trader.class);

    /**
     * Make a object to seach a Trader (org.omg.CosTrading.Lookup) for PIDS
     * offers.
     *
     * @param lookup the Trader ref.
     * @param orb the ORB (org.omg.CORBA.ORB).
     **/
    public Trader(Lookup lookup, ORB orb) {
        lookup_ = lookup;
        orb_ = orb;
    }

    /**
     * set the offer type
     * @param type
     */
    public void setTraderOfferType(String type) {
        TRADER_OFFER_TYPE = type;
    }

    /**
     * The simplest method. Returns a list of all the PIDS offers from this
     * trader.
     *
     * @see #getServicesWithConstraint
     **/
    public Offer[] getServices() {
        return getServicesWithConstraint("TRUE");
    }

    /**
     * Matching on the domain name and the configuration (local) name;
     * matches can be exact or partial. Partila matches are substring matches,
     * the matched offer includes the constraint string as a substring of
     * the matching string (in the offer).
     *
     * @param localName the (full or partial) string to match to the
     *        local_name property of the offer.
     * @param localExact a (boolean) flag. True implies an exact match.
     * @param domainName the (full or partial) string to match to the
     *        domain_name property of the offer.
     * @param domainExact a (boolean) flag. True implies an exact match.
     *
     * @see #getServicesWithLocalName
     * @see #getServicesWithDomainName
     * @see #getServicesWithConstraint
     **/
    public Offer[] getServicesWithLocalAndDomainNames(String localName, boolean localExact, String domainName, boolean domainExact) {
        String localMatch = "~";
        if (localExact) localMatch = "==";
        String domainMatch = "~";
        if (domainExact) domainMatch = "==";
        return getServicesWithConstraint("('" + domainName + "'" + domainMatch + "domain_name)" + " and " + "('" + localName + "'" + localMatch + "component_name)");
    }

    /**
     * Exact matching on the configuration (local) name.
     * @param localName the string to match to the
     *        local_name property of the offer.
     *
     * @see #getServicesWithConstraint
     **/
    public Offer[] getServicesWithLocalName(String localName) {
        return getServicesWithConstraint("component_name==" + "'" + localName + "'");
    }

    /**
     * Matching (partial or exact) on the configuration (local) name.
     * Partial matches are substring matches;
     * the matched offer includes the constraint string as a substring of
     * the matching string (in the offer).
     *
     * @param localName the (full or partial) string to match to the
     *        local_name property of the offer.
     * @param exact a (boolean) flag. True implies an exact match.
     *
     * @see #getServicesWithConstraint
     **/
    public Offer[] getServicesWithLocalName(String localName, boolean exact) {
        if (exact) return getServicesWithLocalName(localName); else return getServicesWithConstraint("'" + localName + "'" + "~component_name");
    }

    /**
     * Exact matching on the domain name.
     * @param domainName the string to match to the
     *        domain_name property of the offer.
     *
     * @see #getServicesWithConstraint
     **/
    public Offer[] getServicesWithDomainName(String domainName) {
        return getServicesWithConstraint("domain_name==" + "'" + domainName + "'");
    }

    /**
     * Matching (partial or exact) on the domain name.
     * Partial matches are substring matches;
     * the matched offer includes the constraint string as a substring of
     * the matching string (in the offer).
     *
     * @param domainName the (full or partial) string to match to the
     *        domain_name property of the offer.
     * @param exact a (boolean) flag. True implies an exact match.
     *
     * @see #getServicesWithConstraint
     **/
    public Offer[] getServicesWithDomainName(String domainName, boolean exact) {
        if (exact) return getServicesWithDomainName(domainName); else return getServicesWithConstraint("'" + domainName + "'" + "~domain_name");
    }

    /**
     * Add to a list of traits use to further constrain the next search
     * request. The list of traits is initially null. If ths list is null
     * or empty, then no trait-related constraints are used. The list is
     * reset after each search. The search returns the PIDS offers
     * that contain the given traits as supported traits.
     *
     * @param trait the trait (String) to add to the list of traits.
     *
     * @see #resetTraitMatchList
     **/
    public void addToTraitMatchList(String trait) {
        if (traitsToMatch == null) traitsToMatch = new Vector();
        traitsToMatch.addElement(trait);
    }

    /**
     * Reset (to null) the list of traits used to constrain the search for
     * PIDS offers.
     *
     * @see #addToTraitMatchList
     **/
    public void resetTraitMatchList() {
        traitsToMatch = null;
    }

    /**
     * A basic utility search method; used to implement all other
     * methods. A constraint string is given. Note if the list of traits
     * (for constraining the search) is not null then further constraints
     * will be added to this given constraint in the form of "'trait' in
     * supported_traits" (the constraint imlpied by the list); therefor
     * if a basic constrained search is desired, make sure that the
     * list of traits (initially null) is set to null.
     *
     * @see #resetTraitMatchList
     * @see #addToTraitMatchList
     * @see #getServices
     * @see #getServicesWithLocalAndDomainNames
     **/
    public Offer[] getServicesWithConstraint(String theConstraint) {
        String type = TRADER_OFFER_TYPE;
        String constraint = addTraitsToMatch(theConstraint);
        cat.debug("The constraint = " + constraint);
        String preference = "";
        int count = 0;
        Vector collection = new Vector();
        try {
            Policy[] policies = new Policy[3];
            policies[0] = new Policy();
            policies[0].name = "exact_type_match";
            policies[0].value = orb_.create_any();
            policies[0].value.insert_boolean(false);
            policies[1] = new Policy();
            policies[1].name = "use_dynamic_properties";
            policies[1].value = orb_.create_any();
            policies[1].value.insert_boolean(true);
            policies[2] = new Policy();
            policies[2].name = "use_proxy_offers";
            policies[2].value = orb_.create_any();
            policies[2].value.insert_boolean(true);
            SpecifiedProps desiredProps = new SpecifiedProps();
            desiredProps.all_dummy((short) HowManyProps._all);
            desiredProps.__default(HowManyProps.all);
            OfferSeqHolder offers = new OfferSeqHolder();
            OfferIteratorHolder iter = new OfferIteratorHolder();
            PolicyNameSeqHolder limits = new PolicyNameSeqHolder();
            lookup_.query(type, constraint, preference, policies, desiredProps, 20, offers, iter, limits);
            count = offers.value.length;
            collection.addElement(offers);
            cat.debug("Received " + count + " offers...");
            if (iter.value != null) {
                boolean more;
                do {
                    OfferSeqHolder seq = new OfferSeqHolder();
                    more = iter.value.next_n(20, seq);
                    count += seq.value.length;
                    cat.debug("Received " + count + " offers...");
                    collection.addElement(seq);
                } while (more);
                iter.value.destroy();
            }
        } catch (IllegalServiceType e) {
            cat.error("Trader: Illegal service type '" + e.type + "'");
        } catch (UnknownServiceType e) {
            cat.error("Trader: Unknown service type '" + e.type + "'");
        } catch (IllegalConstraint e) {
            cat.error("Trader: Illegal constraint");
        } catch (IllegalPreference e) {
            cat.error("Trader: Illegal preference");
        } catch (IllegalPolicyName e) {
            cat.error("Trader: Illegal policy '" + e.name + "'");
        } catch (PolicyTypeMismatch e) {
            cat.error("Trader: Policy type mismatch for '" + e.the_policy.name + "'");
        } catch (InvalidPolicyValue e) {
            cat.error("Trader: Invalid policy value for '" + e.the_policy.name + "'");
        } catch (IllegalPropertyName e) {
            cat.error("Trader: Illegal property name '" + e.name + "'");
        } catch (DuplicatePropertyName e) {
            cat.error("Trader: Duplicate property name '" + e.name + "'");
        } catch (DuplicatePolicyName e) {
            cat.error("Trader: Duplicate policy name '" + e.name + "'");
        } catch (org.omg.CORBA.SystemException e) {
            cat.error("Trader: CORBA System error occurred: " + e);
        }
        if (count > 0) {
            Offer[] ret = new Offer[count];
            Enumeration e = collection.elements();
            count = 0;
            while (e.hasMoreElements()) {
                OfferSeqHolder s = (OfferSeqHolder) e.nextElement();
                for (int i = 0; i < s.value.length; i++) {
                    ret[count + i] = s.value[i];
                }
                count += s.value.length;
            }
            return (ret);
        } else return (null);
    }

    /**
     * Will attempt to clear out all the offers for the Trader that this
     * object refers to (see Trader(), the constructor). The Trader needs
     * to be running. This method returns without comment; all errors simply
     * print a message to System.out.
     *
     * @see #Trader
     */
    public void clearAllOffers() {
        if (lookup_ == null) return;
        Register r = lookup_.register_if();
        String constraint = "TRUE";
        try {
            r.withdraw_using_constraint(TRADER_OFFER_TYPE, constraint);
            System.out.println("Offer(s) removed.");
        } catch (IllegalServiceType e) {
            cat.error("Trader" + "(clearAllOffers): " + "IllegalServiceType exception " + "-- attempting to access registered type = " + TRADER_OFFER_TYPE);
        } catch (UnknownServiceType e) {
            cat.error("Trader" + "(clearAllOffers): " + "UnknownServiceType exception " + "-- attempting to access registered type = " + TRADER_OFFER_TYPE);
        } catch (IllegalConstraint e) {
            cat.error("Trader" + "(clearAllOffers): " + "Illegal constraint = " + constraint);
        } catch (org.omg.CosTrading.RegisterPackage.NoMatchingOffers e) {
            cat.error("Trader" + "(clearAllOffers): " + "No offers to clear");
        }
    }

    /**
     * Build a Constraint to handle the traits to be matched
     * @param constraint
     * @return
     */
    private String addTraitsToMatch(String constraint) {
        String trait;
        if (traitsToMatch == null) return constraint;
        if (traitsToMatch.size() == 0) return constraint;
        String newConstraint = "(" + constraint + ")";
        Enumeration e = traitsToMatch.elements();
        while (e.hasMoreElements()) {
            trait = (String) e.nextElement();
            newConstraint += " and ('" + trait + "' in supported_traits)";
        }
        traitsToMatch = null;
        return newConstraint;
    }
}
