package com.abstratt.uml.classdiagram;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Extension;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import com.abstratt.modelviewer.IndentedPrintWriter;
import com.abstratt.modelviewer.render.IRenderingSession;

public class StereotypeRenderer implements IEObjectRenderer<Class> {

    public void renderObject(Class element, IndentedPrintWriter w, IRenderingSession<EObject> context) {
        w.println("// stereotype " + element.getQualifiedName());
        w.print('"' + element.getName() + "\" [");
        w.println("label=<");
        w.enterLevel();
        w.println("<TABLE border=\"0\" cellspacing=\"0\" cellpadding=\"0\" cellborder=\"0\" port=\"port\">");
        w.print("<TR><TD><TABLE border=\"1\" cellborder=\"0\" CELLPADDING=\"3\" CELLSPACING=\"0\" ALIGN=\"LEFT\">");
        w.print("<TR><TD>");
        w.print(UMLRenderingUtils.addGuillemots("stereotype"));
        w.print("</TD></TR>");
        w.print("<TR><TD>");
        w.print(element.getName());
        w.print("</TD></TR>");
        if (context.isShallow()) {
            w.print("<TR><TD>");
            w.print("(from " + element.getNearestPackage().getQualifiedName() + ")");
            w.print("</TD></TR>");
        }
        w.print("</TABLE></TD></TR>");
        List<Property> properties = new ArrayList<Property>();
        for (Property property : element.getOwnedAttributes()) if (!(property.getAssociation() instanceof Extension)) properties.add(property);
        if (!properties.isEmpty() && !context.isShallow()) {
            w.print("<TR><TD><TABLE border=\"1\" cellborder=\"0\" CELLPADDING=\"0\" CELLSPACING=\"5\" ALIGN=\"LEFT\">");
            context.render(properties);
            w.print("</TABLE></TD></TR>");
        }
        if (!element.getOperations().isEmpty() && !context.isShallow()) {
            w.print("<TR><TD><TABLE border=\"1\" cellborder=\"0\" CELLPADDING=\"0\" CELLSPACING=\"5\" align=\"left\">");
            context.render(element.getOperations());
            w.print("</TABLE></TD></TR>");
        }
        w.exitLevel();
        w.println("</TABLE>>];");
        w.enterLevel();
        List<Generalization> generalizations = element.getGeneralizations();
        context.render(generalizations);
        List<InterfaceRealization> realizations = element.getInterfaceRealizations();
        context.render(realizations);
    }
}
