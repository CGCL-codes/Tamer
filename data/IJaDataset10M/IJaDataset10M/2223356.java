package net.sf.isnake.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import net.sf.isnake.core.iSnakeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the resources of User Interface (Images, Buttons Background, etc)
 * and makes it available across all the UI Panels
 *
 * @author  Abhishek Dutta (adutta.np@gmail.com)
 * @version $Id: UIResources.java 146 2008-05-07 02:39:19Z thelinuxmaniac $
 */
public class UIResources {

    private ClassLoader classLoader;

    private JFrame gameFrame;

    private final Logger logger = LoggerFactory.getLogger(UIResources.class);

    private iSnakeConfiguration conf;

    public static String GAME_TITLE = "iSnake - Intelligent Multiplayer Snake";

    /**
     * Images and other resource required for the application
     */
    private static String iSnakeLogoLargePath = "net/sf/isnake/resources/image/iSnakeLogoLarge.png";

    private static String iSnakeLogoSmallPath = "net/sf/isnake/resources/image/iSnakeLogoSmall.png";

    private static String iSnakeLogoReflctPath = "net/sf/isnake/resources/image/ApplicationStartLogo.png";

    private static String iSnakeIconPath = "net/sf/isnake/resources/image/iSnakeIcon.png";

    private Image iSnakeLogoLarge;

    private Image iSnakeLogoSmall;

    private Image iSnakeLogoReflct;

    private Image iSnakeIcon;

    private static String messageBgndPath = "net/sf/isnake/resources/image/MessagePanelBgnd.png";

    private Image messageBgnd;

    private static String menuButtonPath = "net/sf/isnake/resources/image/MainMenuButton.png";

    private static String menuButtonRollOverPath = "net/sf/isnake/resources/image/MainMenuButton_roll.png";

    private static String cuteSnakeGreenPath = "net/sf/isnake/resources/image/CuteSnakeGreen.png";

    private static String cuteSnakeBluePath = "net/sf/isnake/resources/image/CuteSnakeBlue.png";

    private static String cuteSnakePurplePath = "net/sf/isnake/resources/image/CuteSnakePurple.png";

    private static String wallOnGameFieldPath = "net/sf/isnake/resources/image/WallOnGameField3D.png";

    private static String inputFieldBackgroundPath = "net/sf/isnake/resources/image/InputFieldBackground.png";

    private static String gameFieldPath = "net/sf/isnake/resources/image/GameField.png";

    private Image cuteSnakeGreen;

    private Image cuteSnakeBlue;

    private Image cuteSnakePurple;

    private Image wallOnGameField3D;

    private Image inputFieldBackground;

    private Image gameField;

    private static String playerListBgndPath = "net/sf/isnake/resources/image/PlayerListBgnd.png";

    private static String playerListItemBgndPath = "net/sf/isnake/resources/image/PlayerListItemBgnd.png";

    private static String scoreBgndPath = "net/sf/isnake/resources/image/ScoreBgnd.png";

    private static String lifeSnakeIconAlivePath = "net/sf/isnake/resources/image/LifeSnakeIcon_alive.png";

    private static String lifeSnakeIconDeadPath = "net/sf/isnake/resources/image/LifeSnakeIcon_dead.png";

    private static String extraInfoBgndPath = "net/sf/isnake/resources/image/ExtraInfoBgnd.png";

    private static String chatPanelBgndPath = "net/sf/isnake/resources/image/ChatPanelBgnd.png";

    private static String playerReadySignPath = "net/sf/isnake/resources/image/PlayerReadySign.png";

    private Image snakeBlockImage[];

    private static String aboutPanelImagePath = "net/sf/isnake/resources/image/AboutPanelImage.jpg";

    private Image aboutPanelImage;

    private static String errorSignPath = "net/sf/isnake/resources/image/ErrorIcon.png";

    private static String button72x28Path = "net/sf/isnake/resources/image/Button72x28";

    private Image errorSign;

    private Image playerListBgnd;

    private Image playerListItemBgnd;

    private Image scoreBgnd;

    private Image extraInfoBgnd;

    private Image lifeSnakeIconAlive;

    private Image lifeSnakeIconDead;

    private Image chatPanel;

    private Image playerReadySign;

    /**
     * Fonts used in this application
     */
    private static String FontZektonPath = "net/sf/isnake/resources/font/zekton.ttf";

    private static String FontSansPath = "net/sf/isnake/resources/font/LiberationSans-Regular.ttf";

    private static String FontMonoPath = "net/sf/isnake/resources/font/LiberationMono-Regular.ttf";

    private static String FontSerifPath = "net/sf/isnake/resources/font/LiberationSerif-Regular.ttf";

