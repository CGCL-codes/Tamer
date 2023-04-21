package org.xisemele.impl;

import org.xisemele.api.Xisemele;

/**
 * Fábrica para {@link Xisemele}.
 * 
 * @author Carlos Eduardo Coral.
 */
public class XisemeleFactory {

    /**
    * Retorna uma nova instância de {@link Xisemele}.
    * 
    * @return
    *       nova instância de {@link Xisemele}.
    */
    public static Xisemele newXisemele() {
        return new XisemeleImpl();
    }
}
