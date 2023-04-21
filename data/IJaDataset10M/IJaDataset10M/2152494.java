package net.rptools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import net.rptools.inittool.db.PropertyTable;
import net.rptools.inittool.db.PropertyTableDescriptor;
import net.rptools.inittool.db.PropertyTableXML;
import net.rptools.inittool.db.PropertyTable.PropertyMap;
import net.rptools.inittool.ui.AddCombatantDialog;

/**
 * A test for reading RP Tools data from XML files.
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
public class TestRPToolsXML {

    /**
   * Load a rp tools data file.
   * 
   * @param args name of the file or a command name and file.
   * @throws IOException
   */
    public static void main(String[] args) throws IOException {
        String cmd = "display";
        String file = args[0];
        if (args.length > 1) {
            cmd = args[0];
            file = args[1];
        }
        if (cmd.equalsIgnoreCase("delete")) {
            PropertyTable.deletePropertyTable(AddCombatantDialog.DB_NAME, AddCombatantDialog.TABLE_NAME);
            return;
        }
        PropertyTable table = null;
        PropertyTableXML xml = PropertyTableXML.getInstance();
        Reader reader = new BufferedReader(new FileReader(file));
        PropertyTableDescriptor ptd = xml.readDescriptor(reader);
        if (cmd.equalsIgnoreCase("display")) {
            System.out.println(ptd.toString());
        } else if (cmd.equalsIgnoreCase("create")) {
            table = new PropertyTable(AddCombatantDialog.DB_NAME, AddCombatantDialog.TABLE_NAME, ptd);
        }
        reader.close();
        reader = new BufferedReader(new FileReader(file));
        if (cmd.equalsIgnoreCase("display")) {
            List<PropertyMap> maps = xml.loadXML(reader);
            System.out.println("\n\nMaps: ");
            for (PropertyMap map : maps) {
                System.out.println(map.toString());
            }
        } else if (cmd.equalsIgnoreCase("create") || cmd.equalsIgnoreCase("load")) {
            xml.loadXML(reader, table);
        }
    }
}
