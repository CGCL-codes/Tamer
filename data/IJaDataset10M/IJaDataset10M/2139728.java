package com.objetdirect.gwt.umlapi.client.artifacts.clazz;

import java.util.HashMap;
import java.util.Map.Entry;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.objetdirect.gwt.umlapi.client.artifacts.LinkArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.RelationLinkArtifact;
import com.objetdirect.gwt.umlapi.client.artifacts.RelationLinkArtifactPart;
import com.objetdirect.gwt.umlapi.client.contextMenu.MenuBarAndTitle;
import com.objetdirect.gwt.umlapi.client.editors.RelationFieldEditor;
import com.objetdirect.gwt.umlapi.client.engine.GeometryManager;
import com.objetdirect.gwt.umlapi.client.engine.Point;
import com.objetdirect.gwt.umlapi.client.exceptions.GWTUMLAPIException;
import com.objetdirect.gwt.umlapi.client.gfx.GfxManager;
import com.objetdirect.gwt.umlapi.client.gfx.GfxObject;
import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;
import com.objetdirect.gwt.umlapi.client.helpers.ThemeManager;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkKind;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkStyle;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * This class represents any relation artifact between two classes
 * 
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 */
public class ClassRelationLinkArtifact extends RelationLinkArtifact {

    protected transient GfxObject arrowVirtualGroup;

    protected transient GfxObject line;

    protected transient GfxObject textVirtualGroup;

    private transient HashMap<RelationLinkArtifactPart, GfxObject> gfxObjectPart;

    /**
	 * /!\ Don't forget to increment the serialVersionUID if you change any of the fields above /!\
	 */
    private static final long serialVersionUID = 1L;

    protected ClassArtifact leftClassArtifact;

    protected ClassArtifact rightClassArtifact;

    private int current_delta;

    /** Default constructor ONLY for gwt rpc serializaton. */
    @Deprecated
    @SuppressWarnings("unused")
    private ClassRelationLinkArtifact() {
    }

    /**
	 * Constructor of {@link ClassRelationLinkArtifact}
	 * 
	 * @param canvas
	 *            Where the gfxObject are displayed
	 * @param id
	 *            The artifacts's id
	 * @param left
	 *            The left {@link ClassArtifact} of the relation
	 * @param right
	 *            The right {@link ClassArtifact} of the relation
	 * @param relationKind
	 *            The kind of relation this link is.
	 */
    public ClassRelationLinkArtifact(final UMLCanvas canvas, int id, final ClassArtifact left, final ClassArtifact right, final LinkKind relationKind) {
        super(canvas, id, left, right, relationKind);
        gfxObjectPart = new HashMap<RelationLinkArtifactPart, GfxObject>();
        leftClassArtifact = left;
        left.addDependency(this, right);
        rightClassArtifact = right;
        if (right != left) {
            right.addDependency(this, left);
        } else {
            isSelfLink = true;
        }
        relation.setLeftTarget(left.toUMLComponent());
        relation.setRightTarget(right.toUMLComponent());
    }

    @Override
    public void edit(final GfxObject editedGfxObject) {
        RelationLinkArtifactPart editPart = null;
        for (Entry<RelationLinkArtifactPart, GfxObject> entry : gfxObjectPart.entrySet()) {
            if (entry.getValue().equals(editedGfxObject)) {
                editPart = entry.getKey();
            }
        }
        if (editPart == null) {
            this.createPart(RelationLinkArtifactPart.NAME);
        } else {
            edit(editPart);
        }
    }

    private void edit(final RelationLinkArtifactPart editPart) {
        if (editPart == null) {
            throw new GWTUMLAPIException("There is no corresponding RelationLinkArtifactPart attached to the given GfxObject");
        }
        GfxObject editedGfxObject = gfxObjectPart.get(editPart);
        final RelationFieldEditor editor = new RelationFieldEditor(canvas, this, editPart);
        editor.startEdition(editPart.getText(relation), editedGfxObject.getLocation().getX(), editedGfxObject.getLocation().getY(), GfxManager.getPlatform().getTextWidthFor(editedGfxObject) + RECTANGLE_RIGHT_PADDING + RECTANGLE_LEFT_PADDING, false, true);
    }

