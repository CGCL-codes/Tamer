package org.hermeneutix.view.swing.elements;

import static org.hermeneutix.control.MessageKeyConstants.ANALYSIS_ROLE_BASEKEY;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.Border;
import org.hermeneutix.control.Messages;
import org.hermeneutix.control.Options;
import org.hermeneutix.control.swing.PopupFactory;
import org.hermeneutix.control.swing.SemControl;
import org.hermeneutix.model.AbstractConnectable;
import org.hermeneutix.model.Relation;
import org.hermeneutix.view.swing.SemAnalysisPanel;

/**
 * view representation of a {@link Relation} drawing colored lines to show the
 * relations between its subordinated {@link IConnectable}s and
 * {@link JTextField}s above displaying their roles
 * 
 * @author C. Englert
 */
@SuppressWarnings("serial")
public final class SemRelation extends AbstractCommentable implements IConnectable {

    /**
	 * half thickness of the displayed lines
	 */
    protected static final int HALF_LINE_THICKNESS = 2;

    /**
	 * lowered bevel border, when selected
	 */
    protected static final Border COMMENT_BORDER = BorderFactory.createLoweredBevelBorder();

    /**
	 * raised bevel border, when not selected
	 */
    private final Border default_border;

    /**
	 * super ordinated semantical analysis panel
	 */
    final SemAnalysisPanel semArea;

    /**
	 * view representation of the sub ordinated associates
	 */
    private final IConnectable[] viewAssociates;

    /**
	 * text fields displaying the respective roles of the sub ordinated
	 * associates
	 */
    private final JTextField[] roleFields;

    /**
	 * relation line color
	 */
    private final Color color;

    /**
	 * represented model {@link Relation}
	 */
    private final Relation represented;

    /**
	 * the origin language of the current analysis is aligned from left to right
	 */
    private final boolean leftAligned;

    /**
	 * check box to select this {@link SemRelation}
	 */
    private final JCheckBox checkBox = new JCheckBox();

    /**
	 * depth in the relation tree of the current analysis
	 */
    private final int depth;

    /**
	 * index of the first contained proposition
	 */
    private final double firstGridY;

    /**
	 * index of the last contained proposition
	 */
    private final double lastGridY;

    /**
	 * index where to connect to the superordinated relation
	 */
    private double connectY;

    /**
	 * creates a new {@link SemRelation} in the specified semantical analysis
	 * view and setting its values regarding to the represented {@link Relation}
	 * 
	 * @param semArea
	 *            semantical analysis view to be contained in
	 * @param represented
	 *            model {@link Relation} to display
	 */
    public SemRelation(final SemAnalysisPanel semArea, final Relation represented) {
        super(null);
        this.semArea = semArea;
        this.represented = represented;
        this.leftAligned = semArea.getProject().getLanguageFile().isLeftAligned();
        int maxDepth = -1;
        final AbstractConnectable[] modelAssociates = represented.getAssociates();
        this.viewAssociates = new IConnectable[modelAssociates.length];
        for (int i = 0; i < modelAssociates.length; i++) {
            this.viewAssociates[i] = SemControl.getRepresentative(semArea, modelAssociates[i]);
            maxDepth = Math.max(maxDepth, this.viewAssociates[i].getDepth());
        }
        this.depth = maxDepth + 1;
        if (represented.getSuperOrdinatedRelation() == null) {
            add(this.checkBox);
        }
        this.roleFields = new JTextField[modelAssociates.length];
        for (int i = 0; i < this.roleFields.length; i++) {
            final JTextField roleField = new JTextField();
            roleField.setEditable(false);
            roleField.setFont(this.semArea.getProject().getHmxClient().getStandardFont());
            roleField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
            this.roleFields[i] = roleField;
            add(roleField);
        }
        this.firstGridY = SemControl.getRepresentative(semArea, represented.getFirstPropositionContained()).getConnectY();
        this.lastGridY = SemControl.getRepresentative(semArea, represented.getLastPropositionContained()).getConnectY();
        refreshRoles();
        this.default_border = BorderFactory.createEmptyBorder(COMMENT_BORDER.getBorderInsets(this).top, 0, COMMENT_BORDER.getBorderInsets(this).bottom, 0);
        addCommentListener();
        addPopup(PopupFactory.createSemRelationPopup(this.semArea, this));
        Color chosenColor = Color.RED;
        try {
            final String value = Options.getInstance().getProperty(Options.RELATION_COLOR);
            if (value != null && !value.isEmpty()) {
                chosenColor = new Color(Integer.parseInt(value));
            }
        } catch (NumberFormatException ex) {
        }
        this.color = chosenColor;
        setPreferredSize(new Dimension(calculateWidth(), getPreferredSize().height));
    }

