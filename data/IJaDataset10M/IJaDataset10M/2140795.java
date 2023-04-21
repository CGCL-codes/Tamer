package com.syrus.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.logging.LogManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;

public class TeamTest {

    private Team team;

    @BeforeClass
    public static void setUpClass() {
        InputStream is = TeamTest.class.getResourceAsStream("/logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
        }
    }

    @Before
    public void setUp() {
        team = new Team();
    }

    @Test
    public void testSetVelocity() throws Exception {
        assumeThat(team.getVelocity(), is(0.7f));
        team.setVelocity(0.8f);
        Field field = Team.class.getDeclaredField("velocity");
        field.setAccessible(true);
        Float velocity = (Float) field.get(team);
        assertThat(velocity, is(not(0.7f)));
        assertThat(velocity, is(0.8f));
    }

    @Test
    public void testSetOutOfBoundsVelocity() {
        try {
            team.setVelocity(-0.1f);
        } catch (IllegalArgumentException e) {
        }
        try {
            team.setVelocity(1.1f);
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testGetVelocity() {
        assertThat(team.getVelocity(), is(0.7f));
    }

    @Test
    public void testAddDeveloper() throws Exception {
        team.addDeveloper("BJD");
        Field field = Team.class.getDeclaredField("developers");
        field.setAccessible(true);
        Collection developers = (Collection) field.get(team);
        assertThat(developers.isEmpty(), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullDeveloper() {
        team.addDeveloper(null);
    }

    @Test
    public void testGetSize() {
        assumeThat(team.getSize(), is(0));
        team.addDeveloper("BJD");
        assertThat(team.getSize(), is(1));
    }

    @Test
    public void tesGetDevelopers() {
        assumeThat(team.getDevelopers().isEmpty(), is(true));
        team.addDeveloper("BJD");
        assertThat(team.getDevelopers().isEmpty(), is(false));
        assertThat(team.getDevelopers().contains("BJD"), is(true));
    }

    @Test
    public void testEquals() {
        assertThat(team.equals(team), is(true));
        assertThat(team.equals(new Object()), is(false));
        assertThat(team.equals(new Team()), is(true));
        assertThat(team.equals(new Team(0.8f)), is(false));
        Team t = new Team();
        t.addDeveloper("BJD");
        assertThat(team.equals(t), is(false));
        team.addDeveloper("BJD");
        assertThat(team.equals(t), is(true));
    }

    @Test
    public void testHashCode() {
        int hashCode = team.hashCode();
        assertThat(hashCode, is(new Team().hashCode()));
        assertThat(hashCode, is(not(new Team(0.8f).hashCode())));
        team.addDeveloper("BJD");
        assertThat(team.hashCode(), is(not(hashCode)));
    }

    @Test
    public void testSerialize() throws Exception {
        team.addDeveloper("BJD");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(team);
        oos.flush();
        oos.close();
        byte[] data = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Team deserialized = (Team) ois.readObject();
        ois.close();
        assertThat(team.equals(deserialized), is(true));
    }
}
