package edu.iastate.pdlreasoner.model;

import java.io.Serializable;
import java.net.URI;

public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    protected URI m_URI;

    protected Role(URI uri) {
        m_URI = uri;
    }

    public URI getURI() {
        return m_URI;
    }

    @Override
    public String toString() {
        return m_URI.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Role)) return false;
        Role other = (Role) obj;
        return m_URI.equals(other.m_URI);
    }

    @Override
    public int hashCode() {
        return m_URI.hashCode();
    }
}
