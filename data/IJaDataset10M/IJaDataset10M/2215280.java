package net.sf.cb2xml;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import net.sf.cb2xml.def.BasicNumericDefinition;
import net.sf.cb2xml.def.NumericDefinition;
import net.sf.cb2xml.sablecc.analysis.DepthFirstAdapter;
import net.sf.cb2xml.sablecc.node.ABinaryUsagePhrase;
import net.sf.cb2xml.sablecc.node.ABlankWhenZeroClause;
import net.sf.cb2xml.sablecc.node.ABlankWhenZeroClauseClause;
import net.sf.cb2xml.sablecc.node.AComp1UsagePhrase;
import net.sf.cb2xml.sablecc.node.AComp2UsagePhrase;
import net.sf.cb2xml.sablecc.node.AComp3UsagePhrase;
import net.sf.cb2xml.sablecc.node.AComp4UsagePhrase;
import net.sf.cb2xml.sablecc.node.AComp5UsagePhrase;
import net.sf.cb2xml.sablecc.node.AComp6UsagePhrase;
import net.sf.cb2xml.sablecc.node.ACompUsagePhrase;
import net.sf.cb2xml.sablecc.node.ADisplay1UsagePhrase;
import net.sf.cb2xml.sablecc.node.ADisplayUsagePhrase;
import net.sf.cb2xml.sablecc.node.AFixedOccursFixedOrVariable;
import net.sf.cb2xml.sablecc.node.AFunctionPointerUsagePhrase;
import net.sf.cb2xml.sablecc.node.AIndexUsagePhrase;
import net.sf.cb2xml.sablecc.node.AItem;
import net.sf.cb2xml.sablecc.node.ALeadingLeadingOrTrailing;
import net.sf.cb2xml.sablecc.node.ANationalUsagePhrase;
import net.sf.cb2xml.sablecc.node.AObjectReferencePhrase;
import net.sf.cb2xml.sablecc.node.AOccursTo;
import net.sf.cb2xml.sablecc.node.APackedDecimalUsagePhrase;
import net.sf.cb2xml.sablecc.node.APictureClause;
import net.sf.cb2xml.sablecc.node.APointerUsagePhrase;
import net.sf.cb2xml.sablecc.node.AProcedurePointerUsagePhrase;
import net.sf.cb2xml.sablecc.node.ARecordDescription;
import net.sf.cb2xml.sablecc.node.ARedefinesClause;
import net.sf.cb2xml.sablecc.node.ASequenceLiteralSequence;
import net.sf.cb2xml.sablecc.node.ASignClause;
import net.sf.cb2xml.sablecc.node.ASingleLiteralSequence;
import net.sf.cb2xml.sablecc.node.ASynchronizedClauseClause;
import net.sf.cb2xml.sablecc.node.AThroughSequenceLiteralSequence;
import net.sf.cb2xml.sablecc.node.AThroughSingleLiteralSequence;
import net.sf.cb2xml.sablecc.node.ATrailingLeadingOrTrailing;
import net.sf.cb2xml.sablecc.node.AValueClause;
import net.sf.cb2xml.sablecc.node.AValueItem;
import net.sf.cb2xml.sablecc.node.AVariableOccursFixedOrVariable;
import net.sf.cb2xml.sablecc.node.TAlphanumericLiteral;
import net.sf.cb2xml.sablecc.node.THighValues;
import net.sf.cb2xml.sablecc.node.TLowValues;
import net.sf.cb2xml.sablecc.node.TNulls;
import net.sf.cb2xml.sablecc.node.TNumber88;
import net.sf.cb2xml.sablecc.node.TNumberNot88;
import net.sf.cb2xml.sablecc.node.TQuotes;
import net.sf.cb2xml.sablecc.node.TSpaces;
import net.sf.cb2xml.sablecc.node.TZeros;
import net.sf.cb2xml.sablecc.node.Token;
import net.sf.cb2xml.sablecc.parser.Parser;
import net.sf.cb2xml.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Main logic for translating the parse tree of SableCC into XML.
 * Currently the XML element and attribute names are hardcoded.
 *
 * All the inA* methods are fired when the corresponding node is "visited".
 * Each node name corresponds to each "production" etc. within the grammar file.
 *
 * The "tree walking" approach generates the XML DOM in a very inituitive manner.
 *
 * @author Peter Thomas
 *
 * @version .91a Bruce Martin
 * <p>
 * <ol compact>
 * <li>Change ---... to ===... in comments
 * <li>Increment Position when initial level>01
 * <li>Convert Picture to uppercase (unlike java, Cobol is not case sensitive)
 * <li>Added storage-length (actual field length in bytes) based on Mainframe
 *     sizes
 * </ul>
 * @version .92a Jean-Francois Gagnon
 * <p>
 * <ol compact>
 * <li>Implemented Bruce Martin's changes in version 0.92
 * <li>Added a sign-position=("trailing"|"leading") attribute
 * <li>Fixed the Sign Separate size computation (changes the storage length only)
 * <li>Made the CopyBookAnalyzer class constructor public
 * </ul>
 * @version .95 Bruce Martin<ol>
 * <li>Adding new interface NumericDefinition to encapsulate binary size / position calculation
 * <li>Adding support for PC Cobol's,
 * <li>Adding size calculation for comp-1, comp-2.
 * <li>Adding support for comp-6
 * <li>Ensure comp-1 / comp-2 fields are tagged as numeric. Comp-1 / 2 fields are floating point
 * numbers and do not have a picture clause i.e.
 *      03 float                 comp-1.
 *      03 double             comp-2.
 * <li>Changing position calculation for sync verb.
 * <li>Add blank-when-zero attribute to xml
 * </ol>
 */
