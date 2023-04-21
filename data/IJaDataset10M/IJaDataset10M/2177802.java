package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DISSOLUTION;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGAcrossEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGAcrossExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGFieldDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGParameterDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGReturnDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGFieldInNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGFieldOutNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterInNode;

public class InterProceduralPDG extends PDG {

    public InterProceduralPDG(final Collection<IntraProceduralPDG> pdgs, final boolean buildDataDependency, final boolean buildControlDependency, final boolean buildExecutionDependency) {
        super(new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory());
        this.methods = new HashSet<CallableUnitInfo>();
        this.unitToPDGMap = new HashMap<CallableUnitInfo, IntraProceduralPDG>();
        this.acrossEdges = new HashSet<PDGAcrossEdge>();
        this.buildDataDependency = buildDataDependency;
        this.buildControlDependency = buildControlDependency;
        this.buildExecutionDependency = buildExecutionDependency;
        this.countObjectStateChange = true;
        this.optimize = true;
        this.dataDependencyDistance = Integer.MAX_VALUE;
        this.controlDependencyDistance = Integer.MAX_VALUE;
        this.executionDependencyDistance = Integer.MAX_VALUE;
        for (final IntraProceduralPDG pdg : pdgs) {
            final CallableUnitInfo unit = pdg.getMethodInfo();
            this.methods.add(unit);
            this.unitToPDGMap.put(unit, pdg);
            this.nodes.addAll(pdg.getAllNodes());
            for (final PDGNode<?> pdgNode : pdg.getAllNodes()) {
                this.pdgNodeFactory.addNode(pdgNode);
            }
        }
        this.buildInterPDG();
    }

    public InterProceduralPDG(final Collection<CallableUnitInfo> methods, final IPDGNodeFactory pdgNodeFactory, final ICFGNodeFactory cfgNodeFactory, final boolean buildDataDependency, final boolean buildControlDependency, final boolean buildExecutionDependency, final boolean countObjectStateChange, final boolean optimize, final int dataDependencyDistance, final int controlDependencyDistance, final int executionDependencyDistance) {
        super(pdgNodeFactory, cfgNodeFactory);
        this.methods = Collections.unmodifiableCollection(methods);
        this.unitToPDGMap = new HashMap<CallableUnitInfo, IntraProceduralPDG>();
        this.acrossEdges = new HashSet<PDGAcrossEdge>();
        this.buildDataDependency = buildDataDependency;
        this.buildControlDependency = buildControlDependency;
        this.buildExecutionDependency = buildExecutionDependency;
        this.countObjectStateChange = countObjectStateChange;
        this.optimize = optimize;
        this.dataDependencyDistance = dataDependencyDistance;
        this.controlDependencyDistance = controlDependencyDistance;
        this.executionDependencyDistance = executionDependencyDistance;
        this.buildPDG();
    }

    public InterProceduralPDG(final Collection<CallableUnitInfo> methods, final boolean buildDataDependency, final boolean buildControlDependency, final boolean buildExecutionDependency, final boolean countObjectStateChange, final boolean optimize, final int dataDependencyDistance, final int controlDependencyDistance, final int executionDependencyDistance) {
        this(methods, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory(), buildDataDependency, buildControlDependency, buildExecutionDependency, countObjectStateChange, optimize, dataDependencyDistance, controlDependencyDistance, executionDependencyDistance);
    }

