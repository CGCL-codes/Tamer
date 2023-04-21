package net.sourceforge.mazix.core.business.impl.memory.levels;

import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static net.sourceforge.mazix.components.constants.log.ErrorConstants.UNEXPECTED_ERROR;
import static net.sourceforge.mazix.components.utils.comparable.ComparableResult.AFTER;
import static net.sourceforge.mazix.components.utils.comparable.ComparableResult.EQUAL;
import static net.sourceforge.mazix.components.utils.string.StringUtils.isEmpty;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_EMPTY;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_FINDALL;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_FINDALL_ID_ALREADY_EXIST;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_SERIES_EMPTY;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_SERIES_NAME_EMPTY;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_SERIES_SAVE;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_SERIES_TRANSLATION_FINDALL;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_SERIES_TRANSLATION_FINDALL_LOCALE_ALREADY_EXIST;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_LEVEL_SERIES_UPDATE;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_PROCESS_DAO_CORE_IMPL_EMPTY;
import static net.sourceforge.mazix.core.constants.message.MessageErrorConstants.ERROR_PROGRESSION_LEVEL_ID_NOT_SUPPORTED;
import static net.sourceforge.mazix.persistence.constants.DAOConstants.DEFAULT_DAO_CORE_IMPLEMENTATION;
import static net.sourceforge.mazix.persistence.constants.LevelConstants.MAX_COLUMNS;
import static net.sourceforge.mazix.persistence.constants.LevelConstants.MAX_LINES;
import static net.sourceforge.mazix.persistence.constants.LevelConstants.MIN_COLUMNS;
import static net.sourceforge.mazix.persistence.constants.LevelConstants.MIN_LINES;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import net.sourceforge.mazix.components.exception.LogicException;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.components.integer.IntegerWithinInterval;
import net.sourceforge.mazix.components.integer.PositiveInteger;
import net.sourceforge.mazix.components.locale.RestrictedLocale;
import net.sourceforge.mazix.core.business.impl.BusinessType;
import net.sourceforge.mazix.core.business.levels.Level;
import net.sourceforge.mazix.core.business.levels.LevelSeries;
import net.sourceforge.mazix.core.business.levels.time.impl.SecondTimeImpl;
import net.sourceforge.mazix.core.saveable.Saveable;
import net.sourceforge.mazix.core.translations.Translation;
import net.sourceforge.mazix.core.translations.WithTranslations;
import net.sourceforge.mazix.core.translations.impl.TranslationImpl;
import net.sourceforge.mazix.core.translations.impl.WithTranslationsHelper;
import net.sourceforge.mazix.persistence.dao.DAOCoreType;
import net.sourceforge.mazix.persistence.dto.levels.LevelDTO;
import net.sourceforge.mazix.persistence.dto.levels.LevelSeriesDTO;
import net.sourceforge.mazix.persistence.dto.levels.LevelSeriesTranslationDTO;

/**
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * 
 * @since 0.8
 * @version 0.8
 */
public class LevelSeriesMemoryImpl implements LevelSeries {

    /** Serial version UID. */
    private static final long serialVersionUID = 3170807389643464301L;

    /** The class logger. */
    private static final transient Logger LOGGER = getLogger(LevelSeriesMemoryImpl.class.getName());

    /** The level series DTO instance to communicate with the persistence layer. */
    private LevelSeriesDTO levelSeriesDTO = null;

    /** The {link DAOCoreType} instance to perform persistence operations. */
    private DAOCoreType daoCoreType = null;

    /** The {link BusinessType} instance to get business implementation. */
    private BusinessType businessType = null;

    /** The {@code Map} which stores a level id associated to the level instance. */
    private Map<PositiveInteger, Level> levels = null;

    /** The helper to implement the {@link WithTranslations} interface. */
    private WithTranslationsHelper withTranslationHelper = null;

    /**
     * Default constructor.
     * 
     * @throws LogicException
     * @since 0.8
     */
    private LevelSeriesMemoryImpl(final LevelSeriesDTO levelSeries, final DAOCoreType daoType) throws LogicException {
        if (levelSeries == null) {
            throw new LogicException(ERROR_LEVEL_SERIES_EMPTY);
        } else {
            if (daoType == null) {
                throw new LogicException(ERROR_PROCESS_DAO_CORE_IMPL_EMPTY);
            } else {
                levelSeriesDTO = levelSeries;
                daoCoreType = daoType;
            }
        }
    }

