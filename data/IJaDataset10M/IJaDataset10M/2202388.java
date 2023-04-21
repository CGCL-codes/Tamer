package joeq.Compiler.Analysis.IPA;

import jwutil.collections.IndexMap;
import jwutil.collections.IndexedMap;
import jwutil.graphs.PathNumbering;

public class PAProxy {

    public PAProxy(PA that) {
        this.VerifyAssertions = that.VerifyAssertions;
        this.WRITE_PARESULTS_BATCHFILE = that.WRITE_PARESULTS_BATCHFILE;
        this.TRACE = that.TRACE;
        this.TRACE_SOLVER = that.TRACE_SOLVER;
        this.TRACE_BIND = that.TRACE_BIND;
        this.TRACE_RELATIONS = that.TRACE_RELATIONS;
        this.TRACE_OBJECT = that.TRACE_OBJECT;
        this.TRACE_CONTEXT = that.TRACE_CONTEXT;
        this.out = that.out;
        this.USE_JOEQ_CLASSLIBS = that.USE_JOEQ_CLASSLIBS;
        this.INCREMENTAL1 = that.INCREMENTAL1;
        this.INCREMENTAL2 = that.INCREMENTAL2;
        this.INCREMENTAL3 = that.INCREMENTAL3;
        this.ADD_CLINIT = that.ADD_CLINIT;
        this.ADD_THREADS = that.ADD_THREADS;
        this.ADD_FINALIZERS = that.ADD_FINALIZERS;
        this.IGNORE_EXCEPTIONS = that.IGNORE_EXCEPTIONS;
        this.FILTER_VP = that.FILTER_VP;
        this.FILTER_HP = that.FILTER_HP;
        this.CARTESIAN_PRODUCT = that.CARTESIAN_PRODUCT;
        this.THREAD_SENSITIVE = that.THREAD_SENSITIVE;
        this.OBJECT_SENSITIVE = that.OBJECT_SENSITIVE;
        this.CONTEXT_SENSITIVE = that.CONTEXT_SENSITIVE;
        this.CS_CALLGRAPH = that.CS_CALLGRAPH;
        this.DISCOVER_CALL_GRAPH = that.DISCOVER_CALL_GRAPH;
        this.DUMP_DOTGRAPH = that.DUMP_DOTGRAPH;
        this.FILTER_NULL = that.FILTER_NULL;
        this.LONG_LOCATIONS = that.LONG_LOCATIONS;
        this.INCLUDE_UNKNOWN_TYPES = that.INCLUDE_UNKNOWN_TYPES;
        this.INCLUDE_ALL_UNKNOWN_TYPES = that.INCLUDE_ALL_UNKNOWN_TYPES;
        this.MAX_PARAMS = that.MAX_PARAMS;
        this.bddnodes = that.bddnodes;
        this.bddcache = that.bddcache;
        this.resultsFileName = that.resultsFileName;
        this.callgraphFileName = that.callgraphFileName;
        this.initialCallgraphFileName = that.initialCallgraphFileName;
        this.USE_VCONTEXT = that.USE_VCONTEXT;
        this.USE_HCONTEXT = that.USE_HCONTEXT;
        this.newMethodSummaries = that.newMethodSummaries;
        this.rootMethods = that.rootMethods;
        this.cg = that.cg;
        this.ocg = that.ocg;
        this.bdd = that.bdd;
        this.V1 = that.V1;
        this.V2 = that.V2;
        this.I = that.I;
        this.H1 = that.H1;
        this.H2 = that.H2;
        this.Z = that.Z;
        this.F = that.F;
        this.T1 = that.T1;
        this.T2 = that.T2;
        this.N = that.N;
        this.M = that.M;
        this.V1c = that.V1c;
        this.V2c = that.V2c;
        this.H1c = that.H1c;
        this.H2c = that.H2c;
        this.V_BITS = that.V_BITS;
        this.I_BITS = that.I_BITS;
        this.H_BITS = that.H_BITS;
        this.Z_BITS = that.Z_BITS;
        this.F_BITS = that.F_BITS;
        this.T_BITS = that.T_BITS;
        this.N_BITS = that.N_BITS;
        this.M_BITS = that.M_BITS;
        this.VC_BITS = that.VC_BITS;
        this.HC_BITS = that.HC_BITS;
        this.MAX_VC_BITS = that.MAX_VC_BITS;
        this.MAX_HC_BITS = that.MAX_HC_BITS;
        this.Vmap = that.Vmap;
        this.Imap = that.Imap;
        this.Hmap = that.Hmap;
        this.Fmap = that.Fmap;
        this.Tmap = that.Tmap;
        this.Nmap = that.Nmap;
        this.Mmap = that.Mmap;
        this.vCnumbering = that.vCnumbering;
        this.hCnumbering = that.hCnumbering;
        this.oCnumbering = that.oCnumbering;
        this.A = that.A;
        this.vP = that.vP;
        this.S = that.S;
        this.L = that.L;
        this.vT = that.vT;
        this.hT = that.hT;
        this.aT = that.aT;
        this.cha = that.cha;
        this.actual = that.actual;
        this.formal = that.formal;
        this.Iret = that.Iret;
        this.Mret = that.Mret;
        this.Ithr = that.Ithr;
        this.Mthr = that.Mthr;
        this.mI = that.mI;
        this.mV = that.mV;
        this.sync = that.sync;
        this.fT = that.fT;
        this.fC = that.fC;
        this.hP = that.hP;
        this.IE = that.IE;
        this.IEcs = that.IEcs;
        this.vPfilter = that.vPfilter;
        this.hPfilter = that.hPfilter;
        this.NNfilter = that.NNfilter;
        this.IEfilter = that.IEfilter;
        this.visited = that.visited;
        this.staticCalls = that.staticCalls;
        this.reverseLocal = that.reverseLocal;
        this.varorder = that.varorder;
        this.V1toV2 = that.V1toV2;
        this.V2toV1 = that.V2toV1;
        this.H1toH2 = that.H1toH2;
        this.H2toH1 = that.H2toH1;
        this.V1H1toV2H2 = that.V1H1toV2H2;
        this.V2H2toV1H1 = that.V2H2toV1H1;
        this.V1ctoV2c = that.V1ctoV2c;
        this.V1cV2ctoV2cV1c = that.V1cV2ctoV2cV1c;
        this.V1cH1ctoV2cV1c = that.V1cH1ctoV2cV1c;
        this.T2toT1 = that.T2toT1;
        this.T1toT2 = that.T1toT2;
        this.H1toV1c = that.H1toV1c;
        this.V1ctoH1 = that.V1ctoH1;
        this.V1csets = that.V1csets;
        this.V1cH1equals = that.V1cH1equals;
        this.V1set = that.V1set;
        this.V2set = that.V2set;
        this.H1set = that.H1set;
        this.H2set = that.H2set;
        this.T1set = that.T1set;
        this.T2set = that.T2set;
        this.Fset = that.Fset;
        this.Mset = that.Mset;
        this.Nset = that.Nset;
        this.Iset = that.Iset;
        this.Zset = that.Zset;
        this.V1V2set = that.V1V2set;
        this.V1Fset = that.V1Fset;
        this.V2Fset = that.V2Fset;
        this.V1FV2set = that.V1FV2set;
        this.V1H1set = that.V1H1set;
        this.H1Fset = that.H1Fset;
        this.H2Fset = that.H2Fset;
        this.H1H2set = that.H1H2set;
        this.H1FH2set = that.H1FH2set;
        this.IMset = that.IMset;
        this.INset = that.INset;
        this.INH1set = that.INH1set;
        this.INT2set = that.INT2set;
        this.T2Nset = that.T2Nset;
        this.MZset = that.MZset;
        this.V1cset = that.V1cset;
        this.V2cset = that.V2cset;
        this.H1cset = that.H1cset;
        this.H2cset = that.H2cset;
        this.V1cV2cset = that.V1cV2cset;
        this.V1cH1cset = that.V1cH1cset;
        this.H1cH2cset = that.H1cH2cset;
        this.V1cdomain = that.V1cdomain;
        this.V2cdomain = that.V2cdomain;
        this.H1cdomain = that.H1cdomain;
        this.H2cdomain = that.H2cdomain;
        this.rangeMap = that.rangeMap;
        this.object_class = that.object_class;
        this.javaLangObject_clone = that.javaLangObject_clone;
        this.cloneable_class = that.cloneable_class;
        this.throwable_class = that.throwable_class;
        this.javaLangObject_fakeclone = that.javaLangObject_fakeclone;
        this.last_V = that.last_V;
        this.last_H = that.last_H;
        this.last_T = that.last_T;
        this.last_N = that.last_N;
        this.last_F = that.last_F;
        this.finalizer_method = that.finalizer_method;
        this.main_method = that.main_method;
        this.run_method = that.run_method;
        this.old1_A = that.old1_A;
        this.old1_S = that.old1_S;
        this.old1_L = that.old1_L;
        this.old1_vP = that.old1_vP;
        this.old1_hP = that.old1_hP;
        this.old3_t3 = that.old3_t3;
        this.old3_vP = that.old3_vP;
        this.old3_t4 = that.old3_t4;
        this.old3_hT = that.old3_hT;
        this.old3_t6 = that.old3_t6;
        this.old3_t9 = that.old3_t9;
        this.old2_myIE = that.old2_myIE;
        this.old2_visited = that.old2_visited;
        this.TS = that.TS;
        this.thread_runs = that.thread_runs;
        this.varPathSelector = that.varPathSelector;
        this.THREADS_ONLY = that.THREADS_ONLY;
        this.heapPathSelector = that.heapPathSelector;
        this.polyClasses = that.polyClasses;
        this.MATCH_FACTORY = that.MATCH_FACTORY;
        this.objectPathSelector = that.objectPathSelector;
        this.V1H1correspondence = that.V1H1correspondence;
    }

