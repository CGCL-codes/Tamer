package com.daveoxley.cnery.actions;

import com.daveoxley.cnery.entities.SceneAction;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

/**
 *
 * @author dave
 */
@Name("sceneActionHome")
@Scope(ScopeType.CONVERSATION)
public class SceneActionHome extends EntityHome<SceneAction> {
}
