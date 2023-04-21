package com.googlecode.progobots.ui.text;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import com.googlecode.progobots.Location;
import com.googlecode.progobots.Progobot;
import com.googlecode.progobots.World;

public class WorldEditCommandTest {

    private PrintfConsole console;

    private World world;

    private WorldEditCommand command;

    @Before
    public void setUp() throws Exception {
        console = spy(new PrintfConsole());
        world = new World();
        command = new WorldEditCommand("edit-world", "edit-world desc", console, world);
    }

    @Test
    public void testRunQuit() {
        when(console.readKey()).thenReturn((int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n", console.getPrintfString());
    }

    @Test
    public void testRunRightLeft() {
        when(console.readKey()).thenReturn((int) 'l', (int) 'l', (int) 'j', (int) 'j', (int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n" + world.getDescription(new Location(2, 1)) + "\n" + world.getDescription(new Location(3, 1)) + "\n" + world.getDescription(new Location(2, 1)) + "\n" + world.getDescription(new Location(1, 1)) + "\n", console.getPrintfString());
    }

    @Test
    public void testRunUpDown() {
        when(console.readKey()).thenReturn((int) 'i', (int) 'i', (int) 'k', (int) 'k', (int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n" + world.getDescription(new Location(1, 2)) + "\n" + world.getDescription(new Location(1, 3)) + "\n" + world.getDescription(new Location(1, 2)) + "\n" + world.getDescription(new Location(1, 1)) + "\n", console.getPrintfString());
    }

    @Test
    public void testRunBeeper() {
        when(console.readKey()).thenReturn((int) 'b', (int) 'b', (int) 'B', (int) 'B', (int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n" + "Bipador deixado\n" + "Bipador deixado\n" + "Bipador pego\n" + "Bipador pego\n", console.getPrintfString());
    }

    @Test
    public void testRunTurnProgobot() {
        world.add(new Progobot(world));
        when(console.readKey()).thenReturn((int) ' ', (int) ' ', (int) ' ', (int) ' ', (int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n" + "Robô virou para o North\n" + "Robô virou para o West\n" + "Robô virou para o South\n" + "Robô virou para o East\n", console.getPrintfString());
    }

    @Test
    public void testRunMoveProgobot() {
        world.add(new Progobot(world));
        when(console.readKey()).thenReturn((int) 'l', (int) ' ', (int) 'j', (int) ' ', (int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n" + world.getDescription(new Location(2, 1)) + "\n" + "Robô posicionado em 2 1\n" + "Cruzamento 1 1: paredes South West.\n" + "Robô posicionado em 1 1\n", console.getPrintfString());
    }

    @Test
    public void testRunSelectWorld() {
        world.add(new Progobot(world));
        when(console.readKey()).thenReturn((int) '\t', (int) '\t', (int) 'q');
        command.run("edit-world");
        assertEquals(world.getDescription(new Location(1, 1)) + "\n" + "Estado final\n" + world.getExpectedState().getDescription(new Location(1, 1)) + "\n" + "Estado inicial\n" + world.getDescription(new Location(1, 1)) + "\n", console.getPrintfString());
    }
}
