package sjrd.tricktakinggame.remotable;

import java.util.*;

/**
 * Implémentation de base par défaut de <tt>RemotableGame</tt>
 * @author sjrd
 */
public class BaseRemotableGame<T extends RemotableTeam, P extends RemotablePlayer> implements RemotableGame {

    /**
     * Equipes
     */
    private final List<T> teams = new ArrayList<T>();

    /**
     * Itérable sur les équipes remotables
     */
    private final Iterable<RemotableTeam> teamsIterable = new RemotableGame.TeamsIterable(this);

    /**
     * Joueurs
     */
    private final List<P> players = new ArrayList<P>();

    /**
     * Itérable sur les joueurs remotables
     */
    private final Iterable<RemotablePlayer> playersIterable = new RemotableGame.PlayersIterable(this);

    /**
     * Liste des annonces (<tt>null</tt> si pas d'enchère en cours)
     */
    private List<Announce> announces = null;

    /**
     * Nom du contrat courant
     */
    private String currentContractName = null;

    /**
     * Serveur
     */
    private P dealer = null;

    /**
     * Joueur actif, qui a la main
     */
    private P activePlayer = null;

    /**
     * Nombre de parties jouées
     */
    private int playCount = 0;

    /**
     * Ajoute une équipe
     * @param team Equipe à ajouter
     * @return Index de l'équipe
     */
    protected int addTeam(T team) {
        teams.add(team);
        return teams.size() - 1;
    }

    /**
     * Ajoute un joueur
     * @param player Joueur à ajouter
     * @return Position du joueur
     */
    protected int addPlayer(P player) {
        players.add(player);
        return players.size() - 1;
    }

    /**
     * {@inheritDoc}
     */
    public int getTeamCount() {
        return teams.size();
    }

    /**
     * {@inheritDoc}
     */
    public T getTeams(int index) {
        return teams.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public T getCyclicTeams(int index) {
        return getTeams(index % teams.size());
    }

    /**
     * Itérateur sur les équipes
     * @return Itérateur sur les équipes
     */
    public Iterator<T> teamsIterator() {
        return teams.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<RemotableTeam> getTeamsIterator() {
        return new RemotableGame.TeamsIterator(this);
    }

    /**
     * Itérable sur les équipes
     * @return Itérable sur les équipes
     */
    public Iterable<T> teamsIteratable() {
        return teams;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<RemotableTeam> getTeamsIterable() {
        return teamsIterable;
    }

    /**
     * {@inheritDoc}
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * {@inheritDoc}
     */
    public P getPlayers(int index) {
        return players.get(index);
    }

    /**
     * {@inheritDoc}
     */
    public P getCyclicPlayers(int index) {
        return getPlayers(index % players.size());
    }

    /**
     * Itérateur sur les joueurs
     * @return Itérateur sur les joueurs
     */
    public Iterator<P> playersIterator() {
        return players.iterator();
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<RemotablePlayer> getPlayersIterator() {
        return new RemotableGame.PlayersIterator(this);
    }

    /**
     * Itérable sur les joueurs
     * @return Itérable sur les joueurs
     */
    public Iterable<P> playersIterable() {
        return players;
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<RemotablePlayer> getPlayersIterable() {
        return playersIterable;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isAuctioning() {
        return announces != null;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized List<Announce> getAnnounces() {
        if (announces == null) return null; else return new ArrayList<Announce>(announces);
    }

    /**
     * Modifie les annonces faites
     * @param value Annonces faites (<tt>null</tt> pour pas d'enchère)
     */
    protected synchronized void setAnnounces(List<? extends Announce> value) {
        if (value == null) announces = null; else announces = new ArrayList<Announce>(value);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized String getCurrentContractName() {
        return currentContractName;
    }

    /**
     * Modifie le nom du contrat courant
     * @param value Nom du nouveau contrat (<tt>null</tt> pour pas de contrat)
     */
    protected synchronized void setCurrentContractName(String value) {
        currentContractName = value;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized P getDealer() {
        return dealer;
    }

    /**
     * Modifie le serveur
     * @param value Nouveau serveur
     */
    protected synchronized void setDealer(P value) {
        dealer = value;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized P getActivePlayer() {
        return activePlayer;
    }

    /**
     * Modifie le joueur actif
     * @param value Nouveau joueur actif
     */
    protected synchronized void setActivePlayer(P value) {
        activePlayer = value;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getPlayCount() {
        return playCount;
    }

    /**
     * Modifie le nombre de parties jouées
     * @param value Nouveau nombre de parties jouées
     */
    protected synchronized void setPlayCount(int value) {
        playCount = value;
    }

    /**
     * Incrémente le nombre de parties jouées
     */
    protected synchronized void incPlayCount() {
        playCount++;
    }
}
