package org.opengis.test;

import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.Collection;
import org.opengis.util.InternationalString;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Series;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.CitationDate;
import org.opengis.metadata.citation.PresentationForm;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.IdentifiedObject;
import org.junit.*;
import static org.junit.Assert.*;
import static org.opengis.test.FactoryFilter.ByAuthority.EPSG;

/**
 * Tests {@link FactoryFilter}.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   3.1
 */
public strictfp class FactoryFilterTest implements AuthorityFactory, Citation, InternationalString {

    /**
     * The authority name of the dummy factory to use for testing purpose.
     */
    private String authority;

    /**
     * Tests the filtering of EPSG factories.
     */
    @Test
    public void testEPSG() {
        assertTrue(filter("EPSG"));
        assertTrue(filter("epsg"));
        assertTrue(filter(" EPSG  "));
        assertFalse(filter("AUTO"));
        assertFalse(filter("IGNF"));
    }

    /**
     * Returns the result of {@link FactoryFilter#filter} when given a factory for the
     * given authority.
     */
    private boolean filter(final String expected) {
        authority = expected;
        return EPSG.filter(AuthorityFactory.class, this);
    }

    @Override
    public Citation getAuthority() {
        return this;
    }

    @Override
    public InternationalString getTitle() {
        return this;
    }

    @Override
    public Collection<InternationalString> getAlternateTitles() {
        return null;
    }

    @Override
    public String toString(Locale locale) {
        return authority;
    }

    @Override
    public int length() {
        return authority.length();
    }

    @Override
    public char charAt(int index) {
        return authority.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return authority.substring(start, end);
    }

    @Override
    public int compareTo(InternationalString o) {
        return authority.compareTo(o.toString());
    }

    @Override
    public Citation getVendor() {
        return null;
    }

    @Override
    public Collection<CitationDate> getDates() {
        return null;
    }

    @Override
    public InternationalString getEdition() {
        return null;
    }

    @Override
    public Date getEditionDate() {
        return null;
    }

    @Override
    public Collection<Identifier> getIdentifiers() {
        return null;
    }

    @Override
    public Collection<ResponsibleParty> getCitedResponsibleParties() {
        return null;
    }

    @Override
    public Collection<PresentationForm> getPresentationForms() {
        return null;
    }

    @Override
    public Series getSeries() {
        return null;
    }

    @Override
    public InternationalString getOtherCitationDetails() {
        return null;
    }

    @Override
    public InternationalString getCollectiveTitle() {
        return null;
    }

    @Override
    public String getISBN() {
        return null;
    }

    @Override
    public String getISSN() {
        return null;
    }

    @Override
    public InternationalString getDescriptionText(String code) {
        return null;
    }

    @Override
    public IdentifiedObject createObject(String code) {
        return null;
    }

    @Override
    public Set<String> getAuthorityCodes(Class<? extends IdentifiedObject> type) {
        return null;
    }
}
