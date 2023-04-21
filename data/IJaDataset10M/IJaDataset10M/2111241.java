package pcgen.gui.filter;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public interface MutableFilter<E> extends DisplayableFilter<E> {

    public void setDescription(String description);

    public void setCode(String code);
}
