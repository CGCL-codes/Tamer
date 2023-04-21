package com.kenstevens.stratdom.control;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.main.Spring;
import com.kenstevens.stratdom.site.action.ActionFactory;
import com.kenstevens.stratdom.ui.BattleTabItem;

@Scope("prototype")
@Component
public class BattleTabItemControl implements TopLevelController {

    @Autowired
    private ActionFactory actionFactory;

    @Autowired
    private Spring spring;

    private BattleLogTableControl battleLogTableControl;

    private BattleTabItem battleTabItem;

    public BattleTabItemControl(BattleTabItem battleTabItem) {
        this.battleTabItem = battleTabItem;
        setButtonListeners();
    }

    private void setButtonListeners() {
        battleTabItem.getUpdateButton().addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                try {
                    actionFactory.battleLog();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void setControllers() {
        battleLogTableControl = spring.getBean(BattleLogTableControl.class, new Object[] { battleTabItem.getBattleLogTable() });
    }

    public void setContents() {
        battleLogTableControl.setContents();
    }
}
