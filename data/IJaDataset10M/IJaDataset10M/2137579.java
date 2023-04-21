package rescuecore.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Shape;
import rescuecore.Memory;
import rescuecore.RescueConstants;
import rescuecore.objects.Building;

public abstract class BuildingRenderer implements MapRenderer {

    private static final BuildingRenderer ORDINARY = new OrdinaryBuildingRenderer();

    public static BuildingRenderer ordinaryBuildingRenderer() {
        return ORDINARY;
    }

    public static BuildingRenderer filledBuildingRenderer(int fillMode, Color colour) {
        return new FilledBuildingRenderer(fillMode, colour);
    }

    public static BuildingRenderer outlinedBuildingRenderer(int outlineMode, Color colour) {
        return new OutlinedBuildingRenderer(outlineMode, colour);
    }

    public boolean canRender(Object o) {
        return (o instanceof Building);
    }

    private static class OrdinaryBuildingRenderer extends BuildingRenderer {

        public Shape render(Object o, Memory memory, Graphics g, ScreenTransform transform) {
            Building b = (Building) o;
            int[] apexes = b.getApexes();
            int[] xs = new int[apexes.length / 2];
            int[] ys = new int[apexes.length / 2];
            for (int i = 0; i < xs.length; ++i) {
                xs[i] = transform.toScreenX(apexes[i * 2]);
                ys[i] = transform.toScreenY(apexes[i * 2 + 1]);
            }
            int fieryness = b.getFieryness();
            Color colour = ViewConstants.BUILDING_COLOUR;
            if (b.isFireStation()) colour = ViewConstants.FIRE_STATION_COLOUR;
            if (b.isAmbulanceCenter()) colour = ViewConstants.AMBULANCE_CENTER_COLOUR;
            if (b.isPoliceOffice()) colour = ViewConstants.POLICE_OFFICE_COLOUR;
            if (b.isRefuge()) colour = ViewConstants.REFUGE_COLOUR;
            switch(fieryness) {
                case RescueConstants.FIERYNESS_HEATING:
                    colour = ViewConstants.HEATING_COLOUR;
                    break;
                case RescueConstants.FIERYNESS_BURNING:
                    colour = ViewConstants.FIRE_COLOUR;
                    break;
                case RescueConstants.FIERYNESS_INFERNO:
                    colour = ViewConstants.INFERNO_COLOUR;
                    break;
                case RescueConstants.FIERYNESS_WATER_DAMAGE:
                    colour = ViewConstants.WATER_DAMAGE_COLOUR;
                    break;
                case RescueConstants.FIERYNESS_SLIGHTLY_BURNT:
                case RescueConstants.FIERYNESS_MODERATELY_BURNT:
                case RescueConstants.FIERYNESS_VERY_BURNT:
                    colour = ViewConstants.EXTINGUISHED_COLOUR;
                    break;
                case RescueConstants.FIERYNESS_BURNT_OUT:
                    colour = ViewConstants.BURNT_OUT_COLOUR;
                    break;
            }
            RenderTools.setFillMode(g, ViewConstants.FILL_MODE_SOLID, colour);
            g.fillPolygon(xs, ys, xs.length);
            RenderTools.setLineMode(g, ViewConstants.LINE_MODE_SOLID, Color.black);
            g.drawPolygon(xs, ys, xs.length);
            return new Polygon(xs, ys, xs.length);
        }
    }

    private static class FilledBuildingRenderer extends BuildingRenderer {

        private int mode;

        private Color colour;

        FilledBuildingRenderer(int mode, Color colour) {
            this.mode = mode;
            this.colour = colour;
        }

        public Shape render(Object o, Memory memory, Graphics g, ScreenTransform transform) {
            Building b = (Building) o;
            int[] apexes = b.getApexes();
            int[] xs = new int[apexes.length / 2];
            int[] ys = new int[apexes.length / 2];
            for (int i = 0; i < xs.length; ++i) {
                xs[i] = transform.toScreenX(apexes[i * 2]);
                ys[i] = transform.toScreenY(apexes[i * 2 + 1]);
            }
            RenderTools.setFillMode(g, mode, colour);
            g.fillPolygon(xs, ys, xs.length);
            return new Polygon(xs, ys, xs.length);
        }
    }

    private static class OutlinedBuildingRenderer extends BuildingRenderer {

        private int mode;

        private Color colour;

        OutlinedBuildingRenderer(int mode, Color colour) {
            this.mode = mode;
            this.colour = colour;
        }

        public Shape render(Object o, Memory memory, Graphics g, ScreenTransform transform) {
            Building b = (Building) o;
            int[] apexes = b.getApexes();
            int[] xs = new int[apexes.length / 2];
            int[] ys = new int[apexes.length / 2];
            for (int i = 0; i < xs.length; ++i) {
                xs[i] = transform.toScreenX(apexes[i * 2]);
                ys[i] = transform.toScreenY(apexes[i * 2 + 1]);
            }
            RenderTools.setLineMode(g, mode, colour);
            g.drawPolygon(xs, ys, xs.length);
            return new Polygon(xs, ys, xs.length);
        }
    }
}
