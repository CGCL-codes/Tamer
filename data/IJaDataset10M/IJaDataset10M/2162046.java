package tcl.lang;

import java.util.*;

public class Var {

    /**
     * Flag bits for variables. The first three (SCALAR, ARRAY, and
     * LINK) are mutually exclusive and give the "type" of the variable.
     * UNDEFINED is independent of the variable's type. Note that
     * using an int field here instead of allocating 9 boolean
     * members makes the resulting object take up less memory.
     * If there were only 8 boolean fields then the size of
     * the Var object would be the same.
     *
     * SCALAR -			1 means this is a scalar variable and not
     *				an array or link. The tobj field contains
     *				the variable's value.
     * ARRAY -			1 means this is an array variable rather
     *				than a scalar variable or link. The
     *				arraymap field points to the array's
     *				hashtable for its elements.
     * LINK -			1 means this Var structure contains a
     *				reference to another Var structure that
     *				either has the real value or is itself
     *				another LINK pointer. Variables like
     *				this come about through "upvar" and "global"
     *				commands, or through references to variables
     *				in enclosing namespaces.
     * UNDEFINED -		1 means that the variable is in the process
     *				of being deleted. An undefined variable
     *				logically does not exist and survives only
     *				while it has a trace, or if it is a global
     *				variable currently being used by some
     *				procedure.
     * IN_HASHTABLE -		1 means this variable is in a hashtable. 0 if
     *				a local variable that was assigned a slot
     *				in a procedure frame by	the compiler so the
     *				Var storage is part of the call frame.
     * TRACE_EXISTS -		1 means that trace(s) exist on this
     *				scalar or array variable. This flag is
     *				set when (var.traces == null), it is
     *				cleared when there are no more traces.
     * TRACE_ACTIVE -		1 means that trace processing is currently
     *				underway for a read or write access, so
     *				new read or write accesses should not cause
     *				trace procedures to be called and the
     *				variable can't be deleted.
     * ARRAY_ELEMENT -		1 means that this variable is an array
     *				element, so it is not legal for it to be
     *				an array itself (the ARRAY flag had
     *				better not be set).
     * NAMESPACE_VAR -		1 means that this variable was declared
     *				as a namespace variable. This flag ensures
     *				it persists until its namespace is
     *				destroyed or until the variable is unset;
     *				it will persist even if it has not been
     *				initialized and is marked undefined.
     *				The variable's refCount is incremented to
     *				reflect the "reference" from its namespace.
     * NO_CACHE -		1 means that code should not be able to hold
     *				a cached reference to this variable. This flag
     *				is only set for Var objects returned by
     *				a namespace or interp resolver. It is not possible
     *				to clear this flag, so the variable can't
     *				be cached as long as it is alive.
     * NON_LOCAL -		1 means that the variable exists in the
     *				compiled local table, but it is not a
     *				local or imported local. This flag is
     *				only set in compiled code when scoped
     *				global var refs like $::myvar are found.
     *				These variables are not considered part
     *				of a variable frame and can't be found
     *				at runtime.
     */
    static final int SCALAR = 0x1;

    static final int ARRAY = 0x2;

    static final int LINK = 0x4;

    static final int UNDEFINED = 0x8;

    static final int IN_HASHTABLE = 0x10;

    static final int TRACE_ACTIVE = 0x20;

    static final int ARRAY_ELEMENT = 0x40;

    static final int NAMESPACE_VAR = 0x80;

    static final int NO_CACHE = 0x100;

    static final int NON_LOCAL = 0x200;

    static final int TRACE_EXISTS = 0x400;

    static final int EXPLICIT_LOCAL_NAME = 0x1000;

    final boolean isVarScalar() {
        return ((flags & SCALAR) != 0);
    }

    final boolean isVarLink() {
        return ((flags & LINK) != 0);
    }

    final boolean isVarArray() {
        return ((flags & ARRAY) != 0);
    }

    final boolean isVarUndefined() {
        return ((flags & UNDEFINED) != 0);
    }

    final boolean isVarArrayElement() {
        return ((flags & ARRAY_ELEMENT) != 0);
    }

    final boolean isVarNamespace() {
        return ((flags & NAMESPACE_VAR) != 0);
    }

    final boolean isVarInHashtable() {
        return ((flags & IN_HASHTABLE) != 0);
    }

    final boolean isVarTraceExists() {
        return ((flags & TRACE_EXISTS) != 0);
    }

    final boolean isVarNoCache() {
        return ((flags & NO_CACHE) != 0);
    }

    final boolean isVarNonLocal() {
        return ((flags & NON_LOCAL) != 0);
    }

    final void setVarScalar() {
        flags = (flags & ~(ARRAY | LINK)) | SCALAR;
    }

    final void setVarArray() {
        flags = (flags & ~(SCALAR | LINK)) | ARRAY;
    }

    final void setVarLink() {
        flags = (flags & ~(SCALAR | ARRAY)) | LINK;
    }

    final void setVarArrayElement() {
        flags = (flags & ~ARRAY) | ARRAY_ELEMENT;
    }

    final void setVarUndefined() {
        flags |= UNDEFINED;
    }

    final void setVarNamespace() {
        flags |= NAMESPACE_VAR;
    }

    final void setVarInHashtable() {
        flags |= IN_HASHTABLE;
    }

    final void setVarNonLocal() {
        flags |= NON_LOCAL;
    }

    final void setVarNoCache() {
        flags |= NO_CACHE;
    }

    final void setVarTraceExists() {
        flags |= TRACE_EXISTS;
    }

    final void clearVarUndefined() {
        flags &= ~UNDEFINED;
    }

    final void clearVarInHashtable() {
        flags &= ~IN_HASHTABLE;
    }

    final void clearVarTraceExists() {
        flags &= ~TRACE_EXISTS;
    }

    /**
     * A Var object is one of the following three types.
     *
     *  <li>Scalar variable - tobj is the object stored in the var.
     *	<li> Array variable - arraymap is the hashtable that stores
     *     all the elements. <p>
     *  <li> Upvar (Link) - linkto is the variable associated by this upvar.
     * </ul>
     */
    TclObject tobj;

    HashMap arraymap;

    Var linkto;

    /**
     * List that holds the traces that were placed in this Var
     */
    ArrayList traces;

    ArrayList sidVec;

    /**
     * Miscellaneous bits of information about variable.
     *
     * @see Var#SCALAR
     * @see Var#ARRAY
     * @see Var#LINK
     * @see Var#UNDEFINED
     * @see Var#IN_HASHTABLE
     * @see Var#TRACE_ACTIVE
     * @see Var#ARRAY_ELEMENT
     * @see Var#NAMESPACE_VAR
     */
    int flags;

    /**
     * If variable is in a hashtable, either the
     * hash table entry that refers to this
     * variable or null if the variable has been
     * detached from its hash table (e.g. an
     * array is deleted, but some of its
     * elements are still referred to in
     * upvars). null if the variable is not in a
     * hashtable. This is used to delete an
     * variable from its hashtable if it is no
     * longer needed.
     */
    HashMap table;

    /**
     * The key under which this variable is stored in the hash table.
     */
    String hashKey;

    /**
     * Counts number of active uses of this
     * variable, not including its entry in the
     * call frame or the hash table: 1 for each
     * additional variable whose link points
     * here, 1 for each nested trace active on
     * variable, and 1 if the variable is a 
     * namespace variable. This record can't be
     * deleted until refCount becomes 0.
     */
    int refCount;

    /**
     * Reference to the namespace that contains
     * this variable. This is set only for namespace
     * variables. A local variable in a procedure
     * will always have a null ns field.
     */
    Namespace ns;

