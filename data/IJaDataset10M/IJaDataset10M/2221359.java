package jmud.command;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import jmud.Char;
import jmud.Door;
import jmud.Global;
import jmud.Keywords;
import jmud.util.StrUtil;
import jmud.util.file.LineReader;
import jmud.util.log.Log;
import jgp.container.Vector;
import jgp.adaptor.Copy;
import jgp.adaptor.Enumerator;
import jgp.adaptor.BufferedReaderEnumeration;

public class CommandTable {

    static final int PLR = Command.OPT_PLR;

    static final int CRT = Command.OPT_CRT;

    static final int BOTH = PLR | CRT;

    static final Command theCommands[] = { new Ability(".", Char.R_USER, Char.P_DEAD, BOTH), new AddMagic("+" + Keywords.SPELL, Char.R_ADMIN, Char.P_DEAD, BOTH), new SubMagic("-" + Keywords.SPELL, Char.R_ADMIN, Char.P_DEAD, BOTH), new AddJob("+" + Keywords.JOB, Char.R_ADMIN, Char.P_DEAD, BOTH), new SubJob("-" + Keywords.JOB, Char.R_ADMIN, Char.P_DEAD, BOTH), new AddAbility("+" + Keywords.ABILITY, Char.R_ADMIN, Char.P_DEAD, BOTH), new SubAbility("-" + Keywords.ABILITY, Char.R_ADMIN, Char.P_DEAD, BOTH), new Move(Door.directions[Door.D_NORTH], Char.R_GUEST, Char.P_STANDING, BOTH, Door.D_NORTH), new Move(Door.directions[Door.D_SOUTH], Char.R_GUEST, Char.P_STANDING, BOTH, Door.D_SOUTH), new Move(Door.directions[Door.D_EAST], Char.R_GUEST, Char.P_STANDING, BOTH, Door.D_EAST), new Move(Door.directions[Door.D_WEST], Char.R_GUEST, Char.P_STANDING, BOTH, Door.D_WEST), new Move(Door.directions[Door.D_UP], Char.R_GUEST, Char.P_STANDING, BOTH, Door.D_UP), new Move(Door.directions[Door.D_DOWN], Char.R_GUEST, Char.P_STANDING, BOTH, Door.D_DOWN), new Open("abra", Char.R_USER, Char.P_SITTING, BOTH), new Wake("acorde", Char.R_USER, Char.P_ASLEEP, BOTH), new Group("agrupe", Char.R_USER, Char.P_SITTING, BOTH), new Help("ajuda", Char.R_DISABLED, Char.P_DEAD, BOTH), new Annotate(Keywords.ANNOTATE, Char.R_GUEST, Char.P_SITTING, BOTH), new Erase("apague", Char.R_USER, Char.P_SITTING, BOTH), new Learn(Keywords.LEARN, Char.R_USER, Char.P_SITTING, BOTH), new SpeedWalk(Keywords.SHORTCUT, Char.R_ADMIN, Char.P_DEAD, BOTH), new Attack("ataque", Char.R_USER, Char.P_STANDING, BOTH), new SetAttr("atribua", Char.R_ADMIN, Char.P_DEAD, BOTH), new Reset("automatize", Char.R_ADMIN, Char.P_DEAD, BOTH), new Cache(Keywords.CACHE, Char.R_ADMIN, Char.P_DEAD, BOTH, Keywords.CACHE), new Hunt(Keywords.HUNT, Char.R_USER, Char.P_STANDING, BOTH), new Commands("comandos", Char.R_DISABLED, Char.P_DEAD, BOTH), new Buy("compre", Char.R_USER, Char.P_SITTING, BOTH), new CopyCommand("copie", Char.R_GUEST, Char.P_SITTING, BOTH), new ColorCommand("cores", Char.R_USER, Char.P_DEAD, BOTH), new Create("crie", Char.R_ADMIN, Char.P_DEAD, BOTH), new Heal("cure", Char.R_ADMIN, Char.P_DEAD, BOTH), new Say("diga", Char.R_DISABLED, Char.P_SITTING, BOTH), new Description("descricao", Char.R_USER, Char.P_DEAD, BOTH), new Shutdown("desative", Char.R_ADMIN, Char.P_DEAD, BOTH), new Rest("descanse", Char.R_USER, Char.P_RESTING, BOTH), new Disconnect("desconecte", Char.R_ADMIN, Char.P_DEAD, BOTH), new Unlock("destranque", Char.R_USER, Char.P_SITTING, BOTH), new Destroy("destrua", Char.R_ADMIN, Char.P_DEAD, BOTH), new Donate("doe", Char.R_USER, Char.P_STANDING, BOTH), new Gifts(Keywords.GIFTS, Char.R_ADMIN, Char.P_DEAD, BOTH), new Sleep("durma", Char.R_USER, Char.P_ASLEEP, BOTH), new Echo("ecoe", Char.R_ADMIN, Char.P_DEAD, BOTH), new Effects(Keywords.EFFECTS, Char.R_ADMIN, Char.P_DEAD, BOTH, Keywords.EFFECTS + StrUtil.optEnclose(StrUtil.rplEnclose(Keywords.CHAR))), new Emote("encene", Char.R_USER, Char.P_STANDING, BOTH), new Write("escreva", Char.R_USER, Char.P_SITTING, BOTH), new Give("entregue", Char.R_USER, Char.P_SITTING, BOTH), new Equip("equipe", Char.R_USER, Char.P_STANDING, BOTH), new Score("escore", Char.R_GUEST, Char.P_DEAD, BOTH), new Delete("exclua", Char.R_DISABLED, Char.P_DEAD, BOTH), new Close("feche", Char.R_USER, Char.P_SITTING, BOTH), new Sheet("ficha", Char.R_ADMIN, Char.P_DEAD, BOTH), new Force("force", Char.R_ADMIN, Char.P_DEAD, BOTH), new Flee("fuja", Char.R_USER, Char.P_FIGHTING, BOTH), new Funds("fundos", Char.R_ADMIN, Char.P_DEAD, BOTH), new Grimoire(Keywords.GRIMOIRE, Char.R_ADMIN, Char.P_DEAD, BOTH, Keywords.GRIMOIRE), new Abilities(Keywords.ABILITIES, Char.R_USER, Char.P_DEAD, BOTH), new Inventory(Keywords.INVENTORY, Char.R_USER, Char.P_SITTING, BOTH, Keywords.INVENTORY + StrUtil.optEnclose(StrUtil.or(StrUtil.rplEnclose(Keywords.CHAR), StrUtil.rplEnclose(Keywords.PLAYER) + StrUtil.optEnclose(Keywords.FILE)))), new Index("indice", Char.R_ADMIN, Char.P_DEAD, BOTH), new NewbieLock("iniciantes", Char.R_ADMIN, Char.P_DEAD, PLR), new Invisibility("invisibilidade", Char.R_ADMIN, Char.P_DEAD, BOTH), new Cast("lance", Char.R_USER, Char.P_SITTING, BOTH), new Read("leia", Char.R_GUEST, Char.P_SITTING, BOTH), new Stand("levante", Char.R_USER, Char.P_RESTING, BOTH), new List("liste", Char.R_USER, Char.P_SITTING, BOTH), new LogCommand("log", Char.R_ADMIN, Char.P_DEAD, PLR), new ShopCommand("loja", Char.R_ADMIN, Char.P_DEAD, BOTH), new Spells(Keywords.SPELLS, Char.R_USER, Char.P_RESTING, BOTH, Keywords.SPELLS + " <" + Keywords.PLAYER + "> [" + Keywords.FILE + "]"), new Hosts(Keywords.HOSTS, Char.R_ADMIN, Char.P_DEAD, BOTH, Keywords.HOSTS + " <" + Keywords.PLAYER + "> [" + Keywords.FILE + "]"), new MasterCommand(Keywords.MASTER, Char.R_ADMIN, Char.P_DEAD, BOTH), new Moderator("moderador", Char.R_DISABLED, Char.P_DISABLED, BOTH), new Look("olhe", Char.R_GUEST, Char.P_SITTING, BOTH), new Where("onde", Char.R_ADMIN, Char.P_DEAD, BOTH), new Omni("onisciencia", Char.R_ADMIN, Char.P_DEAD, BOTH), new Get("pegue", Char.R_USER, Char.P_SITTING, BOTH), new Put("ponha", Char.R_USER, Char.P_SITTING, BOTH), new Price("preco", Char.R_USER, Char.P_SITTING, BOTH), new Tell("privado", Char.R_USER, Char.P_SITTING, BOTH), new Priviledges("privilegios", Char.R_ADMIN, Char.P_DEAD, BOTH), new Jobs(Keywords.JOBS, Char.R_ADMIN, Char.P_DEAD, BOTH), new Promote("promova", Char.R_ADMIN, Char.P_DEAD, BOTH), new Prototypes("prototipos", Char.R_ADMIN, Char.P_DEAD, BOTH), new Gossip("publique", Char.R_USER, Char.P_SITTING, BOTH), new Who("quem", Char.R_GUEST, Char.P_SITTING, BOTH), new Region(Keywords.REGION, Char.R_ADMIN, Char.P_DEAD, BOTH), new Remove("remova", Char.R_USER, Char.P_STANDING, BOTH), new Repeat("repita", Char.R_USER, Char.P_DEAD, BOTH), new Rooms(Keywords.ROOMS, Char.R_ADMIN, Char.P_DEAD, BOTH, StrUtil.or(Keywords.ROOMS + StrUtil.rplEnclose(Keywords.NAME), StrUtil.grpEnclose(StrUtil.rplEnclose(Keywords.NUMBER) + StrUtil.optEnclose(Keywords.NUMBER + "2")))), new Save("salve", Char.R_USER, Char.P_DEAD, BOTH), new Sit("sente", Char.R_USER, Char.P_RESTING, BOTH), new Sessions("sessoes", Char.R_ADMIN, Char.P_DEAD, BOTH), new Follow("siga", Char.R_USER, Char.P_STANDING, BOTH), new Syntax(Keywords.SYNTAX, Char.R_USER, Char.P_DEAD, BOTH, Keywords.SYNTAX + " <" + Keywords.COMMAND + ">"), new Drop("solte", Char.R_USER, Char.P_SITTING, BOTH), new Time("tempo", Char.R_ADMIN, Char.P_DEAD, BOTH), new Bring("traga", Char.R_ADMIN, Char.P_DEAD, BOTH), new Lock("tranque", Char.R_USER, Char.P_SITTING, BOTH), new Transfer("transfira", Char.R_ADMIN, Char.P_DEAD, BOTH), new Users("usuarios", Char.R_ADMIN, Char.P_DEAD, BOTH), new Go("va", Char.R_ADMIN, Char.P_DEAD, BOTH), new Sell("venda", Char.R_USER, Char.P_SITTING, BOTH), new Version("versao", Char.R_DISABLED, Char.P_DEAD, BOTH), new Zones("zonas", Char.R_ADMIN, Char.P_DEAD, BOTH), new Quit("fim", Char.R_DISABLED, Char.P_RESTING, BOTH) };