    /**
	 * draw the colored lines to show the relations between its subordinated
	 * {@link IConnectable}s and calculates the bounds of the role
	 * {@link JTextField}s.
	 * 
	 * @see javax.swing.JComponent#paintComponent(Graphics)
	 */
    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final int endX = calculateWidth();
        if (endX > getSize().width) {
            final Dimension size = new Dimension(endX, getSize().height);
            setPreferredSize(size);
            setSize(size);
            return;
        }
        final Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(this.color);
        final int gridHeight = (int) (this.lastGridY - this.firstGridY + 1);
        final int height = getSize().height;
        final double partY = height / (double) gridHeight;
        int startX;
        if (this.represented.getSuperOrdinatedRelation() == null) {
            final Dimension boxSize = this.checkBox.getPreferredSize();
            int posX;
            if (this.leftAligned) {
                posX = HALF_LINE_THICKNESS;
                startX = (boxSize.width + (2 * HALF_LINE_THICKNESS));
            } else {
                posX = getSize().width - (boxSize.width + HALF_LINE_THICKNESS);
                startX = getSize().width - (boxSize.width + (2 * HALF_LINE_THICKNESS));
            }
            this.checkBox.setBounds(posX, (int) (((this.connectY - (this.firstGridY - 0.5)) * partY) - (boxSize.height / 2.)), boxSize.width, boxSize.height);
        } else {
            if (this.leftAligned) {
                startX = 0;
            } else {
                startX = getSize().width;
            }
        }
        final int topBorder = COMMENT_BORDER.getBorderInsets(this).top;
        int lineLeftEnd, lineWidth;
        final int[] horizontalLines = new int[this.viewAssociates.length];
        if (this.leftAligned) {
            lineLeftEnd = startX;
            lineWidth = getSize().width - startX;
        } else {
            lineLeftEnd = 0;
            lineWidth = startX;
        }
        for (int i = 0; i < this.viewAssociates.length; i++) {
            final int lineY = (int) ((this.viewAssociates[i].getConnectY() - this.firstGridY + 0.5) * partY - (topBorder / 2.));
            final Rectangle singleHorizontal = new Rectangle(lineLeftEnd, lineY, lineWidth, (2 * HALF_LINE_THICKNESS));
            graphics2D.draw(singleHorizontal);
            graphics2D.fill(singleHorizontal);
            horizontalLines[i] = lineY;
        }
        int verticalPos;
        if (this.leftAligned) {
            verticalPos = lineLeftEnd;
        } else {
            verticalPos = startX - (2 * HALF_LINE_THICKNESS);
        }
        final Rectangle verticalLine = new Rectangle(verticalPos, horizontalLines[0], (2 * HALF_LINE_THICKNESS), (horizontalLines[horizontalLines.length - 1] - horizontalLines[0]) + (2 * HALF_LINE_THICKNESS));
        graphics2D.draw(verticalLine);
        graphics2D.fill(verticalLine);
        int fieldX = startX + 2 + (3 * HALF_LINE_THICKNESS);
        for (int i = 0; i < this.roleFields.length; i++) {
            final JTextField roleField = this.roleFields[i];
            final int fieldHeight = roleField.getPreferredSize().height;
            final int fieldWidth = roleField.getPreferredSize().width + 5;
            if (!this.leftAligned) {
                fieldX = startX - (2 + (3 * HALF_LINE_THICKNESS) + fieldWidth);
            }
            final int fieldY = horizontalLines[i] - fieldHeight - 2 - HALF_LINE_THICKNESS;
            roleField.setBounds(fieldX, fieldY, fieldWidth, fieldHeight);
        }
    }

    /**
	 * calculates the minimum preferred width of the {@link SemRelation}
	 * regarding the {@link JTextField}s containing the roles and the additional
	 * border spacings.
	 * 
	 * @return minimum preferred width
	 */
    private int calculateWidth() {
        int result = -1;
        for (JTextField singleRoleField : this.roleFields) {
            result = Math.max(result, singleRoleField.getPreferredSize().width);
        }
        if (this.represented.getSuperOrdinatedRelation() == null) {
            result += this.checkBox.getPreferredSize().width + (2 * HALF_LINE_THICKNESS);
        }
        return result + 11 + (4 * HALF_LINE_THICKNESS);
    }

    @Override
    public Relation getRepresented() {
        return this.represented;
    }

    public void setCheckBoxVisible(final boolean val) {
        this.checkBox.setVisible(val);
    }

    public boolean isChecked() {
        return this.checkBox.isSelected();
    }

    public void setNotChecked() {
        this.checkBox.setSelected(false);
    }

    public int getDepth() {
        return this.depth;
    }

    @Override
    protected void setDefaultBorder() {
        setBorder(this.default_border);
    }

    @Override
    protected void setCommentBorder() {
        setBorder(COMMENT_BORDER);
    }

    public double getConnectY() {
        return this.connectY;
    }

    /**
	 * @return point to connect of the first contained {@link SemProposition}
	 */
    public double getFirstGridY() {
        return this.firstGridY;
    }

    /**
	 * @return point to connect of the last contained {@link SemProposition}
	 */
    public double getLastGridY() {
        return this.lastGridY;
    }

    /**
	 * enables the ability to be commented.
	 */
    private void addCommentListener() {
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent event) {
                dealWithComments(SemRelation.this.semArea.getProject());
            }
        });
    }

    /**
	 * adds the popup menu for creating, editing and removing of relations.
	 * 
	 * @param popup
	 *            PopupMenu to add
	 */
    private void addPopup(final JPopupMenu popup) {
        final MouseAdapter listener = new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                if (event.isPopupTrigger()) {
                    popup.show(event.getComponent(), event.getX(), event.getY());
                }
            }
        };
        setComponentPopupMenu(popup);
        addMouseListener(listener);
        for (JTextField singleRoleField : this.roleFields) {
            singleRoleField.setComponentPopupMenu(popup);
            singleRoleField.addMouseListener(listener);
        }
    }

    /**
	 * shows the current roles of the represented {@link Relation}.
	 */
    private void refreshRoles() {
        final boolean[] weights = new boolean[this.viewAssociates.length];
        for (int i = 0; i < this.viewAssociates.length; i++) {
            final IConnectable singleAssociate = this.viewAssociates[i];
            weights[i] = singleAssociate.getRepresented().isHighWeight();
            String role = Messages.getString(ANALYSIS_ROLE_BASEKEY + singleAssociate.getRepresented().getRole());
            if (weights[i]) {
                role = role.toUpperCase(Locale.getDefault());
            }
            final JTextField roleField = this.roleFields[i];
            roleField.setText(role);
            roleField.setSize(roleField.getPreferredSize());
        }
        int equalRoleCount = 1;
        for (int i = 1; i < this.roleFields.length; i++) {
            if (this.roleFields[i - 1].getText().equals(this.roleFields[i].getText())) {
                this.roleFields[i - 1].setText(this.roleFields[i - 1].getText() + equalRoleCount);
                equalRoleCount++;
                if (((i + 1) == this.roleFields.length) || (!(this.roleFields[i].getText().equals(this.roleFields[i + 1].getText())))) {
                    this.roleFields[i].setText(this.roleFields[i].getText() + (equalRoleCount));
                    equalRoleCount++;
                }
            } else if (((i + 1) < this.roleFields.length) && (this.roleFields[i - 1].getText().equals(this.roleFields[i + 1].getText()))) {
                this.roleFields[i - 1].setText(this.roleFields[i - 1].getText() + equalRoleCount);
                equalRoleCount++;
            } else if (((i - 1) > 0) && (this.roleFields[i - 2].getText().startsWith(this.roleFields[i].getText()))) {
                this.roleFields[i].setText(this.roleFields[i].getText() + equalRoleCount);
                equalRoleCount++;
            }
        }
        Dimension preferred = getPreferredSize();
        preferred = new Dimension(preferred.width + (2 * HALF_LINE_THICKNESS), preferred.height);
        setSize(preferred);
        for (int i = 0; i < this.viewAssociates.length; i++) {
            if (weights[0] != weights[i]) {
                if (weights[i]) {
                    this.connectY = this.viewAssociates[i].getConnectY();
                } else {
                    this.connectY = this.viewAssociates[0].getConnectY();
                }
                return;
            }
        }
        this.connectY = Math.round(this.viewAssociates[0].getConnectY() + this.viewAssociates[this.viewAssociates.length - 1].getConnectY()) * 0.5;
    }

    /**
	 * adds the specified {@link MouseListener} to itself and all of its
	 * {@link Component}s.
	 */
    @Override
    public synchronized void addMouseListener(final MouseListener listener) {
        super.addMouseListener(listener);
        for (Component singleComponent : getComponents()) {
            singleComponent.addMouseListener(listener);
        }
    }
}
