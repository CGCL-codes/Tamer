package astcentric.structure.vl.basic;

import astcentric.structure.bl.Data;
import astcentric.structure.bl.DataFactory;
import astcentric.structure.bl.LongData;

class LongDataFactory extends NumberDataFactory implements DataFactory<Long> {

    LongDataFactory(Compiler compiler, BasicVLNodes nodes) {
        super(compiler, nodes);
    }

    public Data create(Long data) {
        return new LongData(getConstructor(), data);
    }
}
