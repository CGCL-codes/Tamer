package pl.edu.amu.wmi.kino.visualjavafx.nodes.objects.factory;

import java.beans.IntrospectionException;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import pl.edu.amu.wmi.kino.visualjavafx.nodes.factory.NodeCreator;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.shapes.Elipse;
import pl.edu.amu.wmi.kino.visualjavafx.nodes.objects.shapes.ElipseNode;

/**
 *
 * @author psychollek
 */
public class ElipseNodeFactory implements NodeCreator {

    public boolean canCreateNodeFrom(Object obj) {
        if (obj instanceof Elipse) return true;
        return false;
    }

    public Node generateNode(Object obj) {
        Elipse elp = (Elipse) obj;
        try {
            return new ElipseNode(elp);
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
}
