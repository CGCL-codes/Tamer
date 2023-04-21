package edu.gsbme.msource.CellML;

import java.io.IOException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import edu.gsbme.MMLParser2.CellML.CellMLComponent;
import edu.gsbme.MMLParser2.CellML.CellMLConnection;
import edu.gsbme.MMLParser2.Factory.CMLFactory;
import edu.gsbme.MMLParser2.MathML.MathMLParser;
import edu.gsbme.MMLParser2.MathML.MEE.MEEParser;
import edu.gsbme.MMLParser2.MathML.MEE.Error.SyntaxError;
import edu.gsbme.MMLParser2.MathML.MEE.Evaluate.ScalarValue;
import edu.gsbme.MMLParser2.MathML.MEE.Evaluate.VariableTables;
import edu.gsbme.MMLParser2.MathML.MEE.MathAST.AST;
import edu.gsbme.MMLParser2.MathML.MEE.Units.Prefix;
import edu.gsbme.MMLParser2.MathML.MEE.Units.PrefixClass;
import edu.gsbme.MMLParser2.MathML.MEE.Units.UnitDefinitions;
import edu.gsbme.MMLParser2.MathML.MEE.Units.unitDef;
import edu.gsbme.MMLParser2.MathML.MEE.Units.units;
import edu.gsbme.MMLParser2.MathML.MEE.Units.UnitDefinitions.UNITS;
import edu.gsbme.MMLParser2.MathML.MEE.Units.utility.correctUnitTree;
import edu.gsbme.MMLParser2.MathML.MEE.Units.utility.parseUnitTree;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.extractPiecewiseConditionalVariable;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.generateNormalizePiecewiseEquation;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.isDiffEquation;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.isPiecewiseEquation;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.returnIndependentVariables;
import edu.gsbme.MMLParser2.MathML.MEE.Utility.returnStateVariable;
import edu.gsbme.messagehandler.CategoryStructure;
import edu.gsbme.messagehandler.MessageHandler;
import edu.gsbme.messagehandler.MessageType;
import edu.gsbme.msource.CellML.Structure.CComponent;
import edu.gsbme.msource.CellML.Structure.CEquation;
import edu.gsbme.msource.CellML.Structure.CModel;
import edu.gsbme.msource.CellML.Structure.CVariable;
import edu.gsbme.msource.CellML.Structure.EquationType;
import edu.gsbme.msource.CellML.Structure.VariableType;
import edu.gsbme.msource.CellML.Structure.CEquation.Unit_Check;
import edu.gsbme.msource.CellML.Utility.fillUnitAST;
import edu.gsbme.msource.CellML.Utility.normalizeVariableName;
import edu.gsbme.msource.CellML.Utility.printCModel;

/**
 * This is the main class to access CellML document into CModel data structure.
 * This creates a accessible CellML model from a CellML Document
 * @author David
 *
 */
public class CellMLHandler {

    public static void main(String[] arg) {
        runFileDialog();
    }

