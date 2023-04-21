package org.proteusframework.core.api.model;

import org.proteusframework.core.api.config.IConfigurable;

/**
 * Standard Proteus Plugin. This interface is strongly preferred for development of standard Proteus plugins that are
 * not Proteus Platform Service plugin instances.
 *
 * @author Tacoma Four
 */
public interface IStandardPlugin extends IPlugin, IDisplayableName, IConfigurable {
}
