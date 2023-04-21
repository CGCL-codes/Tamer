package cx.ath.contribs.internal.xerces.impl.xs.identity;

import org.xml.sax.SAXException;
import cx.ath.contribs.internal.xerces.impl.Constants;
import cx.ath.contribs.internal.xerces.impl.xpath.XPath;
import cx.ath.contribs.internal.xerces.util.IntStack;
import cx.ath.contribs.internal.xerces.xni.QName;
import cx.ath.contribs.internal.xerces.xni.XMLAttributes;
import cx.ath.contribs.internal.xerces.xs.AttributePSVI;
import cx.ath.contribs.internal.xerces.xs.ShortList;
import cx.ath.contribs.internal.xerces.xs.XSTypeDefinition;

/**
 * XPath matcher.
 *
 * @xerces.internal 
 *
 * @author Andy Clark, IBM
 *
 * @version $Id: XPathMatcher.java,v 1.1 2007/06/02 09:58:59 paul Exp $
 */
public class XPathMatcher {

    /** Compile to true to debug everything. */
    protected static final boolean DEBUG_ALL = false;

    /** Compile to true to debug method callbacks. */
    protected static final boolean DEBUG_METHODS = false || DEBUG_ALL;

    /** Compile to true to debug important method callbacks. */
    protected static final boolean DEBUG_METHODS2 = false || DEBUG_METHODS || DEBUG_ALL;

    /** Compile to true to debug the <em>really</em> important methods. */
    protected static final boolean DEBUG_METHODS3 = false || DEBUG_METHODS || DEBUG_ALL;

    /** Compile to true to debug match. */
    protected static final boolean DEBUG_MATCH = false || DEBUG_ALL;

    /** Compile to true to debug step index stack. */
    protected static final boolean DEBUG_STACK = false || DEBUG_ALL;

    /** Don't touch this value unless you add more debug constants. */
    protected static final boolean DEBUG_ANY = DEBUG_METHODS || DEBUG_METHODS2 || DEBUG_METHODS3 || DEBUG_MATCH || DEBUG_STACK;

    protected static final int MATCHED = 1;

    protected static final int MATCHED_ATTRIBUTE = 3;

    protected static final int MATCHED_DESCENDANT = 5;

    protected static final int MATCHED_DESCENDANT_PREVIOUS = 13;

    /** XPath location path. */
    private XPath.LocationPath[] fLocationPaths;

    /** True if XPath has been matched. */
    private int[] fMatched;

    /** The matching string. */
    protected Object fMatchedString;

    /** Integer stack of step indexes. */
    private IntStack[] fStepIndexes;

    /** Current step. */
    private int[] fCurrentStep;

    /**
     * No match depth. The value of this field will be zero while
     * matching is successful for the given xpath expression.
     */
    private int[] fNoMatchDepth;

    final QName fQName = new QName();

    /**
     * Constructs an XPath matcher that implements a document fragment
     * handler.
     *
     * @param xpath   The xpath.
     */
    public XPathMatcher(XPath xpath) {
        fLocationPaths = xpath.getLocationPaths();
        fStepIndexes = new IntStack[fLocationPaths.length];
        for (int i = 0; i < fStepIndexes.length; i++) fStepIndexes[i] = new IntStack();
        fCurrentStep = new int[fLocationPaths.length];
        fNoMatchDepth = new int[fLocationPaths.length];
        fMatched = new int[fLocationPaths.length];
    }

    /** 
     * Returns value of first member of fMatched that
     * is nonzero.  
     */
    public boolean isMatched() {
        for (int i = 0; i < fLocationPaths.length; i++) if (((fMatched[i] & MATCHED) == MATCHED) && ((fMatched[i] & MATCHED_DESCENDANT_PREVIOUS) != MATCHED_DESCENDANT_PREVIOUS) && ((fNoMatchDepth[i] == 0) || ((fMatched[i] & MATCHED_DESCENDANT) == MATCHED_DESCENDANT))) return true;
        return false;
    }

    protected void handleContent(XSTypeDefinition type, boolean nillable, Object value, short valueType, ShortList itemValueType) {
    }

    /**
     * This method is called when the XPath handler matches the
     * XPath expression. Subclasses can override this method to
     * provide default handling upon a match.
     */
    protected void matched(Object actualValue, short valueType, ShortList itemValueType, boolean isNil) {
        if (DEBUG_METHODS3) {
            System.out.println(toString() + "#matched(\"" + actualValue + "\")");
        }
    }