    /**
	 * Request a creation of a {@link RelationLinkArtifactPart} and set its text.
	 * 
	 * @param part
	 *            The {@link RelationLinkArtifactPart} to edit
	 */
    public void createPart(final RelationLinkArtifactPart part) {
        String defaultText;
        switch(part) {
            case NAME:
                defaultText = leftClassArtifact.getName() + "-" + rightClassArtifact.getName();
                break;
            case LEFT_CARDINALITY:
            case RIGHT_CARDINALITY:
                defaultText = "*";
                break;
            case LEFT_CONSTRAINT:
            case RIGHT_CONSTRAINT:
                defaultText = "{union}";
                break;
            case LEFT_ROLE:
            case RIGHT_ROLE:
                defaultText = "role";
                break;
            case LEFT_STEREOTYPE:
            case RIGHT_STEREOTYPE:
                defaultText = "<<owner>>";
                break;
            default:
                defaultText = "?";
        }
        part.setText(relation, defaultText);
        this.rebuildGfxObject();
        this.edit(gfxObjectPart.get(part));
    }

    /**
	 * Getter for the left {@link ClassArtifact} of this relation
	 * 
	 * @return the left {@link ClassArtifact} of this relation
	 */
    public ClassArtifact getLeftClassArtifact() {
        return leftClassArtifact;
    }

    /**
	 * Getter for the right {@link ClassArtifact} of this relation
	 * 
	 * @return the right {@link ClassArtifact} of this relation
	 */
    public ClassArtifact getRightClassArtifact() {
        return rightClassArtifact;
    }

    @Override
    public MenuBarAndTitle getRightMenu() {
        final MenuBarAndTitle rightMenu = new MenuBarAndTitle();
        rightMenu.setName(relation.getLinkKind().getName() + " " + leftClassArtifact.getName() + " " + relation.getLeftAdornment().getShape().getIdiom() + "-" + relation.getRightAdornment().getShape().getIdiom(true) + " " + rightClassArtifact.getName());
        final MenuBar leftSide = new MenuBar(true);
        final MenuBar rightSide = new MenuBar(true);
        for (final RelationLinkArtifactPart relationLinkArtifactPart : RelationLinkArtifactPart.values()) {
            final MenuBar editDelete = new MenuBar(true);
            if (!relationLinkArtifactPart.getText(relation).equals("")) {
                editDelete.addItem("Edit", this.editCommand(relationLinkArtifactPart));
                editDelete.addItem("Delete", this.deleteCommand(relationLinkArtifactPart));
            } else {
                editDelete.addItem("Create", this.createCommand(relationLinkArtifactPart));
            }
            if (relationLinkArtifactPart.isLeft()) {
                leftSide.addItem(relationLinkArtifactPart.toString(), editDelete);
            } else {
                if (relationLinkArtifactPart != RelationLinkArtifactPart.NAME) {
                    rightSide.addItem(relationLinkArtifactPart.toString(), editDelete);
                } else {
                    rightMenu.addItem(relationLinkArtifactPart.toString(), editDelete);
                }
            }
        }
        final MenuBar leftNavigability = new MenuBar(true);
        leftNavigability.addItem("Navigable", this.setNavigabilityCommand(relation, true, true));
        leftNavigability.addItem("Not Navigable", this.setNavigabilityCommand(relation, true, false));
        leftNavigability.addItem("Unknown", this.setNavigabilityCommand(relation, true));
        leftSide.addItem("Navigability", leftNavigability);
        final MenuBar rightNavigability = new MenuBar(true);
        rightNavigability.addItem("Navigable", this.setNavigabilityCommand(relation, false, true));
        rightNavigability.addItem("Not Navigable", this.setNavigabilityCommand(relation, false, false));
        rightNavigability.addItem("Unknown", this.setNavigabilityCommand(relation, false));
        rightSide.addItem("Navigability", rightNavigability);
        rightMenu.addItem(leftClassArtifact.getName() + " side", leftSide);
        rightMenu.addItem(rightClassArtifact.getName() + " side", rightSide);
        rightMenu.addItem("Reverse", this.reverseCommand(relation));
        return rightMenu;
    }

    @Override
    public void removeCreatedDependency() {
        leftClassArtifact.removeDependency(this);
        rightClassArtifact.removeDependency(this);
    }

