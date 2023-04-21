package zildo.monde.sprites.persos;

import java.util.ArrayList;
import java.util.List;
import zildo.client.sound.BankSound;
import zildo.fwk.gfx.EngineFX;
import zildo.fwk.script.xml.element.TriggerElement;
import zildo.monde.items.Item;
import zildo.monde.map.Tile;
import zildo.monde.sprites.SpriteEntity;
import zildo.monde.sprites.desc.EntityType;
import zildo.monde.sprites.desc.PersoDescription;
import zildo.monde.sprites.desc.SpriteAnimation;
import zildo.monde.sprites.desc.SpriteDescription;
import zildo.monde.sprites.elements.Element;
import zildo.monde.sprites.persos.action.PersoAction;
import zildo.monde.sprites.persos.ia.PathFinder;
import zildo.monde.sprites.persos.ia.PathFinderSquirrel;
import zildo.monde.sprites.persos.ia.PathFinderStraightFlying;
import zildo.monde.sprites.utils.MouvementPerso;
import zildo.monde.sprites.utils.MouvementZildo;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;
import zildo.monde.util.Pointf;
import zildo.monde.util.Zone;
import zildo.server.EngineZildo;
import zildo.server.MapManagement;

public abstract class Perso extends Element {

    public enum PersoInfo {

        NEUTRAL, ENEMY, ZILDO, SHOOTABLE_NEUTRAL
    }

    protected Zone zone_deplacement;

    protected int compte_dialogue;

    private String effect;

    protected PersoInfo info;

    protected boolean alerte;

    protected MouvementPerso quel_deplacement;

    protected PersoDescription quel_spr;

    protected int attente;

    protected PathFinder pathFinder;

    protected float px, py;

    protected int pos_seqsprite;

    private Element en_bras;

    protected MouvementZildo mouvement;

    protected int cptMouvement;

    private int coming_map;

    protected int pv, maxpv;

    private boolean ghost = false;

    private int money;

    protected int countArrow;

    protected int countBomb;

    protected int countKey;

    private Point posAvantSaut;

    protected Point posShadowJump;

    private Angle jumpAngle;

    protected PersoAction action;

    private int count = 0;

    protected boolean inWater = false;

    protected boolean inDirt = false;

    private boolean wounded;

    private Perso dialoguingWith;

    private String dialogSwitch;

    private Perso following;

    public Item weapon;