    /**
     * The start of the document fragment.
     */
    public void startDocumentFragment() {
        if (DEBUG_METHODS) {
            System.out.println(toString() + "#startDocumentFragment(" + ")");
        }
        fMatchedString = null;
        for (int i = 0; i < fLocationPaths.length; i++) {
            fStepIndexes[i].clear();
            fCurrentStep[i] = 0;
            fNoMatchDepth[i] = 0;
            fMatched[i] = 0;
        }
    }

    /**
     * The start of an element. If the document specifies the start element
     * by using an empty tag, then the startElement method will immediately
     * be followed by the endElement method, with no intervening methods.
     *
     * @param element    The name of the element.
     * @param attributes The element attributes.
     *
     * @throws SAXException Thrown by handler to signal an error.
     */
    public void startElement(QName element, XMLAttributes attributes) {
        if (DEBUG_METHODS2) {
            System.out.println(toString() + "#startElement(" + "element={" + element + "}," + "attributes=..." + attributes + ")");
        }
        for (int i = 0; i < fLocationPaths.length; i++) {
            int startStep = fCurrentStep[i];
            fStepIndexes[i].push(startStep);
            if ((fMatched[i] & MATCHED_DESCENDANT) == MATCHED || fNoMatchDepth[i] > 0) {
                fNoMatchDepth[i]++;
                continue;
            }
            if ((fMatched[i] & MATCHED_DESCENDANT) == MATCHED_DESCENDANT) {
                fMatched[i] = MATCHED_DESCENDANT_PREVIOUS;
            }
            if (DEBUG_STACK) {
                System.out.println(toString() + ": " + fStepIndexes[i]);
            }
            XPath.Step[] steps = fLocationPaths[i].steps;
            while (fCurrentStep[i] < steps.length && steps[fCurrentStep[i]].axis.type == XPath.Axis.SELF) {
                if (DEBUG_MATCH) {
                    XPath.Step step = steps[fCurrentStep[i]];
                    System.out.println(toString() + " [SELF] MATCHED!");
                }
                fCurrentStep[i]++;
            }
            if (fCurrentStep[i] == steps.length) {
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " XPath MATCHED!");
                }
                fMatched[i] = MATCHED;
                continue;
            }
            int descendantStep = fCurrentStep[i];
            while (fCurrentStep[i] < steps.length && steps[fCurrentStep[i]].axis.type == XPath.Axis.DESCENDANT) {
                if (DEBUG_MATCH) {
                    XPath.Step step = steps[fCurrentStep[i]];
                    System.out.println(toString() + " [DESCENDANT] MATCHED!");
                }
                fCurrentStep[i]++;
            }
            boolean sawDescendant = fCurrentStep[i] > descendantStep;
            if (fCurrentStep[i] == steps.length) {
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " XPath DIDN'T MATCH!");
                }
                fNoMatchDepth[i]++;
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " [CHILD] after NO MATCH");
                }
                continue;
            }
            if ((fCurrentStep[i] == startStep || fCurrentStep[i] > descendantStep) && steps[fCurrentStep[i]].axis.type == XPath.Axis.CHILD) {
                XPath.Step step = steps[fCurrentStep[i]];
                XPath.NodeTest nodeTest = step.nodeTest;
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " [CHILD] before");
                }
                if (!matches(nodeTest, element)) {
                    if (fCurrentStep[i] > descendantStep) {
                        fCurrentStep[i] = descendantStep;
                        continue;
                    }
                    fNoMatchDepth[i]++;
                    if (DEBUG_MATCH) {
                        System.out.println(toString() + " [CHILD] after NO MATCH");
                    }
                    continue;
                }
                fCurrentStep[i]++;
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " [CHILD] after MATCHED!");
                }
            }
            if (fCurrentStep[i] == steps.length) {
                if (sawDescendant) {
                    fCurrentStep[i] = descendantStep;
                    fMatched[i] = MATCHED_DESCENDANT;
                } else {
                    fMatched[i] = MATCHED;
                }
                continue;
            }
            if (fCurrentStep[i] < steps.length && steps[fCurrentStep[i]].axis.type == XPath.Axis.ATTRIBUTE) {
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " [ATTRIBUTE] before");
                }
                int attrCount = attributes.getLength();
                if (attrCount > 0) {
                    XPath.NodeTest nodeTest = steps[fCurrentStep[i]].nodeTest;
                    for (int aIndex = 0; aIndex < attrCount; aIndex++) {
                        attributes.getName(aIndex, fQName);
                        if (matches(nodeTest, fQName)) {
                            fCurrentStep[i]++;
                            if (fCurrentStep[i] == steps.length) {
                                fMatched[i] = MATCHED_ATTRIBUTE;
                                int j = 0;
                                for (; j < i && ((fMatched[j] & MATCHED) != MATCHED); j++) ;
                                if (j == i) {
                                    AttributePSVI attrPSVI = (AttributePSVI) attributes.getAugmentations(aIndex).getItem(Constants.ATTRIBUTE_PSVI);
                                    fMatchedString = attrPSVI.getActualNormalizedValue();
                                    matched(fMatchedString, attrPSVI.getActualNormalizedValueType(), attrPSVI.getItemValueTypes(), false);
                                }
                            }
                            break;
                        }
                    }
                }
                if ((fMatched[i] & MATCHED) != MATCHED) {
                    if (fCurrentStep[i] > descendantStep) {
                        fCurrentStep[i] = descendantStep;
                        continue;
                    }
                    fNoMatchDepth[i]++;
                    if (DEBUG_MATCH) {
                        System.out.println(toString() + " [ATTRIBUTE] after");
                    }
                    continue;
                }
                if (DEBUG_MATCH) {
                    System.out.println(toString() + " [ATTRIBUTE] after MATCHED!");
                }
            }
        }
    }

    /**
       * @param element
       *        name of the element.
       * @param type
       *        content type of this element. IOW, the XML schema type
       *        of the <tt>value</tt>. Note that this may not be the type declared
       *        in the element declaration, but it is "the actual type". For example,
       *        if the XML is &lt;foo xsi:type="xs:string">aaa&lt;/foo>, this
       *        parameter will be "xs:string".
       * @param nillable - nillable
       *        true if the element declaration is nillable.
       * @param value - actual value
       *        the typed value of the content of this element. 
       */
    public void endElement(QName element, XSTypeDefinition type, boolean nillable, Object value, short valueType, ShortList itemValueType) {
        if (DEBUG_METHODS2) {
            System.out.println(toString() + "#endElement(" + "element={" + element + "}," + ")");
        }
        for (int i = 0; i < fLocationPaths.length; i++) {
            fCurrentStep[i] = fStepIndexes[i].pop();
            if (fNoMatchDepth[i] > 0) {
                fNoMatchDepth[i]--;
            } else {
                int j = 0;
                for (; j < i && ((fMatched[j] & MATCHED) != MATCHED); j++) ;
                if ((j < i) || (fMatched[j] == 0) || ((fMatched[j] & MATCHED_ATTRIBUTE) == MATCHED_ATTRIBUTE)) {
                    continue;
                }
                handleContent(type, nillable, value, valueType, itemValueType);
                fMatched[i] = 0;
            }
            if (DEBUG_STACK) {
                System.out.println(toString() + ": " + fStepIndexes[i]);
            }
        }
    }

    /** Returns a string representation of this object. */
    public String toString() {
        StringBuffer str = new StringBuffer();
        String s = super.toString();
        int index2 = s.lastIndexOf('.');
        if (index2 != -1) {
            s = s.substring(index2 + 1);
        }
        str.append(s);
        for (int i = 0; i < fLocationPaths.length; i++) {
            str.append('[');
            XPath.Step[] steps = fLocationPaths[i].steps;
            for (int j = 0; j < steps.length; j++) {
                if (j == fCurrentStep[i]) {
                    str.append('^');
                }
                str.append(steps[j].toString());
                if (j < steps.length - 1) {
                    str.append('/');
                }
            }
            if (fCurrentStep[i] == steps.length) {
                str.append('^');
            }
            str.append(']');
            str.append(',');
        }
        return str.toString();
    }

    /** Normalizes text. */
    private String normalize(String s) {
        StringBuffer str = new StringBuffer();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            switch(c) {
                case '\n':
                    {
                        str.append("\\n");
                        break;
                    }
                default:
                    {
                        str.append(c);
                    }
            }
        }
        return str.toString();
    }

    /** Returns true if the given QName matches the node test. **/
    private static boolean matches(XPath.NodeTest nodeTest, QName value) {
        if (nodeTest.type == XPath.NodeTest.QNAME) {
            return nodeTest.name.equals(value);
        }
        if (nodeTest.type == XPath.NodeTest.NAMESPACE) {
            return nodeTest.name.uri == value.uri;
        }
        return true;
    }
}
