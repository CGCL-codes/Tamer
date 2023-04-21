package dk.mirasola.systemtraining.bridgewidgets.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import dk.mirasola.systemtraining.bridgewidgets.client.i18n.BridgeTexts;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Bid;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Card;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.ContractBid;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Seat;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.SpecialBid;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.Suit;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.CardsCountInterval;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.CompareOperator;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.HcpInterval;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.LogicOperator;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.shape.ExactShapeElement;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.shape.IntervalShapeElement;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.shape.Shape;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.shape.ShapeElement;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.shape.ShapeType;
import dk.mirasola.systemtraining.bridgewidgets.shared.model.distributionfiltertree.shape.WildcardShapeElement;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RenderUtil {

    private static BridgeTexts bridgeTexts = GWT.create(BridgeTexts.class);

    private static List<Seat> seatsSorted = Arrays.asList(Seat.NORTH, Seat.EAST, Seat.SOUTH, Seat.WEST);

    private static List<Suit> suitsSorted = Arrays.asList(Suit.SPADE, Suit.HEART, Suit.DIAMOND, Suit.CLUB);

    public static SafeHtml renderLogicOperator(LogicOperator logicOperator) {
        if (logicOperator == null) {
            throw new NullPointerException();
        }
        if (LogicOperator.AND == logicOperator) {
            return SafeHtmlUtils.fromTrustedString(bridgeTexts.logicAnd());
        } else {
            return SafeHtmlUtils.fromTrustedString(bridgeTexts.logicOr());
        }
    }

    public static SafeHtml renderSeatLowercase(Seat seat) {
        if (seat == null) {
            throw new NullPointerException();
        }
        switch(seat) {
            case NORTH:
                return SafeHtmlUtils.fromTrustedString(bridgeTexts.north().toLowerCase());
            case EAST:
                return SafeHtmlUtils.fromTrustedString(bridgeTexts.east().toLowerCase());
            case SOUTH:
                return SafeHtmlUtils.fromTrustedString(bridgeTexts.south().toLowerCase());
            case WEST:
                return SafeHtmlUtils.fromTrustedString(bridgeTexts.west().toLowerCase());
            default:
                throw new RuntimeException("Unknown type of seat: " + seat);
        }
    }

    public static void renderSeatLowercase(Seat seat, SafeHtmlBuilder sb) {
        sb.append(renderSeatLowercase(seat));
    }

    public static SafeHtml renderShape(Shape shape) {
        List<ShapeElement> shapeElements = shape.getShapeElements();
        return renderShapeElements(shape.getShapeElements());
    }

    private static SafeHtml renderShapeElements(List<ShapeElement> shapeElements) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        for (int i = 0; i < 4; i++) {
            ShapeElement shapeElement = shapeElements.get(i);
            if (shapeElement instanceof ExactShapeElement) {
                sb.append(((ExactShapeElement) shapeElement).getCardsCount());
            } else if (shapeElement instanceof IntervalShapeElement) {
                IntervalShapeElement intervalShapeElement = (IntervalShapeElement) shapeElement;
                sb.append(intervalShapeElement.getMinCardsCount());
                sb.append(SafeHtmlUtils.fromTrustedString(":"));
                sb.append(intervalShapeElement.getMaxCardsCount());
            } else if (shapeElement instanceof WildcardShapeElement) {
                sb.append(SafeHtmlUtils.fromTrustedString("&lowast;"));
            }
            if (i < 3) {
                sb.append(SafeHtmlUtils.fromTrustedString("-"));
            }
        }
        return sb.toSafeHtml();
    }

    public static SafeHtml renderShapeType(ShapeType shapeType) {
        return renderShapeElements(shapeType.getShapeElements());
    }

    public static SafeHtml renderBid(Bid bid) {
        if (bid == null) {
            throw new NullPointerException();
        } else if (bid instanceof SpecialBid) {
            switch((SpecialBid) bid) {
                case PAS:
                    return bridgeTexts.pasShort();
                case DOUBLE:
                    return bridgeTexts.doubleShort();
                case REDOUBLE:
                    return bridgeTexts.redoubleShort();
                case NULLBID:
                    return SafeHtmlUtils.fromTrustedString("");
                default:
                    throw new RuntimeException("Unknown type of bid " + bid);
            }
        } else if (bid instanceof ContractBid) {
            ContractBid contractBid = (ContractBid) bid;
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            sb.append(contractBid.getLevel());
            sb.append(renderCall(contractBid.getCall()));
            return sb.toSafeHtml();
        } else {
            throw new RuntimeException("Unknown type of bid " + bid);
        }
    }

    public static SafeHtml renderCall(ContractBid.Call call) {
        switch(call) {
            case NOTRUMPH:
                return bridgeTexts.noTrumphShort();
            case SPADE:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#000000\">&spades;</span>");
            case HEART:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#ff0000\">&hearts;</span>");
            case DIAMOND:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#ff0000\">&diams;</span>");
            case CLUB:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#000000\">&clubs;</span>");
            default:
                throw new RuntimeException("Unknown suit " + call);
        }
    }

    public static SafeHtml renderSeatUppercaseFirst(Seat seat) {
        if (seat == null) {
            throw new NullPointerException();
        }
        switch(seat) {
            case NORTH:
                return SafeHtmlUtils.fromTrustedString(uppercaseFirst(bridgeTexts.north()));
            case EAST:
                return SafeHtmlUtils.fromTrustedString(uppercaseFirst(bridgeTexts.east()));
            case SOUTH:
                return SafeHtmlUtils.fromTrustedString(uppercaseFirst(bridgeTexts.south()));
            case WEST:
                return SafeHtmlUtils.fromTrustedString(uppercaseFirst(bridgeTexts.west()));
            default:
                throw new RuntimeException("Unknown type of seat: " + seat);
        }
    }

    public static String uppercaseFirst(String text) {
        if (text.length() > 0) {
            return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
        } else {
            return text;
        }
    }

    public static void renderRangShort(Card.Rang cardRang, SafeHtmlBuilder sb) {
        if (cardRang == null) {
            throw new NullPointerException();
        }
        switch(cardRang) {
            case ACE:
                sb.append(SafeHtmlUtils.fromTrustedString(bridgeTexts.aceShort()));
                break;
            case KING:
                sb.append(SafeHtmlUtils.fromTrustedString(bridgeTexts.kingShort()));
                break;
            case QUEEN:
                sb.append(SafeHtmlUtils.fromTrustedString(bridgeTexts.queenShort()));
                break;
            case JACK:
                sb.append(SafeHtmlUtils.fromTrustedString(bridgeTexts.jackShort()));
                break;
            case TEN:
                sb.append(SafeHtmlUtils.fromTrustedString(bridgeTexts.tenShort()));
                break;
            case NINE:
                sb.append(9);
                break;
            case EIGTH:
                sb.append(8);
                break;
            case SEVEN:
                sb.append(7);
                break;
            case SIX:
                sb.append(6);
                break;
            case FIVE:
                sb.append(5);
                break;
            case FOUR:
                sb.append(4);
                break;
            case THREE:
                sb.append(3);
                break;
            case TWO:
                sb.append(2);
                break;
            default:
                throw new RuntimeException("Unknown type of card rang " + cardRang);
        }
    }

    public static SafeHtml renderSeatsLowercase(Set<Seat> seats, String delimiter) {
        if (seats.size() == 0) {
            return SafeHtmlUtils.fromTrustedString("");
        } else if (seats.size() == 1) {
            return renderSeatLowercase(seats.iterator().next());
        } else {
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            boolean first = true;
            for (Seat seat : seatsSorted) {
                if (seats.contains(seat)) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(SafeHtmlUtils.fromTrustedString(delimiter));
                    }
                    renderSeatLowercase(seat, sb);
                }
            }
            return sb.toSafeHtml();
        }
    }

    public static SafeHtml renderSeatsShort(Set<Seat> seats, String delimiter) {
        if (seats.size() == 0) {
            return SafeHtmlUtils.fromTrustedString("");
        } else if (seats.size() == 1) {
            return renderSeatShort(seats.iterator().next());
        } else {
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            boolean first = true;
            for (Seat seat : seatsSorted) {
                if (seats.contains(seat)) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(SafeHtmlUtils.fromTrustedString(delimiter));
                    }
                    sb.append(renderSeatShort(seat));
                }
            }
            return sb.toSafeHtml();
        }
    }

    public static SafeHtml renderSeatShort(Seat seat) {
        switch(seat) {
            case NORTH:
                return bridgeTexts.northShort();
            case EAST:
                return bridgeTexts.eastShort();
            case SOUTH:
                return bridgeTexts.southShort();
            case WEST:
                return bridgeTexts.westShort();
            default:
                throw new RuntimeException();
        }
    }

    public static SafeHtml renderSuits(Set<Suit> suits, String delimiter) {
        if (suits.size() == 0) {
            return SafeHtmlUtils.fromTrustedString("");
        } else if (suits.size() == 1) {
            return renderSuit(suits.iterator().next());
        } else {
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            boolean first = true;
            for (Suit suit : suitsSorted) {
                if (suits.contains(suit)) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(SafeHtmlUtils.fromTrustedString(delimiter));
                    }
                    sb.append(renderSuit(suit));
                }
            }
            return sb.toSafeHtml();
        }
    }

    public static SafeHtml renderSuit(Suit suit) {
        switch(suit) {
            case SPADE:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#000000\">&spades;</span>");
            case HEART:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#ff0000\">&hearts;</span>");
            case DIAMOND:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#ff0000\">&diams;</span>");
            case CLUB:
                return SafeHtmlUtils.fromTrustedString("<span style=\"color:#000000\">&clubs;</span>");
            default:
                throw new RuntimeException("Unknown suit " + suit);
        }
    }

    public static SafeHtml renderHcpInterval(HcpInterval hcpInterval) {
        if (hcpInterval.getMinHcp() == hcpInterval.getMaxHcp()) {
            return bridgeTexts.exactHcpInterval(hcpInterval.getMinHcp());
        } else if (hcpInterval.getMaxHcp() == HcpInterval.HCP_INTERVAL_MAX) {
            return bridgeTexts.lowboundOnlyHcpInterval(hcpInterval.getMinHcp());
        } else {
            return bridgeTexts.hcpInterval(hcpInterval.getMinHcp(), hcpInterval.getMaxHcp());
        }
    }

    public static SafeHtml renderCardsCountInterval(CardsCountInterval cardsCountInterval) {
        if (cardsCountInterval.getMinCardsCount() == cardsCountInterval.getMaxCardsCount()) {
            return bridgeTexts.exactCardsCountInterval(cardsCountInterval.getMinCardsCount());
        } else if (cardsCountInterval.getMaxCardsCount() == CardsCountInterval.CARDS_COUNT_INTERVAL_MAX) {
            return bridgeTexts.lowboundOnlyCardsCountInterval(cardsCountInterval.getMinCardsCount());
        } else {
            return bridgeTexts.cardsCountInterval(cardsCountInterval.getMinCardsCount(), cardsCountInterval.getMaxCardsCount(), "-");
        }
    }

    public static SafeHtml renderCardsCountInterval(CardsCountInterval cardsCountInterval, String delimiter) {
        if (cardsCountInterval.getMinCardsCount() == cardsCountInterval.getMaxCardsCount()) {
            return bridgeTexts.exactCardsCountInterval(cardsCountInterval.getMinCardsCount());
        } else if (cardsCountInterval.getMaxCardsCount() == CardsCountInterval.CARDS_COUNT_INTERVAL_MAX) {
            return bridgeTexts.lowboundOnlyCardsCountInterval(cardsCountInterval.getMinCardsCount());
        } else {
            return bridgeTexts.cardsCountInterval(cardsCountInterval.getMinCardsCount(), cardsCountInterval.getMaxCardsCount(), delimiter);
        }
    }

    public static SafeHtml renderComparaOperator(CompareOperator compareOperator) {
        switch(compareOperator) {
            case EQUAL:
                return SafeHtmlUtils.fromTrustedString("=");
            case LESS_THAN:
                return SafeHtmlUtils.fromTrustedString("&lt;");
            case LESS_THAN_OR_EQUAL:
                return SafeHtmlUtils.fromTrustedString("&le;");
            case GREATER_THAN:
                return SafeHtmlUtils.fromTrustedString("&gt;");
            case GREATER_THAN_OR_EQUAL:
                return SafeHtmlUtils.fromTrustedString("&ge;");
            default:
                throw new RuntimeException("Unknown compare operator");
        }
    }

    private RenderUtil() {
    }
}
