package tm.cpp.analysis;

import java.util.Enumeration;
import java.util.Vector;
import tm.clc.analysis.DeclarationSet;
import tm.clc.analysis.DeclarationSetMulti;
import tm.clc.analysis.FunctionDeclaration;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.utilities.Assert;
import tm.utilities.Debug;

/**
 * Performs overloaded function resolution during code parsing.
 * Singleton pattern.
 * @author Derek Reilly
 * @created Oct 12, 2001
 */
public class OverloadResolver {

    /** 1. a function named in the function call syntax (13.3.1.1.1) */
    public static final int FN_CALL_SYNTAX = 0;

    /** 2. a function call operator, a pointer-to-function
        or reference-to-pointer-to-function conversion function, or a
        reference-to-function conversion function on a class object named
        in the function call syntax (13.3.1.1.2) */
    public static final int FN_CALL_OP = 1;

    /** 3. the operator referenced in an expression (13.3.1.2) */
    public static final int OP_IN_EXP = 2;

    /** 4. a constructor for direct-initialization of a class
        object (13.3.1.3) */
    public static final int CONSTRUCTOR = 3;

    /** 5. a user-defined conversion for copy-constructor of a class object
        (13.3.1.4) */
    public static final int COPY_CONV = 4;

    /** 6. a conversion function for initialization of a nonclass type from
        an expression of class type (13.3.1.5) */
    public static final int NONCLASS_CONV = 5;

    /** 7. a conversion function to an lvalue to which a reference will be
        directly bound */
    public static final int REF_CONV = 6;

    private static final int EXACT_MATCH = 10;

    private static final int PROMOTION = 9;

    private static final int CONVERSION = 8;

    private static final int PTR_TO_BOOL_CONVERSION = 7;

    private static final int USER_DEFINED = 6;

    private static final int ELLIPSIS = 5;

    private static final int AMBIGUOUS = -1;

    private static final int NONE = -2;

    private static OverloadResolver instance = new OverloadResolver();

    private OverloadResolver() {
    }

    private static StandardConversions sc = StandardConversions.getInstance();

    private static Debug debug = Debug.getInstance();

    /**
     * Provides access to the single <code>OverloadResolver</code> instance.
     *
     * @return the instance
     */
    public static OverloadResolver getInstance() {
        return instance;
    }

    /**
     * Given a function id and type, provides the "munged" name, suitable
     * for runtime identification
     * <p> for example, a function with signature <code>void f (int x)</code>
     * , defined in global scope would have a munged id of <em>::f$void(int)</em>
     * @param id the function id
     * @param ft the function type
     * @return the munged name
     */
    public static String munge(ScopedName id, TypeNode ft) {
        return munge(id, ft, true);
    }

    public static String munge(ScopedName id, TypeNode ft, boolean fqn) {
        String name = (fqn) ? id.getName() : id.getTerminalId();
        return name + "$" + ft.typeId();
    }

    /**
     * Finds the <code>FunctionDeclaration</code> with the same parameter types
     * in the same order as are found in the parameter list. Only exact type
     * equivalence on each parameter will match, disregarding top level cvq.
     * @param decls the list of declarations to search for a match
     * @param tyFun the type of the function.
     * @return the matching declaration, null if none matches.
     */
    public static FunctionDeclaration findMatch(DeclarationSet decls, TyAbstractFun tyFun) {
        FunctionDeclaration match = null;
        for (Enumeration e = decls.elements(); e.hasMoreElements(); ) {
            Object nextDecl = e.nextElement();
            FunctionDeclaration d = (FunctionDeclaration) nextDecl;
            Vector pTypes = d.getParameters();
            if (pTypes.size() == tyFun.getParamCount()) {
                int i = 0;
                for (; i < tyFun.getParamCount(); i++) {
                    TypeNode pt = tyFun.getParamType(i);
                    TypeNode dpt = (TypeNode) pTypes.elementAt(i);
                    if (!sc.equivalentParameterTypes(dpt, pt)) break;
                }
                if (i == tyFun.getParamCount()) match = d;
            }
        }
        return match;
    }

    /**
     * Performs overload resolution for default context (regular function
     * call in the function call syntax)
     * @param candidates the set of candidate function
     * <code>Declarations</code>
     * @param args an ordered list of the <code>TypeNodes</code> corresponding
     * to the argument types encountered in the function call
     * @return the <code>RankedFunction</code> that best matches the arg list,
     * or <code>null</code> if the function call cannot be disambiguated.
     */
    public RankedFunction disambiguate(DeclarationSet candidates, Vector args) {
        return disambiguate(candidates, args, FN_CALL_SYNTAX);
    }

    /**
     * Performs overload resolution.
     * @param candidates the set of candidate function
     * <code>Declarations</code>
     * @param args an ordered list of the <code>TypeNodes</code> corresponding
     * to the argument types encountered in the function call
     * @param context the context in which the overload resolution is taking
     * place (see the static constants defined in this class)
     * @return the <code>RankedFunction</code> that best matches the arg list,
     * or <code>null</code> if the function call cannot be disambiguated.
     */
    public RankedFunction disambiguate(DeclarationSet candidates, Vector args, int context) {
        RankedFunction best_function = null;
        Vector viable_functions = getViableFunctions(candidates, args, context);
        if (!viable_functions.isEmpty()) {
            debug.msg(Debug.COMPILE, "comparing set of viable functions");
            best_function = getBestViableFunction(viable_functions, args, context);
        }
        debug.msg(Debug.COMPILE, "best_function null : " + (best_function == null));
        return best_function;
    }

