package fr.enterprisesuite.test.unitaire.metier.admin;

import fr.enterprisesuite.back.administration.collaborateur.metier.GestionCollaborateurInterface;
import fr.enterprisesuite.back.administration.enums.StateEnum;
import fr.enterprisesuite.back.administration.persistence.vo.Adresse;
import fr.enterprisesuite.back.administration.persistence.vo.Collaborateur;
import java.sql.PreparedStatement;
import java.util.Date;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>
 * Description : .
 * </p>
 * @author $Author$
 * @version $Revision$
 */
public class CollaborateurMetierTest extends TestCase {

    /** */
    @SuppressWarnings("unused")
    private static final org.apache.commons.logging.Log LOGGER = org.apache.commons.logging.LogFactory.getLog(CollaborateurMetierTest.class.getName());

    /** */
    private static GestionCollaborateurInterface mCollaborateurMetier;

    static {
        try {
            ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationAdminContext.xml");
            mCollaborateurMetier = (GestionCollaborateurInterface) ctx.getBean("collaborateurMetier");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Exception during JPA EntityManager instanciation.");
        }
    }

    /** */
    private int mIdCollaborateur;

    /**
     * Cree un nouvel objet GestionClientTest .
     *
     * @param testName
     */
    public CollaborateurMetierTest(String testName) {
        super(testName);
    }

    /**
     *  Methode :  .
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/flex_enterprise_suite?user=root");
            conn.setAutoCommit(true);
            PreparedStatement vDelContratProfil = conn.prepareStatement("delete from Adresse where ADR_ID_ADRESSE = (SELECT ADR_COLLABORATEUR FROM COLLABORATEUR WHERE COL_ID_COLLABORATEUR=?)");
            vDelContratProfil.setInt(1, mIdCollaborateur);
            vDelContratProfil.execute();
            vDelContratProfil = conn.prepareStatement("delete from collaborateur where COL_ID_COLLABORATEUR=?");
            vDelContratProfil.setInt(1, mIdCollaborateur);
            vDelContratProfil.execute();
        } catch (Exception e) {
            System.out.println("Connection ratee: " + e);
        }
    }

    /**
     *  Methode :  .
     */
    public void testCreerCollaborateur() {
        final Collaborateur vCollaborateur = getCollaborateur();
        mIdCollaborateur = mCollaborateurMetier.creerCollaborateur(vCollaborateur);
    }

    /**
     *  Methode :  .
     *
     * @return
     */
    private Collaborateur getCollaborateur() {
        final Collaborateur vCollaborateur = new Collaborateur();
        final Adresse vAdresse = new Adresse();
        vAdresse.setCodePostal("75009");
        vAdresse.setDeuxLigne("deuxieme ligne");
        vAdresse.setPays("France");
        vAdresse.setPremLigne("prem ligne");
        vAdresse.setVille("Paris");
        vCollaborateur.setAdresseCollaborateur(vAdresse);
        vCollaborateur.setCoutMoyen(500);
        vCollaborateur.setDateModifCout(new Date());
        vCollaborateur.setEmailCollaborateur("jho@tutu.fr");
        vCollaborateur.setMatricule("toto");
        vCollaborateur.setNom("Hornecker");
        vCollaborateur.setPrenom("Jean");
        vCollaborateur.setRoleCollaborateur("toto");
        vCollaborateur.setStatutCollaborateur(StateEnum.ACTIF.toString());
        return vCollaborateur;
    }

    /**
     *  Methode :  .
     */
    public void testRechercherCollaborateurById() {
        mIdCollaborateur = mCollaborateurMetier.creerCollaborateur(getCollaborateur());
        final Collaborateur vCollaborateur = mCollaborateurMetier.rechercherCollaborateurById(mIdCollaborateur);
        assertEquals(vCollaborateur.getIdCollaborateur(), mIdCollaborateur);
        assertEquals(vCollaborateur.getAdresseCollaborateur().getCodePostal(), "75009");
    }

    /**
     *  Methode :  .
     */
    public void testModifierCollaborateur() {
        final Collaborateur vCollaborateur = getCollaborateur();
        mIdCollaborateur = mCollaborateurMetier.creerCollaborateur(vCollaborateur);
        Collaborateur vCollaborateur2 = mCollaborateurMetier.rechercherCollaborateurById(mIdCollaborateur);
        vCollaborateur2.setNom("NouveauNom");
        vCollaborateur2.getAdresseCollaborateur().setCodePostal("75010");
        mCollaborateurMetier.modifierCollaborateur(vCollaborateur2);
        Collaborateur vCollaborateur3 = mCollaborateurMetier.rechercherCollaborateurById(mIdCollaborateur);
        assertEquals(vCollaborateur3.getNom(), "NouveauNom");
        assertEquals(vCollaborateur3.getAdresseCollaborateur().getCodePostal(), "75010");
    }

    /**
     *  Methode :  .
     */
    public void testDeleteCollaborateur() {
        try {
            final Collaborateur vCollaborateur = getCollaborateur();
            vCollaborateur.setStatutCollaborateur(StateEnum.INACTIF.toString());
            mIdCollaborateur = mCollaborateurMetier.creerCollaborateur(vCollaborateur);
            mCollaborateurMetier.deleteCollaborateurById(vCollaborateur.getIdCollaborateur());
            Collaborateur vCollaborateur2 = mCollaborateurMetier.rechercherCollaborateurById(mIdCollaborateur);
            assertNull(vCollaborateur2);
        } catch (Exception vEx) {
            vEx.printStackTrace();
            fail();
        }
    }

    /**
     *  Methode :  .
     */
    public void testRechercherListCollaborateurs() {
        assertNotNull(mCollaborateurMetier.rechercherListCollaborateurs());
    }
}
