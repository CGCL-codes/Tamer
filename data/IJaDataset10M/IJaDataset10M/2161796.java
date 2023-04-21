package org.jcrpg.ui.window.interaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import org.jcrpg.threed.PooledSharedNode;
import org.jcrpg.ui.UIBase;
import org.jcrpg.ui.window.PagedInputWindow;
import org.jcrpg.ui.window.element.TextLabel;
import org.jcrpg.ui.window.element.input.InputBase;
import org.jcrpg.ui.window.element.input.ListSelect;
import org.jcrpg.ui.window.element.input.TextButton;
import org.jcrpg.util.Language;
import org.jcrpg.world.ai.EncounterInfo;
import org.jcrpg.world.ai.EntityMemberInstance;
import org.jcrpg.world.ai.abs.skill.InterceptionSkill;
import org.jcrpg.world.ai.abs.skill.SkillBase;
import org.jcrpg.world.ai.abs.skill.SkillGroups;
import org.jcrpg.world.ai.humanoid.MemberPerson;
import org.jcrpg.world.ai.player.PartyInstance;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.scenegraph.shape.Quad;
import com.ardor3d.util.geom.SharedCopyLogic;

/**
 * Here player can decide what to do to avoid or to make sure an encounter, or can set preparations before the
 * encounter, or set devices to avoid it (traps, hide, magic etc.). Also rules to what Player based encounters
 * to pop up as interception/encounter and what not - friendly/neutral/hostile. 
 * @author pali
 */
public class BehaviorWindow extends PagedInputWindow {

    @Override
    public void hide() {
        super.hide();
        lockLookAndMove(false);
    }

    @Override
    public void show() {
        super.show();
        lockLookAndMove(true);
        getSelected().deactivate();
    }

    Node page0 = new Node();

    ArrayList<ListSelect> skillSelectors = new ArrayList<ListSelect>();

    ArrayList<TextLabel> memberNames = new ArrayList<TextLabel>();

    ListSelect noticeFriendly, noticeNeutral, noticeHostile;

    TextButton save, cancel, revert;

    public static String[] noticeIds = new String[] { "yes", "no" };

    public static String[] noticeTexts = new String[] { Language.v("yes"), Language.v("no") };

    public static Object[] noticeObjects = new Object[] { true, false };

