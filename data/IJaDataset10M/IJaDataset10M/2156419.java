package org.coode.oae.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import org.coode.oae.utils.ParserFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRow;
import org.semanticweb.owl.model.OWLAnnotation;
import org.semanticweb.owl.model.OWLAnnotationAxiom;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.util.NamespaceUtil;
import uk.ac.manchester.mae.parser.ArithmeticsParser;
import uk.ac.manchester.mae.parser.ArithmeticsParserVisitor;
import uk.ac.manchester.mae.parser.MAEAdd;
import uk.ac.manchester.mae.parser.MAEBigSum;
import uk.ac.manchester.mae.parser.MAEBinding;
import uk.ac.manchester.mae.parser.MAEConflictStrategy;
import uk.ac.manchester.mae.parser.MAEIdentifier;
import uk.ac.manchester.mae.parser.MAEIntNode;
import uk.ac.manchester.mae.parser.MAEMult;
import uk.ac.manchester.mae.parser.MAEPower;
import uk.ac.manchester.mae.parser.MAEStart;
import uk.ac.manchester.mae.parser.MAEStoreTo;
import uk.ac.manchester.mae.parser.MAEmanSyntaxClassExpression;
import uk.ac.manchester.mae.parser.MAEpropertyChainExpression;
import uk.ac.manchester.mae.parser.ParseException;
import uk.ac.manchester.mae.parser.SimpleNode;

/**
 * @author Luigi Iannone
 * 
 *         The University Of Manchester<br>
 *         Bio-Health Informatics Group<br>
 *         Apr 28, 2008
 */
@SuppressWarnings("serial")
public class ViewFormulaCellRederer extends JPanel implements ListCellRenderer, ArithmeticsParserVisitor {

    protected OWLEditorKit owlEditorKit;

    protected DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();

    protected String formulaString = "";

    private boolean isClassView;

    private JLabel formulaURILabel;

    private JTextArea formulaContentArea;

    private JLabel iconLabel;

    private static final Color LABEL_COLOR = Color.BLUE.darker();

    /**
	 * @param owlEditorKit
	 */
    public ViewFormulaCellRederer(boolean isClassView, OWLEditorKit owlEditorKit) {
        this.owlEditorKit = owlEditorKit;
        this.isClassView = isClassView;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(1, 2, 1, 2));
        this.formulaURILabel = new JLabel();
        this.formulaURILabel.setForeground(LABEL_COLOR);
        this.formulaContentArea = new JTextArea();
        this.formulaContentArea.setFont(new Font("lucida grande", Font.PLAIN, 12));
        this.formulaContentArea.setLineWrap(true);
        this.formulaContentArea.setWrapStyleWord(true);
        this.add(this.formulaURILabel, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new BorderLayout(3, 3));
        contentPanel.add(this.formulaContentArea, BorderLayout.CENTER);
        this.add(contentPanel, BorderLayout.SOUTH);
        this.formulaContentArea.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(2, 20, 2, 2));
        contentPanel.setOpaque(false);
        this.iconLabel = new JLabel();
        contentPanel.add(this.iconLabel, BorderLayout.WEST);
        this.iconLabel.setIcon(OWLIcons.getIcon("property.data.png"));
    }

    /**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
	 *      java.lang.Object, int, boolean, boolean)
	 */
    @SuppressWarnings("unchecked")
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component toReturn = this.defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof AbstractOWLFrameSectionRow) {
            AbstractOWLFrameSectionRow<Object, OWLAnnotationAxiom<OWLDataProperty>, ? extends Object> row = (AbstractOWLFrameSectionRow) value;
            OWLAnnotationAxiom axiom = row.getAxiom();
            OWLAnnotation annotation = axiom.getAnnotation();
            String uriString = annotation.getAnnotationURI().toString();
            NamespaceUtil nsUtil = new NamespaceUtil();
            String localName = nsUtil.split(uriString, null)[1];
            String annotationValue = annotation.getAnnotationValueAsConstant().getLiteral();
            ParserFactory.initParser(annotationValue, this.owlEditorKit.getModelManager());
            try {
                SimpleNode formula = ArithmeticsParser.Start();
                formula.jjtAccept(this, null);
                String rendering = this.formulaString;
                String propertyName = this.owlEditorKit.getModelManager().getRendering(axiom.getSubject());
                if (this.isClassView) {
                    this.formulaURILabel.setText(propertyName + " " + localName);
                } else {
                    this.formulaURILabel.setText(localName);
                }
                this.formulaContentArea.setText(rendering);
                if (isSelected) {
                    this.formulaContentArea.setForeground(list.getSelectionForeground());
                    this.formulaURILabel.setForeground(list.getSelectionForeground());
                } else {
                    this.formulaContentArea.setForeground(list.getForeground());
                    this.formulaURILabel.setForeground(LABEL_COLOR);
                }
                this.iconLabel.setVisible(this.isClassView);
                toReturn = this;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    public Object visit(MAEStart node, Object data) {
        this.formulaString = node.toString();
        return this.formulaString;
    }

    public Object visit(MAEConflictStrategy node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public Object visit(MAEStoreTo node, Object data) {
        String toReturn = " STORETO <";
        this.formulaString += toReturn;
        toReturn += node.childrenAccept(this, data);
        this.formulaString += ">";
        return toReturn + ">";
    }

    public Object visit(MAEmanSyntaxClassExpression node, Object data) {
        String toReturn = " APPLESTO <" + node.getContent() + ">";
        this.formulaString += toReturn;
        return toReturn;
    }

    public Object visit(MAEBinding node, Object data) {
        String toReturn = node.getIdentifier() + "=";
        this.formulaString += node.getIdentifier() + "=";
        toReturn += node.childrenAccept(this, data);
        return toReturn;
    }

    public Object visit(MAEAdd node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public Object visit(MAEMult node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public Object visit(MAEPower node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public Object visit(MAEIntNode node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public Object visit(MAEIdentifier node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public Object visit(MAEBigSum node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }

    public String getFormulaString() {
        return this.formulaString;
    }

    public Object visit(MAEpropertyChainExpression node, Object data) {
        this.formulaString += node.toString();
        return node.toString();
    }
}
