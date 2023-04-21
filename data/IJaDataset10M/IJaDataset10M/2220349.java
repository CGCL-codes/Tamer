package in.co.codedoc.cg.velocity;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.Node;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class ForEachMethodDirective extends CGBlockDirective {

    public ForEachMethodDirective() {
        super("comma separated list of fully qualified annotation class names");
    }

    @Override
    public String getName() {
        return "ForEachMethod";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
        super.init(rs, context, node);
        try {
            annotations = new ArrayList<Class<? extends Annotation>>();
            String[] annotationClassNames = getArgument(context, node, 0).split("[,; ]+");
            for (String annotationClassName : annotationClassNames) {
                annotations.add((Class<? extends Annotation>) Class.forName(annotationClassName));
            }
        } catch (Throwable th) {
            throw new TemplateInitException(th.getMessage(), getName(), getColumn(), getLine());
        }
    }

    @Override
    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
        if ((annotations == null) || (annotations.size() == 0)) {
            RenderOnce(context, writer, node, null);
        } else {
            for (Class<? extends Annotation> annotation : annotations) {
                RenderOnce(context, writer, node, annotation);
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private void RenderOnce(InternalContextAdapter context, Writer writer, Node node, Class<? extends Annotation> annotation) throws IOException {
        if ((context.get("currentClass")) == null || !(context.get("currentClass") instanceof TypeDeclaration)) {
            throw new RuntimeException("[" + getName() + ":" + getLine() + "]:" + "ForEachMethod ban be used only inside of a directive that sets currentClass" + " context variable to a TypeDeclaration (For example, use it inside ForEachClass." + " Or set that context variable yourself)");
        }
        TypeDeclaration td = (TypeDeclaration) context.get("currentClass");
        for (MethodDeclaration md : td.getMethods()) {
            Annotation anno = null;
            if (annotation != null) {
                try {
                    anno = md.getAnnotation(annotation);
                } catch (Throwable th) {
                }
            }
            if (annotation == null || anno != null) {
                if (anno != null) {
                    context.put("currentMethodAnno", anno);
                }
                context.put("currentMethod", md);
                try {
                    node.jjtGetChild(1).render(context, writer);
                } finally {
                    if (anno != null) {
                        context.remove("currentMethodAnno");
                    }
                    context.remove("currentMethod");
                }
            }
        }
    }

    private Collection<Class<? extends Annotation>> annotations;
}
