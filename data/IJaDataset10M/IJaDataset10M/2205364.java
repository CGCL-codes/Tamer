package net.sf.saxon.expr.instruct;

import net.sf.saxon.expr.*;
import net.sf.saxon.om.*;
import net.sf.saxon.pattern.NameTest;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.*;
import net.sf.saxon.value.*;
import net.sf.saxon.value.StringValue;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
* The Bindery class holds information about variables and their values. From Saxon 8.1, it is
* used only for global variables: local variables are now held in the XPathContext object.
*
* Variables are identified by a Binding object. Values will always be of class Value.
*/
public final class Bindery {

    private ValueRepresentation[] globals;

    private long[] busy;

    private GlobalParameterSet globalParameters;

    private SlotManager globalVariableMap;

    private Map<GlobalVariable, Set<GlobalVariable>> dependencies = new HashMap<GlobalVariable, Set<GlobalVariable>>();

    private boolean applyConversionRules = true;

    /**
     * Define how many slots are needed for global variables
     * @param map the SlotManager that keeps track of slot allocation for global variables.
    */
    public void allocateGlobals(SlotManager map) {
        globalVariableMap = map;
        int n = map.getNumberOfVariables() + 1;
        globals = new ValueRepresentation[n];
        busy = new long[n];
        for (int i = 0; i < n; i++) {
            globals[i] = null;
            busy[i] = -1L;
        }
    }

    /**
     * Say whether the function conversion rules should be applied to supplied
     * parameter values. For example, this allows an integer to be supplied as the value
     * for a parameter where the expected type is xs:double. The default is true.
     * @param convert true if function conversion rules are to be applied to supplied
     * values; if false, the supplied value must match the required type exactly.
     * @since 9.3
     */
    public void setApplyFunctionConversionRulesToExternalVariables(boolean convert) {
        applyConversionRules = convert;
    }

    /**
     * Ask whether the function conversion rules should be applied to supplied
     * parameter values. For example, this allows an integer to be supplied as the value
     * for a parameter where the expected type is xs:double. The default is true.
     * @return true if function conversion rules are to be applied to supplied
     * values; if false, the supplied value must match the required type exactly.
     * @since 9.3
     */
    public boolean isApplyFunctionConversionRulesToExternalVariables() {
        return applyConversionRules;
    }

    /**
    * Define global parameters
    * @param params The ParameterSet passed in by the user, eg. from the command line
    */
    public void defineGlobalParameters(GlobalParameterSet params) {
        globalParameters = params;
    }

    /**
     * Use global parameter. This is called when a global xsl:param element is processed.
     * If a parameter of the relevant name was supplied, it is bound to the xsl:param element.
     * Otherwise the method returns false, so the xsl:param default will be evaluated.
     * @param qName The name of the parameter
     * @param slot The slot number allocated to the parameter
     * @param requiredType The declared type of the parameter
     * @param context the XPath dynamic evaluation context
     * @return true if a parameter of this name was supplied, false if not
     */
    public boolean useGlobalParameter(StructuredQName qName, int slot, SequenceType requiredType, XPathContext context) throws XPathException {
        if (globals != null && globals[slot] != null) {
            return true;
        }
        if (globalParameters == null) {
            return false;
        }
        Object obj = globalParameters.get(qName);
        if (obj == null) {
            return false;
        }
        if (obj instanceof DocumentInfo) {
            String systemId = ((DocumentInfo) obj).getSystemId();
            try {
                if (systemId != null && new URI(systemId).isAbsolute()) {
                    DocumentPool pool = context.getController().getDocumentPool();
                    if (pool.find(systemId) == null) {
                        pool.add(((DocumentInfo) obj), systemId);
                    }
                }
            } catch (URISyntaxException err) {
            }
        }
        ValueRepresentation val;
        if (obj instanceof ValueRepresentation) {
            val = (ValueRepresentation) obj;
        } else {
            JPConverter converter = JPConverter.allocate(obj.getClass(), context.getConfiguration());
            val = converter.convert(obj, context);
        }
        if (val == null) {
            val = EmptySequence.getInstance();
        }
        if (applyConversionRules) {
            val = applyFunctionConversionRules(val, requiredType, context);
        }
        XPathException err = TypeChecker.testConformance(val, requiredType, context);
        if (err != null) {
            throw err;
        }
        globals[slot] = val;
        return true;
    }

