#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId}.query.parameters;

import java.util.ArrayList;
import java.util.List;

import ${package}.${artifactId}.query.interfaces.IHibernateParameter;

/**
 * Filter parameter implementing the 'or' condition
 * For example, the following code:
 * 
 * <pre>
 * {@code
 * List<Order> orders = null;
 * AlternativeParametersSet param = new AlternativeParametersSet();
 * param10.addParameter(new SingleValueQueryParameter(Order.ID, 1, IHibernateSingleValueParameter.OperationType.EQUAL ));
 * param10.addParameter(new SingleValueQueryParameter(Order.ID, 3, IHibernateSingleValueParameter.OperationType.EQUAL ));
 * orders = orderDAO.selectWithParameter(param);
 * }
 * </pre>
 *
 * will result in a query:
 * <pre>
 * {@code
 * select * from order where order_id = 1 or order_id = 3
 * }
 * </pre>
 *
 * @author mkrzempek, pstepaniak
 *
 */
public class AlternativeParametersSet extends AbstractQueryParameter 
{
	private final List<IHibernateParameter> alternativeParameters = new ArrayList<IHibernateParameter>();
	
	public AlternativeParametersSet()
	{
		super("");
	}
	
	public void addParameter(final IHibernateParameter parameter)
	{
		this.alternativeParameters.add(parameter);
	}
	
	public List<IHibernateParameter> getAlternativeParameters()
	{
		return this.alternativeParameters;
	}
	
	@Override
	public void extendPropertyName(final String parentPropertyName)
	{
		for (IHibernateParameter parameter : this.alternativeParameters)
		{
			parameter.extendPropertyName(parentPropertyName);
		}
	}
	
	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(this.getClass().getName());
		stringBuilder.append(" {Alternative parameters set: ");
		stringBuilder.append(this.alternativeParameters.toString());
		stringBuilder.append("}");
		
		return stringBuilder.toString();
	}
}
