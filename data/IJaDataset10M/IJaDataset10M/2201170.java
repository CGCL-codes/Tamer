package net.sf.rcpforms.examples.complete.widgets;

import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Remo Loetscher
 *
 */
public class MasterDetail_DetailGroup {

    public RCPText name = new RCPText("name: "), birthDate = new RCPText("birthDate: "), age = new RCPText("age: "), overdrawAccount = new RCPText("overdrawAccount: "), childCount = new RCPText("childCount: "), accountBalance = new RCPText("accountBalance: "), gender = new RCPText("gender: ");

    public void createUI(FormToolkit toolkit, GridBuilder parentBuilder) {
        RCPGroup group = new RCPGroup("Selected object: ");
        GridBuilder groupBuilder = parentBuilder.addContainerSpan(group, 2, 2, 1, false);
        groupBuilder.addLineGrabAndFill(name, 1);
        groupBuilder.addLine(birthDate);
        birthDate.setState(EControlState.READONLY, true);
        groupBuilder.addLine(age);
        age.setState(EControlState.READONLY, true);
        groupBuilder.addLineGrabAndFill(overdrawAccount, 1);
        overdrawAccount.setState(EControlState.READONLY, true);
        groupBuilder.addLine(childCount);
        childCount.setState(EControlState.READONLY, true);
        groupBuilder.addLineGrabAndFill(accountBalance, 1);
        accountBalance.setState(EControlState.READONLY, true);
        groupBuilder.addLine(gender);
        gender.setState(EControlState.READONLY, true);
        toolkit.paintBordersFor(group.getClientComposite());
    }
}
