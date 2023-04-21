package net.sourceforge.smarthomephone.automationactionlet;

import java.util.ResourceBundle;
import java.util.Set;
import net.sourceforge.smarthomephone.entities.Automationparameter;

public interface Actionlet {

    public String showModuleAutomation(String parameters, ResourceBundle transBundle);

    public void execute(Set<Automationparameter> paramSet);
}
