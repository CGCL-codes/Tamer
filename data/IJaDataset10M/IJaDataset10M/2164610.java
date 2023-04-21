package tcl.lang;

import java.util.*;

public class Namespace {

    public String name;

    public String fullName;

    public DeleteProc deleteProc;

    public Namespace parent;

    public HashMap childTable;

    public long nsId;

    public Interp interp;

    public int flags;

    public int activationCount;

    public int refCount;

    public HashMap cmdTable;

    public HashMap varTable;

    public String[] exportArray;

    public int numExportPatterns;

    public int maxExportPatterns;

    public Resolver resolver;

    public String toString() {
        return fullName;
    }

    public static interface DeleteProc {

        public void delete();
    }

    static class ResolvedNsName {

        Namespace ns;

        long nsId;

        Namespace refNs;

        int refCount;
    }

    public static final int FIND_ONLY_NS = 0x1000;

    private static final int NUM_TRAIL_ELEMS = 5;

    private static long numNsCreated = 0;

    private static Object nsMutex = new Object();

    static final int NS_DYING = 0x01;

    static final int NS_DEAD = 0x02;

    public static final int CREATE_NS_IF_UNKNOWN = 0x800;

    public static Namespace getCurrentNamespace(Interp interp) {
        if (interp.varFrame != null) {
            return interp.varFrame.ns;
        } else {
            return interp.globalNs;
        }
    }

    public static Namespace getGlobalNamespace(Interp interp) {
        return interp.globalNs;
    }

    public static void pushCallFrame(Interp interp, CallFrame frame, Namespace namespace, boolean isProcCallFrame) {
        Namespace ns;
        if (namespace == null) {
            ns = getCurrentNamespace(interp);
        } else {
            ns = namespace;
            if ((ns.flags & NS_DEAD) != 0) {
                throw new TclRuntimeError("Trying to push call frame for dead namespace");
            }
        }
        ns.activationCount++;
        frame.ns = ns;
        frame.isProcCallFrame = isProcCallFrame;
        frame.objv = null;
        frame.caller = interp.frame;
        frame.callerVar = interp.varFrame;
        if (interp.varFrame != null) {
            frame.level = (interp.varFrame.level + 1);
        } else {
            frame.level = 1;
        }
        frame.varTable = null;
        interp.frame = frame;
        interp.varFrame = frame;
    }

    public static void popCallFrame(Interp interp) {
        CallFrame frame = interp.frame;
        int saveErrFlag;
        Namespace ns;
        interp.frame = frame.caller;
        interp.varFrame = frame.callerVar;
        saveErrFlag = (interp.flags & Parser.ERR_IN_PROGRESS);
        if (frame.varTable != null) {
            Var.deleteVars(interp, frame.varTable);
            frame.varTable = null;
        }
        interp.flags |= saveErrFlag;
        ns = frame.ns;
        ns.activationCount--;
        if (((ns.flags & NS_DYING) != 0) && (ns.activationCount == 0)) {
            deleteNamespace(ns);
        }
        frame.ns = null;
    }

    public static Namespace createNamespace(Interp interp, String name, DeleteProc deleteProc) {
        Namespace ns, ancestor;
        Namespace parent;
        Namespace globalNs = getGlobalNamespace(interp);
        String simpleName;
        StringBuffer buffer1, buffer2;
        if ((globalNs == null) && (interp.varFrame == null)) {
            parent = null;
            simpleName = "";
        } else if (name.length() == 0) {
            interp.setResult("can't create namespace \"\": only global namespace can have empty name");
            return null;
        } else {
            GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
            getNamespaceForQualName(interp, name, null, (CREATE_NS_IF_UNKNOWN | TCL.LEAVE_ERR_MSG), gnfqnr);
            parent = gnfqnr.ns;
            simpleName = gnfqnr.simpleName;
            if (simpleName.length() == 0) {
                return parent;
            }
            if (parent.childTable.get(simpleName) != null) {
                interp.setResult("can't create namespace \"" + name + "\": already exists");
                return null;
            }
        }
        ns = new Namespace();
        ns.name = simpleName;
        ns.fullName = null;
        ns.deleteProc = deleteProc;
        ns.parent = parent;
        ns.childTable = new HashMap();
        synchronized (nsMutex) {
            numNsCreated++;
            ns.nsId = numNsCreated;
        }
        ns.interp = interp;
        ns.flags = 0;
        ns.activationCount = 0;
        ns.refCount = 1;
        ns.cmdTable = new HashMap();
        ns.varTable = new HashMap();
        ns.exportArray = null;
        ns.numExportPatterns = 0;
        ns.maxExportPatterns = 0;
        ns.resolver = null;
        if (parent != null) {
            parent.childTable.put(simpleName, ns);
        }
        buffer1 = new StringBuffer();
        buffer2 = new StringBuffer();
        for (ancestor = ns; ancestor != null; ancestor = ancestor.parent) {
            if (ancestor != globalNs) {
                buffer1.append("::");
                buffer1.append(ancestor.name);
            }
            buffer1.append(buffer2);
            buffer2.setLength(0);
            buffer2.append(buffer1);
            buffer1.setLength(0);
        }
        name = buffer2.toString();
        ns.fullName = name;
        return ns;
    }

