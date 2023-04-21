package com.tll.server.rpc.entity;

import javax.servlet.ServletContext;
import com.google.inject.Inject;
import com.tll.common.model.IEntityTypeResolver;
import com.tll.mail.MailManager;
import com.tll.model.IEntityAssembler;
import com.tll.model.IEntityFactory;
import com.tll.model.SchemaInfo;
import com.tll.server.IExceptionHandler;
import com.tll.server.marshal.IMarshalOptionsResolver;
import com.tll.server.marshal.Marshaler;
import com.tll.service.entity.IEntityServiceFactory;

/**
 * PersistContext - Servlet context scoped data object holding needed types for
 * processing persist/model data related functionality.
 * @author jpk
 */
public final class PersistContext {

    private static final long key = 7366163949288867262L;

    /**
	 * The key identifying the {@link PersistContext} in the
	 * {@link ServletContext}.
	 */
    public static final String KEY = Long.toString(key);

    private final MailManager mailManager;

    private final SchemaInfo schemaInfo;

    private final Marshaler marshaler;

    private final IMarshalOptionsResolver marshalOptionsResolver;

    private final IEntityTypeResolver entityTypeResolver;

    private final IEntityFactory entityFactory;

    private final IEntityAssembler entityAssembler;

    private final IEntityServiceFactory entityServiceFactory;

    private final IExceptionHandler exceptionHandler;

    private final PersistCache persistCache;

    /**
	 * Constructor
	 * @param mailManager
	 * @param schemaInfo
	 * @param marshaler
	 * @param marshalOptionsResolver
	 * @param entityTypeResolver
	 * @param entityFactory
	 * @param entityAssembler
	 * @param entityServiceFactory
	 * @param exceptionHandler
	 * @param persistCache
	 */
    @Inject
    public PersistContext(MailManager mailManager, SchemaInfo schemaInfo, Marshaler marshaler, IMarshalOptionsResolver marshalOptionsResolver, IEntityTypeResolver entityTypeResolver, IEntityFactory entityFactory, IEntityAssembler entityAssembler, IEntityServiceFactory entityServiceFactory, IExceptionHandler exceptionHandler, PersistCache persistCache) {
        super();
        this.mailManager = mailManager;
        this.schemaInfo = schemaInfo;
        this.marshaler = marshaler;
        this.marshalOptionsResolver = marshalOptionsResolver;
        this.entityTypeResolver = entityTypeResolver;
        this.entityFactory = entityFactory;
        this.entityAssembler = entityAssembler;
        this.entityServiceFactory = entityServiceFactory;
        this.exceptionHandler = exceptionHandler;
        this.persistCache = persistCache;
    }

    public IEntityServiceFactory getEntityServiceFactory() {
        return entityServiceFactory;
    }

    public IEntityFactory getEntityFactory() {
        return entityFactory;
    }

    public IEntityAssembler getEntityAssembler() {
        return entityAssembler;
    }

    public MailManager getMailManager() {
        return mailManager;
    }

    public SchemaInfo getSchemaInfo() {
        return schemaInfo;
    }

    public Marshaler getMarshaler() {
        return marshaler;
    }

    public IExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public IEntityTypeResolver getEntityTypeResolver() {
        return entityTypeResolver;
    }

    public IMarshalOptionsResolver getMarshalOptionsResolver() {
        return marshalOptionsResolver;
    }

    public PersistCache getPersistCache() {
        return persistCache;
    }
}
