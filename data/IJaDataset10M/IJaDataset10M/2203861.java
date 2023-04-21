package org.coode.oae.ui;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import org.coode.oae.utils.ParserFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import uk.ac.manchester.mae.evaluation.BindingModel;
import uk.ac.manchester.mae.evaluation.FormulaModel;
import uk.ac.manchester.mae.evaluation.PropertyChainModel;
import uk.ac.manchester.mae.evaluation.StorageModel;
import uk.ac.manchester.mae.parser.ArithmeticsParser;
import uk.ac.manchester.mae.parser.MAEBinding;
import uk.ac.manchester.mae.parser.MAEStart;
import uk.ac.manchester.mae.parser.MAEpropertyChainCell;
import uk.ac.manchester.mae.parser.MAEpropertyChainExpression;
import uk.ac.manchester.mae.parser.ParseException;
import uk.ac.manchester.mae.visitor.BindingPropertyChainExtractor;
import uk.ac.manchester.mae.visitor.protege.ProtegeFormulaModelExtractor;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Apr 7, 2008
 */
public class MAENodeAdapter {

    public static MutableTreeNode toTreeNode(MAEBinding binding, OWLModelManager manager) {
        DefaultMutableTreeNode toReturn = new DefaultMutableTreeNode(binding.getIdentifier());
        BindingPropertyChainExtractor propertyExtractor = new BindingPropertyChainExtractor(binding);
        MAEpropertyChainExpression propertyChain = (MAEpropertyChainExpression) binding.jjtAccept(propertyExtractor, null);
        if (propertyChain != null) {
            DefaultMutableTreeNode propertyChainNode = toTreeNode(propertyChain, manager);
            toReturn.insert(propertyChainNode, 0);
        }
        return toReturn;
    }

    public static DefaultMutableTreeNode toTreeNode(MAEpropertyChainExpression propertyChain, OWLModelManager modelManager) {
        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();
        DefaultMutableTreeNode toReturn;
        for (MAEpropertyChainCell cell : propertyChain.getCells()) {
            list.add(new DefaultMutableTreeNode(cell));
        }
        for (int i = list.size() - 1; i > 0; i--) {
            list.get(i - 1).insert(list.get(i), 0);
        }
        toReturn = list.get(0);
        return toReturn;
    }

    public static BindingModel toBindingModel(DefaultMutableTreeNode node) {
        String identifier = node.getUserObject().toString();
        BindingModel toReturn = new BindingModel(identifier, null);
        if (!node.isLeaf()) {
            DefaultMutableTreeNode propertyChainNode = (DefaultMutableTreeNode) node.getChildAt(0);
            PropertyChainModel propertyChainModel = toPropertyChainModel(propertyChainNode);
            toReturn.setPropertyChainModel(propertyChainModel);
        }
        return toReturn;
    }

    private static PropertyChainModel toPropertyChainModel(DefaultMutableTreeNode propertyChainNode) {
        PropertyChainModel toReturn = (PropertyChainModel) propertyChainNode.getUserObject();
        if (!propertyChainNode.isLeaf()) {
            toReturn.setChild(toPropertyChainModel((DefaultMutableTreeNode) propertyChainNode.getChildAt(0)));
        }
        return toReturn;
    }

    public static MAEStart toFormula(FormulaModel formulaModel, OWLModelManager modelManager) throws ParseException {
        ParserFactory.initParser(formulaModel.render(modelManager), modelManager);
        return (MAEStart) ArithmeticsParser.Start();
    }

    public static FormulaModel toFormulaModel(MAEStart formula, URI formulaURI, OWLEditorKit owlEditorKit) {
        ProtegeFormulaModelExtractor fme = new ProtegeFormulaModelExtractor(owlEditorKit.getModelManager());
        formula.jjtAccept(fme, null);
        FormulaModel extractedFormulaModel = fme.getExtractedFormulaModel();
        if (extractedFormulaModel != null) {
            extractedFormulaModel.setFormulaURI(formulaURI);
        }
        return extractedFormulaModel;
    }

    public static StorageModel toStorageModel(DefaultMutableTreeNode storageSubTreeRoot) {
        return new StorageModel(toPropertyChainModel(storageSubTreeRoot));
    }

    public static MutableTreeNode toTreeNode(StorageModel storageModel, OWLModelManager modelManager) {
        DefaultMutableTreeNode toReturn = new DefaultMutableTreeNode("Store to");
        PropertyChainModel propertyChainModel = storageModel.getPropertyChainModel();
        if (propertyChainModel != null) {
            toReturn.insert(toTreeNode(propertyChainModel, modelManager), 0);
        }
        return toReturn;
    }

    public static MutableTreeNode toTreeNode(PropertyChainModel propertyChainModel, OWLModelManager modelManager) {
        DefaultMutableTreeNode toReturn = new DefaultMutableTreeNode(propertyChainModel);
        if (propertyChainModel.getChild() != null) {
            toReturn.insert(toTreeNode(propertyChainModel.getChild(), modelManager), 0);
        }
        return toReturn;
    }

    public static MutableTreeNode toTreeNode(BindingModel binding, OWLModelManager modelManager) {
        DefaultMutableTreeNode toReturn = new DefaultMutableTreeNode(binding.getIdentifier());
        PropertyChainModel propertyChainModel = binding.getPropertyChainModel();
        if (propertyChainModel != null) {
            toReturn.insert(toTreeNode(propertyChainModel, modelManager), 0);
        }
        return toReturn;
    }
}
