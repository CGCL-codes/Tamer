package org.gwings.client.demo;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright 2007 Marcelo Emanoel B. Diniz <marceloemanoel AT gmail.com>
 *
 * @author Marcelo Emanoel
 * @since 07/03/2007
 */
public class GWingsDemo implements EntryPoint, WindowResizeListener {

    private TabPanel panel;

    private DocStack stack;

    private DockPanel layout;

    public void onModuleLoad() {
        Window.enableScrolling(false);
        Window.setMargin("5px");
        Window.addWindowResizeListener(this);
        panel = new TabPanel();
        stack = new DocStack();
        layout = new DockPanel();
        panel.add(new TableTab(), "Table");
        panel.add(new PaginableTableTab(), "Paginable Table");
        panel.add(new ListSelectorTab(), "Selector");
        panel.add(new SliderTab(), "Slider");
        panel.add(new SpinnerTab(), "Spinner");
        panel.add(new QueryBoxTab(), "QueryBox");
        panel.add(new ColorChooserTab(), "Color Chooser");
        panel.add(new RoundBoxTab(), "Round Box");
        DeferredCommand.add(new Command() {

            public void execute() {
                int width = Window.getClientWidth();
                int height = Window.getClientHeight();
                onWindowResized(width, height);
                panel.selectTab(0);
            }
        });
        panel.addTabListener(new TabListener() {

            public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
                AbstractDemoPanel demoPanel = (AbstractDemoPanel) panel.getWidget(tabIndex);
                setComponentLinks(demoPanel.getLinks());
                setProperties(demoPanel.getProperties());
                stack.showStack(0);
            }

            public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex) {
                return true;
            }
        });
        layout.setVerticalAlignment(VerticalPanel.ALIGN_BOTTOM);
        layout.add(panel, DockPanel.CENTER);
        layout.add(stack, DockPanel.EAST);
        layout.setCellWidth(panel, "69%");
        layout.setCellHeight(panel, "100%");
        layout.setCellWidth(stack, "39%");
        layout.setCellHeight(stack, "100%");
        layout.setSpacing(4);
        layout.setSize("100%", "100%");
        panel.setSize("100%", "100%");
        stack.setSize("100%", "100%");
        RootPanel.get().add(layout);
    }

    public void onWindowResized(int width, int height) {
        layout.setPixelSize(width - 10, height - 10);
    }

    public void setProperties(FlexTable propertiesTable) {
        stack.setTableProperties(propertiesTable);
    }

    public void setComponentLinks(HTML links) {
        stack.setLinks(links);
    }
}
