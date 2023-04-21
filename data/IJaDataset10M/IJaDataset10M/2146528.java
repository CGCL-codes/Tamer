package sourceforge.xmlschematograp;

import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import org.apache.log4j.Logger;
import org.xml.sax.*;
import sourceforge.xmlschematograp.Main.*;
import com.sun.xml.xsom.*;
import com.sun.xml.xsom.impl.ParticleImpl;
import com.sun.xml.xsom.parser.*;

public class Grapher {

    private static final Logger LOG = Logger.getLogger(Grapher.class);

    Set<XSComponent> notProcessed = new HashSet<XSComponent>();

    Set<XSComponent> processed = new HashSet<XSComponent>();

    Set<Arrow> arrows = new HashSet<Arrow>();

    Set<Node> nodes = new HashSet<Node>() {

        public boolean add(Node n) {
            if (n instanceof XSParticle) {
                throw new IllegalStateException("we shouldn't be doing particles");
            }
            return super.add(n);
        }
    };

    Set<String> ignoredNamespaces = new HashSet<String>();

    {
        ignoredNamespaces.add("http://www.w3.org/2001/XMLSchema");
        ignoredNamespaces.add("http://www.w3.org/XML/1998/namespace");
    }

    void mustDo(XSComponent toDo) {
        if (toDo == null) return;
        if (toDo instanceof XSParticle) {
            throw new IllegalStateException("we shouldn't be doing particles: " + toDo.getClass());
        }
        if (!processed.contains(toDo)) {
            notProcessed.add(toDo);
        }
    }

    void mustDo(Collection<? extends XSComponent> toDo) {
        if (toDo == null) return;
        for (XSComponent o : toDo) {
            mustDo(o);
        }
    }

    public void output(XSSchemaSet set, PrintWriter out) throws Exception {
        mustDo(set.getSchemas());
        while (!notProcessed.isEmpty()) {
            XSComponent o = notProcessed.iterator().next();
            Node n = new Node(o);
            nodes.add(n);
            for (Processor<?> p : getProcessors(o)) {
                p.process(o, n);
            }
            notProcessed.remove(o);
            processed.add(o);
        }
        out.println("digraph {");
        for (XSSchema s : set.getSchemas()) {
            if (s.getTargetNamespace().endsWith("XMLSchema")) {
                continue;
            }
            out.println("subgraph cluster" + Node.n(s) + " { label=\"" + s.getTargetNamespace() + "\"");
            for (Iterator<Node> i = nodes.iterator(); i.hasNext(); ) {
                Node n = i.next();
                if (!s.equals(n.n.getOwnerSchema())) {
                    continue;
                }
                i.remove();
                out.println(n.getDot());
            }
            out.println("}");
        }
        for (Iterator<Node> i = nodes.iterator(); i.hasNext(); ) {
            Node n = i.next();
            i.remove();
            if (isIgnoredNamespace(n.n)) {
                continue;
            }
            out.println(n.getDot());
        }
        for (Arrow a : arrows) {
            if (isIgnoredNamespace(a.from)) {
                continue;
            }
            if (isIgnoredNamespace(a.to)) {
                continue;
            }
            out.println(a.getDot());
        }
        out.println("}");
    }

    boolean isIgnoredNamespace(XSComponent o) {
        return o.getOwnerSchema() == null || ignoredNamespaces.contains(o.getOwnerSchema().getTargetNamespace());
    }

    private Arrow arrow(XSComponent a, XSComponent b, String head) {
        if (a == null || b == null) return null;
        Arrow arr = new Arrow(a, b);
        arr.head = head;
        arrows.add(arr);
        mustDo(a);
        mustDo(b);
        return arr;
    }

    private Arrow arrow(XSComponent a, XSComponent b, String head, String label) {
        if (a == null || b == null) return null;
        Arrow arr = new Arrow(a, b);
        arr.head = head;
        arr.label = label;
        arrows.add(arr);
        mustDo(a);
        mustDo(b);
        return arr;
    }

