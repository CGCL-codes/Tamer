package alice.cartago.examples.roomsworld;

import alice.cartago.CartagoService;
import alice.cartago.ICartagoWorkspace;
import alice.cartago.ICartagoContext;
import alice.cartago.tools.BasicLogger;

/**
 *
 * @author michelepiunti
 */
public class Main {

    public static void main(String[] args) throws Exception {
        String WS = "CartagoBDI";
        CartagoService.installNode(4010);
        ICartagoContext defaultContext = CartagoService.joinDefaultWorkspace("main");
        defaultContext.createWorkspace(WS);
        new agensFaber("AgensFaber", WS).start();
        new agentTimer("AgentTimer", WS).start();
        new agentExpHandler("AgentExpHandler", WS).start();
    }
}