    public static boolean VerifyAssertions;

    public static boolean WRITE_PARESULTS_BATCHFILE;

    public boolean TRACE;

    public boolean TRACE_SOLVER;

    public boolean TRACE_BIND;

    public boolean TRACE_RELATIONS;

    public boolean TRACE_OBJECT;

    public boolean TRACE_CONTEXT;

    public java.io.PrintStream out;

    public static boolean USE_JOEQ_CLASSLIBS;

    public boolean INCREMENTAL1;

    public boolean INCREMENTAL2;

    public boolean INCREMENTAL3;

    public boolean ADD_CLINIT;

    public boolean ADD_THREADS;

    public boolean ADD_FINALIZERS;

    public boolean IGNORE_EXCEPTIONS;

    public boolean FILTER_VP;

    public boolean FILTER_HP;

    public boolean CARTESIAN_PRODUCT;

    public boolean THREAD_SENSITIVE;

    public boolean OBJECT_SENSITIVE;

    public boolean CONTEXT_SENSITIVE;

    public boolean CS_CALLGRAPH;

    public boolean DISCOVER_CALL_GRAPH;

    public boolean DUMP_DOTGRAPH;

    public boolean FILTER_NULL;

    public boolean LONG_LOCATIONS;

    public boolean INCLUDE_UNKNOWN_TYPES;

