package desperateDoctors.userInterface;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import desperateDoctors.visibleObjects.*;
import desperateDoctors.*;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 * Classe g�rant toute l'interface. Contient pas grand chose, mais � la facult� d'afficher beaucoup de choses.
 * @author Nad�ge Barrage
 * @author Jean-Fran�ois Geyelin
 * @author Francis Lim
 * @version 1.0
 * @since 1.0
 */
public class UserInterface2 extends JFrame implements Serializable {

    private transient Image buffer;

    public static int WINDOW_WIDTH = 800;

    public static int WINDOW_HEIGHT = 600;

    private transient Image groundImage;

    private Player p;

    private int tempX;

    private int tempY;

    private Coordinates tempCoord;

    private transient MouseAdapter mouseListener;

    private transient MouseMotionAdapter mouseMotionAdapter;

    private MouseEvent lastMousePressedEvent;

    private MouseEvent lastMousePosition;

    private ArrayList<VisibleObject> lastVisibleObjectList;

    private ArrayList<JFButton> buttonList;

    private ItemPanel ip;

    private Game game;

    public UserInterface2(Game game_) {
        game = game_;
        lastMousePosition = null;
        ip = new ItemPanel();
        buttonList = new ArrayList<JFButton>();
        buttonList.add(new JFButton("A propos", new OpenAboutPopup()));
        buttonList.add(new JFButton("Aide", new OpenHelpPopup()));
        if (Main.isApplication()) {
            buttonList.add(new JFButton("Sauver", new SaveGame(game)));
            buttonList.add(new JFButton("Charger", new LoadGame(game)));
        }
        buttonList.add(new JFButton("Lieu", new PrintLocation()));
        buttonList.add(new JFButton("Retour", new GoBack()));
        buttonList.add(new JFButton("Objets", new OpenItemPanel(ip)));
        int tempY = 150;
        for (JFButton jf : buttonList) {
            jf.setXY(6, tempY);
            tempY += jf.getHeight(this) + 5;
        }
        buffer = null;
        setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        setTitle("DD");
        createBuffer();
        groundImage = null;
        initComponents();
        super.repaint();
    }

    public void createBuffer() {
        buffer = createImage(WINDOW_WIDTH, WINDOW_HEIGHT);
        groundImage = null;
    }

    public void callAfterDeserialization() {
        setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setVisible(true);
        System.out.println("JE REND VISIBLE LA FENETRE");
        initComponents();
        createBuffer();
    }

    public void animateItemPanel() {
        ip.animate();
    }

    public boolean clickOnItemPanel(MouseEvent evt, Player p) {
        return ip.click(evt, p);
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("options");
    }

