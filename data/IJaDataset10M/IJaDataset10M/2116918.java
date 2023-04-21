package vydavky.objects.save;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Objekt pre ukladanie mien do XML pomocou JAXB.
 *
 * @see vydavky.client.ciselniky.CMena
 */
@XmlType
public class CMenaSave {

    @XmlAttribute
    public Long id;

    @XmlAttribute
    public String text;

    @XmlAttribute
    public Long poradie;

    /**
   * Bezparametricky konstruktor
   */
    @SuppressWarnings("PMD")
    public CMenaSave() {
    }

    /**
   * Bezparametricky konstruktor
   */
    public CMenaSave(final Long id, final String text, final Long poradie) {
        this.id = id;
        this.text = text;
        this.poradie = poradie;
    }
}
