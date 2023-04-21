package org.mazix.screen;

import static java.util.logging.Level.SEVERE;
import static org.mazix.constants.CharacterConstants.LEFT_BRACKET_CHAR;
import static org.mazix.constants.CharacterConstants.RIGHT_BRACKET_CHAR;
import static org.mazix.constants.FileConstants.CONFIG_PATH;
import static org.mazix.constants.FileConstants.OPTIONS_FILE;
import static org.mazix.constants.FileConstants.PROFILES_FILE;
import static org.mazix.constants.FileConstants.XML_EXTENSION;
import static org.mazix.constants.FileConstants.XSD_EXTENSION;
import static org.mazix.constants.GlobalConstants.BLANK_STRING;
import static org.mazix.constants.GlobalConstants.X_DEFAULT;
import static org.mazix.constants.GlobalConstants.Y_DEFAULT;
import static org.mazix.constants.GlobalConstants.Z_DEFAULT;
import static org.mazix.constants.LanguageConstants.CONTRIB_LEVELS_LANG;
import static org.mazix.constants.LanguageConstants.OFFICIAL_LEVELS_LANG;
import static org.mazix.constants.ScreenConstants.GAME_VERSION_BUTTON;
import static org.mazix.constants.ScreenConstants.MAIN_MENU_BUTTONS;
import static org.mazix.constants.ScreenConstants.MAIN_TITLE_BUTTON;
import static org.mazix.constants.ScreenConstants.PROFILE_BUTTON_INDEX_MAIN_SCREEN;
import static org.mazix.constants.XMLConstants.DEFAULT_XML_IMPLEMENTATION;
import static org.mazix.constants.log.ErrorConstants.WRITE_OPTIONS_ERROR;
import static org.mazix.constants.log.ErrorConstants.WRITE_PROFILES_ERROR;
import static org.mazix.constants.log.ErrorConstants.XML_INIT_ERROR;
import static org.mazix.constants.log.InfoConstants.CLOSING_LOG_FILE_INFO;
import static org.mazix.constants.log.InfoConstants.INITIALIZING_XML_WRITERS_INFO;
import static org.mazix.constants.log.InfoConstants.QUITTING_GAME_INFO;
import static org.mazix.constants.log.InfoConstants.WRITING_OPTIONS_FILE_INFO;
import static org.mazix.constants.log.InfoConstants.WRITING_PROFILES_FILE_INFO;
import static org.mazix.constants.log.WarningConstants.NOT_FOUND_MENU_WARNING;
import java.util.Map;
import java.util.logging.Logger;
import javax.vecmath.Vector3f;
import javax.xml.parsers.ParserConfigurationException;
import org.mazix.camera3D.GameCamera;
import org.mazix.component3D.GraphicButton3D;
import org.mazix.kernel.GameData;
import org.mazix.kernel.GameOptions;
import org.mazix.kernel.Profile;
import org.mazix.log.LogUtils;
import org.mazix.xml.impl.XMLFactory;

/**
 * Class representing the main screen.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.2
 * @version 0.7
 */
public class MainScreen extends ScreenAbstract {

    /** The class logger. */
    private static final Logger LOGGER = Logger.getLogger("org.mazix.screen.MainScreen");

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
     * @see #MainScreen(Vector3f, GameData, GameCamera)
     */
    public MainScreen(final GameData gameData, final GameCamera gameCamera) {
        this(new Vector3f(X_DEFAULT, Y_DEFAULT, Z_DEFAULT), gameData, gameCamera);
    }

    /**
     * Full constructor. Sets the screen position to default. May throw a {@code
     * NullPointerException} if {@code position} or {@code GameData} are <code>null</code>.
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
     * @see #MainScreen(GameData, GameCamera)
     */
    public MainScreen(final Vector3f position, final GameData gameData, final GameCamera gameCamera) {
        super(position, gameData, gameCamera, translateMenuButton(MAIN_TITLE_BUTTON, gameData), GAME_VERSION_BUTTON.clone(), new GraphicButton3D(BLANK_STRING, 0, -12, 0), translateListOfMenuButton(MAIN_MENU_BUTTONS, gameData));
        getMenusButton().get(PROFILE_BUTTON_INDEX_MAIN_SCREEN).setLabelValue(LEFT_BRACKET_CHAR + getData().getOptions().getCurrentProfile() + RIGHT_BRACKET_CHAR);
    }

