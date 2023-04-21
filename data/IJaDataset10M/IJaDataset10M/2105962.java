package Sample1.HumanInterfaceComponent.sample_cliente;

import javax.swing.*;
import java.awt.*;
import KFramework30.Widgets.*;
import KFramework30.Base.*;
import KFramework30.Communication.persistentObjectManagerClass;
import ProblemDomainComponent.sample_clientClass;
import java.util.HashMap;

public class clientBrowserClass extends KDataBrowserBaseClass {

    public clientBrowserClass(KConfigurationClass configurationParam, KLogClass logParam, JTable tableParam, java.awt.Window parentWindow) throws KExceptionClass {
        super(configurationParam, logParam, true, tableParam, parentWindow, sample_clientClass.class, clientEditDialogClass.class);
    }

    public void initializeTable() throws KExceptionClass {
        super.initializeSQLQuery(" client_id , client_name, client_address, CLIENT_EXPRESS_DELIVERY, CLIENT_DISCOUNT ", " sample_client cli ", "CLIENT_ID");
        setColumnNames("cli", "CLIENT_ID", "id");
        setColumnNames("cli", "CLIENT_NAME", "Name");
        setColumnNames("cli", "CLIENT_ADDRESS", "Address");
        setColumnNames("cli", "CLIENT_EXPRESS_DELIVERY", "Express");
        setColumnNames("cli", "CLIENT_DISCOUNT", "Discount");
        setDefaultOrder("  client_name ");
        super.initializeTable();
        adjustColumnWidth("Name", 100);
        adjustColumnWidth("Address", 200);
    }
}
