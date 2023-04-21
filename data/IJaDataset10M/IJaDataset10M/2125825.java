package org.omg.tacsit.ui.viewport;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.common.util.CollectionUtils;
import org.omg.tacsit.controller.Viewport;
import org.omg.tacsit.entity.PositionedEntity;
import org.omg.tacsit.geometry.GeodeticPosition;

/**
 * An action which scales a Viewport to a set of Entities.  The viewport will be scaled to be the smallest
 * possible, while containing all of the entities, with a given margin of boundary as a border.
 * @author Matthew Child
 */
public class ScaleToEntitiesAction extends ConfigurableAction {

    private Viewport viewportToScale;

    private List<? extends PositionedEntity> entitiesToContain;

    private double margin;

    private int minimumScalePointCount;

    /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
    public ScaleToEntitiesAction(String name, Icon icon) {
        super(name, icon);
        minimumScalePointCount = 1;
    }

    /**
   * Gets the Viewport that will be scaled.
   * @return The viewport to scale.
   */
    public Viewport getViewportToScale() {
        return viewportToScale;
    }

    /**
   * Sets the Viewport that will be scaled.
   * @param viewportToScale The viewport to scale.
   */
    public void setViewportToScale(Viewport viewportToScale) {
        this.viewportToScale = viewportToScale;
        checkEnabledState();
    }

    /**
   * Gets the Entities that should be contained in the scaled viewport.
   * @return The list of entities to contain
   */
    public List<? extends PositionedEntity> getEntitiesToContain() {
        return entitiesToContain;
    }

    /**
   * Sets the entities that should be contained in the scaled viewport.
   * @param entitiesToContain The list of entities to contain.
   */
    public void setEntitiesToContain(List<? extends PositionedEntity> entitiesToContain) {
        this.entitiesToContain = CollectionUtils.copyToUnmodifiableList(entitiesToContain);
        checkEnabledState();
    }

    /**
   * Gets the margin border distance around the entities that should be visible.
   * @return The distance (in meters)
   */
    public double getMargin() {
        return margin;
    }

    /**
   * Sets the margin border distance around the entities that should be visible.
   * @param margin The distance (in meters)
   */
    public void setMargin(double margin) {
        this.margin = margin;
    }

    /**
   * Gets the minimum number of scale points that are required for this action to be performed.
   * @return The minimum scale point count.
   */
    public int getMinimumScalePointCount() {
        return minimumScalePointCount;
    }

    /**
   * Sets the minimum number of scale points that are required for this action to be performed.
   * @param minimumScalePointCount The minimum scale point count.  May not be negative.
   */
    public void setMinimumScalePointCount(int minimumScalePointCount) {
        if (minimumScalePointCount < 0) {
            throw new IllegalArgumentException("minimumScalePointCount may not be null");
        }
        this.minimumScalePointCount = minimumScalePointCount;
    }

    private boolean hasMinimumScalePointCount() {
        if (entitiesToContain == null) {
            return false;
        } else {
            List<GeodeticPosition> positionsFromEntities = getPositionsFromEntities();
            return minimumScalePointCount <= positionsFromEntities.size();
        }
    }

    @Override
    public boolean isPerformable() {
        return (viewportToScale != null) && hasMinimumScalePointCount();
    }

    private List<GeodeticPosition> getPositionsFromEntities() {
        if (entitiesToContain == null) {
            return Collections.emptyList();
        }
        List<GeodeticPosition> positions = new ArrayList();
        for (PositionedEntity positionedEntity : entitiesToContain) {
            GeodeticPosition positionForEntity = positionedEntity.getReferencePosition();
            if (positionForEntity != null) {
                positions.add(positionForEntity);
            }
        }
        return positions;
    }

    public void actionPerformed(ActionEvent e) {
        List<GeodeticPosition> positionsForEntities = getPositionsFromEntities();
        viewportToScale.scaleToPoints(positionsForEntities, margin);
    }
}
