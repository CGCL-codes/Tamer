package cham.open.pattern.observer;

/**
 *
 * @author Chaminda Amarasinghe <chaminda.amarasinghe@headstrong.com>
 */
public interface Observer {

    void update(int val);

    int getVal();
}