    /**
	 * Reset the navigability of the left side to unknown <br />
	 * The left side must not be a generalization, realization, aggregation or composition otherwise this method do
	 * nothing
	 */
    public void resetLeftNavigability() {
        if (relation.getLeftAdornment().isNavigabilityAdornment()) {
            relation.setLeftAdornment(LinkAdornment.NONE);
        }
    }

    /**
	 * Reset the navigability of the right side to unknown <br />
	 * The right side must not be a generalization, realization, aggregation or composition otherwise this method do
	 * nothing
	 */
    public void resetRightNavigability() {
        if (relation.getRightAdornment().isNavigabilityAdornment()) {
            relation.setRightAdornment(LinkAdornment.NONE);
        }
    }

    /**
	 * Setter for the left and right cardinalities in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param leftCardinality
	 *            The left cardinality text to be set
	 * @param rightCardinality
	 *            The right cardinality text to be set
	 */
    public void setCardinalities(final String leftCardinality, final String rightCardinality) {
        relation.setLeftCardinality(leftCardinality);
        relation.setRightCardinality(rightCardinality);
    }

    /**
	 * Setter for the relation left {@link LinkArtifact.LinkAdornment}
	 * 
	 * @param leftAdornment
	 *            The left {@link LinkArtifact.LinkAdornment} to be set
	 */
    public void setLeftAdornment(final LinkAdornment leftAdornment) {
        relation.setLeftAdornment(leftAdornment);
    }

    /**
	 * Setter for the leftCardinality in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param leftCardinality
	 *            The leftCardinality text to be set
	 */
    public void setLeftCardinality(final String leftCardinality) {
        relation.setLeftCardinality(leftCardinality);
    }

    /**
	 * Setter for the leftConstraint in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param leftConstraint
	 *            The leftConstraint text to be set
	 */
    public void setLeftConstraint(final String leftConstraint) {
        relation.setLeftConstraint(leftConstraint);
    }

    /**
	 * Set the state of left navigability <br />
	 * The left side must not be a generalization, realization, aggregation or composition otherwise this method do
	 * nothing <br />
	 * To set the unknown state see {@link ClassRelationLinkArtifact#resetLeftNavigability()}
	 * 
	 * @param isNavigable
	 *            If true set the link's side to navigable otherwise set it to NOT navigable
	 * 
	 */
    public void setLeftNavigability(final boolean isNavigable) {
        if (relation.getLeftAdornment().isNavigabilityAdornment()) {
            relation.setLeftAdornment(isNavigable ? LinkAdornment.WIRE_ARROW : LinkAdornment.WIRE_CROSS);
        }
    }

    /**
	 * Setter for the leftRole in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param leftRole
	 *            The leftRole text to be set
	 */
    public void setLeftRole(final String leftRole) {
        relation.setLeftRole(leftRole);
    }

    /**
	 * Setter for the relation {@link LinkArtifact.LinkStyle}
	 * 
	 * @param linkStyle
	 *            The {@link LinkArtifact.LinkStyle} to be set
	 */
    public void setLinkStyle(final LinkStyle linkStyle) {
        relation.setLinkStyle(linkStyle);
    }

    /**
	 * Setter for the name in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param name
	 *            The name text to be set
	 */
    public void setName(final String name) {
        relation.setName(name);
    }

    /**
	 * Setter for the relation {@link LinkKind}
	 * 
	 * @param relationKind
	 *            The {@link LinkKind} to be set
	 */
    public void setRelationKind(final LinkKind relationKind) {
        relation.setLinkKind(relationKind);
    }

    /**
	 * Setter for the relation right {@link LinkArtifact.LinkAdornment}
	 * 
	 * @param rightAdornment
	 *            The right{@link LinkArtifact.LinkAdornment} to be set
	 */
    public void setRightAdornment(final LinkAdornment rightAdornment) {
        relation.setRightAdornment(rightAdornment);
    }

    /**
	 * Setter for the rightCardinality in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param rightCardinality
	 *            The rightCardinality text to be set
	 */
    public void setRightCardinality(final String rightCardinality) {
        relation.setRightCardinality(rightCardinality);
    }

