package org.jcrpg.world.object;

import java.util.ArrayList;
import java.util.HashMap;
import org.jcrpg.apps.Jcrpg;
import org.jcrpg.threed.J3DCore;
import org.jcrpg.world.ai.EntityMemberInstance;
import org.jcrpg.world.ai.abs.attribute.Attributes;
import org.jcrpg.world.ai.abs.attribute.Resistances;
import org.jcrpg.world.ai.abs.skill.SkillInstance;
import org.jcrpg.world.ai.body.BodyPart;

public class EntityObjInventory {

    EntityMemberInstance instance = null;

    public EntityObjInventory(EntityMemberInstance owner) {
        this.instance = owner;
    }

    /**
	 * Inventory.
	 */
    private ArrayList<ObjInstance> inventory = new ArrayList<ObjInstance>();

    /**
	 * Objects that are currently equipped.
	 */
    private ArrayList<ObjInstance> equipped = new ArrayList<ObjInstance>();

    public boolean hasInInventoryForSkillAndLevel(SkillInstance skill) {
        return hasInListForSkill(inventory, skill);
    }

    public boolean hasInEquippedForSkillAndLevel(SkillInstance skill) {
        return hasInListForSkill(equipped, skill);
    }

    public boolean hasInListForSkill(ArrayList<ObjInstance> list, SkillInstance skill) {
        for (ObjInstance o : list) {
            if (o.description.requirementSkillAndLevel == null) continue;
            if (o.description.requirementSkillAndLevel.skill == skill.skill) {
                if (o.description.requirementSkillAndLevel.level <= skill.level) {
                    if (o.needsAttachmentDependencyForSkill()) {
                        if (o.getAttachedDependencies() != null) {
                            if (hasOneOfTypes(o.getAttachedDependencies())) {
                                return true;
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * Returns objects in inventory that are currently usable for the given skill instance - even checking needed attachments
	 * if it's needed.
	 * @param skill
	 * @return
	 */
    public ArrayList<InventoryListElement> getObjectsForSkillInInventory(SkillInstance skill) {
        return getObjectsForSkillInInventory(skill, -1);
    }

    /**
	 * Returns objects filtering for skill/attachment and lineup too.
	 * @param skill
	 * @param targetLineUpDistance
	 * @return
	 */
    public ArrayList<InventoryListElement> getObjectsForSkillInInventory(SkillInstance skill, int targetLineUpDistance) {
        return getObjectsForSkill(inventory, skill, targetLineUpDistance);
    }

    /**
	 * Returns objects in equipped inventory that are currently usable for the given skill instance - even checking needed attachments
	 * if it's needed.
	 * @param skill
	 * @return
	 */
    public ArrayList<InventoryListElement> getObjectsForSkillInEquipped(SkillInstance skill) {
        return getObjectsForSkillInEquipped(skill, -1);
    }

    /**
	 * Returns objects filtering for skill/attachment and lineup too.
	 * @param skill
	 * @param targetLineUpDistance
	 * @return
	 */
    public ArrayList<InventoryListElement> getObjectsForSkillInEquipped(SkillInstance skill, int targetLineUpDistance) {
        return getObjectsForSkill(equipped, skill, targetLineUpDistance);
    }

    /**
	 * Returns objects that are usable with the skill - checking attachment is present (if needed).
	 * @param list
	 * @param skill
	 * @return
	 */
    private ArrayList<InventoryListElement> getObjectsForSkill(ArrayList<ObjInstance> list, SkillInstance skill, int targetLineUpDistance) {
        HashMap<Obj, InventoryListElement> gatherer = new HashMap<Obj, InventoryListElement>();
        ArrayList<InventoryListElement> objList = new ArrayList<InventoryListElement>();
        for (ObjInstance o : list) {
            if (o.description.requirementSkillAndLevel == null) continue;
            if (o.description.requirementSkillAndLevel.skill == skill.skill) {
                if (o.description.requirementSkillAndLevel.level <= skill.level) {
                    if (targetLineUpDistance >= 0) {
                        if (o.description.getUseRangeInLineup() != Obj.NO_RANGE && o.description.getUseRangeInLineup() < targetLineUpDistance) continue;
                    }
                    if (o.needsAttachmentDependencyForSkill()) {
                        if (o.getAttachedDependencies() != null) {
                            if (hasOneOfTypes(o.getAttachedDependencies())) {
                                InventoryListElement l = null;
                                if (o.description.isGroupable()) {
                                    l = gatherer.get(o.description);
                                }
                                if (l == null) {
                                    l = new InventoryListElement(this, o.description);
                                    gatherer.put(o.description, l);
                                    objList.add(l);
                                }
                                l.objects.add(o);
                            }
                        }
                    } else {
                        InventoryListElement l = null;
                        if (o.description.isGroupable()) {
                            l = gatherer.get(o.description);
                        }
                        if (l == null) {
                            l = new InventoryListElement(this, o.description);
                            gatherer.put(o.description, l);
                            objList.add(l);
                        }
                        l.objects.add(o);
                    }
                }
            }
        }
        return objList;
    }

    public ArrayList<InventoryListElement> getUsableObjects() {
        HashMap<Obj, InventoryListElement> gatherer = new HashMap<Obj, InventoryListElement>();
        ArrayList<InventoryListElement> objList = new ArrayList<InventoryListElement>();
        for (ObjInstance o : inventory) {
            if (o.isAttacheable()) continue;
            if (o.description instanceof Weapon) continue;
            if (o.description instanceof Armor && !equipped.contains(o)) continue;
            boolean add = false;
            SkillInstance i = o.description.requirementSkillAndLevel;
            if (i != null) {
                if (instance.getSkillLevel(i.skill) >= i.level) {
                    add = true;
                }
            } else {
                add = true;
            }
            if (add) {
                InventoryListElement l = null;
                if (o.description.isGroupable()) {
                    l = gatherer.get(o.description);
                }
                if (l == null) {
                    l = new InventoryListElement(this, o.description);
                    gatherer.put(o.description, l);
                    objList.add(l);
                }
                l.objects.add(o);
            }
        }
        return objList;
    }

    /**
	 * 
	 * @param possibleTypesOrdered
	 * @return if inventory contains one of the possible types then returns true.
	 */
    public boolean hasOneOfTypes(ArrayList<Obj> possibleTypesOrdered) {
        if (possibleTypesOrdered == null) return false;
        for (Obj type : possibleTypesOrdered) {
            for (ObjInstance i : inventory) {
                if (i.description == type) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
	 * Get one instance object of the ordered type list if inventory has one, and remove it.
	 * Generally used for getting an ammunition out of the inventory.
	 * @param possibleTypesOrdered
	 * @return The object instance.
	 */
    public ObjInstance getOneInstanceOfTypesAndRemove(ArrayList<Obj> possibleTypesOrdered) {
        if (possibleTypesOrdered == null) return null;
        ObjInstance toRemove = null;
        for (Obj type : possibleTypesOrdered) {
            for (ObjInstance i : inventory) {
                if (i.description == type) {
                    toRemove = i;
                }
            }
        }
        if (toRemove != null) {
            if (toRemove.useOnce()) inventory.remove(toRemove);
        }
        return toRemove;
    }

    public ObjInstance getOneInstanceOfTypeAndRemove(Obj type) {
        if (type == null) return null;
        ObjInstance toRemove = null;
        for (ObjInstance i : inventory) {
            if (i.description == type) {
                toRemove = i;
            }
        }
        if (toRemove != null) {
            if (toRemove.useOnce()) inventory.remove(toRemove);
        }
        return toRemove;
    }

    public void useOnceAndRemove(ObjInstance objInstance) {
        if (objInstance.useOnce()) {
            inventory.remove(objInstance);
        }
    }

    /**
	 * Returns the first possible object instance which is of type in an ordered type list.
	 * @param possibleTypesOrdered
	 * @return
	 */
    public Obj getPossibleNextOneType(ArrayList<Obj> possibleTypesOrdered) {
        if (possibleTypesOrdered == null) return null;
        for (Obj type : possibleTypesOrdered) {
            for (ObjInstance i : inventory) {
                if (i.description == type) {
                    return type;
                }
            }
        }
        return null;
    }

    public boolean equip(ObjInstance equipment) {
        if (!inventory.contains(equipment)) return false;
        if (!(equipment.description instanceof Equippable)) return false;
        ArrayList<BodyPart> parts = instance.description.getBodyType().bodyParts;
        Class<? extends BodyPart> part = ((Equippable) equipment.description).getEquippableBodyPart();
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("EntityObjInventory.equip Part: " + part);
        boolean found = false;
        BodyPart bPart = null;
        for (BodyPart p : parts) {
            if (p.getClass() == part) {
                bPart = p;
                found = true;
            }
        }
        if (!found) return false;
        if (J3DCore.LOGGING()) Jcrpg.LOGGER.finer("EntityObjInventory.equip Part: FOUND " + part);
        int counterForEquipped = 0;
        int maxEquipped = bPart.getMaxNumberOfObjToEquip();
        for (ObjInstance eq : equipped) {
            if (((Equippable) eq.description).getEquippableBodyPart() == part) {
                counterForEquipped++;
            }
            if (counterForEquipped == maxEquipped) return false;
        }
        equipped.add(equipment);
        if (equipment.description instanceof BonusObject) instance.memberState.recalculateMaximums(true);
        return true;
    }

    public boolean unequip(ObjInstance object) {
        if (!equipped.contains(object)) return false;
        if (object.description instanceof BonusObject) {
            if (((BonusObject) object.description).isCursed()) {
                return false;
            }
        }
        equipped.remove(object);
        if (object.description instanceof BonusObject) instance.memberState.recalculateMaximums(true);
        return true;
    }

    public void getSumOfBonuses(BodyPart part) {
    }

    public void remove(ObjInstance object) {
        inventory.remove(object);
        if (equipped.contains(object)) {
            unequip(object);
            equipped.remove(object);
        }
    }

    public void add(ObjInstance object) {
        if (inventory.contains(object)) return;
        inventory.add(object);
    }

    public ArrayList<InventoryListElement> getInventoryListForUse(boolean filterAttachments) {
        return getInventoryList(filterAttachments, true);
    }

    public ArrayList<InventoryListElement> getInventoryList(boolean filterAttachments, boolean filterWeaponArmor) {
        HashMap<Obj, InventoryListElement> gatherer = new HashMap<Obj, InventoryListElement>();
        ArrayList<InventoryListElement> objList = new ArrayList<InventoryListElement>();
        for (ObjInstance o : inventory) {
            if (filterAttachments && o.isAttacheable()) continue;
            if (filterWeaponArmor && o.description instanceof Weapon) continue;
            if (filterWeaponArmor && o.description instanceof Armor && !equipped.contains(o)) continue;
            InventoryListElement l = null;
            if (o.description.isGroupable()) {
                l = gatherer.get(o.description);
            }
            if (l == null) {
                l = new InventoryListElement(this, o.description);
                gatherer.put(o.description, l);
                objList.add(l);
            }
            l.objects.add(o);
        }
        return objList;
    }

    public ArrayList<ObjInstance> getInventory() {
        return inventory;
    }

    public ArrayList<ObjInstance> getEquipped() {
        return equipped;
    }

    public Armor getEquippedArmor(Class<? extends BodyPart> lookUp) {
        for (ObjInstance i : equipped) {
            if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("check attr bonus I " + i.getName());
            if (i.description instanceof Armor) {
                Armor bo = (Armor) i.description;
                if (lookUp != null) {
                    if (i.description instanceof Equippable) {
                        if (((Equippable) i.description).getEquippableBodyPart() != lookUp) continue;
                    } else {
                        return bo;
                    }
                }
            }
        }
        return null;
    }

    public Attributes getEquipmentAttributeValues(Class<? extends BodyPart> lookUp) {
        Attributes sum = null;
        for (ObjInstance i : equipped) {
            if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("EntObjInv getEquipmentAttributeValues: check attr bonus I " + i.getName());
            if (i.description instanceof BonusObject) {
                BonusObject bo = (BonusObject) i.description;
                if (lookUp == null && bo.isBodyPartBonusOnly()) continue;
                if (lookUp != null) {
                    if (!bo.isBodyPartBonusOnly()) continue;
                    if (i.description instanceof Equippable) {
                        if (((Equippable) i.description).getEquippableBodyPart() != lookUp) continue;
                    } else {
                        continue;
                    }
                }
                if (J3DCore.LOGGING()) Jcrpg.LOGGER.finest("" + bo.getAttributeValues());
                if (bo.getAttributeValues() != null) {
                    Attributes bonus = bo.getAttributeValues();
                    try {
                        if (sum == null) sum = bonus.getClass().newInstance();
                        sum.appendAttributes(bonus);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return sum;
    }

    public Resistances getEquipmentResistanceValues(Class<? extends BodyPart> lookUp) {
        Resistances sum = null;
        for (ObjInstance i : equipped) {
            if (i.description instanceof BonusObject) {
                BonusObject bo = (BonusObject) i.description;
                if (lookUp == null && bo.isBodyPartBonusOnly()) continue;
                if (lookUp != null) {
                    if (!bo.isBodyPartBonusOnly()) continue;
                    if (i.description instanceof Equippable) {
                        if (((Equippable) i.description).getEquippableBodyPart() != lookUp) continue;
                    } else {
                        continue;
                    }
                }
                if (bo.getAttributeValues() != null) {
                    Resistances bonus = bo.getResistanceValues();
                    try {
                        if (sum == null) sum = bonus.getClass().newInstance();
                        sum.appendResistances(bonus);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        return sum;
    }
}
