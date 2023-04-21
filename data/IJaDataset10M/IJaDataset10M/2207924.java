package gxbind.extjs.ext.config;

import gxbind.extjs.ext.ExtElement;
import gxbind.extjs.ext.Offset;
import gxbind.extjs.ext.Toolbar;
import java.util.Map;
import com.google.gwt.user.client.Element;

public interface IGridPanelWritable {

    void setAdjustments(Offset offset);

    void setAutoCreate(boolean isAutoCreate);

    void setAutoCreate(Map autoCreateElementSpec);

    void setAutoScroll(boolean isAutoScroll);

    void setBackground(boolean isBackground);

    void setClosable(boolean isClosable);

    void setFitContainer(boolean isFitContainer);

    void setFitToFrame(boolean isFitToFrame);

    void setLoadOnce(boolean isLoadOnce);

    void setParams(String params);

    void setParams(Map params);

    void setResizeEl(String elementId);

    void setResizeEl(Element element);

    void setResizeEl(ExtElement element);

    void setTitle(String title);

    void setToolbar(Toolbar toolbar);

    void setUrl(String url);
}