    /**
	 * Setter for the rightConstraint in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param rightConstraint
	 *            The rightConstraint text to be set
	 */
    public void setRightConstraint(final String rightConstraint) {
        relation.setRightConstraint(rightConstraint);
    }

    /**
	 * Set the state of right navigability <br />
	 * The right side must not be a generalization, realization, aggregation or composition otherwise this method do
	 * nothing <br />
	 * To set the unknown state see {@link ClassRelationLinkArtifact#resetRightNavigability()}
	 * 
	 * @param isNavigable
	 *            If true set the link's side to navigable otherwise set it to NOT navigable
	 * 
	 */
    public void setRightNavigability(final boolean isNavigable) {
        if (relation.getRightAdornment().isNavigabilityAdornment()) {
            relation.setRightAdornment(isNavigable ? LinkAdornment.WIRE_ARROW : LinkAdornment.WIRE_CROSS);
        }
    }

    /**
	 * Setter for the rightRole in {@link UMLRelation} This does not update the graphical object
	 * 
	 * @param rightRole
	 *            The rightRole text to be set
	 */
    public void setRightRole(final String rightRole) {
        relation.setRightRole(rightRole);
    }

    public UMLRelation toUMLComponent() {
        return relation;
    }

    @Override
    public String toURL() {
        return "ClassRelationLink$<" + leftClassArtifact.getId() + ">!<" + rightClassArtifact.getId() + ">!" + relation.getLinkKind().getName() + "!" + relation.getName() + "!" + relation.getLinkStyle().getName() + "!" + relation.getLeftAdornment().getName() + "!" + relation.getLeftCardinality() + "!" + relation.getLeftConstraint() + "!" + relation.getLeftRole() + "!" + relation.getRightAdornment().getName() + "!" + relation.getRightCardinality() + "!" + relation.getRightConstraint() + "!" + relation.getRightRole();
    }

    @Override
    public void unselect() {
        super.unselect();
        line.setStroke(ThemeManager.getTheme().getClassRelationForegroundColor(), 1);
        arrowVirtualGroup.setStroke(ThemeManager.getTheme().getClassRelationForegroundColor(), 1);
    }

    int getTextX(final GfxObject text, final boolean isLeft) {
        Point relative_point1 = leftPoint;
        Point relative_point2 = rightPoint;
        final int textWidth = GfxManager.getPlatform().getTextWidthFor(text);
        if (!isLeft) {
            relative_point1 = rightPoint;
            relative_point2 = leftPoint;
        }
        switch(isLeft ? leftDirection : rightDirection) {
            case LEFT:
                return relative_point1.getX() - textWidth - RECTANGLE_LEFT_PADDING;
            case RIGHT:
                return relative_point1.getX() + RECTANGLE_RIGHT_PADDING;
            case UP:
            case DOWN:
            case UNKNOWN:
                if (relative_point1.getX() < relative_point2.getX()) {
                    return relative_point1.getX() - textWidth - RECTANGLE_LEFT_PADDING;
                }
                return relative_point1.getX() + RECTANGLE_RIGHT_PADDING;
        }
        return 0;
    }

    int getTextY(final GfxObject text, final boolean isLeft) {
        Point relative_point1 = leftPoint;
        Point relative_point2 = rightPoint;
        if (!isLeft) {
            relative_point1 = rightPoint;
            relative_point2 = leftPoint;
        }
        final int textHeight = GfxManager.getPlatform().getTextHeightFor(text);
        final int delta = current_delta;
        current_delta += 8;
        switch(isLeft ? leftDirection : rightDirection) {
            case LEFT:
            case RIGHT:
                if (relative_point1.getY() > relative_point2.getY()) {
                    return relative_point1.getY() + RECTANGLE_BOTTOM_PADDING + delta;
                }
                return relative_point1.getY() - textHeight - RECTANGLE_TOP_PADDING - delta;
            case UP:
                return relative_point1.getY() - textHeight - RECTANGLE_TOP_PADDING - delta;
            case DOWN:
            case UNKNOWN:
                return relative_point1.getY() + RECTANGLE_BOTTOM_PADDING + delta;
        }
        return 0;
    }

