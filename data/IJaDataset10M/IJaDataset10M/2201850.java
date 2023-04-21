package atlantik.game.action;

import modelz.UpdateListener;
import modelz.Updateable;
import atlantik.game.Atlantik;

public abstract class BoundAction<A extends BoundAction<A, T>, T extends Updateable<T>> extends AtlantikAction<A> {

    protected final T item;

    protected BoundAction(Atlantik client, T item) {
        super(client);
        this.item = item;
        item.addListener(new UpdateListener<T>() {

            public void updated(T arg0) {
                update();
            }
        });
    }
}
