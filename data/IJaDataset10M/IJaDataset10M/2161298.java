package uk.ac.roslin.ensembl.model;

import java.io.Serializable;
import java.util.Comparator;
import uk.ac.roslin.ensembl.model.core.CoordinateSystem;

public abstract class AbstractMapping implements Serializable {

    protected MappableObject source = null;

    protected MappableObject target = null;

    protected AbstractMapping reverseMapping = null;

    protected AbstractCoordinate sourceCoordinates = null;

    protected AbstractCoordinate targetCoordinates = null;

    public void setSource(MappableObject source) {
        this.source = source;
    }

    public void setTarget(MappableObject target) {
        this.target = target;
    }

    public MappableObject getSource() {
        return source;
    }

    public MappableObject getTarget() {
        return target;
    }

    public ObjectType getTargetType() {
        return (this.getTarget() != null) ? this.getTarget().getType() : null;
    }

    public ObjectType getSourceType() {
        return (this.getSource() != null) ? this.getSource().getType() : null;
    }

    public String getTargetHashID() {
        return (this.getTarget() != null) ? this.getTarget().getHashID() : "";
    }

    public String getSourceHashID() {
        return (this.getSource() != null) ? this.getSource().getHashID() : "";
    }

    public AbstractCoordinate getSourceCoordinates() {
        return this.sourceCoordinates;
    }

    public AbstractCoordinate getTargetCoordinates() {
        return this.targetCoordinates;
    }

    public void setSourceCoordinates(AbstractCoordinate coord) {
        this.sourceCoordinates = coord;
    }

    public void setTargetCoordinates(AbstractCoordinate coord) {
        this.targetCoordinates = coord;
    }

    @Override
    public boolean equals(Object mapping) {
        if (this == mapping) {
            return true;
        }
        if (!(mapping instanceof AbstractMapping)) {
            return false;
        }
        AbstractMapping m = (AbstractMapping) mapping;
        boolean out = true;
        if (!(!this.getSourceHashID().equals("") && !m.getSourceHashID().equals("") && !this.getTargetHashID().equals("") && !m.getTargetHashID().equals("") && this.getSourceHashID().equals(m.getSourceHashID()) && this.getTargetHashID().equals(m.getTargetHashID()))) {
            return false;
        }
        return out;
    }

    @Override
    public int hashCode() {
        return this.getSourceHashID().hashCode() + this.getTargetHashID().hashCode();
    }

    public abstract AbstractMapping getReverseMapping();

    public void setReverseMapping(AbstractMapping reverseMapping) {
        this.reverseMapping = reverseMapping;
    }

    public static final MappingOnSourceComparator mappingOnSourceComparator = new MappingOnSourceComparator();

    public static final MappingOnTargetComparator mappingOnTargetComparator = new MappingOnTargetComparator();
}

/**
     * Comparator Class for Mappings, based primarily on their source coordinates.
     * This will sort a set of Mappings on the order of the source coordinates
     * or failing that on the ids on the target object, then source object. Beware
     * that this comparison doesn't match the equals logic.
     * @author tpaterso
     */
class MappingOnSourceComparator implements Comparator<AbstractMapping>, Serializable {

    public int compare(AbstractMapping o1, AbstractMapping o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2 == null) {
            return +1;
        }
        int out;
        if (o1.getSourceCoordinates() == null) {
            if (o2.getSourceCoordinates() == null) {
                out = 0;
            } else {
                return -1;
            }
        } else if (o2.getSourceCoordinates() == null) {
            return +1;
        } else {
            out = o1.getSourceCoordinates().compareTo(o2.getSourceCoordinates());
        }
        if (out != 0) {
            return out;
        } else {
            if (o1.getTargetHashID().equals(o2.getTargetHashID()) && o1.getSourceHashID().equals(o2.getSourceHashID())) {
                return 0;
            } else {
                out = -1;
                try {
                    out = o1.getTargetHashID().compareTo(o2.getTargetHashID());
                    if (out == 0) {
                        out = o1.getSourceHashID().compareTo(o2.getSourceHashID());
                    }
                    if (out == 0) {
                        out = -1;
                    }
                } catch (Exception e) {
                }
                return out;
            }
        }
    }
}

/**
     * Comparator Class for Mappings, based primarily on their target coordinates.
     * This will sort a set of Mappings on the order of the target coordinates
     * or failing that on the ids on the source object, then target object. Beware
     * that this comparison doesn't match the equals logic.
     * @author tpaterso
     */
class MappingOnTargetComparator implements Comparator<AbstractMapping>, Serializable {

    public int compare(AbstractMapping o1, AbstractMapping o2) {
        if (o1 == null) {
            if (o2 == null) {
                return 0;
            } else {
                return -1;
            }
        } else if (o2 == null) {
            return +1;
        }
        int out;
        if (o1.getTargetCoordinates() == null) {
            if (o2.getTargetCoordinates() == null) {
                out = 0;
            } else {
                return -1;
            }
        } else if (o2.getTargetCoordinates() == null) {
            return +1;
        } else {
            out = o1.getTargetCoordinates().compareTo(o2.getTargetCoordinates());
        }
        if (out != 0) {
            return out;
        } else {
            if (o1.getTargetHashID().equals(o2.getTargetHashID()) && o1.getSourceHashID().equals(o2.getSourceHashID())) {
                return 0;
            } else {
                out = -1;
                try {
                    out = o1.getSourceHashID().compareTo(o2.getSourceHashID());
                    if (out == 0) {
                        out = o1.getTargetHashID().compareTo(o2.getTargetHashID());
                    }
                    if (out == 0) {
                        out = -1;
                    }
                } catch (Exception e) {
                }
                return out;
            }
        }
    }
}
