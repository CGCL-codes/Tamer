package org.fao.waicent.xmap2D;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

public class Zoom {

    public String code;

    public String label;

    public Rectangle2D bounds;

    public Dimension image_size;

    public Dimension prev_image_size;

    public Zoom() {
        code = "";
        label = "";
        bounds = null;
    }

    public Zoom(String code, String label, Rectangle2D bounds) {
        this.code = code;
        this.label = label;
        this.bounds = bounds;
    }

    public Zoom(String code, String label, Rectangle2D bounds, Dimension image_size) {
        this.code = code;
        this.label = label;
        this.bounds = bounds;
        this.image_size = image_size;
    }

    public Zoom(String code, String label, Rectangle2D bounds, Dimension image_size, Dimension prev_image_size) {
        this.code = code;
        this.label = label;
        this.bounds = bounds;
        this.image_size = image_size;
        this.prev_image_size = prev_image_size;
    }

    public String toString() {
        String rStr = this.code + "," + this.label + " (bounds: " + this.bounds.toString() + ") (image size: " + this.image_size.toString() + ")";
        return rStr;
    }

    public Zoom parseZoom(String setting) throws NumberFormatException {
        int comma_index_1 = setting.indexOf(",");
        int comma_index_2 = setting.indexOf(",", comma_index_1 + 1);
        int comma_index_3 = setting.indexOf(",", comma_index_2 + 1);
        int comma_index_4 = setting.indexOf(",", comma_index_3 + 1);
        int comma_index_5 = setting.indexOf(",", comma_index_4 + 1);
        if (comma_index_5 == -1) {
            throw new NumberFormatException();
        }
        String c = setting.substring(0, comma_index_1).trim();
        String l = setting.substring(comma_index_1 + 1, comma_index_2).trim();
        int x1 = Integer.parseInt(setting.substring(comma_index_2 + 1, comma_index_3).trim());
        int y1 = Integer.parseInt(setting.substring(comma_index_3 + 1, comma_index_4).trim());
        int x2 = Integer.parseInt(setting.substring(comma_index_4 + 1, comma_index_5).trim());
        int y2 = Integer.parseInt(setting.substring(comma_index_5 + 1).trim());
        int x = Math.min(x1, x2);
        if (x < 0) {
            x = 0;
        }
        int y = Math.min(y1, y2);
        if (y < 0) {
            y = 0;
        }
        int width = Math.abs(x1 - x2);
        if (width < 1) {
            width = 1;
        }
        int height = Math.abs(y1 - y2);
        if (height < 1) {
            height = 1;
        }
        return new Zoom(c, l, new Rectangle2D.Float(x, y, width, height));
    }

    public Rectangle2D getZoomBoxBack(Dimension current_image_size) {
        if (this.prev_image_size == null) {
            return null;
        }
        double proportion = current_image_size.width / this.bounds.getWidth();
        double checkproportion = current_image_size.height / this.bounds.getHeight();
        System.out.println("Zoom.getZoomBoxBack : " + proportion + "," + checkproportion);
        double X = -1 * this.bounds.getX() * proportion;
        double Y = -1 * this.bounds.getY() * proportion;
        double W = this.prev_image_size.width * proportion;
        double H = this.prev_image_size.height * proportion;
        Rectangle2D zoombox = new Rectangle2D.Double(X, Y, W, H);
        return zoombox;
    }

    public Rectangle2D getZoomBoxFwd(Dimension current_image_size) {
        if (this.prev_image_size == null) {
            return null;
        }
        double proportion = current_image_size.width / this.prev_image_size.getWidth();
        double checkproportion = current_image_size.height / this.prev_image_size.getHeight();
        System.out.println("Zoom.getZoomBoxFwd : " + proportion + "," + checkproportion);
        double X = this.bounds.getX() * proportion;
        double Y = this.bounds.getY() * proportion;
        double W = this.bounds.getWidth() * proportion;
        double H = this.bounds.getHeight() * proportion;
        Rectangle2D zoombox = new Rectangle2D.Double(X, Y, W, H);
        return zoombox;
    }
}
