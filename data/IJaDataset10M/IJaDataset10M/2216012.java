package tripleo.fmsys;

public interface FMResponse<E> {

    public Code code();

    public CharSequence value();

    public CharSequence exception();

    public E get();
}
