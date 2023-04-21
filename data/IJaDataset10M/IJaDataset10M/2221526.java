package mdes.slick.sui.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import mdes.slick.sui.Sui;
import mdes.slick.sui.Button;
import mdes.slick.sui.CheckBox;
import mdes.slick.sui.Container;
import mdes.slick.sui.Display;
import mdes.slick.sui.Label;
import mdes.slick.sui.Theme;
import mdes.slick.sui.Frame;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import mdes.slick.sui.skin.simple.SimpleSkin;
import mdes.slick.sui.theme.BitterLemonTheme;
import mdes.slick.sui.theme.CopperTheme;
import mdes.slick.sui.theme.SteelAcidTheme;
import mdes.slick.sui.theme.SteelBlueTheme;
import mdes.slick.sui.theme.SteelSepiaTheme;
import mdes.slick.sui.theme.XMLThemeIO;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

/**
 *
 * @author davedes
 */
public class ThemeDump extends BasicGame {

    public static void main(String[] args) throws Exception {
        AppGameContainer app = new AppGameContainer(new ThemeDump());
        app.setDisplayMode(800, 600, false);
        app.start();
    }

    /** Creates a new instance of ThemeDump */
    public ThemeDump() {
        super("ThemeDump");
    }

    public static final int COLORS = 17;

    private Label[] labels = new Label[COLORS];

    private Label[] methodNames = new Label[COLORS];

    private Color[] colors = new Color[COLORS];

    private String[] methods;

    private ArrayList themes = new ArrayList();

    private Label nameLabel;

    private Display display;

    private int currentIndex = 0;

    private Button left, right;

    private Frame demoBox = null;

    private void populateThemes() {
        themes.add(new SteelBlueTheme());
        themes.add(new SteelSepiaTheme());
        themes.add(new SteelAcidTheme());
        themes.add(new BitterLemonTheme());
        themes.add(new CopperTheme());
        try {
            themes.add(XMLThemeIO.read("res/steelBlue.xml"));
        } catch (Exception e) {
        }
    }

