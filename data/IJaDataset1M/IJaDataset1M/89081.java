package client;

import client.controller.CasinoController;
import client.model.casino.CasinoInterface;
import client.model.casino.CasinoModel;

public class SPREEClient {

    public static void main(String[] args) {
        CasinoInterface model = new CasinoModel();
        if (args.length == 2) {
            new CasinoController(model, args[0], args[1]);
        } else if (args.length == 4) {
            new CasinoController(model, args[0], args[1], args[2], args[3]);
        } else {
            new CasinoController(model);
        }
    }
}