    public InterProceduralPDG(final Collection<CallableUnitInfo> methods, final boolean buildDataDependency, final boolean buildControlDependency, final boolean buildExecutionDependency, final boolean countObjectStateChange, final boolean optimize) {
        this(methods, buildDataDependency, buildControlDependency, buildExecutionDependency, countObjectStateChange, optimize, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    protected void buildPDG() {
        for (final CallableUnitInfo method : this.methods) {
            final IntraProceduralPDG pdg = new IntraProceduralPDG(method, this.pdgNodeFactory, this.cfgNodeFactory, this.buildDataDependency, this.buildControlDependency, this.buildExecutionDependency, this.countObjectStateChange, this.optimize, DISSOLUTION.TRUE, this.dataDependencyDistance, this.controlDependencyDistance, this.executionDependencyDistance);
            this.unitToPDGMap.put(method, pdg);
            this.nodes.addAll(pdg.getAllNodes());
        }
        this.buildInterPDG();
    }

    private void buildInterPDG() {
        for (final IntraProceduralPDG unitPDG : this.unitToPDGMap.values()) {
            for (final PDGNode<?> node : unitPDG.getAllNodes()) {
                this.buildPamareterDataDependency(node);
                this.buildFieldDataDependency(unitPDG, node);
                this.buildReturnDataDependency(node);
                this.buildExecutionDependency(node);
            }
        }
    }

    /**
	 * ��ŗ^����ꂽ�m�[�h�ŌĂяo����Ă��郁�\�b�h�Ƃ̈������f�[�^�ˑ��֌W���\�z����
	 * 
	 * @param node
	 */
    private void buildPamareterDataDependency(final PDGNode<?> node) {
        final ExecutableElementInfo core = node.getCore();
        for (final CallInfo<? extends CallableUnitInfo> call : core.getCalls()) {
            final List<ExpressionInfo> arguments = call.getArguments();
            final Map<Integer, PDGNode<?>> definitionNodes = new HashMap<Integer, PDGNode<?>>();
            final Map<Integer, VariableInfo<?>> definitionVariables = new HashMap<Integer, VariableInfo<?>>();
            for (int index = 0; index < arguments.size(); index++) {
                if (arguments.get(index) instanceof VariableUsageInfo<?>) {
                    final VariableInfo<?> usedVariable = ((VariableUsageInfo<?>) arguments.get(index)).getUsedVariable();
                    for (final PDGEdge edge : node.getBackwardEdges()) {
                        if (edge instanceof PDGDataDependenceEdge) {
                            final PDGDataDependenceEdge dataEdge = (PDGDataDependenceEdge) edge;
                            final VariableInfo<?> variable = dataEdge.getVariable();
                            if (usedVariable.equals(variable)) {
                                final PDGNode<?> definitionNode = edge.getFromNode();
                                definitionNodes.put(index, definitionNode);
                                definitionVariables.put(index, usedVariable);
                                break;
                            }
                        }
                    }
                }
            }
            final Set<CallableUnitInfo> callees = this.getCallees(call);
            for (final CallableUnitInfo callee : callees) {
                final IntraProceduralPDG calleePDG = this.unitToPDGMap.get(callee);
                if (null == calleePDG) {
                    break;
                }
                final List<ParameterInfo> parameters = callee.getParameters();
                for (final Entry<Integer, PDGNode<?>> entry : definitionNodes.entrySet()) {
                    final int index = entry.getKey();
                    final PDGNode<?> definitionNode = entry.getValue();
                    final VariableInfo<?> variable = definitionVariables.get(index);
                    final ParameterInfo parameter = parameters.get(index);
                    if (variable.equals(parameter)) {
                        continue;
                    }
                    final PDGParameterInNode parameterNode = calleePDG.getParameterNode(parameter);
                    for (final PDGEdge edge : parameterNode.getForwardEdges()) {
                        final PDGNode<?> referenceNode = edge.getToNode();
                        final PDGParameterDataDependenceEdge acrossEdge = new PDGParameterDataDependenceEdge(definitionNode, referenceNode, variable, call);
                        this.acrossEdges.add(acrossEdge);
                        definitionNode.addForwardEdge(acrossEdge);
                        referenceNode.addBackwardEdge(acrossEdge);
                    }
                }
            }
        }
    }

    /**
	 * ��ŗ^����ꂽ�m�[�h�ŌĂяo����Ă��郁�\�b�h�Ƃ̃t�B�[���h������f�[�^�ˑ��֌W���\�z����
	 * 
	 * @param unitPDG
	 * @param node
	 */
    private void buildFieldDataDependency(final IntraProceduralPDG unitPDG, final PDGNode<?> node) {
        final Map<FieldInfo, PDGFieldOutNode> fieldOutNodes = Collections.unmodifiableMap(unitPDG.fieldOutNodes);
        final ExecutableElementInfo core = node.getCore();
        for (final CallInfo<? extends CallableUnitInfo> call : core.getCalls()) {
            final Set<CallableUnitInfo> callees = this.getCallees(call);
            for (final CallableUnitInfo callee : callees) {
                final IntraProceduralPDG calleePDG = this.unitToPDGMap.get(callee);
                if (null == calleePDG) {
                    break;
                }
                final Map<FieldInfo, PDGFieldInNode> fieldInNodes = Collections.unmodifiableMap(calleePDG.fieldInNodes);
                for (final Entry<FieldInfo, PDGFieldOutNode> fieldOut : fieldOutNodes.entrySet()) {
                    final FieldInfo field = fieldOut.getKey();
                    final PDGFieldOutNode fieldOutNode = fieldOut.getValue();
                    final PDGFieldInNode fieldInNode = fieldInNodes.get(field);
                    if (null != fieldInNode) {
                        final Set<PDGNode<?>> fromNodes = new HashSet<PDGNode<?>>();
                        for (final PDGEdge edge : fieldOutNode.getBackwardEdges()) {
                            fromNodes.add(edge.getFromNode());
                        }
                        final Set<PDGNode<?>> toNodes = new HashSet<PDGNode<?>>();
                        for (final PDGEdge edge : fieldInNode.getForwardEdges()) {
                            toNodes.add(edge.getToNode());
                        }
                        for (final PDGNode<?> fromNode : fromNodes) {
                            for (final PDGNode<?> toNode : toNodes) {
                                final PDGFieldDataDependenceEdge acrossEdge = new PDGFieldDataDependenceEdge(fromNode, toNode, field, call);
                                this.acrossEdges.add(acrossEdge);
                                fromNode.addForwardEdge(acrossEdge);
                                toNode.addBackwardEdge(acrossEdge);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * ��ŗ^����ꂽ�m�[�h�ŌĂяo����Ă��郁�\�b�h�Ƃ̕Ԃ�l������f�[�^�ˑ��֌W���\�z����
	 * 
	 * @param node
	 */
    private void buildReturnDataDependency(final PDGNode<?> node) {
        final ExecutableElementInfo core = node.getCore();
        for (final CallInfo<? extends CallableUnitInfo> call : core.getCalls()) {
            final Set<CallableUnitInfo> callees = this.getCallees(call);
            for (final CallableUnitInfo callee : callees) {
                if (!(callee instanceof MethodInfo)) {
                    break;
                }
                final MethodInfo calleeMethod = (MethodInfo) callee;
                if (calleeMethod.getReturnType() instanceof VoidTypeInfo) {
                    break;
                }
                final IntraProceduralPDG unitPDG = this.unitToPDGMap.get(calleeMethod);
                if (null == unitPDG) {
                    break;
                }
                final Map<PDGNode<?>, VariableInfo<?>> definitionNodes = new HashMap<PDGNode<?>, VariableInfo<?>>();
                for (final PDGNode<?> exitNode : unitPDG.getExitNodes()) {
                    if (exitNode.getCore() instanceof ReturnStatementInfo) {
                        for (final PDGEdge edge : exitNode.getBackwardEdges()) {
                            if (edge instanceof PDGDataDependenceEdge) {
                                definitionNodes.put(edge.getFromNode(), ((PDGDataDependenceEdge) edge).getVariable());
                            }
                        }
                    }
                }
                final Set<PDGNode<?>> referenceNodes = new HashSet<PDGNode<?>>();
                for (final PDGEdge edge : node.getForwardEdges()) {
                    if (edge instanceof PDGDataDependenceEdge) {
                        referenceNodes.add(edge.getToNode());
                    }
                }
                for (final Entry<PDGNode<?>, VariableInfo<?>> entry : definitionNodes.entrySet()) {
                    final PDGNode<?> definitionNode = entry.getKey();
                    final VariableInfo<?> variable = entry.getValue();
                    for (final PDGNode<?> referenceNode : referenceNodes) {
                        final PDGReturnDataDependenceEdge acrossEdge = new PDGReturnDataDependenceEdge(definitionNode, referenceNode, variable, call);
                        definitionNode.addForwardEdge(acrossEdge);
                        referenceNode.addBackwardEdge(acrossEdge);
                        this.acrossEdges.add(acrossEdge);
                    }
                }
            }
        }
    }

    /**
	 * ��ŗ^����ꂽ�m�[�h�ŌĂяo����Ă��郁�\�b�h�����Ƃ̎��s�ˑ��֌W���\�z����
	 * 
	 * @param node
	 */
    private void buildExecutionDependency(final PDGNode<?> node) {
        final ExecutableElementInfo core = node.getCore();
        for (final CallInfo<? extends CallableUnitInfo> call : core.getCalls()) {
            final Set<CallableUnitInfo> callees = this.getCallees(call);
            for (final CallableUnitInfo callee : callees) {
                final IntraProceduralPDG unitPDG = this.unitToPDGMap.get(callee);
                if (null == unitPDG) {
                    break;
                }
                {
                    final Set<PDGNode<?>> fromNodes = new HashSet<PDGNode<?>>();
                    for (final PDGEdge edge : node.getBackwardEdges()) {
                        if (edge instanceof PDGExecutionDependenceEdge) {
                            fromNodes.add(edge.getFromNode());
                        }
                    }
                    final Set<PDGNode<?>> toNodes = new HashSet<PDGNode<?>>();
                    for (final PDGEdge edge : unitPDG.getMethodEnterNode().getForwardEdges()) {
                        if (edge instanceof PDGExecutionDependenceEdge) {
                            toNodes.add(edge.getToNode());
                        }
                    }
                    for (final PDGNode<?> fromNode : fromNodes) {
                        for (final PDGNode<?> toNode : toNodes) {
                            final PDGAcrossExecutionDependenceEdge acrossEdge = new PDGAcrossExecutionDependenceEdge(fromNode, toNode, call);
                            fromNode.addForwardEdge(acrossEdge);
                            toNode.addBackwardEdge(acrossEdge);
                            this.acrossEdges.add(acrossEdge);
                        }
                    }
                }
                {
                    final Set<PDGNode<?>> fromNodes = new HashSet<PDGNode<?>>();
                    for (final PDGNode<?> exitNode : unitPDG.getExitNodes()) {
                        if (exitNode.getCore() instanceof ReturnStatementInfo) {
                            for (final PDGEdge edge : exitNode.getBackwardEdges()) {
                                if (edge instanceof PDGExecutionDependenceEdge) {
                                    fromNodes.add(edge.getFromNode());
                                }
                            }
                        } else {
                            fromNodes.add(exitNode);
                        }
                    }
                    final Set<PDGNode<?>> toNodes = new HashSet<PDGNode<?>>();
                    for (final PDGEdge edge : node.getForwardEdges()) {
                        if (edge instanceof PDGExecutionDependenceEdge) {
                            toNodes.add(edge.getToNode());
                        }
                    }
                    for (final PDGNode<?> fromNode : fromNodes) {
                        for (final PDGNode<?> toNode : toNodes) {
                            final PDGAcrossExecutionDependenceEdge acrossEdge = new PDGAcrossExecutionDependenceEdge(fromNode, toNode, call);
                            fromNode.addForwardEdge(acrossEdge);
                            toNode.addBackwardEdge(acrossEdge);
                            this.acrossEdges.add(acrossEdge);
                        }
                    }
                }
            }
        }
    }

    public Collection<IntraProceduralPDG> getEntries() {
        return this.unitToPDGMap.values();
    }

    public Set<PDGAcrossEdge> getAcrossEdges() {
        return this.acrossEdges;
    }

    /**
	 * ��ŗ^����ꂽ�Ăяo���ɂ����ČĂяo�����\���̂���CallableUnitInfo��Ԃ�
	 * 
	 * @param call
	 * @return
	 */
    private Set<CallableUnitInfo> getCallees(final CallInfo<? extends CallableUnitInfo> call) {
        final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
        final CallableUnitInfo callee = call.getCallee();
        callees.add(callee);
        if (callee instanceof MethodInfo) {
            callees.addAll(((MethodInfo) callee).getOverriders());
        }
        return callees;
    }

    private final Collection<CallableUnitInfo> methods;

    private final Map<CallableUnitInfo, IntraProceduralPDG> unitToPDGMap;

    private final Set<PDGAcrossEdge> acrossEdges;

    private final boolean buildDataDependency;

    private final boolean buildControlDependency;

    private final boolean buildExecutionDependency;

    private final boolean countObjectStateChange;

    private final boolean optimize;

    private final int dataDependencyDistance;

    private final int controlDependencyDistance;

    private final int executionDependencyDistance;
}
