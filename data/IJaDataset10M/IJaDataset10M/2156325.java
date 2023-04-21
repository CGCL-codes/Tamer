package org.gwings.client.demo;

import org.gwings.client.ui.ColorChooser;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

/**
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
 * @author Marcelo Emanoel, Luciano Broussal
 * @since 11/03/2007
 */
public class ColorChooserTab extends AbstractDemoPanel {

    private ColorChooser colorChooser;

    private FlexTable layout;

    public ColorChooserTab() {
        initialize();
        setupUI();
        setupStyles();
        setupListeners();
    }

    private void initialize() {
        colorChooser = new ColorChooser();
        layout = new FlexTable();
    }

    private void setupUI() {
        add(layout);
        layout.setWidget(1, 1, colorChooser);
        colorChooser.setPixelSize(500, 100);
    }

    private void setupStyles() {
    }

    private void setupListeners() {
    }

    public HTML getLinks() {
        return new HTML();
    }

    public FlexTable getProperties() {
        return new FlexTable();
    }
}
