package jblip.apps.clip.cmds;

import jblip.BlipClient;

public class Delete extends ClipCommand {

    @Override
    public void execute(BlipClient client, String[] args) {
        Integer id = null;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("ERROR: " + args[0] + " is not a number");
        }
        client.deleteMessage(id);
        if (client.getLastError() == null) {
            System.out.println("Message deleted.");
        } else {
            System.out.println("ERROR: " + client.getLastError());
        }
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getUsageMessage() {
        return "<message id>";
    }
}
