package net.sourceforge.scrollrack;

public class ClientProtocol {

    private Game game;

    public ClientProtocol(Game game) {
        this.game = game;
    }

    public void send_hello() {
        send("hello value:2");
    }

    public void send_player_name() {
        String name = game.player[game.local_player].name;
        send("set-player-name name:" + quote(name));
    }

    public void send_player_life() {
        int life = game.player[game.local_player].life;
        send("set-player-value key:\"life\" value:" + life);
    }

    public void send_join(String name) {
        send("join name:" + quote(name));
    }

    public void send_unjoin() {
        send("unjoin");
    }

    public void send_deck() {
        String message;
        if (game.deck == null) {
            send("set-deck cards:{}");
            send("set-sideboard cards:{}");
        } else {
            message = "set-deck cards:";
            send_card_names(message, game.deck.cardlist());
            message = "set-sideboard cards:";
            send_card_names(message, game.deck.sideboard());
        }
    }

    private void send_card_names(String message, IntList arr) {
        StringBuffer sb;
        int size, i, globalid, count;
        CardInfo info;
        sb = new StringBuffer();
        sb.append(message);
        sb.append('{');
        size = arr.size();
        i = 0;
        while (i < size) {
            globalid = arr.get(i);
            count = 1;
            while ((i + count < size) && (globalid == arr.get(i + count))) count++;
            info = game.cardbase.get(globalid);
            sb.append(" " + quote(count + " " + info.name));
            i += count;
        }
        sb.append('}');
        send(sb.toString());
    }

    public void send_shuffle(int zone) {
        send("shuffle zone:" + zone);
    }

    public void send_message(String message, int pid) {
        send("message pid:" + pid + " text:" + quote(message));
    }

    public void move_card(int cid, int pid, int zone, int x, int y, boolean loc) {
        StringBuffer buffer;
        buffer = new StringBuffer();
        buffer.append("move-card");
        buffer.append(" cid:" + cid);
        buffer.append(" pid:" + pid);
        buffer.append(" zone:" + zone);
        buffer.append(" x:" + x);
        buffer.append(" y:" + y);
        buffer.append(" loc:" + boolean_value(loc));
        send(buffer.toString());
    }

    public void send_rearrange_zone(int pid, int zone, int cid, int newidx) {
        send("rearrange-zone cid:" + cid + " value:" + newidx);
    }

    public void send_card_counters(int cid, int counters) {
        send("set-card-value cid:" + cid + " key:" + quote("Counters") + " value:" + counters);
    }

    public void send_card_flag(int cid, int flag, boolean state) {
        String key = null;
        if (flag == TableCard.TAPPED_FLAG) key = "Tapped"; else if (flag == TableCard.ATTACK_FLAG) key = "Attack"; else if (flag == TableCard.NO_UNTAP_FLAG) key = "UntapMode";
        if (key != null) {
            send("set-card-value cid:" + cid + " key:" + quote(key) + " value:" + boolean_value(state));
        }
    }

    public void send_card_controller(int cid, int pid) {
        send("set-card-value cid:" + cid + " key:" + quote("Controller") + " value:" + pid);
    }

    public void send_card_color(int cid, int color) {
        send("set-card-value cid:" + cid + " key:" + quote("Color") + " value:" + color);
    }

    public void send_ping(int pid) {
        send("send-ping pid:" + pid);
    }

    public void send_return_ping(int pid) {
        send("return-ping pid:" + pid);
    }

    public void send_reveal_request(int pid, int zone, int size) {
        send("reveal-request pid:" + pid + " zone:" + zone + " size:" + size);
    }

    public void send_show_zone(int zone, int size, int pid) {
        send("show-zone zone:" + zone + " size:" + size + " pid:" + pid);
    }

    public void send_flip_coin() {
        send("flip-coin");
    }

    public void send_roll_die(int size) {
        send("roll-die value:" + size);
    }

    public void send_show_random(int zone) {
        send("show-random zone:" + zone);
    }

    public void send_define_card(String name, String pow, int color) {
        if (pow == null) pow = "";
        send_event("define-card", name + ":" + color + ":" + pow);
    }

    public void send_add_card(String name, int znum) {
        send("add-card name:" + quote(name) + " zone:" + znum);
    }

    public void send_set_phase(int phase) {
        send_event("set-phase", phase);
    }

    public void send_next_turn() {
        send_event("next-turn", 0);
    }

    public void send_loaded_new_deck() {
        send_event("loaded-new-deck", 0);
    }