    public static void deleteNamespace(Namespace namespace) {
        Namespace ns = namespace;
        Interp interp = ns.interp;
        Namespace globalNs = getGlobalNamespace(interp);
        if (ns.activationCount > 0) {
            ns.flags |= NS_DYING;
            if (ns.parent != null) {
                ns.parent.childTable.remove(ns.name);
            }
            ns.parent = null;
        } else {
            teardownNamespace(ns);
            if ((ns != globalNs) || ((interp.flags & Parser.DELETED) != 0)) {
                Var.deleteVars(ns.interp, ns.varTable);
                ns.childTable.clear();
                ns.cmdTable.clear();
                if (ns.refCount == 0) {
                    free(ns);
                } else {
                    ns.flags |= NS_DEAD;
                }
            }
        }
    }

    static void teardownNamespace(Namespace ns) {
        Interp interp = ns.interp;
        Namespace childNs;
        WrappedCommand cmd;
        Namespace globalNs = getGlobalNamespace(interp);
        int i;
        if (ns == globalNs) {
            String errorInfoStr, errorCodeStr;
            try {
                errorInfoStr = interp.getVar("errorInfo", TCL.GLOBAL_ONLY).toString();
            } catch (TclException e) {
                errorInfoStr = null;
            }
            try {
                errorCodeStr = interp.getVar("errorCode", TCL.GLOBAL_ONLY).toString();
            } catch (TclException e) {
                errorCodeStr = null;
            }
            Var.deleteVars(interp, ns.varTable);
            if (errorInfoStr != null) {
                try {
                    interp.setVar("errorInfo", errorInfoStr, TCL.GLOBAL_ONLY);
                } catch (TclException e) {
                }
            }
            if (errorCodeStr != null) {
                try {
                    interp.setVar("errorCode", errorCodeStr, TCL.GLOBAL_ONLY);
                } catch (TclException e) {
                }
            }
        } else {
            Var.deleteVars(interp, ns.varTable);
        }
        if (ns.parent != null) {
            ns.parent.childTable.remove(ns.name);
        }
        ns.parent = null;
        while ((childNs = (Namespace) FirstHashEntry(ns.childTable)) != null) {
            deleteNamespace(childNs);
        }
        while ((cmd = (WrappedCommand) FirstHashEntry(ns.cmdTable)) != null) {
            interp.deleteCommandFromToken(cmd);
        }
        if (ns.exportArray != null) {
            ns.exportArray = null;
            ns.numExportPatterns = 0;
            ns.maxExportPatterns = 0;
        }
        if (ns.deleteProc != null) {
            ns.deleteProc.delete();
        }
        ns.deleteProc = null;
        ns.nsId = 0;
    }

    static void free(Namespace ns) {
        ns.name = null;
        ns.fullName = null;
    }

