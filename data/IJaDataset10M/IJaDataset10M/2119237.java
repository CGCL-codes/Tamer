package cn.ekuma.epos.datalogic.define.dao;

import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.model.Field;
import com.openbravo.format.Formats;
import cn.ekuma.data.dao.BaseDAO;
import cn.ekuma.epos.symmetricds.AppConfig;
import cn.ekuma.epos.symmetricds.bean.NodeChannelCtl;

public class NodeChannelCtlDAO extends BaseDAO<NodeChannelCtl> {

    public NodeChannelCtlDAO(I_Session s) {
        super(s);
    }

    @Override
    public NodeChannelCtl readValues(DataRead dr, NodeChannelCtl obj) throws BasicException {
        if (obj == null) obj = new NodeChannelCtl();
        obj.setNodeId(dr.getString(1));
        obj.setChannelId(dr.getString(2));
        obj.setSuspendEnabled(dr.getBoolean(3));
        obj.setIgnoreEnabled(dr.getBoolean(4));
        obj.setLastExtractTime(dr.getTimestamp(5));
        return obj;
    }

    @Override
    public void writeInsertValues(DataWrite dp, NodeChannelCtl obj) throws BasicException {
        dp.setString(1, obj.getNodeId());
        dp.setString(2, obj.getChannelId());
        dp.setBoolean(3, obj.isSuspendEnabled());
        dp.setBoolean(4, obj.isIgnoreEnabled());
        dp.setTimestamp(5, obj.getLastExtractTime());
    }

    @Override
    public TableDefinition getTable() {
        return new TableDefinition(s, AppConfig.getAppProperty(AppConfig.TABLE_PREFIX) + "_" + "node_channel_ctl", new Field[] { new Field("node_id", Datas.STRING, Formats.STRING), new Field("channel_id", Datas.STRING, Formats.STRING), new Field("suspend_enabled", Datas.BOOLEAN, Formats.BOOLEAN), new Field("ignore_enabled", Datas.BOOLEAN, Formats.BOOLEAN), new Field("last_extract_time", Datas.TIMESTAMP, Formats.TIMESTAMP) }, new int[] { 0 });
    }

    /**
	 *  <table name="node_channel_ctl" description="Used to ignore or suspend a channel. A channel that is ignored will have its data_events batched and they will immediately be marked as 'OK' without sending them. A channel that is suspended is skipped when batching data_events.">
        <column name="node_id" type="VARCHAR" size="50" required="true" primaryKey="true"  description="Unique identifier for a node." />
        <column name="channel_id" type="VARCHAR" size="20" required="true" primaryKey="true"  description="The name of the channel_id that is being controlled." />
        <column name="suspend_enabled" type="BOOLEANINT" size="1" default="0"  description="Indicates if this channel is suspended, which prevents its Data Events from being batched." />
        <column name="ignore_enabled" type="BOOLEANINT" size="1" default="0"  description="Indicates if this channel is ignored, which marks its Data Events as if they were actually processed." />
        <column name="last_extract_time" type="TIMESTAMP"  description="Record the last time data was extract for a node and a channel." />
    </table>
	 */
    @Override
    public Class getSuportClass() {
        return NodeChannelCtl.class;
    }
}
