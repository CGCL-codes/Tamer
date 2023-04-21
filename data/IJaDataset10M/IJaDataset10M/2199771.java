package net.sourceforge.mazix.ui.screen.credits;

import static net.sourceforge.mazix.components.constants.CommonConstants.BLANK_STRING;
import static net.sourceforge.mazix.components.constants.GraphicConstants.X_DEFAULT;
import static net.sourceforge.mazix.components.constants.GraphicConstants.Y_DEFAULT;
import static net.sourceforge.mazix.components.constants.GraphicConstants.Z_DEFAULT;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.AUTHOR_BUTTON;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.CREDITS_MENU_BUTTONS;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.CREDITS_TITLE_BUTTON;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.DATE_BUTTON;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.GAME_VERSION_BUTTON;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.JAVA_VERSION_BUTTON;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.LICENCE_BUTTON;
import static net.sourceforge.mazix.ui.constants.ScreenConstants.WEBSITE_BUTTON;
import javax.media.j3d.BranchGroup;
import javax.vecmath.Vector3f;
import net.sourceforge.mazix.uicomponents.graphic.labels.buttons.AbstractGraphicButton;
import net.sourceforge.mazix.uicomponents.graphic.labels.buttons.GraphicButton3D;
import net.sourceforge.mazix.core.kernel.GameData;
import net.sourceforge.mazix.ui.camera3D.GameCamera;
import net.sourceforge.mazix.ui.screen.AbstractScreen;
import net.sourceforge.mazix.ui.screen.main.MainScreen;

/**
 * Class representing the credits screen.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.2
 * @version 0.7
 */
public class CreditsScreen extends AbstractScreen {

    /** The game last build button. */
    private AbstractGraphicButton gameVersionDateButton = null;

    /** The author button. */
    private AbstractGraphicButton authorButton = null;

    /** The web site button. */
    private AbstractGraphicButton webSiteButton = null;

    /** The java version button. */
    private AbstractGraphicButton javaVersionButton = null;

    /** The license button. */
    private AbstractGraphicButton licenceButton = null;

    /**
     * Default constructor. Sets the screen position to default. May throw a {@code
     * NullPointerException} if {@code gameData} is <code>null</code>.
     * 
     * @param gameData
     *            the {@code GameData} instance containing all necessary data of the game, can't be
     *            <code>null</code>.
     * @param gameCamera
     *            the screen {@code GameCamera} which contains the observer view, can't be
     *            <code>null</code>.
     * @since 0.2
     * @see #CreditsScreen(Vector3f, GameData, GameCamera)
     */
    public CreditsScreen(final GameData gameData, final GameCamera gameCamera) {
        this(new Vector3f(X_DEFAULT, Y_DEFAULT, Z_DEFAULT), gameData, gameCamera);
    }

    /**
     * Full constructor. Sets the screen position to default. May throw a {@code
     * NullPointerException} if {@code position} or {@code gameData} is <code>null</code>.
     * 
     * @param position
     *            the {@code Vector3f} containing the 3D position of the screen, can't be
     *            <code>null</code>.
     * @param gameData
     *            the {@code GameData} instance containing all necessary data of the game, can't be
     *            <code>null</code>.
     * @param gameCamera
     *            the screen {@code GameCamera} which contains the observer view, can't be
     *            <code>null</code>.
     * @since 0.2
     * @see #CreditsScreen(GameData, GameCamera)
     */
    public CreditsScreen(final Vector3f position, final GameData gameData, final GameCamera gameCamera) {
        super(position, gameData, gameCamera, translateMenuButton(CREDITS_TITLE_BUTTON, gameData), GAME_VERSION_BUTTON.clone(), new GraphicButton3D(BLANK_STRING, 0, -12, 0), translateListOfMenuButton(CREDITS_MENU_BUTTONS, gameData));
        setGameVersionDateButton(DATE_BUTTON.clone());
        getGameVersionDateButton().setLabelText(gameData.getTranslation(getGameVersionDateButton().getLabelText()));
        setAuthorButton(AUTHOR_BUTTON.clone());
        getAuthorButton().setLabelText(gameData.getTranslation(getAuthorButton().getLabelText()));
        setWebSiteButton(WEBSITE_BUTTON.clone());
        getWebSiteButton().setLabelText(gameData.getTranslation(getWebSiteButton().getLabelText()));
        setJavaVersionButton(JAVA_VERSION_BUTTON.clone());
        getJavaVersionButton().setLabelText(gameData.getTranslation(getJavaVersionButton().getLabelText()));
        setLicenceButton(LICENCE_BUTTON.clone());
        getLicenceButton().setLabelText(gameData.getTranslation(getLicenceButton().getLabelText()));
    }