    public static void runFileDialog() {
        Shell shell = new Shell();
        FileDialog dialog = new FileDialog(shell);
        dialog.open();
        Path path = new Path(dialog.getFilterPath());
        path = (Path) path.append(dialog.getFileName());
        CMLFactory factory = null;
        try {
            factory = new CMLFactory("", "file:///" + path.toOSString());
            CellMLHandler x = new CellMLHandler(factory, "", true);
            printCModel.printDebug(x.CellML);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    CMLFactory cmlfactory;

    public CModel CellML;

    public IDGenerator idHandler;

    boolean normalizePiecewise = false;

    private MessageHandler msgHandler;

    private MessageHandler unitHandler;

    public CellMLHandler(CMLFactory cmlFactory, String model_id, boolean unitParsing) {
        this.cmlfactory = cmlFactory;
        CellML = new CModel(model_id);
        generate();
        this.msgHandler = new MessageHandler();
        this.unitHandler = new MessageHandler();
        if (unitParsing) unitParsing();
    }

    public CellMLHandler(String uri_filename, String model_id, boolean unitParsing) throws IOException, SAXException {
        this.cmlfactory = new CMLFactory(model_id);
        this.cmlfactory.Open(uri_filename);
        this.msgHandler = new MessageHandler();
        this.unitHandler = new MessageHandler();
        CellML = new CModel(model_id);
        generate();
        if (unitParsing) unitParsing();
    }

    public CellMLHandler(String uri_filename, String model_id, boolean unitParsing, boolean normalizePiecewise) throws IOException, SAXException {
        this.cmlfactory = new CMLFactory(model_id);
        this.cmlfactory.Open(uri_filename);
        this.msgHandler = new MessageHandler();
        this.unitHandler = new MessageHandler();
        CellML = new CModel(model_id);
        this.normalizePiecewise = normalizePiecewise;
        generate();
        if (unitParsing) unitParsing();
    }

    public void generate() {
        parseUnitsList();
        parseComponentList();
        idHandler = new IDGenerator(CellML);
        parseConnectionList();
        if (normalizePiecewise) {
            normalizePiecewiseEquations();
        }
        normalizeVariableName.normalizeModel(idHandler, CellML);
    }

    private VariableTables generateComponentVariableTable(CComponent component) {
        VariableTables table = new VariableTables();
        CVariable[] varlist = component.variables.values().toArray(new CVariable[component.variables.size()]);
        for (int i = 0; i < varlist.length; i++) {
            String value = varlist[i].getNetworkValue();
            if (value != null) {
                ScalarValue variable = new ScalarValue(varlist[i].getVariableName(), Double.valueOf(value));
                table.variables.put(variable.getID(), variable);
            }
        }
        return table;
    }

    private void unitParsing() {
        for (int i = 0; i < CellML.components.size(); i++) {
            CComponent component = CellML.components.get(i);
            CEquation[] eqlist = component.equations.values().toArray(new CEquation[component.equations.size()]);
            for (int j = 0; j < eqlist.length; j++) {
                if (!isPiecewiseEquation.isPiecewiseEquation(eqlist[j].getAST())) {
                    if (!fillUnitAST.parse(eqlist[j].getAST(), this, component.getID())) {
                        System.out.println(eqlist[j].getID());
                        VariableTables table = generateComponentVariableTable(component);
                        MessageHandler parseMsg = parseUnitTree.parse(eqlist[j].getAST(), table);
                        MessageHandler convertMsg = null;
                        if (parseMsg.getMessageTypeCount(MessageType.ERROR) > 0) {
                            System.out.println(eqlist[j].getID() + " Parsing : Failed");
                            convertMsg = correctUnitTree.convert(eqlist[j].getAST());
                            if (convertMsg.returnMessageList().size() == 0) {
                                System.out.println(eqlist[j].getID() + " : No Conversion required");
                                eqlist[j].setUnitCheckStatus(Unit_Check.Succeed);
                            } else if (convertMsg.getMessageTypeCount(MessageType.ERROR) > 0) {
                                System.out.println(eqlist[j].getID() + " Conversion : Failed");
                                eqlist[j].setUnitCheckStatus(Unit_Check.Unit_Correct_Failed);
                            } else {
                                System.out.println(eqlist[j].getID() + " Conversion : Succeeded");
                                eqlist[j].setUnitCheckStatus(Unit_Check.Unit_Correct_Succeed);
                            }
                        } else {
                            System.out.println(eqlist[j].getID() + " Parsing : Succeeded");
                            eqlist[j].setUnitCheckStatus(Unit_Check.Succeed);
                            parseMsg.printAllMessage();
                        }
                        if (parseMsg.getMessageCount() > 0) {
                            CategoryStructure eq_category = unitHandler.createNewCategory(eqlist[j].getID(), eqlist[j].getID());
                            CategoryStructure unit_check_category = unitHandler.createNewCategory(eq_category, "Unit Checking", "Unit Checking");
                            msgHandler.appendAsCategory(unit_check_category, "CellMLHandler.java", "Units Parsing Check", parseMsg);
                            if (convertMsg != null) {
                                if (convertMsg.returnMessageList().size() > 0) {
                                    CategoryStructure unit_conversion_category = unitHandler.createNewCategory(eq_category, "Unit Conversion", "Unit Conversion");
                                    msgHandler.appendAsCategory(unit_conversion_category, "CellMLHandler.java", "Units Conversion", convertMsg);
                                }
                            }
                        }
                        System.out.println("");
                    } else {
                        System.out.println("ERROR : FullUnitAST failed. Error in CellML Document Units");
                    }
                } else {
                    System.out.println("TODO : Update Piecewise Unit checking @ CellMLHandler.java");
                }
            }
        }
    }

    private void parseUnitsList() {
        NodeList units_list = cmlfactory.getUnitsWorker().returnUnitsList();
        for (int i = 0; i < units_list.getLength(); i++) {
            Element us = (Element) units_list.item(i);
            units uObj = new units(us.getAttribute("name"));
            parseUnitsComponent(us, uObj);
            CellML.units_lib.add(uObj);
        }
        CellML.normalizeUnits();
    }

    private void parseUnitsComponent(Element uElement, units u) {
        NodeList unit_list = cmlfactory.getUnitsWorker().returnUnitList(uElement);
        for (int i = 0; i < unit_list.getLength(); i++) {
            Element uElem = (Element) unit_list.item(i);
            unitDef uObj = new unitDef(uElem.getAttribute("units"));
            if (uElem.hasAttribute("exponent")) {
                uObj.exponent = Double.parseDouble(uElem.getAttribute("exponent"));
            }
            if (uElem.hasAttribute("offset")) {
                uObj.offset = Double.parseDouble(uElem.getAttribute("offset"));
            }
            if (uElem.hasAttribute("prefix")) {
                if (IntParsable(uElem.getAttribute("prefix"))) {
                    uObj.prefix = new PrefixClass(Integer.parseInt(uElem.getAttribute("prefix")));
                } else {
                    uObj.prefix = new PrefixClass(Prefix.valueOf(uElem.getAttribute("prefix")));
                }
            }
            if (uElem.hasAttribute("multiplier")) {
                uObj.multiplier = Double.parseDouble(uElem.getAttribute("multiplier"));
            }
            u.unit_list.add(uObj);
        }
    }

    private boolean IntParsable(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private void parseComponentList() {
        NodeList components = cmlfactory.getComponentWorker().returnComponentList();
        for (int i = 0; i < components.getLength(); i++) {
            Element temp = (Element) components.item(i);
            CComponent comp = CellML.insertComponent(temp.getAttribute("name"));
            comp.setIndex(i);
            parseComponent(comp, temp);
        }
    }

    /**
	 * 
	 * @param comp
	 * @param element <component>
	 */
    private void parseComponent(CComponent comp, Element element) {
        parseVariables(comp, element);
        parseEquations(comp, element);
    }

    private void parseVariables(CComponent comp, Element element) {
        Element[] varList = CellMLComponent.returnAllVariables(element);
        for (int i = 0; i < varList.length; i++) {
            CVariable var = comp.insertVariable(element.getAttribute("name") + "." + varList[i].getAttribute("name"), varList[i].getAttribute("name"));
            var.setType(VariableType.Variable);
            var.component_id = comp.getID();
            var.index = i;
            if (varList[i].hasAttribute("initial_value")) {
                var.value = varList[i].getAttribute("initial_value");
                var.setType(VariableType.Constant);
            }
            if (varList[i].hasAttribute("units")) {
                String unit = varList[i].getAttribute("units");
                if (UnitDefinitions.isBasicUnit(unit)) {
                    UNITS bu = UNITS.valueOf(unit);
                    var.unit = bu.returnUnits();
                } else {
                    units u = CellML.returnUnits(unit);
                    if (u == null) {
                        System.out.println("ERROR : No unit found for : " + unit + " @CellMLHandler.parseVariables");
                    }
                    var.unit = u;
                }
            }
        }
    }

    private void parseEquations(CComponent comp, Element element) {
        Element math = cmlfactory.getComponentWorker().returnMathTag(element);
        if (math != null) {
            Element[] apply = MathMLParser.returnApplyChildList(math);
            for (int i = 0; i < apply.length; i++) {
                try {
                    MEEParser mexp = new MEEParser(apply[i], false);
                    CEquation eq;
                    if (apply[i].hasAttribute("id")) eq = comp.insertEquation(element.getAttribute("name") + "." + apply[i].getAttribute("id")); else {
                        eq = comp.insertEquation(element.getAttribute("name") + ".apply[" + i + "]");
                    }
                    eq.component_id = comp.getID();
                    eq.index = i;
                    AST equation_node = mexp.getTreeRoot();
                    eq.setEquation(equation_node);
                    if (isDiffEquation.isDE(equation_node)) {
                        eq.setType(EquationType.Differential);
                        String[] state_variables = returnStateVariable.returnStateVariable(equation_node);
                        for (int j = 0; j < state_variables.length; j++) {
                            comp.getVariable(comp.getID() + "." + state_variables[j]).setType(VariableType.State_Variable);
                        }
                        String[] ind_variables = returnIndependentVariables.returnIndepenedentVariable(equation_node);
                        for (int j = 0; j < ind_variables.length; j++) {
                            comp.getVariable(comp.getID() + "." + ind_variables[j]).setType(VariableType.Independent_Variable);
                        }
                    } else {
                        eq.setType(EquationType.Expression);
                    }
                } catch (SyntaxError e) {
                    System.out.println("ERROR : CellMLHandler.java " + e);
                }
            }
        }
    }

    public CEquation getEquation(String path) {
        String[] ids = path.split(".");
        if (ids.length != 2) {
            System.out.println("Error :  Path length not 2");
            return null;
        }
        CComponent component = CellML.getComponent(ids[0]);
        return component.getEquations(ids[1]);
    }

    public CVariable getVariable(String path) {
        String[] ids = path.split("\\.");
        if (ids.length != 2) {
            System.out.println("Error :  Path length not 2 : " + path + " : " + ids.length);
            return null;
        }
        CComponent component = CellML.getComponent(ids[0]);
        return component.getVariable(path);
    }

    private void parseConnectionList() {
        NodeList connList = cmlfactory.getConnectionWorker().returnConnectionList();
        for (int i = 0; i < connList.getLength(); i++) {
            Element conn = (Element) connList.item(i);
            parseConnection(conn);
        }
    }

    private void parseConnection(Element connection) {
        Element map_component = CellMLConnection.returnMapComponents(connection);
        String comp1 = "EMPTY";
        String comp2 = "EMPTY";
        if (map_component == null) {
            System.out.println("ERROR : no map_component found @ CellMLHandler.parseConnection()");
        } else {
            comp1 = map_component.getAttribute("component_1");
            comp2 = map_component.getAttribute("component_2");
        }
        NodeList map_var_list = CellMLConnection.returnMapVariableList(connection);
        for (int i = 0; i < map_var_list.getLength(); i++) {
            Element map_var = (Element) map_var_list.item(i);
            String var1 = map_var.getAttribute("variable_1");
            String var2 = map_var.getAttribute("variable_2");
            CVariable varRef1 = CellML.getComponent(comp1).getVariable(comp1 + "." + var1);
            CVariable varRef2 = CellML.getComponent(comp2).getVariable(comp2 + "." + var2);
            if (varRef1 == null) System.out.println("ERROR : variable not found @ CellMLHandler.parseConnection : " + comp1 + "." + var1);
            if (varRef2 == null) System.out.println("ERROR : variable not found @ CellMLHandler.parseConnection : " + comp2 + "." + var2);
            if (!varRef1.containsConnectionMapping(varRef2)) varRef1.insertConnectionMapping(varRef2);
            if (!varRef2.containsConnectionMapping(varRef1)) varRef2.insertConnectionMapping(varRef1);
        }
    }

    public units returnUnits(String id) {
        return CellML.returnUnits(id);
    }

    /**
	 * Extract component componet out of component1.blah
	 * @param path
	 * @return
	 */
    public static String getComponentID(String path) {
        if (!path.contains(".")) return path; else {
            int index = path.indexOf(".");
            return path.substring(0, index);
        }
    }

    private void normalizePiecewiseEquations() {
        for (int i = 0; i < CellML.components.size(); i++) {
            CComponent comp = CellML.components.get(i);
            CEquation[] eq_array = comp.equations.values().toArray(new CEquation[comp.equations.size()]);
            for (int j = 0; j < eq_array.length; j++) {
                CEquation eq = eq_array[j];
                if (isPiecewiseEquation.isPiecewiseEquation(eq.getAST())) {
                    String[] cond_var = extractPiecewiseConditionalVariable.getConditionalVariables(eq.getAST());
                    CVariable var = comp.getVariableByName(cond_var[0]);
                    if (var.getNetworkValue() != null) {
                        VariableTables table = new VariableTables();
                        table.variables.put(var.getVariableName(), new ScalarValue(var.getVariableName(), Double.valueOf(var.getNetworkValue())));
                        AST normalized = generateNormalizePiecewiseEquation.generate(eq.getAST(), table);
                        eq.setEquation(normalized);
                    }
                }
            }
        }
    }

    public MessageHandler getMessageHandler() {
        return msgHandler;
    }

    public boolean hasError() {
        if (getMessageHandler().getMessageTypeCount(MessageType.ERROR) > 0 || getMessageHandler().searchCategory(MessageType.ERROR.toString()) != null) return true; else return false;
    }

    public boolean hasWarnings() {
        if (getMessageHandler().getMessageTypeCount(MessageType.WARNING) > 0 || getMessageHandler().searchCategory(MessageType.WARNING.toString()) != null) return true; else return false;
    }

    /**
	 * Generate Error messages. Error messages are generally generated when the application
	 * can no longer be executed successfully. An error message is generally 
	 * followed by an exception terminating the program
	 * @param message
	 * @param location
	 * @param source
	 */
    protected void insertErrorMessage(String message, String location, String source) {
        getMessageHandler().createNewCategorizedMessage(MessageType.ERROR, message, source, location, null);
    }

    /**
	 * Generate a warning message.
	 * @param message
	 * @param location
	 * @param source
	 */
    protected void insertWarningMessage(String message, String location, String source) {
        getMessageHandler().createNewCategorizedMessage(MessageType.WARNING, message, source, location, null);
    }

    /**
	 * Generate a general message
	 * @param message
	 * @param location
	 * @param source
	 */
    protected void insertGeneralmessage(String message, String location, String source) {
        getMessageHandler().createNewCategorizedMessage(MessageType.INFORMATION, message, source, location, null);
    }

    public MessageHandler getUnitMessageHandler() {
        return unitHandler;
    }

    public void dispose() {
        cmlfactory = null;
        idHandler = null;
        msgHandler.dispose();
        msgHandler = null;
        unitHandler.dispose();
        unitHandler = null;
        CellML = null;
    }
}
