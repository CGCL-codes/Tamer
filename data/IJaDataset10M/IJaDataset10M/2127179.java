package org.xtext.example.ui.labeling;

import java.util.ListIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;
import org.xtext.example.swrtj.Class;
import org.xtext.example.swrtj.Constructor;
import org.xtext.example.swrtj.Field;
import org.xtext.example.swrtj.FieldDeclaration;
import org.xtext.example.swrtj.FieldName;
import org.xtext.example.swrtj.File;
import org.xtext.example.swrtj.Import;
import org.xtext.example.swrtj.Interface;
import org.xtext.example.swrtj.Method;
import org.xtext.example.swrtj.MethodName;
import org.xtext.example.swrtj.Parameter;
import org.xtext.example.swrtj.Program;
import org.xtext.example.swrtj.ProvidedMethod;
import org.xtext.example.swrtj.Record;
import org.xtext.example.swrtj.RequiredField;
import org.xtext.example.swrtj.RequiredMethod;
import org.xtext.example.swrtj.Trait;
import org.xtext.example.swrtj.Type;
import org.xtext.example.utils.Lookup;

/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
public class SwrtjLabelProvider extends DefaultEObjectLabelProvider {

    private Lookup lookup = new Lookup();

    String image(Class ele) {
        return "class_obj.gif";
    }

    String image(Import ele) {
        return "import.gif";
    }

    String image(Interface ele) {
        return "interface_obj.gif";
    }

    String image(Program ele) {
        return "program.gif";
    }

    String image(File ele) {
        return "file.gif";
    }

    String image(Record ele) {
        return "record_obj.gif";
    }

    String image(Trait ele) {
        return "trait_obj.gif";
    }

    String image(Constructor constructor) {
        return "class_obj.gif";
    }

    String image(Parameter ele) {
        return "field_public_obj.gif";
    }

    String text(Import imp) {
        return imp.getImportURI();
    }

    String text(Constructor constructor) {
        return ((Class) constructor.eContainer()).getName() + "(" + listToText(constructor.getParameterList()) + ")";
    }

    String image(FieldName f) {
        Field field = lookup.getOwnerField(f);
        if (field instanceof FieldDeclaration) return "field_public_obj.gif"; else if (field instanceof RequiredField) return "field_required_obj.png";
        return null;
    }

    String text(FieldName f) {
        return f.getName() + " : " + text(lookup.getType(f));
    }

    String text(Type type) {
        return (type.getInterfaceType() != null ? type.getInterfaceType().getName() : type.getPrimitiveType());
    }

    String image(MethodName m) {
        Method method = lookup.getOwnerMethod(m);
        if (method instanceof ProvidedMethod) return "methdef_obj.gif"; else if (method instanceof RequiredMethod) return "methodrequest_obj.png";
        return null;
    }

    String image(Method m) {
        return image(m.getMethodRef());
    }

    String text(MethodName m) {
        return m.getName() + "(" + listToText((EList<Parameter>) lookup.getParameterList(m)) + ") : " + text(lookup.getReturnType(m));
    }

    String text(Method m) {
        return text(m.getMethodRef());
    }

    String text(Parameter param) {
        return text(param.getType()) + " " + param.getName();
    }

    String listToText(EList<Parameter> list) {
        StringBuffer buffer = new StringBuffer();
        ListIterator<Parameter> it = list.listIterator();
        while (it.hasNext()) {
            buffer.append(text(it.next()));
            if (it.hasNext()) buffer.append(", ");
        }
        return buffer.toString();
    }
}
