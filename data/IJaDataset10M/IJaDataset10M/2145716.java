package de.enough.polish.ui;

import java.io.IOException;
import de.enough.polish.ui.Canvas;
import de.enough.polish.ui.StyleSheet;
import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.android.lcdui.Image;
import de.enough.polish.event.EventManager;
import de.enough.polish.ui.backgrounds.TranslucentSimpleBackground;
import de.enough.polish.util.ArrayList;
import de.enough.polish.util.DeviceControl;
import de.enough.polish.util.HashMap;
import de.enough.polish.util.Locale;
import de.enough.polish.android.midlet.MIDlet;

/**
 * <p>Provides a more powerful alternative to the build-in menu bar of the Screen-class.</p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2009</p>
 * <pre>
 * history
 *        24-Jan-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class MenuBar extends Item {

    private static final int MENU_KEY = -111;

    private Command hideCommand;

    private Command positiveCommand;

    protected final ArrayList commandsList;

    protected final Container commandsContainer;

    protected boolean isOpened;

    protected Command singleLeftCommand;

    protected final CommandItem singleLeftCommandItem;

    protected Command singleRightCommand;

    protected final CommandItem singleRightCommandItem;

    protected Command singleMiddleCommand;

    private int topY;

    protected boolean isSoftKeyPressed;

    protected boolean canScrollDownwards;

    protected boolean canScrollUpwards;

    protected boolean paintScrollIndicator;

    protected Image optionsImage;

    protected boolean showImageAndText;

    protected Image selectImage;

    protected Image cancelImage;

    protected Background overlayBackground;

    protected final HashMap<Command, CommandItem> allCommands;

    protected boolean isOrientationVertical;

    protected Style menuItemStyle;

    private ArrayList commandsBeforeLayer;

    /**
	 * Creates a new menu bar
	 * 
	 * @param screen the parent screen
	 */
    public MenuBar(Screen screen) {
        this(screen, null);
    }

    /**
	 * Creates a new menu bar
	 * 
	 * @param screen the parent screen
	 * @param style the style of this menu-bar
	 */
    public MenuBar(Screen screen, Style style) {
        super(style);
        this.screen = screen;
        this.commandsList = new ArrayList();
        this.allCommands = new HashMap<Command, CommandItem>();
        this.commandsContainer = new Container(true, de.enough.polish.ui.StyleSheet.defaultStyle);
        this.commandsContainer.parent = this;
        this.commandsContainer.screen = screen;
        if (this.commandsContainer.style != null) {
            this.commandsContainer.setStyle(this.commandsContainer.style);
        }
        this.commandsContainer.layout |= LAYOUT_SHRINK;
        Command dummy = new Command("", Command.ITEM, 10000);
        this.singleRightCommandItem = new CommandItem(dummy, this, de.enough.polish.ui.StyleSheet.defaultStyle);
        this.singleRightCommandItem.setImageAlign(Graphics.LEFT);
        this.singleLeftCommandItem = new CommandItem(dummy, this, de.enough.polish.ui.StyleSheet.defaultStyle);
        this.singleLeftCommandItem.setImageAlign(Graphics.LEFT);
    }

    public void addCommand(Command cmd) {
        if (this.menuItemStyle != null) {
            addCommand(cmd, this.menuItemStyle);
        } else {
            addCommand(cmd, de.enough.polish.ui.StyleSheet.defaultStyle);
        }
    }

    public void addCommand(Command cmd, Style commandStyle) {
        if (cmd == this.singleLeftCommand || cmd == this.singleRightCommand || cmd == this.singleMiddleCommand || this.commandsList.contains(cmd)) {
            return;
        }
        CommandItem item = new CommandItem(cmd, this, commandStyle);
        this.allCommands.put(cmd, item);
        int type = cmd.getCommandType();
        int priority = cmd.getPriority();
        if ((cmd != this.hideCommand) && (type == Command.BACK || type == Command.CANCEL || type == Command.EXIT)) {
            if (this.singleRightCommand == null || this.singleRightCommand.getPriority() > priority) {
                this.singleRightCommand = cmd;
            }
        }
        if (this.positiveCommand == null && type != Command.BACK && type != Command.CANCEL && type != Command.EXIT) {
            this.positiveCommand = cmd;
        }
        addCommand(item);
        if (this.isInitialized) {
            setInitialized(false);
            repaint();
        }
    }

    public void removeCommand(Command cmd) {
        this.allCommands.remove(cmd);
        if (cmd == this.positiveCommand) {
            this.positiveCommand = null;
        }
        if (cmd == this.singleLeftCommand) {
            this.singleLeftCommand = null;
            if (this.isInitialized) {
                setInitialized(false);
                repaint();
            }
        }
        if (cmd == this.singleRightCommand) {
            this.singleRightCommand = null;
            if (this.singleLeftCommand != null) {
                if (this.singleLeftCommand.getCommandType() == Command.BACK || this.singleLeftCommand.getCommandType() == Command.CANCEL) {
                    this.singleRightCommand = this.singleLeftCommand;
                    this.singleRightCommandItem.setText(this.singleLeftCommand.getLabel());
                    this.singleLeftCommand = null;
                }
                if (this.isInitialized) {
                    setInitialized(false);
                    repaint();
                }
                return;
            }
            int index = this.commandsList.indexOf(cmd);
            if (index != -1) {
                if (index == this.commandsContainer.getFocusedIndex()) {
                    this.commandsContainer.focusChild(-1);
                }
                this.commandsList.remove(index);
                this.commandsContainer.remove(index);
            }
            int newSingleRightCommandIndex = getNextNegativeOrPositiveCommandIndex(true);
            if (newSingleRightCommandIndex != -1) {
                this.singleRightCommand = (Command) this.commandsList.get(newSingleRightCommandIndex);
                this.singleRightCommandItem.setText(this.singleRightCommand.getLabel());
            }
        }
        int index = this.commandsList.indexOf(cmd);
        if (index != -1) {
            if (index == this.commandsContainer.getFocusedIndex()) {
                this.commandsContainer.focusChild(-1);
            }
            this.commandsList.remove(index);
            this.commandsContainer.remove(index);
        }
        if (isInitialized()) {
            setInitialized(false);
            repaint();
        }
    }

    /**
	 * Retrieves the next possible middle command (ITEM or OK with the lowest priority number).
	 * @return the next possible middle command or null if none has found
	 */
    private Command extractNextMiddleCommand() {
        Command next = null;
        next = getNextMiddleCommand(next, this.singleLeftCommand);
        next = getNextMiddleCommand(next, this.singleRightCommand);
        Object[] myCommands = this.commandsList.getInternalArray();
        int index = -1;
        for (int i = 0; i < myCommands.length; i++) {
            Command command = (Command) myCommands[i];
            if (command == null) {
                break;
            }
            Command cmd = getNextMiddleCommand(next, command);
            if (cmd != next) {
                index = i;
                next = cmd;
            }
        }
        if (next != null) {
            if (next == this.singleLeftCommand) {
                this.singleLeftCommand = null;
                this.singleLeftCommandItem.setText(null);
                index = getNextNegativeOrPositiveCommandIndex(false);
                if (index != -1) {
                    this.singleLeftCommand = (Command) this.commandsList.remove(index);
                    this.commandsContainer.remove(index);
                    this.singleLeftCommandItem.setText(this.singleLeftCommand.getLabel());
                }
            } else if (next == this.singleRightCommand) {
                this.singleRightCommand = null;
                this.singleRightCommandItem.setText(null);
                index = getNextNegativeOrPositiveCommandIndex(false);
                if (index != -1) {
                    this.singleRightCommand = (Command) this.commandsList.remove(index);
                    this.commandsContainer.remove(index);
                    this.singleRightCommandItem.setText(this.singleRightCommand.getLabel());
                }
            } else {
                if (index == this.commandsContainer.focusedIndex) {
                    this.commandsContainer.focusChild(-1);
                }
                this.commandsContainer.remove(index);
                this.commandsList.remove(index);
                if (this.commandsList.size() == 1) {
                    CommandItem remainingItem = (CommandItem) this.commandsContainer.get(0);
                    if (!remainingItem.hasChildren) {
                        this.singleLeftCommand = (Command) this.commandsList.get(0);
                        this.singleLeftCommandItem.setText(this.singleLeftCommand.getLabel());
                        this.commandsList.clear();
                        this.commandsContainer.clear();
                    }
                }
            }
        }
        return next;
    }

    private Command getNextMiddleCommand(Command current, Command cmd) {
        if (cmd != null) {
            int commandType = cmd.getCommandType();
            if (commandType == Command.OK || commandType == Command.ITEM) {
                if (current == null || cmd.getPriority() < current.getPriority()) {
                    return cmd;
                }
            }
        }
        return current;
    }

    private int getNextNegativeOrPositiveCommandIndex(boolean isNegative) {
        Object[] myCommands = this.commandsList.getInternalArray();
        int maxPriority = 1000;
        int maxPriorityId = -1;
        for (int i = 0; i < myCommands.length; i++) {
            Command command = (Command) myCommands[i];
            if (command == null) {
                break;
            }
            int type = command.getCommandType();
            if (((isNegative && ((type == Command.BACK || type == Command.CANCEL || type == Command.STOP || type == Command.EXIT))) || (!isNegative && (type == Command.OK || type == Command.ITEM || type == Command.SCREEN))) && command.getPriority() < maxPriority) {
                maxPriority = command.getPriority();
                maxPriorityId = i;
            }
        }
        return maxPriorityId;
    }

    protected void initContent(int firstLineWidth, int availWidth, int availHeight) {
        if (this.isOpened) {
            int titleHeight = this.screen.getTitleHeight();
            int screenHeight = this.screen.screenHeight;
            this.topY = titleHeight;
            this.commandsContainer.setScrollHeight(screenHeight - titleHeight);
            int containerHeight = this.commandsContainer.getItemHeight(firstLineWidth, firstLineWidth, availHeight);
            int commandsContainerY = screenHeight - containerHeight - 1;
            if (commandsContainerY < titleHeight) {
                containerHeight -= titleHeight - commandsContainerY;
                commandsContainerY = titleHeight;
            }
            this.commandsContainer.relativeY = -containerHeight;
            this.commandsContainer.relativeX = 0;
            this.commandsContainer.relativeX = availWidth - this.commandsContainer.itemWidth;
            this.canScrollUpwards = (this.commandsContainer.yOffset != 0) && (this.commandsContainer.focusedIndex != 0);
            this.canScrollDownwards = (this.commandsContainer.yOffset + containerHeight > screenHeight - titleHeight) && (this.commandsContainer.focusedIndex != this.commandsList.size() - 1);
            this.paintScrollIndicator = this.canScrollUpwards || this.canScrollDownwards;
        } else {
            this.background = null;
            this.border = null;
            this.contentWidth = 0;
            this.contentHeight = 0;
        }
    }

    protected void paintBackgroundAndBorder(int x, int y, int width, int height, Graphics g) {
        if (this.isOpened) {
            if (this.overlayBackground != null) {
                int overlayWidth = this.screen.screenWidth;
                int titleHeight = this.screen.getTitleHeight();
                this.overlayBackground.paint(0, titleHeight, overlayWidth, this.screen.screenHeight - titleHeight, g);
            }
        }
        super.paintBackgroundAndBorder(x, y, width, height, g);
    }

    protected void paintContent(int x, int y, int leftBorder, int rightBorder, Graphics g) {
        boolean paintLeftCommand = false;
        boolean paintRightCommand = false;
        boolean paintMiddleCommand = false;
        if (this.isOpened) {
            int clipX = g.getClipX();
            int clipY = g.getClipY();
            int clipWidth = g.getClipWidth();
            int clipHeight = g.getClipHeight();
            int maxClipHeight = Math.max(this.relativeY - this.topY, this.screen.screenHeight - this.topY);
            g.setClip(0, this.topY, this.screen.screenWidth, maxClipHeight);
            this.commandsContainer.paint(x + this.commandsContainer.relativeX, y + this.commandsContainer.relativeY, x + this.commandsContainer.relativeX, x + this.commandsContainer.relativeX + this.commandsContainer.itemWidth, g);
            g.setClip(clipX, clipY, clipWidth, clipHeight);
        }
        if (paintLeftCommand) {
            CommandItem item = this.singleLeftCommandItem;
            int itemX = x + item.relativeX;
            item.paint(itemX, y + item.relativeY, itemX, itemX + item.itemWidth, g);
        }
        if (paintRightCommand) {
            CommandItem item = this.singleRightCommandItem;
            int itemX = x + item.relativeX;
            item.paint(itemX, y + item.relativeY, itemX, itemX + item.itemWidth, g);
        }
    }

    /**
	 * Used to toggle the opened state of the menu bar
	 * 
	 * @param open true when the menu should be opened
	 */
    protected void setOpen(boolean open) {
        if (!open && this.isOpened) {
            this.commandsContainer.hideNotify();
            this.isInitialized = (open == this.isOpened);
            this.isOpened = open;
        } else if (open && !this.isOpened) {
            if (this.commandsContainer.size() == 0) {
                return;
            }
            this.isInitialized = (open == this.isOpened);
            this.isOpened = open;
            MIDlet.midletInstance.hideSoftKeyboard();
            this.commandsContainer.focusChild(0);
            this.commandsContainer.showNotify();
        }
    }

    protected boolean handleKeyPressed(int keyCode, int gameAction) {
        this.isSoftKeyPressed = false;
        if (this.isOpened) {
            if (keyCode == MENU_KEY) {
                return true;
            }
            if (isSelectOptionsMenuKey(keyCode, gameAction)) {
                this.isSoftKeyPressed = true;
                notifyKeyPressed();
                CommandItem commandItem = (CommandItem) this.commandsContainer.getFocusedItem();
                commandItem.handleKeyPressed(0, Canvas.FIRE);
                return true;
            } else if (isCloseOptionsMenuKey(keyCode, gameAction) || (keyCode == 4)) {
                CommandItem item = getCloseOptionsItem();
                if (item != null) {
                    item.notifyItemPressedStart();
                }
                this.isSoftKeyPressed = true;
                notifyKeyPressed();
                this.commandsContainer.handleKeyPressed(0, Canvas.LEFT);
                return true;
            } else {
                boolean handled = this.commandsContainer.handleKeyPressed(keyCode, gameAction);
                if (handled) {
                    setInitialized(false);
                } else {
                    if (gameAction == Canvas.DOWN || gameAction == Canvas.UP) {
                        de.enough.polish.util.Debug.debug("error", "de.enough.polish.ui.MenuBar", 1241, "Container DID NOT HANDLE DOWN OR UP, selectedIndex=" + this.commandsContainer.getFocusedIndex() + ", count=" + this.commandsContainer.size() + ", cycling=", this.commandsContainer.allowCycling);
                    }
                    if (keyCode >= Canvas.KEY_NUM1 && keyCode <= Canvas.KEY_NUM9) {
                        int index = keyCode - Canvas.KEY_NUM1;
                        if (index <= this.commandsContainer.size()) {
                            CommandItem item = (CommandItem) this.commandsContainer.get(index);
                            if (item.getAppearanceMode() != Item.PLAIN) {
                                if (!item.isFocused) {
                                    this.commandsContainer.focusChild(index);
                                }
                                handled = item.handleKeyPressed(0, Canvas.FIRE);
                                return handled;
                            }
                        }
                    }
                }
                return true;
            }
        } else {
            if (keyCode == MENU_KEY && this.commandsContainer.size() > 0) {
                notifyKeyPressed();
                return true;
            }
            if (this.singleLeftCommand != null && this.screen.isSoftKeyLeft(keyCode, gameAction) && this.singleLeftCommandItem.getAppearanceMode() != PLAIN) {
                this.isSoftKeyPressed = true;
                notifyKeyPressed();
                this.singleLeftCommandItem.notifyItemPressedStart();
                return true;
            } else if (this.singleRightCommand != null && this.screen.isSoftKeyRight(keyCode, gameAction) && this.singleRightCommandItem.getAppearanceMode() != PLAIN) {
                this.isSoftKeyPressed = true;
                notifyKeyPressed();
                this.singleRightCommandItem.notifyItemPressedStart();
                return true;
            } else if (isOpenOptionsMenuKey(keyCode, gameAction)) {
                this.isSoftKeyPressed = true;
                CommandItem item = getOpenOptionsItem();
                if (item != null) {
                    item.notifyItemPressedStart();
                }
                return true;
            }
        }
        return false;
    }

    private CommandItem getOpenOptionsItem() {
        CommandItem result;
        result = this.singleLeftCommandItem;
        return result;
    }

    /**
	 * Retrieves the item responsible for closing the options.
	 * @return the commanditem that closes an open menu, can be null
	 */
    private CommandItem getCloseOptionsItem() {
        CommandItem item;
        item = this.singleRightCommandItem;
        return item;
    }

    /**
	 * Determines whether the given key should open the commands menu
	 * @param keyCode the key code
	 * @return true when the commands menu should be opened by this key
	 */
    private boolean isOpenOptionsMenuKey(int keyCode, int gameAction) {
        boolean result;
        result = this.screen.isSoftKeyLeft(keyCode, gameAction);
        return result;
    }

    /**
	 * Determines whether the given key should close the commands menu
	 * @param keyCode the key code
	 * @return true when the commands menu should be closed by this key
	 */
    private boolean isCloseOptionsMenuKey(int keyCode, int gameAction) {
        boolean result;
        result = this.screen.isSoftKeyRight(keyCode, gameAction);
        return result;
    }

    /**
	 * Determines whether the given key should select an entry of the opened the commands menu
	 * @param keyCode the key code
	 * @return true when the key should select the currently focused command option
	 */
    private boolean isSelectOptionsMenuKey(int keyCode, int gameAction) {
        boolean result;
        result = this.screen.isSoftKeyLeft(keyCode, gameAction);
        return result;
    }

    /**
	 * Commits different actions depending on the device using a menubar
	 */
    public static void notifyKeyPressed() {
    }

    protected boolean handleKeyReleased(int keyCode, int gameAction) {
        if (this.isOpened) {
            if (keyCode == MENU_KEY && this.commandsContainer.size() > 0) {
                setOpen(false);
                return true;
            }
            if (isSelectOptionsMenuKey(keyCode, gameAction)) {
                this.isSoftKeyPressed = true;
                CommandItem commandItem = (CommandItem) this.commandsContainer.getFocusedItem();
                return commandItem.handleKeyReleased(0, Canvas.FIRE);
            } else if (isCloseOptionsMenuKey(keyCode, gameAction) || (keyCode == 4)) {
                this.isSoftKeyPressed = true;
                int selectedIndex = this.commandsContainer.getFocusedIndex();
                if (!this.commandsContainer.handleKeyReleased(0, Canvas.LEFT) || selectedIndex != this.commandsContainer.getFocusedIndex()) {
                    CommandItem item = getCloseOptionsItem();
                    if (item != null) {
                        item.notifyItemPressedEnd();
                    }
                    setOpen(false);
                }
                return true;
            } else {
                if (gameAction == Canvas.FIRE && ((CommandItem) this.commandsContainer.focusedItem).command == this.hideCommand) {
                    setOpen(false);
                    return true;
                }
                boolean handled = this.commandsContainer.handleKeyReleased(keyCode, gameAction);
                if (handled) {
                    setInitialized(false);
                } else {
                    if (keyCode >= Canvas.KEY_NUM1 && keyCode <= Canvas.KEY_NUM9) {
                        int index = keyCode - Canvas.KEY_NUM1;
                        if (index <= this.commandsContainer.size()) {
                            CommandItem item = (CommandItem) this.commandsContainer.get(index);
                            if (item.getAppearanceMode() != Item.PLAIN) {
                                if (!item.isFocused) {
                                    this.commandsContainer.focusChild(index);
                                }
                                handled = item.handleKeyReleased(0, Canvas.FIRE);
                                return handled;
                            }
                        }
                    }
                }
                return true;
            }
        } else {
            if (keyCode == MENU_KEY && this.commandsContainer.size() > 0) {
                setOpen(true);
                return true;
            }
            if (this.singleLeftCommand != null && this.screen.isSoftKeyLeft(keyCode, gameAction) && this.singleLeftCommandItem.getAppearanceMode() != PLAIN) {
                this.isSoftKeyPressed = true;
                this.singleLeftCommandItem.notifyItemPressedEnd();
                this.screen.callCommandListener(this.singleLeftCommand);
                return true;
            } else if (this.singleRightCommand != null && this.screen.isSoftKeyRight(keyCode, gameAction) && this.singleRightCommandItem.getAppearanceMode() != PLAIN) {
                this.isSoftKeyPressed = true;
                this.singleRightCommandItem.notifyItemPressedEnd();
                this.screen.callCommandListener(this.singleRightCommand);
                return true;
            } else if (isOpenOptionsMenuKey(keyCode, gameAction)) {
                this.isSoftKeyPressed = true;
                Item item = getOpenOptionsItem();
                if (item != null) {
                    item.notifyItemPressedEnd();
                }
                if (!this.isOpened && this.positiveCommand != null && (this.singleRightCommand == null && this.commandsContainer.size() == 2)) {
                    this.screen.callCommandListener(this.positiveCommand);
                    return true;
                } else if (this.commandsList.size() > 0) {
                    setOpen(true);
                    return true;
                }
            }
        }
        return super.handleKeyReleased(keyCode, gameAction);
    }

    protected boolean handleKeyRepeated(int keyCode, int gameAction) {
        return false;
    }

    /**
	 * Returns true if the specified relativeY is in the menubar 
	 * @return true if the specified relativeY is in the menubar, otherwise false
	 */
    protected boolean isInMenubar(int relativeY) {
        return relativeY > 0;
    }

    protected boolean handlePointerPressed(int relX, int relY) {
        int leftCommandEndX = this.singleLeftCommandItem.relativeX + this.singleLeftCommandItem.itemWidth;
        int rightCommandStartX = this.singleRightCommandItem.relativeX;
        if (this.isOpened) {
            relY -= this.commandsContainer.relativeY;
            relX -= this.commandsContainer.relativeX;
            boolean handled = this.commandsContainer.handlePointerPressed(relX, relY);
            return true;
        }
        return false;
    }

    protected boolean handlePointerReleased(int relX, int relY) {
        int leftCommandEndX = this.singleLeftCommandItem.relativeX + this.singleLeftCommandItem.itemWidth;
        int rightCommandStartX = this.singleRightCommandItem.relativeX;
        if (this.isOpened) {
            relY -= this.commandsContainer.relativeY;
            relX -= this.commandsContainer.relativeX;
            boolean handled = this.commandsContainer.handlePointerReleased(relX, relY);
            if (!handled) {
                setOpen(false);
            }
            return true;
        }
        return false;
    }

    protected boolean handlePointerDragged(int relX, int relY) {
        if (this.isOpened) {
            int y = relY - this.commandsContainer.relativeY;
            int x = relX = this.commandsContainer.relativeX;
            this.commandsContainer.handlePointerDragged(x, y);
            return true;
        }
        return super.handlePointerDragged(relX, relY);
    }

    public void setStyle(Style style) {
        if (this.overlayBackground == null) {
            this.overlayBackground = new TranslucentSimpleBackground(0x88000000);
        }
        this.background = null;
        this.border = null;
    }

    public void animate(long currentTime, ClippingRegion repaintRegion) {
        super.animate(currentTime, repaintRegion);
        if (this.isOpened) {
            this.commandsContainer.animate(currentTime, repaintRegion);
        }
        if (this.singleLeftCommandItem != null) {
            this.singleLeftCommandItem.animate(currentTime, repaintRegion);
        }
        if (this.singleRightCommandItem != null) {
            this.singleRightCommandItem.animate(currentTime, repaintRegion);
        }
    }

    /**
	 * Adds the given command as a subcommand to the specified parent command.
	 * 
	 * @param parentCommand the parent command
	 * @param childCommand the child command
	 * @throws IllegalStateException when the parent command has not be added before
	 * @see #addSubCommand(Command, Command, Style)
	 */
    public void addSubCommand(Command childCommand, Command parentCommand) {
        if (this.menuItemStyle != null) {
            addSubCommand(childCommand, parentCommand, this.menuItemStyle);
        } else {
            addSubCommand(childCommand, parentCommand, de.enough.polish.ui.StyleSheet.defaultStyle);
        }
    }

    /**
	 * Adds the given command as a subcommand to the specified parent command.
	 * 
	 * @param parentCommand the parent command
	 * @param childCommand the child command
	 * @param commandStyle the style for the command
	 * @throws IllegalStateException when the parent command has not be added before
	 * @see #addSubCommand(Command, Command)
	 */
    public void addSubCommand(Command childCommand, Command parentCommand, Style commandStyle) {
        if (parentCommand == this.positiveCommand) {
            this.positiveCommand = null;
        }
        CommandItem parentCommandItem = (CommandItem) this.allCommands.get(parentCommand);
        if (parentCommand == this.singleLeftCommand) {
            addCommand(parentCommandItem);
            this.singleLeftCommand = null;
        } else if (parentCommand == this.singleRightCommand) {
            addCommand(parentCommandItem);
            this.singleRightCommand = null;
            if (this.singleLeftCommand != null) {
                Command cmd = this.singleLeftCommand;
                this.singleLeftCommand = null;
                addCommand(cmd);
            }
        }
        if (parentCommandItem == null) {
            throw new IllegalStateException();
        }
        CommandItem child = new CommandItem(childCommand, parentCommandItem, commandStyle);
        this.allCommands.put(childCommand, child);
        parentCommandItem.addChild(child);
        if (this.isOpened) {
            setInitialized(false);
            repaint();
        }
    }

    /**
	 * Removes the given command as a subcommand.
	 * 
	 * @param childCommand the command to remove
	 * @param parentCommand the parent command of the command to remove.
	 * @throws IllegalStateException when the command has not been added before
	 */
    public void removeSubCommand(Command childCommand, Command parentCommand) {
        CommandItem childCommandItem = (CommandItem) this.allCommands.get(childCommand);
        if (childCommandItem == null) {
            throw new IllegalStateException();
        }
        CommandItem parentCommandItem = (CommandItem) this.allCommands.get(parentCommand);
        if (parentCommandItem == null) {
            throw new IllegalStateException();
        }
        this.allCommands.remove(childCommand);
        parentCommandItem.removeChild(childCommand);
        if (this.isOpened) {
            setInitialized(false);
            repaint();
        }
    }

    /**
	 * Adds the given command item to the list of commands at the appropriate place.
	 * 
	 * @param item the command item
	 */
    private void addCommand(CommandItem item) {
        if (item == null) {
            de.enough.polish.util.Debug.debug("error", "de.enough.polish.ui.MenuBar", 2009, "MenuBar.addCommand(CommandItem): Unable to add null CommandItem");
            return;
        }
        Command cmd = item.command;
        int priority = cmd.getPriority();
        if (this.commandsList.size() == 0) {
            this.commandsList.add(cmd);
            this.commandsContainer.add(item);
        } else {
            Command[] myCommands = (Command[]) this.commandsList.toArray(new Command[this.commandsList.size()]);
            boolean inserted = false;
            for (int i = 0; i < myCommands.length; i++) {
                Command command = myCommands[i];
                if (cmd == command) {
                    return;
                }
                if (command.getPriority() > priority) {
                    this.commandsList.add(i, cmd);
                    this.commandsContainer.add(i, item);
                    inserted = true;
                    break;
                }
            }
            if (!inserted) {
                this.commandsList.add(cmd);
                this.commandsContainer.add(item);
            }
        }
    }

    /**
	 * Removes all commands from this MenuBar.
	 * This option is only available when the "menu" fullscreen mode is activated.
	 */
    public void removeAllCommands() {
        this.singleLeftCommand = null;
        this.singleRightCommand = null;
        this.singleMiddleCommand = null;
        this.commandsList.clear();
        this.allCommands.clear();
        this.commandsContainer.clear();
        this.singleLeftCommandItem.setText(null);
        this.singleLeftCommandItem.setImage((Image) null);
        this.singleRightCommandItem.setText(null);
        this.singleRightCommandItem.setImage((Image) null);
        setOpen(false);
        repaint();
    }

    /**
	 * Retrieves the CommandItem used for rendering the specified command. 
	 * 
	 * @param command the command
	 * @return the corresponding CommandItem or null when this command is not present in this MenuBar.
	 */
    public CommandItem getCommandItem(Command command) {
        if (command == this.singleLeftCommand) {
            return this.singleLeftCommandItem;
        } else if (command == this.singleRightCommand) {
            return this.singleRightCommandItem;
        }
        return (CommandItem) this.allCommands.get(command);
    }

    public Item getItemAt(int relX, int relY) {
        if (this.isOpened && relY < 0) {
            return this.commandsContainer.getItemAt(relX - this.commandsContainer.relativeX, relY - this.commandsContainer.relativeY);
        } else if (relY >= 0) {
            Item item = this.singleLeftCommandItem.getItemAt(relX - this.singleLeftCommandItem.relativeX, relY - this.singleLeftCommandItem.relativeY);
            if (item != null) {
                return item;
            }
            item = this.singleRightCommandItem.getItemAt(relX - this.singleRightCommandItem.relativeX, relY - this.singleRightCommandItem.relativeY);
            if (item != null) {
                return item;
            }
        }
        return super.getItemAt(relX, relY);
    }

    /**
	 * @return true when this menubar should be positioned vertically, e.g. on the right side of the screen
	 */
    public boolean isOrientationVertical() {
        return this.isOrientationVertical;
    }

    public void setOrientationVertical(boolean isVertical) {
    }

    public int getSpaceTop(int width, int height) {
        return 0;
    }

    public int getSpaceLeft(int width, int height) {
        return 0;
    }

    public int getSpaceRight(int width, int height) {
        return 0;
    }

    public int getSpaceBottom(int width, int height) {
        getItemHeight(width, width, height);
        return 0;
    }

    /**
	 * Retrieves the number of commands in this menubar.
	 * 
	 * @return the number of commands in this menubar.
	 */
    public int size() {
        return this.allCommands.size();
    }

    public Command getCommand(int index) {
        return (Command) this.allCommands.keys()[index];
    }

    public Style getMenuItemStyle() {
        return this.menuItemStyle;
    }

    public void setMenuItemStyle(Style menuItemStyle) {
        this.menuItemStyle = menuItemStyle;
        Object[] commandItems = this.allCommands.values();
        for (int i = 0; i < commandItems.length; i++) {
            CommandItem item = (CommandItem) commandItems[i];
            item.setStyle(menuItemStyle);
        }
        repaint();
    }

    /**
	 * Retrieves the container that contains CommandItems.
	 * Do not modify the container unless you know what you are doing.
	 * @return the container
	 */
    public Container getCommandsContainer() {
        return this.commandsContainer;
    }

    /**
	 * Checks if the specified command is already registered within this menubar.
	 * @param command the command
	 * @return true when the given command is already registered
	 */
    public boolean contains(Command command) {
        return (this.allCommands != null) && (this.allCommands.get(command) != null);
    }

    /**
	 * Adds a new layer of commands, e.g. used for popup ChoiceGroups
	 * @param layerCommands the new layer commands
	 */
    public void addCommandsLayer(Command[] layerCommands) {
        if (this.allCommands != null) {
            this.commandsBeforeLayer = new ArrayList();
            this.commandsBeforeLayer.addAll(this.allCommands.keys());
        }
        removeAllCommands();
        for (int i = 0; i < layerCommands.length; i++) {
            Command command = layerCommands[i];
            addCommand(command);
        }
    }

    /**
	 * 
	 */
    public void removeCommandsLayer() {
        removeAllCommands();
        if (this.commandsBeforeLayer != null) {
            Object[] originalCommands = this.commandsBeforeLayer.getInternalArray();
            for (int i = 0; i < originalCommands.length; i++) {
                Command command = (Command) originalCommands[i];
                if (command == null) {
                    break;
                }
                addCommand(command);
            }
        }
    }

    /**
	 * Informs the menubar about an possibly updated default command.
	 * When the menubar displays middle commands, the default command will be shown as the middle command.
	 * 
	 * @param cmd the new default command
	 */
    public void informDefaultCommand(Command cmd) {
    }

    public void fireEvent(String eventName, Object eventData) {
        super.fireEvent(eventName, eventData);
        this.singleLeftCommandItem.fireEvent(eventName, eventData);
        this.singleRightCommandItem.fireEvent(eventName, eventData);
        this.commandsContainer.fireEvent(eventName, eventData);
    }

    /**
	 * Focuses the child command item of this menubar.
	 * @param item the item
	 */
    public void focusChild(Item item) {
        if (this.isOpened) {
            int index = this.commandsContainer.indexOf(item);
            if (index != -1) {
                this.commandsContainer.focusChild(index);
            } else {
                CommandItem focItem = (CommandItem) this.commandsContainer.focusedItem;
                if (focItem != null) {
                    focItem.focusChild(item);
                }
            }
        }
    }

    protected void hideNotify() {
        super.hideNotify();
        if (this.isOpened) {
            setOpen(false);
        }
        if (this.singleLeftCommandItem != null) {
            this.singleLeftCommandItem.hideNotify();
        }
        if (this.singleRightCommandItem != null) {
            this.singleRightCommandItem.hideNotify();
        }
    }

    protected void showNotify() {
        super.showNotify();
        if (this.singleLeftCommandItem != null) {
            this.singleLeftCommandItem.showNotify();
        }
        if (this.singleRightCommandItem != null) {
            this.singleRightCommandItem.showNotify();
        }
    }
}