    /**
     * @see org.mazix.screen.ScreenAbstract#pressEnterKeyEvent()
     * @since 0.2
     */
    @Override
    public ScreenAbstract pressEnterKeyEvent() {
        assert getMenusButton() != null : "getMenusButton() is null";
        assert getData() != null : "getData() is null";
        assert getCamera() != null : "getCamera() is null";
        ScreenAbstract outcome = null;
        if (getMenusButton().getCurrentButtonIndex() == 0) {
            outcome = new LevelsScreen(getData(), getCamera(), getData().getOfficialSeries(), OFFICIAL_LEVELS_LANG);
        } else if (getMenusButton().getCurrentButtonIndex() == 1) {
            outcome = new LevelsScreen(getData(), getCamera(), getData().getContribSeries(), CONTRIB_LEVELS_LANG);
        } else if (getMenusButton().getCurrentButtonIndex() == 2) {
            outcome = new ProfileScreen(getData(), getCamera());
        } else if (getMenusButton().getCurrentButtonIndex() == 3) {
            outcome = new OptionsScreen(getData(), getCamera());
        } else if (getMenusButton().getCurrentButtonIndex() == 4) {
        } else if (getMenusButton().getCurrentButtonIndex() == 5) {
            outcome = new CreditsScreen(getData(), getCamera());
        } else if (getMenusButton().getCurrentButtonIndex() == 6) {
        } else if (getMenusButton().getCurrentButtonIndex() == 7) {
            quitGame();
        } else {
            LOGGER.warning(LogUtils.buildLogString(NOT_FOUND_MENU_WARNING, new Object[] { this.getClass(), getMenusButton().getCurrentButtonIndex() }));
        }
        return outcome;
    }

    /**
     * @see org.mazix.screen.ScreenAbstract#pressEscapeKeyEvent()
     * @since 0.2
     */
    @Override
    public ScreenAbstract pressEscapeKeyEvent() {
        quitGame();
        return null;
    }

    /**
     * This method manages to properly close the game. Basically, it manages to write modified
     * options and profiles, but also the game's log.
     * 
     * @since 0.4
     */
    private void quitGame() {
        assert getData() != null : "getData() is null";
        try {
            LOGGER.info(INITIALIZING_XML_WRITERS_INFO);
            final XMLFactory xmlFactory = XMLFactory.getXMLFactoryFromType(DEFAULT_XML_IMPLEMENTATION);
            LOGGER.info(WRITING_OPTIONS_FILE_INFO);
            writeGameOptions(getData().getOptions(), xmlFactory);
            LOGGER.info(WRITING_PROFILES_FILE_INFO);
            writeProfiles(getData().getProfiles(), xmlFactory);
        } catch (final ParserConfigurationException pce) {
            LOGGER.log(SEVERE, XML_INIT_ERROR, pce);
        }
        LOGGER.info(CLOSING_LOG_FILE_INFO);
        LogUtils.closeAllLogHandlers();
        LOGGER.info(QUITTING_GAME_INFO);
        System.exit(0);
    }

    /**
     * Writes and saves a {@link GameOptions} instance in a XML options file with the passed
     * {@link XMLFactory}.
     * 
     * @param the
     *            {@link GameOptions} instance to be saved, can't be <code>null</code>.
     * @param xmlFactory
     *            the {@link XMLFactory} instance to write the XML file, must'nt be <code>null</code>.
     * @since 0.7
     */
    private void writeGameOptions(final GameOptions gameOptions, final XMLFactory xmlFactory) {
        assert gameOptions != null : "gameOptions is null";
        assert xmlFactory != null : "xmlFactory is null";
        try {
            xmlFactory.writeGameOptions(gameOptions, CONFIG_PATH + OPTIONS_FILE + XML_EXTENSION, OPTIONS_FILE + XSD_EXTENSION);
        } catch (final Exception e) {
            LOGGER.log(SEVERE, WRITE_OPTIONS_ERROR, e);
        }
    }

    /**
     * Writes and saves a {@link GameOptions} instance in a XML options file with the passed
     * {@link XMLFactory}.
     * 
     * @param profiles
     *            the {@code Map} instance to be saved, it contains profile names as keys and {@code Profile} as values, must'nt be <code>null</code>.
     * @param xmlFactory
     *            the {@link XMLFactory} instance to write the XML file, must'nt be <code>null</code>.
     * @since 0.7
     */
    private void writeProfiles(final Map<String, Profile> profiles, final XMLFactory xmlFactory) {
        assert profiles != null : "profiles is null";
        assert xmlFactory != null : "xmlFactory is null";
        try {
            xmlFactory.writeProfiles(profiles, CONFIG_PATH + PROFILES_FILE + XML_EXTENSION, PROFILES_FILE + XSD_EXTENSION);
        } catch (final Exception e) {
            LOGGER.log(SEVERE, WRITE_PROFILES_ERROR, e);
        }
    }
}
