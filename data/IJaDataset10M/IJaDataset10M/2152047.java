package net.zero.smarttrace.data.events;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import net.zero.smarttrace.data.EArgument;

@Entity
public class EMethodEntryEvent extends EStackFrameLocatableEvent {

    private Set<EArgument> arguments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "methodEntryEvent")
    public Set<EArgument> getArguments() {
        return arguments;
    }

    public void setArguments(Set<EArgument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " -> " + super.toString();
    }
}
