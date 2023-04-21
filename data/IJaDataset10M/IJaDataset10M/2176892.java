package game;

/** This class is responsible to initialize the game start */
public class StandardStartInitializer {

    public StandardStartInitializer() {
    }

    /** Method responsible to initialize the game start */
    public void standardStart(Game theGame) {
        theGame.getSpartaPlayer().setPrestige(4);
        theGame.getSpartaPlayer().setMetal(4);
        theGame.getSpartaPlayer().setWood(4);
        theGame.getSpartaPlayer().setWine(4);
        theGame.getSpartaPlayer().setOil(4);
        theGame.getSpartaPlayer().setSilver(4);
        theGame.getSpartaPlayer().setWheat(0);
        theGame.getSpartaPlayer().setPlayerTradeDock(theGame.getGameTradeDocks().get("spartaTradeDock"));
        theGame.getSpartaPlayer().setCapital(theGame.getGamePolis().get("sparta"));
        theGame.getSpartaPlayer().addPolis(theGame.getGamePolis().get("sparta"));
        theGame.getSpartaPlayer().addPolis(theGame.getGamePolis().get("gythion"));
        theGame.getSpartaPlayer().addPolis(theGame.getGamePolis().get("pylos"));
        for (Polis p : theGame.getSpartaPlayer().getPlayerPolis()) {
            if (p.getSysName().equals("sparta")) {
                p.setActualPopulation(4);
                p.setPolisOwner(theGame.getSpartaPlayer());
                break;
            }
        }
        for (Polis p : theGame.getSpartaPlayer().getPlayerPolis()) {
            if (p.getSysName().equals("gythion")) {
                p.setActualPopulation(1);
                p.setPolisOwner(theGame.getSpartaPlayer());
                break;
            }
        }
        for (Polis p : theGame.getSpartaPlayer().getPlayerPolis()) {
            if (p.getSysName().equals("pylos")) {
                p.setActualPopulation(2);
                p.setPolisOwner(theGame.getSpartaPlayer());
                break;
            }
        }
        Hoplite sparta_hop1 = new Hoplite(theGame.getSpartaPlayer(), theGame.getGameTerritories().get("laconia"));
        theGame.getGameTerritories().get("laconia").addUnit(sparta_hop1);
        theGame.getSpartaPlayer().addUnit(sparta_hop1);
        Hoplite sparta_hop2 = new Hoplite(theGame.getSpartaPlayer(), theGame.getGameTerritories().get("laconia"));
        theGame.getGameTerritories().get("laconia").addUnit(sparta_hop2);
        theGame.getSpartaPlayer().addUnit(sparta_hop2);
        Hoplite sparta_hop3 = new Hoplite(theGame.getSpartaPlayer(), theGame.getGameTerritories().get("laconia"));
        theGame.getGameTerritories().get("laconia").addUnit(sparta_hop3);
        theGame.getSpartaPlayer().addUnit(sparta_hop3);
        Trirreme sparta_tri1 = new Trirreme(theGame.getSpartaPlayer(), theGame.getGameSeas().get("ionianSea"));
        theGame.getGameSeas().get("ionianSea").addUnit(sparta_tri1);
        theGame.getSpartaPlayer().addUnit(sparta_tri1);
        Trirreme sparta_tri2 = new Trirreme(theGame.getSpartaPlayer(), theGame.getGameSeas().get("myrtoanSea"));
        theGame.getGameSeas().get("myrtoanSea").addUnit(sparta_tri2);
        theGame.getSpartaPlayer().addUnit(sparta_tri2);
        TradeBoat sparta_tra1 = new TradeBoat(theGame.getSpartaPlayer(), theGame.getGameTradeDocks().get("spartaTradeDock"));
        theGame.getGameTradeDocks().get("spartaTradeDock").addUnit(sparta_tra1);
        theGame.getSpartaPlayer().addUnit(sparta_tra1);
        Proxenus spartaProxenus = new Proxenus(theGame.getSpartaPlayer(), theGame.getGamePolis().get("sparta"));
        theGame.getGamePolis().get("sparta").addUnit(spartaProxenus);
        theGame.getSpartaPlayer().addUnit(spartaProxenus);
        theGame.getSpartaPlayer().setPlayerProxenus(spartaProxenus);
        theGame.getAthensPlayer().setPrestige(4);
        theGame.getAthensPlayer().setMetal(4);
        theGame.getAthensPlayer().setWood(4);
        theGame.getAthensPlayer().setWine(4);
        theGame.getAthensPlayer().setOil(4);
        theGame.getAthensPlayer().setSilver(0);
        theGame.getAthensPlayer().setWheat(4);
        theGame.getAthensPlayer().setPlayerTradeDock(theGame.getGameTradeDocks().get("athensTradeDock"));
        theGame.getAthensPlayer().setCapital(theGame.getGamePolis().get("athens"));
        theGame.getAthensPlayer().addPolis(theGame.getGamePolis().get("athens"));
        theGame.getAthensPlayer().addPolis(theGame.getGamePolis().get("chalcis"));
        theGame.getAthensPlayer().addPolis(theGame.getGamePolis().get("chios"));
        for (Polis p : theGame.getAthensPlayer().getPlayerPolis()) {
            if (p.getSysName().equals("athens")) {
                p.setActualPopulation(5);
                p.setPolisOwner(theGame.getAthensPlayer());
                break;
            }
        }
        for (Polis p : theGame.getAthensPlayer().getPlayerPolis()) {
            if (p.getSysName().equals("chalcis")) {
                p.setActualPopulation(1);
                p.setPolisOwner(theGame.getAthensPlayer());
                break;
            }
        }
        for (Polis p : theGame.getAthensPlayer().getPlayerPolis()) {
            if (p.getSysName().equals("chios")) {
                p.setActualPopulation(2);
                p.setPolisOwner(theGame.getAthensPlayer());
                break;
            }
        }
        Hoplite ath_hop1 = new Hoplite(theGame.getAthensPlayer(), theGame.getGameTerritories().get("attica"));
        theGame.getGameTerritories().get("attica").addUnit(ath_hop1);
        theGame.getAthensPlayer().addUnit(ath_hop1);
        Hoplite ath_hop2 = new Hoplite(theGame.getAthensPlayer(), theGame.getGameTerritories().get("attica"));
        theGame.getGameTerritories().get("attica").addUnit(ath_hop2);
        theGame.getAthensPlayer().addUnit(ath_hop2);
        Hoplite ath_hop3 = new Hoplite(theGame.getAthensPlayer(), theGame.getGameTerritories().get("attica"));
        theGame.getGameTerritories().get("attica").addUnit(ath_hop3);
        theGame.getAthensPlayer().addUnit(ath_hop3);
        Hoplite ath_hop4 = new Hoplite(theGame.getAthensPlayer(), theGame.getGameTerritories().get("ionia"));
        theGame.getGameTerritories().get("ionia").addUnit(ath_hop4);
        theGame.getAthensPlayer().addUnit(ath_hop4);
        Hoplite ath_hop5 = new Hoplite(theGame.getAthensPlayer(), theGame.getGameTerritories().get("ionia"));
        theGame.getGameTerritories().get("ionia").addUnit(ath_hop5);
        theGame.getAthensPlayer().addUnit(ath_hop5);
        Trirreme ath_tri1 = new Trirreme(theGame.getAthensPlayer(), theGame.getGameSeas().get("cycladesIslands"));
        theGame.getGameSeas().get("cycladesIslands").addUnit(ath_tri1);
        theGame.getAthensPlayer().addUnit(ath_tri1);
        Trirreme ath_tri2 = new Trirreme(theGame.getAthensPlayer(), theGame.getGameSeas().get("cycladesIslands"));
        theGame.getGameSeas().get("cycladesIslands").addUnit(ath_tri2);
        theGame.getAthensPlayer().addUnit(ath_tri2);
        Trirreme ath_tri3 = new Trirreme(theGame.getAthensPlayer(), theGame.getGameSeas().get("sporadesIslands"));
        theGame.getGameSeas().get("sporadesIslands").addUnit(ath_tri3);
        theGame.getAthensPlayer().addUnit(ath_tri3);
        TradeBoat ath_tra1 = new TradeBoat(theGame.getAthensPlayer(), theGame.getGameTradeDocks().get("athensTradeDock"));
        theGame.getGameTradeDocks().get("athensTradeDock").addUnit(ath_tra1);
        theGame.getAthensPlayer().addUnit(ath_tra1);
        Proxenus athensProxenus = new Proxenus(theGame.getAthensPlayer(), theGame.getGamePolis().get("athens"));
        theGame.getGamePolis().get("athens").addUnit(athensProxenus);
        theGame.getAthensPlayer().addUnit(athensProxenus);
        theGame.getAthensPlayer().setPlayerProxenus(athensProxenus);
    }
}