    public static Command findCommandByName(Char ch, String name) {
        for (int i = 0; i < theCommands.length; ++i) {
            Command comm = theCommands[i];
            if (comm.hasName(name) && ch.canExecute(comm)) return comm;
        }
        return null;
    }

    static void loadSyntax(FileReader file) {
        Vector lines = new Vector();
        try {
            Copy.all(lines, new BufferedReaderEnumeration(new LineReader(file)));
        } catch (IOException e) {
            Log.err("Falha de E/S ao ler arquivo de sintaxe: " + Global.getConfig().getSyntaxFileName());
            return;
        }
        for (Enumeration cmdEnum = new Enumerator(theCommands); cmdEnum.hasMoreElements(); ) {
            Command cmd = (Command) cmdEnum.nextElement();
            for (Enumeration linEnum = lines.elements(); linEnum.hasMoreElements(); ) {
                String line = (String) linEnum.nextElement();
                StringTokenizer tok = new StringTokenizer(line);
                if (tok.hasMoreTokens()) {
                    String word = tok.nextToken();
                    if (cmd.hasName(word)) {
                        if (cmd.getSyntax() != null) Log.warn("Redefinindo sintaxe do comando: '" + cmd.getName() + "'.");
                        cmd.setSyntax(line);
                        break;
                    }
                }
            }
        }
    }

    static {
        String fileName = Global.getConfig().getSyntaxFileName();
        if (fileName == null) {
            Log.warn("Arquivo de sintaxe n�o definido na configura��o");
        } else {
            Log.info("Carregando tabela de sintaxe: " + fileName);
            try {
                FileReader fr = new FileReader(fileName);
                loadSyntax(fr);
                fr.close();
            } catch (FileNotFoundException e) {
                Log.warn("Arquivo de sintaxe n�o encontrado: " + fileName);
            } catch (IOException e) {
                Log.err("Falha de E/S: " + fileName);
            }
        }
    }
}
