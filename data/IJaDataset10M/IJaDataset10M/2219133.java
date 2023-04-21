package ca.cutterslade.match.scheduler;

import java.util.Set;
import com.google.common.collect.ImmutableSet;

public final class Time {

    private final String name;

    static ImmutableSet<Time> forNames(Set<String> names) {
        ImmutableSet.Builder<Time> b = ImmutableSet.builder();
        for (String name : names) b.add(new Time(name));
        return b.build();
    }

    Time(String name) {
        if (null == name) throw new IllegalArgumentException("name may not be null");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Time other = (Time) obj;
        if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Time [name=" + name + "]";
    }
}
