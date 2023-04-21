package de.volkerraum.pokerbot.tournamentengine.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tournament-report")
@XmlRootElement
public class TournamentReport {

    @XmlAttribute(name = "date")
    protected Date date = null;

    @XmlElement(name = "level")
    protected List<LevelReport> levels = new ArrayList<LevelReport>();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<LevelReport> getLevels() {
        return levels;
    }

    public void setLevels(List<LevelReport> levels) {
        this.levels = levels;
    }
}
