package vse.core;

import java.util.Iterator;
import com.conicsoft.bdkJ.core.EventObj;
import com.conicsoft.bdkJ.core.IEventObject;
import com.conicsoft.bdkJ.gui.propertyitem_data;
import com.conicsoft.bdkJ.gui.propertyitem_group;
import com.conicsoft.bdkJ.gui.propertyitem_single;

class ControlProperty implements IControlProperty {

    private IControl m_owner = null;

    protected propertyitem_group m_root = new propertyitem_group() {

        {
            connect("on_single_data_changed", new IEventObject() {

                @Override
                public void main(EventObj obj) {
                    __emit_propchanged_evet(obj);
                }
            });
        }
    };

    protected void __emit_propchanged_evet(EventObj obj) {
        if (m_owner != null) {
            m_owner.emit_event(Defines.EVT_PROPCHANGED, obj);
        }
    }

    public ControlProperty(IControl __owner) {
        m_owner = __owner;
    }

    @Override
    public propertyitem_single add(propertyitem_single item) {
        m_root.add(item);
        return item;
    }

    @Override
    public propertyitem_single at(int id) {
        return m_root.at(id);
    }

    @Override
    public void clear() {
        m_root.clear();
    }

    @Override
    public int size() {
        return m_root.size();
    }

    @Override
    public Iterator<propertyitem_single> iterator() {
        return m_root.iterator();
    }

    @Override
    public propertyitem_group add(propertyitem_group item) {
        m_root.add(item);
        return item;
    }

    @Override
    public IControl owner() {
        return m_owner;
    }

    @Override
    public propertyitem_data find(String path) {
        String[] result = path.split("::");
        if (result.length == 0) return null;
        propertyitem_single p_single = m_root.find(result[0]);
        if (p_single == null) return null;
        if (result.length == 1) return p_single.data();
        for (int i = 1; i < result.length; ++i) {
            if ((p_single instanceof propertyitem_group) == false) return null;
            propertyitem_group p_grp = (propertyitem_group) p_single;
            p_single = p_grp.find(result[i]);
        }
        if (p_single != null) return p_single.data();
        return null;
    }
}
