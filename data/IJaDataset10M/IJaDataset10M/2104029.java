package com.google.devtools.depan.java.editors;

import com.google.devtools.depan.eclipse.plugins.ElementClassTransformer;
import com.google.devtools.depan.eclipse.utils.ElementEditor;
import com.google.devtools.depan.java.graph.FieldElement;
import com.google.devtools.depan.java.graph.InterfaceElement;
import com.google.devtools.depan.java.graph.MethodElement;
import com.google.devtools.depan.java.graph.PackageElement;
import com.google.devtools.depan.java.graph.TypeElement;
import com.google.devtools.depan.java.integration.JavaElementDispatcher;
import com.google.devtools.depan.model.Element;

/**
 * An {@link JavaElementDispatcher} returning the class of the corresponding editor.
 *
 * @author ycoppel@google.com (Yohann Coppel)
 *
 */
public final class ElementEditors extends JavaElementDispatcher<Class<? extends ElementEditor>> implements ElementClassTransformer<Class<? extends ElementEditor>> {

    /**
   * Unique instance.
   */
    private static ElementEditors instance = new ElementEditors();

    /**
   * This is a singleton: private constructor to prevent instantiation.
   */
    private ElementEditors() {
    }

    public static ElementEditors getInstance() {
        return instance;
    }

    /**
   * Match the given {@link Element} against its class, and return the Class
   * of the corresponding {@link ElementEditor}.
   * @param e the Element we want to edit.
   * @return the class for the corresponding ElementEditor.
   */
    public static Class<? extends ElementEditor> matchElement(Element e) {
        return ((JavaElementDispatcher<Class<? extends ElementEditor>>) instance).match(e);
    }

    @Override
    public Class<? extends ElementEditor> match(TypeElement e) {
        return TypeEditor.class;
    }

    @Override
    public Class<? extends ElementEditor> match(MethodElement e) {
        return MethodEditor.class;
    }

    @Override
    public Class<? extends ElementEditor> match(FieldElement e) {
        return FieldEditor.class;
    }

    @Override
    public Class<? extends ElementEditor> match(InterfaceElement e) {
        return InterfaceEditor.class;
    }

    @Override
    public Class<? extends ElementEditor> match(PackageElement e) {
        return PackageEditor.class;
    }

    @Override
    public Class<? extends ElementEditor> transform(Class<? extends Element> element) {
        if (TypeElement.class.isAssignableFrom(element)) {
            return TypeEditor.class;
        }
        if (MethodElement.class.isAssignableFrom(element)) {
            return MethodEditor.class;
        }
        if (FieldElement.class.isAssignableFrom(element)) {
            return FieldEditor.class;
        }
        if (InterfaceElement.class.isAssignableFrom(element)) {
            return InterfaceEditor.class;
        }
        if (PackageElement.class.isAssignableFrom(element)) {
            return PackageEditor.class;
        }
        return null;
    }
}
