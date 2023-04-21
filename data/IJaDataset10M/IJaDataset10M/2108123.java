package uk.ac.city.soi.everest.monitor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.city.soi.everest.bse.XMLConverter;
import uk.ac.city.soi.everest.core.Constants;
import uk.ac.city.soi.everest.core.Fluent;
import uk.ac.city.soi.everest.core.Function;
import uk.ac.city.soi.everest.core.Signature;
import uk.ac.city.soi.everest.core.TimeExpression;
import uk.ac.city.soi.everest.core.Variable;

/**
 * This class converts Template object into XML. Templates are converted into XML (and vice versa) only for internal
 * communication, e.g. monitoring report is sent back from the analyser web service to the manager. Although XML schema
 * for Template is different from the XML schema for formula, they are very similar. So most of the methods in this
 * class are similar to the corresponding methods in FormulaWriter class.
 * 
 * @author Khaled Mahbub
 */
public class TemplateWriter {

    Document document;

    /**
     * Creates a new instance of TemplateWriter.
     */
    public TemplateWriter() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }

    /**
     * Generates TimeVariable element in XML Template.
     * 
     * @param name -
     *            name of the time variable
     * @param value -
     *            value of the time variable
     * @param tagName -
     *            tagName is used to use this method to generate TimeVariable element in different cases. For example
     *            inside Happens, tag name of TimeVariable is TimeVar, but inside TimeExpression element tag name of
     *            Timevariable is Time
     * @return
     */
    private Element getTimeVariable(String name, long value, String tagName) {
        Element var = null;
        try {
            var = document.createElement(tagName);
            Element varName = document.createElement("varName");
            varName.appendChild(document.createTextNode(name));
            var.appendChild(varName);
            Element varType = document.createElement("varType");
            varType.appendChild(document.createTextNode("TimeVariable"));
            var.appendChild(varType);
            if (value != Constants.TIME_UD && value != Constants.RANGE_UB && value != Constants.RANGE_LB) {
                Element timeValue = document.createElement("value");
                timeValue.appendChild(document.createTextNode(String.valueOf(value)));
                var.appendChild(timeValue);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return var;
    }

    /**
     * This method is used to generate Variable element in XML Template.
     * 
     * @param var -
     *            Variable object for which the element will be generated
     * @param tagName -
     *            tagName was used to generate Variable element in different cases (probably not used at the end, due to
     *            change in XML schema)
     */
    private Element getVariable(Variable var, String tagName) {
        Element varElem = null;
        try {
            varElem = document.createElement(tagName);
            if (var.isStatic()) varElem.setAttribute("persistent", "true"); else varElem.setAttribute("persistent", "false");
            if (!var.getConstName().equals("")) {
                varElem.setAttribute("forMatching", "true");
                Element varName = document.createElement("varName");
                varName.appendChild(document.createTextNode(var.getConstName()));
                varElem.appendChild(varName);
            } else {
                varElem.setAttribute("forMatching", "false");
                Element varName = document.createElement("varName");
                varName.appendChild(document.createTextNode(var.getName()));
                varElem.appendChild(varName);
            }
            if (var.getType().endsWith("Array")) {
                Element array = document.createElement("array");
                Element arrayType = document.createElement("type");
                arrayType.appendChild(document.createTextNode(var.getType()));
                array.appendChild(arrayType);
                ArrayList values = var.getValues();
                for (int i = 0; i < values.size(); i++) {
                    String val = (String) values.get(i);
                    Element value = document.createElement("value");
                    Element indexValue = document.createElement("indexValue");
                    indexValue.appendChild(document.createTextNode(String.valueOf(i)));
                    value.appendChild(indexValue);
                    Element cellValue = document.createElement("cellValue");
                    cellValue.appendChild(document.createTextNode(val));
                    value.appendChild(cellValue);
                    array.appendChild(value);
                }
                varElem.appendChild(array);
            } else {
                Element varType = document.createElement("varType");
                varType.appendChild(document.createTextNode(var.getType()));
                varElem.appendChild(varType);
                if (var.getValue() != null) {
                    Element varValue = document.createElement("value");
                    varValue.appendChild(document.createTextNode(var.getValue().toString()));
                    varElem.appendChild(varValue);
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return varElem;
    }

    /**
     * This method is used to generate TimeExpression element in XML template.
     * 
     * @param jTex -
     *            TimeExpression object for which the element will be generated
     * @param tagName -
     *            tagName was used to generate Expressionvariable element in different cases. For example inside
     *            Happens, tag name for the starting time expression in the range is fromTime, and the ending time is
     *            toTime
     * 
     */
    private Element getTimeExpression(TimeExpression jTex, String tagName) {
        Element tex = null;
        try {
            tex = document.createElement(tagName);
            Element time = getTimeVariable(jTex.getTimeVarName(), jTex.getTimeValue(), "time");
            tex.appendChild(time);
            if (jTex.getNumber() != 0) {
                Element operator = null;
                if (jTex.getOperator().equals(Constants.PLUS)) operator = document.createElement("plus"); else operator = document.createElement("minus");
                operator.appendChild(document.createTextNode(String.valueOf(jTex.getNumber())));
                tex.appendChild(operator);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return tex;
    }

    /**
     * Genreates re_term or ic_term element in XML template
     * 
     * @param operation -
     *            name of the operation in re_term or ic_term
     * @param partner -
     *            name of the partner in re_term or ic_term
     * @param variables -
     *            variables inside re_term or ic_term
     * @param tagName -
     *            name of the tag, i.e. re_term or ic_term
     * @return
     */
    private Element getReIcTerm(String operation, String partner, ArrayList variables, String tagName) {
        Element term = null;
        try {
            term = document.createElement(tagName);
            Element opnName = document.createElement("operationName");
            opnName.appendChild(document.createTextNode(operation));
            term.appendChild(opnName);
            Element partnerName = document.createElement("partnerName");
            partnerName.appendChild(document.createTextNode(partner));
            term.appendChild(partnerName);
            for (int i = 0; i < variables.size(); i++) {
                Variable ecVar = (Variable) variables.get(i);
                Element var = getVariable(ecVar, "variable");
                term.appendChild(var);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return term;
    }

    /**
     * Genreates rc_term or ir_term element in XML template
     * 
     * @param operation -
     *            name of the operation in rc_term or ir_term
     * @param partner -
     *            name of the partner in rc_term or ir_term
     * @param variables -
     *            variables inside rc_term or ir_term
     * @param tagName -
     *            name of the tag, i.e. rc_term or ir_term
     * @return
     */
    private Element getRcIrTerm(String operation, String partner, ArrayList variables, String tagName) {
        Element term = null;
        try {
            term = document.createElement(tagName);
            Element opnName = document.createElement("operationName");
            opnName.appendChild(document.createTextNode(operation));
            term.appendChild(opnName);
            Element partnerName = document.createElement("partnerName");
            partnerName.appendChild(document.createTextNode(partner));
            term.appendChild(partnerName);
            for (int i = 0; i < variables.size(); i++) {
                Variable ecVar = (Variable) variables.get(i);
                Element var = getVariable(ecVar, "variable");
                term.appendChild(var);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return term;
    }

    /**
     * Genreates as_term element in XML template. This method was never used in Serenity, this was used in SeCSE as
     * SeCSE monitor deals with BPEL specification.
     * 
     * @param operation -
     *            name of the operation in as_term
     * @param tagName -
     *            name of the tag, as_term
     * @return
     */
    private Element getAsTerm(String operation, String tagName) {
        Element term = null;
        try {
            term = document.createElement(tagName);
            Element opnName = document.createElement("operationName");
            opnName.appendChild(document.createTextNode(operation));
            term.appendChild(opnName);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return term;
    }

    /**
     * This method is used to generate Function element in XML template.
     * 
     * @param func -
     *            Function object for which the element will be generated
     * @param tagName -
     *            tagName was used to generate Function element in different cases (probably not used at the end, due to
     *            change in XML schema)
     */
    private Element getFunction(Function func, String tagName) {
        Element funcElem = null;
        try {
            funcElem = document.createElement(tagName);
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(func.getOperationName()));
            funcElem.appendChild(name);
            Element partner = document.createElement("partner");
            partner.appendChild(document.createTextNode(func.getPartner()));
            funcElem.appendChild(partner);
            LinkedHashMap params = func.getParameters();
            Iterator it = params.values().iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                if (obj instanceof Variable) {
                    Variable var = (Variable) obj;
                    Element varElem = getVariable(var, "variable");
                    funcElem.appendChild(varElem);
                } else if (obj instanceof Function) {
                    Function paramFunc = (Function) obj;
                    Element paramFuncElem = getFunction(paramFunc, "operationCall");
                    funcElem.appendChild(paramFuncElem);
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return funcElem;
    }

    /**
     * Generates fluent element in XML template.
     * 
     * @param fluent -
     *            Fluent object for which the element will be generated
     * @return
     */
    private Element getFluent(Fluent fluent) {
        Element flnt = null;
        try {
            flnt = document.createElement("fluent");
            flnt.setAttribute("name", fluent.getFluentName());
            for (int i = 0; i < fluent.getArguments().size(); i++) {
                Object obj = fluent.getArguments().get(i);
                if (obj instanceof Variable) {
                    Variable tVar = ((Variable) obj).makeCopy();
                    flnt.appendChild(getVariable(tVar, Constants.VARIABLE));
                } else if (obj instanceof Function) {
                    Function func = (Function) obj;
                    flnt.appendChild(getFunction(func, Constants.OPERATION_CALL));
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return flnt;
    }

    /**
     * Generates happens element in XML template
     * 
     * @param pred -
     *            Predicate object for which the element will be generated
     * @return
     */
    private Element getHappens(Signature sig) {
        Element happens = null;
        try {
            happens = document.createElement("happens");
            Element term = null;
            if (sig.getPrefix().equals("rc")) term = getRcIrTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "rc_term"); else if (sig.getPrefix().equals("ir")) term = getRcIrTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "ir_term"); else if (sig.getPrefix().equals("re")) term = getReIcTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "re_term"); else if (sig.getPrefix().equals("ic")) term = getReIcTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "ic_term"); else if (sig.getPrefix().equals("as")) term = getAsTerm(sig.getOperationName(), "as_term");
            happens.appendChild(term);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return happens;
    }

    /**
     * Generates initiates/terminates element in XML template
     * 
     * @param pred -
     *            Predicate object for which the element will be generated
     * @param tagName -
     *            name of the tag, e.g. initiates or terminates
     * @return
     */
    private Element getInitiatesTerminates(Signature sig, String tagName) {
        Element initiates = null;
        try {
            initiates = document.createElement(tagName);
            Element term = null;
            if (sig.getPrefix().equals("rc")) term = getRcIrTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "rc_term"); else if (sig.getPrefix().equals("ir")) term = getRcIrTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "ir_term"); else if (sig.getPrefix().equals("ic")) term = getReIcTerm(sig.getOperationName(), sig.getPartnerId(), sig.getVariables(), "ic_term"); else if (sig.getPrefix().equals("as")) term = getAsTerm(sig.getOperationName(), "as_term");
            initiates.appendChild(term);
            Element fluent = getFluent(sig.getFluent());
            initiates.appendChild(fluent);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return initiates;
    }

    /**
     * Generates holdsAt element in XML template
     * 
     * @param pred -
     *            Predicate object for which the element will be generated
     * @param tagName
     *            was used to generate Function element in different cases (probably not used at the end, due to change
     *            in XML schema)
     * @return
     */
    private Element getHoldsAt(Signature sig) {
        Element holds = null;
        try {
            holds = document.createElement("holdsAt");
            Element fluent = getFluent(sig.getFluent());
            holds.appendChild(fluent);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return holds;
    }

    /**
     * Generates constants element in XML template
     * 
     * @param var -
     *            Variable object that holds the constants and for which element will be generated
     * @return
     */
    private Element getConstant(Variable var) {
        Element constant = null;
        try {
            constant = document.createElement("constant");
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(var.getConstName()));
            Element value = document.createElement("value");
            value.appendChild(document.createTextNode(var.getValue().toString()));
            constant.appendChild(name);
            constant.appendChild(value);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return constant;
    }

    /**
     * Generates RelationalPredicate element in XML template
     * 
     * @param pred -
     *            Predicate object for which the element will be generated. It should be noted that Predicate object is
     *            used to model relational predicate also (see documentation in FormulaReader class)
     * @return
     */
    private Element getVarRelation(Predicate pred) {
        Element varRel = null;
        HashMap relName = new HashMap();
        relName.put(Constants.EQUAL, "equal");
        relName.put(Constants.NOT_EQUAL, "notEqualTo");
        relName.put(Constants.LESS_THAN, "lessThan");
        relName.put(Constants.LESS_THAN_EQUAL, "lessThanEqualTo");
        relName.put(Constants.GREATER_THAN, "greaterThan");
        relName.put(Constants.GREATER_THAN_EQUAL, "greaterThanEqualTo");
        try {
            varRel = document.createElement((String) relName.get(pred.getOperationName()));
            Element oprnd1 = document.createElement("operand1");
            Element oprnd2 = document.createElement("operand2");
            varRel.appendChild(oprnd1);
            varRel.appendChild(oprnd2);
            if (pred.getPrefix().equals(Constants.RELATION_VV)) {
                Variable var1 = new Variable(pred.getVariable(0));
                Variable var2 = new Variable(pred.getVariable(1));
                oprnd1.appendChild(getVariable(var1, "variable"));
                oprnd2.appendChild(getVariable(var2, "variable"));
            } else if (pred.getPrefix().equals(Constants.RELATION_VC)) {
                Variable var1 = new Variable(pred.getVariable(0));
                Variable var2 = new Variable(pred.getVariable(1));
                oprnd1.appendChild(getVariable(var1, "variable"));
                oprnd2.appendChild(getConstant(var2));
            } else if (pred.getPrefix().equals(Constants.RELATION_CV)) {
                Variable var1 = new Variable(pred.getVariable(0));
                Variable var2 = new Variable(pred.getVariable(1));
                oprnd1.appendChild(getConstant(var1));
                oprnd2.appendChild(getVariable(var2, "variable"));
            } else if (pred.getPrefix().equals(Constants.RELATION_VF)) {
                Variable var1 = new Variable(pred.getVariable(0));
                Function func = new Function((Function) pred.getVariables().get(1));
                oprnd1.appendChild(getVariable(var1, "variable"));
                oprnd2.appendChild(getFunction(func, "operationCall"));
            } else if (pred.getPrefix().equals(Constants.RELATION_FV)) {
                Variable var = new Variable(pred.getVariable(1));
                Function func = new Function((Function) pred.getVariables().get(0));
                oprnd2.appendChild(getVariable(var, "variable"));
                oprnd1.appendChild(getFunction(func, "operationCall"));
            } else if (pred.getPrefix().equals(Constants.RELATION_FC)) {
                Function func = new Function((Function) pred.getVariables().get(0));
                Variable var = new Variable(pred.getVariable(1));
                oprnd1.appendChild(getFunction(func, "operationCall"));
                oprnd2.appendChild(getConstant(var));
            } else if (pred.getPrefix().equals(Constants.RELATION_CF)) {
                Function func = new Function((Function) pred.getVariables().get(1));
                Variable var = new Variable(pred.getVariable(0));
                oprnd1.appendChild(getConstant(var));
                oprnd2.appendChild(getFunction(func, "operationCall"));
            } else if (pred.getPrefix().equals(Constants.RELATION_FF)) {
                Function func1 = new Function((Function) pred.getVariables().get(0));
                Function func2 = new Function((Function) pred.getVariables().get(1));
                oprnd1.appendChild(getFunction(func1, "operationCall"));
                oprnd2.appendChild(getFunction(func2, "operationCall"));
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return varRel;
    }

    /**
     * Generates predicate element in XML template. This method uses getVarRelation method for relational predicate and
     * getHappens, getInitiates etc. methods for other predicates
     * 
     * @param pred -
     *            Predicate object for which the element will be generated
     * @return
     */
    private Element generatePredicate(Predicate pred) {
        Element predicate = document.createElement("predicate");
        try {
            if (pred.getEcName().equals(Constants.PRED_RELATION)) {
                Element relaPredicate = document.createElement("relationalPredicate");
                relaPredicate.appendChild(getVarRelation(pred));
                predicate.appendChild(relaPredicate);
            } else {
                predicate.appendChild(generateSignature(pred));
                if (pred.isNegated()) predicate.setAttribute("negated", "true"); else predicate.setAttribute("negated", "false");
                if (pred.getPHappens()) predicate.setAttribute("pHappens", "true"); else predicate.setAttribute("pHappens", "false");
                if (pred.isUnconstrained()) predicate.setAttribute("unconstrained", "true"); else predicate.setAttribute("unconstrained", "false");
                if (pred.isRecordable()) predicate.setAttribute("recordable", "true"); else predicate.setAttribute("recordable", "false");
                if (pred.isConfirmed()) predicate.setAttribute("confirmed", "true"); else predicate.setAttribute("confirmed", "false");
                predicate.setAttribute("minLikelihood", String.valueOf(pred.getMinLikelihood()));
                predicate.setAttribute("maxLikelihood", String.valueOf(pred.getMaxLikelihood()));
            }
            if (predicate != null) {
                Element tQuan = document.createElement("timeVarQuantifier");
                tQuan.appendChild(document.createTextNode(pred.getQuantifier()));
                predicate.appendChild(tQuan);
                predicate.appendChild(getTimeVariable(pred.getTimeVarName(), pred.getTimeVar().getValue(), "timeVar"));
                predicate.appendChild(getTimeExpression(pred.getLowBound(), "fromTime"));
                predicate.appendChild(getTimeExpression(pred.getUpBound(), "toTime"));
                Element truthVal = document.createElement("truthValue");
                truthVal.appendChild(document.createTextNode(pred.getTruthVal()));
                predicate.appendChild(truthVal);
                Element source = document.createElement("source");
                source.appendChild(document.createTextNode(pred.getSource()));
                predicate.appendChild(source);
                Element id = document.createElement("ID");
                id.appendChild(document.createTextNode(pred.getID()));
                predicate.appendChild(id);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return predicate;
    }

    /**
     * Generates operator element in XML template. Mainly used in TimeExpression element.
     * 
     * @param operator -
     *            name of the operator, (plus or minus)
     * @return
     */
    private Element getOperator(String operator) {
        Element opt = null;
        try {
            opt = document.createElement("operator");
            opt.appendChild(document.createTextNode(operator));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return opt;
    }

    /**
     * Generates predicates element in XML template.
     */
    private Element generatePredicates(Template template, String tagName, int start, int end) {
        Element predicates = null;
        try {
            predicates = document.createElement(tagName);
            for (int i = start; i < end; i++) {
                Predicate pred = template.getPredicate(i);
                Element predicate = generatePredicate(pred);
                predicates.appendChild(predicate);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return predicates;
    }

    /**
     * Generates signature element in XML template
     */
    private Element generateSignature(Signature signature) {
        Element sig = null;
        try {
            sig = document.createElement("signature");
            if (signature.getEcName().equals("Happens")) sig.appendChild(getHappens(signature)); else if (signature.getEcName().equals("Initiates")) sig.appendChild(getInitiatesTerminates(signature, "initiates")); else if (signature.getEcName().equals("Terminates")) sig.appendChild(getInitiatesTerminates(signature, "terminates")); else if (signature.getEcName().equals("HoldsAt")) sig.appendChild(getHoldsAt(signature));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return sig;
    }

    /**
     * Generates signature element for a link in XML template.
     * 
     * @param signature
     * @return
     */
    private Element generateLinkSignature(Signature signature) {
        Element sig = null;
        try {
            sig = document.createElement("signature");
            if (signature.getEcName().equals("Happens")) sig.appendChild(getHappens(signature)); else if (signature.getEcName().equals("Initiates")) sig.appendChild(getInitiatesTerminates(signature, "initiates")); else if (signature.getEcName().equals("HoldsAt")) sig.appendChild(document.createElement("holdsAt"));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return sig;
    }

    /**
     * Generates depenedent element in XML template for a given Link object
     * 
     * @param link
     * @return
     */
    private Element generateDependant(Link link) {
        Element dependant = null;
        try {
            dependant = document.createElement("dependant");
            Element target = document.createElement("targetId");
            target.appendChild(document.createTextNode(link.getTargetId()));
            dependant.appendChild(target);
            Element type = document.createElement("type");
            type.appendChild(document.createTextNode(link.getLinkType()));
            dependant.appendChild(type);
            Element sig = generateLinkSignature(link);
            dependant.appendChild(sig);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return dependant;
    }

    /**
     * Generates dependents element in the XML template
     * 
     * @param template -
     *            Template object for which dependents will be generated
     * @return
     */
    private Element generateDependants(Template template) {
        Element dependants = null;
        try {
            dependants = document.createElement("dependants");
            for (int i = 0; i < template.linkSize(); i++) {
                Link link = template.getLink(i);
                Element dependant = generateDependant(link);
                dependants.appendChild(dependant);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return dependants;
    }

    /**
     * Generates varBinding element in XML template.
     * 
     * @param uf -
     *            collection of unified variables in the Template object
     * @return
     */
    private Element generateVarBindings(HashMap uf) {
        Element varBindings = null;
        try {
            varBindings = document.createElement("varBindings");
            Iterator it = uf.values().iterator();
            while (it.hasNext()) {
                Variable var = (Variable) it.next();
                varBindings.appendChild(getVariable(var, "variable"));
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return varBindings;
    }

    /**
     * Generates template element in XML template
     * 
     * @param template -
     *            Template object for which the element will be generated
     * @return
     */
    private Element generateTemplate(Template template) {
        Element temp = null;
        try {
            temp = document.createElement("template");
            temp.setAttribute("formulaId", template.getFormulaId());
            temp.setAttribute("instanceId", template.getTemplateId());
            temp.setAttribute("type", template.getType());
            temp.setAttribute("active", String.valueOf(template.isActive()));
            temp.appendChild(generateDependants(template));
            temp.appendChild(generateVarBindings(template.getUnifiers()));
            Element status = document.createElement("status");
            status.appendChild(document.createTextNode(template.getStatus()));
            temp.appendChild(status);
            temp.appendChild(generatePredicates(template, "body", 0, template.bodySize()));
            temp.appendChild(generatePredicates(template, "head", template.bodySize(), template.totalPredicates()));
            Element decTime = document.createElement("decisionTime");
            decTime.appendChild(document.createTextNode(String.valueOf(template.getDET())));
            temp.appendChild(decTime);
            Element copyC = document.createElement("copyCounter");
            copyC.appendChild(document.createTextNode(String.valueOf(template.getCC())));
            temp.appendChild(copyC);
            temp.setAttribute("threatLikelihood", String.valueOf(template.getThreatLikelihood()));
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return temp;
    }

    /**
     * Generates template element in XML template and writes the template in a file.
     * 
     * @param fileName -
     *            name of the file where the templates will be saved
     * @param templaets -
     *            collection of template objects for which the element will be generated
     * 
     */
    public void generateXML(String fileName, LinkedHashMap templates) {
        try {
            Element root = document.createElement("tempplates");
            root.setAttribute("xmlns", "http://tempuri.org/ec/templates");
            document.appendChild(root);
            Iterator it = templates.values().iterator();
            while (it.hasNext()) {
                Template temp = (Template) it.next();
                Element template = generateTemplate(temp);
                root.appendChild(template);
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Generates template element in XML template and writes the template in a file.
     * 
     * @param fileName -
     *            name of the file where the templates will be saved
     * @param templates -
     *            List of template objects for which the element will be generated
     * 
     */
    public void generateXML(String fileName, ArrayList templates) {
        try {
            Element root = document.createElement("templates");
            root.setAttribute("xmlns", "http://tempuri.org/ec/templates");
            document.appendChild(root);
            for (int i = 0; i < templates.size(); i++) {
                Template temp = (Template) templates.get(i);
                Element template = generateTemplate(temp);
                root.appendChild(template);
            }
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Generates template element in XML template and writes the template in a file.
     * 
     * @param fileName -
     *            name of the file where the template will be saved
     * @param temp -
     *            template object for which the element will be generated
     * 
     */
    public void generateXML(String fileName, Template temp) {
        try {
            Element root = document.createElement("templates");
            root.setAttribute("xmlns", "http://tempuri.org/ec/templates");
            document.appendChild(root);
            Element template = generateTemplate(temp);
            root.appendChild(template);
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(fileName));
            transformer.transform(source, result);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /**
     * Generates template element in XML template returns the String representation of the XML template.
     * 
     * @param templates -
     *            List of template objects for which the element will be generated
     * 
     */
    public String generateXML(LinkedHashMap templates) {
        String docStr = "";
        try {
            Element root = document.createElement("templates");
            root.setAttribute("xmlns", "http://tempuri.org/ec/template");
            document.appendChild(root);
            Iterator it = templates.values().iterator();
            while (it.hasNext()) {
                Template temp = (Template) it.next();
                Element template = generateTemplate(temp);
                root.appendChild(template);
            }
            docStr = XMLConverter.toString(root);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return docStr;
    }

    /**
     * Generates template element in XML template returns the String representation of the XML template.
     * 
     * @param templates -
     *            List of template objects for which the element will be generated
     * 
     */
    public String generateXML(ArrayList templates) {
        String docStr = "";
        try {
            Element root = document.createElement("templates");
            root.setAttribute("xmlns", "http://tempuri.org/ec/templates");
            document.appendChild(root);
            for (int i = 0; i < templates.size(); i++) {
                Template temp = (Template) templates.get(i);
                Element template = generateTemplate(temp);
                root.appendChild(template);
            }
            docStr = XMLConverter.toString(root);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return docStr;
    }

    /**
     * Generates template element in XML template returns the String representation of the XML template.
     * 
     * @param temp -
     *            template object for which the element will be generated
     * 
     */
    public String generateXML(Template temp) {
        String docStr = "";
        try {
            Element root = document.createElement("templates");
            root.setAttribute("xmlns", "http://tempuri.org/ec/template");
            document.appendChild(root);
            Element template = generateTemplate(temp);
            root.appendChild(template);
            docStr = XMLConverter.toString(root);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return docStr;
    }
}
