package net.sf.lightbound.controller;

/**
 * @author Esa Tanskanen
 *
 */
public interface InterfaceProvider {

    ContextRenderInterface get(Class<?> clazz);
}
