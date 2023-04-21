package server;

import port.util;
import server.gamelog.EGamelog;
import utility.Log;
import utility.Rand;
import utility.shared.Shared_H;
import ai.Aitools;
import common.City;
import common.Game;
import common.Government;
import common.Map;
import common.Terrain_H;
import common.Unit;
import common.Unittype_P;
import common.event_type;
import common.city.city;
import common.map.tile;
import common.player.Player_H;
import common.player.diplstate_type;
import common.player.player;
import common.player.player_ai;
import common.terrian.terrain_flag_id;
import common.unit.unit;
import common.unittype.unit_role_id;

public class Barbarian {

    public static final int MIN_UNREST_DIST = 5;

    public static final int MAX_UNREST_DIST = 8;

    public static final int UPRISE_CIV_SIZE = 10;

    public static final int UPRISE_CIV_MORE = 30;

    public static final int UPRISE_CIV_MOST = 50;

    public static final int MAP_FACTOR = 2000;

    /**************************************************************************
	 * Is player a land barbarian?
	 **************************************************************************/
    static boolean is_land_barbarian(player pplayer) {
        return (pplayer.ai.barbarian_type == player_ai.LAND_BARBARIAN);
    }

    /**************************************************************************
	 * Is player a sea barbarian?
	 **************************************************************************/
    static boolean is_sea_barbarian(player pplayer) {
        return (pplayer.ai.barbarian_type == player_ai.SEA_BARBARIAN);
    }

    /**************************************************************************
	 * Creates the land/sea barbarian player and inits some stuff. If barbarian
	 * player already exists, return player pointer. If barbarians are dead,
	 * revive them with a new leader :-)
	 * 
	 * Dead barbarians forget the map and lose the money.
	 **************************************************************************/
    static player create_barbarian_player(boolean land) {
        int newplayer = Game.game.nplayers;
        for (player barbarians : Game.game.players) {
            if ((land && is_land_barbarian(barbarians)) || (!land && is_sea_barbarian(barbarians))) {
                if (!barbarians.is_alive) {
                    barbarians.economic.gold = 0;
                    barbarians.is_alive = true;
                    barbarians.is_dying = false;
                    Srv_main.pick_ai_player_name(Game.game.nation_count - 1, barbarians.name);
                    barbarians.username = Player_H.ANON_USER_NAME;
                    for (tile ptile : Map.map.tiles) {
                        Maphand.map_clear_known(ptile, barbarians);
                    }
                }
                barbarians.economic.gold += 100;
                return barbarians;
            }
        }
        if (newplayer >= Shared_H.MAX_NUM_PLAYERS + Shared_H.MAX_NUM_BARBARIANS) {
            util.die("Too many players in server/barbarian.c");
        }
        player barbarians;
        barbarians = Game.game.players[newplayer];
        Plrhand.server_player_init(barbarians, true);
        barbarians.nation = Game.game.nation_count - 1;
        Srv_main.pick_ai_player_name(Game.game.nation_count - 1, barbarians.name);
        Game.game.nplayers++;
        Game.game.nbarbarians++;
        Game.game.max_players = Game.game.nplayers;
        barbarians.username = Player_H.ANON_USER_NAME;
        barbarians.is_connected = false;
        barbarians.government = Game.game.default_government;
        barbarians.target_government = Game.game.default_government;
        assert (barbarians.revolution_finishes < 0);
        barbarians.capital = false;
        barbarians.economic.gold = 100;
        barbarians.turn_done = true;
        barbarians.ai.control = true;
        if (land) {
            barbarians.ai.barbarian_type = player_ai.LAND_BARBARIAN;
        } else {
            barbarians.ai.barbarian_type = player_ai.SEA_BARBARIAN;
        }
        Stdinhand.set_ai_level_directer(barbarians, Game.game.skill_level);
        Plrhand.init_tech(barbarians);
        Plrhand.give_initial_techs(barbarians);
        for (player pplayer : Game.game.players) {
            if (pplayer != barbarians) {
                pplayer.diplstates[barbarians.player_no].type = diplstate_type.DS_WAR;
                barbarians.diplstates[pplayer.player_no].type = diplstate_type.DS_WAR;
            }
        }
        util.freelog(Log.LOG_VERBOSE, "Created barbarian %s, player %d", barbarians.name, barbarians.player_no);
        Plrhand.notify_player_ex(null, null, event_type.E_UPRISING, ("Barbarians gain a leader by the name %s.  Dangerous " + "times may lie ahead."), barbarians.name);
        Gamelog.gamelog(EGamelog.GAMELOG_PLAYER, barbarians);
        Gamehand.send_game_info(null);
        Plrhand.send_player_info(barbarians, null);
        return barbarians;
    }

