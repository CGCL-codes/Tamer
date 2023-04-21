package com.monad.homerun.objmgt;

import java.util.Map;
import java.io.IOException;

/**
 * ControlRuntime is the high-level runtime interface for all object controls
 */
public interface ControlRuntime {

    String getName();

    boolean doAction(String action, Map<String, Object> parameters) throws IOException;
}
