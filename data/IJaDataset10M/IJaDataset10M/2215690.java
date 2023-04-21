package xQuery;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprFunction;
import com.hp.hpl.jena.sparql.expr.FunctionLabel;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.syntax.Element;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;

/**
 * class  Compiler from SPARQL to XQuery
 *
 */
public class XQueryCompiler implements IXueryCompilable {

    private Query query = null;

    /**
	 * RDF module name 
	 */
    public static String rdfFuncModuleName = "rdffunc";

    /**
     * RDF module namsespace
     */
    public static String rdfFuncModuleNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#/func";

    /**
     * Namespace that is representing PREFIX : 
     */
    public static String SelfNamespace = "self";

    /**
     * Namespace for prefix rdf 
     */
    public static String rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    /**
	 * Prefix name for rdf = "rdf"
	 */
    public static String rdfPrefixName = "rdf";

    private static XQueryModule xRdfModule = null;

    private XQueryWhere xquerywhere = null;

    private String docname = "docname";

    private int prefnum = 1;

    public XQueryCompiler(Query query) {
        this.query = query;
        if (XQueryCompiler.xRdfModule == null) XQueryCompiler.xRdfModule = new XQueryModule(XQueryCompiler.rdfFuncModuleName, XQueryCompiler.rdfFuncModuleNamespace);
        this.xquerywhere = new XQueryWhere();
        AddNsPrefixNamespace(XQueryCompiler.rdfNamespace, XQueryCompiler.rdfPrefixName);
    }

    /**
	 * Check if  prefix exists otherwise add it to prefixmapping
	 */
    private void AddNsPrefixNamespace(String ns, String prefix) {
        if (this.query.getPrefixMapping().getNsURIPrefix(ns) == null) {
            this.query.setPrefix(prefix, ns);
        }
    }

    public String getPropertyName(Node predicate) throws Exception {
        if (!predicate.isURI()) throw new Exception(String.format("Invalid %s URI", predicate));
        String result = query.getPrefixMapping().qnameFor(predicate.getURI());
        if (result != null && result.startsWith(":")) result = XQueryCompiler.SelfNamespace + result;
        if (result == null) {
            String uri = predicate.getURI();
            int ind = uri.lastIndexOf('#');
            int ind2 = uri.lastIndexOf('/');
            if (ind2 > ind) ind = ind2;
            String ns = uri.substring(0, ind + 1);
            String localpart = uri.substring(ind + 1);
            String prefix = String.format("pref%d", prefnum);
            this.AddNsPrefixNamespace(ns, prefix);
            prefnum++;
            result = String.format("%s:%s", prefix, localpart);
        }
        return result;
    }

    /**
	 * Get XQuery variable name from the resource  
	 * @param resource 
	 * @param ismulti  if true gets XQuery variable that represent variable collection  
	 * @return result
	 * @throws Exception
	 */
    public String getXQueryVariable(Node resource, boolean ismulti) throws Exception {
        if (!resource.isVariable()) throw new Exception(String.format("Invalid %s resource", resource.getName()));
        String ends = "";
        if (ismulti) ends = "s";
        String result = String.format("$%s%s", resource.getName(), ends);
        return result;
    }

    public String getXQueryVariable(String resultvar, boolean ismulti) {
        String xqueryvar = "$" + resultvar;
        if (ismulti) xqueryvar += "s";
        return xqueryvar;
    }

    public String getXQueryVariable(String resultvar) {
        return this.getXQueryVariable(resultvar, false);
    }

    /**
	 * gets RDF/XML document name
	 * @return gets document name
	 */
    public String getDocName() {
        return docname;
    }

    /**
	 * set RDF/XML document name
	 * @param docname
	 */
    public void setDocName(String docname) {
        this.docname = docname;
    }

    @Override
    public String Compile() throws Exception {
        String xqueryresult = XQueryCompiler.xRdfModule.Compile() + "\n";
        if (!query.isSelectType()) throw new Exception("Only select  is supported");
        if (query.isDistinct()) throw new Exception("Select distinct is not supported");
        ElementGroup block = (ElementGroup) query.getQueryPattern();
        for (Iterator<Element> i = block.getElements().iterator(); i.hasNext(); ) {
            Element element = i.next();
            if (element instanceof ElementTriplesBlock) {
                ElementTriplesBlock t = (ElementTriplesBlock) element;
                xqueryresult += AddElementTriplesBlock(t);
            }
            if (element instanceof ElementFilter) {
                ElementFilter elementFilter = (ElementFilter) element;
                Expr expr = elementFilter.getExpr();
                String xqueryexp = GetExpFilter(expr);
                this.xquerywhere.AddExpression(xqueryexp);
            }
        }
        xqueryresult += this.xquerywhere.getResult();
        xqueryresult += AddResultVars(query.getResultVars());
        xqueryresult = AddNamespaces(query.getPrefixMapping()) + xqueryresult;
        return xqueryresult;
    }

