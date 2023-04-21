package net.sf.xisemele.impl;

import net.sf.xisemele.api.Xisemele;

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
        FormatterProviderImpl formatterProvider = new FormatterProviderImpl();
        return new XisemeleImpl(new FactoryImpl(formatterProvider), formatterProvider);
    }
}
