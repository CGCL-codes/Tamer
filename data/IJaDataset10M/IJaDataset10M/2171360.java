package com.abstratt.graphviz.uml.classdiagram;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import com.abstratt.modelviewer.IndentedPrintWriter;
import com.abstratt.modelviewer.render.IRenderingSession;

public class ClassRenderer implements IEObjectRenderer<Class> {

    public void renderObject(Class element, IndentedPrintWriter w, IRenderingSession<EObject> context) {
        w.println("// class " + element.getQualifiedName());
        w.print('"' + element.getName() + "\" [");
        w.println("label=<");
        w.enterLevel();
        w.println("<TABLE border=\"0\" cellspacing=\"0\" cellpadding=\"0\" cellborder=\"0\" port=\"port\">");
        w.print("<TR><TD><TABLE border=\"1\" cellborder=\"0\" CELLPADDING=\"3\" CELLSPACING=\"0\" ALIGN=\"LEFT\">");
        if (!element.getAppliedStereotypes().isEmpty()) {
            w.print("<TR><TD>");
            StringBuffer stereotypeList = new StringBuffer();
            for (Stereotype current : element.getAppliedStereotypes()) {
                stereotypeList.append(current.getName());
                stereotypeList.append(", ");
            }
            stereotypeList.delete(stereotypeList.length() - 2, stereotypeList.length());
            w.print(UMLRenderingUtils.addGuillemots(stereotypeList.toString()));
            w.print("</TD></TR>");
        }
        w.print("<TR><TD>");
        w.print(element.getName());
        w.print("</TD></TR>");
        if (context.isShallow()) {
            w.print("<TR><TD>");
            final Package nearestPackage = element.getNearestPackage();
            final String packageName = nearestPackage == null ? "?" : nearestPackage.getQualifiedName();
            w.print("(from " + packageName + ")");
            w.print("</TD></TR>");
        }
        w.exitLevel();
        w.print("</TABLE></TD></TR>");
        if (!element.getAttributes().isEmpty() && !context.isShallow()) {
            w.print("<TR><TD><TABLE border=\"1\" cellborder=\"0\" CELLPADDING=\"0\" CELLSPACING=\"5\" ALIGN=\"LEFT\">");
            context.render(element.getAttributes());
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
