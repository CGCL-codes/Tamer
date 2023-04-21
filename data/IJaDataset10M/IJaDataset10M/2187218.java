package de.mangas.modeltransformator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.expressions.BooleanLiteralExp;
import org.eclipse.ocl.expressions.IntegerLiteralExp;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.expressions.PropertyCallExp;
import org.eclipse.ocl.expressions.RealLiteralExp;
import org.eclipse.ocl.expressions.Variable;
import org.eclipse.ocl.expressions.VariableExp;
import org.eclipse.ocl.uml.OCL;
import org.eclipse.ocl.uml.UMLEnvironmentFactory;
import org.eclipse.ocl.utilities.AbstractVisitor;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.StructuralFeature;

public class IntervalCounter extends AbstractVisitor<String, Classifier, Operation, StructuralFeature, EnumerationLiteral, Parameter, EObject, EObject, EObject, Constraint> {

    private static boolean DEBUG = true;

    private static final String BOOLEAN = "Boolean";

    private static final String INTEGER = "Integer";

    @SuppressWarnings("unused")
    private static final String PLUS = "+";

    private static final String LESS_THAN = "<";

    private static final String LESS_THAN_EQUAL = "<=";

    private static final String GREATER_THAN = ">";

    private static final String GREATER_THAN_EQUAL = ">=";

    private static final String NOT_EQUAL = "<>";

    @SuppressWarnings("unused")
    private static final String MINUS = "-";

    private static final String EQUAL = "=";

    @SuppressWarnings("unused")
    private static final String MULT = "*";

    public List<String> variablen = new ArrayList<String>();

    public List<String> logVariablen = new ArrayList<String>();

    public java.util.HashMap<String, de.mangas.modeltransformator.util.ALAImpl> atomareBedingungen = new java.util.HashMap<String, ALAImpl>();

    /**
	 *liste mit partitionen 
	 */
    public java.util.HashMap<String, java.util.List<Partition>> partitions = new java.util.HashMap<String, List<Partition>>();

    public java.util.HashMap<String, Partition> mapPartitions = new HashMap<String, Partition>();

    Classifier context;

    Operation specOp;

    IntervalCounter(Classifier context, java.util.HashMap<String, java.util.List<Partition>> partitions) {
        super();
        this.context = context;
        this.specOp = null;
        this.partitions = partitions;
    }

    public IntervalCounter(Classifier context, Operation specOperation, HashMap<String, List<Partition>> partitions) {
        super();
        this.context = context;
        this.specOp = specOperation;
        this.partitions = partitions;
    }

    void getPartition(org.eclipse.ocl.uml.OCLExpression oclExpression, java.util.HashMap<String, java.util.List<Partition>> lPartitions) {
        StringBuilder sb;
        String sourceResult;
        String argumentResult;
        Partition tempPart;
        org.eclipse.ocl.uml.impl.OperationCallExpImpl opearationCallExp = (org.eclipse.ocl.uml.impl.OperationCallExpImpl) oclExpression;
        org.eclipse.ocl.uml.OCLExpression source = (org.eclipse.ocl.uml.OCLExpression) opearationCallExp.getSource();
        org.eclipse.ocl.uml.OCLExpression argument = (org.eclipse.ocl.uml.OCLExpression) opearationCallExp.getArgument().get(0);
        int opCode = opearationCallExp.getOperationCode();
        switch(opCode) {
            case org.eclipse.ocl.utilities.PredefinedType.NOT:
                getPartition(oclExpression, lPartitions);
                break;
            case org.eclipse.ocl.utilities.PredefinedType.LESS_THAN:
                if (DEBUG) {
                    System.out.print("Opeartioncode equals LESS_THAN ");
                    System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                }
                sourceResult = source.toString();
                argumentResult = argument.toString();
                sb = new StringBuilder();
                sb.append(sourceResult);
                sb.append(" " + LESS_THAN + " ");
                sb.append(argumentResult);
                tempPart = createPartition2(sourceResult, argumentResult, sb, LESS_THAN);
                addPartition(tempPart, lPartitions);
                break;
            case org.eclipse.ocl.utilities.PredefinedType.LESS_THAN_EQUAL:
                if (DEBUG) {
                    System.out.print("Opeartioncode equals LESS_THAN_EQUAL ");
                    System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                }
                sourceResult = source.toString();
                argumentResult = argument.toString();
                sb = new StringBuilder();
                sb.append(sourceResult);
                sb.append(" " + LESS_THAN_EQUAL + " ");
                sb.append(argumentResult);
                tempPart = createPartition2(sourceResult, argumentResult, sb, LESS_THAN_EQUAL);
                addPartition(tempPart, lPartitions);
                break;
            case org.eclipse.ocl.utilities.PredefinedType.GREATER_THAN:
                if (DEBUG) {
                    System.out.print("Opeartioncode equals GREATER_THAN  ");
                    System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                }
                sourceResult = source.toString();
                argumentResult = argument.toString();
                sb = new StringBuilder();
                sb.append(sourceResult);
                sb.append(" " + GREATER_THAN + " ");
                sb.append(argumentResult);
                tempPart = createPartition2(sourceResult, argumentResult, sb, GREATER_THAN);
                addPartition(tempPart, lPartitions);
                break;
            case org.eclipse.ocl.utilities.PredefinedType.GREATER_THAN_EQUAL:
                if (DEBUG) {
                    System.out.print("Opeartioncode equals GREATER_THAN_EQUAL ");
                    System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                }
                sourceResult = source.toString();
                argumentResult = argument.toString();
                sb = new StringBuilder();
                sb.append(sourceResult);
                sb.append(" " + GREATER_THAN_EQUAL + " ");
                sb.append(argumentResult);
                tempPart = createPartition2(sourceResult, argumentResult, sb, GREATER_THAN_EQUAL);
                addPartition(tempPart, lPartitions);
                break;
            case org.eclipse.ocl.utilities.PredefinedType.EQUAL:
                if (DEBUG) {
                    System.out.print("Opeartioncode equals EQUAL ");
                    System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                }
                sourceResult = source.toString();
                argumentResult = argument.toString();
                sb = new StringBuilder();
                sb.append(sourceResult);
                sb.append(" " + EQUAL + " ");
                sb.append(argumentResult);
                tempPart = createPartition2(sourceResult, argumentResult, sb, EQUAL);
                addPartition(tempPart, lPartitions);
                break;
        }
    }