    public BehaviorWindow(UIBase base) {
        super(base);
        try {
            Quad hudQuad = loadImageToQuad("/baseWindowFrame.dds", 0.7f * core.getDisplay().getWidth(), 1.52f * (core.getDisplay().getHeight() / 2), core.getDisplay().getWidth() / 2, 1.16f * core.getDisplay().getHeight() / 2);
            hudQuad.setRenderState(base.hud.hudAS);
            Mesh sQuad = PooledSharedNode.copyMesh("", hudQuad);
            sQuad.setTranslation(hudQuad.getTranslation());
            page0.attachChildAt(sQuad, 0);
            float yDelta = 0.00f;
            new TextLabel("", this, page0, 0.4f, yDelta + 0.11f, 0.0f, 0.06f, 400f, Language.v("behaviorWindow.heading"), false);
            new TextLabel("", this, page0, 0.26f, yDelta + 0.20f, 0.0f, 0.06f, 600f, Language.v("behaviorWindow.members") + ":", false);
            float sizeSelect = 0.05f;
            for (int i = 0; i < 6; i++) {
                ListSelect sel = new ListSelect("member" + i, this, page0, 0.53f, yDelta + 0.24f + sizeSelect * i, 0.3f, 0.04f, 600f, new String[0], new String[0], null, null);
                sel.focusUponMouseEnter = true;
                sel.globalTooltip = "Selecting a skill as the behavior skill will grant it is used to its full level as you walk around. The others are being used at a third of its full level.";
                skillSelectors.add(sel);
                memberNames.add(new TextLabel("name" + i, this, page0, 0.27f, yDelta + 0.24f + sizeSelect * i, 0.0f, 0.04f, 600f, "", false));
                addInput(0, skillSelectors.get(i));
            }
            new TextLabel("friendly_text", this, page0, 0.24f, yDelta + 0.19f + sizeSelect * 8, 0.1f, 0.04f, 600f, Language.v("behaviorWindow.noticeFriendly"), false);
            noticeFriendly = new ListSelect("friendly", this, page0, 0.53f, yDelta + 0.19f + sizeSelect * 8, 0.3f, 0.04f, 600f, noticeIds, noticeTexts, noticeObjects, null, null);
            new TextLabel("neutral_text", this, page0, 0.24f, yDelta + 0.19f + sizeSelect * 9, 0.1f, 0.04f, 600f, Language.v("behaviorWindow.noticeNeutral"), false);
            noticeNeutral = new ListSelect("neutral_text", this, page0, 0.53f, yDelta + 0.19f + sizeSelect * 9, 0.3f, 0.04f, 600f, noticeIds, noticeTexts, noticeObjects, null, null);
            new TextLabel("hostile_text", this, page0, 0.24f, yDelta + 0.19f + sizeSelect * 10, 0.1f, 0.04f, 600f, Language.v("behaviorWindow.noticeHostile"), false);
            noticeHostile = new ListSelect("hostile", this, page0, 0.53f, yDelta + 0.19f + sizeSelect * 10, 0.3f, 0.04f, 600f, noticeIds, noticeTexts, noticeObjects, null, null);
            addInput(0, noticeFriendly);
            addInput(0, noticeNeutral);
            addInput(0, noticeHostile);
            save = new TextButton("save", this, page0, 0.3f, yDelta + 0.75f, 0.18f, 0.06f, 500f, Language.v("behaviorWindow.save"), "S");
            revert = new TextButton("revert", this, page0, 0.51f, yDelta + 0.75f, 0.18f, 0.06f, 500f, Language.v("behaviorWindow.revert"), "R");
            cancel = new TextButton("cancel", this, page0, 0.72f, yDelta + 0.75f, 0.18f, 0.06f, 500f, Language.v("behaviorWindow.cancel"), "C");
            addInput(0, save);
            addInput(0, revert);
            addInput(0, cancel);
            addPage(0, page0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateToParty() {
        int counter = 0;
        if (party != null) for (EntityMemberInstance i : party.orderedParty) {
            if (i != null) {
                ListSelect select = skillSelectors.get(counter);
                select.reattach();
                select.subject = i;
                Collection<Class<? extends SkillBase>> skills = i.description.getCommonSkills().getSkillsOfType(InterceptionSkill.class);
                if (skills == null) skills = new HashSet<Class<? extends SkillBase>>();
                String[] texts = new String[skills.size()];
                String[] tooltips = new String[skills.size()];
                Object[] objects = new Object[skills.size()];
                String[] ids = new String[skills.size()];
                int counter_2 = 0;
                int selected = 0;
                for (Class<? extends SkillBase> skill : skills) {
                    String text = Language.v("skills." + skill.getSimpleName()) + " (" + i.description.getCommonSkills().getSkillLevel(skill, null) + ")";
                    tooltips[counter_2] = Language.v("skills.tooltip." + skill.getSimpleName());
                    texts[counter_2] = text;
                    ids[counter_2] = "" + counter_2;
                    SkillBase b = (SkillBase) SkillGroups.skillBaseInstances.get(skill);
                    objects[counter_2] = b;
                    if (i.behaviorSkill != null && i.behaviorSkill.getClass() == b.getClass()) {
                        selected = counter_2;
                    }
                    counter_2++;
                }
                select.ids = ids;
                select.texts = texts;
                select.objects = objects;
                select.tooltips = tooltips;
                select.setUpdated(true);
                select.deactivate();
                select.setSelected(selected);
                memberNames.get(counter).text = ((MemberPerson) i.description).foreName;
                memberNames.get(counter).activate();
            }
            counter++;
        }
        noticeFriendly.setSelected(party.noticeFriendly);
        noticeNeutral.setSelected(party.noticeNeutral);
        noticeHostile.setSelected(party.noticeHostile);
        for (int i = counter; i < 6; i++) {
            ListSelect select = skillSelectors.get(i);
            select.detach();
        }
    }

    public PartyInstance party;

    public Collection<EncounterInfo> possibleEncounters;

    @Override
    public void setupPage() {
        super.setupPage();
    }

    @Override
    public boolean handleKey(String key) {
        if (super.handleKey(key)) return true;
        return false;
    }

    @Override
    public boolean inputChanged(InputBase base, String message) {
        super.inputChanged(base, message);
        return false;
    }

    @Override
    public boolean inputEntered(InputBase base, String message) {
        return false;
    }

    @Override
    public boolean inputLeft(InputBase base, String message) {
        return false;
    }

    @Override
    public boolean inputUsed(InputBase base, String message) {
        if (base == revert) {
            updateToParty();
            setupPage();
            return true;
        } else if (base == save) {
            for (ListSelect s : skillSelectors) {
                EntityMemberInstance i = (EntityMemberInstance) s.subject;
                if (s.isEnabled() && i != null) {
                    i.behaviorSkill = (InterceptionSkill) s.getSelectedObject();
                }
            }
            party.noticeFriendly = (Boolean) noticeFriendly.getSelectedObject();
            party.noticeNeutral = (Boolean) noticeNeutral.getSelectedObject();
            party.noticeHostile = (Boolean) noticeHostile.getSelectedObject();
            toggle();
            core.uiBase.hud.characters.update();
            return true;
        }
        if (base == cancel) {
            updateToParty();
            setupPage();
            toggle();
            return true;
        }
        if (message.equals("enter")) return true;
        return false;
    }
}
