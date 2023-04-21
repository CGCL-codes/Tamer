package fr.ign.cogit.geoxygene.util.console;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.apache.ojb.broker.util.configuration.impl.OjbConfiguration;
import fr.ign.cogit.geoxygene.datatools.Geodatabase;
import fr.ign.cogit.geoxygene.datatools.ojb.GeodatabaseOjbFactory;
import fr.ign.cogit.geoxygene.util.loader.MetadataReader;
import fr.ign.cogit.geoxygene.util.loader.XMLJavaDicoGenerator;
import fr.ign.cogit.geoxygene.util.loader.gui.GUICompileMessage;
import fr.ign.cogit.geoxygene.util.loader.gui.GUIConfigOJBXMLJava;

/**
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.1
 * 
 */
class SqlToJava extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static List<String> allTables = new ArrayList<String>();

    private static String mappingFileName = "repository_geo.xml";

    private static String extentClassName = "fr.ign.cogit.geoxygene.feature.FT_Feature";

    private static String extentMappingFileName = null;

    private static String packageName = "geoxygene.geodata";

    private static String geOxygeneData;

    private static String geOxygeneMapping;

    protected static void action(int mapping) {
        System.out.println("Generation de mapping XML  et de classes java ... ");
        if (mapping == GeOxygeneConsole.CASTOR) {
            System.out.println("CASTOR : marche pas !");
            return;
        }
        try {
            OjbConfiguration config = new OjbConfiguration();
            File fileMapping = new File(config.getRepositoryFilename());
            Geodatabase data = GeodatabaseOjbFactory.newInstance();
            MetadataReader theMetadataReader = new MetadataReader(data);
            SqlToJava.allTables = theMetadataReader.getSelectedTables();
            if (SqlToJava.allTables.size() == 0) {
                System.out.println("Aucune table selectionnee ...");
                return;
            }
            System.out.println("Generation du mapping, des classes java et du dico ...");
            File tryFileData = new File(fileMapping.getParentFile().getParentFile().getParentFile(), "data");
            if (tryFileData.exists()) {
                SqlToJava.geOxygeneData = tryFileData.getPath();
            } else {
                tryFileData = new File(fileMapping.getParentFile().getParentFile().getParentFile().getParentFile(), "data");
                if (tryFileData.exists()) {
                    SqlToJava.geOxygeneData = tryFileData.getPath();
                }
            }
            SqlToJava.geOxygeneMapping = fileMapping.getParentFile().getPath();
            GUIConfigOJBXMLJava configuration = new GUIConfigOJBXMLJava(SqlToJava.packageName, SqlToJava.geOxygeneData, SqlToJava.geOxygeneMapping, SqlToJava.extentClassName, SqlToJava.mappingFileName, SqlToJava.extentMappingFileName);
            String[] selectedValues = configuration.showDialog();
            if (selectedValues[2] == null) {
                return;
            }
            XMLJavaDicoGenerator generator = new XMLJavaDicoGenerator(null, data, false, SqlToJava.allTables, selectedValues[0], selectedValues[1], selectedValues[2], selectedValues[3], selectedValues[4], selectedValues[5]);
            generator.writeAll();
            GUICompileMessage message = new GUICompileMessage();
            message.showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
