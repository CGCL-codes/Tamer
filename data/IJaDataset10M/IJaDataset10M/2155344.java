package com.g2d.studio.scene.units;

import java.awt.Component;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JList;
import com.cell.gameedit.SetResource;
import com.cell.gameedit.object.SpriteSet;
import com.cell.gfx.game.CCD;
import com.cell.gfx.game.CSprite;
import com.cell.rpg.instance.zones.ability.InstanceZoneUnitKillAction;
import com.cell.rpg.instance.zones.ability.InstanceZoneUnitVisible;
import com.cell.rpg.quest.ability.QuestAccepter;
import com.cell.rpg.quest.ability.QuestPublisher;
import com.cell.rpg.scene.Actor;
import com.cell.rpg.scene.SceneUnit;
import com.cell.rpg.scene.ability.ActorAuctioneer;
import com.cell.rpg.scene.ability.ActorBank;
import com.cell.rpg.scene.ability.ActorDropItem;
import com.cell.rpg.scene.ability.ActorJobTrainer;
import com.cell.rpg.scene.ability.ActorPathStart;
import com.cell.rpg.scene.ability.ActorPostOffice;
import com.cell.rpg.scene.ability.ActorSellItem;
import com.cell.rpg.scene.ability.ActorSkillTrainer;
import com.cell.rpg.scene.ability.ActorTalk;
import com.cell.rpg.scene.ability.ActorTransport;
import com.cell.rpg.scene.script.trigger.Event;
import com.g2d.annotation.Property;
import com.g2d.awt.util.Tools;
import com.g2d.cell.CellSetResource;
import com.g2d.cell.game.SceneSprite;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.TextTip;
import com.g2d.display.Tip;
import com.g2d.display.ui.Menu;
import com.g2d.editor.DisplayObjectEditor;
import com.g2d.game.rpg.Unit;
import com.g2d.geom.Rectangle;
import com.g2d.geom.Shape;
import com.g2d.studio.Studio;
import com.g2d.studio.Version;
import com.g2d.studio.gameedit.ObjectAdapters;
import com.g2d.studio.gameedit.template.XLSUnit;
import com.g2d.studio.quest.QuestCellEditAdapter;
import com.g2d.studio.res.Res;
import com.g2d.studio.scene.editor.SceneAbilityAdapters;
import com.g2d.studio.scene.editor.SceneEditor;
import com.g2d.studio.scene.editor.SceneUnitMenu;
import com.g2d.studio.scene.editor.SceneUnitTagEditor;
import com.g2d.studio.scene.effect.AbilityEffectInfos;
import com.g2d.util.Drawing;
import com.g2d.BufferedImage;
import com.g2d.Color;
import com.g2d.Graphics2D;
import com.g2d.Stroke;

/**
 * @author WAZA
 *
 */
@Property("一个游戏角色")
public class SceneActor extends SceneSprite implements SceneUnitTag<Actor> {

    private static final long serialVersionUID = Version.VersionGS;

    public final SceneEditor editor;

    final Actor actor;

    public final XLSUnit xls_unit;

    TextTip tip = new TextTip();

    Rectangle snap_shape = new Rectangle(-2, -2, 4, 4);

    AbilityEffectInfos<Actor> effects = new AbilityEffectInfos<Actor>(new Class<?>[] { QuestPublisher.class, QuestAccepter.class, ActorTalk.class, ActorBank.class, ActorAuctioneer.class, ActorPostOffice.class, ActorSkillTrainer.class, ActorJobTrainer.class, ActorSellItem.class, ActorTransport.class, ActorDropItem.class, ActorPathStart.class }, new BufferedImage[] { Tools.wrap_g2d(Res.img_quest_info), Tools.wrap_g2d(Res.img_quest_info2), Tools.wrap_g2d(Res.img_talk), Tools.wrap_g2d(Res.img_npc_bank), Tools.wrap_g2d(Res.img_sell_item), Tools.wrap_g2d(Res.img_mail), Tools.wrap_g2d(Res.img_skill_trainer), Tools.wrap_g2d(Res.img_job_trainer), Tools.wrap_g2d(Res.img_sell_item), Tools.wrap_g2d(Res.img_transport), Tools.wrap_g2d(Res.img_item_info), Tools.wrap_g2d(Res.icon_camera) });

    /**
	 * 新建的单位
	 * @param editor
	 * @param xls_unit
	 * @param x
	 * @param y
	 */
    public SceneActor(SceneEditor editor, XLSUnit xls_unit, int x, int y, int anim) {
        this.editor = editor;
        this.xls_unit = xls_unit;
        this.cur_anim = anim;
        this.setLocation(x, y);
        this.init(xls_unit.getCPJSprite().getCPJFile().getSetResource(), xls_unit.getCPJSprite().name);
        if (!editor.getGameScene().getWorld().addChild(this)) {
            throw new IllegalStateException();
        }
        this.actor = new Actor(getID() + "", xls_unit.getID());
    }

    /**
	 * 读入的单位
	 * @param editor
	 * @param actor
	 */
    public SceneActor(SceneEditor editor, Actor actor) {
        this.editor = editor;
        this.actor = actor;
        {
            this.xls_unit = Studio.getInstance().getObjectManager().getObject(XLSUnit.class, actor.template_unit_id);
            this.cur_anim = actor.animate;
            this.setID(editor.getGameScene().getWorld(), actor.id);
            this.setLocation(actor.x, actor.y);
            this.init(xls_unit.getCPJSprite().getCPJFile().getSetResource(), xls_unit.getCPJSprite().name);
        }
        if (!editor.getGameScene().getWorld().addChild(this)) {
            throw new IllegalStateException();
        }
    }

