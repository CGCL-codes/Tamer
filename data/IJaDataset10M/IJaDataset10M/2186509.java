package org.jcrpg.ui.window.interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import org.jcrpg.game.EncounterLogic;
import org.jcrpg.game.element.TurnActMemberChoice;
import org.jcrpg.game.element.TurnActUnitLineup;
import org.jcrpg.game.logic.PlayerActChoiceInfo;
import org.jcrpg.threed.PooledSharedNode;
import org.jcrpg.ui.UIBase;
import org.jcrpg.ui.UIImageCache;
import org.jcrpg.ui.window.PagedInputWindow;
import org.jcrpg.ui.window.element.TextLabel;
import org.jcrpg.ui.window.element.input.CheckBox;
import org.jcrpg.ui.window.element.input.InputBase;
import org.jcrpg.ui.window.element.input.ListMultiSelect;
import org.jcrpg.ui.window.element.input.ListSelect;
import org.jcrpg.ui.window.element.input.TextButton;
import org.jcrpg.util.Language;
import org.jcrpg.world.ai.Ecology;
import org.jcrpg.world.ai.EncounterInfo;
import org.jcrpg.world.ai.EncounterUnitData;
import org.jcrpg.world.ai.EntityMemberInstance;
import org.jcrpg.world.ai.EntityScaledRelationType;
import org.jcrpg.world.ai.abs.skill.SkillActForm;
import org.jcrpg.world.ai.abs.skill.SkillBase;
import org.jcrpg.world.ai.abs.skill.SkillGroups;
import org.jcrpg.world.ai.abs.skill.TurnActSkill;
import org.jcrpg.world.ai.humanoid.MemberPerson;
import org.jcrpg.world.ai.player.PartyInstance;
import org.jcrpg.world.object.BonusObject;
import org.jcrpg.world.object.BonusSkillActFormDesc;
import org.jcrpg.world.object.InventoryListElement;
import org.jcrpg.world.object.Obj;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.shape.Quad;

/**
 * Social rivalry or combat with turn based acting on behalf of player and AI, decisions of Player are done through this window.
 * Player should be able to select skills to use, select groups to use the skill on, select way of using a skill, etc.
 * @author pali
 *
 */
public class TurnActWindow extends PagedInputWindow {

    Node pageIntro = new Node();

    TextLabel introTitle;

    /**
	 * Page where selecting what to do in a turn is being done.
	 */
    Node page0 = new Node();

    ListSelect memberSelect;

    ListSelect skillSelect;

    ListMultiSelect groupSelect;

    TextButton leave;

    TextButton ok;

    TextLabel header, desc;

    ArrayList<TextLabel> memberNames = new ArrayList<TextLabel>();

    ArrayList<ListSelect> skillSelectors = new ArrayList<ListSelect>();

    ArrayList<ListSelect> skillActFormSelectors = new ArrayList<ListSelect>();

    ArrayList<ListSelect> groupSelectors = new ArrayList<ListSelect>();

    ArrayList<ListSelect> inventorySelectors = new ArrayList<ListSelect>();

    ArrayList<CheckBox> validityBoxes = new ArrayList<CheckBox>();

