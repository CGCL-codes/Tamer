package zildo.fwk.script.command;

import zildo.client.ClientEngineZildo;
import zildo.client.ClientEvent;
import zildo.client.ClientEventNature;
import zildo.client.MapDisplay;
import zildo.client.sound.BankMusic;
import zildo.client.sound.BankSound;
import zildo.client.stage.SinglePlayer;
import zildo.fwk.gfx.filter.FilterEffect;
import zildo.fwk.script.xml.element.ActionElement;
import zildo.fwk.ui.UIText;
import zildo.monde.items.Item;
import zildo.monde.items.ItemKind;
import zildo.monde.quest.actions.ScriptAction;
import zildo.monde.sprites.desc.PersoDescription;
import zildo.monde.sprites.desc.SpriteAnimation;
import zildo.monde.sprites.desc.SpriteDescription;
import zildo.monde.sprites.elements.Element;
import zildo.monde.sprites.elements.ElementGear;
import zildo.monde.sprites.elements.ElementImpact;
import zildo.monde.sprites.elements.ElementImpact.ImpactKind;
import zildo.monde.sprites.persos.Perso;
import zildo.monde.sprites.persos.PersoZildo;
import zildo.monde.sprites.utils.MouvementPerso;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;
import zildo.server.EngineZildo;

/**
 * Class splitted from ScriptExecutor, in order to clarify things.
 * <p/>
 * This class has just to render one action.
 * @author tchegito
 */
public class ActionExecutor {

    ScriptExecutor scriptExec;

    int count;

    public ActionExecutor(ScriptExecutor p_scriptExec) {
        scriptExec = p_scriptExec;
    }