    /**
     * NewVar -> Var
     * 
     * Construct a variable and initialize its fields.
     */
    Var() {
        tobj = null;
        arraymap = null;
        linkto = null;
        ns = null;
        hashKey = null;
        table = null;
        refCount = 0;
        traces = null;
        sidVec = null;
        flags = (SCALAR | UNDEFINED | IN_HASHTABLE);
    }

    /**
     * Used to create a String that describes this variable.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (ns != null) {
            sb.append(ns.fullName);
            if (ns.fullName.equals("::")) {
                sb.append(hashKey);
            } else {
                sb.append("::");
                sb.append(hashKey);
            }
        } else {
            sb.append(hashKey);
        }
        if (isVarScalar()) {
            sb.append(" ");
            sb.append("SCALAR");
        }
        if (isVarLink()) {
            sb.append(" ");
            sb.append("LINK");
        }
        if (isVarArray()) {
            sb.append(" ");
            sb.append("ARRAY");
        }
        if (isVarUndefined()) {
            sb.append(" ");
            sb.append("UNDEFINED");
        }
        if (isVarArrayElement()) {
            sb.append(" ");
            sb.append("ARRAY_ELEMENT");
        }
        if (isVarNamespace()) {
            sb.append(" ");
            sb.append("NAMESPACE_VAR");
        }
        if (isVarInHashtable()) {
            sb.append(" ");
            sb.append("IN_HASHTABLE");
        }
        if (isVarTraceExists()) {
            sb.append(" ");
            sb.append("TRACE_EXISTS");
        }
        if (isVarNoCache()) {
            sb.append(" ");
            sb.append("NO_CACHE");
        }
        return sb.toString();
    }

    /**
     * Used by ArrayCmd to create a unique searchId string.  If the 
     * sidVec List is empty then simply return 1.  Else return 1 
     * plus the SearchId.index value of the last Object in the vector.
     * 
     * @param None
     * @return The int value for unique SearchId string.
     */
    protected int getNextIndex() {
        int size = sidVec.size();
        if (size == 0) {
            return 1;
        }
        SearchId sid = (SearchId) sidVec.get(size - 1);
        return (sid.getIndex() + 1);
    }

    /**
     * Find the SearchId that in the sidVec List that is equal the 
     * unique String s and returns the iterator associated with
     * that SearchId.
     *
     * @param s String that ia a unique identifier for a SearchId object
     * @return Iterator if a match is found else null.
     */
    protected Iterator getSearch(String s) {
        SearchId sid;
        for (int i = 0; i < sidVec.size(); i++) {
            sid = (SearchId) sidVec.get(i);
            if (sid.equals(s)) {
                return sid.getIterator();
            }
        }
        return null;
    }

    /**
     * Find the SearchId object in the sidVec list and remove it.
     *
     * @param sid String that ia a unique identifier for a SearchId object.
     */
    protected boolean removeSearch(String sid) {
        SearchId curSid;
        for (int i = 0; i < sidVec.size(); i++) {
            curSid = (SearchId) sidVec.get(i);
            if (curSid.equals(sid)) {
                sidVec.remove(i);
                return true;
            }
        }
        return false;
    }

    static final String noSuchVar = "no such variable";

    static final String isArray = "variable is array";

    static final String needArray = "variable isn't array";

    static final String noSuchElement = "no such element in array";

    static final String danglingElement = "upvar refers to element in deleted array";

    static final String danglingVar = "upvar refers to variable in deleted namespace";

    static final String badNamespace = "parent namespace doesn't exist";

    static final String missingName = "missing variable name";

