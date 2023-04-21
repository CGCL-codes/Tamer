package shellkk.qiq.jdm.engine;

import java.util.Collection;
import java.util.Date;
import javax.datamining.NamedObject;
import javax.datamining.Enum;
import shellkk.qiq.jdm.MiningObjectImpl;

public interface IPersistEngine {

    public NamedObject getPersistObjectType();

    public boolean doesObjectExist(String objname) throws Exception;

    public void saveObject(String objname, MiningObjectImpl obj, boolean overwrite) throws Exception;

    public MiningObjectImpl getObject(String objname) throws Exception;

    public MiningObjectImpl getObjectLightly(String objname) throws Exception;

    public void removeObject(String objname) throws Exception;

    public Date getCreationDate(String name) throws Exception;

    public String getDescription(String name) throws Exception;

    public Collection<String> getObjectNames(Date date1, Date date2) throws Exception;

    public Collection<String> getObjectNames(Date date1, Date date2, Enum minorType) throws Exception;

    public void renameObject(String oldname, String newname) throws Exception;

    public Collection<MiningObjectImpl> getObjects(Date date1, Date date2) throws Exception;

    public Collection<MiningObjectImpl> getObjectsLightly(Date date1, Date date2) throws Exception;

    public Collection<MiningObjectImpl> getObjects(Date date1, Date date2, Enum minorType) throws Exception;

    public Collection<MiningObjectImpl> getObjectsLightly(Date date1, Date date2, Enum minorType) throws Exception;

    public void setDescription(String name, String description) throws Exception;

    public String[] getLoadedObjectNames() throws Exception;

    public void requestObjectLoad(String name) throws Exception;

    public void requestObjectUnload(String name) throws Exception;

    public boolean updateIfPersisted(MiningObjectImpl obj) throws Exception;
}
