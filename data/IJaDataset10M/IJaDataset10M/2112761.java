package edu.tufts.osidimpl.repository.google.local;

public class Type extends org.osid.shared.Type {

    protected Type(String authority, String domain, String keyword, String description) {
        super(authority, domain, keyword, description);
    }

    protected Type(String authority, String domain, String keyword) {
        super(authority, domain, keyword);
    }
}