    @Override
    protected void buildGfxObject() {
        if (isTheOneRebuilding) {
            return;
        }
        gfxObjectPart.clear();
        line = this.buildLine();
        line.setStroke(ThemeManager.getTheme().getClassRelationForegroundColor(), 1);
        line.setStrokeStyle(relation.getLinkStyle().getGfxStyle());
        line.addToVirtualGroup(gfxObject);
        arrowVirtualGroup = GfxManager.getPlatform().buildVirtualGroup();
        arrowVirtualGroup.addToVirtualGroup(gfxObject);
        final GfxObject leftArrow = GeometryManager.getPlatform().buildAdornment(leftPoint, leftDirectionPoint, relation.getLeftAdornment());
        final GfxObject rightArrow = GeometryManager.getPlatform().buildAdornment(rightPoint, rightDirectionPoint, relation.getRightAdornment());
        if (leftArrow != null) {
            leftArrow.addToVirtualGroup(arrowVirtualGroup);
        }
        if (rightArrow != null) {
            rightArrow.addToVirtualGroup(arrowVirtualGroup);
        }
        textVirtualGroup = GfxManager.getPlatform().buildVirtualGroup();
        textVirtualGroup.addToVirtualGroup(gfxObject);
        if (!relation.getName().equals("")) {
            Log.trace("Creating name");
            final GfxObject nameGfxObject = GfxManager.getPlatform().buildText(relation.getName(), nameAnchorPoint);
            nameGfxObject.setFont(OptionsManager.getSmallFont());
            nameGfxObject.addToVirtualGroup(textVirtualGroup);
            nameGfxObject.setStroke(ThemeManager.getTheme().getClassRelationBackgroundColor(), 0);
            nameGfxObject.setFillColor(ThemeManager.getTheme().getClassRelationForegroundColor());
            nameGfxObject.translate(new Point(-GfxManager.getPlatform().getTextWidthFor(nameGfxObject) / 2, 0));
            gfxObjectPart.put(RelationLinkArtifactPart.NAME, nameGfxObject);
        }
        current_delta = 0;
        if (!relation.getLeftCardinality().equals("")) {
            GfxObject leftCardinalityGfxText = this.createText(relation.getLeftCardinality(), RelationLinkArtifactPart.LEFT_CARDINALITY);
            leftCardinalityGfxText.addToVirtualGroup(textVirtualGroup);
        }
        if (!relation.getLeftConstraint().equals("")) {
            GfxObject leftConstraintGfxText = this.createText(relation.getLeftConstraint(), RelationLinkArtifactPart.LEFT_CONSTRAINT);
            leftConstraintGfxText.addToVirtualGroup(textVirtualGroup);
        }
        if (!relation.getLeftRole().equals("")) {
            GfxObject leftRoleGfxText = this.createText(relation.getLeftRole(), RelationLinkArtifactPart.LEFT_ROLE);
            leftRoleGfxText.addToVirtualGroup(textVirtualGroup);
        }
        if (!relation.getLeftStereotype().equals("")) {
            GfxObject leftStereotypeGfxText = this.createText(relation.getLeftStereotype(), RelationLinkArtifactPart.LEFT_STEREOTYPE);
            leftStereotypeGfxText.addToVirtualGroup(textVirtualGroup);
        }
        current_delta = 0;
        if (!relation.getRightCardinality().equals("")) {
            GfxObject rightCardinalityGfxText = this.createText(relation.getRightCardinality(), RelationLinkArtifactPart.RIGHT_CARDINALITY);
            rightCardinalityGfxText.addToVirtualGroup(textVirtualGroup);
        }
        if (!relation.getRightConstraint().equals("")) {
            GfxObject rightConstraintGfxText = this.createText(relation.getRightConstraint(), RelationLinkArtifactPart.RIGHT_CONSTRAINT);
            rightConstraintGfxText.addToVirtualGroup(textVirtualGroup);
        }
        if (!relation.getRightRole().equals("")) {
            GfxObject rightRoleGfxText = this.createText(relation.getRightRole(), RelationLinkArtifactPart.RIGHT_ROLE);
            rightRoleGfxText.addToVirtualGroup(textVirtualGroup);
        }
        if (!relation.getRightStereotype().equals("")) {
            GfxObject rightStereotypeGfxText = this.createText(relation.getRightStereotype(), RelationLinkArtifactPart.RIGHT_STEREOTYPE);
            rightStereotypeGfxText.addToVirtualGroup(textVirtualGroup);
        }
        gfxObject.moveToBack();
    }