    public TurnActWindow(UIBase base) {
        super(base);
        try {
            Quad hudQuad = loadImageToQuad("/popups/turnact.png", 0.6f * core.getDisplay().getWidth(), 1.2f * (core.getDisplay().getHeight() / 2), core.getDisplay().getWidth() / 2, 1.55f * core.getDisplay().getHeight() / 2);
            hudQuad.setRenderState(base.hud.hudAS);
            Mesh sQuad = PooledSharedNode.copyMesh("", hudQuad);
            sQuad.setTranslation(hudQuad.getTranslation());
            pageIntro.attachChildAt(sQuad, 0);
            introTitle = new TextLabel("", this, pageIntro, 0.5f, 0.19f, 0.3f, 0.06f, 450f, Language.v("turnActWindow.intro"), false, true, new ColorRGBA(0.7f, 0.1f, 0.1f, 1f));
            new TextLabel("", this, pageIntro, 0.5f, 0.22f, 0.3f, 0.06f, 600f, Language.v("turnActWindow.pressText"), false, true, new ColorRGBA(ColorRGBA.BLACK));
            addPage(1, pageIntro);
        } catch (Exception ex) {
        }
        try {
            Quad hudQuad = loadImageToQuad("/nonPatternFrame1_trans.png", 0.75f * core.getDisplay().getWidth(), 1.67f * (core.getDisplay().getHeight() / 2), core.getDisplay().getWidth() / 2, 1.18f * core.getDisplay().getHeight() / 2);
            hudQuad.setRenderState(base.hud.hudAS);
            Mesh sQuad = PooledSharedNode.copyMesh("", hudQuad);
            sQuad.setTranslation(hudQuad.getTranslation());
            page0.attachChildAt(sQuad, 0);
            Quad shieldQuad = loadImageToQuad("/windowicons/turnact_shield.png", 0.1f * core.getDisplay().getWidth(), 0.1f * (core.getDisplay().getHeight()), 0.35f * core.getDisplay().getWidth() / 2f, 1.85f * core.getDisplay().getHeight() / 2);
            shieldQuad.setRenderState(base.hud.hudAS);
            Mesh shQuad1 = PooledSharedNode.copyMesh("", shieldQuad);
            Mesh shQuad2 = PooledSharedNode.copyMesh("", shieldQuad);
            shQuad2.setTranslation(1.65f * core.getDisplay().getWidth() / 2f, 1.85f * core.getDisplay().getHeight() / 2, 0);
            page0.attachChildAt(shQuad1, 0);
            page0.attachChildAt(shQuad2, 0);
            header = new TextLabel("", this, page0, 0.50f, 0.044f, 0.0f, 0.06f, 400f, Language.v("turnActWindow.header"), false, true, InputBase.DEF_NORMAL_COLOR);
            desc = new TextLabel("", this, page0, 0.24f, 0.075f, 0.0f, 0.06f, 600f, "You are facing the inevitable.", false);
            new TextLabel("", this, page0, 0.24f, 0.100f, 0.0f, 0.06f, 600f, "You have to choose what skills, acts and items to use.", false);
            float sizeSelect = 0.10f;
            for (int i = 0; i < 6; i++) {
                CheckBox box = new CheckBox("box" + i, this, page0, 0.18f, 0.20f + sizeSelect * i, 0.03f, 0.04f, false);
                validityBoxes.add(box);
                skillSelectors.add(new ListSelect("skill" + i, this, page0, 0.38f, 0.15f + sizeSelect * i, 0.3f, 0.04f, 600f, new String[0], new String[0], null, null));
                skillActFormSelectors.add(new ListSelect("actForm" + i, this, page0, 0.70f, 0.15f + sizeSelect * i, 0.3f, 0.04f, 600f, new String[0], new String[0], null, null));
                inventorySelectors.add(new ListSelect("inv" + i, this, page0, 0.38f, 0.25f, 0.20f + sizeSelect * i, 0.3f, 0.04f, 600f, new String[0], new String[0], new Object[0], new Quad[0], null, null));
                groupSelectors.add(new ListSelect("group" + i, this, page0, 0.70f, 0.20f + sizeSelect * i, 0.3f, 0.04f, 600f, new String[0], new String[0], null, null));
                memberNames.add(new TextLabel("name" + i, this, page0, 0.15f, 0.15f + sizeSelect * i, 0.0f, 0.04f, 600f, "", false));
                addInput(0, skillSelectors.get(i));
                addInput(0, skillActFormSelectors.get(i));
                addInput(0, inventorySelectors.get(i));
                addInput(0, groupSelectors.get(i));
            }
            ok = new TextButton("ok", this, page0, 0.24f, 0.77f, 0.18f, 0.06f, 500f, Language.v("turnActWindow.ok"), "S");
            new TextLabel("", this, page0, 0.60f, 0.74f, 0.1f, 0.06f, 600f, "Use <>^V & 1-6 for selection.", false);
            new TextLabel("", this, page0, 0.60f, 0.78f, 0.1f, 0.06f, 600f, "Use S if you are ready.", false);
            addInput(0, ok);
            leave = new TextButton("leave", this, page0, 0.46f, 0.77f, 0.18f, 0.06f, 500f, Language.v("turnActWindow.leave"), "L");
            addInput(0, leave);
            addPage(0, page0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        base.addEventHandler("enter", this);
        base.addEventHandler("1", this);
        base.addEventHandler("2", this);
        base.addEventHandler("3", this);
        base.addEventHandler("4", this);
        base.addEventHandler("5", this);
        base.addEventHandler("6", this);
        base.addEventHandler("U", this);
        base.addEventHandler("N", this);
        base.addEventHandler("T", this);
        base.addEventHandler("B", this);
        base.addEventHandler("inventoryWindow", this);
        base.addEventHandler("charSheetWindow", this);
    }

    public PartyInstance party;

    public EncounterInfo encountered;

    public boolean playerInitiated = false;

    public int turnActType;

    String langPostfix = "";

    /**
	 * Indicates if this is a combat turn act phase or a social rivalry.
	 */
    boolean combat = false;

    /**
	 * number of characters shown on page, updateToParty sets it.
	 */
    int numberOfChars = 0;

    public void setPageData(int turnActType, PartyInstance party, EncounterInfo encountered, boolean playerInitiated) {
        currentPage = 1;
        this.playerInitiated = playerInitiated;
        if (turnActType == EncounterLogic.ENCOUTNER_PHASE_RESULT_COMBAT) {
            langPostfix = "combat";
            combat = true;
        } else {
            langPostfix = "social";
            combat = false;
        }
        party.turnActPhaseCounter++;
        introTitle.text = Language.v("turnActWindow.intro." + langPostfix) + " " + party.turnActPhaseCounter;
        introTitle.activate();
        header.text = Language.v("turnActWindow.header." + langPostfix);
        header.activate();
        desc.text = Language.v("turnActWindow.desc." + langPostfix);
        desc.activate();
        this.party = party;
        this.encountered = encountered;
        info = new PlayerActChoiceInfo();
    }

    public static Object doNothingSkillChoiceObject = new Object();

    public static Object doUseSkillChoiceObject = new Object();

    public void clear() {
        encountered = null;
        for (ListSelect s : groupSelectors) {
            s.objects = null;
        }
        if (encounterUnitDataList != null) encounterUnitDataList.clear();
    }

    TreeMap<Integer, Class<? extends SkillBase>> tempFilteredSkills = new TreeMap<Integer, Class<? extends SkillBase>>();

    public void updateToParty() {
        int counter = 0;
        if (party != null) for (EntityMemberInstance i : party.orderedParty) {
            if (i != null && !i.isDead()) {
                ListSelect select = skillSelectors.get(counter);
                inventorySelectors.get(counter).reattach();
                validityBoxes.get(counter).reattach();
                skillActFormSelectors.get(counter).reattach();
                groupSelectors.get(counter).reattach();
                select.reattach();
                select.subject = i;
                Collection<Class<? extends SkillBase>> skills = i.description.getCommonSkills().getSkillsOfType(combat ? Ecology.PHASE_TURNACT_COMBAT : Ecology.PHASE_TURNACT_SOCIAL_RIVALRY);
                if (skills == null) skills = new HashSet<Class<? extends SkillBase>>();
                tempFilteredSkills.clear();
                for (Class<? extends SkillBase> skill : skills) {
                    SkillBase base = SkillGroups.skillBaseInstances.get(skill);
                    if (base.getActForms() == SkillBase.noActFormList) continue;
                    if (base.needsInventoryItem) {
                        if (!i.inventory.hasInInventoryForSkillAndLevel(i.description.getCommonSkills().skills.get(skill))) continue;
                    }
                    int level = i.description.getCommonSkills().getSkillLevel(skill, null);
                    while (tempFilteredSkills.get(10000 - level) != null) {
                        level++;
                    }
                    tempFilteredSkills.put(10000 - level, skill);
                }
                skills = tempFilteredSkills.values();
                String[] texts = new String[skills.size() + 2];
                Object[] objects = new Object[skills.size() + 2];
                String[] ids = new String[skills.size() + 2];
                int counter_2 = 0;
                int selected = 0;
                for (Class<? extends SkillBase> skill : skills) {
                    String text = Language.v("skills." + skill.getSimpleName()) + " (" + i.description.getCommonSkills().getSkillLevel(skill, null) + ")";
                    texts[counter_2] = text;
                    ids[counter_2] = "" + counter_2;
                    SkillBase b = (SkillBase) SkillGroups.skillBaseInstances.get(skill);
                    objects[counter_2] = b;
                    if (i.behaviorSkill != null && i.behaviorSkill.getClass() == b.getClass()) {
                        selected = counter_2;
                    }
                    counter_2++;
                }
                ids[ids.length - 2] = "Do Use";
                texts[texts.length - 2] = Language.v("turnActWindow.Use");
                objects[objects.length - 2] = doUseSkillChoiceObject;
                ids[ids.length - 1] = "Do Nothing";
                texts[texts.length - 1] = Language.v("turnActWindow.DoNothing");
                objects[objects.length - 1] = doNothingSkillChoiceObject;
                select.ids = ids;
                select.texts = texts;
                select.objects = objects;
                select.setUpdated(true);
                select.deactivate();
                select.setSelected(selected);
                memberNames.get(counter).text = ((MemberPerson) i.description).foreName;
                memberNames.get(counter).activate();
                memberNames.get(counter).reattach();
                inputChanged(select, "");
                counter++;
            }
        }
        for (int i = counter; i < 6; i++) {
            ListSelect select = skillSelectors.get(i);
            select.detach();
            select = groupSelectors.get(i);
            select.detach();
            select = skillActFormSelectors.get(i);
            select.detach();
            select = inventorySelectors.get(i);
            select.detach();
            memberNames.get(i).detach();
            validityBoxes.get(i).detach();
        }
        numberOfChars = counter;
    }

    ArrayList<EncounterUnitData> encounterUnitDataList = null;

    @Override
    public void setupPage() {
        if (currentPage == 0 && !noNeedForRefreshPage) {
            encounterUnitDataList = encountered.getEncounterUnitDataList(null);
            ArrayList<EncounterUnitData> list = encounterUnitDataList;
            for (ListSelect groupSelect : groupSelectors) {
                String[] ids = new String[list.size()];
                Object[] objects = new Object[list.size()];
                String[] texts = new String[list.size()];
                int count = 0;
                for (EncounterUnitData data : list) {
                    ids[count] = "" + count;
                    data.updateNameInTurnActPhase();
                    texts[count] = data.name;
                    objects[count] = data;
                    count++;
                }
                groupSelect.reset();
                groupSelect.ids = ids;
                groupSelect.objects = objects;
                groupSelect.texts = texts;
                groupSelect.setUpdated(true);
                groupSelect.activate();
            }
            updateToParty();
            restoreSettings();
            for (ListSelect skillSelect : skillSelectors) {
                if (skillSelect.getSelectedObject() == doNothingSkillChoiceObject || skillSelect.getSelectedObject() == doUseSkillChoiceObject) {
                    inputChanged(skillSelect, "fake");
                }
            }
            for (int i = 0; i < numberOfChars; i++) {
                ListSelect select = skillSelectors.get(i);
                select.deactivate();
                select = groupSelectors.get(i);
                select.deactivate();
                select = skillActFormSelectors.get(i);
                select.deactivate();
                select = inventorySelectors.get(i);
                select.deactivate();
            }
        }
        noNeedForRefreshPage = false;
        super.setupPage();
        if (currentPage == 0) {
            getSelected().deactivate();
            for (int i = 0; i < numberOfChars; i++) {
                inputLeft(skillSelectors.get(i), "setup");
            }
        }
        for (int i = numberOfChars; i < 6; i++) {
            ListSelect select = skillSelectors.get(i);
            select.detach();
            select = groupSelectors.get(i);
            select.detach();
            select = skillActFormSelectors.get(i);
            select.detach();
            select = inventorySelectors.get(i);
            select.detach();
        }
    }

    @Override
    public boolean inputLeft(InputBase base, String message) {
        if (skillSelectors.contains(base) || skillActFormSelectors.contains(base) || groupSelectors.contains(base) || inventorySelectors.contains(base)) {
            int charNo = 0;
            if (skillSelectors.contains(base)) {
                charNo = skillSelectors.indexOf(base);
            }
            if (skillActFormSelectors.contains(base)) {
                charNo = skillActFormSelectors.indexOf(base);
            }
            if (groupSelectors.contains(base)) {
                charNo = groupSelectors.indexOf(base);
            }
            if (inventorySelectors.contains(base)) {
                charNo = inventorySelectors.indexOf(base);
            }
            boolean b = checkIsValidAct(charNo);
            System.out.println("######### CHAR " + charNo + " VALID? " + b);
            validityBoxes.get(charNo).setChecked(b);
            validityBoxes.get(charNo).deactivate();
        }
        return false;
    }

    @Override
    public boolean inputEntered(InputBase base, String message) {
        return false;
    }

    public HashMap<EntityMemberInstance, SkillBase> hmMemberSelectedSkill = new HashMap<EntityMemberInstance, SkillBase>();

    @SuppressWarnings("unchecked")
    @Override
    public boolean inputChanged(InputBase base, String message) {
        if (skillSelectors.contains(base)) {
            ListSelect skillSelect = (ListSelect) base;
            EntityMemberInstance i = (EntityMemberInstance) skillSelect.subject;
            int index = skillSelectors.indexOf(skillSelect);
            ListSelect skillActFormSelect = skillActFormSelectors.get(index);
            ListSelect inventorySelect = inventorySelectors.get(index);
            if (skillSelect.getSelectedObject() == doNothingSkillChoiceObject || skillSelect.getSelectedObject() == doUseSkillChoiceObject) {
                String[] texts = new String[0];
                Object[] objects = new Object[0];
                String[] ids = new String[0];
                skillActFormSelect.ids = ids;
                skillActFormSelect.texts = texts;
                skillActFormSelect.objects = objects;
                skillActFormSelect.setUpdated(true);
                skillActFormSelect.deactivate();
                skillActFormSelect.setSelected(0);
                skillActFormSelect.storedState = null;
                skillActFormSelect.setEnabled(false);
            }
            if (skillSelect.getSelectedObject() == doNothingSkillChoiceObject) {
                String[] texts = new String[0];
                Object[] objects = new Object[0];
                String[] ids = new String[0];
                Quad[] icons = new Quad[0];
                inventorySelect.ids = ids;
                inventorySelect.texts = texts;
                inventorySelect.objects = objects;
                inventorySelect.icons = icons;
                inventorySelect.setUpdated(true);
                inventorySelect.deactivate();
                inventorySelect.setSelected(0);
                inventorySelect.storedState = null;
                inventorySelect.setEnabled(false);
                base.activate();
            } else if (skillSelect.getSelectedObject() == doUseSkillChoiceObject) {
                ArrayList<InventoryListElement> objInstances = i.inventory.getUsableObjects();
                String[] texts = new String[objInstances.size()];
                Object[] objects = new Object[objInstances.size()];
                String[] ids = new String[objInstances.size()];
                Quad[] icons = new Quad[objInstances.size()];
                int counter = 0;
                for (InventoryListElement objInstance : objInstances) {
                    ids[counter] = "" + counter;
                    texts[counter] = objInstance.getName();
                    objects[counter] = objInstance;
                    try {
                        icons[counter] = UIImageCache.getImage(UIBase.RES_TYPE_GAME_2D, objInstance.description.getIconFilePath(), true, 15f, 15f);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    counter++;
                }
                inventorySelect.ids = ids;
                inventorySelect.texts = texts;
                inventorySelect.objects = objects;
                inventorySelect.icons = icons;
                inventorySelect.setUpdated(true);
                inventorySelect.deactivate();
                inventorySelect.setEnabled(true);
                inventorySelect.setSelected(0);
                inputChanged(inventorySelect, "fake");
                base.activate();
            } else {
                SkillBase s = (SkillBase) skillSelect.getSelectedObject();
                {
                    ArrayList<Class<? extends SkillActForm>> forms = i.getDoableActForms(s.getClass());
                    TreeMap<Integer, Class<? extends SkillActForm>> orderedForms = new TreeMap<Integer, Class<? extends SkillActForm>>();
                    for (Class<? extends SkillActForm> form : forms) {
                        SkillActForm formInst = SkillGroups.getSkillActFormInstance(s.getClass(), form);
                        Integer point = formInst.getBiggestUsedPoint();
                        while (orderedForms.get(point) != null) {
                            point++;
                        }
                        orderedForms.put(point, form);
                    }
                    forms.clear();
                    forms.addAll(orderedForms.values());
                    String[] texts = new String[forms.size()];
                    Object[] objects = new Object[forms.size()];
                    String[] ids = new String[forms.size()];
                    int counter = 0;
                    for (Class<? extends SkillActForm> form : forms) {
                        ids[counter] = "" + counter;
                        texts[counter] = SkillGroups.getSkillActFormInstance(s.getClass(), form).getName();
                        objects[counter] = form;
                        counter++;
                    }
                    skillActFormSelect.ids = ids;
                    skillActFormSelect.texts = texts;
                    skillActFormSelect.objects = objects;
                    skillActFormSelect.setUpdated(true);
                    skillActFormSelect.deactivate();
                    skillActFormSelect.setSelected(0);
                    skillActFormSelect.setEnabled(true);
                    if (counter == 0) {
                    } else {
                        inputChanged(skillActFormSelect, "fake");
                    }
                }
                if (s.needsInventoryItem) {
                    ArrayList<InventoryListElement> objInstances = i.inventory.getObjectsForSkillInInventory(i.description.getCommonSkills().skills.get(s.getClass()));
                    String[] texts = new String[objInstances.size()];
                    Object[] objects = new Object[objInstances.size()];
                    String[] ids = new String[objInstances.size()];
                    Quad[] icons = new Quad[objInstances.size()];
                    int counter = 0;
                    for (InventoryListElement objInstance : objInstances) {
                        ids[counter] = "" + counter;
                        texts[counter] = objInstance.getName();
                        objects[counter] = objInstance;
                        try {
                            icons[counter] = UIImageCache.getImage(UIBase.RES_TYPE_GAME_2D, objInstance.description.getIconFilePath(), true, 15f, 15f);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        counter++;
                    }
                    inventorySelect.ids = ids;
                    inventorySelect.texts = texts;
                    inventorySelect.objects = objects;
                    inventorySelect.icons = icons;
                    inventorySelect.setUpdated(true);
                    inventorySelect.deactivate();
                    inventorySelect.setEnabled(true);
                    inventorySelect.setSelected(0);
                    inputChanged(inventorySelect, "fake");
                } else {
                    inventorySelect.ids = new String[0];
                    inventorySelect.texts = new String[0];
                    inventorySelect.setUpdated(true);
                    inventorySelect.deactivate();
                    inputChanged(inventorySelect, "fake");
                }
                base.activate();
            }
            return true;
        } else if (skillActFormSelectors.contains(base)) {
            ListSelect skillActFormSelect = (ListSelect) base;
            Class<? extends SkillActForm> form = (Class<? extends SkillActForm>) skillActFormSelect.getSelectedObject();
            if (form == null) return true;
            int index = skillActFormSelectors.indexOf(skillActFormSelect);
            ListSelect skillSelect = skillSelectors.get(index);
            ListSelect groupSelect = groupSelectors.get(index);
            ListSelect inventorySelect = inventorySelectors.get(index);
            EntityMemberInstance i = (EntityMemberInstance) skillSelect.subject;
            SkillBase s = (SkillBase) skillSelect.getSelectedObject();
            InventoryListElement objInstance = null;
            int maxLineUpDistanceRange = Obj.NO_RANGE;
            if (s.needsInventoryItem) {
                objInstance = (InventoryListElement) inventorySelect.getSelectedObject();
                if (objInstance != null) {
                    maxLineUpDistanceRange = objInstance.description.getUseRangeInLineup();
                } else {
                    maxLineUpDistanceRange = 5;
                }
            }
            if (s instanceof TurnActSkill) {
                int skillLineRange = ((TurnActSkill) s).getUseRangeInLineup();
                if (skillLineRange != -1) {
                    skillLineRange += 2;
                    if (maxLineUpDistanceRange > skillLineRange || maxLineUpDistanceRange == Obj.NO_RANGE) {
                        maxLineUpDistanceRange = skillLineRange;
                    }
                }
            }
            SkillActForm formInstance = s.getActForm(form);
            boolean friendly = true;
            boolean groupTarget = false;
            if (formInstance.atomicEffect <= 0) {
                friendly = false;
            }
            if (formInstance.targetType != SkillActForm.TARGETTYPE_LIVING_MEMBER) {
                groupTarget = true;
            }
            ArrayList<EncounterUnitData> list = encounterUnitDataList;
            updateGroupSelect(groupSelect, list, i.encounterData.turnActLineup, i.encounterData.getCurrentLine(), maxLineUpDistanceRange, friendly, groupTarget);
            if ("fake".equals(message)) skillSelect.activate(); else base.activate();
        } else if (inventorySelectors.contains(base)) {
            int index = inventorySelectors.indexOf(base);
            ListSelect skillSelect = skillSelectors.get(index);
            EntityMemberInstance i = (EntityMemberInstance) skillSelect.subject;
            if (skillSelect.getSelectedObject() == doUseSkillChoiceObject) {
                ListSelect inventorySelect = inventorySelectors.get(index);
                ListSelect groupSelect = groupSelectors.get(index);
                InventoryListElement objInst = (InventoryListElement) inventorySelect.getSelectedObject();
                if (objInst != null) {
                    int range = objInst.description.getUseRangeInLineup();
                    boolean friendly = true;
                    boolean groupTarget = false;
                    if (objInst.objects.get(0).description instanceof BonusObject) {
                        ArrayList<BonusSkillActFormDesc> bonusList = ((BonusObject) objInst.objects.get(0).description).getSkillActFormBonusEffectTypes();
                        if (bonusList != null && bonusList.size() > 0) {
                            if (bonusList.get(0).form.atomicEffect >= 0) {
                                friendly = true;
                            } else {
                                friendly = false;
                            }
                        }
                    }
                    updateGroupSelect(groupSelect, encounterUnitDataList, i.encounterData.turnActLineup, i.encounterData.getCurrentLine(), range, friendly, groupTarget);
                }
            } else {
                ListSelect skillActFormSelect = skillActFormSelectors.get(index);
                inputChanged(skillActFormSelect, "inventory");
            }
            base.activate();
        }
        return false;
    }

    /**
	 * Updates group select based on provided information, filtering out unnecessary elements.
	 * @param groupSelect The groupselector to update.
	 * @param list List of possible encountered.
	 * @param lineUpOfSource source member's lineup
	 * @param maxLineUpDistanceRange max lineup range
	 * @param friendly is this a friendly act
	 * @param groupTarget is this a group targetted act
	 */
    private void updateGroupSelect(ListSelect groupSelect, ArrayList<EncounterUnitData> list, TurnActUnitLineup sourceLineup, int lineUpOfSource, int maxLineUpDistanceRange, boolean friendly, boolean groupTarget) {
        ArrayList<EncounterUnitData> filteredList = new ArrayList<EncounterUnitData>();
        for (EncounterUnitData unitData : list) {
            if (unitData.turnActLineup == sourceLineup) {
                lineUpOfSource = 0;
            }
            if (maxLineUpDistanceRange != Obj.NO_RANGE) {
                if (lineUpOfSource + unitData.getCurrentLine() > maxLineUpDistanceRange) {
                    continue;
                }
            }
            if (groupTarget && !unitData.isGroupId) continue;
            if (!groupTarget && unitData.getUnit() == party.theFragment) continue;
            int relation = party.theFragment.getRelationLevel(unitData.getUnit());
            if (!friendly) {
                if (unitData.getUnit() == party.theFragment) continue;
                if (relation > EntityScaledRelationType.NEUTRAL) continue;
                if (unitData.parent == party.theFragment) continue;
            } else {
                if (relation <= EntityScaledRelationType.NEUTRAL) {
                    if (unitData.getUnit() != party.theFragment && unitData.parent != party.theFragment) continue;
                }
            }
            filteredList.add(unitData);
        }
        list = filteredList;
        String[] ids = new String[list.size()];
        Object[] objects = new Object[list.size()];
        String[] texts = new String[list.size()];
        int count = 0;
        for (EncounterUnitData data : list) {
            ids[count] = "" + count;
            data.updateNameInTurnActPhase();
            texts[count] = data.name;
            objects[count] = data;
            count++;
        }
        groupSelect.reset();
        groupSelect.ids = ids;
        groupSelect.objects = objects;
        groupSelect.texts = texts;
        groupSelect.setUpdated(true);
        groupSelect.deactivate();
    }

    private boolean checkIsValidAct(int character) {
        int counter = character;
        info.memberToChoice.clear();
        {
            ListSelect s = skillSelectors.get(character);
            if (s.isEnabled()) {
                EntityMemberInstance i = (EntityMemberInstance) s.subject;
                TurnActMemberChoice choice = new TurnActMemberChoice();
                choice.member = i;
                if (skillSelectors.get(counter).getSelectedObject() == doNothingSkillChoiceObject) {
                    choice.doNothing = true;
                } else if (skillSelectors.get(counter).getSelectedObject() == doUseSkillChoiceObject) {
                    choice.doUse = true;
                    InventoryListElement obj = (InventoryListElement) inventorySelectors.get(counter).getSelectedObject();
                    EncounterUnitData fragmentAndSubunit = null;
                    fragmentAndSubunit = (EncounterUnitData) groupSelectors.get(counter).getSelectedObject();
                    choice.target = fragmentAndSubunit;
                    choice.targetMember = fragmentAndSubunit.getFirstLivingMember();
                    choice.usedObject = obj;
                    if (obj == null) return false;
                } else {
                    SkillBase sb = null;
                    sb = (SkillBase) skillSelectors.get(counter).getSelectedObject();
                    InventoryListElement obj = (InventoryListElement) inventorySelectors.get(counter).getSelectedObject();
                    Class<? extends SkillActForm> f = null;
                    f = (Class<? extends SkillActForm>) skillActFormSelectors.get(counter).getSelectedObject();
                    if (f == null) return false;
                    if (sb.needsInventoryItem && obj == null) return false;
                    EncounterUnitData fragmentAndSubunit = null;
                    fragmentAndSubunit = (EncounterUnitData) groupSelectors.get(counter).getSelectedObject();
                    if (fragmentAndSubunit == null) return false;
                    choice.skill = i.getSkills().skills.get(sb.getClass());
                    SkillActForm selectedForm = null;
                    for (SkillActForm formInst : sb.getActForms()) {
                        if (formInst.getClass() == f) {
                            selectedForm = formInst;
                        }
                    }
                    choice.skillActForm = selectedForm;
                    choice.target = fragmentAndSubunit;
                    choice.targetMember = fragmentAndSubunit.getFirstLivingMember();
                    if (selectedForm != null && selectedForm.skill.needsInventoryItem) {
                        choice.usedObject = obj;
                    } else {
                        choice.usedObject = null;
                    }
                }
                info.memberToChoice.put(i, choice);
            }
            counter++;
        }
        return true;
    }

    public PlayerActChoiceInfo info = new PlayerActChoiceInfo();

    @SuppressWarnings("unchecked")
    @Override
    public boolean inputUsed(InputBase base, String message) {
        if (base == leave) {
            if (core.gameState.gameLogic.encounterLogic.checkLeaveEncounterPhase(party.theFragment, encountered)) {
                toggle();
                core.uiBase.hud.mainBox.addEntry("Your party will try to leave the encounter.");
                int counter = 0;
                info.memberToChoice.clear();
                for (ListSelect s : skillSelectors) {
                    if (s.isEnabled()) {
                        EntityMemberInstance i = (EntityMemberInstance) s.subject;
                        TurnActMemberChoice choice = new TurnActMemberChoice();
                        choice.member = i;
                        choice.doNothing = true;
                        choice.doEscape = true;
                        info.memberToChoice.put(i, choice);
                    }
                    counter++;
                }
                info.doEscape = true;
                info.callbackWindow = this;
                core.gameState.gameLogic.encounterLogic.doTurnActTurn(info, encountered);
            } else {
                core.uiBase.hud.mainBox.addEntry("Your party couldn't leave the encounter.");
            }
            return true;
        }
        if (base == ok) {
            int counter = 0;
            info.memberToChoice.clear();
            for (ListSelect s : skillSelectors) {
                if (s.isEnabled()) {
                    EntityMemberInstance i = (EntityMemberInstance) s.subject;
                    TurnActMemberChoice choice = new TurnActMemberChoice();
                    choice.member = i;
                    if (skillSelectors.get(counter).getSelectedObject() == doNothingSkillChoiceObject) {
                        choice.doNothing = true;
                    } else if (skillSelectors.get(counter).getSelectedObject() == doUseSkillChoiceObject) {
                        choice.doUse = true;
                        InventoryListElement obj = (InventoryListElement) inventorySelectors.get(counter).getSelectedObject();
                        EncounterUnitData fragmentAndSubunit = null;
                        fragmentAndSubunit = (EncounterUnitData) groupSelectors.get(counter).getSelectedObject();
                        choice.target = fragmentAndSubunit;
                        choice.targetMember = fragmentAndSubunit.getFirstLivingMember();
                        choice.usedObject = obj;
                        if (obj == null) return true;
                    } else {
                        SkillBase sb = null;
                        sb = (SkillBase) skillSelectors.get(counter).getSelectedObject();
                        InventoryListElement obj = (InventoryListElement) inventorySelectors.get(counter).getSelectedObject();
                        Class<? extends SkillActForm> f = null;
                        f = (Class<? extends SkillActForm>) skillActFormSelectors.get(counter).getSelectedObject();
                        if (f == null) return true;
                        if (sb.needsInventoryItem && obj == null) return true;
                        EncounterUnitData fragmentAndSubunit = null;
                        fragmentAndSubunit = (EncounterUnitData) groupSelectors.get(counter).getSelectedObject();
                        if (fragmentAndSubunit == null) return true;
                        choice.skill = i.getSkills().skills.get(sb.getClass());
                        SkillActForm selectedForm = null;
                        for (SkillActForm formInst : sb.getActForms()) {
                            if (formInst.getClass() == f) {
                                selectedForm = formInst;
                            }
                        }
                        choice.skillActForm = selectedForm;
                        choice.target = fragmentAndSubunit;
                        choice.targetMember = fragmentAndSubunit.getFirstLivingMember();
                        if (selectedForm != null && selectedForm.skill.needsInventoryItem) {
                            choice.usedObject = obj;
                        } else {
                            choice.usedObject = null;
                        }
                    }
                    info.memberToChoice.put(i, choice);
                }
                counter++;
            }
            core.uiBase.hud.mainBox.addEntry("Starting turn!");
            toggle();
            info.callbackWindow = this;
            core.gameState.gameLogic.encounterLogic.doTurnActTurn(info, encountered);
            storeSettings();
            return true;
        }
        return false;
    }

    public boolean noNeedForRefreshPage = false;

    @Override
    public boolean handleKey(String key) {
        if (super.handleKey(key)) return true;
        if ("charSheetWindow".equals(key)) {
            if (currentPage == 0) {
                noNeedForRefreshPage = true;
                core.charSheetWindow.fallbackWindow = this;
                core.charSheetWindow.noToggleWindowByKeySettingAfterFallbackWindowUse = true;
                toggle();
                core.getInputHandler().setForbidWindowEventHandling(false);
                core.charSheetWindow.toggle();
                return true;
            }
        } else if ("inventoryWindow".equals(key)) {
            if (currentPage == 0) {
                noNeedForRefreshPage = true;
                core.inventoryWindow.fallbackWindow = this;
                core.inventoryWindow.canDoActions = false;
                toggle();
                core.getInputHandler().setForbidWindowEventHandling(false);
                core.inventoryWindow.toggle();
                return true;
            }
        } else if ("enter".equals(key)) {
            if (currentPage == 1) {
                currentPage = 0;
                setupPage();
                return true;
            }
        }
        if ("T".equals(key) && currentPage == 0) {
            InputBase base = getSelected();
            if (base instanceof ListSelect) {
                ListSelect select = (ListSelect) base;
                select.setSelected(0);
                inputChanged(base, "");
            }
        } else if ("B".equals(key) && currentPage == 0) {
            InputBase base = getSelected();
            if (base instanceof ListSelect) {
                ListSelect select = (ListSelect) base;
                select.setSelected(select.maxCount - 1);
                inputChanged(base, "");
            }
        } else if ("U".equals(key) && currentPage == 0) {
            InputBase base = getSelected();
            if (skillSelectors.contains(base)) {
                ListSelect skillSelect = (ListSelect) base;
                skillSelect.setSelected(doUseSkillChoiceObject);
                inputChanged(base, "");
            }
            return true;
        } else if ("N".equals(key) && currentPage == 0) {
            InputBase base = getSelected();
            if (skillSelectors.contains(base)) {
                ListSelect skillSelect = (ListSelect) base;
                skillSelect.setSelected(doNothingSkillChoiceObject);
                inputChanged(base, "");
            }
            return true;
        } else if ("123456".indexOf(key) != -1 && currentPage == 0) {
            int toChar = Integer.parseInt(key);
            if (toChar <= numberOfChars) {
                setSelected(skillSelectors.get(toChar - 1));
            }
            return true;
        }
        return false;
    }
}