    public boolean INCLUDE_ALL_UNKNOWN_TYPES;

    public int MAX_PARAMS;

    public int bddnodes;

    public int bddcache;

    public static java.lang.String resultsFileName;

    public static java.lang.String callgraphFileName;

    public static java.lang.String initialCallgraphFileName;

    public boolean USE_VCONTEXT;

    public boolean USE_HCONTEXT;

    public java.util.Map newMethodSummaries;

    public java.util.Set rootMethods;

    public joeq.Compiler.Quad.CallGraph cg;

    public joeq.Compiler.Analysis.IPA.ObjectCreationGraph ocg;

    public net.sf.javabdd.BDDFactory bdd;

    public net.sf.javabdd.BDDDomain V1;

    public net.sf.javabdd.BDDDomain V2;

    public net.sf.javabdd.BDDDomain I;

    public net.sf.javabdd.BDDDomain H1;

    public net.sf.javabdd.BDDDomain H2;

    public net.sf.javabdd.BDDDomain Z;

    public net.sf.javabdd.BDDDomain F;

    public net.sf.javabdd.BDDDomain T1;

    public net.sf.javabdd.BDDDomain T2;

    public net.sf.javabdd.BDDDomain N;

    public net.sf.javabdd.BDDDomain M;

    public net.sf.javabdd.BDDDomain[] V1c;