    /**************************************************************************
	 * Check if a tile is land and free of enemy units
	 **************************************************************************/
    static boolean is_free_land(tile ptile, player who) {
        return (!Terrain_H.is_ocean(ptile.terrain) && null == Unit.is_non_allied_unit_tile((ptile), who));
    }

    /**************************************************************************
	 * Check if a tile is sea and free of enemy units
	 **************************************************************************/
    static boolean is_free_sea(tile ptile, player who) {
        return (Terrain_H.is_ocean(ptile.terrain) && null == Unit.is_non_allied_unit_tile((ptile), who));
    }

    /**************************************************************************
	 * Unleash barbarians means give barbarian player some units and move them
	 * out of the hut, unless there's no place to go.
	 * 
	 * Barbarian unit deployment algorithm: If enough free land around, deploy
	 * on land, if not enough land but some sea free, load some of them on
	 * boats, otherwise (not much land and no sea) kill enemy unit and stay in a
	 * village. The return value indicates if the explorer survived entering the
	 * vilage.
	 **************************************************************************/
    boolean unleash_barbarians(tile ptile) {
        player barbarians;
        int unit, unit_cnt, land_cnt = 0, sea_cnt = 0;
        int boat;
        int i, me;
        tile utile = null;
        boolean alive = true;
        if (Game.game.barbarianrate == 0 || (Game.game.year < Game.game.onsetbarbarian)) {
            for (unit punit : (ptile).units.data) {
                Unittools.wipe_unit(punit);
            }
            return false;
        }
        unit_cnt = 3 + Rand.myrand(4);
        barbarians = create_barbarian_player(true);
        me = barbarians.player_no;
        for (i = 0; i < unit_cnt; i++) {
            unit = Unittools.find_a_unit_type(unit_role_id.L_BARBARIAN.ordinal(), unit_role_id.L_BARBARIAN_TECH.ordinal());
            Unittools.create_unit(barbarians, ptile, unit, 0, 0, -1);
            util.freelog(Log.LOG_DEBUG, "Created barbarian unit %s", Unittype_P.unit_types[unit].name);
        }
        for (tile tile1 : util.adjc_tile_iterate(ptile)) {
            land_cnt += is_free_land(tile1, barbarians) ? 1 : 0;
            sea_cnt += is_free_sea(tile1, barbarians) ? 1 : 0;
        }
        if (land_cnt >= 3) {
            for (unit punit2 : (ptile).units.data) {
                if (punit2.owner == me) {
                    Unittools.send_unit_info(null, punit2);
                    do {
                        do {
                            utile = Map.rand_neighbour(ptile);
                        } while (!is_free_land(utile, barbarians));
                    } while (!Unithand.handle_unit_move_request(punit2, utile, true, false));
                    util.freelog(Log.LOG_DEBUG, "Moved barbarian unit from %d %d to %d, %d", ptile.x, ptile.y, utile.x, utile.y);
                }
            }
        } else {
            if (sea_cnt > 0) {
                tile btile = null;
                for (unit punit2 : (ptile).units.data) {
                    if (punit2.owner == me) {
                        Unittools.send_unit_info(null, punit2);
                        while (true) {
                            utile = Map.rand_neighbour(ptile);
                            if (Unit.can_unit_move_to_tile(punit2, utile, true)) {
                                break;
                            }
                            if (btile != null && Unit.can_unit_move_to_tile(punit2, btile, true)) {
                                utile = btile;
                                break;
                            }
                            if (is_free_sea(utile, barbarians)) {
                                boat = Unittools.find_a_unit_type(unit_role_id.L_BARBARIAN_BOAT.ordinal(), -1);
                                Unittools.create_unit(barbarians, utile, boat, 0, 0, -1);
                                btile = utile;
                                break;
                            }
                        }
                        Unithand.handle_unit_move_request(punit2, utile, true, false);
                    }
                }
            } else {
                for (unit punit2 : (ptile).units.data) {
                    if (punit2.owner != me) {
                        Unittools.wipe_unit(punit2);
                        alive = false;
                    } else {
                        Unittools.send_unit_info(null, punit2);
                    }
                }
            }
        }
        if (utile != null) {
            Maphand.show_area(barbarians, utile, 3);
        }
        return alive;
    }

