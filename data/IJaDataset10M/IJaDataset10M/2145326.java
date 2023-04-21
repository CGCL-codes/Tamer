package bluffinmuffin.poker.entities;

import bluffinmuffin.game.entities.Card;
import bluffinmuffin.game.entities.CardSet;
import bluffinmuffin.poker.HandEvaluator;

public class PlayerInfo {

    private String m_name;

    private int m_noSeat;

    private final CardSet m_cards = new CardSet(2);

    private int m_initMoneyAmnt;

    private int m_moneySafeAmnt;

    private int m_moneyBetAmnt;

    private boolean m_playing;

    private boolean m_allIn;

    private boolean m_showingCards;

    private boolean m_zombie;

    /**
     * Tuple Player d'un jeu de poker
     * 
     */
    public PlayerInfo() {
        m_name = "Anonymous Player";
        m_noSeat = -1;
        m_moneySafeAmnt = 0;
        m_initMoneyAmnt = 0;
    }

    /**
     * Tuple Player d'un jeu de poker
     * 
     * @param p_name
     *            Nom du player
     */
    public PlayerInfo(String p_name) {
        this();
        m_name = p_name;
    }

    /**
     * Tuple Player d'un jeu de poker
     * 
     * @param p_name
     *            Nom du player
     * @param p_money
     *            Argent initial du player
     */
    public PlayerInfo(String p_name, int p_money) {
        this(p_name);
        m_moneySafeAmnt = p_money;
        m_initMoneyAmnt = p_money;
    }

    /**
     * Tuple Player d'un jeu de poker
     * 
     * @param p_noSeat
     *            Position du player
     * @param p_name
     *            Nom du player
     * @param p_money
     *            Argent initial du player
     */
    public PlayerInfo(int p_noSeat, String p_name, int p_money) {
        this(p_name, p_money);
        m_noSeat = p_noSeat;
    }

    /**
     * Tuple Player d'un jeu de poker
     * 
     * @param p_noSeat
     *            Position du player
     */
    public PlayerInfo(int p_noSeat) {
        this();
        m_noSeat = p_noSeat;
    }

    /**
     * @return Position du joueur autour de la table
     */
    public int getNoSeat() {
        return m_noSeat;
    }

    /**
     * @return Nom du joueur
     */
    public String getName() {
        return m_name;
    }

    /**
     * Position du joueur autour de la table
     * 
     * @param noSeat
     */
    public void setNoSeat(int noSeat) {
        m_noSeat = noSeat;
    }

    /**
     * Defini de maniere arbitraire les cartes du joueur
     * 
     * @param card1
     * @param card2
     */
    public void setCards(Card card1, Card card2) {
        m_cards.clear();
        m_cards.add(card1);
        m_cards.add(card2);
    }

    /**
     * Defini de maniere arbitraire les cartes du joueur
     * 
     * @param set
     *            CardSet contenant la main complete (2 cartes)
     */
    public void setCards(CardSet set) {
        m_cards.clear();
        while (!set.isEmpty()) {
            final Card gc = set.pop();
            m_cards.add(gc);
        }
    }

    /**
     * Retourne les cartes du joueur dans un tableau
     * 
     * @param canSeeCards
     *            Indique si l'on doit cacher les cartes ou non.
     *            Mettre a true sur le client est une bonne idee car on les recoit deja cachee si necessaire
     * @return
     */
    public Card[] getCards(boolean canSeeCards) {
        final Card[] holeCards = new Card[2];
        m_cards.toArray(holeCards);
        if (holeCards[0] != null && holeCards[1] == null && holeCards[0].getId() < 0) {
            holeCards[1] = holeCards[0];
        }
        for (int j = 0; j < 2; ++j) {
            if (holeCards[j] == null) {
                holeCards[j] = Card.NO_CARD;
            } else if (!m_playing && !m_allIn) {
                holeCards[j] = Card.NO_CARD;
            } else if (!canSeeCards && !m_showingCards) {
                holeCards[j] = Card.HIDDEN_CARD;
            }
        }
        return holeCards;
    }

