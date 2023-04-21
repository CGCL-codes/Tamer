package scene;

import java.awt.BorderLayout;
import java.util.Collection;
import javax.media.opengl.GL;
import javax.media.opengl.GLJPanel;
import javax.swing.JPanel;

public class Scene3DUtilisateur implements Scene3DUtilisateurInterface {

    private GLScene3D maScene3D;

    private GLJPanel monPanel3D;

    private Couleur couleurDeFond;

    double camX = 1, camY = 1, camZ = 1;

    public Scene3DUtilisateur() {
        this.setMaScene3D(new GLScene3D(this));
        this.setMonPanel3D(new GLJPanel());
    }

    public final void addAllMouseEventListener(MouseHandler mouseHandler) {
        this.getMaScene3D().getCanvas().addMouseListener(mouseHandler);
        this.getMaScene3D().getCanvas().addMouseMotionListener(mouseHandler);
        this.getMaScene3D().getCanvas().addMouseWheelListener(mouseHandler);
    }

    public final void addAllKeyEventListener(KeyHandler keyHandler) {
        this.getMaScene3D().getCanvas().addKeyListener(keyHandler);
    }

    private GLScene3D getMaScene3D() {
        return maScene3D;
    }

    private void setMaScene3D(GLScene3D mascene3d) {
        this.maScene3D = mascene3d;
    }

    /**
	 * Pr�ciser ici le code pour votre visualisation
	 */
    public void afficherDansMaScene3D(GL gl) {
    }

    /**
	 * Pr�ciser ici le code correspond � vos displaylists
	 *
	 */
    public void initialiserMesDisplayLists(GL gl) {
    }

    /**
	 * Cette m�thode retourne un JPanel (Swing)
	 * contenant la sc�ne 3D
	 * @return un JPanel contenant la sc�ne 3D
	 */
    public final JPanel getJPanel() {
        return getMonPanel3D();
    }

    private JPanel getMonPanel3D() {
        return monPanel3D;
    }

    private void setMonPanel3D(GLJPanel panel) {
        this.monPanel3D = panel;
        this.monPanel3D.setLayout(new BorderLayout());
        this.monPanel3D.add(this.getMaScene3D().getCanvas(), BorderLayout.CENTER);
    }

    public final void definirCouleurDeFond(GL gl) {
        gl.glClearColor(getCouleurDeFond().getRed(), getCouleurDeFond().getGreen(), getCouleurDeFond().getBlue(), getCouleurDeFond().getAlpha());
    }

    /**
	 * D�finir � l'initialisation la couleur de fond de la sc�ne 3D
	 * @param couleur : instance de la classe Couleur
	 */
    public final void setCouleurDeFond(Couleur couleur) {
        couleurDeFond = couleur;
    }

    private final Couleur getCouleurDeFond() {
        if (couleurDeFond == null) return new Couleur(0, 0, 0, 1); else return couleurDeFond;
    }

    public final void positionnerCamera(float x, float y, float z) {
        this.getMaScene3D().getCurrentCamera().setEyeXYZ(x, y, z);
        this.getMaScene3D().getCurrentCamera().calculerRayon();
        this.getMaScene3D().getCurrentCamera().calcTheta();
        this.getMaScene3D().getCurrentCamera().calcRho();
    }

    public final void positionnerHorizon(float x, float y, float z) {
        this.getMaScene3D().getCurrentCamera().setCentreXYZ(x, y, z);
        this.getMaScene3D().getCurrentCamera().calculerRayon();
        this.getMaScene3D().getCurrentCamera().calcTheta();
        this.getMaScene3D().getCurrentCamera().calcRho();
    }

    public final void switchCurrentCamera() {
        this.getMaScene3D().changerCurrentCamera();
    }

    /**
	 * dans le cadre d'une s�lection � un objet.
	 * valeur renvoy�e : son identifiant
	 *
	 */
    public final int getSelectedVObject() {
        return this.getMaScene3D().getSelectedVObject();
    }

    /**
	 * r�cup�rer le conteneur d'objets s�lectionn�s
	 */
    public Collection<Integer> getSelectedVObjects() {
        return this.getMaScene3D().getSelectedVObjects();
    }

    /**
	 * vider le conteneur d'objets s�lectionn�s
	 */
    public void viderSelectedVObjects() {
        this.getMaScene3D().viderSelectedVObjects();
    }

    public void setJPopupMenu(javax.swing.JPopupMenu jpop) {
        this.getMaScene3D().setPopup(jpop);
    }
}
