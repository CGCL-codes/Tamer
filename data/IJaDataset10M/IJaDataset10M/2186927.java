package com.googlecode.java2objc.objc;

import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.expr.Expression;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import com.google.code.java2objc.code.ObjcExpression;
import com.google.code.java2objc.code.ObjcMethod;
import com.google.code.java2objc.code.ObjcStatementBlock;
import com.google.code.java2objc.code.ObjcType;
import com.google.code.java2objc.converters.ExpressionConverter;
import com.google.common.collect.Lists;

/**
 * Representation of an Objective C Enum. Objective C doesn't have rich object based Enums like
 * Java does, so this type generates an equivalent pattern for enums.
 * 
 * @author Inderjeet Singh
 */
public final class ObjcEnumType extends ObjcType {

    private final List<ObjcEnumEntry> entries;

    public ObjcEnumType(CompilationContext context, String name, Set<ObjcType> imports, List<ObjcEnumEntry> entries, ObjcType containingClass) {
        super(context, name, false, false);
        this.entries = entries;
    }

    protected ObjcEnumType(CompilationContext context, String name) {
        super(context, name, context.getTypeRepo().getNSObject(), null, true);
        this.entries = new LinkedList<ObjcEnumEntry>();
    }

    @Override
    public void init(CompilationContext context, ObjcType baseClass, List<ObjcType> protocols, ObjcType containingClass, List<ObjcMethod> methods, List<ObjcField> fields, List<ObjcStatementBlock> initializers, List<ObjcType> subTypes, String comments) {
        super.init(context, baseClass, protocols, containingClass, methods, fields, initializers, subTypes, comments);
        for (ObjcMethod method : this.methods) {
            method.getParams().add(0, new ObjcMethodParam(this, "e", 0));
        }
    }

    public List<ObjcEnumEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean isProtocol() {
        return false;
    }

    public boolean hasImpl() {
        return true;
    }

    @Override
    public void append(SourceCodeWriter writer) {
        if (writer.isWritingHeaderFile()) {
            appendHeaderBody(writer);
        } else if (hasImpl()) {
            appendImplBody(writer);
        }
    }

    public void appendHeaderBody(SourceCodeWriter writer) {
        if (writer.isWritingHeaderFile()) {
            if (comments != null) {
                writer.appendDocComment(comments);
            }
            writer.newLine().append("typedef enum").append(" {").indent().newLine();
            boolean first = true;
            for (ObjcEnumEntry entry : entries) {
                if (first) {
                    first = false;
                } else {
                    writer.append(',').newLine();
                }
                writer.append(entry.getName());
            }
            writer.unIndent();
            writer.newLine().append("} ").append(name).endStatement();
            if (fields.size() > 0) {
                writer.newLine();
                writer.append("typedef struct {").newLine().indent();
                for (ObjcField field : fields) {
                    field.append(writer);
                }
                writer.unIndent().append("} ").append(name).append("_Fields;").newLine().newLine();
                writer.append("static ").append(name).append("_Fields ").append(name).append("_Data[").append(String.valueOf(entries.size())).append("] = {").newLine().indent();
                first = true;
                for (ObjcEnumEntry entry : entries) {
                    if (first) {
                        first = false;
                    } else {
                        writer.append(", ");
                    }
                    writer.append("{ ");
                    first = true;
                    for (ObjcExpression expr : entry.getArgs()) {
                        if (first) {
                            first = false;
                        } else {
                            writer.append(", ");
                        }
                        writer.append(expr);
                    }
                    writer.append("}").newLine();
                }
                writer.unIndent().append('}').endStatement();
            }
            writer.newLine();
            writer.append(name).append(' ').append(name).append("ValueOf(NSString *text);").newLine();
            writer.append(name).append(' ').append(name).append("Description(").append(name).append(" value);").newLine();
            if (methods.size() > 0) {
                writer.newLine();
                for (ObjcMethod method : methods) {
                    if (!ModifierSet.isPrivate(method.getModifiers())) {
                        appendMethodSignature(writer, method);
                        writer.endStatement();
                    }
                }
            }
            for (ObjcType subType : subTypes) {
                subType.append(writer);
            }
        }
    }

    private void appendImplBody(SourceCodeWriter writer) {
        appendValueOf(writer);
        appendDescription(writer);
        if (methods.size() > 0) {
            for (ObjcMethod method : methods) {
                appendMethodSignature(writer, method);
                writer.append(' ').append(method.getMethodBody());
            }
        }
        for (ObjcType subType : subTypes) {
            subType.append(writer);
        }
    }

    private void appendValueOf(SourceCodeWriter writer) {
        writer.newLine();
        writer.append(name).append(' ').append(name).append("ValueOf(NSString *text) {").newLine();
        writer.indent().append("if (text) {").newLine();
        writer.indent();
        boolean first = true;
        for (ObjcEnumEntry entry : entries) {
            if (first) {
                first = false;
            } else {
                writer.append("else ");
            }
            writer.append("if ([text isEqualToString:@\"").append(entry.getName()).append("\"])").newLine();
            writer.indent().append("return ").append(entry.getName()).endStatement().unIndent();
        }
        writer.unIndent().append('}').newLine();
        writer.append("return -1").endStatement().unIndent();
        writer.append('}').newLine();
    }

    private void appendDescription(SourceCodeWriter writer) {
        writer.newLine();
        writer.append(name).append(' ').append(name).append("Description(").append(name).append(" value) {").newLine();
        writer.indent().append("switch (value) {").newLine();
        writer.indent();
        boolean first = true;
        for (ObjcEnumEntry entry : entries) {
            if (first) {
                first = false;
            } else {
                writer.append("else ");
            }
            writer.append("case ").append(entry.getName()).append(':').newLine();
            writer.indent().append("return ").append("@\"").append(entry.getName()).append('"').endStatement().unIndent();
        }
        writer.unIndent().append('}').newLine();
        writer.append("return nil").endStatement().unIndent();
        writer.append('}').newLine();
    }

    private void appendMethodSignature(SourceCodeWriter writer, ObjcMethod method) {
        writer.append(method.getReturnType().getPointerTypeName()).append(' ');
        writer.append(name).append('_').append(method.getName()).append('(');
        if (method.getParams() != null && method.getParams().size() > 0) {
            boolean first = true;
            for (ObjcMethodParam param : method.getParams()) {
                if (first) {
                    first = false;
                } else {
                    writer.append(", ");
                }
                writer.append(param.getType().getPointerTypeName()).append(' ').append(param.getName());
                for (int i = 0; i < param.getArrayCount(); i++) {
                    writer.append("*");
                }
            }
        }
        writer.append(')');
    }

    public static class ObjcEnumEntry {

        private final String name;

        private final List<ObjcExpression> args;

        public ObjcEnumEntry(CompilationContext context, EnumConstantDeclaration entry) {
            this.name = entry.getName();
            this.args = Lists.newArrayList();
            if (entry.getArgs() != null) {
                ExpressionConverter converter = context.getExpressionConverter();
                for (Expression expression : entry.getArgs()) {
                    this.args.add(converter.to(expression));
                }
            }
        }

        public ObjcEnumEntry(String name, List<ObjcExpression> args) {
            this.name = name;
            this.args = args;
        }

        public String getName() {
            return name;
        }

        public List<ObjcExpression> getArgs() {
            return args;
        }
    }
}