    private Arrow arrow(XSComponent a, XSComponent b, String head, String tail, String label) {
        if (a == null || b == null) return null;
        Arrow arr = new Arrow(a, b);
        arr.head = head;
        arr.tail = tail;
        arr.label = label;
        arrows.add(arr);
        mustDo(a);
        mustDo(b);
        return arr;
    }

    Map<Class<? extends XSComponent>, Processor<? extends XSComponent>> processors = new HashMap<Class<? extends XSComponent>, Processor<? extends XSComponent>>();

    {
        new ProcessComponent();
        new ProcessContentType();
        new ProcessSchema();
        new ProcessDeclaration();
        new ProcessType();
        new ProcessSimpleType();
        new ProcessListSimpleType();
        new ProcessUnionSimpleType();
        new ProcessRestrictionSimpleType();
        new ProcessAttributeDecl();
        new ProcessAttGroupDecl();
        new ProcessAttContainer();
        new ProcessAttributeUse();
        new ProcessComplexType();
        new ProcessParticle();
        new ProcessTerm();
        new ProcessModelGroup();
        new ProcessModelGroupDecl();
        new ProcessElementDecl();
        new ProcessIdentityConstraint();
        new ProcessXPath();
        new ProcessWildcard();
        new ProcessWildcardAny();
        new ProcessWildcardOther();
        new ProcessWildcardUnion();
    }

    abstract class Processor<T extends XSComponent> {

        final Class<T> clazz;

        Processor(Class<T> clazz) {
            this.clazz = clazz;
            processors.put(clazz, this);
        }

        final void process(XSComponent o, Node n) {
            if (!clazz.isAssignableFrom(o.getClass())) {
                throw new IllegalArgumentException(o + " is not a " + clazz);
            }
            processInternam((T) o, n);
        }

        abstract void processInternam(T o, Node n);
    }

    class ProcessComponent extends Processor<XSComponent> {

        ProcessComponent() {
            super(XSComponent.class);
        }

        void processInternam(XSComponent o, Node n) {
            if (o.getAnnotation() != null && o.getAnnotation().getAnnotation() != null) {
                String annot = (String) o.getAnnotation().getAnnotation();
                if (annot.length() > 255) annot = annot.substring(0, 255);
                if (annot.indexOf('.') != -1) {
                    annot = annot.substring(0, annot.indexOf('.') + 1);
                }
                annot = annot.trim();
                if (annot.length() > 0) {
                    StringBuffer sb = new StringBuffer();
                    int linelen = (int) Math.sqrt(annot.length()) * 2;
                    for (; ; ) {
                        int space = annot.indexOf(' ', linelen);
                        int ret = annot.indexOf('\n');
                        if (ret != -1 && (ret < space || space == -1)) {
                            sb.append(annot.substring(0, ret + 1));
                            annot = annot.substring(ret + 1).trim();
                        } else {
                            if (space == -1) {
                                sb.append(annot);
                                break;
                            } else {
                                sb.append(annot.substring(0, space));
                                sb.append('\n');
                                annot = annot.substring(space + 1).trim();
                            }
                        }
                    }
                    if (n.contents.size() < 1 || !(n.contents.get(0) instanceof String)) {
                        n.contents.add(0, sb.toString());
                    } else {
                        n.contents.add(1, sb.toString());
                    }
                }
            }
        }
    }

    class ProcessContentType extends Processor<XSContentType> {

        ProcessContentType() {
            super(XSContentType.class);
        }

        void processInternam(XSContentType o, Node n) {
        }
    }

    class ProcessSchema extends Processor<XSSchema> {

        ProcessSchema() {
            super(XSSchema.class);
        }

        void processInternam(XSSchema o, Node n) {
            n.contents.add(0, o.getTargetNamespace());
            mustDo(o.getAttGroupDecls().values());
            mustDo(o.getAttributeDecls().values());
            mustDo(o.getComplexTypes().values());
            mustDo(o.getElementDecls().values());
            mustDo(o.getIdentityConstraints().values());
            mustDo(o.getModelGroupDecls().values());
            mustDo(o.getNotations().values());
            mustDo(o.getSimpleTypes().values());
            mustDo(o.getTypes().values());
        }
    }

