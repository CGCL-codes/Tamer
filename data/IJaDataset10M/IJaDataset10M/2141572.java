package lt.ktu.scheduler.base;

import lt.ktu.scheduler.base.collections.BaseCollection;

public class BaseTask extends AbstractBase implements CollectionPart {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7747510813622718667L;

    private static int idGenerator = 0;

    private int id;

    public BaseTask() {
        id = idGenerator;
        idGenerator++;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    public void setCollection(BaseCollection<?> col) {
    }
}