    /**
     * Apply the function conversion rules to a value, given a required type.
     * @param value a value to be converted
     * @param requiredType the required type
     * @param context the conversion context
     * @return the converted value
     * @throws XPathException if the value cannot be converted to the required type
     */
    public static Value applyFunctionConversionRules(ValueRepresentation value, SequenceType requiredType, final XPathContext context) throws XPathException {
        final TypeHierarchy th = context.getConfiguration().getTypeHierarchy();
        final ItemType requiredItemType = requiredType.getPrimaryType();
        ItemType suppliedItemType = (value instanceof NodeInfo ? new NameTest(((NodeInfo) value)) : ((Value) value).getItemType(th));
        SequenceIterator iterator = Value.asIterator(value);
        if (requiredItemType.isAtomicType()) {
            if (!suppliedItemType.isAtomicType()) {
                iterator = Atomizer.getAtomizingIterator(iterator);
                suppliedItemType = suppliedItemType.getAtomizedItemType();
            }
            if (th.relationship(suppliedItemType, BuiltInAtomicType.UNTYPED_ATOMIC) != TypeHierarchy.DISJOINT) {
                ItemMappingFunction converter = new ItemMappingFunction() {

                    public Item mapItem(Item item) throws XPathException {
                        if (item instanceof UntypedAtomicValue) {
                            ConversionResult val = ((UntypedAtomicValue) item).convert((AtomicType) requiredItemType, true, context.getConfiguration().getConversionRules());
                            if (val instanceof ValidationFailure) {
                                ValidationFailure vex = (ValidationFailure) val;
                                throw vex.makeException();
                            }
                            return (AtomicValue) val;
                        } else {
                            return item;
                        }
                    }
                };
                iterator = new ItemMappingIterator(iterator, converter, true);
            }
            if (requiredItemType.equals(BuiltInAtomicType.DOUBLE)) {
                ItemMappingFunction promoter = new ItemMappingFunction() {

                    public Item mapItem(Item item) throws XPathException {
                        if (item instanceof NumericValue) {
                            return ((AtomicValue) item).convert(BuiltInAtomicType.DOUBLE, true, context.getConfiguration().getConversionRules()).asAtomic();
                        } else {
                            throw new XPathException("Cannot promote non-numeric value to xs:double", "XPTY0004", context);
                        }
                    }
                };
                iterator = new ItemMappingIterator(iterator, promoter, true);
            } else if (requiredItemType.equals(BuiltInAtomicType.FLOAT)) {
                ItemMappingFunction promoter = new ItemMappingFunction() {

                    public Item mapItem(Item item) throws XPathException {
                        if (item instanceof DoubleValue) {
                            throw new XPathException("Cannot promote xs:double value to xs:float", "XPTY0004", context);
                        } else if (item instanceof NumericValue) {
                            return ((AtomicValue) item).convert(BuiltInAtomicType.FLOAT, true, context.getConfiguration().getConversionRules()).asAtomic();
                        } else {
                            throw new XPathException("Cannot promote non-numeric value to xs:float", "XPTY0004", context);
                        }
                    }
                };
                iterator = new ItemMappingIterator(iterator, promoter, true);
            }
            if (requiredItemType.equals(BuiltInAtomicType.STRING) && th.relationship(suppliedItemType, BuiltInAtomicType.ANY_URI) != TypeHierarchy.DISJOINT) {
                ItemMappingFunction promoter = new ItemMappingFunction() {

                    public Item mapItem(Item item) throws XPathException {
                        if (item instanceof AnyURIValue) {
                            return new StringValue(item.getStringValueCS());
                        } else {
                            return item;
                        }
                    }
                };
                iterator = new ItemMappingIterator(iterator, promoter, true);
            }
        }
        return Value.asValue(SequenceExtent.makeSequenceExtent(iterator));
    }

    /**
    * Provide a value for a global variable
    * @param binding identifies the variable
    * @param value the value of the variable
    */
    public void defineGlobalVariable(GlobalVariable binding, ValueRepresentation value) {
        globals[binding.getSlotNumber()] = value;
    }

