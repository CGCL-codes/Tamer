package gov.nasa.worldwind.view.orbit;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.util.PropertyAccessor;
import gov.nasa.worldwind.view.ViewPropertyAccessor;

/**
 * @author dcollins
 * @version $Id: OrbitViewPropertyAccessor.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class OrbitViewPropertyAccessor extends ViewPropertyAccessor {

    private OrbitViewPropertyAccessor() {
    }

    public static PropertyAccessor.PositionAccessor createCenterPositionAccessor(OrbitView view) {
        return new CenterPositionAccessor(view);
    }

    public static PropertyAccessor.DoubleAccessor createZoomAccessor(OrbitView view) {
        return new ZoomAccessor(view);
    }

    private static class CenterPositionAccessor implements PropertyAccessor.PositionAccessor {

        private OrbitView orbitView;

        public CenterPositionAccessor(OrbitView view) {
            this.orbitView = view;
        }

        public Position getPosition() {
            if (this.orbitView == null) return null;
            return orbitView.getCenterPosition();
        }

        public boolean setPosition(Position value) {
            if (this.orbitView == null || value == null) return false;
            try {
                this.orbitView.setCenterPosition(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private static class ZoomAccessor implements PropertyAccessor.DoubleAccessor {

        OrbitView orbitView;

        public ZoomAccessor(OrbitView orbitView) {
            this.orbitView = orbitView;
        }

        public final Double getDouble() {
            if (this.orbitView == null) return null;
            return this.orbitView.getZoom();
        }

        public final boolean setDouble(Double value) {
            if (this.orbitView == null || value == null) return false;
            try {
                this.orbitView.setZoom(value);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