    /**
     * @see org.AbstractScreen.screen.ScreenAbstract#buildGraphicShape()
     * @since 0.7
     */
    @Override
    protected BranchGroup buildGraphicShape() {
        assert getGameVersionDateButton() != null : "getGameVersionDateButton() is null";
        assert getAuthorButton() != null : "getAuthorButton() is null";
        assert getWebSiteButton() != null : "getWebSiteButton() is null";
        assert getJavaVersionButton() != null : "getJavaVersionButton() is null";
        assert getLicenceButton() != null : "getLicenceButton() is null";
        final BranchGroup shape = super.buildGraphicShape();
        shape.addChild(getGameVersionDateButton().getCoreShape());
        shape.addChild(getAuthorButton().getCoreShape());
        shape.addChild(getWebSiteButton().getCoreShape());
        shape.addChild(getJavaVersionButton().getCoreShape());
        shape.addChild(getLicenceButton().getCoreShape());
        return shape;
    }

    /**
     * Gets the value of authorButton.
     * 
     * @return the value of authorButton.
     * @see #setAuthorButton(AbstractGraphicButton)
     * @since 0.7
     */
    private AbstractGraphicButton getAuthorButton() {
        return authorButton;
    }

    /**
     * Gets the value of gameVersionDateButton.
     * 
     * @return the value of gameVersionDateButton.
     * @see #setGameVersionDateButton(AbstractGraphicButton)
     * @since 0.7
     */
    private AbstractGraphicButton getGameVersionDateButton() {
        return gameVersionDateButton;
    }

    /**
     * Gets the value of javaVersionButton.
     * 
     * @return the value of javaVersionButton.
     * @see #setJavaVersionButton(AbstractGraphicButton)
     * @since 0.7
     */
    private AbstractGraphicButton getJavaVersionButton() {
        return javaVersionButton;
    }

    /**
     * Gets the value of licenceButton.
     * 
     * @return the value of licenceButton.
     * @see #setLicenceButton(AbstractGraphicButton)
     * @since 0.7
     */
    private AbstractGraphicButton getLicenceButton() {
        return licenceButton;
    }

    /**
     * Gets the value of webSiteButton.
     * 
     * @return the value of webSiteButton.
     * @see #setWebSiteButton(AbstractGraphicButton)
     * @since 0.7
     */
    private AbstractGraphicButton getWebSiteButton() {
        return webSiteButton;
    }

    /**
     * @see org.AbstractScreen.screen.ScreenAbstract#pressEnterKeyEvent()
     * @since 0.2
     */
    @Override
    public AbstractScreen pressEnterKeyEvent() {
        assert getMenusButton() != null : "getMenusButton() is null";
        return getMenusButton().getCurrentButtonIndex() == 0 ? new MainScreen(getData(), getCamera()) : null;
    }

    /**
     * @see org.AbstractScreen.screen.ScreenAbstract#pressEscapeKeyEvent()
     * @since 0.2
     */
    @Override
    public AbstractScreen pressEscapeKeyEvent() {
        return new MainScreen(getData(), getCamera());
    }

    /**
     * Sets the value of authorButton.
     * 
     * @param value
     *            the authorButton to set, can be <code>null</code>.
     * @see #getAuthorButton()
     * @since 0.7
     */
    private void setAuthorButton(final AbstractGraphicButton value) {
        authorButton = value;
    }

    /**
     * Sets the value of gameVersionDateButton.
     * 
     * @param value
     *            the gameVersionDateButton to set, can be <code>null</code>.
     * @see #getGameVersionDateButton()
     * @since 0.7
     */
    private void setGameVersionDateButton(final AbstractGraphicButton value) {
        gameVersionDateButton = value;
    }

    /**
     * Sets the value of javaVersionButton.
     * 
     * @param value
     *            the javaVersionButton to set, can be <code>null</code>.
     * @see #getJavaVersionButton()
     * @since 0.7
     */
    private void setJavaVersionButton(final AbstractGraphicButton value) {
        javaVersionButton = value;
    }

    /**
     * Sets the value of licenceButton.
     * 
     * @param value
     *            the licenceButton to set, can be <code>null</code>.
     * @see #getLicenceButton()
     * @since 0.7
     */
    private void setLicenceButton(final AbstractGraphicButton value) {
        licenceButton = value;
    }

    /**
     * Sets the value of webSiteButton.
     * 
     * @param value
     *            the webSiteButton to set, can be <code>null</code>.
     * @see #getWebSiteButton()
     * @since 0.7
     */
    private void setWebSiteButton(final AbstractGraphicButton value) {
        webSiteButton = value;
    }
}
