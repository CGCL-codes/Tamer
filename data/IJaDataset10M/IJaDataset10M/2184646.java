package alice.c4jason;

import java.util.*;
import java.io.*;
import java.util.logging.*;
import alice.cartago.*;
import alice.cartago.mansyntax.*;

public class ManualBridge {

    private Manual man;

    private String artifactTempl;

    private static final String[] artifactLibTheory = { new String("+!build_artifact(ToolType,ToolName,ToolParams,ToolId) : true				\n" + "<- cartago.makeArtifact(ToolName,ToolType,ToolParams,ToolId);				\n" + "   +known_artifact(ToolName,ToolId).										\n"), new String("+?known_artifact(ToolName,ToolId) : true									\n" + "<- cartago.lookupArtifact(ToolName,ToolId);									\n" + "   +known_artifact(ToolName,ToolId).										\n"), new String("+!locate_artifact(ToolType,ToolName,ToolId) : true 							\n" + "<- cartago.lookupArtifact(ToolName,ToolId);									\n" + "   +known_artifact(ToolName,ToolId).								\n"), new String("-!locate_artifact(ToolType,ToolName,ToolId) : true 							\n" + "<- cartago.makeArtifact(ToolName,ToolType,ToolId);							\n" + "   +known_artifact(ToolName,ToolId).										\n"), new String("+!locate_artifact(ToolType,ToolName,Params,ToolId) : true 					\n" + "<- cartago.lookupArtifact(ToolName,ToolId);									\n" + "   +known_artifact(ToolName,ToolId).										\n"), new String("-!locate_artifact(ToolType,ToolName,Params,ToolId) : true					\n" + "<- !locate_artifact_tryToMake(ToolType,ToolName,Params,ToolId).				\n"), new String("+!locate_artifact_tryToMake(ToolType,ToolName,Params,ToolId) : true			\n" + "<- cartago.makeArtifact(ToolName,ToolType,Params,ToolId);					\n" + "   +known_artifact(ToolName,ToolId).										\n"), new String("-!locate_artifact_tryToMake(ToolType,ToolName,Params,ToolId) : true			\n" + "<- !locate_artifact(ToolType,ToolName,Params,ToolId).						\n") };

    public ManualBridge(Manual man, String artifactTempl) {
        this.man = man;
        this.artifactTempl = artifactTempl;
    }

    public String[] getAsPlans() {
        List<UsageProtocol> protocols = man.getUsageProtocols();
        String manualName = man.getName();
        String[] man = new String[protocols.size()];
        int index = 0;
        for (UsageProtocol p : protocols) {
            StringBuffer plan = new StringBuffer("@" + p.getSignature() + "[ manual(" + manualName + ") ]");
            plan.append("\n+!" + p.getFunction());
            if (p.getPrecondition() != null) {
                plan.append(" : " + p.getPrecondition());
            }
            String SEP = null;
            boolean first = true;
            boolean bodyPresent = false;
            UsageProtBody protocol = p.getBody();
            if (protocol != null) {
                bodyPresent = true;
                plan.append("\n <-");
                while (protocol != null) {
                    if (first) {
                        SEP = " ";
                        first = false;
                    } else {
                        SEP = ";\n    ";
                    }
                    alice.cartago.mansyntax.Term term = protocol.getBodyTerm();
                    UsageProtBody.BodyType btype = protocol.getBodyType();
                    if (btype == UsageProtBody.BodyType.action) {
                        if (term.isStructure()) {
                            Structure st = (Structure) term;
                            if (st.getFunctor().equals("use")) {
                                Structure st1 = new Structure("cartago.use");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("sense")) {
                                Structure st1 = new Structure("cartago.sense");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("observeProperty")) {
                                Structure st1 = new Structure("cartago.observeProperty");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("build")) {
                                Structure st1 = new Structure("build_artifact");
                                List<Term> list = new ArrayList<Term>();
                                if (st.getArity() == 3) {
                                    list.add(new StringTerm(artifactTempl));
                                }
                                for (Term t : st.getTerms()) {
                                    list.add(t);
                                }
                                st1.setTerms(list);
                                plan.append(SEP + "!" + st1);
                            } else if (st.getFunctor().equals("locate")) {
                                Structure st1 = new Structure("locate_artifact");
                                List<Term> list = new ArrayList<Term>();
                                if (st.getArity() == 2) {
                                    list.add(new StringTerm(artifactTempl));
                                }
                                for (Term t : st.getTerms()) {
                                    list.add(t);
                                }
                                st1.setTerms(list);
                                plan.append(SEP + "!" + st1);
                            } else if (st.getFunctor().equals("tupleToArg")) {
                                Structure st1 = new Structure("cartago.tupleToTerm");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("tupleTemplateToArg")) {
                                Structure st1 = new Structure("cartago.tupleTemplateToTerm");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("argToTuple")) {
                                Structure st1 = new Structure("cartago.termToTuple");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("argToTupleTemplate")) {
                                Structure st1 = new Structure("cartago.termToTupleTemplate");
                                st1.setTerms(st.getTerms());
                                plan.append(SEP + st1);
                            } else if (st.getFunctor().equals("matchArg")) {
                                RelExpr op = new RelExpr(st.getTerm(0), RelExpr.RelationalOp.unify, st.getTerm(1));
                                plan.append(SEP + op);
                            } else {
                                plan.append(SEP + term);
                            }
                        } else {
                            plan.append(SEP + term);
                        }
                    } else if (btype == UsageProtBody.BodyType.achieve) {
                        plan.append(SEP + "!" + term);
                    } else if (btype == UsageProtBody.BodyType.achieveNF) {
                        plan.append(SEP + term);
                    } else if (btype == UsageProtBody.BodyType.achieveNF) {
                        plan.append(SEP + term);
                    } else if (btype == UsageProtBody.BodyType.addBel) {
                        plan.append(SEP + "+" + term);
                    } else if (btype == UsageProtBody.BodyType.delBel) {
                        plan.append(SEP + "-" + term);
                    } else if (btype == UsageProtBody.BodyType.delAddBel) {
                        plan.append(SEP + "-+" + term);
                    } else if (btype == UsageProtBody.BodyType.internalAction) {
                        plan.append(SEP + "+" + term);
                    } else if (btype == UsageProtBody.BodyType.test) {
                        plan.append(SEP + term);
                    } else {
                        plan.append(SEP + term);
                    }
                    protocol = protocol.getBodyNext();
                }
            }
            plan.append(".\n\n");
            man[index++] = plan.toString();
        }
        return man;
    }

    public String[] getCommonPlans() {
        return artifactLibTheory;
    }
}
