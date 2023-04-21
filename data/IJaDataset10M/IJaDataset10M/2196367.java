package alice.c4jason;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Atom;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.VarTerm;
import java.util.Collection;
import java.util.Map;
import alice.cartago.*;
import alice.cartago.util.Tuple;
import alice.cartago.util.TupleTemplate;

public class TermConversionUtils {

    private static final String[] NO_PARAMS = new String[] {};

    public static IPerceptionFilter toFilter(Term template) {
        if (template.isList()) {
            ListTerm l = (ListTerm) template;
            alice.cartago.Tuple[] tlist = new alice.cartago.Tuple[l.size()];
            int j = 0;
            for (Term templ : l) {
                if (templ.isStructure()) {
                    Structure p = (Structure) templ;
                    String[] objs = new String[p.getArity()];
                    for (int i = 0; i < p.getArity(); i++) {
                        Term t = p.getTerm(i);
                        if (t.isVar()) {
                            objs[i] = null;
                        } else if (t.isString()) {
                            objs[i] = ((StringTerm) t).getString();
                        } else {
                            objs[i] = t.toString();
                        }
                    }
                    tlist[j] = new alice.cartago.Tuple(p.getFunctor(), (Object[]) objs);
                } else if (templ.isAtom()) {
                    tlist[j] = new alice.cartago.Tuple(((Atom) templ).getFunctor());
                } else if (templ.isString()) {
                    tlist[j] = new alice.cartago.Tuple(((StringTerm) templ).getString().toString());
                }
                j++;
            }
            return new StringTupleListFilter(tlist);
        } else if (template.isStructure()) {
            Structure p = (Structure) template;
            String[] objs = new String[p.getArity()];
            for (int i = 0; i < p.getArity(); i++) {
                Term t = p.getTerm(i);
                if (t.isVar()) {
                    objs[i] = null;
                } else if (t.isString()) {
                    objs[i] = ((StringTerm) t).getString();
                } else {
                    objs[i] = t.toString();
                }
            }
            return new StringTupleFilter(p.getFunctor(), objs);
        } else if (template.isAtom()) {
            return new StringTupleFilter(((Atom) template).getFunctor(), NO_PARAMS);
        } else if (template.isString()) {
            return new StringTupleFilter(template.toString(), NO_PARAMS);
        } else {
            return null;
        }
    }

    public static Term toTerm(alice.cartago.Perception p, JavaLibrary lib) throws Exception {
        return ASSyntax.createStructure(p.getLabel(), objectArray2termArray(p.getContents(), lib));
    }

    public static Object termToObject(Term t, JavaLibrary lib) {
        if (t.isString()) {
            return ((StringTerm) t).getString();
        } else if (t instanceof VarTerm) {
            VarTerm var = (VarTerm) t;
            if (var.isGround()) {
                if (var.getValue().isAtom()) {
                    Atom t2 = (Atom) var.getValue();
                    if (t2.equals(Atom.LTrue)) {
                        return Boolean.TRUE;
                    } else if (t2.equals(Atom.LFalse)) {
                        return Boolean.FALSE;
                    } else {
                        Object obj = lib.getObject(t2);
                        if (obj != null) {
                            return obj;
                        } else {
                            return t2.getFunctor();
                        }
                    }
                } else if (var.getValue().isNumeric()) {
                    NumberTerm nt = (NumberTerm) var.getValue();
                    double d = nt.solve();
                    if (((byte) d) == d) {
                        return (byte) d;
                    } else if (((int) d) == d) {
                        return (int) d;
                    } else if (((float) d) == d) {
                        return (float) d;
                    } else if (((long) d) == d) {
                        return (long) d;
                    } else {
                        return d;
                    }
                } else {
                    return var.getValue().toString();
                }
            } else {
                return null;
            }
        } else if (t.isAtom()) {
            Atom t2 = (Atom) t;
            if (t2.equals(Atom.LTrue)) {
                return Boolean.TRUE;
            } else if (t2.equals(Atom.LFalse)) {
                return Boolean.FALSE;
            } else {
                Object obj = lib.getObject(t2);
                if (obj != null) {
                    return obj;
                } else {
                    return t2.getFunctor();
                }
            }
        } else if (t.isNumeric()) {
            NumberTerm nt = (NumberTerm) t;
            double d = nt.solve();
            if (((byte) d) == d) {
                return (byte) d;
            } else if (((int) d) == d) {
                return (int) d;
            } else if (((float) d) == d) {
                return (float) d;
            } else if (((long) d) == d) {
                return (long) d;
            } else {
                return d;
            }
        } else {
            return t.toString();
        }
    }

