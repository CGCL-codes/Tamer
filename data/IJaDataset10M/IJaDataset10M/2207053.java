package com.ibm.tuningfork.infra.units;

import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;

/**
 * The unit of measure of a sample, stream, or axis
 * 
 * 
 */
public class FunctionUnit extends Unit {

    public final Unit domain, range;

    public FunctionUnit(Unit domain, Unit range) {
        super(domain.name() + " -> " + range.name(), domain.scaling - range.scaling);
        this.domain = domain;
        this.range = range;
    }

    @Override
    public void collectReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        cb.convert(domain);
        cb.convert(range);
    }

    public boolean equals(Object obj2) {
        if (!(obj2 instanceof FunctionUnit)) {
            return false;
        }
        FunctionUnit unit2 = (FunctionUnit) obj2;
        return domain.equals(unit2.domain) && range.equals(unit2.range);
    }

    public String name() {
        return domain.name() + " -> " + range.name();
    }

    public String nominalUnit() {
        return domain.nominalUnit() + " -> " + range.nominalUnit();
    }

    public String nominalUnitAbbrv() {
        return domain.nominalUnitAbbrv() + " -> " + range.nominalUnitAbbrv();
    }

    public boolean categorical() {
        return false;
    }

    public boolean percentage() {
        return false;
    }
}
