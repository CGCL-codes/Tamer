package org.nakedobjects.plugins.dndviewer.viewer.basic;

import java.awt.event.KeyEvent;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dndviewer.Canvas;
import org.nakedobjects.plugins.dndviewer.Click;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.FocusManager;
import org.nakedobjects.plugins.dndviewer.KeyboardAction;
import org.nakedobjects.plugins.dndviewer.NullContent;
import org.nakedobjects.plugins.dndviewer.Toolkit;
import org.nakedobjects.plugins.dndviewer.UserAction;
import org.nakedobjects.plugins.dndviewer.UserActionSet;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.ViewAxis;
import org.nakedobjects.plugins.dndviewer.ViewRequirement;
import org.nakedobjects.plugins.dndviewer.ViewSpecification;
import org.nakedobjects.plugins.dndviewer.Workspace;
import org.nakedobjects.plugins.dndviewer.viewer.content.AbstractContent;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Color;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Image;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Location;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Padding;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Shape;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Size;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Text;
import org.nakedobjects.plugins.dndviewer.viewer.focus.SubviewFocusManager;
import org.nakedobjects.plugins.dndviewer.viewer.view.simple.AbstractView;

public class PopupMenu extends AbstractView {

    private static class Item {

        public static Item createDivider() {
            final Item item = new Item();
            item.isBlank = true;
            return item;
        }

        public static Item createNoOption() {
            final Item item = new Item();
            item.name = "no options";
            return item;
        }

        public static Item createOption(final UserAction action, final Object object, final View view, final Location location) {
            final Item item = new Item();
            if (action == null) {
                item.isBlank = true;
            } else {
                item.isBlank = false;
                item.action = action;
                item.view = view;
                item.name = action.getName(view);
                item.description = action.getDescription(view);
                final Consent consent = action.disabled(view);
                item.isDisabled = consent.isVetoed();
                item.reasonDisabled = consent.getReason();
            }
            return item;
        }

        UserAction action;

        String description;

        boolean isBlank;

        boolean isDisabled;

        String name;

        String reasonDisabled;

        View view;

        private Item() {
        }

        public String getHelp() {
            return action.getHelp(view);
        }

        @Override
        public String toString() {
            return isBlank ? "NONE" : (name + " " + (isDisabled ? "DISABLED " : " " + action));
        }
    }

    private class PopupContent extends AbstractContent {

        public PopupContent() {
        }

        public Consent canDrop(final Content sourceContent) {
            return Veto.DEFAULT;
        }

        public void debugDetails(final DebugString debug) {
        }

        public NakedObject drop(final Content sourceContent) {
            return null;
        }

        public String getDescription() {
            final int optionNo = getOption();
            return items[optionNo].description;
        }

        public String getHelp() {
            final int optionNo = getOption();
            return items[optionNo].getHelp();
        }

        public String getIconName() {
            return null;
        }

        public Image getIconPicture(final int iconHeight) {
            return null;
        }

        public String getId() {
            return null;
        }

        public NakedObject getNaked() {
            return null;
        }

        public boolean isOptionEnabled() {
            return false;
        }

        public NakedObjectSpecification getSpecification() {
            return null;
        }

        public boolean isTransient() {
            return false;
        }

        public void parseTextEntry(final String entryText) {
        }

        public String title() {
            final int optionNo = getOption();
            return items[optionNo].name;
        }

        public NakedObject[] getOptions() {
            return null;
        }
    }

    private static class PopupSpecification implements ViewSpecification {

        public boolean canDisplay(final Content content, ViewRequirement requirement) {
            return false;
        }

        public View createView(final Content content, final ViewAxis axis) {
            return null;
        }

        public String getName() {
            return "Popup Menu";
        }

        public boolean isAligned() {
            return false;
        }

        public boolean isOpen() {
            return true;
        }

        public boolean isReplaceable() {
            return false;
        }

        public boolean isSubView() {
            return false;
        }
    }

    private static final Logger LOG = Logger.getLogger(PopupMenu.class);

    private Color backgroundColor;

    private View forView;

    private Item[] items = new Item[0];

    private int optionIdentified;

    private final FocusManager simpleFocusManager;

    public PopupMenu(PopupMenuContainer parent) {
        super(new NullContent(), new PopupSpecification(), null);
        setContent(new PopupContent());
        setParent(parent);
        simpleFocusManager = new SubviewFocusManager(this);
    }

