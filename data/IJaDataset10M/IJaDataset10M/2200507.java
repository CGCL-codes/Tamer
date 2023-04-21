package net.sf.cyberrails.Util.PathUtils;

public interface Displayer<F> {

    public void starting(Path<F> path);

    public void visited(Path<F> path);

    public void expanded(Path<F> path);

    public void found(Path<F> path);
}
