package ontograf;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.PrefixMapping;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.*;
import ontograf.model.ModelKlasy;
import ontograf.model.ModelProperty;

public class ClassHierarchy {

    protected OntModel m_model;

    private Map m_anonIDs = new HashMap();

    private int m_anonCount = 0;

    public List pobierzStruktrue(String absolutePath) throws FileNotFoundException {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        FileReader fr = new FileReader(absolutePath);
        m.read(fr, "");
        List lista = new ArrayList();
        Iterator i = m.listHierarchyRootClasses();
        while (i.hasNext()) {
            ModelKlasy mk = showClass((OntClass) i.next(), new ArrayList(), 0, null);
            if (mk != null) lista.add(mk);
        }
        for (int j = 0; j < lista.size() - 1; j++) {
            for (int k = j + 1; k < lista.size(); k++) {
                ModelKlasy mk1 = (ModelKlasy) lista.get(j);
                ModelKlasy mk2 = (ModelKlasy) lista.get(k);
                if (mk1.getURI().trim().compareTo(mk2.getURI().trim()) == 0) {
                    lista.remove(k);
                }
            }
        }
        return lista;
    }

    protected ModelKlasy showClass(OntClass cls, List occurs, int depth, ModelKlasy m) {
        ModelKlasy mk = renderClassDescription(cls, depth);
        if (m != null) m.addSubClass(mk);
        System.out.println();
        if (mk != null) {
            for (Iterator i = cls.listDeclaredProperties(); i.hasNext(); ) {
                OntProperty p = (OntProperty) i.next();
                System.out.println(p);
                ModelProperty mp = new ModelProperty();
                mp.setURI(p.getURI());
                mp.setKlasa(cls.getURI().trim());
                mk.addProperty(mp);
                mk.addPropertyUri(p.getURI().trim());
            }
            for (Iterator i = cls.listDisjointWith(); i.hasNext(); ) {
                OntClass o = (OntClass) i.next();
                mk.addDisjoint(o.getURI().trim());
            }
            for (Iterator i = cls.listEquivalentClasses(); i.hasNext(); ) {
                OntClass o = (OntClass) i.next();
                if (o.getURI() != null) mk.addEquivalent(o.getURI().trim());
            }
        }
        if (cls.canAs(OntClass.class) && !occurs.contains(cls)) {
            for (Iterator i = cls.listSubClasses(true); i.hasNext(); ) {
                OntClass sub = (OntClass) i.next();
                occurs.add(cls);
                showClass(sub, occurs, depth + 1, mk);
                occurs.remove(cls);
            }
        }
        return mk;
    }

    public ModelKlasy renderClassDescription(OntClass c, int depth) {
        indent(System.out, depth);
        if (c.isRestriction()) {
            return null;
        } else {
            if (!c.isAnon()) {
                System.out.print("Class ");
                renderURI(System.out, c.getModel(), c.getURI());
                System.out.print(' ');
                ModelKlasy mk = new ModelKlasy();
                mk.setURI(c.getURI());
                mk.setLabel(c.getURI().split("#")[1]);
                return mk;
            } else {
                renderAnonymous(System.out, c, "class");
                return null;
            }
        }
    }

    protected void renderRestriction(PrintStream out, Restriction r) {
        if (!r.isAnon()) {
            out.print("Restriction ");
            renderURI(out, r.getModel(), r.getURI());
        } else {
            renderAnonymous(out, r, "restriction");
        }
        out.print(" on property ");
        renderURI(out, r.getModel(), r.getOnProperty().getURI());
    }

    /** Render a URI */
    protected void renderURI(PrintStream out, PrefixMapping prefixes, String uri) {
        out.print(prefixes.shortForm(uri));
    }

    /** Render an anonymous class or restriction */
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

    /** Generate the indentation */
    protected void indent(PrintStream out, int depth) {
        for (int i = 0; i < depth; i++) {
            out.print("  ");
        }
    }
}
