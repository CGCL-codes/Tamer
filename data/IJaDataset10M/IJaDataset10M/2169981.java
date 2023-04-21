package net.ar.webonswing.swing.layouts;

import java.awt.*;
import net.ar.guia.own.interfaces.*;
import net.ar.guia.own.layouters.*;
import net.ar.guia.plugins.swing.*;
import net.ar.guia.render.templates.*;
import net.ar.guia.visitor.*;

public class PropagateTemplateLayoutByName implements LayoutManager2, TemplateLayouter {

    protected PropagateTemplateLayouterByName propagateTemplateLayouterByName;

    public PropagateTemplateLayoutByName(Template aTemplate) {
        this(aTemplate, true);
    }

    public PropagateTemplateLayoutByName(Template aTemplate, boolean isCompleted) {
        propagateTemplateLayouterByName = new PropagateTemplateLayouterByName(aTemplate, isCompleted);
    }

    public PropagateTemplateLayoutByName(Template aTemplate, LayoutManager anOriginalLayout, boolean isCompleted) {
        this(aTemplate, isCompleted);
    }

    public float getLayoutAlignmentX(Container aTarget) {
        return 0;
    }

    public float getLayoutAlignmentY(Container aTarget) {
        return 0;
    }

    public void invalidateLayout(Container aTarget) {
    }

    public Dimension maximumLayoutSize(Container aTarget) {
        return new Dimension(640, 480);
    }

    public void addLayoutComponent(Component aComponent, Object aConstraints) {
    }

    public void accept(Visitor aVisitor) {
        propagateTemplateLayouterByName.accept(aVisitor);
    }

    public void addConstraintedChild(VisualComponent aComponent, Object aConstraint) {
        propagateTemplateLayouterByName.addConstraintedChild(aComponent, aConstraint);
    }

    public void doLayout() {
        propagateTemplateLayouterByName.doLayout();
    }

    public boolean equals(Object obj) {
        return propagateTemplateLayouterByName.equals(obj);
    }

    public Template getClonedTemplate() {
        return propagateTemplateLayouterByName.getClonedTemplate();
    }

    public int hashCode() {
        return propagateTemplateLayouterByName.hashCode();
    }

    public void setComponentToLayout(VisualComponent aComponent) {
        propagateTemplateLayouterByName.setComponentToLayout(aComponent);
    }

    public void setConstraint(VisualComponent comp, Object aConstraint) {
        propagateTemplateLayouterByName.setConstraint(comp, aConstraint);
    }

    public void setTemplate(Template aTemplate) {
        propagateTemplateLayouterByName.setTemplate(aTemplate);
    }

    public String toString() {
        return propagateTemplateLayouterByName.toString();
    }

    public void removeLayoutComponent(Component comp) {
    }

    public void layoutContainer(Container parent) {
        setComponentToLayout(GuiaToSwingPlugin.getWrapperFor(parent));
        doLayout();
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(1000, 1000);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(1000, 1000);
    }

    public Template getTemplate() {
        return propagateTemplateLayouterByName.getTemplate();
    }

    public void removeConstraint(Object aConstraint) {
        propagateTemplateLayouterByName.removeConstraint(aConstraint);
    }

    public void removeConstraintedChild(VisualComponent aComponent) {
        propagateTemplateLayouterByName.removeConstraintedChild(aComponent);
    }
}
