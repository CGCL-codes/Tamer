package net.sourceforge.mazix.core.levels.impl.persistence;

import java.util.Map;
import net.sourceforge.mazix.components.exception.LogicException;
import net.sourceforge.mazix.components.locale.RestrictedLocale;
import net.sourceforge.mazix.core.levels.Level;
import net.sourceforge.mazix.core.levels.LevelSeries;
import net.sourceforge.mazix.core.translations.Translation;

/**
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public class LevelSeriesPersistenceImpl implements LevelSeries {

    /** Serial version UID. */
    private static final long serialVersionUID = -8239595311392878610L;

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void addLevel(final Level level) throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void addTranslation(final Translation translation) throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public int compareTo(final LevelSeries o) {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void createLevelSeries() throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public LevelSeries deepClone() {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteLevel(final int levelId) throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteLevels() throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteLevelSeries() throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteTranslation(final RestrictedLocale locale) throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteTranslations() throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Level getLevel(final Integer levelId) throws LogicException {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Map<Integer, Level> getLevels() throws LogicException {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public int getLevelsSize() throws LogicException {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public String getName() throws LogicException {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Translation getTranslation(final RestrictedLocale locale) throws LogicException {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Map<RestrictedLocale, Translation> getTranslations() throws LogicException {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public int getTranslationsSize() throws LogicException {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void save() throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void setName(final String value) throws LogicException {
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void updateLevelSeriesName(final String newLevelSeriesName) throws LogicException {
    }
}
