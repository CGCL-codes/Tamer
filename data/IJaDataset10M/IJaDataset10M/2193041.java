package server;

import objects.Race;
import java.io.*;

public final class DeferredOrderCommand extends AbstractCommand {

    @Override
    public final boolean doIt(String[] cmd, Race r) {
        boolean result = false;
        switch(cmd.length) {
            case 1:
                result = listAllOrders(r);
                break;
            case 2:
                result = deleteOrder(r, cmd[1]);
                break;
            case 3:
                if (cmd[2].equalsIgnoreCase("LIST")) result = listOrder(r, cmd[1]);
                break;
            default:
                setErrorMessage("Bad command's format");
                break;
        }
        return result;
    }

    private static int getTurnNumber(String turnStr) {
        int result = -1;
        try {
            result = Integer.parseInt(turnStr);
        } catch (NumberFormatException e) {
            setErrorMessage("Bad number format: " + turnStr);
        }
        return result;
    }

    private static boolean deleteOrder(Race race, String turnStr) {
        int turn = getTurnNumber(turnStr);
        if (turn < 0) return false;
        File order = new File("games" + File.separator + race.galaxy.name + File.separator + "orders" + File.separator + race.name + '_' + String.valueOf(turn) + ".ord");
        if (order.exists()) return order.delete();
        setErrorMessage("Order for turn " + String.valueOf(turn) + " not found");
        return false;
    }

    private static boolean listOrder(Race race, String turnStr) {
        int turn = getTurnNumber(turnStr);
        if (turn < 0) return false;
        try {
            listOrder(new FileReader("games" + File.separator + race.galaxy.name + File.separator + "orders" + File.separator + race.name + '_' + String.valueOf(turn) + ".ord"), turn);
        } catch (FileNotFoundException e) {
            setErrorMessage("Order for turn " + String.valueOf(turn) + " not found");
            return false;
        }
        return true;
    }

    private static void listOrder(FileReader fr, int turn) {
        String text = skipMessageBody(new LineNumberReader(fr));
        System.out.println("*** the text of order for turn " + String.valueOf(turn) + " ***");
        System.out.println(text);
        System.out.println("*** end of order's text ***");
    }

    private static boolean listAllOrders(Race race) {
        File ordersDir = new File("games" + File.separator + race.galaxy.name + File.separator + "orders");
        File[] orders = ordersDir.listFiles(new OrderFilter(race));
        if (orders.length == 0) System.out.println("No orders was found"); else {
            System.out.println("Total: " + orders.length + " orders");
            for (File order1 : orders) {
                String order = order1.getName();
                order = order.substring(0, order.lastIndexOf(".ord"));
                order = order.substring(order.lastIndexOf('_') + 1, order.length());
                try {
                    listOrder(new FileReader(order1), Integer.parseInt(order));
                } catch (FileNotFoundException e) {
                }
            }
        }
        return true;
    }

    public static final class OrderFilter implements FilenameFilter {

        final Race race;

        public OrderFilter(Race race) {
            this.race = race;
        }

        public final boolean accept(File dir, String name) {
            boolean result = false;
            if (name.startsWith(race.name + '_') && name.endsWith(".ord")) result = true;
            return result;
        }
    }
}