    public net.sf.javabdd.BDDDomain[] V2c;

    public net.sf.javabdd.BDDDomain[] H1c;

    public net.sf.javabdd.BDDDomain[] H2c;

    public int V_BITS;

    public int I_BITS;

    public int H_BITS;

    public int Z_BITS;

    public int F_BITS;

    public int T_BITS;

    public int N_BITS;

    public int M_BITS;

    public int VC_BITS;

    public int HC_BITS;

    public int MAX_VC_BITS;

    public int MAX_HC_BITS;

    public IndexMap Vmap;

    public IndexMap Imap;

    public IndexedMap Hmap;

    public IndexMap Fmap;

    public IndexMap Tmap;

    public IndexMap Nmap;

    public IndexMap Mmap;

    public PathNumbering vCnumbering;

    public PathNumbering hCnumbering;

    public PathNumbering oCnumbering;

    public net.sf.javabdd.BDD A;

    public net.sf.javabdd.BDD vP;

    public net.sf.javabdd.BDD S;

    public net.sf.javabdd.BDD L;

    public net.sf.javabdd.BDD vT;

    public net.sf.javabdd.BDD hT;

    public net.sf.javabdd.BDD aT;

    public net.sf.javabdd.BDD cha;

    public net.sf.javabdd.BDD actual;

    public net.sf.javabdd.BDD formal;

    public net.sf.javabdd.BDD Iret;

    public net.sf.javabdd.BDD Mret;

    public net.sf.javabdd.BDD Ithr;

    public net.sf.javabdd.BDD Mthr;

    public net.sf.javabdd.BDD mI;

    public net.sf.javabdd.BDD mV;

    public net.sf.javabdd.BDD sync;

    public net.sf.javabdd.BDD fT;

    public net.sf.javabdd.BDD fC;

    public net.sf.javabdd.BDD hP;

    public net.sf.javabdd.BDD IE;

    public net.sf.javabdd.BDD IEcs;

    public net.sf.javabdd.BDD vPfilter;

    public net.sf.javabdd.BDD hPfilter;

    public net.sf.javabdd.BDD NNfilter;

    public net.sf.javabdd.BDD IEfilter;

    public net.sf.javabdd.BDD visited;

    public net.sf.javabdd.BDD staticCalls;

    public boolean reverseLocal;

    public java.lang.String varorder;

    public net.sf.javabdd.BDDPairing V1toV2;

    public net.sf.javabdd.BDDPairing V2toV1;

    public net.sf.javabdd.BDDPairing H1toH2;

    public net.sf.javabdd.BDDPairing H2toH1;

    public net.sf.javabdd.BDDPairing V1H1toV2H2;

    public net.sf.javabdd.BDDPairing V2H2toV1H1;

    public net.sf.javabdd.BDDPairing V1ctoV2c;

    public net.sf.javabdd.BDDPairing V1cV2ctoV2cV1c;

    public net.sf.javabdd.BDDPairing V1cH1ctoV2cV1c;

    public net.sf.javabdd.BDDPairing T2toT1;

    public net.sf.javabdd.BDDPairing T1toT2;

    public net.sf.javabdd.BDDPairing[] H1toV1c;

    public net.sf.javabdd.BDDPairing[] V1ctoH1;

    public net.sf.javabdd.BDDVarSet[] V1csets;

    public net.sf.javabdd.BDD[] V1cH1equals;

    public net.sf.javabdd.BDDVarSet V1set;

    public net.sf.javabdd.BDDVarSet V2set;

    public net.sf.javabdd.BDDVarSet H1set;

