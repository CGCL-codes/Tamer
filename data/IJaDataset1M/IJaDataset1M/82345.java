#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId}.query.parameters;

import ${package}.${artifactId}.query.interfaces.IHibernateParameter;

/**
 * Base implementation of IHibernateParameter interface
 * @author mkrzempek, pstepaniak
 *
 */
public abstract class AbstractQueryParameter implements IHibernateParameter
{
	private String propertyName = null;
	
	public AbstractQueryParameter(final String propertyName)
	{
		if(propertyName == null)
		{
			throw new IllegalStateException("propertyName is null!!!");
		}
		this.propertyName = propertyName;
	}
	
	public String getPropertyName()
	{
		return this.propertyName;
	}
	
	public void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}
	
	public void extendPropertyName(final String parentPropertyName)
	{
		if (this.propertyName.contains("."))
		{
			final int dotIndex = this.propertyName.indexOf('.');
			this.propertyName = this.propertyName.substring(dotIndex + 1);
		}
		
		this.propertyName = parentPropertyName + "." + this.propertyName;
	}
}
