package net.sf.isnake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ListIterator;
import javax.imageio.ImageIO;
import net.sf.isnake.core.GameController;
import net.sf.isnake.core.GameFieldCoordinate;
import net.sf.isnake.core.GameFieldMatrix;
import net.sf.isnake.core.PlayerData;
import net.sf.isnake.core.Snake;
import net.sf.isnake.core.iSnakeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameFieldCanvas extends java.awt.Canvas {

    private final Logger logger = LoggerFactory.getLogger(UIResources.class);

    private UIResources uiResources;

    private iSnakeConfiguration conf;

    private GameController gameController;

    private GameFieldMatrix gameFieldMatrix;

    private BufferStrategy bufferStrategy;

    private Color snakeColor[];

    private Image foodImage, wallImage, nullImage;

    private Image snakeImage[][][];

    public static Integer CELL_OBJECT_HEAD0 = 0;

    public static Integer CELL_OBJECT_HEAD1 = 1;

    public static Integer CELL_OBJECT_BODY = 2;

    public static Integer CELL_OBJECT_BODY_CORNER = 3;

    public static Integer CELL_OBJECT_TAIL0 = 4;

    public static Integer CELL_OBJECT_COUNT = 5;

    /** Creates a new instance of GameFieldCanvas */
    public GameFieldCanvas(GameController gc, GameFieldMatrix gfm, iSnakeConfiguration isc, UIResources uir) {
        this.setBounds(0, 0, 580, 580);
        this.setPreferredSize(new Dimension(580, 580));
        this.setMinimumSize(new Dimension(580, 580));
        snakeColor = new Color[iSnakeConfiguration.MAX_PLAYER_COUNT + 1];
        setConf(isc);
        setUiResources(uir);
        setGameController(gc);
        setGameFieldMatrix(gfm);
        setBackground(getUiResources().getGameFieldBgndColor());
        addKeyListener(getGameController().getInputHandler());
        addKeyListener(getGameController().getDummyInputHandler());
        snakeImage = new BufferedImage[9][][];
        for (int i = 0; i < 9; i++) {
            snakeImage[i] = new BufferedImage[CELL_OBJECT_COUNT][];
        }
        for (int k = 1; k < 9; k++) {
            for (int j = 0; j < CELL_OBJECT_COUNT; j++) {
                snakeImage[k][j] = new BufferedImage[4];
            }
        }
        GraphicsConfiguration gconf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image sourceImage;
        System.out.print("Loading game field snake images ... ");
        for (int j = 1; j < 9; j++) {
            for (int i = 0; i < 4; i++) {
                try {
                    sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/gamefield/snake" + (j) + "/Snake" + (j) + "Head0" + (i + 1) + ".png"));
                    snakeImage[j][CELL_OBJECT_HEAD0][i] = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
                    snakeImage[j][CELL_OBJECT_HEAD0][i].getGraphics().drawImage(sourceImage, 0, 0, null);
                    sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/gamefield/snake" + (j) + "/Snake" + (j) + "Head1" + (i + 1) + ".png"));
                    snakeImage[j][CELL_OBJECT_HEAD1][i] = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
                    snakeImage[j][CELL_OBJECT_HEAD1][i].getGraphics().drawImage(sourceImage, 0, 0, null);
                    sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/gamefield/snake" + (j) + "/Snake" + (j) + "Body" + (i + 1) + ".png"));
                    snakeImage[j][CELL_OBJECT_BODY][i] = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
                    snakeImage[j][CELL_OBJECT_BODY][i].getGraphics().drawImage(sourceImage, 0, 0, null);
                    sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/gamefield/snake" + (j) + "/Snake" + (j) + "BodyCorner" + (i + 1) + ".png"));
                    snakeImage[j][CELL_OBJECT_BODY_CORNER][i] = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
                    snakeImage[j][CELL_OBJECT_BODY_CORNER][i].getGraphics().drawImage(sourceImage, 0, 0, null);
                    sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/gamefield/snake" + (j) + "/Snake" + (j) + "Tail0" + (i + 1) + ".png"));
                    snakeImage[j][CELL_OBJECT_TAIL0][i] = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
                    snakeImage[j][CELL_OBJECT_TAIL0][i].getGraphics().drawImage(sourceImage, 0, 0, null);
                } catch (IOException ioe) {
                    System.out.println("Failed !\n");
                }
            }
        }
        try {
            sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/FoodBlock.png"));
            foodImage = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
            foodImage.getGraphics().drawImage(sourceImage, 0, 0, null);
            sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/WallBlock.png"));
            wallImage = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
            wallImage.getGraphics().drawImage(sourceImage, 0, 0, null);
            sourceImage = ImageIO.read(getUiResources().getClassLoader().getResource("net/sf/isnake/resources/image/gamefield/NullObject.png"));
            nullImage = gconf.createCompatibleImage(sourceImage.getWidth(null), sourceImage.getHeight(null), Transparency.OPAQUE);
            nullImage.getGraphics().drawImage(sourceImage, 0, 0, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.print("Finished\n");
        getLogger().debug("Initialized Image resources");
    }

    public void transformLocalPlayerImages() {
        int localPlayerId = getGameController().getLocalPlayer().getPlayerId();
        Graphics g = snakeImage[localPlayerId][CELL_OBJECT_BODY][Snake.CELL_OREN_RIGHT - 1].getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawRect(1, 4, 1, 1);
        g2.drawRect(4, 4, 1, 1);
        g2.drawRect(7, 4, 1, 1);
        g = snakeImage[localPlayerId][CELL_OBJECT_BODY][Snake.CELL_OREN_DOWN - 1].getGraphics();
        g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawRect(4, 1, 1, 1);
        g2.drawRect(4, 4, 1, 1);
        g2.drawRect(4, 7, 1, 1);
        g = snakeImage[localPlayerId][CELL_OBJECT_BODY][Snake.CELL_OREN_LEFT - 1].getGraphics();
        g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawRect(2, 4, 1, 1);
        g2.drawRect(5, 4, 1, 1);
        g2.drawRect(8, 4, 1, 1);
        g = snakeImage[localPlayerId][CELL_OBJECT_BODY][Snake.CELL_OREN_UP - 1].getGraphics();
        g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.drawRect(4, 2, 1, 1);
        g2.drawRect(4, 5, 1, 1);
        g2.drawRect(4, 8, 1, 1);
    }

    public void updateGameFieldCanvas() {
        bufferStrategy = this.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        renderGameField(g);
        bufferStrategy.show();
    }

    /**
     * Render's the contents of game field
     * Complete the animation in 5 cycles
     */
    private void renderGameField(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        GameFieldCoordinate coordinate = new GameFieldCoordinate();
        Short gameFieldObject = new Short("0");
        Integer playerId = 0;
        Short snakeObjectId = (short) 0;
        Integer cellObjectOrientation = Snake.CELL_OREN_NONE;
        boolean foodPainted = false;
        for (int i = 0; i < GameFieldMatrix.GAME_FIELD_HEIGHT; i++) {
            for (int j = 0; j < GameFieldMatrix.GAME_FIELD_WIDTH; j++) {
                coordinate.set(i, j);
                cellObjectOrientation = Snake.CELL_OREN_NONE;
                gameFieldObject = gameFieldMatrix.getGameFieldObject(coordinate);
                if (gameFieldObject >= GameFieldMatrix.LOWER_LIMIT_SNAKE && gameFieldObject <= GameFieldMatrix.UPPER_LIMIT_SNAKE) {
                    playerId = (gameFieldObject - (gameFieldObject % 100)) / 100;
                    snakeObjectId = (short) (gameFieldObject % 100);
                    if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_HEAD0_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_HEAD0_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_HEAD0_LL;
                        drawHead0(g2, playerId, coordinate, cellObjectOrientation);
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_HEAD1_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_HEAD1_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_HEAD1_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                            }
                            drawHead1Corner(g2, playerId, coordinate, cellObjectOrientation);
                        } else {
                            drawHead1(g2, playerId, coordinate, cellObjectOrientation);
                        }
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_BODY_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_BODY_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_BODY_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                            }
                            drawBodyCorner(g2, playerId, coordinate, cellObjectOrientation);
                        } else {
                            drawBody(g2, playerId, coordinate, cellObjectOrientation);
                        }
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_TAIL0_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_TAIL0_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_TAIL0_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                                cellObjectOrientation = GameFieldMatrix.tailOrentation[cellObjectOrientation][0];
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                                cellObjectOrientation = GameFieldMatrix.tailOrentation[cellObjectOrientation][1];
                            }
                        } else {
                            drawTail0(g2, playerId, coordinate, cellObjectOrientation);
                        }
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_TAIL1_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_TAIL1_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_TAIL1_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                            }
                            drawBodyCorner(g2, playerId, coordinate, cellObjectOrientation);
                        } else {
                            drawBody(g2, playerId, coordinate, cellObjectOrientation);
                        }
                    }
                } else if (gameFieldObject == GameFieldMatrix.OBJECT_WALL) {
                    drawObjectWall(g2, coordinate);
                } else if (gameFieldObject == GameFieldMatrix.OBJECT_CFOOD) {
                    drawCurrentFood(g2, coordinate);
                    foodPainted = true;
                } else if (gameFieldObject == GameFieldMatrix.OBJECT_NULL) {
                    drawObjectNull(g2, coordinate);
                }
            }
        }
        if (!foodPainted) {
            gameFieldMatrix.setCurrentFood(gameController.getCurrentFood());
            drawCurrentFood(g2, gameController.getCurrentFood());
        }
    }

    /**
     * Initialize the snake color array for each player's snake
     */
    public void initSnakeColor() {
        for (ListIterator<PlayerData> itr = getGameController().getGamePlayers().listIterator(); itr.hasNext(); ) {
            PlayerData pd = itr.next();
            if (pd.getPlayerId() != 0) {
                snakeColor[pd.getPlayerId()] = pd.getSnakeColor();
            }
        }
    }

    private void drawHead0(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_HEAD0][cellOrientation - 1], x, y, null);
        }
    }

    private void drawHead1(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_HEAD1][cellOrientation - 1], x, y, null);
        }
    }

    private void drawHead1Corner(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY_CORNER][cellOrientation - 1], x, y, null);
        }
    }

    private void drawBody(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY][cellOrientation - 1], x, y, null);
        }
    }

    private void drawBodyCorner(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY_CORNER][cellOrientation - 1], x, y, null);
        }
    }

    private void drawTail0(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_TAIL0][cellOrientation - 1], x, y, null);
        }
    }

    private void drawTail0Corner(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_TAIL0][cellOrientation - 1], x, y, null);
        }
    }

    private void drawTail1(Graphics2D g2, Integer playerId, GameFieldCoordinate coordinate, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!getBufferStrategy().contentsLost()) {
            g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY][cellOrientation - 1], x, y, null);
        }
    }

    private void drawObjectWall(Graphics2D g2, GameFieldCoordinate coordinate) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!bufferStrategy.contentsLost()) {
            g2.drawImage(wallImage, x, y, null);
        }
    }

    private void drawCurrentFood(Graphics2D g2, GameFieldCoordinate coordinate) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!bufferStrategy.contentsLost()) {
            g2.drawImage(foodImage, x, y, null);
        }
    }

    private void drawObjectNull(Graphics2D g2, GameFieldCoordinate coordinate) {
        int x = UIResources.GAME_FIELD_PAD.width + coordinate.getX() * 10;
        int y = UIResources.GAME_FIELD_PAD.height + coordinate.getY() * 10;
        if (!bufferStrategy.contentsLost()) {
            g2.drawImage(nullImage, x, y, null);
        }
    }

    /**************************************************************************
     *  Optimization STARTS
     **************************************************************************/
    public void updateGameFieldCanvas_light() {
        bufferStrategy = this.getBufferStrategy();
        Graphics g = bufferStrategy.getDrawGraphics();
        renderGameField_light(g);
        bufferStrategy.show();
    }

    /**
     * Render's the contents of game field
     * Complete the animation in 5 cycles
     */
    private void renderGameField_light(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        GameFieldCoordinate coordinate = new GameFieldCoordinate();
        Short gameFieldObject = new Short("0");
        Integer playerId = 0;
        Short snakeObjectId = (short) 0;
        Integer cellObjectOrientation = Snake.CELL_OREN_NONE;
        boolean foodPainted = false;
        for (byte i = 0; i < GameFieldMatrix.GAME_FIELD_HEIGHT; i++) {
            for (byte j = 0; j < GameFieldMatrix.GAME_FIELD_WIDTH; j++) {
                cellObjectOrientation = Snake.CELL_OREN_NONE;
                gameFieldObject = gameFieldMatrix.getGameFieldObject(i, j);
                if (gameFieldObject >= GameFieldMatrix.LOWER_LIMIT_SNAKE && gameFieldObject <= GameFieldMatrix.UPPER_LIMIT_SNAKE) {
                    playerId = (gameFieldObject - (gameFieldObject % 100)) / 100;
                    snakeObjectId = (short) (gameFieldObject % 100);
                    if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_HEAD0_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_HEAD0_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_HEAD0_LL;
                        drawHead0(g2, playerId, i, j, cellObjectOrientation);
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_HEAD1_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_HEAD1_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_HEAD1_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                            }
                            drawHead1Corner(g2, playerId, i, j, cellObjectOrientation);
                        } else {
                            drawHead1(g2, playerId, i, j, cellObjectOrientation);
                        }
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_BODY_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_BODY_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_BODY_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                            }
                            drawBodyCorner(g2, playerId, coordinate, cellObjectOrientation);
                        } else {
                            drawBody(g2, playerId, i, j, cellObjectOrientation);
                        }
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_TAIL0_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_TAIL0_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_TAIL0_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                                cellObjectOrientation = GameFieldMatrix.tailOrentation[cellObjectOrientation][0];
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                                cellObjectOrientation = GameFieldMatrix.tailOrentation[cellObjectOrientation][1];
                            }
                        } else {
                            drawTail0(g2, playerId, i, j, cellObjectOrientation);
                        }
                    } else if (snakeObjectId >= GameFieldMatrix.INDEX_SNAKE_TAIL1_LL && snakeObjectId <= GameFieldMatrix.INDEX_SNAKE_TAIL1_UL) {
                        cellObjectOrientation = snakeObjectId - GameFieldMatrix.INDEX_SNAKE_TAIL1_LL;
                        if (cellObjectOrientation > 4) {
                            if (cellObjectOrientation > 10) {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_LARGER;
                            } else {
                                cellObjectOrientation = cellObjectOrientation - Snake.CELL_CORNER_SMALLER;
                            }
                            drawBodyCorner(g2, playerId, i, j, cellObjectOrientation);
                        } else {
                            drawBody(g2, playerId, i, j, cellObjectOrientation);
                        }
                    }
                } else if (gameFieldObject == GameFieldMatrix.OBJECT_WALL) {
                    drawObjectWall(g2, coordinate);
                } else if (gameFieldObject == GameFieldMatrix.OBJECT_CFOOD) {
                    drawCurrentFood(g2, i, j);
                    foodPainted = true;
                } else if (gameFieldObject == GameFieldMatrix.OBJECT_NULL) {
                    drawObjectNull(g2, i, j);
                }
            }
        }
        if (!foodPainted) {
            gameFieldMatrix.setCurrentFood(gameController.getCurrentFood());
            drawCurrentFood(g2, gameController.getCurrentFood());
        }
    }

    private void drawHead0(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_HEAD0][cellOrientation - 1], x, y, null);
    }

    private void drawHead1(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_HEAD1][cellOrientation - 1], x, y, null);
    }

    private void drawHead1Corner(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY_CORNER][cellOrientation - 1], x, y, null);
    }

    private void drawBody(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY][cellOrientation - 1], x, y, null);
    }

    private void drawBodyCorner(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY_CORNER][cellOrientation - 1], x, y, null);
    }

    private void drawTail0(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_TAIL0][cellOrientation - 1], x, y, null);
    }

    private void drawTail0Corner(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_TAIL0][cellOrientation - 1], x, y, null);
    }

    private void drawTail1(Graphics2D g2, Integer playerId, byte i, byte j, Integer cellOrientation) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(snakeImage[playerId][CELL_OBJECT_BODY][cellOrientation - 1], x, y, null);
    }

    private void drawObjectWall(Graphics2D g2, byte i, byte j) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(wallImage, x, y, null);
    }

    private void drawCurrentFood(Graphics2D g2, byte i, byte j) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.drawImage(foodImage, x, y, null);
    }

    private void drawObjectNull(Graphics2D g2, byte i, byte j) {
        int x = UIResources.GAME_FIELD_PAD.width + i * 10;
        int y = UIResources.GAME_FIELD_PAD.height + j * 10;
        g2.setColor(getUiResources().getGameFieldBgndColor());
        g2.fillRect(x, y, UIResources.GAME_FIELD_BLOCK.width, UIResources.GAME_FIELD_BLOCK.height);
    }

    /**************************************************************************
     *  Optimization ENDS
     **************************************************************************/
    public UIResources getUiResources() {
        return uiResources;
    }

    public void setUiResources(UIResources uiResources) {
        this.uiResources = uiResources;
    }

    public iSnakeConfiguration getConf() {
        return conf;
    }

    public void setConf(iSnakeConfiguration conf) {
        this.conf = conf;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameFieldMatrix getGameFieldMatrix() {
        return gameFieldMatrix;
    }

    public void setGameFieldMatrix(GameFieldMatrix gameFieldMatrix) {
        this.gameFieldMatrix = gameFieldMatrix;
    }

    public Logger getLogger() {
        return logger;
    }
}