    public net.sf.javabdd.BDDVarSet H2set;

    public net.sf.javabdd.BDDVarSet T1set;

    public net.sf.javabdd.BDDVarSet T2set;

    public net.sf.javabdd.BDDVarSet Fset;

    public net.sf.javabdd.BDDVarSet Mset;

    public net.sf.javabdd.BDDVarSet Nset;

    public net.sf.javabdd.BDDVarSet Iset;

    public net.sf.javabdd.BDDVarSet Zset;

    public net.sf.javabdd.BDDVarSet V1V2set;

    public net.sf.javabdd.BDDVarSet V1Fset;

    public net.sf.javabdd.BDDVarSet V2Fset;

    public net.sf.javabdd.BDDVarSet V1FV2set;

    public net.sf.javabdd.BDDVarSet V1H1set;

    public net.sf.javabdd.BDDVarSet H1Fset;

    public net.sf.javabdd.BDDVarSet H2Fset;

    public net.sf.javabdd.BDDVarSet H1H2set;

    public net.sf.javabdd.BDDVarSet H1FH2set;

    public net.sf.javabdd.BDDVarSet IMset;

    public net.sf.javabdd.BDDVarSet INset;

    public net.sf.javabdd.BDDVarSet INH1set;

    public net.sf.javabdd.BDDVarSet INT2set;

    public net.sf.javabdd.BDDVarSet T2Nset;

    public net.sf.javabdd.BDDVarSet MZset;

    public net.sf.javabdd.BDDVarSet V1cset;

    public net.sf.javabdd.BDDVarSet V2cset;

    public net.sf.javabdd.BDDVarSet H1cset;

    public net.sf.javabdd.BDDVarSet H2cset;

    public net.sf.javabdd.BDDVarSet V1cV2cset;

    public net.sf.javabdd.BDDVarSet V1cH1cset;

    public net.sf.javabdd.BDDVarSet H1cH2cset;

    public net.sf.javabdd.BDD V1cdomain;

    public net.sf.javabdd.BDD V2cdomain;

    public net.sf.javabdd.BDD H1cdomain;

    public net.sf.javabdd.BDD H2cdomain;

    public java.util.Map rangeMap;

    public joeq.Class.jq_Class object_class;

    public joeq.Class.jq_Method javaLangObject_clone;

    public joeq.Class.jq_Class cloneable_class;

    public joeq.Class.jq_Class throwable_class;

    public joeq.Class.jq_Method javaLangObject_fakeclone;

    public int last_V;

    public int last_H;

    public int last_T;

    public int last_N;

    public int last_F;

    public joeq.Class.jq_NameAndDesc finalizer_method;

    public static joeq.Class.jq_NameAndDesc main_method;

    public static joeq.Class.jq_NameAndDesc run_method;

    public net.sf.javabdd.BDD old1_A;

    public net.sf.javabdd.BDD old1_S;

    public net.sf.javabdd.BDD old1_L;

    public net.sf.javabdd.BDD old1_vP;

    public net.sf.javabdd.BDD old1_hP;

    public net.sf.javabdd.BDD old3_t3;

    public net.sf.javabdd.BDD old3_vP;

    public net.sf.javabdd.BDD old3_t4;

    public net.sf.javabdd.BDD old3_hT;

    public net.sf.javabdd.BDD old3_t6;

    public net.sf.javabdd.BDD[] old3_t9;

    public net.sf.javabdd.BDD old2_myIE;

    public net.sf.javabdd.BDD old2_visited;

    public joeq.Compiler.Analysis.IPA.PA.ToString TS;

    public static java.util.Map thread_runs;

    public joeq.Compiler.Analysis.IPA.PA.VarPathSelector varPathSelector;

    public static boolean THREADS_ONLY;

    public joeq.Compiler.Analysis.IPA.PA.HeapPathSelector heapPathSelector;

    public static java.util.Set polyClasses;

    public static boolean MATCH_FACTORY;

    public joeq.Compiler.Analysis.IPA.PA.ObjectPathSelector objectPathSelector;

    public java.util.Map V1H1correspondence;
}
