package cn.ekuma.epos.core.client;

import com.smartgwt.client.widgets.Canvas;

public interface GWTPanelFactory {

    Canvas create();

    String getID();

    String getDescription();
}
