package no.ntnu.it.fw.saml2api;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * A class that represents an eduPerson from Feide-compatible
 * ldap-servers ( <a href="http://www.feide.no/">http://www.feide.no</a>).
 * </p>
 *
 * <p>It implements Serializable so that saml2api session state can be
 * serialized, both for persisting it across server invocations, and
 * for shared state between clustered server instances.</p>
 *
 * @author Trond Kandal, Applikasjonsseksjonen, NTNU IT, NTNU
 * @version $Id: EduPerson.java 74 2011-05-28 20:05:34Z kandal $
 *
 */
public final class EduPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, List<String>> feideMap;

    private transient EduOrg org;

    private transient List<EduOrgUnit> orgUnits;

    private transient EduOrgUnit primaryOrgUnit;

    /**
     * Construct a EduPerson-object from the given map.
     * @param attributes the map containing the EduPerson attribute values
     */
    public EduPerson(final Map<String, List<String>> attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attributes map may not be null");
        }
        this.feideMap = attributes;
    }

    /**
     * Returns an attribute with the given attribute name.
     * An attribute have a name, but there can be several values for
     * one name. The index parameter tells which of the values to get.
     *
     * @param attribute the attribute name
     * @param index the index
     * @return The attribute or {@code null} if not found.
     */
    String getAttribute(String attribute, int index) {
        List<String> list = feideMap.get(attribute);
        return list != null ? (String) list.get(index) : null;
    }

    List<String> getAttributes(final String attribute) {
        List<String> list = feideMap.get(attribute);
        return list != null ? Collections.unmodifiableList(list) : Collections.<String>emptyList();
    }

    private ArrayList<String> getAttributesDeprecatedArrayList(final String attribute) {
        List<String> list = feideMap.get(attribute);
        return list != null ? new ArrayList<String>(list) : null;
    }

    /**
     * Returns the full name ( cn ) for this person.
     *
     *@return the name for this person; returns null if the name is not set.
     *
     */
    public String getFullname() {
        return getAttribute("cn", 0);
    }

    /**
     * Returns the last name ( sn ) for a person.
     *
     *@return A String with the last name; returns null if not set.
     *
     */
    public String getLastname() {
        return getAttribute("sn", 0);
    }

    /**
     * Gets the principal name ( eduPersonPrincipalName ).The
     * principal name for <a href="http://www.feide.no/">Feide</a> is on
     * the form "ola@domain.no".
     *
     * @return A String with the principal-name; returns {@code null} if not set.
     *
     */
    public String getPrincipalName() {
        return getAttribute("edupersonprincipalname", 0);
    }

    /**
     * Returns the ID of the user ( eduPersonTargetedID ).
     *
     * @return A String with the ID; return {@code null} if not set.
     *
     */
    public String getID() {
        return getAttribute("edupersontargetedid", 0);
    }

    /**
     * Returns the username ( uid ).
     *
     * @return A String with the username; return {@code null} if not set.
     *
     */
    public String getUsername() {
        return getAttribute("uid", 0);
    }

    /**
     * Gets the birth date (noredupersonbirthdate).
     *
     * The string has the format {@code YYYYMMDD}, using 4 digits for year, 2 digits for month and 2 digits for day
     * as described in <a href="http://www.ietf.org/rfc/rfc3339.txt">RFC 3339</a> but without the dashes.
     *
     * @return A String with the birth date.
     */
    public String getBirthDate() {
        return getAttribute("noredupersonbirthdate", 0);
    }

    /**
     * Get the email-address ( mail ).
     *
     * @return A String with the email-address; {@code null} if not set.
     *
     */
    public String getEmail() {
        return getAttribute("mail", 0);
    }

    /**
     * Gets the affiliation ( eduPersonAffiliation ) for this person.
     *
     * @return An ArrayList with all affiliations; {@code null} if not set.
     * @deprecated use {@link #getAffiliations()} instead.
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public ArrayList getAffiliation() {
        return getAttributesDeprecatedArrayList("edupersonaffiliation");
    }

    /**
     * Gets the affiliations ( eduPersonAffiliation ) for this person.
     *
     * @return A list of all affiliations; empty list if not set if not set.
     *
     */
    public List<String> getAffiliations() {
        return getAttributes("edupersonaffiliation");
    }

    /**
     * Gets primary the affiliation ( eduPersonPrimaryAffiliation ) for this person.
     *
     * @return An String with the primary affiliation. Typical values are "member", "faculty", "employee" or "student".
     * Returns {@code null} of not set.
     *
     */
    public String getPrimaryAffiliation() {
        return getAttribute("edupersonprimaryaffiliation", 0);
    }

    /**
     * Gets the orgDN ( eduPersonOrgDN ) for this person.  A legal  orgDN is on
     * the form "dc=domain,dc=no".
     *
     * @return A String with the orgDN; {@code null} if not set.
     *
     */
    public String getOrgDN() {
        return getAttribute("edupersonorgdn", 0);
    }

    /**
     * Gets the local identity numbers ( norEduPersonLIN ) for this person.
     * @return An ArrayList with all identities; {@code null} if not set.
     * @deprecated Use {@link #getLocalIdentityNumbers()} instead.
     */
    @SuppressWarnings("rawtypes")
    @Deprecated
    public ArrayList getNorEduPersonLin() {
        return getAttributesDeprecatedArrayList("noredupersonlin");
    }

    /**
     * Gets the local identity numbers ( norEduPersonLIN ) for this person.
     * @return A list of all identities; empty list if not set if not set.
     */
    public List<String> getLocalIdentityNumbers() {
        return getAttributes("noredupersonlin");
    }

    /**
     * Gets the user's display name (displayName).
     * @return A String with the displayName; {@code null} if not set.
     *
     */
    public String getDisplayName() {
        return getAttribute("displayname", 0);
    }

    /**
     * Gets the persons given name (givenName), not their surname nor
     * middle name.
     * @return A String with the givenName; {@code null} if not set.
     *
     */
    public String getGivenName() {
        return getAttribute("givenname", 0);
    }

    /**
     * Gets the person's national identity number.
     * @return An ArrayList with all NINs; {@code null} if not set.
     * @deprecated Use {@link #getNationalIdentityNumbers()} instead.
     */
    @SuppressWarnings("rawtypes")
    public ArrayList getNorEduPersonNIN() {
        return getAttributesDeprecatedArrayList("noredupersonnin");
    }

    /**
     * Gets the person's national identity number.
     * @return A List with all NINs; empty list if not set if not set.
     */
    public List<String> getNationalIdentityNumbers() {
        return getAttributes("noredupersonnin");
    }

    public EduOrg getOrganization() {
        if (org == null) {
            org = new EduOrg(this);
        }
        return org;
    }

    public List<EduOrgUnit> getOrgUnits() {
        if (orgUnits == null) {
            List<String> dns = feideMap.get("edupersonorgunitdn");
            EduOrgUnit[] units = new EduOrgUnit[dns != null ? dns.size() : 0];
            for (int i = 0; i < units.length; i++) {
                units[i] = new EduOrgUnit(this, i);
            }
            orgUnits = Collections.unmodifiableList(Arrays.asList(units));
        }
        return orgUnits;
    }

    public EduOrgUnit getPrimaryOrgUnit() {
        if (primaryOrgUnit == null) {
            List<EduOrgUnit> units = getOrgUnits();
            String primary = getAttribute("edupersonprimaryorgunitdn", 0);
            if (primary != null && units.size() > 0) {
                for (EduOrgUnit unit : units) {
                    if (primary.equals(unit.getDn())) {
                        primaryOrgUnit = unit;
                        break;
                    }
                }
            }
        }
        return primaryOrgUnit;
    }

    /**
     * @return The contents of the object as a human-readable String
     */
    public String dump() {
        StringBuffer s = new StringBuffer();
        Iterator<String> i = feideMap.keySet().iterator();
        while (i.hasNext()) {
            String property = i.next();
            List<String> values = feideMap.get(property);
            if (values.size() > 1) {
                for (int j = 0; j < values.size(); j++) {
                    s.append(property).append("[").append(j).append("]=").append(values.get(j)).append("\n");
                }
            } else {
                s.append(property).append("=");
                if (values.size() == 1) {
                    s.append(values.get(0));
                }
                s.append("\n");
            }
        }
        return s.toString();
    }

    @Override
    public int hashCode() {
        return feideMap.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this || (other != null && other.getClass() == getClass() && ((EduPerson) other).feideMap.equals(feideMap));
    }
}
