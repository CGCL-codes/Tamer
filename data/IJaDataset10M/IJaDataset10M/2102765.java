package de.d3web.we.flow.type;

import de.knowwe.kdom.xml.XMLContent;

/**
 * 
 * 
 * @author hatko Created on: 09.10.2009
 */
public class EdgeContentType extends XMLContent {

    private static EdgeContentType instance;

    private EdgeContentType() {
    }

    public static EdgeContentType getInstance() {
        if (instance == null) instance = new EdgeContentType();
        return instance;
    }

    @Override
    protected void init() {
        this.childrenTypes.add(OriginType.getInstance());
        this.childrenTypes.add(TargetType.getInstance());
        this.childrenTypes.add(GuardType.getInstance());
    }
}
