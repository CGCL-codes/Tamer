package polr.client.network;

import java.util.HashMap;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.newdawn.slick.SlickException;
import polr.client.GlobalGame;
import polr.client.logic.OurPlayer;
import polr.client.logic.Player;
import polr.client.logic.TempPokemon;
import polr.client.logic.Player.Dirs;
import polr.client.language.*;

/** 
 * This handles all messages received from the server
 */
public class ProtocolHandler extends IoHandlerAdapter {

    private GlobalGame thisGame;

    public ProtocolHandler(GlobalGame game) {
        thisGame = game;
    }

    /** 
	    * Called once the session has begun
	    */
    public void sessionOpened(IoSession session) {
        System.out.println("Connected to server!");
    }

    /** 
	    * Called once the session is closed
	    */
    public void sessionClosed(IoSession session) {
        System.err.println("Total " + session.getReadBytes() + " byte(s)");
        thisGame.setIsPlaying(false);
        thisGame.returnToServerSelect();
    }

    /** 
	    * Called when a message is received from the server. The first char of the message tells the client what to do.
	    */
    @SuppressWarnings("static-access")
    public void messageReceived(IoSession session, Object m) {
        Player p;
        String message = (String) m;
        System.out.println(message);
        switch(message.charAt(0)) {
            case 'l':
                thisGame.getLoading().setVisible(true);
                switch(message.charAt(1)) {
                    case 's':
                        thisGame.getLogin().setVisible(false);
                        thisGame.setIsPlaying(true);
                        break;
                    case '0':
                        GlobalGame.messageBox("Account does not exist.", thisGame.getLogin());
                        thisGame.getLoading().setVisible(false);
                        thisGame.getLogin().enable();
                        break;
                    case '1':
                        GlobalGame.messageBox("Wrong password.", thisGame.getLogin());
                        thisGame.getLoading().setVisible(false);
                        thisGame.getLogin().enable();
                        break;
                    case '2':
                        GlobalGame.messageBox("An unknown error occured. \nPlease try again.", thisGame.getLogin());
                        thisGame.getLoading().setVisible(false);
                        thisGame.getLogin().enable();
                        break;
                    case '3':
                        GlobalGame.messageBox("This account has been banned.", thisGame.getLogin());
                        thisGame.getLoading().setVisible(false);
                        thisGame.getLogin().enable();
                        break;
                    case '4':
                        try {
                            Thread.sleep(120000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        thisGame.getLoading().setVisible(false);
                        thisGame.getLogin().enable();
                        break;
                    case '5':
                        GlobalGame.messageBox("The server is attempting to save data from your last login. Please try login in a few minutes.", thisGame.getLogin());
                        try {
                            Thread.sleep(120000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        thisGame.getLoading().setVisible(false);
                        thisGame.getLogin().enable();
                        break;
                }
                break;
            case 'r':
                switch(message.charAt(1)) {
                    case 's':
                        GlobalGame.messageBox("Successful Registation! \nYou may now login.", thisGame.getLogin());
                        thisGame.getLogin().goToLogin();
                        break;
                    case '0':
                        GlobalGame.messageBox("Username already exists.", thisGame.getLogin());
                        break;
                    case '1':
                        GlobalGame.messageBox("Username banned.", thisGame.getLogin());
                        break;
                    case '2':
                        GlobalGame.messageBox("Please fill out all of the form \nbefore attempting to register.", thisGame.getLogin());
                        break;
                    case '3':
                        GlobalGame.messageBox("An unknown error occurred.", thisGame.getLogin());
                        break;
                    case '4':
                        GlobalGame.messageBox("The server is in lockdown mode. Please try again later.", thisGame.getLogin());
                        break;
                }
                break;
            case 'A':
                String[] workload = message.substring(1).split(",");
                if (workload[1].equalsIgnoreCase(thisGame.user)) {
                    p = new OurPlayer();
                    p = new OurPlayer();
                    OurPlayer pl = (OurPlayer) p;
                    pl.thisPlayer = true;
                    if (thisGame.thisPlayer != null) pl.transfer(thisGame.thisPlayer);
                    thisGame.thisPlayer = pl;
                    thisGame.getMapMatrix().setCurrentPlayer(p);
                    if (thisGame.thisPlayer.getTeam() == null) {
                        thisGame.getPacketGenerator().write("up");
                        thisGame.getPacketGenerator().write("ub");
                    }
                    if (thisGame.getLoading().isVisible()) thisGame.getLoading().setVisible(false);
                } else p = new Player();
                p.index = Long.parseLong(workload[0]);
                if (workload[3].equalsIgnoreCase("Invisible")) {
                    p.username = "";
                    p.spriteType = "Invisible";
                } else {
                    p.username = workload[1];
                    p.spriteType = workload[3];
                }
                p.setFacing(p.dirValue(workload[2]));
                p.x = Short.parseShort(workload[4]);
                p.y = Short.parseShort(workload[5]);
                p.svrX = Short.parseShort(workload[4]);
                p.svrY = Short.parseShort(workload[5]);
                if (p instanceof OurPlayer) {
                    thisGame.thisPlayer.map = thisGame.getMapMatrix().getMap(1, 1);
                    thisGame.thisPlayer.map.setXOffset(GlobalGame.width / 2 - thisGame.thisPlayer.x);
                    thisGame.thisPlayer.map.setYOffset(GlobalGame.height / 2 - thisGame.thisPlayer.y);
                }
                thisGame.queuePlayer(p);
                break;
            case 'd':
                for (int i = 0; i < thisGame.thisPlayer.map.getMapPlayers().size(); i++) {
                    if (thisGame.thisPlayer.map.getMapPlayers().get(i).index == Long.parseLong(message.substring(1))) {
                        thisGame.thisPlayer.map.getMapPlayers().remove(i);
                        break;
                    }
                }
                break;
            case 'P':
                String[] pdata = message.substring(1).split(",");
                if (thisGame.thisPlayer != null) {
                    thisGame.thisPlayer.initPokemon(pdata);
                    thisGame.setTeamUpdate(true);
                } else {
                    System.err.println("ERROR WITH INITIALISING POKEMON DATA");
                }
                break;
            case 'B':
                switch(message.charAt(1)) {
                    case 'i':
                        message = message.substring(2);
                        while (message.length() >= 5) {
                            int q = Integer.parseInt(message.substring(3, 5));
                            int id = Integer.parseInt(message.substring(0, 3));
                            for (int i = 1; i <= q; i++) {
                                thisGame.thisPlayer.addItem(id);
                            }
                            message = message.length() - 5 > 0 ? message.substring(5) : "";
                        }
                        thisGame.updateBagInfo();
                        break;
                    case 'a':
                        message = message.substring(2);
                        thisGame.thisPlayer.addItem(Integer.parseInt(message));
                        thisGame.updateBagInfo();
                        break;
                    case 'r':
                        thisGame.thisPlayer.useItem(Integer.parseInt(message.substring(2)));
                        thisGame.updateBagInfo();
                        break;
                }
                break;
            case 'm':
                thisGame.getLoading().setVisible(true);
                String[] mapWork = message.substring(1).split(",");
                thisGame.mapX = Integer.parseInt(mapWork[0]);
                thisGame.mapY = Integer.parseInt(mapWork[1]);
                thisGame.newMap = true;
                break;
            case 'c':
                switch(message.charAt(1)) {
                    case 'w':
                        thisGame.getInterface().addWorldChatLine(message.substring(2));
                        break;
                    case 'l':
                        thisGame.getInterface().addLocalChatLine(message.substring(2));
                        break;
                    case 'm':
                        GlobalGame.messageBox(message.substring(2), thisGame.getDisplay());
                        break;
                    case 's':
                        break;
                }
                break;
            case 'b':
                switch(message.charAt(1)) {
                    case 'i':
                        thisGame.beginBattle(message.substring(2, message.indexOf(',')), message.charAt(message.indexOf(',') + 1), Integer.parseInt(message.substring(message.lastIndexOf(",") + 1)));
                        break;
                    case 'I':
                        thisGame.getBattle().setPlayerBattleIndex(Integer.parseInt(message.substring(2)));
                        break;
                    case 'P':
                        String[] ePokeData = message.substring(2).split(",");
                        thisGame.getBattle().addEnemyPokemon(ePokeData[1], Integer.parseInt(ePokeData[0]), Integer.parseInt(ePokeData[2]), Integer.parseInt(ePokeData[3]), Integer.parseInt(ePokeData[4]), ePokeData[5]);
                        break;
                    case 'a':
                        thisGame.getBattle().requestMove();
                        break;
                    case 'c':
                        int ballData = Integer.parseInt(message.substring(2));
                        switch(ballData) {
                            case 1:
                                thisGame.getBattle().addMessage("The Pokemon was caught successfully!");
                                break;
                            case 2:
                                thisGame.getBattle().addMessage("There wasn't anywhere to put the Pokemon.");
                                thisGame.getBattle().addMessage("The pokemon was sent to your storage box.");
                                break;
                            case 3:
                                thisGame.getBattle().endBattle("CAUGHT");
                            case 4:
                                thisGame.getBattle().addMessage("It got away...");
                                break;
                        }
                    case '?':
                        thisGame.getBattle().forceSwitchPoke();
                        break;
                    case 'A':
                        if (thisGame.getBattle() != null) {
                            thisGame.getBattle().requestMove();
                        }
                        if (message.endsWith("s")) {
                            thisGame.getBattle().forceSwitchPoke();
                        }
                        break;
                    case 'p':
                        try {
                            String[] ppCount = message.split("!");
                            thisGame.getBattle().updatePP(Integer.parseInt(ppCount[1]), Integer.parseInt(ppCount[2]));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 'm':
                        String[] data = message.split(";");
                        String moveMsg = data[1] + " used " + data[2] + ".";
                        thisGame.getBattle().addMessage(moveMsg);
                        break;
                    case 'd':
                        try {
                            String[] comp = message.split(";");
                            TempPokemon struckPoke;
                            if (comp[1].equals(thisGame.user)) {
                                System.out.println("OUR POKE HAS TAKEN DAMAGE");
                                struckPoke = thisGame.getBattle().getCurPoke();
                                struckPoke.setCurHP(struckPoke.getCurHP() + Integer.parseInt(comp[3]));
                                if (struckPoke.getCurHP() < 0) struckPoke.setCurHP(0);
                                if (struckPoke.getCurHP() > struckPoke.getMaxHP()) struckPoke.setCurHP(struckPoke.getMaxHP());
                                thisGame.getBattle().updatePlayerHP(struckPoke.getCurHP());
                            } else {
                                System.out.println("ENEMY POKE HAS TAKEN DAMAGE");
                                struckPoke = thisGame.getBattle().getCurEnemyPoke();
                                struckPoke.setCurHP(struckPoke.getCurHP() + Integer.parseInt(comp[3]));
                                if (struckPoke.getCurHP() < 0) struckPoke.setCurHP(0);
                                if (struckPoke.getCurHP() > struckPoke.getMaxHP()) struckPoke.setCurHP(struckPoke.getMaxHP());
                                thisGame.getBattle().updateEnemyHP(struckPoke.getCurHP());
                            }
                            String damage;
                            if (Integer.parseInt(comp[3]) < 0) {
                                damage = comp[1] + "'s " + comp[2] + " took " + Integer.parseInt(comp[3]) * -1 + " damage!\n" + "It has only " + struckPoke.getCurHP() + " out" + " of " + struckPoke.getMaxHP() + " HP remaining.";
                            } else {
                                damage = comp[1] + "'s " + comp[2] + " was healed for " + Integer.parseInt(comp[3]) + " HP.\n" + "It now has " + struckPoke.getCurHP() + " out" + " of " + struckPoke.getMaxHP() + " HP remaining.";
                            }
                            thisGame.getBattle().addMessage(damage);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 's':
                        try {
                            String[] compo = message.split(";");
                            String switchMsg;
                            if (compo[1].equals(thisGame.user)) {
                                switchMsg = "You switched out " + thisGame.getBattle().getCurPoke().getName();
                                thisGame.getBattle().switchOurPoke(Integer.parseInt(compo[2]));
                                switchMsg += " and sent in " + thisGame.getBattle().getCurPoke().getName() + "!";
                            } else {
                                if (thisGame.getBattle().getCurEnemyPoke() != null) {
                                    switchMsg = compo[1] + " switched out " + thisGame.getBattle().getCurEnemyPoke().getName();
                                    thisGame.getBattle().switchEnemyPoke(Integer.parseInt(compo[2]));
                                    switchMsg += " and sent in " + thisGame.getBattle().getCurEnemyPoke().getName() + "!";
                                } else {
                                    thisGame.getBattle().switchEnemyPoke(Integer.parseInt(compo[2]));
                                    switchMsg = compo[1] + " sent out " + thisGame.getBattle().getCurEnemyPoke().getName() + "!";
                                }
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 'S':
                        String[] effectData = message.split(";");
                        if (effectData[1].equals(thisGame.user)) {
                            switch(Integer.parseInt(effectData[1])) {
                                case '0':
                                    thisGame.getBattle().getCurPoke().setEffect("burnt");
                                    break;
                                case '1':
                                    thisGame.getBattle().getCurPoke().setEffect("poisoned");
                                    break;
                                case '2':
                                    thisGame.getBattle().getCurPoke().setEffect("frozen");
                                    break;
                                case '3':
                                    thisGame.getBattle().getCurPoke().setEffect("confused");
                                    break;
                                case '4':
                                    thisGame.getBattle().getCurPoke().setEffect("asleep");
                                    break;
                                case '5':
                                    thisGame.getBattle().getCurPoke().setEffect("paralized");
                                    break;
                                case 'a':
                                    break;
                            }
                            thisGame.getBattle().addMessage(thisGame.getBattle().getCurPoke().getName() + " is " + thisGame.getBattle().getCurPoke().getEffect());
                        } else {
                            switch(Integer.parseInt(effectData[2])) {
                                case '0':
                                    thisGame.getBattle().getCurEnemyPoke().setEffect("burnt");
                                    break;
                                case '1':
                                    thisGame.getBattle().getCurEnemyPoke().setEffect("poisoned");
                                    break;
                                case '2':
                                    thisGame.getBattle().getCurEnemyPoke().setEffect("frozen");
                                    break;
                                case '3':
                                    thisGame.getBattle().getCurEnemyPoke().setEffect("confused");
                                    break;
                                case '4':
                                    thisGame.getBattle().getCurEnemyPoke().setEffect("asleep");
                                    break;
                                case '5':
                                    thisGame.getBattle().getCurEnemyPoke().setEffect("paralized");
                                    break;
                                case 'a':
                                    break;
                            }
                            thisGame.getBattle().addMessage(thisGame.getBattle().getCurEnemyPoke().getName() + " is " + thisGame.getBattle().getCurEnemyPoke().getEffect());
                        }
                        break;
                    case 'R':
                        String[] effRemoved = message.split(";");
                        if (effRemoved[1].equals(thisGame.user)) {
                            thisGame.getBattle().addMessage(thisGame.getBattle().getCurPoke().getName() + " is no longer " + thisGame.getBattle().getCurPoke().getEffect());
                            thisGame.getBattle().getCurPoke().setEffect("normal");
                        } else {
                            thisGame.getBattle().addMessage(thisGame.getBattle().getCurEnemyPoke().getName() + " is no longer " + thisGame.getBattle().getCurEnemyPoke().getEffect());
                            thisGame.getBattle().getCurEnemyPoke().setEffect("normal");
                        }
                    case 'M':
                        String[] moves = message.split(",");
                        for (int i = 2; i < moves.length; i++) {
                            thisGame.getBattle().learnMove(Integer.parseInt(moves[1]), moves[2]);
                        }
                        break;
                    case 'l':
                        String[] lvlData = message.split(",");
                        int pkmnID = Integer.parseInt(lvlData[1]);
                        int level = Integer.parseInt(lvlData[2]);
                        thisGame.thisPlayer.getTeam()[pkmnID].setLevel(level);
                        thisGame.getBattle().refreshPokemonInfo();
                        break;
                    case 'e':
                        thisGame.getBattle().giveExp(message.substring(1));
                        break;
                    case 'E':
                        String[] evosData = message.split(",");
                        thisGame.getBattle().addMessage("What!? " + evosData[1] + " is evolving!");
                        thisGame.getBattle().addMessage(evosData[1] + " evolved into " + evosData[2]);
                        break;
                    case 'f':
                        if (thisGame.getBattle() != null) {
                            thisGame.getBattle().endBattle(message.substring(1));
                        }
                        thisGame.setTeamUpdate(true);
                        break;
                    case 'z':
                        thisGame.getBattle().addMessage(message.substring(2) + "fainted");
                        break;
                    case 'r':
                        thisGame.getBattle().run();
                    case '!':
                        break;
                }
                break;
            case 'T':
                try {
                    thisGame.talkToNPC(message.substring(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 'U':
                try {
                    if (thisGame.thisPlayer.map != null) thisGame.getMapMatrix().findPlayer(message.substring(1)).moveUp();
                } catch (Exception e) {
                    thisGame.getLoading().setVisible(true);
                    thisGame.thisPlayer.map.wipe();
                    GlobalGame.getPacketGenerator().write("m");
                }
                break;
            case 'L':
                try {
                    if (thisGame.thisPlayer.map != null) thisGame.getMapMatrix().findPlayer(message.substring(1)).moveLeft();
                } catch (Exception e) {
                    thisGame.getLoading().setVisible(true);
                    thisGame.thisPlayer.map.wipe();
                    GlobalGame.getPacketGenerator().write("m");
                }
                break;
            case 'R':
                try {
                    if (thisGame.thisPlayer.map != null) thisGame.getMapMatrix().findPlayer(message.substring(1)).moveRight();
                } catch (Exception e) {
                    thisGame.getLoading().setVisible(true);
                    thisGame.thisPlayer.map.wipe();
                    GlobalGame.getPacketGenerator().write("m");
                }
                break;
            case 'D':
                try {
                    if (thisGame.thisPlayer.map != null) thisGame.getMapMatrix().findPlayer(message.substring(1)).moveDown();
                } catch (Exception e) {
                    thisGame.getLoading().setVisible(true);
                    thisGame.thisPlayer.map.wipe();
                    GlobalGame.getPacketGenerator().write("m");
                }
                break;
            case 'C':
                switch(message.charAt(1)) {
                    case 'S':
                        try {
                            thisGame.getMapMatrix().findPlayer(message.substring(2, message.indexOf(","))).spriteType = message.substring(message.indexOf(",") + 1);
                        } catch (Exception e) {
                            System.err.println("Could not find player for sprite change.");
                            e.printStackTrace();
                        }
                        break;
                    case 'U':
                        try {
                            thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Up);
                        } catch (Exception e) {
                            thisGame.getLoading().setVisible(true);
                            thisGame.thisPlayer.map.wipe();
                            GlobalGame.getPacketGenerator().write("m");
                        }
                        break;
                    case 'D':
                        try {
                            thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Down);
                        } catch (Exception e) {
                            thisGame.getLoading().setVisible(true);
                            thisGame.thisPlayer.map.wipe();
                            GlobalGame.getPacketGenerator().write("m");
                        }
                        break;
                    case 'L':
                        try {
                            thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Left);
                        } catch (Exception e) {
                            thisGame.getLoading().setVisible(true);
                            thisGame.thisPlayer.map.wipe();
                            GlobalGame.getPacketGenerator().write("m");
                        }
                        break;
                    case 'R':
                        try {
                            thisGame.getMapMatrix().findPlayer(message.substring(2)).setFacing(Dirs.Right);
                        } catch (Exception e) {
                            thisGame.getLoading().setVisible(true);
                            thisGame.thisPlayer.map.wipe();
                            GlobalGame.getPacketGenerator().write("m");
                        }
                        break;
                }
                break;
            case 'x':
                HashMap<String, Integer> merch = new HashMap<String, Integer>();
                String[] merchData = message.split(",");
                for (int i = 1; i < merchData.length; i += 2) {
                    merch.put(merchData[i], Integer.parseInt(merchData[i + 1]));
                }
                thisGame.useShop(merch);
                break;
            case 'w':
                String[] pokeData = message.split(";");
                thisGame.setWildEnemy(pokeData[1], pokeData[2], pokeData[3], pokeData[4], pokeData[5]);
                break;
            case 'S':
                try {
                    thisGame.usePokeStorageBox(message.substring(1));
                } catch (SlickException e2) {
                    e2.printStackTrace();
                }
                break;
        }
    }

    /** 
	    * If a network error occurs, this gets called.
	    */
    public void exceptionCaught() {
        System.err.println("An error occured in the Network Protocol.");
    }

    private String toProperCase(String target) {
        return target.toUpperCase().substring(0, 1) + target.toLowerCase().substring(1);
    }
}
