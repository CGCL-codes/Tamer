package org.gems.designer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.gems.designer.model.Atom;
import org.gems.designer.model.Container;
import org.gems.designer.model.Model;
import org.gems.designer.model.ModelObject;
import org.gems.designer.model.Root;
import org.gems.designer.model.Wire;

public class XMLSerializer extends ObjectSerializer {

    private LinkedList<Wire> cons_;

    @Override
    public ModelInstance readModel(IFile loc, ClassLoader cl, IProgressMonitor m) throws Exception {
        ModelInstance inst = null;
        return inst;
    }

    @Override
    public void serializeModel(ModelInstance tosave, IFile file, IProgressMonitor monitor) throws Exception {
    }

    @Override
    public String getFileExtension() {
        return "gemx";
    }

    @Override
    public String getType() {
        return "XML";
    }

    public void serializeState(StringBuffer xml, ModelObject obj) {
        xml.append("<id>");
        xml.append(obj.getID());
        xml.append("</id>");
        Set<String> names = obj.getAttributeNames();
        for (String name : names) {
            xml.append("\n<atom n='");
            xml.append(name);
            xml.append("'>\n");
            serialize(xml, obj.getAttribute(name));
            xml.append("\n");
            xml.append("</atom>");
        }
        cons_.addAll(obj.getConnections());
    }

    public void serialize(StringBuffer xml, Color c) {
        xml.append("<color>" + c + "</color>");
    }

    public void serialize(StringBuffer xml, Integer i) {
        xml.append("<int>" + i.intValue() + "</int>");
    }

    public void serialize(StringBuffer xml, Double i) {
        xml.append("<dbl>" + i.doubleValue() + "</dbl>");
    }

    public void serialize(StringBuffer xml, Boolean b) {
        xml.append("<bool>" + b.booleanValue() + "</bool>");
    }

    public void serialize(StringBuffer xml, Dimension b) {
        xml.append("<dimension>" + b + "</dimension>");
    }

    public void serialize(StringBuffer xml, Point p) {
        xml.append("<point>" + p + "</point>");
    }

    public void serialize(StringBuffer xml, String b) {
        xml.append("<str>" + b + "</str>");
    }

    public void serialize(StringBuffer xml, Object obj) {
        if (obj instanceof Integer) {
            serialize(xml, (Integer) obj);
        } else if (obj instanceof Double) {
            serialize(xml, (Double) obj);
        } else if (obj instanceof Color) {
            serialize(xml, (Color) obj);
        } else if (obj instanceof Boolean) {
            serialize(xml, (Boolean) obj);
        } else if (obj instanceof String) {
            serialize(xml, (String) obj);
        } else if (obj instanceof Point) {
            serialize(xml, (Point) obj);
        } else if (obj instanceof Dimension) {
            serialize(xml, (Dimension) obj);
        } else if (obj == null) {
            xml.append("null");
        } else {
            try {
                ByteArrayOutputStream bits = new ByteArrayOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(bits);
                oo.writeObject(obj);
                oo.flush();
                oo.close();
                xml.append("<obj><![CDATA[" + new String(bits.toByteArray()) + "]]></obj>");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void serialize(StringBuffer xml, ModelObject obj) {
        if (obj instanceof Container) {
            xml.append("<container type='" + obj.getClass().getName() + "'>");
            serializeState(xml, obj);
            xml.append("<ch>");
            List children = ((Container) obj).getChildren();
            for (Object c : children) {
                serialize(xml, (ModelObject) c);
            }
            xml.append("</ch>");
            xml.append("</connection>");
        } else if (obj instanceof Atom) {
            xml.append("<atom>");
            serializeState(xml, obj);
            xml.append("</atom>");
        }
    }

    public void serialize(StringBuffer xml, Wire obj) {
        xml.append("<cn s='" + obj.getSource().getID() + "' ");
        xml.append("t='" + obj.getTarget().getID() + "' ");
        xml.append("st='" + obj.getSourceTerminal() + "' ");
        xml.append("tt='" + obj.getTargetTerminal() + "'>");
        Enumeration names = obj.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            xml.append("\n<a n='");
            xml.append(name);
            xml.append("'>\n");
            serialize(xml, obj.getAttribute(name));
            xml.append("\n");
            xml.append("</a>");
        }
        xml.append("</cn>");
    }

    public void serialize(StringBuffer xml, ModelInstance inst) {
        cons_ = new LinkedList<Wire>();
        xml.append("<mdl>");
        serialize(xml, inst.getRoot());
        xml.append("<cns>");
        for (Wire w : cons_) {
            serialize(xml, w);
        }
        xml.append("</cns>");
        xml.append("</mdl>");
    }
}
