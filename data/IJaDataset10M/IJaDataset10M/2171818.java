package com.rezzix.kowa.ai;

import java.util.ArrayList;
import com.rezzix.kowa.actors.Player;
import com.rezzix.kowa.market.Merchandise;
import com.rezzix.kowa.util.Constants;
import static com.rezzix.kowa.util.Constants.*;

public class Advisor {

    public static String getAdvice(Player player) {
        ArrayList<String> advices = new ArrayList<String>();
        if (player.getGame().getTurns() < 15) {
            ArrayList<String> risings = new ArrayList<String>();
            for (Merchandise merchandise : player.getGame().getMarket().getMerchandises()) {
                if (merchandise.isRising() != null && merchandise.isRising()) {
                    risings.add(merchandise.getName());
                }
            }
            String commerceAdvice = "Focus on commerce";
            for (String merchandiseName : risings) {
                commerceAdvice += ", " + merchandiseName;
            }
            if (risings.size() == 0) {
            } else if (risings.size() == 1) {
                commerceAdvice += " is rising";
            } else {
                commerceAdvice += " are rising";
            }
            advices.add(commerceAdvice);
        }
        if (player.getGame().getTurns() > 15 && player.getWareHouse().getLastValueChange() > 2000 && player.getEscorts().size() == 0) {
            advices.add("You may need an escort to help your security");
        }
        for (Merchandise merchandise : player.getWareHouse().getMerchandises()) {
            if (merchandise.isRising() == null) {
                advices.add("merchandise " + merchandise.getName() + " is not giving benifit try changing it");
                continue;
            }
            if (merchandise.isRising() == false) {
                advices.add("merchandise " + merchandise.getName() + " has its price falling get rid of it");
            }
        }
        if (player.getWareHouse().getLastValueChange() < player.getEscortsSalary()) {
            advices.add("Your income from commerce" + player.getWareHouse().getLastValueChange() + currency + " was less than " + player.getEscortsSalary() + currency + " only keep escorts that you really need");
        }
        if (player.getEscortsSalary() > 0) {
            advices.add("Be sure to keep enough money for you escorts salary " + player.getEscortsSalary() + Constants.currency + " each turn ");
        }
        if (player.getTotalMoney() > 100000) {
            advices.add("Don't invest all your money in one merchandise and always keep some cash to deal with events");
        }
        if (advices.isEmpty()) {
            advices.add("No advice to give for the moment");
        }
        StringBuffer adviceSb = new StringBuffer();
        for (String advice : advices) {
            adviceSb.append(" - ").append(advice).append("\n");
        }
        return adviceSb.toString();
    }
}
