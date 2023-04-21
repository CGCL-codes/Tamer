package net.sourceforge.httpchecker.interfaces;

import java.net.URI;

public interface FindHandler {

    void found(String text, String group, URI file);
}
