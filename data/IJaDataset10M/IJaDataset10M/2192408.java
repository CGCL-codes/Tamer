package net.seagis.metadata;

import java.util.Collection;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import net.seagis.catalog.Entry;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.CitationDate;
import org.opengis.metadata.citation.PresentationForm;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.metadata.citation.Series;
import org.opengis.util.InternationalString;

/**
 *
 * @author legal
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Citation")
public class CitationEntry extends Entry implements Citation {

    public InternationalString getTitle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<? extends InternationalString> getAlternateTitles() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<? extends CitationDate> getDates() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InternationalString getEdition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getEditionDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<? extends Identifier> getIdentifiers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<String> getIdentifierTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<? extends ResponsibleParty> getCitedResponsibleParties() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<PresentationForm> getPresentationForm() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Series getSeries() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InternationalString getOtherCitationDetails() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InternationalString getCollectiveTitle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getISBN() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getISSN() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