    /**
     * Evalue la valeur de la main du joueur en fonction de ses cartes et de celles sur la table
     * 
     * @param p_board
     *            Les cartes sur la table
     * @return Un long produit par le hand evaluator
     */
    public long evaluateCards(CardSet p_board) {
        final CardSet hand = new CardSet(p_board);
        hand.addAll(m_cards);
        return HandEvaluator.hand7Eval(HandEvaluator.encode(hand));
    }

    /**
     * @return Argent du joueur au moment ou il s'installe a la table
     */
    public int getInitMoneyAmnt() {
        return m_initMoneyAmnt;
    }

    /**
     * @return Argent du joueur qu'il a en sa pocession, non-jouee
     */
    public int getMoneySafeAmnt() {
        return m_moneySafeAmnt;
    }

    /**
     * @return Argent du joueur qu'il a jouee depuis le debut de la round
     */
    public int getMoneyBetAmnt() {
        return m_moneyBetAmnt;
    }

    /**
     * @return Argent du joueur qu'il a en sa pocession + celle jouee depuis le debut du round
     */
    public int getMoneyAmnt() {
        return getMoneyBetAmnt() + getMoneySafeAmnt();
    }

    /**
     * Argent du joueur qu'il a en sa pocession, non-jouee
     * 
     * @param amnt
     */
    public void setMoneySafeAmnt(int amnt) {
        m_moneySafeAmnt = amnt;
    }

    /**
     * Augmenter l'argent du joueur qu'il a en sa pocession, non-jouee
     * 
     * @param amnt
     */
    public void incMoneySafeAmnt(int amnt) {
        m_moneySafeAmnt += amnt;
    }

    /**
     * Argent du joueur qu'il a jouee depuis le debut de la round
     * 
     * @param amnt
     */
    public void setMoneyBetAmnt(int amnt) {
        m_moneyBetAmnt = amnt;
    }

    /**
     * Est-il en train de jouer ?
     * 
     * @return Faux si All-in ou Folded ou NotPlaying, Vrai sinon
     */
    public boolean isPlaying() {
        return m_playing;
    }

    /**
     * Est-il en zombie ?
     * 
     * @return Vrai si le player a quitté la partie, Faux sinon
     */
    public boolean isZombie() {
        return m_zombie;
    }

    /**
     * Est-il All-in
     * 
     * @return Vrai si All-in, faux sinon
     */
    public boolean isAllIn() {
        return m_allIn;
    }

    /**
     * Sommes nous au showdown et le player montre ses cartes
     * 
     * @return Vrai si les autres peuvent voir ses cartes, faux sinon
     */
    public boolean isShowingCJards() {
        return m_showingCards;
    }

    /**
     * D�fini que le joueur est All-In
     * 
     */
    public void setAllIn() {
        m_allIn = true;
        m_playing = false;
    }

    /**
     * D�fini que le joueur est Zombie
     * 
     */
    public void setZombie() {
        m_zombie = true;
    }

    /**
     * D�fini que le joueur est Folded
     * 
     */
    public void setNotPlaying() {
        m_allIn = false;
        m_playing = false;
    }

    /**
     * D�fini que le joueur est Playing
     */
    public void setPlaying() {
        m_showingCards = false;
        m_allIn = false;
        m_playing = true;
    }

    /**
     * D�fini si oui ou non le joueur montre ses cartes
     * 
     * @param showingCards
     *            Est-ce que le joueur montre ses cartes
     */
    public void setShowingCards(boolean showingCards) {
        m_showingCards = showingCards;
    }

    /**
     * Verifie si le joueur est en position de jouer
     * 
     * @return True si il est assis et a de l'argent
     */
    public boolean canPlay() {
        return m_noSeat >= 0 && m_moneySafeAmnt > 0;
    }

    /**
     * Verifie si peux better un certain montant
     * 
     * @param amnt
     * @return True si il le peux
     */
    public boolean canBet(int amnt) {
        return (amnt <= m_moneySafeAmnt);
    }

    /**
     * Essai de better, si reussite decremente MoneyBetAmnt et incremente MoneySafeAmnt
     * 
     * @param amnt
     * @return True si c'est une reussite
     */
    public boolean tryBet(int amnt) {
        if (!canBet(amnt)) {
            return false;
        }
        m_moneySafeAmnt -= amnt;
        m_moneyBetAmnt += amnt;
        return true;
    }
}
