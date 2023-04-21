package com.ham.mud.characters.player;

import com.ham.mud.Colors;
import com.ham.mud.characters.Experience;
import com.ham.mud.classes.MudClassService;
import com.ham.mud.configuration.FilePaths;
import com.ham.mud.items.Item;
import com.ham.mud.items.ItemService;
import com.ham.mud.races.RaceService;
import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by hlucas on Jun 28, 2011 at 12:06:42 PM
 */
public class PlayerService {

    public static Player getPlayer(String username) {
        try {
            return load(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void save(Player player) {
        try {
            doSave(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void doSave(Player player) throws IOException {
        File file = new File(FilePaths.getPlayerFilePath(player.getName().toLowerCase()));
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(player.getZone() + " " + player.getAtX() + " " + player.getAtY() + Colors.NEW_LINE);
        List<Item> inventory = player.getInventory();
        StringBuilder sb = new StringBuilder();
        for (Item item : inventory) {
            sb.append(item.getFileName());
            sb.append(" ");
        }
        String itemsString = sb.toString().trim();
        out.write(itemsString + Colors.NEW_LINE);
        sb = new StringBuilder();
        Collection<Item> equipment = player.getEquipment().values();
        for (Item item : equipment) {
            if (item == null) continue;
            sb.append(item.getFileName());
            sb.append(" ");
        }
        String equipmentString = sb.toString().trim();
        out.write(equipmentString + Colors.NEW_LINE);
        out.write(player.getRace().getName() + " " + player.getMudClass().getName() + " " + player.getExperience().getCount() + Colors.NEW_LINE);
        out.write(String.valueOf(player.getGold()));
        out.close();
        fw.close();
    }

    public static Player load(String username) throws IOException {
        File file = new File(FilePaths.getPlayerFilePath(username.toLowerCase()));
        if (!file.exists()) {
            Player player = new Player(username, "town", 0, 0);
            player.setMudClass(MudClassService.getClass("warrior"));
            player.setExperience(new Experience(0));
            save(player);
            return player;
        }
        FileInputStream inputStream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(inputStream);
        String[] at = in.readLine().split(" ");
        String[] itemNames = in.readLine().split(" ");
        String[] equipmentNames = in.readLine().split(" ");
        String[] raceClassAndExperience = in.readLine().split(" ");
        String gold = in.readLine();
        in.close();
        inputStream.close();
        Player player = new Player(username, at[0], Integer.valueOf(at[1]), Integer.valueOf(at[2]));
        player.setInventory(ItemService.getItems(itemNames));
        player.setEquipment(ItemService.getEquipment(equipmentNames));
        player.setRace(RaceService.getRace(raceClassAndExperience[0]));
        player.setMudClass(MudClassService.getClass(raceClassAndExperience[1].toLowerCase()));
        player.setExperience(new Experience(Integer.valueOf(raceClassAndExperience[2])));
        player.setGold(Integer.valueOf(gold));
        return player;
    }

    public static boolean exists(String username) {
        return new File(FilePaths.getPlayerFilePath(username)).exists();
    }
}
