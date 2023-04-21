package wcp.model.world;

import wcp.model.world.LevelRequirement;

/**
 * @hibernate.joined-subclass table = "LEVELREQUIREMENT"
 * @hibernate.joined-subclass-key column = "ID"
 * 
 * @author timconinx
 *
 */
public class LevelRequirementImpl extends InstanceRequirementImpl implements LevelRequirement {
}