    /**
     * @param p_action
     * @return boolean
     */
    public boolean render(ActionElement p_action) {
        boolean achieved = false;
        if (p_action.waiting) {
            waitForEndAction(p_action);
            achieved = p_action.done;
        } else {
            Perso perso = EngineZildo.persoManagement.getNamedPerso(p_action.who);
            if (perso != null) {
                scriptExec.involved.add(perso);
            }
            Point location = p_action.location;
            if (p_action.delta && location != null) {
                Point currentPos = null;
                if (perso != null) {
                    currentPos = new Point(perso.x, perso.y);
                } else if ("camera".equals(p_action.what)) {
                    currentPos = ClientEngineZildo.mapDisplay.getCamera();
                }
                if (currentPos == null) {
                    throw new RuntimeException("We need valid 'who' or 'what' attribute");
                }
                location = location.translate(currentPos.x, currentPos.y);
            }
            String text = p_action.text;
            switch(p_action.kind) {
                case pos:
                    if (perso != null) {
                        perso.x = location.x;
                        perso.y = location.y;
                    } else if ("camera".equals(p_action.what)) {
                        ClientEngineZildo.mapDisplay.setCamera(location);
                        ClientEngineZildo.mapDisplay.setFocusedEntity(null);
                    }
                    achieved = true;
                    break;
                case moveTo:
                    if (perso != null) {
                        perso.setGhost(true);
                        perso.setTarget(location);
                        perso.setForward(p_action.backward);
                        perso.setSpeed(p_action.speed);
                        perso.setOpen(p_action.open);
                        perso.setUnstoppable(p_action.unstoppable);
                    } else if ("camera".equals(p_action.what)) {
                        ClientEngineZildo.mapDisplay.setTargetCamera(location);
                        ClientEngineZildo.mapDisplay.setFocusedEntity(null);
                    }
                    break;
                case speak:
                    String sentence = UIText.getGameText(text);
                    EngineZildo.dialogManagement.launchDialog(SinglePlayer.getClientState(), null, new ScriptAction(sentence));
                    scriptExec.userEndedAction = false;
                    break;
                case script:
                    MouvementPerso script = MouvementPerso.fromInt(p_action.val);
                    perso.setQuel_deplacement(script);
                    String param = p_action.fx;
                    if (param != null) {
                        switch(script) {
                            case ZONE:
                                perso.setZone_deplacement(EngineZildo.mapManagement.range(perso.getX() - 16 * 5, perso.getY() - 16 * 5, perso.getX() + 16 * 5, perso.getY() + 16 * 5));
                                break;
                            case OBSERVE:
                                Perso persoToObserve = EngineZildo.persoManagement.getNamedPerso(param);
                                perso.setFollowing(persoToObserve);
                                break;
                        }
                    }
                    achieved = true;
                    break;
                case angle:
                    if (perso.getTarget() != null) {
                        return false;
                    }
                    perso.setAngle(Angle.fromInt(p_action.val));
                    achieved = true;
                    break;
                case wait:
                    count = p_action.val;
                    break;
                case fadeIn:
                    EngineZildo.askEvent(new ClientEvent(ClientEventNature.FADE_IN, FilterEffect.fromInt(p_action.val)));
                    break;
                case fadeOut:
                    EngineZildo.askEvent(new ClientEvent(ClientEventNature.FADE_OUT, FilterEffect.fromInt(p_action.val)));
                    break;
                case clear:
                    EngineZildo.askEvent(new ClientEvent(ClientEventNature.CLEAR));
                    achieved = true;
                    break;
                case map:
                    EngineZildo.mapManagement.loadMap(p_action.text, false);
                    ClientEngineZildo.mapDisplay.setCurrentMap(EngineZildo.mapManagement.getCurrentMap());
                    achieved = true;
                    break;
                case focus:
                    Element toFocus = perso;
                    if (p_action.what != null) {
                        toFocus = EngineZildo.spriteManagement.getNamedElement(p_action.what);
                    }
                    if (toFocus == null) {
                        ClientEngineZildo.mapDisplay.setFocusedEntity(null);
                    }
                    if (p_action.delta) {
                        Point cameraLoc = new Point(toFocus.x - MapDisplay.CENTER_X, toFocus.y - MapDisplay.CENTER_Y);
                        ClientEngineZildo.mapDisplay.setTargetCamera(cameraLoc);
                    }
                    ClientEngineZildo.mapDisplay.setFocusedEntity(toFocus);
                    achieved = !p_action.delta || p_action.unblock;
                    break;
                case spawn:
                    if (p_action.who != null) {
                        PersoDescription desc = PersoDescription.valueOf(p_action.text);
                        Perso newOne = EngineZildo.persoManagement.createPerso(desc, location.x, location.y, 0, p_action.who, p_action.val);
                        newOne.setSpeed(p_action.speed);
                        newOne.setEffect(p_action.fx);
                        newOne.initPersoFX();
                        EngineZildo.spriteManagement.spawnPerso(newOne);
                    } else {
                        if (EngineZildo.spriteManagement.getNamedElement(p_action.what) != null) {
                            SpriteDescription desc = SpriteDescription.Locator.findNamedSpr(p_action.text);
                            Element elem = EngineZildo.spriteManagement.spawnElement(desc, location.x, location.y, 0);
                            elem.setName(p_action.what);
                        }
                    }
                    achieved = true;
                    break;
                case impact:
                    ImpactKind impactKind = ImpactKind.valueOf(p_action.text);
                    EngineZildo.spriteManagement.spawnSprite(new ElementImpact(location.x, location.y, impactKind, null));
                    achieved = true;
                    break;
                case animation:
                    SpriteAnimation anim = SpriteAnimation.valueOf(p_action.text);
                    Element animElem = EngineZildo.spriteManagement.spawnSpriteGeneric(anim, location.x, location.y, 0, null, null);
                    if (p_action.what != null) {
                        animElem.setName(p_action.what);
                    }
                    achieved = true;
                    break;
                case take:
                    if (p_action.who == null || "zildo".equalsIgnoreCase(p_action.who)) {
                        PersoZildo zildo = EngineZildo.persoManagement.getZildo();
                        if (p_action.val != 0) {
                            zildo.pickGoodies(null, p_action.val);
                        } else {
                            zildo.pickItem(ItemKind.fromString(text), null);
                        }
                    } else if (perso != null) {
                        Element elem = EngineZildo.spriteManagement.getNamedElement(p_action.what);
                        perso.addPersoSprites(elem);
                    }
                    achieved = true;
                    break;
                case putDown:
                    PersoZildo zildo = EngineZildo.persoManagement.getZildo();
                    zildo.removeItem(ItemKind.fromString(text));
                    achieved = true;
                    break;
                case mapReplace:
                    EngineZildo.scriptManagement.addReplacedMapName(p_action.what, text);
                    achieved = true;
                    break;
                case exec:
                    EngineZildo.scriptManagement.execute(text);
                    break;
                case music:
                    if (text == null) {
                        EngineZildo.soundManagement.broadcastSound((BankMusic) null, (Point) null);
                    } else {
                        BankMusic musicSnd = BankMusic.valueOf(text);
                        EngineZildo.soundManagement.broadcastSound(musicSnd, (Point) null);
                    }
                    EngineZildo.soundManagement.setForceMusic(true);
                    achieved = true;
                    break;
                case sound:
                    BankSound snd = BankSound.valueOf(text);
                    EngineZildo.soundManagement.playSound(snd, null);
                    achieved = true;
                    break;
                case remove:
                    Element toRemove;
                    if (p_action.what != null) {
                        toRemove = EngineZildo.spriteManagement.getNamedElement(p_action.what);
                    } else {
                        toRemove = perso;
                        EngineZildo.persoManagement.removePerso((Perso) toRemove);
                    }
                    EngineZildo.spriteManagement.deleteSprite(toRemove);
                    achieved = true;
                    break;
                case markQuest:
                    if (p_action.val == 1) {
                        EngineZildo.scriptManagement.accomplishQuest(p_action.text, true);
                    } else {
                        EngineZildo.scriptManagement.resetQuest(p_action.text);
                    }
                    achieved = true;
                    break;
                case attack:
                    if (p_action.text != null) {
                        Item weapon = new Item(ItemKind.fromString(text));
                        perso.setWeapon(weapon);
                        perso.attack();
                        achieved = true;
                    }
                    break;
                case activate:
                    Element toActivate = EngineZildo.spriteManagement.getNamedElement(p_action.what);
                    ElementGear gearToActivate = (ElementGear) toActivate;
                    gearToActivate.activate(p_action.activate);
                    break;
            }
            p_action.done = achieved;
            p_action.waiting = !achieved;
        }
        return achieved;
    }