public class CopyBookAnalyzer extends DepthFirstAdapter {

    private static int[] DEFAULT_SYNC = { 2, 2, 4, 4 };

    private static int[] tmpSizesUsed = { 2, 4, 8 };

    private static NumericDefinition numDef = new BasicNumericDefinition("", tmpSizesUsed, DEFAULT_SYNC, false, 4, 4);

    public CopyBookAnalyzer(String copyBookName, Parser parser) {
        this.copyBookName = copyBookName;
        this.parser = parser;
    }

    private Parser parser;

    private String copyBookName;

    private Document document;

    private Item prevItem, curItem;

    class Item {

        int level;

        Element element;

        Item(int level, String name) {
            this.level = level;
            element = document.createElement("item");
            element.setAttribute("level", new DecimalFormat("00").format(level));
            element.setAttribute("name", name);
        }

        Item() {
        }
    }

    public Document getDocument() {
        return document;
    }

    public void inARecordDescription(ARecordDescription node) {
        document = XmlUtils.getNewXmlDocument();
        Element root = document.createElement("copybook");
        root.setAttribute("filename", copyBookName);
        document.appendChild(root);
    }

    public void outARecordDescription(ARecordDescription node) {
        Element el;
        int lastPos = 1;
        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            org.w3c.dom.Node testNode = nodeList.item(i);
            if (testNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                el = (Element) nodeList.item(i);
                if ("01".equals(el.getAttribute("level"))) {
                    postProcessNode(el, 1);
                } else {
                    lastPos = postProcessNode(el, lastPos);
                }
            }
        }
    }

    public void caseTNumberNot88(TNumberNot88 node) {
        checkForComments(node);
    }

    public void caseTNumber88(TNumber88 node) {
        checkForComments(node);
    }

    public void checkForComments(Token node) {
        List list = (List) parser.ignoredTokens.getIn(node);
        if (list != null) {
            Iterator i = list.iterator();
            while (i.hasNext()) {
                String s = i.next().toString().trim();
                if (s.length() > 1) {
                    curItem.element.getParentNode().insertBefore(document.createComment(correctForMinus(s)), curItem.element);
                }
            }
        }
    }

    /**
     * Replace '-' chars with '=' to aviod invalid XML comments
     *
     * @param s input string Comment
     * @return corrected comment
     */
    private String correctForMinus(String s) {
        int start = s.indexOf("--");
        if (start >= 0) {
            int i = start;
            StringBuffer buf = new StringBuffer(s);
            while (i < s.length() && buf.charAt(i) == '-') {
                buf.replace(i, i + 1, "=");
                i += 1;
            }
            s = buf.toString();
        }
        return s;
    }

    public void inAItem(AItem node) {
        int level = Integer.parseInt(node.getNumberNot88().toString().trim());
        String name = node.getDataNameOrFiller().toString().trim();
        curItem = new Item(level, name);
        if (level <= 77) {
            if (prevItem == null) {
                document.getDocumentElement().appendChild(curItem.element);
            } else if (curItem.level > prevItem.level) {
                prevItem.element.appendChild(curItem.element);
            } else if (curItem.level == prevItem.level) {
                prevItem.element.getParentNode().appendChild(curItem.element);
            } else if (curItem.level < prevItem.level) {
                Element tempElement = prevItem.element;
                while (true) {
                    tempElement = (Element) tempElement.getParentNode();
                    String tempLevel = tempElement.getAttribute("level");
                    if ("".equals(tempLevel)) {
                        tempElement.appendChild(curItem.element);
                        break;
                    }
                    int tempLevelNumber = Integer.parseInt(tempLevel);
                    if (tempLevelNumber == curItem.level) {
                        tempElement.getParentNode().appendChild(curItem.element);
                        break;
                    } else if (tempLevelNumber < curItem.level) {
                        tempElement.appendChild(curItem.element);
                        break;
                    }
                }
            }
            prevItem = curItem;
        }
    }

    public void inARedefinesClause(ARedefinesClause node) {
        String dataName = node.getDataName().getText();
        curItem.element.setAttribute("redefines", dataName);
    }

    public void inAFixedOccursFixedOrVariable(AFixedOccursFixedOrVariable node) {
        curItem.element.setAttribute("occurs", node.getNumber().toString().trim());
    }

    public void inAVariableOccursFixedOrVariable(AVariableOccursFixedOrVariable node) {
        curItem.element.setAttribute("occurs", node.getNumber().toString().trim());
        curItem.element.setAttribute("depending-on", node.getDataName().getText());
    }

    public void inAOccursTo(AOccursTo node) {
        curItem.element.setAttribute("occurs-min", node.getNumber().toString().trim());
    }

    public void inASynchronizedClauseClause(ASynchronizedClauseClause node) {
        curItem.element.setAttribute("sync", "true");
    }

    public void inAPictureClause(APictureClause node) {
        boolean positive = true;
        String characterString = removeChars(node.getCharacterString().toString(), " ");
        curItem.element.setAttribute("picture", characterString);
        if (characterString.charAt(0) == 'S' || characterString.charAt(0) == 's') {
            curItem.element.setAttribute("signed", "true");
            characterString = characterString.substring(1);
            positive = false;
        }
        int displayLength = 0, storageLength = 0;
        if (curItem.element.hasAttribute("display-length")) {
            displayLength = Integer.parseInt(curItem.element.getAttribute("display-length"));
        }
        if (curItem.element.hasAttribute("storage-length")) {
            storageLength = Integer.parseInt(curItem.element.getAttribute("storage-length"));
        }
        int decimalPos = -1;
        boolean isNumeric = false;
        boolean isFirstCurrencySymbol = true;
        String ucCharacterString = characterString.toUpperCase();
        for (int i = 0; i < characterString.length(); i++) {
            char c = ucCharacterString.charAt(i);
            switch(c) {
                case 'A':
                case 'B':
                case 'E':
                    storageLength++;
                case 'G':
                case 'N':
                    storageLength++;
                    displayLength++;
                    break;
                case '.':
                    displayLength++;
                case 'V':
                    isNumeric = true;
                    decimalPos = displayLength;
                    break;
                case 'P':
                    if (characterString.charAt(0) == 'P') {
                        decimalPos = 0;
                    }
                    isNumeric = true;
                    displayLength++;
                    break;
                case '$':
                    if (isFirstCurrencySymbol) {
                        isFirstCurrencySymbol = false;
                        isNumeric = true;
                    } else {
                        displayLength++;
                    }
                    break;
                case 'C':
                case 'D':
                    i++;
                case 'Z':
                case '9':
                case '0':
                case '+':
                case '-':
                case '*':
                    isNumeric = true;
                case '/':
                case ',':
                case 'X':
                    displayLength++;
                    break;
                case '(':
                    int endParenPos = characterString.indexOf(')', i + 1);
                    int count = Integer.parseInt(characterString.substring(i + 1, endParenPos));
                    i = endParenPos;
                    displayLength = displayLength + count - 1;
            }
        }
        setLength(curItem.element, positive, displayLength);
        if (decimalPos != -1) {
            curItem.element.setAttribute("scale", displayLength - decimalPos + "");
            if (characterString.indexOf('.') != -1) {
                curItem.element.setAttribute("insert-decimal-point", "true");
            }
        }
        if (isNumeric) {
            curItem.element.setAttribute("numeric", "true");
        }
    }

    public void inASignClause(ASignClause node) {
        if (node.getSeparateCharacter() != null) {
            curItem.element.setAttribute("sign-separate", "true");
        }
    }

    public void inALeadingLeadingOrTrailing(ALeadingLeadingOrTrailing node) {
        curItem.element.setAttribute("sign-position", "leading");
    }

    public void inATrailingLeadingOrTrailing(ATrailingLeadingOrTrailing node) {
        curItem.element.setAttribute("sign-position", "trailing");
    }

    /**
	 * @see net.sf.cb2xml.sablecc.analysis.DepthFirstAdapter#inABlankWhenZeroClause(net.sf.cb2xml.sablecc.node.ABlankWhenZeroClause)
	 */
    public void inABlankWhenZeroClause(ABlankWhenZeroClause node) {
        curItem.element.setAttribute("blank-when-zero", "true");
        super.inABlankWhenZeroClause(node);
    }

    /**
	 * @see net.sf.cb2xml.sablecc.analysis.DepthFirstAdapter#inABlankWhenZeroClauseClause(net.sf.cb2xml.sablecc.node.ABlankWhenZeroClauseClause)
	 */
    public void inABlankWhenZeroClauseClause(ABlankWhenZeroClauseClause node) {
        curItem.element.setAttribute("blank-when-zero", "true");
        super.inABlankWhenZeroClauseClause(node);
    }

    public void inABinaryUsagePhrase(ABinaryUsagePhrase node) {
        curItem.element.setAttribute("usage", "binary");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inACompUsagePhrase(ACompUsagePhrase node) {
        curItem.element.setAttribute("usage", "computational");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inAComp1UsagePhrase(AComp1UsagePhrase node) {
        curItem.element.setAttribute("usage", "computational-1");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inAComp2UsagePhrase(AComp2UsagePhrase node) {
        curItem.element.setAttribute("usage", "computational-2");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inAComp3UsagePhrase(AComp3UsagePhrase node) {
        curItem.element.setAttribute("usage", "computational-3");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inAComp4UsagePhrase(AComp4UsagePhrase node) {
        curItem.element.setAttribute("usage", "computational-4");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inAComp5UsagePhrase(AComp5UsagePhrase node) {
        curItem.element.setAttribute("usage", "computational-5");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inAComp6UsagePhrase(AComp6UsagePhrase node) {
        curItem.element.setAttribute("usage", "computational-6");
        curItem.element.setAttribute("numeric", "true");
    }

    public void inADisplayUsagePhrase(ADisplayUsagePhrase node) {
        curItem.element.setAttribute("usage", "display");
    }

    public void inADisplay1UsagePhrase(ADisplay1UsagePhrase node) {
        curItem.element.setAttribute("usage", "display-1");
    }

    public void inAIndexUsagePhrase(AIndexUsagePhrase node) {
        curItem.element.setAttribute("usage", "index");
    }

    public void inANationalUsagePhrase(ANationalUsagePhrase node) {
        curItem.element.setAttribute("usage", "national");
    }

    public void inAObjectReferencePhrase(AObjectReferencePhrase node) {
        curItem.element.setAttribute("object-reference", node.getDataName().getText());
    }

    public void inAPackedDecimalUsagePhrase(APackedDecimalUsagePhrase node) {
        curItem.element.setAttribute("usage", "packed-decimal");
    }

    public void inAPointerUsagePhrase(APointerUsagePhrase node) {
        curItem.element.setAttribute("usage", "pointer");
    }

    public void inAProcedurePointerUsagePhrase(AProcedurePointerUsagePhrase node) {
        curItem.element.setAttribute("usage", "procedure-pointer");
    }

    public void inAFunctionPointerUsagePhrase(AFunctionPointerUsagePhrase node) {
        curItem.element.setAttribute("usage", "function-pointer");
    }

    public void caseTZeros(TZeros node) {
        node.setText("zeros");
    }

    public void caseTSpaces(TSpaces node) {
        node.setText("spaces");
    }

    public void caseTHighValues(THighValues node) {
        node.setText("high-values");
    }

    public void caseTLowValues(TLowValues node) {
        node.setText("low-values");
    }

    public void caseTQuotes(TQuotes node) {
        node.setText("quotes");
    }

    public void caseTNulls(TNulls node) {
        node.setText("nulls");
    }

    public void caseTAlphanumericLiteral(TAlphanumericLiteral node) {
        String nodeText = node.getText();
        if (nodeText.startsWith("X")) {
            node.setText(nodeText.replace('\"', '\''));
        } else {
            if (nodeText.indexOf("\"") != -1) {
                node.setText(removeChars(nodeText, "\""));
            } else {
                node.setText(removeChars(nodeText, "'"));
            }
        }
    }

    public void outAValueClause(AValueClause node) {
        curItem.element.setAttribute("value", node.getLiteral().toString().trim());
    }

    public void inAValueItem(AValueItem node) {
        String name = node.getDataName().getText();
        curItem = new Item();
        curItem.element = document.createElement("condition");
        curItem.element.setAttribute("name", name);
        prevItem.element.appendChild(curItem.element);
    }

    public void outASingleLiteralSequence(ASingleLiteralSequence node) {
        if (node.getAll() != null) {
            curItem.element.setAttribute("all", "true");
        }
        Element element = document.createElement("condition");
        element.setAttribute("value", node.getLiteral().toString().trim());
        curItem.element.appendChild(element);
    }

    public void outASequenceLiteralSequence(ASequenceLiteralSequence node) {
        Element element = document.createElement("condition");
        element.setAttribute("value", node.getLiteral().toString().trim());
        curItem.element.appendChild(element);
    }

    public void outAThroughSingleLiteralSequence(AThroughSingleLiteralSequence node) {
        Element element = document.createElement("condition");
        element.setAttribute("value", node.getFrom().toString().trim());
        element.setAttribute("through", node.getTo().toString().trim());
        curItem.element.appendChild(element);
    }

    public void outAThroughSequenceLiteralSequence(AThroughSequenceLiteralSequence node) {
        Element element = document.createElement("condition");
        element.setAttribute("value", node.getFrom().toString().trim());
        element.setAttribute("through", node.getTo().toString().trim());
        curItem.element.appendChild(element);
    }

    private String removeChars(String s, String charToRemove) {
        StringTokenizer st = new StringTokenizer(s, charToRemove, false);
        StringBuffer b = new StringBuffer();
        while (st.hasMoreElements()) {
            b.append(st.nextElement());
        }
        return b.toString();
    }

    /**
	 * This is for DOM post-processing of the XML before saving to resolve the field lengths
	 * of each node and also calculate the start position of the data field in the
	 * raw copybook buffer (mainframe equivalent)
	 * recursive traversal.  note that REDEFINES declarations are taken care of
	 * as well as the OCCURS declarations
	 */
    private int postProcessNode(Element element, int startPos) {
        int actualLength = 0;
        int displayLength = 0;
        int newPos;
        String usage = "";
        if (element.hasAttribute("usage")) {
            usage = element.getAttribute("usage");
        }
        if (element.hasAttribute("redefines")) {
            String redefinedName = element.getAttribute("redefines");
            Element redefinedElement = null;
            NodeList nodeList = document.getDocumentElement().getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element testElement = (Element) nodeList.item(i);
                if (testElement.getAttribute("name").equalsIgnoreCase(redefinedName)) {
                    redefinedElement = testElement;
                    break;
                }
            }
            if (redefinedElement != null && redefinedElement.hasAttribute("position")) {
                startPos = Integer.parseInt(redefinedElement.getAttribute("position"));
                redefinedElement.setAttribute("redefined", "true");
            } else {
                System.out.println(">> position error " + element.getAttribute("name") + " %% " + redefinedName);
            }
        }
        newPos = startPos;
        if (element.hasAttribute("display-length")) {
            displayLength = Integer.parseInt(element.getAttribute("display-length"));
        } else {
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                org.w3c.dom.Node testNode = nodeList.item(i);
                if (testNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    Element childElement = (Element) testNode;
                    if (!childElement.getTagName().equals("condition")) {
                        newPos = postProcessNode(childElement, newPos);
                    }
                }
                displayLength = newPos - startPos;
            }
        }
        actualLength = setLength(element, !"true".equals(element.getAttribute("signed")), displayLength);
        int syncOn = 1;
        int remainder;
        if (element.hasAttribute("sync")) {
            syncOn = numDef.getSyncAt(usage, actualLength);
            remainder = (startPos - 1) % syncOn;
            if (remainder > 0) {
                startPos = startPos - remainder + syncOn;
            }
        }
        element.setAttribute("position", startPos + "");
        if (element.hasAttribute("occurs")) {
            actualLength *= Integer.parseInt(element.getAttribute("occurs"));
        }
        return startPos + actualLength;
    }

    /**
	 * Assigning display and actual length to current element
	 *
	 */
    private int setLength(Element element, boolean positive, int displayLength) {
        int storageLength = displayLength;
        if (element.hasAttribute("usage")) {
            if (numDef != null) {
                String usage = element.getAttribute("usage");
                displayLength = numDef.chkStorageLength(storageLength, usage);
                storageLength = numDef.getBinarySize(usage, displayLength, positive, element.hasAttribute("sync"));
            }
        } else if (element.hasAttribute("sign-separate") && "true".equalsIgnoreCase(element.getAttribute("sign-separate"))) {
            storageLength += 1;
            displayLength += 1;
        }
        element.setAttribute("display-length", displayLength + "");
        element.setAttribute("storage-length", storageLength + "");
        return storageLength;
    }

    /**
	 * Set the possible Sizes for Comp fields
	 * @param numericDef numeric definition class
	 */
    public static void setNumericDetails(NumericDefinition numericDef) {
        numDef = numericDef;
    }
}
