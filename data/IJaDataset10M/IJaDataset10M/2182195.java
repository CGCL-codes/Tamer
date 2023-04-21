package de.felixbruns.jotify.gui.swing.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class JotifyTable extends JTable {

    private Color alternateBackground;

    public JotifyTable(TableModel model) {
        super(model);
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setShowGrid(false);
        this.setBackground(new Color(55, 55, 55));
        this.setAlternateBackground(new Color(49, 49, 49));
        this.setForeground(Color.WHITE);
        this.setSelectionBackground(new Color(169, 217, 254));
        this.setSelectionForeground(new Color(55, 55, 55));
        this.setFont(new Font(Font.DIALOG, Font.PLAIN, 11));
        this.setDefaultRenderer(Object.class, new JotifyAlternatingTableCellRenderer());
        this.setDefaultRenderer(JotifyPopularityIndicator.class, new JotifyPopularityIndicator());
        TableColumnModel columnModel = this.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderRenderer(new JotifyTableHeaderRenderer());
        }
        this.getColumn("Track").setPreferredWidth(120);
        this.getColumn("Artist").setPreferredWidth(120);
        this.getColumn("Time").setMinWidth(45);
        this.getColumn("Time").setMaxWidth(45);
        this.getColumn("Time").setCellRenderer(new JotifyAlternatingTableCellRenderer(SwingConstants.RIGHT));
        this.getColumn("Time").setHeaderRenderer(new JotifyTableHeaderRenderer(SwingConstants.RIGHT));
        this.getColumn("Popularity").setMinWidth(30);
        this.getColumn("Popularity").setPreferredWidth(80);
        this.getColumn("Album").setPreferredWidth(120);
    }

    private Color getAlternateBackground() {
        return this.alternateBackground;
    }

    private void setAlternateBackground(Color color) {
        this.alternateBackground = color;
    }

    private class JotifyAlternatingTableCellRenderer extends DefaultTableCellRenderer {

        private int alignment;

        public JotifyAlternatingTableCellRenderer() {
            this(SwingConstants.LEFT);
        }

        public JotifyAlternatingTableCellRenderer(int alignment) {
            this.alignment = alignment;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            JotifyTable table = (JotifyTable) t;
            if (v instanceof String) {
                this.setText((String) v);
                this.setHorizontalAlignment(this.alignment);
            }
            this.setBorder(new EmptyBorder(4, 4, 4, 4));
            this.setForeground(table.getForeground());
            this.setFont(table.getFont());
            if (s) {
                this.setForeground(table.getSelectionForeground());
                this.setBackground(table.getSelectionBackground());
            } else {
                this.setForeground(table.getForeground());
                this.setBackground((r % 2 == 0) ? table.getBackground() : table.getAlternateBackground());
            }
            return this;
        }
    }

    private class JotifyTableHeaderRenderer extends DefaultTableCellRenderer {

        private int alignment;

        public JotifyTableHeaderRenderer() {
            this(SwingConstants.LEFT);
        }

        public JotifyTableHeaderRenderer(int alignment) {
            this.alignment = alignment;
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            super.getTableCellRendererComponent(t, v, s, f, r, c);
            this.setForeground(new Color(0, 0, 0, 192));
            this.setFont(new Font(Font.DIALOG, Font.BOLD, 11));
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Rectangle bounds = this.getBounds();
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, new Color(168, 168, 168), 0, bounds.height, new Color(134, 134, 134)));
            g2.fillRect(0, 0, bounds.width, bounds.height);
            g2.setPaint(null);
            g2.setColor(new Color(255, 255, 255, 127));
            g2.drawLine(0, 0, bounds.width - 1, 0);
            g2.drawLine(0, 0, 0, bounds.height - 1);
            g2.setColor(new Color(0, 0, 0, 192));
            g2.drawLine(0, bounds.height - 1, bounds.width - 1, bounds.height - 1);
            g2.setColor(new Color(0, 0, 0, 80));
            g2.drawLine(bounds.width - 1, 0, bounds.width - 1, bounds.height - 1);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            FontMetrics metrics = g2.getFontMetrics();
            g2.setColor(this.getForeground());
            if (this.alignment == SwingConstants.RIGHT) {
                g2.drawString(this.getText(), bounds.width - metrics.stringWidth(this.getText()) - 5, bounds.height / 2 + (metrics.getMaxAscent() - metrics.getMaxDescent()) / 2);
            } else if (this.alignment == SwingConstants.CENTER) {
                g2.drawString(this.getText(), bounds.width / 2 - metrics.stringWidth(this.getText()) / 2, bounds.height / 2 + (metrics.getMaxAscent() - metrics.getMaxDescent()) / 2);
            } else {
                g2.drawString(this.getText(), 5, bounds.height / 2 + (metrics.getMaxAscent() - metrics.getMaxDescent()) / 2);
            }
        }
    }
}