    /**
     * An action has started. Here we're waiting for it to finish.
     * @param p_action
     */
    private void waitForEndAction(ActionElement p_action) {
        String who = p_action.who;
        Perso perso = EngineZildo.persoManagement.getNamedPerso(who);
        boolean achieved = false;
        switch(p_action.kind) {
            case moveTo:
                if (perso != null) {
                    achieved = perso.hasReachedTarget();
                } else if ("camera".equals(p_action.what)) {
                    achieved = ClientEngineZildo.mapDisplay.getTargetCamera() == null;
                }
                break;
            case focus:
                achieved = ClientEngineZildo.mapDisplay.getTargetCamera() == null;
                break;
            case speak:
                achieved = scriptExec.userEndedAction;
                break;
            case wait:
                achieved = (count-- == 0);
                break;
            case fadeIn:
            case fadeOut:
                achieved = ClientEngineZildo.guiDisplay.isFadeOver();
                System.out.println("waiting for fade");
                break;
            case activate:
                Element toActivate = EngineZildo.spriteManagement.getNamedElement(p_action.what);
                ElementGear gearToActivate = (ElementGear) toActivate;
                achieved = !gearToActivate.isActing();
                break;
            case exec:
                achieved = true;
                break;
        }
        p_action.waiting = !achieved;
        p_action.done = achieved;
    }
}