    public void init(GameContainer container) throws SlickException {
        display = new Display(container);
        display.setSendingGlobalEvents(false);
        display.setFocusable(true);
        Method[] m = Theme.class.getMethods();
        Arrays.sort(m, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Method) o1).getName().compareTo(((Method) o2).getName());
            }
        });
        ArrayList list = new ArrayList(COLORS);
        for (int i = 0; i < m.length; i++) {
            String nameStr = m[i].getName();
            if (nameStr.indexOf("getName") == -1) list.add(nameStr);
        }
        methods = (String[]) list.toArray(new String[list.size()]);
        class BorderLabel extends Label {

            public void renderComponent(GUIContext c, Graphics g) {
                super.renderComponent(c, g);
                g.setColor(Color.darkGray);
                g.draw(this.getAbsoluteBounds());
            }
        }
        ;
        float startx = 280;
        float starty = 70;
        Container parent = display;
        for (int i = 0; i < labels.length; i++) {
            methodNames[i] = new BorderLabel();
            methodNames[i].setText(methods[i]);
            methodNames[i].setSize(155, 25);
            methodNames[i].setOpaque(true);
            methodNames[i].setLocation(startx, starty + i * (methodNames[i].getHeight() + 5));
            parent.add(methodNames[i]);
            labels[i] = new BorderLabel();
            labels[i].setOpaque(true);
            labels[i].setSize(65, 25);
            labels[i].setLocation(startx + methodNames[i].getWidth() + 5, starty + i * (labels[i].getHeight() + 5));
            parent.add(labels[i]);
        }
        Container panel = new Container();
        left = new Button("Previous");
        left.pack();
        left.setRequestFocusEnabled(false);
        left.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    setTheme((Theme) themes.get(currentIndex));
                    updateButtons();
                }
            }
        });
        panel.add(left);
        right = new Button("Next");
        right.pack();
        right.setRequestFocusEnabled(false);
        right.setX(left.getWidth() + 5);
        right.setHeight(left.getHeight());
        right.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (currentIndex < themes.size() - 1) {
                    currentIndex++;
                    setTheme((Theme) themes.get(currentIndex));
                    updateButtons();
                }
            }
        });
        panel.add(right);
        panel.setSize(right.getX() + right.getWidth(), left.getHeight());
        panel.setLocationRelativeTo(display);
        panel.setY(starty - panel.getHeight() - 5);
        display.add(panel);
        nameLabel = new Label("No themes found.");
        nameLabel.pack();
        nameLabel.setLocationRelativeTo(display);
        nameLabel.setY(panel.getY() - nameLabel.getHeight() - 5);
        display.add(nameLabel);
        final CheckBox box = new CheckBox("Show Demo Box");
        demoBox = new DemoFrame();
        demoBox.setLocation(50, 100);
        demoBox.getCloseButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                box.setSelected(false);
            }
        });
        display.add(demoBox);
        box.pack();
        box.setX(demoBox.getX());
        box.setY(demoBox.getY() - box.getHeight() - 5);
        box.setSelected(true);
        box.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                demoBox.setVisible(box.isSelected());
            }
        });
        box.setRequestFocusEnabled(false);
        display.add(box);
        if (Sui.getSkin() instanceof SimpleSkin) {
            final CheckBox roundBox = new CheckBox("Round Rectangles");
            roundBox.pack();
            roundBox.setSelected(true);
            SimpleSkin.setRoundRectanglesEnabled(roundBox.isSelected());
            roundBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SimpleSkin.setRoundRectanglesEnabled(roundBox.isSelected());
                }
            });
            box.setWidth(Math.max(roundBox.getWidth(), box.getWidth()));
            roundBox.setWidth(Math.max(roundBox.getWidth(), box.getWidth()));
            roundBox.setLocation(box.getX(), box.getY() - roundBox.getHeight() - 5);
            roundBox.setRequestFocusEnabled(false);
            display.add(roundBox);
        }
        populateThemes();
        if (!themes.isEmpty()) {
            setTheme((Theme) themes.get(0));
        }
        updateButtons();
    }

    private void setTheme(Theme theme) {
        if (theme == null) return;
        Sui.setTheme(theme);
        colors[0] = theme.getActiveTitleBar1();
        colors[1] = theme.getActiveTitleBar2();
        colors[2] = theme.getBackground();
        colors[3] = theme.getDisabledMask();
        colors[4] = theme.getForeground();
        colors[5] = theme.getPrimary1();
        colors[6] = theme.getPrimary2();
        colors[7] = theme.getPrimary3();
        colors[8] = theme.getPrimaryBorder1();
        colors[9] = theme.getPrimaryBorder2();
        colors[10] = theme.getSecondary1();
        colors[11] = theme.getSecondary2();
        colors[12] = theme.getSecondary3();
        colors[13] = theme.getSecondaryBorder1();
        colors[14] = theme.getSecondaryBorder2();
        colors[15] = theme.getTitleBar1();
        colors[16] = theme.getTitleBar2();
        for (int i = 0; i < labels.length; i++) {
            Color c = colors[i];
            labels[i].setBackground(new Color(c.r, c.g, c.b, c.a));
            methodNames[i].setBackground(theme.getBackground());
            methodNames[i].setForeground(theme.getForeground());
        }
        nameLabel.setForeground(theme.getForeground());
        nameLabel.setText(theme.getName());
        Sui.updateComponentTreeTheme(display);
    }

    public void updateButtons() {
        left.setEnabled(currentIndex != 0 && !themes.isEmpty());
        right.setEnabled(currentIndex < themes.size() - 1 && !themes.isEmpty());
    }

    public void update(GameContainer container, int delta) throws SlickException {
        display.update(container, delta);
        if (container.getInput().isKeyPressed(Input.KEY_ESCAPE)) container.exit();
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        g.setBackground(Color.lightGray);
        display.render(container, g);
    }
}
