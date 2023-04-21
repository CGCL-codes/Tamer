package de.bsd.x2svg.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import de.bsd.x2svg.RuntimeParameters;

/**
 * @author hwr@pilhuhn.de
 *
 */
public class SVGDisplayFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JSVGCanvas canvas;

    private float scale = (float) 1.0;

    private JTextField scaleField;

    private int yinset;

    public SVGDisplayFrame(RuntimeParameters params) {
        yinset = getInsets().bottom;
        canvas = new JSVGCanvas();
        JSVGScrollPane sp = new JSVGScrollPane(canvas);
        sp.setAutoscrolls(true);
        sp.setScrollbarsAlwaysVisible(true);
        sp.scaleChange((float) 1.0);
        setPreferredSize(new Dimension(450, 300));
        setMinimumSize(new Dimension(250, 150));
        File file = new File(params.getSvgOutputFile());
        canvas.setURI(file.toURI().toString());
        setTitle("x2svg - " + params.getInputFileName());
        Container contentPane = getContentPane();
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.add(sp, BorderLayout.CENTER);
        Panel buttons = new Panel();
        buttons.setLayout(new FlowLayout());
        JButton b = new JButton("1:1");
        b.setToolTipText("Original size");
        b.addActionListener(this);
        buttons.add(b);
        JButton b2 = new JButton("++");
        b2.setToolTipText("Zoom in");
        b2.addActionListener(this);
        buttons.add(b2);
        JButton b3 = new JButton("--");
        b3.addActionListener(this);
        b3.setToolTipText("Zoom out");
        buttons.add(b3);
        JButton b4 = new JButton("X");
        b4.addActionListener(this);
        b4.setToolTipText("Center display");
        buttons.add(b4);
        panel.add(buttons, BorderLayout.NORTH);
        scaleField = new JTextField(Float.toString(scale));
        scaleField.setEditable(false);
        panel.add(scaleField, BorderLayout.SOUTH);
        contentPane.add(panel);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String text = button.getText();
            System.out.println("--> " + text);
            AffineTransform at = canvas.getViewBoxTransform();
            if ("X".equals(text)) {
                if (at != null) {
                    at.translate(0, -yinset);
                    at.scale(scale, scale);
                    canvas.setPaintingTransform(at);
                }
            } else {
                if ("++".equals(text)) {
                    scale *= 1.4;
                } else if ("--".equals(text)) {
                    scale /= 1.4;
                } else if ("1:1".equals(text)) {
                    scale = (float) 1.0;
                }
                if (at != null) {
                    at.setToScale(scale, scale);
                    canvas.setPaintingTransform(at);
                    scaleField.setText(Float.toString(scale));
                }
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("Paint");
        System.out.println(g.getClip());
        super.paint(g);
    }
}
