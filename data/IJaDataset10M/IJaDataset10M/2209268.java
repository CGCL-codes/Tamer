package cham.open.pattern.state;

/**
 *
 * @author Chaminda Amarasinghe <chaminda.amarasinghe@headstrong.com>
 */
public abstract class State {

    abstract Object getObject();

    abstract void releaseObject(Object object);
}