    public void send_inform_sideboard() {
        send_event("inform-sideboard", 0);
    }

    public void send_request_new_game(boolean flag) {
        send("request-new-game value:" + boolean_value(flag));
    }

    private void send_event(String event, String text) {
        send("event name:" + quote(event) + " text:" + quote(text));
    }

    private void send_event(String event, int value) {
        send("event name:" + quote(event) + " value:" + value);
    }

    public void send_peek_card(int cid) {
        send("peek-card cid:" + cid);
    }

    private String quote(String text) {
        return "\"" + text + "\"";
    }

    private int boolean_value(boolean flag) {
        return (flag ? 1 : 0);
    }

    private void send(String text) {
        game.connection.send(text);
    }

    /**
 * Take a full message (command and arguments) from the server
 * as a single line of text.  Parse and execute it.
 */
    public void process_message(String message) {
        MessageArgs args;
        Player player;
        int i, globalid, idx, color;
        String name, buf, powtuf;
        Zone zone;
        int[] pair;
        args = new MessageArgs(message);
        if (args.cmd.equals("hello")) {
            if (game.num_players > 1) {
                game.display_message("ERROR: Resetting local player number.");
            }
            send_hello();
            send_player_name();
            return;
        }
        if (args.cmd.equals("join")) {
            game.state = Game.GAME_CLIENT;
            player = game.player[game.local_player];
            player.set_flag(Player.CONNECTED_FLAG);
            if (game.local_player != args.pid) {
                game.num_players = args.pid;
                game.local_player = args.pid;
                game.player = new Player[args.pid + 1];
                game.player[0] = null;
                for (i = 1; i < args.pid; i++) {
                    game.player[i] = new Player(null);
                }
                game.player[args.pid] = player;
            }
            return;
        }
        if (args.cmd.equals("player-connect")) {
            if (args.pid > game.num_players) {
                Player[] parr = new Player[args.pid + 1];
                for (i = 1; i <= game.num_players; i++) parr[i] = game.player[i];
                for (i = game.num_players + 1; i <= args.pid; i++) parr[i] = new Player(null);
                game.player = parr;
                game.num_players = args.pid;
                name = "Player " + args.pid;
            } else {
                name = game.player[args.pid].name;
            }
            player = game.player[args.pid];
            if (args.value != 0) {
                player.set_flag(Player.CONNECTED_FLAG);
            } else {
                player.unset_flag(Player.CONNECTED_FLAG);
            }
            game.display_message(name + " has " + (args.value != 0 ? "" : "dis") + "connected.");
            return;
        }
        if (args.cmd.equals("set-player-name")) {
            game.set_player_name(args.pid, args.name);
            return;
        }
        if (args.cmd.equals("set-player-value")) {
            if (args.text.equals("life")) {
                game.set_player_life(args.pid, args.value);
            }
            return;
        }
        if (args.cmd.equals("set-deck")) {
            player = game.player[args.pid];
            if (args.value > 0) {
                player.set_flag(Player.WILL_PLAY_FLAG);
                game.display_message(player.name + " has submitted a new " + args.value + " card deck.");
            } else {
                player.unset_flag(Player.WILL_PLAY_FLAG);
                game.display_message(player.name + " will watch the next game.");
            }
            return;
        }
        if (args.cmd.equals("set-sideboard")) {
            player = game.player[args.pid];
            game.display_message(player.name + " has a " + args.value + " card sideboard.");
            return;
        }
        if (args.cmd.equals("set-zone")) {
            zone = game.player[args.pid].zone[args.zone];
            zone.clear();
            if (args.cids != null) for (i = 0; i < args.cids.length; i++) zone.add(args.cids[i], false);
            if ((args.pid == game.local_player) && (args.zone == Game.SIDEBOARD_ZONE)) initialize_sideboard(args.cids);
            return;
        }
        if (args.cmd.equals("shuffle")) {
            player = game.player[args.pid];
            game.display_message(player.name + " is shuffling " + Game.name_of_zone(args.zone) + ".");
            zone = player.zone[args.zone];
            zone_close_dialog(zone);
            for (i = 0; i < zone.size(); i++) {
                idx = zone.get(i);
                if (game.cidarr.size() > idx) game.cidarr.set(idx, 0);
            }
            return;
        }
        if (args.cmd.equals("message")) {
            if (args.text != null) game.display_message(game.player[args.pid].name + " says: '" + args.text + "'");
            return;
        }
        if (args.cmd.equals("reveal")) {
            globalid = game.cardbase.find_globalid(args.name);
            while (game.cidarr.size() <= args.cid) game.cidarr.add(0);
            game.cidarr.set(args.cid, globalid);
            return;
        }
        if (args.cmd.equals("move-card")) {
            remote_move_card(args.cid, args.pid, args.zone, args.x, args.y, (args.value != 0), args.who);
            return;
        }
        if (args.cmd.equals("rearrange-zone")) {
            zone = game.player[args.pid].zone[args.zone];
            idx = zone.find_cid(args.cid);
            if ((idx < 0) || (args.value < 0) || (args.value >= zone.size())) return;
            game.rearrange_zone(args.who, args.pid, args.zone, idx, args.value);
            return;
        }
        if (args.cmd.equals("set-card-value")) {
            remote_set_card_value(args.cid, args.text, args.value, args.pid);
            return;
        }
        if (args.cmd.equals("send-ping")) {
            send_return_ping(args.pid);
            return;
        }
        if (args.cmd.equals("return-ping")) {
            if (args.pid == 0) args.pid = game.local_player;
            process_return_ping(args.pid);
            return;
        }
        if (args.cmd.equals("show-zone")) {
            player = game.player[args.who];
            buf = "";
            if ((args.value > 0) && (args.value < player.zone[args.zone].size())) buf = "top " + args.value + " cards of ";
            buf += Game.name_of_zone(args.zone);
            name = (args.pid == 0 ? "everyone" : args.pid == args.who ? "self" : game.player[args.pid].name);
            game.display_message(player.name + " reveals " + buf + " to " + name + ".");
            if ((args.who == game.local_player) && (args.zone == Game.HAND_ZONE)) return;
            if ((args.pid == 0) || (args.pid == game.local_player)) game.view_zone(args.who, args.zone, args.value);
            return;
        }
        if (args.cmd.equals("flip-coin")) {
            game.flip_coin(args.pid, args.value);
            return;
        }
        if (args.cmd.equals("roll-die")) {
            game.roll_die(args.pid, args.value, args.x);
            return;
        }
        if (args.cmd.equals("show-random")) {
            pair = new int[2];
            idx = game.find_card(args.cid, pair);
            if ((idx >= 0) && (pair[1] == Game.HAND_ZONE)) game.show_random(pair[0], args.cid);
            return;
        }
        if (args.cmd.equals("add-card")) {
            player = game.player[args.pid];
            name = game.get_card_name(args.cid);
            if (name == null) name = "Unknown Card";
            game.display_message(player.name + " creates new card: " + name + ".");
            zone = player.zone[args.zone];
            zone.add(args.cid, false);
            return;
        }
        if (args.cmd.equals("request-new-game")) {
            player = game.player[args.pid];
            if (args.value != 0) {
                game.display_message(player.name + " is requesting a new game.");
                player.set_flag(Player.WANTS_NEW_FLAG);
            } else {
                game.display_message(player.name + " is not ready for a new game.");
                player.unset_flag(Player.WANTS_NEW_FLAG);
            }
            return;
        }
        if (args.cmd.equals("reset-game")) {
            game.start_new_game();
            return;
        }
        if (args.cmd.equals("event")) {
            if (args.name.equals("define-card")) {
                name = args.text;
                if (name == null) return;
                powtuf = null;
                color = 0;
                idx = name.indexOf(':');
                if (idx >= 0) {
                    powtuf = name.substring(idx + 1);
                    name = name.substring(0, idx);
                    idx = powtuf.indexOf(':');
                    if (idx >= 0) {
                        buf = powtuf.substring(idx + 1);
                        powtuf = powtuf.substring(0, idx);
                        color = 0;
                        try {
                            color = Integer.parseInt(buf);
                        } catch (Exception exception) {
                        }
                    }
                }
                game.cardbase.add_card(name, powtuf, color);
                return;
            }
            if (args.name.equals("set-phase")) {
                if ((args.value >= 0) && (args.value <= Game.NUM_PHASES)) game.set_phase(args.value);
                return;
            }
            if (args.name.equals("next-turn")) {
                game.next_turn();
                return;
            }
            if (args.name.equals("loaded-new-deck")) {
                name = game.player[args.pid].name;
                game.display_message(name + " has loaded a new deck for the " + "next game.");
                return;
            }
            if (args.name.equals("inform-sideboard")) {
                name = game.player[args.pid].name;
                game.display_message(name + " is modifying deck for " + "the next game.");
                return;
            }
        }
        if (args.cmd.equals("peek-card")) {
            name = game.get_card_name(args.cid);
            if (name == null) name = "card " + args.cid;
            if (args.pid == game.local_player) {
                game.display_message("Face-Down Card is " + name + ".");
            } else {
                game.display_message(game.player[args.pid].name + " peeks at " + name + ".");
            }
            return;
        }
    }

