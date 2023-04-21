package ontograf;

import java.io.PrintStream;
import java.util.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.PrefixMapping;

public class DescribeClass {

    private Map m_anonIDs = new HashMap();

    private int m_anonCount = 0;

    public void describeClass(PrintStream out, OntClass cls) {
        renderClassDescription(out, cls);
        out.println();
        for (Iterator i = cls.listSuperClasses(true); i.hasNext(); ) {
            out.print("  is a sub-class of ");
            renderClassDescription(out, (OntClass) i.next());
            out.println();
        }
        for (Iterator i = cls.listSubClasses(true); i.hasNext(); ) {
            out.print("  is a super-class of ");
            renderClassDescription(out, (OntClass) i.next());
            out.println();
        }
    }

    public void renderClassDescription(PrintStream out, OntClass c) {
        if (c.isUnionClass()) {
            renderBooleanClass(out, "union", c.asUnionClass());
        } else if (c.isIntersectionClass()) {
            renderBooleanClass(out, "intersection", c.asIntersectionClass());
        } else if (c.isComplementClass()) {
            renderBooleanClass(out, "complement", c.asComplementClass());
        } else if (c.isRestriction()) {
            renderRestriction(out, c.asRestriction());
        } else {
            if (!c.isAnon()) {
                out.print("Class ");
                renderURI(out, prefixesFor(c), c.getURI());
                out.print(' ');
            } else {
                renderAnonymous(out, c, "class");
            }
        }
    }

    protected void renderRestriction(PrintStream out, Restriction r) {
        if (!r.isAnon()) {
            out.print("Restriction ");
            renderURI(out, prefixesFor(r), r.getURI());
        } else {
            renderAnonymous(out, r, "restriction");
        }
        out.println();
        renderRestrictionElem(out, "    on property", r.getOnProperty());
        out.println();
        if (r.isAllValuesFromRestriction()) {
            renderRestrictionElem(out, "    all values from", r.asAllValuesFromRestriction().getAllValuesFrom());
        }
        if (r.isSomeValuesFromRestriction()) {
            renderRestrictionElem(out, "    some values from", r.asSomeValuesFromRestriction().getSomeValuesFrom());
        }
        if (r.isHasValueRestriction()) {
            renderRestrictionElem(out, "    has value", r.asHasValueRestriction().getHasValue());
        }
    }

    protected void renderRestrictionElem(PrintStream out, String desc, RDFNode value) {
        out.print(desc);
        out.print(" ");
        renderValue(out, value);
    }

    protected void renderValue(PrintStream out, RDFNode value) {
        if (value.canAs(OntClass.class)) {
            renderClassDescription(out, (OntClass) value.as(OntClass.class));
        } else if (value instanceof Resource) {
            Resource r = (Resource) value;
            if (r.isAnon()) {
                renderAnonymous(out, r, "resource");
            } else {
                renderURI(out, r.getModel(), r.getURI());
            }
        } else if (value instanceof Literal) {
            out.print(((Literal) value).getLexicalForm());
        } else {
            out.print(value);
        }
    }

    protected void renderURI(PrintStream out, PrefixMapping prefixes, String uri) {
        out.print(prefixes.shortForm(uri));
    }

    protected PrefixMapping prefixesFor(Resource n) {
        return n.getModel().getGraph().getPrefixMapping();
    }

    protected void renderAnonymous(PrintStream out, Resource anon, String name) {
        String anonID = (String) m_anonIDs.get(anon.getId());
        if (anonID == null) {
            anonID = "a-" + m_anonCount++;
            m_anonIDs.put(anon.getId(), anonID);
        }
        out.print("Anonymous ");
        out.print(name);
        out.print(" with ID ");
        out.print(anonID);
    }

    protected void renderBooleanClass(PrintStream out, String op, BooleanClassDescription boolClass) {
        out.print(op);
        out.println(" of {");
        for (Iterator i = boolClass.listOperands(); i.hasNext(); ) {
            out.print("      ");
            renderClassDescription(out, (OntClass) i.next());
            out.println();
        }
        out.print("  } ");
    }
}
