package com.pp.cameleon.api.document.service;

import com.pp.cameleon.api.document.dto.Exercice;
import com.pp.cameleon.api.service.CrudService;
import com.pp.cameleon.api.validation.ValidationFailedException;
import java.util.Collection;

/**
 * ExerciceService
 */
public interface ExerciceService extends CrudService<Exercice> {

    /**
     * Adds a keypoint to the specified exercice. If the keypoint name is not found in the database,
     * it will then be created.
     *
     * @param exerciceId The exercice where to add the keypoint.
     * @param keypoint   The keypoint name.
     * @return The object.
     * @throws ValidationFailedException If provided bean is not valid to add.
     */
    Exercice addKeypoint(Integer exerciceId, String keypoint) throws ValidationFailedException;

    /**
     * Adds a keypoint to the specified exercice. If the keypoint name is not found in the database,
     * it will then be created.
     *
     * @param exerciceId The exercice where to add the keypoint.
     * @param keypointId The keypoint identificator.
     * @return The object.
     * @throws ValidationFailedException If provided bean is not valid to add.
     */
    Exercice addKeypoint(Integer exerciceId, Integer keypointId) throws ValidationFailedException;

    /**
     * Lists all the exercices containing the matching keypoint bean.
     *
     * @param ownerId
     *@param keypointId The keypoint id. @return The list of exercices.
     * @throws ValidationFailedException If provided bean is not valid for search.
     */
    Collection<Exercice> list(Integer ownerId, Integer keypointId) throws ValidationFailedException;

    /**
     * Removes the keypoint from the exercice.
     *
     * @param exerciceId The exercice identificator.
     * @param keypointId The keypoint identificator.
     * @throws ValidationFailedException If the keypoint is not found in the exercice.
     */
    void removeKeypoint(Integer exerciceId, Integer keypointId) throws ValidationFailedException;

    /**
     * Removes the keypoint from the exercice.
     *
     * @param exerciceId The exercice identificator.
     * @param keypoint   The keypoint name.
     * @throws ValidationFailedException If the keypoint is not found in the exercice.
     */
    void removeKeypoint(Integer exerciceId, String keypoint) throws ValidationFailedException;
}
