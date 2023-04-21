package objets;

import java.util.Scanner;
import util.Constantes;

/**
 * @author  Boo
 */
public class Row {

    private String type = "";

    private String name = "";

    private String steamId = "";

    private String side = "";

    private String victimName = "";

    private String victimId = "";

    private String aSide = "";

    private String gun = "";

    private int damage = -1;

    private int damageArmor = -1;

    private int health = -1;

    private int armor = -1;

    private String hitgroup = "";

    private String say = "";

    private String sayTeam = "";

    private String commande = "";

    private String bomb = "";

    private String worldMessage = "";

    private String cssMessage = "";

    public Row(String ligneBrut) {
        System.out.println("*********LIGNE BRUT***************");
        System.out.println(ligneBrut);
        System.out.println("************LIGNE*****************");
        int mm = 0;
        Scanner scanner = new Scanner(ligneBrut);
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
            mm++;
        }
        scanner = new Scanner(ligneBrut);
        String[] scannerString = new String[mm];
        mm = 0;
        while (scanner.hasNext()) {
            scannerString[mm] = scanner.next();
            mm++;
        }
        String scan = scannerString[0].replaceAll("\"", "");
        if (scan.equals("World") || scan.equals("world")) {
            type = "world";
            worldMessage = scannerString[2].replaceAll("\"", "");
            if (worldMessage.contains("Restart_Round")) worldMessage = "Restart_Round";
        } else if (scan.contains("CSSMATCH") || scan.contains("CSSMatch")) {
            type = "cssmatch";
            if (ligneBrut.contains("Fin de la manche")) cssMessage = "endManche";
            int id = -1;
            for (int i = 0; i < scannerString.length - 1 && id == -1; i++) {
                if (scannerString[i].contains("<id=")) id = i;
                if (id != -1) {
                    steamId = scannerString[id + 1].replaceAll("<steamID=\"", "").replaceAll("\">", "");
                    commande = scannerString[id + 5];
                    if (commande.equals("buy")) {
                        gun = scannerString[id + 6];
                    } else if (commande.equals("jointeam")) {
                        side = scannerString[id + 6] == "2" ? Constantes.T : Constantes.CT;
                    }
                }
            }
            if (id != -1) {
                for (int j = 2; j < id; j++) {
                    name += scannerString[j];
                }
            }
        } else if (ligneBrut.charAt(0) == '"') {
            type = "action";
            int id = -1;
            for (int i = 0; i < scannerString.length - 1 && id == -1; i++) {
                String ligneSub = "";
                if (scannerString[i].contains("><STEAM_")) {
                    id = i;
                    for (int j = 0; j <= id; j++) {
                        if (j == id) name += scannerString[j].substring(0, scannerString[j].substring(0, scannerString[j].indexOf("><STEAM_")).lastIndexOf("<")); else if (j == 0) name += scannerString[j].substring(1) + " "; else name += scannerString[j] + " ";
                    }
                    ligneSub = ligneBrut.substring(ligneBrut.indexOf("<STEAM_"));
                    steamId = ligneSub.substring(1, ligneSub.indexOf("><"));
                    ligneSub = ligneSub.substring(ligneSub.indexOf("><") + 1);
                }
                if (scannerString[i].contains("><BOT><")) {
                    id = i;
                    name = scannerString[id].substring(1, scannerString[id].substring(0, scannerString[id].indexOf("><BOT")).lastIndexOf("<"));
                    ligneSub = ligneBrut.substring(ligneBrut.indexOf("<BOT"));
                    steamId = name;
                    ligneSub = ligneSub.substring(ligneSub.indexOf("><") + 1);
                }
                if (id != -1) {
                    side = ligneSub.substring(1, ligneSub.indexOf(">"));
                    if (!"".equals(side) && !"Unassigned".equals(side)) side = side.equals(Constantes.CT) ? Constantes.CT : Constantes.T;
                    commande = scannerString[id + 1];
                    if (commande.equals("say")) {
                        for (int j = id + 2; j <= scannerString.length - 2; j++) {
                            say += scannerString[j] + " ";
                        }
                        say = say.substring(1, say.length() - 2);
                    } else if (commande.equals("say_team")) {
                        for (int j = id + 2; j <= scannerString.length - 2; j++) {
                            sayTeam += scannerString[j] + " ";
                        }
                        sayTeam = sayTeam.substring(1, sayTeam.length() - 2);
                    } else if (commande.equals("changed")) {
                        name = "";
                        for (int j = id + 4; j <= scannerString.length - 2; j++) {
                            name += scannerString[j] + " ";
                        }
                        name = name.substring(1, name.length() - 2);
                    } else if (commande.equals("joined")) {
                        if (Constantes.T.equals(scannerString[scannerString.length - 2])) side = Constantes.T; else if (Constantes.CT.equals(scannerString[scannerString.length - 2])) side = Constantes.CT; else if (Constantes.SPEC.equals(scannerString[scannerString.length - 2])) side = Constantes.SPEC;
                    } else if (commande.equals("commited")) {
                        commande = "suicide";
                        health = 0;
                        armor = 0;
                    } else if (commande.equals("triggered")) {
                        commande = "bomb";
                        if (Constantes.BOMBGOT.equals(scannerString[scannerString.length - 2])) bomb = Constantes.BOMBGOT; else if (Constantes.BOMBDROP.equals(scannerString[scannerString.length - 2])) bomb = Constantes.BOMBDROP; else if (Constantes.BOMBPLANTED.equals(scannerString[scannerString.length - 2])) bomb = Constantes.BOMBPLANTED; else if (Constantes.BOMBWITHOUT.equals(scannerString[scannerString.length - 2])) bomb = Constantes.BOMBWITHOUT; else if (Constantes.BOMBWITH.equals(scannerString[scannerString.length - 2])) bomb = Constantes.BOMBWITH; else if (Constantes.BOMBDEFUSED.equals(scannerString[scannerString.length - 2])) bomb = Constantes.BOMBDEFUSED;
                    } else if (commande.equals("killed") || commande.equals("attacked")) {
                        ligneSub = ligneSub.substring(ligneSub.indexOf("ed") + 4);
                        scannerString = new String[25];
                        scanner = new Scanner(ligneSub);
                        mm = 0;
                        System.out.println("************LIGNESUB**************");
                        while (scanner.hasNext()) {
                            System.out.println(scanner.next());
                            mm++;
                        }
                        scannerString = new String[mm];
                        scanner = new Scanner(ligneSub);
                        mm = 0;
                        while (scanner.hasNext()) {
                            scannerString[mm] = scanner.next();
                            mm++;
                        }
                        id = -1;
                        for (int k = 0; i < scannerString.length - 1 && id == -1; k++) {
                            if (scannerString[k].contains("><STEAM_")) {
                                id = k;
                                for (int j = 0; j <= id; j++) {
                                    if (j == id) victimName += scannerString[j].substring(0, scannerString[j].substring(0, scannerString[j].indexOf("><STEAM_")).lastIndexOf("<")); else if (j == 0) victimName += scannerString[j].substring(1) + " "; else victimName += scannerString[j] + " ";
                                }
                                ligneSub = ligneSub.substring(ligneSub.indexOf("<STEAM_"));
                                victimId = ligneSub.substring(1, ligneSub.indexOf("><"));
                                ligneSub = ligneSub.substring(ligneSub.indexOf("><") + 1);
                            }
                            if (scannerString[k].contains("><BOT><")) {
                                id = k;
                                victimName = scannerString[id].substring(0, scannerString[id].substring(0, scannerString[id].indexOf("><BOT")).lastIndexOf("<"));
                                ligneSub = ligneSub.substring(ligneSub.indexOf("<BOT"));
                                victimId = victimName;
                                ligneSub = ligneSub.substring(ligneSub.indexOf("><") + 1);
                            }
                            if (id != -1) {
                                aSide = ligneSub.substring(1, ligneSub.indexOf(">")).equals(Constantes.CT) ? Constantes.CT : Constantes.T;
                                gun = scannerString[id + 2].replaceAll("\"", "");
                                if (ligneSub.contains("headshot")) hitgroup = "headshot";
                                if (commande.equals("killed")) {
                                    health = 0;
                                    armor = 0;
                                    if (ligneSub.contains("headshot")) hitgroup = "headshot";
                                } else if (commande.equals("attacked")) {
                                    damage = Integer.valueOf(scannerString[id + 4].replaceAll("\"", "").replaceAll("\\)", ""));
                                    damageArmor = Integer.valueOf(scannerString[id + 6].replaceAll("\"", "").replaceAll("\\)", ""));
                                    health = Integer.valueOf(scannerString[id + 8].replaceAll("\"", "").replaceAll("\\)", ""));
                                    armor = Integer.valueOf(scannerString[id + 10].replaceAll("\"", "").replaceAll("\\)", ""));
                                    for (int j = id + 12; j < scannerString.length - 1; j++) {
                                        hitgroup += scannerString[j].replaceAll("\"", "").replaceAll("\\)", "");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return name;
    }

    public void setNom(String nom) {
        this.name = nom;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getVictimName() {
        return victimName;
    }

    public void setVictimName(String victimName) {
        this.victimName = victimName;
    }

    public String getVictimId() {
        return victimId;
    }

    public void setVictimId(String victimId) {
        this.victimId = victimId;
    }

    public String getASide() {
        return aSide;
    }

    public void setASide(String aSide) {
        this.aSide = aSide;
    }

    public String getGun() {
        return gun;
    }

    public void setGun(String gun) {
        this.gun = gun;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamageArmor() {
        return damageArmor;
    }

    public void setDamageArmor(int damageArmor) {
        this.damageArmor = damageArmor;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public String getHitgroup() {
        return hitgroup;
    }

    public void setHitgroup(String hitgroup) {
        this.hitgroup = hitgroup;
    }

    public String getSay() {
        return say;
    }

    public void setSay(String say) {
        this.say = say;
    }

    public String getSayTeam() {
        return sayTeam;
    }

    public void setSayTeam(String sayTeam) {
        this.sayTeam = sayTeam;
    }

    public String getCommande() {
        return commande;
    }

    public void setCommande(String commande) {
        this.commande = commande;
    }

    public String getBomb() {
        return bomb;
    }

    public void setBomb(String bomb) {
        this.bomb = bomb;
    }

    public String getWorldMessage() {
        return worldMessage;
    }

    public void setWorldMessage(String worldMessage) {
        this.worldMessage = worldMessage;
    }

    public String getCssMessage() {
        return cssMessage;
    }

    public void setCssMessage(String cssMessage) {
        this.cssMessage = cssMessage;
    }

    @Override
    public String toString() {
        return "aSide=" + aSide + "\narme=" + gun + ", armor=" + armor + "\nbomb=" + bomb + ", commande=" + commande + "\ncssMessage=" + cssMessage + ", damage=" + damage + "\ndamageArmor=" + damageArmor + ", health=" + health + "\nhitgroup=" + hitgroup + ", nom=" + name + "\nsay=" + say + ", sayTeam=" + sayTeam + "\nside=" + side + ", steamId=" + steamId + "\ntype=" + type + ", victimeId=" + victimId + "\nvictimeNom=" + victimName + ", worldMessage=" + worldMessage;
    }
}
