package be.jabapage.snooker.service.importation.impl;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import be.jabapage.snooker.exception.EntityNotFoundException;
import be.jabapage.snooker.exception.MultipleInstanceFoundException;
import be.jabapage.snooker.exception.ProcessingException;
import be.jabapage.snooker.jdo.administration.Player;
import be.jabapage.snooker.service.administration.api.IPlayerService;
import be.jabapage.snooker.service.importation.api.IPlayerImportService;
import be.jabapage.snooker.service.overwriter.api.IPlayerOverwriter;

@RunWith(MockitoJUnitRunner.class)
public class PlayerImportServiceTest {

    @Mock
    private IPlayerService playerService;

    @Mock
    private IPlayerOverwriter playerOverwriter;

    @Mock
    private Player player;

    private IPlayerImportService service;

    private static final String PLAYER_NAME = "PLAYER_NAME";

    private static final String PLAYER_NAME_NON_EXISTING = "PLAYER_NAME_NON_EXISTING";

    private static final String PLAYER_NAME_DOUBLE = "PLAYER_NAME_DOUBLE";

    @Before
    public void setUp() throws Exception {
        service = new PlayerImportService(playerService, playerOverwriter);
        doReturn(player).when(playerService).retrieve(PLAYER_NAME);
        doThrow(mock(MultipleInstanceFoundException.class)).when(playerService).retrieve(PLAYER_NAME_DOUBLE);
        doThrow(mock(EntityNotFoundException.class)).when(playerService).retrieve(PLAYER_NAME_NON_EXISTING);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerImportServiceNullServiceArgument() {
        new PlayerImportService(null, playerOverwriter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlayerImportServiceNullOverwriterArgument() {
        new PlayerImportService(playerService, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImportPlayerNullArgument() throws Exception {
        service.importPlayer(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImportPlayerNullNameArgument() throws Exception {
        service.importPlayer(new Player());
    }

    @Test
    public void testImportPlayerServiceExistingPlayer() throws Exception {
        Player importPlayer = new Player();
        importPlayer.setName(PLAYER_NAME);
        service.importPlayer(importPlayer);
        InOrder order = inOrder(playerService, playerOverwriter);
        order.verify(playerService).retrieve(PLAYER_NAME);
        order.verify(playerOverwriter).overwrite(player, importPlayer);
        order.verify(playerService).store(importPlayer);
    }

    @Test
    public void testImportPlayerServiceNonExistingPlayer() throws Exception {
        Player importPlayer = new Player();
        importPlayer.setName(PLAYER_NAME_NON_EXISTING);
        service.importPlayer(importPlayer);
        InOrder order = inOrder(playerService);
        order.verify(playerService).retrieve(PLAYER_NAME_NON_EXISTING);
        order.verify(playerService).store(importPlayer);
    }

    @Test(expected = ProcessingException.class)
    public void testImportPlayerServiceDoubleExistingPlayer() throws Exception {
        Player importPlayer = new Player();
        importPlayer.setName(PLAYER_NAME_DOUBLE);
        service.importPlayer(importPlayer);
    }
}
