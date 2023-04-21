package net.sf.rottz.tv.server.communication.commands;

import net.sf.rottz.tv.common.character.SpokenPhrase;
import net.sf.rottz.tv.server.GameCommandCreator;
import net.sf.rottz.tv.server.GameServer;
import net.sf.rottz.tv.server.world.GameCharacterServer;

public class GameCommandGameChatMsg extends GameCommand {

    private final GameCharacterServer charSpeaking;

    private final String chatMsg;

    public GameCommandGameChatMsg(GameCommandCreator cmdCreator, GameCharacterServer cmdDestChar, String chatMsg) {
        super(cmdCreator);
        this.charSpeaking = cmdDestChar;
        this.chatMsg = chatMsg;
    }

    @Override
    public void executeCore(GameServer game) {
        System.out.println("Executing " + getCommandName() + " command. cmd created by " + getCreatorName() + " and executing on char : " + charSpeaking.getName());
        SpokenPhrase phrase = new SpokenPhrase(game.getGameWorld().getCurrentTime(), chatMsg);
        charSpeaking.addChatMessage(phrase);
    }

    @Override
    public String getCommandName() {
        return "Game Speak";
    }
}
