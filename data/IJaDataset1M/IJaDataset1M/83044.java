package com.loribel.commons.swing.editor;

import org.syntax.jedit.tokenmarker.PropsTokenMarker;

/**
 * Editor for gbxml.
 */
public class GB_EditorXsml extends GB_EditorAbstract {

    public GB_EditorXsml() {
        this.setTokenMarker(new PropsTokenMarker());
        this.setTabSize(2);
    }
}
