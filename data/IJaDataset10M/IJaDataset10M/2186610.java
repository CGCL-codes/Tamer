package ru.scriptum.model.data;

import java.util.List;
import ru.scriptum.model.IRelation;
import ru.scriptum.model.domaindata.User;

public interface IElement extends IPersistable {

    void addRelation(IRelation relation);

    ISimplePoint getLocation();

    List<IRelation> getRelations(String relationType);

    List<IElement> getTargets(String relationType);

    List<IElement> getSources(String relationType);

    long getTimesAccessed();

    User getUser();

    boolean isLeaf(String relationType);

    boolean isRoot(String relationType);

    void removeRelation(IRelation relation);

    void setLocation(ISimplePoint p);

    void setRelations(List<IRelation> children);

    void setTimesAccessed(long i);

    void setUser(User user);
}
