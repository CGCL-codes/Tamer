package net.sf.ideoreport.freemarker.tests;

import java.util.HashMap;
import java.util.Map;
import freemarker.template.SimpleSequence;

/**
 * @author ACOLAS
 *
 */
public class FreemarkerHardCodedData extends AbstractFreemarkerGenericTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * constructeur
	 *
	 */
    public FreemarkerHardCodedData() {
        super();
    }

    /**
	 * m�thode remplissant les donn�es pour Test
	 * @return Map comportant toute l'arborescence des donn�es
	 * @throws Exception
	 */
    private Map fillData() {
        Map forfait = new HashMap();
        forfait.put("titre", "Suivi des forfaits");
        Map theme1 = new HashMap();
        forfait.put("theme", theme1);
        theme1.put("titre2", "General");
        Map fils1 = new HashMap();
        theme1.put("fils1", fils1);
        fils1.put("titre3", "Note CMMI");
        fils1.put("data1", new Float(7.2));
        fils1.put("data2", new Float(4.2));
        fils1.put("data3", new Float(6.2));
        fils1.put("data4", new Float(5.2));
        fils1.put("data5", new Float(8.2));
        Map fils2 = new HashMap();
        theme1.put("fils2", fils2);
        fils2.put("titre3", "Taux Marge");
        fils2.put("data1", new Integer(10));
        fils2.put("data2", new Integer(15));
        fils2.put("data3", new Integer(12));
        fils2.put("data4", new Integer(14));
        fils2.put("data5", new Integer(15));
        Map fils3 = new HashMap();
        theme1.put("fils3", fils3);
        fils3.put("titre3", "CA Initial");
        fils3.put("data1", new Integer(100));
        fils3.put("data2", new Integer(100));
        fils3.put("data3", new Integer(100));
        fils3.put("data4", new Integer(100));
        fils3.put("data5", new Integer(100));
        Map fils4 = new HashMap();
        theme1.put("fils4", fils4);
        fils4.put("titre3", "CA Avenant");
        fils4.put("data1", new Integer(10));
        fils4.put("data2", new Integer(45));
        fils4.put("data3", new Integer(45));
        fils4.put("data4", new Integer(89));
        fils4.put("data5", new Integer(56));
        return forfait;
    }

    protected Map getDataModel() {
        Map root = new HashMap();
        root.put("titre", "Test IdeoReport");
        root.put("titre_tableau", "Tableau 1 : Test d'un tableau avec Freemarker");
        root.put("totalSQLI", "Total SQLI");
        root.put("border_thin", "<style> table, th, td, thead, tfoot " + "{ border:thin solid;border-spacing:0px;}</style>");
        root.put("border_medium", "<style> table, th, td, thead, tfoot  " + "{ border:medium solid;border-spacing:0px;}</style>");
        root.put("border_large", "<style>table, th, td, thead, tfoot " + "{ border:5px solid;border-spacing:0px;}</style>");
        root.put("th_orange", "<style> th " + "{background-color:#ec6900}</style>");
        root.put("th_yellow", "<style> th " + "{background-color:#ffff00}</style>");
        root.put("th_green", "<style> th " + "{background-color:#00ff00}</style>");
        root.put("tfoot_orange", "<style> tfoot " + "{background-color:#ec6900}</style>");
        root.put("tfoot_yellow", "<style> tfoot " + "{background-color:#ffff00}</style>");
        root.put("tfoot_green", "<style> tfoot " + "{background-color:#00ff00}</style>");
        root.put("border_color_black", "<style> table, td, th, thead, tfoot " + "{ border-color:black;}</style>");
        root.put("border_color_green", "<style> table, td, th, th, thead, tfoot  " + "{ border-color:green;}</style>");
        root.put("border_color_red", "<style> table, td, th, thead, tfoot " + "{ border-color:red;}</style>");
        root.put("font_color_red", "<style> table, td, thead, body  " + "{color:red;}</style>");
        root.put("font_color_green", "<style> table, td, thead, body  " + "{color:green;}</style>");
        root.put("font_color_black", "<style> table, td, thead, body " + "{color:black;}</style>");
        root.put("font_size_thin", "<style> table, td, thead, th, tfoot, body " + "{font-size:8pt}</style>");
        root.put("font_size_medium", "<style> table,td, thead, th, tfoot, body  " + "{font-size:12pt}</style>");
        root.put("font_size_large", "<style> table, td, thead, th, tfoot, body  " + "{font-size:16pt}</style>");
        root.put("font_verdana", "<style> table, td, th, thead, tfoot, body  " + "{font-family: Verdana;}</style>");
        root.put("font_arial", "<style> table, td, th, thead, tfoot, body  " + "{font-family: Arial;}</style>");
        root.put("font_times", "<style> table, td, th, thead, tfoot, body  " + "{font-family: Times New Roman;}</style>");
        root.put("font_style_thin", "<style> table,td, th, thead, tfoot, body " + "{font-weight:lighter}</style>");
        root.put("font_style_medium", "<style> table, th, td, thead, tfoot, body " + "{font-weight:normal}</style>");
        root.put("font_style_large", "<style> table, th, td, thead, tfoot, body " + "{font-weight:bold}</style>");
        root.put("tfoot_font_orange", "<style> .footer " + "{color:red;}</style>");
        root.put("tfoot_font_green;", "<style> .footer " + "{color:green;}</style>");
        root.put("tfoot_font_black", "<style> .footer " + "{color:black;}</style>");
        root.put("colorCellTableau_orange", "<style> .cellTableau " + "{background-color:#f7edd2}</style>");
        root.put("colorCellTableau_blue", "<style> .cellTableau " + "{background-color:#c7e3f6}</style>");
        root.put("colorCellTableau_green", "<style> .cellTableau " + "{background-color:#ccedd2}</style>");
        root.put("colorCellTableau_NOK", "<style> .cellTableauNOK " + "{background-color:#ff0000}</style>");
        root.put("colorCellTableau_OK", "<style> .cellTableauOK " + "{background-color:#00ff00}</style>");
        root.put("colorCellTableau_NOK_orange", "<style> .cellTableauNOK_orange " + "{background-color:#ec6900}</style>");
        SimpleSequence forfaitsList = new SimpleSequence();
        forfaitsList.add(fillData());
        root.put("forfaits", forfaitsList);
        return root;
    }
}
