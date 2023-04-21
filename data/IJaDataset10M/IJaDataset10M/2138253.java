package gdg.dataGeneration.columnDataGeneration;

/**
 * 
 * @author Silvio Donnini
 */
public class IntegerDataGenerator extends ColumnDataGenerator {

    public Object formatObject(Object next) {
        return next;
    }

    @Override
    protected String formatString(Object nextValue) {
        return this.formatObject(nextValue).toString();
    }
}
