package net.sourceforge.mazix.persistence.dao.impl.bundle.language;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static net.sourceforge.mazix.components.constants.CharacterConstants.UNDERSCORE_CHAR;
import static net.sourceforge.mazix.components.constants.CommonFileConstants.PROPERTIES_EXTENSION;
import static net.sourceforge.mazix.components.utils.file.FileUtils.writeIntoFile;
import static net.sourceforge.mazix.persistence.constants.FileConstants.LANGUAGE_PATH;
import static net.sourceforge.mazix.persistence.constants.LanguageConstants.SOUND_EFFECT_VOLUME_LANG;
import static net.sourceforge.mazix.persistence.constants.LanguageConstants.TIME_OVER_LANG;
import static net.sourceforge.mazix.persistence.constants.LanguageTestConstants.BUNDLE_LANGUAGE_DAO;
import static net.sourceforge.mazix.persistence.constants.LanguageTestConstants.LANGUAGE_PROPERTIES_TEST_FILE;
import static org.junit.Assert.assertEquals;
import java.io.File;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dao.language.LanguageDAOTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test classes for {@link BundleLanguageDAO}.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public class BundleLanguageDAOTest implements LanguageDAOTest {

    /**
     * This method is called before each test to reset the profiles temporary XML file content.
     * 
     * @since 0.8
     */
    @Before
    public void initializeFiles() {
        writeIntoFile("#Created by JInto - www.guh-software.de\r\n" + "#Thu Feb 19 21:21:08 GMT 2009\r\n" + "game_name = Mazix\r\n" + "official_levels = Official levels\r\n" + "contrib_levels = Contrib levels\r\n" + "profile = Profiles\r\n" + "options = Options\r\n" + "help = Help\r\n" + "credits = Credits\r\n" + "level_editor = Level editor\r\n" + "quit = Quit\r\n" + "new_profile = New profile\r\n" + "select_profile = Select profile\r\n" + "modify_profile = Modify profile\r\n" + "delete_profile = Delete profile\r\n" + "back = Back\r\n" + "ok = OK\r\n" + "name = Name\r\n" + "profile_info = Profile info\r\n" + "reset_progressions = Reset all levels progression\r\n" + "last_build = Last build\r\n" + "web_site = Web site\r\n" + "original_author = Original author\r\n" + "java_version = Java version\r\n" + "licence = Licence\r\n" + "language = Language\r\n" + "sound_effect_volume = Effects volume\r\n" + "sound_music_volume = Music volume\r\n" + "screen_resolution = Screen resolution\r\n" + "not_enough_profile = Can't delete profile (mustn't be the only one)\r\n" + "new_profile_already_exists = Can't create profile (already exists)\r\n" + "modify_profile_already_exists = Can't modify profile (already exists)\r\n" + "empty_profile_name = The profile name is empty\r\n" + "modify_profile_name = Modify profile name\r\n" + "restart_game_exists = Restart game to take changes into account\r\n" + "series = Series\r\n" + "level = Level\r\n" + "time = Time\r\n" + "moves = Moves\r\n" + "series_finished = Congratulation you have finished this series\r\n" + "level_finished_next_unblocked = Congralutation you have finished this level and unblocked the next one\r\n" + "level_finished = Congratulation you have finished this level\r\n" + "time_over = Time is over, game finished", BUNDLE_LANGUAGE_DAO.getFilePath() + LANGUAGE_PROPERTIES_TEST_FILE + PROPERTIES_EXTENSION);
        writeIntoFile("#Created by JInto - www.guh-software.de\r\n" + "#Thu Feb 19 21:21:08 GMT 2009\r\n" + "game_name = Mazix\r\n" + "official_levels = Official levels\r\n" + "contrib_levels = Contrib levels\r\n" + "profile = Profiles\r\n" + "options = Options\r\n" + "help = Help\r\n" + "credits = Credits\r\n" + "level_editor = Level editor\r\n" + "quit = Quit\r\n" + "new_profile = New profile\r\n" + "select_profile = Select profile\r\n" + "modify_profile = Modify profile\r\n" + "delete_profile = Delete profile\r\n" + "back = Back\r\n" + "ok = OK\r\n" + "name = Name\r\n" + "profile_info = Profile info\r\n" + "reset_progressions = Reset all levels progression\r\n" + "last_build = Last build\r\n" + "web_site = Web site\r\n" + "original_author = Original author\r\n" + "java_version = Java version\r\n" + "licence = Licence\r\n" + "language = Language\r\n" + "sound_effect_volume = Effects volume\r\n" + "sound_music_volume = Music volume\r\n" + "screen_resolution = Screen resolution\r\n" + "not_enough_profile = Can't delete profile (mustn't be the only one)\r\n" + "new_profile_already_exists = Can't create profile (already exists)\r\n" + "modify_profile_already_exists = Can't modify profile (already exists)\r\n" + "empty_profile_name = The profile name is empty\r\n" + "modify_profile_name = Modify profile name\r\n" + "restart_game_exists = Restart game to take changes into account\r\n" + "series = Series\r\n" + "level = Level\r\n" + "time = Time\r\n" + "moves = Moves\r\n" + "series_finished = Congratulation you have finished this series\r\n" + "level_finished_next_unblocked = Congralutation you have finished this level and unblocked the next one\r\n" + "level_finished = Congratulation you have finished this level\r\n" + "time_over = Time over, press a key to restart", BUNDLE_LANGUAGE_DAO.getFilePath() + LANGUAGE_PROPERTIES_TEST_FILE + UNDERSCORE_CHAR + ENGLISH + PROPERTIES_EXTENSION);
        writeIntoFile("#Created by JInto - www.guh-software.de\r\n" + "#Thu Feb 19 21:21:08 GMT 2009\r\n" + "game_name = Mazix\r\n" + "official_levels = Niveaux officiels\r\n" + "contrib_levels = Niveaux supplémentaires\r\n" + "profile = Profils\r\n" + "options = Options\r\n" + "help = Aide\r\n" + "credits = Credits\r\n" + "level_editor = Editeur de niveaux\r\n" + "quit = Quitter\r\n" + "new_profile = Nouveau profil\r\n" + "select_profile = Sélectionner profil\r\n" + "modify_profile = Modifier profil\r\n" + "delete_profile = Supprimer profil\r\n" + "back = Retour\r\n" + "ok = OK\r\n" + "name = Nom\r\n" + "profile_info = Info profil\r\n" + "reset_progressions = Réinitialiser progressions\r\n" + "last_build = Dernière build\r\n" + "web_site = Site web\r\n" + "original_author = Auteur original\r\n" + "java_version = Version java\r\n" + "licence = Licence\r\n" + "language = Langage\r\n" + "sound_effect_volume = Volume des effets\r\n" + "sound_music_volume = Volume de la musique\r\n" + "screen_resolution = Résolution de l'écran\r\n" + "not_enough_profile = Ne peut pas supprimer le profil (ne doit pas être le seul)\r\n" + "new_profile_already_exists = Ne peut pas créer le profil (déjà existant)\r\n" + "modify_profile_already_exists = Ne peut pas modifier le profil (already exists)\r\n" + "empty_profile_name = Le nom du profil est vide\r\n" + "modify_profile_name = Modifier le nom d'un profil\r\n" + "restart_game_exists = Relancer le jeu pour prendre en compte les modifications\r\n" + "series = Série\r\n" + "level = Niveau\r\n" + "time = Temps\r\n" + "moves = Déplacements\r\n" + "series_finished = Félicitation vous avez terminé la série\r\n" + "level_finished_next_unblocked = Félicitation vous avez terminé le niveau et débloquer le suivant\r\n" + "level_finished = Félicitation vous avez terminé le niveau", LANGUAGE_PATH + LANGUAGE_PROPERTIES_TEST_FILE + UNDERSCORE_CHAR + FRENCH + PROPERTIES_EXTENSION);
    }

    /**
     * This method is called after all tests, it is used to remove the temporary file used for the
     * tests.
     * 
     * @since 0.8
     */
    @After
    public void removeLanguageTestFile() {
        final File defaultLanguageTestFile = new File(BUNDLE_LANGUAGE_DAO.getFilePath() + LANGUAGE_PROPERTIES_TEST_FILE + PROPERTIES_EXTENSION);
        defaultLanguageTestFile.delete();
        final File englishLanguageTestFile = new File(BUNDLE_LANGUAGE_DAO.getFilePath() + LANGUAGE_PROPERTIES_TEST_FILE + UNDERSCORE_CHAR + ENGLISH + PROPERTIES_EXTENSION);
        englishLanguageTestFile.delete();
        final File frenchLanguageTestFile = new File(BUNDLE_LANGUAGE_DAO.getFilePath() + LANGUAGE_PROPERTIES_TEST_FILE + UNDERSCORE_CHAR + FRENCH + PROPERTIES_EXTENSION);
        frenchLanguageTestFile.delete();
    }

    /**
     * @see net.sourceforge.mazix.persistence.dao.language.LanguageDAOTest#testGetTranslationWithExistingKeyWithDefaultLocale()
     * @since 0.8
     */
    @Test
    @Override
    public void testGetTranslationWithExistingKeyWithDefaultLocale() throws PersistenceException {
        final String translation = BUNDLE_LANGUAGE_DAO.getTranslation(SOUND_EFFECT_VOLUME_LANG);
        assertEquals("Effects volume", translation);
    }

    /**
     * @see net.sourceforge.mazix.persistence.dao.language.LanguageDAOTest#testGetTranslationWithExistingKeyWithLocale()
     * @since 0.8
     */
    @Test
    @Override
    public void testGetTranslationWithExistingKeyWithLocale() throws PersistenceException {
        final String translation = BUNDLE_LANGUAGE_DAO.getTranslation(FRENCH, SOUND_EFFECT_VOLUME_LANG);
        assertEquals("Volume des effets", translation);
    }

    /**
     * @see net.sourceforge.mazix.persistence.dao.language.LanguageDAOTest#testGetTranslationWithNotExistingKey()
     * @since 0.8
     */
    @Test(expected = PersistenceException.class)
    @Override
    public void testGetTranslationWithNotExistingKey() throws PersistenceException {
        BUNDLE_LANGUAGE_DAO.getTranslation("welcome_mazix");
    }

    /**
     * @see net.sourceforge.mazix.persistence.dao.language.LanguageDAOTest#testGetTranslationWithNotExistingKeyWithLocale()
     * @since 0.8
     */
    @Test
    @Override
    public void testGetTranslationWithNotExistingKeyWithLocale() throws PersistenceException {
        final String translation = BUNDLE_LANGUAGE_DAO.getTranslation(FRENCH, TIME_OVER_LANG);
        assertEquals("Time is over, game finished", translation);
    }
}