    public Item getWeapon() {
        return weapon;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public Perso getFollowing() {
        return following;
    }

    public void setFollowing(Perso following) {
        this.following = following;
    }

    List<Element> persoSprites;

    public Zone getZone_deplacement() {
        return zone_deplacement;
    }

    public void setZone_deplacement(Zone zone_deplacement) {
        this.zone_deplacement = zone_deplacement;
    }

    public int getCompte_dialogue() {
        return compte_dialogue;
    }

    public void setCompte_dialogue(int compte_dialogue) {
        this.compte_dialogue = compte_dialogue;
    }

    public PersoInfo getInfo() {
        return info;
    }

    public void setInfo(PersoInfo info) {
        this.info = info;
    }

    public boolean isAlerte() {
        return alerte;
    }

    public void setAlerte(boolean alerte) {
        this.alerte = alerte;
    }

    public MouvementPerso getQuel_deplacement() {
        return quel_deplacement;
    }

    public void setQuel_deplacement(MouvementPerso p_script) {
        quel_deplacement = p_script;
        Point target = pathFinder.getTarget();
        switch(p_script) {
            case ZONE:
            case IMMOBILE:
                pathFinder = new PathFinder(this);
                pathFinder.setTarget(null);
                break;
            case BIRD:
                pathFinder = new PathFinderStraightFlying(this);
                pathFinder.setTarget(target);
                break;
            case SQUIRREL:
                pathFinder = new PathFinderSquirrel(this);
                break;
            case WAKEUP:
                pos_seqsprite = 0;
                break;
        }
    }

    public int getAttente() {
        return attente;
    }

    public void setAttente(int attente) {
        this.attente = attente;
    }

    public float getPx() {
        return px;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public float getPy() {
        return py;
    }

    public void setPy(float py) {
        this.py = py;
    }

    public int getPos_seqsprite() {
        return pos_seqsprite;
    }

    public void setPos_seqsprite(int pos_seqsprite) {
        this.pos_seqsprite = pos_seqsprite;
    }

    public Element getEn_bras() {
        return en_bras;
    }

    public void setEn_bras(Element en_bras) {
        this.en_bras = en_bras;
    }

    public MouvementZildo getMouvement() {
        return mouvement;
    }

    public void setMouvement(MouvementZildo mouvement) {
        this.mouvement = mouvement;
    }

    public int getComing_map() {
        return coming_map;
    }

    public void setComing_map(int coming_map) {
        this.coming_map = coming_map;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public int getMaxpv() {
        return maxpv;
    }

    public void setMaxpv(int maxpv) {
        this.maxpv = maxpv;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getDialogSwitch() {
        return dialogSwitch;
    }

    public void setDialogSwitch(String p_dialogSwitch) {
        if (p_dialogSwitch != null && p_dialogSwitch.length() > 0) {
            dialogSwitch = p_dialogSwitch;
        } else {
            dialogSwitch = null;
        }
    }

    public boolean isWounded() {
        return wounded;
    }

    public void setWounded(boolean wounded) {
        this.wounded = wounded;
    }

    public List<Element> getPersoSprites() {
        return persoSprites;
    }

    public void setPersoSprites(List<Element> persoSprites) {
        this.persoSprites = persoSprites;
    }

    public void addPersoSprites(Element elem) {
        this.persoSprites.add(elem);
        elem.setLinkedPerso(this);
    }

    public Perso() {
        super();
        entityType = EntityType.PERSO;
        money = (int) Math.random();
        wounded = false;
        alerte = false;
        px = 0.0f;
        py = 0.0f;
        compte_dialogue = 0;
        attente = 0;
        quel_deplacement = MouvementPerso.IMMOBILE;
        persoSprites = new ArrayList<Element>();
        pathFinder = new PathFinder(this);
    }

    @Override
    public void finalize() {
        if (persoSprites != null && persoSprites.size() > 0) {
            for (Element e : persoSprites) {
                EngineZildo.spriteManagement.deleteSprite(e);
            }
            persoSprites.clear();
        }
    }

    public void hide() {
        this.visible = false;
        if (this.persoSprites.size() > 0) {
            for (Element e : persoSprites) {
                e.setVisible(false);
            }
        }
    }

    @Override
    public void setSpecialEffect(EngineFX specialEffect) {
        super.setSpecialEffect(specialEffect);
        if (this.persoSprites.size() > 0) {
            for (Element e : persoSprites) {
                e.setSpecialEffect(specialEffect);
            }
        }
    }

    public Point getAttackTarget() {
        final int add_anglex[] = { 0, 1, 0, -1 };
        final int add_angley[] = { -1, 0, 1, 0 };
        Point p = new Point();
        p.setX(((int) getX() + 5 * add_anglex[angle.value]) / 16);
        p.setY(((int) getY() + 5 * add_angley[angle.value]) / 16);
        return p;
    }

    public void placeAt(Point p_point) {
        placeAt(p_point.getX(), p_point.getY());
    }

    public void placeAt(int p_posX, int p_posY) {
        int diffX = (int) x - p_posX;
        int diffY = (int) y - p_posY;
        x = p_posX;
        y = p_posY;
        for (Element elem : persoSprites) {
            elem.x += diffX;
            elem.y += diffY;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Perso=" + name + "\nx=" + x + "\ny=" + y + "\ninfo=" + info + "\nmvt=" + mouvement);
        return sb.toString();
    }

    /**
	 * Push the character away, with a hit point located at the given
	 * coordinates.
	 * 
	 * @param p_cx
	 * @param p_cy
	 */
    protected void project(float p_cx, float p_cy, int p_speed) {
        float diffx = getX() - p_cx;
        float diffy = getY() - p_cy;
        double norme = Math.sqrt((diffx * diffx) + (diffy * diffy));
        if (norme == 0.0f) {
            norme = 1.0f;
        }
        this.setPx((float) (p_speed * (diffx / norme)));
        this.setPy((float) (p_speed * (diffy / norme)));
    }

    /**
	 * Try to move character at the given location, and returns corrected one.
	 * <p/>
	 * The correction is based on two methods: -transform diagonal movement into
	 * lateral -transform lateral movement into diagonal
	 * <p/>
	 * If no one succeeds, returns the original location.
	 * 
	 * @param p_xx
	 * @param p_yy
	 * @return corrected location, or same one if character can't move at all.
	 */
    public Pointf tryMove(float p_xx, float p_yy) {
        MapManagement mapManagement = EngineZildo.mapManagement;
        float xx = p_xx;
        float yy = p_yy;
        if (mapManagement.collide((int) xx, (int) yy, this)) {
            float diffx = xx - x;
            float diffy = yy - y;
            if (diffx != 0 && diffy != 0) {
                if (!mapManagement.collide((int) xx, (int) y, this)) {
                    yy = (int) y;
                } else if (!mapManagement.collide((int) x, (int) yy, this)) {
                    xx = (int) x;
                }
            } else {
                float speed;
                if (diffx == 0) {
                    speed = Math.abs(diffy);
                    if (!mapManagement.collide(xx + speed, yy, this)) {
                        xx += speed;
                    } else if (!mapManagement.collide(xx - speed, yy, this)) {
                        xx -= speed;
                    }
                } else if (diffy == 0) {
                    speed = Math.abs(diffx);
                    if (!mapManagement.collide(xx, yy + speed, this)) {
                        yy += speed;
                    } else if (!mapManagement.collide(xx, yy - speed, this)) {
                        yy -= speed;
                    }
                }
            }
            if (mapManagement.collide(xx, yy, this)) {
                xx = (int) x;
                yy = (int) y;
            }
        }
        return new Pointf(xx, yy);
    }

    public abstract void initPersoFX();

    public abstract void beingWounded(float cx, float cy, Perso p_shooter, int p_damage);

    public void parry(float cx, float cy, Perso p_shooter) {
    }

    public abstract void stopBeingWounded();

    public abstract void attack();

    public void die(boolean p_link, Perso p_shooter) {
        EngineZildo.spriteManagement.spawnSpriteGeneric(SpriteAnimation.DEATH, (int) x, (int) y, 0, p_link ? this : null, null);
        TriggerElement trig = TriggerElement.createDeathTrigger(name);
        EngineZildo.scriptManagement.trigger(trig);
    }

    public abstract void finaliseComportement(int compteur_animation);

    public void animate(int compteur) {
        if (action != null && getPv() > 0) {
            if (attente != 0) {
                attente--;
            }
            if (action.launchAction()) {
                action = null;
            }
        }
        switch(this.getQuel_deplacement()) {
            case OBSERVE:
                Perso observed = this.getFollowing();
                if (observed == null || !isZildo()) {
                    observed = EngineZildo.persoManagement.getZildo();
                }
                if (observed != null) {
                    sight(observed, true);
                }
                break;
        }
    }

    public void takeSomething(int objX, int objY, SpriteDescription d, Element object) {
    }

    private Point transitionCrossed;

    private Angle transitionAngle;

    /**
	 * Perso walk on a tile, so he reacts (water), or tile change (door).
	 * 
	 * @param p_sound
	 *            TRUE=play sound when modifying map.
	 * @return boolean (TRUE=slow down)
	 */
    public boolean walkTile(boolean p_sound) {
        int cx = (int) (x / 16);
        int cy = (int) (y / 16);
        MapManagement mapManagement = EngineZildo.mapManagement;
        Tile tile = mapManagement.getCurrentMap().readmap(cx, cy, false);
        if (tile == null) {
            return false;
        }
        int onmap = tile.getValue();
        if (tile.parent.getTransition() != null) {
            if (transitionCrossed == null) {
                setForeground(true);
                transitionCrossed = new Point(cx, cy);
                transitionAngle = tile.parent.getTransition();
            }
        } else if (transitionCrossed != null) {
            Angle choosed = Angle.fromDirection(cx - transitionCrossed.x, cy - transitionCrossed.y);
            if (choosed != transitionAngle) {
                setForeground(false);
            }
            transitionCrossed = null;
        }
        boolean slowDown = false;
        boolean repeatSound = false;
        inWater = false;
        inDirt = false;
        BankSound snd = null;
        switch(onmap) {
            case 278:
                if (pathFinder.open) {
                    mapManagement.getCurrentMap().writemap(cx, cy, 314);
                    mapManagement.getCurrentMap().writemap(cx + 1, cy, 315);
                    snd = BankSound.OuvrePorte;
                }
                break;
            case 279:
                if (pathFinder.open) {
                    mapManagement.getCurrentMap().writemap(cx - 1, cy, 314);
                    mapManagement.getCurrentMap().writemap(cx, cy, 315);
                    snd = BankSound.OuvrePorte;
                }
                break;
            case 200:
            case 374:
                snd = BankSound.ZildoGadou;
                inDirt = true;
                repeatSound = true;
                slowDown = true;
                break;
            case 846:
                inWater = true;
                snd = BankSound.ZildoPatauge;
                repeatSound = true;
                break;
            case 857:
            case 858:
            case 861:
            case 862:
                if (!isGhost()) {
                    EngineZildo.scriptManagement.execute("miniStairsDown");
                }
                slowDown = true;
                break;
            case 859:
            case 860:
            case 863:
            case 864:
                if (!isGhost()) {
                    EngineZildo.scriptManagement.execute("miniStairsUp");
                }
                slowDown = true;
                break;
            case 206:
            case 207:
            case 170:
            case 171:
            case 172:
            case 91 + 7 * 256:
                slowDown = true;
                break;
        }
        if (repeatSound) {
            if (count > 15) {
                count = 0;
            } else {
                snd = null;
                count++;
            }
        }
        if (snd != null && p_sound && isZildo()) {
            EngineZildo.soundManagement.broadcastSound(snd, this);
        }
        if (!EngineZildo.game.multiPlayer) {
            String mapName = EngineZildo.mapManagement.getCurrentMap().getName();
            TriggerElement trig = TriggerElement.createLocationTrigger(mapName, new Point(x, y));
            EngineZildo.scriptManagement.trigger(trig);
        }
        return slowDown;
    }

    public boolean linkedSpritesContains(SpriteEntity entity) {
        return persoSprites.contains(entity) || en_bras == entity;
    }

    @Override
    public void setForeground(boolean p_foreground) {
        super.setForeground(p_foreground);
        for (Element e : persoSprites) {
            e.setForeground(p_foreground);
        }
        if (getEn_bras() != null) {
            getEn_bras().setForeground(p_foreground);
        }
    }

    public int getCptMouvement() {
        return cptMouvement;
    }

    public void setCptMouvement(int cptMouvement) {
        this.cptMouvement = cptMouvement;
    }

    public Perso getDialoguingWith() {
        return dialoguingWith;
    }

    public void setDialoguingWith(Perso p_dialoguingWith) {
        this.dialoguingWith = p_dialoguingWith;
    }

    public boolean isGhost() {
        return ghost;
    }

    public void setGhost(boolean ghost) {
        this.ghost = ghost;
    }

    public boolean isUnstoppable() {
        return pathFinder.unstoppable;
    }

    public void setUnstoppable(boolean p_value) {
        pathFinder.unstoppable = p_value;
    }

    @Override
    public void setVisible(boolean p_visible) {
        super.setVisible(p_visible);
        for (SpriteEntity entity : persoSprites) {
            entity.setVisible(p_visible);
        }
    }

    public Point getTarget() {
        return pathFinder.getTarget();
    }

    public void setTarget(Point target) {
        this.pathFinder.setTarget(target);
    }

    public boolean hasReachedTarget() {
        return pathFinder.getTarget() == null || pathFinder.hasReachedTarget();
    }

    public void setSpeed(float p_speed) {
        if (p_speed > 0.0f) {
            pathFinder.speed = p_speed;
        }
    }

    public void setForward(boolean p_forward) {
        pathFinder.backward = p_forward;
    }

    public void setOpen(boolean p_open) {
        pathFinder.open = p_open;
    }

    public Pointf reachDestination(float p_speed) {
        return pathFinder.reachDestination(p_speed);
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String p_effect) {
        this.effect = p_effect;
    }

    public int getCountArrow() {
        return countArrow;
    }

    public void setCountArrow(int countArrow) {
        this.countArrow = countArrow;
    }

    public int getCountBomb() {
        return countBomb;
    }

    public void setCountBomb(int countBomb) {
        this.countBomb = countBomb;
    }

    public int getCountKey() {
        return countKey;
    }

    public void setCountKey(int countKey) {
        this.countKey = countKey;
    }

    /**
	 * Turn character in order to see given perso.
	 * 
	 * @param p_target
	 * @param p_shortRadius
	 *            TRUE=sight only if target is in short perimeter / FALSE=sight
	 *            whenever target is
	 */
    public void sight(Perso p_target, boolean p_shortRadius) {
        int xx = (int) (getX() - p_target.getX());
        int yy = (int) (getY() - p_target.getY());
        if (Math.abs(yy) >= Math.abs(xx) || (p_shortRadius && (Math.abs(xx) > 96 || Math.abs(yy) > 96))) {
            if (yy > 0) {
                setAngle(Angle.NORD);
            } else {
                setAngle(Angle.SUD);
            }
        } else {
            if (xx > 0) {
                setAngle(Angle.OUEST);
            } else {
                setAngle(Angle.EST);
            }
        }
    }

    @Override
    public PersoDescription getDesc() {
        return (PersoDescription) desc;
    }

    public void setPathFinder(PathFinder p_pf) {
        pathFinder = p_pf;
    }

    public Point getCenteredScreenPosition() {
        Point pos = new Point(getScrX(), getScrY());
        pos.add(sprModel.getTaille_x() / 2, sprModel.getTaille_y() / 2);
        return pos;
    }

    /**
	 * Starts a jump in given angle.
	 * 
	 * @param p_angle
	 *            should not be null
	 */
    private void jump(Angle p_angle) {
        Point zildoAvantSaut = new Point(x, y);
        mouvement = MouvementZildo.SAUTE;
        jumpAngle = p_angle;
        posShadowJump = p_angle.getLandingPoint().translate((int) x, (int) y);
        setEn_bras(null);
        posAvantSaut = zildoAvantSaut;
        attente = 0;
        EngineZildo.soundManagement.broadcastSound(BankSound.ZildoTombe, this);
    }

    /**
	 * Check if character is about to jump : near a cliff with room for landing point.<br/>
	 * If he can, start jumping.
	 * @param loc
	 */
    public void tryJump(Pointf loc) {
        int cx = (int) (loc.x / 16);
        int cy = (int) (loc.y / 16);
        Angle angleResult = EngineZildo.mapManagement.getAngleJump(angle, cx, cy);
        SpriteEntity pushed = null;
        if (isZildo()) {
            pushed = ((PersoZildo) this).getPushingSprite();
        }
        if (angleResult != null && pushed == null) {
            Point landingPoint = angleResult.getLandingPoint().translate((int) x, (int) y);
            if (!EngineZildo.mapManagement.collide(landingPoint.x, landingPoint.y, this)) {
                jump(angleResult);
            }
        }
    }

    /**
	 * Character is jumping : move him
	 * @return TRUE if he has been relocated to his start position (ex: fallen in water)
	 */
    public boolean moveJump() {
        boolean relocate = false;
        if (getAttente() == 32) {
            setMouvement(MouvementZildo.VIDE);
            int cx = (int) (x / 16);
            int cy = (int) (y / 16);
            int onMap = EngineZildo.mapManagement.getCurrentMap().readmap(cx, cy);
            if (onMap >= 108 && onMap <= 138) {
                Point zildoAvantSaut = getPosAvantSaut();
                x = zildoAvantSaut.getX();
                y = zildoAvantSaut.getY();
                relocate = true;
                beingWounded(x, y, null, 2);
                stopBeingWounded();
                EngineZildo.soundManagement.broadcastSound(BankSound.ZildoPlonge, this);
            } else {
                setForeground(false);
                EngineZildo.soundManagement.broadcastSound(BankSound.ZildoAtterit, this);
            }
            z = 0;
            attente = 0;
        } else {
            Point landingPoint = getJumpAngle().getLandingPoint();
            float pasx = landingPoint.x / 32.0f;
            float pasy = landingPoint.y / 32.0f;
            x += pasx;
            y += pasy;
            double beta = (Math.PI * attente) / 32.0f;
            z = (int) (8.0f * Math.sin(beta));
            attente++;
        }
        return relocate;
    }

    public Point getPosAvantSaut() {
        return posAvantSaut;
    }

    public Angle getJumpAngle() {
        return jumpAngle;
    }
}
