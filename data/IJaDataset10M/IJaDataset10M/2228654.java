package com.siemens.ct.exi.datatype.strings;

import com.siemens.ct.exi.context.DecoderContext;
import com.siemens.ct.exi.context.QNameContext;
import com.siemens.ct.exi.values.StringValue;
import com.siemens.ct.exi.values.Value;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class BoundedStringDecoderImpl extends StringDecoderImpl {

    protected final int valueMaxLength;

    protected final int valuePartitionCapacity;

    protected int globalID;

    protected LocalIDMap[] localIdMapping;

    static class LocalIDMap {

        final int localID;

        final QNameContext context;

        public LocalIDMap(int localID, QNameContext context) {
            this.localID = localID;
            this.context = context;
        }
    }

    public BoundedStringDecoderImpl(boolean localValuePartitions, int valueMaxLength, int valuePartitionCapacity) {
        super(localValuePartitions);
        this.valueMaxLength = valueMaxLength;
        this.valuePartitionCapacity = valuePartitionCapacity;
        this.globalID = -1;
        if (valuePartitionCapacity > 0 && localValuePartitions) {
            localIdMapping = new LocalIDMap[valuePartitionCapacity];
        }
    }

    @Override
    public void addValue(DecoderContext coder, QNameContext context, StringValue value) {
        if (valueMaxLength < 0 || value.getCharactersLength() <= valueMaxLength) {
            if (valuePartitionCapacity < 0) {
                super.addValue(coder, context, value);
            } else if (valuePartitionCapacity == 0) {
            } else {
                assert (!globalValues.contains(value));
                if ((++globalID) == valuePartitionCapacity) {
                    globalID = 0;
                }
                if (globalValues.size() > globalID) {
                    Value prev = globalValues.set(globalID, value);
                    if (prev != null) {
                        if (localValuePartitions) {
                            LocalIDMap lvsFree = localIdMapping[globalID];
                            assert (lvsFree != null);
                        }
                    }
                } else {
                    globalValues.add(value);
                }
                if (localValuePartitions) {
                    localIdMapping[globalID] = new LocalIDMap(coder.getNumberOfStringValues(context), context);
                    coder.addStringValue(context, value);
                }
            }
        }
    }

    @Override
    public void clear() {
        super.clear();
        globalID = -1;
    }
}
