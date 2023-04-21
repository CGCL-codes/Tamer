package org.w3._1998.math.mathml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

/**
 * <p>Java class for interval.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="interval.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.w3.org/1998/Math/MathML}Content-expr.class" maxOccurs="2"/>
 *       &lt;attGroup ref="{http://www.w3.org/1998/Math/MathML}interval.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interval.type", propOrder = { "contentExprClass" })
public class IntervalType {

    @XmlElementRefs({ @XmlElementRef(name = "mn", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "declare", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ln", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "list", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ceiling", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "prsubset", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "domain", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "integers", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "momentabout", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sin", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "logbase", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arcsech", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "or", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "gt", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mpadded", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "curl", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mo", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sech", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "lowlimit", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mphantom", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msub", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "approx", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mmultiscripts", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "min", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arg", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mroot", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "matrix", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "tanh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "limit", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "implies", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "semantics", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "scalarproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "xor", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "factorof", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "quotient", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "uplimit", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "emptyset", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccot", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "plus", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "exists", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "divide", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "root", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "infinity", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sec", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "codomain", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mean", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "bvar", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "geq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ident", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "interval", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "merror", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "munderover", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cosh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sum", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "median", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "primes", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "laplacian", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "set", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "rem", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "inverse", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "neq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "partialdiff", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "malignmark", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "eq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "lcm", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msup", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "piecewise", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cn", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mover", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "and", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mstyle", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "log", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "divergence", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "conjugate", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mtable", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "lt", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arcsinh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "naturalnumbers", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "variance", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "forall", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cartesianproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "false", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "intersect", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msqrt", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "minus", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "domainofapplication", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "subset", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "complexes", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "power", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cot", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "tendsto", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mi", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "pi", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "equivalent", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mfrac", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mfenced", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "degree", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "image", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "eulergamma", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mode", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "notprsubset", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "true", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "condition", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arcsec", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "munder", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "notin", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "imaginary", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "msubsup", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arctan", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "in", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "gcd", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arcsin", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "not", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ci", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "product", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "floor", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "lambda", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "cos", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "exp", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccosh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "union", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sinh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "moment", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "transpose", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "reals", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "vector", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "vectorproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "times", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "diff", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "coth", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "card", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "rationals", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "maligngroup", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "tan", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "factorial", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "outerproduct", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "determinant", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arctanh", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mspace", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccsc", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "leq", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "notanumber", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "int", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "notsubset", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "imaginaryi", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "max", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "maction", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "abs", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "menclose", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "csch", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "exponentiale", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "selector", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "ms", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccos", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "apply", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "csc", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "sdev", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccsch", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mtext", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "setdiff", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "arccoth", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "grad", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "csymbol", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "mrow", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "real", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class), @XmlElementRef(name = "compose", namespace = "http://www.w3.org/1998/Math/MathML", type = JAXBElement.class) })
    protected List<JAXBElement<?>> contentExprClass;

    @XmlAttribute
    protected String closure;

    @XmlAttribute(name = "class")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> clazz;

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
     * Gets the value of the contentExprClass property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentExprClass property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentExprClass().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link MnType }{@code >}
     * {@link JAXBElement }{@code <}{@link DeclareType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ListType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link PrsubsetType }{@code >}
     * {@link JAXBElement }{@code <}{@link MomentaboutType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogbaseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link MpaddedType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link CurlType }{@code >}
     * {@link JAXBElement }{@code <}{@link MoType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link LowlimitType }{@code >}
     * {@link JAXBElement }{@code <}{@link MsubType }{@code >}
     * {@link JAXBElement }{@code <}{@link MphantomType }{@code >}
     * {@link JAXBElement }{@code <}{@link MmultiscriptsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link MrootType }{@code >}
     * {@link JAXBElement }{@code <}{@link MatrixType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link LimitType }{@code >}
     * {@link JAXBElement }{@code <}{@link SemanticsType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link ScalarproductType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link UplimitType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MeanType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link BvarType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MerrorType }{@code >}
     * {@link JAXBElement }{@code <}{@link IntervalType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MunderoverType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link MedianType }{@code >}
     * {@link JAXBElement }{@code <}{@link LaplacianType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetType }{@code >}
     * {@link JAXBElement }{@code <}{@link InverseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link PartialdiffType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MalignmarkType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MsupType }{@code >}
     * {@link JAXBElement }{@code <}{@link PiecewiseType }{@code >}
     * {@link JAXBElement }{@code <}{@link CnType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MstyleType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MoverType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link DivergenceType }{@code >}
     * {@link JAXBElement }{@code <}{@link MtableType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link VarianceType }{@code >}
     * {@link JAXBElement }{@code <}{@link CartesianproductType }{@code >}
     * {@link JAXBElement }{@code <}{@link IntersectType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link MsqrtType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link DomainofapplicationType }{@code >}
     * {@link JAXBElement }{@code <}{@link SubsetType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MiType }{@code >}
     * {@link JAXBElement }{@code <}{@link TendstoType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link MfracType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MfencedType }{@code >}
     * {@link JAXBElement }{@code <}{@link DegreeType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ModeType }{@code >}
     * {@link JAXBElement }{@code <}{@link NotprsubsetType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConditionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link NotinType }{@code >}
     * {@link JAXBElement }{@code <}{@link MunderType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MsubsupType }{@code >}
     * {@link JAXBElement }{@code <}{@link InType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link CiType }{@code >}
     * {@link JAXBElement }{@code <}{@link LogicType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link LambdaType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MomentType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link UnionType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link TransposeType }{@code >}
     * {@link JAXBElement }{@code <}{@link VectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link VectorproductType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link DiffType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link CardType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MaligngroupType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link OuterproductType }{@code >}
     * {@link JAXBElement }{@code <}{@link DeterminantType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link RelationsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link MspaceType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link IntType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link NotsubsetType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link MactionType }{@code >}
     * {@link JAXBElement }{@code <}{@link MencloseType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link SelectorType }{@code >}
     * {@link JAXBElement }{@code <}{@link ConstantType }{@code >}
     * {@link JAXBElement }{@code <}{@link MsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link ApplyType }{@code >}
     * {@link JAXBElement }{@code <}{@link SdevType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link GradType }{@code >}
     * {@link JAXBElement }{@code <}{@link ElementaryFunctionsType }{@code >}
     * {@link JAXBElement }{@code <}{@link SetdiffType }{@code >}
     * {@link JAXBElement }{@code <}{@link MtextType }{@code >}
     * {@link JAXBElement }{@code <}{@link CsymbolType }{@code >}
     * {@link JAXBElement }{@code <}{@link ArithType }{@code >}
     * {@link JAXBElement }{@code <}{@link MrowType }{@code >}
     * {@link JAXBElement }{@code <}{@link FunctionsType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContentExprClass() {
        if (contentExprClass == null) {
            contentExprClass = new ArrayList<JAXBElement<?>>();
        }
        return this.contentExprClass;
    }

    /**
     * Gets the value of the closure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosure() {
        if (closure == null) {
            return "closed";
        } else {
            return closure;
        }
    }

    /**
     * Sets the value of the closure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosure(String value) {
        this.closure = value;
    }

    /**
     * Gets the value of the clazz property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clazz property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClazz().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClazz() {
        if (clazz == null) {
            clazz = new ArrayList<String>();
        }
        return this.clazz;
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
