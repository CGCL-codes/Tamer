package proxymusic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>Comments from original DTD:
 * <pre>
 * Ornaments can be any of several types, followed
 * optionally by accidentals. The accidental-mark
 * element's content is represented the same as an
 * accidental element, but with a different name to
 * reflect the different musical meaning.
 * </pre>
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;choice>
 *             &lt;element ref="{}trill-mark"/>
 *             &lt;element ref="{}turn"/>
 *             &lt;element ref="{}delayed-turn"/>
 *             &lt;element ref="{}inverted-turn"/>
 *             &lt;element ref="{}shake"/>
 *             &lt;element ref="{}wavy-line"/>
 *             &lt;element ref="{}mordent"/>
 *             &lt;element ref="{}inverted-mordent"/>
 *             &lt;element ref="{}schleifer"/>
 *             &lt;element ref="{}tremolo"/>
 *             &lt;element ref="{}other-ornament"/>
 *           &lt;/choice>
 *           &lt;element ref="{}accidental-mark" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "trillMarkOrTurnOrDelayedTurn" })
@XmlRootElement(name = "ornaments")
public class Ornaments implements Serializable {

    @XmlElements({ @XmlElement(name = "accidental-mark", type = AccidentalMark.class), @XmlElement(name = "other-ornament", type = OtherOrnament.class), @XmlElement(name = "inverted-mordent", type = InvertedMordent.class), @XmlElement(name = "mordent", type = Mordent.class), @XmlElement(name = "shake", type = Shake.class), @XmlElement(name = "tremolo", type = Tremolo.class), @XmlElement(name = "schleifer", type = Schleifer.class), @XmlElement(name = "trill-mark", type = TrillMark.class), @XmlElement(name = "wavy-line", type = WavyLine.class), @XmlElement(name = "inverted-turn", type = InvertedTurn.class), @XmlElement(name = "turn", type = Turn.class), @XmlElement(name = "delayed-turn", type = DelayedTurn.class) })
    protected List<Object> trillMarkOrTurnOrDelayedTurn;

    /**
     * Gets the value of the trillMarkOrTurnOrDelayedTurn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trillMarkOrTurnOrDelayedTurn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTrillMarkOrTurnOrDelayedTurn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccidentalMark }
     * {@link OtherOrnament }
     * {@link InvertedMordent }
     * {@link Mordent }
     * {@link Shake }
     * {@link Tremolo }
     * {@link Schleifer }
     * {@link TrillMark }
     * {@link WavyLine }
     * {@link InvertedTurn }
     * {@link Turn }
     * {@link DelayedTurn }
     * 
     * 
     */
    public List<Object> getTrillMarkOrTurnOrDelayedTurn() {
        if (trillMarkOrTurnOrDelayedTurn == null) {
            trillMarkOrTurnOrDelayedTurn = new ArrayList<Object>();
        }
        return this.trillMarkOrTurnOrDelayedTurn;
    }
}
