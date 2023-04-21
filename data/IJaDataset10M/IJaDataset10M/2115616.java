package org.unitmetrics.core;

import org.eclipse.core.resources.IResource;
import org.unitmetrics.IUnit;
import org.unitmetrics.util.Assert;

/**
 * Default implementation of a resource unit.
 * @author Martin Kersten
 */
public class Unit implements IUnit {

    private final IResource resource;

    private final String identifier;

    private final String unitType;

    private final Object unit;

    private final int level;

    public Unit(IResource resource, Object unit, String unitType, String identifier, int level) {
        Assert.notNull(resource);
        Assert.notNull(unitType);
        Assert.notNull(identifier);
        Assert.inRange(level, 0, Integer.MAX_VALUE);
        this.resource = resource;
        this.unit = unit;
        this.unitType = unitType;
        this.identifier = identifier;
        this.level = level;
    }

    public IResource getResource() {
        return resource;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Object getUnit() {
        return unit;
    }

    public int getLevel() {
        return level;
    }

    public int hashCode() {
        return identifier.hashCode();
    }

    public boolean equals(Object object) {
        if (object == this) return true; else if (object != null && object instanceof IUnit) {
            IUnit unit = (IUnit) object;
            return getUnitType().equals(unit.getUnitType()) && getIdentifier().equals(unit.getIdentifier()) && getResource().equals(unit.getResource());
        }
        return false;
    }
}
