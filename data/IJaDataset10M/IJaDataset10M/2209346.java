package de.zeroseven.abc;

/**
 * @author t.uhlmann
 *
 */
public class ASNamespaceSet extends ABCObject {

    private ASConstantPool abc;

    private int[] ns;

    /**
	 * @param constantPool
	 */
    public ASNamespaceSet(ASConstantPool cpool) {
        this.abc = cpool;
    }

    @Override
    public void decode(ABCCoder coder) {
        int count = coder.readU30();
        ns = new int[count];
        for (int i = 0; i < ns.length; i++) {
            ns[i] = coder.readU30();
        }
    }

    @Override
    public void encode(ABCCoder coder) {
        coder.writeU30(ns.length);
        for (int i = 0; i < ns.length; i++) {
            coder.writeU30(ns[i]);
        }
    }

    @Override
    public int length() {
        return ABCCoder.getVarLenSize(ns, true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NamespaceSet {");
        for (int i = 0; i < ns.length; i++) {
            sb.append(abc.getNamespace(i));
            if (i < ns.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
