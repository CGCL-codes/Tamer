package de.sonivis.tool.core.datamodel.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import ca.odell.glazedlists.EventList;
import de.sonivis.tool.core.datamodel.ContentElement;
import de.sonivis.tool.core.datamodel.InfoSpace;
import de.sonivis.tool.core.datamodel.InfoSpaceItemProperty;
import de.sonivis.tool.core.datamodel.exceptions.CannotConnectToDatabaseException;
import de.sonivis.tool.core.datamodel.proxy.IContentElement;
import de.sonivis.tool.core.datamodel.proxy.IContextRelation;

/**
 * Requirements for DAO implementations for a {@link IContextRelation}.
 * 
 * @author Andreas Erber
 * @version $Revision: 1417 $, $Date: 2010-01-28 09:24:56 -0500 (Thu, 28 Jan 2010) $
 */
@SuppressWarnings("unchecked")
public interface IContextRelationDAO extends IGenericDAO<IContextRelation>, IInfoSpaceItemDAO<IContextRelation> {

    /**
	 * Name of the source field.
	 */
    String SOURCE_FIELD = "source";

    /**
	 * Name of the target field.
	 */
    String TARGET_FIELD = "target";

    /**
	 * Name of the established field
	 */
    String ESTABLISHED_FIELD = "established";

    /**
	 * Flag to eagerly load the source {@link ContentElement} of the {@link IContextRelation}.
	 */
    Integer INIT_SOURCE = 1;

    /**
	 * Flag to eagerly load the target {@link ContentElement} of the {@link IContextRelation}.
	 */
    Integer INIT_TARGET = 2;

    /**
	 * Flag to eagerly load source and target {@link ContentElement}s of the
	 * {@link IContextRelation}.
	 */
    Integer INIT_ALL = 3;

