package mud.bootstrap;

import mud.core.*;

/**
 * User: Michael Keller
 * Date: Jan 1, 2005
 * Copyright by Continuum.
 * Do not use for nuclear reactors and time machines.
 */
public class Start {

    public static object create(object env) {
        object start = new object("start", env, Permission.admin, Permission.wizard, Permission.admin, Permission.wizard);
        start.addBuiltinMethod("onEnter", Permission.player, Permission.wizard, Permission.wizard, Permission.disallowed, Start.class, object.class, object.class);
        return start;
    }

    /**
	 *
	 * @param self the start room
	 * @param item the entering object
	 */
    public static final void onEnter(object self, object item) {
        User user = item.getUser();
        if (user != null) {
            user.tell(text);
        }
    }

    private static final String text = "\n" + "Not bad so far. You are in the place where new players are\n" + "placed by default. Now you have to start coding the MUD. A good\n" + "thing would be to implement a few basic commands such as 'look'.\n" + "\n" + "Also you might want to introduce a concept like 'room' with exits\n" + "like 'north' or 'west'. Btw this place is *not* a room. It is\n" + "merely an object that displays this message to animate objects\n" + "entering its inventory.\n" + "\n" + "I wish you good luck.\n" + "Presently you cannot do anything.\n";
}
