package Processing;

import Scene.Scene;
import processing.core.*;

public abstract class Vue extends PApplet {

    private Boolean isPartielle;

    private Scene[] banque;

    private int sceneEnCours;

    /**
	 * les variables rsave, bsave et gsave permettent la sauvegarde de la couleur de l'objet
	 * selectionné dans les vues filles.
	 * @see Dissection.masquerObjet(int) et Dissection.demasquerObjet(int)
	 */
    private int rsave = 0;

    private int bsave = 0;

    private int gsave = 0;

    /**
	 * x et y sont la position du curseur de la souris dans la vue
	 * arrete est la lngueur de l'arrête d'un cube
	 */
    private int x;

    private int y;

    private int arrete;

    private int objetEnCours;

    /**
	 * dimensions du volume contenant l'empilement d'objets
	 */
    private int xVue;

    private int yVue;

    private int zVue;

    /**
	 *  met un objet en surbrillance (couleur choisie)
	 * @param int : objet
	 * @return void
	 */
    public abstract void surbrillance(int objet);

    /**
	 * permet de savoir sur quel objet on a clique
	 * @param s : Scene
	 * @return int : objet
	 */
    public abstract int recupererObjet(Scene s);

    /**
	 * @see doc Processing
	 * setup prermet de régler les parametres de base de la scene Processing (taille...) 
	 */
    public abstract void setup();

    /**
	 * @see doc Processing
	 * draw est la méthode appelée automatiquement dans laquelle 
	 * on crée tous les objets à dessinner
	 */
    public abstract void draw();

    public Scene[] getBanque() {
        return banque;
    }

    public void setBanque(Scene[] banque) {
        this.banque = banque;
    }

    public Boolean getIsPartielle() {
        return isPartielle;
    }

    public void setIsPartielle(Boolean isPartielle) {
        this.isPartielle = isPartielle;
    }

    public void setIsPartielle() {
        this.isPartielle = false;
        if (this.getBanque().length > 1) {
            this.isPartielle = true;
        }
    }

    public int getSceneEncours() {
        return sceneEnCours;
    }

    public void setSceneEnCours(int sceneEncours) {
        this.sceneEnCours = sceneEncours;
    }

    public int getRsave() {
        return rsave;
    }

    public void setRsurb(int rsurb) {
        this.rsave = rsurb;
    }

    public int getBsurb() {
        return bsave;
    }

    public void setBsurb(int bsurb) {
        this.bsave = bsurb;
    }

    public int getGsurb() {
        return gsave;
    }

    public void setGsurb(int gsurb) {
        this.gsave = gsurb;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBsave() {
        return bsave;
    }

    public void setBsave(int bsave) {
        this.bsave = bsave;
    }

    public int getGsave() {
        return gsave;
    }

    public void setGsave(int gsave) {
        this.gsave = gsave;
    }

    public int getArrete() {
        return arrete;
    }

    public void setArrete(int arrete) {
        this.arrete = arrete;
    }

    public int getObjetEnCours() {
        return objetEnCours;
    }

    public void setObjetEnCours(int objetEnCours) {
        this.objetEnCours = objetEnCours;
    }

    public void setRsave(int rsave) {
        this.rsave = rsave;
    }

    public int getXVue() {
        return xVue;
    }

    public void setXVue(int vue) {
        xVue = vue;
    }

    public int getYVue() {
        return yVue;
    }

    public void setYVue(int vue) {
        yVue = vue;
    }

    public int getZVue() {
        return zVue;
    }

    public void setZVue(int vue) {
        zVue = vue;
    }
}