    public static void exportList(Interp interp, Namespace namespace, String pattern, boolean resetListFirst) throws TclException {
        final int INIT_EXPORT_PATTERNS = 5;
        Namespace ns, exportNs;
        Namespace currNs = getCurrentNamespace(interp);
        String simplePattern, patternCpy;
        int neededElems, len, i;
        if (namespace == null) {
            ns = currNs;
        } else {
            ns = namespace;
        }
        if (resetListFirst) {
            if (ns.exportArray != null) {
                for (i = 0; i < ns.numExportPatterns; i++) {
                    ns.exportArray[i] = null;
                }
                ns.exportArray = null;
                ns.numExportPatterns = 0;
                ns.maxExportPatterns = 0;
            }
        }
        GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
        getNamespaceForQualName(interp, pattern, ns, TCL.LEAVE_ERR_MSG, gnfqnr);
        exportNs = gnfqnr.ns;
        simplePattern = gnfqnr.simpleName;
        if ((exportNs != ns) || (pattern.compareTo(simplePattern) != 0)) {
            throw new TclException(interp, "invalid export pattern \"" + pattern + "\": pattern can't specify a namespace");
        }
        neededElems = ns.numExportPatterns + 1;
        if (ns.exportArray == null) {
            ns.exportArray = new String[INIT_EXPORT_PATTERNS];
            ns.numExportPatterns = 0;
            ns.maxExportPatterns = INIT_EXPORT_PATTERNS;
        } else if (neededElems > ns.maxExportPatterns) {
            int numNewElems = 2 * ns.maxExportPatterns;
            String[] newArray = new String[numNewElems];
            System.arraycopy(ns.exportArray, 0, newArray, 0, ns.numExportPatterns);
            ns.exportArray = newArray;
            ns.maxExportPatterns = numNewElems;
        }
        ns.exportArray[ns.numExportPatterns] = pattern;
        ns.numExportPatterns++;
        return;
    }

    static void appendExportList(Interp interp, Namespace namespace, TclObject obj) throws TclException {
        Namespace ns;
        int i;
        if (namespace == null) {
            ns = getCurrentNamespace(interp);
        } else {
            ns = namespace;
        }
        for (i = 0; i < ns.numExportPatterns; i++) {
            TclList.append(interp, obj, TclString.newInstance(ns.exportArray[i]));
        }
        return;
    }

    public static void importList(Interp interp, Namespace namespace, String pattern, boolean allowOverwrite) throws TclException {
        Namespace ns, importNs;
        Namespace currNs = getCurrentNamespace(interp);
        String simplePattern, cmdName;
        WrappedCommand cmd, realCmd;
        ImportRef ref;
        WrappedCommand autoCmd, importedCmd;
        ImportedCmdData data;
        boolean wasExported;
        int i, result;
        if (namespace == null) {
            ns = currNs;
        } else {
            ns = namespace;
        }
        autoCmd = findCommand(interp, "auto_import", null, TCL.GLOBAL_ONLY);
        if (autoCmd != null) {
            TclObject[] objv = new TclObject[2];
            objv[0] = TclString.newInstance("auto_import");
            objv[0].preserve();
            objv[1] = TclString.newInstance(pattern);
            objv[1].preserve();
            cmd = autoCmd;
            try {
                cmd.cmd.cmdProc(interp, objv);
            } finally {
                objv[0].release();
                objv[1].release();
            }
            interp.resetResult();
        }
        if (pattern.length() == 0) {
            throw new TclException(interp, "empty import pattern");
        }
        GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
        getNamespaceForQualName(interp, pattern, ns, TCL.LEAVE_ERR_MSG, gnfqnr);
        importNs = gnfqnr.ns;
        simplePattern = gnfqnr.simpleName;
        if (importNs == null) {
            throw new TclException(interp, "unknown namespace in import pattern \"" + pattern + "\"");
        }
        if (importNs == ns) {
            if (pattern == simplePattern) {
                throw new TclException(interp, "no namespace specified in import pattern \"" + pattern + "\"");
            } else {
                throw new TclException(interp, "import pattern \"" + pattern + "\" tries to import from namespace \"" + importNs.name + "\" into itself");
            }
        }
        for (Iterator iter = importNs.cmdTable.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            cmdName = (String) entry.getKey();
            if (Util.stringMatch(cmdName, simplePattern)) {
                wasExported = false;
                for (i = 0; i < importNs.numExportPatterns; i++) {
                    if (Util.stringMatch(cmdName, importNs.exportArray[i])) {
                        wasExported = true;
                        break;
                    }
                }
                if (!wasExported) {
                    continue;
                }
                if ((ns.cmdTable.get(cmdName) == null) || allowOverwrite) {
                    StringBuffer ds;
                    ds = new StringBuffer();
                    ds.append(ns.fullName);
                    if (ns != interp.globalNs) {
                        ds.append("::");
                    }
                    ds.append(cmdName);
                    cmd = (WrappedCommand) importNs.cmdTable.get(cmdName);
                    if (cmd.cmd instanceof ImportedCmdData) {
                        realCmd = getOriginalCommand(cmd);
                        if ((realCmd != null) && (realCmd.ns == currNs) && (currNs.cmdTable.get(cmdName) != null)) {
                            throw new TclException(interp, "import pattern \"" + pattern + "\" would create a loop containing command \"" + ds.toString() + "\"");
                        }
                    }
                    data = new ImportedCmdData();
                    interp.createCommand(ds.toString(), data);
                    importedCmd = findCommand(interp, ds.toString(), ns, (TCL.NAMESPACE_ONLY | TCL.LEAVE_ERR_MSG));
                    data.realCmd = cmd;
                    data.self = importedCmd;
                    ref = new ImportRef();
                    ref.importedCmd = importedCmd;
                    ref.next = cmd.importRef;
                    cmd.importRef = ref;
                } else {
                    throw new TclException(interp, "can't import command \"" + cmdName + "\": already exists");
                }
            }
        }
        return;
    }

