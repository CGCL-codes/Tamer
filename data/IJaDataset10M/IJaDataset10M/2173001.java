package picto.test;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import picto.core.*;
import picto.draw.*;

/**
 *
 * @author davedes
 */
public class DrawTest extends JFrame implements DrawListener, Overlay {

    public static void main(String[] args) {
        DrawTest test = new DrawTest();
        test.setLocationRelativeTo(null);
        test.setVisible(true);
    }

    private DrawCanvas canvas = new DrawCanvas();

    private Robot robot;

    private Project proj;

    private Brush currentBrush;

    private Layer currentLayer;

    private Color foregroundPaint = Color.black;

    private int masterDiam = Brush.DEFAULT_DIAMETER;

    private boolean masterDiamEnabled = false;

    public DrawTest() {
        setTitle("Picto Beta Test");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(canvas);
        try {
            robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            proj = createProject();
            canvas.installProject(proj);
            if (proj.getBrushPack() == null || proj.getBrushPack().size() == 0) throw new IOException("no brush packs");
            if (proj.getLayerGroup() == null || proj.getLayerGroup().size() == 0) throw new IOException("no layers");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        currentBrush = proj.getBrushPack().get(0);
        currentLayer = proj.getLayerGroup().get(0);
        Dimension size = proj.getSize();
        brushState = new ImageState(size.width, size.height);
        canvas.setSize(size);
        setSize(size);
        setResizable(false);
        canvas.addDrawListener(this);
        setJMenuBar(createMenuBar());
    }

    protected Project createProject() throws IOException {
        Project proj = new Project();
        proj.setSize(new Dimension(500, 500));
        LayerGroup group = new LayerGroup();
        Layer bg = proj.createLayer("Background");
        Layer mg = proj.createLayer("Midground");
        Layer fg = proj.createLayer("Foreground");
        group.add(bg);
        group.add(mg);
        group.add(fg);
        proj.setLayerGroup(group);
        BrushPack pack = new BrushPack();
        Color fgColor = foregroundPaint;
        CachedImage spatter = new CachedImage("brush_spatter", "res/brushes/brush1.png");
        CachedImage soft = new CachedImage("brush_soft", "res/brushes/brush2.png");
        Brush b1 = new Brush("Hard Round");
        b1.setDiameter(8);
        b1.setTextureColor(fgColor);
        b1.ensureModifiedTexture();
        pack.add(b1);
        Brush b2 = new Brush("Soft Round");
        b2.setTexture(soft);
        b2.setDiameter(soft.getWidth() / 2);
        b2.setTextureColor(fgColor);
        b2.ensureModifiedTexture();
        pack.add(b2);
        Brush b3 = new Brush("Spatter");
        b3.setTexture(spatter);
        b3.setDiameter(spatter.getWidth() / 2);
        b3.setTextureColor(fgColor);
        b3.ensureModifiedTexture();
        pack.add(b3);
        proj.setBrushPack(pack);
        return proj;
    }

    private int lastX, lastY;

    private ImageState brushState;

    private boolean brushing;

    private boolean modCheck(DrawEvent e) {
        return false;
    }

    public void drawPress(DrawEvent e) {
        if (modCheck(e)) return;
        brushing = true;
        currentLayer.setOverlay(this);
        paintDot(brushState.getGraphics(), e.getX(), e.getY(), e.getPressure());
        this.lastX = e.getX();
        this.lastY = e.getY();
    }

    public void drawDrag(DrawEvent e) {
        if (modCheck(e)) {
            return;
        }
        float pressure = e.getPressure();
        int size = brushSize(pressure);
        float x1 = lastX;
        float y1 = lastY;
        float x2 = e.getX();
        float y2 = e.getY();
        float dist = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        float dx = (x2 - x1) / dist;
        float dy = (y2 - y1) / dist;
        float inc = size / 8f;
        inc = Math.max(inc, 1f);
        for (float d = 1f; d <= dist; d += inc) {
            int x = (int) Math.round(x1 + dx * d);
            int y = (int) Math.round(y1 + dy * d);
            paintDot(brushState.getGraphics(), x, y, pressure);
        }
        this.lastX = (int) x2;
        this.lastY = (int) y2;
    }

    public void drawRelease(DrawEvent e) {
        if (brushing) {
            brushing = false;
            apply();
        }
    }

    private void apply() {
        Layer layer = currentLayer;
        ArchivableImage layerImg = layer.getImage();
        Graphics2D layerGraphics = layerImg.getGraphics();
        paintOverlay(layerGraphics);
        layer.setOverlay(null);
        Graphics2D g2d = brushState.getGraphics();
        int w = brushState.getWidth();
        int h = brushState.getHeight();
        clear(g2d, w, h);
        canvas.repaint();
    }

    private void clear(Graphics2D g2d, int width, int height) {
        Composite oldComp = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1f));
        g2d.fillRect(0, 0, width, height);
        g2d.setComposite(oldComp);
    }

    public void paintOverlay(Graphics2D g2d) {
        if (brushState == null) return;
        Composite oldComp = g2d.getComposite();
        float opacity = proj.getOptions().getBrushOpacity();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2d.drawImage(brushState.getImage(), 0, 0, null);
        g2d.setComposite(oldComp);
    }

    private int brushSize(float pressure) {
        int d = masterDiamEnabled ? masterDiam : currentBrush.getDiameter();
        return Math.max(1, (int) (d * pressure));
    }

    protected void paintDot(Graphics2D g2d, int x, int y, float pressure) {
        Image texture = currentBrush != null ? currentBrush.getModifiedTexture() : null;
        int size = brushSize(pressure);
        int mid = size / 2;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setPaint(foregroundPaint);
        if (texture == null) g2d.fillOval(x - mid, y - mid, size, size); else g2d.drawImage(texture, x - mid, y - mid, size, size, null);
        canvas.repaint(x - mid, y - mid, size, size);
    }

    public void setForegroundPaint(Color c) {
        foregroundPaint = c;
        currentBrush.setTextureColor(foregroundPaint);
        currentBrush.ensureModifiedTexture();
    }

    protected JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(quit);
        bar.add(file);
        JMenu options = new JMenu("Options");
        JMenuItem color = new JMenuItem("Choose Color");
        color.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(DrawTest.this, "Choose Color", foregroundPaint);
                if (c != null) {
                    setForegroundPaint(c);
                }
            }
        });
        options.add(color);
        JMenuItem opacity = new JMenuItem("Set Opacity");
        opacity.addActionListener(new ActionListener() {

            private JSpinner spinner = null;

            private JPanel content = null;

            private void init() {
                if (spinner == null) {
                    spinner = new JSpinner(new SpinnerNumberModel(100, 0, 100, 2));
                    JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner);
                    spinner.setEditor(editor);
                    content = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    content.add(new JLabel("Select master opacity: "));
                    content.add(spinner);
                    content.add(new JLabel("%"));
                }
            }

            public void actionPerformed(ActionEvent e) {
                init();
                Project.Options opts = proj.getOptions();
                spinner.setValue(opts.getBrushOpacity() * 100);
                canvas.setDrawingEnabled(false);
                JOptionPane.showMessageDialog(DrawTest.this, content, "Set Opacity", JOptionPane.PLAIN_MESSAGE);
                canvas.setDrawingEnabled(true);
                float f = ((Number) spinner.getValue()).floatValue() / 100;
                opts.setBrushOpacity(f);
            }
        });
        options.add(opacity);
        JMenuItem diam = new JMenuItem("Set Master Diameter");
        diam.addActionListener(new ActionListener() {

            private JSpinner spinner = null;

            private JPanel content = null;

            private JCheckBox box = null;

            private void init() {
                if (spinner == null) {
                    int def = Brush.DEFAULT_DIAMETER;
                    spinner = new JSpinner(new SpinnerNumberModel(def, 0, 100, 1));
                    JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner);
                    spinner.setEditor(editor);
                    box = new JCheckBox("Master Enabled");
                    box.addActionListener(this);
                    JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    top.add(new JLabel("Diameter: "));
                    top.add(spinner);
                    top.add(new JLabel("px"));
                    JPanel bot = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    bot.add(box);
                    content = new JPanel(new BorderLayout());
                    content.add(top, BorderLayout.NORTH);
                    content.add(bot, BorderLayout.SOUTH);
                }
            }

            public void actionPerformed(ActionEvent e) {
                init();
                if (e.getSource() == box) {
                    masterDiamEnabled = box.isSelected();
                } else {
                    box.setSelected(masterDiamEnabled);
                    spinner.setValue(masterDiam);
                    JOptionPane.showMessageDialog(DrawTest.this, content, "Master Diameter", JOptionPane.PLAIN_MESSAGE);
                    int i = ((Number) spinner.getValue()).intValue();
                    masterDiam = i;
                }
            }
        });
        options.add(diam);
        final JCheckBoxMenuItem pressure = new JCheckBoxMenuItem("Tablet Pressure");
        pressure.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                proj.getOptions().setPressureEnabled(pressure.isSelected());
            }
        });
        pressure.setSelected(proj.getOptions().isPressureEnabled());
        options.add(pressure);
        bar.add(options);
        JMenu layer = new JMenu("Layer");
        ButtonGroup layerBtnGrp = new ButtonGroup();
        for (int i = 0; i < proj.getLayerGroup().size(); i++) {
            final Layer l = proj.getLayerGroup().get(i);
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(l.getName());
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    currentLayer = l;
                }
            });
            layer.add(item);
            layerBtnGrp.add(item);
            if (i == 0) {
                item.setSelected(true);
            }
        }
        JMenuItem fillLayer = new JMenuItem("Fill Current");
        fillLayer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(DrawTest.this, "Fill Color", Color.white);
                if (c != null) {
                    Graphics2D g2d = currentLayer.getImage().getGraphics();
                    Dimension d = proj.getSize();
                    g2d.setColor(c);
                    g2d.fillRect(0, 0, d.width, d.height);
                    canvas.repaint();
                }
            }
        });
        layer.add(fillLayer);
        JMenuItem clearLayer = new JMenuItem("Clear Current");
        clearLayer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Graphics2D g2d = currentLayer.getImage().getGraphics();
                Dimension d = proj.getSize();
                clear(g2d, d.width, d.height);
                canvas.repaint();
            }
        });
        layer.add(clearLayer);
        bar.add(layer);
        JMenu brush = new JMenu("Brush");
        ButtonGroup brushBtnGrp = new ButtonGroup();
        for (int i = 0; i < proj.getBrushPack().size(); i++) {
            final Brush b = proj.getBrushPack().get(i);
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(b.getName());
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    currentBrush = b;
                    currentBrush.setTextureColor(foregroundPaint);
                    currentBrush.ensureModifiedTexture();
                }
            });
            brush.add(item);
            brushBtnGrp.add(item);
            if (i == 0) {
                item.setSelected(true);
            }
        }
        bar.add(brush);
        return bar;
    }
}