    public void getPartitionFromGuard(org.eclipse.ocl.uml.OCLExpression oclExpression, List<String> partitionen) throws Exception {
        StringBuilder sb;
        String referredOparationName;
        if (oclExpression instanceof org.eclipse.ocl.uml.impl.OperationCallExpImpl) {
            org.eclipse.ocl.uml.impl.OperationCallExpImpl opearationCallExp = (org.eclipse.ocl.uml.impl.OperationCallExpImpl) oclExpression;
            referredOparationName = opearationCallExp.getReferredOperation().getName();
            System.out.println("CallExpression: " + referredOparationName);
            org.eclipse.ocl.uml.OCLExpression source = (org.eclipse.ocl.uml.OCLExpression) opearationCallExp.getSource();
            org.eclipse.ocl.uml.OCLExpression argument = (org.eclipse.ocl.uml.OCLExpression) opearationCallExp.getArgument().get(0);
            @SuppressWarnings("unused") java.util.HashMap<String, java.util.List<Partition>> lPartitions_Source = new HashMap<String, List<Partition>>();
            @SuppressWarnings("unused") java.util.HashMap<String, java.util.List<Partition>> lPartitions_Argument = new HashMap<String, List<Partition>>();
            List<String> partitionen_Source = new ArrayList<String>();
            List<String> partitionen_Argument = new ArrayList<String>();
            if (referredOparationName.equalsIgnoreCase("and")) {
                getPartitionFromGuard(source, partitionen_Source);
                getPartitionFromGuard(argument, partitionen_Argument);
                for (String exp : partitionen_Source) if (partitionen.contains(exp) == false) partitionen.add(exp);
                for (String exp : partitionen_Argument) if (partitionen.contains(exp) == false) partitionen.add(exp);
            } else {
                int opCode = opearationCallExp.getOperationCode();
                String sourceResult;
                String argumentResult;
                @SuppressWarnings("unused") Partition tempPart;
                switch(opCode) {
                    case org.eclipse.ocl.utilities.PredefinedType.NOT:
                        getPartitionFromGuard(oclExpression, partitionen);
                        break;
                    case org.eclipse.ocl.utilities.PredefinedType.LESS_THAN:
                        if (DEBUG) {
                            System.out.print("Opeartioncode equals LESS_THAN ");
                            System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                        }
                        sourceResult = source.toString();
                        argumentResult = argument.toString();
                        sb = new StringBuilder();
                        sb.append(sourceResult);
                        sb.append(" " + LESS_THAN + " ");
                        sb.append(argumentResult);
                        if (partitionen.contains(sb.toString()) == false) {
                            partitionen.add(sb.toString());
                        }
                        break;
                    case org.eclipse.ocl.utilities.PredefinedType.LESS_THAN_EQUAL:
                        if (DEBUG) {
                            System.out.print("Opeartioncode equals LESS_THAN_EQUAL ");
                            System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                        }
                        sourceResult = source.toString();
                        argumentResult = argument.toString();
                        sb = new StringBuilder();
                        sb.append(sourceResult);
                        sb.append(" " + LESS_THAN_EQUAL + " ");
                        sb.append(argumentResult);
                        if (partitionen.contains(sb.toString()) == false) {
                            partitionen.add(sb.toString());
                        }
                        break;
                    case org.eclipse.ocl.utilities.PredefinedType.GREATER_THAN:
                        if (DEBUG) {
                            System.out.print("Opeartioncode equals GREATER_THAN  ");
                            System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                        }
                        sourceResult = source.toString();
                        argumentResult = argument.toString();
                        sb = new StringBuilder();
                        sb.append(sourceResult);
                        sb.append(" " + GREATER_THAN + " ");
                        sb.append(argumentResult);
                        if (partitionen.contains(sb.toString()) == false) {
                            partitionen.add(sb.toString());
                        }
                        break;
                    case org.eclipse.ocl.utilities.PredefinedType.GREATER_THAN_EQUAL:
                        if (DEBUG) {
                            System.out.print("Opeartioncode equals GREATER_THAN_EQUAL ");
                            System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                        }
                        sourceResult = source.toString();
                        argumentResult = argument.toString();
                        sb = new StringBuilder();
                        sb.append(sourceResult);
                        sb.append(" " + GREATER_THAN_EQUAL + " ");
                        sb.append(argumentResult);
                        if (partitionen.contains(sb.toString()) == false) {
                            partitionen.add(sb.toString());
                        }
                        break;
                    case org.eclipse.ocl.utilities.PredefinedType.EQUAL:
                        if (DEBUG) {
                            System.out.print("Opeartioncode equals EQUAL ");
                            System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                        }
                        sourceResult = source.toString();
                        argumentResult = argument.toString();
                        sb = new StringBuilder();
                        sb.append(sourceResult);
                        sb.append(" " + EQUAL + " ");
                        sb.append(argumentResult);
                        if (source.getType().getName().equals("Boolean") || argument.getType().getName().equals("Boolean")) {
                            Exception ex = new Exception("keine relation");
                            throw ex;
                        }
                        if (partitionen.contains(sb.toString()) == false) {
                            partitionen.add(sb.toString());
                        }
                        break;
                    case org.eclipse.ocl.utilities.PredefinedType.NOT_EQUAL:
                        if (DEBUG) {
                            System.out.print("Opeartioncode equals NOT_EQUAL ");
                            System.out.println("Operation.type: " + opearationCallExp.getType().getName());
                        }
                        sourceResult = source.toString();
                        argumentResult = argument.toString();
                        sb = new StringBuilder();
                        sb.append(sourceResult);
                        sb.append(" " + NOT_EQUAL + " ");
                        sb.append(argumentResult);
                        if (partitionen.contains(sb.toString()) == false) {
                            partitionen.add(sb.toString());
                        }
                        break;
                }
            }
        }
    }

