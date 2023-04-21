package org.vesuf.model.application;

import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.model.uml.behavior.statemachines.*;
import org.vesuf.model.presentation.*;

/**
 *  The presentation mapper maps ui states and classifiers to parts.
 *  This implements essentially a ternary assiciation between
 *  model element, ui states and ui parts.
 */
public interface IPresentationMapper {

    public IPart getPart(IStateVertex state, IModelElement element);
}
