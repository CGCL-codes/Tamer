package com.mobileares.midp.widgets.client.frame;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-3-16
 * Time: 16:13:59
 * To change this template use File | Settings | File Templates.
 */
public class Frame extends Composite {

    public interface LayoutListener {

        void layout(int width, int height);
    }

    private FlowPanel wrapper = new FlowPanel();

    private Header header;

    private MainPanel main;

    private boolean autoHeight = false;

    private int bodyHeight = 0;

    private List<LayoutListener> listeners = new ArrayList();

    public void addListener(LayoutListener listener) {
        listeners.add(listener);
    }

    public Header getHeader() {
        return header;
    }

    public MainPanel getMain() {
        return main;
    }

    public Frame() {
        wrapper.setStyleName("frame-wrapper");
        initWidget(wrapper);
        initHeader();
        initMain();
        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(ResizeEvent resizeEvent) {
                reSize();
            }
        });
    }

    public int getBodyHeight() {
        return height;
    }

    private int height;

    public void reSize() {
        DeferredCommand.addCommand(new Command() {

            public void execute() {
                height = Window.getClientHeight() - header.getOffsetHeight() - 12;
                for (LayoutListener listener : listeners) {
                    listener.layout(Window.getClientWidth(), height);
                }
                main.menuContainer.setHeight(height - 10);
                if (autoHeight) {
                    if (bodyHeight < height) {
                        main.bodyContainer.setHeight(height - 10 + "px");
                    } else main.bodyContainer.setHeight(bodyHeight + "px");
                } else main.bodyContainer.setHeight(height - 10);
            }
        });
    }

    private void initHeader() {
        header = new Header();
        wrapper.add(header);
    }

    private void initMain() {
        main = new MainPanel();
        wrapper.add(main);
    }

    private void initMenu() {
    }

    private void initContent() {
    }

    private void initFooter() {
    }

    @Override
    protected void onLoad() {
        reSize();
    }

    public void setAutoHeight(boolean flag) {
        autoHeight = flag;
    }
}
