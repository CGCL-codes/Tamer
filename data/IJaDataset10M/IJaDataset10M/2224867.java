package org.iqual.chaplin.wrap;

/**
 * @author Zbynek Slajchrt
 * @since 21.12.2009 19:54:43
 */
public interface WrapperInstance {

    Object invoke(ChainLink next, Object... args) throws Throwable;
}
