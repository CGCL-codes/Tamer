package org.xith3d.render.prerender;

import org.xith3d.render.shader.Shader;

/**
 * A simple class for encapsulating a definition of a set of state priorites for
 * sorting. We will have different profiles attached to different render bins to
 * optimize for different conditions.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus) [code cleaning]
 */
public class StatePriorities {

    protected int statePriorities[] = new int[Shader.MAX_SHADER_STATES];

    protected int numStatePriorities = 0;

    public StatePriorities() {
    }
}