    class ProcessDeclaration extends Processor<XSDeclaration> {

        ProcessDeclaration() {
            super(XSDeclaration.class);
        }

        void processInternam(XSDeclaration o, Node n) {
            n.contents.add(0, o.getName() == null ? "(anonymous)" : o.getName());
        }
    }

    class ProcessType extends Processor<XSType> {

        ProcessType() {
            super(XSType.class);
        }

        @Override
        void processInternam(XSType o, Node n) {
            Arrow base = arrow(o, o.getBaseType(), "onormal");
            arrow(o, o.getRedefinedBy(), "vee", "redefinition");
            switch(o.getDerivationMethod()) {
                case XSType.EXTENSION:
                    base.label = "extends";
                    break;
                case XSType.RESTRICTION:
                    base.label = "restricts";
                    break;
                case XSType.SUBSTITUTION:
                    base.label = "subsitutes";
                    break;
                default:
                    base.label = "derivation " + o.getDerivationMethod();
                    break;
            }
        }
    }

    class ProcessAttributeDecl extends Processor<XSAttributeDecl> {

        ProcessAttributeDecl() {
            super(XSAttributeDecl.class);
        }

        @Override
        void processInternam(XSAttributeDecl o, Node n) {
            if (isIgnoredNamespace(o.getType())) {
                n.contents.add(new Object[] { "Type", o.getType().getName() });
            } else {
                arrow(o, o.getType(), "normal");
            }
            if (o.getDefaultValue() != null) {
                n.contents.add(new Object[] { "Default", o.getDefaultValue() });
            }
            if (o.getFixedValue() != null) {
                n.contents.add(new Object[] { "Fixed", o.getFixedValue() });
            }
        }
    }

    class ProcessElementDecl extends Processor<XSElementDecl> {

        ProcessElementDecl() {
            super(XSElementDecl.class);
        }

        @Override
        void processInternam(XSElementDecl o, Node n) {
            n.color = "blue";
            if (isIgnoredNamespace(o.getType())) {
                n.contents.add(new Object[] { "Type", o.getType().getName() });
            } else {
                arrow(o, o.getType(), "normal");
            }
            if (o.getDefaultValue() != null) {
                n.contents.add(new Object[] { "DEFAULT", o.getDefaultValue() });
            }
            if (o.getFixedValue() != null) {
                n.contents.add(new Object[] { "FIXED", o.getFixedValue() });
            }
            for (XSIdentityConstraint c : o.getIdentityConstraints()) {
                arrow(o, c, "none", "none", "constraint");
            }
            arrow(o, o.getSubstAffiliation(), "vee", "subst");
            for (XSElementDecl e : o.getSubstitutables()) {
                if (!e.equals(o)) {
                    arrow(o, e, "vee", "substitutable");
                }
            }
        }
    }

    class ProcessAttGroupDecl extends Processor<XSAttGroupDecl> {

        ProcessAttGroupDecl() {
            super(XSAttGroupDecl.class);
        }

        @Override
        void processInternam(XSAttGroupDecl o, Node n) {
        }
    }

    class ProcessIdentityConstraint extends Processor<XSIdentityConstraint> {

        ProcessIdentityConstraint() {
            super(XSIdentityConstraint.class);
        }

