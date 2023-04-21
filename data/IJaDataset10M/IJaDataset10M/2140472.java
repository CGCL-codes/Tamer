package edu.neu.ccs.demeterf.demfgen;

import edu.neu.ccs.demeterf.demfgen.classes.*;
import edu.neu.ccs.demeterf.lib.List;

/**  */
public class TypeCollect<T> {

    List<T> used = List.create();

    public synchronized void add(T tu) {
        used = used.push(tu);
    }

    public synchronized void pop() {
        used = used.pop();
    }

    public T top() {
        return used.top();
    }

    public boolean isEmpty() {
        return used.isEmpty();
    }

    public synchronized boolean has(List.Pred<T> p) {
        return used.contains(p);
    }

    public String toString() {
        return "" + used;
    }

    public static class UseCollect extends TypeCollect<TypeUse> {

        public static List.Pred<TypeUse> comp(final TypeUse tu) {
            return new List.Pred<TypeUse>() {

                public boolean huh(TypeUse tu2) {
                    return tu.toString().equals(tu2.toString());
                }
            };
        }
    }

    public static class DefCollect extends TypeCollect<TypeDef> {

        public static List.GComp<TypeUse, TypeDef> comp() {
            return new Comp();
        }

        public static class Comp extends List.GComp<TypeUse, TypeDef> {

            public boolean comp(TypeUse tu, TypeDef td) {
                return fullname(tu).equals(fullname(td));
            }

            public String fullname(TypeUse t) {
                return t.toString();
            }

            public String fullname(TypeDef t) {
                return t.name() + t.typeParams();
            }
        }
    }
}
