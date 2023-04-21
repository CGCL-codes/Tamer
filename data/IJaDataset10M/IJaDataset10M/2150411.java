package edu.rabbit.schema;

import java.util.List;

/**
 * Function expression.
 * 
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 */
public interface IFunctionExpression extends IExpression {

    public String getName();

    public boolean areDistinctArguments();

    public List<IExpression> getArguments();

    public boolean isAll();
}
