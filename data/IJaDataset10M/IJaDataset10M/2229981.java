package org.jowidgets.examples.common.workbench.demo2.workbench.command;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.tools.command.ActionBuilder;
import org.jowidgets.tools.command.CommandAction;

public class CopyAction extends CommandAction {

    public CopyAction() {
        super(new ActionBuilder() {

            {
                setText("Copy");
                setToolTipText("Copy");
                setIcon(SilkIcons.PAGE_COPY);
                setAccelerator('C', Modifier.CTRL);
                setMnemonic('C');
            }
        });
        setCommand(new DummyCommandExecuter());
    }
}
