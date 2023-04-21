package gqtiv2;

public class OperatorsHandler {

    private String[] returnValue;

    private org.w3c.dom.Node theElement;

    private org.w3c.dom.Node thenode;

    private String myNodeName;

    private String parentNode;

    private DocumentDataStore thedataStore;

    private String errstr;

    private boolean strict = true;

    private String debug;

    public OperatorsHandler(org.w3c.dom.Node theNode, String pNode, DocumentDataStore ds) {
        thedataStore = ds;
        theElement = theNode;
        parentNode = pNode;
        myNodeName = theElement.getNodeName();
    }

    public String[] run(String[] Parameters) throws gqtiexcept.XMLException {
        try {
            String[] outputValue;
            String typeinfo = "";
            String firsttype = "";
            String type;
            String firstcardinality;
            String cardinality;
            debug = thedataStore.getProperty("debug");
            if (debug.equals("ON")) System.out.println("***************Operator: " + myNodeName + "***********");
            if (thedataStore.getProperty("strictType").equals("ON")) strict = true; else strict = false;
            if (myNodeName.equals("and")) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                outputValue[1] = "TRUE";
                typeinfo = "";
                for (int i = 0; i < childElementCount; i++) {
                    thenode = qtiv2utils.getChildElement(theElement, i);
                    DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                    returnValue = parser.parseDocument(thenode, Parameters);
                    if (returnValue == null) return null;
                    if (returnValue[1] == null) return null;
                    typeinfo = returnValue[0];
                    if (strict) qtiv2utils.typeCheck(thedataStore, "boolean", "single", typeinfo, myNodeName, debug);
                    if (returnValue[1].equals("FALSE")) {
                        outputValue[1] = "FALSE";
                        break;
                    }
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("anyN")) {
                String min = qtiv2utils.selectAttribute(theElement, "min", "");
                String max = qtiv2utils.selectAttribute(theElement, "max", "");
                int minno = Integer.parseInt(min);
                int maxno = Integer.parseInt(max);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                outputValue[1] = "NULL";
                int minNo = Integer.parseInt(min);
                int maxNo = Integer.parseInt(max);
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                int numbertrue = 0;
                int numberfalse = 0;
                int numbernull = 0;
                for (int i = 0; i < childElementCount; i++) {
                    thenode = qtiv2utils.getChildElement(theElement, i);
                    DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                    returnValue = parser.parseDocument(thenode, Parameters);
                    if (returnValue != null) {
                        typeinfo = returnValue[0];
                        if (strict) qtiv2utils.typeCheck(thedataStore, "boolean", "single", typeinfo, myNodeName, debug);
                        if (returnValue[1].equals("TRUE")) numbertrue++;
                        if (returnValue[1].equals("FALSE")) numberfalse++;
                    }
                }
                numbernull = childElementCount - (numbertrue + numberfalse);
                if ((numbertrue >= minno) && ((numbertrue + numbernull) <= maxno)) outputValue[1] = "TRUE"; else if ((numberfalse > (childElementCount - minno)) || (numbertrue > maxno)) outputValue[1] = "FALSE"; else return null;
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("contains")) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                typeinfo = "";
                String[] soughtValues;
                String[] memberValues;
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                boolean found = false;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                memberValues = parser.parseDocument(thenode, Parameters);
                if (memberValues == null) return null;
                if (debug.equals("ON")) for (int a = 0; a < memberValues.length; a++) System.out.println("MEMBER " + a + ":" + memberValues[a]);
                typeinfo = memberValues[0];
                firsttype = typeinfo.substring(0, typeinfo.indexOf(":"));
                firstcardinality = typeinfo.substring(typeinfo.indexOf(":") + 1);
                if (strict) qtiv2utils.typeCheck(thedataStore, "any", "multiple/ordered", typeinfo, myNodeName, debug);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                soughtValues = parser2.parseDocument(thenode, Parameters);
                if (soughtValues == null) return null;
                if (debug.equals("ON")) for (int b = 0; b < soughtValues.length; b++) System.out.println("SOUGHT " + b + ":" + soughtValues[b]);
                typeinfo = soughtValues[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, firsttype, firstcardinality, typeinfo, myNodeName, debug);
                if (firstcardinality.equals("ordered")) {
                    if (debug.equals("ON")) System.out.println("ORDERED CONTAINS TEST");
                    outputValue[1] = "FALSE";
                    for (int i = 1; i < memberValues.length; i++) {
                        if (memberValues[i].equals(soughtValues[1])) {
                            if (debug.equals("ON")) System.out.println("FOUND FIRST SOUGHT " + soughtValues[1]);
                            outputValue[1] = "TRUE";
                            for (int i2 = 2; i2 < soughtValues.length; i2++) {
                                if (debug.equals("ON")) System.out.println("LOOKING FOR  " + soughtValues[i2]);
                                i++;
                                if ((i > memberValues.length) || (!soughtValues[i2].equals(memberValues[i]))) {
                                    outputValue[1] = "FALSE";
                                    if (debug.equals("ON")) System.out.println("NOT FOUND");
                                    return outputValue;
                                }
                            }
                        }
                    }
                } else {
                    if (debug.equals("ON")) System.out.println("NOT ORDERED CONTAINS TEST");
                    for (int i = 1; i < soughtValues.length; i++) {
                        if (debug.equals("ON")) System.out.println("LOOKING FOR " + soughtValues[i]);
                        found = false;
                        for (int i2 = 1; i2 < memberValues.length; i2++) {
                            if (debug.equals("ON")) System.out.println("MEMBER " + memberValues[i2]);
                            if (soughtValues[i].equals(memberValues[i2])) {
                                if (debug.equals("ON")) System.out.println("FOUND");
                                found = true;
                                memberValues[i2] = null;
                                break;
                            }
                        }
                        if (!found) {
                            break;
                        }
                    }
                    if (found) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("delete")) {
                if (debug.equals("ON")) System.out.println("DELETION");
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                firsttype = typeinfo.substring(0, typeinfo.indexOf(":"));
                firstcardinality = typeinfo.substring(typeinfo.indexOf(":") + 1);
                if (strict) qtiv2utils.typeCheck(thedataStore, "any", "single", typeinfo, myNodeName, debug);
                String valueToDelete = returnValue[1];
                if (debug.equals("ON")) System.out.println("VALUE TO DELETE " + valueToDelete);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] containerValues = parser2.parseDocument(thenode, Parameters);
                if (containerValues == null) return null;
                typeinfo = containerValues[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, firsttype, "multiple", typeinfo, myNodeName, debug);
                java.util.ArrayList<String> remainder = new java.util.ArrayList<String>(10);
                for (int i = 0; i < containerValues.length; i++) {
                    if (debug.equals("ON")) System.out.println("CONTAINER VALUE " + i + " " + containerValues[i]);
                    if (containerValues[i] == null) {
                        remainder.add(null);
                        System.out.println("VALUE ADDED NULL");
                    } else if (!containerValues[i].equals(valueToDelete)) {
                        remainder.add(containerValues[i]);
                        if (debug.equals("ON")) System.out.println("VALUE ADDED " + containerValues[i]);
                    }
                }
                remainder.trimToSize();
                outputValue = new String[remainder.size()];
                remainder.toArray(outputValue);
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("divide")) {
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] dividendValue = parser.parseDocument(thenode, Parameters);
                if (dividendValue == null) return null;
                typeinfo = dividendValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer/float", "single", typeinfo, myNodeName, debug);
                double dividend = Double.parseDouble(dividendValue[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] divisorValue = parser2.parseDocument(thenode, Parameters);
                if (divisorValue == null) return null;
                typeinfo = divisorValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer/float", "single", typeinfo, myNodeName, debug);
                double divisor = Double.parseDouble(divisorValue[1]);
                double quotient = dividend / divisor;
                outputValue[0] = "float:single";
                outputValue[1] = Double.toString(quotient);
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if ((myNodeName.equals("durationGTE")) || (myNodeName.equals("durationLT"))) {
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] testedValue = parser.parseDocument(thenode, Parameters);
                if (testedValue == null) return null;
                typeinfo = testedValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "duration", "single", typeinfo, myNodeName, debug);
                double testValue = Double.parseDouble(testedValue[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] standardValue = parser2.parseDocument(thenode, Parameters);
                if (standardValue == null) return null;
                typeinfo = standardValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "duration", "single", typeinfo, myNodeName, debug);
                double standard = Double.parseDouble(standardValue[1]);
                if (myNodeName.equals("durationGTE")) {
                    if (testValue >= standard) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                }
                if (myNodeName.equals("durationLT")) {
                    if (testValue < standard) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("equal")) {
                String toleranceMode = qtiv2utils.selectAttribute(theElement, "toleranceMode", "exact");
                String tolerance = qtiv2utils.selectAttribute(theElement, "tolerance", "");
                double upperlimit = 0;
                double lowerlimit = 0;
                double firstVal;
                double secondVal;
                double difference;
                if (!toleranceMode.equals("exact")) {
                    if (tolerance.equals("")) {
                        errstr = "XML ERROR in &lt;" + myNodeName + "&gt;<br />" + " Tolerance must be specified";
                        throw new gqtiexcept.XMLException(errstr);
                    }
                    int pos = tolerance.indexOf(" ");
                    if (pos > -1) {
                        lowerlimit = Double.parseDouble(tolerance.substring(0, pos));
                        upperlimit = Double.parseDouble(tolerance.substring(pos + 1));
                    } else {
                        lowerlimit = Double.parseDouble(tolerance);
                        upperlimit = lowerlimit;
                    }
                }
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] firstValue = parser.parseDocument(thenode, Parameters);
                if (firstValue == null) return null;
                typeinfo = firstValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                firstVal = Double.parseDouble(firstValue[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] secondValue = parser2.parseDocument(thenode, Parameters);
                if (secondValue == null) return null;
                typeinfo = secondValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                secondVal = Double.parseDouble(secondValue[1]);
                difference = firstVal - secondVal;
                if (toleranceMode.equals("exact")) {
                    if (difference == 0) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                } else if (toleranceMode.equals("absolute")) {
                    if ((firstVal >= secondVal - lowerlimit) && (firstVal <= secondVal + lowerlimit)) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                } else if (toleranceMode.equals("relative")) {
                    if ((firstVal >= secondVal * (1 - lowerlimit / 100)) && (firstVal <= secondVal * (1 + upperlimit / 100))) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                }
                debug = thedataStore.getProperty("debug");
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("equalRounded")) {
                debug = thedataStore.getProperty("debug");
                String roundingMode = qtiv2utils.selectAttribute(theElement, "roundingMode", "significantFigures");
                String figures = qtiv2utils.selectAttribute(theElement, "figures", "");
                thenode = qtiv2utils.getChildElement(theElement, 0);
                double firstVal;
                double secondVal;
                String rounded1 = "";
                String rounded2 = "";
                String conversionLetter = "";
                String formatString = "";
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] firstValue = parser.parseDocument(thenode, Parameters);
                if (firstValue == null) return null;
                typeinfo = firstValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                firstVal = Double.parseDouble(firstValue[1]);
                if (debug.equals("ON")) System.out.println("equalRounded first value input" + firstVal);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] secondValue = parser2.parseDocument(thenode, Parameters);
                if (secondValue == null) return null;
                typeinfo = secondValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                secondVal = Double.parseDouble(secondValue[1]);
                if (debug.equals("ON")) System.out.println("equalRounded second value input" + secondVal);
                if (roundingMode.equals("significantFigures")) conversionLetter = "g"; else if (roundingMode.equals("decimalPlaces")) conversionLetter = "f"; else {
                    errstr = "XML ERROR in &lt;" + myNodeName + "&gt;<br />" + " roundingMode incorrectly specified";
                    throw new gqtiexcept.XMLException(errstr);
                }
                formatString = "%." + figures.trim() + conversionLetter;
                if (debug.equals("ON")) System.out.println("equalRounded formatString " + formatString);
                rounded1 = String.format(formatString, firstVal);
                if (debug.equals("ON")) System.out.println("equalRounded rounded1 " + rounded1);
                rounded2 = String.format(formatString, secondVal);
                if (debug.equals("ON")) System.out.println("equalRounded rounded2 " + rounded2);
                if (rounded1.equals(rounded2)) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if ((myNodeName.equals("gt")) || (myNodeName.equals("gte")) || (myNodeName.equals("lt")) || (myNodeName.equals("lte"))) {
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] firstValue = parser.parseDocument(thenode, Parameters);
                if (firstValue == null) return null;
                typeinfo = firstValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                double first = Double.parseDouble(firstValue[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] secondValue = parser.parseDocument(thenode, Parameters);
                if (secondValue == null) return null;
                typeinfo = secondValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                double second = Double.parseDouble(secondValue[1]);
                if (myNodeName.equals("gt")) if (first > second) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE"; else if (myNodeName.equals("gte")) if (first >= second) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE"; else if (myNodeName.equals("lt")) if (first < second) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE"; else if (myNodeName.equals("lte")) if (first <= second) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("fieldValue")) {
                String fieldIdentifier = qtiv2utils.selectAttribute(theElement, "fieldIdentifier", "");
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                outputValue = null;
                String[] collectedValues = parser.parseDocument(thenode, Parameters);
                for (int i = 1; i < collectedValues.length; i++) {
                    String variableValue = collectedValues[i];
                    java.util.StringTokenizer t = new java.util.StringTokenizer(variableValue, ":");
                    String baseType = t.nextToken().trim();
                    String ident = t.nextToken().trim();
                    String value = t.nextToken().trim();
                    if (ident.equals(fieldIdentifier)) {
                        outputValue = new String[2];
                        outputValue[0] = baseType + ":single";
                        outputValue[1] = value;
                        break;
                    }
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("index")) {
                String n = qtiv2utils.selectAttribute(theElement, "n", "");
                int no = Integer.parseInt(n);
                outputValue = new String[2];
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (no > (returnValue.length - 1)) return null;
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "any", "ordered", typeinfo, myNodeName, debug);
                String[] typedata = new String[2];
                typedata = typeinfo.split(":");
                outputValue[0] = typedata[0] + ":single";
                outputValue[1] = returnValue[no];
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("inside")) {
                String shape = qtiv2utils.selectAttribute(theElement, "shape", "");
                String coords = qtiv2utils.selectAttribute(theElement, "coords", "");
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                boolean found = false;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "point", "any", typeinfo, myNodeName, debug);
                for (int i = 0; i < returnValue.length; i++) {
                    String point = returnValue[i];
                    found = qtiv2utils.inside(point, coords, shape);
                    if (found) break;
                }
                if (found) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if ((myNodeName.equals("integerDivide")) || (myNodeName.equals("integerModules"))) {
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer", "single", typeinfo, myNodeName, debug);
                int dividend = Integer.parseInt(returnValue[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser2.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer", "single", typeinfo, myNodeName, debug);
                int divisor = Integer.parseInt(returnValue[1]);
                if (divisor == 0) return null;
                if (myNodeName.equals("integerDivide")) {
                    int quotient = Math.round(dividend / divisor);
                    outputValue[0] = "integer:single";
                    outputValue[1] = Integer.toString(quotient);
                }
                if (myNodeName.equals("integerModulus")) {
                    int modulus = dividend % divisor;
                    outputValue[0] = "integer:single";
                    outputValue[1] = Integer.toString(modulus);
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("integerToFloat")) {
                org.w3c.dom.Node thenode;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer", "single", typeinfo, myNodeName, debug);
                int value = Integer.parseInt(returnValue[1]);
                outputValue[0] = "float:single";
                outputValue[1] = Double.toString((double) value);
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("isNull")) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) {
                    outputValue[1] = "TRUE";
                } else {
                    outputValue[1] = "FALSE";
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("match")) {
                boolean matchresult;
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                int matchx;
                int matchy;
                String[] secondValue;
                String[] firstValue;
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                org.w3c.dom.Node thenode;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                firstValue = parser.parseDocument(thenode, Parameters);
                if (debug.equals("ON")) System.out.println("collected first value ");
                if (firstValue == null) return null;
                typeinfo = firstValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "any/!float/!duration", "any", typeinfo, myNodeName, debug);
                firsttype = typeinfo.substring(0, typeinfo.indexOf(":"));
                firstcardinality = typeinfo.substring(typeinfo.indexOf(":") + 1);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                secondValue = parser2.parseDocument(thenode, Parameters);
                if (debug.equals("ON")) System.out.println("collected second value");
                if (secondValue == null) return null;
                typeinfo = secondValue[0];
                if ((!(typeinfo.substring(0, typeinfo.indexOf(":")).equals(firsttype))) || (!(typeinfo.substring(typeinfo.indexOf(":") + 1).equals(firstcardinality))) || (!(firstValue.length == secondValue.length))) {
                    if (debug.equals("ON")) System.out.println("Type, Cardinality or length do not match ");
                    return null;
                }
                if (strict) qtiv2utils.typeCheck(thedataStore, firsttype, firstcardinality, typeinfo, myNodeName, debug);
                if (debug.equals("ON")) System.out.println("First Value Length " + firstValue.length);
                if (firstValue.length <= 1) {
                    outputValue[1] = "FALSE";
                    return outputValue;
                }
                if (debug.equals("ON")) System.out.println("Cardinality:" + firstcardinality);
                if (!(firstcardinality.equals("multiple"))) {
                    if (debug.equals("ON")) System.out.println("Cardinality" + firstValue.length);
                    matchresult = true;
                    for (int i = 1; i < firstValue.length; i++) {
                        String match1 = firstValue[i];
                        String match2 = secondValue[i];
                        if (debug.equals("ON")) System.out.println("Matching " + match1 + " with " + match2);
                        if (!(compare(match1, match2, firsttype))) {
                            matchresult = false;
                            break;
                        }
                    }
                    if (matchresult) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                    if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                    return outputValue;
                } else {
                    if (debug.equals("ON")) System.out.println("Doing multiple");
                    for (matchx = 1; matchx < firstValue.length; matchx++) {
                        matchresult = false;
                        String match1 = firstValue[matchx];
                        for (matchy = 1; matchy < firstValue.length; matchy++) {
                            String match2 = secondValue[matchy];
                            if (debug.equals("ON")) System.out.println("Comparing" + match1 + "with" + match2);
                            if (compare(match1, match2, firsttype)) {
                                matchresult = true;
                                if (debug.equals("ON")) System.out.println("Comparison result" + matchresult);
                                break;
                            }
                        }
                        if (!(matchresult)) {
                            outputValue[1] = "FALSE";
                            if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                            return outputValue;
                        }
                    }
                    if (debug.equals("ON")) System.out.println("Doing Reverse");
                    for (matchx = 1; matchx < firstValue.length; matchx++) {
                        matchresult = false;
                        String match1 = secondValue[matchx];
                        for (matchy = 1; matchy < firstValue.length; matchy++) {
                            String match2 = firstValue[matchy];
                            if (debug.equals("ON")) System.out.println("Comparing" + match1 + "with" + match2);
                            if (compare(match1, match2, firsttype)) {
                                matchresult = true;
                                if (debug.equals("ON")) System.out.println("Comparison result" + matchresult);
                                break;
                            }
                        }
                        if (!(matchresult)) {
                            outputValue[1] = "FALSE";
                            if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                            return outputValue;
                        }
                    }
                    outputValue[1] = "TRUE";
                    if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                    return outputValue;
                }
            } else if (myNodeName.equals("member")) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                typeinfo = "";
                String[] soughtValue;
                String[] containerValues;
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                boolean found = false;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                soughtValue = parser.parseDocument(thenode, Parameters);
                if (soughtValue == null) {
                    return null;
                }
                typeinfo = soughtValue[0];
                firsttype = typeinfo.substring(0, typeinfo.indexOf(":"));
                firstcardinality = typeinfo.substring(typeinfo.indexOf(":") + 1);
                if (strict) qtiv2utils.typeCheck(thedataStore, "any/!float/!duration", "single", typeinfo, myNodeName, debug);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                containerValues = parser2.parseDocument(thenode, Parameters);
                if (containerValues == null) {
                    return null;
                }
                typeinfo = containerValues[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, firsttype, "multiple/ordered", typeinfo, myNodeName, debug);
                if (debug.equals("ON")) {
                    System.out.println("Member: sought value: " + soughtValue[1]);
                    System.out.println("In container of length: " + containerValues.length);
                }
                for (int i = 1; i < containerValues.length; i++) {
                    if (debug.equals("ON")) System.out.println("Member: container value no: + " + i + ": " + containerValues[1]);
                    if (soughtValue[1].equals(containerValues[i])) {
                        found = true;
                        break;
                    }
                }
                if (found) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("multiple")) {
                java.util.ArrayList<String> data = new java.util.ArrayList<String>(10);
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                if (childElementCount == 0) return null;
                for (int i = 0; i < childElementCount; i++) {
                    thenode = qtiv2utils.getChildElement(theElement, i);
                    DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                    returnValue = parser.parseDocument(thenode, Parameters);
                    if (returnValue != null) {
                        typeinfo = returnValue[0];
                        if (i == 0) {
                            data.add(typeinfo);
                            firsttype = typeinfo.substring(0, typeinfo.indexOf(":"));
                            firstcardinality = typeinfo.substring(typeinfo.indexOf(":") + 1);
                            if (strict) qtiv2utils.typeCheck(thedataStore, "any", "single/multiple", typeinfo, myNodeName, debug);
                        } else {
                            if (strict) qtiv2utils.typeCheck(thedataStore, firsttype, "single/multiple", typeinfo, myNodeName, debug);
                        }
                        for (int i2 = 1; i2 < returnValue.length; i2++) {
                            data.add(returnValue[i2]);
                        }
                    } else {
                        typeinfo = "null:null";
                        data.add(null);
                    }
                }
                data.trimToSize();
                String[] collectedValues = new String[data.size()];
                data.toArray(collectedValues);
                collectedValues[0] = typeinfo.substring(0, typeinfo.indexOf(":")) + ":" + "multiple";
                if (debug.equals("ON")) qtiv2utils.reportValue(collectedValues, myNodeName);
                return collectedValues;
            } else if (myNodeName.equals("not")) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                org.w3c.dom.Node thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "boolean", "single", typeinfo, myNodeName, debug);
                if (returnValue[1].equals("FALSE")) outputValue[1] = "TRUE"; else if (returnValue[1].equals("TRUE")) outputValue[1] = "FALSE"; else outputValue[1] = null;
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("or")) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                outputValue[1] = "FALSE";
                int numbernull = 0;
                int numberfalse = 0;
                org.w3c.dom.Node thenode;
                for (int i = 0; i < childElementCount; i++) {
                    thenode = qtiv2utils.getChildElement(theElement, i);
                    DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                    returnValue = parser.parseDocument(thenode, Parameters);
                    if (returnValue == null) numbernull++; else {
                        typeinfo = returnValue[0];
                        if (strict) qtiv2utils.typeCheck(thedataStore, "boolean", "single", typeinfo, myNodeName, debug);
                        if (returnValue[1].equals("FALSE")) numberfalse++;
                        if (returnValue[1].equals("TRUE")) {
                            outputValue[1] = "TRUE";
                            break;
                        }
                    }
                }
                if (outputValue[1].equals("TRUE")) {
                    if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                    return outputValue;
                } else if (numberfalse == childElementCount) {
                    outputValue[1] = "FALSE";
                    if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                    return outputValue;
                } else {
                    if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                    return null;
                }
            } else if (myNodeName.equals("ordered")) {
                java.util.ArrayList<String> data = new java.util.ArrayList<String>(10);
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                typeinfo = "";
                if (childElementCount == 0) return null;
                for (int i = 0; i < childElementCount; i++) {
                    thenode = qtiv2utils.getChildElement(theElement, i);
                    DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                    returnValue = parser.parseDocument(thenode, Parameters);
                    if (returnValue != null) {
                        typeinfo = returnValue[0];
                        if (i == 0) {
                            data.add(typeinfo);
                            firsttype = typeinfo.substring(0, typeinfo.indexOf(":"));
                            firstcardinality = typeinfo.substring(typeinfo.indexOf(":") + 1);
                            if (strict) qtiv2utils.typeCheck(thedataStore, "any", "single/ordered", typeinfo, myNodeName, debug);
                        } else {
                            if (strict) qtiv2utils.typeCheck(thedataStore, firsttype, "single/ordered", typeinfo, myNodeName, debug);
                        }
                        for (int i2 = 1; i2 < returnValue.length; i2++) data.add(returnValue[i2]);
                    }
                }
                data.trimToSize();
                if (data.size() == 1) return null;
                String[] collectedValues = new String[data.size()];
                data.toArray(collectedValues);
                collectedValues[0] = typeinfo.substring(0, typeinfo.indexOf(":")) + ":" + "ordered";
                if (debug.equals("ON")) qtiv2utils.reportValue(collectedValues, myNodeName);
                return collectedValues;
            } else if (myNodeName.equals("patternMatch")) {
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                String regex = qtiv2utils.selectAttribute(theElement, "pattern", "");
                if (debug.equals("ON")) System.out.println("Patternmatch: " + regex);
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                if (debug.equals("ON")) System.out.println("Pattern string to match: " + returnValue[1]);
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "string", "single", typeinfo, myNodeName, debug);
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                java.util.regex.Matcher matcher = pattern.matcher(returnValue[1]);
                if (debug.equals("ON")) System.out.println("Pattern match: " + returnValue[1]);
                typeinfo = returnValue[0];
                if (matcher.find()) {
                    if (debug.equals("ON")) System.out.println("Pattern match True ");
                    outputValue[1] = "TRUE";
                } else {
                    if (debug.equals("ON")) System.out.println("Pattern match False ");
                    outputValue[1] = "FALSE";
                }
                return outputValue;
            } else if (myNodeName.equals("power")) {
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] operand = parser.parseDocument(thenode, Parameters);
                if (operand == null) return null;
                typeinfo = operand[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                double operandval = Double.parseDouble(operand[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] operator = parser2.parseDocument(thenode, Parameters);
                if (operator == null) return null;
                typeinfo = operator[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                double operatorval = Double.parseDouble(returnValue[1]);
                double result = Math.pow(operandval, operatorval);
                outputValue[0] = "float:single";
                outputValue[1] = Double.toString(result);
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if ((myNodeName.equals("product")) || (myNodeName.equals("sum"))) {
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                java.util.ArrayList<String> data = new java.util.ArrayList<String>(10);
                typeinfo = "";
                String outputType = "integer";
                for (int i = 0; i < childElementCount; i++) {
                    thenode = qtiv2utils.getChildElement(theElement, i);
                    DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                    returnValue = parser.parseDocument(thenode, Parameters);
                    if (returnValue == null) return null;
                    typeinfo = returnValue[0];
                    if (debug.equals("ON")) {
                        System.out.println("SUM/PRODUCT TYPEINFO" + typeinfo);
                        System.out.println("SUM?PRODUCT RETURN VALUE" + returnValue[1]);
                    }
                    type = typeinfo.substring(0, typeinfo.indexOf(":"));
                    if (type.equals("float")) outputType = "float";
                    if (strict) qtiv2utils.typeCheck(thedataStore, "float/integer", "single", typeinfo, myNodeName, debug);
                    data.add(returnValue[1]);
                }
                data.trimToSize();
                if (data.size() == 1) return null;
                String[] collectedValues = new String[data.size()];
                data.toArray(collectedValues);
                if (debug.equals("ON")) for (int i = 0; i < collectedValues.length; i++) System.out.println(" SUM/PRODUCT COLLECTED VALUES" + i + ":" + collectedValues[i]);
                outputValue = new String[2];
                double product = 1;
                double sum = 0;
                int intproduct;
                int intsum;
                if (myNodeName.equals("product")) {
                    for (int i = 0; i < collectedValues.length; i++) {
                        if (collectedValues[i].equals("")) return null;
                        if (outputType.equals("integer")) {
                            int number = Integer.parseInt(collectedValues[i]);
                            product = product * number;
                        } else {
                            double number = Double.parseDouble(collectedValues[i]);
                            product = product * number;
                        }
                        if (debug.equals("ON")) System.out.println("PRODUCT FOR VALUE " + i + ":" + product);
                    }
                    if (outputType.equals("integer")) {
                        intproduct = (int) product;
                        outputValue[0] = "integer:single";
                        outputValue[1] = Integer.toString(intproduct);
                    } else {
                        outputValue[0] = "float:single";
                        outputValue[1] = Double.toString(product);
                    }
                }
                if (myNodeName.equals("sum")) {
                    for (int i = 0; i < collectedValues.length; i++) {
                        if (collectedValues[i].equals("")) return null;
                        if (outputType.equals("integer")) {
                            String nostr = collectedValues[i];
                            int number = Integer.parseInt(nostr.trim());
                            if (debug.equals("ON")) System.out.println("SUM " + sum);
                            sum = sum + number;
                            if (debug.equals("ON")) {
                                System.out.println("NotoAdd " + number);
                                System.out.println("SUMNOW " + sum);
                            }
                        } else {
                            String nostr = collectedValues[i];
                            double number = Double.parseDouble(nostr.trim());
                            sum = sum + number;
                        }
                    }
                    if (outputType.equals("integer")) {
                        intsum = (int) sum;
                        outputValue[0] = "integer:single";
                        outputValue[1] = Integer.toString(intsum);
                    } else {
                        outputValue[0] = "float:single";
                        outputValue[1] = Double.toString(sum);
                    }
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("random")) {
                outputValue = new String[2];
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "any", "multiple/ordered", typeinfo, myNodeName, debug);
                type = typeinfo.substring(0, typeinfo.indexOf(":"));
                int NoOfValues = returnValue.length - 1;
                int randValue = (int) (Math.random() * NoOfValues);
                outputValue[0] = type + ":single";
                outputValue[1] = returnValue[1 + randValue];
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if ((myNodeName.equals("round")) || (myNodeName.equals("truncate"))) {
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                returnValue = parser.parseDocument(thenode, Parameters);
                if (returnValue == null) return null;
                typeinfo = returnValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "float", "single", typeinfo, myNodeName, debug);
                double value = Double.parseDouble(returnValue[1]);
                outputValue[0] = "integer:single";
                if (myNodeName.equals("round")) outputValue[1] = Integer.toString((int) Math.round(value));
                if (myNodeName.equals("truncate")) {
                    String doubleString = Double.toString(value);
                    outputValue[1] = doubleString.substring(0, doubleString.indexOf('.'));
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("stringMatch")) {
                boolean casecounts = false;
                boolean substringcounts = false;
                String caseSensitive = qtiv2utils.selectAttribute(theElement, "caseSensitive", "true");
                if (caseSensitive.equalsIgnoreCase("true")) casecounts = true;
                String substringSensitive = qtiv2utils.selectAttribute(theElement, "substring", "true");
                if (substringSensitive.equalsIgnoreCase("true")) substringcounts = true;
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                String[] secondValue;
                String[] firstValue;
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                firstValue = parser.parseDocument(thenode, Parameters);
                if (firstValue == null) return null;
                typeinfo = firstValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "string", "single", typeinfo, myNodeName, debug);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                secondValue = parser2.parseDocument(thenode, Parameters);
                if (secondValue == null) return null;
                typeinfo = secondValue[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "string", "single", typeinfo, myNodeName, debug);
                if (!casecounts) {
                    firstValue[1] = firstValue[1].toUpperCase();
                    secondValue[1] = secondValue[1].toUpperCase();
                }
                if (substringcounts) {
                    if (firstValue[1].contains(secondValue[1])) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                } else {
                    if (firstValue[1].equals(secondValue[1])) outputValue[1] = "TRUE"; else outputValue[1] = "FALSE";
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("substring")) {
                boolean casecounts = false;
                String caseSensitive = qtiv2utils.selectAttribute(theElement, "caseSensitive", "true");
                if (caseSensitive.equalsIgnoreCase("true")) casecounts = true;
                int childElementCount = qtiv2utils.getChildElementCount(theElement);
                String[] outerString;
                String[] soughtString;
                String longstring;
                String shortstring;
                outputValue = new String[2];
                outputValue[0] = "boolean:single";
                boolean found = false;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                soughtString = parser.parseDocument(thenode, Parameters);
                if (soughtString == null) return null;
                typeinfo = soughtString[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "string", "single", typeinfo, myNodeName, debug);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                outerString = parser2.parseDocument(thenode, Parameters);
                if (outerString == null) return null;
                typeinfo = outerString[0];
                if (strict) qtiv2utils.typeCheck(thedataStore, "string", "single", typeinfo, myNodeName, debug);
                if (casecounts) {
                    longstring = outerString[1];
                    shortstring = soughtString[1];
                } else {
                    longstring = outerString[1].toUpperCase();
                    shortstring = soughtString[1].toUpperCase();
                }
                if (longstring.indexOf(shortstring) < 0) outputValue[1] = "FALSE"; else outputValue[1] = "TRUE";
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else if (myNodeName.equals("subtract")) {
                org.w3c.dom.Node thenode;
                thenode = qtiv2utils.getChildElement(theElement, 0);
                outputValue = new String[2];
                DOMNodeParser parser = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] firstValue = parser.parseDocument(thenode, Parameters);
                if (firstValue == null) return null;
                typeinfo = firstValue[0];
                String type1 = typeinfo.substring(0, typeinfo.indexOf(":"));
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer/float", "single", typeinfo, myNodeName, debug);
                double first = Double.parseDouble(firstValue[1]);
                thenode = qtiv2utils.getChildElement(theElement, 1);
                DOMNodeParser parser2 = new DOMNodeParser(thenode, myNodeName, thedataStore);
                String[] secondValue = parser.parseDocument(thenode, Parameters);
                if (secondValue == null) return null;
                typeinfo = secondValue[0];
                String type2 = typeinfo.substring(0, typeinfo.indexOf(":"));
                if (strict) qtiv2utils.typeCheck(thedataStore, "integer/float", "single", typeinfo, myNodeName, debug);
                double second = Double.parseDouble(returnValue[1]);
                double result = first - second;
                if ((type1.equals("integer")) && (type2.equals("integer"))) {
                    outputValue[0] = "integer:single";
                    outputValue[1] = Integer.toString((int) result);
                } else {
                    outputValue[0] = "float:single";
                    outputValue[1] = Double.toString(result);
                }
                if (debug.equals("ON")) qtiv2utils.reportValue(outputValue, myNodeName);
                return outputValue;
            } else return null;
        } catch (NumberFormatException nfe) {
            errstr = "NUMBER FORMAT  ERROR in &lt;" + myNodeName + "&gt;<br />";
            throw new gqtiexcept.XMLException(errstr);
        }
    }

    private boolean compare(String match1, String match2, String type) {
        if (type.equals("pair")) {
            if (match1.equals(match2)) return true; else {
                if (debug.equals("ON")) System.out.println("Pair : Reversing " + match2);
                match2 = qtiv2utils.reversePair(match2, " ");
                if (debug.equals("ON")) System.out.println("Pair Matching " + match1 + " with " + match2);
                if (!(match1.equals(match2))) return false; else return true;
            }
        }
        if (type.equals("boolean")) {
            if (debug.equals("ON")) System.out.println("BOOLEAN");
            if ((match1.equals("FALSE")) || (match1.equals("false")) || (match1.equals("0"))) match1 = "FALSE"; else if ((match1.equals("TRUE")) || (match1.equals("true")) || (!(match1.equals("0")))) match1 = "TRUE";
            if ((match2.equals("FALSE")) || (match2.equals("false")) || (match2.equals("0"))) match2 = "FALSE"; else if ((match2.equals("TRUE")) || (match2.equals("true")) || (!(match2.equals("0")))) match2 = "TRUE";
            if (debug.equals("ON")) System.out.println("Boolean Matching " + match1 + " with " + match2);
        }
        if (type.equals("float")) {
            if ((match1.equals("")) || (match2.equals(""))) return false;
            float float1 = Float.parseFloat(match1);
            float float2 = Float.parseFloat(match2);
            match1 = Float.toString(float1);
            match2 = Float.toString(float2);
            if (debug.equals("ON")) System.out.println("Float Matching " + match1 + " with " + match2);
        }
        if (match1.equals(match2)) return true; else return false;
    }
}
