package net.sourceforge.mazix.persistence.dao.profiles;

import java.util.Set;
import net.sourceforge.mazix.components.exception.PersistenceException;
import net.sourceforge.mazix.persistence.dto.levels.LevelSeriesDTO;
import net.sourceforge.mazix.persistence.dto.profiles.ProfileDTO;
import net.sourceforge.mazix.persistence.dto.profiles.ProgressionDTO;

/**
 * Data access object for the profile progressions.
 * 
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 * @since 0.7
 * @version 0.8
 */
public interface ProgressionDAO {

    /**
     * Creates or updates a progression.
     * 
     * @param progressionDTO
     *            the {@link ProgressionDTO} instance to create if it doesn't already exist or to
     *            update if it already exists, mustn't be <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while creating or updating the progression.
     * @since 0.7
     */
    void createOrUpdateProgression(ProgressionDTO progressionDTO) throws PersistenceException;

    /**
     * Deletes all progressions of a profile.
     * 
     * @param profileDTO
     *            the {@link ProfileDTO} instance to delete the progressions from, mustn't be
     *            <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while deleting all progressions.
     * @see #deleteAllProgressions(String)
     * @since 0.8
     */
    void deleteAllProgressions(ProfileDTO profileDTO) throws PersistenceException;

    /**
     * Deletes all progressions following a profile name.
     * 
     * @param profileName
     *            the profile name to delete the progressions from, mustn't be <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while deleting all progressions.
     * @see #deleteAllProgressions(ProfileDTO)
     * @since 0.8
     */
    void deleteAllProgressions(String profileName) throws PersistenceException;

    /**
     * Deletes a progression.
     * 
     * @param profileDTO
     *            the {@link ProfileDTO} instance to delete the progression from if it exists,
     *            mustn't be <code>null</code>.
     * @param levelSeriesDTO
     *            the {@link LevelSeriesDTO} instance to delete the progression from if it exists,
     *            mustn't be <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while deleting the progression.
     * @since 0.7
     */
    void deleteProgression(ProfileDTO profileDTO, LevelSeriesDTO levelSeriesDTO) throws PersistenceException;

    /**
     * Deletes a progression.
     * 
     * @param progressionDTO
     *            the {@link ProgressionDTO} instance to delete if it exists, mustn't be
     *            <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while deleting the progression.
     * @see #deleteAllProgressions(ProfileDTO)
     * @since 0.7
     */
    void deleteProgression(ProgressionDTO progressionDTO) throws PersistenceException;

    /**
     * Deletes a progression.
     * 
     * @param profileName
     *            the profile name to delete the progression from if it exists, mustn't be
     *            <code>null</code>.
     * @param levelSeriesName
     *            the level series name to delete the progression from if it exists, mustn't be
     *            <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while deleting the progression.
     * @since 0.7
     */
    void deleteProgression(String profileName, String levelSeriesName) throws PersistenceException;

    /**
     * Finds all progressions of a profile.
     * 
     * @param profileDTO
     *            the {@link ProfileDTO} instance to get the progressions from, mustn't be
     *            <code>null</code>.
     * @return the {@link Set} of all {@link ProgressionDTO} associated to the profile, returns an
     *         empty {@link Set} if no progressions are found.
     * @throws PersistenceException
     *             if any persistence exception occurs while finding the progressions.
     * @see #findAllProgressions(String)
     * @since 0.7
     */
    Set<ProgressionDTO> findAllProgressions(ProfileDTO profileDTO) throws PersistenceException;

    /**
     * Finds all progressions following a profile name.
     * 
     * @param profileName
     *            the profile name to get the progressions from, mustn't be <code>null</code>.
     * @return the {@link Set} of all {@link ProgressionDTO} associated to the profile, returns an
     *         empty {@link Set} if no progressions are found.
     * @throws PersistenceException
     *             if any persistence exception occurs while finding the progressions.
     * @see #findAllProgressions(ProfileDTO)
     * @since 0.7
     */
    Set<ProgressionDTO> findAllProgressions(String profileName) throws PersistenceException;

    /**
     * Finds a progression following its profile and the level series.
     * 
     * @param profileDTO
     *            the {@link ProfileDTO} instance to get the progression from, mustn't be
     *            <code>null</code>.
     * @param levelSeriesDTO
     *            the {@link LevelSeriesDTO} instance to get the progression from, mustn't be
     *            <code>null</code>.
     * @return the {@link ProgressionDTO} instance if found, <code>null</code> otherwise.
     * @throws PersistenceException
     *             if any persistence exception occurs while finding the progression.
     * @see #findProgression(String, String)
     * @since 0.7
     */
    ProgressionDTO findProgression(ProfileDTO profileDTO, LevelSeriesDTO levelSeriesDTO) throws PersistenceException;

    /**
     * Finds a progression following its profile and the level series name.
     * 
     * @param profileName
     *            the profile name to get the progression from, mustn't be <code>null</code>.
     * @param levelSeriesName
     *            the level series name to get the progression from, mustn't be <code>null</code>.
     * @return the {@link ProgressionDTO} instance if found, <code>null</code> otherwise.
     * @throws PersistenceException
     *             if any persistence exception occurs while finding the progression.
     * @see #findProgression(ProfileDTO, LevelSeriesDTO)
     * @since 0.7
     */
    ProgressionDTO findProgression(String profileName, String levelSeriesName) throws PersistenceException;

    /**
     * Reset all progressions of a profile.
     * 
     * @param profileDTO
     *            the {@link ProfileDTO} instance to reset the progressions from, mustn't be
     *            <code>null</code>.
     * @param reachedLevelId
     *            the maximum reached level id to set to all existing progressions, mustn't be
     *            <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while reseting all progressions.
     * @see #resetAllProgressions(String, int)
     * @since 0.8
     */
    void resetAllProgressions(ProfileDTO profileDTO, int reachedLevelId) throws PersistenceException;

    /**
     * Reset all progressions of a profile.
     * 
     * @param profileName
     *            the profile name to reset the progressions from, mustn't be <code>null</code>.
     * @param reachedLevelId
     *            the maximum reached level id to set to all existing progressions, mustn't be
     *            <code>null</code>.
     * @throws PersistenceException
     *             if any persistence exception occurs while reseting all progressions.
     * @see #resetAllProgressions(String, int)
     * @since 0.8
     */
    void resetAllProgressions(String profileName, int reachedLevelId) throws PersistenceException;
}