    private void addItems(final View target, final UserAction[] options, final int len, final Vector list, final NakedObjectActionType type) {
        final int initialSize = list.size();
        for (int i = 0; i < len; i++) {
            if (options[i].getType() == type) {
                if (initialSize > 0 && list.size() == initialSize) {
                    list.addElement(Item.createDivider());
                }
                list.addElement(Item.createOption(options[i], null, target, getLocation()));
            }
        }
    }

    protected Color backgroundColor() {
        return backgroundColor;
    }

    @Override
    public Consent canChangeValue() {
        return Veto.DEFAULT;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    protected Color disabledColor() {
        return Toolkit.getColor(ColorsAndFonts.COLOR_MENU_DISABLED);
    }

    /**
     * Draws the popup menu
     * 
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    @Override
    public void draw(final Canvas canvas) {
        Size coreSize = getSize();
        final int width = coreSize.getWidth();
        final int height = coreSize.getHeight();
        canvas.drawSolidRectangle(0, 0, width, height, backgroundColor);
        canvas.draw3DRectangle(0, 0, width, height, backgroundColor, true);
        final int itemHeight = style().getLineHeight() + VPADDING;
        int baseLine = style().getAscent() + getPadding().getTop() + 1;
        final int left = getPadding().getLeft();
        for (int i = 0; i < items.length; i++) {
            if (items[i].isBlank) {
                final int y = baseLine - (style().getAscent() / 2);
                canvas.drawLine(1, y, width - 2, y, backgroundColor.brighter());
                canvas.drawLine(1, y - 1, width - 2, y - 1, backgroundColor.darker());
            } else {
                Color color;
                if (items[i].isDisabled || items[i].action == null) {
                    color = disabledColor();
                } else if (getOption() == i) {
                    final int top = getPadding().getTop() + i * itemHeight;
                    final int depth = style().getLineHeight() + 2;
                    canvas.drawSolidRectangle(2, top, width - 4, depth, backgroundColor.darker());
                    canvas.draw3DRectangle(2, top, width - 4, depth + 1, backgroundColor.brighter(), false);
                    color = reversedColor();
                } else {
                    color = normalColor();
                }
                canvas.drawText(items[i].name, left, baseLine, color, style());
                if (items[i].action instanceof UserActionSet) {
                    Shape arrow;
                    arrow = new Shape(0, 0);
                    arrow.extendsLine(4, 4);
                    arrow.extendsLine(-4, 4);
                    canvas.drawSolidShape(arrow, width - 10, baseLine - 8, color);
                }
            }
            baseLine += itemHeight;
        }
    }

    @Override
    public void firstClick(final Click click) {
        if (click.button1() || click.button3()) {
            mouseMoved(click.getLocation());
            invoke();
        }
    }

    @Override
    public void focusLost() {
    }

    @Override
    public void focusReceived() {
    }

    @Override
    public FocusManager getFocusManager() {
        return simpleFocusManager;
    }

    @Override
    public Size getMaximumSize() {
        final Size size = new Size();
        for (int i = 0; i < items.length; i++) {
            final int itemWidth = items[i].isBlank ? 0 : style().stringWidth(items[i].name);
            size.ensureWidth(itemWidth);
            size.extendHeight(style().getLineHeight() + VPADDING);
        }
        size.extend(getPadding());
        size.extendWidth(HPADDING * 2);
        return size;
    }

    public int getOption() {
        return optionIdentified;
    }

    public int getOptionPostion() {
        final int itemHeight = style().getLineHeight() + VPADDING;
        return itemHeight * getOption();
    }

    public int getOptionCount() {
        return items.length;
    }

    @Override
    public Padding getPadding() {
        final Padding in = super.getPadding();
        in.extendTop(VPADDING);
        in.extendBottom(VPADDING);
        in.extendLeft(HPADDING + 5);
        in.extendRight(HPADDING + 5);
        return in;
    }

    @Override
    public Workspace getWorkspace() {
        return forView.getWorkspace();
    }

    @Override
    public boolean hasFocus() {
        return false;
    }

    private void invoke() {
        final int option = getOption();
        final Item item = items[option];
        if (item.isBlank || item.action == null || item.action.disabled(forView).isVetoed()) {
            return;
        } else if (item.action instanceof UserActionSet) {
            UserAction[] menuOptions = ((UserActionSet) item.action).getMenuOptions();
            ((PopupMenuContainer) getParent()).openSubmenu(menuOptions);
        } else {
            final Workspace workspace = getWorkspace();
            final Location location = new Location(getAbsoluteLocation());
            location.subtract(workspace.getView().getAbsoluteLocation());
            final Padding padding = workspace.getView().getPadding();
            location.move(-padding.getLeft(), -padding.getTop());
            location.move(30, 0);
            getParent().dispose();
            LOG.debug("execute " + item.name + " on " + forView + " in " + workspace);
            item.action.execute(workspace, forView, location);
        }
    }

    @Override
    public void keyPressed(final KeyboardAction key) {
        final int keyCode = key.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            if (getParent() == null) {
                dispose();
            }
            key.consume();
        } else if (keyCode == KeyEvent.VK_ENTER) {
            key.consume();
            invoke();
        } else if (keyCode == KeyEvent.VK_RIGHT && items[getOption()].action instanceof UserActionSet) {
            key.consume();
            invoke();
        } else if (keyCode == KeyEvent.VK_UP) {
            key.consume();
            if (optionIdentified == 0) {
                optionIdentified = items.length;
            }
            for (int i = optionIdentified - 1; i >= 0; i--) {
                if (items[i].isBlank) {
                    continue;
                }
                if (items[i].isDisabled) {
                    continue;
                }
                setOption(i);
                break;
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            key.consume();
            if (optionIdentified == items.length - 1) {
                optionIdentified = -1;
            }
            for (int i = optionIdentified + 1; i < items.length; i++) {
                if (items[i].isBlank) {
                    continue;
                }
                if (items[i].isDisabled) {
                    continue;
                }
                setOption(i);
                break;
            }
        }
    }

    @Override
    public void keyReleased(final int keyCode, final int modifiers) {
    }

    @Override
    public void keyTyped(final char keyCode) {
    }

    public View makeView(final NakedObject object, final NakedObjectAssociation field) throws CloneNotSupportedException {
        throw new RuntimeException();
    }

    @Override
    public void markDamaged() {
        if (getParent() == null) {
            super.markDamaged();
        } else {
            getParent().markDamaged();
        }
    }

    @Override
    public void mouseMoved(final Location at) {
        int option = (at.getY() - getPadding().getTop()) / (style().getLineHeight() + VPADDING);
        option = Math.max(option, 0);
        option = Math.min(option, items.length - 1);
        if (option >= 0 && optionIdentified != option) {
            setOption(option);
            markDamaged();
        }
    }

    protected Color normalColor() {
        return Toolkit.getColor(ColorsAndFonts.COLOR_MENU);
    }

    protected Color reversedColor() {
        return Toolkit.getColor(ColorsAndFonts.COLOR_MENU_REVERSED);
    }

    public void setOption(final int option) {
        if (option != optionIdentified) {
            optionIdentified = option;
            markDamaged();
            updateFeedback();
        }
    }

    private void updateFeedback() {
        final Item item = items[optionIdentified];
        if (item.isBlank) {
            getFeedbackManager().clearAction();
        } else if (isEmpty(item.reasonDisabled)) {
            getFeedbackManager().setAction(item.description == null ? "" : item.description);
        } else {
            getFeedbackManager().setAction(item.reasonDisabled);
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    void show(final View target, final UserAction[] options, final Color color) {
        this.forView = target;
        optionIdentified = 0;
        backgroundColor = color;
        final int len = options.length;
        if (len == 0) {
            items = new Item[] { Item.createNoOption() };
        } else {
            final Vector list = new Vector();
            addItems(target, options, len, list, UserAction.USER);
            addItems(target, options, len, list, UserAction.EXPLORATION);
            addItems(target, options, len, list, UserAction.DEBUG);
            items = new Item[list.size()];
            list.copyInto(items);
        }
        updateFeedback();
    }

    protected Text style() {
        return Toolkit.getText(ColorsAndFonts.TEXT_MENU);
    }

    @Override
    public String toString() {
        return "PopupMenu [location=" + getLocation() + ",item=" + optionIdentified + ",itemCount=" + (items == null ? 0 : items.length) + "]";
    }

    protected boolean transparentBackground() {
        return false;
    }
}