    /**************************************************************************
	 * Is sea not further than a couple of tiles away from land?
	 **************************************************************************/
    static boolean is_near_land(tile tile0) {
        for (tile ptile : util.square_tile_iterate(tile0, 4)) {
            if (!Terrain_H.is_ocean(ptile.terrain)) {
                return true;
            }
        }
        return false;
    }

    /**************************************************************************
	 * Return this or a neighbouring tile that is free of any units
	 **************************************************************************/
    static tile find_empty_tile_nearby(tile ptile) {
        for (tile tile1 : util.square_tile_iterate(ptile, 1)) {
            if ((tile1).units.foo_list_size() == 0) {
                return tile1;
            }
        }
        return null;
    }

    /**************************************************************************
	 * The barbarians are summoned at a randomly chosen place if: 1. It's not
	 * closer than MIN_UNREST_DIST and not further than MAX_UNREST_DIST from the
	 * nearest city. City owner is called 'victim' here. 2. The place or a
	 * neighbouring tile must be empty to deploy the units. 3. If it's the sea
	 * it shouldn't be far from the land. (questionable) 4. Place must be known
	 * to the victim 5. The uprising chance depends also on the victim empire
	 * size, its government (civil_war_chance) and barbarian difficulty level.
	 * 6. The number of land units also depends slightly on victim's empire size
	 * and barbarian difficulty level. Q: The empire size is used so there are
	 * no uprisings in the beginning of the Game.game (year is not good as it
	 * can be customized), but it seems a bit unjust if someone is always small.
	 * So maybe it should rather be an average number of cities (all
	 * cities/player num)? Depending on the victim government type is also
	 * questionable.
	 **************************************************************************/
    static void try_summon_barbarians() {
        tile ptile, utile;
        int i, boat, cap, dist, unit;
        int uprise = 1;
        city pc;
        player barbarians, victim;
        ptile = Map.rand_map_pos();
        if (Terrain_H.terrain_has_flag(ptile.terrain, terrain_flag_id.TER_NO_BARBS)) {
            return;
        }
        if (null == (pc = Aitools.dist_nearest_city(null, ptile, true, false))) {
            return;
        }
        victim = City.city_owner(pc);
        dist = Map.real_map_distance(ptile, pc.tile);
        util.freelog(Log.LOG_DEBUG, "Closest city to %d %d is %s at %d %d which is %d far", ptile.x, ptile.y, pc.name, pc.tile.x, pc.tile.y, dist);
        if (dist > MAX_UNREST_DIST || dist < MIN_UNREST_DIST) {
            return;
        }
        if (null == (utile = find_empty_tile_nearby(ptile)) || (!Maphand.map_is_known(utile, victim) && !Terrain_H.is_ocean(utile.terrain)) || !is_near_land(utile)) {
            return;
        }
        if ((int) Rand.myrand(UPRISE_CIV_MORE) > (int) victim.cities.foo_list_size() - UPRISE_CIV_SIZE / (Game.game.barbarianrate - 1) || Rand.myrand(100) > Government.get_gov_pcity(pc).civil_war) {
            return;
        }
        util.freelog(Log.LOG_DEBUG, "Barbarians are willing to fight");
        if (Map.map_has_special(utile, Terrain_H.S_HUT)) {
            Map.map_clear_special(utile, Terrain_H.S_HUT);
            Maphand.update_tile_knowledge(utile);
        }
        if (!Terrain_H.is_ocean(utile.terrain)) {
            barbarians = create_barbarian_player(true);
            if (victim.cities.foo_list_size() > UPRISE_CIV_MOST) {
                uprise = 3;
            }
            for (i = 0; i < Rand.myrand(3) + uprise * Game.game.barbarianrate; i++) {
                unit = Unittools.find_a_unit_type(unit_role_id.L_BARBARIAN.ordinal(), unit_role_id.L_BARBARIAN_TECH.ordinal());
                Unittools.create_unit(barbarians, utile, unit, 0, 0, -1);
                util.freelog(Log.LOG_DEBUG, "Created barbarian unit %s", Unittype_P.unit_types[unit].name);
            }
            Unittools.create_unit(barbarians, utile, Unittype_P.get_role_unit(unit_role_id.L_BARBARIAN_LEADER.ordinal(), 0), 0, 0, -1);
        } else {
            unit ptrans;
            barbarians = create_barbarian_player(false);
            boat = Unittools.find_a_unit_type(unit_role_id.L_BARBARIAN_BOAT.ordinal(), -1);
            ptrans = Unittools.create_unit(barbarians, utile, boat, 0, 0, -1);
            cap = Unit.get_transporter_capacity(utile.units.foo_list_get(0));
            for (i = 0; i < cap - 1; i++) {
                unit = Unittools.find_a_unit_type(unit_role_id.L_BARBARIAN_SEA.ordinal(), unit_role_id.L_BARBARIAN_SEA_TECH.ordinal());
                Unittools.create_unit_full(barbarians, utile, unit, 0, 0, -1, -1, ptrans);
                util.freelog(Log.LOG_DEBUG, "Created barbarian unit %s", Unittype_P.unit_types[unit].name);
            }
            Unittools.create_unit_full(barbarians, utile, Unittype_P.get_role_unit(unit_role_id.L_BARBARIAN_LEADER.ordinal(), 0), 0, 0, -1, -1, ptrans);
        }
        for (unit punit2 : utile.units.data) {
            Unittools.send_unit_info(null, punit2);
        }
        Maphand.show_area(barbarians, utile, 3);
        Maphand.show_area(barbarians, pc.tile, 3);
        if (is_land_barbarian(barbarians)) {
            Plrhand.notify_player_ex(victim, utile, event_type.E_UPRISING, "Native unrest near %s led by %s.", pc.name, barbarians.name);
        } else if (Maphand.map_is_known_and_seen(utile, victim)) {
            Plrhand.notify_player_ex(victim, utile, event_type.E_UPRISING, "Sea raiders seen near %s!", pc.name);
        }
    }

    /**************************************************************************
	 * Summon barbarians out of the blue. Try more times for more difficult
	 * levels - which means there can be more than one uprising in one year!
	 **************************************************************************/
    void summon_barbarians() {
        int i, n;
        if (Game.game.barbarianrate == 0) {
            return;
        }
        if (Game.game.year < Game.game.onsetbarbarian) {
            return;
        }
        n = Map.map.map_num_tiles() / MAP_FACTOR;
        if (n == 0) {
            n = 1;
        }
        for (i = 0; i < n * (Game.game.barbarianrate - 1); i++) {
            try_summon_barbarians();
        }
    }
}
