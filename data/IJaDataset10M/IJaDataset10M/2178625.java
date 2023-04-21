package gralej.blocks;

import gralej.Config;
import gralej.om.AbstractVisitor;
import gralej.om.IAny;
import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.IRelation;
import gralej.om.ITable;
import gralej.om.ITag;
import gralej.om.ITree;
import gralej.om.IType;
import gralej.om.ITypedFeatureStructure;
import gralej.om.IVisitable;
import gralej.om.IneqsAndResidue;
import gralej.om.lrs.IExCont;
import gralej.om.lrs.IFunctor;
import gralej.om.lrs.IInCont;
import gralej.om.lrs.ILRSExpr;
import gralej.om.lrs.IMetaVar;
import gralej.om.lrs.INamedTerm;
import gralej.om.lrs.ISqCont;
import gralej.om.lrs.ITerm;
import gralej.om.lrs.IVar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public final class BlockCreator extends AbstractVisitor {

    private Block _result;

    private BlockPanel _panel;

    private LabelFactory _labfac;

    private Map<IEntity, ContentCreator> _contentCreatorCache;

    private boolean _instantiateLazy = true;

    private static Set<String> _infixOps;

    public BlockCreator(BlockPanel panel) {
        _panel = panel;
        _labfac = _panel.getStyle().getLabelFactory();
    }

    public Block createBlock(IVisitable vob) {
        return createBlock(vob, true);
    }

    public Block createBlock(IVisitable vob, boolean instantiateLazy) {
        _instantiateLazy = instantiateLazy;
        return createBlock(vob, IneqsAndResidue.EMPTY);
    }

    public Block createBlock(IVisitable vob, IneqsAndResidue iqres) {
        vob.accept(this);
        if (!iqres.residue().isEmpty() || !iqres.ineqs().isEmpty()) {
            VerticalListBlock vl = new VerticalListBlock(_panel);
            vl.addChild(_result);
            vl.addChild(_labfac.createFunctorLabel(" ", _panel));
            _instantiateLazy = false;
            processRelations(iqres.ineqs(), "~~", vl);
            processRelations(iqres.residue(), "--", vl);
            vl.sealChildren();
            _result = vl;
        }
        return _result;
    }

    private void processRelations(List<IRelation> rels, String switchLabel, VerticalListBlock vl) {
        if (rels.isEmpty()) return;
        ContentLabel residueSwitchLabel = _labfac.createFunctorLabel(switchLabel, _panel);
        vl.addChild(residueSwitchLabel);
        VerticalListBlock relList = new VerticalListBlock(_panel);
        residueSwitchLabel.setContent(relList);
        for (IRelation rel : rels) {
            visit(rel);
            relList.addChild(_result);
        }
        relList.sealChildren();
        vl.addChild(relList);
    }

    @Override
    public void visit(ITable table) {
        processFeatVals(table.rows());
        if (table.heading() != null) _result = new TableBlock(_panel, _labfac.createHeadingLabel(table.heading(), _panel), (AVPairListBlock) _result);
        _result.setModel(table);
    }

    @Override
    public void visit(IList ls) {
        List<Block> ll = new LinkedList<Block>();
        for (IEntity entity : ls.elements()) {
            entity.accept(this);
            ll.add(_result);
        }
        Block tail = null;
        if (ls.tail() != null) {
            ls.tail().accept(this);
            tail = _result;
        }
        _result = new ListBlock(_panel, new ListContentBlock(_panel, ll, tail));
        _result.setModel(ls);
    }

    @Override
    public void visit(ITag tag) {
        if (tag.target() != null) {
            ContentLabel lab = _labfac.createTagLabel(tag.number() + "", _panel);
            lab.setModel(tag);
            _result = new ReentrancyBlock(_panel, lab, tag.number(), getContentCreator(tag.target()));
        } else {
            Label lab = _labfac.createUnboundVarLabel(tag.number() + "", _panel);
            lab.setModel(tag);
            final Label unboundVarLabel = lab;
            _result = new ContainerBlock() {

                {
                    setPanel(_panel);
                    setLayout(LayoutFactory.getInstance().getReentrancyLayout());
                    addChild(unboundVarLabel);
                }
            };
        }
        _result.setModel(tag);
    }

    @Override
    public void visit(IAny any) {
        _result = _labfac.createAnyLabel(any.value(), _panel);
        _result.setModel(any);
    }

    @Override
    public void visit(ILRSExpr lrs) {
        LRSTreeBlock lrsTree = new LRSTreeBlock(_panel, createLRSTree(lrs.subTerms()));
        lrsTree.setModel(lrs);
        _result = new LRSBlock(_panel, lrsTree, lrs);
        _result.setModel(lrs);
    }

    private LRSNodeBlock createLRSTree(List<ITerm> terms) {
        List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        List<Block> labels = new LinkedList<Block>();
        return createLRSNode(terms, labels, childNodes);
    }

    private LRSNodeBlock createLRSNode(List<Block> labels, List<NodeBlock> childNodes) {
        return new LRSNodeBlock(_panel, labels, childNodes);
    }

    private LRSNodeBlock createLRSNode(ITerm term) {
        List<Block> labels = new LinkedList<Block>();
        List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        processLRSTerm(term, labels, childNodes);
        return createLRSNode(labels, childNodes);
    }

    private LRSNodeBlock createLRSNode(List<ITerm> terms, List<Block> labels, List<NodeBlock> childNodes) {
        for (ITerm term : terms) {
            if (!labels.isEmpty()) labels.add(_labfac.createLRSLabel(",", _panel));
            processLRSTerm(term, labels, childNodes);
        }
        return createLRSNode(labels, childNodes);
    }

    private void processLRSTerm(ITerm term, List<Block> labels, List<NodeBlock> childNodes) {
        if (term instanceof INamedTerm) {
            if (term.isLeafTerm()) labels.add(_labfac.createLRSLabel(term.uiName(), _panel)); else {
                LRSContentLabel lab = _labfac.createLRSContentLabel(term.uiName(), _panel);
                labels.add(lab);
                for (ITerm subTerm : term.subTerms()) {
                    LRSNodeBlock node = createLRSNode(subTerm);
                    node.setParentLabel(lab);
                    childNodes.add(node);
                    lab.addChildNode(node);
                }
            }
        }
        if (term instanceof IFunctor) {
            labels.add(_labfac.createLRSLabel("(", _panel));
            boolean firstDone = false;
            for (ITerm arg : ((IFunctor) term).args()) {
                if (firstDone) labels.add(_labfac.createLRSLabel(",", _panel)); else firstDone = true;
                processLRSTerm(arg, labels, childNodes);
            }
            labels.add(_labfac.createLRSLabel(")", _panel));
        } else if (term instanceof IMetaVar) {
        } else if (term instanceof IVar) {
        } else if (term instanceof IInCont) {
            labels.add(_labfac.createLRSLabel("{", _panel));
            for (ITerm subTerm : term.subTerms()) processLRSTerm(subTerm, labels, childNodes);
            labels.add(_labfac.createLRSLabel("}", _panel));
        } else if (term instanceof IExCont) {
            labels.add(_labfac.createLRSLabel("^", _panel));
            for (ITerm subTerm : term.subTerms()) processLRSTerm(subTerm, labels, childNodes);
        } else if (term instanceof ISqCont) {
            labels.add(_labfac.createLRSLabel("[", _panel));
            for (ITerm subTerm : term.subTerms()) processLRSTerm(subTerm, labels, childNodes);
            labels.add(_labfac.createLRSLabel("]", _panel));
        } else {
            throw new AssertionError("unknown lrs term: " + term + " [" + term.getClass() + "]");
        }
        if (term.hasConstraints()) {
            labels.add(_labfac.createLRSLabel("/", _panel));
            labels.add(_labfac.createLRSLabel("(", _panel));
            processConstraints(labels, term.positiveConstraints());
            if (term.hasNegativeConstraints()) {
                labels.add(_labfac.createLRSLabel(")~(", _panel));
                processConstraints(labels, term.negativeConstraints());
            }
            labels.add(_labfac.createLRSLabel(")", _panel));
        }
    }

    private void processConstraints(Collection<Block> labels, Iterable<ITag> tags) {
        boolean comma = false;
        for (ITag tag : tags) {
            if (comma) labels.add(_labfac.createLRSLabel(",", _panel)); else comma = true;
            if (tag.number() != -1) {
                tag.accept(this);
                labels.add(_result);
            } else {
                labels.add(_labfac.createLRSLabel("[*]", _panel));
            }
        }
    }

    private void processFeatVals(Iterable<IFeatureValuePair> featVals) {
        List<AVPairBlock> ll = new LinkedList<AVPairBlock>();
        for (IFeatureValuePair featVal : featVals) {
            featVal.accept(this);
            ll.add((AVPairBlock) _result);
        }
        _result = new AVPairListBlock(_panel, ll);
    }

    @Override
    public void visit(IFeatureValuePair featVal) {
        ContentLabel alab = _labfac.createAttributeLabel(featVal.feature().toUpperCase(), _panel);
        alab.setModel(featVal);
        featVal.value().accept(this);
        _result = new AVPairBlock(_panel, alab, _result, featVal.isHidden());
        if (featVal.isHidden()) alab.flip();
        _result.setModel(featVal);
    }

    @Override
    public void visit(ITypedFeatureStructure tfs) {
        if (tfs.isSpecies()) {
            _result = _labfac.createSpeciesLabel(tfs.typeName(), _panel);
            if (tfs.isDifferent()) tfs.type().setDifferent(true);
            _result.setModel(tfs.type());
            return;
        }
        processFeatVals(tfs.featureValuePairs());
        AVPairListBlock avPairs = (AVPairListBlock) _result;
        ContentLabel sortLabel = null;
        if (tfs.type() != null) {
            sortLabel = _labfac.createSortLabel(tfs.typeName(), _panel);
            sortLabel.setModel(tfs);
        }
        _result = new AVMBlock(_panel, sortLabel, avPairs);
        _result.setModel(tfs);
    }

    @Override
    public void visit(IType type) {
        _result = _labfac.createSortLabel(type.typeName(), _panel);
        _result.setModel(type);
    }

    @Override
    public void visit(ITree tree) {
        _result = new TreeBlock(_panel, createTree(tree));
        _result.setModel(tree);
    }

    private NodeBlock createTree(ITree u) {
        if (u.label() == null && u.content() == null) throw new IllegalStateException("both 'label' and 'content' are null");
        Label label = null;
        java.util.List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        if (u.isLeaf()) {
            if (u.label() != null) label = _labfac.createLeafNodeLabel(u.label(), _panel);
        } else {
            for (ITree v : u.children()) childNodes.add(createTree(v));
            if (u.label() != null) label = _labfac.createInternalNodeLabel(u.label(), _panel);
        }
        if (label != null) label.setModel(u);
        Block content = null;
        if (u.content() != null) {
            u.content().accept(this);
            content = _result;
        }
        NodeBlock nodeBlock = new AVMNodeBlock(_panel, label, content, childNodes);
        nodeBlock.setModel(u);
        for (NodeBlock childNode : childNodes) childNode.setParentNode(nodeBlock);
        return nodeBlock;
    }

    @Override
    public void visit(IRelation rel) {
        if (rel.arity() == 2 && isInfixOp(rel)) {
            rel.arg(0).accept(this);
            Block op1 = _result;
            rel.arg(1).accept(this);
            Block op2 = _result;
            _result = new InfixOperatorBlock(_panel, rel, op1, op2);
        } else {
            Block b = new LazyRelationBlock(_panel, rel, this);
            if (_instantiateLazy) {
                ContentLabel lab = ((ContentLabel) b.getChildren().get(0));
                lab.flip();
                _instantiateLazy = true;
            }
            _result = b;
        }
        _result.setModel(rel);
    }

    private ContentCreator getContentCreator(final IEntity entity) {
        if (_contentCreatorCache == null) _contentCreatorCache = new HashMap<IEntity, ContentCreator>();
        ContentCreator cc = _contentCreatorCache.get(entity);
        if (cc == null) {
            cc = new ContentCreator() {

                BlockCreator bcv;

                public Block createContent() {
                    if (bcv == null) bcv = new BlockCreator(_panel);
                    return bcv.createBlock(entity);
                }
            };
            _contentCreatorCache.put(entity, cc);
        }
        return cc;
    }

    private static boolean isInfixOp(IRelation rel) {
        if (_infixOps == null) {
            _infixOps = new TreeSet<String>();
            for (String s : Config.s("block.infixOperators").trim().split("\\s+")) _infixOps.add(s);
        }
        return _infixOps.contains(rel.name());
    }
}
