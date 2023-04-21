package server.buildings.handicraft;

import server.buildings.InfiniteProducer;
import server.buildings.ProductionChain;
import server.people.Citizen;
import server.resources.Resource;
import server.resources.materials.Brick;
import server.resources.materials.Tool;
import server.resources.materials.Wood;
import server.resources.necessities.Cloth;
import server.resources.necessities.Clothes;

public class Tailoring extends InfiniteProducer implements HandicraftInterface {

    public static final int BUILDING_COST = 150;

    public static final int CURRENT_COST = 10;

    public static final int STANDBY_COST = 5;

    static {
        people.put(Citizen.class, new Citizen(200));
        resources.put(Tool.class, new Tool(3));
        resources.put(Wood.class, new Wood(6));
        resources.put(Brick.class, new Brick(2));
        Resource[] inputs = { new Cloth(2) };
        chains.add(new ProductionChain(inputs, new Clothes(2)));
    }
}
