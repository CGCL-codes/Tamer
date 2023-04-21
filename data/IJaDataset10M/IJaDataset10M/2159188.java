package dk.mirasola.systemtraining.bridgewidgets.shared.factory;

import dk.mirasola.systemtraining.bridgewidgets.shared.model.Bid;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.ContractBid;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.SpecialBid;

public class BidFactory {

    public static String bidToString(Bid bid) {
        if (bid == null) {
            throw new NullPointerException();
        } else if (bid instanceof SpecialBid) {
            switch((SpecialBid) bid) {
                case PAS:
                    return "P";
                case DOUBLE:
                    return "D";
                case REDOUBLE:
                    return "R";
                case NULLBID:
                    return "N";
                default:
                    throw new RuntimeException("Unknown specialbid: " + bid);
            }
        } else if (bid instanceof ContractBid) {
            ContractBid contractBid = (ContractBid) bid;
            switch(contractBid.getCall()) {
                case CLUB:
                    return String.valueOf(contractBid.getLevel()) + "C";
                case DIAMOND:
                    return String.valueOf(contractBid.getLevel()) + "D";
                case HEART:
                    return String.valueOf(contractBid.getLevel()) + "H";
                case SPADE:
                    return String.valueOf(contractBid.getLevel()) + "S";
                case NOTRUMPH:
                    return String.valueOf(contractBid.getLevel()) + "N";
                default:
                    throw new RuntimeException("Unknown call :" + contractBid.getCall());
            }
        }
        throw new RuntimeException("Unknow type of bid " + bid);
    }

    public static Bid stringToBid(String encodedStr) {
        if (encodedStr == null) {
            throw new NullPointerException();
        } else if (encodedStr.length() == 1) {
            if ("P".equals(encodedStr)) {
                return SpecialBid.PAS;
            } else if ("D".equals(encodedStr)) {
                return SpecialBid.DOUBLE;
            } else if ("R".equals(encodedStr)) {
                return SpecialBid.REDOUBLE;
            } else if ("N".equals(encodedStr)) {
                return SpecialBid.NULLBID;
            } else {
                throw new RuntimeException("Cannot parse '" + encodedStr + "' to a bid");
            }
        } else if (encodedStr.length() == 2) {
            int level = 0;
            try {
                level = Integer.parseInt(encodedStr.substring(0, 1));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Cannot parse '" + encodedStr + "' to a bid");
            }
            if (level < 1 || level > 7) {
                throw new RuntimeException("Cannot parse '" + encodedStr + "' to a bid");
            }
            String callStr = encodedStr.substring(1, 2);
            ContractBid.Call call = null;
            if ("C".equals(callStr)) {
                call = ContractBid.Call.CLUB;
            } else if ("D".equals(callStr)) {
                call = ContractBid.Call.DIAMOND;
            } else if ("H".equals(callStr)) {
                call = ContractBid.Call.HEART;
            } else if ("S".equals(callStr)) {
                call = ContractBid.Call.SPADE;
            } else if ("N".equals(callStr)) {
                call = ContractBid.Call.NOTRUMPH;
            }
            if (call == null) {
                throw new RuntimeException("Cannot parse '" + encodedStr + "' to a bid");
            }
            for (ContractBid contractBid : ContractBid.values(level)) {
                if (contractBid.getCall().equals(call)) return contractBid;
            }
            throw new RuntimeException("Cannot parse '" + encodedStr + "' to a bid");
        } else {
            throw new RuntimeException("Cannot parse '" + encodedStr + "' to a bid");
        }
    }

    private BidFactory() {
    }
}