    public static Literal toLiteral(alice.cartago.Event p, JavaLibrary lib) {
        Object[] contents = p.getContents();
        try {
            Literal l = ASSyntax.createLiteral(p.getLabel(), objectArray2termArray(contents, lib));
            l.addAnnot(ASSyntax.createStructure("source", ASSyntax.createString(p.getSourceId().getName())));
            l.addAnnot(ASSyntax.createStructure("workspace", ASSyntax.createString(p.getSourceId().getWorkspaceId().getName())));
            OpId opId = p.getRelatedOpId();
            Structure opName = ASSyntax.createStructure("op_name");
            if (opId != null) {
                opName.addTerm(ASSyntax.createString(opId.getOpName()));
            }
            l.addAnnot(opName);
            if (opId != null) {
                Structure opids = ASSyntax.createStructure("op_id", ASSyntax.createNumber(opId.getId()));
                l.addAnnot(opids);
            }
            Structure when = ASSyntax.createStructure("when", ASSyntax.createNumber(p.getTime()));
            l.addAnnot(when);
            Structure evId = ASSyntax.createStructure("event_id", ASSyntax.createNumber(p.getId()));
            l.addAnnot(evId);
            return l;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Literal toTerm(ArtifactObsProperty p, JavaLibrary lib) throws Exception {
        return ASSyntax.createLiteral(p.getName(), object2term(p.getValue(), lib));
    }

    public static Literal toPercept(ArtifactId source, String propName, Object value, JavaLibrary lib) throws Exception {
        Literal struct = ASSyntax.createLiteral(propName, object2term(value, lib));
        struct.addAnnot(ASSyntax.createStructure("source", new Atom("percept")));
        struct.addAnnot(ASSyntax.createStructure("artifact", ASSyntax.createString(source.getName())));
        struct.addAnnot(ASSyntax.createStructure("workspace", ASSyntax.createString(source.getWorkspaceId().getName())));
        return struct;
    }

    @SuppressWarnings("unchecked")
    public static Term object2term(Object value, JavaLibrary lib) throws Exception {
        if (value == null) {
            return ASSyntax.createVar("_");
        } else if (value instanceof ToProlog) {
            return ASSyntax.parseTerm(((ToProlog) value).getAsPrologStr());
        } else if (value instanceof ArtifactId) {
            ArtifactId aid = (ArtifactId) value;
            return ASSyntax.createStructure("artifact_id", ASSyntax.createNumber(aid.getId()), ASSyntax.createString(aid.getName()), ASSyntax.createString(aid.getWorkspaceId().toString()));
        } else if (value instanceof Number) {
            try {
                return ASSyntax.parseNumber(value.toString());
            } catch (Exception ex) {
                return ASSyntax.createString(value.toString());
            }
        } else if (value instanceof String) {
            return ASSyntax.createString(value.toString());
        } else if (value instanceof Map) {
            ListTerm l = new ListTermImpl();
            ListTerm tail = l;
            Map m = (Map) value;
            for (Object k : m.keySet()) {
                Term pair = ASSyntax.createStructure("map", object2term(k, lib), object2term(m.get(k), lib));
                tail = tail.append(pair);
            }
            return l;
        } else if (value instanceof Collection) {
            ListTerm l = new ListTermImpl();
            ListTerm tail = l;
            for (Object e : (Collection) value) tail = tail.append(object2term(e, lib));
            return l;
        } else if (value instanceof Tuple) {
            Tuple t = (Tuple) value;
            Structure st = ASSyntax.createStructure(t.getLabel());
            for (int i = 0; i < t.getNArgs(); i++) {
                st.addTerm(object2term(t.getContent(i), lib));
            }
            return st;
        }
        return lib.registerDynamic(value);
    }

    public static Term[] objectArray2termArray(Object[] values, JavaLibrary lib) throws Exception {
        Term[] result = new Term[values.length];
        for (int i = 0; i < values.length; i++) result[i] = object2term(values[i], lib);
        return result;
    }

    public static Tuple termToTuple(Term t, JavaLibrary lib) {
        if (t.isAtom()) {
            return new Tuple(((Atom) t).getFunctor());
        } else if (t.isStructure()) {
            Structure st = (Structure) t;
            Term[] ta = st.getTermsArray();
            Object[] objs = new Object[ta.length];
            for (int i = 0; i < ta.length; i++) {
                objs[i] = termToObject(ta[i], lib);
            }
            return new Tuple(st.getFunctor(), objs);
        } else throw new IllegalArgumentException();
    }

    public static TupleTemplate termToTupleTemplate(Term t, JavaLibrary lib) {
        if (t.isAtom()) {
            return new TupleTemplate(((Atom) t).getFunctor());
        } else if (t.isStructure()) {
            Structure st = (Structure) t;
            Term[] ta = st.getTermsArray();
            Object[] objs = new Object[ta.length];
            for (int i = 0; i < ta.length; i++) {
                objs[i] = termToObject(ta[i], lib);
            }
            return new TupleTemplate(st.getFunctor(), objs);
        } else throw new IllegalArgumentException();
    }

    public static Term tupleToTerm(Tuple t, JavaLibrary lib) throws Exception {
        Structure st = new Structure(t.getLabel());
        for (Object obj : t.getContents()) {
            st.addTerm(object2term(obj, lib));
        }
        return st;
    }

    public static Term tupleTemplateToTerm(TupleTemplate t, JavaLibrary lib) throws Exception {
        Structure st = new Structure(t.getLabel());
        for (Object obj : t.getContents()) {
            st.addTerm(object2term(obj, lib));
        }
        return st;
    }
}
