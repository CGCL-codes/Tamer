package jshm.sh.links;

import java.util.List;
import jshm.Difficulty;
import jshm.Game;
import jshm.GameSeries;
import jshm.GameTitle;
import jshm.Instrument;
import jshm.Platform;
import jshm.gh.GhGameTitle;
import jshm.sh.RbPlatform;
import jshm.sh.URLs;
import jshm.wt.WtGame;
import jshm.wt.WtGameTitle;

class GhGamesTemplate extends Link {

    GhGamesTemplate(final String name, final String urlFmt) {
        super(name);
        for (GameTitle t : GameTitle.getBySeries(GameSeries.GUITAR_HERO)) {
            GhGameTitle tt = (GhGameTitle) t;
            Link ttlLink = new Link(t.getLongName());
            ttlLink.icon = t.getIcon();
            List<Game> games = Game.getByTitle(t);
            for (Game g : games) {
                Link gameLink = null;
                if (games.size() > 1) {
                    gameLink = new Link(g.platform.getShortName());
                    gameLink.icon = g.platform.getIcon();
                } else {
                    gameLink = ttlLink;
                }
                for (Difficulty d : Difficulty.values()) {
                    if (Difficulty.CO_OP == d && !tt.supportsCoOp) continue;
                    if (Difficulty.EXPERT_PLUS == d) break;
                    Link diffLink = new Link(d.getLongName(), String.format(URLs.BASE + "/" + urlFmt + "game=%s&diff=%s", g.scoreHeroId, d.scoreHeroId), d.getIcon());
                    gameLink.add(diffLink);
                }
                if (gameLink != ttlLink) {
                    ttlLink.add(gameLink);
                }
            }
            if (games.size() > 1) {
                Link allLink = new Link("All");
                for (Difficulty d : Difficulty.values()) {
                    if (Difficulty.CO_OP == d && !tt.supportsCoOp) continue;
                    if (Difficulty.EXPERT_PLUS == d) break;
                    Link diffLink = new Link(d.getLongName(), String.format(URLs.BASE + "/" + urlFmt + "group=%s&game=0&diff=%s", tt.scoreHeroGroupId, d.scoreHeroId), d.getIcon());
                    allLink.add(diffLink);
                }
                ttlLink.add(allLink);
            }
            add(ttlLink);
        }
        if ("Manage Scores".equals(name) || "Rankings".equals(name) || "Top Scores".equals(name)) {
            for (GameTitle t : GameTitle.getBySeries(GameSeries.WORLD_TOUR)) {
                WtGameTitle tt = (WtGameTitle) t;
                Link ttlLink = new Link(t.getLongName());
                ttlLink.icon = t.getIcon();
                for (Platform p : t.platforms) {
                    WtGame wgame = (WtGame) Game.getByTitleAndPlatform(t, p);
                    Link platLink = new Link(p.getShortName(), p.getIcon());
                    for (int groupSize = 1; groupSize <= 4; groupSize++) {
                        Link sizeLink = new Link(groupSize + "-part");
                        for (Instrument.Group g : Instrument.Group.getBySize(groupSize, true)) {
                            Link groupLink = new Link(g.getLongName(true), g.getIcon());
                            for (Difficulty d : Difficulty.values()) {
                                if (Difficulty.CO_OP == d) continue;
                                if (Difficulty.EXPERT_PLUS == d) {
                                    if (!tt.supportsExpertPlus) break;
                                    if (g.size != 1 || !g.instruments[0].isDrums()) break;
                                }
                                Link diffLink = new Link(d.getLongName(), String.format(URLs.BASE + "/" + urlFmt + URLs.wt.ARGS_FMT, tt.scoreHeroGroupId, wgame.scoreHeroId, RbPlatform.getId(p), groupSize, g.worldTourId, d.scoreHeroId), d.getIcon());
                                groupLink.add(diffLink);
                            }
                            if (groupLink != sizeLink) sizeLink.add(groupLink);
                        }
                        platLink.add(sizeLink);
                    }
                    ttlLink.add(platLink);
                }
                if (t.platforms.length > 1) {
                    Link allLink = new Link("All");
                    for (int groupSize = 1; groupSize <= 4; groupSize++) {
                        Link sizeLink = new Link(groupSize + "-part");
                        for (Instrument.Group g : Instrument.Group.getBySize(groupSize, true)) {
                            Link groupLink = new Link(g.getLongName(true), g.getIcon());
                            for (Difficulty d : Difficulty.values()) {
                                if (Difficulty.CO_OP == d) continue;
                                if (Difficulty.EXPERT_PLUS == d) {
                                    if (!tt.supportsExpertPlus) break;
                                    if (g.size != 1 || !g.instruments[0].isDrums()) break;
                                }
                                Link diffLink = new Link(d.getLongName(), String.format(URLs.BASE + "/" + urlFmt + URLs.wt.ARGS_FMT, tt.scoreHeroGroupId, 0, 0, groupSize, g.worldTourId, d.scoreHeroId), d.getIcon());
                                groupLink.add(diffLink);
                            }
                            if (groupLink != sizeLink) sizeLink.add(groupLink);
                        }
                        allLink.add(sizeLink);
                    }
                    ttlLink.add(allLink);
                }
                add(ttlLink);
            }
        }
    }
}
