package battlezone;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        String playerName = (String) JOptionPane.showInputDialog(null, "What is your name?", "Battlezone", JOptionPane.QUESTION_MESSAGE, null, null, System.getProperty("user.name"));
        if (playerName != null) {
            Configuration.getInstance().setPlayerName(playerName);
        } else {
            return;
        }
        Model model = new Model();
        View view = new View();
        Controller controller = new Controller();
        Network network = new Network();
        model.setNetwork(network);
        model.setView(view);
        view.setController(controller);
        controller.setModel(model);
        model.spawnMyTank();
    }
}