    @Override
    protected String handleVariable(Variable<Classifier, Parameter> variable, String initResult) {
        System.out.println("Vname:" + variable.getName() + " Type: " + variable.getType().toString());
        return null;
    }

    /**
	 * bildet aus einer relation eine partition
	 * @param sourceResult linke h�lfte
	 * @param sourceResults rechte h�lfte
	 * @param propertyCallBuilder gesamter ausdruck
	 * @param boundaryType operator
	 */
    @SuppressWarnings("unused")
    private void createPartition(String sourceResult, String sourceResults, StringBuilder propertyCallBuilder, String boundaryType) {
        String v1;
        String v2;
        String v_temp;
        Integer value1;
        Integer value2;
        Integer value_temp;
        v1 = getProperty(sourceResult);
        v2 = getProperty(sourceResults);
        v_temp = null;
        if (v1 != null && v2 == null) v_temp = v1; else if (v1 == null && v2 != null) v_temp = v2;
        value1 = getBoundaryValue(sourceResult);
        value2 = getBoundaryValue(sourceResults);
        value_temp = null;
        if (value1 != null && value2 == null) value_temp = value1; else if (value1 == null && value2 != null) value_temp = value2;
        if (v_temp != null && value_temp != null) {
            Boundary.BoundaryType bt = null;
            if (boundaryType.equals(LESS_THAN) || boundaryType.equals(GREATER_THAN)) bt = Boundary.BoundaryType.EXCLUDING; else if (boundaryType.equals(LESS_THAN_EQUAL) || boundaryType.equals(GREATER_THAN_EQUAL) || boundaryType.equals(EQUAL)) bt = Boundary.BoundaryType.INCLUDING;
            if (boundaryType.equals(LESS_THAN) || boundaryType.equals(LESS_THAN_EQUAL)) {
                if (v1 != null) {
                    Boundary upper = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, upper, null);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    addPartition(temp_part, partitions);
                } else {
                    Boundary lower = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, null, lower);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    addPartition(temp_part, partitions);
                }
            } else if (boundaryType.equals(GREATER_THAN) || boundaryType.equals(GREATER_THAN_EQUAL)) {
                if (v1 != null) {
                    Boundary lower = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, null, lower);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    addPartition(temp_part, partitions);
                } else {
                    Boundary upper = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, upper, null);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    addPartition(temp_part, partitions);
                }
            } else if (boundaryType.equals(EQUAL)) {
                Boundary lower = new Boundary(value_temp, bt);
                Boundary upper = lower;
                Partition temp_part = new Partition(v_temp, upper, lower);
                temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                addPartition(temp_part, partitions);
            }
        }
    }

    private Partition createPartition2(String sourceResult, String sourceResults, StringBuilder propertyCallBuilder, String boundaryType) {
        String v1;
        String v2;
        String v_temp;
        Integer value1;
        Integer value2;
        Integer value_temp;
        v1 = getProperty(sourceResult);
        v2 = getProperty(sourceResults);
        v_temp = null;
        if (v1 != null && v2 == null) v_temp = v1; else if (v1 == null && v2 != null) v_temp = v2;
        value1 = getBoundaryValue(sourceResult);
        value2 = getBoundaryValue(sourceResults);
        value_temp = null;
        if (value1 != null && value2 == null) value_temp = value1; else if (value1 == null && value2 != null) value_temp = value2;
        if (v_temp != null && value_temp != null) {
            Boundary.BoundaryType bt = null;
            if (boundaryType.equals(LESS_THAN) || boundaryType.equals(GREATER_THAN)) bt = Boundary.BoundaryType.EXCLUDING; else if (boundaryType.equals(LESS_THAN_EQUAL) || boundaryType.equals(GREATER_THAN_EQUAL) || boundaryType.equals(EQUAL)) bt = Boundary.BoundaryType.INCLUDING;
            if (boundaryType.equals(LESS_THAN) || boundaryType.equals(LESS_THAN_EQUAL)) {
                if (v1 != null) {
                    Boundary upper = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, upper, null);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    return temp_part;
                } else {
                    Boundary lower = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, null, lower);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    return temp_part;
                }
            } else if (boundaryType.equals(GREATER_THAN) || boundaryType.equals(GREATER_THAN_EQUAL)) {
                if (v1 != null) {
                    Boundary lower = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, null, lower);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    return temp_part;
                } else {
                    Boundary upper = new Boundary(value_temp, bt);
                    Partition temp_part = new Partition(v_temp, upper, null);
                    temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                    return temp_part;
                }
            } else if (boundaryType.equals(EQUAL)) {
                Boundary lower = new Boundary(value_temp, bt);
                Boundary upper = lower;
                Partition temp_part = new Partition(v_temp, upper, lower);
                temp_part.getAtomareBed().add(propertyCallBuilder.toString());
                return temp_part;
            }
        }
        return null;
    }

    private void addPartition(Partition tempPart, HashMap<String, List<Partition>> partitions2) {
        if (partitions2.containsKey(tempPart.getVar())) {
            if (partitions2.get(tempPart.getVar()).contains(tempPart) == false) {
                partitions2.get(tempPart.getVar()).add(tempPart);
            }
        } else {
            java.util.List<Partition> tempList = new java.util.ArrayList<Partition>();
            tempList.add(tempPart);
            partitions2.put(tempPart.getVar(), tempList);
        }
    }

    private String getProperty(String sourceResult) {
        String _result = null;
        GetPropertyVisitor myVisitor = new GetPropertyVisitor();
        try {
            OCLExpression<?> oExpression = null;
            OCL oOcl = org.eclipse.ocl.uml.OCL.newInstance(new UMLEnvironmentFactory(new ResourceSetImpl()));
            OCL.Helper helper = oOcl.createOCLHelper();
            helper.setValidating(false);
            if (this.specOp == null) helper.setContext(this.context); else helper.setOperationContext(this.context, this.specOp);
            oExpression = helper.createQuery(sourceResult);
            oExpression.accept(myVisitor);
            if (myVisitor.variablen.size() == 1 && myVisitor.properties.size() == 0) _result = myVisitor.variablen.get(0); else if (myVisitor.variablen.size() == 0 && myVisitor.properties.size() == 1) {
                _result = myVisitor.properties.get(0);
            } else {
                String errMsg = "InterValCounter.getProperty(): Problem beim finden der richtigen Variable oder Property zum Erstellen einer Partition";
                System.err.println(errMsg);
                return null;
            }
        } catch (ParserException e) {
            myVisitor.variablen = null;
            e.printStackTrace();
        }
        return _result;
    }

    private Integer getBoundaryValue(String sourceResult) {
        Integer value = null;
        GetIntegerVisitor myVisitor = new GetIntegerVisitor();
        try {
            OCLExpression<?> oExpression = null;
            OCL oOcl = org.eclipse.ocl.uml.OCL.newInstance(new UMLEnvironmentFactory(new ResourceSetImpl()));
            OCL.Helper helper = oOcl.createOCLHelper();
            if (this.specOp == null) helper.setContext(this.context); else helper.setOperationContext(this.context, this.specOp);
            helper.setValidating(false);
            oExpression = helper.createQuery(sourceResult);
            oExpression.accept(myVisitor);
            value = myVisitor.grenzwert;
        } catch (ParserException e) {
            myVisitor.values = null;
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public String visitRealLiteralExp(RealLiteralExp<Classifier> realLiteral) {
        return null;
    }

    @Override
    public String visitBooleanLiteralExp(BooleanLiteralExp<Classifier> booleanLiteral) {
        return null;
    }

    @Override
    public String visitIntegerLiteralExp(IntegerLiteralExp<Classifier> integerLiteral) {
        return integerLiteral.getIntegerSymbol().toString();
    }

    @Override
    public String visitVariableExp(VariableExp<Classifier, Parameter> vExp) {
        Variable<Classifier, Parameter> vd = vExp.getReferredVariable();
        org.eclipse.uml2.uml.Type variableType = vd.getType();
        if (variableType instanceof PrimitiveType) {
            if (variableType.getName().equals(BOOLEAN)) {
                if (logVariablen.contains(vd.getName()) == false) {
                    logVariablen.add(vd.getName());
                    ALAImpl temp = new ALAImpl(vd.getName(), vd.getName());
                    atomareBedingungen.put(temp.getAusdruck(), temp);
                }
            }
        }
        return vd.getName();
    }

    @Override
    public String visitPropertyCallExp(PropertyCallExp<Classifier, StructuralFeature> callExp) {
        org.eclipse.uml2.uml.Property p = null;
        String value = null;
        if (callExp.getReferredProperty() != null) {
            p = (Property) callExp.getReferredProperty();
            org.eclipse.uml2.uml.Type propertyType = p.getType();
            if (propertyType instanceof PrimitiveType) {
                if (propertyType.getName().equals(BOOLEAN)) {
                    if (logVariablen.contains(p.getName()) == false) {
                        logVariablen.add(p.getName());
                        ALAImpl temp = new ALAImpl(p.getName(), p.getName());
                        atomareBedingungen.put(temp.getAusdruck(), temp);
                        value = p.getName();
                    }
                } else if (propertyType.getName().equals(INTEGER)) {
                    if (p.isSetDefault() && p.isReadOnly()) {
                        value = p.getName();
                    } else value = p.getName();
                }
            }
        }
        result = value;
        return value;
    }

    public void getPartitionenAusRelationen(List<List<String>> tempL, List<HashMap<String, List<Partition>>> tempHashMapPartitionen, OCL.Helper helper) {
        org.eclipse.ocl.uml.OCLExpression oclExpression = null;
        for (List<String> zeile : tempL) {
            java.util.HashMap<String, List<Partition>> partitionAusZeile = new HashMap<String, List<Partition>>();
            for (String relation : zeile) {
                try {
                    oclExpression = helper.createQuery(relation);
                    this.getPartition(oclExpression, partitionAusZeile);
                } catch (ParserException e) {
                    e.printStackTrace();
                }
            }
            if (partitionAusZeile.keySet().size() > 0) {
                tempHashMapPartitionen.add(partitionAusZeile);
            }
        }
    }
}
