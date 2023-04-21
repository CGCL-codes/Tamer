package com.antares.commons.predicate;

import com.antares.commons.exception.PropertyTypeNotSupportedException;

/**
 * Helper para el manejo de PredicateFactory  
 *
 * @version 1.0.0 Created 01/05/2011 by Julian Martinez
 * @author <a href:mailto:otakon@gmail.com> Julian Martinez </a>
 */
public class PredicateFactoryHelper {

    private static final PredicateFactory NUMBER_PREDICATE_FACTORY = new NumberPredicateFactory();

    private static final PredicateFactory TEXT_PREDICATE_FACTORY = new TextPredicateFactory();

    private static final PredicateFactory DATE_PREDICATE_FACTORY = new DatePredicateFactory();

    private static final PredicateFactory BOOLEAN_PREDICATE_FACTORY = new BooleanPredicateFactory();

    private static final PredicateFactory OPTION_PREDICATE_FACTORY = new OptionPredicateFactory();

    /**
	 * Devuelve el PredicateFactory correspondiente al tipo de propiedad necesaria 
	 * @param propertyType tipo de propiedad
	 * @return factory concreto de predicados
	 * @throws PropertyTypeNotSupportedException si el tipo de propiedad no se encuentra soportado por el sistema 
	 */
    public static PredicateFactory getPredicateFactory(PropertyType propertyType) throws PropertyTypeNotSupportedException {
        PredicateFactory predicateFactory = null;
        switch(propertyType) {
            case NUMERIC:
                predicateFactory = NUMBER_PREDICATE_FACTORY;
                break;
            case TEXT:
                predicateFactory = TEXT_PREDICATE_FACTORY;
                break;
            case DATE:
                predicateFactory = DATE_PREDICATE_FACTORY;
                break;
            case BOOLEAN:
                predicateFactory = BOOLEAN_PREDICATE_FACTORY;
                break;
            case OPTION:
                predicateFactory = OPTION_PREDICATE_FACTORY;
                break;
            default:
                throw new PropertyTypeNotSupportedException("The property type " + propertyType + " is not supported");
        }
        return predicateFactory;
    }
}