    private Font FontZekton;

    private Font FontSans;

    private Font FontMono;

    private Font FontSerif;

    /**
     * Color Definations
     */
    private Color colorIntroPanelGrad;

    private Color colorTextFieldFocusGained;

    private Color colorTextFieldFocusLost;

    private Color colorLabel;

    private Color colorScoreLabel;

    private Color colorScore;

    private Color strikeOutColor;

    private Color gameFieldBgndColor;

    private Color wallColor;

    private Color currentFoodColor;

    private Color statusMsgBgndColor;

    private Color statusMsgColor;

    private Color statusAnimColor;

    private Color errMsgBgndColor;

    private Color errMsgColor;

    private RenderingHints hintForSpeedProcessing;

    private RenderingHints hintForQualityProcessing;

    private int logoSmallX;

    private int logoSmallY;

    public static final Dimension GAME_FIELD_PAD = new Dimension(0, 0);

    public static final Dimension GAME_FIELD_BLOCK = new Dimension(10, 10);

    /** Creates a new instance of UIResources */
    public UIResources(ClassLoader c, iSnakeConfiguration isc) {
        this.setClassLoader(c);
        this.setConf(isc);
        setLogoSmallX(getConf().getPrefDim().width / 20);
        setLogoSmallY(getConf().getPrefDim().height / 20);
        setColorIntroPanelGrad(new Color(195, 213, 246, 255));
        setColorTextFieldFocusGained(new Color(170, 170, 170));
        setColorTextFieldFocusLost(new Color(192, 192, 192));
        setColorLabel(new Color(255, 204, 0));
        setGameFieldBgndColor(new Color(22, 93, 160));
        setColorScoreLabel(new Color(255, 255, 0));
        setColorScore(Color.WHITE);
        setStrikeOutColor(Color.YELLOW);
        setWallColor(new Color(0, 0, 0));
        setCurrentFoodColor(new Color(0, 255, 255));
        setStatusMsgBgndColor(new Color(255, 204, 0));
        setStatusMsgColor(new Color(0, 0, 0));
        setStatusAnimColor(new Color(255, 255, 255));
        setErrMsgBgndColor(new Color(255, 255, 255));
        setErrMsgColor(new Color(5, 5, 5));
        try {
            setISnakeLogoLarge(ImageIO.read(getClassLoader().getResource(iSnakeLogoLargePath)));
            setISnakeLogoSmall(ImageIO.read(getClassLoader().getResource(iSnakeLogoSmallPath)));
            setISnakeLogoReflct(ImageIO.read(getClassLoader().getResource(iSnakeLogoReflctPath)));
            setISnakeIcon(ImageIO.read(getClassLoader().getResource(iSnakeIconPath)));
            setCuteSnakeGreen(ImageIO.read(getClassLoader().getResource(cuteSnakeGreenPath)));
            setCuteSnakeBlue(ImageIO.read(getClassLoader().getResource(cuteSnakeBluePath)));
            setCuteSnakePurple(ImageIO.read(getClassLoader().getResource(cuteSnakePurplePath)));
            setWallOnGameField3D(ImageIO.read(getClassLoader().getResource(getWallOnGameFieldPath())));
            setInputFieldBackground(ImageIO.read(getClassLoader().getResource(getInputFieldBackgroundPath())));
            setGameField(ImageIO.read(getClassLoader().getResource(getGameFieldPath())));
            setPlayerListBgnd(ImageIO.read(getClassLoader().getResource(playerListBgndPath)));
            setPlayerListItemBgnd(ImageIO.read(getClassLoader().getResource(playerListItemBgndPath)));
            setScoreBgnd(ImageIO.read(getClassLoader().getResource(scoreBgndPath)));
            setLifeSnakeIconAlive(ImageIO.read(getClassLoader().getResource(lifeSnakeIconAlivePath)));
            setLifeSnakeIconDead(ImageIO.read(getClassLoader().getResource(lifeSnakeIconDeadPath)));
            setChatPanel(ImageIO.read(getClassLoader().getResource(chatPanelBgndPath)));
            setPlayerReadySign(ImageIO.read(getClassLoader().getResource(playerReadySignPath)));
            setAboutPanelImage(ImageIO.read(getClassLoader().getResource(aboutPanelImagePath)));
            setErrorSign(ImageIO.read(getClassLoader().getResource(errorSignPath)));
            getLogger().debug("Initialized Image resources");
            try {
                setFontZekton(Font.createFont(Font.TRUETYPE_FONT, getClassLoader().getResource(getFontZektonPath()).openStream()));
                setFontSerif(Font.createFont(Font.TRUETYPE_FONT, getClassLoader().getResource(getFontSerifPath()).openStream()));
                setFontSans(Font.createFont(Font.TRUETYPE_FONT, getClassLoader().getResource(getFontSansPath()).openStream()));
                setFontMono(Font.createFont(Font.TRUETYPE_FONT, getClassLoader().getResource(getFontMonoPath()).openStream()));
                getLogger().debug("Initialized Font resources");
            } catch (FontFormatException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        setHintForSpeedProcessing(new RenderingHints(null));
        getHintForSpeedProcessing().put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        getHintForSpeedProcessing().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        setHintForQualityProcessing(new RenderingHints(null));
        getHintForQualityProcessing().put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        getHintForQualityProcessing().put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public static String getISnakeLogoLargePath() {
        return iSnakeLogoLargePath;
    }

    public static String getISnakeLogoSmallPath() {
        return iSnakeLogoSmallPath;
    }

    public static String getISnakeLogoReflctPath() {
        return iSnakeLogoReflctPath;
    }

    public Image getISnakeLogoLarge() {
        return iSnakeLogoLarge;
    }

    public void setISnakeLogoLarge(Image iSnakeLogoLarge) {
        this.iSnakeLogoLarge = iSnakeLogoLarge;
    }

    public Image getISnakeLogoSmall() {
        return iSnakeLogoSmall;
    }

    public void setISnakeLogoSmall(Image iSnakeLogoSmall) {
        this.iSnakeLogoSmall = iSnakeLogoSmall;
    }

    public Image getISnakeLogoReflct() {
        return iSnakeLogoReflct;
    }

    public void setISnakeLogoReflct(Image iSnakeLogoReflct) {
        this.iSnakeLogoReflct = iSnakeLogoReflct;
    }

    public static String getFontZektonPath() {
        return FontZektonPath;
    }

    public static String getFontSansPath() {
        return FontSansPath;
    }

    public static String getFontMonoPath() {
        return FontMonoPath;
    }

    public static String getFontSerifPath() {
        return FontSerifPath;
    }

    public Font getFontZekton() {
        return FontZekton;
    }

    public void setFontZekton(Font FontZekton) {
        this.FontZekton = FontZekton;
    }

    public Font getFontSans() {
        return FontSans;
    }

    public void setFontSans(Font FontSans) {
        this.FontSans = FontSans;
    }

    public Font getFontMono() {
        return FontMono;
    }

    public void setFontMono(Font FontMono) {
        this.FontMono = FontMono;
    }

    public Font getFontSerif() {
        return FontSerif;
    }

    public void setFontSerif(Font FontSerif) {
        this.FontSerif = FontSerif;
    }

    public Logger getLogger() {
        return logger;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public RenderingHints getHintForSpeedProcessing() {
        return hintForSpeedProcessing;
    }

    public void setHintForSpeedProcessing(RenderingHints hintForSpeedProcessing) {
        this.hintForSpeedProcessing = hintForSpeedProcessing;
    }

    public RenderingHints getHintForQualityProcessing() {
        return hintForQualityProcessing;
    }

    public void setHintForQualityProcessing(RenderingHints hintForQualityProcessing) {
        this.hintForQualityProcessing = hintForQualityProcessing;
    }

    public Color getColorIntroPanelGrad() {
        return colorIntroPanelGrad;
    }

    public void setColorIntroPanelGrad(Color colorIntroPanelGrad) {
        this.colorIntroPanelGrad = colorIntroPanelGrad;
    }

    public static String getMenuButtonPath() {
        return menuButtonPath;
    }

    public static String getMenuButtonRollOverPath() {
        return menuButtonRollOverPath;
    }

    public int getLogoSmallX() {
        return logoSmallX;
    }

    public void setLogoSmallX(int logoSmallX) {
        this.logoSmallX = logoSmallX;
    }

    public int getLogoSmallY() {
        return logoSmallY;
    }

    public void setLogoSmallY(int logoSmallY) {
        this.logoSmallY = logoSmallY;
    }

    public iSnakeConfiguration getConf() {
        return conf;
    }

    public void setConf(iSnakeConfiguration conf) {
        this.conf = conf;
    }

    public static String getCuteSnakeGreenPath() {
        return cuteSnakeGreenPath;
    }

    public static void setCuteSnakeGreenPath(String aCuteSnakeGreenPath) {
        cuteSnakeGreenPath = aCuteSnakeGreenPath;
    }

    public static String getCuteSnakeBluePath() {
        return cuteSnakeBluePath;
    }

    public static void setCuteSnakeBluePath(String aCuteSnakeBluePath) {
        cuteSnakeBluePath = aCuteSnakeBluePath;
    }

    public static String getCuteSnakePurplePath() {
        return cuteSnakePurplePath;
    }

    public static void setCuteSnakePurplePath(String aCuteSnakePurplePath) {
        cuteSnakePurplePath = aCuteSnakePurplePath;
    }

    public static String getWallOnGameFieldPath() {
        return wallOnGameFieldPath;
    }

    public static void setWallOnGameFieldPath(String aWallOnGameFieldPath) {
        wallOnGameFieldPath = aWallOnGameFieldPath;
    }

    public static String getInputFieldBackgroundPath() {
        return inputFieldBackgroundPath;
    }

    public static void setInputFieldBackgroundPath(String aInputFieldBackgroundPath) {
        inputFieldBackgroundPath = aInputFieldBackgroundPath;
    }

    public static void setMenuButtonPath(String aMenuButtonPath) {
        menuButtonPath = aMenuButtonPath;
    }

    public Image getCuteSnakeGreen() {
        return cuteSnakeGreen;
    }

    public void setCuteSnakeGreen(Image cuteSnakeGreen) {
        this.cuteSnakeGreen = cuteSnakeGreen;
    }

    public Image getCuteSnakeBlue() {
        return cuteSnakeBlue;
    }

    public void setCuteSnakeBlue(Image cuteSnakeBlue) {
        this.cuteSnakeBlue = cuteSnakeBlue;
    }

    public Image getCuteSnakePurple() {
        return cuteSnakePurple;
    }

    public void setCuteSnakePurple(Image cuteSnakePurple) {
        this.cuteSnakePurple = cuteSnakePurple;
    }

    public Image getWallOnGameField3D() {
        return wallOnGameField3D;
    }

    public void setWallOnGameField3D(Image wallOnGameField3D) {
        this.wallOnGameField3D = wallOnGameField3D;
    }

    public Image getInputFieldBackground() {
        return inputFieldBackground;
    }

    public void setInputFieldBackground(Image inputFieldBackground) {
        this.inputFieldBackground = inputFieldBackground;
    }

    public Color getColorTextFieldFocusGained() {
        return colorTextFieldFocusGained;
    }

    public void setColorTextFieldFocusGained(Color colorTextFieldFocusGained) {
        this.colorTextFieldFocusGained = colorTextFieldFocusGained;
    }

    public Color getColorTextFieldFocusLost() {
        return colorTextFieldFocusLost;
    }

    public void setColorTextFieldFocusLost(Color colorTextFieldFocusLost) {
        this.colorTextFieldFocusLost = colorTextFieldFocusLost;
    }

    public Color getColorLabel() {
        return colorLabel;
    }

    public void setColorLabel(Color colorLabel) {
        this.colorLabel = colorLabel;
    }

    public Image getGameField() {
        return gameField;
    }

    public void setGameField(Image gameField) {
        this.gameField = gameField;
    }

    public static String getGameFieldPath() {
        return gameFieldPath;
    }

    public static void setGameFieldPath(String aGameFieldPath) {
        gameFieldPath = aGameFieldPath;
    }

    public Image getPlayerListBgnd() {
        return playerListBgnd;
    }

    public void setPlayerListBgnd(Image playerListBgnd) {
        this.playerListBgnd = playerListBgnd;
    }

    public Image getPlayerListItemBgnd() {
        return playerListItemBgnd;
    }

    public void setPlayerListItemBgnd(Image playerListItemBgnd) {
        this.playerListItemBgnd = playerListItemBgnd;
    }

    public static String getPlayerListBgndPath() {
        return playerListBgndPath;
    }

    public static void setPlayerListBgndPath(String aPlayerListBgndPath) {
        playerListBgndPath = aPlayerListBgndPath;
    }

    public static String getPlayerListItemBgndPath() {
        return playerListItemBgndPath;
    }

    public static void setPlayerListItemBgndPath(String aPlayerListItemBgndPath) {
        playerListItemBgndPath = aPlayerListItemBgndPath;
    }

    public Image getScoreBgnd() {
        return scoreBgnd;
    }

    public void setScoreBgnd(Image scoreBgnd) {
        this.scoreBgnd = scoreBgnd;
    }

    public static String getScoreBgndPath() {
        return scoreBgndPath;
    }

    public static void setScoreBgndPath(String aScoreBgndPath) {
        scoreBgndPath = aScoreBgndPath;
    }

    public Image getExtraInfoBgnd() {
        return extraInfoBgnd;
    }

    public void setExtraInfoBgnd(Image extraInfoBgnd) {
        this.extraInfoBgnd = extraInfoBgnd;
    }

    public static String getExtraInfoBgndPath() {
        return extraInfoBgndPath;
    }

    public static void setExtraInfoBgndPath(String aExtraInfoBgndPath) {
        extraInfoBgndPath = aExtraInfoBgndPath;
    }

    public Color getColorScoreLabel() {
        return colorScoreLabel;
    }

    public void setColorScoreLabel(Color colorScoreLabel) {
        this.colorScoreLabel = colorScoreLabel;
    }

    public Color getColorScore() {
        return colorScore;
    }

    public void setColorScore(Color colorScore) {
        this.colorScore = colorScore;
    }

    public Image getLifeSnakeIconAlive() {
        return lifeSnakeIconAlive;
    }

    public void setLifeSnakeIconAlive(Image lifeSnakeIconAlive) {
        this.lifeSnakeIconAlive = lifeSnakeIconAlive;
    }

    public Image getLifeSnakeIconDead() {
        return lifeSnakeIconDead;
    }

    public void setLifeSnakeIconDead(Image lifeSnakeIconDead) {
        this.lifeSnakeIconDead = lifeSnakeIconDead;
    }

    public static String getLifeSnakeIconAlivePath() {
        return lifeSnakeIconAlivePath;
    }

    public static void setLifeSnakeIconAlivePath(String aLifeSnakeIconAlivePath) {
        lifeSnakeIconAlivePath = aLifeSnakeIconAlivePath;
    }

    public static String getLifeSnakeIconDeadPath() {
        return lifeSnakeIconDeadPath;
    }

    public static void setLifeSnakeIconDeadPath(String aLifeSnakeIconDeadPath) {
        lifeSnakeIconDeadPath = aLifeSnakeIconDeadPath;
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public void setGameFrame(JFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public Image getChatPanel() {
        return chatPanel;
    }

    public void setChatPanel(Image chatPanel) {
        this.chatPanel = chatPanel;
    }

    public Color getGameFieldBgndColor() {
        return gameFieldBgndColor;
    }

    public void setGameFieldBgndColor(Color gameFieldBgndColor) {
        this.gameFieldBgndColor = gameFieldBgndColor;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public void setWallColor(Color wallColor) {
        this.wallColor = wallColor;
    }

    public Color getCurrentFoodColor() {
        return currentFoodColor;
    }

    public void setCurrentFoodColor(Color currentFoodColor) {
        this.currentFoodColor = currentFoodColor;
    }

    public Image getPlayerReadySign() {
        return playerReadySign;
    }

    public void setPlayerReadySign(Image playerReadySign) {
        this.playerReadySign = playerReadySign;
    }

    public Image getMessageBgnd() {
        return messageBgnd;
    }

    public void setMessageBgnd(Image messageBgnd) {
        this.messageBgnd = messageBgnd;
    }

    public Color getStatusMsgBgndColor() {
        return statusMsgBgndColor;
    }

    public void setStatusMsgBgndColor(Color statusMsgBgndColor) {
        this.statusMsgBgndColor = statusMsgBgndColor;
    }

    public Color getStatusMsgColor() {
        return statusMsgColor;
    }

    public void setStatusMsgColor(Color statusMsgColor) {
        this.statusMsgColor = statusMsgColor;
    }

    public Color getErrMsgBgndColor() {
        return errMsgBgndColor;
    }

    public void setErrMsgBgndColor(Color errMsgBgndColor) {
        this.errMsgBgndColor = errMsgBgndColor;
    }

    public Color getErrMsgColor() {
        return errMsgColor;
    }

    public void setErrMsgColor(Color errMsgColor) {
        this.errMsgColor = errMsgColor;
    }

    public Color getStatusAnimColor() {
        return statusAnimColor;
    }

    public void setStatusAnimColor(Color statusAnimColor) {
        this.statusAnimColor = statusAnimColor;
    }

    public Image getErrorSign() {
        return errorSign;
    }

    public void setErrorSign(Image errorSign) {
        this.errorSign = errorSign;
    }

    public static String getButton72x28Path() {
        return button72x28Path;
    }

    public Color getStrikeOutColor() {
        return strikeOutColor;
    }

    public void setStrikeOutColor(Color strikeOutColor) {
        this.strikeOutColor = strikeOutColor;
    }

    public Image getAboutPanelImage() {
        return aboutPanelImage;
    }

    public void setAboutPanelImage(Image aboutPanelImage) {
        this.aboutPanelImage = aboutPanelImage;
    }

    public Image getISnakeIcon() {
        return iSnakeIcon;
    }

    public void setISnakeIcon(Image iSnakeIcon) {
        this.iSnakeIcon = iSnakeIcon;
    }
}
