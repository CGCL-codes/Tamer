package packetsamurai.parser;

import packetsamurai.parser.datatree.DataTreeNodeContainer;
import packetsamurai.parser.datatree.ValuePart;
import packetsamurai.parser.formattree.Part;
import packetsamurai.parser.parttypes.BlockPartType;
import packetsamurai.parser.parttypes.DoublePartType;
import packetsamurai.parser.parttypes.FloatPartType;
import packetsamurai.parser.parttypes.IntBSPartType;
import packetsamurai.parser.parttypes.IntPartType;
import packetsamurai.parser.parttypes.LongPartType;
import packetsamurai.parser.parttypes.RawPartType;
import packetsamurai.parser.parttypes.StringPartType;
import packetsamurai.parser.parttypes.BlockPartType.blockType;
import packetsamurai.parser.parttypes.IntPartType.intType;
import packetsamurai.parser.parttypes.StringPartType.stringType;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public abstract class PartType {

    private String _name;

    public static final PartType c = new IntPartType("c", intType.c);

    public static final PartType h = new IntPartType("h", intType.h);

    public static final PartType d = new IntPartType("d", intType.d);

    public static final PartType bcc = new IntBSPartType("cbs", intType.c);

    public static final PartType bch = new IntBSPartType("bch", intType.h);

    public static final PartType bcd = new IntBSPartType("dbs", intType.d);

    public static final PartType f = new FloatPartType("f");

    public static final PartType df = new DoublePartType("df");

    public static final PartType Q = new LongPartType("Q");

    public static final PartType S = new StringPartType("S", stringType.S);

    public static final PartType s = new StringPartType("s", stringType.s);

    public static final PartType b = new RawPartType("b");

    public static final PartType x = new RawPartType("x");

    public static final PartType Ss = new StringPartType("Ss", stringType.Ss);

    public static final PartType forBlock = new BlockPartType("forblock", blockType.forblock);

    public static final PartType swicthBlock = new BlockPartType("switchblock", blockType.switchblock);

    public static final PartType block = new BlockPartType("block", blockType.block);

    public PartType(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public abstract ValuePart getValuePart(DataTreeNodeContainer parent, Part part);

    public abstract boolean isReadableType();

    public abstract boolean isBlockType();

    public abstract int getTypeByteNumber();

    @Override
    public String toString() {
        return _name;
    }
}
