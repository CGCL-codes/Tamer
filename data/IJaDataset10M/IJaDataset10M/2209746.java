package board;

import gamerule.ShipPlacementException;
import org.junit.Test;
import ship.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author tymoshya
 * @since 8/4/11
 */
public class BoardTest {

    final int BOARD_WIDTH = 10;

    final int BOARD_HEIGHT = 10;

    final Coordinate c1 = new Coordinate(1, 1);

    final Coordinate c2 = new Coordinate(10, 10);

    IBoard mockedBoard = mock(IBoard.class);

    IShip placeableShip = new ShipThreeDeck(c1, Direction.HORIZONTAL);

    IShip unPlaceableShip = new ShipOneDeck(c2, Direction.HORIZONTAL);

    @Test
    public void testGetWidthReturnBoardWidth() {
        when(mockedBoard.getWidth()).thenReturn(BOARD_WIDTH);
        assertEquals(BOARD_WIDTH, mockedBoard.getWidth());
    }

    @Test
    public void testGetHeightReturnBoardHeight() {
        when(mockedBoard.getHeight()).thenReturn(BOARD_HEIGHT);
        assertEquals(BOARD_HEIGHT, mockedBoard.getHeight());
    }

    @Test
    public void testPlaceShipVisually() {
        Board board = new BoardClassic();
        Shipyard shipyard = new Shipyard(board);
        Ship ship1 = new ShipFourDeck(new Coordinate(1, 1), Direction.VERTICAL);
        Ship ship2 = new ShipThreeDeck(new Coordinate(4, 4), Direction.HORIZONTAL);
        try {
            shipyard.place(ship1);
            shipyard.place(ship2);
        } catch (ShipPlacementException e) {
            e.printStackTrace();
        }
        System.out.println(board);
    }
}
