package com.eastidea.qaforum.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import org.jboss.seam.annotations.Name;
import com.eastidea.qaforum.dao.base.BaseMaintDaoImpl;
import com.eastidea.qaforum.model.Position;

@Stateless
@Name("positionMaintDao")
public class PositionMaintDaoImpl extends BaseMaintDaoImpl implements PositionMaintDao {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4604655091144086764L;

    public ArrayList<SelectItem> getPositionDictionary() {
        ArrayList<SelectItem> dictionary = new ArrayList<SelectItem>();
        List<Position> list = getAllPosition();
        Position position;
        Iterator i = list.iterator();
        while (i.hasNext()) {
            position = (Position) i.next();
            dictionary.add(new SelectItem(position.getPositionid().toString(), position.getName()));
        }
        return dictionary;
    }

    @SuppressWarnings("unchecked")
    public List<Position> getAllPosition() {
        List resultList = entityManager.createQuery("select o from Position o order by o.name").getResultList();
        List<Position> list = resultList;
        return list;
    }
}
