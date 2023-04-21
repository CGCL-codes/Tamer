package com.google.devtools.depan.java.eclipse;

import com.google.devtools.depan.eclipse.plugins.ElementTransformer;
import com.google.devtools.depan.java.JavaResources;
import com.google.devtools.depan.java.graph.FieldElement;
import com.google.devtools.depan.java.graph.InterfaceElement;
import com.google.devtools.depan.java.graph.MethodElement;
import com.google.devtools.depan.java.graph.PackageElement;
import com.google.devtools.depan.java.graph.TypeElement;
import com.google.devtools.depan.java.integration.JavaElementDispatcher;
import com.google.devtools.depan.model.Element;
import org.eclipse.jface.resource.ImageDescriptor;

public class NodeIconTransformer extends JavaElementDispatcher<ImageDescriptor> implements ElementTransformer<ImageDescriptor> {

    private static final NodeIconTransformer instance = new NodeIconTransformer();

    public static NodeIconTransformer getInstance() {
        return instance;
    }

    private NodeIconTransformer() {
    }

    @Override
    public ImageDescriptor match(TypeElement e) {
        return JavaResources.IMAGE_DESC_TYPE;
    }

    @Override
    public ImageDescriptor match(MethodElement e) {
        return JavaResources.IMAGE_DESC_METHOD;
    }

    @Override
    public ImageDescriptor match(FieldElement e) {
        return JavaResources.IMAGE_DESC_FIELD;
    }

    @Override
    public ImageDescriptor match(InterfaceElement e) {
        return JavaResources.IMAGE_DESC_INTERFACE;
    }

    @Override
    public ImageDescriptor match(PackageElement e) {
        return JavaResources.IMAGE_DESC_PACKAGE;
    }

    @Override
    public ImageDescriptor transform(Element element) {
        return this.match(element);
    }
}
