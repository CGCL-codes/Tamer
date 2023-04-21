package graphxt.view;

import java.awt.Font;
import java.awt.Color;

/**
 *
 * @author giulianoxt
 */
public class GraphTheme {

    public GraphTheme() {
        setBackgroundColor(new Color(50, 50, 50));
        setEdgeColor(new Color(255, 0, 0));
        setEdgeFont(new Font("SansSerif", Font.PLAIN, 13));
        setEdgeFontColor(Color.WHITE);
        setEdgeWidth(2.5);
        setMaxVertexDiameter(1000);
        setMinVertexDiameter(25);
        setSelectedEdgeColor(Color.ORANGE);
        setSelectedVertexFillColor(Color.DARK_GRAY);
        setSelectedVertexGlowColor(Color.WHITE);
        setSelectedVertexGlowSize(8);
        setVertexFillColor(Color.DARK_GRAY);
        setVertexFont(new Font("SansSerif", Font.PLAIN, 14));
        setVertexFontColor(Color.WHITE);
        setVertexStrokeColor(Color.WHITE);
        setVertexStrokeWidth(1);
    }

    public Color getBackgroundColor() {
        return background_color;
    }

    public void setBackgroundColor(Color background_color) {
        this.background_color = background_color;
    }

    public double getMaxVertexDiameter() {
        return max_vertex_radius;
    }

    public void setMaxVertexDiameter(double max_vertex_radius) {
        this.max_vertex_radius = max_vertex_radius;
    }

    public double getMinVertexDiameter() {
        return min_vertex_radius;
    }

    public void setMinVertexDiameter(double min_vertex_radius) {
        this.min_vertex_radius = min_vertex_radius;
    }

    public Color getSelectedVertexFillColor() {
        return selected_vertex_fill_color;
    }

    public void setSelectedVertexFillColor(Color selected_vertex_fill_color) {
        this.selected_vertex_fill_color = selected_vertex_fill_color;
    }

    public Color getSelectedVertexGlowColor() {
        return selected_vertex_glow_color;
    }

    public void setSelectedVertexGlowColor(Color selected_vertex_glow_color) {
        this.selected_vertex_glow_color = selected_vertex_glow_color;
    }

    public double getSelectedVertexGlowSize() {
        return selected_vertex_glow_size;
    }

    public void setSelectedVertexGlowSize(double selected_vertex_glow_size) {
        this.selected_vertex_glow_size = selected_vertex_glow_size;
    }

    public Color getVertexFillColor() {
        return vertex_fill_color;
    }

    public void setVertexFillColor(Color vertex_fill_color) {
        this.vertex_fill_color = vertex_fill_color;
    }

    public Font getVertexFont() {
        return vertex_font;
    }

    public void setVertexFont(Font vertex_font) {
        this.vertex_font = vertex_font;
    }

    public Color getVertexFontColor() {
        return vertex_font_color;
    }

    public void setVertexFontColor(Color vertex_font_color) {
        this.vertex_font_color = vertex_font_color;
    }

    public Color getVertexStrokeColor() {
        return vertex_stroke_color;
    }

    public void setVertexStrokeColor(Color vertex_stroke_color) {
        this.vertex_stroke_color = vertex_stroke_color;
    }

    public double getVertexStrokeWidth() {
        return vertex_stroke_width;
    }

    public void setVertexStrokeWidth(double vertex_stroke_width) {
        this.vertex_stroke_width = vertex_stroke_width;
    }

    public Font getEdgeFont() {
        return edge_font;
    }

    public void setEdgeFont(Font f) {
        edge_font = f;
    }

    public double getEdgeWidth() {
        return edge_width;
    }

    public void setEdgeWidth(double w) {
        edge_width = w;
    }

    public Color getSelectedEdgeColor() {
        return selected_edge_color;
    }

    public void setSelectedEdgeColor(Color c) {
        selected_edge_color = c;
    }

    public Color getEdgeColor() {
        return edge_color;
    }

    public void setEdgeColor(Color c) {
        edge_color = c;
    }

    public Color getEdgeFontColor() {
        return edge_font_color;
    }

    public void setEdgeFontColor(Color c) {
        edge_font_color = c;
    }

    private Color background_color;

    private Font vertex_font;

    private double vertex_stroke_width;

    private double selected_vertex_glow_size;

    private double min_vertex_radius, max_vertex_radius;

    private Color vertex_fill_color;

    private Color vertex_font_color;

    private Color vertex_stroke_color;

    private Color selected_vertex_glow_color;

    private Color selected_vertex_fill_color;

    private Font edge_font;

    private double edge_width;

    private Color selected_edge_color;

    private Color edge_color;

    private Color edge_font_color;

    public static final GraphTheme Default = new GraphTheme();
}
