package eu.actorsproject.xlim.type;

import java.util.List;
import eu.actorsproject.util.Pair;
import eu.actorsproject.xlim.XlimType;
import eu.actorsproject.xlim.XlimTypeArgument;

public class ListTypeConstructor extends ParametricTypeKind {

    public ListTypeConstructor() {
        super("List");
    }

    @Override
    protected Object getParameter(List<XlimTypeArgument> typeArgList) {
        if (typeArgList.size() == 2) {
            XlimType type = null;
            Integer size = null;
            for (XlimTypeArgument arg : typeArgList) {
                String name = arg.getName();
                if (name.equals("type")) {
                    type = arg.getType();
                } else if (name.equals("size")) {
                    size = Integer.valueOf(arg.getValue());
                } else throw new IllegalArgumentException("Unexpected parameter \"" + name + "\" to type List");
            }
            if (type != null && size != null) return new Pair<XlimType, Integer>(type, size);
        }
        throw new IllegalArgumentException("Type \"List\" requires parameters \"type\" and \"size\"");
    }

    @Override
    protected XlimType create(Object typeParameter) {
        Pair pair = (Pair) typeParameter;
        XlimType type = (XlimType) pair.getFirst();
        Integer size = (Integer) pair.getSecond();
        return new ListType(this, type, size);
    }

    @Override
    XlimType createLub(XlimType t1, XlimType t2) {
        assert (t1 == t2);
        return t1;
    }
}
