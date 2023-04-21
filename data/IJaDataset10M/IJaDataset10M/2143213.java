package org.identifylife.descriptlet.store.oxm;

import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.identifylife.core.utils.CollectionUtils;
import org.identifylife.descriptlet.store.model.State;

/**
 * 
 * @author dbarnier
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class States {

    @XmlElement(name = "state")
    private List<State> states;

    public States() {
    }

    public States(Collection<State> states) {
        if (states != null && states.size() > 0) {
            this.states = CollectionUtils.newArrayList();
            this.states.addAll(states);
        }
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    @XmlAttribute(name = "count")
    public int getStateCount() {
        if (states != null) {
            return states.size();
        }
        return 0;
    }

    @XmlTransient
    public List<String> getStateIds() {
        List<String> results = CollectionUtils.newArrayList();
        if (states != null) {
            for (State s : states) {
                results.add(s.getUuid());
            }
        }
        return results;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("states", getStates()).toString();
    }
}
