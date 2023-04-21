package net.ar.guia.managers.contributors;

import net.ar.guia.own.adapters.visual.*;
import net.ar.guia.visitor.*;

public interface VisualAdapterContributorVisitor extends Visitor {

    public abstract void visitVisualAdapterContributor(VisualAdapterContributor aContributor);
}
