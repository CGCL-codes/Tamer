package ao.simple.alexo.card;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class AlexoHand {

    public static int valueOf(AlexoCard a, AlexoCard b, AlexoCard c) {
        List<AlexoCard> inOrder = Arrays.asList(a, b, c);
        Collections.sort(inOrder);
        int vals = AlexoCard.VALUES.length;
        int valA = inOrder.get(0).ordinal(), valB = inOrder.get(1).ordinal(), valC = inOrder.get(2).ordinal();
        return valA + vals * (valB + vals * valC);
    }

    public static int valueOf(AlexoCard hole, AlexoCommunity community) {
        return valueOf(hole, community.flop(), community.turn());
    }
}