    /**
     * Set/Unset a flag to indicate that a particular global variable is currently being
     * evaluated. Note that this code is not synchronized, so there is no absolute guarantee that
     * two threads will not both evaluate the same global variable; however, apart from wasted time,
     * it is harmless if they do.
     * @param binding the global variable in question
     * @return true if evaluation of the variable should proceed; false if it is found that the variable has now been
     * evaluated in another thread.
     * @throws net.sf.saxon.trans.XPathException If an attempt is made to set the flag when it is already set, this means
     * the definition of the variable is circular.
    */
    public boolean setExecuting(GlobalVariable binding) throws XPathException {
        long thisThread = Thread.currentThread().getId();
        int slot = binding.getSlotNumber();
        long busyThread = busy[slot];
        if (busyThread != -1L) {
            if (busyThread == thisThread) {
                throw new XPathException.Circularity("Circular definition of variable " + binding.getVariableQName().getDisplayName());
            } else {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(20 * i);
                    } catch (InterruptedException e) {
                    }
                    if (busy[slot] == -1L) {
                        return false;
                    }
                }
                return true;
            }
        }
        busy[slot] = thisThread;
        return true;
    }

    /**
     * Indicate that a global variable is not currently being evaluated
     * @param binding the global variable
     */
    public void setNotExecuting(GlobalVariable binding) {
        int slot = binding.getSlotNumber();
        busy[slot] = -1L;
    }

    /**
     * Save the value of a global variable, and mark evaluation as complete.
     * @param binding the global variable in question
     * @param value the value that this thread has obtained by evaluating the variable
     * @return the value actually given to the variable. Exceptionally this will be different from the supplied
     * value if another thread has evaluated the same global variable concurrently. The returned value should be
     * used in preference, to ensure that all threads agree on the value. They could be different if for example
     * the variable is initialized using the collection() function.
    */
    public synchronized ValueRepresentation saveGlobalVariableValue(GlobalVariable binding, ValueRepresentation value) {
        int slot = binding.getSlotNumber();
        if (globals[slot] != null) {
            return globals[slot];
        } else {
            busy[slot] = -1L;
            globals[slot] = value;
            return value;
        }
    }

    /**
    * Get the value of a global variable
    * @param binding the Binding that establishes the unique instance of the variable
    * @return the Value of the variable if defined, null otherwise.
    */
    public ValueRepresentation getGlobalVariableValue(GlobalVariable binding) {
        return globals[binding.getSlotNumber()];
    }

    /**
    * Get the value of a global variable whose slot number is known
    * @param slot the slot number of the required variable
    * @return the Value of the variable if defined, null otherwise.
    */
    public ValueRepresentation getGlobalVariable(int slot) {
        return globals[slot];
    }

    /**
    * Set the value of a global variable whose slot number is known
    * @param slot the slot number of the required variable
    * @param value the Value of the variable if defined, null otherwise.
    */
    public void setGlobalVariable(int slot, ValueRepresentation value) {
        globals[slot] = value;
    }

    /**
     * Assign a new value to a global variable. Supports saxon:assign.
     * @param binding identifies the global variable or parameter
     * @param value the value to be assigned to the variable
    */
    public void assignGlobalVariable(GlobalVariable binding, ValueRepresentation value) {
        defineGlobalVariable(binding, value);
    }

    /**
     * Get the Global Variable Map, containing the mapping of variable names (fingerprints)
     * to slot numbers. This is provided for use by debuggers.
     * @return the SlotManager containing information about the assignment of slot numbers
     * to global variables and parameters
     */
    public SlotManager getGlobalVariableMap() {
        return globalVariableMap;
    }

    /**
     * Get all the global variables, as an array. This is provided for use by debuggers
     * that know the layout of the global variables within the array.
     * @return the array of global varaibles.
     */
    public ValueRepresentation[] getGlobalVariables() {
        return globals;
    }

    /**
     * Register the dependency of one variable ("one") upon another ("two"), throwing an exception if this would establish
     * a cycle of dependencies.
     * @param one the first (dependent) variable
     * @param two the second (dependee) variable
     * @throws XPathException if adding this dependency creates a cycle of dependencies among global variables.
     */
    public synchronized void registerDependency(GlobalVariable one, GlobalVariable two) throws XPathException {
        if (one == two) {
            throw new XPathException.Circularity("Circular dependency among global variables: " + one.getVariableQName().getDisplayName() + " depends on its own value");
        }
        Set<GlobalVariable> transitiveDependencies = dependencies.get(two);
        if (transitiveDependencies != null) {
            if (transitiveDependencies.contains(one)) {
                throw new XPathException.Circularity("Circular dependency among variables: " + one.getVariableQName().getDisplayName() + " depends on the value of " + two.getVariableQName().getDisplayName() + ", which depends directly or indirectly on the value of " + one.getVariableQName().getDisplayName());
            }
            for (GlobalVariable var : transitiveDependencies) {
                registerDependency(one, var);
            }
        }
        Set<GlobalVariable> existingDependencies = dependencies.get(one);
        if (existingDependencies == null) {
            existingDependencies = new HashSet<GlobalVariable>();
            dependencies.put(one, existingDependencies);
        }
        existingDependencies.add(two);
    }
}
