package tw.bennu.feeler.db.service;

import java.util.Iterator;

public interface IFeelerEntityBox {

    public void addFeelerEntity(IFeelerEntity entity);

    public Iterator<IFeelerEntity> iteratorFeelerEntity();
}