    private String GetExpFilter(Expr expr) throws Exception {
        String result = "";
        NodeValue constexp = null;
        ExprFunction exprfunction = null;
        String varname = null;
        constexp = expr.getConstant();
        exprfunction = expr.getFunction();
        varname = expr.getVarName();
        Expr expr1 = null, expr2 = null;
        String param1 = null, param2 = null;
        if (varname != null) result = this.getXQueryVariable(varname, false);
        if (exprfunction != null) {
            String opername = exprfunction.getOpName();
            List args = exprfunction.getArgs();
            int argscount = exprfunction.numArgs();
            boolean issupported = false;
            FunctionLabel flabel = exprfunction.getFunctionSymbol();
            String func = flabel.getSymbol();
            if (opername != null && argscount == 2) {
                expr1 = (Expr) args.get(0);
                expr2 = (Expr) args.get(1);
                param1 = GetExpFilter(expr1);
                param2 = GetExpFilter(expr2);
                if (func.equals("and") || func.equals("or")) opername = func;
                result = String.format("(%s %s %s)", param1, opername, param2);
                issupported = true;
            }
            if (func == "regex") {
                if (argscount >= 2) {
                    expr1 = (Expr) args.get(0);
                    expr2 = (Expr) args.get(1);
                    Expr expr3 = null;
                    String param3 = null;
                    param1 = GetExpFilter(expr1);
                    param2 = GetExpFilter(expr2);
                    if (argscount == 3) {
                        expr3 = (Expr) args.get(2);
                        param3 = GetExpFilter(expr3);
                    }
                    if (argscount == 2) result = String.format("fn:matches(%s,%s)", param1, param2);
                    if (argscount == 3) result = String.format("fn:matches(%s,%s,%s)", param1, param2, param3);
                }
                issupported = true;
            }
            if (func == "lang") {
                expr1 = (Expr) args.get(0);
                if (!expr1.isVariable()) throw new Exception("for function lang params must be variable");
                param1 = GetExpFilter(expr1);
                result = String.format("%s/@xml:lang", param1);
                issupported = true;
            }
            if (!issupported) throw new Exception(String.format("Function %s is not supported", func));
        }
        if (constexp != null) {
            result = constexp.toString();
        }
        return result;
    }

    private String AddNamespaces(PrefixMapping prefixMapping) {
        String result = "";
        Set<String> keys = prefixMapping.getNsPrefixMap().keySet();
        for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
            String ns = i.next();
            String urivalue = prefixMapping.getNsPrefixURI(ns);
            if (ns.length() == 0) ns = XQueryCompiler.SelfNamespace;
            result += String.format("declare namespace %s='%s';\n", ns, urivalue);
        }
        return result;
    }

    private String AddElementTriplesBlock(ElementTriplesBlock t) throws Exception {
        boolean isfirst = true;
        String xqueryresult = "";
        for (Iterator<Triple> i2 = t.triples(); i2.hasNext(); ) {
            Triple triple = (Triple) i2.next();
            Node obj = triple.getObject();
            Node subject = triple.getSubject();
            Node predicate = triple.getPredicate();
            if (isfirst) {
                if (subject.isVariable()) {
                    String pred = this.getPropertyName(predicate);
                    String mainTriplestr = String.format("let $%ss := document('%s')//*[%s]", subject.getName(), docname, pred);
                    xqueryresult += mainTriplestr + "\n";
                }
                xqueryresult += AddSimpleCycle(subject);
                isfirst = false;
            }
            if (obj.isVariable() && subject.isVariable()) {
                String predstr = this.getPropertyName(predicate);
                xqueryresult += String.format("let $%1$ss:=$%2$s/%3$s \n", obj.getName(), subject.getName(), predstr);
                xqueryresult += AddSimpleCycle(obj);
            }
            if (obj.isLiteral() && subject.isVariable()) {
                String predstr = this.getPropertyName(predicate);
                String xqueryvar = this.getXQueryVariable(subject, false);
                Object liverval = obj.getLiteralValue();
                if (liverval instanceof String) {
                    String exp = String.format("%1$s/%2$s=\"%3$s\"", xqueryvar, predstr, liverval);
                    this.xquerywhere.AddExpression(exp);
                }
            }
            if (subject.isVariable() && obj.isURI()) {
                String predstr = this.getPropertyName(predicate);
                String exp = String.format("$%1$s/%2$s/@rdf:resource='%3$s'", subject.getName(), predstr, obj.getURI());
                if (predstr.equals("rdf:type")) {
                    String objnamespace = obj.getNameSpace();
                    String objlocalname = obj.getLocalName();
                    exp = String.format("(%1$s or (fn:namespace-uri($%2$s)='%3$s' and fn:local-name($%2$s)='%4$s'))", exp, subject.getName(), objnamespace, objlocalname);
                }
                this.xquerywhere.AddExpression(exp);
            }
        }
        return xqueryresult;
    }

    /**
	 * 
	 * @param resultVars  select variable list 
	 * @return 
	 */
    private String AddResultVars(List<String> resultVars) {
        String result = "return <result>\n";
        for (Iterator<String> iter = resultVars.iterator(); iter.hasNext(); ) {
            String var = iter.next();
            String xqueryvar = this.getXQueryVariable(var, false);
            result += "{" + String.format("rdffunc:objectResult(%1$s,%1$ss)", xqueryvar) + "}\n";
        }
        result += "</result>\n";
        return result;
    }

    private String AddSimpleCycle(Node subject) {
        String result = "";
        result = String.format("for $%1$s in $%1$ss \n", subject.getName());
        return result;
    }
}
