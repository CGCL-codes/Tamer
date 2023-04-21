package javamicroweb.html;

public interface HTMLSpan<C> extends HTMLInlineElement<C> {

    public void add(HTMLInlineElement<C> child);

    public void add(HTMLTextNode<C> child);
}
