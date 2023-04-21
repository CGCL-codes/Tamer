package mipt.io.table.analytic;

/**
 * ValueSet returned by AnaliticTable.findGroupSet method and having in each row
 *  not only values of Expression calculated (getValue() - it must be group function)
 *  but also corresponding value of GroupExpression (getGroupValue())
 * Approapriate metadata (what group produses set) is returned by getGroupExpression()
 */
public interface GroupSet extends ValueSet {

    /**
 * 
 * @return mipt.search.GroupExpression
 */
    mipt.search.analytic.GroupExpression getGroupExpression();

    /**
 * 
 * @return java.lang.Object
 */
    Object getGroupValue();
}
