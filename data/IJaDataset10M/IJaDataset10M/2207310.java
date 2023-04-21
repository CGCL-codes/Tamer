package n3_project.helpers;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import unif.Instanciator;
import unif.N3JavaHelper;
import deductions.Namespaces;

/**
 * We leverage on the capability of Drools engine to host any Java object in the
 * Knowledge Base (called Object Oriented RETE) . So we insert Java application
 * objects in the KB along with business objects (the Triple objects). That is,
 * we insert the Java objects (like the ApplicationKB class) directly, without
 * passing through a stage of instantiation .
 * 
 * @author jmv
 * 
 */
public class N3JavaMappingBuiltin extends AbstractN3TranslationPlugin implements N3TranslationPlugin {

    private static final char N3_JAVA_PATH_SEPARATOR = '-';

    private char n3JavaPathSeparator = N3_JAVA_PATH_SEPARATOR;

    private String n3JavaPathSeparatorS;

    private DroolsTripleHandler droolsTripleHandler;

    public static final String javaPrefix = Namespaces.prefixToId.get("java");

    public static final String javaMethodPrefix = Namespaces.prefixToId.get("javam");

    public static final String javaClassPrefix = javaPrefix;

    N3JavaMappingBuiltin() {
        setN3JavaPathSeparator(n3JavaPathSeparator);
    }

    /**
	 * Class membership:
	 * 
	 * <pre>
	 * ?OBJ    a java:p-MyClass.
	 * _:OBJ2  a java:p-MyClass.
	 * pf:OBJ3 a java:p-MyClass.
	 * 
	 * Drools code in the antecedent:
	 * 
	 * </pre>
	 * 
	 * Method or property call:
	 * 
	 * <pre>
	 * ?OBJ java:myMethod ("arg") .
	 * ?OBJP java:myProperty "bla" .
	 * _:OBJ2 java:myProperty "bla" .
	 * 
	 * Drools code in the antecedent
	 *  
	 * </pre>
	 * 
	 * @see n3_project.helpers.N3TranslationPlugin#acceptAntecedent(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, n3_project.helpers.DroolsTripleHandler)
	 */
    @Override
    public boolean acceptAntecedent(String subject, String verb, String object, String source, String ruleId, DroolsTripleHandler droolsTripleHandler) {
        this.droolsTripleHandler = droolsTripleHandler;
        if (N3JavaHelper.isJavaInstanciation(subject, verb, object)) {
            StringBuilder droolsResult = new StringBuilder("  Assignment( ");
            appendAssignmentNameCriterium(subject, object, droolsTripleHandler, droolsResult);
            droolsResult.append(" )\n");
            droolsTripleHandler.appendLHS(droolsResult.toString());
            return true;
        } else if (N3JavaHelper.isJavaPropertyOrIdentifier(verb)) {
            StringBuilder droolsResult = new StringBuilder("  Assignment( ");
            appendAssignmentNameCriterium(subject, object, droolsTripleHandler, droolsResult);
            droolsResult.append(", " + DroolsTripleHandler.n3Variable2DroolsVariable(subject) + "_reference : reference");
            droolsResult.append(", reference.");
            droolsResult.append(Instanciator.getLocalName(verb));
            droolsResult.append(" == ");
            droolsResult.append(object);
            droolsResult.append(" )\n");
            droolsTripleHandler.appendLHS(droolsResult.toString());
            return true;
        } else {
        }
        return false;
    }

    /** append an Assignment Name Criterium to droolsResult, that is one of:
	 * name == $OBJ
	 * $OBJ: name
	 *  
	 * @param subject
	 * @param object
	 * @param droolsTripleHandler 
	 * @param droolsResult
	 */
    private void appendAssignmentNameCriterium(String subject, String object, DroolsTripleHandler droolsTripleHandler, StringBuilder droolsResult) {
        String droolsVariable = DroolsTripleHandler.n3Variable2DroolsVariable(subject);
        String droolsOperand = droolsTripleHandler.n3ValueToOperand(subject);
        if (DroolsTripleHandler.isVariable(subject)) {
            if (droolsTripleHandler.expressionAssignedToVariable(subject)) {
                droolsResult.append("name").append(" == ").append(droolsTripleHandler.getDroolsValueFromN3Variable(subject));
            } else {
                if (droolsTripleHandler.variableAlreadyMet(subject, null)) {
                    droolsResult.append("name").append(" == ").append(droolsOperand);
                    String javaTypeFromN3Variable = droolsTripleHandler.javaTypeFromN3Variable(subject);
                    if (javaTypeFromN3Variable == null) {
                        if (object == null) {
                            System.err.println("N3JavaMappingBuiltin.acceptConsequent(): SEVERE: \n  " + subject + " : this variable should have been declared in N3 before.");
                        } else {
                            System.err.println("N3JavaMappingBuiltin.acceptConsequent(): WARNING: \n  " + subject + " a " + object + ".\n  : this variable was Already Met; put this Java object creation BEFORE its uses.");
                        }
                    }
                } else {
                    droolsResult.append(droolsVariable).append(" : name");
                }
            }
        } else {
            droolsResult.append("name").append(" == ").append(escapeSpecialCharactersAndSurroundWithQuotes(subject));
        }
        String className = null;
        if (object != null) {
            className = makeClassName(object);
            droolsResult.append(", reference.class == ").append(className);
            droolsTripleHandler.assignTypeToN3Variable(subject, className);
        } else {
            droolsResult.append(", ").append(droolsVariable).append("_class : reference.class");
        }
        String var_ref = droolsVariable + "_reference";
        droolsResult.append(", ").append(var_ref).append(" : reference ");
        droolsTripleHandler.assignTypeToN3Variable(var_ref, className);
    }

