package clavicom.core.engine;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import clavicom.core.keygroup.CKey;
import clavicom.core.keygroup.keyboard.blocks.CKeyGroup;
import clavicom.core.keygroup.keyboard.blocks.CKeyList;
import clavicom.core.keygroup.keyboard.command.CCode;
import clavicom.core.keygroup.keyboard.command.CCommand;
import clavicom.core.keygroup.keyboard.command.commandSet.CCommandSet;
import clavicom.core.keygroup.keyboard.key.CKeyCharacter;
import clavicom.core.keygroup.keyboard.key.CKeyDynamicString;
import clavicom.core.keygroup.keyboard.key.CKeyShortcut;
import clavicom.core.keygroup.keyboard.key.CKeyKeyboard;
import clavicom.core.listener.OnClickKeyCharacterListener;
import clavicom.core.listener.OnClickKeyDynamicStringCommandListener;
import clavicom.core.listener.OnClickKeyShortcutListener;
import clavicom.core.listener.ReleaseHoldableKeysListener;
import clavicom.core.message.CMessageEngine;
import clavicom.core.profil.CKeyboard;
import clavicom.core.profil.CProfil;
import clavicom.gui.language.UIString;
import clavicom.tools.TKeyAction;
import clavicom.tools.TLevelEnum;

public class CCommandEngine implements OnClickKeyCharacterListener, OnClickKeyShortcutListener, OnClickKeyDynamicStringCommandListener {

    protected EventListenerList listenerNewMessageList;

    static CCommandEngine instance;

    List<CKey> holdKey;

    Robot robot;

    protected EventListenerList listenerList;