    static void forgetImport(Interp interp, Namespace namespace, String pattern) throws TclException {
        Namespace ns, importNs, actualCtx;
        String simplePattern, cmdName;
        WrappedCommand cmd;
        if (namespace == null) {
            ns = getCurrentNamespace(interp);
        } else {
            ns = namespace;
        }
        GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
        getNamespaceForQualName(interp, pattern, ns, TCL.LEAVE_ERR_MSG, gnfqnr);
        importNs = gnfqnr.ns;
        actualCtx = gnfqnr.actualCxt;
        simplePattern = gnfqnr.simpleName;
        if (importNs == null) {
            throw new TclException(interp, "unknown namespace in namespace forget pattern \"" + pattern + "\"");
        }
        for (Iterator iter = importNs.cmdTable.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            cmdName = (String) entry.getKey();
            if (Util.stringMatch(cmdName, simplePattern)) {
                cmd = (WrappedCommand) ns.cmdTable.get(cmdName);
                if (cmd != null) {
                    if (cmd.cmd instanceof ImportedCmdData) {
                        interp.deleteCommandFromToken(cmd);
                    }
                }
            }
        }
        return;
    }

    public static WrappedCommand getOriginalCommand(WrappedCommand command) {
        WrappedCommand cmd = command;
        ImportedCmdData data;
        if (!(cmd.cmd instanceof ImportedCmdData)) {
            return null;
        }
        while (cmd.cmd instanceof ImportedCmdData) {
            data = (ImportedCmdData) cmd.cmd;
            cmd = data.realCmd;
        }
        return cmd;
    }

    static void invokeImportedCmd(Interp interp, ImportedCmdData data, TclObject[] objv) throws TclException {
        WrappedCommand realCmd = data.realCmd;
        realCmd.cmd.cmdProc(interp, objv);
    }

    static void deleteImportedCmd(ImportedCmdData data) {
        WrappedCommand realCmd = data.realCmd;
        WrappedCommand self = data.self;
        ImportRef ref, prev;
        prev = null;
        for (ref = realCmd.importRef; ref != null; ref = ref.next) {
            if (ref.importedCmd == self) {
                if (prev == null) {
                    realCmd.importRef = ref.next;
                } else {
                    prev.next = ref.next;
                }
                ref = null;
                data = null;
                return;
            }
            prev = ref;
        }
        throw new TclRuntimeError("DeleteImportedCmd: did not find cmd in real cmd's list of import references");
    }

    static class GetNamespaceForQualNameResult {

        Namespace ns;

        Namespace altNs;

        Namespace actualCxt;

        String simpleName;
    }

