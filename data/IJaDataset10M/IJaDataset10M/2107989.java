package org.polepos.teams.hibernate.data;

import java.util.*;
import org.polepos.framework.*;
import com.db4o.foundation.*;

public class ComplexHolder0 implements CheckSummable {

    private long id;

    private String name;

    private List<ComplexHolder0> children = new ArrayList<ComplexHolder0>();

    private ComplexHolder0[] array;

    private static IdGenerator _idGenerator = new IdGenerator();

    public ComplexHolder0() {
        id = _idGenerator.nextId();
    }

    public static ComplexHolder0 generate(int depth, int leafs) {
        ComplexHolder0 complexHolder = new ComplexHolder0();
        complexHolder.name = "root";
        createChildren(complexHolder, depth - 1, leafs);
        return complexHolder;
    }

    private static void createChildren(ComplexHolder0 root, int depth, int numChildren) {
        if (depth < 1) {
            return;
        }
        int factoryIdx = 0;
        int holderIdx = 0;
        List<ComplexHolder0> parentLevel = Arrays.asList(root);
        for (int i = 0; i < depth; i++) {
            Closure4<ComplexHolder0> curFactory = FACTORIES[factoryIdx];
            List<ComplexHolder0> childLevel = new ArrayList<ComplexHolder0>();
            for (ComplexHolder0 curParent : parentLevel) {
                for (int childIdx = 0; childIdx < numChildren; childIdx++) {
                    ComplexHolder0 curChild = curFactory.run();
                    curChild.name = String.valueOf(holderIdx);
                    curChild.array = createArray(holderIdx);
                    curChild.setSpecial(holderIdx);
                    curParent.addChild(curChild);
                    childLevel.add(curChild);
                    holderIdx++;
                }
            }
            parentLevel = childLevel;
            factoryIdx++;
            if (factoryIdx == FACTORIES.length) {
                factoryIdx = 0;
            }
        }
    }

    private static ComplexHolder0[] createArray(int holderIdx) {
        ComplexHolder0[] holders = new ComplexHolder0[] { new ComplexHolder0(), new ComplexHolder1(), new ComplexHolder2(), new ComplexHolder3(), new ComplexHolder4() };
        for (int i = 0; i < holders.length; i++) {
            holders[i].name = "a" + holderIdx + "_" + i;
        }
        return holders;
    }

    public void addChild(ComplexHolder0 child) {
        children.add(child);
    }

    public static final Closure4[] FACTORIES = { new Closure4<ComplexHolder0>() {

        @Override
        public ComplexHolder0 run() {
            return new ComplexHolder0();
        }
    }, new Closure4<ComplexHolder0>() {

        @Override
        public ComplexHolder0 run() {
            return new ComplexHolder1();
        }
    }, new Closure4<ComplexHolder0>() {

        @Override
        public ComplexHolder0 run() {
            return new ComplexHolder2();
        }
    }, new Closure4<ComplexHolder0>() {

        @Override
        public ComplexHolder0 run() {
            return new ComplexHolder3();
        }
    }, new Closure4<ComplexHolder0>() {

        @Override
        public ComplexHolder0 run() {
            return new ComplexHolder4();
        }
    } };

    @Override
    public long checkSum() {
        class CheckSumVisitor implements Visitor<ComplexHolder0> {

            long checkSum;

            @Override
            public void visit(ComplexHolder0 holder) {
                checkSum += Math.abs(holder.ownCheckSum());
            }
        }
        CheckSumVisitor visitor = new CheckSumVisitor();
        traverse(visitor, new NullVisitor<ComplexHolder0>());
        return visitor.checkSum;
    }

    public void traverse(Visitor<ComplexHolder0> preVisitor, Visitor<ComplexHolder0> postVisitor) {
        internalTraverse(new IdentityHashMap<ComplexHolder0, ComplexHolder0>(), preVisitor, postVisitor);
    }

    private void internalTraverse(IdentityHashMap<ComplexHolder0, ComplexHolder0> visited, Visitor<ComplexHolder0> preVisitor, Visitor<ComplexHolder0> postVisitor) {
        if (visited.containsKey(this)) {
            return;
        }
        visited.put(this, this);
        preVisitor.visit(this);
        for (ComplexHolder0 child : getChildren()) {
            child.internalTraverse(visited, preVisitor, postVisitor);
        }
        if (getArray() != null) {
            for (ComplexHolder0 child : getArray()) {
                child.internalTraverse(visited, preVisitor, postVisitor);
            }
        }
        postVisitor.visit(this);
    }

    public long ownCheckSum() {
        return getName().hashCode();
    }

    protected void setSpecial(int value) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ComplexHolder0> getChildren() {
        return children;
    }

    public void setChildren(List<ComplexHolder0> children) {
        this.children = children;
    }

    public ComplexHolder0[] getArray() {
        return array;
    }

    public void setArray(ComplexHolder0[] array) {
        this.array = array;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