    /**
	 * Get the total amount of currently persisted entities in this {@link InfoSpace} having the
	 * specified type.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @param clazz
	 *            The type of the entities to be counted.
	 * @return Number of entities matching the requirements.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available
	 */
    Integer count(InfoSpace is, Class<? extends IContextRelation> clazz) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} entities of the specified type in the given
	 * {@link InfoSpace}.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @param clazz
	 *            The sub-type of {@link IContextRelation} to look for.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findByType(InfoSpace is, Class<? extends IContextRelation> clazz) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} entities of the specified {@link Set} of sub-types in
	 * the given {@link InfoSpace}.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @param clazzes
	 *            The {@link Set} of sub-types of {@link IContextRelation} to look for.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findByType(InfoSpace is, Set<Class<? extends IContextRelation>> clazzes) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of {@link #findByType()}.
	 * <p>
	 * Note, this method will only return the overall super-type and not necessarily the actual
	 * type.
	 * </p>
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findByTypeType();

    /**
	 * Retrieve all {@link IContextRelation} entities that share the same target specified by
	 * <em>target</em>.
	 * 
	 * @param targetId
	 *            Identifier of the {@link ContentElement} entity a relationship aims at.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match.
	 * @deprecated use {@link #findSources(IContentElement, Integer)}
	 */
    @Deprecated
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(Long targetId);

    /**
	 * Retrieve all {@link IContextRelation} items having the specified {@link IContentElement
	 * target}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param target
	 *            The {@link IContentElement} entity each {@link IContextRelation} entity must have
	 *            as a target item.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(IContentElement target, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of the specified type having the specified
	 * {@link IContentElement target}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param target
	 *            The {@link IContentElement} entity the {@link IContextRelation} entities must have
	 *            as a target item.
	 * @param clazz
	 *            The type the {@link IContextRelation} entities must have. If <code>null</code> the
	 *            type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(IContentElement target, Class<? extends IContextRelation> clazz, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items having one (or more) of the specified sub-types
	 * and the specified {@link IContentElement target}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param target
	 *            The {@link IContentElement} entity each {@link IContextRelation} entity must have
	 *            as a target item.
	 * @param clazzes
	 *            The sub-types of {@link IContextRelation} to be looked for. If <code>null</code>
	 *            or empty the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(IContentElement target, Set<Class<? extends IContextRelation>> clazzes, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items having the specified {@link Collection targets}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param targets
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's target must be contained in.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(Collection<? extends IContentElement> targets, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of the specified type having the specified
	 * {@link Collection targets}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param targets
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's target must be contained in.
	 * @param clazz
	 *            The type the {@link IContextRelation} entities must have. If <code>null</code> the
	 *            type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(Collection<? extends IContentElement> targets, Class<? extends IContextRelation> clazz, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of one (or more) of the specified sub-types
	 * having the specified {@link Collection targets}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param targets
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's target must be contained in.
	 * @param clazzes
	 *            The sub-types of {@link IContextRelation} to be looked for. If <code>null</code>
	 *            the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findSources(Collection<? extends IContentElement> targets, Set<Class<? extends IContextRelation>> clazzes, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of {@link #findSources()}.
	 * <p>
	 * Note, this method will only return the overall super-type and not necessarily the actual
	 * type.
	 * </p>
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findSourcesType();

    /**
	 * Retrieve all {@link IContextRelation} entities that share the same source specified by
	 * <em>source</em>.
	 * 
	 * @param sourceId
	 *            Identifier of the {@link ContentElement} entity a relationship sources from.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match
	 * @deprecated use {@link #findTargets(IContentElement,Integer)}
	 */
    @Deprecated
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(Long sourceId);

    /**
	 * Retrieve all {@link IContextRelation} items having the specified {@link IContentElement
	 * source}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param source
	 *            The {@link IContentElement} entity a {@link IContextRelation} entity must have as
	 *            a source item.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(IContentElement source, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of the specified type having the specified
	 * {@link IContentElement source}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param source
	 *            The {@link IContentElement} entity a {@link IContextRelation} entity must have as
	 *            a source item.
	 * @param clazz
	 *            The type the {@link IContextRelation} entities must have. If <code>null</code> the
	 *            type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(IContentElement source, Class<? extends IContextRelation> clazz, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items having one (or more) of the specified sub-types
	 * and the specified {@link IContentElement source}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param source
	 *            The {@link IContentElement} entity each {@link IContextRelation} entity must have
	 *            as a source item.
	 * @param clazzes
	 *            The sub-types of {@link IContextRelation} to be looked for. If <code>null</code>
	 *            or empty the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(IContentElement source, Set<Class<? extends IContextRelation>> clazzes, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items having the specified {@link Collection sources}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param sources
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's source must be contained in.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(Collection<? extends IContentElement> sources, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of the specified type having the specified
	 * {@link Collection sources}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param sources
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's source must be contained in.
	 * @param clazz
	 *            The type the {@link IContextRelation} entities must have. If <code>null</code> the
	 *            type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(Collection<? extends IContentElement> sources, Class<? extends IContextRelation> clazz, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of one (or more) of the specified sub-types
	 * having the specified {@link Collection sources}.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param sources
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's source must be contained in.
	 * @param clazzes
	 *            The sub-types of {@link IContextRelation} to be looked for. If <code>null</code>
	 *            the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findTargets(Collection<? extends IContentElement> sources, Set<Class<? extends IContextRelation>> clazzes, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of {@link #findTargets()}
	 * <p>
	 * Note, this method will only return the overall super-type and not necessarily the actual
	 * type.
	 * </p>
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findTargetsType();

    /**
	 * Get all {@link IContextRelation} entities the {@link ContentElement} identified by
	 * <em>contentElementId</em> has.
	 * <p>
	 * Note, that the result may contain duplicate entries. Refer to
	 * {@link #findAllUniqueRelationships(Long)} for a list without duplicates.
	 * </p>
	 * 
	 * @param contentElementId
	 *            Identifier of the {@link ContentElement} entity whose {@link IContextRelation}
	 *            entities are to be found.
	 * @return An {@link EventList} of {@link IContextRelation} entities that the identified
	 *         {@link ContentElement} has. May contain duplicates.
	 * @see #findAllUniqueRelationships(Long)
	 * @deprecated use {@link #findAllRelationships(IContentElement, Integer)}
	 */
    @Deprecated
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(Long contentElementId);

    /**
	 * Get all {@link IContextRelation} entities the {@link IContentElement} entity identified by
	 * <em>contentElement</em> has.
	 * <p>
	 * Note, that the result may contain duplicate entries.
	 * </p>
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param contentElement
	 *            The {@link IContentElement} entity whose {@link IContextRelation} entities are to
	 *            be found.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of {@link IContextRelation} entities the specified
	 *         {@link IContentElement} entity is part of. May contain duplicates.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(IContentElement contentElement, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of the specified sub-type having the specified
	 * {@link IContentElement} for either a source or a target.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param target
	 *            The {@link IContentElement} entity the {@link IContextRelation} entities must have
	 *            as a source or target item.
	 * @param clazz
	 *            The type the {@link IContextRelation} entities must have. If <code>null</code> the
	 *            type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(IContentElement contentElement, Class<? extends IContextRelation> clazz, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} items of the specified sub-types having the specified
	 * {@link IContentElement} for either a source or a target.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param target
	 *            The {@link IContentElement} entity the {@link IContextRelation} entities must have
	 *            as a source or target item.
	 * @param clazz
	 *            The sub-types of {@link IContextRelation} to be looked for. If <code>null</code>
	 *            the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(IContentElement contentElement, Set<Class<? extends IContextRelation>> clazzes, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Get all {@link IContextRelation} entities each {@link IContentElement} entity contained in
	 * <em>contentElements</em> has.
	 * <p>
	 * Note, that the result may contain duplicate entries.
	 * </p>
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param contentElements
	 *            The {@link Collection} of {@link IContentElement} entities whose
	 *            {@link IContextRelation} entities are to be found.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of {@link IContextRelation} entities the specified
	 *         {@link IContentElement} entities are part of. May contain duplicates.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(Collection<? extends IContentElement> contentElements, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} entities of the specified sub-type having the specified
	 * {@link Collection} of {@link IContentElement} entities either as source or target.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param contentElements
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's source or target must be contained in.
	 * @param clazz
	 *            The sub-type the {@link IContextRelation} entities must have. If <code>null</code>
	 *            the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(Collection<? extends IContentElement> contentElements, Class<? extends IContextRelation> clazz, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} entities of the specified sub-types having the
	 * specified {@link Collection} of {@link IContentElement} entities either as source or target.
	 * <p>
	 * Use the <code>flag</code> argument to define the items that are to be initialized. Useful
	 * value are defined by the class constants {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and
	 * {@link #INIT_ALL}. Passing <code>null</code> will return an {@link EventList} of
	 * {@link IContextRelation}s with neither source nor target {@link ContentElement} initialized.
	 * </p>
	 * 
	 * @param contentElements
	 *            A {@link Collection} of {@link IContentElement} entities a
	 *            {@link IContextRelation} entity's source or target must be contained in.
	 * @param clazzes
	 *            The sub-types of {@link IContextRelation} to be looked for. If <code>null</code>
	 *            the type {@link IContextRelation} will be used as default.
	 * @param flag
	 *            One of {@link #INIT_SOURCE}, {@link #INIT_TARGET}, and {@link #INIT_ALL} to
	 *            initialize certain associations.
	 * @return An {@link EventList} of matching {@link IContextRelation} items.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available.
	 */
    EventList<IContextRelation<IContentElement, IContentElement>> findAllRelationships(Collection<? extends IContentElement> contentElements, Set<Class<? extends IContextRelation>> clazzes, Integer flag) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findAllRelationships()}
	 * <p>
	 * Note, this method will only return the overall super-type and not necessarily the actual
	 * type.
	 * </p>
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findAllRelationshipsType();

    /**
	 * Find {@link IContextRelation} entities having an {@link InfoSpaceItemProperty} of the
	 * specified name attached.
	 * <p>
	 * The value argument is treated optional. If non-<code>null</code>, the result set will be
	 * restricted to contain only those entries where the property value equals the specified
	 * <em>value</em>.
	 * </p>
	 * 
	 * @param is
	 *            The {@link InfoSpace} to restrict the search to.
	 * @param clazzes
	 *            A {@link Set} of sub-types to restrict result set to.
	 * @param propName
	 *            The name of the {@link InfoSpaceItemProperty} an {@link IContextRelation} entity
	 *            must have.
	 * @param value
	 *            Optional value the property value is to be restricted to.
	 * @return An {@link EventList} of matching {@link IContextRelation} entities.
	 * @throws CannotConnectToDatabaseException
	 *             If the persistence store is not available.
	 */
    EventList<IContextRelation> findByProperty(InfoSpace is, Set<Class<? extends IContextRelation>> clazzes, String propName, Serializable value) throws CannotConnectToDatabaseException;

    /**
	 * Find {@link IContextRelation} entities having an {@link InfoSpaceItemProperty} of the
	 * specified name attached.
	 * <p>
	 * The <em>lowVal</em> and <em>hiVal</em> arguments are treated optional. If non-
	 * <code>null</code>, the result set will be restricted to contain only those entries that are
	 * greater than or equal <em>lowVal</em> and/or less than or equal <em>hiVal</em>. I.e., none,
	 * one or both values may optionally be set.
	 * </p>
	 * 
	 * @param is
	 *            The {@link InfoSpace} to restrict the search to.
	 * @param clazzes
	 *            A {@link Set} of sub-types to restrict result set to.
	 * @param propName
	 *            The name of the {@link InfoSpaceItemProperty} an {@link IContextRelation} entity
	 *            must have.
	 * @param lowVal
	 *            Optional lower inclusive border the property value is to be restricted to.
	 * @param hiVal
	 *            Optional upper inclusive border the property value is to be restricted to.
	 * @return An {@link EventList} of matching {@link IContextRelation} entities.
	 * @throws CannotConnectToDatabaseException
	 */
    EventList<IContextRelation> findByProperty(InfoSpace is, Set<Class<? extends IContextRelation>> clazzes, String propName, Serializable lowVal, Serializable hiVal) throws CannotConnectToDatabaseException;

    /**
	 * Retrieve all {@link IContextRelation} entities that were established on the specified
	 * {@link Date} or later.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @param established
	 *            Earliest acceptable creation {@link Date}.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available
	 */
    EventList<IContextRelation> findEstablishedAfter(InfoSpace is, Date established) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findEstablishedAfter(InfoSpace, Date)}.
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findEstablishedAfterType();

    /**
	 * Retrieve all {@link IContextRelation} entities that were established on the specified
	 * {@link Date} or earlier.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @param established
	 *            Latest acceptable creation {@link Date}.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available
	 */
    EventList<IContextRelation> findEstablishedBefore(InfoSpace is, Date established) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findEstablishedBefore(InfoSpace, Date)}.
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findEstablishedBeforeType();

    /**
	 * Retrieve all {@link IContextRelation} entities that were established between the specified
	 * {@link Date}s.
	 * 
	 * @param is
	 *            The {@link InfoSpace} to search in.
	 * @param start
	 *            Earliest acceptable creation {@link Date}.
	 * @param end
	 *            Latest acceptable creation {@link Date}.
	 * @return An {@link EventList} of {@link IContextRelation} entities that match.
	 * @throws CannotConnectToDatabaseException
	 *             if connection to persistence store is not available
	 */
    EventList<IContextRelation> findEstablishedBetween(InfoSpace is, Date start, Date end) throws CannotConnectToDatabaseException;

    /**
	 * The data type of the objects contained in the {@link EventList} of
	 * {@link #findEstablishedBetween(InfoSpace, Date, Date)}.
	 * 
	 * @return The contained data type.
	 */
    Class<? extends IContextRelation> findEstablishedBetweenType();
}