    public static final boolean isArrayVarname(String varName) {
        final int lastInd = varName.length() - 1;
        if (varName.charAt(lastInd) == ')') {
            if (varName.indexOf('(') != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * TclLookupVar -> lookupVar
     *
     * This procedure is used by virtually all of the variable
     * code to locate a variable given its name(s).
     *
     * @param part1 if part2 isn't NULL, this is the name of an array.
     *      Otherwise, this is a full variable name that could include 
     *      a parenthesized array elemnt or a scalar.
     * @param part2 Name of an element within array, or null.
     * @param flags Only the TCL.GLOBAL_ONLY bit matters.
     * @param msg Verb to use in error messages, e.g.  "read" or "set".
     * @param create OR'ed combination of CRT_PART1 and CRT_PART2.
     *      Tells which entries to create if they don't already exist.
     * @param throwException true if an exception should be throw if the
     *	    variable cannot be found.
     * @return a two element array. a[0] is the variable indicated by
     *      part1 and part2, or null if the variable couldn't be
     *      found and throwException is false.
     *      <p>
     *      If the variable is found, a[1] is the array that
     *      contains the variable (or null if the variable is a scalar).
     *      If the variable can't be found and either createPart1 or
     *      createPart2 are true, a new as-yet-undefined (VAR_UNDEFINED)
     *      variable instance is created, entered into a hash
     *      table, and returned.
     *      Note: it's possible that var.value of the returned variable
     *      may be null (variable undefined), even if createPart1 or createPart2
     *      are true (these only cause the hash table entry or array to be created).
     *      For example, the variable might be a global that has been unset but
     *      is still referenced by a procedure, or a variable that has been unset
     *      but it only being kept in existence by a trace.
     * @exception TclException if the variable cannot be found and 
     *      throwException is true.
     *
     */
    static Var[] lookupVar(Interp interp, String part1, String part2, int flags, String msg, boolean createPart1, boolean createPart2) throws TclException {
        CallFrame varFrame = interp.varFrame;
        HashMap table;
        Var var;
        String elName;
        int openParen;
        Namespace varNs, cxtNs;
        Interp.ResolverScheme res;
        var = null;
        varNs = null;
        elName = part2;
        openParen = -1;
        int lastInd = part1.length() - 1;
        if ((lastInd > 0) && (part1.charAt(lastInd) == ')')) {
            openParen = part1.indexOf('(');
        }
        if (openParen != -1) {
            if (part2 != null) {
                if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                    throw new TclVarException(interp, part1, part2, msg, needArray);
                }
                return null;
            }
            elName = part1.substring(openParen + 1, lastInd);
            part2 = elName;
            part1 = part1.substring(0, openParen);
        }
        if (((flags & TCL.GLOBAL_ONLY) != 0) || (interp.varFrame == null)) {
            cxtNs = interp.globalNs;
        } else {
            cxtNs = interp.varFrame.ns;
        }
        if (cxtNs.resolver != null || interp.resolvers != null) {
            try {
                if (cxtNs.resolver != null) {
                    var = cxtNs.resolver.resolveVar(interp, part1, cxtNs, flags);
                    if (var != null) {
                        var.setVarNoCache();
                    }
                } else {
                    var = null;
                }
                if (var == null && interp.resolvers != null) {
                    for (ListIterator iter = interp.resolvers.listIterator(); var == null && iter.hasNext(); ) {
                        res = (Interp.ResolverScheme) iter.next();
                        var = res.resolver.resolveVar(interp, part1, cxtNs, flags);
                        if (var != null) {
                            var.setVarNoCache();
                        }
                    }
                }
            } catch (TclException e) {
                var = null;
            }
        }
        if (((flags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY)) != 0) || (varFrame == null) || !varFrame.isProcCallFrame || (part1.indexOf("::") != -1)) {
            String tail;
            var = Namespace.findNamespaceVar(interp, part1, null, flags & ~TCL.LEAVE_ERR_MSG);
            if (var == null) {
                if (createPart1) {
                    Namespace.GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
                    Namespace.getNamespaceForQualName(interp, part1, null, flags, gnfqnr);
                    varNs = gnfqnr.ns;
                    tail = gnfqnr.simpleName;
                    if (varNs == null) {
                        if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                            throw new TclVarException(interp, part1, part2, msg, badNamespace);
                        }
                        return null;
                    }
                    if (tail == null) {
                        if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                            throw new TclVarException(interp, part1, part2, msg, missingName);
                        }
                        return null;
                    }
                    var = new Var();
                    varNs.varTable.put(tail, var);
                    var.hashKey = tail;
                    var.table = varNs.varTable;
                    var.ns = varNs;
                } else {
                    if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                        throw new TclVarException(interp, part1, part2, msg, noSuchVar);
                    }
                    return null;
                }
            }
        } else {
            if (varFrame.compiledLocals != null) {
                Var[] compiledLocals = varFrame.compiledLocals;
                String[] compiledLocalsNames = varFrame.compiledLocalsNames;
                final int MAX = compiledLocals.length;
                for (int i = 0; i < MAX; i++) {
                    if (compiledLocalsNames[i].equals(part1)) {
                        Var clocal = compiledLocals[i];
                        if (clocal == null) {
                            if (createPart1) {
                                var = new Var();
                                var.hashKey = part1;
                                var.clearVarInHashtable();
                                compiledLocals[i] = var;
                            }
                        } else {
                            if (clocal.isVarNonLocal()) {
                                throw new TclRuntimeError("can't lookup scoped variable \"" + part1 + "\" in local table");
                            }
                            var = clocal;
                        }
                        break;
                    }
                }
            }
            if (var == null) {
                table = varFrame.varTable;
                if (createPart1) {
                    if (table == null) {
                        table = new HashMap();
                        varFrame.varTable = table;
                    }
                    var = (Var) table.get(part1);
                    if (var == null) {
                        var = new Var();
                        table.put(part1, var);
                        var.hashKey = part1;
                        var.table = table;
                    }
                } else {
                    if (table != null) {
                        var = (Var) table.get(part1);
                    }
                    if (var == null) {
                        if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                            throw new TclVarException(interp, part1, part2, msg, noSuchVar);
                        }
                        return null;
                    }
                }
            }
        }
        while (var.isVarLink()) {
            var = var.linkto;
        }
        if (elName == null) {
            Var[] ret = interp.lookupVarResult;
            ret[0] = var;
            ret[1] = null;
            return ret;
        }
        return Var.lookupArrayElement(interp, part1, elName, flags, msg, createPart1, createPart2, var);
    }

    static Var[] lookupArrayElement(Interp interp, String part1, String part2, int flags, String msg, boolean createPart1, boolean createPart2, Var var) throws TclException {
        if (var.isVarUndefined() && !var.isVarArrayElement()) {
            if (!createPart1) {
                if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                    throw new TclVarException(interp, part1, part2, msg, noSuchVar);
                }
                return null;
            }
            if (((var.flags & IN_HASHTABLE) != 0) && (var.table == null)) {
                if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                    throw new TclVarException(interp, part1, part2, msg, danglingVar);
                }
                return null;
            }
            var.setVarArray();
            var.clearVarUndefined();
            var.arraymap = new HashMap();
        } else if (!var.isVarArray()) {
            if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                throw new TclVarException(interp, part1, part2, msg, needArray);
            }
            return null;
        }
        Var arrayVar = var;
        HashMap arrayTable = var.arraymap;
        if (createPart2) {
            Var searchvar = (Var) arrayTable.get(part2);
            if (searchvar == null) {
                if (var.sidVec != null) {
                    deleteSearches(var);
                }
                var = new Var();
                arrayTable.put(part2, var);
                var.hashKey = part2;
                var.table = arrayTable;
                var.ns = arrayVar.ns;
                var.setVarArrayElement();
            } else {
                var = searchvar;
            }
        } else {
            var = (Var) arrayTable.get(part2);
            if (var == null) {
                if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                    throw new TclVarException(interp, part1, part2, msg, noSuchElement);
                }
                return null;
            }
        }
        Var[] ret = interp.lookupVarResult;
        ret[0] = var;
        ret[1] = arrayVar;
        return ret;
    }

    /**
     * Tcl_GetVar2Ex -> getVar
     *
     * Query the value of a variable, given a two-part name consisting
     * of array name and element within array.
     *
     * @param interp the interp that holds the variable
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param flags misc flags that control the actions of this method.
     * @return the value of the variable.
     */
    static TclObject getVar(Interp interp, String part1, String part2, int flags) throws TclException {
        Var[] result = lookupVar(interp, part1, part2, flags, "read", false, true);
        if (result == null) {
            return null;
        }
        return getVarPtr(interp, result[0], result[1], part1, part2, flags);
    }

    /**
     * TclPtrGetVar -> getVarPtr
     *
     * Query the value of a variable, given refs to the variables
     * Var objects.
     *
     * @param interp the interp that holds the variable
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param flags misc flags that control the actions of this method.
     * @return the value of the variable.
     */
    static TclObject getVarPtr(Interp interp, Var var, Var array, String part1, String part2, int flags) throws TclException {
        try {
            if ((var.traces != null) || ((array != null) && (array.traces != null))) {
                String msg = callTraces(interp, array, var, part1, part2, (flags & (TCL.NAMESPACE_ONLY | TCL.GLOBAL_ONLY)) | TCL.TRACE_READS);
                if (msg != null) {
                    if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                        throw new TclVarException(interp, part1, part2, "read", msg);
                    }
                    return null;
                }
            }
            if (var.isVarScalar() && !var.isVarUndefined()) {
                return var.tobj;
            }
            if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                String msg;
                if (var.isVarUndefined() && (array != null) && !array.isVarUndefined()) {
                    msg = noSuchElement;
                } else if (var.isVarArray()) {
                    msg = isArray;
                } else {
                    msg = noSuchVar;
                }
                throw new TclVarException(interp, part1, part2, "read", msg);
            }
        } finally {
            if (var.isVarUndefined()) {
                cleanupVar(var, array);
            }
        }
        return null;
    }

    /**
     * Tcl_SetVar2Ex -> setVar
     *
     *	Given a two-part variable name, which may refer either to a scalar
     *	variable or an element of an array, change the value of the variable
     *	to a new Tcl object value. See the setVarPtr() method for the
     *	arguments to be passed to this method.
     */
    static TclObject setVar(Interp interp, String part1, String part2, TclObject newValue, int flags) throws TclException {
        Var[] result = lookupVar(interp, part1, part2, flags, "set", true, true);
        if (result == null) {
            return null;
        }
        return setVarPtr(interp, result[0], result[1], part1, part2, newValue, flags);
    }

    /**
     * TclPtrSetVar -> setVarPtr
     *
     *	This method implements setting of a variable value that has
     *	already been resolved into Var refrences. Pass the resolved
     *	var refrences and a two-part variable name, which may refer
     *	either to a scalar or an element of an array. This method will
     *	change the value of the variable to a new TclObject value.
     *	If the named scalar or array or element
     *	doesn't exist then this method will create one.
     *
     * @param interp the interp that holds the variable
     * @param var a resolved Var ref
     * @param array a resolved Var ref
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param newValue the new value for the variable
     * @param flags misc flags that control the actions of this method
     *
     *	Returns a pointer to the TclObject holding the new value of the
     *	variable. If the write operation was disallowed because an array was
     *	expected but not found (or vice versa), then null is returned; if
     *	the TCL.LEAVE_ERR_MSG flag is set, then an exception will be raised.
     *  Note that the returned object may not be the same one referenced
     *  by newValue because variable traces may modify the variable's value.
     *	The value of the given variable is set. If either the array or the
     *	entry didn't exist then a new variable is created.
     *
     *	The reference count is decremented for any old value of the variable
     *	and incremented for its new value. If the new value for the variable
     *	is not the same one referenced by newValue (perhaps as a result
     *	of a variable trace), then newValue's ref count is left unchanged
     *	by Tcl_SetVar2Ex. newValue's ref count is also left unchanged if
     *	we are appending it as a string value: that is, if "flags" includes
     *	TCL.APPEND_VALUE but not TCL.LIST_ELEMENT.
     *
     *	The reference count for the returned object is _not_ incremented: if
     *	you want to keep a reference to the object you must increment its
     *	ref count yourself.
     */
    static TclObject setVarPtr(Interp interp, Var var, Var array, String part1, String part2, TclObject newValue, int flags) throws TclException {
        TclObject oldValue;
        String bytes;
        if (((var.flags & IN_HASHTABLE) != 0) && (var.table == null)) {
            if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                if (var.isVarArrayElement()) {
                    throw new TclVarException(interp, part1, part2, "set", danglingElement);
                } else {
                    throw new TclVarException(interp, part1, part2, "set", danglingVar);
                }
            }
            return null;
        }
        if (var.isVarArray() && !var.isVarUndefined()) {
            if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                throw new TclVarException(interp, part1, part2, "set", isArray);
            }
            return null;
        }
        try {
            oldValue = var.tobj;
            if ((flags & TCL.APPEND_VALUE) != 0) {
                if (var.isVarUndefined() && (oldValue != null)) {
                    oldValue.release();
                    var.tobj = null;
                    oldValue = null;
                }
                if ((flags & TCL.LIST_ELEMENT) != 0) {
                    if (oldValue == null) {
                        oldValue = TclList.newInstance();
                        var.tobj = oldValue;
                        oldValue.preserve();
                    } else if (oldValue.isShared()) {
                        var.tobj = oldValue.duplicate();
                        oldValue.release();
                        oldValue = var.tobj;
                        oldValue.preserve();
                    }
                    TclList.append(interp, oldValue, newValue);
                } else {
                    bytes = newValue.toString();
                    if (oldValue == null) {
                        var.tobj = TclString.newInstance(bytes);
                        var.tobj.preserve();
                    } else {
                        if (oldValue.isShared()) {
                            var.tobj = oldValue.duplicate();
                            oldValue.release();
                            oldValue = var.tobj;
                            oldValue.preserve();
                        }
                        TclString.append(oldValue, bytes);
                    }
                }
            } else {
                if ((flags & TCL.LIST_ELEMENT) != 0) {
                    int listFlags;
                    if (oldValue != null) {
                        oldValue.release();
                    }
                    bytes = newValue.toString();
                    listFlags = Util.scanElement(interp, bytes);
                    StringBuffer sb = new StringBuffer(64);
                    Util.convertElement(bytes, listFlags, sb);
                    oldValue = TclString.newInstance(sb.toString());
                    var.tobj = oldValue;
                    var.tobj.preserve();
                } else if (newValue != oldValue) {
                    var.tobj = newValue;
                    newValue.preserve();
                    if (oldValue != null) {
                        oldValue.release();
                    }
                }
            }
            var.setVarScalar();
            var.clearVarUndefined();
            if (array != null) {
                array.clearVarUndefined();
            }
            if ((var.traces != null) || ((array != null) && (array.traces != null))) {
                String msg = callTraces(interp, array, var, part1, part2, (flags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY)) | TCL.TRACE_WRITES);
                if (msg != null) {
                    if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                        throw new TclVarException(interp, part1, part2, "set", msg);
                    }
                    return null;
                }
            }
            if (var.isVarScalar() && !var.isVarUndefined()) {
                return var.tobj;
            }
            return TclString.newInstance("");
        } finally {
            if (var.isVarUndefined()) {
                cleanupVar(var, array);
            }
        }
    }

    static TclObject initVarCompiledLocalScalar(final Interp interp, final String varname, final TclObject newValue, final Var[] compiledLocals, final int localIndex) throws TclException {
        final boolean validate = false;
        if (validate) {
            CallFrame varFrame = interp.varFrame;
            if (varFrame == null) {
                throw new TclRuntimeError("null interp.varFrame");
            }
            if (varFrame != interp.frame) {
                throw new TclRuntimeError("interp.frame vs interp.varFrame mismatch");
            }
            if (varFrame.isProcCallFrame == false) {
                throw new TclRuntimeError("expected isProcCallFrame to be true");
            }
            if (varFrame.compiledLocals == null) {
                throw new TclRuntimeError("expected non-null compiledLocals");
            }
            if (Var.isArrayVarname(varname)) {
                throw new TclRuntimeError("unexpected array variable name \"" + varname + "\"");
            }
            HashMap table = varFrame.varTable;
            if (table != null && table.size() > 0) {
                Var var = (Var) table.get(varname);
                if (var != null) {
                    throw new TclException(interp, "duplicate var found in local table for " + varname);
                }
            }
            if (compiledLocals[localIndex] != null) {
                throw new TclException(interp, "compiled local slot should be null for " + varname);
            }
            if (varname.indexOf("::") != -1) {
                throw new TclRuntimeError("scoped scalar should neve be initialized here " + varname);
            }
        }
        Var var = new Var();
        if (validate) {
            if (var.flags != (SCALAR | UNDEFINED | IN_HASHTABLE)) {
                throw new TclRuntimeError("invalid Var flags state");
            }
            if (var.tobj != null) {
                throw new TclRuntimeError("expected null Var tobj value");
            }
            if (var.arraymap != null) {
                throw new TclRuntimeError("expected null Var arraymap value");
            }
            if (var.linkto != null) {
                throw new TclRuntimeError("expected null Var linkto value");
            }
            if (var.table != null) {
                throw new TclRuntimeError("expected null Var table");
            }
        }
        var.flags = SCALAR;
        var.hashKey = varname;
        var.tobj = newValue;
        newValue.preserve();
        compiledLocals[localIndex] = var;
        return newValue;
    }

    static TclObject setVarCompiledLocalScalarInvalid(Interp interp, String varname, TclObject newValue) throws TclException {
        final boolean validate = false;
        if (validate) {
            CallFrame varFrame = interp.varFrame;
            if (varFrame == null) {
                throw new TclRuntimeError("null interp.varFrame");
            }
            if (varFrame != interp.frame) {
                throw new TclRuntimeError("interp.frame vs interp.varFrame mismatch");
            }
            if (varFrame.isProcCallFrame == false) {
                throw new TclRuntimeError("expected isProcCallFrame to be true");
            }
            if (Var.isArrayVarname(varname)) {
                throw new TclRuntimeError("unexpected array variable name \"" + varname + "\"");
            }
            if (!varname.startsWith("::") && (-1 != varname.indexOf("::"))) {
                throw new TclRuntimeError("unexpected scoped scalar");
            }
        }
        return setVar(interp, varname, null, newValue, TCL.LEAVE_ERR_MSG);
    }

    static TclObject getVarCompiledLocalScalarInvalid(Interp interp, String varname) throws TclException {
        final boolean validate = false;
        if (validate) {
            CallFrame varFrame = interp.varFrame;
            if (varFrame == null) {
                throw new TclRuntimeError("null interp.varFrame");
            }
            if (varFrame != interp.frame) {
                throw new TclRuntimeError("interp.frame vs interp.varFrame mismatch");
            }
            if (varFrame.isProcCallFrame == false) {
                throw new TclRuntimeError("expected isProcCallFrame to be true");
            }
            if (varFrame.compiledLocals == null) {
                throw new TclRuntimeError("expected non-null compiledLocals");
            }
            if (varname == null) {
                throw new TclRuntimeError("varname can't be null");
            }
            if ((varname.charAt(varname.length() - 1) == ')') && (varname.indexOf('(') != -1)) {
                throw new TclRuntimeError("unexpected array variable name \"" + varname + "\"");
            }
            if (!varname.startsWith("::") && (-1 != varname.indexOf("::"))) {
                throw new TclRuntimeError("unexpected scoped scalar");
            }
        }
        return Var.getVar(interp, varname, null, TCL.LEAVE_ERR_MSG);
    }

    static TclObject initVarCompiledLocalArray(final Interp interp, final String varname, final String key, final TclObject newValue, final Var[] compiledLocals, final int localIndex) throws TclException {
        final boolean validate = false;
        if (validate) {
            CallFrame varFrame = interp.varFrame;
            if (varFrame == null) {
                throw new TclRuntimeError("null interp.varFrame");
            }
            if (varFrame != interp.frame) {
                throw new TclRuntimeError("interp.frame vs interp.varFrame mismatch");
            }
            if (varFrame.isProcCallFrame == false) {
                throw new TclRuntimeError("expected isProcCallFrame to be true");
            }
            if (varFrame.compiledLocals == null) {
                throw new TclRuntimeError("expected non-null compiledLocals");
            }
            if (Var.isArrayVarname(varname)) {
                throw new TclRuntimeError("unexpected array variable name \"" + varname + "\"");
            }
            if (key == null) {
                throw new TclRuntimeError("null array key");
            }
            HashMap table = varFrame.varTable;
            if (table != null && table.size() > 0) {
                Var var = (Var) table.get(varname);
                if (var != null) {
                    throw new TclException(interp, "duplicate var found in local table for " + varname);
                }
            }
            if (compiledLocals[localIndex] != null) {
                throw new TclException(interp, "compiled local slot should be null for " + varname);
            }
            if (varname.indexOf("::") != -1) {
                throw new TclRuntimeError("scoped scalar should neve be initialized here " + varname);
            }
        }
        Var var = new Var();
        var.clearVarInHashtable();
        var.hashKey = varname;
        compiledLocals[localIndex] = var;
        return setVar(interp, varname, key, newValue, TCL.LEAVE_ERR_MSG);
    }

    static TclObject setVarCompiledLocalArrayInvalid(Interp interp, String varname, String key, TclObject newValue) throws TclException {
        final boolean validate = false;
        if (validate) {
            CallFrame varFrame = interp.varFrame;
            if (varFrame == null) {
                throw new TclRuntimeError("null interp.varFrame");
            }
            if (varFrame != interp.frame) {
                throw new TclRuntimeError("interp.frame vs interp.varFrame mismatch");
            }
            if (varFrame.isProcCallFrame == false) {
                throw new TclRuntimeError("expected isProcCallFrame to be true");
            }
            if (varFrame.compiledLocals == null) {
                throw new TclRuntimeError("expected non-null compiledLocals");
            }
            if (Var.isArrayVarname(varname)) {
                throw new TclRuntimeError("unexpected array variable name \"" + varname + "\"");
            }
            if (key == null) {
                throw new TclRuntimeError("null array key");
            }
            HashMap table = varFrame.varTable;
            if (table != null && table.size() > 0) {
                Var var = (Var) table.get(varname);
                if (var != null) {
                    throw new TclException(interp, "duplicate var found in local table for " + varname);
                }
            }
            if (varname.indexOf("::") != -1) {
                throw new TclRuntimeError("scoped scalar should neve be initialized here " + varname);
            }
        }
        return setVar(interp, varname, key, newValue, TCL.LEAVE_ERR_MSG);
    }

    static TclObject getVarCompiledLocalArrayInvalid(Interp interp, String varname, String key) throws TclException {
        final boolean validate = false;
        if (validate) {
            CallFrame varFrame = interp.varFrame;
            if (varFrame == null) {
                throw new TclRuntimeError("null interp.varFrame");
            }
            if (varFrame != interp.frame) {
                throw new TclRuntimeError("interp.frame vs interp.varFrame mismatch");
            }
            if (varFrame.compiledLocals == null) {
                throw new TclRuntimeError("expected non-null compiledLocals");
            }
            if (key == null) {
                throw new TclRuntimeError("array key can't be null");
            }
            if ((varname.charAt(varname.length() - 1) == ')') && (varname.indexOf('(') != -1)) {
                throw new TclRuntimeError("unexpected array variable name \"" + varname + "\"");
            }
        }
        return Var.getVar(interp, varname, key, TCL.LEAVE_ERR_MSG);
    }

    static TclObject getVarCompiledLocalArray(final Interp interp, final String varname, final String key, final Var resolved, final boolean leaveErrMsg) throws TclException {
        int flags = 0;
        if (leaveErrMsg) {
            flags = TCL.LEAVE_ERR_MSG;
        }
        Var[] result = Var.lookupArrayElement(interp, varname, key, flags, "read", false, false, resolved);
        if (result == null) {
            return null;
        }
        return Var.getVarPtr(interp, result[0], result[1], varname, key, TCL.LEAVE_ERR_MSG);
    }

    static TclObject setVarCompiledLocalArray(final Interp interp, final String varname, final String key, final TclObject newValue, final Var resolved) throws TclException {
        final int flags = TCL.LEAVE_ERR_MSG;
        Var[] result = lookupArrayElement(interp, varname, key, flags, "set", false, true, resolved);
        return setVarPtr(interp, result[0], result[1], varname, key, newValue, flags);
    }

    /**
     *  TclIncrVar2 -> incrVar
     *
     *	Given a two-part variable name, which may refer either to a scalar
     *	variable or an element of an array, increment the Tcl object value
     *  of the variable by a specified amount.
     *
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param incrAmount Amount to be added to variable.
     * @param flags misc flags that control the actions of this method
     *
     * Results:
     *	Returns a reference to the TclObject holding the new value of the
     *	variable. If the specified variable doesn't exist, or there is a
     *	clash in array usage, or an error occurs while executing variable
     *	traces, then a TclException will be raised.
     *
     * Side effects:
     *	The value of the given variable is incremented by the specified
     *	amount. If either the array or the entry didn't exist then a new
     *	variable is created. The ref count for the returned object is _not_
     *	incremented to reflect the returned reference; if you want to keep a
     *	reference to the object you must increment its ref count yourself.
     *
     *----------------------------------------------------------------------
     */
    static TclObject incrVar(Interp interp, String part1, String part2, int incrAmount, int flags) throws TclException {
        TclObject varValue = null;
        boolean createdNewObj;
        int i;
        boolean err;
        err = false;
        try {
            varValue = getVar(interp, part1, part2, flags);
        } catch (TclException e) {
            err = true;
            throw e;
        } finally {
            if (err || varValue == null) {
                interp.addErrorInfo("\n    (reading value of variable to increment)");
            }
        }
        createdNewObj = false;
        if (varValue.isShared()) {
            varValue = varValue.duplicate();
            createdNewObj = true;
        }
        try {
            TclInteger.incr(interp, varValue, incrAmount);
        } catch (TclException e) {
            if (createdNewObj) {
                varValue.release();
            }
            throw e;
        }
        return setVar(interp, part1, part2, varValue, flags);
    }

    /**
     * Tcl_UnsetVar2 -> unsetVar
     *
     * Unset a variable, given a two-part name consisting of array
     * name and element within array.
     *
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param flags misc flags that control the actions of this method.
     *
     *	If part1 and part2 indicate a local or global variable in interp,
     *	it is deleted.  If part1 is an array name and part2 is null, then
     *	the whole array is deleted.
     *
     */
    static void unsetVar(Interp interp, String part1, String part2, int flags) throws TclException {
        Var dummyVar;
        Var var;
        Var array;
        TclObject obj;
        int result;
        Var[] lookup_result = lookupVar(interp, part1, part2, flags, "unset", false, false);
        if (lookup_result == null) {
            throw new TclRuntimeError("unexpected null reference");
        }
        var = lookup_result[0];
        array = lookup_result[1];
        result = (var.isVarUndefined() ? TCL.ERROR : TCL.OK);
        if ((array != null) && (array.sidVec != null)) {
            deleteSearches(array);
        }
        dummyVar = new Var();
        dummyVar.tobj = var.tobj;
        dummyVar.arraymap = var.arraymap;
        dummyVar.linkto = var.linkto;
        dummyVar.traces = var.traces;
        dummyVar.flags = var.flags;
        dummyVar.hashKey = var.hashKey;
        dummyVar.table = var.table;
        dummyVar.refCount = var.refCount;
        dummyVar.ns = var.ns;
        var.setVarUndefined();
        var.setVarScalar();
        var.tobj = null;
        var.arraymap = null;
        var.linkto = null;
        var.traces = null;
        var.sidVec = null;
        if ((dummyVar.traces != null) || ((array != null) && (array.traces != null))) {
            var.refCount++;
            dummyVar.flags &= ~TRACE_ACTIVE;
            callTraces(interp, array, dummyVar, part1, part2, (flags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY)) | TCL.TRACE_UNSETS);
            dummyVar.traces = null;
            var.refCount--;
        }
        if (dummyVar.isVarArray() && !dummyVar.isVarUndefined()) {
            deleteArray(interp, part1, dummyVar, (flags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY)) | TCL.TRACE_UNSETS);
        }
        if (dummyVar.isVarScalar() && (dummyVar.tobj != null)) {
            obj = dummyVar.tobj;
            obj.release();
            dummyVar.tobj = null;
        }
        if ((var.flags & NAMESPACE_VAR) != 0) {
            var.flags &= ~NAMESPACE_VAR;
            var.refCount--;
        }
        cleanupVar(var, array);
        Var.setUndefinedToNull(interp, part1, part2);
        if (result != TCL.OK) {
            if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
                throw new TclVarException(interp, part1, part2, "unset", ((array == null) ? noSuchVar : noSuchElement));
            }
        }
    }

    /**
     * Tcl_TraceVar2 -> traceVar
     *
     * Trace a variable, given a two-part name consisting of array
     * name and element within array.
     *
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param flags misc flags that control the actions of this method.
     * @param trace the trace to comand to add.
     */
    static void traceVar(Interp interp, String part1, String part2, int flags, VarTrace proc) throws TclException {
        Var[] result;
        Var var, array;
        result = lookupVar(interp, part1, part2, (flags | TCL.LEAVE_ERR_MSG), "trace", true, true);
        if (result == null) {
            throw new TclException(interp, "");
        }
        var = result[0];
        array = result[1];
        if (var.traces == null) {
            var.setVarTraceExists();
            var.traces = new ArrayList();
        }
        TraceRecord rec = new TraceRecord();
        rec.trace = proc;
        rec.flags = flags & (TCL.TRACE_READS | TCL.TRACE_WRITES | TCL.TRACE_UNSETS | TCL.TRACE_ARRAY);
        var.traces.add(0, rec);
    }

    /**
     * Tcl_UntraceVar2 -> untraceVar
     *
     * Untrace a variable, given a two-part name consisting of array
     * name and element within array. This will Remove a
     * previously-created trace for a variable.
     *
     * @param interp Interpreter containing variable.
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name.
     * @param flags misc flags that control the actions of this method.
     * @param proc the trace to delete.
     */
    static void untraceVar(Interp interp, String part1, String part2, int flags, VarTrace proc) {
        Var[] result = null;
        Var var;
        try {
            result = lookupVar(interp, part1, part2, flags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY), null, false, false);
            if (result == null) {
                return;
            }
        } catch (TclException e) {
            throw new TclRuntimeError("unexpected TclException: " + e);
        }
        var = result[0];
        if (var.traces != null) {
            int len = var.traces.size();
            for (int i = 0; i < len; i++) {
                TraceRecord rec = (TraceRecord) var.traces.get(i);
                if (rec.trace == proc) {
                    var.traces.remove(i);
                    break;
                }
            }
            if (var.traces.size() == 0) {
                var.traces = null;
                var.clearVarTraceExists();
            }
        }
        if (var.isVarUndefined()) {
            cleanupVar(var, null);
        }
    }

    /**
     * Tcl_VarTraceInfo2 -> getTraces
     *
     * @return the list of traces of a variable.
     *
     * @param interp Interpreter containing variable.
     * @param part1 1st part of the variable name.
     * @param part2 2nd part of the variable name (can be null).
     * @param flags misc flags that control the actions of this method.
     */
    protected static ArrayList getTraces(Interp interp, String part1, String part2, int flags) throws TclException {
        Var[] result;
        result = lookupVar(interp, part1, part2, flags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY), null, false, false);
        if (result == null) {
            return null;
        }
        return result[0].traces;
    }

    protected static void makeUpvar(Interp interp, CallFrame frame, String otherP1, String otherP2, int otherFlags, String myName, int myFlags, int localIndex) throws TclException {
        Var other, var, array;
        Var[] result = null;
        CallFrame varFrame;
        CallFrame savedFrame = null;
        HashMap table;
        Namespace ns, altNs;
        String tail;
        boolean newvar = false;
        boolean foundInCompiledLocalsArray = false;
        boolean foundInLocalTable = false;
        Var[] compiledLocals = null;
        try {
            if ((otherFlags & TCL.NAMESPACE_ONLY) == 0) {
                savedFrame = interp.varFrame;
                interp.varFrame = frame;
            }
            int otherLookupFlags = (otherFlags | TCL.LEAVE_ERR_MSG);
            if ((myFlags & EXPLICIT_LOCAL_NAME) != 0) {
                otherLookupFlags = otherFlags;
            }
            result = Var.lookupVar(interp, otherP1, otherP2, otherLookupFlags, "access", true, true);
        } finally {
            if ((otherFlags & TCL.NAMESPACE_ONLY) == 0) {
                interp.varFrame = savedFrame;
            }
        }
        if (result == null) {
            return;
        }
        other = result[0];
        array = result[1];
        if (other == null) {
            throw new TclRuntimeError("unexpected null reference");
        }
        if (other.isVarLink()) {
            throw new TclRuntimeError("other var resolved to a link var");
        }
        varFrame = interp.varFrame;
        if (((myFlags & (TCL.GLOBAL_ONLY | TCL.NAMESPACE_ONLY)) != 0) || (varFrame == null) || !varFrame.isProcCallFrame || ((myName.indexOf("::") != -1) && ((myFlags & EXPLICIT_LOCAL_NAME) == 0))) {
            Namespace.GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
            Namespace.getNamespaceForQualName(interp, myName, null, myFlags, gnfqnr);
            ns = gnfqnr.ns;
            altNs = gnfqnr.altNs;
            tail = gnfqnr.simpleName;
            if (ns == null) {
                ns = altNs;
            }
            if (ns == null) {
                throw new TclException(interp, "bad variable name \"" + myName + "\": unknown namespace");
            }
            if (((otherP2 != null) ? array.ns : other.ns) == null) {
                throw new TclException(interp, "bad variable name \"" + myName + "\": upvar won't create namespace variable that refers to procedure variable");
            }
            var = (Var) ns.varTable.get(tail);
            if (var == null) {
                newvar = true;
                var = new Var();
                ns.varTable.put(tail, var);
                var.hashKey = tail;
                var.table = ns.varTable;
                var.ns = ns;
            }
        } else {
            var = null;
            compiledLocals = varFrame.compiledLocals;
            if (compiledLocals != null) {
                if (localIndex == -1) {
                    final int MAX = compiledLocals.length;
                    String[] compiledLocalsNames = varFrame.compiledLocalsNames;
                    for (int i = 0; i < MAX; i++) {
                        if (compiledLocalsNames[i].equals(myName)) {
                            foundInCompiledLocalsArray = true;
                            localIndex = i;
                            var = compiledLocals[i];
                            break;
                        }
                    }
                } else {
                    foundInCompiledLocalsArray = true;
                    var = compiledLocals[localIndex];
                }
            }
            if (!foundInCompiledLocalsArray) {
                table = varFrame.varTable;
                if (table == null) {
                    table = new HashMap();
                    varFrame.varTable = table;
                }
                if (table != null) {
                    var = (Var) table.get(myName);
                }
                if (var == null) {
                    newvar = true;
                    var = new Var();
                    table.put(myName, var);
                    var.hashKey = myName;
                    var.table = table;
                }
                if (var != null) {
                    foundInLocalTable = true;
                }
            }
            if (foundInCompiledLocalsArray && (var == null)) {
                newvar = true;
                var = new Var();
                var.hashKey = myName;
                var.clearVarInHashtable();
            }
        }
        if (!newvar) {
            if (var == other) {
                throw new TclException(interp, "can't upvar from variable to itself");
            }
            if (var.isVarLink()) {
                Var link = var.linkto;
                if (link == other) {
                    return;
                }
                link.refCount--;
                if (link.isVarUndefined()) {
                    cleanupVar(link, null);
                }
            } else if (!var.isVarUndefined()) {
                throw new TclException(interp, "variable \"" + myName + "\" already exists");
            } else if (var.traces != null) {
                throw new TclException(interp, "variable \"" + myName + "\" has traces: can't use for upvar");
            }
        }
        var.setVarLink();
        var.clearVarUndefined();
        var.linkto = other;
        other.refCount++;
        if (foundInCompiledLocalsArray) {
            if (newvar) {
                compiledLocals[localIndex] = var;
            }
            if ((myFlags & EXPLICIT_LOCAL_NAME) != 0) {
                var.setVarNonLocal();
            }
        }
        return;
    }

    public static String getVariableFullName(Interp interp, Var var) {
        StringBuffer buff = new StringBuffer();
        if (var != null) {
            if (!var.isVarArrayElement()) {
                if (var.ns != null) {
                    buff.append(var.ns.fullName);
                    if (var.ns != interp.globalNs) {
                        buff.append("::");
                    }
                }
                if (var.hashKey != null) {
                    buff.append(var.hashKey);
                }
            }
        }
        return buff.toString();
    }

    /**
     * CallTraces -> callTraces
     *
     * This procedure is invoked to find and invoke relevant
     * trace procedures associated with a particular operation on
     * a variable.  This procedure invokes traces both on the
     * variable and on its containing array (where relevant).
     *
     * @param interp Interpreter containing variable.
     * @param array array variable that contains the variable, or null
     *   if the variable isn't an element of an array.
     * @param var Variable whose traces are to be invoked.
     * @param part1 the first part of a variable name.
     * @param part2 the second part of a variable name.
     * @param flags Flags to pass to trace procedures: indicates
     *   what's happening to variable, plus other stuff like
     *   TCL.GLOBAL_ONLY, TCL.NAMESPACE_ONLY, and TCL.INTERP_DESTROYED.
     * @return null if no trace procedures were invoked, or
     *   if all the invoked trace procedures returned successfully.
     *   The return value is non-null if a trace procedure returned an
     *   error (in this case no more trace procedures were invoked
     *   after the error was returned). In this case the return value
     *   is a pointer to a string describing the error.
     */
    protected static String callTraces(Interp interp, Var array, Var var, String part1, String part2, int flags) {
        TclObject oldResult;
        int i;
        if ((var.flags & Var.TRACE_ACTIVE) != 0) {
            return null;
        }
        var.flags |= Var.TRACE_ACTIVE;
        var.refCount++;
        if (part2 == null) {
            int len = part1.length();
            if (len > 0) {
                if (part1.charAt(len - 1) == ')') {
                    for (i = 0; i < len - 1; i++) {
                        if (part1.charAt(i) == '(') {
                            break;
                        }
                    }
                    if (i < len - 1) {
                        if (i < len - 2) {
                            part2 = part1.substring(i + 1, len - 1);
                            part1 = part1.substring(0, i);
                        }
                    }
                }
            }
        }
        oldResult = interp.getResult();
        oldResult.preserve();
        interp.resetResult();
        try {
            if (array != null) {
                array.refCount++;
            }
            if ((array != null) && (array.traces != null)) {
                for (i = 0; (array.traces != null) && (i < array.traces.size()); i++) {
                    TraceRecord rec = (TraceRecord) array.traces.get(i);
                    if ((rec.flags & flags) != 0) {
                        try {
                            rec.trace.traceProc(interp, part1, part2, flags);
                        } catch (TclException e) {
                            if ((flags & TCL.TRACE_UNSETS) == 0) {
                                return interp.getResult().toString();
                            }
                        }
                    }
                }
            }
            if ((flags & TCL.TRACE_UNSETS) != 0) {
                flags |= TCL.TRACE_DESTROYED;
            }
            for (i = 0; (var.traces != null) && (i < var.traces.size()); i++) {
                TraceRecord rec = (TraceRecord) var.traces.get(i);
                if ((rec.flags & flags) != 0) {
                    try {
                        rec.trace.traceProc(interp, part1, part2, flags);
                    } catch (TclException e) {
                        if ((flags & TCL.TRACE_UNSETS) == 0) {
                            return interp.getResult().toString();
                        }
                    }
                }
            }
            return null;
        } finally {
            if (array != null) {
                array.refCount--;
            }
            var.flags &= ~TRACE_ACTIVE;
            var.refCount--;
            interp.setResult(oldResult);
            oldResult.release();
        }
    }

    /**
     * DeleteSearches -> deleteSearches
     *
     *	This procedure is called to free up all of the searches
     *	associated with an array variable.
     *
     * @param interp Interpreter containing array.
     * @param arrayVar the array variable to delete searches from.
     */
    protected static void deleteSearches(Var arrayVar) {
        arrayVar.sidVec = null;
    }

    /**
     * TclDeleteVars -> deleteVars
     *
     *	This procedure is called to recycle all the storage space
     *	associated with a table of variables. For this procedure
     *	to work correctly, it must not be possible for any of the
     *	variables in the table to be accessed from Tcl commands
     *	(e.g. from trace procedures).
     *
     * @param interp Interpreter containing array.
     * @param table HashMap that holds the Vars to delete
     */
    protected static void deleteVars(Interp interp, HashMap table) {
        int flags;
        Namespace currNs = Namespace.getCurrentNamespace(interp);
        flags = TCL.TRACE_UNSETS;
        if (table == interp.globalNs.varTable) {
            flags |= (TCL.INTERP_DESTROYED | TCL.GLOBAL_ONLY);
        } else if (table == currNs.varTable) {
            flags |= TCL.NAMESPACE_ONLY;
        }
        for (Iterator iter = table.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            deleteVar(interp, (Var) entry.getValue(), flags);
        }
        table.clear();
    }

    /**
// FIXME: Make more like TclDeleteCompiledLocalVars()
     * TclDeleteVars -> deleteVars
     *
     *	This procedure is called to recycle all the storage space
     *	associated with an array of variables. For this procedure
     *	to work correctly, it must not be possible for any of the
     *	variables in the array to be accessed from Tcl commands
     *	(e.g. from trace procedures).
     *
     * @param interp Interpreter containing array.
     * @param compiledLocals array of compiled local variables
     */
    protected static void deleteVars(Interp interp, Var[] compiledLocals) {
        final int flags = TCL.TRACE_UNSETS;
        final int max = compiledLocals.length;
        for (int i = 0; i < max; i++) {
            Var clocal = compiledLocals[i];
            if (clocal != null) {
                deleteVar(interp, clocal, flags);
                compiledLocals[i] = null;
            }
        }
    }

    /**
     * deleteVar
     *
     *	This procedure is called to recycle all the storage space
     *	associated with a single Var instance.
     *
     * @param interp Interpreter containing array.
     * @param var A Var refrence to be deleted
     * @param flags flags to pass to trace callbacks.
     */
    protected static void deleteVar(Interp interp, Var var, int flags) {
        if ((var.flags & LINK) != 0) {
            Var link = var.linkto;
            link.refCount--;
            if ((link.refCount == 0) && (link.traces == null) && ((link.flags & (UNDEFINED | IN_HASHTABLE)) == (UNDEFINED | IN_HASHTABLE))) {
                if (link.hashKey == null) {
                    var.linkto = null;
                } else if (link.table != var.table) {
                    link.table.remove(link.hashKey);
                    link.table = null;
                    var.linkto = null;
                }
            }
        }
        if (var.traces != null) {
            String fullname = getVariableFullName(interp, var);
            callTraces(interp, null, var, fullname, null, flags);
        }
        if ((var.flags & ARRAY) != 0) {
            deleteArray(interp, var.hashKey, var, flags);
            var.arraymap = null;
        } else if (((var.flags & SCALAR) != 0) && (var.tobj != null)) {
            TclObject obj = var.tobj;
            obj.release();
            var.tobj = null;
        }
        var.hashKey = null;
        var.table = null;
        var.traces = null;
        var.setVarUndefined();
        var.setVarScalar();
        if ((var.flags & NAMESPACE_VAR) != 0) {
            var.flags &= ~NAMESPACE_VAR;
            var.refCount--;
        }
    }

    /**
     * DeleteArray -> deleteArray
     *
     * This procedure is called to free up everything in an array
     * variable.  It's the caller's responsibility to make sure
     * that the array is no longer accessible before this procedure
     * is called.
     *
     * @param interp Interpreter containing array.
     * @param arrayName name of array (used for trace callbacks).
     * @param var the array variable to delete.
     * @param flags Flags to pass to CallTraces.
     */
    protected static void deleteArray(Interp interp, String arrayName, Var var, int flags) {
        Var el;
        TclObject obj;
        deleteSearches(var);
        HashMap table = var.arraymap;
        for (Iterator iter = table.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            el = (Var) entry.getValue();
            if (el.isVarScalar() && (el.tobj != null)) {
                obj = el.tobj;
                obj.release();
                el.tobj = null;
            }
            String tmpkey = (String) el.hashKey;
            el.hashKey = null;
            el.table = null;
            if (el.traces != null) {
                el.flags &= ~TRACE_ACTIVE;
                callTraces(interp, null, el, arrayName, tmpkey, flags);
                el.traces = null;
            }
            el.setVarUndefined();
            el.setVarScalar();
            if (el.refCount == 0) {
            }
        }
        table.clear();
        var.arraymap = null;
    }

    /**
     * CleanupVar -> cleanupVar
     *
     * This procedure is called when it looks like it may be OK
     * to free up the variable's record and hash table entry, and
     * those of its containing parent.  It's called, for example,
     * when a trace on a variable deletes the variable.
     *
     * @param var variable that may be a candidate for being expunged.
     * @param array Array that contains the variable, or NULL if this
     *   variable isn't an array element.
     */
    protected static void cleanupVar(Var var, Var array) {
        if (var.isVarUndefined() && (var.refCount == 0) && (var.traces == null) && ((var.flags & IN_HASHTABLE) != 0)) {
            if (var.table != null) {
                var.table.remove(var.hashKey);
                var.table = null;
                var.hashKey = null;
            }
        }
        if (array != null) {
            if (array.isVarUndefined() && (array.refCount == 0) && (array.traces == null) && ((array.flags & IN_HASHTABLE) != 0)) {
                if (array.table != null) {
                    array.table.remove(array.hashKey);
                    array.table = null;
                    array.hashKey = null;
                }
            }
        }
    }

    static Var resolveScalar(Var v) {
        int flags = v.flags;
        if ((flags & LINK) != 0) {
            v = v.linkto;
            flags = v.flags;
        }
        if ((flags & (LINK | ARRAY | ARRAY_ELEMENT | UNDEFINED | TRACE_EXISTS | NO_CACHE)) != 0) {
            return null;
        }
        return v;
    }

    static Var resolveArray(Var v) {
        int flags = v.flags;
        if ((flags & LINK) != 0) {
            v = v.linkto;
            flags = v.flags;
        }
        if ((flags & (LINK | SCALAR | UNDEFINED | NO_CACHE)) != 0) {
            return null;
        }
        return v;
    }

    static void setUndefinedToNull(Interp interp, String part1, String part2) {
        CallFrame varFrame = interp.varFrame;
        if (varFrame == null) {
            return;
        }
        if (varFrame.compiledLocals != null) {
            Var[] compiledLocals = varFrame.compiledLocals;
            final int MAX = compiledLocals.length;
            for (int i = 0; i < MAX; i++) {
                Var clocal = compiledLocals[i];
                if (clocal != null && !clocal.isVarNonLocal() && clocal.hashKey.equals(part1)) {
                    if (!clocal.isVarLink() && clocal.isVarUndefined()) {
                        if (clocal.refCount == 0) {
                            compiledLocals[i] = null;
                        }
                    }
                    return;
                }
            }
        }
    }

    static void AppendLocals(Interp interp, TclObject list, String pattern, boolean includeLinks) throws TclException {
        Var var;
        String varName;
        CallFrame frame = interp.varFrame;
        HashMap localVarTable = frame.varTable;
        if (localVarTable != null) {
            for (Iterator iter = localVarTable.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                varName = (String) entry.getKey();
                var = (Var) entry.getValue();
                if (!var.isVarUndefined() && (includeLinks || !var.isVarLink())) {
                    if ((pattern == null) || Util.stringMatch(varName, pattern)) {
                        TclList.append(interp, list, TclString.newInstance(varName));
                    }
                }
            }
        }
        Var[] compiledLocals = frame.compiledLocals;
        if (compiledLocals != null) {
            final int max = compiledLocals.length;
            for (int i = 0; i < max; i++) {
                Var clocal = compiledLocals[i];
                if (clocal != null && !clocal.isVarNonLocal()) {
                    var = clocal;
                    varName = (String) var.hashKey;
                    if (!var.isVarUndefined() && (includeLinks || !var.isVarLink())) {
                        if ((pattern == null) || Util.stringMatch(varName, pattern)) {
                            TclList.append(interp, list, TclString.newInstance(varName));
                        }
                    }
                }
            }
        }
    }
}
