package com.mapbased.sfw.component;

import org.dom4j.Element;
import com.mapbased.sfw.model.Fragment;
import com.mapbased.sfw.model.LoadContext;
import com.mapbased.sfw.model.RenderContext;
import com.mapbased.sfw.model.Renderable;

public abstract class Component implements Renderable, Xmlable {

    public int mapVar(String name) {
        return -1;
    }

    /**
	 * 可以触发的服务器端事件名字列表
	 * 
	 * @return
	 */
    public String[] getEventNames() {
        return null;
    }

    /**
	 * 处理事件
	 * 
	 * @param rc
	 * @param evtName
	 */
    public void handleEvent(RenderContext rc, String evtName) {
    }

    public void setContainer(Fragment holder) {
    }

    ;

    /**
	 * todo
	 * 
	 * @param name
	 * @param value
	 */
    public final void setProperty(String name, Object value) {
        try {
            this.getClass().getField(name).set(this, value);
        } catch (Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public final Object getProperty(String name) {
        try {
            return this.getClass().getField(name).get(this);
        } catch (Exception e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public final void parseProperties(Property[] properties, Element ele, LoadContext lc) {
        for (Property p : properties) {
            p.parseXml(ele, lc, this);
        }
    }

    public final void xmlProperties(Property[] properties, XmlContext xc) {
        for (Property p : properties) {
            xc.sb.append(" ");
            p.toXml(xc, this);
        }
    }
}
