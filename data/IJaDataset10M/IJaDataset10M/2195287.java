package copasi.ws.sbrml;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * <p>Java class for maction.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="maction.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.w3.org/1998/Math/MathML}maction.content" maxOccurs="unbounded"/>
 *       &lt;attGroup ref="{http://www.w3.org/1998/Math/MathML}maction.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "maction.type", propOrder = { "misAndMosAndMns" })
@XmlRootElement(name = "maction")
public class Maction {

    @XmlElementRefs({ @XmlElementRef(name = "malignmark", namespace = "http://www.w3.org/1998/Math/MathML", type = Malignmark.class), @XmlElementRef(name = "mn", namespace = "http://www.w3.org/1998/Math/MathML", type = Mn.class), @XmlElementRef(name = "moment", namespace = "http://www.w3.org/1998/Math/MathML", type = Moment.class), @XmlElementRef(name = "cartesianproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = Cartesianproduct.class), @XmlElementRef(name = "arcsinh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "notsubset", namespace = "http://www.w3.org/1998/Math/MathML", type = Notsubset.class), @XmlElementRef(name = "factorof", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "lowlimit", namespace = "http://www.w3.org/1998/Math/MathML", type = Lowlimit.class), @XmlElementRef(name = "arccoth", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mode", namespace = "http://www.w3.org/1998/Math/MathML", type = Mode.class), @XmlElementRef(name = "mroot", namespace = "http://www.w3.org/1998/Math/MathML", type = Mroot.class), @XmlElementRef(name = "menclose", namespace = "http://www.w3.org/1998/Math/MathML", type = Menclose.class), @XmlElementRef(name = "sum", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ceiling", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arg", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "vector", namespace = "http://www.w3.org/1998/Math/MathML", type = Vector.class), @XmlElementRef(name = "arctan", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "factorial", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "approx", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "int", namespace = "http://www.w3.org/1998/Math/MathML", type = Int.class), @XmlElementRef(name = "notprsubset", namespace = "http://www.w3.org/1998/Math/MathML", type = Notprsubset.class), @XmlElementRef(name = "not", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "coth", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "scalarproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = Scalarproduct.class), @XmlElementRef(name = "bvar", namespace = "http://www.w3.org/1998/Math/MathML", type = Bvar.class), @XmlElementRef(name = "ln", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cos", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mphantom", namespace = "http://www.w3.org/1998/Math/MathML", type = Mphantom.class), @XmlElementRef(name = "mover", namespace = "http://www.w3.org/1998/Math/MathML", type = Mover.class), @XmlElementRef(name = "mspace", namespace = "http://www.w3.org/1998/Math/MathML", type = Mspace.class), @XmlElementRef(name = "root", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "merror", namespace = "http://www.w3.org/1998/Math/MathML", type = Merror.class), @XmlElementRef(name = "prsubset", namespace = "http://www.w3.org/1998/Math/MathML", type = Prsubset.class), @XmlElementRef(name = "implies", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "conjugate", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "equivalent", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "quotient", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "csymbol", namespace = "http://www.w3.org/1998/Math/MathML", type = Csymbol.class), @XmlElementRef(name = "mean", namespace = "http://www.w3.org/1998/Math/MathML", type = Mean.class), @XmlElementRef(name = "min", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "geq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "tan", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "codomain", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arcsech", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccsch", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "abs", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "tendsto", namespace = "http://www.w3.org/1998/Math/MathML", type = Tendsto.class), @XmlElementRef(name = "sin", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "limit", namespace = "http://www.w3.org/1998/Math/MathML", type = Limit.class), @XmlElementRef(name = "selector", namespace = "http://www.w3.org/1998/Math/MathML", type = Selector.class), @XmlElementRef(name = "true", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "eq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sdev", namespace = "http://www.w3.org/1998/Math/MathML", type = Sdev.class), @XmlElementRef(name = "laplacian", namespace = "http://www.w3.org/1998/Math/MathML", type = Laplacian.class), @XmlElementRef(name = "condition", namespace = "http://www.w3.org/1998/Math/MathML", type = Condition.class), @XmlElementRef(name = "reals", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccsc", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "maction", namespace = "http://www.w3.org/1998/Math/MathML", type = Maction.class), @XmlElementRef(name = "determinant", namespace = "http://www.w3.org/1998/Math/MathML", type = Determinant.class), @XmlElementRef(name = "transpose", namespace = "http://www.w3.org/1998/Math/MathML", type = Transpose.class), @XmlElementRef(name = "image", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "set", namespace = "http://www.w3.org/1998/Math/MathML", type = Set.class), @XmlElementRef(name = "subset", namespace = "http://www.w3.org/1998/Math/MathML", type = Subset.class), @XmlElementRef(name = "log", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mi", namespace = "http://www.w3.org/1998/Math/MathML", type = Mi.class), @XmlElementRef(name = "mmultiscripts", namespace = "http://www.w3.org/1998/Math/MathML", type = Mmultiscripts.class), @XmlElementRef(name = "mpadded", namespace = "http://www.w3.org/1998/Math/MathML", type = Mpadded.class), @XmlElementRef(name = "integers", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "product", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "divergence", namespace = "http://www.w3.org/1998/Math/MathML", type = Divergence.class), @XmlElementRef(name = "vectorproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = Vectorproduct.class), @XmlElementRef(name = "divide", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "card", namespace = "http://www.w3.org/1998/Math/MathML", type = Card.class), @XmlElementRef(name = "degree", namespace = "http://www.w3.org/1998/Math/MathML", type = Degree.class), @XmlElementRef(name = "naturalnumbers", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "exists", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "momentabout", namespace = "http://www.w3.org/1998/Math/MathML", type = Momentabout.class), @XmlElementRef(name = "primes", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arctanh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "maligngroup", namespace = "http://www.w3.org/1998/Math/MathML", type = Maligngroup.class), @XmlElementRef(name = "emptyset", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "plus", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "false", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mo", namespace = "http://www.w3.org/1998/Math/MathML", type = Mo.class), @XmlElementRef(name = "msubsup", namespace = "http://www.w3.org/1998/Math/MathML", type = Msubsup.class), @XmlElementRef(name = "cosh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "inverse", namespace = "http://www.w3.org/1998/Math/MathML", type = Inverse.class), @XmlElementRef(name = "gcd", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "imaginary", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "eulergamma", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arcsec", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "logbase", namespace = "http://www.w3.org/1998/Math/MathML", type = Logbase.class), @XmlElementRef(name = "real", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sech", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "lt", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mfenced", namespace = "http://www.w3.org/1998/Math/MathML", type = Mfenced.class), @XmlElementRef(name = "matrix", namespace = "http://www.w3.org/1998/Math/MathML", type = Matrix.class), @XmlElementRef(name = "exponentiale", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "rem", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccosh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msup", namespace = "http://www.w3.org/1998/Math/MathML", type = Msup.class), @XmlElementRef(name = "or", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sec", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mtext", namespace = "http://www.w3.org/1998/Math/MathML", type = Mtext.class), @XmlElementRef(name = "apply", namespace = "http://www.w3.org/1998/Math/MathML", type = Apply.class), @XmlElementRef(name = "max", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "setdiff", namespace = "http://www.w3.org/1998/Math/MathML", type = Setdiff.class), @XmlElementRef(name = "gt", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msqrt", namespace = "http://www.w3.org/1998/Math/MathML", type = Msqrt.class), @XmlElementRef(name = "minus", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "imaginaryi", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "floor", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccot", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "semantics", namespace = "http://www.w3.org/1998/Math/MathML", type = Semantics.class), @XmlElementRef(name = "interval", namespace = "http://www.w3.org/1998/Math/MathML", type = Interval.class), @XmlElementRef(name = "median", namespace = "http://www.w3.org/1998/Math/MathML", type = Median.class), @XmlElementRef(name = "domainofapplication", namespace = "http://www.w3.org/1998/Math/MathML", type = Domainofapplication.class), @XmlElementRef(name = "partialdiff", namespace = "http://www.w3.org/1998/Math/MathML", type = Partialdiff.class), @XmlElementRef(name = "leq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "piecewise", namespace = "http://www.w3.org/1998/Math/MathML", type = Piecewise.class), @XmlElementRef(name = "csch", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "xor", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "tanh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "munder", namespace = "http://www.w3.org/1998/Math/MathML", type = Munder.class), @XmlElementRef(name = "domain", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "grad", namespace = "http://www.w3.org/1998/Math/MathML", type = Grad.class), @XmlElementRef(name = "forall", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "list", namespace = "http://www.w3.org/1998/Math/MathML", type = copasi.ws.sbrml.List.class), @XmlElementRef(name = "mfrac", namespace = "http://www.w3.org/1998/Math/MathML", type = Mfrac.class), @XmlElementRef(name = "munderover", namespace = "http://www.w3.org/1998/Math/MathML", type = Munderover.class), @XmlElementRef(name = "neq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msub", namespace = "http://www.w3.org/1998/Math/MathML", type = Msub.class), @XmlElementRef(name = "mtable", namespace = "http://www.w3.org/1998/Math/MathML", type = Mtable.class), @XmlElementRef(name = "lcm", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ms", namespace = "http://www.w3.org/1998/Math/MathML", type = Ms.class), @XmlElementRef(name = "notin", namespace = "http://www.w3.org/1998/Math/MathML", type = Notin.class), @XmlElementRef(name = "power", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ci", namespace = "http://www.w3.org/1998/Math/MathML", type = Ci.class), @XmlElementRef(name = "infinity", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "exp", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "curl", namespace = "http://www.w3.org/1998/Math/MathML", type = Curl.class), @XmlElementRef(name = "in", namespace = "http://www.w3.org/1998/Math/MathML", type = In.class), @XmlElementRef(name = "arccos", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "rationals", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mstyle", namespace = "http://www.w3.org/1998/Math/MathML", type = Mstyle.class), @XmlElementRef(name = "variance", namespace = "http://www.w3.org/1998/Math/MathML", type = Variance.class), @XmlElementRef(name = "outerproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = Outerproduct.class), @XmlElementRef(name = "declare", namespace = "http://www.w3.org/1998/Math/MathML", type = Declare.class), @XmlElementRef(name = "notanumber", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "and", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "csc", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cn", namespace = "http://www.w3.org/1998/Math/MathML", type = Cn.class), @XmlElementRef(name = "arcsin", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "union", namespace = "http://www.w3.org/1998/Math/MathML", type = Union.class), @XmlElementRef(name = "complexes", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "times", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "diff", namespace = "http://www.w3.org/1998/Math/MathML", type = Diff.class), @XmlElementRef(name = "compose", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cot", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mrow", namespace = "http://www.w3.org/1998/Math/MathML", type = Mrow.class), @XmlElementRef(name = "pi", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "intersect", namespace = "http://www.w3.org/1998/Math/MathML", type = Intersect.class), @XmlElementRef(name = "ident", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "uplimit", namespace = "http://www.w3.org/1998/Math/MathML", type = Uplimit.class), @XmlElementRef(name = "lambda", namespace = "http://www.w3.org/1998/Math/MathML", type = Lambda.class), @XmlElementRef(name = "sinh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class) })
    protected java.util.List<Object> misAndMosAndMns;

    @XmlAttribute(required = true)
    protected String actiontype;

    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger selection;

    @XmlAttribute(name = "class")
    @XmlSchemaType(name = "NMTOKENS")
    protected java.util.List<String> clazzs;

    @XmlAttribute
    protected String style;

    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object xref;

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String href;

    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the misAndMosAndMns property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the misAndMosAndMns property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMisAndMosAndMns().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Malignmark }
     * {@link Moment }
     * {@link Mn }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Cartesianproduct }
     * {@link Notsubset }
     * {@link Lowlimit }
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Mode }
     * {@link Menclose }
     * {@link Mroot }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Vector }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Notprsubset }
     * {@link Int }
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link Scalarproduct }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Bvar }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Mover }
     * {@link Mphantom }
     * {@link Mspace }
     * {@link Merror }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Prsubset }
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Mean }
     * {@link Csymbol }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Tendsto }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Limit }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Selector }
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link Sdev }
     * {@link Condition }
     * {@link Laplacian }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Maction }
     * {@link Determinant }
     * {@link Transpose }
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link Set }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Subset }
     * {@link Mi }
     * {@link Mmultiscripts }
     * {@link Mpadded }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Divergence }
     * {@link Vectorproduct }
     * {@link Degree }
     * {@link Card }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Momentabout }
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Maligngroup }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Mo }
     * {@link Inverse }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Msubsup }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Logbase }
     * {@link Mfenced }
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link Matrix }
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Msup }
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Mtext }
     * {@link Apply }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Setdiff }
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Msqrt }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Semantics }
     * {@link Domainofapplication }
     * {@link Median }
     * {@link Interval }
     * {@link Partialdiff }
     * {@link Piecewise }
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link Munder }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link Grad }
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link copasi.ws.sbrml.List }
     * {@link Mfrac }
     * {@link Munderover }
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link Msub }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Mtable }
     * {@link Ms }
     * {@link Notin }
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Ci }
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Curl }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link In }
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Mstyle }
     * {@link Variance }
     * {@link Outerproduct }
     * {@link Declare }
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Cn }
     * {@link Union }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link Diff }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link Mrow }
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link Intersect }
     * {@link Uplimit }
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link Lambda }
     * 
     * 
     */
    public java.util.List<Object> getMisAndMosAndMns() {
        if (misAndMosAndMns == null) {
            misAndMosAndMns = new ArrayList<Object>();
        }
        return this.misAndMosAndMns;
    }

    /**
     * Gets the value of the actiontype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActiontype() {
        return actiontype;
    }

    /**
     * Sets the value of the actiontype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActiontype(String value) {
        this.actiontype = value;
    }

    /**
     * Gets the value of the selection property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSelection() {
        if (selection == null) {
            return new BigInteger("1");
        } else {
            return selection;
        }
    }

    /**
     * Sets the value of the selection property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSelection(BigInteger value) {
        this.selection = value;
    }

    /**
     * Gets the value of the clazzs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazzs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazzs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public java.util.List<String> getClazzs() {
        if (clazzs == null) {
            clazzs = new ArrayList<String>();
        }
        return this.clazzs;
    }

    /**
     * Gets the value of the style property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStyle() {
        return style;
    }

    /**
     * Sets the value of the style property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStyle(String value) {
        this.style = value;
    }

    /**
     * Gets the value of the xref property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getXref() {
        return xref;
    }

    /**
     * Sets the value of the xref property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setXref(Object value) {
        this.xref = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }
}