    @Override
    protected void select() {
        super.select();
        line.setStroke(ThemeManager.getTheme().getClassRelationHighlightedForegroundColor(), 2);
        arrowVirtualGroup.setStroke(ThemeManager.getTheme().getClassRelationHighlightedForegroundColor(), 2);
    }

    private Command createCommand(final RelationLinkArtifactPart relationArtifactPart) {
        return new Command() {

            public void execute() {
                ClassRelationLinkArtifact.this.createPart(relationArtifactPart);
            }
        };
    }

    private GfxObject createText(final String text, final RelationLinkArtifactPart part) {
        final GfxObject textGfxObject = GfxManager.getPlatform().buildText(text, Point.getOrigin());
        textGfxObject.setFont(OptionsManager.getSmallFont());
        textGfxObject.setStroke(ThemeManager.getTheme().getClassRelationBackgroundColor(), 0);
        textGfxObject.setFillColor(ThemeManager.getTheme().getClassRelationForegroundColor());
        if (leftClassArtifact != rightClassArtifact) {
            Log.trace("Creating text : " + text + " at " + this.getTextX(textGfxObject, part.isLeft()) + " : " + this.getTextY(textGfxObject, part.isLeft()));
            textGfxObject.translate(new Point(this.getTextX(textGfxObject, part.isLeft()), this.getTextY(textGfxObject, part.isLeft())));
        } else {
            if (part.isLeft()) {
                textGfxObject.translate(Point.add(leftClassArtifact.getCenter(), new Point(OptionsManager.get("ArrowWidth") / 2 + TEXT_LEFT_PADDING, -(leftClassArtifact.getHeight() + REFLEXIVE_PATH_Y_GAP) / 2 + current_delta)));
            } else {
                textGfxObject.translate(Point.add(leftClassArtifact.getLocation(), new Point(leftClassArtifact.getWidth() + REFLEXIVE_PATH_X_GAP + TEXT_LEFT_PADDING, current_delta)));
            }
            current_delta += 8;
        }
        gfxObjectPart.put(part, textGfxObject);
        return textGfxObject;
    }

    private Command deleteCommand(final RelationLinkArtifactPart relationArtifactPart) {
        return new Command() {

            public void execute() {
                relationArtifactPart.setText(ClassRelationLinkArtifact.this.relation, "");
                ClassRelationLinkArtifact.this.rebuildGfxObject();
            }
        };
    }

    private Command editCommand(final RelationLinkArtifactPart relationArtifactPart) {
        return new Command() {

            public void execute() {
                ClassRelationLinkArtifact.this.edit(relationArtifactPart);
            }
        };
    }

    private Command reverseCommand(final UMLRelation linkRelation) {
        return new Command() {

            public void execute() {
                linkRelation.reverse();
                ClassRelationLinkArtifact.this.rebuildGfxObject();
            }
        };
    }

    private Command setNavigabilityCommand(final UMLRelation relation, final boolean isLeft) {
        return new Command() {

            public void execute() {
                if (isLeft) {
                    relation.setLeftAdornment(LinkAdornment.NONE);
                } else {
                    relation.setRightAdornment(LinkAdornment.NONE);
                }
                ClassRelationLinkArtifact.this.rebuildGfxObject();
            }
        };
    }

    private Command setNavigabilityCommand(final UMLRelation relation, final boolean isLeft, final boolean isNavigable) {
        return new Command() {

            public void execute() {
                final LinkAdornment adornment = isNavigable ? LinkAdornment.WIRE_ARROW : LinkAdornment.WIRE_CROSS;
                if (isLeft) {
                    relation.setLeftAdornment(adornment);
                } else {
                    relation.setRightAdornment(adornment);
                }
                ClassRelationLinkArtifact.this.rebuildGfxObject();
            }
        };
    }

    @Override
    public void setUpAfterDeserialization() {
        gfxObjectPart = new HashMap<RelationLinkArtifactPart, GfxObject>();
        buildGfxObject();
    }
}
