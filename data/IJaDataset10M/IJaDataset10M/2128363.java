package org.wsmostudio.bpmo.imports.epml.model;

/**
 * @author Luchesar Cekov
 */
public class Arc extends IntIdentifiable {

    private Integer source;

    private Integer target;

    public Arc(EPC aEpc) {
        super(aEpc);
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer aSource) {
        source = aSource;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer aTarget) {
        target = aTarget;
    }
}
