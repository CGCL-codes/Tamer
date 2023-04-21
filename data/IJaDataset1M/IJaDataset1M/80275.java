package free.jin.event;

import free.jin.Connection;
import free.jin.GameListItem;

/**
 * The event fired when a game list arrives from the server.
 */
public class GameListEvent extends JinEvent {

    /**
   * The code for a history game list event. When this is the event type, the
   * game list items are instances of {@link free.jin.HistoryListItem}.
   */
    public static final int HISTORY_LIST_EVENT_ID = 1;

    /**
   * The code for a search result game list event. When this is the event type,
   * the game list items are instances of {@link free.jin.SearchListItem}.
   */
    public static final int SEARCH_LIST_EVENT_ID = 2;

    /**
   * The code for a liblist game list event. When this is the event type,
   * the game list items are instances of {@link free.jin.LibListItem}.
   */
    public static final int LIBLIST_EVENT_ID = 3;

    /**
   * The code for a stored game list event. When this is the event type,
   * the game list items are instances of {@link free.jin.StoredListItem}.
   */
    public static final int STORED_LIST_EVENT_ID = 4;

    /**
   * The id of the event.
   */
    private final int id;

    /**
   * The game list.
   */
    private final GameListItem[] gameList;

    /**
   * The title of the game list.
   */
    private final String listTitle;

    /**
   * The amount of items in the complete list.
   */
    private final int totalNumItems;

    /**
   * The index of the first item in this list, within the complete list.
   */
    private final int firstIndex;

    /**
   * The index of the last item in this list, within the complete list.
   */
    private final int lastIndex;

    /**
   * Creates a new GameListEvent with the given source <code>Connection</code>,
   * id, list of GameListItems, title of the list, the amount of items in the
   * complete list, the index of the first and last items in this list, within 
   * the complete list. The list implied by this event is not always the 
   * complete one.
   */
    public GameListEvent(Connection conn, int id, GameListItem[] gameList, String listTitle, int totalNumItems, int firstIndex, int lastIndex) {
        super(conn);
        this.id = id;
        this.gameList = (GameListItem[]) gameList.clone();
        this.listTitle = listTitle;
        this.totalNumItems = totalNumItems;
        this.firstIndex = firstIndex;
        this.lastIndex = lastIndex;
    }

    /**
   * Returns the id of this GameListEvent.
   */
    public int getID() {
        return id;
    }

    /**
   * Returns an array containing the game list items. You may cast the array
   * to an array of the actual type (HistoryListItem [] for example).
   */
    public GameListItem[] getGameList() {
        return (GameListItem[]) gameList.clone();
    }

    /**
   * Returns the number of game list items in the list.
   */
    public int getItemCount() {
        return gameList.length;
    }

    /**
   * Returns the item at the given index.
   */
    public GameListItem getItem(int n) {
        return gameList[n];
    }

    /**
   * Returns the title of the list.
   */
    public String getListTitle() {
        return listTitle;
    }

    /**
   * Returns the amount of items in the complete list.
   */
    public int getTotalItemCount() {
        return totalNumItems;
    }

    /**
   * Returns the index within the complete list of the first item in this list 
   * (the list in this event might not be the complete one).
   */
    public int getFirstIndex() {
        return firstIndex;
    }

    /**
   * Returns the index within the complete list of the last item in this list
   * (the list in this event might not be the complete one).
   */
    public int getLastIndex() {
        return lastIndex;
    }
}