    static void getNamespaceForQualName(Interp interp, String qualName, Namespace cxtNs, int flags, GetNamespaceForQualNameResult gnfqnr) {
        gnfqnr.ns = null;
        gnfqnr.altNs = null;
        gnfqnr.actualCxt = null;
        gnfqnr.simpleName = null;
        Namespace ns = cxtNs;
        Namespace altNs;
        Namespace globalNs = getGlobalNamespace(interp);
        Namespace entryNs;
        String start, end;
        String nsName;
        int len;
        int start_ind, end_ind, name_len;
        if ((flags & TCL.GLOBAL_ONLY) != 0) {
            ns = globalNs;
        } else if (ns == null) {
            if (interp.varFrame != null) {
                ns = interp.varFrame.ns;
            } else {
                ns = interp.globalNs;
            }
        }
        start_ind = 0;
        name_len = qualName.length();
        if ((name_len >= 2) && (qualName.charAt(0) == ':') && (qualName.charAt(1) == ':')) {
            start_ind = 2;
            while ((start_ind < name_len) && (qualName.charAt(start_ind) == ':')) {
                start_ind++;
            }
            ns = globalNs;
            if (start_ind >= name_len) {
                gnfqnr.ns = globalNs;
                gnfqnr.altNs = null;
                gnfqnr.actualCxt = globalNs;
                gnfqnr.simpleName = "";
                return;
            }
        }
        gnfqnr.actualCxt = ns;
        altNs = globalNs;
        if ((ns == globalNs) || ((flags & (TCL.NAMESPACE_ONLY | FIND_ONLY_NS)) != 0)) {
            altNs = null;
        }
        end_ind = start_ind;
        while (start_ind < name_len) {
            len = 0;
            for (end_ind = start_ind; end_ind < name_len; end_ind++) {
                if (((name_len - end_ind) > 1) && (qualName.charAt(end_ind) == ':') && (qualName.charAt(end_ind + 1) == ':')) {
                    end_ind += 2;
                    while ((end_ind < name_len) && (qualName.charAt(end_ind) == ':')) {
                        end_ind++;
                    }
                    break;
                }
                len++;
            }
            if ((end_ind == name_len) && !((end_ind - start_ind >= 2) && ((qualName.charAt(end_ind - 1) == ':') && (qualName.charAt(end_ind - 2) == ':')))) {
                if ((flags & FIND_ONLY_NS) != 0) {
                    nsName = qualName.substring(start_ind);
                } else {
                    gnfqnr.ns = ns;
                    gnfqnr.altNs = altNs;
                    gnfqnr.simpleName = qualName.substring(start_ind);
                    return;
                }
            } else {
                nsName = qualName.substring(start_ind, start_ind + len);
            }
            if (ns != null) {
                entryNs = (Namespace) ns.childTable.get(nsName);
                if (entryNs != null) {
                    ns = entryNs;
                } else if ((flags & CREATE_NS_IF_UNKNOWN) != 0) {
                    CallFrame frame = interp.newCallFrame();
                    pushCallFrame(interp, frame, ns, false);
                    ns = createNamespace(interp, nsName, null);
                    popCallFrame(interp);
                    if (ns == null) {
                        throw new RuntimeException("Could not create namespace " + nsName);
                    }
                } else {
                    ns = null;
                }
            }
            if (altNs != null) {
                altNs = (Namespace) altNs.childTable.get(nsName);
            }
            if ((ns == null) && (altNs == null)) {
                gnfqnr.ns = null;
                gnfqnr.altNs = null;
                gnfqnr.simpleName = null;
                return;
            }
            start_ind = end_ind;
        }
        if (((flags & FIND_ONLY_NS) != 0) || ((end_ind > start_ind) && (qualName.charAt(end_ind - 1) != ':'))) {
            gnfqnr.simpleName = null;
        } else {
            gnfqnr.simpleName = qualName.substring(end_ind);
        }
        if (((flags & FIND_ONLY_NS) != 0) && (name_len == 0) && (ns != globalNs)) {
            ns = null;
        }
        gnfqnr.ns = ns;
        gnfqnr.altNs = altNs;
        return;
    }

