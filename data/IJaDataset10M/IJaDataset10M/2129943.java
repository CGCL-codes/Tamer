package vehikel.codeconverter;

import org.antlr.runtime.tree.CommonTree;
import vehikel.datamodel.IMessageArray;
import vehikel.hal.build.BuildResultsModel;

/**
 * Repository für ein DSL Programm.
 * @author Thomas Jourdan
 */
public class ConverterResultsModel extends BuildResultsModel implements IMessageArray {

    /**
	 * Kürzel der verwendeten Roboterplattform.
	 */
    public String targetName;

    /**
	 * Verwendete Konfiguration.
	 */
    public String configurationName;

    /**
	 * Generierter Text in der Systeminplementierungssprache.
	 */
    public String targetCode;

    /**
	 * Namensverwaltung für die Namen der DSL..
	 */
    public NameList nameList = new NameList();

    /**
	 * Abstract Syntax Tree der DSL
	 */
    public CommonTree ast;
}
