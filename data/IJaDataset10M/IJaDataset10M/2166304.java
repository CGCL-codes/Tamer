package jest.ui;

import java.awt.Component;
import java.awt.Label;
import javax.swing.tree.TreeNode;
import jest.classfile.ClassFile;
import jest.classfile.MethodDescriptor;
import jest.bytecode.Disassembler;

/**
  * The ClassMethodsTreeNode reads in the class methods and attributes,
  * including the method bytecode.
  */
public class ClassMethodsTreeNode extends VectorTreeNode implements Showable {

    MethodDescriptor[] mds;

    public ClassMethodsTreeNode(ClassFile cf, TreeNode parent) {
        int i, j, k;
        StringTreeNode stn;
        Log.log("Creating a new ClassMethodsTreeNode.", "ui", Log.EXTRA);
        mds = cf.getMethods();
        setParent(parent);
        setName("methods");
        for (i = 0; i < mds.length; i++) {
            Object[] chlds;
            if (mds[i].hasCodeAttribute()) {
                chlds = new Object[4];
                chlds[0] = "code";
                Log.log("Creating disassembler for method #" + i + ": " + mds[i].toString(), "ui", Log.EXTRA);
                chlds[1] = new Disassembler(mds[i].getCodeAttribute(), cf);
            } else chlds = new Object[2];
            chlds[chlds.length - 2] = "attributes";
            Log.log("Creating attributes children for method #" + i, "ui", Log.EXTRA);
            k = mds[i].getAttributes().length;
            chlds[chlds.length - 1] = new Object[2 * k];
            for (j = 0; j < k; j++) {
                ((Object[]) chlds[chlds.length - 1])[2 * j] = mds[i].getAttributes()[j].toString();
                ((Object[]) chlds[chlds.length - 1])[2 * j + 1] = mds[i].getAttributes()[j];
            }
            stn = new StringTreeNode(mds[i].toString(), chlds);
            stn.setDisplay(mds[i]);
            stn.setChildLongLabel(k, "Method attributes");
            add(stn);
        }
    }

    public Component getComponent() {
        return new Label("> The methods defined by this class.");
    }
}