    public Actor onWrite() {
        actor.name = getID() + "";
        actor.animate = cur_anim;
        actor.x = getX();
        actor.y = getY();
        actor.z = priority;
        return actor;
    }

    @Override
    public void added(DisplayObjectContainer parent) {
        this.enable = true;
        this.enable_focus = true;
        this.enable_drag = true;
        this.enable_input = true;
        super.added(parent);
        actor.name = getID() + "";
    }

    @Override
    public void loaded(SetResource set, CSprite cspr, SpriteSet spr) {
        super.loaded(set, cspr, spr);
        this.getSprite().setCurrentAnimate(cur_anim);
    }

    protected boolean enable_click_focus() {
        return true;
    }

    @Override
    public void onReadComplete(Vector<SceneUnitTag<?>> all) {
    }

    @Override
    public void onWriteReady(Vector<SceneUnitTag<?>> all) {
    }

    @Override
    public Actor getUnit() {
        return actor;
    }

    @Override
    public Unit getGameUnit() {
        return this;
    }

    @Override
    public Color getSnapColor() {
        return Color.GREEN;
    }

    @Override
    public Shape getSnapShape() {
        return snap_shape;
    }

    public void update() {
        super.update();
        if (csprite != null) {
            csprite.nextCycFrame();
            CCD bounds = csprite.getFrameBounds(csprite.getCurrentAnimate());
            this.local_bounds.setBounds(bounds.X1, bounds.Y1, bounds.getWidth(), bounds.getHeight());
        }
        if (editor.isToolSelect()) {
            if (editor.getSelectedUnit() == this) {
                if (getRoot().isMouseWheelUP()) {
                    getSprite().nextAnimate(-1);
                }
                if (getRoot().isMouseWheelDown()) {
                    getSprite().nextAnimate(1);
                }
                cur_anim = getSprite().getCurrentAnimate();
            }
        }
        effects.updateActor(this);
    }

    BufferedImage img_script = Tools.wrap_g2d(Res.img_script);

    @Override
    protected void renderAfter(Graphics2D g) {
        super.renderAfter(g);
        if (editor != null) {
            if (getUnit().getBindedTriggers().getTriggerCount() > 0) {
                g.drawImage(img_script, -img_script.getWidth() / 2, local_bounds.y);
            }
            g.pushStroke();
            try {
                g.setStroke(new com.g2d.BasicStroke(2));
                if (editor.getSelectedPage().isSelectedType(getClass())) {
                    if (editor.getSelectedUnit() == this) {
                        drawTouchRange(g);
                        drawLookRange(g);
                        g.setColor(Color.WHITE);
                        g.draw(local_bounds);
                        g.setColor(Color.WHITE);
                        Drawing.drawStringBorder(g, getSprite().getCurrentAnimate() + "/" + getSprite().getAnimateCount(), 0, getSprite().getVisibleBotton() + 1, Drawing.TEXT_ANCHOR_HCENTER | Drawing.TEXT_ANCHOR_TOP);
                    } else if (isPickedMouse()) {
                        drawTouchRange(g);
                        if (editor.isToolSelect()) {
                            g.setColor(Color.GREEN);
                            g.draw(local_bounds);
                        }
                    }
                    this.enable = editor.isToolSelect();
                } else {
                    this.enable = false;
                }
            } finally {
                g.popStroke();
            }
            Util.drawScript(g, editor, this);
        }
    }

    protected void drawTouchRange(Graphics2D g) {
        if (actor != null) {
            g.setColor(Color.GREEN);
            g.drawArc(-actor.touch_range, -actor.touch_range, actor.touch_range << 1, actor.touch_range << 1, 0, 360);
        }
    }

    protected void drawLookRange(Graphics2D g) {
        if (actor != null) {
            g.setColor(Color.YELLOW);
            g.drawArc(-actor.look_range, -actor.look_range, actor.look_range << 1, actor.look_range << 1, 0, 360);
            g.setColor(new Color((int) (Color.YELLOW.getARGB() & 0x7fffffff)));
            g.fillArc(-actor.look_range, -actor.look_range, actor.look_range << 1, actor.look_range << 1, 0, 360);
        }
    }

    @Override
    public String toString() {
        return getID() + "";
    }

    @Override
    public Tip getTip() {
        return Util.getTip(editor, this, tip);
    }

    @Override
    public DisplayObjectEditor<?> getEditorForm() {
        return new SceneUnitTagEditor(editor, this, new SceneAbilityAdapters.ActorPathStartAdapter(editor), new SceneAbilityAdapters.ActorTransportAdapter(editor), new SceneAbilityAdapters.ActorSkillTrainerAdapter(), new SceneAbilityAdapters.ActorTalkAdapter(), new ObjectAdapters.ItemListIDSelectAdapter(), new ObjectAdapters.ShopItemListIDSelectAdapter(), new QuestCellEditAdapter.QuestTriggerAdapter());
    }

    @Override
    public Menu getEditMenu() {
        return new SceneUnitMenu(editor, this);
    }

    @Override
    public Component getListComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return null;
    }

    @Override
    public ImageIcon getListIcon(boolean update) {
        return null;
    }

    @Override
    public String getListName() {
        return getID() + "";
    }

    @Override
    public void onHideFrom() {
    }
}
