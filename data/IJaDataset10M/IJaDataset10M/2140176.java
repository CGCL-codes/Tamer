package com.loribel.tools.maven.bo;

import java.io.StringWriter;
import com.loribel.commons.GB_FwkGuiInitializer;

/**
 * Classe demo pour GB_Bo2Sql
 * 
 * @author Gregory Borelli
 * @author Patrice Gagnon
 */
public class GB_BO2SqlDemo {

    /**
     * M�thode qui appelle la classe GB_Bo2Sql pour g�n�rer les crate table de BOMetaData etBOProperty
     * et imprimer le r�sultat � l'�cran
     * @param args 
     */
    public static void main(String[] args) {
        GB_FwkGuiInitializer.initAll();
        StringWriter writer = new StringWriter();
        GB_BO2Sql.setUseDropTable(true);
        GB_BO2Sql.writeSQL(new String[] { "BOMetaData", "BOProperty" }, writer);
        System.out.println(writer);
    }
}