    private String makeClassName(String object) {
        String className_with_separator = Instanciator.getLocalName(object);
        String className = className_with_separator.replaceAll(n3JavaPathSeparatorS, "\\.");
        return className;
    }

    @Override
    public boolean acceptConsequentList(String subject, String verb, List<String> list, String src, String ruleId, StringBuilder rHSresult) {
        if (N3JavaHelper.isJavaMethod(verb)) {
            rHSresult.append("( ");
            castToActualJavaType(subject, rHSresult, true);
            methodOrPropertyCallee(subject, verb, rHSresult);
            rHSresult.append(Instanciator.getLocalName(verb));
            rHSresult.append("( ");
            for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
                String arg = (String) iterator.next();
                List<String> objectCreation = new ArrayList<String>();
                String argAsDRL = processTripleElementForConsequent(arg, ruleId, objectCreation);
                castToActualJavaType(arg, rHSresult);
                rHSresult.append(argAsDRL);
                if (iterator.hasNext()) {
                    rHSresult.append(", ");
                }
            }
            rHSresult.append(" );\n");
            return true;
        }
        return false;
    }

    /** Cast to actual Java type;
	 * casting will occur only when it is of non primitive type. */
    private void castToActualJavaType(String n3Variable, StringBuilder rHSresult) {
        String javaTypeFromN3Variable = droolsTripleHandler.javaTypeFromN3Variable(n3Variable);
        if (javaTypeFromN3Variable != null) {
            rHSresult.append(" ( ").append(javaTypeFromN3Variable).append(" ) ");
        }
    }

    /**
	 * Cast to actual Java type; a special case is when ?OBJ has not been
	 * referred to as a Java object in the antecedent side. In this case this
	 * expression should be added in the antecedent side (identical to Class
	 * membership Translation in the antecedent):
	 * 
	 * Assignment( name == $OBJ, reference.class == p.MyClass, $OBJ_reference :
	 * reference )
	 * 
	 * or
	 * 
	 * Assignment( name == "<http://mySite.org#OBJ3>",
            $OBJ3_reference :  reference )
	 * 
	 * The class name, e.g. p.MyClass , will be obtained from the method
	 * DroolsTripleHandler.javaTypeFromN3Variable(String var) , provided it has
	 * been assigned before with exactly this name in another rule, or as a fact
	 * through TripleStoreDrools.assign() .
	 * 
	 * @param n3Variable
	 * @param droolsResult
	 * @param searchInWM
	 */
    private void castToActualJavaType(String n3Variable, StringBuilder rHSresult, boolean searchInWM) {
        String javaTypeFromN3Variable = droolsTripleHandler.javaTypeFromN3Variable(n3Variable);
        boolean javaTypeKownFromN3Variable = javaTypeFromN3Variable != null;
        String droolsVariable = "$" + Instanciator.getLocalName(n3Variable);
        String variable_reference = droolsVariable + "_reference";
        if (searchInWM && !javaTypeKownFromN3Variable && !droolsTripleHandler.isVariableAlreadyMet(variable_reference, null)) {
            StringBuilder droolsResult = new StringBuilder("  // from RHS we know it is a Java variable, but type is unknown:\n");
            if (URLHelper.isResource(n3Variable)) {
                droolsResult.append("  Assignment( ");
                appendAssignmentNameCriterium(n3Variable, null, droolsTripleHandler, droolsResult);
                droolsTripleHandler.appendJavaToLHS(droolsResult.toString());
                droolsTripleHandler.variableAlreadyMet(variable_reference, null);
            }
        }
        castToActualJavaType(n3Variable, rHSresult);
    }

    /**
	 * @param term
	 * @param ruleId
	 * @param objectCreation
	 * @return
	 */
    private String processTripleElementForConsequent(String term, String ruleId, List<String> objectCreation) {
        String criterion;
        if (DroolsTripleHandler.isVariable(term)) {
            criterion = droolsTripleHandler.processTripleElementForConsequent(term, ruleId, objectCreation);
            if (droolsTripleHandler.javaTypeFromN3Variable(term) == null || !"String".equals(droolsTripleHandler.javaTypeFromN3Variable(term))) {
                criterion += "_reference";
            }
        } else if (URLHelper.isResource(term)) {
            criterion = DroolsTripleHandler.n3Variable2DroolsVariable(term) + "_reference";
        } else {
            criterion = term;
            System.err.println("N3JavaMappingBuiltin.processTripleElementForConsequent(): WARNING: " + term + " is not a variable and not a Resource");
        }
        return criterion;
    }

    @Override
    public void addRulesOrQueries(StringBuilder result) {
    }

    /**
	 * @see n3_project.helpers.N3TranslationPlugin#acceptConsequent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, n3_project.helpers.DroolsTripleHandler)
	 */
    @Override
    public boolean acceptConsequent(String subject, String verb, String object, String source, String ruleId, DroolsTripleHandler droolsTripleHandler) {
        if (N3JavaHelper.isJavaInstanciation(subject, verb, object)) {
            StringBuilder droolsResult = new StringBuilder();
            List<String> subjectCreation = new ArrayList<String>();
            String subjectVariable = droolsTripleHandler.processTripleElementForConsequent(subject, ruleId, subjectCreation);
            String newIdDeclarations = droolsTripleHandler.declareNewIdInJava(subjectCreation);
            droolsResult.append(newIdDeclarations);
            String className = makeClassName(object);
            droolsResult.append("  ");
            droolsResult.append(className);
            droolsResult.append(" ");
            droolsResult.append(subjectVariable);
            droolsResult.append("_reference");
            try {
                Class<?> subjectClass = Class.forName(className);
                int mods = subjectClass.getModifiers();
                if (!Modifier.isAbstract(mods)) {
                    droolsResult.append(" = new ");
                    droolsResult.append(className);
                    droolsResult.append(" ()");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            droolsResult.append(";\n");
            droolsResult.append("  insert( new Assignment( ");
            droolsResult.append(subjectVariable);
            droolsResult.append(".toString(), ");
            droolsResult.append(subjectVariable);
            droolsResult.append("_reference");
            droolsResult.append(" ));\n");
            droolsTripleHandler.appendRHS(droolsResult.toString());
            return true;
        } else if (N3JavaHelper.isJavaPropertyOrIdentifier(verb)) {
            List<String> objectCreation = new ArrayList<String>();
            String objectVariable = processTripleElementForConsequent(object, ruleId, objectCreation);
            StringBuilder droolsResult = new StringBuilder("  ( ");
            castToActualJavaType(subject, droolsResult, true);
            methodOrPropertyCallee(subject, verb, droolsResult);
            droolsResult.append(makeSetterName(Instanciator.getLocalName(verb)));
            droolsResult.append("( ");
            droolsResult.append(objectVariable);
            droolsResult.append(" );\n");
            droolsTripleHandler.appendRHS(droolsResult.toString());
            return true;
        } else if (N3JavaHelper.isJavaMethod(verb)) {
            System.err.println("N3JavaMappingBuiltin.acceptConsequent(): WARNING: \n" + subject + " " + verb + " " + object + ".\n : prefer N3 list notation for method argument.");
            StringBuilder droolsResult = new StringBuilder();
            droolsResult.append("  ( ");
            castToActualJavaType(subject, droolsResult, true);
            methodOrPropertyCallee(subject, verb, droolsResult);
            droolsResult.append(Instanciator.getLocalName(verb));
            droolsResult.append("( ");
            String arg = object;
            List<String> objectCreation = new ArrayList<String>();
            String argAsDRL = processTripleElementForConsequent(arg, ruleId, objectCreation);
            castToActualJavaType(arg, droolsResult);
            droolsResult.append(argAsDRL);
            droolsResult.append(" );\n");
            droolsTripleHandler.appendRHS(droolsResult.toString());
            return true;
        } else {
        }
        return false;
    }

    /**
	 * @param localName
	 * @return
	 */
    private Object makeSetterName(String propName) {
        return "set" + propName.substring(0, 1).toUpperCase() + propName.substring(1);
    }

    /** generate callee for method Or Property call */
    private void methodOrPropertyCallee(String subject, String verb, StringBuilder droolsResult) {
        droolsResult.append(" ");
        String subjectAsDRL = processTripleElementForConsequent(subject, "", new ArrayList<String>());
        droolsResult.append(subjectAsDRL);
        droolsResult.append(" ) . ");
    }

    char getN3JavaPathSeparator() {
        return n3JavaPathSeparator;
    }

    void setN3JavaPathSeparator(char n3JavaPathSeparator) {
        this.n3JavaPathSeparator = n3JavaPathSeparator;
        n3JavaPathSeparatorS = new String(new char[] { n3JavaPathSeparator });
    }

    /**
	 * escape Quotes and surround with Quotes, except for Boolean and numeric values;
	 * for adding a string or a Java object in Drools
	 * language
	 */
    public static String escapeSpecialCharactersAndSurroundWithQuotes(String s) {
        try {
            if (s == null) {
                return null;
            } else if ("true".equals(s)) {
                return "Boolean.TRUE";
            } else if ("false".equals(s)) {
                return "Boolean.FALSE";
            } else {
                Double.parseDouble(s);
                return s;
            }
        } catch (NumberFormatException e) {
        }
        return "\"" + StringHelper.escapeSpecialCharacters(s) + "\"";
    }
}
