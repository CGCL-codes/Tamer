package sjrd.tricktakinggame.remotable;

import java.util.*;
import java.util.concurrent.locks.*;
import sjrd.tricktakinggame.cards.*;

/**
 * Implémentation de base par défaut de <tt>RemotablePlayer</tt>
 * @author sjrd
 */
public class BaseRemotablePlayer<G extends RemotableGame, T extends RemotableTeam> implements RemotablePlayer {

    /**
     * Jeu propriétaire
     */
    private G game;

    /**
     * Equipe de ce joueur
     */
    private T team;

    /**
     * Nom du joueur
     */
    private String name;

    /**
     * Position
     */
    private int position;

    /**
     * Cartes en main
     */
    public final List<Card> cards = new ArrayList<Card>();

    /**
     * Verrou de type multi-read-exclusive-write sur les cartes en main
     */
    private final ReadWriteLock cardsLock = new ReentrantReadWriteLock();

    /**
     * Cartes ramassées
     */
    public final List<Card> collectedCards = new ArrayList<Card>();

    /**
     * Verrou de type multi-read-exclusive-write sur les cartes ramassées
     */
    private final ReadWriteLock collectedCardsLock = new ReentrantReadWriteLock();

    /**
     * Indique si une carte a été jouée dans le pli courant
     */
    private boolean hasPlayedACard = false;

    /**
     * Indique si la carte jouée dans le pli est cachée
     */
    private boolean playedCardHidden = false;

    /**
     * Carte jouée dans le pli courant
     */
    private Card playedCard = null;

    /**
     * Crée un nouveau joueur
     * <p>
     * Il appartient aux classes descendantes de se référencer auprès du jeu
     * propriétaire et de l'équipe.
     * </p>
     * @param aGame Jeu propriétaire
     * @param aTeam Equipe du joueur
     * @param aName Nom du joueur
     */
    public BaseRemotablePlayer(G aGame, T aTeam, String aName) {
        super();
        game = aGame;
        position = game.getPlayerCount();
        team = aTeam;
        name = aName;
    }

    /**
     * @see sjrd.tricktakinggame.remotable.RemotablePlayer#getGame()
     */
    public G getGame() {
        return game;
    }

    /**
     * @see sjrd.tricktakinggame.remotable.RemotablePlayer#getTeam()
     */
    public T getTeam() {
        return team;
    }

    /**
     * @see sjrd.tricktakinggame.remotable.RemotablePlayer#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    public int getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    public RemotablePlayer nextPlayer(int count) {
        return game.getCyclicPlayers(position + count);
    }

    /**
     * {@inheritDoc}
     */
    public RemotablePlayer nextPlayer() {
        return nextPlayer(1);
    }

    /**
     * Verrouille les cartes en lecture
     */
    public void lockCardsRead() {
        cardsLock.readLock().lock();
    }

    /**
     * Déverrouille les cartes en lecture
     */
    public void unlockCardsRead() {
        cardsLock.readLock().unlock();
    }

    /**
     * Verrouille les cartes en écriture
     */
    public void lockCardsWrite() {
        cardsLock.writeLock().lock();
    }

    /**
     * Déverrouille les cartes en écriture
     */
    public void unlockCardsWrite() {
        cardsLock.writeLock().unlock();
    }

    /**
     * {@inheritDoc}
     */
    public int getCardCount() {
        lockCardsRead();
        try {
            return cards.size();
        } finally {
            unlockCardsRead();
        }
    }

    /**
     * {@inheritDoc}
     */
    public Card getCards(int index) {
        lockCardsRead();
        try {
            return cards.get(index);
        } finally {
            unlockCardsRead();
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<Card> getCardsSnapshot() {
        lockCardsRead();
        try {
            return new ArrayList<Card>(cards);
        } finally {
            unlockCardsRead();
        }
    }

    /**
     * Indique si le joueur a une carte donnée en main
     * @param card Carte à tester
     * @return <tt>true</tt> s'il a la carte en main, <tt>false</tt> sinon
     */
    public boolean hasCard(Card card) {
        lockCardsRead();
        try {
            return cards.contains(card);
        } finally {
            unlockCardsRead();
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean hasPlayedCard() {
        return hasPlayedACard;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isPlayedCardHidden() {
        return playedCardHidden;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Card getPlayedCard() {
        return playedCard;
    }

    /**
     * Supprime la carte jouée
     */
    protected synchronized void resetPlayedCard() {
        hasPlayedACard = false;
        playedCardHidden = false;
        playedCard = null;
    }

    /**
     * Modifie la carte jouée
     * @param value Nouvelle carte jouée (peut être <tt>null</tt>)
     * @param hidden <tt>true</tt> si elle est cachée, <tt>false</tt> sinon
     */
    protected synchronized void setPlayedCard(Card value, boolean hidden) {
        hasPlayedACard = true;
        playedCard = value;
        playedCardHidden = hidden;
    }

    /**
     * Modifie la carte jouée, et indique qu'elle est visible
     * @param value Nouvelle carte jouée
     */
    protected void setPlayedCard(Card value) {
        setPlayedCard(value, false);
    }

    /**
     * Verrouille les cartes ramassées en lecture
     */
    public void lockCollectedCardsRead() {
        collectedCardsLock.readLock().lock();
    }

    /**
     * Déverrouille les cartes ramassées en lecture
     */
    public void unlockCollectedCardsRead() {
        collectedCardsLock.readLock().unlock();
    }

    /**
     * Verrouille les cartes ramassées en écriture
     */
    public void lockCollectedCardsWrite() {
        collectedCardsLock.writeLock().lock();
    }

    /**
     * Déverrouille les cartes ramassées en écriture
     */
    public void unlockCollectedCardsWrite() {
        collectedCardsLock.writeLock().unlock();
    }

    /**
     * {@inheritDoc}
     */
    public List<Card> getCollectedCardsSnapshot() {
        lockCollectedCardsRead();
        try {
            return new ArrayList<Card>(collectedCards);
        } finally {
            unlockCollectedCardsRead();
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getCollectedTrickCount() {
        lockCollectedCardsRead();
        try {
            return collectedCards.size() / game.getPlayerCount();
        } finally {
            unlockCollectedCardsRead();
        }
    }
}
