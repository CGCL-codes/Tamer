package org.freelords.xmlmanager;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/** Scenario as can be loaded from an XML file.
 *
 * Note that this class is closely connected to the {@link ScenarioSummary} class.
 * However, while the latter just keeps modifiable and the most important data,
 * this one represents a true scenario freshly loadable from XML, including set
 * up armies etc.
 *
 * Some of the advanced things a scenario could in principle do (e.g. requiring
 * certain players to appear) are, however, not implemented, so this class is
 * not yet finished.
 *
 * @author James Andrews
 */
@XmlRootElement(name = "scenario")
public class Scenario {

    /** The name of the scenario */
    @XmlAttribute
    private String name;

    /** Preferred background music. */
    @XmlElement(name = "background-music")
    private BackgroundPlayList playList;

    /** Returns the name */
    public String getName() {
        return name;
    }

    public BackgroundPlayList getBackgroundPlayList() {
        return playList;
    }
}
