package de.grogra.xl.property;

import de.grogra.reflect.Type;
import de.grogra.reflect.TypeLoader;

/**
 * This <code>CompiletimeModel</code> is used by an XL compiler to
 * parameterize property-related aspects of XL language features. 
 * This mechanism allows XL to be used for a variety of
 * data sources. Implementations have to provide a suitable mapping of the specific properties
 * of the data source in order to define an easy-to-use model.
 * <p>
 * A <code>CompiletimeModel</code> defines the specificity of the properties of a
 * data source at compile-time. This has to be accompanied by an implementation
 * of a {@link de.grogra.xl.property.RuntimeModel} that is used at run-time.
 * The correct run-time model instance is obtained by invocations of
 * {@link de.grogra.xl.property.RuntimeModelFactory#modelForName} with the name
 * returned by {@link #getRuntimeName()} as parameter.  
 * <p>
 * A comprehensive specification of the behaviour of <code>CompiletimeModel</code>
 * is given by the specification of the XL programming language.
 * 
 * @author Ole Kniemeyer
 */
public interface CompiletimeModel {

    /**
	 * Returns a direct property. This method returns the direct property
	 * named <code>fieldName</code> declared in <code>type</code>. If no
	 * such property exists, <code>null</code> is returned.
	 *
	 * @param type the type in which the property is declared
	 * @param fieldName the name of the property
	 * @return the direct property, if any
	 */
    Property getDirectProperty(Type<?> type, String fieldName);

    /**
	 * Defines the name of the corresponding
	 * {@link RuntimeModel}. This name
	 * is used during run-time in invocations of
	 * {@link RuntimeModelFactory#modelForName}
	 * in order to obtain the {@link RuntimeModel}
	 * suitable for the code that is compiled within this compile-time model. 
	 * 
	 * @return the name of the corresponding run-time model
	 */
    String getRuntimeName();

    /**
	 * A compile-time <code>Property</code> is declared by the current
	 * {@link CompiletimeModel} and represents a property as defined
	 * by the specification of the XL programming language. Properties
	 * are similar to fields, but can be used in deferred
	 * assignments and some other constructs of the XL programming
	 * language.
	 * <p>
	 * A <code>Property</code> represents a property at compile-time.
	 * This has to be accompanied by a corresponding instance of
	 * {@link RuntimeModel.Property} that is used at run-time.
	 * The correct run-time property instance is obtained by invocations of
	 * {@link RuntimeModel#propertyForName(String, ClassLoader)}
	 * with the name returned by {@link #getRuntimeName()} as parameter.
	 * <p>
	 * A comprehensive specification of the behaviour of <code>Property</code>
	 * is given by the specification of the XL programming language.
	 * 
	 * @author Ole Kniemeyer
	 */
    interface Property {

        /**
		 * Returns this property's type.
		 * 
		 * @return this property's type
		 */
        Type<?> getType();

        /**
		 * Returns this property's compile-time model.
		 * This is the model which has declared
		 * this property.
		 * 
		 * @return this property's model
		 */
        CompiletimeModel getModel();

        /**
		 * Returns a subproperty. If this property has a subproperty named
		 * <code>name</code>, then this subproperty is returned, otherwise
		 * <code>null</code>.
		 *
		 * @param name the name of the subproperty
		 * @return the subproperty, if any
		 */
        Property getSubProperty(String name);

        /**
		 * Returns the component property. If this property has an array type,
		 * then a component property may exist and is returned by this method,
		 * otherwise <code>null</code> is returned.
		 *
		 * @return the component property, if any
		 */
        Property getComponentProperty();

        /**
		 * Returns a type-cast property. <code>type</code> has to be
		 * a reference type which is assignable to the type of this
		 * property. The method returns the corresponding type-cast property.
		 *
		 * @param type the type of the type-cast property
		 * @return the type-cast property
		 */
        Property getTypeCastProperty(Type<?> type);

        /**
		 * Defines the name of the corresponding
		 * {@link RuntimeModel.Property}. This name
		 * is used during run-time in invocations of
		 * {@link RuntimeModel#propertyForName(String, ClassLoader)}
		 * in order to obtain the {@link RuntimeModel.Property}
		 * which corresponds to this compile-time property. 
		 * 
		 * @return the name of the corresponding run-time property
		 */
        String getRuntimeName();

        Type<? extends RuntimeModel.Property> getRuntimeType();
    }
}