        @Override
        void processInternam(XSIdentityConstraint o, Node n) {
            n.contents.add(0, o.getName());
            switch(o.getCategory()) {
                case XSIdentityConstraint.KEY:
                    n.contents.add(new Object[] { "Category", "KEY" });
                    break;
                case XSIdentityConstraint.KEYREF:
                    n.contents.add(new Object[] { "Category", "KEYREF" });
                    arrow(o, o.getReferencedKey(), "vee", "referencedKey");
                    break;
                case XSIdentityConstraint.UNIQUE:
                    n.contents.add(new Object[] { "Category", "UNIQUE" });
                    break;
                default:
                    n.contents.add(new Object[] { "Category", o.getCategory() });
                    break;
            }
            n.contents.add(new Object[] { "Selector", o.getSelector().getXPath().value });
            List fields = new ArrayList();
            for (XSXPath p : o.getFields()) {
                fields.add(p.getXPath().value);
            }
            n.contents.add(new Object[] { "Fields", fields });
        }
    }

    class ProcessAttContainer extends Processor<XSAttContainer> {

        ProcessAttContainer() {
            super(XSAttContainer.class);
        }

        @Override
        void processInternam(XSAttContainer o, Node n) {
            List uses = new ArrayList();
            for (XSAttributeUse u : o.getAttributeUses()) {
                List stuff = new ArrayList();
                uses.add(stuff);
                String name = "" + Node.p(u) + "" + u.getDecl().getName();
                if (u.getDecl().getType().getName() != null) {
                    name = name + ": " + u.getDecl().getType().getName();
                }
                stuff.add(name);
                if (!isIgnoredNamespace(u.getDecl().getType())) {
                    Arrow arrow = arrow(o, u.getDecl().getType(), null);
                    arrow.tailLabel = u.getDecl().getName();
                    arrow.fromPort = u;
                    if (u.isRequired()) arrow.head = "vee"; else arrow.head = "ovee";
                }
                if (u.getDefaultValue() != null) {
                    stuff.add("DEFAULT :" + u.getDefaultValue());
                }
                if (u.getFixedValue() != null) {
                    stuff.add("FIXED :" + u.getFixedValue());
                }
                if (u.getDecl().getDefaultValue() != null) {
                    stuff.add("DEFAULT :" + u.getDecl().getDefaultValue());
                }
                if (u.getDecl().getFixedValue() != null) {
                    stuff.add("FIXED :" + u.getDecl().getFixedValue());
                }
            }
            if (!uses.isEmpty()) {
                n.contents.add(new Object[] { "Attrs", uses });
            }
            for (XSAttGroupDecl u : o.getAttGroups()) {
                arrow(o, u, "vee");
            }
        }
    }

    class ProcessAttributeUse extends Processor<XSAttributeUse> {

        ProcessAttributeUse() {
            super(XSAttributeUse.class);
        }

        @Override
        void processInternam(XSAttributeUse o, Node n) {
            arrow(o, o.getDecl(), "vee");
            if (o.getFixedValue() != null) {
                n.contents.add(new Object[] { "FIXED", o.getFixedValue() });
            }
            if (o.getDefaultValue() != null) {
                n.contents.add(new Object[] { "DEFAULT", o.getDefaultValue() });
            }
        }
    }

    class ProcessSimpleType extends Processor<XSSimpleType> {

        ProcessSimpleType() {
            super(XSSimpleType.class);
        }

        @Override
        void processInternam(XSSimpleType o, Node n) {
            n.color = "darkgreen";
            n.contents.add(new Object[] { "Variety", o.getVariety() });
        }
    }

    class ProcessXPath extends Processor<XSXPath> {

        ProcessXPath() {
            super(XSXPath.class);
        }

        @Override
        void processInternam(XSXPath o, Node n) {
            n.contents.add(o.getXPath());
        }
    }

    class ProcessListSimpleType extends Processor<XSListSimpleType> {

        ProcessListSimpleType() {
            super(XSListSimpleType.class);
        }

        @Override
        void processInternam(XSListSimpleType o, Node n) {
            arrow(o, o.getItemType(), "vee", "ItemType");
        }
    }

    class ProcessUnionSimpleType extends Processor<XSUnionSimpleType> {

        ProcessUnionSimpleType() {
            super(XSUnionSimpleType.class);
        }

