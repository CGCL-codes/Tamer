package net.sourceforge.modelintegra.core.reporting.snippet;

import java.util.Iterator;
import net.sourceforge.modelintegra.core.metamodel.mimodel.DataDictionary;
import net.sourceforge.modelintegra.core.metamodel.mimodel.MINamedElement;
import org.eclipse.emf.common.util.EList;

public class ReportGlossary {

    public static String print(DataDictionary pGlossary) {
        String report = "";
        if (pGlossary == null) {
            throw new RuntimeException();
        }
        EList listEntity = pGlossary.getEntity();
        EList listEnumeration = pGlossary.getMienumeration();
        EList listActor = pGlossary.getUcactor();
        EList listUC = pGlossary.getUc();
        report += "<p>\n" + "<h1>Akteure</h1>\n" + "</p>\n";
        Iterator iterActor = listActor.iterator();
        while (iterActor.hasNext()) {
            MINamedElement ne = (MINamedElement) iterActor.next();
            report += "<p>\n" + "<b>" + ne.getName() + "</b><br>\n" + "" + ne.getDocumentation() + "\n" + "</p>\n";
        }
        report += "<p>\n" + "<h1>Entit�ten</h1>\n" + "</p>\n";
        Iterator iterEntity = listEntity.iterator();
        while (iterEntity.hasNext()) {
            MINamedElement ne = (MINamedElement) iterEntity.next();
            report += "<p>\n" + "<b>" + ne.getName() + "</b><br>\n" + "" + ne.getDocumentation() + "\n" + "</p>\n";
        }
        report += "<p>\n" + "<h1>Aufz�hlungen</h1>\n" + "</p>\n";
        Iterator iterEnumeration = listEnumeration.iterator();
        while (iterEnumeration.hasNext()) {
            MINamedElement ne = (MINamedElement) iterEnumeration.next();
            report += "<p>\n" + "<b>" + ne.getName() + "</b><br>\n" + "" + ne.getDocumentation() + "\n" + "</p>\n";
        }
        report += "<p>\n" + "<h1>Use Cases</h1>\n" + "</p>\n";
        Iterator iterUC = listUC.iterator();
        while (iterUC.hasNext()) {
            MINamedElement ne = (MINamedElement) iterUC.next();
            report += "<p>\n" + "<b>" + ne.getName() + "</b><br>\n" + "" + ne.getDocumentation() + "\n" + "</p>\n";
        }
        return report;
    }
}
