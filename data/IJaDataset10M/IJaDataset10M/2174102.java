package org.osmius.dao;

import java.util.List;

public interface OsmMessageDao extends Dao {

    public List getNonReadedOsmMessages();

    public List getOsmMessages();

    public void setOsmMessagesReaded();

    public void deleteOsmMessageById(String idnMessage);
}
