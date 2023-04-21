package ch.jester.system.swiss.simple.util;

import java.util.Comparator;
import ch.jester.model.Player;
import ch.jester.model.PlayerCard;
import ch.jester.model.Title;

/**
 * Vergleichen von 2 Spielern nach den FIDE Regeln
 * für das Schweizer System<br/>
 * <link>http://www.fide.com/fide/handbook.html?id=83&view=article</link><br/>
 * A.2 Order:<br/>
 * a. score<br/>
 * b. rating<br/>
 * c. FIDE-title<br/>
 * d. alphabetically<br/>
 * 
 * 
 */
public class PlayerComparator implements Comparator<PlayerCard> {

    private RatingType ratingType;

    public PlayerComparator(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    @Override
    public int compare(PlayerCard p1, PlayerCard p2) {
        Double p1Points = p1.getPoints();
        Double p2Points = p2.getPoints();
        if (p2Points.compareTo(p1Points) == 0) {
            switch(ratingType) {
                case ELO:
                    return compareElo(p1, p2);
                case NWZ:
                    return compareNWZ(p1, p2);
                case ESTIMATED:
                    return compareEstimatedElo(p1, p2);
                default:
                    break;
            }
        } else {
            return p2Points.compareTo(p1Points);
        }
        return 0;
    }

    private int compareElo(PlayerCard p1, PlayerCard p2) {
        Player player1 = p1.getPlayer();
        Player player2 = p2.getPlayer();
        Integer player1Elo = player1.getElo() != null ? player1.getElo() : 0;
        Integer player2Elo = player2.getElo() != null ? player2.getElo() : 0;
        if (player2Elo.compareTo(player1Elo) == 0) {
            return compareTitle(player1, player2);
        } else {
            return player2Elo.compareTo(player1Elo);
        }
    }

    private int compareNWZ(PlayerCard p1, PlayerCard p2) {
        Player player1 = p1.getPlayer();
        Player player2 = p2.getPlayer();
        Integer player1Nwz = player1.getNationalElo() != null ? player1.getNationalElo() : 0;
        Integer player2Nwz = player2.getNationalElo() != null ? player2.getNationalElo() : 0;
        if (player2Nwz.compareTo(player1Nwz) == 0) {
            return compareTitle(player1, player2);
        } else {
            return player2Nwz.compareTo(player1Nwz);
        }
    }

    private int compareEstimatedElo(PlayerCard p1, PlayerCard p2) {
        Player player1 = p1.getPlayer();
        Player player2 = p2.getPlayer();
        Integer player1EstimatedElo = player1.getEstimatedElo() != null ? player1.getEstimatedElo() : 0;
        Integer player2EstimatedElo = player2.getEstimatedElo() != null ? player2.getEstimatedElo() : 0;
        if (player2EstimatedElo.compareTo(player1EstimatedElo) == 0) {
            return compareTitle(player1, player2);
        } else {
            return player2EstimatedElo.compareTo(player1EstimatedElo);
        }
    }

    private int compareTitle(Player player1, Player player2) {
        Title titlePlayer1 = player1.getTitle() != null ? player1.getTitle() : Title.NONE;
        Title titlePlayer2 = player2.getTitle() != null ? player2.getTitle() : Title.NONE;
        if (titlePlayer1.compareTo(titlePlayer2) == 0) {
            return compareAlpabeticaly(player1, player2);
        } else {
            return titlePlayer1.compareTo(titlePlayer2);
        }
    }

    private int compareAlpabeticaly(Player player1, Player player2) {
        String lastNameP1 = player1.getLastName();
        String lastNameP2 = player2.getLastName();
        if (lastNameP1.compareToIgnoreCase(lastNameP2) == 0) {
            return player1.getFirstName().compareToIgnoreCase(player2.getFirstName());
        } else return lastNameP1.compareToIgnoreCase(lastNameP2);
    }
}
