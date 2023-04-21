package net.sf.gridarta.gui.dialog.gameobjectattributes;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import net.sf.gridarta.gui.treasurelist.CFTreasureListTree;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.archetypetype.ArchetypeTypeSet;
import net.sf.gridarta.model.face.FaceObjectProviders;
import net.sf.gridarta.model.face.FaceObjects;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmanager.MapManager;
import net.sf.gridarta.model.settings.GlobalSettings;
import net.sf.gridarta.model.spells.GameObjectSpell;
import net.sf.gridarta.model.spells.NumberSpell;
import net.sf.gridarta.model.spells.Spells;
import net.sf.gridarta.model.treasurelist.TreasureTree;
import net.sf.gridarta.textedit.textarea.TextAreaDefaults;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;

public class GameObjectAttributesDialogFactory<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> {

    /**
     * Action Builder.
     */
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The gameObject objects that already are shown, to avoid opening a dialog
     * twice.
     */
    private final Map<G, JDialog> dialogs = new HashMap<G, JDialog>();

    /**
     * The list of CF type-data.
     */
    @NotNull
    private final ArchetypeTypeSet archetypeTypeSet;

    /**
     * The parent frame for showing dialog boxes.
     */
    @NotNull
    private final JFrame parent;

    /**
     * The {@link CFTreasureListTree} to display.
     */
    @NotNull
    private final CFTreasureListTree treasureListTree;

    /**
     * The {@link FaceObjectProviders} for looking up faces.
     */
    @NotNull
    private final FaceObjectProviders faceObjectProviders;

    /**
     * The {@link AnimationObjects} instance for choosing animation names.
     */
    @NotNull
    private final AnimationObjects animationObjects;

    /**
     * The {@link GlobalSettings} instance.
     */
    @NotNull
    private final GlobalSettings globalSettings;

    /**
     * The {@link FileFilter} to use for map files.
     */
    @NotNull
    private final FileFilter mapFileFilter;

    /**
     * The {@link FileFilter} to use for script files.
     */
    @NotNull
    private final FileFilter scriptFileFilter;

    /**
     * The {@link FaceObjects} instance for choosing face names.
     */
    @NotNull
    private final FaceObjects faceObjects;

    /**
     * The game object spells to use.
     */
    @NotNull
    private final Spells<GameObjectSpell<G, A, R>> gameObjectSpells;

    /**
     * The numbered spells.
     */
    @NotNull
    private final Spells<NumberSpell> numberSpells;

    /**
     * The index for "no spell".
     */
    private final int undefinedSpellIndex;

    /**
     * The {@link TreasureTree} to use.
     */
    @NotNull
    private final TreasureTree treasureTree;

    /**
     * The {@link ImageIcon} for no animations.
     */
    @NotNull
    private final ImageIcon noFaceSquareIcon;

    /**
     * The {@link ImageIcon} for undefined animations.
     */
    @NotNull
    private final ImageIcon unknownSquareIcon;

    /**
     * The {@link TextAreaDefaults} for text fields.
     */
    @NotNull
    private TextAreaDefaults textAreaDefaults;

    /**
     * The {@link MapManager} instance.
     */
    @NotNull
    private final MapManager<G, A, R> mapManager;

    /**
     * Creates a new instance.
     * @param archetypeTypeSet the list of CF type-data
     * @param parent the parent frame for dialog boxes
     * @param treasureListTree the treasure list tree to display
     * @param faceObjectProviders the face object providers for looking up
     * faces
     * @param animationObjects the animation objects instance for choosing
     * animation names
     * @param globalSettings the global settings to use
     * @param mapFileFilter the file filter to use for map files
     * @param scriptFileFilter the file filter to use for script files
     * @param faceObjects the face objects instance for choosing face names
     * @param gameObjectSpells the game object spells to use
     * @param numberSpells the numbered spells to use
     * @param undefinedSpellIndex the index for "no spell"
     * @param treasureTree the treasure tree to use
     * @param noFaceSquareIcon the image icon for no animations
     * @param unknownSquareIcon the image icon for undefined animations
     * @param mapManager the map manager instance
     */
    public GameObjectAttributesDialogFactory(@NotNull final ArchetypeTypeSet archetypeTypeSet, @NotNull final JFrame parent, @NotNull final CFTreasureListTree treasureListTree, @NotNull final FaceObjectProviders faceObjectProviders, @NotNull final AnimationObjects animationObjects, @NotNull final GlobalSettings globalSettings, @NotNull final FileFilter mapFileFilter, @NotNull final FileFilter scriptFileFilter, @NotNull final FaceObjects faceObjects, @NotNull final Spells<GameObjectSpell<G, A, R>> gameObjectSpells, @NotNull final Spells<NumberSpell> numberSpells, final int undefinedSpellIndex, @NotNull final TreasureTree treasureTree, @NotNull final ImageIcon noFaceSquareIcon, @NotNull final ImageIcon unknownSquareIcon, @NotNull final MapManager<G, A, R> mapManager) {
        this.archetypeTypeSet = archetypeTypeSet;
        this.parent = parent;
        this.treasureListTree = treasureListTree;
        this.faceObjectProviders = faceObjectProviders;
        this.animationObjects = animationObjects;
        this.globalSettings = globalSettings;
        this.mapFileFilter = mapFileFilter;
        this.scriptFileFilter = scriptFileFilter;
        this.faceObjects = faceObjects;
        this.gameObjectSpells = gameObjectSpells;
        this.numberSpells = numberSpells;
        this.undefinedSpellIndex = undefinedSpellIndex;
        this.treasureTree = treasureTree;
        this.unknownSquareIcon = unknownSquareIcon;
        this.noFaceSquareIcon = noFaceSquareIcon;
        this.mapManager = mapManager;
    }

    @Deprecated
    public void setTextAreaDefaults(@NotNull final TextAreaDefaults textAreaDefaults) {
        this.textAreaDefaults = textAreaDefaults;
    }

    /**
     * Shows the game object attributes dialog for a given {@link GameObject}
     * instance.
     * @param gameObject the GameObject to be displayed by this dialog
     */
    public void showAttributeDialog(@NotNull final G gameObject) {
        final G head = gameObject.getHead();
        if (head.hasUndefinedArchetype()) {
            ACTION_BUILDER.showMessageDialog(parent, "openAttrDialogNoDefaultArch");
            return;
        }
        synchronized (dialogs) {
            if (dialogs.containsKey(head)) {
                dialogs.get(head).toFront();
            } else {
                final GameObjectAttributesDialog<G, A, R> pane = new GameObjectAttributesDialog<G, A, R>(this, archetypeTypeSet, head, parent, treasureListTree, faceObjectProviders, animationObjects, globalSettings, mapFileFilter, scriptFileFilter, faceObjects, gameObjectSpells, numberSpells, undefinedSpellIndex, treasureTree, noFaceSquareIcon, unknownSquareIcon, textAreaDefaults, mapManager);
                final JDialog dialog = pane.createDialog();
                dialogs.put(head, dialog);
            }
        }
    }

    /**
     * Hides the game object attributes dialog for a given {@link GameObject}
     * instance.
     * @param gameObject the GameObject to be displayed by this dialog
     */
    public void hideAttributeDialog(@NotNull final G gameObject) {
        final Window dialog = dialogs.remove(gameObject);
        if (dialog != null) {
            dialog.dispose();
        }
    }
}
