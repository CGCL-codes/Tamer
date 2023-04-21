package net.sf.balm.biz.module;

/**
 * 模块中心扩展点，模块中心只提供了最基本的功能， 在用户进行扩展和自定制的时候默认的模块中心并不能完全支持用户需求，
 * 因此用户可以通过实现扩展点的方式对默认的模块中心进行功能增强
 * 
 * @author dz
 */
public interface IModuleCenterExtensionPoint extends ILifecycle {
}
