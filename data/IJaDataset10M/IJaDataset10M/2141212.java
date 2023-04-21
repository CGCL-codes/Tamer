package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.dom.Element;

/**
 * This interface is responsible for abstracting the rendering of stylistic
 * markup to emulate style properties, for protocols such as HTML3.2 and WML
 * which do not support CSS at all.
 * <p>
 * It provides a simple-to-use "facade" interface for the protocols to use.
 */
public interface StyleEmulationRenderer {

    /**
     * Write stylistic markup to emulate the style provided.
     *
     * @param element the output buffer to write to.
     */
    void applyStyleToElement(Element element);

    /**
     * Tell the renderer that the specified element should not have
     * styling adding to it.
     */
    void exclude(String element);
}