    /**
     * Gets the value of <code>businessType</code>.
     * 
     * @return the value of <code>businessType</code>.
     * @since 0.8
     */
    private BusinessType getBusinessType() {
        return businessType;
    }

    /**
     * Default constructor.
     * 
     * @param name
     *            the profile name, mustn't be <code>null</code> nor empty.
     * @throws LogicException
     * @since 0.8
     */
    public LevelSeriesMemoryImpl(final String name) throws LogicException {
        this(new LevelSeriesDTO(), DEFAULT_DAO_CORE_IMPLEMENTATION);
        setName(name);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void addLevel(final Level level) throws LogicException {
        if (level == null) {
            throw new LogicException(ERROR_LEVEL_EMPTY);
        } else {
            getLevels().put(level.getId(), level);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void addTranslation(final Translation translation) throws LogicException {
        getWithTranslationHelper().addTranslation(translation);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public int compareTo(final LevelSeries l) {
        int comparison = AFTER.getResult();
        try {
            if (this == l) {
                comparison = EQUAL.getResult();
            } else {
                comparison = getName().compareTo(l.getName());
            }
        } catch (final LogicException e) {
            LOGGER.log(SEVERE, UNEXPECTED_ERROR, e);
        }
        return comparison;
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
        LevelSeriesMemoryImpl l = null;
        try {
            l = (LevelSeriesMemoryImpl) super.clone();
            l.levelSeriesDTO = getLevelSeriesDTO().deepClone();
            l.withTranslationHelper = getWithTranslationHelper().deepClone();
            l.levels = new TreeMap<PositiveInteger, Level>();
            for (final Map.Entry<PositiveInteger, Level> level : l.levels.entrySet()) {
                l.levels.put(level.getKey(), level.getValue().deepClone());
            }
        } catch (final CloneNotSupportedException cnse) {
            LOGGER.log(SEVERE, UNEXPECTED_ERROR, cnse);
        } catch (final LogicException e) {
            LOGGER.log(SEVERE, UNEXPECTED_ERROR, e);
        }
        return l;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteLevel(final int levelId) throws LogicException {
        if (levelId < 0) {
            throw new LogicException(ERROR_PROGRESSION_LEVEL_ID_NOT_SUPPORTED);
        } else {
            getLevels().remove(levelId);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteLevels() throws LogicException {
        getLevels().clear();
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
        getWithTranslationHelper().deleteTranslation(locale);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void deleteTranslations() throws LogicException {
        getWithTranslationHelper().deleteTranslations();
    }

    /**
     * Gets the value of <code>daoCoreType</code>.
     * 
     * @return the value of <code>daoCoreType</code>.
     * @since 0.8
     */
    private DAOCoreType getDaoCoreType() {
        return daoCoreType;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Level getLevel(final Integer levelId) throws LogicException {
        return getLevels().get(levelId);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Map<PositiveInteger, Level> getLevels() throws LogicException {
        if (levels == null) {
            levels = getLevels(getLevelSeriesDTO(), getDaoCoreType(), getBusinessType());
        }
        return levels;
    }

    /**
     * Gets all levels associated to the level series in the persistence system.
     * 
     * @param daoCoreImpl
     * @param busType
     * @return the {@link Map} of all {@link Level} associated to the level series. Its contains
     *         level ids as keys and {@link Level} instance as values.
     * @throws LogicException
     *             if any exception occurs while finding the levels.
     * @since 0.8
     */
    private Map<PositiveInteger, Level> getLevels(final LevelSeriesDTO lvlSeriesDTO, final DAOCoreType daoCoreImpl, final BusinessType busType) throws LogicException {
        final Map<PositiveInteger, Level> levelsMap = new TreeMap<PositiveInteger, Level>();
        if (daoCoreImpl == null) {
            throw new LogicException(ERROR_PROCESS_DAO_CORE_IMPL_EMPTY);
        } else {
            try {
                final Set<LevelDTO> levelDTOSet = daoCoreImpl.getDAOFactory().getLevelDAO().findAllLevels(lvlSeriesDTO);
                for (final LevelDTO levelDTO : levelDTOSet) {
                    final Level level = levelsMap.get(levelDTO.getLevelId());
                    if (level == null) {
                        final Level lvl = busType.getBusinessFactory().getLevelImpl(new PositiveInteger(levelDTO.getLevelId()), levelDTO.getAuthor(), new IntegerWithinInterval(MAX_COLUMNS, MIN_COLUMNS, levelDTO.getColumns()), new IntegerWithinInterval(MAX_LINES, MIN_LINES, levelDTO.getColumns()), levelDTO.getGraphicType(), new SecondTimeImpl(levelDTO.getTime()), lvlSeriesDTO.getLevelSeriesFileName());
                        levelsMap.put(new PositiveInteger(levelDTO.getLevelId()), lvl);
                    } else {
                        throw new LogicException(ERROR_LEVEL_FINDALL_ID_ALREADY_EXIST);
                    }
                }
            } catch (final PersistenceException e) {
                throw new LogicException(ERROR_LEVEL_FINDALL, e);
            }
        }
        return levelsMap;
    }

    /**
     * Gets the value of <code>levelSeriesDTO</code>.
     * 
     * @return the value of <code>levelSeriesDTO</code>.
     * @since 0.8
     */
    private LevelSeriesDTO getLevelSeriesDTO() {
        return levelSeriesDTO;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public int getLevelsSize() throws LogicException {
        return getLevels().size();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public String getName() throws LogicException {
        return getLevelSeriesDTO().getLevelSeriesFileName();
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Translation getTranslation(final RestrictedLocale locale) throws LogicException {
        return getWithTranslationHelper().getTranslation(locale);
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public Map<RestrictedLocale, Translation> getTranslations() throws LogicException {
        return getWithTranslationHelper().getTranslations();
    }

    /**
     * Gets all level series translations associated to the level series in the default persistence
     * system.
     * 
     * @param daoCoreImpl
     * @return the {@link Map} of all {@link Translation} associated to the level series. Its
     *         contains {@link Locale} as keys and {@link Translation} instance as values.
     * @throws LogicException
     *             if any exception occurs while finding the level series translations.
     * @since 0.8
     */
    private Map<RestrictedLocale, Translation> getTranslations(final DAOCoreType daoCoreImpl) throws LogicException {
        final Map<RestrictedLocale, Translation> translationsMap = new TreeMap<RestrictedLocale, Translation>();
        if (daoCoreImpl == null) {
            throw new LogicException(ERROR_PROCESS_DAO_CORE_IMPL_EMPTY);
        } else {
            try {
                final Set<LevelSeriesTranslationDTO> levelSeriesTranslationDTOSet = daoCoreImpl.getDAOFactory().getLevelSeriesTranslationDAO().findAllLevelSeriesTranslations(getLevelSeriesDTO());
                for (final LevelSeriesTranslationDTO levelSeriesTranslationDTO : levelSeriesTranslationDTOSet) {
                    final Translation levelSeriesTranslation = translationsMap.get(levelSeriesTranslationDTO.getLocale());
                    if (levelSeriesTranslation == null) {
                        translationsMap.put(new RestrictedLocale(levelSeriesTranslationDTO.getLocale()), new TranslationImpl(levelSeriesTranslationDTO));
                    } else {
                        throw new LogicException(ERROR_LEVEL_SERIES_TRANSLATION_FINDALL_LOCALE_ALREADY_EXIST);
                    }
                }
            } catch (final PersistenceException e) {
                throw new LogicException(ERROR_LEVEL_SERIES_TRANSLATION_FINDALL, e);
            }
        }
        return translationsMap;
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public int getTranslationsSize() throws LogicException {
        return getWithTranslationHelper().getTranslationsSize();
    }

    /**
     * Gets the value of <code>withTranslationHelper</code>.
     * 
     * @return the value of <code>withTranslationHelper</code>.
     * @throws LogicException
     * @since 0.8
     */
    private WithTranslationsHelper getWithTranslationHelper() throws LogicException {
        if (withTranslationHelper == null) {
            withTranslationHelper = new WithTranslationsHelper(getTranslations(getDaoCoreType()));
        }
        return withTranslationHelper;
    }

    /**
     * @param daoCoreImpl
     * @param levelSeries
     * @param levelsMap
     * @throws LogicException
     * @since 0.8
     */
    private void saveLevels(final DAOCoreType daoCoreImpl, final LevelSeriesDTO levelSeries, final Map<PositiveInteger, Level> levelsMap) throws LogicException {
        if (daoCoreImpl == null) {
            throw new LogicException(ERROR_PROCESS_DAO_CORE_IMPL_EMPTY);
        } else {
            try {
                daoCoreImpl.getDAOFactory().getLevelDAO().deleteAllLevels(levelSeries);
                for (final Saveable saveableLevel : levelsMap.values()) {
                    saveableLevel.save();
                }
            } catch (final PersistenceException e) {
                throw new LogicException(ERROR_LEVEL_SERIES_SAVE, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void save() throws LogicException {
        saveLevelSeriesTranslations(getDaoCoreType(), getLevelSeriesDTO(), getTranslations());
        saveLevels(getDaoCoreType(), getLevelSeriesDTO(), getLevels());
    }

    /**
     * @param daoCoreImpl
     * @param levelSeries
     * @param translationsMap
     * @throws LogicException
     * @since 0.8
     */
    private void saveLevelSeriesTranslations(final DAOCoreType daoCoreImpl, final LevelSeriesDTO levelSeries, final Map<RestrictedLocale, Translation> translationsMap) throws LogicException {
        if (daoCoreImpl == null) {
            throw new LogicException(ERROR_PROCESS_DAO_CORE_IMPL_EMPTY);
        } else {
            try {
                daoCoreImpl.getDAOFactory().getLevelSeriesTranslationDAO().deleteAllLevelSeriesTranslations(levelSeries);
                for (final Map.Entry<RestrictedLocale, Translation> translation : translationsMap.entrySet()) {
                    final LevelSeriesTranslationDTO levelSeriesTranslationDTO = new LevelSeriesTranslationDTO(levelSeries.getLevelSeriesFileName(), translation.getKey().getLocaleCopy(), translation.getValue().getTranslatedName(), translation.getValue().getTranslatedComment());
                    daoCoreImpl.getDAOFactory().getLevelSeriesTranslationDAO().createOrUpdateLevelSeriesTranslation(levelSeriesTranslationDTO);
                }
            } catch (final PersistenceException e) {
                throw new LogicException(ERROR_LEVEL_SERIES_SAVE, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void setName(final String value) throws LogicException {
        if (isEmpty(value)) {
            throw new LogicException(ERROR_LEVEL_SERIES_NAME_EMPTY);
        } else {
            getLevelSeriesDTO().setLevelSeriesFileName(value);
        }
    }

    /**
     * Update a level series in the persistence system.
     * 
     * @param daoCoreImpl
     * @param newLevelSeriesName
     *            the new level series name for the update, mustn't be <code>null</code>. This
     *            instance will replace the old one.
     * @throws LogicException
     *             if any exception occurs while updating the level series.
     * @since 0.8
     */
    private void updateLevelSeriesName(final DAOCoreType daoCoreImpl, final String newLevelSeriesName) throws LogicException {
        if (daoCoreImpl == null) {
            throw new LogicException(ERROR_PROCESS_DAO_CORE_IMPL_EMPTY);
        } else {
            if (isEmpty(newLevelSeriesName)) {
                throw new LogicException(ERROR_LEVEL_SERIES_EMPTY);
            } else {
                try {
                    daoCoreImpl.getDAOFactory().getLevelSeriesDAO().updateLevelSeries(getLevelSeriesDTO().getLevelSeriesFileName(), newLevelSeriesName);
                } catch (final PersistenceException e) {
                    throw new LogicException(ERROR_LEVEL_SERIES_UPDATE, e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @since 0.8
     */
    @Override
    public void updateLevelSeriesName(final String newLevelSeriesName) throws LogicException {
        updateLevelSeriesName(getDaoCoreType(), newLevelSeriesName);
    }
}
