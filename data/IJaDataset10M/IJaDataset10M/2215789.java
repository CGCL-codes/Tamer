package mic.rules;

import toxTree.tree.rules.StructureAlertCDK;
import ambit2.smarts.query.SMARTSException;

public class SA29 extends StructureAlertCDK {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5061212731819103939L;

    public static String SA29 = "Aromatic diazo";

    public static String SA29_smarts = "a[N]=[N]a";

    public static String SA29_noSO3H = "[$(a:a(S(=[OX1])(=[OX1])([O-,OX2H1]))),$(a:a:a(S(=[OX1])(=[OX1])([O-,OX2H1]))),$(a:a:a:a(S(=[OX1])(=[OX1])([O-,OX2H1]))),$(a:a:a:a:a(S(=[OX1])(=[OX1])([O-,OX2H1])))][N]=[N][$(a:a(S(=[OX1])(=[OX1])([O-,OX2H1]))),$(a:a:a(S(=[OX1])(=[OX1])([O-,OX2H1]))),$(a:a:a:a(S(=[OX1])(=[OX1])([O-,OX2H1]))),$(a:a:a:a:a(S(=[OX1])(=[OX1])([O-,OX2H1])))]";

    public SA29() {
        super();
        try {
            setContainsAllSubstructures(true);
            addSubstructure(SA29, SA29_smarts);
            addSubstructure("aromatic diazo with sulfonic group on both  rings", SA29_noSO3H, true);
            setID("SA29");
            setTitle(SA29);
            StringBuffer e = new StringBuffer();
            e.append("<html>");
            e.append(SA29);
            e.append("<ul>");
            e.append("<li>");
            e.append("If a sulfonic acid group (-SO3H) is present on each of the rings that contain the diazo group, the substance should be not classified.");
            e.append("</ul>");
            e.append("</html>");
            setExplanation(e.toString());
            examples[0] = "O=C(Nc4=cc=c(c5=cc(=c(N=Nc2=cc=c(N=Nc1=cc=c(c=c1)S(=O)(=O)O)c3=cc=c(c=c23)S(=O)(=O)O)c(O)=c45)S(=O)(=O)O)S(=O)(=O)O)C";
            examples[1] = "N(=Nc1=cc=cc=c1)c2=cc=cc=c2";
            editable = false;
        } catch (SMARTSException x) {
            logger.error(x);
        }
    }
}