        @Override
        void processInternam(XSUnionSimpleType o, Node n) {
            for (int i = 0; i < o.getMemberSize(); i++) {
                arrow(o, o.getMember(i), "vee", "union");
            }
        }
    }

    class ProcessRestrictionSimpleType extends Processor<XSRestrictionSimpleType> {

        ProcessRestrictionSimpleType() {
            super(XSRestrictionSimpleType.class);
        }

        @Override
        void processInternam(XSRestrictionSimpleType o, Node n) {
            List ll = new ArrayList();
            List enumeration = new ArrayList();
            for (XSFacet f : o.getDeclaredFacets()) {
                if (f.getName().equals("enumeration")) {
                    enumeration.add(f.getValue().value + (f.isFixed() ? " (FIXED)" : ""));
                } else {
                    ll.add(new Object[] { f.getName(), f.getValue().value + (f.isFixed() ? " (FIXED)" : "") });
                }
            }
            if (!ll.isEmpty()) {
                n.contents.add(new Object[] { "Facets", ll });
            }
            if (!enumeration.isEmpty()) {
                int cols = (int) Math.sqrt(enumeration.size() / 4.0) + 1;
                Object[] ee = new Object[cols + 1];
                ee[0] = "Enum";
                for (int i = 0; i < cols; i++) {
                    ee[i + 1] = new ArrayList();
                }
                for (int i = 0; i < enumeration.size(); i++) {
                    ((List) ee[(i % cols) + 1]).add(enumeration.get(i));
                }
                for (int i = enumeration.size(); i % cols != 0; i++) {
                    ((List) ee[(i % cols) + 1]).add("");
                }
                n.contents.add(ee);
            }
        }
    }

    class ProcessComplexType extends Processor<XSComplexType> {

        ProcessComplexType() {
            super(XSComplexType.class);
        }

        @Override
        void processInternam(XSComplexType o, Node n) {
            n.color = "red";
            if (o.getContentType() instanceof ParticleImpl) {
                doParticleArrow(o, (XSParticle) o.getContentType());
            } else {
                arrow(o, o.getContentType(), "vee", "content");
            }
            if (o.getExplicitContent() instanceof ParticleImpl) {
                doParticleArrow(o, (XSParticle) o.getExplicitContent());
            } else {
                arrow(o, o.getExplicitContent(), "vee", "explicit");
            }
            arrow(o, o.getRedefinedBy(), "vee", "redefinedBy");
            mustDo(o.getScope());
        }
    }

    class ProcessParticle extends Processor<XSParticle> {

        ProcessParticle() {
            super(XSParticle.class);
        }

        @Override
        void processInternam(XSParticle o, Node n) {
            throw new IllegalStateException("we shouldn't be doing particles " + o.getClass());
        }
    }

    class ProcessModelGroupDecl extends Processor<XSModelGroupDecl> {

        ProcessModelGroupDecl() {
            super(XSModelGroupDecl.class);
        }

        @Override
        void processInternam(XSModelGroupDecl o, Node n) {
            arrow(o, o.getModelGroup(), "vee");
        }
    }

    class ProcessTerm extends Processor<XSTerm> {

        ProcessTerm() {
            super(XSTerm.class);
        }

        @Override
        void processInternam(XSTerm o, Node n) {
        }
    }

    class ProcessModelGroup extends Processor<XSModelGroup> {

        ProcessModelGroup() {
            super(XSModelGroup.class);
        }

        @Override
        void processInternam(XSModelGroup o, Node n) {
            n.contents.add(o.getCompositor() + " of");
            for (int i = 0; i < o.getSize(); i++) {
                doParticleArrow(o, o.getChild(i));
            }
        }
    }