    private void zone_close_dialog(Zone zone) {
        ZoneTable table = zone.get_table();
        if (table != null) {
            ZoneDialog dialog = table.get_dialog();
            if (dialog != null) dialog.close();
        }
    }

    /**
 * Used by the remote player to move a card from any zone or playarea
 * to any zone or playarea.
 */
    private boolean remote_move_card(int cid, int dstpid, int dstznum, int x, int y, boolean bottom, int who) {
        int[] pair;
        int idx, srcpid, srcznum;
        boolean revealed, state;
        TableCard tcard;
        pair = new int[2];
        idx = game.find_card(cid, pair);
        srcpid = pair[0];
        srcznum = pair[1];
        tcard = null;
        if (idx < 0) {
            tcard = game.playarea.get_table_card(cid);
            if (tcard == null) return (false);
        }
        revealed = false;
        if (dstpid < game.min_player() || dstpid > game.max_player()) return (false);
        if (dstznum == Game.PLAYAREA_ZONE) {
            if (tcard != null) {
                if (revealed) game.display_message(game.player[who].name + " reveals face-down: " + game.get_card_name(cid));
                state = ((tcard.flags & TableCard.FLIPPED_FLAG) != 0);
                if ((state && !bottom) || (!state && bottom)) game.set_table_card_flag(tcard, TableCard.FLIPPED_FLAG, bottom, who);
                game.playarea.move_table_card(cid, x, y);
            } else {
                game.play_card(srcpid, srcznum, idx, who, x, y, bottom);
            }
            return (true);
        }
        if ((dstznum < 1) || (dstznum > Game.NUM_ZONES)) return (false);
        if (tcard != null) {
            game.unplay_card(cid, dstpid, dstznum, who, bottom);
        } else {
            if (srcpid != dstpid) return (false);
            game.move_card(srcpid, srcznum, idx, dstznum, bottom);
        }
        return (true);
    }

