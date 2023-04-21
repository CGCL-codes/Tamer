package org.amse.tanks.view;

import org.amse.tanks.model.Model;

public interface View {

    void update(Model model);

    String getStr();
}
