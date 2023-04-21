package org.eoti.rei.ndfcmd;

import org.eoti.rei.cmp.*;
import java.util.*;

public class SortCmd extends NDFBrowserCommand {

    public String[] commands() {
        return new String[] { "sort" };
    }

    protected enum SortMethod {

        AuctionDate("Auction Date"), EquityLtH("Equity (Low to High)"), EquityHtL("Equity (High to Low)"), ValueLtH("Market Value (Low to High"), ValueHtL("Market Value (High to Low");

        SortMethod(String desc) {
            this.desc = desc;
        }

        private String desc;

        public String getDescription() {
            return desc;
        }
    }

    public void process(List<String> commandLine) throws Exception {
        format("\n");
        for (SortMethod method : SortMethod.values()) format("%d. %s\n", method.ordinal(), method.getDescription());
        String input = readLine("Choose Sort Method> ");
        SortMethod method = SortMethod.values()[Integer.parseInt(input)];
        switch(method) {
            case AuctionDate:
                Collections.sort(browser.getReader().rows(), new AuctionDateComparator(browser));
                break;
            case EquityLtH:
                Collections.sort(browser.getReader().rows(), new EquityLtHComparator(browser));
                break;
            case EquityHtL:
                Collections.sort(browser.getReader().rows(), new EquityHtLComparator(browser));
                break;
            case ValueLtH:
                Collections.sort(browser.getReader().rows(), new ValueLtHComparator(browser));
                break;
            case ValueHtL:
                Collections.sort(browser.getReader().rows(), new ValueHtLComparator(browser));
                break;
        }
    }

    public void displayHelp() {
        format("sort: display menu and sort the houses.\n");
    }
}