    public static Namespace findNamespace(Interp interp, String name, Namespace contextNs, int flags) {
        Namespace ns;
        GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
        getNamespaceForQualName(interp, name, contextNs, (flags | FIND_ONLY_NS), gnfqnr);
        ns = gnfqnr.ns;
        if (ns != null) {
            return ns;
        } else if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
            interp.setResult("unknown namespace \"" + name + "\"");
        }
        return null;
    }

    public static WrappedCommand findCommand(Interp interp, String name, Namespace contextNs, int flags) throws TclException {
        Interp.ResolverScheme res;
        Namespace cxtNs;
        Namespace ns0, ns1;
        String simpleName;
        int search;
        WrappedCommand cmd;
        if ((flags & TCL.GLOBAL_ONLY) != 0) {
            cxtNs = getGlobalNamespace(interp);
        } else if (contextNs != null) {
            cxtNs = contextNs;
        } else {
            cxtNs = getCurrentNamespace(interp);
        }
        if (cxtNs.resolver != null || interp.resolvers != null) {
            try {
                if (cxtNs.resolver != null) {
                    cmd = cxtNs.resolver.resolveCmd(interp, name, cxtNs, flags);
                } else {
                    cmd = null;
                }
                if (cmd == null && interp.resolvers != null) {
                    for (ListIterator iter = interp.resolvers.listIterator(); cmd == null && iter.hasNext(); ) {
                        res = (Interp.ResolverScheme) iter.next();
                        cmd = res.resolver.resolveCmd(interp, name, cxtNs, flags);
                    }
                }
                if (cmd != null) {
                    return cmd;
                }
            } catch (TclException e) {
                return null;
            }
        }
        GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
        getNamespaceForQualName(interp, name, contextNs, flags, gnfqnr);
        ns0 = gnfqnr.ns;
        ns1 = gnfqnr.altNs;
        cxtNs = gnfqnr.actualCxt;
        simpleName = gnfqnr.simpleName;
        cmd = null;
        for (search = 0; (search < 2) && (cmd == null); search++) {
            Namespace ns;
            if (search == 0) {
                ns = ns0;
            } else if (search == 1) {
                ns = ns1;
            } else {
                throw new TclRuntimeError("bad search value" + search);
            }
            if ((ns != null) && (simpleName != null)) {
                cmd = (WrappedCommand) ns.cmdTable.get(simpleName);
            }
        }
        if (cmd != null) {
            return cmd;
        } else if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
            throw new TclException(interp, "unknown command \"" + name + "\"");
        }
        return null;
    }

    public static Var findNamespaceVar(Interp interp, String name, Namespace contextNs, int flags) throws TclException {
        Interp.ResolverScheme res;
        Namespace cxtNs;
        Namespace ns0, ns1;
        String simpleName;
        int search;
        Var var;
        if ((flags & TCL.GLOBAL_ONLY) != 0) {
            cxtNs = getGlobalNamespace(interp);
        } else if (contextNs != null) {
            cxtNs = contextNs;
        } else {
            cxtNs = getCurrentNamespace(interp);
        }
        if (cxtNs.resolver != null || interp.resolvers != null) {
            try {
                if (cxtNs.resolver != null) {
                    var = cxtNs.resolver.resolveVar(interp, name, cxtNs, flags);
                } else {
                    var = null;
                }
                if (var == null && interp.resolvers != null) {
                    for (ListIterator iter = interp.resolvers.listIterator(); var == null && iter.hasNext(); ) {
                        res = (Interp.ResolverScheme) iter.next();
                        var = res.resolver.resolveVar(interp, name, cxtNs, flags);
                    }
                }
                if (var != null) {
                    return var;
                }
            } catch (TclException e) {
                return null;
            }
        }
        GetNamespaceForQualNameResult gnfqnr = interp.getnfqnResult;
        getNamespaceForQualName(interp, name, contextNs, flags, gnfqnr);
        ns0 = gnfqnr.ns;
        ns1 = gnfqnr.altNs;
        cxtNs = gnfqnr.actualCxt;
        simpleName = gnfqnr.simpleName;
        var = null;
        for (search = 0; (search < 2) && (var == null); search++) {
            Namespace ns;
            if (search == 0) {
                ns = ns0;
            } else if (search == 1) {
                ns = ns1;
            } else {
                throw new TclRuntimeError("bad search value" + search);
            }
            if ((ns != null) && (simpleName != null)) {
                var = (Var) ns.varTable.get(simpleName);
            }
        }
        if (var != null) {
            return var;
        } else if ((flags & TCL.LEAVE_ERR_MSG) != 0) {
            interp.setResult("unknown variable \"" + name + "\"");
        }
        return null;
    }

    static void resetShadowedCmdRefs(Interp interp, WrappedCommand newCmd) {
        String cmdName;
        Namespace ns, tmpNs;
        Namespace trailNs, shadowNs;
        Namespace globalNs = getGlobalNamespace(interp);
        int i;
        boolean found;
        WrappedCommand wcmd;
        Namespace[] trailArray = null;
        int trailFront = -1;
        int trailSize = NUM_TRAIL_ELEMS;
        cmdName = newCmd.hashKey;
        for (ns = newCmd.ns; (ns != null) && (ns != globalNs); ns = ns.parent) {
            found = true;
            shadowNs = globalNs;
            for (i = trailFront; i >= 0; i--) {
                trailNs = trailArray[i];
                tmpNs = (Namespace) shadowNs.childTable.get(trailNs.name);
                if (tmpNs != null) {
                    shadowNs = tmpNs;
                } else {
                    found = false;
                    break;
                }
            }
            if (found) {
                wcmd = (WrappedCommand) shadowNs.cmdTable.get(cmdName);
                if (wcmd != null) {
                    for (Iterator iter = ns.cmdTable.entrySet().iterator(); iter.hasNext(); ) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        wcmd = (WrappedCommand) entry.getValue();
                        wcmd.incrEpoch();
                    }
                }
            }
            if (trailArray == null) {
                trailArray = new Namespace[NUM_TRAIL_ELEMS];
            }
            trailFront++;
            if (trailFront == trailSize) {
                int size = trailSize * 2;
                Namespace[] tmp = new Namespace[size];
                System.arraycopy(trailArray, 0, tmp, 0, trailArray.length);
                trailArray = tmp;
                trailSize = size;
            }
            trailArray[trailFront] = ns;
        }
    }

    /**
     *----------------------------------------------------------------------
     *
     * Tcl_SetNamespaceResolvers -> setNamespaceResolver
     *
     *	Sets the command/variable resolution object for a namespace,
     *	thereby changing the way that command/variable names are
     *	interpreted.  This allows extension writers to support different
     *	name resolution schemes, such as those for object-oriented
     *	packages.
     *
     *	Command resolution is handled by the following method:
     *
     *  resolveCmd (Interp interp, String name,
     *      Namespace context, int flags)
     *  throws TclException;
     *          
     *	Whenever a command is executed or Namespace.findCommand is invoked
     *	within the namespace, this method is called to resolve the
     *	command name.  If this method is able to resolve the name,
     *	it should return the corresponding WrappedCommand.  Otherwise,
     *	the procedure can return null, and the command will
     *	be treated under the usual name resolution rules.  Or, it can
     *	throw a TclException, and the command will be considered invalid.
     *
     *	Variable resolution is handled by the following method:
     *
     *  resolveVar (Interp interp, String name,
     *      Namespace context, int flags)
     *  throws TclException;
     *
     *  If this method is able to resolve the name, it should return
     *  the variable as Var object.  The method may also
     *	return null, and the variable will be treated under the usual
     *  name resolution rules.  Or, it can throw a TclException,
     *	and the variable will be considered invalid.
     *
     * Results:
     *	See above.
     *
     * Side effects:
     *	None.
     *
     *----------------------------------------------------------------------
     */
    public static void setNamespaceResolver(Namespace namespace, Resolver resolver) {
        namespace.resolver = resolver;
    }

    /**
     *----------------------------------------------------------------------
     *
     * Tcl_GetNamespaceResolvers -> getNamespaceResolver
     *
     *	Returns the current command/variable resolution object
     *	for a namespace.  By default, these objects are null.
     *	New objects can be installed by calling setNamespaceResolver,
     *  to provide new name resolution rules.
     *
     * Results:
     *	Returns the esolver object assigned to this namespace.
     *	Returns null otherwise.
     *
     * Side effects:
     *	None.
     *
     *----------------------------------------------------------------------
     */
    static Resolver getNamespaceResolver(Namespace namespace) {
        return namespace.resolver;
    }

    /**
     *----------------------------------------------------------------------
     *
     * Tcl_FirstHashEntry -> FirstHashEntry
     *
     *	Return the first Object value contained in the given table.
     *	This method is used only when taking apart a table where
     *	entries in the table could be removed elsewhere. An Iterator
     *	is no longer valid once entries have been removed so it
     *	is not possible to take a table apart safely with a single
     *	iterator. This method returns null when there are no more
     *	elements in the table, so it should not be used with a
     *	table that contains null values. This method is not efficient,
     *	but it is required when dealing with a Java Iterator when
     *	the table being iterated could have elements added or deleted.
     *
     *----------------------------------------------------------------------
     */
    static Object FirstHashEntry(HashMap table) {
        Object retVal;
        Set eset = table.entrySet();
        if (eset.size() == 0) {
            return null;
        }
        Iterator iter = eset.iterator();
        if (!iter.hasNext()) {
            throw new TclRuntimeError("no next() object but set size was " + eset.size());
        }
        Map.Entry entry = (Map.Entry) iter.next();
        retVal = entry.getValue();
        if (retVal == null) {
            throw new TclRuntimeError("entry value should not be null");
        }
        return retVal;
    }
}
