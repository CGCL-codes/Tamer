package pdp.scrabble;

import pdp.scrabble.game.AIConfig;
import pdp.scrabble.game.Bag;
import pdp.scrabble.game.Board;
import pdp.scrabble.game.BoardCase;
import pdp.scrabble.game.DicMap;
import pdp.scrabble.game.Dictionary;
import pdp.scrabble.game.GameEngine;
import pdp.scrabble.game.GameEnvironment;
import pdp.scrabble.game.Letter;
import pdp.scrabble.game.LettersProbability;
import pdp.scrabble.game.Location;
import pdp.scrabble.game.Placement;
import pdp.scrabble.game.Player;
import pdp.scrabble.game.Rack;
import pdp.scrabble.game.Resolution;
import pdp.scrabble.game.impl.FactoryImpl;
import pdp.scrabble.multiplayer.Client;
import pdp.scrabble.multiplayer.Server;
import pdp.scrabble.ihm.MainFrame;
import pdp.scrabble.ihm.MainFrame_old;

/** Main game factory.
 * The easiest way to call it, is to use a static import of it:
 * import static pdp.scrabble.Factory.FACTORY;
 */
public interface Factory {

    /** Factory reference, for game object creation. */
    public static final Factory FACTORY = FactoryImpl.getInstance();

    /** Create a game.
     * @param language game language.
     * @param mainFrame main frame reference.
     * @return created game.
     */
    public Game createGame(String language, MainFrame_old mainFrame);

    /** Create a blank bag.
     * @return created bag.
     */
    public Bag createBag();

    /** Create a board.
     * @return created board.
     */
    public Board createBoard();

    /** Create a board case.
     * @param letterMult letter multiplicator.
     * @param wordMult word multiplicator.
     * @param v vertical location.
     * @param h horizontal location.
     * @return created board case.
     */
    public BoardCase createBoardCase(int letterMult, int wordMult, int v, int h);

    /** Create a rack for a player.
     * @return created rack.
     */
    public Rack createRack(Bag bag);

    /** Create a letter.
     * @param name letter name.
     * @param value letter value.
     * @param id letter id.
     * @return created letter.
     */
    public Letter createLetter(char name, int value, int id);

    /** Create a player for a game.
     * @param game game reference.
     * @param name player name.
     * @param id player id.
     * @return created player.
     */
    public Player createPlayer(GameEnvironment game, String name, int id);

    /** Create a blank dictionay.
     * @return created dictionary.
     */
    public Dictionary createDictionary();

    /** Create a solver object which allows player to get help.
     * @param game game reference.
     * @return created helper.
     */
    public Resolution createResolution(Game game);

    /** Create a placement containing list of player actions to be executed.
     * @return created placement.
     */
    public Placement createPlacement();

    /** Create a new letters probability.
     * @param bag bag reference.
     * @return letters probability created.
     */
    public LettersProbability createLettersProbability(Bag bag);

    /** Create a new location with specified coordinates.
     * @param v vertical location.
     * @param h horizontal location.
     * @return created location.
     */
    public Location createLocation(int v, int h);

    /** Create a dicmap structure (used for word indexation in dictionary).
     * @param array true for array impl (faster), false for tree impl (low RAM)
     * @return created dic map.
     */
    public DicMap createDicMap(boolean array);

    /** Create a server for multiplayer game.
     * @param port server port.
     * @param language server language.
     * @return created server.
     */
    public Server createServer(int port, String language);

    /** Create a client for multiplayer game
     * @param mainFrame
     * @param name
     * @return created client
     */
    public Client createClient(MainFrame mainFrame, String name);

    public GameEngine createEngine(String mode, GameEnvironment gameEnv);
}