    void doParticleArrow(XSComponent o, XSParticle p) {
        if (o == null || p == null) return;
        Arrow arr = arrow(o, p.getTerm(), "", "diamond", null);
        if (p.getMinOccurs() == 0) {
            arr.head = "odot";
        }
        if (p.getMinOccurs() <= 1 && (p.getMaxOccurs() >= 1 || p.getMaxOccurs() == -1)) {
            arr.head = "tee" + arr.head;
        }
        if (p.getMaxOccurs() > 1 || p.getMaxOccurs() == -1) {
            arr.head = "crow" + arr.head;
        }
        if (p.getMinOccurs() > 1 || p.getMaxOccurs() > 1) {
            arr.headLabel = p.getMinOccurs() + ".." + (p.getMaxOccurs() == -1 ? "*" : p.getMaxOccurs());
        }
    }

    class ProcessWildcard extends Processor<XSWildcard> {

        ProcessWildcard() {
            super(XSWildcard.class);
        }

        @Override
        void processInternam(XSWildcard o, Node n) {
            switch(o.getMode()) {
                case XSWildcard.LAX:
                    n.contents.add(new Object[] { "Wildcard", "LAX" });
                    break;
                case XSWildcard.SKIP:
                    n.contents.add(new Object[] { "Wildcard", "SKIP" });
                    break;
                case XSWildcard.STRTICT:
                    n.contents.add(new Object[] { "Wildcard", "STRICT" });
                    break;
                default:
                    n.contents.add(new Object[] { "Wildcard", o.getMode() });
                    break;
            }
        }
    }

    class ProcessWildcardAny extends Processor<XSWildcard.Any> {

        ProcessWildcardAny() {
            super(XSWildcard.Any.class);
        }

        @Override
        void processInternam(XSWildcard.Any o, Node n) {
        }
    }

    class ProcessWildcardOther extends Processor<XSWildcard.Other> {

        ProcessWildcardOther() {
            super(XSWildcard.Other.class);
        }

        @Override
        void processInternam(XSWildcard.Other o, Node n) {
            n.contents.add(new Object[] { "Wildcard NS", o.getOtherNamespace() });
        }
    }

    class ProcessWildcardUnion extends Processor<XSWildcard.Union> {

        ProcessWildcardUnion() {
            super(XSWildcard.Union.class);
        }

        @Override
        void processInternam(XSWildcard.Union o, Node n) {
            n.contents.add(new Object[] { "Wildcard NS Union", o.getNamespaces() });
        }
    }

    Map<Class<? extends XSComponent>, Collection<Processor<?>>> processorsForClass = new HashMap<Class<? extends XSComponent>, Collection<Processor<?>>>();

    @SuppressWarnings("cast")
    <T extends XSComponent> Collection<Processor<? super T>> getProcessors(T object) {
        if (!processorsForClass.containsKey(object.getClass())) {
            Set<Class<? super T>> todo = new HashSet<Class<? super T>>();
            Set<Class<? super T>> done = new HashSet<Class<? super T>>();
            todo.add((Class<T>) object.getClass());
            while (!todo.isEmpty()) {
                Class<? super T> c = todo.iterator().next();
                if (XSComponent.class.isAssignableFrom(c)) {
                    {
                        Class<? super T> cc = c.getSuperclass();
                        if (cc != null && !done.contains(cc)) todo.add(cc);
                    }
                    for (Class<?> cc : c.getInterfaces()) {
                        if (cc != null && !done.contains(cc)) todo.add((Class<? super T>) cc);
                    }
                }
                todo.remove(c);
                done.add(c);
            }
            Collection<Processor<?>> result = new ArrayList<Processor<?>>();
            for (Class<? super T> cc : done) {
                if (!XSComponent.class.isAssignableFrom(cc)) continue;
                if (!cc.isInterface()) {
                    continue;
                }
                if (cc.getName().endsWith("Impl")) {
                    continue;
                }
                Class<? extends XSComponent> c = (Class<? extends XSComponent>) cc;
                if (!processors.containsKey(c)) {
                    LOG.warn("Unhandled type " + c);
                } else {
                    result.add(processors.get(c));
                }
            }
            processorsForClass.put(object.getClass(), result);
        }
        return (Collection<Processor<? super T>>) (Collection<?>) processorsForClass.get(object.getClass());
    }
}
