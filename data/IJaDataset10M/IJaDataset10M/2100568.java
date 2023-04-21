package command.commandMessages;

import command.AuctionHouse;
import java.util.Observable;
import services.*;

public class MessageNotification extends command.Command implements facade.TickObserver {

    AuctionHouse auctionHouse = AuctionHouse.getInstance();

    public String execute(String canal, String params) {
        return "";
    }

    @Override
    public void tick() {
    }
}
