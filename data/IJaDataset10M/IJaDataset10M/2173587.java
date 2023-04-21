package pl.wcislo.sbql4j.tools.apt.mirror.declaration;

import java.util.LinkedHashMap;
import java.util.Map;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationMirror;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationTypeElementDeclaration;
import pl.wcislo.sbql4j.mirror.declaration.AnnotationValue;
import pl.wcislo.sbql4j.mirror.declaration.Declaration;
import pl.wcislo.sbql4j.mirror.type.AnnotationType;
import pl.wcislo.sbql4j.mirror.util.SourcePosition;
import pl.wcislo.sbql4j.tools.apt.mirror.AptEnv;
import pl.wcislo.sbql4j.tools.javac.code.Attribute;
import pl.wcislo.sbql4j.tools.javac.code.Symbol.MethodSymbol;
import pl.wcislo.sbql4j.tools.javac.util.Name;
import pl.wcislo.sbql4j.tools.javac.util.Pair;

/**
 * Implementation of AnnotationMirror
 */
public class AnnotationMirrorImpl implements AnnotationMirror {

    protected final AptEnv env;

    protected final Attribute.Compound anno;

    protected final Declaration decl;

    AnnotationMirrorImpl(AptEnv env, Attribute.Compound anno, Declaration decl) {
        this.env = env;
        this.anno = anno;
        this.decl = decl;
    }

    /**
     * Returns a string representation of this annotation.
     * String is of one of the forms:
     *     @com.example.foo(name1=val1, name2=val2)
     *     @com.example.foo(val)
     *     @com.example.foo
     * Omit parens for marker annotations, and omit "value=" when allowed.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder("@");
        Constants.Formatter fmtr = Constants.getFormatter(sb);
        fmtr.append(anno.type.tsym);
        int len = anno.values.length();
        if (len > 0) {
            sb.append('(');
            boolean first = true;
            for (Pair<MethodSymbol, Attribute> val : anno.values) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                Name name = val.fst.name;
                if (len > 1 || name != env.names.value) {
                    fmtr.append(name);
                    sb.append('=');
                }
                sb.append(new AnnotationValueImpl(env, val.snd, this));
            }
            sb.append(')');
        }
        return fmtr.toString();
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationType getAnnotationType() {
        return (AnnotationType) env.typeMaker.getType(anno.type);
    }

    /**
     * {@inheritDoc}
     */
    public Map<AnnotationTypeElementDeclaration, AnnotationValue> getElementValues() {
        Map<AnnotationTypeElementDeclaration, AnnotationValue> res = new LinkedHashMap<AnnotationTypeElementDeclaration, AnnotationValue>();
        for (Pair<MethodSymbol, Attribute> val : anno.values) {
            res.put(getElement(val.fst), new AnnotationValueImpl(env, val.snd, this));
        }
        return res;
    }

    public SourcePosition getPosition() {
        return (decl == null) ? null : decl.getPosition();
    }

    public Declaration getDeclaration() {
        return this.decl;
    }

    /**
     * Returns the annotation type element for a symbol.
     */
    private AnnotationTypeElementDeclaration getElement(MethodSymbol m) {
        return (AnnotationTypeElementDeclaration) env.declMaker.getExecutableDeclaration(m);
    }
}
