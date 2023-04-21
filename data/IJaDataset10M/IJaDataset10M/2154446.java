package org.omwg.mediation.language.export.map;

import org.omwg.mediation.language.export.SyntaxFormat;
import org.omwg.mediation.language.objectmodel.api.Annotation;
import org.omwg.mediation.language.objectmodel.api.ComplexExpression;
import org.omwg.mediation.language.objectmodel.api.Expression;
import org.omwg.mediation.language.objectmodel.api.Id;
import org.omwg.mediation.language.objectmodel.api.MappingDoc;
import org.omwg.mediation.language.objectmodel.api.MappingRule;
import org.omwg.mediation.language.objectmodel.impl.AnnotationImpl;
import org.omwg.mediation.language.objectmodel.impl.IRI;
import org.omwg.mediation.parser.alignment.Direction;
import org.omwg.mediation.parser.alignment.ExpressionType;
import org.omwg.mediation.parser.alignment.Namespace;

/**
 * <p>
 * This SyntaxFormat was made to transform a document to the abstract language.
 * </p>
 * <p>
 * To use this SyntaxFormat specify <code>map</code> as syntax when using the
 * Exporter.export method.
 * </p>
 * 
 * @author ricp??t
 */
public class MapSyntaxFormat implements SyntaxFormat {

    /** buffer representing the actual transformed document */
    private StringBuilder export = new StringBuilder();

    /**
	 * <p>
	 * This is only the default constructor which doesn't do anything
	 * </p>
	 */
    public MapSyntaxFormat() {
    }

    public String export(final MappingDoc doc) {
        export.append("MappingDocument( ");
        export.append(export(doc.getId()));
        export.append("\n");
        export.append("\tsource( ");
        export.append(export(doc.getSource()));
        export.append(" )\n");
        export.append("\ttarget( ");
        export.append(export(doc.getTarget()));
        export.append(" )\n\n");
        for (Annotation anno : doc.getAnnotations()) {
            export.append("\t").append(export(anno));
        }
        if (!doc.getType().equals("")) {
            export.append("\t");
            export.append(export(new AnnotationImpl(new IRI(Namespace.OMWG.getShortCut() + ":type"), doc.getType())));
        }
        if (!doc.getLevel().equals("")) {
            export.append("\t");
            export.append(export(new AnnotationImpl(new IRI(Namespace.OMWG.getShortCut() + ":level"), doc.getLevel())));
        }
        export.append("\n");
        for (MappingRule rule : doc.getRules()) {
            export.append("\t").append(export(rule)).append("\n");
        }
        export.append(")\n");
        return export.toString();
    }

    public String export(final IRI id) {
        return "<\"" + id.getIri() + "\">";
    }

    public String export(final Annotation annotation) {
        return "annotation(" + annotation.getId().toString() + " '" + annotation.getValue() + "' )\n";
    }

    public String export(final MappingRule rule) {
        StringBuilder buffer = new StringBuilder();
        Expression source = rule.getSource();
        Expression target = rule.getTarget();
        if (source.getType() == ExpressionType.CLASS) {
            if (target.getType() == ExpressionType.CLASS) {
                buffer.append("classMapping( ");
            } else if (target.getType() == ExpressionType.RELATION) {
                buffer.append("classRelationMapping( ");
            } else if (target.getType() == ExpressionType.ATTRIBUTE) {
                buffer.append("classAttributeMapping( ");
            } else if (target.getType() == ExpressionType.INSTANCE) {
                buffer.append("classInstanceMapping( ");
            }
        } else if (source.getType() == ExpressionType.RELATION) {
            if (target.getType() == ExpressionType.CLASS) {
                buffer.append("relationClassMapping( ");
            } else if (target.getType() == ExpressionType.RELATION) {
                buffer.append("relationMapping( ");
            } else if (target.getType() == ExpressionType.ATTRIBUTE) {
                buffer.append("relationAttributeMapping( ");
            } else if (target.getType() == ExpressionType.INSTANCE) {
                buffer.append("relationInstanceMapping( ");
            }
        } else if (source.getType() == ExpressionType.ATTRIBUTE) {
            if (target.getType() == ExpressionType.CLASS) {
                buffer.append("attributeClassMapping( ");
            } else if (target.getType() == ExpressionType.RELATION) {
                buffer.append("attributeRelationMapping( ");
            } else if (target.getType() == ExpressionType.ATTRIBUTE) {
                buffer.append("attributeMapping( ");
            } else if (target.getType() == ExpressionType.INSTANCE) {
                buffer.append("attributeInstanceMapping( ");
            }
        } else if (source.getType() == ExpressionType.INSTANCE) {
            if (target.getType() == ExpressionType.CLASS) {
                buffer.append("instanceClassMapping( ");
            } else if (target.getType() == ExpressionType.RELATION) {
                buffer.append("instanceRelationMapping( ");
            } else if (target.getType() == ExpressionType.ATTRIBUTE) {
                buffer.append("instanceAttributeMapping( ");
            } else if (target.getType() == ExpressionType.INSTANCE) {
                buffer.append("instanceMapping( ");
            }
        }
        buffer.append("\n");
        for (Annotation anno : rule.getAnnotations()) {
            buffer.append("\t").append(export(anno));
        }
        if (rule.getMeasure() >= 0 && rule.getMeasure() <= 1) {
            buffer.append("\tmeasure(").append(rule.getMeasure()).append(")\n");
        }
        if (!(source.getType() == ExpressionType.INSTANCE && target.getType() == ExpressionType.INSTANCE)) if (rule.getRelation().getOmwgDirection() == Direction.BIDIRECTIONAL) {
            buffer.append("\tbidirectional \n");
        } else if (rule.getRelation().getOmwgDirection() == Direction.UNIDIRECTIONAL) {
            buffer.append("\tunidirectional \n");
        } else {
            throw new IllegalArgumentException("The Direction " + rule.getRelation().getOmwgDirection() + " can not be exported.");
        }
        buffer.append("\t\t");
        buffer.append(export(source)).append("\n\t\t");
        buffer.append(export(target)).append("\n\t)\n");
        return buffer.toString();
    }

    public String export(final ComplexExpression expression) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(expression.getOperator().toString().toLowerCase());
        buffer.append("(");
        for (Expression expr : expression.getSubexpressions()) {
            buffer.append(export(expr)).append(" ");
        }
        buffer.append(")");
        return buffer.toString();
    }

    public String getComposedString() {
        return export.toString();
    }

    public String export(Expression expr) {
        if (expr.isComplexExpression()) {
            return export((ComplexExpression) expr);
        }
        return export(expr.getId());
    }

    public String export(Id id) {
        if (id instanceof IRI) {
            return export((IRI) id);
        } else {
            throw new IllegalArgumentException("Can not export an Id of type: " + id.getClass().getName());
        }
    }
}