    /**
 * Used by the remote player to associate a value with a card in
 * a playarea.
 */
    private void remote_set_card_value(int cid, String key, int value, int pid) {
        TableCard tcard;
        int flag;
        tcard = game.playarea.get_table_card(cid);
        if ((tcard == null) || (key == null)) return;
        flag = 0;
        if (key.equalsIgnoreCase("Counters")) {
            game.set_card_counters(tcard, value);
        } else if (key.equalsIgnoreCase("Tapped")) {
            flag = TableCard.TAPPED_FLAG;
        } else if (key.equalsIgnoreCase("Attack")) {
            flag = TableCard.ATTACK_FLAG;
        } else if (key.equalsIgnoreCase("UntapMode")) {
            flag = TableCard.NO_UNTAP_FLAG;
        } else if (key.equalsIgnoreCase("Controller")) {
            game.set_card_controller(tcard, value);
        } else if (key.equalsIgnoreCase("Color")) {
            game.set_table_card_color(tcard, value);
        }
        if (flag != 0) {
            game.set_table_card_flag(tcard, flag, (value != 0), pid);
        }
    }

    /**
 * Tell the local player that a remote player has returned a ping.
 */
    private void process_return_ping(int pid) {
        Player player;
        String name;
        int secs;
        player = game.player[pid];
        if (player.last_ping == 0) return;
        name = (pid == game.local_player ? "Server" : player.name);
        secs = (int) ((System.currentTimeMillis() - player.last_ping + 500) / 1000);
        game.display_message(name + " returned ping in " + secs + " seconds.");
        player.last_ping = 0;
    }

    /**
 * The server does not shuffle sideboards.
 * So we know what these cids represent.
 */
    private void initialize_sideboard(int[] cids) {
        IntList globalids = game.deck.sideboard();
        int size1 = (cids == null ? 0 : cids.length);
        int size2 = (globalids == null ? 0 : globalids.size());
        if (size1 != size2) {
            game.display_message("Local sideboard has wrong number of cards.");
            return;
        }
        for (int iii = 0; iii < size1; iii++) {
            while (game.cidarr.size() <= cids[iii]) game.cidarr.add(0);
            game.cidarr.set(cids[iii], globalids.get(iii));
        }
    }
}