    private Vector getViableFunctions(DeclarationSet candidates, Vector args, int context) {
        DeclarationSetMulti viable_functions = new DeclarationSetMulti();
        int argCount = args.size();
        debug.msg(Debug.COMPILE, "getting viable functions");
        for (Enumeration e = candidates.elements(); e.hasMoreElements(); ) {
            FunctionDeclaration fd = (FunctionDeclaration) e.nextElement();
            debug.msg(Debug.COMPILE, "looking at candidate " + fd.getRuntimeId());
            Vector params = fd.getParameters();
            int fdParamCount = params.size();
            debug.msg(Debug.COMPILE, "param count " + fdParamCount + " arg count " + argCount);
            if (fdParamCount == argCount || (fdParamCount < argCount && fd.hasEllipsis()) || (fdParamCount > argCount && fd.defaultValuesAfter(argCount - 1))) {
                RankedFunction rf = new RankedFunction(fd, argCount);
                int pidx = 0;
                for (; pidx < argCount; pidx++) {
                    if (pidx >= fdParamCount) rf.addRanking(pidx, ELLIPSIS); else {
                        TypeNode pType = (TypeNode) params.elementAt(pidx);
                        TypeNode aType = (TypeNode) args.elementAt(pidx);
                        int cRank = conversionRanking(aType, pType, context);
                        if (cRank == NONE || cRank == AMBIGUOUS) break;
                        rf.addRanking(pidx, cRank);
                    }
                }
                if (pidx == argCount) {
                    debug.msg(Debug.COMPILE, "Viable: " + rf);
                    viable_functions.addElement(rf);
                }
            }
        }
        return viable_functions;
    }

    private RankedFunction getBestViableFunction(Vector viable_functions, Vector args, int context) {
        boolean multipleBest = false;
        Enumeration e = viable_functions.elements();
        RankedFunction bestSoFar = (RankedFunction) e.nextElement();
        while (e.hasMoreElements()) {
            RankedFunction fd = (RankedFunction) e.nextElement();
            int overall = 0;
            Assert.check(fd.rankings.length == bestSoFar.rankings.length);
            debug.msg(Debug.COMPILE, "Comparing " + fd + " to " + bestSoFar);
            for (int i = 0; i < fd.rankings.length; i++) {
                int champRank = bestSoFar.rankings[i];
                int chalRank = fd.rankings[i];
                if (champRank > chalRank) {
                    if (overall == 1) {
                        overall = 0;
                        break;
                    } else {
                        overall = -1;
                    }
                } else if (champRank < chalRank) {
                    if (overall == -1) {
                        overall = 0;
                        break;
                    } else {
                        overall = 1;
                    }
                }
            }
            debug.msg(Debug.COMPILE, overall == 1 ? "better" : overall == 0 ? "same" : "worse");
            switch(overall) {
                case 1:
                    multipleBest = false;
                    bestSoFar = fd;
                    break;
                case 0:
                    multipleBest = true;
                    break;
                default:
                    break;
            }
        }
        debug.msg(Debug.COMPILE, "multiple best is " + multipleBest);
        return (multipleBest) ? null : bestSoFar;
    }

    private int conversionRanking(TypeNode from, TypeNode to, int context) {
        int rank = NONE;
        while (from instanceof TyAbstractRef) from = ((TyAbstractRef) from).getPointeeType();
        while (to instanceof TyAbstractRef) to = ((TyAbstractRef) to).getPointeeType();
        boolean[] uninteresting = new boolean[1];
        switch(sc.determine(from, to, uninteresting)) {
            case StandardConversions.EQUIVALENT_TYPES:
            case StandardConversions.LVAL_TO_RVAL:
            case StandardConversions.ARRAY_TO_POINTER:
            case StandardConversions.FUNCTION_TO_POINTER:
            case StandardConversions.QUALIFICATION:
                rank = EXACT_MATCH;
                break;
            case StandardConversions.INT_PROMOTION:
            case StandardConversions.FLOAT_PROMOTION:
                rank = PROMOTION;
                break;
            case StandardConversions.INT_CONVERSION:
            case StandardConversions.FLOAT_CONVERSION:
            case StandardConversions.FLOAT_INT_CONVERSION:
            case StandardConversions.PTR_CONVERSION:
            case StandardConversions.PTR_TO_MEMBER_CONVERSION:
            case StandardConversions.BOOL_CONVERSION:
                rank = CONVERSION;
                break;
            case StandardConversions.PTR_TO_BOOL:
                rank = PTR_TO_BOOL_CONVERSION;
                break;
            case StandardConversions.USER_DEFINED:
                rank = USER_DEFINED;
                break;
            default:
                rank = NONE;
                break;
        }
        return rank;
    }
}
