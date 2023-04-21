package core.view.component;

import java.util.StringTokenizer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import core.var.JListVar;
import core.view.JaxilComponent;
import core.view.List;
import core.view.builder.Item;

/**
 * create a List
 * @author gael
 */
public class JListFactory implements ComponentFactory {

    public void create(Item out, String name, String type, String icon, String label, String values) {
        List o = new List();
        if (values != null) {
            StringTokenizer split = new StringTokenizer(values, ";");
            DefaultListModel model = new DefaultListModel();
            while (split.hasMoreTokens()) model.addElement(split.nextToken());
            o.setModel(model);
        }
        out.setComponent(o);
    }

    @Override
    public String getText(JaxilComponent comp) {
        StringBuffer buf = new StringBuffer();
        ListModel listmodel = ((JList) comp).getModel();
        for (int i = 0; i < listmodel.getSize(); i++) buf.append(listmodel.getElementAt(i) + ";");
        return buf.toString();
    }

    @Override
    public Object getVar(JaxilComponent comp) {
        return new JListVar((JList) comp);
    }
}