    protected CCommandEngine(CKeyboard keyboard) {
        listenerNewMessageList = new EventListenerList();
        holdKey = new ArrayList<CKey>();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            CMessageEngine.newError(UIString.getUIString("MSG_COMMAND_ENGINE_NO_ROBOT"), e.getMessage());
            return;
        }
        listen(keyboard);
        listenerList = new EventListenerList();
    }

    public static void createInstance(CKeyboard keyboard) {
        instance = new CCommandEngine(keyboard);
    }

    public static CCommandEngine getInstance() {
        return instance;
    }

    public void listen(CKeyKeyboard keyboardKey) {
        if (keyboardKey != null) {
            if (keyboardKey instanceof CKeyCharacter) {
                ((CKeyCharacter) keyboardKey).addOnClickKeyCharacterListener(this);
            } else if (keyboardKey instanceof CKeyDynamicString) {
                ((CKeyDynamicString) keyboardKey).addOnClickKeyDynamicStringListenerCommand(this);
            } else if (keyboardKey instanceof CKeyShortcut) {
                ((CKeyShortcut) keyboardKey).addOnClickKeyShortcutListener(this);
            }
        }
    }

    public void listen(CKeyboard keyboard) {
        for (int i = 0; i < keyboard.groupCount(); ++i) {
            CKeyGroup keyGroup = keyboard.getKeyGroup(i);
            if (keyGroup != null) {
                for (int j = 0; j < keyGroup.listCount(); ++j) {
                    CKeyList keyList = keyGroup.getkeyList(j);
                    if (keyList != null) {
                        for (int k = 0; k < keyList.keyCount(); ++k) {
                            CKeyKeyboard keyboardKey = keyList.getKeyKeyboard(k);
                            if (keyboardKey != null) {
                                if (keyboardKey instanceof CKeyCharacter) {
                                    ((CKeyCharacter) keyboardKey).addOnClickKeyCharacterListener(this);
                                } else if (keyboardKey instanceof CKeyDynamicString) {
                                    ((CKeyDynamicString) keyboardKey).addOnClickKeyDynamicStringListenerCommand(this);
                                } else if (keyboardKey instanceof CKeyShortcut) {
                                    ((CKeyShortcut) keyboardKey).addOnClickKeyShortcutListener(this);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void unListen(CKeyKeyboard keyboardKey) {
        if (keyboardKey != null) {
            if (keyboardKey instanceof CKeyCharacter) {
                ((CKeyCharacter) keyboardKey).removeOnClickKeyCharacterListener(this);
            } else if (keyboardKey instanceof CKeyDynamicString) {
                ((CKeyDynamicString) keyboardKey).removeOnClickKeyDynamicStringListenerCommand(this);
            } else if (keyboardKey instanceof CKeyShortcut) {
                ((CKeyShortcut) keyboardKey).removeOnClickKeyShortcutListener(this);
            }
        }
    }

    protected void executeCommande(List<CCommand> commandList, CKey key) {
        CCode code = null;
        if (key.isHoldable()) {
            if (holdKey.contains(key)) {
                holdKey.remove(key);
            } else {
                holdKey.add(key);
            }
        } else {
            for (CKey holdableKey : holdKey) {
                if (holdableKey instanceof CKeyCharacter) {
                    DoKeyCharacter((CKeyCharacter) holdableKey, false);
                }
            }
            for (CCommand command : commandList) {
                for (int i = 0; i < command.Size(); ++i) {
                    code = command.GetCode(i);
                    if (code.GetKeyAction() == TKeyAction.PRESSED) {
                        doKeyCodePress(code);
                    } else if (code.GetKeyAction() == TKeyAction.RELEASED) {
                        doKeyCodeRelease(code);
                    }
                }
            }
            for (CKey holdableKey : holdKey) {
                if (holdableKey instanceof CKeyCharacter) {
                    DoKeyCharacter((CKeyCharacter) holdableKey, true);
                }
            }
            holdKey.clear();
            CLevelEngine.getInstance().setCurrentLevel(TLevelEnum.NORMAL, false);
            fireReleaseHoldableKeys();
        }
    }

    public void ClearHoldKey() {
        holdKey.clear();
    }

    private void DoKeyCharacter(CKeyCharacter character, boolean release) {
        CCommand command = character.getCommand(CLevelEngine.getInstance().getCurrentLevel());
        CCode code = null;
        if (command != null) {
            for (int i = 0; i < command.Size(); ++i) {
                code = command.GetCode(i);
                if (release) {
                    if (code.GetKeyAction() == TKeyAction.RELEASED) {
                        doKeyCodeRelease(code);
                    }
                } else {
                    if (code.GetKeyAction() == TKeyAction.PRESSED) {
                        doKeyCodePress(code);
                    }
                }
            }
        }
    }

    protected void doKeyCodePress(CCode code) {
        try {
            robot.keyPress(code.GetKeyEvent());
        } catch (Exception ex) {
            CMessageEngine.newError(UIString.getUIString("MSG_COMMAND_ENGINE_CODE_INCORECT"));
            return;
        }
    }

    protected void doKeyCodeRelease(CCode code) {
        try {
            robot.keyRelease(code.GetKeyEvent());
        } catch (Exception ex) {
            CMessageEngine.newError(UIString.getUIString("MSG_COMMAND_ENGINE_CODE_INCORECT"));
            return;
        }
    }

    public void onClickKeyCharacter(CKeyCharacter keyCharacter) {
        if (keyCharacter.getCommand(CLevelEngine.getInstance().getCurrentLevel()) == null) return;
        List<CCommand> commandList = new ArrayList<CCommand>();
        CCommand keyCommand = keyCharacter.getCommand(CLevelEngine.getInstance().getCurrentLevel());
        commandList.add(keyCommand);
        executeCommande(commandList, keyCharacter);
    }

    public void onClickKeyShortcut(CKeyShortcut keyShortcut) {
        if (keyShortcut.getCommand() == null) return;
        List<CCommand> commandList = new ArrayList<CCommand>();
        commandList.add(keyShortcut.getCommand());
        executeCommande(commandList, keyShortcut);
    }

    public void onClickKeyDynamicStringCommand(CKeyDynamicString keyDynamicString) {
        List<CCommand> commandList = null;
        try {
            commandList = keyDynamicString.getCommands();
        } catch (Exception e) {
            return;
        }
        if (commandList.size() < keyDynamicString.getCurrentIndex()) {
            commandList.clear();
        } else {
            for (int i = 0; i < keyDynamicString.getCurrentIndex(); ++i) {
                commandList.remove(0);
            }
        }
        if (CProfil.getInstance().getAdvancedOption().isAddSpaceAfterString()) {
            CCommand commandEspace = CCommandSet.GetInstance().GetCommand(" ");
            if (commandEspace != null) {
                commandList.add(commandEspace);
            }
        }
        if (commandList != null) {
            executeCommande(commandList, keyDynamicString);
        }
    }

    public void addReleaseHoldableKeysListener(ReleaseHoldableKeysListener l) {
        this.listenerList.add(ReleaseHoldableKeysListener.class, l);
    }

    public void removeReleaseHoldableKeysListener(ReleaseHoldableKeysListener l) {
        this.listenerList.remove(ReleaseHoldableKeysListener.class, l);
    }

    protected void fireReleaseHoldableKeys() {
        ReleaseHoldableKeysListener[] listeners = (ReleaseHoldableKeysListener[]) listenerList.getListeners(ReleaseHoldableKeysListener.class);
        for (int i = listeners.length - 1; i >= 0; i--) {
            listeners[i].releasedHoldableKeys();
        }
    }
}