    private void initComponents() {
        mouseMotionAdapter = new MouseMotionAdapter() {

            public void mouseMoved(MouseEvent evt) {
                lastMousePosition = evt;
            }
        };
        addMouseMotionListener(mouseMotionAdapter);
        mouseListener = new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                boolean buttonWasClicked = false;
                for (JFButton jf : buttonList) {
                    if (jf.mouseIsOver(evt)) {
                        buttonWasClicked = true;
                        jf.performAction();
                    }
                }
                if (buttonWasClicked) evt = null;
                lastMousePressedEvent = evt;
            }
        };
        addMouseListener(mouseListener);
        if (Main.isApplication()) setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public void setGroundImage(Image i) {
        groundImage = i;
    }

    public Image loadImage(String s) {
        System.out.println("j'essaye de charger:" + s + "\n");
        Image tempImage;
        try {
            tempImage = ImageIO.read(getClass().getResource(s));
        } catch (Exception e) {
            return null;
        }
        return TraitementImage.rendreTransparent(tempImage);
    }

    public void drawCase(int x, int y, int n, ArrayList<VisibleObject> array, Graphics graphics) {
        Coordinates c = ConvertCoordinates.convertToScreen(new Coordinates(x, y));
        c.add(tempCoord);
        String s = TileImageClass.getImageName(n);
        if (s != null) {
            Image image;
            image = ImageHashMap.getImage(s);
            if (image != null) graphics.drawImage(image, (int) (c.getX()), (int) (c.getY() + 26 - image.getHeight(this)), this);
        }
        if (array == null) return;
        for (VisibleObject o : array) {
            Image i = o.getImageToDraw();
            if (i != null) {
                Coordinates coord = ConvertCoordinates.convertToScreen(o.getCoord());
                coord.add(new Coordinates((50 - i.getWidth(this)) / 2, 26 - i.getHeight(this)));
                coord.add(tempCoord);
                int xTemp = (int) (coord.getX());
                int yTemp = (int) (coord.getY());
                o.draw(xTemp, yTemp, graphics, this);
            }
        }
    }

    public boolean isInArray(int x, int y, int[][] a) {
        return (!(x < 0 || y < 0 || x >= a.length || y >= a[0].length));
    }

    public void addToObjectArray(VisibleObject vo, Object[][] array) {
        Coordinates c = vo.getCase();
        ArrayList<VisibleObject> a = (ArrayList<VisibleObject>) array[(int) c.getX()][(int) c.getY()];
        if (a == null) {
            array[(int) c.getX()][(int) c.getY()] = (Object) new ArrayList<VisibleObject>(2);
            a = (ArrayList<VisibleObject>) array[(int) c.getX()][(int) c.getY()];
        }
        a.add(vo);
    }

    @Override
    public void paint(Graphics graphics) {
        if (buffer != null && p != null) {
            Graphics bufferGraphics = buffer.getGraphics();
            bufferGraphics.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
            tempCoord = Camera.calculateCoordinates(p);
            int[][] tileArray = p.getCurrentRoom().getRoomData();
            Object[][] objectArray = (new Object[tileArray.length][tileArray[0].length]);
            int i, j;
            for (i = 0; i < objectArray.length; i++) {
                for (j = 0; j < objectArray[0].length; j++) {
                    objectArray[i][j] = null;
                }
            }
            addToObjectArray(p, objectArray);
            Collection<Item> itemList = p.getCurrentRoom().getItemCollection();
            for (Item item : itemList) {
                addToObjectArray(item, objectArray);
            }
            ArrayList<VisibleObject> voArray = p.getCurrentRoom().getVisibleObjects();
            for (VisibleObject vo : voArray) {
                addToObjectArray(vo, objectArray);
            }
            Coordinates tempCoordinates;
            ;
            lastVisibleObjectList = null;
            if (lastMousePosition != null) {
                Coordinates c = new Coordinates(lastMousePosition.getX() - 20, lastMousePosition.getY());
                c.substract(tempCoord);
                c = ConvertCoordinates.convertToIsometric(c);
                int x = (int) c.getX();
                int y = (int) c.getY();
                if (isInArray(x, y, tileArray)) {
                    lastVisibleObjectList = (ArrayList<VisibleObject>) objectArray[x][y];
                    if (objectArray[x][y] != null) {
                        for (VisibleObject vo : lastVisibleObjectList) vo.setHighlighted(true);
                    }
                }
            }
            int dx;
            int dy;
            for (dy = 0; dy < objectArray[0].length; dy++) {
                for (dx = 0; dx < objectArray.length; dx++) {
                    int xCase = dx;
                    int yCase = dy;
                    if (isInArray(xCase, yCase, tileArray)) drawCase(xCase, yCase, tileArray[xCase][yCase], (ArrayList<VisibleObject>) objectArray[xCase][yCase], bufferGraphics);
                }
            }
            if (lastMousePosition != null) {
                for (JFButton jf : buttonList) {
                    jf.draw(bufferGraphics, lastMousePosition, this);
                }
            }
            bufferGraphics.setColor(Color.BLACK);
            bufferGraphics.drawString("temps restant:" + (p.getTimeLeft() / 1000), 20, 50);
            ip.draw(bufferGraphics, p, this);
            if (lastVisibleObjectList != null) {
                for (VisibleObject vo : lastVisibleObjectList) vo.setHighlighted(false);
            }
            if (p.hasLost()) {
                Image image;
                image = ImageHashMap.getImage("perdu.PNG");
                if (image != null) bufferGraphics.drawImage(image, 100, 100, this);
            }
            graphics.drawImage(buffer, 0, 0, this);
        } else {
            System.out.println("meu :");
            super.paint(graphics);
        }
    }

    public void draw(Player p_) {
        p = p_;
        JFButtonAction.setPlayer(p);
        repaint();
    }

    public MouseEvent getLastMouseEvent() {
        MouseEvent e = lastMousePressedEvent;
        lastMousePressedEvent = null;
        return e;
    }

    public ArrayList<VisibleObject> getVisibleObjectArray() {
        return lastVisibleObjectList;
    }

    @Override
    public void dispose() {
        super.dispose();
        for (JFButton jf : buttonList) {
            jf.dispose();
        }
    }
}
