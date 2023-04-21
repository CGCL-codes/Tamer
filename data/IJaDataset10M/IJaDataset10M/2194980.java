package wotlas.common.objects.magicals;

import wotlas.common.power.*;
import wotlas.common.objects.usefuls.UsefulObject;

/** 
 * The base class for all magical objects.
 * 
 * @author Elann
 * @see wotlas.common.objects.usefuls.UsefulObject
 */
public abstract class MagicalObject extends UsefulObject {

    /** The weave produced by the object. THE CLASS WEAVE DOES NOT EXIST.
   */
    private Weave magicEffect;

    /** The powers involved in the magical action. THE CLASS POWER DOES NOT EXIST.
   */
    private String[] powersInvolved;

    /** The only constructor
   * @param magicEffect The Weave produced
   * @param powersInvolved The Powers involved
   */
    public MagicalObject(Weave magicEffect, String[] powersInvolved) {
        super();
        this.magicEffect = magicEffect;
        this.powersInvolved = powersInvolved;
        this.className = "MagicalObject";
        this.objectName = "default magical object";
    }

    /** Get the magical effect caused by the object when used.
   * @return magicEffect
   */
    public Weave getMagicEffect() {
        return magicEffect;
    }

    /** Get the powers involved in the magical effect.
   * @return powersInvolved
   */
    public String[] getPowersInvolved() {
        return powersInvolved;
    }
}
